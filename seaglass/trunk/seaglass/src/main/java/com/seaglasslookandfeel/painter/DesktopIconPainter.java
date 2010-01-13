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
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's DesktopIconPainter.
 */
public final class DesktopIconPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED
    }

    private PaintContext           ctx;

    private Rectangle2D            rect                = new Rectangle2D.Float(0, 0, 0, 0);
    private RoundRectangle2D       roundRect           = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color                  borderColor         = new Color(0x545454);

    private Color                  innerHighLightColor = new Color(0x55ffffff, true);

    private Color                  inactiveColor1      = new Color(0xededed);
    private Color                  inactiveColor2      = new Color(0xe0e0e0);

    private static final Insets    insets              = new Insets(5, 5, 5, 5);
    private static final Dimension dimension           = new Dimension(28, 26);
    private static final CacheMode cacheMode           = CacheMode.FIXED_SIZES;
    private static final Double    maxH                = Double.POSITIVE_INFINITY;
    private static final Double    maxV                = Double.POSITIVE_INFINITY;

    public DesktopIconPainter(Which state) {
        super();
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = decodeRoundRect1(width, height);
        g.setPaint(borderColor);
        g.fill(s);

        s = decodeRoundRect2(width, height);
        g.setPaint(innerHighLightColor);
        g.fill(s);

        s = decodeRect1(width, height);
        g.setPaint(decodeGradient2(s));
        g.fill(s);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private Shape decodeRoundRect1(int width, int height) {
        roundRect.setRoundRect(2, 0, width - 3, height - 2, 4.8333335f, 4.8333335f);
        return roundRect;
    }

    private Shape decodeRoundRect2(int width, int height) {
        roundRect.setRoundRect(3, 1, width - 5, height - 4, 3.1f, 3.1f);
        return roundRect;
    }

    private Shape decodeRect1(int width, int height) {
        rect.setRect(4, 2, width - 7, height - 6);
        return rect;
    }

    private Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 1.0f }, new Color[] {
            inactiveColor1,
            inactiveColor2 });
    }
}
