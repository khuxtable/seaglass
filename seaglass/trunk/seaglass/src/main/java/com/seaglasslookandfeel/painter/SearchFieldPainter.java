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

import com.seaglasslookandfeel.painter.util.ShapeUtil;

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

    private Color        outerFocusColor        = decodeColor("seaGlassOuterFocus");
    private Color        innerFocusColor        = decodeColor("seaGlassFocus");
    private Color        outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus");
    private Color        innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus");

    private Color        TRANSPARENT_COLOR      = new Color(0, 0, 0, 0);
    // private Color LIGHT_SHADOW_COLOR = new Color(0, 0, 0, 0x0a);
    private Color        DARK_SHADOW_COLOR      = new Color(0, 0, 0, 0x1e);

    private Color        DISABLED_BORDER        = new Color(0xdddddd);
    private Color        ENABLED_BORDER         = new Color(0xbbbbbb);
    private Color        ENABLED_TOOLBAR_BORDER = new Color(0x888888);

    private Which        state;
    private PaintContext ctx;
    private boolean      focused;

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
        Shape s = decodeBackground(x, y, width, height);
        g.setColor(color);
        g.fill(s);
    }

    private void paintSolidBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, Color color) {
        Shape s = decodeSolidBackground(x, y, width, height);
        g.setColor(color);
        g.fill(s);
    }

    private void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        g.setColor(DISABLED_BORDER);
        drawBorder(g, x, y, width, height);
    }

    private void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);

        if (focused) {
            g.setColor(useToolBarColors ? outerToolBarFocusColor : outerFocusColor);
            drawBorder(g, x - 2, y - 2, width + 4, height + 4);
            g.setColor(useToolBarColors ? innerToolBarFocusColor : innerFocusColor);
            drawBorder(g, x - 1, y - 1, width + 2, height + 2);
        }
        paintInternalDropShadow(g, x, y, width, height);

        g.setColor(!focused && useToolBarColors ? ENABLED_TOOLBAR_BORDER : ENABLED_BORDER);
        drawBorder(g, x, y, width, height);
    }

    private void paintInternalDropShadow(Graphics2D g, int x, int y, int width, int height) {
        paintInnerShadow(g, x, y, width, height);
    }

    private void paintInnerShadow(Graphics2D g, int x, int y, int width, int height) {
        Shape s = decodeRoundedFilled(x + 1, y + 1, width - 2, height - 2);
        g.setPaint(decodeGradientRoundedTopShadow(s));
        s = ShapeUtil.createRoundedInternalShadow(x, y, width, height);
        g.fill(s);
    }

    private Shape decodeBackground(int x, int y, int width, int height) {
        return decodeRoundedFilled(x + 1, y + 1, width - 2, height - 2);
    }

    private Shape decodeSolidBackground(int x, int y, int width, int height) {
        return decodeRoundedFilled(x - 2, y - 2, width + 4, height + 4);
    }

    private void drawBorder(Graphics2D g, int x, int y, int width, int height) {
        Shape s = ShapeUtil.createRoundRectangle(x, y, width - 1, height - 1, height / 2.0);
        g.draw(s);
    }

    private Shape decodeRoundedFilled(int x, int y, int width, int height) {
        return ShapeUtil.createRoundRectangle(x, y, width, height, height / 2.0);
    }

    private Paint decodeGradientRoundedTopShadow(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minY = (float) bounds.getMinY();
        float maxY = (float) bounds.getMaxY();
        float midX = (float) bounds.getCenterX();
        float lowY = (float) (2.0 / bounds.getHeight());
        return decodeGradient(midX, minY, midX, maxY, new float[] { 0f, lowY, 1f }, new Color[] {
            DARK_SHADOW_COLOR,
            TRANSPARENT_COLOR,
            TRANSPARENT_COLOR });
    }
}
