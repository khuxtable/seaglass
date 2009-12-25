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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.ToolBarNorthState;
import com.seaglasslookandfeel.state.ToolBarSouthState;

/**
 * Nimbus's FrameAndRootPainter.
 */
public final class FrameAndRootPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    };

    private static final int              TITLE_BAR_HEIGHT        = 25;

    // Constants for the PaintContext.
    private static final Insets           insets                  = new Insets(25, 6, 6, 6);
    private static final Dimension        dimension               = new Dimension(25, 36);
    private static final CacheMode        cacheMode               = PaintContext.CacheMode.FIXED_SIZES;
    private static final Double           maxH                    = Double.POSITIVE_INFINITY;
    private static final Double           maxV                    = Double.POSITIVE_INFINITY;

    private static final Color            borderColor             = new Color(0x808080);

    private static final Color            ACTIVE_TITLE_COLOR_T    = new Color(0xd0d0d0);
    private static final Color            INACTIVE_TITLE_COLOR_T  = new Color(0xededed);

    private static final Color            ACTIVE_TOP_COLOR_T      = new Color(0xc9c9c9);
    private static final Color            ACTIVE_TOP_COLOR_B      = new Color(0xb7b7b7);
    private static final Color            INACTIVE_TOP_COLOR_T    = new Color(0xe9e9e9);
    private static final Color            INACTIVE_TOP_COLOR_B    = new Color(0xe0e0e0);

    private static final Color            ACTIVE_BOTTOM_COLOR_T   = new Color(0x999999);
    private static final Color            ACTIVE_BOTTOM_COLOR_B   = new Color(0x909090);
    private static final Color            INACTIVE_BOTTOM_COLOR_T = new Color(0xcfcfcf);
    private static final Color            INACTIVE_BOTTOM_COLOR_B = new Color(0xcacaca);

    private static final State            toolBarNorthState       = new ToolBarNorthState();
    private static final State            toolBarSouthState       = new ToolBarSouthState();

    private static final RoundRectangle2D roundRect               = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Which                         state;
    private PaintContext                  ctx;

    public FrameAndRootPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            paintBackgroundEnabledAndWindowFocused(g, c, width, height);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintFrame(g, c, width, height, INACTIVE_TITLE_COLOR_T, INACTIVE_TOP_COLOR_T, INACTIVE_TOP_COLOR_B, INACTIVE_BOTTOM_COLOR_T,
            INACTIVE_BOTTOM_COLOR_B);
    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g, JComponent c, int width, int height) {
        paintFrame(g, c, width, height, ACTIVE_TITLE_COLOR_T, ACTIVE_TOP_COLOR_T, ACTIVE_TOP_COLOR_B, ACTIVE_BOTTOM_COLOR_T,
            ACTIVE_BOTTOM_COLOR_B);
    }

    private void paintFrame(Graphics2D g, JComponent c, int width, int height, Color topColor, Color titleBottomColor,
        Color topToolBarBottomColor, Color bottomToolBarTopColor, Color bottomColor) {
        roundRect.setRoundRect(0, 0, width - 1, height - 1, 8.0, 8.0);
        g.setColor(borderColor);
        g.draw(roundRect);

        JMenuBar mb = null;
        Component[] cArray = null;
        if (c instanceof JInternalFrame) {
            JInternalFrame iframe = (JInternalFrame) c;
            mb = iframe.getJMenuBar();
            cArray = iframe.getContentPane().getComponents();
        } else if (c instanceof JRootPane) {
            JRootPane root = (JRootPane) c;
            mb = root.getJMenuBar();
            cArray = root.getContentPane().getComponents();
        }

        int topToolBarHeight = 0;
        int bottomToolBarHeight = 0;
        if (cArray != null) {
            for (Component comp : cArray) {
                if (comp instanceof JToolBar) {
                    if (toolBarNorthState.isInState((JComponent) comp)) {
                        topToolBarHeight = comp.getHeight();
                    } else if (toolBarSouthState.isInState((JComponent) comp)) {
                        bottomToolBarHeight = comp.getHeight();
                    }
                }
            }
        }

        int titleHeight = TITLE_BAR_HEIGHT;
        if (mb != null && !"true".equals(c.getClientProperty("SeaGlass.JRootPane.MenuInTitle"))) {
            titleHeight += mb.getHeight();
        }

        roundRect.setRoundRect(1, 1, width - 2, height - 2, 7.0, 7.0);
        g.setPaint(decodeGradient(roundRect, titleHeight, topToolBarHeight, bottomToolBarHeight, topColor, titleBottomColor,
            topToolBarBottomColor, bottomToolBarTopColor, bottomColor));
        g.fill(roundRect);
    }

    private Paint decodeGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, Color topColor,
        Color titleBottomColor, Color topToolBarBottomColor, Color bottomToolBarTopColor, Color bottomColor) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();

        float midX = x + w / 2.0f;
        float titleBottom = titleHeight / h;
        if (titleBottom >= 1.0f) {
            titleBottom = 1.0f - 0.00004f;
        }

        float[] midPoints = null;
        Color[] colors = null;
        if (topToolBarHeight > 0 && bottomToolBarHeight > 0) {
            float topToolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (topToolBarBottom >= 1.0f) {
                topToolBarBottom = 1.0f - 0.00002f;
            }
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] {
                0.0f,
                titleBottom,
                titleBottom + 0.00001f,
                topToolBarBottom,
                topToolBarBottom + 0.00001f,
                bottomToolBarTop,
                bottomToolBarTop + 0.00001f,
                1.0f };
            colors = new Color[] {
                topColor,
                titleBottomColor,
                titleBottomColor,
                topToolBarBottomColor,
                topToolBarBottomColor,
                bottomToolBarTopColor,
                bottomToolBarTopColor,
                bottomColor };
        } else if (topToolBarHeight > 0) {
            float toolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (toolBarBottom >= 1.0f) {
                toolBarBottom = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, titleBottom + 0.00001f, toolBarBottom, toolBarBottom + 0.00001f, 1.0f };
            colors = new Color[] { topColor, titleBottomColor, titleBottomColor, topToolBarBottomColor, topToolBarBottomColor, bottomColor };
        } else if (bottomToolBarHeight > 0) {
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, titleBottom + 0.00001f, bottomToolBarTop, bottomToolBarTop + 0.00001f, 1.0f };
            colors = new Color[] { topColor, titleBottomColor, titleBottomColor, bottomToolBarTopColor, bottomToolBarTopColor, bottomColor };
        } else {
            midPoints = new float[] { 0.0f, titleBottom, titleBottom + 0.00001f, 1.0f };
            colors = new Color[] { topColor, titleBottomColor, titleBottomColor, bottomColor };
        }

        return decodeGradient(midX, y, x + midX, y + h, midPoints, colors);
    }
}
