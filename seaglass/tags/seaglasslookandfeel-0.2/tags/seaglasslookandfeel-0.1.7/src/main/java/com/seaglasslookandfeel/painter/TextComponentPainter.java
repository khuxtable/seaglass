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

/**
 * TextComponentPainter implementation.
 */
public final class TextComponentPainter extends AbstractCommonColorsPainter {
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

    private SeaGlassInternalShadowEffect internalShadow = new SeaGlassInternalShadowEffect();

    private Which                        state;
    private PaintContext                 ctx;
    private CommonControlState            type;
    private boolean                      focused;

    public TextComponentPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);

        type = (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_SOLID_DISABLED || state == Which.BORDER_DISABLED) ? CommonControlState.DISABLED
                : CommonControlState.ENABLED;
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
        case BACKGROUND_ENABLED:
        case BACKGROUND_SELECTED:
            paintBackground(g, c, x, y, width, height);
            break;
        case BACKGROUND_SOLID_DISABLED:
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolid(g, c, x, y, width, height);
            break;
        case BORDER_DISABLED:
        case BORDER_ENABLED:
        case BORDER_FOCUSED:
            paintBorder(g, c, x, y, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackground(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();
        if (type == CommonControlState.DISABLED) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        }

        Shape s = shapeGenerator.createRectangle(x + 1, y + 1, width - 2, height - 2);
        g.setPaint(color);
        g.fill(s);
    }

    private void paintBackgroundSolid(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();
        if (type == CommonControlState.DISABLED) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        }

        Shape s = shapeGenerator.createRectangle(x - 2, y - 2, width + 4, height + 4);
        g.setPaint(color);
        g.fill(s);
    }

    private void paintBorder(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;

        if (focused) {
            s = shapeGenerator.createRectangle(x - 2, y - 2, width + 3, height + 3);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.draw(s);
            s = shapeGenerator.createRectangle(x - 1, y - 1, width + 1, height + 1);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.draw(s);
        }

        if (type != CommonControlState.DISABLED) {
            s = shapeGenerator.createRectangle(x + 1, x + 1, width - 2, height - 2);
            internalShadow.fill(g, s, false, true);
        }

        g.setPaint(getTextBorderPaint(type, !focused && useToolBarColors));
        g.drawRect(x, y, width - 1, height - 1);
    }
}
