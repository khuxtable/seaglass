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
import java.awt.Rectangle;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ComboBoxTextFieldPainter implementation.
 */
public final class SpinnerFormattedTextFieldPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED, BACKGROUND_FOCUSED, BACKGROUND_SELECTED_FOCUSED,
    }

    private Color        outerFocusColor        = decodeColor("seaGlassOuterFocus");
    private Color        innerFocusColor        = decodeColor("seaGlassFocus");
    private Color        outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus");
    private Color        innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus");

    private Color        disabledBorder         = new Color(0xdddddd);
    private Color        enabledBorder          = new Color(0xbbbbbb);
    private Color        darkerShadow           = new Color(0xe1e1e1);
    private Color        lighterShadow          = new Color(0xf5f5f5);

    private Rectangle    rect                   = new Rectangle();
    private Path2D       path                   = new Path2D.Double();

    private Which        state;
    private PaintContext ctx;
    private boolean      focused;

    public SpinnerFormattedTextFieldPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_SELECTED_FOCUSED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintDisabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            paintSelected(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, disabledBorder);
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, enabledBorder);
    }

    private void paintSelected(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, enabledBorder);
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height, Color borderColor) {
        boolean useFocusColors = isInToolBar(c);
        if (focused) {
            rect.setBounds(0, 0, width, height);
            g.setColor(useFocusColors ? outerToolBarFocusColor : outerFocusColor);
            g.fill(rect);
            rect.setBounds(1, 1, width - 1, height - 2);
            g.setColor(useFocusColors ? innerToolBarFocusColor : innerFocusColor);
            g.fill(rect);
        }

        rect.setBounds(3, 3, width - 3, height - 6);
        g.setColor(c.getBackground());
        g.fill(rect);

        paintInternalDropShadow(g, width, height);

        g.setColor(borderColor);
        setRect(2, 2, width - 2, height - 4);
        g.draw(path);
    }

    private void paintInternalDropShadow(Graphics2D g, int width, int height) {
        g.setColor(darkerShadow);
        g.drawLine(3, 3, width - 1, 3);
        g.setColor(lighterShadow);
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
