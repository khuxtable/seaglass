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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Sea Glass's ArrowButtonPainter class.
 */
public final class ArrowButtonPainter extends AbstractRegionPainter {

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_ENABLED, FOREGROUND_PRESSED,
    }

    private Color disabledColor = decodeColor("ArrowButton[Disabled].foreground");
    private Color enabledColor  = decodeColor("ArrowButton[Enabled].foreground");
    private Color pressedColor  = decodeColor("ArrowButton[Pressed].foreground");

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new ArrowButtonPainter object.
     *
     * @param state the control state.
     */
    public ArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {

        case FOREGROUND_DISABLED:
            paintForegroundDisabled(g, width, height);
            break;

        case FOREGROUND_ENABLED:
            paintForegroundEnabled(g, width, height);
            break;

        case FOREGROUND_PRESSED:
            paintForegroundPressed(g, width, height);
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
     * Paint the arrow in disabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param width  the width.
     * @param height the height.
     */
    private void paintForegroundDisabled(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);

        g.setPaint(disabledColor);
        g.fill(s);

    }

    /**
     * Paint the arrow in enabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param width  the width.
     * @param height the height.
     */
    private void paintForegroundEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);

        g.setPaint(enabledColor);
        g.fill(s);

    }

    /**
     * Paint the arrow in pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param width  the width.
     * @param height the height.
     */
    private void paintForegroundPressed(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);

        g.setPaint(pressedColor);
        g.fill(s);

    }

    /**
     * Create the arrow path.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the arrow path.
     */
    private Shape decodeArrowPath(int width, int height) {
        return shapeGenerator.createArrowLeft(width * 0.2, height * 0.2, width * 0.6, height * 0.6);
    }
}
