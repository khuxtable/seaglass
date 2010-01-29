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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
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

    private static final int      TITLE_BAR_HEIGHT      = 25;

    private static final ColorSet active                = new ColorSet(new Color(0xafbecf), new Color(0x96adc4), new Color(0x96adc4),
                                                            new Color(0x8ea7c0));
    private static final ColorSet inactive              = new ColorSet(new Color(0xededed), new Color(0xe0e0e0), new Color(0xe0e0e0),
                                                            new Color(0xd3d3d3));

    private static final Color    borderColor           = new Color(0x545454);

    private static final Color    INNER_HIGHLIGHT_COLOR = new Color(0x55ffffff, true);

    private static final State    toolBarNorthState     = new ToolBarNorthState();
    private static final State    toolBarSouthState     = new ToolBarSouthState();

    private Which                 state;
    private PaintContext          ctx;

    public FrameAndRootPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(PaintContext.CacheMode.FIXED_SIZES);
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
        paintFrame(g, c, width, height, inactive);
    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g, JComponent c, int width, int height) {
        paintFrame(g, c, width, height, active);
    }

    private void paintFrame(Graphics2D g, JComponent c, int width, int height, ColorSet colors) {
        Shape s = decodePath(CornerSize.INNER_FOCUS, 0, 0, width - 1, height - 1);
        g.setColor(borderColor);
        g.draw(s);

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

        s = decodePath(CornerSize.BORDER, 1, 1, width - 2, height - 2);
        g.setPaint(decodeGradient(s, titleHeight, topToolBarHeight, bottomToolBarHeight, colors.topColorT, colors.topColorB,
            colors.bottomColorT, colors.bottomColorB));
        g.fill(s);

        s = decodePath(CornerSize.INTERIOR, 1, 1, width - 3, height - 3);
        g.setPaint(INNER_HIGHLIGHT_COLOR);
        g.draw(s);
    }

    private Shape decodePath(CornerSize size, int x, int y, int width, int height) {
        return ShapeUtil.createRoundRectangle(x, y, width, height, size);
    }

    private Paint decodeGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, Color topColorT, Color topColorB,
        Color bottomColorT, Color bottomColorB) {
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

            midPoints = new float[] { 0.0f, topToolBarBottom, bottomToolBarTop, 1.0f };
            colors = new Color[] { topColorT, topColorB, bottomColorT, bottomColorB };
        } else if (topToolBarHeight > 0) {
            float toolBarBottom = (titleHeight + topToolBarHeight) / h;
            if (toolBarBottom >= 1.0f) {
                toolBarBottom = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, toolBarBottom, 1.0f };
            colors = new Color[] { topColorT, topColorB, bottomColorT };
        } else if (bottomToolBarHeight > 0) {
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;
            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, bottomToolBarTop, 1.0f };
            colors = new Color[] { topColorT, topColorB, bottomColorT, bottomColorB };
        } else {
            midPoints = new float[] { 0.0f, titleBottom, 1.0f };
            colors = new Color[] { topColorT, topColorB, topColorB };
        }

        return decodeGradient(midX, y, x + midX, y + h, midPoints, colors);
    }

    /**
     * A set of colors to use for the button.
     */
    public static class ColorSet {

        public Color topColorT;
        public Color topColorB;
        public Color bottomColorT;
        public Color bottomColorB;

        public ColorSet(Color topColorT, Color topColorB, Color bottomColorT, Color bottomColorB) {
            this.topColorT = topColorT;
            this.topColorB = topColorB;
            this.bottomColorT = bottomColorT;
            this.bottomColorB = bottomColorB;
        }
    }
}
