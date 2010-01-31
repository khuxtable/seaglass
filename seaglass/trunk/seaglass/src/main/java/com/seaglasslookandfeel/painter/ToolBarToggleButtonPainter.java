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
import com.seaglasslookandfeel.painter.util.PaintUtil.ToolbarToggleButtonType;

/**
 * ToolBarToggleButtonPainter implementation.
 */
public final class ToolBarToggleButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_SELECTED,
        BACKGROUND_SELECTED_FOCUSED,
        BACKGROUND_PRESSED_SELECTED,
        BACKGROUND_PRESSED_SELECTED_FOCUSED,
        BACKGROUND_DISABLED_SELECTED,
    }

    private PaintContext ctx;
    private boolean      isSelected;

    public ToolBarToggleButtonPainter(Which state) {
        super();
        ctx = new PaintContext(CacheMode.FIXED_SIZES);

        switch (state) {
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
        case BACKGROUND_DISABLED_SELECTED:
            isSelected = true;
            break;
        default:
            isSelected = false;
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (isSelected) {
            Shape s = ShapeUtil.createRectangle(0, 0, 1, height);
            g.setPaint(PaintUtil.getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER));
            g.fill(s);

            s = ShapeUtil.createRectangle(1, 0, 1, height);
            g.setPaint(PaintUtil.getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER_EDGE));
            g.fill(s);
            s = ShapeUtil.createRectangle((width - 2), 0, 1, height);
            g.setPaint(PaintUtil.getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER_EDGE));
            g.fill(s);

            s = ShapeUtil.createRectangle(0, 0, 1, height);
            g.setPaint(PaintUtil.getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.OUTER_EDGE));
            g.fill(s);
            s = ShapeUtil.createRectangle((width - 1), 0, 1, height);
            g.setPaint(PaintUtil.getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.OUTER_EDGE));
            g.fill(s);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
