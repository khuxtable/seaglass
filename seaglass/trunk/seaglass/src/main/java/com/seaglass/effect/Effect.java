/*
 * @(#)Effect.java	1.2 07/12/12
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass.effect;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.lang.ref.SoftReference;

import javax.swing.ImageIcon;

import sun.awt.AppContext;

/**
 * Effect
 * 
 * Copied from Nimbus's Effect by Jasper Potts. This was package local.
 */
public abstract class Effect {
    public enum EffectType {
        UNDER, BLENDED, OVER
    }

    // =================================================================================================================
    // Abstract Methods

    /**
     * Get the type of this effect, one of UNDER,BLENDED,OVER. UNDER means the
     * result of apply effect should be painted under the src image. BLENDED
     * means the result of apply sffect contains a modified src image so just it
     * should be painted. OVER means the result of apply effect should be
     * painted over the src image.
     * 
     * @return The effect type
     */
    abstract EffectType getEffectType();

    /**
     * Get the opacity to use to paint the result effected image if the
     * EffectType is UNDER or OVER.
     * 
     * @return The opactity for the effect, 0.0f -> 1.0f
     */
    abstract float getOpacity();

    /**
     * Apply the effect to the src image generating the result . The result
     * image may or may not contain the source image depending on what the
     * effect type is.
     * 
     * @param src
     *            The source image for applying the effect to
     * @param dst
     *            The dstination image to paint effect result into. If this is
     *            null then a new image will be created
     * @param w
     *            The width of the src image to apply effect to, this allow the
     *            src and dst buffers to be bigger than the area the need effect
     *            applied to it
     * @param h
     *            The height of the src image to apply effect to, this allow the
     *            src and dst buffers to be bigger than the area the need effect
     *            applied to it
     * @return The result of appl
     */
    public abstract BufferedImage applyEffect(BufferedImage src, BufferedImage dst, int w, int h);

    // =================================================================================================================
    // Static data cache

    protected static ArrayCache getArrayCache() {
        ArrayCache cache = (ArrayCache) AppContext.getAppContext().get(ArrayCache.class);
        if (cache == null) {
            cache = new ArrayCache();
            AppContext.getAppContext().put(ArrayCache.class, cache);
        }
        return cache;
    }

    protected static class ArrayCache {
        private SoftReference<int[]>  tmpIntArray   = null;
        private SoftReference<byte[]> tmpByteArray1 = null;
        private SoftReference<byte[]> tmpByteArray2 = null;
        private SoftReference<byte[]> tmpByteArray3 = null;

        protected int[] getTmpIntArray(int size) {
            int[] tmp;
            if (tmpIntArray == null || (tmp = tmpIntArray.get()) == null || tmp.length < size) {
                // create new array
                tmp = new int[size];
                tmpIntArray = new SoftReference<int[]>(tmp);
            }
            return tmp;
        }

        protected byte[] getTmpByteArray1(int size) {
            byte[] tmp;
            if (tmpByteArray1 == null || (tmp = tmpByteArray1.get()) == null || tmp.length < size) {
                // create new array
                tmp = new byte[size];
                tmpByteArray1 = new SoftReference<byte[]>(tmp);
            }
            return tmp;
        }

        protected byte[] getTmpByteArray2(int size) {
            byte[] tmp;
            if (tmpByteArray2 == null || (tmp = tmpByteArray2.get()) == null || tmp.length < size) {
                // create new array
                tmp = new byte[size];
                tmpByteArray2 = new SoftReference<byte[]>(tmp);
            }
            return tmp;
        }

        protected byte[] getTmpByteArray3(int size) {
            byte[] tmp;
            if (tmpByteArray3 == null || (tmp = tmpByteArray3.get()) == null || tmp.length < size) {
                // create new array
                tmp = new byte[size];
                tmpByteArray3 = new SoftReference<byte[]>(tmp);
            }
            return tmp;
        }
    }

    public static BufferedImage createBufferedImage(int width, int height, boolean hasAlpha) {
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(width, height, transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(width, height, type);
        }

        return bimage;
    }

    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent
        // Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the
        // screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    // This method returns true if the specified image has transparent pixels
    private static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
}
