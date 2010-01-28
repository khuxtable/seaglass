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
 * ComboBoxPainter implementation.
 */
public final class ComboBoxPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_DISABLED_PRESSED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SELECTED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_FOCUSED_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
    }

    private Color                      outerFocusColor        = decodeColor("seaGlassOuterFocus");
    private Color                      innerFocusColor        = decodeColor("seaGlassFocus");
    private Color                      outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus");
    private Color                      innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus");
    private Color                      outerShadowColor       = new Color(0x0a000000, true);
    private Color                      innerShadowColor       = new Color(0x1c000000, true);

    public ButtonStateColors           enabled;
    public ButtonStateColors           pressed;
    public ButtonStateColors           disabled;

    private ComboBoxArrowButtonPainter buttonPainter;

    // TODO Get this from the UI.
    private static final int           buttonWidth            = 21;

    private Which                      state;
    private PaintContext               ctx;
    private boolean                    editable;

    public ComboBoxPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        editable = false;
        if (state == Which.BACKGROUND_DISABLED_EDITABLE || state == Which.BACKGROUND_ENABLED_EDITABLE
                || state == Which.BACKGROUND_PRESSED_EDITABLE) {
            editable = true;
        } else if (state == Which.BACKGROUND_FOCUSED_EDITABLE) {
            editable = true;
        } else {
            ComboBoxArrowButtonPainter.Which arrowState;
            if (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_DISABLED_PRESSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_DISABLED_EDITABLE;
            } else if (state == Which.BACKGROUND_PRESSED || state == Which.BACKGROUND_PRESSED_FOCUSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_PRESSED_EDITABLE;
            } else {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_ENABLED_EDITABLE;
            }
            buttonPainter = new ComboBoxArrowButtonPainter(arrowState);
        }

        // Set the default colors.
        setEnabled(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff, true), new Color(
            0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf)));
        setPressed(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff, true), new Color(
            0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf)));
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
        case BACKGROUND_DISABLED:
        case BACKGROUND_DISABLED_PRESSED:
            paintDropShadow(g, width, height, true);
            paintDisabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintDropShadow(g, width, height, true);
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_FOCUSED:
            paintFocus(g, c, width, height);
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_PRESSED_FOCUSED:
            paintFocus(g, c, width, height);
            paintPressed(g, c, width, height);
            break;
        case BACKGROUND_PRESSED:
        case BACKGROUND_ENABLED_SELECTED:
            paintDropShadow(g, width, height, true);
            paintPressed(g, c, width, height);
            break;
        case BACKGROUND_FOCUSED_EDITABLE:
            paintFocus(g, c, width, height);
            break;
        case BACKGROUND_DISABLED_EDITABLE:
        case BACKGROUND_ENABLED_EDITABLE:
        case BACKGROUND_PRESSED_EDITABLE:
            paintDropShadow(g, width, height, false);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width - buttonWidth, height, disabled);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(width - buttonWidth, 0);
        buttonPainter.doPaint(g2, c, buttonWidth, height, null);
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width - buttonWidth, height, enabled);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(width - buttonWidth, 0);
        buttonPainter.doPaint(g2, c, buttonWidth, height, null);
    }

    private void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paintButton(g, c, width - buttonWidth, height, pressed);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(width - buttonWidth, 0);
        buttonPainter.doPaint(g2, c, buttonWidth, height, null);
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

    private void paintFocus(Graphics2D g, JComponent c, int width, int height) {
        g.setColor(isInToolBar(c) ? outerToolBarFocusColor : outerFocusColor);
        Shape s = setPath(CornerSize.OUTER_FOCUS, 0, 0, width, height);
        g.fill(s);
        g.setColor(isInToolBar(c) ? innerToolBarFocusColor : innerFocusColor);
        s = setPath(CornerSize.INNER_FOCUS, 1, 1, width - 2, height - 2);
        g.fill(s);
    }

    private void paintDropShadow(Graphics2D g, int width, int height, boolean full) {
        // FIXME Make this work again.
//        Shape s = g.getClip();
//        if (full) {
//            g.setClip(0, 0, width, height);
//        } else {
//            g.setClip(width - buttonWidth, 0, buttonWidth, height);
//        }
//        g.setColor(outerShadowColor);
//        s = setPath(CornerSize.OUTER_FOCUS, 1, 2, width - 2, height - 2);
//        g.fill(s);
//        g.setColor(innerShadowColor);
//        s = setPath(CornerSize.INNER_FOCUS, 2, 2, width - 4, height - 3);
//        g.fill(s);
//        g.setClip(s);
    }

    private Shape decodeBorder(int width, int height) {
        return decodeButtonPath(CornerSize.BORDER, 2, 2, width - 2, height - 4);
    }

    private Shape decodeInterior(int width, int height) {
        return decodeButtonPath(CornerSize.INTERIOR, 3, 3, width - 3, height - 6);
    }

    private Shape decodeButtonPath(CornerSize size, int left, int top, int width, int height) {
        return ShapeUtil.createQuad(size, left, top, width, height, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.SQUARE,
            CornerStyle.SQUARE);
    }

    private Shape setPath(CornerSize size, int x, int y, int width, int height) {
        CornerStyle leftStyle = editable ? CornerStyle.SQUARE : CornerStyle.ROUNDED;
        return ShapeUtil.createQuad(size, x, y, width, height, leftStyle, leftStyle, CornerStyle.ROUNDED, CornerStyle.ROUNDED);
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
