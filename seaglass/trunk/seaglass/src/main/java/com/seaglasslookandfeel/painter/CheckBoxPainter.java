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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ColorUtil.FocusType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * CheckBoxPainter implementation.
 */
public final class CheckBoxPainter extends AbstractRegionPainter {
    public static enum Which {
        ICON_DISABLED,
        ICON_ENABLED,
        ICON_FOCUSED,
        ICON_PRESSED,
        ICON_PRESSED_FOCUSED,
        ICON_SELECTED,
        ICON_SELECTED_FOCUSED,
        ICON_PRESSED_SELECTED,
        ICON_PRESSED_SELECTED_FOCUSED,
        ICON_DISABLED_SELECTED,
    }

    private PaintContext ctx;
    private boolean      focused;
    private boolean      selected;

    private Effect       dropShadow = new SeaGlassDropShadowEffect();

    private ButtonType   type;

    public CheckBoxPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getButtonType(state);

        focused = false;
        selected = false;
        if (state == Which.ICON_FOCUSED || state == Which.ICON_PRESSED_FOCUSED || state == Which.ICON_SELECTED_FOCUSED
                || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            focused = true;
        }
        if (state == Which.ICON_SELECTED || state == Which.ICON_PRESSED_SELECTED || state == Which.ICON_DISABLED_SELECTED
                || state == Which.ICON_SELECTED_FOCUSED || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            selected = true;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = width < height ? width : height;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        Shape s;
        if (focused) {
            boolean useToolBarFocus = isInToolBar(c);
            s = ShapeUtil.createRoundRectangle(x, y, size, size, CornerSize.CHECKBOX_OUTER_FOCUS);
            g.setPaint(ColorUtil.getFocusPaint(g, s, FocusType.OUTER_FOCUS, useToolBarFocus));
            g.fill(s);
            s = ShapeUtil.createRoundRectangle(x + 1, y + 1, size - 2, size - 2, CornerSize.CHECKBOX_INNER_FOCUS);
            g.setPaint(ColorUtil.getFocusPaint(g, s, FocusType.INNER_FOCUS, useToolBarFocus));
            g.fill(s);
        }

        s = ShapeUtil.createRoundRectangle(x + 2, y + 2, size - 4, size - 4, CornerSize.CHECKBOX_BORDER);
        if (!focused) {
            dropShadow.fill(g, s);
        }
        ColorUtil.fillCheckBoxBorderColors(g, s, type);

        s = ShapeUtil.createRoundRectangle(x + 3, y + 3, size - 6, size - 6, CornerSize.CHECKBOX_INTERIOR);
        ColorUtil.fillCheckBoxInteriorColors(g, s, type);

        if (selected) {
            Shape s1 = createCheckMark(x, y, size);
            ColorUtil.fillCheckBoxBulletColors(g, s1, type);
        }
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case ICON_DISABLED:
            return ButtonType.DISABLED;
        case ICON_ENABLED:
        case ICON_FOCUSED:
            return ButtonType.ENABLED;
        case ICON_PRESSED:
        case ICON_PRESSED_FOCUSED:
            return ButtonType.PRESSED;
        case ICON_SELECTED:
        case ICON_SELECTED_FOCUSED:
            return ButtonType.SELECTED;
        case ICON_PRESSED_SELECTED:
        case ICON_PRESSED_SELECTED_FOCUSED:
            return ButtonType.PRESSED_SELECTED;
        case ICON_DISABLED_SELECTED:
            return ButtonType.DISABLED_SELECTED;
        }
        return null;
    }

    private static final double SIZE_MULTIPLIER = 2.0 / 3.0;
    private static final double X_MULTIPLIER    = 5.0 / 18.0;
    private static final double Y_MULTIPLIER    = 1.0 / 18.0;

    private Shape createCheckMark(int x, int y, int size) {
        int markSize = (int) (size * SIZE_MULTIPLIER + 0.5);
        int markX = x + (int) (size * X_MULTIPLIER + 0.5);
        int markY = y + (int) (size * Y_MULTIPLIER + 0.5);

        return ShapeUtil.createCheckMark(markX, markY, markSize, markSize);
    }
}
