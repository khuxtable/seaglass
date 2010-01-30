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

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.FocusType;

/**
 * ComboBoxTextFieldPainter implementation.
 */
public final class SpinnerFormattedTextFieldPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED, BACKGROUND_FOCUSED, BACKGROUND_SELECTED_FOCUSED,
    }

    private Color        disabledBorder = new Color(0xdddddd);
    private Color        enabledBorder  = new Color(0xbbbbbb);

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
            Shape s = ShapeUtil.createRectangle(0, 0, width, height);
            ColorUtil.fillFocus(g, s, FocusType.OUTER_FOCUS, useFocusColors);
            s = ShapeUtil.createRectangle(1, 1, width - 1, height - 2);
            ColorUtil.fillFocus(g, s, FocusType.INNER_FOCUS, useFocusColors);
        }

        g.setColor(c.getBackground());
        g.fillRect(3, 3, width - 3, height - 6);

        paintInternalDropShadow(g, width, height);

        g.setColor(borderColor);
        Shape s = setRect(2, 2, width - 2, height - 4);
        g.draw(s);
    }

    private void paintInternalDropShadow(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createRectangle(3, 3, width - 1, 2);
        ColorUtil.fillInternalShadow(g, s);
    }

    private Shape setRect(int x, int y, int width, int height) {
        return ShapeUtil.createOpenRectangle(x, y, width - 1, height - 1);
    }
}
