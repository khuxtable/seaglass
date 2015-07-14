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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

/**
 * Search field icon implementation.
 */
public final class SearchFieldIconPainter extends AbstractRegionPainter {
    public static enum Which {
        FIND_ICON_DISABLED, FIND_ICON_ENABLED, FIND_ICON_ENABLED_POPUP,

        CANCEL_ICON_DISABLED, CANCEL_ICON_ENABLED, CANCEL_ICON_PRESSED,
    }

    private Color        searchIconColor        = decodeColor("seaGlassSearchIcon");
    private Color        cancelIconColor        = decodeColor("seaGlassCancelIcon");
    private Color        cancelIconPressedColor = decodeColor("seaGlassCancelIconPressed");

    private Which        state;
    private PaintContext ctx;

    public SearchFieldIconPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FIND_ICON_ENABLED:
            paintSearchGlass(g, 0, 0, width, height, false);
            break;
        case FIND_ICON_ENABLED_POPUP:
            paintSearchGlass(g, 0, 0, width, height, true);
            break;
        case CANCEL_ICON_ENABLED:
            paintCancelIcon(g, 0, 0, width, height, cancelIconColor);
            break;
        case CANCEL_ICON_PRESSED:
            paintCancelIcon(g, 0, 0, width, height, cancelIconPressedColor);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintSearchGlass(Graphics2D g, int x, int y, int width, int height, boolean hasPopup) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int glassX = 2;
        final int glassY = 2;
        final int glassRadius = 8;

        g.setStroke(new BasicStroke(2));
        g.setColor(searchIconColor);
        g.drawOval(glassX, glassY, glassRadius, glassRadius);

        final int handleOffset = 7;
        final int handleX = glassX + handleOffset;
        final int handleY = glassY + handleOffset;
        final int handleLength = 3;

        g.drawLine(handleX, handleY, handleX + handleLength, handleY + handleLength);

        if (hasPopup) {
            final int popupX = glassX + glassRadius + 3;
            final int popupY = glassY + 3;

            Shape s = shapeGenerator.createArrowDown(popupX, popupY, 7, 4);
            g.setColor(searchIconColor);
            g.fill(s);
        }
    }

    private void paintCancelIcon(Graphics2D g, int x, int y, int width, int height, Color color) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = shapeGenerator.createCancelIcon(2, 2, width - 4, height - 4);
        g.setColor(color);
        g.fill(s);
    }
}
