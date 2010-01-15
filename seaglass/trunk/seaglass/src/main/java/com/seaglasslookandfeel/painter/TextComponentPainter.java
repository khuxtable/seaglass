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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;

/**
 * TextComponentPainter implementation.
 */
public final class TextComponentPainter extends AbstractRegionPainter {
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

    private Color                  OUTER_FOCUS_COLOR      = decodeColor("seaGlassOuterFocus", 0f, 0f, 0f, 0);
    private Color                  INNER_FOCUS_COLOR      = decodeColor("seaGlassFocus", 0f, 0f, 0f, 0);

    private static final Color     DISABLED_BORDER        = new Color(0xdddddd);
    private static final Color     ENABLED_BORDER         = new Color(0xbbbbbb);
    private static final Color     ENABLED_TOOLBAR_BORDER = new Color(0x888888);

    private static final Effect    dropShadow             = new SeaGlassDropShadowEffect();
    private static final Dimension dimension              = new Dimension(90, 30);
    private static final Insets    insets                 = new Insets(5, 3, 3, 3);

    private Path2D                 path                   = new Path2D.Double();
    private Rectangle2D            rect                   = new Rectangle2D.Double();

    private Which                  state;
    private PaintContext           ctx;

    public TextComponentPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        boolean searchType = (c instanceof JTextField && "search".equals(c.getClientProperty("JTextField.variant")));

