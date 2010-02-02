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
package com.seaglasslookandfeel.painter.util;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.UIManager;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * @author Kathryn Huxtable
 */
public class PaintUtil {

    public enum ButtonType {
        ENABLED, PRESSED, DEFAULT, DEFAULT_PRESSED, DISABLED, DISABLED_SELECTED, SELECTED, PRESSED_SELECTED, ACTIVE, INACTIVE, SCROLL_CAP,
    }

    public enum FocusType {
        INNER_FOCUS, OUTER_FOCUS,
    }

    public enum ToolbarToggleButtonType {
        INNER, INNER_EDGE, OUTER_EDGE
    }

    private static Color       transparentColor;

    private static Color       outerFocus;
    private static Color       innerFocus;
    private static Color       outerToolBarFocus;
    private static Color       innerToolBarFocus;

    private static TwoColors   innerShadow;

    private static TwoColors   buttonBorderEnabled;
    private static TwoColors   buttonBorderPressed;
    private static TwoColors   buttonBorderDefault;
    private static TwoColors   buttonBorderDefaultPressed;
    private static TwoColors   buttonBorderSelected;
    private static TwoColors   buttonBorderPressedSelected;
    private static TwoColors   buttonBorderDisabled;
    private static TwoColors   buttonBorderDisabledSelected;

    private static FourColors  buttonInteriorEnabled;
    private static FourColors  buttonInteriorPressed;
    private static FourColors  buttonInteriorDefault;
    private static FourColors  buttonInteriorDefaultPressed;
    private static FourColors  buttonInteriorSelected;
    private static FourColors  buttonInteriorPressedSelected;
    private static FourColors  buttonInteriorDisabled;
    private static FourColors  buttonInteriorDisabledSelected;

    private static TwoColors   texturedButtonBorderEnabled;
    private static TwoColors   texturedButtonBorderPressed;
    private static TwoColors   texturedButtonBorderDefault;
    private static TwoColors   texturedButtonBorderDefaultPressed;
    private static TwoColors   texturedButtonBorderSelected;
    private static TwoColors   texturedButtonBorderPressedSelected;
    private static TwoColors   texturedButtonBorderDisabled;
    private static TwoColors   texturedButtonBorderDisabledSelected;

    private static FourColors  texturedButtonInteriorEnabled;
    private static FourColors  texturedButtonInteriorPressed;
    private static FourColors  texturedButtonInteriorDefault;
    private static FourColors  texturedButtonInteriorDefaultPressed;
    private static FourColors  texturedButtonInteriorSelected;
    private static FourColors  texturedButtonInteriorPressedSelected;
    private static FourColors  texturedButtonInteriorDisabled;
    private static FourColors  texturedButtonInteriorDisabledSelected;

    private static TwoColors   scrollBarThumbBorderPressed;
    private static FourColors  scrollBarThumbInteriorPressed;

    private static TwoColors   buttonBulletEnabled;
    private static TwoColors   buttonbulletDisabled;

    private static TwoColors   comboBoxButtonBorderDisabled;
    private static TwoColors   comboBoxButtonBorderEnabled;
    private static TwoColors   comboBoxButtonBorderPressed;

    private static FourColors  comboBoxButtonInteriorDisabled;
    private static FourColors  comboBoxButtonInteriorEnabled;
    private static FourColors  comboBoxButtonInteriorPressed;

    private static TwoColors   comboBoxBackgroundBorderDisabled;
    private static TwoColors   comboBoxBackgroundBorderEnabled;
    private static TwoColors   comboBoxBackgroundBorderPressed;

    private static FourColors  comboBoxBackgroundInteriorDisabled;
    private static FourColors  comboBoxBackgroundInteriorEnabled;
    private static FourColors  comboBoxBackgroundInteriorPressed;

    private static TwoColors   rootPaneActive;
    private static TwoColors   rootPaneInactive;

    private static Color       frameBorderActive;
    private static Color       frameBorderInactive;
    private static Color       frameInnerHighlightInactive;
    private static Color       frameInnerHighlightActive;

    private static FourColors  frameActive;
    private static FourColors  frameInactive;

    private static Color       desktopPane;

    private static TwoColors   menuItemBackground;
    private static Color       menuItemBottomLine;

    private static Color       popupMenuBorderDisabled;
    private static Color       popupMenuBorderEnabled;

    private static Color       popupMenuInteriorDisabled;
    private static Color       popupMenuInteriorEnabled;

    private static Color       popupMenuSeparator;

    private static TwoColors   progressBarTrackDisabled;
    private static Color       progressBarTrackInteriorDisabled;

    private static TwoColors   progressBarTrackEnabled;
    private static Color       progressBarTrackInteriorEnabled;

    private static FourColors  progressBarDisabled;
    private static FourColors  progressBarEnabled;
    private static FourColors  progressBarIndeterminatePatternDisabled;
    private static FourColors  progressBarIndeterminatePatternEnabled;
    private static Color       progressBarEndDisabled;
    private static Color       progressBarEndEnabled;

    private static TwoColors   scrollBarTrackBackground;
    private static FourColors  scrollBarTrackGradient;

    private static TwoColors   scrollBarCapColors;

    private static TwoColors   scrollBarButtonIncreaseApart;
    private static TwoColors   scrollBarButtonIncreaseTogether;
    private static TwoColors   scrollBarButtonIncreasePressed;

    private static TwoColors   scrollBarButtonDecreaseApart;
    private static TwoColors   scrollBarButtonDecreaseTogether;
    private static TwoColors   scrollBarButtonDecreasePressed;

    private static Color       scrollBarButtonLine;
    private static Color       scrollBarButtonLinePressed;
    private static Color       scrollBarButtonArrow;
    private static Color       scrollBarButtonArrowDisabled;

    private static Color       scrollBarButtonDarkDivider;
    private static Color       scrollBarButtonLightDivider;

    private static TwoColors   sliderTrackBorderDisabled;
    private static TwoColors   sliderTrackInteriorDisabled;

    private static TwoColors   sliderTrackBorderEnabled;
    private static TwoColors   sliderTrackInteriorEnabled;

    private static TwoColors   spinnerNextBorderDisabled;
    private static TwoColors   spinnerNextBorderEnabled;
    private static TwoColors   spinnerNextBorderPressed;

    private static TwoColors   spinnerNextInteriorDisabled;
    private static TwoColors   spinnerNextInteriorEnabled;
    private static TwoColors   spinnerNextInteriorPressed;

    private static TwoColors   spinnerPrevBorderDisabled;
    private static TwoColors   spinnerPrevBorderEnabled;
    private static TwoColors   spinnerPrevBorderPressed;

    private static TwoColors   spinnerPrevInteriorDisabled;
    private static TwoColors   spinnerPrevInteriorEnabled;
    private static TwoColors   spinnerPrevInteriorPressed;

    private static Color       spinnerPrevTopLineDisabled;
    private static Color       spinnerPrevTopLineEnabled;
    private static Color       spinnerPrevTopLinePressed;

    private static Color       spinnerArrowDisabled;
    private static Color       spinnerArrowEnabled;

    private static Color       splitPaneDividerBackgroundOuter;
    private static Color       splitPaneDividerBackgroundEnabled;
    private static TwoColors   splitPaneDividerBorder;
    private static ThreeColors splitPaneDividerInterior;

