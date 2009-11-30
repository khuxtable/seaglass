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
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's InternalFramePainter.
 */
public final class InternalFramePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    };

    // Constants for the PaintContext.
    private static final Insets    insets                   = new Insets(25, 6, 6, 6);
    private static final Dimension dimension                = new Dimension(25, 36);
    private static final CacheMode cacheMode                = PaintContext.CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                     = Double.POSITIVE_INFINITY;
    private static final Double    maxV                     = Double.POSITIVE_INFINITY;

    private Which                  state;
    private PaintContext           ctx;

    private RoundRectangle2D       roundRect                = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color                  activeTopColor           = new Color(206, 206, 206);
    private Color                  activeBottomTitleColor   = new Color(196, 196, 196);
    private Color                  activeBottomColor        = new Color(166, 166, 166);

    private Color                  inactiveTopColor         = new Color(236, 236, 236);
    private Color                  inactiveBottomTitleColor = new Color(217, 217, 217);
    private Color                  inactiveBottomColor      = new Color(216, 216, 216);

    public InternalFramePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            paintBackgroundEnabledAndWindowFocused(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        roundRect.setRoundRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f), // height
            8.0f, 8.0f); // rounding
        g.setPaint(decodeActiveGradient(roundRect, inactiveTopColor, inactiveBottomTitleColor, inactiveBottomColor));
        g.fill(roundRect);
    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g) {
        roundRect.setRoundRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f), // height
            8.0f, 8.0f); // rounding
        g.setPaint(decodeActiveGradient(roundRect, activeTopColor, activeBottomTitleColor, activeBottomColor));
        g.fill(roundRect);
    }

    private Paint decodeActiveGradient(Shape s, Color color1, Color color2, Color color3) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();

        float titleBottom = decodeY(1.0f) / h;
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.0f,
            titleBottom,
            titleBottom + 0.00001f,
            1.0f }, new Color[] { color1, color2, color2, color3 });
    }
}
