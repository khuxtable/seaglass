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
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's FileChooserBackgroundPainter.
 */
public final class FileChooserBackgroundPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED
    }

    private Which                  state;
    private PaintContext           ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    private Rectangle2D            rect      = new Rectangle2D.Float(0, 0, 0, 0);

    private Color                  color1    = new Color(214, 217, 223);

    // FIXME These are not assigned properly.
    private static final Insets    insets    = new Insets(7, 7, 7, 7);
    private static final Dimension dimension = new Dimension(86, 28);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    public FileChooserBackgroundPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);

    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(1.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(2.0f) - decodeY(1.0f)); // height
        return rect;
    }
}
