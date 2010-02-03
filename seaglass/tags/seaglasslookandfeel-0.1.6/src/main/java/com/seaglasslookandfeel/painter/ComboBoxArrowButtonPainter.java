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
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * ComboBoxArrowButtonPainter implementation.
 */
public final class ComboBoxArrowButtonPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_PRESSED, BACKGROUND_SELECTED,

        FOREGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_PRESSED, FOREGROUND_SELECTED,

        FOREGROUND_ENABLED_EDITABLE, FOREGROUND_DISABLED_EDITABLE,
    }

    private Which             state;
    private PaintContext      ctx;

    private CommonControlType type;

    public ComboBoxArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getButtonType(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_PRESSED:
        case BACKGROUND_SELECTED:
            paintButton(g, c, width, height);
            break;
        case FOREGROUND_ENABLED:
        case FOREGROUND_DISABLED:
        case FOREGROUND_PRESSED:
        case FOREGROUND_SELECTED:
            paintArrows(g, c, width, height);
            break;
        case FOREGROUND_ENABLED_EDITABLE:
        case FOREGROUND_DISABLED_EDITABLE:
            paintArrowDown(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED:
            return CommonControlType.ENABLED;
        case BACKGROUND_PRESSED:
        case BACKGROUND_SELECTED:
            return CommonControlType.PRESSED;
        case FOREGROUND_ENABLED:
        case FOREGROUND_PRESSED:
        case FOREGROUND_SELECTED:
        case FOREGROUND_ENABLED_EDITABLE:
            return CommonControlType.ENABLED;
        case FOREGROUND_DISABLED:
        case FOREGROUND_DISABLED_EDITABLE:
            return CommonControlType.DISABLED;
        }
        return null;
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = createButtonPath(CornerSize.BORDER, 0, 2, width - 2, height - 4);
        g.setPaint(getComboBoxButtonBorderPaint(s, type));
        g.fill(s);

        s = createButtonPath(CornerSize.INTERIOR, 1, 3, width - 4, height - 6);
        g.setPaint(getComboBoxButtonInteriorPaint(s, type));
        g.fill(s);
    }

    private void paintArrows(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(0.5, 0.5, 3, 4);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);

        s = ShapeUtil.createArrowRight(6.5, 0.5, 3, 4);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDown(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(1, 1, 4.2, 6);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private Shape createButtonPath(CornerSize size, int x, int y, int w, int h) {
        return ShapeUtil.createRoundRectangle(x, y, w, h, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED);
    }

    public Paint getComboBoxButtonBorderPaint(Shape s, CommonControlType type) {
        TwoColors colors = getCommonBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getComboBoxButtonInteriorPaint(Shape s, CommonControlType type) {
        FourColors colors = getCommonInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    public TwoColors getCommonBorderColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return super.getCommonBorderColors(CommonControlType.DISABLED);
        case ENABLED:
            return super.getCommonBorderColors(CommonControlType.PRESSED);
        case PRESSED:
            return super.getCommonBorderColors(CommonControlType.PRESSED_SELECTED);
        }
        return null;
    }

    public FourColors getCommonInteriorColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return super.getCommonInteriorColors(CommonControlType.DISABLED);
        case ENABLED:
            return super.getCommonInteriorColors(CommonControlType.PRESSED);
        case PRESSED:
            return super.getCommonInteriorColors(CommonControlType.PRESSED_SELECTED);
        }
        return null;
    }
}
