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
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public class MenuItemPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_MOUSEOVER, BACKGROUND_MOUSEOVER_UNIFIED
    }

    private Color        menuItemBackgroundBase   = decodeColor("menuItemBackgroundBase");

    private Color        menuItemBackgroundTop    = deriveColor(menuItemBackgroundBase, -0.003425f, -0.027540f, 0.070588f, 0);
    private Color        menuItemBackgroundBottom = deriveColor(menuItemBackgroundBase, 0.001337f, 0.040989f, -0.078431f, 0);

    private TwoColors    menuItemBackground       = new TwoColors(menuItemBackgroundTop, menuItemBackgroundBottom);
    private Color        menuItemBottomLine       = deriveColor(menuItemBackgroundBase, 0.006069f, 0.131520f, -0.105882f, 0);

    private Which        state;
    private PaintContext ctx;

    public MenuItemPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_MOUSEOVER:
            paintBackgroundMouseOver(g, c, width, height);
            break;
        case BACKGROUND_MOUSEOVER_UNIFIED:
            paintBackgroundMouseOverUnified(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    protected void paintBackgroundMouseOver(Graphics2D g, JComponent c, int width, int height) {
        Shape s = shapeGenerator.createRectangle(0, 0, width, height);
        g.setPaint(getMenuItemBackgroundPaint(s));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(s);
        Rectangle b = s.getBounds();
        int width1 = b.width;
        int height1 = b.height;
        g.setColor(getMenuItemBottomLinePaint(c));
        g.drawLine(0, height1 - 1, width1 - 1, height1 - 1);
    }
    
    protected void paintBackgroundMouseOverUnified(Graphics2D g, JComponent c, int width, int height) {
        Shape s = shapeGenerator.createRectangle(0, 0, width, height);
        g.setPaint(getMenuItemBackgroundPaintUnified(s));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(s);
    }

    public Paint getMenuItemBackgroundPaint(Shape s) {
        return createVerticalGradient(s, menuItemBackground);
    }

    public Paint getMenuItemBackgroundPaintUnified(Shape s) {
        return createVerticalGradient(s, new TwoColors(Color.WHITE, Color.WHITE));
    }

    public Color getMenuItemBottomLinePaint(JComponent c) {
        return menuItemBottomLine;
    }
}
