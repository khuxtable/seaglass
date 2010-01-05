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

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;

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

    private static final Color     OUTER_FOCUS_COLOR      = new Color(0x8072a5d2, true);
    private static final Color     INNER_FOCUS_COLOR      = new Color(0x73a4d1);

    private static final Color     DISABLED_BORDER        = new Color(0xdddddd);
    private static final Color     ENABLED_BORDER         = new Color(0xbbbbbb);
    private static final Color     ENABLED_TOOLBAR_BORDER = new Color(0x888888);

    private static final Effect    dropShadow             = new TextFieldDropShadowEffect();
    private static final Dimension dimension              = new Dimension(90, 30);
    private static final Insets    insets                 = new Insets(5, 3, 3, 3);

    Path2D                         path                   = new Path2D.Double();

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

        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g, c, width, height, searchType);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, c, width, height, searchType);
            break;
        case BACKGROUND_SOLID_DISABLED:
            paintBackgroundSolidDisabled(g, c, width, height, searchType);
            break;
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolidEnabled(g, c, width, height, searchType);
            break;
        case BACKGROUND_SELECTED:
            paintBackgroundEnabled(g, c, width, height, searchType);
            break;
        case BORDER_DISABLED:
            paintBorderDisabled(g, c, width, height, searchType);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g, c, width, height, searchType, false);
            break;
        case BORDER_FOCUSED:
            paintBorderEnabled(g, c, width, height, searchType, true);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g, JComponent c, int width, int height, boolean searchType) {
        setPath(3, 3, width - 6, height - 6, searchType);
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        g.setColor(color);
        g.fill(path);
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height, boolean searchType) {
        setPath(3, 3, width - 6, height - 6, searchType);
        g.setColor(c.getBackground());
        g.fill(path);
    }

    private void paintBackgroundSolidDisabled(Graphics2D g, JComponent c, int width, int height, boolean searchType) {
        setPath(0, 0, width, height, searchType);
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        g.setColor(color);
        g.fill(path);
    }

    private void paintBackgroundSolidEnabled(Graphics2D g, JComponent c, int width, int height, boolean searchType) {
        setPath(0, 0, width, height, searchType);
        g.setColor(c.getBackground());
        g.fill(path);
    }

    private void paintBorderDisabled(Graphics2D g, JComponent c, int width, int height, boolean searchType) {
        paintInternalDropShadow(g, width, height, searchType);

        g.setColor(DISABLED_BORDER);
        g.drawRect(2, 2, width - 5, height - 5);
    }

    private void paintBorderEnabled(Graphics2D g, JComponent c, int width, int height, boolean searchType, boolean focused) {
        if (focused) {
            setPath(0, 0, width - 1, height - 1, searchType);
            g.setColor(OUTER_FOCUS_COLOR);
            g.draw(path);
            setPath(1, 1, width - 3, height - 3, searchType);
            g.setColor(INNER_FOCUS_COLOR);
            g.draw(path);
        }
        paintInternalDropShadow(g, width, height, searchType);
        Color color = ENABLED_BORDER;
        for (Container container = c.getParent(); container != null; container = container.getParent()) {
            if (container instanceof JToolBar) {
                color = ENABLED_TOOLBAR_BORDER;
                break;
            }
        }

        setPath(2, 2, width - 5, height - 5, searchType);
        g.setColor(color);
        g.draw(path);
    }

    private void paintInternalDropShadow(Graphics2D g, int width, int height, boolean searchType) {
        Shape s = g.getClip();
        g.setClip(2, 2, width - 5, height - 5);
        setShadowPart(2, 2, width - 6, height - 6, searchType);
        dropShadow.draw(g, path, width, height);
        g.setClip(s);
    }

    private void setPath(int x, int y, int width, int height, boolean searchType) {
        path.reset();
        double x0 = x;
        double y0 = y;
        double x2 = x + width;
        double y2 = y + height;
        if (searchType) {
            double xa = x + height / 2.0;
            double x2a = x2 - height / 2.0;
            double y1 = y + height / 2.0;
            path.moveTo(xa, y);
            path.quadTo(x0, y0, x0, y1);
            path.quadTo(x0, y2, xa, y2);
            path.lineTo(x2a, y2);
            path.quadTo(x2, y2, x2, y1);
            path.quadTo(x2, y0, x2a, y0);
        } else {
            path.moveTo(x0, y0);
            path.lineTo(x0, y2);
            path.lineTo(x2, y2);
            path.lineTo(x2, y0);
        }
        path.closePath();
    }

    private void setShadowPart(int x, int y, int width, int height, boolean searchType) {
        y++;
        height--;
        path.reset();
        double x0 = x;
        double y0 = y;
        double x2 = x + width;
        if (searchType) {
            double xa = x + height / 2.0;
            double x2a = x2 - height / 2.0;
            double y1 = y + height / 2.0;
            path.moveTo(x0, y1);
            path.quadTo(x0, y, xa, y0);
            path.lineTo(x2a, y0);
            path.quadTo(x2, y0, x2, y1);
        } else {
            path.moveTo(x0, y0);
            path.lineTo(x2, y0);
        }
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    public static class TextFieldDropShadowEffect extends DropShadowEffect {

        public TextFieldDropShadowEffect() {
            color = new Color(230, 230, 230);
            angle = 90;
            distance = 0;
            size = 3;
            opacity = 0.35f;
        }
    }
}
