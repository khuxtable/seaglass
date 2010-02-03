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

    private static Color     outerFocus;
    private static Color     innerFocus;
    private static Color     outerToolBarFocus;
    private static Color     innerToolBarFocus;

    private static TwoColors scrollBarCapColors;

    private static TwoColors scrollBarButtonIncreaseApart;
    private static TwoColors scrollBarButtonIncreaseTogether;
    private static TwoColors scrollBarButtonIncreasePressed;

    private static TwoColors scrollBarButtonDecreaseApart;
    private static TwoColors scrollBarButtonDecreaseTogether;
    private static TwoColors scrollBarButtonDecreasePressed;

    private static Color     scrollBarButtonLine;
    private static Color     scrollBarButtonLinePressed;
    private static Color     scrollBarButtonArrow;
    private static Color     scrollBarButtonArrowDisabled;

    private static Color     scrollBarButtonDarkDivider;
    private static Color     scrollBarButtonLightDivider;

    private static Color     tabbedPaneTabAreaBackLineEnabled;
    private static Color     tabbedPaneTabAreaBackLineDisabled;
    private static Color     tabbedPaneTabAreaLightShadow;
    private static Color     tabbedPaneTabAreaDarkShadow;

    private static Color     textComponentBorderDisabled;
    private static Color     textComponentBorderEnabled;
    private static Color     textComponentBorderEnabledToolbar;

    static {
        outerFocus = decodeColor("seaGlassOuterFocus");
        innerFocus = decodeColor("seaGlassFocus");
        outerToolBarFocus = decodeColor("seaGlassToolBarOuterFocus");
        innerToolBarFocus = decodeColor("seaGlassToolBarFocus");

        Color scrollBarButtonBase = decodeColor("scrollBarButtonBase");
        Color scrollBarButtonBasePressed = decodeColor("scrollBarButtonBasePressed");

        tabbedPaneTabAreaBackLineEnabled = decodeColor("tabbedPaneTabAreaBackLineEnabled");
        tabbedPaneTabAreaLightShadow = decodeColor("tabbedPaneTabAreaLightShadow");
        tabbedPaneTabAreaDarkShadow = decodeColor("tabbedPaneTabAreaDarkShadow");

        textComponentBorderEnabled = decodeColor("seaGlassTextEnabledBorder");
        textComponentBorderEnabledToolbar = decodeColor("seaGlassTextEnabledToolbarBorder");
        textComponentBorderDisabled = decodeColor("seaGlassTextDisabledBorder");

        // ------- Assign from base colors -------

        scrollBarCapColors = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f, -0.266667f, 0));

        Color scrollBarButtonTopPressed = deriveColor(scrollBarButtonBasePressed, 0.000737f, -0.105657f, 0.101961f, 0);
        Color scrollBarButtonMiddlePressed = deriveColor(scrollBarButtonBasePressed, 0.001240f, -0.041156f, 0.035294f, 0);
        Color scrollBarButtonBottomPressed = deriveColor(scrollBarButtonBasePressed, 0.000348f, 0.050949f, -0.039216f, 0);
        Color scrollBarButtonLinePressedColor = deriveColor(scrollBarButtonBasePressed, -0.001400f, 0.110160f, -0.043137f, 0);

        scrollBarButtonIncreaseApart = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0), scrollBarButtonBase);
        scrollBarButtonIncreaseTogether = new TwoColors(deriveColor(scrollBarButtonBase, 0f, 0f, -0.180392f, 0), deriveColor(
            scrollBarButtonBase, 0f, 0f, -0.101961f, 0));
        scrollBarButtonIncreasePressed = new TwoColors(scrollBarButtonMiddlePressed, scrollBarButtonBottomPressed);

        scrollBarButtonDecreaseApart = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f, -0.2f, 0));
        scrollBarButtonDecreaseTogether = new TwoColors(scrollBarButtonBase, deriveColor(scrollBarButtonBase, 0f, 0f, -0.086275f, 0));
        scrollBarButtonDecreasePressed = new TwoColors(scrollBarButtonTopPressed, scrollBarButtonMiddlePressed);

        scrollBarButtonLine = deriveColor(scrollBarButtonBase, 0f, 0f, -0.258824f, 0);
        scrollBarButtonLinePressed = scrollBarButtonLinePressedColor;
        scrollBarButtonArrow = deriveColor(scrollBarButtonBase, 0f, 0f, -0.666667f, 0);
        scrollBarButtonArrowDisabled = disable(scrollBarButtonArrow);

        scrollBarButtonDarkDivider = deriveColor(scrollBarButtonBase, 0f, 0f, -1f, -(int) (scrollBarButtonBase.getAlpha() * 0.87843137f));
        scrollBarButtonLightDivider = deriveColor(scrollBarButtonBase, 0f, 0f, 0f, -(int) (scrollBarButtonBase.getAlpha() * 0.75294117647f));

        tabbedPaneTabAreaBackLineDisabled = disable(tabbedPaneTabAreaBackLineEnabled);
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

    public static Paint getFocusPaint(Shape s, FocusType focusType, boolean useToolBarFocus) {
        if (focusType == FocusType.OUTER_FOCUS) {
            return useToolBarFocus ? outerToolBarFocus : outerFocus;
        } else {
            return useToolBarFocus ? innerToolBarFocus : innerFocus;
        }
    }

    public static Paint getScrollBarButtonBackgroundPaint(Shape s, ButtonType type, boolean isIncrease, boolean buttonsTogether) {
        TwoColors colors = getScrollBarButtonBackgroundColors(type, isIncrease, buttonsTogether);
        return createHorizontalGradient(s, colors);
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

    private static Paint createHorizontalGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float xMin = (float) bounds.getMinX();
        float xMax = (float) bounds.getMaxX();
        float yCenter = (float) bounds.getCenterY();
        return createGradient(xMin, yCenter, xMax, yCenter, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
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
    public static class TwoColors {
        public Color top;
        public Color bottom;

        public TwoColors(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }

    /**
     * A set of colors to use for scrollbar thumbs and some other controls.
     */
    public static class FourColors extends TwoColors {
        public Color upperMid;
        public Color lowerMid;

        public FourColors(Color top, Color upperMid, Color lowerMid, Color bottom) {
            super(top, bottom);
            this.upperMid = upperMid;
            this.lowerMid = lowerMid;
        }
    }
}
