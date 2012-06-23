/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */
package com.seaglasslookandfeel.util;

import java.awt.GraphicsConfiguration;
import java.awt.Image;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ImageCache - A fixed pixel count sized cache of Images keyed by arbitrary set
 * of arguments. All images are held with SoftReferences so they will be dropped
 * by the GC if heap memory gets tight. When our size hits max pixel count least
 * recently requested images are removed first.
 *
 * <p>Based on Nimbus's ImageCache by Jasper Potts. This was package local.</p>
 *
 */
public class ImageCache {

    // Singleton Instance
    private static final ImageCache instance = new ImageCache();

    // Ordered Map keyed by args hash, ordered by most recent accessed entry.
    private final LinkedHashMap<Integer, PixelCountSoftReference> map = new LinkedHashMap<Integer, PixelCountSoftReference>(16, 0.75f,
                                                                                                                            true);

    // Maximum number of pixels to cache, this is used if maxCount
    private final int maxPixelCount;

    // Maximum cached image size in pxiels
    private final int maxSingleImagePixelSize;

    // The current number of pixels stored in the cache
    private int currentPixelCount = 0;

    // Lock for concurrent access to map
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // Reference queue for tracking lost softreferences to images in the cache
    private ReferenceQueue<Image> referenceQueue = new ReferenceQueue<Image>();

    /**
     * Creates a new ImageCache object.
     */
    public ImageCache() {
        this.maxPixelCount           = (8 * 1024 * 1024) / 4; // 8Mb of pixels
        this.maxSingleImagePixelSize = 300 * 300;
    }

    /**
     * Creates a new ImageCache object.
     *
     * @param maxPixelCount           the maximum pixel count.
     * @param maxSingleImagePixelSize the maximum single image pixel size.
     */
    public ImageCache(int maxPixelCount, int maxSingleImagePixelSize) {
        this.maxPixelCount           = maxPixelCount;
        this.maxSingleImagePixelSize = maxSingleImagePixelSize;
    }

    /**
     * Get static singleton instance.
     *
     * @return the singleton image cache.
     */
    public static ImageCache getInstance() {
        return instance;
    }

    /**
     * Clear the cache.
     */
    public void flush() {
        lock.readLock().lock();

        try {
            map.clear();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Check if the image size is to big to be stored in the cache.
     *
     * @param  w The image width
     * @param  h The image height
     *
     * @return True if the image size is less than max
     */
    public boolean isImageCachable(int w, int h) {
        return (w * h) < maxSingleImagePixelSize;
    }

    /**
     * Get the cached image for given keys
     *
     * @param  config The graphics configuration, needed if cached image is a
     *                Volatile Image. Used as part of cache key
     * @param  w      The image width, used as part of cache key
     * @param  h      The image height, used as part of cache key
     * @param  args   Other arguments to use as part of the cache key
     *
     * @return Returns the cached Image, or null there is no cached image for
     *         key
     */
    public Image getImage(GraphicsConfiguration config, int w, int h, Object... args) {
        lock.readLock().lock();

        try {
            PixelCountSoftReference ref = map.get(hash(config, w, h, args));

            // check reference has not been lost and the key truly matches, in
            // case of false positive hash match
            if (ref != null && ref.equals(config, w, h, args)) {
                return ref.get();
            } else {
                return null;
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the cached image for the specified constraints.
     *
     * @param  image  The image to store in cache
     * @param  config The graphics configuration, needed if cached image is a
     *                Volatile Image. Used as part of cache key
     * @param  w      The image width, used as part of cache key
     * @param  h      The image height, used as part of cache key
     * @param  args   Other arguments to use as part of the cache key
     *
     * @return true if the image could be cached or false if the image is too
     *         big
     */
    public boolean setImage(Image image, GraphicsConfiguration config, int w, int h, Object... args) {
        if (!isImageCachable(w, h))
            return false;

        int hash = hash(config, w, h, args);

        lock.writeLock().lock();

        try {
            PixelCountSoftReference ref = map.get(hash);

            // check if currently in map
            if (ref != null && ref.get() == image) {
                return true;
            }

            // clear out old
            if (ref != null) {
                currentPixelCount -= ref.pixelCount;
                map.remove(hash);
            }

            // add new image to pixel count
            int newPixelCount = image.getWidth(null) * image.getHeight(null);

            currentPixelCount += newPixelCount;

            // clean out lost references if not enough space
            if (currentPixelCount > maxPixelCount) {

                while ((ref = (PixelCountSoftReference) referenceQueue.poll()) != null) {

                    // reference lost
                    map.remove(ref.hash);
                    currentPixelCount -= ref.pixelCount;
                }
            }

            // remove old items till there is enough free space
            if (currentPixelCount > maxPixelCount) {
                Iterator<Map.Entry<Integer, PixelCountSoftReference>> mapIter = map.entrySet().iterator();

                while ((currentPixelCount > maxPixelCount) && mapIter.hasNext()) {
                    Map.Entry<Integer, PixelCountSoftReference> entry = mapIter.next();

                    mapIter.remove();
                    Image img = entry.getValue().get();

                    if (img != null)
                        img.flush();

                    currentPixelCount -= entry.getValue().pixelCount;
                }
            }

            // finaly put new in map
            map.put(hash, new PixelCountSoftReference(image, referenceQueue, newPixelCount, hash, config, w, h, args));

            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Create a unique hash from all the input.
     *
     * @param  config the graphics configuration.
     * @param  w      the width.
     * @param  h      the height.
     * @param  args   the arguments for the image.
     *
     * @return the hash for the object instance.
     */
    private int hash(GraphicsConfiguration config, int w, int h, Object... args) {
        int hash;

        hash = (config != null ? config.hashCode() : 0);
        hash = 31 * hash + w;
        hash = 31 * hash + h;
        hash = 31 * hash + Arrays.deepHashCode(args);

        return hash;
    }

    /**
     * Extended SoftReference that stores the pixel count even after the image
     * is lost.
     */
    private static class PixelCountSoftReference extends SoftReference<Image> {
        private final int pixelCount;
        private final int hash;

        // key parts
        private final GraphicsConfiguration config;
        private final int                   w;
        private final int                   h;
        private final Object[]              args;

        /**
         * Creates a new PixelCountSoftReference object.
         *
         * @param referent   the image.
         * @param q          the reference queue.
         * @param pixelCount the pixel count.
         * @param hash       the hash.
         * @param config     the graphics configuration.
         * @param w          the width.
         * @param h          the height.
         * @param args       the arguments for the image.
         */
        public PixelCountSoftReference(Image referent, ReferenceQueue<? super Image> q, int pixelCount, int hash,
                GraphicsConfiguration config, int w, int h, Object[] args) {
            super(referent, q);
            this.pixelCount = pixelCount;
            this.hash       = hash;
            this.config     = config;
            this.w          = w;
            this.h          = h;
            this.args       = args;
        }

        /**
         * Tests for equality.
         *
         * @param  config the graphics configuration.
         * @param  w      the width.
         * @param  h      the height.
         * @param  args   the arguments for the image.
         *
         * @return {@code true} if the arguments describe the same object as
         *         this one, {@code false} otherwise.
         */
        public boolean equals(GraphicsConfiguration config, int w, int h, Object[] args) {
            return config == this.config && w == this.w && h == this.h && Arrays.equals(args, this.args);
        }
    }
}
