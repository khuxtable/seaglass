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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * SplitPanePainter implementation. Does nothing because the work is done by the
 * divider painter.
 */
public final class SplitPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED
    }

    // Constants for the PaintContext.
    private static final Insets    insets    = new Insets(0, 0, 0, 0);
    private static final Dimension dimension = new Dimension(20, 20);
    private static final CacheMode cacheMode = PaintContext.CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    private PaintContext           ctx;

    public SplitPanePainter(Which state) {
        super();
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }
}
