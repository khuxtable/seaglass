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
 * Sea Glass's ArrowButtonPainter class.
 */
public final class ArrowButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_ENABLED,
    }

    private Color        disabledColor = new Color(0x9ba8cf);
    private Color        enabledColor  = Color.black;

    private Which        state;
    private PaintContext ctx;

    private Path2D       path          = new Path2D.Float();

    public ArrowButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FOREGROUND_DISABLED:
            paintForegroundDisabled(g, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintForegroundEnabled(g, width, height);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintForegroundDisabled(Graphics2D g, int width, int height) {
        path = decodeArrowPath(width, height);
        g.setPaint(disabledColor);
        g.fill(path);

    }

    private void paintForegroundEnabled(Graphics2D g, int width, int height) {
        path = decodeArrowPath(width, height);
        g.setPaint(enabledColor);
        g.fill(path);

    }

    private Path2D decodeArrowPath(int width, int height) {
        path.reset();
        path.moveTo(width * 0.8f, height * 0.2f);
        path.lineTo(width * 0.2f, height * 0.5f);
        path.lineTo(width * 0.8f, height * 0.8f);
        path.lineTo(width * 0.8f, height * 0.2f);
        path.closePath();
        return path;
    }
}
