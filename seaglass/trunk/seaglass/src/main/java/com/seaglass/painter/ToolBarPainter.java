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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglass.util.PlatformUtils;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarPainter extends AbstractRegionPainter {
    final boolean IS_MAC_OSX = PlatformUtils.isMac();

    public static enum Which {
        BORDER_NORTH,
        BORDER_SOUTH,
        BORDER_EAST,
        BORDER_WEST,
        BORDER_NORTH_ENABLED,
        BORDER_SOUTH_ENABLED,
        BORDER_EAST_ENABLED,
        BORDER_WEST_ENABLED,
        HANDLEICON_ENABLED
    };

    private static final Insets    insets                         = new Insets(0, 0, 0, 0);
    private static final Dimension dimension                      = new Dimension(30, 30);
    private static final CacheMode cacheMode                      = CacheMode.NO_CACHING;
    private static final Double    maxH                           = 1.0;
    private static final Double    maxV                           = 1.0;

    private static final Insets    handleInsets                   = new Insets(5, 5, 5, 5);
    private static final Dimension handleDimension                = new Dimension(11, 38);
    private static final CacheMode handleCacheMode                = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    handleMaxH                     = 2.0;
    private static final Double    handleMaxV                     = Double.POSITIVE_INFINITY;

    // For non-Mac use Snow Leopard colors because it has the same Gamma
    // correction.
    private Color                  ACTIVE_TOP_GRADIENT_COLOR      = IS_MAC_OSX ? new Color(0xbcbcbc) : new Color(0xc4c4c4);
    private Color                  ACTIVE_BOTTOM_GRADIENT_COLOR   = IS_MAC_OSX ? new Color(0x9a9a9a) : new Color(0xb2b2b2);
    private Color                  INACTIVE_TOP_GRADIENT_COLOR    = IS_MAC_OSX ? new Color(0xe4e4e4) : new Color(0xe7e7e7);
    private Color                  INACTIVE_BOTTOM_GRADIENT_COLOR = IS_MAC_OSX ? new Color(0xd1d1d1) : new Color(0xdfdfdf);

    private Color                  topColor;
    private Color                  bottomColor;

    // Refers to one of the static final ints above
    private Which                  state;
    private PaintContext           ctx;

    public ToolBarPainter(Which state) {
        super();
        this.state = state;
        if (state == Which.HANDLEICON_ENABLED) {
            this.ctx = new PaintContext(handleInsets, handleDimension, false, handleCacheMode, handleMaxH, handleMaxV);
        } else {
            this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
        }

        topColor = null;
        bottomColor = null;

        switch (state) {
        case BORDER_NORTH:
        case BORDER_SOUTH:
        case BORDER_EAST:
        case BORDER_WEST:
            topColor = INACTIVE_TOP_GRADIENT_COLOR;
            bottomColor = INACTIVE_BOTTOM_GRADIENT_COLOR;
            break;
        case BORDER_NORTH_ENABLED:
        case BORDER_SOUTH_ENABLED:
        case BORDER_EAST_ENABLED:
        case BORDER_WEST_ENABLED:
            topColor = ACTIVE_TOP_GRADIENT_COLOR;
            bottomColor = ACTIVE_BOTTOM_GRADIENT_COLOR;
            break;
        }
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (state == Which.HANDLEICON_ENABLED) {
            painthandleIconEnabled(g);
            return;
        }

        GradientPaint paint = null;
        switch (state) {
        case BORDER_NORTH:
        case BORDER_NORTH_ENABLED:
            paint = new GradientPaint(0, 1, topColor, 0, height, bottomColor);
            break;
        case BORDER_SOUTH:
        case BORDER_SOUTH_ENABLED:
            paint = new GradientPaint(0, 1, topColor, 0, height, bottomColor);
            break;
        case BORDER_EAST:
        case BORDER_EAST_ENABLED:
            paint = new GradientPaint(1, 0, topColor, width, 0, bottomColor);
            break;
        case BORDER_WEST:
        case BORDER_WEST_ENABLED:
            paint = new GradientPaint(1, 0, topColor, width, 0, bottomColor);
            break;
        }

        g.setPaint(paint);
        g.fillRect(0, 0, width, height);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private Path2D      path   = new Path2D.Float();
    private Rectangle2D rect   = new Rectangle2D.Float(0, 0, 0, 0);

    private Color       color2 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color       color3 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);
    private Color       color4 = decodeColor("nimbusBorder", 0.0f, -0.029675633f, 0.109803915f, 0);
    private Color       color5 = decodeColor("nimbusBlueGrey", -0.008547008f, -0.03494492f, -0.07058823f, 0);

    private void painthandleIconEnabled(Graphics2D g) {
        rect = decodeRect3();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color4);
        g.fill(rect);
        path = decodePath1();
        g.setPaint(color5);
        g.fill(path);
        path = decodePath2();
        g.setPaint(color5);
        g.fill(path);
    }

    private Rectangle2D decodeRect3() {
        rect.setRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(2.8f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect4() {
        rect.setRect(decodeX(2.8f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(2.8f), // width
            decodeY(3.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(0.4f));
        path.lineTo(decodeX(0.4f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(0.0f));
        path.closePath();
        return path;
    }

    private Path2D decodePath2() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(2.6f));
        path.lineTo(decodeX(0.4f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(3.0f));
        path.closePath();
        return path;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.0f * w) + x, (0.5f * h) + y, (1.0f * w) + x, (0.5f * h) + y, new float[] { 0.0f, 0.5f, 1.0f },
            new Color[] { color2, decodeColor(color2, color3, 0.5f), color3 });
    }
}
