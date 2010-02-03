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
import java.awt.Paint;
import java.awt.Shape;

import com.seaglasslookandfeel.painter.util.PaintUtil.ButtonType;

public abstract class AbstractCommonColorsPainter extends AbstractRegionPainter {

    private Color      buttonBorderBaseEnabled               = decodeColor("buttonBorderBaseEnabled");
    private Color      buttonBorderBasePressed               = decodeColor("buttonBorderBasePressed");

    private Color      buttonInteriorBaseEnabled             = decodeColor("buttonInteriorBaseEnabled");
    private Color      buttonInteriorBasePressed             = decodeColor("buttonInteriorBasePressed");
    private Color      buttonInteriorBaseSelected            = decodeColor("buttonInteriorBaseSelected");
    private Color      buttonInteriorBasePressedSelected     = decodeColor("buttonInteriorBasePressedSelected");

    private Color      buttonBorderTopEnabled                = deriveColor(buttonBorderBaseEnabled, 0.002841f, -0.068681f, 0.062745f, 0);
    private Color      buttonBorderBottomEnabled             = deriveColor(buttonBorderBaseEnabled, -0.000801f, 0.082964f, -0.066667f, 0);
    private Color      buttonBorderTopPressed                = deriveColor(buttonBorderBasePressed, 0.003151f, -0.036649f, 0f, 0);
    private Color      buttonBorderBottomPressed             = deriveColor(buttonBorderBasePressed, -0.002987f, 0.047120f, 0f, 0);

    private TwoColors  buttonBorderEnabled                   = new TwoColors(buttonBorderTopEnabled, buttonBorderBottomEnabled);
    private TwoColors  buttonBorderPressed                   = new TwoColors(buttonBorderTopPressed, buttonBorderBottomPressed);
    private TwoColors  buttonBorderSelected                  = buttonBorderPressed;
    private TwoColors  buttonBorderPressedSelected           = buttonBorderPressed;
    private TwoColors  buttonBorderDefault                   = buttonBorderSelected;
    private TwoColors  buttonBorderDefaultPressed            = buttonBorderPressedSelected;
    private TwoColors  buttonBorderDisabled                  = disable(buttonBorderEnabled);
    private TwoColors  buttonBorderDisabledSelected          = buttonBorderDisabled;

    private Color      buttonInteriorTopEnabled              = deriveColor(buttonInteriorBaseEnabled, -0.017974f, -0.125841f, 0.027451f, 0);
    private Color      buttonInteriorUpperMidEnabled         = deriveColor(buttonInteriorBaseEnabled, -0.002101f, 0.00291f, 0.007843f, 0);
    private Color      buttonInteriorLowerMidEnabled         = deriveColor(buttonInteriorBaseEnabled, -0.003354f, 0.015574f, 0.003922f, 0);
    private Color      buttonInteriorBottomEnabled           = deriveColor(buttonInteriorBaseEnabled, -0.011029f, -0.106031f, 0.023529f, 0);
    private Color      buttonInteriorTopPressed              = deriveColor(buttonInteriorBasePressed, -0.005111f, -0.240902f, 0.086275f, 0);
    private Color      buttonInteriorUpperMidPressed         = deriveColor(buttonInteriorBasePressed, -0.008629f, 0.005016f, -0.027451f, 0);
    private Color      buttonInteriorLowerMidPressed         = deriveColor(buttonInteriorBasePressed, -0.008658f, 0f, 0f, 0);
    private Color      buttonInteriorBottomPressed           = deriveColor(buttonInteriorBasePressed, -0.027969f, -0.133277f, 0.164706f, 0);
    private Color      buttonInteriorTopSelected             = deriveColor(buttonInteriorBaseSelected, -0.008478f, -0.233526f, 0.05098f, 0);
    private Color      buttonInteriorUpperMidSelected        = deriveColor(buttonInteriorBaseSelected, -0.008234f, -0.009988f, -0.019608f,
                                                                 0);
    private Color      buttonInteriorLowerMidSelected        = deriveColor(buttonInteriorBaseSelected, -0.014034f, 0.002047f, 0.015686f, 0);
    private Color      buttonInteriorBottomSelected          = deriveColor(buttonInteriorBaseSelected, -0.022764f, -0.105923f, 0.141176f, 0);
    private Color      buttonInteriorTopPressedSelected      = deriveColor(buttonInteriorBasePressedSelected, -0.003339f, -0.246722f,
                                                                 0.047059f, 0);
    private Color      buttonInteriorUpperMidPressedSelected = deriveColor(buttonInteriorBasePressedSelected, 0.001453f, 0.001164f,
                                                                 -0.031373f, 0);
    private Color      buttonInteriorLowerMidPressedSelected = deriveColor(buttonInteriorBasePressedSelected, 0f, 0f, 0f, 0);
    private Color      buttonInteriorBottomPressedSelected   = deriveColor(buttonInteriorBasePressedSelected, -0.023428f, -0.154274f,
                                                                 0.12549f, 0);

    private FourColors buttonInteriorEnabled                 = new FourColors(buttonInteriorTopEnabled, buttonInteriorUpperMidEnabled,
                                                                 buttonInteriorLowerMidEnabled, buttonInteriorBottomEnabled);
    private FourColors buttonInteriorPressed                 = new FourColors(buttonInteriorTopPressed, buttonInteriorUpperMidPressed,
                                                                 buttonInteriorLowerMidPressed, buttonInteriorBottomPressed);
    private FourColors buttonInteriorSelected                = new FourColors(buttonInteriorTopSelected, buttonInteriorUpperMidSelected,
                                                                 buttonInteriorLowerMidSelected, buttonInteriorBottomSelected);
    private FourColors buttonInteriorPressedSelected         = new FourColors(buttonInteriorTopPressedSelected,
                                                                 buttonInteriorUpperMidPressedSelected,
                                                                 buttonInteriorLowerMidPressedSelected, buttonInteriorBottomPressedSelected);
    private FourColors buttonInteriorDefault                 = buttonInteriorSelected;
    private FourColors buttonInteriorDefaultPressed          = buttonInteriorPressedSelected;
    private FourColors buttonInteriorDisabled                = desaturate(buttonInteriorEnabled);
    private FourColors buttonInteriorDisabledSelected        = desaturate(buttonInteriorSelected);

    public AbstractCommonColorsPainter() {
        super();
    }

    public Paint getButtonBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getButtonBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getButtonInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getButtonInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    public TwoColors getButtonBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return buttonBorderDisabled;
        case DISABLED_SELECTED:
            return buttonBorderDisabledSelected;
        case ENABLED:
            return buttonBorderEnabled;
        case PRESSED:
            return buttonBorderPressed;
        case DEFAULT:
            return buttonBorderDefault;
        case DEFAULT_PRESSED:
            return buttonBorderDefaultPressed;
        case SELECTED:
            return buttonBorderSelected;
        case PRESSED_SELECTED:
            return buttonBorderPressedSelected;
        }
        return null;
    }

    public FourColors getButtonInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return buttonInteriorDisabled;
        case DISABLED_SELECTED:
            return buttonInteriorDisabledSelected;
        case ENABLED:
            return buttonInteriorEnabled;
        case PRESSED:
            return buttonInteriorPressed;
        case DEFAULT:
            return buttonInteriorDefault;
        case DEFAULT_PRESSED:
            return buttonInteriorDefaultPressed;
        case SELECTED:
            return buttonInteriorSelected;
        case PRESSED_SELECTED:
            return buttonInteriorPressedSelected;
        }
        return null;
    }
}
