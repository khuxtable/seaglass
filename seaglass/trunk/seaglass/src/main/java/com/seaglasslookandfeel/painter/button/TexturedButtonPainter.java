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
package com.seaglasslookandfeel.painter.button;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;

import com.seaglasslookandfeel.painter.ButtonPainter.Which;

/**
 * Paint a "textured" button. This is suitable for placing on darker grey
 * backgrounds such as toolbars and bottom bars.
 *
 * @author Kathryn Huxtable
 */
public class TexturedButtonPainter extends SegmentedButtonPainter {

    private Color texturedButtonBorderBaseEnabled = decodeColor("texturedButtonBorderBaseEnabled");

    private Color texturedButtonInteriorBaseEnabled         = decodeColor("texturedButtonInteriorBaseEnabled");
    private Color texturedButtonInteriorBasePressed         = decodeColor("texturedButtonInteriorBasePressed");
    private Color texturedButtonInteriorBaseSelected        = decodeColor("texturedButtonInteriorBaseSelected");
    private Color texturedButtonInteriorBasePressedSelected = decodeColor("texturedButtonInteriorBasePressedSelected");

    private Color texturedButtonBorderTopEnabled    = texturedButtonBorderBaseEnabled;
    private Color texturedButtonBorderBottomEnabled = texturedButtonBorderBaseEnabled;

    private TwoColors texturedButtonBorderEnabled          = new TwoColors(texturedButtonBorderTopEnabled,
                                                                           texturedButtonBorderBottomEnabled);
    private TwoColors texturedButtonBorderPressed          = super.getCommonBorderColors(CommonControlState.PRESSED);
    private TwoColors texturedButtonBorderDefault          = texturedButtonBorderPressed;
    private TwoColors texturedButtonBorderSelected         = texturedButtonBorderDefault;
    private TwoColors texturedButtonBorderDefaultPressed   = texturedButtonBorderPressed;
    private TwoColors texturedButtonBorderPressedSelected  = texturedButtonBorderDefaultPressed;
    private TwoColors texturedButtonBorderDisabled         = disable(texturedButtonBorderEnabled);
    private TwoColors texturedButtonBorderDisabledSelected = disable(texturedButtonBorderPressed);

    private Color texturedButtonInteriorTopEnabled            = deriveColor(texturedButtonInteriorBaseEnabled, 0f, 0f, 0.054902f, 0);
    private Color texturedButtonInteriorBottomEnabled         = deriveColor(texturedButtonInteriorBaseEnabled, 0f, 0f, -0.062745f, 0);
    private Color texturedButtonInteriorTopPressed            = deriveColor(texturedButtonInteriorBasePressed, -0.003064f, -0.044334f,
                                                                            0.074510f, 0);
    private Color texturedButtonInteriorBottomPressed         = deriveColor(texturedButtonInteriorBasePressed, 0.002723f, 0.057143f,
                                                                            -0.082353f, 0);
    private Color texturedButtonInteriorTopSelected           = deriveColor(texturedButtonInteriorBaseSelected, -0.007658f, -0.042881f,
                                                                            0.078431f, 0);
    private Color texturedButtonInteriorBottomSelected        = deriveColor(texturedButtonInteriorBaseSelected, 0.006872f, 0.053054f,
                                                                            -0.082353f, 0);
    private Color texturedButtonInteriorTopPressedSelected    = deriveColor(texturedButtonInteriorBasePressedSelected, -0.007749f,
                                                                            -0.043169f, 0.070588f, 0);
    private Color texturedButtonInteriorBottomPressedSelected = deriveColor(texturedButtonInteriorBasePressedSelected, 0.007011f,
                                                                            0.056937f, -0.078431f, 0);

    private TwoColors texturedButtonInteriorEnabled          = new TwoColors(texturedButtonInteriorTopEnabled,
                                                                             texturedButtonInteriorBottomEnabled);
    private TwoColors texturedButtonInteriorPressed          = new TwoColors(texturedButtonInteriorTopPressed,
                                                                             texturedButtonInteriorBottomPressed);
    private TwoColors texturedButtonInteriorDefault          = new TwoColors(texturedButtonInteriorTopSelected,
                                                                             texturedButtonInteriorBottomSelected);
    private TwoColors texturedButtonInteriorSelected         = texturedButtonInteriorDefault;
    private TwoColors texturedButtonInteriorDefaultPressed   = texturedButtonInteriorDefault;
    private TwoColors texturedButtonInteriorPressedSelected  = new TwoColors(texturedButtonInteriorTopPressedSelected,
                                                                             texturedButtonInteriorBottomPressedSelected);
    private TwoColors texturedButtonInteriorDisabled         = desaturate(texturedButtonInteriorEnabled);
    private TwoColors texturedButtonInteriorDisabledSelected = desaturate(texturedButtonInteriorDefault);

    /**
     * Create a textured button painter.
     *
     * @param state the button state.
     * @param ctx   the paint context.
     */
    public TexturedButtonPainter(Which state, PaintContext ctx) {
        super(state, ctx);
    }

    /**
     * {@inheritDoc}
     */
    public Paint getCommonBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getTexturedButtonBorderColors(type);

        return createVerticalGradient(s, new TwoColors(colors.top, colors.bottom));
    }

    /**
     * {@inheritDoc}
     */
    public Paint getCommonInteriorPaint(Shape s, CommonControlState type) {
        TwoColors colors = getTexturedButtonInteriorColors(type);

        return createVerticalGradient(s, new TwoColors(colors.top, colors.bottom));
    }

    /**
     * Get the colors for the border.
     *
     * @param  type the button type. Derived from state.
     *
     * @return a two-tuple of colors for top and bottom gradient.
     */
    public TwoColors getTexturedButtonBorderColors(CommonControlState type) {
        switch (type) {

        case DISABLED:
            return texturedButtonBorderDisabled;

        case DISABLED_SELECTED:
            return texturedButtonBorderDisabledSelected;

        case ENABLED:
            return texturedButtonBorderEnabled;

        case PRESSED:
            return texturedButtonBorderPressed;

        case DEFAULT:
            return texturedButtonBorderDefault;

        case SELECTED:
            return texturedButtonBorderSelected;

        case DEFAULT_PRESSED:
            return texturedButtonBorderDefaultPressed;

        case PRESSED_SELECTED:
            return texturedButtonBorderPressedSelected;
        }

        return null;
    }

    /**
     * Get the colors for the interior.
     *
     * @param  type the button type. Derived from the state.
     *
     * @return a two-tuple of colors for top and bottom gradient.
     */
    public TwoColors getTexturedButtonInteriorColors(CommonControlState type) {
        switch (type) {

        case DISABLED:
            return texturedButtonInteriorDisabled;

        case DISABLED_SELECTED:
            return texturedButtonInteriorDisabledSelected;

        case ENABLED:
            return texturedButtonInteriorEnabled;

        case PRESSED:
            return texturedButtonInteriorPressed;

        case DEFAULT:
            return texturedButtonInteriorDefault;

        case SELECTED:
            return texturedButtonInteriorSelected;

        case DEFAULT_PRESSED:
            return texturedButtonInteriorDefaultPressed;

        case PRESSED_SELECTED:
            return texturedButtonInteriorPressedSelected;
        }

        return null;
    }
}