        int x = focusInsets.left;
        int y = focusInsets.top;
        width -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g, c, x, y, width, height, searchType);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, c, x, y, width, height, searchType);
            break;
        case BACKGROUND_SOLID_DISABLED:
            paintBackgroundSolidDisabled(g, c, x, y, width, height, searchType);
            break;
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolidEnabled(g, c, x, y, width, height, searchType);
            break;
        case BACKGROUND_SELECTED:
            paintBackgroundEnabled(g, c, x, y, width, height, searchType);
            break;
        case BORDER_DISABLED:
            paintBorderDisabled(g, c, x, y, width, height, searchType);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g, c, x, y, width, height, searchType, false);
            break;
        case BORDER_FOCUSED:
            paintBorderEnabled(g, c, x, y, width, height, searchType, true);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        paintBackground(g, c, x, y, width, height, searchType, color);
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType) {
        paintBackground(g, c, x, y, width, height, searchType, c.getBackground());
    }

    private void paintBackgroundSolidDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        paintSolidBackground(g, c, x, y, width, height, searchType, color);
    }

    private void paintBackgroundSolidEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType) {
        paintSolidBackground(g, c, x, y, width, height, searchType, c.getBackground());
    }

    private void paintBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType, Color color) {
        Shape s = decodeBackground(x, y, width, height, searchType);
        g.setColor(color);
        g.fill(s);
    }

    private void paintSolidBackground(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType, Color color) {
        Shape s = decodeSolidBackground(x, y, width, height, searchType);
        g.setColor(color);
        g.fill(s);
    }

    private void paintBorderDisabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType) {
        Shape s = decodeStrokedBorder(x, y, width, height, searchType);
        g.setColor(DISABLED_BORDER);
        g.draw(s);
    }

    private void paintBorderEnabled(Graphics2D g, JComponent c, int x, int y, int width, int height, boolean searchType, boolean focused) {
        Shape s;
        if (focused) {
            s = decodeStrokedOuterBorder(x, y, width, height, searchType);
            g.setColor(OUTER_FOCUS_COLOR);
            g.draw(s);
            s = decodeStrokedInnerBorder(x, y, width, height, searchType);
            g.setColor(INNER_FOCUS_COLOR);
            g.draw(s);
        }
        paintInternalDropShadow(g, x, y, width, height, searchType);
        Color color = ENABLED_BORDER;
        for (Container container = c.getParent(); container != null; container = container.getParent()) {
            if (container instanceof JToolBar) {
                color = ENABLED_TOOLBAR_BORDER;
                break;
            }
        }

        s = decodeStrokedBorder(x, y, width, height, searchType);
        g.setColor(color);
        g.draw(s);
    }

    private void paintInternalDropShadow(Graphics2D g, int x, int y, int width, int height, boolean searchType) {
        Shape clipShape = g.getClip();
        g.setClip(x, y, width, height);
        Shape s = decodeShadowBaseShape(x, y, width, height, searchType);
        dropShadow.fill(g, s, Color.white);
        g.setClip(clipShape);
    }

    private Shape decodeBackground(int x, int y, int width, int height, boolean searchType) {
        return decodeBasicBorderShape(x + 1, y + 1, width - 2, height - 2, searchType);
    }

    private Shape decodeSolidBackground(int x, int y, int width, int height, boolean searchType) {
        return decodeBasicBorderShape(x - 2, y - 2, width + 4, height + 4, searchType);
    }

    private Shape decodeStrokedOuterBorder(int x, int y, int width, int height, boolean searchType) {
        return decodeBasicBorderShape(x - 2, y - 2, width + 3, height + 3, searchType);
    }

    private Shape decodeStrokedInnerBorder(int x, int y, int width, int height, boolean searchType) {
        return decodeBasicBorderShape(x - 1, y - 1, width + 1, height + 1, searchType);
    }

    private Shape decodeStrokedBorder(int x, int y, int width, int height, boolean searchType) {
        return decodeBasicBorderShape(x, y, width - 1, height - 1, searchType);
    }

    private Shape decodeBasicBorderShape(int x, int y, int width, int height, boolean searchType) {
        if (searchType) {
            return decodeRoundedBorderShape(x, y, width, height);
        } else {
            return decodeRectBorderShape(x, y, width, height);
        }
    }

    private Shape decodeShadowBaseShape(int x, int y, int width, int height, boolean searchType) {
        if (searchType) {
            return decodeRoundedShadowBaseShape(x, y, width, height);
        } else {
            return decodeRectShadowBaseShape(x, y, width, height);
        }
    }

    private Shape decodeRectBorderShape(int x, int y, int width, int height) {
        rect.setRect(x, y, width, height);
        return rect;
    }

    private Shape decodeRoundedBorderShape(int x, int y, int width, int height) {
        double x0 = x;
        double y0 = y;
        double x2 = x + width;
        double y2 = y + height;
        double xa = x + height / 2.0;
        double x2a = x2 - height / 2.0;
        double y1 = y + height / 2.0;
        path.reset();
        path.moveTo(xa, y);
        path.quadTo(x0, y0, x0, y1);
        path.quadTo(x0, y2, xa, y2);
        path.lineTo(x2a, y2);
        path.quadTo(x2, y2, x2, y1);
        path.quadTo(x2, y0, x2a, y0);
        path.closePath();
        return path;
    }

    private Shape decodeRectShadowBaseShape(int x, int y, int width, int height) {
        double x0 = x - 1;
        double x1 = x0 + 2;
        double x2 = x0 + width;
        double x3 = x0 + width + 4;
        double y0 = y - 1;
        double y1 = y0 + 2;
        double y2 = y0 + height;

        path.reset();
        path.moveTo(x0, y0);
        path.lineTo(x3, y0);
        path.lineTo(x3, y2);
        path.lineTo(x2, y2);
        path.lineTo(x2, y1);
        path.lineTo(x1, y1);
        path.lineTo(x1, y2);
        path.lineTo(x0, y2);
        path.closePath();
        return path;
    }

    private Shape decodeRoundedShadowBaseShape(int x, int y, int width, int height) {
        y++;
        height--;
        double x0 = x;
        double y0 = y;
        double x2 = x + width;
        double xa = x + height / 2.0;
        double x2a = x2 - height / 2.0;
        double y1 = y + height / 2.0;

        path.reset();
        path.moveTo(x0, y1);
        path.quadTo(x0, y, xa, y0);
        path.lineTo(x2a, y0);
        path.quadTo(x2, y0, x2, y1);

        return path;
    }
}
