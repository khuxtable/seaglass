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
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * TableHeaderPainter implementation.
 */
public final class TableHeaderPainter extends AbstractRegionPainter {
    public static enum Which {
        ASCENDINGSORTICON_ENABLED, DESCENDINGSORTICON_ENABLED
    }

    private Which              state;
    private PaintContext       ctx;

    private static final Color arrowColor = new Color(0xc02a5481, true);

    private Path2D             path       = new Path2D.Double();

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
        g.setColor(arrowColor);
        path.reset();
        path.moveTo(4, 1);
        path.lineTo(7, 7);
        path.lineTo(1, 7);
        path.closePath();
        g.fill(path);
    }

    private void paintDescending(Graphics2D g) {
        g.setColor(arrowColor);
        path.reset();
        path.moveTo(4, 7);
        path.lineTo(1, 1);
        path.lineTo(7, 1);
        path.closePath();
        g.fill(path);
    }
}
