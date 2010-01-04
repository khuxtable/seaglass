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
package com.seaglasslookandfeel.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * SliderThumbPainter implementation.
 */
public final class SliderThumbPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_FOCUSED_MOUSEOVER,
        BACKGROUND_FOCUSED_PRESSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_ARROWSHAPE,
        BACKGROUND_DISABLED_ARROWSHAPE,
        BACKGROUND_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_PRESSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE,
    }

    private static final ColorSet  disabled    = new ColorSet(new Color(0x8088ade0, true), new Color(0x805785bf, true), new Color(
                                                   0x80fbfdfe, true), new Color(0x80d6eaf9, true), new Color(0x80d2e8f8, true), new Color(
                                                   0x80f5fafd, true));
    private static final ColorSet  enabled     = new ColorSet(new Color(0x88ade0), new Color(0x5785bf), new Color(0xfbfdfe), new Color(
                                                   0xd6eaf9), new Color(0xd2e8f8), new Color(0xf5fafd));
    private static final ColorSet  pressed     = new ColorSet(new Color(0x4f7bbf), new Color(0x3f76bf), new Color(0xacbdd0), new Color(
                                                   0x688db3), new Color(0x6d93ba), new Color(0xa4cbe4));

    private static final Color     shadowColor = Color.black;
    private static final Effect    dropShadow  = new SeaGlassDropShadowEffect();

    private static final Insets    insets      = new Insets(0, 0, 0, 0);
    private static final Dimension dimension   = new Dimension(15, 20);
    private static final CacheMode cacheMode   = CacheMode.FIXED_SIZES;
    private static final Double    maxH        = 1.0;
    private static final Double    maxV        = 1.0;

    private Ellipse2D              ellipse     = new Ellipse2D.Double();
    private Path2D                 path        = new Path2D.Double();

    private Which                  state;
    private PaintContext           ctx;

    public SliderThumbPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintContinuousDisabled(g, c, width, height, false);
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_MOUSEOVER:
            paintContinuousEnabled(g, c, width, height, false);
            break;
        case BACKGROUND_FOCUSED:
        case BACKGROUND_FOCUSED_MOUSEOVER:
            paintContinuousEnabled(g, c, width, height, true);
            break;
        case BACKGROUND_PRESSED:
            paintContinuousPressed(g, c, width, height, false);
            break;
        case BACKGROUND_FOCUSED_PRESSED:
            paintContinuousPressed(g, c, width, height, true);
            break;
        case BACKGROUND_DISABLED_ARROWSHAPE:
            paintDiscreteDisabled(g, c, width, height, false);
            break;
        case BACKGROUND_ENABLED_ARROWSHAPE:
        case BACKGROUND_MOUSEOVER_ARROWSHAPE:
            paintDiscreteEnabled(g, c, width, height, false);
            break;
        case BACKGROUND_FOCUSED_ARROWSHAPE:
        case BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE:
            paintDiscreteEnabled(g, c, width, height, true);
            break;
        case BACKGROUND_PRESSED_ARROWSHAPE:
            paintDiscretePressed(g, c, width, height, false);
            break;
        case BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE:
            paintDiscretePressed(g, c, width, height, true);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintContinuousDisabled(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintContinuous(g, c, width, height, focused, disabled);
    }

    private void paintContinuousEnabled(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintContinuous(g, c, width, height, focused, enabled);
    }

    private void paintContinuousPressed(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintContinuous(g, c, width, height, focused, pressed);
    }

    private void paintDiscreteDisabled(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintDiscrete(g, c, width, height, focused, disabled);
    }

    private void paintDiscreteEnabled(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintDiscrete(g, c, width, height, focused, enabled);
    }

    private void paintDiscretePressed(Graphics2D g, JComponent c, int width, int height, boolean focused) {
        paintDiscrete(g, c, width, height, focused, pressed);
    }

    private void paintDiscrete(Graphics2D g, JComponent c, int width, int height, boolean focused, ColorSet colors) {
        Shape s = decodeDiscreteBorder(width, height);
        if (!focused) {
            g.drawImage(createDropShadowImage(s), 0, 0, null);
        }
        g.setPaint(decodeBorderGradient(s, colors.border1, colors.border2));
        g.fill(s);
        s = decodeDiscreteInterior(width, height);
        g.setPaint(decodeInteriorGradient(s, colors.interior1, colors.interior2, colors.interior3, colors.interior4));
        g.fill(s);
    }

    private void paintContinuous(Graphics2D g, JComponent c, int width, int height, boolean focused, ColorSet colors) {
        Shape s = decodeContinuousBorder(width, height);
        if (!focused) {
            g.drawImage(createDropShadowImage(s), 0, 0, null);
        }
        g.setPaint(decodeBorderGradient(s, colors.border1, colors.border2));
        g.fill(s);
        s = decodeContinuousInterior(width, height);
        g.setPaint(decodeInteriorGradient(s, colors.interior1, colors.interior2, colors.interior3, colors.interior4));
        g.fill(s);
    }

    /**
     * Create a drop shadow image.
     * 
     * @param s
     *            the shape to use as the shade.
     */
    private BufferedImage createDropShadowImage(Shape s) {
        BufferedImage bimage = DropShadowEffect.createBufferedImage(dimension.width, dimension.height, true);
        Graphics2D gbi = bimage.createGraphics();
        gbi.setColor(shadowColor);
        gbi.fill(s);
        return dropShadow.applyEffect(bimage, null, dimension.width, dimension.height);
    }

    private Paint decodeInteriorGradient(Shape s, Color color1, Color color2, Color color3, Color color4) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        return decodeGradient(x, r.y, x, r.y + r.height, new float[] { 0f, 0.42f, .6f, 1f }, new Color[] { color1, color2, color3, color4 });
    }

    private Paint decodeBorderGradient(Shape s, Color color1, Color color2) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        return decodeGradient(x, r.y, x, r.y + r.height, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    private Shape decodeContinuousBorder(int width, int height) {
        ellipse.setFrame(1, 3, 13, 13);
        return ellipse;
    }

    private Shape decodeContinuousInterior(int width, int height) {
        ellipse.setFrame(2, 4, 11, 11);
        return ellipse;
    }

    private Shape decodeDiscreteBorder(int width, int height) {
        path.reset();
        path.moveTo(1, 5);
        path.quadTo(1, 2, 4, 2);
        path.lineTo(width - 3, 2);
        path.quadTo(width - 1, 2, width - 1, 5);
        path.lineTo(width - 1, height / 2);
        path.quadTo(width - 4, height - 3, width / 2, height - 2);
        path.quadTo(4, height - 3, 1, height / 2);
        path.closePath();
        return path;
    }

    private Shape decodeDiscreteInterior(int width, int height) {
        path.reset();
        path.moveTo(2, 5);
        path.quadTo(2, 3, 4, 3);
        path.lineTo(width - 4, 3);
        path.quadTo(width - 2, 3, width - 2, 5);
        path.lineTo(width - 2, height / 2);
        path.quadTo(width - 5, height - 3, width / 2, height - 3);
        path.quadTo(5, height - 3, 2, height / 2);
        path.closePath();
        return path;
    }

    private static class ColorSet {
        public Color border1;
        public Color border2;
        public Color interior1;
        public Color interior2;
        public Color interior3;
        public Color interior4;

        public ColorSet(Color border1, Color border2, Color interior1, Color interior2, Color interior3, Color interior4) {
            this.border1 = border1;
            this.border2 = border2;
            this.interior1 = interior1;
            this.interior2 = interior2;
            this.interior3 = interior3;
            this.interior4 = interior4;
        }
    }
}
