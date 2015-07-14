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
 * TableHeaderPainter implementation.
 */
public final class TableHeaderPainter extends AbstractRegionPainter {
    public static enum Which {
        ASCENDINGSORTICON_ENABLED, DESCENDINGSORTICON_ENABLED
    }

    private Color        tableHeaderSortIndicator = decodeColor("tableHeaderSortIndicator");

    private Which        state;
    private PaintContext ctx;

    public TableHeaderPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case ASCENDINGSORTICON_ENABLED:
            paintAscending(g);
            break;
        case DESCENDINGSORTICON_ENABLED:
            paintDescending(g);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintAscending(Graphics2D g) {
        Shape s = shapeGenerator.createArrowUp(1, 0, 6, 6);
        g.setPaint(tableHeaderSortIndicator);
        g.fill(s);
    }

    private void paintDescending(Graphics2D g) {
        Shape s = shapeGenerator.createArrowDown(1, 2, 6, 6);
        g.setPaint(tableHeaderSortIndicator);
        g.fill(s);
    }
}