    private static Color       tabbedPaneTabAreaBackLineEnabled;
    private static Color       tabbedPaneTabAreaBackLineDisabled;
    private static Color       tabbedPaneTabAreaLightShadow;
    private static Color       tabbedPaneTabAreaDarkShadow;

    private static Color       textComponentBorderDisabled;
    private static Color       textComponentBorderEnabled;
    private static Color       textComponentBorderEnabledToolbar;

    private static Color       tableHeaderBorderEnabled;
    private static Color       tableHeaderBorderDisabled;

    private static Color       tableHeaderSortIndicator;

    private static FourColors  tableHeaderDisabled;
    private static FourColors  tableHeaderEnabled;
    private static FourColors  tableHeaderSorted;
    private static FourColors  tableHeaderPressed;
    private static FourColors  tableHeaderDisabledSorted;

    private static Color       toolbarHandleMac;
    private static TwoColors   toolbarHandleBorder;
    private static FourColors  toolbarHandleInterior;

    private static TwoColors   toolbarToggleButtonInner;
    private static TwoColors   toolbarToggleButtonInnerEdge;
    private static TwoColors   toolbarToggleButtonOuterEdge;

    static {
        transparentColor = decodeColor("seaGlassTransparent");

        outerFocus = decodeColor("seaGlassOuterFocus");
        innerFocus = decodeColor("seaGlassFocus");
        outerToolBarFocus = decodeColor("seaGlassToolBarOuterFocus");
        innerToolBarFocus = decodeColor("seaGlassToolBarFocus");

        desktopPane = decodeColor("seaGlassDesktopPane");

        rootPaneActive = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"), decodeColor("seaGlassToolBarActiveBottomB"));
        rootPaneInactive = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"), decodeColor("seaGlassToolBarInactiveBottomB"));

        splitPaneDividerBackgroundEnabled = decodeColor("control");

        Color black = Color.BLACK;
        Color black10 = new Color(0x10000000, true);
        Color black12 = new Color(0x12000000, true);
        Color black15 = new Color(0x15000000, true);
        Color black1f = new Color(0x1f000000, true);
        Color black20 = new Color(0x20000000, true);
        Color black28 = new Color(0x28000000, true);
        Color black33 = new Color(0x33000000, true);
        Color black40 = new Color(0x40000000, true);

        Color gray33 = new Color(0x333333);
        Color gray54 = new Color(0x545454);
        Color gray55 = new Color(0x555555);
        Color gray63 = new Color(0x636363);
        Color gray90 = new Color(0x909090);
        Color gray99 = new Color(0x999999);
        Color grayae = new Color(0xaeaeae);
        Color grayb4 = new Color(0xb4b4b4);
        Color graybb = new Color(0xbbbbbb);
        Color graybd = new Color(0xbdbdbd);
        Color grayc4 = new Color(0xc4c4c4);
        Color graycc = new Color(0xcccccc);
        Color grayd1 = new Color(0xd1d1d1);
        Color grayd3 = new Color(0xd3d3d3);
        Color grayd9 = new Color(0xd9d9d9);
        Color graydd = new Color(0xdddddd);
        Color graye0 = new Color(0xe0e0e0);
        Color graye5 = new Color(0xe5e5e5);
        Color graye9 = new Color(0xe9e9e9);
        Color grayeb = new Color(0xebebeb);
        Color grayec = new Color(0xececec);
        Color grayed = new Color(0xededed);
        Color grayee = new Color(0xeeeeee);
        Color grayf2 = new Color(0xf2f2f2);
        Color grayfe = new Color(0xfefefe);

        Color white = Color.WHITE;
        Color white55 = new Color(0x55ffffff, true);
        Color white3f = new Color(0x3fffffff, true);

        Color innerShadowDark = black20;
        Color innerShadowLight = black10;

        Color buttonBorderTopEnabled = new Color(0x88ade0);
        Color buttonBorderBottomEnabled = new Color(0x5785bf);
        Color buttonBorderTopPressed = new Color(0x4f7bbf);
        Color buttonBorderBottomPressed = new Color(0x3f76bf);

        Color scrollBarThumbBorderTopPressed = new Color(0x4076bf);
        Color scrollBarThumbBorderBottomPressed = buttonBorderTopPressed;

        Color buttonInteriorTopEnabled = new Color(0xfbfdfe);
        Color buttonInteriorUpperMidEnabled = new Color(0xd6eaf9);
        Color buttonInteriorLowerMidEnabled = new Color(0xd2e8f8);
        Color buttonInteriorBottomEnabled = new Color(0xf5fafd);
        Color buttonInteriorTopPressed = new Color(0xacbdd0);
        Color buttonInteriorUpperMidPressed = new Color(0x688db3);
        Color buttonInteriorLowerMidPressed = new Color(0x6d93ba);
        Color buttonInteriorBottomPressed = new Color(0xa4cbe4);
        Color buttonInteriorTopSelected = new Color(0xbccedf);
        Color buttonInteriorUpperMidSelected = new Color(0x7fa7cd);
        Color buttonInteriorLowerMidSelected = new Color(0x82b0d6);
        Color buttonInteriorBottomSelected = new Color(0xb0daf6);
        Color buttonInteriorTopPressedSelected = new Color(0xaebdce);
        Color buttonInteriorUpperMidPressedSelected = new Color(0x6f90ba);
        Color buttonInteriorLowerMidPressedSelected = new Color(0x7497c2);
        Color buttonInteriorBottomPressedSelected = new Color(0xaacbe2);

        Color scrollBarThumbInteriorTopPressed = new Color(0xb1dbf5);
        Color scrollBarThumbInteriorUpperMidPressed = new Color(0x7ca7ce);
        Color scrollBarThumbInteriorLowerMidPressed = new Color(0x7ea7cc);
        Color scrollBarThumbInteriorBottomPressed = new Color(0xbbcedf);

        Color texturedButtonBorderTopEnabled = gray99;
        Color texturedButtonBorderBottomEnabled = gray99;

        Color texturedButtonInteriorTopEnabled = grayfe;
        Color texturedButtonInteriorUpperMidEnabled = grayf2;
        Color texturedButtonInteriorLowerMidEnabled = grayec;
        Color texturedButtonInteriorBottomEnabled = graye0;
        Color texturedButtonInteriorTopPressed = new Color(0xa5c9e5);
        Color texturedButtonInteriorUpperMidPressed = new Color(0x90b5d4);
        Color texturedButtonInteriorLowerMidPressed = new Color(0x86abcb);
        Color texturedButtonInteriorBottomPressed = new Color(0x759bbd);
        Color texturedButtonInteriorTopSelected = new Color(0xb0daf6);
        Color texturedButtonInteriorUpperMidSelected = new Color(0x88b3d8);
        Color texturedButtonInteriorLowerMidSelected = new Color(0x7da7cd);
        Color texturedButtonInteriorBottomSelected = new Color(0x7fa7cd);

        Color texturedButtonInteriorTopPressedSelected = new Color(0x93b5cc);
        Color texturedButtonInteriorUpperMidPressedSelected = new Color(0x7094b3);
        Color texturedButtonInteriorLowerMidPressedSelected = new Color(0x6588a6);
        Color texturedButtonInteriorBottomPressedSelected = new Color(0x6787a6);

        Color buttonBulletTopEnabled = gray33;
        Color buttonBulletBottomEnabled = black;

        Color frameTopActive = new Color(0xafbecf);
        Color frameUpperMidActive = new Color(0x96adc4);
        Color frameLowerMidActive = new Color(0x96adc4);
        Color frameBottomActive = new Color(0x8ea7c0);
        Color frameTopInactive = grayed;
        Color frameUpperMidInactive = graye0;
        Color frameLowerMidInactive = graye0;
        Color frameBottomInactive = grayd3;

        Color scrollBarButtonTopPressed = new Color(0xcee2f5);
        Color scrollBarButtonMiddlePressed = new Color(0xb1cbe4);
        Color scrollBarButtonBottomPressed = new Color(0x8fb1d1);
        Color scrollBarButtonLinePressedColor = new Color(0x82abd0);

        frameBorderActive = gray54;
        frameBorderInactive = gray54;

        frameInnerHighlightInactive = white55;
        frameInnerHighlightActive = white55;

        Color menuItemBackgroundTop = new Color(0x6a90b6);
        Color menuItemBackgroundBottom = new Color(0x4a6b90);

        Color progressBarEndLine = new Color(0x4076bf);

        menuItemBottomLine = new Color(0x3a5d89);

        popupMenuBorderEnabled = graydd;
        popupMenuInteriorEnabled = white;

        popupMenuSeparator = graydd;

        progressBarTrackInteriorEnabled = white;

        Color scrollBarTrackBackgroundTop = grayee;
        Color scrollBarTrackBackgroundBottom = white;
        Color scrollBarTrackGradientTop = black33;
        Color scrollBarTrackGradientUpperMid = black15;
        Color scrollBarTrackGradientLowerMid = transparentColor;
        Color scrollBarTrackGradientBottom = black12;
        Color scrollBarCapTop = white;
        Color scrollBarCapBottom = graybb;

        toolbarHandleMac = new Color(0xc8191919, true);

        Color spinnerNextBorderBottomEnabled = new Color(0x4779bf);
        Color spinnerNextBorderBottomPressed = new Color(0x4879bf);
        Color spinnerNextInteriorBottomEnabled = new Color(0x85abcf);
        Color spinnerNextInteriorBottomPressed = new Color(0x6e92b6);

        Color spinnerPrevBorderTopEnabled = new Color(0x4778bf);

        Color spinnerPrevInteriorTopEnabled = new Color(0x81aed4);
        Color spinnerPrevInteriorBottomEnabled = new Color(0xaad4f1);
        Color spinnerPrevInteriorPressedTop = new Color(0x6c91b8);
        Color spinnerPrevInteriorPressedBottom = new Color(0x9cc3de);

        spinnerPrevTopLineEnabled = new Color(0xacc8e0);
        spinnerPrevTopLinePressed = new Color(0x9eb6cf);

        splitPaneDividerBackgroundOuter = grayd9;

        tabbedPaneTabAreaBackLineEnabled = new Color(0x647595);
        tabbedPaneTabAreaLightShadow = new Color(0x55eeeeee, true);
        tabbedPaneTabAreaDarkShadow = new Color(0x55aaaaaa, true);

        tableHeaderBorderEnabled = new Color(0xcad3e0);
        tableHeaderSortIndicator = new Color(0xc02a5481, true);

        Color tableHeaderUpperMidEnabled = new Color(0xeaeff2);
        Color tableHeaderLowerMidEnabled = new Color(0xeff3f7);

        // ------- Assign from base colors -------

        innerShadow = new TwoColors(innerShadowDark, innerShadowLight);

        buttonBorderEnabled = new TwoColors(buttonBorderTopEnabled, buttonBorderBottomEnabled);
        buttonBorderPressed = new TwoColors(buttonBorderTopPressed, buttonBorderBottomPressed);
        buttonBorderSelected = buttonBorderPressed;
        buttonBorderPressedSelected = buttonBorderPressed;
        buttonBorderDefault = buttonBorderSelected;
        buttonBorderDefaultPressed = buttonBorderPressedSelected;
        buttonBorderDisabled = disable(buttonBorderEnabled);
        buttonBorderDisabledSelected = buttonBorderDisabled;

        comboBoxButtonBorderDisabled = buttonBorderDisabled;
        comboBoxButtonBorderEnabled = buttonBorderPressed;
        comboBoxButtonBorderPressed = buttonBorderPressed;

        comboBoxBackgroundBorderDisabled = buttonBorderDisabled;
        comboBoxBackgroundBorderEnabled = buttonBorderEnabled;
        comboBoxBackgroundBorderPressed = buttonBorderEnabled;

        buttonInteriorEnabled = new FourColors(buttonInteriorTopEnabled, buttonInteriorUpperMidEnabled, buttonInteriorLowerMidEnabled,
            buttonInteriorBottomEnabled);
        buttonInteriorPressed = new FourColors(buttonInteriorTopPressed, buttonInteriorUpperMidPressed, buttonInteriorLowerMidPressed,
            buttonInteriorBottomPressed);
        buttonInteriorSelected = new FourColors(buttonInteriorTopSelected, buttonInteriorUpperMidSelected, buttonInteriorLowerMidSelected,
            buttonInteriorBottomSelected);
        buttonInteriorPressedSelected = new FourColors(buttonInteriorTopPressedSelected, buttonInteriorUpperMidPressedSelected,
            buttonInteriorLowerMidPressedSelected, buttonInteriorBottomPressedSelected);
        buttonInteriorDefault = buttonInteriorSelected;
        buttonInteriorDefaultPressed = buttonInteriorPressedSelected;
        buttonInteriorDisabled = desaturate(buttonInteriorEnabled);
        buttonInteriorDisabledSelected = desaturate(buttonInteriorSelected);

        texturedButtonBorderEnabled = new TwoColors(texturedButtonBorderTopEnabled, texturedButtonBorderBottomEnabled);
        texturedButtonBorderPressed = new TwoColors(buttonBorderTopPressed, buttonBorderBottomPressed);
        texturedButtonBorderDefault = texturedButtonBorderPressed;
        texturedButtonBorderSelected = texturedButtonBorderDefault;
        texturedButtonBorderDefaultPressed = texturedButtonBorderPressed;
        texturedButtonBorderPressedSelected = texturedButtonBorderDefaultPressed;
        texturedButtonBorderDisabled = disable(texturedButtonBorderEnabled);
        texturedButtonBorderDisabledSelected = disable(texturedButtonBorderPressed);

        texturedButtonInteriorEnabled = new FourColors(texturedButtonInteriorTopEnabled, texturedButtonInteriorUpperMidEnabled,
            texturedButtonInteriorLowerMidEnabled, texturedButtonInteriorBottomEnabled);
        texturedButtonInteriorPressed = new FourColors(texturedButtonInteriorTopPressed, texturedButtonInteriorUpperMidPressed,
            texturedButtonInteriorLowerMidPressed, texturedButtonInteriorBottomPressed);
        texturedButtonInteriorDefault = new FourColors(texturedButtonInteriorTopSelected, texturedButtonInteriorUpperMidSelected,
            texturedButtonInteriorLowerMidSelected, texturedButtonInteriorBottomSelected);
        texturedButtonInteriorSelected = texturedButtonInteriorDefault;
        texturedButtonInteriorDefaultPressed = texturedButtonInteriorDefault;
        texturedButtonInteriorPressedSelected = new FourColors(texturedButtonInteriorTopPressedSelected,
            texturedButtonInteriorUpperMidPressedSelected, texturedButtonInteriorLowerMidPressedSelected,
            texturedButtonInteriorBottomPressedSelected);
        texturedButtonInteriorDisabled = desaturate(texturedButtonInteriorEnabled);
        texturedButtonInteriorDisabledSelected = desaturate(texturedButtonInteriorDefault);

        scrollBarThumbBorderPressed = new TwoColors(scrollBarThumbBorderTopPressed, scrollBarThumbBorderBottomPressed);

        scrollBarThumbInteriorPressed = new FourColors(scrollBarThumbInteriorTopPressed, scrollBarThumbInteriorUpperMidPressed,
            scrollBarThumbInteriorLowerMidPressed, scrollBarThumbInteriorBottomPressed);

        buttonBulletEnabled = new TwoColors(buttonBulletTopEnabled, buttonBulletBottomEnabled);
        buttonbulletDisabled = disable(buttonBulletEnabled);

        comboBoxButtonInteriorDisabled = buttonInteriorDisabled;
        comboBoxButtonInteriorEnabled = buttonInteriorSelected;
        comboBoxButtonInteriorPressed = buttonInteriorPressed;

        comboBoxBackgroundInteriorDisabled = buttonInteriorDisabled;
        comboBoxBackgroundInteriorEnabled = buttonInteriorEnabled;
        comboBoxBackgroundInteriorPressed = buttonInteriorEnabled;

        frameActive = new FourColors(frameTopActive, frameUpperMidActive, frameLowerMidActive, frameBottomActive);
        frameInactive = new FourColors(frameTopInactive, frameUpperMidInactive, frameLowerMidInactive, frameBottomInactive);

        menuItemBackground = new TwoColors(menuItemBackgroundTop, menuItemBackgroundBottom);

        popupMenuBorderDisabled = disable(popupMenuBorderEnabled);
        popupMenuInteriorDisabled = disable(popupMenuBorderEnabled);

        progressBarTrackEnabled = new TwoColors(buttonBorderBottomPressed, scrollBarThumbBorderTopPressed);
        progressBarTrackDisabled = disable(progressBarTrackEnabled);

        progressBarTrackInteriorDisabled = disable(progressBarTrackInteriorEnabled);

        progressBarEnabled = new FourColors(buttonInteriorTopSelected, buttonInteriorUpperMidSelected, buttonInteriorLowerMidSelected,
            buttonInteriorBottomSelected);
        progressBarDisabled = disable(progressBarEnabled);

        progressBarEndEnabled = progressBarEndLine;
        progressBarEndDisabled = disable(progressBarEndEnabled);

        progressBarIndeterminatePatternEnabled = new FourColors(buttonInteriorTopEnabled, buttonInteriorUpperMidEnabled,
            buttonInteriorLowerMidEnabled, buttonInteriorBottomEnabled);
        progressBarIndeterminatePatternDisabled = disable(progressBarIndeterminatePatternEnabled);

        scrollBarTrackBackground = new TwoColors(scrollBarTrackBackgroundTop, scrollBarTrackBackgroundBottom);
        scrollBarTrackGradient = new FourColors(scrollBarTrackGradientTop, scrollBarTrackGradientUpperMid, scrollBarTrackGradientLowerMid,
            scrollBarTrackGradientBottom);

        scrollBarCapColors = new TwoColors(scrollBarCapTop, scrollBarCapBottom);

        scrollBarButtonIncreaseApart = new TwoColors(grayd1, white);
        scrollBarButtonIncreaseTogether = new TwoColors(grayd1, graye5);
        scrollBarButtonIncreasePressed = new TwoColors(scrollBarButtonMiddlePressed, scrollBarButtonBottomPressed);

        scrollBarButtonDecreaseApart = new TwoColors(white, graycc);
        scrollBarButtonDecreaseTogether = new TwoColors(white, graye9);
        scrollBarButtonDecreasePressed = new TwoColors(scrollBarButtonTopPressed, scrollBarButtonMiddlePressed);

        scrollBarButtonLine = graybd;
        scrollBarButtonLinePressed = scrollBarButtonLinePressedColor;
        scrollBarButtonArrow = gray55;
        scrollBarButtonArrowDisabled = disable(scrollBarButtonArrow);

        scrollBarButtonDarkDivider = black1f;
        scrollBarButtonLightDivider = white3f;

        sliderTrackBorderEnabled = new TwoColors(gray63, grayae);
        sliderTrackBorderDisabled = new TwoColors(disable(gray90), disable(grayb4));

        sliderTrackInteriorEnabled = new TwoColors(grayc4, grayeb);
        sliderTrackInteriorDisabled = disable(sliderTrackInteriorEnabled);

        spinnerNextBorderEnabled = new TwoColors(buttonBorderTopPressed, spinnerNextBorderBottomEnabled);
        spinnerNextBorderPressed = new TwoColors(buttonBorderTopPressed, spinnerNextBorderBottomPressed);
        spinnerNextBorderDisabled = disable(spinnerNextBorderEnabled);

        spinnerNextInteriorEnabled = new TwoColors(buttonInteriorTopSelected, spinnerNextInteriorBottomEnabled);
        spinnerNextInteriorPressed = new TwoColors(buttonInteriorTopPressed, spinnerNextInteriorBottomPressed);
        spinnerNextInteriorDisabled = desaturate(desaturate(spinnerNextInteriorEnabled));

        spinnerPrevBorderEnabled = new TwoColors(spinnerPrevBorderTopEnabled, scrollBarThumbBorderTopPressed);
        spinnerPrevBorderPressed = new TwoColors(spinnerPrevBorderTopEnabled, scrollBarThumbBorderTopPressed);
        spinnerPrevBorderDisabled = disable(spinnerPrevBorderEnabled);

        spinnerPrevInteriorEnabled = new TwoColors(spinnerPrevInteriorTopEnabled, spinnerPrevInteriorBottomEnabled);
        spinnerPrevInteriorPressed = new TwoColors(spinnerPrevInteriorPressedTop, spinnerPrevInteriorPressedBottom);
        spinnerPrevInteriorDisabled = desaturate(desaturate(spinnerPrevInteriorEnabled));

        spinnerPrevTopLineDisabled = desaturate(spinnerPrevTopLineEnabled);

        spinnerArrowEnabled = black;
        spinnerArrowDisabled = desaturate(spinnerArrowEnabled);

        splitPaneDividerBorder = new TwoColors(buttonBorderTopEnabled, buttonBorderBottomEnabled);
        splitPaneDividerInterior = new ThreeColors(buttonInteriorTopEnabled, buttonInteriorLowerMidEnabled, buttonInteriorBottomEnabled);

        tabbedPaneTabAreaBackLineDisabled = disable(tabbedPaneTabAreaBackLineEnabled);

        textComponentBorderDisabled = decodeColor("seaGlassTextDisabledBorder");
        textComponentBorderEnabled = decodeColor("seaGlassTextEnabledBorder");
        textComponentBorderEnabledToolbar = decodeColor("seaGlassTextEnabledToolbarBorder");

        tableHeaderBorderDisabled = disable(tableHeaderBorderEnabled);

        tableHeaderEnabled = new FourColors(buttonInteriorTopEnabled, tableHeaderUpperMidEnabled, tableHeaderLowerMidEnabled,
            buttonInteriorBottomEnabled);
        tableHeaderSorted = new FourColors(buttonInteriorTopSelected, buttonInteriorUpperMidSelected, buttonInteriorLowerMidSelected,
            buttonInteriorBottomSelected);
        tableHeaderPressed = new FourColors(buttonInteriorTopPressed, buttonInteriorUpperMidPressed, buttonInteriorLowerMidPressed,
            buttonInteriorBottomPressed);
        tableHeaderDisabled = disable(tableHeaderEnabled);
        tableHeaderDisabledSorted = disable(tableHeaderSorted);

        toolbarHandleBorder = buttonBorderEnabled;
        toolbarHandleInterior = buttonInteriorEnabled;

        toolbarToggleButtonInner = new TwoColors(transparentColor, black28);
        toolbarToggleButtonInnerEdge = new TwoColors(transparentColor, black20);
        toolbarToggleButtonOuterEdge = new TwoColors(black10, black40);
    }

