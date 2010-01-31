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

    private static Color              transparentColor;

    private static Color              outerFocus;
    private static Color              innerFocus;
    private static Color              outerToolBarFocus;
    private static Color              innerToolBarFocus;

    private static TwoColors          innerRoundedShadow;

    private static Color              rectangularShadowLight;
    private static Color              rectangularShadowDark;

    private static FourLayerColors    buttonEnabled;
    private static FourLayerColors    buttonEnabledPressed;
    private static FourLayerColors    buttonDefault;
    private static FourLayerColors    buttonDefaultPressed;
    private static FourLayerColors    buttonDisabled;
    private static FourLayerColors    buttonDisabledSelected;

    private static FourLayerColors    texturedButtonEnabled;
    private static FourLayerColors    texturedButtonEnabledPressed;
    private static FourLayerColors    texturedButtonDefault;
    private static FourLayerColors    texturedButtonDefaultPressed;
    private static FourLayerColors    texturedButtonDisabled;
    private static FourLayerColors    texturedButtonDisabledSelected;

    private static TwoLayerFourColors scrollBarThumbDisabled;
    private static TwoLayerFourColors scrollBarThumbEnabled;
    private static TwoLayerFourColors scrollBarThumbPressed;

    private static TwoLayerFourColors checkBoxDisabled;
    private static TwoLayerFourColors checkBoxEnabled;
    private static TwoLayerFourColors checkBoxPressed;
    private static TwoLayerFourColors checkBoxSelected;
    private static TwoLayerFourColors checkBoxPressedSelected;

    private static TwoColors          checkBoxBulletEnabled;
    private static TwoColors          checkBoxbulletDisabled;

    private static FourLayerColors    comboBoxButtonDisabled;
    private static FourLayerColors    comboBoxButtonEnabled;
    private static FourLayerColors    comboBoxButtonPressed;

    private static FourLayerColors    comboBoxBackgroundDisabled;
    private static FourLayerColors    comboBoxBackgroundEnabled;
    private static FourLayerColors    comboBoxBackgroundPressed;

    private static TwoColors          rootPaneActive;
    private static TwoColors          rootPaneInactive;

    private static Color              frameBorderActive;
    private static Color              frameBorderInactive;
    private static Color              frameInnerHighlightInactive;
    private static Color              frameInnerHighlightActive;

    private static FrameColors        frameActive;
    private static FrameColors        frameInactive;

    private static Color              desktopPane;

    private static TwoColors          menuItemBackground;
    private static Color              menuItemBottomLine;

    private static Color              popupMenuDisabledBorder;
    private static Color              popupMenuEnabledBorder;

    private static Color              popupMenuDisabledInterior;
    private static Color              popupMenuEnabledInterior;

    private static Color              popupMenuSeparator;

    private static TwoColors          progressBarDisabledTrack;
    private static Color              progressBarDisabledTrackInterior;

    private static TwoColors          progressBarEnabledTrack;
    private static Color              progressBarEnabledTrackInterior;

    private static FourColors         progressBarDisabled;
    private static FourColors         progressBarEnabled;
    private static FourColors         progressBarIndeterminatePatternDisabled;
    private static FourColors         progressBarIndeterminatePattern;
    private static Color              progressBarDisabledEnd;
    private static Color              progressBarEnabledEnd;

    private static TwoColors          scrollBarTrackBackground;
    private static FourColors         scrollBarTrackGradient;

    private static TwoColors          scrollBarCapColors;

    private static TwoColors          scrollBarButtonIncreaseApart;
    private static TwoColors          scrollBarButtonIncreaseTogether;
    private static TwoColors          scrollBarButtonIncreasePressed;

    private static TwoColors          scrollBarButtonDecreaseApart;
    private static TwoColors          scrollBarButtonDecreaseTogether;
    private static TwoColors          scrollBarButtonDecreasePressed;

    private static Color              scrollBarButtonLine          = new Color(0xbdbdbd);
    private static Color              scrollBarButtonLinePressed   = new Color(0x82abd0);
    private static Color              scrollBarButtonArrow         = new Color(0x555555);
    private static Color              scrollBarButtonArrowDisabled = new Color(0x80555555, true);

    private static Color              scrollBarButtonDarkDivider   = new Color(0x1f000000, true);
    private static Color              scrollBarButtonLightDivider  = new Color(0x3fffffff, true);

    private static TwoColors          sliderTrackDisabledBorder;
    private static TwoColors          sliderTrackDisabledInterior;

    private static TwoColors          sliderTrackEnabledBorder;
    private static TwoColors          sliderTrackEnabledInterior;

    private static TwoColors          spinnerNextBorderDisabled;
    private static TwoColors          spinnerNextBorderEnabled;
    private static TwoColors          spinnerNextBorderPressed;

    private static TwoColors          spinnerNextInteriorDisabled;
    private static TwoColors          spinnerNextInteriorEnabled;
    private static TwoColors          spinnerNextInteriorPressed;

    private static TwoColors          spinnerPrevBorderDisabled;
    private static TwoColors          spinnerPrevBorderEnabled;
    private static TwoColors          spinnerPrevBorderPressed;

    private static TwoColors          spinnerPrevInteriorDisabled;
    private static TwoColors          spinnerPrevInteriorEnabled;
    private static TwoColors          spinnerPrevInteriorPressed;

    private static Color              spinnerPrevTopLineDisabled;
    private static Color              spinnerPrevTopLineEnabled;
    private static Color              spinnerPrevTopLinePressed;

    private static Color              spinnerArrowDisabled;
    private static Color              spinnerArrowEnabled;

    private static Color              splitPaneDividerBackgroundOuter;
    private static Color              splitPaneDividerBackgroundEnabled;
    private static TwoColors          splitPaneDividerBorder;
    private static ThreeColors        splitPaneDividerInterior;

    static {
        transparentColor = new Color(0x0, true);

        outerFocus = decodeColor("seaGlassOuterFocus");
        innerFocus = decodeColor("seaGlassFocus");
        outerToolBarFocus = decodeColor("seaGlassToolBarOuterFocus");
        innerToolBarFocus = decodeColor("seaGlassToolBarFocus");

        innerRoundedShadow = new TwoColors(new Color(0x20000000, true), new Color(0x10000000, true));

        rectangularShadowLight = new Color(0x0a000000, true);
        rectangularShadowDark = new Color(0x1e000000, true);

        buttonEnabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff, true),
            new Color(0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf));
        buttonEnabledPressed = new FourLayerColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF));
        buttonDefault = new FourLayerColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF));
        buttonDefaultPressed = new FourLayerColors(new Color(0xc0eeeeee, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffB4D9EE, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF));
        buttonDisabled = new FourLayerColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF));
        buttonDisabledSelected = new FourLayerColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xaaaaaa), new Color(0x8AAFE0), new Color(0x5785BF));

        texturedButtonEnabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true),
            new Color(0, true), 0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c));
        texturedButtonEnabledPressed = new FourLayerColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(
            0xffcccccc, true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c));
        texturedButtonDefault = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true),
            new Color(0, true), 0.5f, new Color(0x999999), new Color(0x555555), new Color(0x4c4c4c));
        texturedButtonDefaultPressed = new FourLayerColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(
            0xffcccccc, true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c));
        texturedButtonDisabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true),
            new Color(0, true), 0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c));
        texturedButtonDisabledSelected = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true),
            new Color(0, true), 0.5f, new Color(0xaaaaaa), new Color(0x555555), new Color(0x4c4c4c));

        checkBoxDisabled = new TwoLayerFourColors(new Color(0x80fbfdfe, true), new Color(0x80d6eaf9, true), new Color(0x80d2e8f8, true),
            new Color(0x80f5fafd, true), 0.45f, 0.62f, new Color(0x6088ade0, true), new Color(0x605785bf, true));
        checkBoxEnabled = new TwoLayerFourColors(new Color(0xfbfdfe), new Color(0xd6eaf9), new Color(0xd2e8f8), new Color(0xf5fafd), 0.45f,
            0.62f, new Color(0x88ade0), new Color(0x5785bf));
        checkBoxPressed = new TwoLayerFourColors(new Color(0xacbdd0), new Color(0x688db3), new Color(0x6d93ba), new Color(0xa4cbe4), 0.45f,
            0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));
        checkBoxSelected = new TwoLayerFourColors(new Color(0xbccedf), new Color(0x7fa7cd), new Color(0x82b0d6), new Color(0xb0daf6),
            0.45f, 0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));
        checkBoxPressedSelected = new TwoLayerFourColors(new Color(0xacbdd0), new Color(0x688db3), new Color(0x6d93ba),
            new Color(0xa4cbe4), 0.45f, 0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));

        scrollBarThumbDisabled = checkBoxDisabled;
        scrollBarThumbEnabled = checkBoxEnabled;
        scrollBarThumbPressed = checkBoxSelected;

        checkBoxBulletEnabled = new TwoColors(new Color(0x333333), new Color(0x000000));
        checkBoxbulletDisabled = new TwoColors(new Color(0x80333333, true), new Color(0x80000000, true));

        comboBoxButtonDisabled = new FourLayerColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF));
        comboBoxButtonEnabled = new FourLayerColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF));
        comboBoxButtonPressed = new FourLayerColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF));

        comboBoxBackgroundDisabled = comboBoxButtonDisabled;
        comboBoxBackgroundEnabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff,
            true), new Color(0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf));
        comboBoxBackgroundPressed = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff,
            true), new Color(0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf));

        rootPaneActive = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"), decodeColor("seaGlassToolBarActiveBottomB"));
        rootPaneInactive = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"), decodeColor("seaGlassToolBarInactiveBottomB"));

        frameBorderActive = new Color(0x545454);
        frameBorderInactive = new Color(0x545454);

        frameInnerHighlightInactive = new Color(0x55ffffff, true);
        frameInnerHighlightActive = new Color(0x55ffffff, true);

        frameActive = new FrameColors(new Color(0xafbecf), new Color(0x96adc4), new Color(0x96adc4), new Color(0x8ea7c0));
        frameInactive = new FrameColors(new Color(0xededed), new Color(0xe0e0e0), new Color(0xe0e0e0), new Color(0xd3d3d3));

        desktopPane = decodeColor("seaGlassDesktopPane");

        menuItemBackground = new TwoColors(new Color(0x6a90b6), new Color(0x4a6b90));
        menuItemBottomLine = new Color(0x3a5d89);

        popupMenuDisabledBorder = new Color(0x80dddddd, true);
        popupMenuEnabledBorder = new Color(0xdddddd);

        popupMenuDisabledInterior = new Color(0x80ffffff, true);
        popupMenuEnabledInterior = new Color(0xffffff);

        popupMenuSeparator = new Color(0xdddddd);

        progressBarDisabledTrack = new TwoColors(new Color(0x803f76bf, true), new Color(0x804076bf, true));
        progressBarDisabledTrackInterior = new Color(0x80ffffff, true);

        progressBarEnabledTrack = new TwoColors(new Color(0x3f76bf), new Color(0x4076bf));
        progressBarEnabledTrackInterior = new Color(0xffffff);

        progressBarDisabled = new FourColors(new Color(0x80bccedf, true), new Color(0x807fa7cd, true), new Color(0x8082b0d6, true),
            new Color(0x80b0daf6, true), 0.45f, 0.6f);
        progressBarDisabledEnd = new Color(0x804076bf, true);

        progressBarIndeterminatePatternDisabled = new FourColors(new Color(0x80fbfdfe, true), new Color(0x80d6eaf9, true), new Color(
            0x80d2e8f8, true), new Color(0x80f5fafd, true), 0.45f, 0.6f);

        progressBarEnabled = new FourColors(new Color(0xbccedf), new Color(0x7fa7cd), new Color(0x82b0d6), new Color(0xb0daf6), 0.45f, 0.6f);
        progressBarEnabledEnd = new Color(0x4076bf);

        progressBarIndeterminatePattern = new FourColors(new Color(0xfbfdfe), new Color(0xd6eaf9), new Color(0xd2e8f8),
            new Color(0xf5fafd), 0.45f, 0.6f);

        scrollBarTrackBackground = new TwoColors(new Color(0xeeeeee), new Color(0xffffff));
        scrollBarTrackGradient = new FourColors(new Color(0x33000000, true), new Color(0x15000000, true), new Color(0x00000000, true),
            new Color(0x12000000, true), 0f, 0f);

        scrollBarCapColors = new TwoColors(new Color(0xffffff), new Color(0xbbbbbb));

        scrollBarButtonIncreaseApart = new TwoColors(new Color(0xd1d1d1), new Color(0xffffff));
        scrollBarButtonIncreaseTogether = new TwoColors(new Color(0xd1d1d1), new Color(0xe5e5e5));
        scrollBarButtonIncreasePressed = new TwoColors(new Color(0x8fb1d1), new Color(0xcee2f5));

        scrollBarButtonDecreaseApart = new TwoColors(new Color(0xffffff), new Color(0xcccccc));
        scrollBarButtonDecreaseTogether = new TwoColors(new Color(0xffffff), new Color(0xe9e9e9));
        scrollBarButtonDecreasePressed = new TwoColors(new Color(0xcee2f5), new Color(0x8fb1d1));

        scrollBarButtonLine = new Color(0xbdbdbd);
        scrollBarButtonLinePressed = new Color(0x82abd0);
        scrollBarButtonArrow = new Color(0x555555);
        scrollBarButtonArrowDisabled = new Color(0x80555555, true);

        scrollBarButtonDarkDivider = new Color(0x1f000000, true);
        scrollBarButtonLightDivider = new Color(0x3fffffff, true);

        sliderTrackDisabledBorder = new TwoColors(new Color(0x80909090, true), new Color(0x80b4b4b4, true));
        sliderTrackDisabledInterior = new TwoColors(new Color(0x80c4c4c4, true), new Color(0x80ebebeb, true));

        sliderTrackEnabledBorder = new TwoColors(new Color(0x636363), new Color(0xaeaeae));
        sliderTrackEnabledInterior = new TwoColors(new Color(0xc4c4c4), new Color(0xebebeb));

        spinnerNextBorderDisabled = new TwoColors(new Color(0x80a2c2ed, true), new Color(0x807ea4d7, true));
        spinnerNextBorderEnabled = new TwoColors(new Color(0x4f7bbf), new Color(0x4779bf));
        spinnerNextBorderPressed = new TwoColors(new Color(0x4f7bbf), new Color(0x4879bf));

        spinnerNextInteriorDisabled = new TwoColors(new Color(0xeaebf1f7, true), new Color(0xdbe2e9f2, true));
        spinnerNextInteriorEnabled = new TwoColors(new Color(0xbccedf), new Color(0x85abcf));
        spinnerNextInteriorPressed = new TwoColors(new Color(0xacbdd0), new Color(0x6e92b6));

        spinnerPrevBorderDisabled = new TwoColors(new Color(0x807aa1d4, true), new Color(0x805987c0, true));
        spinnerPrevBorderEnabled = new TwoColors(new Color(0x4778bf), new Color(0x4076bf));
        spinnerPrevBorderPressed = new TwoColors(new Color(0x4778bf), new Color(0x4076bf));

        spinnerPrevInteriorDisabled = new TwoColors(new Color(0xd8dbe4f0, true), new Color(0xdddae5f0, true));
        spinnerPrevInteriorEnabled = new TwoColors(new Color(0x81aed4), new Color(0xaad4f1));
        spinnerPrevInteriorPressed = new TwoColors(new Color(0x6c91b8), new Color(0x9cc3de));

        spinnerPrevTopLineDisabled = new Color(0xe0e4ebf3, true);
        spinnerPrevTopLineEnabled = new Color(0xacc8e0);
        spinnerPrevTopLinePressed = new Color(0x9eb6cf);

        spinnerArrowDisabled = new Color(0x9ba8cf);
        spinnerArrowEnabled = new Color(0x000000);

        splitPaneDividerBackgroundOuter = new Color(0xd9d9d9);
        splitPaneDividerBackgroundEnabled = decodeColor("control", 0f, 0f, 0f, 0);
        splitPaneDividerBorder = new TwoColors(new Color(0x88ade0), new Color(0x5785bf));
        splitPaneDividerInterior = new ThreeColors(new Color(0xfbfdfe), new Color(0xd2e8f8), new Color(0xf5fafd));
    }

    public static Paint getSplitPaneDividerBackgroundPaint() {
        return splitPaneDividerBackgroundEnabled;
    }

    public static Paint getSplitPaneDividerBackgroundOuterPaint() {
        return splitPaneDividerBackgroundOuter;
    }

    public static Paint getSplitPaneDividerBorderPaint(Shape s) {
        return decodeGradientForegroundBorder(s, splitPaneDividerBorder.topColor, splitPaneDividerBorder.bottomColor);
    }

    public static Paint getSplitPaneDividerInteriorPaint(Shape s) {
        return decodeGradientForegroundInside(s, splitPaneDividerInterior.topColor, splitPaneDividerInterior.midColor,
            splitPaneDividerInterior.bottomColor);
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

    public static Paint getButtonInteriorMainPaint(Shape s, ButtonType type, boolean isTextured) {
        FourLayerColors colors = getButtonColors(type, isTextured);
        return colors.mainColor;
    }

    public static Paint getButtonInteriorLowerShinePaint(Shape s, ButtonType type, boolean isTextured) {
        FourLayerColors colors = getButtonColors(type, isTextured);
        return createTwoColorGradientWithMidpointVertical(s, colors.lowerShine);
    }

    public static Paint getButtonInteriorUpperShinePaint(Shape s, ButtonType type, boolean isTextured) {
        FourLayerColors colors = getButtonColors(type, isTextured);
        return createTwoColorGradientVertical(s, colors.upperShine);
    }

    public static Paint getComboBoxButtonInteriorMainPaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxButtonColors(type);
        return colors.mainColor;
    }

    public static Paint getComboBoxButtonInteriorLowerShinePaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxButtonColors(type);
        return createTwoColorGradientWithMidpointVertical(s, colors.lowerShine);
    }

    public static Paint getComboBoxButtonInteriorUpperShinePaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxButtonColors(type);
        return createTwoColorGradientVertical(s, colors.upperShine);
    }

    public static Paint getComboBoxBackgroundInteriorMainPaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxBackgroundColors(type);
        return colors.mainColor;
    }

    public static Paint getComboBoxBackgroundInteriorLowerShinePaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxBackgroundColors(type);
        return createTwoColorGradientWithMidpointVertical(s, colors.lowerShine);
    }

    public static Paint getComboBoxBackgroundInteriorUpperShinePaint(Shape s, ButtonType type) {
        FourLayerColors colors = getComboBoxBackgroundColors(type);
        return createTwoColorGradientVertical(s, colors.upperShine);
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

    public static Paint getButtonBorderPaint(Shape s, ButtonType type, boolean isTextured) {
        TwoColors colors = getButtonColors(type, isTextured).background;
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getScrollBarThumbBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getScrollBarThumbColors(type).background;
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getCheckBoxBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getCheckBoxColors(type).background;
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

    public static Paint getRadioButtonBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getCheckBoxColors(type).background;
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxButtonBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getComboBoxButtonColors(type).background;
        return createTwoColorGradientVertical(s, colors);
    }

    public static Paint getComboBoxBackgroundBorderPaint(Shape s, ButtonType type) {
        TwoColors colors = getComboBoxBackgroundColors(type).background;
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
        FrameColors colors = getFrameColors(type);
        return createFrameGradient(s, titleHeight, topToolBarHeight, bottomToolBarHeight, colors.topColorT, colors.topColorB,
            colors.bottomColorT, colors.bottomColorB);
    }

    public static Paint getScrollBarThumbInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getScrollBarThumbColors(type).interior;
        return createGradientFourColor(s, colors);
    }

    public static Paint getCheckBoxInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getCheckBoxColors(type).interior;
        return createGradientFourColor(s, colors);
    }

    public static Paint getRadioButtonInteriorPaint(Shape s, ButtonType type) {
        FourColors colors = getCheckBoxColors(type).interior;
        return createGradientFourColor(s, colors);
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
        TwoColors colors = getRootPaneColors(type);
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
        return createGradientFourColor(s, colors);
    }

    public static Paint getProgressBarIndeterminatePaint(Shape s, ButtonType type) {
        FourColors colors = getProgressBarIndeterminateColors(type);
        return createGradientFourColor(s, colors);
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
            innerRoundedShadow.topColor,
            innerRoundedShadow.bottomColor,
            innerRoundedShadow.bottomColor });
    }

    public static Paint getTopShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minY = (float) bounds.getMinY();
        float maxY = (float) bounds.getMaxY();
        float midX = (float) bounds.getCenterX();
        return createGradient(midX, minY, midX, maxY, new float[] { 0f, 1f }, new Color[] { rectangularShadowDark, transparentColor });
    }

    public static Paint getLeftShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX();
        float maxX = (float) bounds.getMaxX();
        float midY = (float) bounds.getCenterY();
        return createGradient(minX, midY, maxX, midY, new float[] { 0f, 1f }, new Color[] { rectangularShadowLight, transparentColor });
    }

    public static Paint getRightShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX();
        float maxX = (float) bounds.getMaxX();
        float midY = (float) bounds.getCenterY();
        return createGradient(minX - 1, midY, maxX - 1, midY, new float[] { 0f, 1f }, new Color[] {
            transparentColor,
            rectangularShadowLight });
    }

    private static FourLayerColors getButtonColors(ButtonType type, boolean textured) {
        switch (type) {
        case DISABLED:
            return textured ? texturedButtonDisabled : buttonDisabled;
        case DISABLED_SELECTED:
            return textured ? texturedButtonDisabledSelected : buttonDisabledSelected;
        case ENABLED:
            return textured ? texturedButtonEnabled : buttonEnabled;
        case PRESSED:
            return textured ? texturedButtonEnabledPressed : buttonEnabledPressed;
        case DEFAULT:
            return textured ? texturedButtonDefault : buttonDefault;
        case DEFAULT_PRESSED:
            return textured ? texturedButtonDefaultPressed : buttonDefaultPressed;
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

    private static TwoLayerFourColors getScrollBarThumbColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return scrollBarThumbDisabled;
        case ENABLED:
            return scrollBarThumbEnabled;
        case PRESSED:
            return scrollBarThumbPressed;
        }
        return null;
    }

    private static TwoLayerFourColors getCheckBoxColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return checkBoxDisabled;
        case ENABLED:
            return checkBoxEnabled;
        case PRESSED:
            return checkBoxPressed;
        case SELECTED:
            return checkBoxSelected;
        case PRESSED_SELECTED:
            return checkBoxPressedSelected;
        }
        return null;
    }

    private static TwoColors getCheckBoxBulletColors(ButtonType type) {
        switch (type) {
        case DISABLED:
        case DISABLED_SELECTED:
            return checkBoxbulletDisabled;
        case ENABLED:
        case PRESSED:
        case SELECTED:
        case PRESSED_SELECTED:
            return checkBoxBulletEnabled;
        }
        return null;
    }

    private static FourLayerColors getComboBoxButtonColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxButtonDisabled;
        case ENABLED:
            return comboBoxButtonEnabled;
        case PRESSED:
            return comboBoxButtonPressed;
        }
        return null;
    }

    private static FourLayerColors getComboBoxBackgroundColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return comboBoxBackgroundDisabled;
        case ENABLED:
            return comboBoxBackgroundEnabled;
        case PRESSED:
            return comboBoxBackgroundPressed;
        }
        return null;
    }

    private static TwoColors getRootPaneColors(ButtonType type) {
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

    private static FrameColors getFrameColors(ButtonType type) {
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
            return popupMenuEnabledBorder;
        case DISABLED:
            return popupMenuDisabledBorder;
        }
        return null;
    }

    private static Color getPopupMenuInteriorColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return popupMenuEnabledInterior;
        case DISABLED:
            return popupMenuDisabledInterior;
        }
        return null;
    }

    private static TwoColors getProgressBarBorderColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarEnabledTrack;
        case DISABLED:
            return progressBarDisabledTrack;
        }
        return null;
    }

    private static Color getProgressBarTrackColors(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarEnabledTrackInterior;
        case DISABLED:
            return progressBarDisabledTrackInterior;
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
            return progressBarIndeterminatePattern;
        case DISABLED:
            return progressBarIndeterminatePatternDisabled;
        }
        return null;
    }

    private static Color getProgressBarEndColor(ButtonType type) {
        switch (type) {
        case ENABLED:
            return progressBarEnabledEnd;
        case DISABLED:
            return progressBarDisabledEnd;
        }
        return null;
    }

    private static TwoColors getSliderTrackBorderColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackDisabledBorder;
        case ENABLED:
            return sliderTrackEnabledBorder;
        }
        return null;
    }

    private static TwoColors getSliderTrackInteriorColors(ButtonType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackDisabledInterior;
        case ENABLED:
            return sliderTrackEnabledInterior;
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
        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, 1f }, new Color[] { colors.topColor, colors.bottomColor });
    }

    private static Paint createTwoColorGradientHorizontal(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xMin = (float) bounds.getMinX();
        float xMax = (float) bounds.getMaxX();
        float yCenter = (float) bounds.getCenterY();
        return createGradient(xMin, yCenter, xMax, yCenter, new float[] { 0f, 1f }, new Color[] { colors.topColor, colors.bottomColor });
    }

    private static Paint createTwoColorGradientWithMidpointVertical(Shape s, TwoColorsWithMidpoint colors) {
        Color midColor = new Color(deriveARGB(colors.topColor, colors.bottomColor, colors.midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float xCenter = (float) bounds.getCenterX();
        float yMin = (float) bounds.getMinY();
        float yMax = (float) bounds.getMaxY();
        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, colors.midpoint, 1f }, new Color[] {
            colors.topColor,
            midColor,
            colors.bottomColor });
    }

    private static Paint createGradientFourColor(Shape s, FourColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xCenter = (float) bounds.getCenterX();
        float yMin = (float) bounds.getMinY();
        float yMax = (float) bounds.getMaxY();
        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, colors.upperMidpoint, colors.lowerMidpoint, 1f },
            new Color[] { colors.topColor, colors.upperMidColor, colors.lowerMidColor, colors.bottomColor });
    }

    private static Paint createFrameGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, Color topColorT,
        Color topColorB, Color bottomColorT, Color bottomColorB) {
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
            colors = new Color[] { topColorT, topColorB, bottomColorT, bottomColorB };
        } else if (topToolBarHeight > 0) {
            float toolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (toolBarBottom >= 1.0f) {
                toolBarBottom = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, toolBarBottom, 1.0f };
            colors = new Color[] { topColorT, topColorB, bottomColorT };
        } else if (bottomToolBarHeight > 0) {
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, bottomToolBarTop, 1.0f };
            colors = new Color[] { topColorT, topColorB, bottomColorT, bottomColorB };
        } else {
            midPoints = new float[] { 0.0f, titleBottom, 1.0f };
            colors = new Color[] { topColorT, topColorB, topColorB };
        }

        return createGradient(midX, y, x + midX, y + h, midPoints, colors);
    }

    private static Paint createCheckMarkGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient(x + w, y, (0.3f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { colors.topColor, colors.bottomColor });
    }

    private static Paint createScrollBarTrackInnerShadowGradient(Shape s, FourColors colors) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return createGradient(width * 0.5f, 0, width * 0.5f, height - 1, new float[] { 0f, 0.142857143f, 0.5f, 0.785714286f, 1f },
            new Color[] { colors.topColor, colors.upperMidColor, colors.lowerMidColor, colors.lowerMidColor, colors.bottomColor });
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
     * @param upperMidColor
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
     * Derives the ARGB value for a color based on an offset between two other
     * colors.
     * 
     * @param color1
     *            The first color
     * @param upperMidColor
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

    /**
     * Two color gradients.
     */
    private static class TwoColors {
        public Color topColor;
        public Color bottomColor;

        public TwoColors(Color topColor, Color bottomColor) {
            this.topColor = topColor;
            this.bottomColor = bottomColor;
        }
    }

    private static class TwoColorsWithMidpoint extends TwoColors {
        public float midpoint;

        public TwoColorsWithMidpoint(Color topColor, Color bottomColor, float midpoint) {
            super(topColor, bottomColor);
            this.midpoint = midpoint;
        }
    }

    private static class ThreeColors {
        public Color topColor;
        public Color midColor;
        public Color bottomColor;

        public ThreeColors(Color topColor, Color midColor, Color bottomColor) {
            this.topColor = topColor;
            this.midColor = midColor;
            this.bottomColor = bottomColor;
        }
    }

    /**
     * A set of colors to use for scrollbar thumbs and some other controls.
     */
    private static class FourColors {

        public Color topColor;
        public Color upperMidColor;
        public Color lowerMidColor;
        public Color bottomColor;
        public float upperMidpoint;
        public float lowerMidpoint;

        public FourColors(Color topColor, Color upperMidColor, Color lowerMidColor, Color bottomColor, float upperMidpoint,
            float lowerMidpoint) {
            this.topColor = topColor;
            this.upperMidColor = upperMidColor;
            this.lowerMidColor = lowerMidColor;
            this.bottomColor = bottomColor;
            this.upperMidpoint = upperMidpoint;
            this.lowerMidpoint = lowerMidpoint;
        }
    }

    /**
     * A set of colors to use for many controls.
     */
    private static class FourLayerColors {

        public TwoColors             upperShine;
        public TwoColorsWithMidpoint lowerShine;
        public Color                 mainColor;
        public TwoColors             background;

        public FourLayerColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
            float lowerShineMidpoint, Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShine = new TwoColors(upperShineTop, upperShineBottom);
            this.lowerShine = new TwoColorsWithMidpoint(lowerShineTop, lowerShineBottom, lowerShineMidpoint);
            this.mainColor = mainColor;
            this.background = new TwoColors(backgroundTop, backgroundBottom);
        }
    }

    /**
     * A set of colors to use for scrollbar thumbs and some other controls.
     */
    private static class TwoLayerFourColors {

        public FourColors interior;
        public TwoColors  background;

        public TwoLayerFourColors(Color innerTop, Color innerUpperMid, Color innerLowerMid, Color innerBottom, float innerUpperMidpoint,
            float innerLowerMidpoint, Color backgroundTop, Color backgroundBottom) {
            this.interior = new FourColors(innerTop, innerUpperMid, innerLowerMid, innerBottom, innerUpperMidpoint, innerLowerMidpoint);
            this.background = new TwoColors(backgroundTop, backgroundBottom);
        }
    }

    /**
     * A set of colors to use for the button.
     */
    private static class FrameColors {

        public Color topColorT;
        public Color topColorB;
        public Color bottomColorT;
        public Color bottomColorB;

        public FrameColors(Color topColorT, Color topColorB, Color bottomColorT, Color bottomColorB) {
            this.topColorT = topColorT;
            this.topColorB = topColorB;
            this.bottomColorT = bottomColorT;
            this.bottomColorB = bottomColorB;
        }
    }
}
