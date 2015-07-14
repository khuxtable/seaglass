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
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

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

    public enum ToolbarToggleButtonType {
        INNER, INNER_EDGE, OUTER_EDGE
    }

    private Color        toolbarToggleButtonBase      = decodeColor("toolbarToggleButtonBase");

    private TwoColors    toolbarToggleButtonInner     = new TwoColors(toolbarToggleButtonBase, deriveColor(toolbarToggleButtonBase, 0f, 0f,
                                                          0f, 0x28));
    private TwoColors    toolbarToggleButtonInnerEdge = new TwoColors(toolbarToggleButtonBase, deriveColor(toolbarToggleButtonBase, 0f, 0f,
                                                          0f, 0xf0));
    private TwoColors    toolbarToggleButtonOuterEdge = new TwoColors(deriveColor(toolbarToggleButtonBase, 0f, 0f, 0f, 0x10), deriveColor(
                                                          toolbarToggleButtonBase, 0f, 0f, 0f, 0x40));

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
            Shape s = shapeGenerator.createRectangle(0, 0, 1, height);
            g.setPaint(getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER));
            g.fill(s);

            s = shapeGenerator.createRectangle(1, 0, 1, height);
            g.setPaint(getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER_EDGE));
            g.fill(s);
            s = shapeGenerator.createRectangle((width - 2), 0, 1, height);
            g.setPaint(getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.INNER_EDGE));
            g.fill(s);

            s = shapeGenerator.createRectangle(0, 0, 1, height);
            g.setPaint(getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.OUTER_EDGE));
            g.fill(s);
            s = shapeGenerator.createRectangle((width - 1), 0, 1, height);
            g.setPaint(getToolbarToggleButtonPaint(s, ToolbarToggleButtonType.OUTER_EDGE));
            g.fill(s);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private TwoColors getToolbarToggleButtonColors(ToolbarToggleButtonType type) {
        switch (type) {
        case INNER:
            return toolbarToggleButtonInner;
        case INNER_EDGE:
            return toolbarToggleButtonInnerEdge;
        case OUTER_EDGE:
            return toolbarToggleButtonOuterEdge;
        }
        return null;
    }

    public Paint getToolbarToggleButtonPaint(Shape s, ToolbarToggleButtonType type) {
        TwoColors colors = getToolbarToggleButtonColors(type);
        return createToolbarToggleButtonGradient(s, colors);
    }

    private Paint createToolbarToggleButtonGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return createGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 0.35f, 0.65f, 1f }, new Color[] {
            colors.top,
            colors.bottom,
            colors.bottom,
            colors.top });
    }
}
