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
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * PopupMenuPainter implementation.
 */
public final class PopupMenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Color             popupMenuInteriorEnabled  = decodeColor("popupMenuInteriorEnabled");
    private Color             popupMenuBorderEnabled    = decodeColor("popupMenuBorderEnabled");
    private Color             popupMenuBorderDisabled   = disable(popupMenuBorderEnabled);
    private Color             popupMenuInteriorDisabled = disable(popupMenuBorderEnabled);

    private PaintContext      ctx;
    private CommonControlType type;

    public PopupMenuPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
        type = getButtonType(state);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRoundRectangle(0, 0, width, height, CornerSize.POPUP_BORDER);
        g.setPaint(getPopupMenuBorderPaint(s, type));
        g.fill(s);
        s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.POPUP_INTERIOR);
        g.setPaint(getPopupMenuInteriorPaint(s, type));
        g.fill(s);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED:
            return CommonControlType.ENABLED;
        }
        return null;
    }

    public Paint getPopupMenuBorderPaint(Shape s, CommonControlType type) {
        return getPopupMenuBorderColors(type);
    }

    public Paint getPopupMenuInteriorPaint(Shape s, CommonControlType type) {
        return getPopupMenuInteriorColors(type);
    }

    private Color getPopupMenuBorderColors(CommonControlType type) {
        switch (type) {
        case ENABLED:
            return popupMenuBorderEnabled;
        case DISABLED:
            return popupMenuBorderDisabled;
        }
        return null;
    }

    private Color getPopupMenuInteriorColors(CommonControlType type) {
        switch (type) {
        case ENABLED:
            return popupMenuInteriorEnabled;
        case DISABLED:
            return popupMenuInteriorDisabled;
        }
        return null;
    }
}
