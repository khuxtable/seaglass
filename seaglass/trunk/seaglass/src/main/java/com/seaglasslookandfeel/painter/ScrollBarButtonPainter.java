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

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        FOREGROUND_ENABLED,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_INCREASE_ENABLED,
        FOREGROUND_INCREASE_DISABLED,
        FOREGROUND_INCREASE_PRESSED,

        FOREGROUND_ENABLED_TOGETHER,
        FOREGROUND_DISABLED_TOGETHER,
        FOREGROUND_PRESSED_TOGETHER,
        FOREGROUND_INCREASE_ENABLED_TOGETHER,
        FOREGROUND_INCREASE_DISABLED_TOGETHER,
        FOREGROUND_INCREASE_PRESSED_TOGETHER,

        FOREGROUND_CAP,
    }

    private Effect       dropShadow = new ScrollButtonDropShadowEffect();

    private Which        state;
    private PaintContext ctx;
    private ButtonType   type;
    private boolean      isIncrease;
    private boolean      buttonsTogether;

    public ScrollBarButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {
        case FOREGROUND_DISABLED:
            type = ButtonType.DISABLED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_DISABLED_TOGETHER:
            type = ButtonType.DISABLED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_ENABLED:
            type = ButtonType.ENABLED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_ENABLED_TOGETHER:
            type = ButtonType.ENABLED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_PRESSED:
            type = ButtonType.PRESSED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_PRESSED_TOGETHER:
            type = ButtonType.PRESSED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_DISABLED:
            type = ButtonType.DISABLED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_DISABLED_TOGETHER:
            type = ButtonType.DISABLED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_ENABLED:
            type = ButtonType.ENABLED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_ENABLED_TOGETHER:
            type = ButtonType.ENABLED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_PRESSED:
            type = ButtonType.PRESSED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_PRESSED_TOGETHER:
            type = ButtonType.PRESSED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_CAP:
            type = ButtonType.SCROLL_CAP;
            isIncrease = false;
            buttonsTogether = true;
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FOREGROUND_DISABLED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_PRESSED:
            paintBackgroundApart(g, width, height);
            paintDecreaseButtonApart(g, width, height);
            break;
        case FOREGROUND_DISABLED_TOGETHER:
        case FOREGROUND_ENABLED_TOGETHER:
        case FOREGROUND_PRESSED_TOGETHER:
            paintBackgroundTogetherDecrease(g, width, height);
            paintDecreaseButtonTogether(g, width, height);
            break;
        case FOREGROUND_INCREASE_DISABLED:
        case FOREGROUND_INCREASE_ENABLED:
        case FOREGROUND_INCREASE_PRESSED:
            paintBackgroundApart(g, width, height);
            paintIncreaseButtonApart(g, width, height);
            break;
        case FOREGROUND_INCREASE_DISABLED_TOGETHER:
        case FOREGROUND_INCREASE_ENABLED_TOGETHER:
        case FOREGROUND_INCREASE_PRESSED_TOGETHER:
            paintBackgroundTogetherIncrease(g, width, height);
            paintIncreaseButtonTogether(g, width, height);
            break;
        case FOREGROUND_CAP:
            paintBackgroundCap(g, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundCap(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollCap(0, 0, width, height);
        dropShadow.fill(g, s);
        ColorUtil.fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundApart(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonApart(0, 0, width, height);
        dropShadow.fill(g, s);
        ColorUtil.fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundTogetherDecrease(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonTogetherDecrease(0, 0, width, height);
        dropShadow.fill(g, s);
        ColorUtil.fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundTogetherIncrease(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonTogetherIncrease(0, 0, width, height);
        dropShadow.fill(g, s);
        ColorUtil.fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintIncreaseButtonApart(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 5, height / 2.0 - 2);
    }

    private void paintDecreaseButtonApart(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 4, height / 2.0 - 3);
    }

    private void paintIncreaseButtonTogether(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 3, height / 2.0 - 3);
    }

    private void paintDecreaseButtonTogether(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0, height / 2.0 - 3);
    }

    private void paintArrowButton(Graphics2D g, double x, double y) {
        Shape s = ShapeUtil.createArrowLeft(x, y, 4, 6);
        ColorUtil.fillScrollBarButtonArrowColors(g, s, type);
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    private static class ScrollButtonDropShadowEffect extends DropShadowEffect {

        public ScrollButtonDropShadowEffect() {
            color = new Color(150, 150, 150);
            angle = 0;
            distance = 0;
            size = 3;
            opacity = 0.25f;
        }
    }
}
