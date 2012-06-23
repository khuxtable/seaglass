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
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ScrollBarTrackPainter implementation.
 */
public final class ScrollBarTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED,
    }

    private Color        scrollBarTrackBackgroundBase   = decodeColor("scrollBarTrackBackgroundBase");
    private Color        scrollBarTrackGradientBase     = decodeColor("scrollBarTrackGradientBase");

    private Color        scrollBarTrackBackgroundTop    = deriveColor(scrollBarTrackBackgroundBase, 0f, 0f, -0.066667f, 0);
    private Color        scrollBarTrackBackgroundBottom = scrollBarTrackBackgroundBase;

    private Color        scrollBarTrackGradientTop      = deriveColor(scrollBarTrackGradientBase, 0f, 0f, 0f, 0x33);
    private Color        scrollBarTrackGradientUpperMid = deriveColor(scrollBarTrackGradientBase, 0f, 0f, 0f, 0x15);
    private Color        scrollBarTrackGradientLowerMid = scrollBarTrackGradientBase;
    private Color        scrollBarTrackGradientBottom   = deriveColor(scrollBarTrackGradientBase, 0f, 0f, 0f, 0x12);

    private TwoColors    scrollBarTrackBackground       = new TwoColors(scrollBarTrackBackgroundTop, scrollBarTrackBackgroundBottom);
    private FourColors   scrollBarTrackGradient         = new FourColors(scrollBarTrackGradientTop, scrollBarTrackGradientUpperMid,
                                                            scrollBarTrackGradientLowerMid, scrollBarTrackGradientBottom);

    private PaintContext ctx;

    public ScrollBarTrackPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRectangle(0, 0, width, height);
        g.setPaint(getScrollBarTrackBackgroundPaint(s));
        g.fill(s);
        g.setPaint(getScrollBarTrackShadowPaint(s));
        g.fill(s);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    public Paint getScrollBarTrackBackgroundPaint(Shape s) {
        return createVerticalGradient(s, scrollBarTrackBackground);
    }

    public Paint getScrollBarTrackShadowPaint(Shape s) {
        return createScrollBarTrackInnerShadowGradient(s, scrollBarTrackGradient);
    }

    private Paint createScrollBarTrackInnerShadowGradient(Shape s, FourColors colors) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return createGradient(width * 0.5f, 0, width * 0.5f, height - 1, new float[] { 0f, 0.142857143f, 0.5f, 0.785714286f, 1f },
            new Color[] { colors.top, colors.upperMid, colors.lowerMid, colors.lowerMid, colors.bottom });
    }
}
