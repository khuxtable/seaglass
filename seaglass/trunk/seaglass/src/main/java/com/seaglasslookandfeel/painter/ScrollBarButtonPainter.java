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

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

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

    private Color             scrollBarButtonBase             = decodeColor("scrollBarButtonBase");
    private Color             scrollBarButtonBasePressed      = decodeColor("scrollBarButtonBasePressed");

    private TwoColors         scrollBarCapColors              = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f,
                                                                  -0.266667f, 0));

    private Color             scrollBarButtonTopPressed       = deriveColor(scrollBarButtonBasePressed, 0.000737f, -0.105657f, 0.101961f, 0);
    private Color             scrollBarButtonMiddlePressed    = deriveColor(scrollBarButtonBasePressed, 0.001240f, -0.041156f, 0.035294f, 0);
    private Color             scrollBarButtonBottomPressed    = deriveColor(scrollBarButtonBasePressed, 0.000348f, 0.050949f, -0.039216f, 0);
    private Color             scrollBarButtonLinePressedColor = deriveColor(scrollBarButtonBasePressed, -0.001400f, 0.110160f, -0.043137f,
                                                                  0);

    private TwoColors         scrollBarButtonIncreaseApart    = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0),
                                                                  scrollBarButtonBase);
    private TwoColors         scrollBarButtonIncreaseTogether = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0),
                                                                  deriveColor(scrollBarButtonBase, 0f, 0f, -0.101961f, 0));
    private TwoColors         scrollBarButtonIncreasePressed  = new TwoColors(scrollBarButtonMiddlePressed, scrollBarButtonBottomPressed);

    private TwoColors         scrollBarButtonDecreaseApart    = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f,
                                                                  -0.2f, 0));
    private TwoColors         scrollBarButtonDecreaseTogether = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f,
                                                                  -0.086275f, 0));
    private TwoColors         scrollBarButtonDecreasePressed  = new TwoColors(scrollBarButtonTopPressed, scrollBarButtonMiddlePressed);

    private Color             scrollBarButtonLine             = deriveColor(scrollBarButtonBase, 0f, 0f, -0.258824f, 0);
    private Color             scrollBarButtonLinePressed      = scrollBarButtonLinePressedColor;
    private Color             scrollBarButtonArrow            = deriveColor(scrollBarButtonBase, 0f, 0f, -0.666667f, 0);
    private Color             scrollBarButtonArrowDisabled    = disable(scrollBarButtonArrow);

    private Color             scrollBarButtonDarkDivider      = deriveColor(scrollBarButtonBase, 0f, 0f, -1f, -(int) (scrollBarButtonBase
                                                                  .getAlpha() * 0.87843137f));
    private Color             scrollBarButtonLightDivider     = deriveColor(scrollBarButtonBase, 0f, 0f, 0f, -(int) (scrollBarButtonBase
                                                                  .getAlpha() * 0.75294117647f));

    private Effect            dropShadow                      = new ScrollButtonDropShadowEffect();

    private Which             state;
    private PaintContext      ctx;
    private CommonControlType type;
    private boolean           isIncrease;
    private boolean           buttonsTogether;

    public ScrollBarButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {
        case FOREGROUND_DISABLED:
            type = CommonControlType.DISABLED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_DISABLED_TOGETHER:
            type = CommonControlType.DISABLED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_ENABLED:
            type = CommonControlType.ENABLED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_ENABLED_TOGETHER:
            type = CommonControlType.ENABLED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_PRESSED:
            type = CommonControlType.PRESSED;
            isIncrease = false;
            buttonsTogether = false;
            break;
        case FOREGROUND_PRESSED_TOGETHER:
            type = CommonControlType.PRESSED;
            isIncrease = false;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_DISABLED:
            type = CommonControlType.DISABLED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_DISABLED_TOGETHER:
            type = CommonControlType.DISABLED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_ENABLED:
            type = CommonControlType.ENABLED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_ENABLED_TOGETHER:
            type = CommonControlType.ENABLED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_INCREASE_PRESSED:
            type = CommonControlType.PRESSED;
            isIncrease = true;
            buttonsTogether = false;
            break;
        case FOREGROUND_INCREASE_PRESSED_TOGETHER:
            type = CommonControlType.PRESSED;
            isIncrease = true;
            buttonsTogether = true;
            break;
        case FOREGROUND_CAP:
            // The type isn't used in this case, but assign it anyway.
            type = CommonControlType.ENABLED;
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
        fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundApart(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonApart(0, 0, width, height);
        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundTogetherDecrease(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonTogetherDecrease(0, 0, width, height);
        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void paintBackgroundTogetherIncrease(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createScrollButtonTogetherIncrease(0, 0, width, height);
        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, type, isIncrease, buttonsTogether);
    }

    private void fillScrollBarButtonInteriorColors(Graphics2D g, Shape s, CommonControlType type, boolean isIncrease,
        boolean buttonsTogether) {
        g.setPaint(getScrollBarButtonBackgroundPaint(s, type, isIncrease, buttonsTogether));
        g.fill(s);

        int width = s.getBounds().width;
        g.setPaint(getScrollBarButtonLinePaint(type));
        g.drawLine(0, 0, width - 1, 0);

        if (state != Which.FOREGROUND_CAP && buttonsTogether) {
            int height = s.getBounds().height;
            g.setPaint(getScrollBarButtonDividerPaint(isIncrease));
            g.drawLine(width - 1, 1, width - 1, height - 1);
        }
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
        g.setPaint(getScrollBarButtonArrowPaint(s, type));
        g.fill(s);
    }

    public Paint getScrollBarButtonBackgroundPaint(Shape s, CommonControlType type, boolean isIncrease, boolean buttonsTogether) {
        TwoColors colors = getScrollBarButtonBackgroundColors(type, isIncrease, buttonsTogether);
        return createHorizontalGradient(s, colors);
    }

    public Paint getScrollBarButtonLinePaint(CommonControlType type) {
        return getScrollBarButtonLineColor(type);
    }

    public Paint getScrollBarButtonDividerPaint(boolean isIncrease) {
        return isIncrease ? scrollBarButtonLightDivider : scrollBarButtonDarkDivider;
    }

    public Paint getScrollBarButtonArrowPaint(Shape s, CommonControlType type) {
        return getScrollBarButtonArrowColor(type);
    }

    private TwoColors getScrollBarButtonBackgroundColors(CommonControlType type, boolean isIncrease, boolean buttonsTogether) {
        if (state == Which.FOREGROUND_CAP) {
            return scrollBarCapColors;
        } else if (type == CommonControlType.PRESSED) {
            return isIncrease ? scrollBarButtonIncreasePressed : scrollBarButtonDecreasePressed;
        } else {
            if (buttonsTogether) {
                return isIncrease ? scrollBarButtonIncreaseTogether : scrollBarButtonDecreaseTogether;
            } else {
                return isIncrease ? scrollBarButtonIncreaseApart : scrollBarButtonDecreaseApart;
            }
        }
    }

    private Color getScrollBarButtonLineColor(CommonControlType type) {
        if (type == CommonControlType.PRESSED) {
            return scrollBarButtonLinePressed;
        } else {
            return scrollBarButtonLine;
        }
    }

    private Color getScrollBarButtonArrowColor(CommonControlType type) {
        if (type == CommonControlType.DISABLED) {
            return scrollBarButtonArrowDisabled;
        } else {
            return scrollBarButtonArrow;
        }
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
