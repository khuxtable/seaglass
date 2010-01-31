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

/**
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SORTED,
        BACKGROUND_ENABLED_FOCUSED_SORTED,
        BACKGROUND_DISABLED_SORTED,
    }

    private PaintContext ctx;
    private ButtonType   type;
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
        g.setPaint(PaintUtil.getTableHeaderPaint(s, type, isSorted));
        g.fill(s);

        g.setPaint(PaintUtil.getTableHeaderBorderPaint(type));
        g.drawLine(0, height - 1, width, height - 1);
        g.drawLine(width - 1, 0, width - 1, height - 1);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
            return ButtonType.ENABLED;
        case BACKGROUND_ENABLED_FOCUSED:
            return ButtonType.ENABLED;
        case BACKGROUND_PRESSED:
            return ButtonType.PRESSED;
        case BACKGROUND_ENABLED_SORTED:
            return ButtonType.ENABLED;
        case BACKGROUND_ENABLED_FOCUSED_SORTED:
            return ButtonType.ENABLED;
        case BACKGROUND_DISABLED_SORTED:
            return ButtonType.DISABLED;
        }
        return null;
    }
}
