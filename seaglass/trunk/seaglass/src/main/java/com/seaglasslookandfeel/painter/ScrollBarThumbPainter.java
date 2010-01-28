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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_PRESSED,
    }

    private ButtonStateColors disabledColors = new ButtonStateColors(new Color(0x80fbfdfe, true), new Color(0x80d6eaf9, true), new Color(
                                                 0x80d2e8f8, true), new Color(0x80f5fafd, true), 0.46f, 0.62f, new Color(0x6088ade0, true),
                                                 new Color(0x605785bf, true));
    private ButtonStateColors enabledColors  = new ButtonStateColors(new Color(0xfbfdfe), new Color(0xd6eaf9), new Color(0xd2e8f8),
                                                 new Color(0xf5fafd), 0.46f, 0.62f, new Color(0x88ade0), new Color(0x5785bf));
    private ButtonStateColors pressedColors  = new ButtonStateColors(new Color(0xb1dbf5), new Color(0x7ca7ce), new Color(0x7ea7cc),
                                                 new Color(0xbbcedf), 0.4f, 0.7f, new Color(0x4076bf), new Color(0x4f7bbf));

    private Which             state;
    private PaintContext      ctx;

    public ScrollBarThumbPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

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
        g.setPaint(decodeGradientBackground(s, colors.background1, colors.background2));
        g.fill(s);

        s = decodeInterior(width, height);
        g.setPaint(decodeGradientInner(s, colors.inner1, colors.inner2, colors.inner3, colors.inner4, colors.midpoint2, colors.midpoint3));
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
        return ShapeUtil.createQuad(x, y, width, height, height / 2.0, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED);
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
     * @param color3
     *            TODO
     * @param color4
     *            TODO
     * @param midpoint2
     * @param midpoint3
     *            TODO
     */
    private Paint decodeGradientInner(Shape s, Color color1, Color color2, Color color3, Color color4, float midpoint2, float midpoint3) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint2, midpoint3, 1f }, new Color[] {
            color1,
            color2,
            color3,
            color4 });
    }

    /**
     * A set of colors to use for the button.
     */
    public class ButtonStateColors {

        public Color inner1;
        public Color inner2;
        public Color inner3;
        public Color inner4;
        public float midpoint2;
        public float midpoint3;
        public Color background1;
        public Color background2;

        public ButtonStateColors(Color inner1, Color inner2, Color inner3, Color inner4, float midpoint2, float midpoint3,
            Color background1, Color background2) {
            this.inner1 = inner1;
            this.inner2 = inner2;
            this.inner3 = inner3;
            this.inner4 = inner4;
            this.midpoint2 = midpoint2;
            this.midpoint3 = midpoint3;
            this.background1 = background1;
            this.background2 = background2;
        }
    }
}
