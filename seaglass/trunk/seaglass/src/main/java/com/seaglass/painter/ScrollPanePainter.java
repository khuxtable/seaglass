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
package com.seaglass.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's ScrollPanePainter.
 */
public final class ScrollPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BORDER_ENABLED_FOCUSED, BORDER_ENABLED
    }

    private static final Insets    insets    = new Insets(5, 5, 5, 5);
    private static final Dimension dimension = new Dimension(122, 24);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    private Which                  state;
    private PaintContext           ctx;

    private Path2D                 path      = new Path2D.Float();
    private Rectangle2D            rect      = new Rectangle2D.Float(0, 0, 0, 0);

    private Color                  color1    = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
    private Color                  color2    = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

    public ScrollPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BORDER_ENABLED_FOCUSED:
            paintBorderEnabledAndFocused(g);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBorderEnabledAndFocused(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color1);
        g.fill(rect);
        path = decodePath1();
        g.setPaint(color2);
        g.fill(path);

    }

    private void paintBorderEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color1);
        g.fill(rect);

    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(0.6f), // x
            decodeY(0.4f), // y
            decodeX(2.4f) - decodeX(0.6f), // width
            decodeY(0.6f) - decodeY(0.4f)); // height
        return rect;
    }

    private Rectangle2D decodeRect2() {
        rect.setRect(decodeX(0.4f), // x
            decodeY(0.4f), // y
            decodeX(0.6f) - decodeX(0.4f), // width
            decodeY(2.6f) - decodeY(0.4f)); // height
        return rect;
    }

    private Rectangle2D decodeRect3() {
        rect.setRect(decodeX(2.4f), // x
            decodeY(0.4f), // y
            decodeX(2.6f) - decodeX(2.4f), // width
            decodeY(2.6f) - decodeY(0.4f)); // height
        return rect;
    }

    private Rectangle2D decodeRect4() {
        rect.setRect(decodeX(0.6f), // x
            decodeY(2.4f), // y
            decodeX(2.4f) - decodeX(0.6f), // width
            decodeY(2.6f) - decodeY(2.4f)); // height
        return rect;
    }

    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.4f), decodeY(0.4f));
        path.lineTo(decodeX(0.4f), decodeY(2.6f));
        path.lineTo(decodeX(2.6f), decodeY(2.6f));
        path.lineTo(decodeX(2.6f), decodeY(0.4f));
        path.curveTo(decodeAnchorX(2.5999999046325684f, 0.0f), decodeAnchorY(0.4000000059604645f, 0.0f), decodeAnchorX(2.880000352859497f,
            0.09999999999999432f), decodeAnchorY(0.4000000059604645f, 0.0f), decodeX(2.8800004f), decodeY(0.4f));
        path.curveTo(decodeAnchorX(2.880000352859497f, 0.09999999999999432f), decodeAnchorY(0.4000000059604645f, 0.0f), decodeAnchorX(
            2.880000352859497f, 0.0f), decodeAnchorY(2.879999876022339f, 0.0f), decodeX(2.8800004f), decodeY(2.8799999f));
        path.lineTo(decodeX(0.120000005f), decodeY(2.8799999f));
        path.lineTo(decodeX(0.120000005f), decodeY(0.120000005f));
        path.lineTo(decodeX(2.8800004f), decodeY(0.120000005f));
        path.lineTo(decodeX(2.8800004f), decodeY(0.4f));
        path.lineTo(decodeX(0.4f), decodeY(0.4f));
        path.closePath();
        return path;
    }
}
