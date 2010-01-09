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
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public final class MenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_SELECTED,
        ARROWICON_DISABLED,
        ARROWICON_ENABLED,
        ARROWICON_ENABLED_SELECTED,
    };

    private Which        state;
    private PaintContext ctx;

    // the following 4 variables are reused during the painting code of the
    // layers
    private Path2D       path   = new Path2D.Float();
    private Rectangle2D  rect   = new Rectangle2D.Float(0, 0, 0, 0);

    private Color        color1 = decodeColor("nimbusSelection", 0.0f, 0.0f, 0.0f, 0);
    private Color        color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.08983666f, -0.17647058f, 0);
    private Color        color3 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.09663743f, -0.4627451f, 0);
    private Color        color4 = new Color(255, 255, 255, 255);

    public MenuPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case BACKGROUND_ENABLED_SELECTED:
            ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 30), false,
                AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0);
            break;
        case ARROWICON_DISABLED:
            ctx = new PaintContext(new Insets(5, 5, 5, 5), new Dimension(9, 10), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
            break;
        case ARROWICON_ENABLED:
            ctx = new PaintContext(new Insets(5, 5, 5, 5), new Dimension(9, 10), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
            break;
        case ARROWICON_ENABLED_SELECTED:
            ctx = new PaintContext(new Insets(1, 1, 1, 1), new Dimension(9, 10), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0);
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED_SELECTED:
            paintBackgroundEnabledAndSelected(g);
            break;
        case ARROWICON_DISABLED:
            paintarrowIconDisabled(g);
            break;
        case ARROWICON_ENABLED:
            paintarrowIconEnabled(g);
            break;
        case ARROWICON_ENABLED_SELECTED:
            paintarrowIconEnabledAndSelected(g);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabledAndSelected(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
    }

    private void paintarrowIconDisabled(Graphics2D g) {
        path = decodePath1();
        g.setPaint(color2);
        g.fill(path);
    }

    private void paintarrowIconEnabled(Graphics2D g) {
        path = decodePath1();
        g.setPaint(color3);
        g.fill(path);
    }

    private void paintarrowIconEnabledAndSelected(Graphics2D g) {
        path = decodePath2();
        g.setPaint(color4);
        g.fill(path);
    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(1.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(2.0f) - decodeY(1.0f)); // height
        return rect;
    }

    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(0.2f));
        path.lineTo(decodeX(2.7512195f), decodeY(2.102439f));
        path.lineTo(decodeX(0.0f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(0.2f));
        path.closePath();
        return path;
    }

    private Path2D decodePath2() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(1.0f));
        path.lineTo(decodeX(1.9529617f), decodeY(1.5625f));
        path.lineTo(decodeX(0.0f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(1.0f));
        path.closePath();
        return path;
    }
}
