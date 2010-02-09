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

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_PRESSED,
    }

    private Color             scrollBarThumbBorderBasePressed       = decodeColor("scrollBarThumbBorderBasePressed");
    private Color             scrollBarThumbInteriorBasePressed     = decodeColor("scrollBarThumbInteriorBasePressed");

    private Color             scrollBarThumbBorderTopPressed        = deriveColor(scrollBarThumbBorderBasePressed, -0.002239f, 0.041885f,
                                                                        0f, 0);
    private Color             scrollBarThumbBorderBottomPressed     = deriveColor(scrollBarThumbBorderBasePressed, 0.003151f, -0.036649f,
                                                                        0f, 0);

    private TwoColors         scrollBarThumbBorderPressed           = new TwoColors(scrollBarThumbBorderTopPressed,
                                                                        scrollBarThumbBorderBottomPressed);

    private Color             scrollBarThumbInteriorTopPressed      = deriveColor(scrollBarThumbInteriorBasePressed, -0.014978f,
                                                                        -0.078885f, 0.168627f, 0);
    private Color             scrollBarThumbInteriorUpperMidPressed = deriveColor(scrollBarThumbInteriorBasePressed, 0.000565f, 0.041623f,
                                                                        0.015686f, 0);
    private Color             scrollBarThumbInteriorLowerMidPressed = deriveColor(scrollBarThumbInteriorBasePressed, 0.000356f, 0.025917f,
                                                                        0.007843f, 0);
    private Color             scrollBarThumbInteriorBottomPressed   = deriveColor(scrollBarThumbInteriorBasePressed, 0f, -0.195001f,
                                                                        0.082353f, 0);

    private FourColors        scrollBarThumbInteriorPressed         = new FourColors(scrollBarThumbInteriorTopPressed,
                                                                        scrollBarThumbInteriorUpperMidPressed,
                                                                        scrollBarThumbInteriorLowerMidPressed,
                                                                        scrollBarThumbInteriorBottomPressed);

    private PaintContext      ctx;
    private CommonControlState type;

    public ScrollBarThumbPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getButtonType(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = shapeGenerator.createRoundRectangle(0, 0, width, height, CornerSize.ROUND_HEIGHT);
        g.setPaint(getScrollBarThumbBorderPaint(s, type));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.ROUND_HEIGHT);
        g.setPaint(getScrollBarThumbInteriorPaint(s, type));
        g.fill(s);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlState getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return CommonControlState.DISABLED;
        case BACKGROUND_ENABLED:
            return CommonControlState.ENABLED;
        case BACKGROUND_PRESSED:
            return CommonControlState.PRESSED;
        }
        return null;
    }

    public Paint getScrollBarThumbBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getCommonBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getScrollBarThumbInteriorPaint(Shape s, CommonControlState type) {
        FourColors colors = getCommonInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    public TwoColors getCommonBorderColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
        case ENABLED:
            return super.getCommonBorderColors(type);
        case PRESSED:
            return scrollBarThumbBorderPressed;
        }
        return null;
    }

    public FourColors getCommonInteriorColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
        case ENABLED:
            return super.getCommonInteriorColors(type);
        case PRESSED:
            return scrollBarThumbInteriorPressed;
        }
        return null;
    }
}
