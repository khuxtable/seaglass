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
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * SpinnerPreviousButtonPainter implementation.
 */
public final class SpinnerPreviousButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        FOREGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_FOCUSED,
        FOREGROUND_PRESSED_FOCUSED,
        FOREGROUND_PRESSED,
    }

    private static final Color     OUTER_FOCUS_COLOR        = new Color(0x8072a5d2, true);
    private static final Color     INNER_FOCUS_COLOR        = new Color(0x73a4d1);

    private static final Color     DISABLED_TOP_INTERIOR    = new Color(0xd8dbe4f0, true);
    private static final Color     DISABLED_BOTTOM_INTERIOR = new Color(0xdddae5f0, true);
    private static final Color     DISABLED_TOP_LINE        = new Color(0xe0e4ebf3, true);
    private static final Color     DISABLED_TOP_BORDER      = new Color(0x807aa1d4, true);
    private static final Color     DISABLED_BOTTOM_BORDER   = new Color(0x805987c0, true);

    private static final Color     ENABLED_TOP_INTERIOR     = new Color(0x81aed4);
    private static final Color     ENABLED_BOTTOM_INTERIOR  = new Color(0xaad4f1);
    private static final Color     ENABLED_TOP_LINE         = new Color(0xacc8e0);
    private static final Color     ENABLED_TOP_BORDER       = new Color(0x4778bf);
    private static final Color     ENABLED_BOTTOM_BORDER    = new Color(0x4076bf);

    private static final Color     PRESSED_TOP_INTERIOR     = new Color(0x6c91b8);
    private static final Color     PRESSED_BOTTOM_INTERIOR  = new Color(0x9cc3de);
    private static final Color     PRESSED_TOP_LINE         = new Color(0x9eb6cf);
    private static final Color     PRESSED_TOP_BORDER       = new Color(0x4778bf);
    private static final Color     PRESSED_BOTTOM_BORDER    = new Color(0x4076bf);

    private static final Color     ENABLED_ARROW_COLOR      = new Color(0x000000);
    private static final Color     DISABLED_ARROW_COLOR     = new Color(0x9ba8cf);

    private static final Insets    insets                   = new Insets(0, 0, 0, 0);
    private static final Dimension dimension                = new Dimension(22, 13);
    private static final CacheMode cacheMode                = CacheMode.FIXED_SIZES;
    private static final Double    maxH                     = 1.0;
    private static final Double    maxV                     = Double.POSITIVE_INFINITY;

    private Path2D                 path                     = new Path2D.Double();

    private Which                  state;
    private PaintContext           ctx;
    private boolean                focused;

    public SpinnerPreviousButtonPainter(Which state) {
        super();
        this.state = state;

        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintButtonDisabled(g, width, height);
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            paintButtonEnabled(g, width, height);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
            paintButtonPressed(g, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintArrowDisabled(g, width, height);
            break;
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            paintArrowEnabled(g, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintButtonDisabled(Graphics2D g, int width, int height) {
        paintButton(g, width, height, DISABLED_TOP_BORDER, DISABLED_BOTTOM_BORDER, DISABLED_TOP_LINE, DISABLED_TOP_INTERIOR,
            DISABLED_BOTTOM_INTERIOR);
    }

    private void paintButtonEnabled(Graphics2D g, int width, int height) {
        paintButton(g, width, height, ENABLED_TOP_BORDER, ENABLED_BOTTOM_BORDER, ENABLED_TOP_LINE, ENABLED_TOP_INTERIOR,
            ENABLED_BOTTOM_INTERIOR);
    }

    private void paintButtonPressed(Graphics2D g, int width, int height) {
        paintButton(g, width, height, PRESSED_TOP_BORDER, PRESSED_BOTTOM_BORDER, PRESSED_TOP_LINE, PRESSED_TOP_INTERIOR,
            PRESSED_BOTTOM_INTERIOR);
    }

    private void paintArrowDisabled(Graphics2D g, int width, int height) {
        paintArrow(g, width, height, DISABLED_ARROW_COLOR);
    }

    private void paintArrowEnabled(Graphics2D g, int width, int height) {
        paintArrow(g, width, height, ENABLED_ARROW_COLOR);
    }

    private void paintButton(Graphics2D g, int width, int height, Color topBorder, Color bottomBorder, Color topLine, Color topInterior,
        Color bottomInterior) {
        Shape s;

        if (focused) {
            s = setButtonShape(0, 0, width, height, 6);
            g.setColor(OUTER_FOCUS_COLOR);
            g.fill(s);

            s = setButtonShape(0, 0, width - 1, height - 1, 5);
            g.setColor(INNER_FOCUS_COLOR);
            g.fill(s);
        }

        s = setButtonShape(0, 0, width - 2, height - 2, 4);
        g.setPaint(decodeGradient(s, topBorder, bottomBorder));
        g.fill(s);

        s = setButtonShape(1, 1, width - 4, height - 4, 3);
        g.setPaint(decodeGradient(s, topInterior, bottomInterior));
        g.fill(s);

        g.setColor(topLine);
        g.drawLine(1, 0, width - 4, 0);
    }

    private void paintArrow(Graphics2D g, int width, int height, Color color) {
        Shape s = setArrowShape(width, height);
        g.setColor(color);
        g.fill(s);
    }

    private Shape setButtonShape(int left, int top, int width, int height, double arc) {
        int right = left + width;
        int bottom = top + height;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom - arc);
        path.quadTo(right, bottom, right - arc, bottom);
        path.lineTo(left, bottom);
        path.closePath();
        return path;
    }

    private Shape setArrowShape(int left, int height) {
        int centerX = 8;
        int centerY = height / 2;
        path.reset();
        path.moveTo(centerX + 2, centerY + 1);
        path.lineTo(centerX + 4, centerY - 2);
        path.lineTo(centerX, centerY - 2);
        path.closePath();
        return path;
    }

    private Paint decodeGradient(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }
}
