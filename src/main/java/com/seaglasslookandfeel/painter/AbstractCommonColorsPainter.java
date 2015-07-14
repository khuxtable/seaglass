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

/**
 * DOCUMENT ME!
 *
 * @author  $author$
 * @version $Revision$, $Date$
 */
public abstract class AbstractCommonColorsPainter extends AbstractRegionPainter {

    /**
     * Common control color states.
     */
    public enum CommonControlState {
        ENABLED, PRESSED, DEFAULT, DEFAULT_PRESSED, DISABLED, DISABLED_SELECTED, SELECTED, PRESSED_SELECTED,
    }

    private Color arrowEnabled  = decodeColor("buttonArrow");
    private Color arrowDisabled = desaturate(arrowEnabled);

    private Color borderBaseEnabled = decodeColor("buttonBorderBaseEnabled");
    private Color borderBasePressed = decodeColor("buttonBorderBasePressed");

    private Color interiorBaseEnabled         = decodeColor("buttonInteriorBaseEnabled");
    private Color interiorBasePressed         = decodeColor("buttonInteriorBasePressed");
    private Color interiorBaseSelected        = decodeColor("buttonInteriorBaseSelected");
    private Color interiorBasePressedSelected = decodeColor("buttonInteriorBasePressedSelected");

    private Color borderTopEnabled    = deriveColor(borderBaseEnabled, 0.002841f, -0.068681f, 0.062745f, 0);
    private Color borderBottomEnabled = deriveColor(borderBaseEnabled, -0.000801f, 0.082964f, -0.066667f, 0);
    private Color borderTopPressed    = deriveColor(borderBasePressed, 0.003151f, -0.036649f, 0f, 0);
    private Color borderBottomPressed = deriveColor(borderBasePressed, -0.002987f, 0.047120f, 0f, 0);

    private TwoColors borderEnabled          = new TwoColors(borderTopEnabled, borderBottomEnabled);
    private TwoColors borderPressed          = new TwoColors(borderTopPressed, borderBottomPressed);
    private TwoColors borderSelected         = borderPressed;
    private TwoColors borderPressedSelected  = borderPressed;
    private TwoColors borderDefault          = borderSelected;
    private TwoColors borderDefaultPressed   = borderPressedSelected;
    private TwoColors borderDisabled         = disable(borderEnabled);
    private TwoColors borderDisabledSelected = borderDisabled;

    private Color interiorTopEnabled              = deriveColor(interiorBaseEnabled, -0.017974f, -0.125841f, 0.027451f, 0);
    private Color interiorUpperMidEnabled         = deriveColor(interiorBaseEnabled, -0.002101f, 0.00291f, 0.007843f, 0);
    private Color interiorLowerMidEnabled         = deriveColor(interiorBaseEnabled, -0.003354f, 0.015574f, 0.003922f, 0);
    private Color interiorBottomEnabled           = deriveColor(interiorBaseEnabled, -0.011029f, -0.106031f, 0.023529f, 0);
    private Color interiorTopPressed              = deriveColor(interiorBasePressed, -0.005111f, -0.240902f, 0.086275f, 0);
    private Color interiorUpperMidPressed         = deriveColor(interiorBasePressed, -0.008629f, 0.005016f, -0.027451f, 0);
    private Color interiorLowerMidPressed         = deriveColor(interiorBasePressed, -0.008658f, 0f, 0f, 0);
    private Color interiorBottomPressed           = deriveColor(interiorBasePressed, -0.027969f, -0.133277f, 0.164706f, 0);
    private Color interiorTopSelected             = deriveColor(interiorBaseSelected, -0.008478f, -0.233526f, 0.05098f, 0);
    private Color interiorUpperMidSelected        = deriveColor(interiorBaseSelected, -0.008234f, -0.009988f, -0.019608f, 0);
    private Color interiorLowerMidSelected        = deriveColor(interiorBaseSelected, -0.014034f, 0.002047f, 0.015686f, 0);
    private Color interiorBottomSelected          = deriveColor(interiorBaseSelected, -0.022764f, -0.105923f, 0.141176f, 0);
    private Color interiorTopPressedSelected      = deriveColor(interiorBasePressedSelected, -0.003339f, -0.246722f, 0.047059f, 0);
    private Color interiorUpperMidPressedSelected = deriveColor(interiorBasePressedSelected, 0.001453f, 0.001164f, -0.031373f, 0);
    private Color interiorLowerMidPressedSelected = deriveColor(interiorBasePressedSelected, 0f, 0f, 0f, 0);
    private Color interiorBottomPressedSelected   = deriveColor(interiorBasePressedSelected, -0.023428f, -0.154274f, 0.12549f, 0);