    public static Paint getToolbarToggleButtonPaint(Shape s, ToolbarToggleButtonType type) {
        TwoColors colors = getToolbarToggleButtonColors(type);
        return createToolbarToggleButtonGradient(s, colors);
    }

    public static Paint getToolbarHandleMacPaint() {
        return toolbarHandleMac;
    }

    public static Paint getToolbarHandleBorderPaint(Shape s) {
        return createTwoColorGradientHorizontal(s, toolbarHandleBorder);
    }

    public static Paint getToolbarHandleInteriorPaint(Shape s) {
        return createFourColorGradientHorizontal(s, toolbarHandleInterior);
    }

    public static Paint getTableHeaderBorderPaint(ButtonType type) {
        return type == ButtonType.DISABLED ? tableHeaderBorderDisabled : tableHeaderBorderEnabled;
    }

    public static Paint getTableHeaderSortIndicatorPaint() {
        return tableHeaderSortIndicator;
    }

    public static Paint getTableHeaderPaint(Shape s, ButtonType type, boolean isSorted) {
        FourColors colors = getTableHeaderColors(type, isSorted);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getTextComponentBorderPaint(ButtonType type, boolean inToolbar) {
        if (type == ButtonType.DISABLED) {
            return textComponentBorderDisabled;
        } else if (inToolbar) {
            return textComponentBorderEnabledToolbar;
        } else {
            return textComponentBorderEnabled;
        }
    }

    public static Paint getSplitPaneDividerBackgroundPaint() {
        return splitPaneDividerBackgroundEnabled;
    }

    public static Paint getSplitPaneDividerBackgroundOuterPaint() {
        return splitPaneDividerBackgroundOuter;
    }

    public static Paint getSplitPaneDividerBorderPaint(Shape s) {
        return decodeGradientForegroundBorder(s, splitPaneDividerBorder.top, splitPaneDividerBorder.bottom);
    }

    public static Paint getSplitPaneDividerInteriorPaint(Shape s) {
        return decodeGradientForegroundInside(s, splitPaneDividerInterior.top, splitPaneDividerInterior.mid,
            splitPaneDividerInterior.bottom);
    }

    public static Paint getTabbedPaneTabAreaBackgroundColor(ButtonType type) {
        return type == ButtonType.DISABLED ? tabbedPaneTabAreaBackLineDisabled : tabbedPaneTabAreaBackLineEnabled;
    }

    public static Paint getTabbedPaneTabAreaHorizontalPaint(int x, int y, int width, int height) {
        float midX = x + width / 2;
        return createGradient(midX, y, midX, y + height, new float[] { 0f, 0.5f, 1f }, new Color[] {
            tabbedPaneTabAreaLightShadow,
            tabbedPaneTabAreaDarkShadow,
            tabbedPaneTabAreaLightShadow });
    }

    public static Paint getTabbedPaneTabAreaVerticalPaint(int x, int y, int width, int height) {
        float midY = y + height / 2;
        return createGradient(x, midY, x + width, midY, new float[] { 0f, 0.5f, 1f }, new Color[] {
            tabbedPaneTabAreaLightShadow,
            tabbedPaneTabAreaDarkShadow,
            tabbedPaneTabAreaLightShadow });
    }

    public static Paint getButtonInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getButtonInteriorColors(type, false);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getButtonInteriorPaint(Shape s, ButtonType type, boolean isTextured) {
        FourColors colors = getButtonInteriorColors(type, isTextured);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxButtonInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getComboBoxButtonInteriorColors(type);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxBackgroundInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getComboBoxBackgroundInteriorColors(type);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getMenuItemBackgroundPaint(Shape s) {
        return createTwoColorGradientVertical(s, menuItemBackground);
    }

    public static Color getMenuItemBottomLinePaint() {
        return menuItemBottomLine;
    }

    public static Paint getFocusPaint(Shape s, FocusType focusType, boolean useToolBarFocus) {
        if (focusType == FocusType.OUTER_FOCUS) {
            return useToolBarFocus ? outerToolBarFocus : outerFocus;
        } else {
            return useToolBarFocus ? innerToolBarFocus : innerFocus;
        }
    }

    public static Paint getProgressBarBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getProgressBarBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getFrameBorderPaint(Shape s, ButtonType type) {
        return getFrameBorderColors(type);
    }

    public static Paint getButtonBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getButtonBorderColors(type, false);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getButtonBorderPaint(Shape s, ButtonType type, boolean isTextured) {
        TwoColors colors = getButtonBorderColors(type, isTextured);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getScrollBarThumbBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getScrollBarThumbBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getSpinnerNextBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getSpinnerNextBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getSpinnerPrevBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getSpinnerPrevBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxButtonBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getComboBoxButtonBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxBackgroundBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getComboBoxBackgroundBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getPopupMenuBorderPaint(Shape s, ButtonType type) {
        return getPopupMenuBorderColors(type);
    }

    public static Paint getSliderTrackBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getSliderTrackBorderColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getFrameInnerHighlightPaint(Shape s, ButtonType type) {
        return getFrameInnerHighlightColors(type);
    }

    public static Paint getSpinnerPrevTopLinePaint(Shape s, ButtonType type) {
        return getSpinnerPrevTopLineColors(type);
    }

    public static Paint getProgressBarTrackPaint(Shape s, ButtonType type) {
        return getProgressBarTrackColors(type);
    }

    public static Paint getPopupMenuInteriorPaint(Shape s, ButtonType type) {
        return getPopupMenuInteriorColors(type);
    }

    public static Paint getPopupMenuSeparatorPaint(Shape s, ButtonType type) {
        return popupMenuSeparator;
    }

    public static Paint getFrameInteriorPaint(Shape s, ButtonType type, int titleHeight, int topToolBarHeight, int bottomToolBarHeight) {
        FourColors colors = getFrameInteriorColors(type);
        return createFrameGradient(s, titleHeight, topToolBarHeight, bottomToolBarHeight, colors);
    }

    public static Paint getScrollBarThumbInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getScrollBarThumbInteriorColors(type);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getRadioButtonBulletPaint(Shape s, ButtonType type) {
        TwoColors colors = getCheckBoxBulletColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getSpinnerNextInteriorPaint(Shape s, ButtonType type) {
        TwoColors colors = getSpinnerNextInteriorColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getSpinnerPrevInteriorPaint(Shape s, ButtonType type) {
        TwoColors colors = getSpinnerPrevInteriorColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getCheckBoxBulletPaint(Shape s, ButtonType type) {
        TwoColors colors = getCheckBoxBulletColors(type);
        return createCheckMarkGradient(s, colors);
    }

    public static Paint getSpinnerArrowPaint(Shape s, ButtonType type) {
        return getSpinnerArrowColors(type);
    }

    public static Paint getRootPaneInteriorPaint(Shape s, ButtonType type) {
        TwoColors colors = getRootPaneInteriorColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getSliderTrackInteriorPaint(Shape s, ButtonType type) {
        TwoColors colors = getSliderTrackInteriorColors(type);
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getDesktopPanePaint(Shape s) {
        return desktopPane;
    }

    public static Paint getProgressBarPaint(Shape s, ButtonType type) {
        FourColors colors = getProgressBarColors(type);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getProgressBarIndeterminatePaint(Shape s, ButtonType type) {
        FourColors colors = getProgressBarIndeterminateColors(type);
        return createFourColorGradientVertical(s, colors);
    }

    public static Paint getProgressBarEndPaint(Shape s, ButtonType type) {
        return getProgressBarEndColor(type);
    }

    public static Paint getScrollBarTrackBackgroundPaint(Shape s) {
        return createTwoColorGradientVertical(s, scrollBarTrackBackground);
    }

    public static Paint getScrollBarTrackShadowPaint(Shape s) {
        return createScrollBarTrackInnerShadowGradient(s, scrollBarTrackGradient);
    }

    public static Paint getScrollBarButtonBackgroundPaint(Shape s, ButtonType type, boolean isIncrease, boolean buttonsTogether) {
        TwoColors colors = getScrollBarButtonBackgroundColors(type, isIncrease, buttonsTogether);
        return createTwoColorGradientHorizontal(s, colors);
    }

    public static Paint getScrollBarButtonLinePaint(ButtonType type) {
        return getScrollBarButtonLineColor(type);
    }

    public static Paint getScrollBarButtonDividerPaint(boolean isIncrease) {
        return isIncrease ? scrollBarButtonLightDivider : scrollBarButtonDarkDivider;
    }

    public static Paint getScrollBarButtonArrowPaint(Shape s, ButtonType type) {
        return getScrollBarButtonArrowColor(type);
    }

    public static Paint getRoundedShadowPaint(Shape s) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        int y1 = r.y;
        float frac = 1.0f / r.height;
        int y2 = r.y + r.height;
        return createGradient(x, y1, x, y2, new float[] { 0f, frac, 1f }, new Color[] {
            innerShadow.top,
            innerShadow.bottom,
            innerShadow.bottom });
    }

    public static Paint getTopShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minY = (float) bounds.getMinY();
        float maxY = (float) bounds.getMaxY();
        float midX = (float) bounds.getCenterX();
        return createGradient(midX, minY, midX, maxY, new float[] { 0f, 1f }, new Color[] { innerShadow.top, transparentColor });
    }

    public static Paint getLeftShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX();
        float maxX = (float) bounds.getMaxX();
        float midY = (float) bounds.getCenterY();
        return createGradient(minX, midY, maxX, midY, new float[] { 0f, 1f }, new Color[] { innerShadow.bottom, transparentColor });
    }

    public static Paint getRightShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX() - 1;
        float maxX = (float) bounds.getMaxX() - 1;
        float midY = (float) bounds.getCenterY();
        return createGradient(minX, midY, maxX, midY, new float[] { 0f, 1f }, new Color[] { transparentColor, innerShadow.bottom });
    }

    private static Paint decodeGradientForegroundBorder(Shape s, Color border1, Color border2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
            new float[] { 0.20645161f, 0.5f, 0.7935484f }, new Color[] { border1, decodeColor(border1, border2, 0.5f), border2 });
    }

    private static Paint decodeGradientForegroundInside(Shape s, Color inside1, Color inside2, Color inside3) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.090322584f,
            0.2951613f,
            0.5f,
            0.5822581f,
            0.66451615f }, new Color[] {
            inside1,
            decodeColor(inside1, inside2, 0.5f),
            inside2,
            decodeColor(inside2, inside3, 0.5f),
            inside3 });
    }

    private static TwoColors getToolbarToggleButtonColors(ToolbarToggleButtonType type) {
        switch (type) {
        case INNER:
            return toolbarToggleButtonInner;
        case INNER_EDGE:
            return toolbarToggleButtonInnerEdge;
        case OUTER_EDGE:
            return toolbarToggleButtonOuterEdge;
        }
        return null;
    }

    private static TwoColors getButtonBorderColors(ButtonType type, boolean textured) {
        if (textured) {
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
        } else {
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
        }
        return null;
    }

    private static FourColors getButtonInteriorColors(ButtonType type, boolean textured) {
        if (textured) {
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
        } else {
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
        }
        return null;
    }

    private static FourColors getTableHeaderColors(ButtonType type, boolean isSorted) {
        switch (type) {
        case DISABLED:
            return isSorted ? tableHeaderDisabledSorted : tableHeaderDisabled;
        case ENABLED:
            return isSorted ? tableHeaderSorted : tableHeaderEnabled;
        case PRESSED:
            return tableHeaderPressed;
        }
        return null;
    }

    private static TwoColors getScrollBarButtonBackgroundColors(ButtonType type, boolean isIncrease, boolean buttonsTogether) {
        if (type == ButtonType.SCROLL_CAP) {
            return scrollBarCapColors;
        } else if (type == ButtonType.PRESSED) {
            return isIncrease ? scrollBarButtonIncreasePressed : scrollBarButtonDecreasePressed;
        } else {
            if (buttonsTogether) {
                return isIncrease ? scrollBarButtonIncreaseTogether : scrollBarButtonDecreaseTogether;
            } else {
                return isIncrease ? scrollBarButtonIncreaseApart : scrollBarButtonDecreaseApart;
            }
        }
    }

    private static Color getScrollBarButtonLineColor(ButtonType type) {
        if (type == ButtonType.PRESSED) {
            return scrollBarButtonLinePressed;
        } else {
            return scrollBarButtonLine;
        }
    }

    private static Color getScrollBarButtonArrowColor(ButtonType type) {
        if (type == ButtonType.DISABLED) {
            return scrollBarButtonArrowDisabled;
        } else {
            return scrollBarButtonArrow;
        }
    }

    private static TwoColors getScrollBarThumbBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return buttonBorderDisabled;
        case ENABLED:
            return buttonBorderEnabled;
        case PRESSED:
            return scrollBarThumbBorderPressed;
        }
        return null;
    }

    private static FourColors getScrollBarThumbInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return buttonInteriorDisabled;
        case ENABLED:
            return buttonInteriorEnabled;
        case PRESSED:
            return scrollBarThumbInteriorPressed;
        }
        return null;
    }

    private static TwoColors getCheckBoxBulletColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return buttonbulletDisabled;
        case ENABLED:
        case PRESSED:
        case SELECTED:
        case PRESSED_SELECTED:
            return buttonBulletEnabled;
        }
        return null;
    }

    private static TwoColors getComboBoxButtonBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxButtonBorderDisabled;
        case ENABLED:
            return comboBoxButtonBorderEnabled;
        case PRESSED:
            return comboBoxButtonBorderPressed;
        }
        return null;
    }

    private static FourColors getComboBoxButtonInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxButtonInteriorDisabled;
        case ENABLED:
            return comboBoxButtonInteriorEnabled;
        case PRESSED:
            return comboBoxButtonInteriorPressed;
        }
        return null;
    }

    private static TwoColors getComboBoxBackgroundBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxBackgroundBorderDisabled;
        case ENABLED:
            return comboBoxBackgroundBorderEnabled;
        case PRESSED:
            return comboBoxBackgroundBorderPressed;
        }
        return null;
    }

    private static FourColors getComboBoxBackgroundInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxBackgroundInteriorDisabled;
        case ENABLED:
            return comboBoxBackgroundInteriorEnabled;
        case PRESSED:
            return comboBoxBackgroundInteriorPressed;
        }
        return null;
    }

    private static TwoColors getRootPaneInteriorColors(ButtonType type) {
        switch (type) {
        case ACTIVE:
            return rootPaneActive;
        case INACTIVE:
            return rootPaneInactive;
        }
        return null;
    }

    private static Color getFrameBorderColors(ButtonType type) {
        switch (type) {
        case INACTIVE:
            return frameBorderInactive;
        case ACTIVE:
            return frameBorderActive;
        }
        return null;
    }

    private static Color getFrameInnerHighlightColors(ButtonType type) {
        switch (type) {
        case INACTIVE:
            return frameInnerHighlightInactive;
        case ACTIVE:
            return frameInnerHighlightActive;
        }
        return null;
    }

    private static FourColors getFrameInteriorColors(ButtonType type) {
        switch (type) {
        case INACTIVE:
            return frameInactive;
        case ACTIVE:
            return frameActive;
        }
        return null;
    }

    private static Color getPopupMenuBorderColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return popupMenuBorderEnabled;
        case DISABLED:
            return popupMenuBorderDisabled;
        }
        return null;
    }

    private static Color getPopupMenuInteriorColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return popupMenuInteriorEnabled;
        case DISABLED:
            return popupMenuInteriorDisabled;
        }
        return null;
    }

    private static TwoColors getProgressBarBorderColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarTrackEnabled;
        case DISABLED:
            return progressBarTrackDisabled;
        }
        return null;
    }

    private static Color getProgressBarTrackColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarTrackInteriorEnabled;
        case DISABLED:
            return progressBarTrackInteriorDisabled;
        }
        return null;
    }

    private static FourColors getProgressBarColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarEnabled;
        case DISABLED:
            return progressBarDisabled;
        }
        return null;
    }

    private static FourColors getProgressBarIndeterminateColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarIndeterminatePatternEnabled;
        case DISABLED:
            return progressBarIndeterminatePatternDisabled;
        }
        return null;
    }

    private static Color getProgressBarEndColor(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarEndEnabled;
        case DISABLED:
            return progressBarEndDisabled;
        }
        return null;
    }

    private static TwoColors getSliderTrackBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackBorderDisabled;
        case ENABLED:
            return sliderTrackBorderEnabled;
        }
        return null;
    }

    private static TwoColors getSliderTrackInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackInteriorDisabled;
        case ENABLED:
            return sliderTrackInteriorEnabled;
        }
        return null;
    }

    private static TwoColors getSpinnerNextBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return spinnerNextBorderDisabled;
        case ENABLED:
            return spinnerNextBorderEnabled;
        case PRESSED:
            return spinnerNextBorderPressed;
        }
        return null;
    }

    private static TwoColors getSpinnerNextInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return spinnerNextInteriorDisabled;
        case ENABLED:
            return spinnerNextInteriorEnabled;
        case PRESSED:
            return spinnerNextInteriorPressed;
        }
        return null;
    }

    private static TwoColors getSpinnerPrevBorderColors(ButtonType type) {
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

    private static TwoColors getSpinnerPrevInteriorColors(ButtonType type) {
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

    private static Color getSpinnerPrevTopLineColors(ButtonType type) {
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

    private static Color getSpinnerArrowColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return spinnerArrowDisabled;
        case ENABLED:
        case PRESSED:
            return spinnerArrowEnabled;
        }
        return null;
    }

    private static Paint createTwoColorGradientVertical(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xCenter = (float) bounds.getCenterX();
        float yMin = (float) bounds.getMinY();
        float yMax = (float) bounds.getMaxY();
        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }

    private static Paint createTwoColorGradientHorizontal(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xMin = (float) bounds.getMinX();
        float xMax = (float) bounds.getMaxX();
        float yCenter = (float) bounds.getCenterY();
        return createGradient(xMin, yCenter, xMax, yCenter, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }

    private static Paint createFourColorGradientVertical(Shape s, FourColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xCenter = (float) bounds.getCenterX();
        float yMin = (float) bounds.getMinY();
        float yMax = (float) bounds.getMaxY();
        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, 0.45f, 0.62f, 1f }, new Color[] {
            colors.top,
            colors.upperMid,
            colors.lowerMid,
            colors.bottom });
    }

    private static Paint createFourColorGradientHorizontal(Shape s, FourColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient(x, (0.5f * h) + y, x + w, (0.5f * h) + y, new float[] { 0f, 0.45f, 0.62f, 1f }, new Color[] {
            colors.top,
            colors.upperMid,
            colors.lowerMid,
            colors.bottom });
    }

    private static Paint createToolbarToggleButtonGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 0.35f, 0.65f, 1f }, new Color[] {
            colors.top,
            colors.bottom,
            colors.bottom,
            colors.top });
    }

    private static Paint createFrameGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, FourColors defColors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();

        float midX = x + w / 2.0f;
        float titleBottom = titleHeight / h;
        if (titleBottom >= 1.0f) {
            titleBottom = 1.0f - 0.00004f;
        }

        float[] midPoints = null;
        Color[] colors = null;
        if (topToolBarHeight > 0 && bottomToolBarHeight > 0) {
            float topToolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (topToolBarBottom >= 1.0f) {
                topToolBarBottom = 1.0f - 0.00002f;
            }
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, topToolBarBottom, bottomToolBarTop, 1.0f };
            colors = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
        } else if (topToolBarHeight > 0) {
            float toolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (toolBarBottom >= 1.0f) {
                toolBarBottom = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, toolBarBottom, 1.0f };
            colors = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid };
        } else if (bottomToolBarHeight > 0) {
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, bottomToolBarTop, 1.0f };
            colors = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
        } else {
            midPoints = new float[] { 0.0f, titleBottom, 1.0f };
            colors = new Color[] { defColors.top, defColors.upperMid, defColors.bottom };
        }

        return createGradient(midX, y, x + midX, y + h, midPoints, colors);
    }

    private static Paint createCheckMarkGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient(x + w, y, (0.3f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }

    private static Paint createScrollBarTrackInnerShadowGradient(Shape s, FourColors colors) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return createGradient(width * 0.5f, 0, width * 0.5f, height - 1, new float[] { 0f, 0.142857143f, 0.5f, 0.785714286f, 1f },
            new Color[] { colors.top, colors.upperMid, colors.lowerMid, colors.lowerMid, colors.bottom });
    }

    /**
     * Given parameters for creating a LinearGradientPaint, this method will
     * create and return a linear gradient paint. One primary purpose for this
     * method is to avoid creating a LinearGradientPaint where the start and end
     * points are equal. In such a case, the end y point is slightly increased
     * to avoid the overlap.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param midpoints
     * @param colors
     * @return a valid LinearGradientPaint. This method never returns null.
     */
    private static final LinearGradientPaint createGradient(float x1, float y1, float x2, float y2, float[] midpoints, Color[] colors) {
        if (x1 == x2 && y1 == y2) {
            y2 += .00001f;
        }
        return new LinearGradientPaint(x1, y1, x2, y2, midpoints, colors);
    }

    /**
     * Decodes and returns a base color in UI defaults.
     * 
     * @param key
     *            A key corresponding to the value in the UI Defaults table of
     *            UIManager where the base color is defined
     * @return The base color.
     */
    private static final Color decodeColor(String key) {
        return decodeColor(key, 0f, 0f, 0f, 0);
    }

    /**
     * Decodes and returns a color, which is derived from a base color in UI
     * defaults.
     * 
     * @param key
     *            A key corresponding to the value in the UI Defaults table of
     *            UIManager where the base color is defined
     * @param hOffset
     *            The hue offset used for derivation.
     * @param sOffset
     *            The saturation offset used for derivation.
     * @param bOffset
     *            The brightness offset used for derivation.
     * @param aOffset
     *            The alpha offset used for derivation. Between 0...255
     * @return The derived color, whos color value will change if the parent
     *         uiDefault color changes.
     */
    private static final Color decodeColor(String key, float hOffset, float sOffset, float bOffset, int aOffset) {
        if (UIManager.getLookAndFeel() instanceof SeaGlassLookAndFeel) {
            SeaGlassLookAndFeel laf = (SeaGlassLookAndFeel) UIManager.getLookAndFeel();
            return laf.getDerivedColor(key, hOffset, sOffset, bOffset, aOffset, true);
        } else {
            // can not give a right answer as painter should not be used outside
            // of nimbus laf but do the best we can
            return Color.getHSBColor(hOffset, sOffset, bOffset);
        }
    }

    /**
     * Decodes and returns a color, which is derived from a offset between two
     * other colors.
     * 
     * @param color1
     *            The first color
     * @param mid
     *            The second color
     * @param midPoint
     *            The offset between color 1 and color 2, a value of 0.0 is
     *            color 1 and 1.0 is color 2;
     * @return The derived color
     */
    private static Color decodeColor(Color color1, Color color2, float midPoint) {
        return new Color(deriveARGB(color1, color2, midPoint));
    }

    /**
     * Derive and returns a color, which is based on an existing color.
     * 
     * @param src
     *            The source color from which to derive the new color.
     * @param hOffset
     *            The hue offset used for derivation.
     * @param sOffset
     *            The saturation offset used for derivation.
     * @param bOffset
     *            The brightness offset used for derivation.
     * @param aOffset
     *            The alpha offset used for derivation. Between 0...255
     * @return The derived color.
     */
    private static Color deriveColor(Color src, float hOffset, float sOffset, float bOffset, int aOffset) {
        float[] tmp = Color.RGBtoHSB(src.getRed(), src.getGreen(), src.getBlue(), null);

        // apply offsets
        tmp[0] = clamp(tmp[0] + hOffset);
        tmp[1] = clamp(tmp[1] + sOffset);
        tmp[2] = clamp(tmp[2] + bOffset);
        int alpha = clamp(src.getAlpha() + aOffset);

        return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24), true);
    }

    private static Color disable(Color color) {
        int alpha = color.getAlpha();
        alpha /= 2;
        return new Color((color.getRGB() & 0xFFFFFF) | (alpha << 24), true);
    }

    private static Color desaturate(Color color) {
        float[] tmp = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        tmp[1] /= 3.0f;
        tmp[2] = clamp(1.0f - (1.0f - tmp[2]) / 3f);
        return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF));
    }

    private static TwoColors disable(TwoColors colors) {
        return new TwoColors(disable(colors.top), disable(colors.bottom));
    }

    private static TwoColors desaturate(TwoColors colors) {
        return new TwoColors(desaturate(colors.top), desaturate(colors.bottom));
    }

    private static FourColors disable(FourColors colors) {
        return new FourColors(disable(colors.top), disable(colors.upperMid), disable(colors.lowerMid), disable(colors.bottom));
    }

    private static FourColors desaturate(FourColors colors) {
        return new FourColors(desaturate(colors.top), desaturate(colors.upperMid), desaturate(colors.lowerMid), desaturate(colors.bottom));
    }

    /**
     * Derives the ARGB value for a color based on an offset between two other
     * colors.
     * 
     * @param color1
     *            The first color
     * @param mid
     *            The second color
     * @param midPoint
     *            The offset between color 1 and color 2, a value of 0.0 is
     *            color 1 and 1.0 is color 2;
     * @return the ARGB value for a new color based on this derivation
     */
    private static int deriveARGB(Color color1, Color color2, float midPoint) {
        int r = color1.getRed() + (int) ((color2.getRed() - color1.getRed()) * midPoint + 0.5f);
        int g = color1.getGreen() + (int) ((color2.getGreen() - color1.getGreen()) * midPoint + 0.5f);
        int b = color1.getBlue() + (int) ((color2.getBlue() - color1.getBlue()) * midPoint + 0.5f);
        int a = color1.getAlpha() + (int) ((color2.getAlpha() - color1.getAlpha()) * midPoint + 0.5f);
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    private static float clamp(float value) {
        if (value < 0) {
            value = 0;
        } else if (value > 1) {
            value = 1;
        }
        return value;
    }

    private static int clamp(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return value;
    }

    /**
     * Two color gradients.
     */
    private static class TwoColors {
        public Color top;
        public Color bottom;

        public TwoColors(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }

    private static class ThreeColors extends TwoColors {
        public Color mid;

        public ThreeColors(Color top, Color mid, Color bottom) {
            super(top, bottom);
            this.mid = mid;
        }
    }

    /**
     * A set of colors to use for scrollbar thumbs and some other controls.
     */
    private static class FourColors extends TwoColors {
        public Color upperMid;
        public Color lowerMid;

        public FourColors(Color top, Color upperMid, Color lowerMid, Color bottom) {
            super(top, bottom);
            this.upperMid = upperMid;
            this.lowerMid = lowerMid;
        }
    }
}
