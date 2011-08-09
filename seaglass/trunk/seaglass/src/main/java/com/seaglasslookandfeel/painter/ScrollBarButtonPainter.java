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

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractRegionPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        FOREGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_PRESSED, FOREGROUND_INCREASE_ENABLED, FOREGROUND_INCREASE_DISABLED,
        FOREGROUND_INCREASE_PRESSED,

        FOREGROUND_ENABLED_TOGETHER, FOREGROUND_DISABLED_TOGETHER, FOREGROUND_PRESSED_TOGETHER, FOREGROUND_INCREASE_ENABLED_TOGETHER,
        FOREGROUND_INCREASE_DISABLED_TOGETHER, FOREGROUND_INCREASE_PRESSED_TOGETHER,

        FOREGROUND_CAP,
    }

    private Color scrollBarButtonBase        = decodeColor("scrollBarButtonBase");
    private Color scrollBarButtonBasePressed = decodeColor("scrollBarButtonBasePressed");

    private TwoColors scrollBarCapColors = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f, -0.266667f, 0));

    private Color scrollBarButtonTopPressed       = deriveColor(scrollBarButtonBasePressed, 0.000737f, -0.105657f, 0.101961f, 0);
    private Color scrollBarButtonMiddlePressed    = deriveColor(scrollBarButtonBasePressed, 0.001240f, -0.041156f, 0.035294f, 0);
    private Color scrollBarButtonBottomPressed    = deriveColor(scrollBarButtonBasePressed, 0.000348f, 0.050949f, -0.039216f, 0);
    private Color scrollBarButtonLinePressedColor = deriveColor(scrollBarButtonBasePressed, -0.001400f, 0.110160f, -0.043137f,
                                                                0);

    private TwoColors scrollBarButtonIncreaseApart    = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0),
                                                                      scrollBarButtonBase);
    private TwoColors scrollBarButtonIncreaseTogether = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0),
                                                                      deriveColor(scrollBarButtonBase, 0f, 0f, -0.101961f, 0));
    private TwoColors scrollBarButtonIncreasePressed  = new TwoColors(scrollBarButtonMiddlePressed, scrollBarButtonBottomPressed);

    private TwoColors scrollBarButtonDecreaseApart    = new TwoColors(scrollBarButtonBase,
                                                                      deriveColor(scrollBarButtonBase, 0f, 0f, -0.2f, 0));
    private TwoColors scrollBarButtonDecreaseTogether = new TwoColors(scrollBarButtonBase,
                                                                      deriveColor(scrollBarButtonBase, 0f, 0f, -0.086275f, 0));
    private TwoColors scrollBarButtonDecreasePressed  = new TwoColors(scrollBarButtonTopPressed, scrollBarButtonMiddlePressed);

    private Color scrollBarButtonLine          = deriveColor(scrollBarButtonBase, 0f, 0f, -0.258824f, 0);
    private Color scrollBarButtonLinePressed   = scrollBarButtonLinePressedColor;
    private Color scrollBarButtonArrow         = deriveColor(scrollBarButtonBase, 0f, 0f, -0.666667f, 0);
    private Color scrollBarButtonArrowDisabled = disable(scrollBarButtonArrow);

    private Color scrollBarButtonDarkDivider  = deriveColor(scrollBarButtonBase, 0f, 0f, -1f,
                                                            -(int) (scrollBarButtonBase.getAlpha() * 0.87843137f));
    private Color scrollBarButtonLightDivider = deriveColor(scrollBarButtonBase, 0f, 0f, 0f,
                                                            -(int) (scrollBarButtonBase.getAlpha() * 0.75294117647f));

    private Effect dropShadow = new ScrollButtonDropShadowEffect();

    private Which        state;
    private PaintContext ctx;
    private boolean      isPressed;
    private boolean      isDisabled;
    private boolean      isIncrease;
    private boolean      buttonsTogether;

    /**
     * Creates a new ScrollBarButtonPainter object.
     *
     * @param state DOCUMENT ME!
     */
    public ScrollBarButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.FIXED_SIZES);

        isPressed  = false;
        isDisabled = false;

        switch (state) {

        case FOREGROUND_DISABLED:
            isDisabled      = true;
            isIncrease      = false;
            buttonsTogether = false;
            break;

        case FOREGROUND_DISABLED_TOGETHER:
            isDisabled      = true;
            isIncrease      = false;
            buttonsTogether = true;
            break;

        case FOREGROUND_ENABLED:
            isIncrease      = false;
            buttonsTogether = false;
            break;

        case FOREGROUND_ENABLED_TOGETHER:
            isIncrease      = false;
            buttonsTogether = true;
            break;

        case FOREGROUND_PRESSED:
            isPressed       = true;
            isIncrease      = false;
            buttonsTogether = false;
            break;

        case FOREGROUND_PRESSED_TOGETHER:
            isPressed       = true;
            isIncrease      = false;
            buttonsTogether = true;
            break;

        case FOREGROUND_INCREASE_DISABLED:
            isDisabled      = true;
            isIncrease      = true;
            buttonsTogether = false;
            break;

        case FOREGROUND_INCREASE_DISABLED_TOGETHER:
            isDisabled      = true;
            isIncrease      = true;
            buttonsTogether = true;
            break;

        case FOREGROUND_INCREASE_ENABLED:
            isIncrease      = true;
            buttonsTogether = false;
            break;

        case FOREGROUND_INCREASE_ENABLED_TOGETHER:
            isIncrease      = true;
            buttonsTogether = true;
            break;

        case FOREGROUND_INCREASE_PRESSED:
            isPressed       = true;
            isIncrease      = true;
            buttonsTogether = false;
            break;

        case FOREGROUND_INCREASE_PRESSED_TOGETHER:
            isPressed       = true;
            isIncrease      = true;
            buttonsTogether = true;
            break;

        case FOREGROUND_CAP:
            isIncrease      = false;
            buttonsTogether = true;
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackgroundCap(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createScrollCap(0, 0, width, height);

        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, isIncrease, buttonsTogether);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackgroundApart(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createScrollButtonApart(0, 0, width, height);

        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, isIncrease, buttonsTogether);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackgroundTogetherDecrease(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createScrollButtonTogetherDecrease(0, 0, width, height);

        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, isIncrease, buttonsTogether);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackgroundTogetherIncrease(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createScrollButtonTogetherIncrease(0, 0, width, height);

        dropShadow.fill(g, s);
        fillScrollBarButtonInteriorColors(g, s, isIncrease, buttonsTogether);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g               DOCUMENT ME!
     * @param s               DOCUMENT ME!
     * @param isIncrease      DOCUMENT ME!
     * @param buttonsTogether DOCUMENT ME!
     */
    private void fillScrollBarButtonInteriorColors(Graphics2D g, Shape s, boolean isIncrease, boolean buttonsTogether) {
        g.setPaint(getScrollBarButtonBackgroundPaint(s, isIncrease, buttonsTogether));
        g.fill(s);

        int width = s.getBounds().width;

        g.setPaint(getScrollBarButtonLinePaint());
        g.drawLine(0, 0, width - 1, 0);

        if (state != Which.FOREGROUND_CAP && buttonsTogether) {
            int height = s.getBounds().height;

            g.setPaint(getScrollBarButtonDividerPaint(isIncrease));
            g.drawLine(width - 1, 1, width - 1, height - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintIncreaseButtonApart(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 5, height / 2.0 - 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintDecreaseButtonApart(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 4, height / 2.0 - 3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintIncreaseButtonTogether(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0 - 3, height / 2.0 - 3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintDecreaseButtonTogether(Graphics2D g, int width, int height) {
        paintArrowButton(g, width / 2.0, height / 2.0 - 3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    private void paintArrowButton(Graphics2D g, double x, double y) {
        Shape s = shapeGenerator.createArrowLeft(x, y, 4, 6);

        g.setPaint(getScrollBarButtonArrowPaint());
        g.fill(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s               DOCUMENT ME!
     * @param  isIncrease      DOCUMENT ME!
     * @param  buttonsTogether DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getScrollBarButtonBackgroundPaint(Shape s, boolean isIncrease, boolean buttonsTogether) {
        TwoColors colors = getScrollBarButtonBackgroundColors(buttonsTogether, isIncrease);

        return createHorizontalGradient(s, colors);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getScrollBarButtonLinePaint() {
        return getScrollBarButtonLineColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isIncrease DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getScrollBarButtonDividerPaint(boolean isIncrease) {
        return isIncrease ? scrollBarButtonLightDivider : scrollBarButtonDarkDivider;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getScrollBarButtonArrowPaint() {
        return getScrollBarButtonArrowColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  buttonsTogether DOCUMENT ME!
     * @param  isIncrease      DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private TwoColors getScrollBarButtonBackgroundColors(boolean buttonsTogether, boolean isIncrease) {
        if (state == Which.FOREGROUND_CAP) {
            return scrollBarCapColors;
        } else if (isPressed) {
            return isIncrease ? scrollBarButtonIncreasePressed : scrollBarButtonDecreasePressed;
        } else {

            if (buttonsTogether) {
                return isIncrease ? scrollBarButtonIncreaseTogether : scrollBarButtonDecreaseTogether;
            } else {
                return isIncrease ? scrollBarButtonIncreaseApart : scrollBarButtonDecreaseApart;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Color getScrollBarButtonLineColor() {
        if (isPressed) {
            return scrollBarButtonLinePressed;
        } else {
            return scrollBarButtonLine;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Color getScrollBarButtonArrowColor() {
        if (isDisabled) {
            return scrollBarButtonArrowDisabled;
        } else {
            return scrollBarButtonArrow;
        }
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    private static class ScrollButtonDropShadowEffect extends DropShadowEffect {

        /**
         * Creates a new ScrollButtonDropShadowEffect object.
         */
        public ScrollButtonDropShadowEffect() {
            color    = new Color(150, 150, 150);
            angle    = 0;
            distance = 0;
            size     = 3;
            opacity  = 0.25f;
        }
    }
}
