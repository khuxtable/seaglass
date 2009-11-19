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

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * PopupMenuPainter implementation.
 * 
 * Based on Nimbus's implementation.
 */
public final class PopupMenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Which                  state;
    private PaintContext           ctx;

    private Rectangle2D            rect      = new Rectangle2D.Float(0, 0, 0, 0);

    private Color                  color1    = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.39607844f, 0);
    private Color                  color2    = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color                  color3    = decodeColor("nimbusBase", 0.021348298f, -0.6150531f, 0.39999998f, 0);

    // FIXME These are not assigned properly.
    private static final Insets    insets    = new Insets(7, 7, 7, 7);
    private static final Dimension dimension = new Dimension(86, 28);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    public PopupMenuPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);

    }

    private void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect3();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);

    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(0.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(3.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect2() {
        rect.setRect(decodeX(1.0045455f), // x
            decodeY(0.11111111f), // y
            decodeX(1.9954545f) - decodeX(1.0045455f), // width
            decodeY(2.909091f) - decodeY(0.11111111f)); // height
        return rect;
    }

    private Rectangle2D decodeRect3() {
        rect.setRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect4() {
        rect.setRect(decodeX(0.5f), // x
            decodeY(0.09090909f), // y
            decodeX(2.5f) - decodeX(0.5f), // width
            decodeY(2.909091f) - decodeY(0.09090909f)); // height
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.0f,
            0.0030f,
            0.02f,
            0.5f,
            0.98f,
            0.996f,
            1.0f }, new Color[] {
            color2,
            decodeColor(color2, color3, 0.5f),
            color3,
            decodeColor(color3, color3, 0.5f),
            color3,
            decodeColor(color3, color2, 0.5f),
            color2 });
    }
}
