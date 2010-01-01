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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ScrollBarTrackPainter implementation.
 */
public final class ScrollBarTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private static final Color[]   colors    = {
        new Color(0xbdbdbd),
        new Color(0xcccccc),
        new Color(0xd8d8d8),
        new Color(0xe3e3e3),
        new Color(0xeaeaea),
        new Color(0xeaeaea),
        new Color(0xf0f0f0),
        new Color(0xf5f5f5),
        new Color(0xf6f6f6),
        new Color(0xf7f7f7),
        new Color(0xf8f8f8),
        new Color(0xf9f9f9),
        new Color(0xf7f7f7),
        new Color(0xf3f3f3),
        new Color(0xededed),
        new Color(0xe4e4e4),                };

    private static final Insets    insets    = new Insets(0, 0, 0, 0);
    private static final Dimension dimension = new Dimension(19, 15);
    // FIXME Need a good gradient so that we don't have to draw an image.
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = 2.0;

    private PaintContext           ctx;

    public ScrollBarTrackPainter(Which state) {
        super();
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        for (int i = 0; i < 15; i++) {
            g.setColor(colors[i]);
            g.drawLine(0, i, width - 1, i);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
