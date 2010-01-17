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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
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

    private final Color  GRAY        = Color.GRAY;
    private final Color  MEDIUM_GRAY = new Color(179, 179, 179);
    private final Color  DARK_GRAY   = Color.DARK_GRAY;

    private Which        state;
    private PaintContext ctx;

    public SearchFieldIconPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case FIND_ICON_DISABLED:
        case FIND_ICON_ENABLED:
        case FIND_ICON_ENABLED_POPUP:
            ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(20, 15), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            break;
        case CANCEL_ICON_DISABLED:
        case CANCEL_ICON_ENABLED:
        case CANCEL_ICON_PRESSED:
            ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(15, 15), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FIND_ICON_ENABLED:
            paintSearchGlass(g, width, height);
            break;
        case FIND_ICON_ENABLED_POPUP:
            paintSearchGlass(g, width, height);
            paintPopupIcon(g, width, height);
            break;
        case CANCEL_ICON_ENABLED:
            paintCancelIcon(g, width, height, false);
            break;
        case CANCEL_ICON_PRESSED:
            paintCancelIcon(g, width, height, true);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintSearchGlass(Graphics2D g, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(new BasicStroke(2));
        final int glassL = 8;
        final int handleL = 3;
        final int handleO = 7;
        final int glassX = 2;
        final int glassY = 2;
        g.setColor(DARK_GRAY);
        g.drawOval(glassX, glassY, glassL, glassL);
        g.drawLine(glassX + handleO, glassY + handleO, glassX + handleO + handleL, glassY + handleO + handleL);
    }

    private void paintPopupIcon(Graphics2D g, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int dropX = 13;
        final int dropY = 5;
        Path2D path = new Path2D.Float();
        path.moveTo(dropX, dropY);
        path.lineTo(dropX + 7, dropY);
        path.lineTo(dropX + 3.5, dropY + 4);
        path.closePath();
        g.fill(path);
    }

    private void paintCancelIcon(Graphics2D g, int width, int height, boolean isArmed) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int circleL = height + 2;
        final int circleX = width - circleL;
        final int circleY = (height - 1 - circleL) / 2;
        g.setColor(isArmed ? GRAY : MEDIUM_GRAY);
        g.fillOval(circleX, circleY, circleL, circleL);
        final int lineL = circleL - 9;
        final int lineX = circleX + 4;
        final int lineY = circleY + 4;
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(lineX, lineY, lineX + lineL, lineY + lineL);
        g.drawLine(lineX, lineY + lineL, lineX + lineL, lineY);
    }
}
