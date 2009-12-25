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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.util.PlatformUtils;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarPainter extends AbstractRegionPainter {
    private static final boolean IS_MAC_OSX = PlatformUtils.isMac() && !PlatformUtils.isSnowLeopard();

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

    private static final Insets    insets                  = new Insets(0, 0, 0, 0);
    private static final Dimension dimension               = new Dimension(30, 30);
    private static final CacheMode cacheMode               = CacheMode.NO_CACHING;
    private static final Double    maxH                    = 1.0;
    private static final Double    maxV                    = 1.0;

    private static final Insets    handleInsets            = new Insets(5, 5, 5, 5);
    private static final Dimension handleDimension         = new Dimension(11, 38);
    private static final CacheMode handleCacheMode         = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    handleMaxH              = 2.0;
    private static final Double    handleMaxV              = Double.POSITIVE_INFINITY;

    // For non-Mac use Snow Leopard colors because it has the same Gamma
    // correction.
    private static final Color     ACTIVE_TOP_COLOR_T      = IS_MAC_OSX ? new Color(0xbcbcbc) : new Color(0xc9c9c9);
    private static final Color     ACTIVE_TOP_COLOR_B      = IS_MAC_OSX ? new Color(0x9a9a9a) : new Color(0xb7b7b7);
    private static final Color     INACTIVE_TOP_COLOR_T    = IS_MAC_OSX ? new Color(0xe4e4e4) : new Color(0xe9e9e9);
    private static final Color     INACTIVE_TOP_COLOR_B    = IS_MAC_OSX ? new Color(0xd1d1d1) : new Color(0xe0e0e0);

    private static final Color     ACTIVE_BOTTOM_COLOR_T   = IS_MAC_OSX ? new Color(0xcccccc) : new Color(0x999999);
    private static final Color     ACTIVE_BOTTOM_COLOR_B   = IS_MAC_OSX ? new Color(0xa7a7a7) : new Color(0x909090);
    private static final Color     INACTIVE_BOTTOM_COLOR_T = IS_MAC_OSX ? new Color(0xe9e9e9) : new Color(0xcfcfcf);
    private static final Color     INACTIVE_BOTTOM_COLOR_B = IS_MAC_OSX ? new Color(0xd8d8d8) : new Color(0xcacaca);

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
            topColor = INACTIVE_TOP_COLOR_T;
            bottomColor = INACTIVE_TOP_COLOR_B;
            break;
        case BORDER_SOUTH:
            topColor = INACTIVE_BOTTOM_COLOR_T;
            bottomColor = INACTIVE_BOTTOM_COLOR_B;
            break;
        case BORDER_EAST:
        case BORDER_WEST:
            topColor = INACTIVE_TOP_COLOR_B;
            bottomColor = INACTIVE_BOTTOM_COLOR_T;
            break;
        case BORDER_NORTH_ENABLED:
            topColor = ACTIVE_TOP_COLOR_T;
            bottomColor = ACTIVE_TOP_COLOR_B;
            break;
        case BORDER_SOUTH_ENABLED:
            topColor = ACTIVE_BOTTOM_COLOR_T;
            bottomColor = ACTIVE_BOTTOM_COLOR_B;
            break;
        case BORDER_EAST_ENABLED:
        case BORDER_WEST_ENABLED:
            topColor = ACTIVE_TOP_COLOR_B;
            bottomColor = ACTIVE_BOTTOM_COLOR_T;
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
            paint = new GradientPaint(0, 0, topColor, 0, height, bottomColor);
            break;
        case BORDER_SOUTH:
        case BORDER_SOUTH_ENABLED:
            paint = new GradientPaint(0, 0, topColor, 0, height, bottomColor);
            break;
        case BORDER_EAST:
        case BORDER_EAST_ENABLED:
        case BORDER_WEST:
        case BORDER_WEST_ENABLED:
            paint = new GradientPaint(0, 0, topColor, width, 0, bottomColor);
            break;
        }

        g.setPaint(paint);
        g.fillRect(0, 0, width, height);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private Path2D path  = new Path2D.Float();

    private Color  color = new Color(25, 25, 25, 200);

    private void painthandleIconEnabled(Graphics2D g) {
        path.reset();
        path.moveTo(decodeX(0.75f), decodeY(0.5f));
        path.lineTo(decodeX(0.75f), decodeY(2.5f));
        path.lineTo(decodeX(1.25f), decodeY(2.5f));
        path.lineTo(decodeX(1.25f), decodeY(0.5f));
        path.closePath();

        g.setColor(color);
        g.draw(path);
    }
}
