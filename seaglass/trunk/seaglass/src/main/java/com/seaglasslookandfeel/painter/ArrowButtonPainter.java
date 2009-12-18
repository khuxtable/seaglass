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
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's ArrowButtonPainter class.
 */
public final class ArrowButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_ENABLED,
    }

    private static final Insets    insets        = new Insets(0, 0, 0, 0);
    private static final Dimension dimension     = new Dimension(10, 10);
    private static final CacheMode cacheMode     = CacheMode.FIXED_SIZES;
    private static final Double    maxH          = 1.0;
    private static final Double    maxV          = 1.0;

    private Which                  state;
    private PaintContext           ctx;

    private Path2D                 path          = new Path2D.Float();

    private Color                  disabledColor = new Color(0x9ba8cf);
    private Color                  enabledColor  = Color.black;

    public ArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FOREGROUND_DISABLED:
            paintForegroundDisabled(g);
            break;
        case FOREGROUND_ENABLED:
            paintForegroundEnabled(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintForegroundDisabled(Graphics2D g) {
        path = decodePath1();
        g.setPaint(disabledColor);
        g.fill(path);

    }

    private void paintForegroundEnabled(Graphics2D g) {
        path = decodePath1();
        g.setPaint(enabledColor);
        g.fill(path);

    }

    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(1.8f), decodeY(1.2f));
        path.lineTo(decodeX(1.2f), decodeY(1.5f));
        path.lineTo(decodeX(1.8f), decodeY(1.8f));
        path.lineTo(decodeX(1.8f), decodeY(1.2f));
        path.closePath();
        return path;
    }
}
