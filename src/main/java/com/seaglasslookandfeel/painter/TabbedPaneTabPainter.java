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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * Button painter. This paints both regular and toggle buttons because they look
 * the same except for the state. This actually delegates to a
 * SegmentedButtonPainter, which does the actual painting based on client
 * properties.
 *
 * @author Kathryn Huxtable
 */
public final class TabbedPaneTabPainter extends AbstractCommonColorsPainter {

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_PRESSED, BACKGROUND_DISABLED, BACKGROUND_DISABLED_SELECTED, BACKGROUND_SELECTED,
        BACKGROUND_PRESSED_SELECTED, BACKGROUND_SELECTED_FOCUSED, BACKGROUND_PRESSED_SELECTED_FOCUSED,
    }

    /**
     * Segment type for a button.
     */
    enum SegmentType {
        NONE, FIRST, MIDDLE, LAST
    }

    private Effect dropShadow = new SeaGlassDropShadowEffect();

    private CommonControlState type;

    /** The button state to paint. */
    protected Which state;

    /** The PaintContext containing cache information. */
    protected PaintContext ctx;

    /** Are we in a focused state? */
    protected boolean focused;

    /**
     * Create a new ButtonPainter.
     *
     * @param state the state of the button to be painted.
     */
    public TabbedPaneTabPainter(Which state) {
        super();

        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {

        case BACKGROUND_SELECTED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            focused = true;
            break;

        default:
            focused = false;
            break;
        }

        type = getButtonType(state);
    }

    /**
     * {@inheritDoc}
     */
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = new Object[] {};

        return extendedCacheKeys;
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        SegmentType segmentStatus = getSegmentType(c);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = focusInsets.left;
        int y = focusInsets.top;

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

    /**
     * Get the button colors for the state.
     *
     * @param  state the button state to paint.
     *
     * @return the button color set.
     */
    protected CommonControlState getButtonType(Which state) {
        switch (state) {

        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            return CommonControlState.DEFAULT;

        case BACKGROUND_DISABLED:
            return CommonControlState.DISABLED;

        case BACKGROUND_ENABLED:
            return CommonControlState.ENABLED;

        case BACKGROUND_PRESSED:
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
        SegmentType segmentType = SegmentType.NONE;

        String position = (String) c.getClientProperty("JTabbedPane.Tab.segmentPosition");

        if ("first".equals(position)) {
            segmentType = SegmentType.FIRST;
        } else if ("middle".equals(position)) {
            segmentType = SegmentType.MIDDLE;
        } else if ("last".equals(position)) {
            segmentType = SegmentType.LAST;
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
