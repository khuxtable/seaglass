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

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * CheckBoxPainter implementation.
 */
public final class CheckBoxPainter extends AbstractCommonColorsPainter {

    private static final double SIZE_MULTIPLIER = 2.0 / 3.0;
    private static final double X_MULTIPLIER    = 5.0 / 18.0;
    private static final double Y_MULTIPLIER    = 1.0 / 18.0;

    /**
     * Control state.
     */
    public static enum Which {
        ICON_DISABLED, ICON_ENABLED, ICON_FOCUSED, ICON_PRESSED, ICON_PRESSED_FOCUSED, ICON_SELECTED, ICON_SELECTED_FOCUSED,
        ICON_PRESSED_SELECTED, ICON_PRESSED_SELECTED_FOCUSED, ICON_DISABLED_SELECTED,
    }

    private Color buttonBulletBottomEnabled = decodeColor("buttonBulletBottomEnabled");

    private TwoColors buttonBulletEnabled  = new TwoColors(deriveColor(buttonBulletBottomEnabled, 0f, 0f, 0.2f, 0),
                                                           buttonBulletBottomEnabled);
    private TwoColors buttonbulletDisabled = disable(buttonBulletEnabled);

    private PaintContext ctx;
    private boolean      focused;
    private boolean      selected;

    private Effect dropShadow = new SeaGlassDropShadowEffect();

    private CommonControlState type;

    /**
     * Creates a new CheckBoxPainter object.
     *
     * @param state the control state to paint.
     */
    public CheckBoxPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getControlType(state);

        focused  = false;
        selected = false;

        if (state == Which.ICON_FOCUSED || state == Which.ICON_PRESSED_FOCUSED || state == Which.ICON_SELECTED_FOCUSED
                || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            focused = true;
        }

        if (state == Which.ICON_SELECTED || state == Which.ICON_PRESSED_SELECTED || state == Which.ICON_DISABLED_SELECTED
                || state == Which.ICON_SELECTED_FOCUSED || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            selected = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = width < height ? width : height;
        int x    = (width - size) / 2;
        int y    = (height - size) / 2;

        Shape s;

        if (focused) {
            boolean useToolBarFocus = isInToolBar(c);

            s = shapeGenerator.createRoundRectangle(x, y, size, size, CornerSize.CHECKBOX_OUTER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarFocus));
            g.fill(s);
            s = shapeGenerator.createRoundRectangle(x + 1, y + 1, size - 2, size - 2, CornerSize.CHECKBOX_INNER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarFocus));
            g.fill(s);
        }

        s = shapeGenerator.createRoundRectangle(x + 2, y + 2, size - 4, size - 4, CornerSize.CHECKBOX_BORDER);

        if (!focused) {
            dropShadow.fill(g, s);
        }

        g.setPaint(getCommonBorderPaint(s, type));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(x + 3, y + 3, size - 6, size - 6, CornerSize.CHECKBOX_INTERIOR);
        g.setPaint(getCommonInteriorPaint(s, type));
        g.fill(s);

        if (selected) {
            s = createCheckMark(x, y, size);
            g.setPaint(getCheckBoxBulletPaint(s, type));
            g.fill(s);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Get the CommonControlState for the state.
     *
     * @param  state the control state.
     *
     * @return the common control color state.
     */
    private CommonControlState getControlType(Which state) {
        switch (state) {

        case ICON_DISABLED:
            return CommonControlState.DISABLED;

        case ICON_ENABLED:
        case ICON_FOCUSED:
            return CommonControlState.ENABLED;

        case ICON_PRESSED:
        case ICON_PRESSED_FOCUSED:
            return CommonControlState.PRESSED;

        case ICON_SELECTED:
        case ICON_SELECTED_FOCUSED:
            return CommonControlState.SELECTED;

        case ICON_PRESSED_SELECTED:
        case ICON_PRESSED_SELECTED_FOCUSED:
            return CommonControlState.PRESSED_SELECTED;

        case ICON_DISABLED_SELECTED:
            return CommonControlState.DISABLED_SELECTED;
        }

        return null;
    }

    /**
     * Create the check mark shape.
     *
     * @param  x    the x coordinate of the upper-left corner of the check mark.
     * @param  y    the y coordinate of the upper-left corner of the check mark.
     * @param  size the check mark size in pixels.
     *
     * @return the check mark shape.
     */
    private Shape createCheckMark(int x, int y, int size) {
        int markSize = (int) (size * SIZE_MULTIPLIER + 0.5);
        int markX    = x + (int) (size * X_MULTIPLIER + 0.5);
        int markY    = y + (int) (size * Y_MULTIPLIER + 0.5);

        return shapeGenerator.createCheckMark(markX, markY, markSize, markSize);
    }

    /**
     * Create the paint for the check mark.
     *
     * @param  s    the check mark shape. Used for bounds.
     * @param  type the color type to create.
     *
     * @return the paint to use for the check mark.
     */
    public Paint getCheckBoxBulletPaint(Shape s, CommonControlState type) {
        return createCheckMarkGradient(s, getCheckBoxBulletColors(type));
    }

    /**
     * Get the colors to use for the check mark.
     *
     * @param  type the state.
     *
     * @return the color set.
     */
    private TwoColors getCheckBoxBulletColors(CommonControlState type) {
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

    /**
     * Creates the gradient to use for the check mark.
     *
     * @param  s      the shape of the check mark. Used for bounds.
     * @param  colors the color set.
     *
     * @return the paint for the check mark.
     */
    private Paint createCheckMarkGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float       x      = (float) bounds.getX();
        float       y      = (float) bounds.getY();
        float       w      = (float) bounds.getWidth();
        float       h      = (float) bounds.getHeight();

        return createGradient(x + w, y, (0.3f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }
}
