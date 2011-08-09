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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractCommonColorsPainter {

    /**
     * State class.
     */
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_ENABLED_FOCUSED, BACKGROUND_PRESSED, BACKGROUND_ENABLED_SORTED,
        BACKGROUND_ENABLED_FOCUSED_SORTED, BACKGROUND_DISABLED_SORTED,
    }

    private Color tableHeaderBorderEnabled       = decodeColor("tableHeaderBorderEnabled");
    private Color tableHeaderInteriorBaseEnabled = decodeColor("tableHeaderInteriorBaseEnabled");

    private Color tableHeaderBorderDisabled = disable(tableHeaderBorderEnabled);

    private Color tableHeaderTopEnabled      = deriveColor(tableHeaderInteriorBaseEnabled, -0.027778f, -0.220842f,
                                                           0.335294f, 0);
    private Color tableHeaderUpperMidEnabled = deriveColor(tableHeaderInteriorBaseEnabled, -0.020833f, 0.000405f, -0.011765f, 0);
    private Color tableHeaderLowerMidEnabled = deriveColor(tableHeaderInteriorBaseEnabled, 0f, -0.000264f, 0.007843f, 0);
    private Color tableHeaderBottomEnabled   = deriveColor(tableHeaderInteriorBaseEnabled, -0.020833f, -0.201033f,
                                                           0.331373f, 0);

    private FourColors tableHeaderEnabled        = new FourColors(tableHeaderTopEnabled, tableHeaderUpperMidEnabled,
                                                                  tableHeaderLowerMidEnabled, tableHeaderBottomEnabled);
    private FourColors tableHeaderSorted         = getCommonInteriorColors(CommonControlState.SELECTED);
    private FourColors tableHeaderPressed        = getCommonInteriorColors(CommonControlState.PRESSED);
    private FourColors tableHeaderDisabled       = disable(tableHeaderEnabled);
    private FourColors tableHeaderDisabledSorted = disable(tableHeaderSorted);

    private PaintContext       ctx;
    private CommonControlState type;
    private boolean            isSorted;

    /**
     * Creates a new TableHeaderRendererPainter object.
     *
     * @param state the state.
     */
    public TableHeaderRendererPainter(Which state) {
        super();
        this.ctx      = new PaintContext(CacheMode.FIXED_SIZES);
        this.type     = getButtonType(state);
        this.isSorted = (state == Which.BACKGROUND_DISABLED_SORTED || state == Which.BACKGROUND_ENABLED_SORTED
                    || state == Which.BACKGROUND_ENABLED_FOCUSED_SORTED);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRectangle(0, 0, width - 1, height - 1);

        g.setPaint(getTableHeaderInteriorPaint(s, type, isSorted));
        g.fill(s);

        g.setPaint(getTableHeaderBorderPaint(type));
        g.drawLine(0, height - 1, width, height - 1);
        g.drawLine(width - 1, 0, width - 1, height - 1);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#getPaintContext()
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Get the common state from the specific state.
     *
     * @param  state the state.
     *
     * @return the common state.
     */
    private CommonControlState getButtonType(Which state) {
        switch (state) {

        case BACKGROUND_DISABLED:
            return CommonControlState.DISABLED;

        case BACKGROUND_ENABLED:
            return CommonControlState.ENABLED;

        case BACKGROUND_ENABLED_FOCUSED:
            return CommonControlState.ENABLED;

        case BACKGROUND_PRESSED:
            return CommonControlState.PRESSED;

        case BACKGROUND_ENABLED_SORTED:
            return CommonControlState.ENABLED;

        case BACKGROUND_ENABLED_FOCUSED_SORTED:
            return CommonControlState.ENABLED;

        case BACKGROUND_DISABLED_SORTED:
            return CommonControlState.DISABLED;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getTableHeaderBorderPaint(CommonControlState type) {
        return type == CommonControlState.DISABLED ? tableHeaderBorderDisabled : tableHeaderBorderEnabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s        DOCUMENT ME!
     * @param  type     DOCUMENT ME!
     * @param  isSorted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getTableHeaderInteriorPaint(Shape s, CommonControlState type, boolean isSorted) {
        return getCommonInteriorPaint(s, type);
//        FourColors colors = getTableHeaderColors(type, isSorted);
//        return createVerticalGradient(s, colors);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type     DOCUMENT ME!
     * @param  isSorted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private FourColors getTableHeaderColors(CommonControlState type, boolean isSorted) {
        switch (type) {

        case DISABLED:
            return isSorted ? tableHeaderDisabledSorted : tableHeaderDisabled;

        case ENABLED:
            return isSorted ? tableHeaderSorted : tableHeaderEnabled;

        case PRESSED:
            return tableHeaderPressed;
        }

        return null;
    }
}
