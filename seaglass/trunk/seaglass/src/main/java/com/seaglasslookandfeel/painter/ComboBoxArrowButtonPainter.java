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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ComboBoxArrowButtonPainter implementation.
 */
public final class ComboBoxArrowButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_PRESSED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
        BACKGROUND_SELECTED_EDITABLE,
        FOREGROUND_ENABLED,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_SELECTED,
        FOREGROUND_EDITABLE,
        FOREGROUND_EDITABLE_DISABLED,
    }

    private static final Color     ENABLED_ARROW_COLOR  = new Color(0x000000);
    private static final Color     DISABLED_ARROW_COLOR = new Color(0x9ba8cf);

    public ButtonStateColors       enabled;
    public ButtonStateColors       pressed;
    public ButtonStateColors       disabled;

    private static final Insets    bgInsets             = new Insets(4, 1, 4, 4);
    private static final Dimension bgDimension          = new Dimension(21, 23);

    private static final Insets    fgInsets             = new Insets(0, 0, 0, 0);
    private static final Dimension fgDimension          = new Dimension(10, 6);

    private static final Dimension fgEditableDimension  = new Dimension(6, 9);

    private Path2D                 path                 = new Path2D.Double();

    private Which                  state;
    private PaintContext           ctx;

    public ComboBoxArrowButtonPainter(Which state) {
        super();
        this.state = state;

        Insets ins = bgInsets;
        Dimension dim = bgDimension;
        if (state == Which.FOREGROUND_ENABLED || state == Which.FOREGROUND_DISABLED || state == Which.FOREGROUND_PRESSED
                || state == Which.FOREGROUND_SELECTED) {
            ins = fgInsets;
            dim = fgDimension;
        } else if (state == Which.FOREGROUND_EDITABLE || state == Which.FOREGROUND_EDITABLE_DISABLED) {
            ins = fgInsets;
            dim = fgEditableDimension;
        }
        ctx = new PaintContext(ins, dim, false, CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Set the default colors.
        setEnabled(new ButtonStateColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true), new Color(
            0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setPressed(new ButtonStateColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true), new Color(
            0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDisabled(new ButtonStateColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true), new Color(
            0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF)));
    }

    public void setEnabled(ButtonStateColors enabled) {
        this.enabled = enabled;
    }

    public void setPressed(ButtonStateColors enabledPressed) {
        this.pressed = enabledPressed;
    }

    public void setDisabled(ButtonStateColors defaultButton) {
        this.disabled = defaultButton;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED_EDITABLE:
            paintDisabledEditable(g, c, width, height);
            break;
        case BACKGROUND_ENABLED_EDITABLE:
            paintEnabledEditable(g, c, width, height);
            break;
        case BACKGROUND_PRESSED_EDITABLE:
            paintPressedEditable(g, c, width, height);
            break;
        case BACKGROUND_SELECTED_EDITABLE:
            paintPressedEditable(g, c, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintArrowsDisabled(g, c, width, height);
            break;
        case FOREGROUND_PRESSED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_SELECTED:
            paintArrowsEnabled(g, c, width, height);
            break;
        case FOREGROUND_EDITABLE:
            paintArrowDownEnabled(g, c, width, height);
            break;
        case FOREGROUND_EDITABLE_DISABLED:
            paintArrowDownDisabled(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabledEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, disabled);
    }

    private void paintEnabledEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, enabled);
    }

    private void paintPressedEditable(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width, height, pressed);
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        path = decodeBorder(width, height);
        g.setPaint(decodeGradientBackground(path, colors.backgroundTop, colors.backgroundBottom));
        g.fill(path);
        path = decodeInterior(width, height);
        g.setColor(colors.mainColor);
        g.fill(path);
        g.setPaint(decodeGradientBottomShine(path, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(path);
        g.setPaint(decodeGradientTopShine(path, colors.upperShineTop, colors.upperShineBottom));
        g.fill(path);
    }

    private void paintArrowsEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        g.setColor(ENABLED_ARROW_COLOR);

        path.reset();
        path.moveTo(3.5, 0.5);
        path.lineTo(0.5, 2.5);
        path.lineTo(3.5, 4.5);
        path.closePath();
        g.fill(path);

        path.reset();
        path.moveTo(6.5, 0.5);
        path.lineTo(9.5, 2.5);
        path.lineTo(6.5, 4.5);
        path.closePath();
        g.fill(path);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowsDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        g.setColor(DISABLED_ARROW_COLOR);

        path.reset();
        path.moveTo(3.5, 0.5);
        path.lineTo(0.5, 2.5);
        path.lineTo(3.5, 4.5);
        path.closePath();
        g.fill(path);

        path.reset();
        path.moveTo(6.5, 0.5);
        path.lineTo(9.5, 2.5);
        path.lineTo(6.5, 4.5);
        path.closePath();
        g.fill(path);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        path.reset();
        path.moveTo(5.2, 1.0);
        path.lineTo(1.0, 4.0);
        path.lineTo(5.2, 7.0);
        path.closePath();
        g.setColor(ENABLED_ARROW_COLOR);
        g.fill(path);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        path.reset();
        path.moveTo(5.2, 1.0);
        path.lineTo(1.0, 4.0);
        path.lineTo(5.2, 7.0);
        path.closePath();
        g.setColor(DISABLED_ARROW_COLOR);
        g.fill(path);

        g.translate(-xOffset, -yOffset);
    }

    Path2D decodeBorder(double width, double height) {
        double arcSize = 4.0;
        double x = 0.0;
        double y = 2.0;
        width -= 2.0;
        height -= 4.0;
        decodeButtonPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    Path2D decodeInterior(double width, double height) {
        double arcSize = 3.0;
        double x = 1.0;
        double y = 3.0;
        width -= 4.0;
        height -= 6.0;
        decodeButtonPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    private void decodeButtonPath(Double left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
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

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the button.
     */
    public class ButtonStateColors {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public ButtonStateColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
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
}
