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
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ColorUtil.FourLayerColors;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * ComboBoxArrowButtonPainter implementation.
 */
public final class ComboBoxArrowButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_PRESSED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
        BACKGROUND_SELECTED_EDITABLE,
        FOREGROUND_ENABLED,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_SELECTED,
        FOREGROUND_EDITABLE,
        FOREGROUND_EDITABLE_DISABLED,
    }

    private static final Color ENABLED_ARROW_COLOR  = new Color(0x000000);
    private static final Color DISABLED_ARROW_COLOR = new Color(0x9ba8cf);

    private Which              state;
    private PaintContext       ctx;

    private FourLayerColors    colors;

    public ComboBoxArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        ButtonType buttonType = getButtonType(state);
        if (buttonType != null) {
            colors = ColorUtil.getComboBoxButtonColors(buttonType);
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED_EDITABLE:
            paintDisabledEditable(g, c, width, height);
            break;
        case BACKGROUND_ENABLED_EDITABLE:
            paintEnabledEditable(g, c, width, height);
            break;
        case BACKGROUND_PRESSED_EDITABLE:
            paintPressedEditable(g, c, width, height);
            break;
        case BACKGROUND_SELECTED_EDITABLE:
            paintPressedEditable(g, c, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintArrowsDisabled(g, c, width, height);
            break;
        case FOREGROUND_PRESSED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_SELECTED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_EDITABLE:
            paintArrowDownEnabled(g, c, width, height);
            break;
        case FOREGROUND_EDITABLE_DISABLED:
            paintArrowDownDisabled(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED_EDITABLE:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED_EDITABLE:
            return ButtonType.ENABLED;
        case BACKGROUND_PRESSED_EDITABLE:
        case BACKGROUND_SELECTED_EDITABLE:
            return ButtonType.PRESSED;
        }
        return null;
    }

    private void paintDisabledEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, colors);
    }

    private void paintEnabledEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, colors);
    }

    private void paintPressedEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, colors);
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height, FourLayerColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = createButtonPath(CornerSize.BORDER, 0, 2, width - 2, height - 4);
        ColorUtil.fillTwoColorGradientVertical(g, s, colors.background);

        s = createButtonPath(CornerSize.INTERIOR, 1, 3, width - 4, height - 6);
        ColorUtil.fillThreeLayerGradientVertical(g, s, colors);
    }

    private void paintArrowsEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(0.5, 0.5, 3, 4);
        g.setColor(ENABLED_ARROW_COLOR);
        g.fill(s);

        s = ShapeUtil.createArrowRight(6.5, 0.5, 3, 4);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowsDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(0.5, 0.5, 3, 4);
        g.setColor(DISABLED_ARROW_COLOR);
        g.fill(s);

        s = ShapeUtil.createArrowRight(6.5, 0.5, 3, 4);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(1, 1, 4.2, 6);
        g.setColor(ENABLED_ARROW_COLOR);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(1, 1, 4.2, 6);
        g.setColor(DISABLED_ARROW_COLOR);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private Shape createButtonPath(CornerSize size, int x, int y, int w, int h) {
        return ShapeUtil.createRoundRectangle(x, y, w, h, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED);
    }
}
