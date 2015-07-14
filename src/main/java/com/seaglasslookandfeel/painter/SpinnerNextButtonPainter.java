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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * SpinnerNextButtonPainter implementation.
 */
public final class SpinnerNextButtonPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        FOREGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_FOCUSED,
        FOREGROUND_PRESSED_FOCUSED,
        FOREGROUND_PRESSED,
    }

    private Color             spinnerNextBorderBottomEnabled   = decodeColor("spinnerNextBorderBottomEnabled");
    private Color             spinnerNextBorderBottomPressed   = decodeColor("spinnerNextBorderBottomPressed");
    private Color             spinnerNextInteriorBottomEnabled = decodeColor("spinnerNextInteriorBottomEnabled");
    private Color             spinnerNextInteriorBottomPressed = decodeColor("spinnerNextInteriorBottomPressed");

    private TwoColors         pressedButtonBorder              = getCommonBorderColors(CommonControlState.PRESSED);
    private TwoColors         spinnerNextBorderEnabled         = new TwoColors(pressedButtonBorder.top, spinnerNextBorderBottomEnabled);
    private TwoColors         spinnerNextBorderPressed         = new TwoColors(pressedButtonBorder.top, spinnerNextBorderBottomPressed);
    private TwoColors         spinnerNextBorderDisabled        = disable(spinnerNextBorderEnabled);

    private FourColors        pressedButtonInterior            = getCommonInteriorColors(CommonControlState.PRESSED);
    private FourColors        selectedButtonInterior           = getCommonInteriorColors(CommonControlState.SELECTED);
    private TwoColors         spinnerNextInteriorEnabled       = new TwoColors(selectedButtonInterior.top, spinnerNextInteriorBottomEnabled);
    private TwoColors         spinnerNextInteriorPressed       = new TwoColors(pressedButtonInterior.top, spinnerNextInteriorBottomPressed);
    private TwoColors         spinnerNextInteriorDisabled      = desaturate(desaturate(spinnerNextInteriorEnabled));

    private PaintContext      ctx;
    private CommonControlState type;
    private boolean           focused;
    private boolean           isForeground;

    public SpinnerNextButtonPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);

        type = getButtonType(state);
        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED 
                || state == Which.FOREGROUND_FOCUSED || state == Which.FOREGROUND_PRESSED_FOCUSED);
        isForeground = isForeground(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (isForeground) {
            paintArrow(g, width, height);
        } else {
            paintButton(g, c, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlState getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case FOREGROUND_DISABLED:
            return CommonControlState.DISABLED;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
            return CommonControlState.ENABLED;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return CommonControlState.PRESSED;
        }
        return null;
    }

    private boolean isForeground(Which state) {
        switch (state) {
        case FOREGROUND_DISABLED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return true;
        default:
            return false;
        }
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;

        JSpinner spinner = (JSpinner) c.getParent();
        boolean editorFocused = false;
        JComponent editor = spinner.getEditor();
        if (editor instanceof DefaultEditor) {
            editorFocused = ((DefaultEditor)editor).getTextField().isFocusOwner();
        }
        if (focused || editorFocused) {
            s = createButtonShape(0, 0, width, height, CornerSize.OUTER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.fill(s);

            s = createButtonShape(0, 1, width - 1, height - 1, CornerSize.INNER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.fill(s);
        }

        s = createButtonShape(0, 2, width - 2, height - 2, CornerSize.BORDER);
        g.setPaint(getSpinnerNextBorderPaint(s, type));
        g.fill(s);

        s = createButtonShape(1, 3, width - 4, height - 4, CornerSize.INTERIOR);
        g.setPaint(getSpinnerNextInteriorPaint(s, type));
        g.fill(s);
    }

    private void paintArrow(Graphics2D g, int width, int height) {
        Shape s = createArrowShape(width, height);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);
    }

    private Shape createButtonShape(int x, int y, int width, int height, CornerSize size) {
        return shapeGenerator.createRoundRectangle(x, y, width, height, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE,
            CornerStyle.ROUNDED);
    }

    private Shape createArrowShape(int left, int height) {
        int centerX = left / 2 -3;
        int centerY = height / 2;
        return shapeGenerator.createArrowUp(centerX, centerY, 4, 3);
    }

    public Paint getSpinnerNextBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getSpinnerNextBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getSpinnerNextInteriorPaint(Shape s, CommonControlState type) {
        TwoColors colors = getSpinnerNextInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    private TwoColors getSpinnerNextBorderColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return spinnerNextBorderDisabled;
        case ENABLED:
            return spinnerNextBorderEnabled;
        case PRESSED:
            return spinnerNextBorderPressed;
        }
        return null;
    }

    private TwoColors getSpinnerNextInteriorColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return spinnerNextInteriorDisabled;
        case ENABLED:
            return spinnerNextInteriorEnabled;
        case PRESSED:
            return spinnerNextInteriorPressed;
        }
        return null;
    }
}
