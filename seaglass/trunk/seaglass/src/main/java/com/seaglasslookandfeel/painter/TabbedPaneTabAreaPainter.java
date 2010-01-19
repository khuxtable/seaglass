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

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.ui.SeaGlassTabbedPaneUI;

/**
 * Sea Glass TabbedPaneTabAreaPainter. Does nothing.
 */
public final class TabbedPaneTabAreaPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED_TOP, BACKGROUND_ENABLED_LEFT, BACKGROUND_ENABLED_BOTTOM, BACKGROUND_ENABLED_RIGHT,

        BACKGROUND_DISABLED_TOP, BACKGROUND_DISABLED_LEFT, BACKGROUND_DISABLED_BOTTOM, BACKGROUND_DISABLED_RIGHT,
    }

    private Color        backgroundColor       = decodeColor("control", 0f, 0f, 0f, 0);

    private Color        enabledBackLineColor  = new Color(0x647595);
    private Color        disabledBackLineColor = new Color(0x80647595, true);

    private Color        lightShadow           = new Color(0x55eeeeee, true);
    private Color        darkShadow            = new Color(0x55aaaaaa, true);

    private Which        state;
    private PaintContext ctx;

    public TabbedPaneTabAreaPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(500, 500), false, CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED_TOP:
            paintBackground(g, c, width, height, disabledBackLineColor);
            break;
        case BACKGROUND_DISABLED_LEFT:
            paintBackground(g, c, width, height, disabledBackLineColor);
            break;
        case BACKGROUND_DISABLED_BOTTOM:
            paintBackground(g, c, width, height, disabledBackLineColor);
            break;
        case BACKGROUND_DISABLED_RIGHT:
            paintBackground(g, c, width, height, disabledBackLineColor);
            break;
        case BACKGROUND_ENABLED_TOP:
            paintBackground(g, c, width, height, enabledBackLineColor);
            break;
        case BACKGROUND_ENABLED_LEFT:
            paintBackground(g, c, width, height, enabledBackLineColor);
            break;
        case BACKGROUND_ENABLED_BOTTOM:
            paintBackground(g, c, width, height, enabledBackLineColor);
            break;
        case BACKGROUND_ENABLED_RIGHT:
            paintBackground(g, c, width, height, enabledBackLineColor);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackground(Graphics2D g, JComponent c, int width, int height, Color lineColor) {
        g.setPaint(backgroundColor);
        g.fillRect(0, 0, width, height);

        JTabbedPane tabPane = (JTabbedPane) c;
        SeaGlassTabbedPaneUI ui = (SeaGlassTabbedPaneUI) tabPane.getUI();
        int orientation = tabPane.getTabPlacement();
        Insets insets = tabPane.getInsets();
        if (orientation == JTabbedPane.LEFT) {
            int offset = ui.calculateMaxTabWidth(orientation) + insets.left;
            paintVerticalLine(g, c, offset / 2 + 3, 0, width, height, lineColor);
        } else if (orientation == JTabbedPane.RIGHT) {
            int offset = ui.calculateMaxTabWidth(orientation);
            paintVerticalLine(g, c, offset / 2 + 3, 0, width, height, lineColor);
        } else if (orientation == JTabbedPane.BOTTOM) {
            int offset = ui.calculateMaxTabHeight(orientation);
            paintHorizontalLine(g, c, 0, offset / 2 + 3, width, height, lineColor);
        } else {
            int offset = ui.calculateMaxTabHeight(orientation) / 2 + insets.top;
            System.out.println("offset = " + offset);
            paintHorizontalLine(g, c, 0, offset + 3, width, height, lineColor);
        }
    }

    private void paintHorizontalLine(Graphics2D g, JComponent c, int x, int y, int width, int height, Color lineColor) {
        g.setPaint(decodeGradientHorizontalShadow(x, y - 1, width, 4));
        g.fillRect(x, y - 1, width, 4);
        g.setColor(lineColor);
        g.drawLine(x, y, x + width - 1, y);
    }

    private void paintVerticalLine(Graphics2D g, JComponent c, int x, int y, int width, int height, Color lineColor) {
        g.setPaint(decodeGradientVerticalShadow(x - 1, y, 3, height));
        g.fillRect(x - 1, y, 3, height);
        g.setColor(lineColor);
        g.drawLine(x, y, x, y + height - 1);
    }

    private Paint decodeGradientHorizontalShadow(int x, int y, int width, int height) {
        float midX = x + width / 2;
        return decodeGradient(midX, y, midX, y + height, new float[] { 0f, 0.5f, 1f }, new Color[] { lightShadow, darkShadow, lightShadow });
    }

    private Paint decodeGradientVerticalShadow(int x, int y, int width, int height) {
        float midY = y + height / 2;
        return decodeGradient(x, midY, x + width, midY, new float[] { 0f, 0.5f, 1f }, new Color[] { lightShadow, darkShadow, lightShadow });
    }
}
