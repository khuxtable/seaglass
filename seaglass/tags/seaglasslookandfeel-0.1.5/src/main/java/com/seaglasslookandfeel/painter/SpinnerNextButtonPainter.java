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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * SpinnerNextButtonPainter implementation.
 */
public final class SpinnerNextButtonPainter extends AbstractRegionPainter {
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

    private Color        outerFocusColor          = decodeColor("seaGlassOuterFocus");
    private Color        innerFocusColor          = decodeColor("seaGlassFocus");
    private Color        outerToolBarFocusColor   = decodeColor("seaGlassToolBarOuterFocus");
    private Color        innerToolBarFocusColor   = decodeColor("seaGlassToolBarFocus");

    private Color        DISABLED_TOP_INTERIOR    = new Color(0xeaebf1f7, true);
    private Color        DISABLED_BOTTOM_INTERIOR = new Color(0xdbe2e9f2, true);
    private Color        DISABLED_TOP_BORDER      = new Color(0x80a2c2ed, true);
    private Color        DISABLED_BOTTOM_BORDER   = new Color(0x807ea4d7, true);

    private Color        ENABLED_TOP_INTERIOR     = new Color(0xbccedf);
    private Color        ENABLED_BOTTOM_INTERIOR  = new Color(0x85abcf);
    private Color        ENABLED_TOP_BORDER       = new Color(0x4f7bbf);
    private Color        ENABLED_BOTTOM_BORDER    = new Color(0x4779bf);

    private Color        PRESSED_TOP_INTERIOR     = new Color(0xacbdd0);
    private Color        PRESSED_BOTTOM_INTERIOR  = new Color(0x6e92b6);
    private Color        PRESSED_TOP_BORDER       = new Color(0x4f7bbf);
    private Color        PRESSED_BOTTOM_BORDER    = new Color(0x4879bf);

    private Color        ENABLED_ARROW_COLOR      = new Color(0x000000);
    private Color        DISABLED_ARROW_COLOR     = new Color(0x9ba8cf);

    private Which        state;
    private PaintContext ctx;
    private boolean      focused;

    public SpinnerNextButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintButtonDisabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            paintButtonEnabled(g, c, width, height);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
            paintButtonPressed(g, c, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintArrowDisabled(g, c, width, height);
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

    private void paintButtonDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, DISABLED_TOP_BORDER, DISABLED_BOTTOM_BORDER, DISABLED_TOP_INTERIOR, DISABLED_BOTTOM_INTERIOR);
    }

    private void paintButtonEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, ENABLED_TOP_BORDER, ENABLED_BOTTOM_BORDER, ENABLED_TOP_INTERIOR, ENABLED_BOTTOM_INTERIOR);
    }

    private void paintButtonPressed(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, PRESSED_TOP_BORDER, PRESSED_BOTTOM_BORDER, PRESSED_TOP_INTERIOR, PRESSED_BOTTOM_INTERIOR);
    }

    private void paintArrowDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintArrow(g, width, height, DISABLED_ARROW_COLOR);
    }

    private void paintArrowEnabled(Graphics2D g, int width, int height) {
        paintArrow(g, width, height, ENABLED_ARROW_COLOR);
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height, Color topBorder, Color bottomBorder, Color topInterior,
        Color bottomInterior) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;

        if (focused) {
            s = setButtonShape(0, 0, width, height, CornerSize.OUTER_FOCUS);
            g.setColor(useToolBarColors ? outerToolBarFocusColor : outerFocusColor);
            g.fill(s);

            s = setButtonShape(0, 1, width - 1, height - 1, CornerSize.INNER_FOCUS);
            g.setColor(useToolBarColors ? innerToolBarFocusColor : innerFocusColor);
            g.fill(s);
        }

        s = setButtonShape(0, 2, width - 2, height - 2, CornerSize.BORDER);
        g.setPaint(decodeGradient(s, topBorder, bottomBorder));
        g.fill(s);

        s = setButtonShape(1, 3, width - 4, height - 4, CornerSize.INTERIOR);
        g.setPaint(decodeGradient(s, topInterior, bottomInterior));
        g.fill(s);
    }

    private void paintArrow(Graphics2D g, int width, int height, Color color) {
        Shape s = setArrowShape(width, height);
        g.setColor(color);
        g.fill(s);
    }

    private Shape setButtonShape(int x, int y, int width, int height, CornerSize size) {
        return ShapeUtil.createRoundRectangle(x, y, width, height, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE,
            CornerStyle.ROUNDED);
    }

    private Shape setArrowShape(int left, int height) {
        int centerX = 8;
        int centerY = height / 2;
        return ShapeUtil.createArrowUp(centerX, centerY, 4, 3);
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
