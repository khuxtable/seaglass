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
import java.awt.Rectangle;
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

    private static final Color END_INNER_COLOR      = new Color(0x00000000, true);
    private static final Color MID_INNER_COLOR      = new Color(0x28000000, true);

    private static final Color END_INNER_EDGE_COLOR = new Color(0x00000000, true);
    private static final Color MID_INNER_EDGE_COLOR = new Color(0x20000000, true);

    private static final Color END_OUTER_EDGE_COLOR = new Color(0x10000000, true);
    private static final Color MID_OUTER_EDGE_COLOR = new Color(0x40000000, true);

    private Rectangle          rect                 = new Rectangle();

    private Which              state;
    private PaintContext       ctx;

    public ToolBarToggleButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
        case BACKGROUND_DISABLED_SELECTED:
            paintSelectedBackground(g, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintSelectedBackground(Graphics2D g, int width, int height) {
        paintRectangle(g, 0, height, END_INNER_COLOR, MID_INNER_COLOR);

        paintRectangle(g, 1, height, END_INNER_EDGE_COLOR, MID_INNER_EDGE_COLOR);
        paintRectangle(g, width - 2, height, END_INNER_EDGE_COLOR, MID_INNER_EDGE_COLOR);

        paintRectangle(g, 0, height, END_OUTER_EDGE_COLOR, MID_OUTER_EDGE_COLOR);
        paintRectangle(g, width - 1, height, END_OUTER_EDGE_COLOR, MID_OUTER_EDGE_COLOR);
    }

    private void paintRectangle(Graphics2D g, int x, int height, Color endColor, Color midColor) {
        rect.setBounds(x, 0, 1, height);
        g.setPaint(decodeGradient(rect, endColor, midColor));
        g.fill(rect);
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    Paint decodeGradient(Shape s, Color endColor, Color middleColor) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 0.35f, 0.65f, 1f }, new Color[] {
            endColor,
            middleColor,
            middleColor,
            endColor });
    }
}
