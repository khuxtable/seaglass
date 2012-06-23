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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_PRESSED,
    }

    private static final Insets    insets         = new Insets(0, 8, 0, 8);
    private static final Dimension dimension      = new Dimension(82, 14);
    private static final CacheMode cacheMode      = CacheMode.FIXED_SIZES;
    private static final Double    maxH           = Double.POSITIVE_INFINITY;
    private static final Double    maxV           = 1.0;

    private ButtonStateColors      disabledColors = new ButtonStateColors(new Color(0xd8f4f8fb, true), new Color(0x00ffffff, true),
                                                      new Color(0x00a8d9fc, true), new Color(0xf7fcff), 0.45f, new Color(0x80a8d2f2, true),
                                                      new Color(0x80a2c2ed, true), new Color(0x805785bf, true));
    private ButtonStateColors      enabledColors  = new ButtonStateColors(new Color(0xd8f4f8fb, true), new Color(0x00ffffff, true),
                                                      new Color(0x00a8d9fc, true), new Color(0xf7fcff), 0.45f, new Color(0xa8d2f2),
                                                      new Color(0xa2c2ed), new Color(0x5785bf));
    private ButtonStateColors      pressedColors  = new ButtonStateColors(new Color(0xbfeeeeee, true), new Color(0x00eeeeee, true),
                                                      new Color(0x00a8d9fc, true), new Color(0xc0e8ff), 0.45f, new Color(0x276fb2),
                                                      new Color(0x4f7bbf), new Color(0x3f76bf));

    private RoundRectangle2D       rect           = new RoundRectangle2D.Double();

    private Which                  state;
    private PaintContext           ctx;

    public ScrollBarThumbPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);

    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintDisabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_PRESSED:
            paintPressed(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintThumb(g, c, width, height, disabledColors);
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintThumb(g, c, width, height, enabledColors);
    }

    private void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paintThumb(g, c, width, height, pressedColors);
    }

    private void paintThumb(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = decodeBorder(width, height);
        g.setPaint(decodeGradientBackground(s, colors.backgroundTop, colors.backgroundBottom));
        g.fill(s);
        s = decodeInterior(width, height);
        g.setColor(colors.mainColor);
        g.fill(s);
        g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(s);
        g.setPaint(decodeGradientTopShine(s, colors.upperShineTop, colors.upperShineBottom));
        g.fill(s);
    }

    private Shape decodeBorder(int width, int height) {
        int x = 0;
        int y = 0;
        return decodePath(x, y, width, height);
    }

    private Shape decodeInterior(int width, int height) {
        int x = 1;
        int y = 1;
        width -= 2;
        height -= 2;
        return decodePath(x, y, width, height);
    }

    private Shape decodePath(int x, int y, int width, int height) {
        double arc = height;
        rect.setRoundRect(x, y, width, height, arc, arc);
        return rect;
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the button.
     */
    public class ButtonStateColors {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public ButtonStateColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
            float lowerShineMidpoint, Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShineTop = upperShineTop;
            this.upperShineBottom = upperShineBottom;
            this.lowerShineTop = lowerShineTop;
            this.lowerShineBottom = lowerShineBottom;
            this.lowerShineMidpoint = lowerShineMidpoint;
            this.mainColor = mainColor;
            this.backgroundTop = backgroundTop;
            this.backgroundBottom = backgroundBottom;
        }
    }
}
