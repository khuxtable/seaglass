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
import com.seaglasslookandfeel.state.ToolBarNorthState;

/**
 * Nimbus's InternalFramePainter.
 */
public final class InternalFramePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    };

    // Constants for the PaintContext.
    private static final Insets            insets                     = new Insets(25, 6, 6, 6);
    private static final Dimension         dimension                  = new Dimension(25, 36);
    private static final CacheMode         cacheMode                  = PaintContext.CacheMode.FIXED_SIZES;
    private static final Double            maxH                       = Double.POSITIVE_INFINITY;
    private static final Double            maxV                       = Double.POSITIVE_INFINITY;

    private static final ToolBarNorthState toolBarNorthState          = new ToolBarNorthState();

    private Which                          state;
    private PaintContext                   ctx;

    private RoundRectangle2D               roundRect                  = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color                          borderColor                = new Color(0x808080);

    private Color                          activeTopColor             = new Color(0xececec);
    private Color                          activeBottomTitleColor     = new Color(0xc9c9c9);
    private Color                          activeBottomToolbarColor   = new Color(0xa7a7a7);
    private Color                          activeBottomColor          = new Color(0xa6a6a6);

    private Color                          inactiveTopColor           = new Color(0xececec);
    private Color                          inactiveBottomTitleColor   = new Color(0xe7e7e7);
    private Color                          inactiveBottomToolbarColor = new Color(0xd8d8d8);
    private Color                          inactiveBottomColor        = new Color(0xd7d7d7);

    public InternalFramePainter(Which state) {
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
        paintFrame(g, c, width, height, inactiveTopColor, inactiveBottomTitleColor, inactiveBottomToolbarColor, inactiveBottomColor);
    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g, JComponent c, int width, int height) {
        paintFrame(g, c, width, height, activeTopColor, activeBottomTitleColor, activeBottomToolbarColor, activeBottomColor);
    }

    private void paintFrame(Graphics2D g, JComponent c, int width, int height, Color color1, Color color2, Color color3, Color color4) {
        roundRect.setRoundRect(0, 0, width - 1, height - 1, 8.0f, 8.0f);
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
        JToolBar tb = null;
        if (cArray != null) {
            for (Component comp : cArray) {
                if (comp instanceof JToolBar && toolBarNorthState.isInState((JComponent) comp)) {
                    tb = (JToolBar) comp;
                    break;
                }
            }
        }

        int titleHeight = 25;
        if (mb != null) {
            titleHeight += mb.getHeight();
        }

        int toolBarHeight = 0;
        if (tb != null) {
            toolBarHeight = tb.getHeight();
        }

        roundRect.setRoundRect(1, 1, width - 2, height - 2, 7.0f, 7.0f);
        g.setPaint(decodeGradient(roundRect, titleHeight, toolBarHeight, color1, color2, color3, color4));
        g.fill(roundRect);
    }

    private Paint decodeGradient(Shape s, int titleHeight, int toolBarHeight, Color color1, Color color2, Color color3, Color color4) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();

        float midX = x + w / 2.0f;
        float titleBottom = titleHeight / h;
        if (toolBarHeight > 0) {
            float toolBarBottom = (titleHeight + toolBarHeight) / h;
            return decodeGradient(midX, y, x + midX, y + h, new float[] {
                0.0f,
                titleBottom,
                titleBottom + 0.00001f,
                toolBarBottom,
                toolBarBottom + 0.00001f,
                1.0f }, new Color[] { color1, color2, color2, color3, color3, color4 });
        } else {
            return decodeGradient(midX, y, x + midX, y + h, new float[] { 0.0f, titleBottom, titleBottom + 0.00001f, 1.0f }, new Color[] {
                color1,
                color2,
                color2,
                color4 });
        }
    }
}
