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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.PaintUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.PaintUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * PopupMenuPainter implementation.
 */
public final class PopupMenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private PaintContext ctx;
    private ButtonType   type;

    public PopupMenuPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
        type = getButtonType(state);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = ShapeUtil.createRoundRectangle(0, 0, width, height, CornerSize.POPUP_BORDER);
        g.setPaint(PaintUtil.getPopupMenuBorderPaint(s, type));
        g.fill(s);
        s = ShapeUtil.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.POPUP_INTERIOR);
        g.setPaint(PaintUtil.getPopupMenuInteriorPaint(s, type));
        g.fill(s);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
            return ButtonType.ENABLED;
        }
        return null;
    }
}
