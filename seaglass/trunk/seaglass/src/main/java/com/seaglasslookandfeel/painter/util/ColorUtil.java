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
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.UIManager;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * @author Kathryn Huxtable
 */
public class ColorUtil {

    public enum ButtonType {
        ENABLED, PRESSED, DEFAULT, DEFAULT_PRESSED, DISABLED, DISABLED_SELECTED, SELECTED, PRESSED_SELECTED, ACTIVE, INACTIVE,
    }

    private static FourLayerColors    buttonEnabled;
    private static FourLayerColors    buttonEnabledPressed;
    private static FourLayerColors    buttonDefault;
    private static FourLayerColors    buttonDefaultPressed;
    private static FourLayerColors    buttonDisabled;
    private static FourLayerColors    buttonDisabledSelected;

    private static FourLayerColors    texturedEnabled;
    private static FourLayerColors    texturedEnabledPressed;
    private static FourLayerColors    texturedDefault;
    private static FourLayerColors    texturedDefaultPressed;
    private static FourLayerColors    texturedDisabled;
    private static FourLayerColors    texturedDisabledSelected;

    private static TwoLayerFourColors scrollBarThumbDisabled;
    private static TwoLayerFourColors scrollBarThumbEnabled;
    private static TwoLayerFourColors scrollBarThumbPressed;

    private static TwoLayerFourColors checkBoxDisabled;
    private static TwoLayerFourColors checkBoxEnabled;
    private static TwoLayerFourColors checkBoxPressed;
    private static TwoLayerFourColors checkBoxSelected;
    private static TwoLayerFourColors checkBoxPressedSelected;

    private static FourLayerColors    comboBoxButtonDisabled;
    private static FourLayerColors    comboBoxButtonEnabled;
    private static FourLayerColors    comboBoxButtonPressed;

    private static FourLayerColors    comboBoxBackgroundDisabled;
    private static FourLayerColors    comboBoxBackgroundEnabled;
    private static FourLayerColors    comboBoxBackgroundPressed;

    private static TwoColors          rootPaneActive;
    private static TwoColors          rootPaneInactive;

