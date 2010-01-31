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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.SeaGlassInternalShadowEffect;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.FocusType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * TextComponentPainter implementation.
 */
public final class SearchFieldPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_SOLID_DISABLED,
        BACKGROUND_SOLID_ENABLED,
        BACKGROUND_SELECTED,
        BORDER_DISABLED,
        BORDER_FOCUSED,
        BORDER_ENABLED,
    }

    private SeaGlassInternalShadowEffect internalShadow       = new SeaGlassInternalShadowEffect();

    private Color                        disabledBorder       = decodeColor("seaGlassTextDisabledBorder");
    private Color                        enabledBorder        = decodeColor("seaGlassTextEnabledBorder");
    private Color                        enabledToolbarBorder = decodeColor("seaGlassTextEnabledToolbarBorder");

    private Which                        state;
    private PaintContext                 ctx;
    private boolean                      focused;

    public SearchFieldPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);

        focused = (state == Which.BORDER_FOCUSED);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        int x = focusInsets.left;
        int y = focusInsets.top;
        width -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g, c, x, y, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, c, x, y, width, height);
            break;
        case BACKGROUND_SOLID_DISABLED:
            paintBackgroundSolidDisabled(g, c, x, y, width, height);
            break;
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolidEnabled(g, c, x, y, width, height);
            break;
        case BACKGROUND_SELECTED:
            paintBackgroundEnabled(g, c, x, y, width, height);
            break;
        case BORDER_DISABLED:
            paintBorderDisabled(g, c, x, y, width, height);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g, c, x, y, width, height);
            break;
        case BORDER_FOCUSED:
            paintBorderEnabled(g, c, x, y, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        paintBackground(g, c, x, y, width, height, color);
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        paintBackground(g, c, x, y, width, height, c.getBackground());
    }

    private void paintBackgroundSolidDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        paintSolidBackground(g, c, x, y, width, height, color);
    }

    private void paintBackgroundSolidEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        paintSolidBackground(g, c, x, y, width, height, c.getBackground());
    }

    private void paintBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Color color) {
        Shape s = ShapeUtil.createRoundRectangle(x + 1, y + 1, width - 2, height - 2, CornerSize.ROUND_HEIGHT);
        g.setColor(color);
        g.fill(s);
    }

    private void paintSolidBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Color color) {
        Shape s = ShapeUtil.createRoundRectangle(x - 2, y - 2, width + 4, height + 4, CornerSize.ROUND_HEIGHT);
        g.setColor(color);
        g.fill(s);
    }

    private void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        g.setColor(disabledBorder);
        Shape s = ShapeUtil.createRoundRectangle(x, y, width - 1, height - 1, CornerSize.ROUND_HEIGHT_DRAW);
        g.draw(s);
    }

    private void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);

        Shape s;
        if (focused) {
            s = ShapeUtil.createRoundRectangle(x - 2, y - 2, width + 4 - 1, height + 4 - 1, CornerSize.ROUND_HEIGHT_DRAW);
            g.setPaint(ColorUtil.getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.draw(s);
            s = ShapeUtil.createRoundRectangle(x - 1, y - 1, width + 2 - 1, height + 2 - 1, CornerSize.ROUND_HEIGHT_DRAW);
            g.setPaint(ColorUtil.getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.draw(s);
        }

        s = ShapeUtil.createInternalDropShadowRounded(x + 1, y + 1, width - 2, height - 2);
        internalShadow.fill(g, s, true, true);

        g.setColor(!focused && useToolBarColors ? enabledToolbarBorder : enabledBorder);
        s = ShapeUtil.createRoundRectangle(x, y, width - 1, height - 1, CornerSize.ROUND_HEIGHT_DRAW);
        g.draw(s);
    }
}
