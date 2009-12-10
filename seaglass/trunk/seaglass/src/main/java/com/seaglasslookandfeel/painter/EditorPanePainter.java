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
 * EditorPanePainter implementation.
 */
public final class EditorPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED,
    }

    private static final Insets    insets              = new Insets(0, 0, 0, 0);
    private static final Dimension dimension           = new Dimension(100, 30);
    private static final CacheMode cacheMode           = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                = Double.POSITIVE_INFINITY;
    private static final Double    maxV                = Double.POSITIVE_INFINITY;

    private static final Color     DISABLED_BACKGROUND = new Color(0xf8f8f8);
    private static final Color     ENABLED_BACKGROUND  = Color.white;

    private Which                  state;
    private PaintContext           ctx;

    public EditorPanePainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Color color = ENABLED_BACKGROUND;
        switch (state) {
        case BACKGROUND_DISABLED:
            color = DISABLED_BACKGROUND;
        case BACKGROUND_ENABLED:
            color = ENABLED_BACKGROUND;
        case BACKGROUND_SELECTED:
            color = ENABLED_BACKGROUND;
        }

        // Paint the rectangle.
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
