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

public final class MenuBarPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BORDER_ENABLED,
    };

    private Which        state;
    private PaintContext ctx;

    private Rectangle2D  rect   = new Rectangle2D.Float(0, 0, 0, 0);

    private Color        color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.07016757f, 0.12941176f, 0);
    private Color        color2 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.10255819f, 0.23921567f, 0);
    private Color        color3 = decodeColor("nimbusBlueGrey", -0.111111104f, -0.10654225f, 0.23921567f, -29);
    private Color        color4 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -255);
    private Color        color5 = decodeColor("nimbusBorder", 0.0f, 0.0f, 0.0f, 0);

    public MenuBarPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case BACKGROUND_ENABLED:
            this.ctx = new PaintContext(new Insets(1, 0, 0, 0), new Dimension(18, 22), false,
                AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0);
            break;
        case BORDER_ENABLED:
            this.ctx = new PaintContext(new Insets(0, 0, 1, 0), new Dimension(30, 30), false,
                AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0);
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);
    }

    private void paintBorderEnabled(Graphics2D g) {
        rect = decodeRect3();
        g.setPaint(color5);
        g.fill(rect);
    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(0.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(1.9523809f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect2() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(0.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(2.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect3() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(2.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(3.0f) - decodeY(2.0f)); // height
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((1.0f * w) + x, (0.0f * h) + y, (1.0f * w) + x, (1.0f * h) + y, new float[] {
            0.0f,
            0.015f,
            0.03f,
            0.23354445f,
            0.7569444f }, new Color[] { color2, decodeColor(color2, color3, 0.5f), color3, decodeColor(color3, color4, 0.5f), color4 });
    }
}
