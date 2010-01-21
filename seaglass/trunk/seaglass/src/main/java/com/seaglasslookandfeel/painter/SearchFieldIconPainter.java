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
import java.awt.geom.Path2D;

import javax.swing.JComponent;

/**
 * Search field icon implementation.
 */
public final class SearchFieldIconPainter extends AbstractRegionPainter {
    public static enum Which {
        FIND_ICON_DISABLED, FIND_ICON_ENABLED, FIND_ICON_ENABLED_POPUP,

        CANCEL_ICON_DISABLED, CANCEL_ICON_ENABLED, CANCEL_ICON_PRESSED,
    }

    private static final Color WHITE       = Color.WHITE;
    private final Color        GRAY        = Color.GRAY;
    private final Color        MEDIUM_GRAY = new Color(0xb3b3b3);
    private final Color        DARK_GRAY   = Color.DARK_GRAY;

    private Path2D             path        = new Path2D.Float();

    private Which              state;
    private PaintContext       ctx;

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
            paintSearchGlass(g, width, height, false);
            break;
        case FIND_ICON_ENABLED_POPUP:
            paintSearchGlass(g, width, height, true);
            break;
        case CANCEL_ICON_ENABLED:
            paintCancelIcon(g, width, height, MEDIUM_GRAY);
            break;
        case CANCEL_ICON_PRESSED:
            paintCancelIcon(g, width, height, GRAY);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintSearchGlass(Graphics2D g, int width, int height, boolean hasPopup) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int glassX = 2;
        final int glassY = 2;
        final int glassRadius = 8;

        g.setStroke(new BasicStroke(2));
        g.setColor(DARK_GRAY);
        g.drawOval(glassX, glassY, glassRadius, glassRadius);

        final int handleOffset = 7;
        final int handleX = glassX + handleOffset;
        final int handleY = glassY + handleOffset;
        final int handleLength = 3;

        g.drawLine(handleX, handleY, handleX + handleLength, handleY + handleLength);

        if (hasPopup) {
            final int popupX = glassX + glassRadius + 3;
            final int popupY = glassY + 3;
            final int popupWidth = 7;
            final int popupHeight = 4;

            path.reset();
            path.moveTo(popupX, popupY);
            path.lineTo(popupX + popupWidth, popupY);
            path.lineTo(popupX + popupWidth / 2.0, popupY + popupHeight);
            path.closePath();

            g.setColor(DARK_GRAY);
            g.fill(path);
        }
    }

    private void paintCancelIcon(Graphics2D g, int width, int height, Color color) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int circleRadius = height - 4;
        final int circleX = 2;
        final int circleY = 2;

        g.setColor(color);
        g.fillOval(circleX, circleY, circleRadius, circleRadius);

        final int lineLength = circleRadius - 9;
        final int lineX = circleX + 4;
        final int lineY = circleY + 4;

        g.setColor(WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawLine(lineX, lineY, lineX + lineLength, lineY + lineLength);
        g.drawLine(lineX, lineY + lineLength, lineX + lineLength, lineY);
    }
}
