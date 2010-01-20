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
import com.seaglasslookandfeel.state.ControlInToolBarState;
import com.seaglasslookandfeel.state.State;

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

    private static final State         inToolBarState         = new ControlInToolBarState();

    private Color                      outerFocusColor        = decodeColor("seaGlassOuterFocus", 0f, 0f, 0f, 0);
    private Color                      innerFocusColor        = decodeColor("seaGlassFocus", 0f, 0f, 0f, 0);
    private Color                      outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus", 0f, 0f, 0f, 0);
    private Color                      innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus", 0f, 0f, 0f, 0);
    private Color                      outerShadowColor       = new Color(0x0a000000, true);
    private Color                      innerShadowColor       = new Color(0x1c000000, true);

    public ButtonStateColors           enabled;
    public ButtonStateColors           pressed;
    public ButtonStateColors           disabled;

    private ComboBoxArrowButtonPainter buttonPainter;

    private static final Insets        insets                 = new Insets(8, 9, 8, 23);
    private static final Dimension     dimension              = new Dimension(105, 23);
    private static final CacheMode     cacheMode              = CacheMode.FIXED_SIZES;
    private static final Double        maxH                   = Double.POSITIVE_INFINITY;
    private static final Double        maxV                   = Double.POSITIVE_INFINITY;

    private static final Insets        editableInsets         = new Insets(0, 0, 0, 0);
    private static final Insets        focusInsets            = new Insets(5, 5, 5, 5);
    private static final int           buttonWidth            = 21;

    private Path2D                     path                   = new Path2D.Double();

    private Which                      state;
    private PaintContext               ctx;
    private boolean                    editable;

    public ComboBoxPainter(Which state) {
        super();
        this.state = state;
        editable = false;
        if (state == Which.BACKGROUND_DISABLED_EDITABLE || state == Which.BACKGROUND_ENABLED_EDITABLE
                || state == Which.BACKGROUND_PRESSED_EDITABLE) {
            editable = true;
            ctx = new PaintContext(editableInsets, dimension, false, cacheMode, maxH, maxV);
        } else if (state == Which.BACKGROUND_FOCUSED_EDITABLE) {
            editable = true;
            ctx = new PaintContext(focusInsets, dimension, false, cacheMode, maxH, maxV);
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
            ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
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

    private void paintFocus(Graphics2D g, JComponent c, int width, int height) {
        boolean inToolBar = inToolBarState.isInState(c);
        g.setColor(inToolBar ? outerToolBarFocusColor : outerFocusColor);
        setPath(0, 0, width, height, 6);
        g.fill(path);
        g.setColor(inToolBar ? innerToolBarFocusColor : innerFocusColor);
        setPath(1, 1, width - 2, height - 2, 5);
        g.fill(path);
    }

    private void paintDropShadow(Graphics2D g, int width, int height, boolean full) {
        Shape s = g.getClip();
        if (full) {
            g.setClip(0, 0, width, height);
        } else {
            g.setClip(width - buttonWidth, 0, buttonWidth, height);
        }
        g.setColor(outerShadowColor);
        setPath(1, 2, width - 2, height - 2, 5);
        g.fill(path);
        g.setColor(innerShadowColor);
        setPath(2, 2, width - 4, height - 3, 5);
        g.fill(path);
        g.setClip(s);
    }

    private Path2D decodeBorder(double width, double height) {
        double arcSize = 4.0;
        double x = 2.0;
        double y = 2.0;
        width -= 2.0;
        height -= 4.0;
        decodeButtonPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    private Path2D decodeInterior(double width, double height) {
        double arcSize = 3.0;
        double x = 3.0;
        double y = 3.0;
        width -= 3.0;
        height -= 6.0;
        decodeButtonPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    private void decodeButtonPath(Double left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);
        path.closePath();
    }

    private void setPath(int x, int y, int width, int height, int arc) {
        path.reset();
        if (editable) {
            path.moveTo(x, y);
            path.lineTo(x, y + height);
        } else {
            path.moveTo(x + arc, y);
            path.quadTo(x, y, x, y + arc);
            path.lineTo(x, y + height - arc);
            path.quadTo(x, y + height, x + arc, y + height);
        }
        path.lineTo(x + width - arc, y + height);
        path.quadTo(x + width, y + height, x + width, y + height - arc);
        path.lineTo(x + width, y + arc);
        path.quadTo(x + width, y, x + width - arc, y);
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
