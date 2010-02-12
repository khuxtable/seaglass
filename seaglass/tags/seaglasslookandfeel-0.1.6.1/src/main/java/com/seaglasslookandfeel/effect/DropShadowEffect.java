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
package com.seaglasslookandfeel.effect;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import java.util.Arrays;

/**
 * DropShadowEffect - This effect currently only works with ARGB type buffered
 * images.
 *
 * <p>Copied from Nimbus's DropShadowEffect by Jasper Potts. This was package
 * local.</p>
 */
public class DropShadowEffect extends ShadowEffect {

    // =================================================================================================================
    // Effect Methods

    /**
     * Get the type of this effect, one of UNDER,BLENDED,OVER. UNDER means the
     * result of apply effect should be painted under the src image. BLENDED
     * means the result of apply sffect contains a modified src image so just it
     * should be painted. OVER means the result of apply effect should be
     * painted over the src image.
     *
     * @return The effect type
     */
    @Override
    public EffectType getEffectType() {
        return EffectType.UNDER;
    }

    /**
     * Apply the effect to the src image generating the result . The result
     * image may or may not contain the source image depending on what the
     * effect type is.
     *
     * @param  src The source image for applying the effect to
     * @param  dst The destination image to paint effect result into. If this is
     *             null then a new image will be created
     * @param  w   The width of the src image to apply effect to, this allow the
     *             src and dst buffers to be bigger than the area the need
     *             effect applied to it
     * @param  h   The height of the src image to apply effect to, this allow
     *             the src and dst buffers to be bigger than the area the need
     *             effect applied to it
     *
     * @return Image with the result of the effect
     */
    @Override
    public BufferedImage applyEffect(BufferedImage src, BufferedImage dst, int w, int h) {
        if (src == null || (src.getType() != BufferedImage.TYPE_INT_ARGB && src.getType() != BufferedImage.TYPE_INT_ARGB_PRE)) {
            throw new IllegalArgumentException("Effect only works with source images of type BufferedImage.TYPE_INT_ARGB. Type is "
                                               + src.getType());
        }

        if (dst != null && dst.getType() != BufferedImage.TYPE_INT_ARGB) {
            throw new IllegalArgumentException("Effect only works with destination images of type BufferedImage.TYPE_INT_ARGB.");
        }

        // calculate offset
        double trangleAngle = Math.toRadians(angle - 90);
        int    offsetX      = (int) (Math.sin(trangleAngle) * distance + 0.5);
        int    offsetY      = (int) (Math.cos(trangleAngle) * distance + 0.5);

        // calculate expanded size
        int tmpOffX = offsetX + size;
        int tmpOffY = offsetX + size;
        int tmpW    = w + offsetX + size + size;
        int tmpH    = h + offsetX + size;

        // create tmp buffers
        int[]  lineBuf = getArrayCache().getTmpIntArray(w);
        byte[] tmpBuf1 = getArrayCache().getTmpByteArray1(tmpW * tmpH);

        Arrays.fill(tmpBuf1, (byte) 0x00);
        byte[] tmpBuf2 = getArrayCache().getTmpByteArray2(tmpW * tmpH);

        // extract src image alpha channel and inverse and offset
        Raster srcRaster = src.getRaster();

        for (int y = 0; y < h; y++) {
            int dy     = (y + tmpOffY);
            int offset = dy * tmpW;

            srcRaster.getDataElements(0, y, w, 1, lineBuf);

            for (int x = 0; x < w; x++) {
                int dx = x + tmpOffX;

                tmpBuf1[offset + dx] = (byte) ((lineBuf[x] & 0xFF000000) >>> 24);
            }
        }

        // blur
        float[] kernel = EffectUtils.createGaussianKernel(size);

        EffectUtils.blur(tmpBuf1, tmpBuf2, tmpW, tmpH, kernel, size); // horizontal

        // pass
        EffectUtils.blur(tmpBuf2, tmpBuf1, tmpH, tmpW, kernel, size); // vertical

        // pass
        // rescale
        float spread = Math.min(1 / (1 - (0.01f * this.spread)), 255);

        for (int i = 0; i < tmpBuf1.length; i++) {
            int val = (int) (((int) tmpBuf1[i] & 0xFF) * spread);

            tmpBuf1[i] = (val > 255) ? (byte) 0xFF : (byte) val;
        }

        // create color image with shadow color and greyscale image as alpha
        if (dst == null)
            dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        WritableRaster shadowRaster = dst.getRaster();
        int            red          = color.getRed();
        int            green        = color.getGreen();
        int            blue         = color.getBlue();

        for (int y = 0; y < h; y++) {
            int srcY         = y + tmpOffY;
            int shadowOffset = (srcY - offsetY) * tmpW;

            for (int x = 0; x < w; x++) {
                int srcX = x + tmpOffX;

                lineBuf[x] = tmpBuf1[shadowOffset + (srcX - offsetX)] << 24 | red << 16 | green << 8 | blue;
            }

            shadowRaster.setDataElements(0, y, w, 1, lineBuf);
        }

        return dst;
    }
}
