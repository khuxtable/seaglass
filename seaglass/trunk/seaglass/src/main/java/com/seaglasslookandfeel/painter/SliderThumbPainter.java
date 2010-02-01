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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.PaintUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.PaintUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.PaintUtil.FocusType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * SliderThumbPainter implementation.
 */
public final class SliderThumbPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_FOCUSED_MOUSEOVER,
        BACKGROUND_FOCUSED_PRESSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_ARROWSHAPE,
        BACKGROUND_DISABLED_ARROWSHAPE,
        BACKGROUND_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_PRESSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE,
    }

    private static final Effect dropShadow = new SeaGlassDropShadowEffect();

    private PaintContext        ctx;
    private ButtonType          type;
    private boolean             isFocused;
    private boolean             isDiscrete;

    public SliderThumbPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {
        case BACKGROUND_DISABLED:
            type = ButtonType.DISABLED;
            isFocused = false;
            isDiscrete = false;
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_MOUSEOVER:
            type = ButtonType.ENABLED;
            isFocused = false;
            isDiscrete = false;
            break;
        case BACKGROUND_PRESSED:
            type = ButtonType.PRESSED;
            isFocused = false;
            isDiscrete = false;
            break;
        case BACKGROUND_FOCUSED:
        case BACKGROUND_FOCUSED_MOUSEOVER:
            type = ButtonType.ENABLED;
            isFocused = true;
            isDiscrete = false;
            break;
        case BACKGROUND_FOCUSED_PRESSED:
            type = ButtonType.PRESSED;
            isFocused = true;
            isDiscrete = false;
            break;
        case BACKGROUND_DISABLED_ARROWSHAPE:
            type = ButtonType.DISABLED;
            isFocused = false;
            isDiscrete = true;
            break;
        case BACKGROUND_ENABLED_ARROWSHAPE:
        case BACKGROUND_MOUSEOVER_ARROWSHAPE:
            type = ButtonType.ENABLED;
            isFocused = false;
            isDiscrete = true;
            break;
        case BACKGROUND_PRESSED_ARROWSHAPE:
            type = ButtonType.PRESSED;
            isFocused = false;
            isDiscrete = true;
            break;
        case BACKGROUND_FOCUSED_ARROWSHAPE:
        case BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE:
            type = ButtonType.ENABLED;
            isFocused = true;
            isDiscrete = true;
            break;
        case BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE:
            type = ButtonType.PRESSED;
            isFocused = true;
            isDiscrete = true;
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (isDiscrete) {
            paintDiscrete(g, c, width, height);
        } else {
            paintContinuous(g, c, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDiscrete(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;
        if (isFocused) {
            s = ShapeUtil.createSliderThumbDiscrete(0, 0, width, height, CornerSize.SLIDER_OUTER_FOCUS);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.fill(s);
            s = ShapeUtil.createSliderThumbDiscrete(1, 1, width - 2, height - 2, CornerSize.SLIDER_INNER_FOCUS);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.fill(s);
        }

        s = ShapeUtil.createSliderThumbDiscrete(2, 2, width - 4, height - 4, CornerSize.SLIDER_BORDER);
        if (!isFocused) {
            dropShadow.fill(g, s);
        }
        g.setPaint(PaintUtil.getButtonBorderPaint(s, type));
        g.fill(s);

        s = ShapeUtil.createSliderThumbDiscrete(3, 3, width - 6, height - 6, CornerSize.SLIDER_INTERIOR);
        g.setPaint(PaintUtil.getButtonInteriorPaint(s, type));
        g.fill(s);
    }

    private void paintContinuous(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;
        if (isFocused) {
            s = ShapeUtil.createSliderThumbContinuous(0, 1, width);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.fill(s);
            s = ShapeUtil.createSliderThumbContinuous(1, 2, width - 2);
            g.setPaint(PaintUtil.getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.fill(s);
        }

        s = ShapeUtil.createSliderThumbContinuous(2, 3, width - 4);
        if (!isFocused) {
            dropShadow.fill(g, s);
        }
        g.setPaint(PaintUtil.getButtonBorderPaint(s, type));
        g.fill(s);

        s = ShapeUtil.createSliderThumbContinuous(3, 4, width - 6);
        g.setPaint(PaintUtil.getButtonInteriorPaint(s, type));
        g.fill(s);
    }
}
