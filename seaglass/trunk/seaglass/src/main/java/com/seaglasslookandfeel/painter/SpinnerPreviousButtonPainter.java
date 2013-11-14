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
 * SpinnerPreviousButtonPainter implementation.
 */
public final class SpinnerPreviousButtonPainter extends AbstractCommonColorsPainter {
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

    private Color             spinnerPrevBorderTopEnabled      = decodeColor("spinnerPrevBorderTopEnabled");
    private Color             spinnerPrevInteriorTopEnabled    = decodeColor("spinnerPrevInteriorTopEnabled");
    private Color             spinnerPrevInteriorBottomEnabled = decodeColor("spinnerPrevInteriorBottomEnabled");
    private Color             spinnerPrevInteriorPressedTop    = decodeColor("spinnerPrevInteriorPressedTop");
    private Color             spinnerPrevInteriorPressedBottom = decodeColor("spinnerPrevInteriorPressedBottom");
    private Color             spinnerPrevTopLineEnabled        = decodeColor("spinnerPrevTopLineEnabled");
    private Color             spinnerPrevTopLinePressed        = decodeColor("spinnerPrevTopLinePressed");
    private Color             scrollBarThumbBorderBasePressed  = decodeColor("scrollBarThumbBorderBasePressed");

    private Color             scrollBarThumbBorderTopPressed   = deriveColor(scrollBarThumbBorderBasePressed, -0.002239f, 0.041885f, 0f, 0);
    private TwoColors         spinnerPrevBorderEnabled         = new TwoColors(spinnerPrevBorderTopEnabled, scrollBarThumbBorderTopPressed);
    private TwoColors         spinnerPrevBorderPressed         = new TwoColors(spinnerPrevBorderTopEnabled, scrollBarThumbBorderTopPressed);
    private TwoColors         spinnerPrevBorderDisabled        = disable(spinnerPrevBorderEnabled);

    private TwoColors         spinnerPrevInteriorEnabled       = new TwoColors(spinnerPrevInteriorTopEnabled,
                                                                   spinnerPrevInteriorBottomEnabled);
    private TwoColors         spinnerPrevInteriorPressed       = new TwoColors(spinnerPrevInteriorPressedTop,
                                                                   spinnerPrevInteriorPressedBottom);
    private TwoColors         spinnerPrevInteriorDisabled      = desaturate(desaturate(spinnerPrevInteriorEnabled));

    private Color             spinnerPrevTopLineDisabled       = desaturate(spinnerPrevTopLineEnabled);

    private PaintContext      ctx;
    private CommonControlState type;
    private boolean           focused;
    private boolean           isForeground;

    public SpinnerPreviousButtonPainter(Which state) {
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

            s = createButtonShape(0, 0, width - 1, height - 1, CornerSize.INNER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.fill(s);
        }

        s = createButtonShape(0, 0, width - 2, height - 2, CornerSize.BORDER);
        g.setPaint(getSpinnerPrevBorderPaint(s, type));
        g.fill(s);

        s = createButtonShape(1, 1, width - 4, height - 4, CornerSize.INTERIOR);
        g.setPaint(getSpinnerPrevInteriorPaint(s, type));
        g.fill(s);

        s = shapeGenerator.createRectangle(1, 0, width - 4, 1);
        g.setPaint(getSpinnerPrevTopLinePaint(s, type));
        g.fill(s);
    }

    private void paintArrow(Graphics2D g, int width, int height) {
        Shape s = createArrowShape(width, height);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);
    }

    private Shape createButtonShape(int x, int y, int width, int height, CornerSize size) {
        return shapeGenerator.createRoundRectangle(x, y, width, height, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED,
            CornerStyle.SQUARE);
    }

    private Shape createArrowShape(int left, int height) {
        int centerX = left / 2 - 3;
        int centerY = height / 2;
        return shapeGenerator.createArrowDown(centerX, centerY - 2, 4, 3);
    }

    public Paint getSpinnerPrevBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getSpinnerPrevBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getSpinnerPrevTopLinePaint(Shape s, CommonControlState type) {
        return getSpinnerPrevTopLineColors(type);
    }

    public Paint getSpinnerPrevInteriorPaint(Shape s, CommonControlState type) {
        TwoColors colors = getSpinnerPrevInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    private TwoColors getSpinnerPrevBorderColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return spinnerPrevBorderDisabled;
        case ENABLED:
            return spinnerPrevBorderEnabled;
        case PRESSED:
            return spinnerPrevBorderPressed;
        }
        return null;
    }

    private TwoColors getSpinnerPrevInteriorColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return spinnerPrevInteriorDisabled;
        case ENABLED:
            return spinnerPrevInteriorEnabled;
        case PRESSED:
            return spinnerPrevInteriorPressed;
        }
        return null;
    }

    private Color getSpinnerPrevTopLineColors(CommonControlState type) {
        switch (type) {
        case DISABLED:
            return spinnerPrevTopLineDisabled;
        case ENABLED:
            return spinnerPrevTopLineEnabled;
        case PRESSED:
            return spinnerPrevTopLinePressed;
        }
        return null;
    }
}