    static {
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

        texturedEnabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0,
            true), 0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c));
        texturedEnabledPressed = new FourLayerColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(
            0xffcccccc, true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c));
        texturedDefault = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0,
            true), 0.5f, new Color(0x999999), new Color(0x555555), new Color(0x4c4c4c));
        texturedDefaultPressed = new FourLayerColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(
            0xffcccccc, true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c));
        texturedDisabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0,
            true), 0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c));
        texturedDisabledSelected = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true),
            new Color(0, true), 0.5f, new Color(0xaaaaaa), new Color(0x555555), new Color(0x4c4c4c));

        scrollBarThumbDisabled = new TwoLayerFourColors(new Color(0x80fbfdfe, true), new Color(0x80d6eaf9, true), new Color(0x80d2e8f8,
            true), new Color(0x80f5fafd, true), 0.45f, 0.62f, new Color(0x6088ade0, true), new Color(0x605785bf, true));
        scrollBarThumbEnabled = new TwoLayerFourColors(new Color(0xfbfdfe), new Color(0xd6eaf9), new Color(0xd2e8f8), new Color(0xf5fafd),
            0.45f, 0.62f, new Color(0x88ade0), new Color(0x5785bf));
        scrollBarThumbPressed = new TwoLayerFourColors(new Color(0xb1dbf5), new Color(0x7ca7ce), new Color(0x7ea7cc), new Color(0xbbcedf),
            0.45f, 0.62f, new Color(0x4076bf), new Color(0x4f7bbf));

        checkBoxDisabled = scrollBarThumbDisabled;
        checkBoxEnabled = scrollBarThumbEnabled;
        checkBoxPressed = new TwoLayerFourColors(new Color(0xacbdd0), new Color(0x688db3), new Color(0x6d93ba), new Color(0xa4cbe4), 0.45f,
            0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));

        checkBoxSelected = new TwoLayerFourColors(new Color(0xbccedf), new Color(0x7fa7cd), new Color(0x82b0d6), new Color(0xb0daf6),
            0.45f, 0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));

        checkBoxPressedSelected = new TwoLayerFourColors(new Color(0xacbdd0), new Color(0x688db3), new Color(0x6d93ba),
            new Color(0xa4cbe4), 0.45f, 0.62f, new Color(0x4f7bbf), new Color(0x3f76bf));

        comboBoxButtonDisabled = new FourLayerColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF));
        comboBoxButtonEnabled = new FourLayerColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF));
        comboBoxButtonPressed = new FourLayerColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF));

        comboBoxBackgroundDisabled = new FourLayerColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC,
            true), new Color(0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF));
        comboBoxBackgroundEnabled = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff,
            true), new Color(0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf));
        comboBoxBackgroundPressed = new FourLayerColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff,
            true), new Color(0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf));

        rootPaneActive = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"), decodeColor("seaGlassToolBarActiveBottomB"));
        rootPaneInactive = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"), decodeColor("seaGlassToolBarInactiveBottomB"));
    }

    public static FourLayerColors getButtonColors(ButtonType type, boolean textured) {
        switch (type) {
        case DISABLED:
            return textured ? texturedDisabled : buttonDisabled;
        case DISABLED_SELECTED:
            return textured ? texturedDisabledSelected : buttonDisabledSelected;
        case ENABLED:
            return textured ? texturedEnabled : buttonEnabled;
        case PRESSED:
            return textured ? texturedEnabledPressed : buttonEnabledPressed;
        case DEFAULT:
            return textured ? texturedDefault : buttonDefault;
        case DEFAULT_PRESSED:
            return textured ? texturedDefaultPressed : buttonDefaultPressed;
        }
        return null;
    }

    public static TwoLayerFourColors getTwoLayerColors(ButtonType type) {
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

    public static TwoLayerFourColors getTwoLayerColors3(ButtonType type) {
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

    public static FourLayerColors getFourLayerColors4(ButtonType type) {
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

    public static FourLayerColors getFourLayerColors5(ButtonType type) {
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

    public static TwoColors getRootPaneColors(ButtonType type) {
        switch (type) {
        case ACTIVE:
            return rootPaneActive;
        case INACTIVE:
            return rootPaneInactive;
        }
        return null;
    }

    public static void paintTwoColorGradientVertical(Graphics2D g, Shape s, Color color1, Color color2) {
        g.setPaint(decodeTwoColorGradientVertical(s, color1, color2));
        g.fill(s);
    }

    private static Paint decodeTwoColorGradientVertical(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    public static void paintThreeLayerGradientVertical(Graphics2D g, Shape s, FourLayerColors colors) {
        g.setColor(colors.mainColor);
        g.fill(s);
        g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(s);
        g.setPaint(decodeGradientTopShine(s, colors.upperShineTop, colors.upperShineBottom));
        g.fill(s);
    }

    private static Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    private static Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    public static void paintTwoLayerFourColorGradientVertical(Graphics2D g, Shape s, TwoLayerFourColors colors) {
        g.setPaint(decodeGradientFourColor(s, colors.inner1, colors.inner2, colors.inner3, colors.inner4, colors.midpoint2,
            colors.midpoint3));
        g.fill(s);
    }

    private static Paint decodeGradientFourColor(Shape s, Color color1, Color color2, Color color3, Color color4, float midpoint2,
        float midpoint3) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint2, midpoint3, 1f }, new Color[] {
            color1,
            color2,
            color3,
            color4 });
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
    private static final LinearGradientPaint decodeGradient(float x1, float y1, float x2, float y2, float[] midpoints, Color[] colors) {
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
     * @param color2
     *            The second color
     * @param midPoint
     *            The offset between color 1 and color 2, a value of 0.0 is
     *            color 1 and 1.0 is color 2;
     * @return The derived color
     */
    private static final Color decodeColor(Color color1, Color color2, float midPoint) {
        return new Color(deriveARGB(color1, color2, midPoint));
    }

    /**
     * Derives the ARGB value for a color based on an offset between two other
     * colors.
     * 
     * @param color1
     *            The first color
     * @param color2
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
    public static class TwoColors {
        public Color topColor;
        public Color bottomColor;

        public TwoColors(Color topColor, Color bottomColor) {
            this.topColor = topColor;
            this.bottomColor = bottomColor;
        }
    }

    /**
     * A set of colors to use for many controls.
     */
    public static class FourLayerColors {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public FourLayerColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
            float lowerShineMidpoint, Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShineTop = upperShineTop;
            this.upperShineBottom = upperShineBottom;
            this.lowerShineTop = lowerShineTop;
            this.lowerShineBottom = lowerShineBottom;
            this.lowerShineMidpoint = lowerShineMidpoint;
            this.mainColor = mainColor;
            this.backgroundTop = backgroundTop;
            this.backgroundBottom = backgroundBottom;
        }
    }

    /**
     * A set of colors to use for scrollbar thumbs and some other controls.
     */
    public static class TwoLayerFourColors {

        public Color inner1;
        public Color inner2;
        public Color inner3;
        public Color inner4;
        public float midpoint2;
        public float midpoint3;
        public Color background1;
        public Color background2;

        public TwoLayerFourColors(Color inner1, Color inner2, Color inner3, Color inner4, float midpoint2, float midpoint3,
            Color background1, Color background2) {
            this.inner1 = inner1;
            this.inner2 = inner2;
            this.inner3 = inner3;
            this.inner4 = inner4;
            this.midpoint2 = midpoint2;
            this.midpoint3 = midpoint3;
            this.background1 = background1;
            this.background2 = background2;
        }
    }
}
