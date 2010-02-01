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
import com.seaglasslookandfeel.painter.util.PaintUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.PaintUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.PaintUtil.FocusType;

/**
 * RadioButtonPainter implementation.
 */
public final class RadioButtonPainter extends AbstractRegionPainter {
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

    private Effect       dropShadow = new SeaGlassDropShadowEffect();

    private PaintContext ctx;
    private boolean      focused;
    private boolean      selected;
    private ButtonType   type;

    public RadioButtonPainter(Which state) {
        super();
        ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {
        case ICON_DISABLED:
        case ICON_ENABLED:
        case ICON_PRESSED:
        case ICON_SELECTED:
        case ICON_PRESSED_SELECTED:
        case ICON_DISABLED_SELECTED:
            focused = false;
            break;
        case ICON_FOCUSED:
        case ICON_PRESSED_FOCUSED:
        case ICON_SELECTED_FOCUSED:
        case ICON_PRESSED_SELECTED_FOCUSED:
            focused = true;
            break;
        }

        switch (state) {
        case ICON_SELECTED:
        case ICON_SELECTED_FOCUSED:
        case ICON_PRESSED_SELECTED:
        case ICON_PRESSED_SELECTED_FOCUSED:
        case ICON_DISABLED_SELECTED:
            selected = true;
            break;
        default:
            selected = false;
        }

        type = getButtonType(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s;
        if (focused) {
            boolean useToolBarFocus = isInToolBar(c);
            s = createBasicShape(width, width, height);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarFocus));
            g.fill(s);
            s = createBasicShape(width - 2, width, height);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarFocus));
            g.fill(s);
        }

        s = createBasicShape(width - 4, width, height);
        if (!focused) {
            dropShadow.fill(g, s);
        }
        g.setPaint(PaintUtil.getButtonBorderPaint(s, type));
        g.fill(s);

        s = createBasicShape(width - 6, width, height);
        g.setPaint(PaintUtil.getButtonInteriorPaint(s, type));
        g.fill(s);

        if (selected) {
            s = createBasicShape(width / 4.5, width, height);
            g.setPaint(PaintUtil.getRadioButtonBulletPaint(s, type));
            g.fill(s);
        }
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case ICON_DISABLED:
        case ICON_DISABLED_SELECTED:
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
        }
        return null;
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private Shape createBasicShape(double diameter, int width, int height) {
        int pos = (int) ((width - diameter) / 2.0 + 0.5);
        int iDiameter = (int) (diameter + 0.5);
        return ShapeUtil.createRadioButton(pos, pos, iDiameter);
    }
}
