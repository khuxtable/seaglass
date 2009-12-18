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
import java.awt.Rectangle;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ComboBoxTextFieldPainter implementation.
 */
public final class ComboBoxTextFieldPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED,
    }

    private static final Color     DISABLED_BORDER = new Color(0xdddddd);
    private static final Color     ENABLED_BORDER  = new Color(0xbbbbbb);
    private static final Color     DARKER_SHADOW   = new Color(0xe1e1e1);
    private static final Color     LIGHTER_SHADOW  = new Color(0xf5f5f5);

    private static final Insets    insets          = new Insets(3, 3, 3, 0);
    private static final Dimension dimension       = new Dimension(64, 23);
    private static final CacheMode cacheMode       = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH            = Double.POSITIVE_INFINITY;
    private static final Double    maxV            = 2.0;

    private Which                  state;
    private PaintContext           ctx;

    private Rectangle              rect            = new Rectangle();
    private Path2D                 path            = new Path2D.Double();

    public ComboBoxTextFieldPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
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
        case BACKGROUND_SELECTED:
            paintSelected(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
        rect.setBounds(3, 3, width - 3, height - 7);
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        g.setColor(color);
        g.fill(rect);

        paintInternalDropShadow(g, width, height);

        g.setColor(DISABLED_BORDER);
        setRect(2, 2, width - 2, height - 5);
        g.draw(path);
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        rect.setBounds(3, 3, width - 3, height - 7);
        g.setColor(c.getBackground());
        g.fill(rect);

        paintInternalDropShadow(g, width, height);

        g.setColor(ENABLED_BORDER);
        setRect(2, 2, width - 2, height - 5);
        g.draw(path);
    }

    private void paintSelected(Graphics2D g, JComponent c, int width, int height) {
        rect.setBounds(3, 3, width - 3, height - 7);
        g.setColor(c.getBackground());
        g.fill(rect);

        paintInternalDropShadow(g, width, height);

        g.setColor(ENABLED_BORDER);
        setRect(2, 2, width - 2, height - 5);
        g.draw(path);
    }

    private void paintInternalDropShadow(Graphics2D g, int width, int height) {
        g.setColor(DARKER_SHADOW);
        g.drawLine(3, 3, width - 1, 3);
        g.setColor(LIGHTER_SHADOW);
        g.drawLine(3, 4, width - 1, 4);
    }

    private void setRect(int x, int y, int width, int height) {
        path.reset();
        path.moveTo(x + width - 1, y);
        path.lineTo(x, y);
        path.lineTo(x, y + height - 1);
        path.lineTo(x + width - 1, y + height - 1);
    }
}
