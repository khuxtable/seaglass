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
package com.seaglasslookandfeel.painter.button;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.ButtonPainter.Which;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * Paint a (possibly) segmented button. The default colors are suitable for
 * drawing on a default background, for instance, a dialog box.
 *
 * @author Kathryn Huxtable
 */
public class SegmentedButtonPainter extends ButtonVariantPainter {

    /**
     * Segment type for a button.
     */
    enum SegmentType {
        NONE, FIRST, MIDDLE, LAST
    }

    private Effect dropShadow = new SeaGlassDropShadowEffect();

    private CommonControlState type;

    /**
     * Create a segmented button painter.
     *
     * @param state the button state.
     * @param ctx   the paint context.
     */
    public SegmentedButtonPainter(Which state, PaintContext ctx) {
        super(state, ctx);

        type = getButtonType(state);
    }

    /**
     * {@inheritDoc}
     */
    public void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        SegmentType segmentStatus = getSegmentType(c);
        int         newHeight     = getButtonHeight(c, height);

        int yOffset = (height - newHeight) / 2;

        height = newHeight;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = focusInsets.left;
        int y = focusInsets.top + yOffset;

        width  -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        boolean useToolBarFocus = isInToolBar(c);
        Shape   s;

        if (focused) {
            s = createOuterFocus(segmentStatus, x, y, width, height);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarFocus));
            g.draw(s);
            s = createInnerFocus(segmentStatus, x, y, width, height);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarFocus));
            g.draw(s);
        }

        if (!isInToolBar(c) || this instanceof TexturedButtonPainter) {
            s = createBorder(segmentStatus, x, y, width, height);

            if (!focused) {
                dropShadow.fill(g, s);
            }

            g.setPaint(getCommonBorderPaint(s, type));
            g.fill(s);

            s = createInterior(segmentStatus, x, y, width, height);
            g.setPaint(getCommonInteriorPaint(s, type));
            g.fill(s);
        }
    }

    /**
     * Compute the new button height for drawing, forcing the button into a
     * standard height.
     *
     * @param  c      the button.
     * @param  height the height requested.
     *
     * @return the new height.
     */
    private int getButtonHeight(JComponent c, int height) {
        String scaleKey  = SeaGlassStyle.getSizeVariant(c);
        int    newHeight = 27;

        // If the key is natural, allow the button to scale.
        if (SeaGlassStyle.NATURAL_KEY.equals(scaleKey)) {
            return height;
        } else if (SeaGlassStyle.LARGE_KEY.equals(scaleKey)) {
            newHeight *= SeaGlassStyle.LARGE_SCALE; // 31
        } else if (SeaGlassStyle.SMALL_KEY.equals(scaleKey)) {
            newHeight *= SeaGlassStyle.SMALL_SCALE; // 23
        } else if (SeaGlassStyle.MINI_KEY.equals(scaleKey)) {
            newHeight *= SeaGlassStyle.MINI_SCALE; // 19
        } else {
            if (height < 22) {
                newHeight *= SeaGlassStyle.MINI_SCALE;
            } else if (height < 26) {
                newHeight *= SeaGlassStyle.SMALL_SCALE;
            } else if (height >= 30) {
                newHeight *= SeaGlassStyle.LARGE_SCALE;
            }
        }

        return newHeight;
    }

    /**
     * Get the button colors for the state.
     *
     * @param  state the button state to paint.
     *
     * @return the button color set.
     */
    protected CommonControlState getButtonType(Which state) {
        switch (state) {

        case BACKGROUND_DEFAULT:
        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            return CommonControlState.DEFAULT;

        case BACKGROUND_PRESSED_DEFAULT:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            return CommonControlState.DEFAULT_PRESSED;

        case BACKGROUND_DISABLED:
            return CommonControlState.DISABLED;

        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            return CommonControlState.ENABLED;

        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return CommonControlState.PRESSED;

        case BACKGROUND_DISABLED_SELECTED:
            return CommonControlState.DISABLED_SELECTED;
        }

        return null;
    }

    /**
     * Get the segment type (if any) from the component's client properties.
     *
     * @param  c the component.
     *
     * @return the segment status.
     */
    protected SegmentType getSegmentType(JComponent c) {
        Object      buttonType  = c.getClientProperty("JButton.buttonType");
        SegmentType segmentType = SegmentType.NONE;

        if (buttonType != null && buttonType instanceof String && ((String) buttonType).startsWith("segmented")) {
            String position = (String) c.getClientProperty("JButton.segmentPosition");

            if ("first".equals(position)) {
                segmentType = SegmentType.FIRST;
            } else if ("middle".equals(position)) {
                segmentType = SegmentType.MIDDLE;
            } else if ("last".equals(position)) {
                segmentType = SegmentType.LAST;
            }
        }

        return segmentType;
    }

    /**
     * Create the shape for the outer focus ring. Designed to be drawn rather
     * than filled.
     *
     * @param  segmentType the segment type.
     * @param  x           the x offset.
     * @param  y           the y offset.
     * @param  w           the width.
     * @param  h           the height.
     *
     * @return the shape of the outer focus ring. Designed to be drawn rather
     *         than filled.
     */
    protected Shape createOuterFocus(final SegmentType segmentType, final int x, final int y, final int w, final int h) {
        switch (segmentType) {

        case FIRST:
            return shapeGenerator.createRoundRectangle(x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUS, CornerStyle.ROUNDED,
                                                       CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE);

        case MIDDLE:
            return shapeGenerator.createRectangle(x - 2, y - 2, w + 3, h + 3);

        case LAST:
            return shapeGenerator.createRoundRectangle(x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUS, CornerStyle.SQUARE,
                                                       CornerStyle.SQUARE, CornerStyle.ROUNDED, CornerStyle.ROUNDED);

        default:
            return shapeGenerator.createRoundRectangle(x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUS);
        }
    }

    /**
     * Create the shape for the inner focus ring. Designed to be drawn rather
     * than filled.
     *
     * @param  segmentType the segment type.
     * @param  x           the x offset.
     * @param  y           the y offset.
     * @param  w           the width.
     * @param  h           the height.
     *
     * @return the shape of the inner focus ring. Designed to be drawn rather
     *         than filled.
     */
    protected Shape createInnerFocus(final SegmentType segmentType, final int x, final int y, final int w, final int h) {
        switch (segmentType) {

        case FIRST:
            return shapeGenerator.createRoundRectangle(x - 1, y - 1, w + 2, h + 1, CornerSize.INNER_FOCUS, CornerStyle.ROUNDED,
                                                       CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE);

        case MIDDLE:
            return shapeGenerator.createRectangle(x - 2, y - 1, w + 3, h + 1);

        case LAST:
            return shapeGenerator.createRoundRectangle(x - 2, y - 1, w + 2, h + 1, CornerSize.INNER_FOCUS, CornerStyle.SQUARE,
                                                       CornerStyle.SQUARE, CornerStyle.ROUNDED, CornerStyle.ROUNDED);

        default:
            return shapeGenerator.createRoundRectangle(x - 1, y - 1, w + 1, h + 1, CornerSize.INNER_FOCUS);
        }
    }

    /**
     * Create the shape for the border.
     *
     * @param  segmentType the segment type.
     * @param  x           the x offset.
     * @param  y           the y offset.
     * @param  w           the width.
     * @param  h           the height.
     *
     * @return the shape of the border.
     */
    protected Shape createBorder(final SegmentType segmentType, final int x, final int y, final int w, final int h) {
        switch (segmentType) {

        case FIRST:
            return shapeGenerator.createRoundRectangle(x, y, w + 2, h, CornerSize.BORDER, CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                                                       CornerStyle.SQUARE, CornerStyle.SQUARE);

        case MIDDLE:
            return shapeGenerator.createRectangle(x - 2, y, w + 4, h);

        case LAST:
            return shapeGenerator.createRoundRectangle(x - 2, y, w + 2, h, CornerSize.BORDER, CornerStyle.SQUARE, CornerStyle.SQUARE,
                                                       CornerStyle.ROUNDED, CornerStyle.ROUNDED);

        default:
            return shapeGenerator.createRoundRectangle(x, y, w, h, CornerSize.BORDER);
        }
    }

    /**
     * Create the shape for the interior.
     *
     * @param  segmentType the segment type.
     * @param  x           the x offset.
     * @param  y           the y offset.
     * @param  w           the width.
     * @param  h           the height.
     *
     * @return the shape of the interior.
     */
    protected Shape createInterior(final SegmentType segmentType, final int x, final int y, final int w, final int h) {
        switch (segmentType) {

        case FIRST:
            return shapeGenerator.createRoundRectangle(x + 1, y + 1, w, h - 2, CornerSize.INTERIOR, CornerStyle.ROUNDED,
                                                       CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE);

        case MIDDLE:
            return shapeGenerator.createRectangle(x - 2, y + 1, w + 3, h - 2);

        case LAST:
            return shapeGenerator.createRoundRectangle(x - 2, y + 1, w + 1, h - 2, CornerSize.INTERIOR, CornerStyle.SQUARE,
                                                       CornerStyle.SQUARE, CornerStyle.ROUNDED, CornerStyle.ROUNDED);

        default:
            return shapeGenerator.createRoundRectangle(x + 1, y + 1, w - 2, h - 2, CornerSize.INTERIOR);
        }
    }
}
