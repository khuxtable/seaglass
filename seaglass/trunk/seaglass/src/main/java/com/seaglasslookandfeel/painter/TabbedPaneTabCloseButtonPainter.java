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
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Search field icon implementation.
 */
public final class TabbedPaneTabCloseButtonPainter extends AbstractRegionPainter {

    /**
     * Control state.
     */
    public static enum Which {
        DISABLED, ENABLED, MOUSEOVER, PRESSED,
    }

    private Color enabledColor = decodeColor("seaGlassTabbedPaneTabCloseIcon");
    private Color pressedColor = decodeColor("seaGlassTabbedPaneTabCloseIconPressed");

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new TabbedPaneTabCloseButtonPainter object.
     *
     * @param state the control state to paint.
     */
    public TabbedPaneTabCloseButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (state) {

        case ENABLED:
            drawGraphic(g, width, height, enabledColor);
            break;

        case MOUSEOVER:
            drawGraphic(g, width, height, enabledColor);
            drawBorder(g, width, height, enabledColor);
            break;

        case PRESSED:
            drawGraphic(g, width, height, pressedColor);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Draw the "close" graphic.
     *
     * @param g      the Graphic context.
     * @param width  the width of the graphic.
     * @param height the height of the graphic.
     * @param color  the color of the graphic.
     */
    private void drawGraphic(Graphics2D g, int width, int height, Color color) {
        Shape s = shapeGenerator.createTabCloseIcon(2, 2, width - 4, height - 4);

        g.setColor(color);
        g.fill(s);
    }

    /**
     * Draw a border around the graphic..
     *
     * @param g      the Graphic context.
     * @param width  the width of the graphic.
     * @param height the height of the graphic.
     * @param color  the color of the graphic.
     */
    private void drawBorder(Graphics2D g, int width, int height, Color color) {
        Shape s = shapeGenerator.createRoundRectangle(0, 0, width - 1, height - 1, CornerSize.CHECKBOX_INTERIOR);

        g.setColor(color);
        g.draw(s);
    }
}
