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
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class MenuItemPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_MOUSEOVER,
    }

    private Which        state;
    private PaintContext ctx;

    private Rectangle2D  rect   = new Rectangle2D.Float(0, 0, 0, 0);

    private Color        color1 = new Color(0x5873d9);
    private Color        color2 = new Color(0x1441a8);

    public MenuItemPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 3), false, CacheMode.NO_CACHING, 1.0, 1.0);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_MOUSEOVER:
            paintBackgroundMouseOver(g, width, height);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundMouseOver(Graphics2D g, int width, int height) {
        Shape s = decodeBackground(width, height);
        g.setPaint(decodeSelectedGradient(s, width, height));
        g.fill(s);
    }

    private Rectangle2D decodeBackground(int width, int height) {
        rect.setRect(0, 0, width, height);
        return rect;
    }

    private Paint decodeSelectedGradient(Shape s, int width, int height) {
        float midX = width / 2.0f;
        return decodeGradient(midX, 0, midX, height, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }
}