    private FourColors interiorEnabled          = new FourColors(interiorTopEnabled, interiorUpperMidEnabled, interiorLowerMidEnabled,
                                                                 interiorBottomEnabled);
    private FourColors interiorPressed          = new FourColors(interiorTopPressed, interiorUpperMidPressed, interiorLowerMidPressed,
                                                                 interiorBottomPressed);
    private FourColors interiorSelected         = new FourColors(interiorTopSelected, interiorUpperMidSelected, interiorLowerMidSelected,
                                                                 interiorBottomSelected);
    private FourColors interiorPressedSelected  = new FourColors(interiorTopPressedSelected, interiorUpperMidPressedSelected,
                                                                 interiorLowerMidPressedSelected, interiorBottomPressedSelected);
    private FourColors interiorDefault          = interiorSelected;
    private FourColors interiorDefaultPressed   = interiorPressedSelected;
    private FourColors interiorDisabled         = desaturate(interiorEnabled);
    private FourColors interiorDisabledSelected = desaturate(interiorSelected);

    private Color textBorderEnabled        = decodeColor("seaGlassTextEnabledBorder");
    private Color textBorderEnabledToolbar = decodeColor("seaGlassTextEnabledToolbarBorder");
    private Color textBorderDisabled       = decodeColor("seaGlassTextDisabledBorder");

    /**
     * Creates a new AbstractCommonColorsPainter object.
     */
    public AbstractCommonColorsPainter() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s    DOCUMENT ME!
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getCommonArrowPaint(Shape s, CommonControlState type) {
        return getCommonArrowColors(type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s    DOCUMENT ME!
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getCommonBorderPaint(Shape s, CommonControlState type) {
        TwoColors colors = getCommonBorderColors(type);

        return createVerticalGradient(s, colors);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s    DOCUMENT ME!
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getCommonInteriorPaint(Shape s, CommonControlState type) {
        FourColors colors = getCommonInteriorColors(type);

        return createVerticalGradient(s, colors);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getCommonArrowColors(CommonControlState type) {
        switch (type) {

        case DISABLED:
            return arrowDisabled;

        case ENABLED:
        case PRESSED:
            return arrowEnabled;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TwoColors getCommonBorderColors(CommonControlState type) {
        switch (type) {

        case DISABLED:
            return borderDisabled;

        case DISABLED_SELECTED:
            return borderDisabledSelected;

        case ENABLED:
            return borderEnabled;

        case PRESSED:
            return borderPressed;

        case DEFAULT:
            return borderDefault;

        case DEFAULT_PRESSED:
            return borderDefaultPressed;

        case SELECTED:
            return borderSelected;

        case PRESSED_SELECTED:
            return borderPressedSelected;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FourColors getCommonInteriorColors(CommonControlState type) {
        switch (type) {

        case DISABLED:
            return interiorDisabled;

        case DISABLED_SELECTED:
            return interiorDisabledSelected;

        case ENABLED:
            return interiorEnabled;

        case PRESSED:
            return interiorPressed;

        case DEFAULT:
            return interiorDefault;

        case DEFAULT_PRESSED:
            return interiorDefaultPressed;

        case SELECTED:
            return interiorSelected;

        case PRESSED_SELECTED:
            return interiorPressedSelected;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type      DOCUMENT ME!
     * @param  inToolbar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getTextBorderPaint(CommonControlState type, boolean inToolbar) {
        if (type == CommonControlState.DISABLED) {
            return textBorderDisabled;
        } else if (inToolbar) {
            return textBorderEnabledToolbar;
        } else {
            return textBorderEnabled;
        }
    }
}
