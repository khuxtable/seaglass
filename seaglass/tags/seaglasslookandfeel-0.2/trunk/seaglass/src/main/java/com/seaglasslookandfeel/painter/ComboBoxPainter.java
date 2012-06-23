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
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * ComboBoxPainter implementation.
 */
public final class ComboBoxPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_DISABLED_PRESSED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SELECTED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_FOCUSED_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
    }

    public CommonControlState           type;

    private ComboBoxArrowButtonPainter buttonPainter;

    // TODO Get this from the UI.
    private static final int           buttonWidth = 21;

    private Which                      state;
    private PaintContext               ctx;
    private boolean                    editable;

    public ComboBoxPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        editable = false;
        if (state == Which.BACKGROUND_DISABLED_EDITABLE || state == Which.BACKGROUND_ENABLED_EDITABLE
                || state == Which.BACKGROUND_PRESSED_EDITABLE || state == Which.BACKGROUND_FOCUSED_EDITABLE) {
            editable = true;
        } else {
            ComboBoxArrowButtonPainter.Which arrowState;
            if (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_DISABLED_PRESSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_DISABLED;
            } else if (state == Which.BACKGROUND_PRESSED || state == Which.BACKGROUND_PRESSED_FOCUSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_PRESSED;
            } else {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_ENABLED;
            }
            buttonPainter = new ComboBoxArrowButtonPainter(arrowState);
        }

        type = getButtonType(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_DISABLED_PRESSED:
            paintDropShadow(g, width, height, true);
            paintButton(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintDropShadow(g, width, height, true);
            paintButton(g, c, width, height);
            break;
        case BACKGROUND_FOCUSED:
            paintFocus(g, c, width, height);
            paintButton(g, c, width, height);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
            paintFocus(g, c, width, height);
            paintButton(g, c, width, height);
            break;
        case BACKGROUND_PRESSED:
        case BACKGROUND_ENABLED_SELECTED:
            paintDropShadow(g, width, height, true);
            paintButton(g, c, width, height);
            break;
        case BACKGROUND_FOCUSED_EDITABLE:
            paintFocus(g, c, width, height);
            break;
        case BACKGROUND_DISABLED_EDITABLE:
        case BACKGROUND_ENABLED_EDITABLE:
        case BACKGROUND_PRESSED_EDITABLE:
            paintDropShadow(g, width, height, false);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlState getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_DISABLED_PRESSED:
        case BACKGROUND_DISABLED_EDITABLE:
            return CommonControlState.DISABLED;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_FOCUSED_EDITABLE:
        case BACKGROUND_ENABLED_EDITABLE:
            return CommonControlState.ENABLED;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
        case BACKGROUND_ENABLED_SELECTED:
        case BACKGROUND_PRESSED_EDITABLE:
            return CommonControlState.PRESSED;
        }
        return null;
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int leftWidth = width - buttonWidth;

        Shape s = createButtonPath(CornerSize.BORDER, 2, 2, leftWidth - 2, height - 4);
        g.setPaint(getComboBoxBackgroundBorderPaint(s, type));
        g.fill(s);

        s = createButtonPath(CornerSize.INTERIOR, 3, 3, leftWidth - 3, height - 6);
        g.setPaint(getComboBoxBackgroundInteriorPaint(s, type));
        g.fill(s);

        // Paint arrow button portion.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(leftWidth, 0);
        buttonPainter.doPaint(g2, c, buttonWidth, height, null);
    }

    private void paintFocus(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarFocus = isInToolBar(c);
        Shape s = createFocusPath(CornerSize.OUTER_FOCUS, 0, 0, width, height);
        g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarFocus));
        g.fill(s);
        s = createFocusPath(CornerSize.INNER_FOCUS, 1, 1, width - 2, height - 2);
        g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarFocus));
        g.fill(s);
    }

    private void paintDropShadow(Graphics2D g, int width, int height, boolean full) {
        // FIXME Make this work again.
        // Shape s = g.getClip();
        // if (full) {
        // g.setClip(0, 0, width, height);
        // } else {
        // g.setClip(width - buttonWidth, 0, buttonWidth, height);
        // }
        // g.setColor(outerShadowColor);
        // s = setPath(CornerSize.OUTER_FOCUS, 1, 2, width - 2, height - 2);
        // g.fill(s);
        // g.setColor(innerShadowColor);
        // s = setPath(CornerSize.INNER_FOCUS, 2, 2, width - 4, height - 3);
        // g.fill(s);
        // g.setClip(s);
    }

    private Shape createButtonPath(CornerSize size, int left, int top, int width, int height) {
        return shapeGenerator.createRoundRectangle(left, top, width, height, size, CornerStyle.ROUNDED, CornerStyle.ROUNDED,
            CornerStyle.SQUARE, CornerStyle.SQUARE);
    }

    private Shape createFocusPath(CornerSize size, int x, int y, int width, int height) {
    	// Rossi: all corners are round on editable combo boxes too.
        CornerStyle leftStyle = CornerStyle.ROUNDED;
        return shapeGenerator.createRoundRectangle(x, y, width, height, size, leftStyle, leftStyle, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED);
    }

    public Paint getComboBoxBackgroundBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getCommonBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getComboBoxBackgroundInteriorPaint(Shape s, CommonControlState type) {
        FourColors colors = getCommonInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    public TwoColors getCommonBorderColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return super.getCommonBorderColors(CommonControlState.DISABLED);
        case ENABLED:
        case PRESSED:
            return super.getCommonBorderColors(CommonControlState.ENABLED);
        }
        return null;
    }

    public FourColors getCommonInteriorColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return super.getCommonInteriorColors(CommonControlState.DISABLED);
        case ENABLED:
        case PRESSED:
            return super.getCommonInteriorColors(CommonControlState.ENABLED);
        }
        return null;
    }
}
