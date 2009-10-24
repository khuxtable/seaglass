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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

/**
 */
public final class DesktopIconPainter extends AbstractRegionPainter {
    public static final int  BACKGROUND_ENABLED = 1;

    private int              state;
    private PaintContext     ctx;

    private Rectangle2D      rect               = new Rectangle2D.Float(0, 0, 0, 0);
    private RoundRectangle2D roundRect          = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color            color1             = decodeColor("nimbusBase", 0.02551502f, -0.47885156f, -0.34901965f, 0);
    private Color            color2             = decodeColor("nimbusBlueGrey", -0.027777791f, -0.102261856f, 0.20392156f, 0);
    private Color            color3             = decodeColor("nimbusBlueGrey", 0.0f, -0.0682728f, 0.09019607f, 0);
    private Color            color4             = decodeColor("nimbusBlueGrey", -0.01111114f, -0.088974595f, 0.16470587f, 0);
    private Color            color5             = decodeColor("nimbusBlueGrey", 0.0f, -0.029445238f, -0.019607842f, 0);

    public DesktopIconPainter(PaintContext ctx, int state) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        roundRect = decodeRoundRect1();
        g.setPaint(color1);
        g.fill(roundRect);
        roundRect = decodeRoundRect2();
        g.setPaint(decodeGradient1(roundRect));
        g.fill(roundRect);
        rect = decodeRect1();
        g.setPaint(decodeGradient2(rect));
        g.fill(rect);

    }

    private RoundRectangle2D decodeRoundRect1() {
        roundRect.setRoundRect(decodeX(0.4f), // x
            decodeY(0.0f), // y
            decodeX(2.8f) - decodeX(0.4f), // width
            decodeY(2.6f) - decodeY(0.0f), // height
            4.8333335f, 4.8333335f); // rounding
        return roundRect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        roundRect.setRoundRect(decodeX(0.6f), // x
            decodeY(0.2f), // y
            decodeX(2.8f) - decodeX(0.6f), // width
            decodeY(2.4f) - decodeY(0.2f), // height
            3.1f, 3.1f); // rounding
        return roundRect;
    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(0.8f), // x
            decodeY(0.4f), // y
            decodeX(2.4f) - decodeX(0.8f), // width
            decodeY(2.2f) - decodeY(0.4f)); // height
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 0.5f, 1.0f },
            new Color[] { color2, decodeColor(color2, color3, 0.5f), color3 });
    }

    private Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 0.24f, 1.0f },
            new Color[] { color4, decodeColor(color4, color5, 0.5f), color5 });
    }
}
