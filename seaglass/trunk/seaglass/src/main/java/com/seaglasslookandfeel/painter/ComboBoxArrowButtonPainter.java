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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

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

    private static final Color ENABLED_ARROW_COLOR  = new Color(0x000000);
    private static final Color DISABLED_ARROW_COLOR = new Color(0x9ba8cf);

    public ButtonStateColors   enabled;
    public ButtonStateColors   pressed;
    public ButtonStateColors   disabled;

    private Which              state;
    private PaintContext       ctx;

    public ComboBoxArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

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

        Shape s = decodeBorder(width, height);
        g.setPaint(decodeGradientBackground(s, colors.backgroundTop, colors.backgroundBottom));
        g.fill(s);
        s = decodeInterior(width, height);
        g.setColor(colors.mainColor);
        g.fill(s);
        g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(s);
        g.setPaint(decodeGradientTopShine(s, colors.upperShineTop, colors.upperShineBottom));
        g.fill(s);
    }

    private void paintArrowsEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(0.5, 0.5, 3, 4);
        g.setColor(ENABLED_ARROW_COLOR);
        g.fill(s);

        s = ShapeUtil.createArrowRight(6.5, 0.5, 3, 4);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowsDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 5;
        int yOffset = height / 2 - 3;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(0.5, 0.5, 3, 4);
        g.setColor(DISABLED_ARROW_COLOR);
        g.fill(s);

        s = ShapeUtil.createArrowRight(6.5, 0.5, 3, 4);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownEnabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(1, 1, 4.2, 6);
        g.setColor(ENABLED_ARROW_COLOR);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private void paintArrowDownDisabled(Graphics2D g, JComponent c, int width, int height) {
        int xOffset = width / 2 - 3;
        int yOffset = height / 2 - 5;
        g.translate(xOffset, yOffset);

        Shape s = ShapeUtil.createArrowLeft(1, 1, 4.2, 6);
        g.setColor(DISABLED_ARROW_COLOR);
        g.fill(s);

        g.translate(-xOffset, -yOffset);
    }

    private Shape decodeBorder(int width, int height) {
        return decodeButtonPath(CornerSize.BORDER, 0, 2, width - 2, height - 4);
    }

    private Shape decodeInterior(int width, int height) {
        return decodeButtonPath(CornerSize.INTERIOR, 1, 3, width - 4, height - 6);
    }

    private Shape decodeButtonPath(CornerSize size, int x, int y, int w, int h) {
        return ShapeUtil.createQuad(x, y, w, h, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED, CornerStyle.ROUNDED);
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
