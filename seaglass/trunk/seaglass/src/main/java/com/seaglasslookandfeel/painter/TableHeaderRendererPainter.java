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
import com.seaglasslookandfeel.painter.util.ShapeUtil;

/**
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SORTED,
        BACKGROUND_ENABLED_FOCUSED_SORTED,
        BACKGROUND_DISABLED_SORTED,
    }

    private Color        tableHeaderBorderEnabled       = decodeColor("tableHeaderBorderEnabled");
    private Color        tableHeaderInteriorBaseEnabled = decodeColor("tableHeaderInteriorBaseEnabled");

    private Color        tableHeaderBorderDisabled      = disable(tableHeaderBorderEnabled);

    private Color        tableHeaderTopEnabled          = deriveColor(tableHeaderInteriorBaseEnabled, -0.027778f, -0.020842f, 0.035294f, 0);
    private Color        tableHeaderUpperMidEnabled     = deriveColor(tableHeaderInteriorBaseEnabled, -0.020833f, 0.000405f, -0.011765f, 0);
    private Color        tableHeaderLowerMidEnabled     = deriveColor(tableHeaderInteriorBaseEnabled, 0f, -0.000264f, 0.007843f, 0);
    private Color        tableHeaderBottomEnabled       = deriveColor(tableHeaderInteriorBaseEnabled, -0.020833f, -0.001033f, 0.031373f, 0);

    private FourColors   tableHeaderEnabled             = new FourColors(tableHeaderTopEnabled, tableHeaderUpperMidEnabled,
                                                            tableHeaderLowerMidEnabled, tableHeaderBottomEnabled);
    private FourColors   tableHeaderSorted              = getCommonInteriorColors(CommonControlType.SELECTED);
    private FourColors   tableHeaderPressed             = getCommonInteriorColors(CommonControlType.PRESSED);
    private FourColors   tableHeaderDisabled            = disable(tableHeaderEnabled);
    private FourColors   tableHeaderDisabledSorted      = disable(tableHeaderSorted);

    private PaintContext ctx;
    private CommonControlType   type;
    private boolean      isSorted;

    public TableHeaderRendererPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
        this.type = getButtonType(state);
        this.isSorted = (state == Which.BACKGROUND_DISABLED_SORTED || state == Which.BACKGROUND_ENABLED_SORTED || state == Which.BACKGROUND_ENABLED_FOCUSED_SORTED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = ShapeUtil.createRectangle(0, 0, width - 1, height - 1);
        g.setPaint(getTableHeaderInteriorPaint(s, type, isSorted));
        g.fill(s);

        g.setPaint(getTableHeaderBorderPaint(type));
        g.drawLine(0, height - 1, width, height - 1);
        g.drawLine(width - 1, 0, width - 1, height - 1);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED:
            return CommonControlType.ENABLED;
        case BACKGROUND_ENABLED_FOCUSED:
            return CommonControlType.ENABLED;
        case BACKGROUND_PRESSED:
            return CommonControlType.PRESSED;
        case BACKGROUND_ENABLED_SORTED:
            return CommonControlType.ENABLED;
        case BACKGROUND_ENABLED_FOCUSED_SORTED:
            return CommonControlType.ENABLED;
        case BACKGROUND_DISABLED_SORTED:
            return CommonControlType.DISABLED;
        }
        return null;
    }

    public Paint getTableHeaderBorderPaint(CommonControlType type) {
        return type == CommonControlType.DISABLED ? tableHeaderBorderDisabled : tableHeaderBorderEnabled;
    }

    public Paint getTableHeaderInteriorPaint(Shape s, CommonControlType type, boolean isSorted) {
        FourColors colors = getTableHeaderColors(type, isSorted);
        return createVerticalGradient(s, colors);
    }

    private FourColors getTableHeaderColors(CommonControlType type, boolean isSorted) {
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
