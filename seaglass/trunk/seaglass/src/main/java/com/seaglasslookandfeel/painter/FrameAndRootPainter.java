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
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

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

    private static final int       TITLE_BAR_HEIGHT        = 25;

    // Constants for the PaintContext.
    private static final Insets    insets                  = new Insets(25, 6, 6, 6);
    private static final Dimension dimension               = new Dimension(25, 36);
    private static final CacheMode cacheMode               = PaintContext.CacheMode.FIXED_SIZES;
    private static final Double    maxH                    = Double.POSITIVE_INFINITY;
    private static final Double    maxV                    = Double.POSITIVE_INFINITY;

    private static final ColorSet  active                  = new ColorSet(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true),
                                                               new Color(0x00A8D9FC, true), new Color(0xffb4d9ee, true), 0.4f, new Color(
                                                                   0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF));
    private static final ColorSet  inactive                = new ColorSet(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true),
                                                               new Color(0x00A8D9FC, true), new Color(0xffF7FCFF, true), 0.4f, new Color(
                                                                   0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF));

    private static final Color     borderColor             = new Color(0x545454);

    // private static final Color ACTIVE_TITLE_COLOR_T = new Color(0x7593b2);
    // private static final Color INACTIVE_TITLE_COLOR_T = new Color(0xededed);
    //
    // private static final Color ACTIVE_TOP_COLOR_T = new Color(0x6888ab);
    // private static final Color ACTIVE_TOP_COLOR_B = new Color(0x5a7da4);
    // private static final Color INACTIVE_TOP_COLOR_T = new Color(0xe9e9e9);
    // private static final Color INACTIVE_TOP_COLOR_B = new Color(0xe0e0e0);
    //
    // private static final Color ACTIVE_BOTTOM_COLOR_T = new Color(0x597ca3);
    // private static final Color ACTIVE_BOTTOM_COLOR_B = new Color(0x5b7fa5);
    // private static final Color INACTIVE_BOTTOM_COLOR_T = new Color(0xcfcfcf);
    // private static final Color INACTIVE_BOTTOM_COLOR_B = new Color(0xcacaca);

    // private static final Color ACTIVE_TITLE_COLOR_T = new Color(0x5f80a5);
    // private static final Color INACTIVE_TITLE_COLOR_T = new Color(0xededed);
    //
    // private static final Color ACTIVE_TOP_COLOR_T = new Color(0x466c97);
    // private static final Color ACTIVE_TOP_COLOR_B = new Color(0x466c97);
    // private static final Color INACTIVE_TOP_COLOR_T = new Color(0xe9e9e9);
    // private static final Color INACTIVE_TOP_COLOR_B = new Color(0xe0e0e0);
    //
    // private static final Color ACTIVE_BOTTOM_COLOR_T = new Color(0x5f80a5);
    // private static final Color ACTIVE_BOTTOM_COLOR_B = new Color(0x466c97);
    // private static final Color INACTIVE_BOTTOM_COLOR_T = new Color(0xcfcfcf);
    // private static final Color INACTIVE_BOTTOM_COLOR_B = new Color(0xcacaca);

    private static final Color     ACTIVE_TITLE_COLOR_T    = new Color(0x42628b);
    private static final Color     INACTIVE_TITLE_COLOR_T  = new Color(0xededed);

    private static final Color     ACTIVE_TOP_COLOR_T      = new Color(0x466c97);
    private static final Color     ACTIVE_TOP_COLOR_B      = new Color(0x466c97);
    private static final Color     INACTIVE_TOP_COLOR_T    = new Color(0xe9e9e9);
    private static final Color     INACTIVE_TOP_COLOR_B    = new Color(0xe0e0e0);

    private static final Color     ACTIVE_BOTTOM_COLOR_T   = new Color(0x5f80a5);
    private static final Color     ACTIVE_BOTTOM_COLOR_B   = new Color(0x2b4d7b);
    private static final Color     INACTIVE_BOTTOM_COLOR_T = new Color(0xcfcfcf);
    private static final Color     INACTIVE_BOTTOM_COLOR_B = new Color(0xcacaca);

    private static final Color     INNER_HIGHLIGHT_COLOR   = new Color(0x55ffffff, true);

    private static final State     toolBarNorthState       = new ToolBarNorthState();
    private static final State     toolBarSouthState       = new ToolBarSouthState();

    private Path2D                 path                    = new Path2D.Double();

    private Which                  state;
    private PaintContext           ctx;

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
         paintFrame(g, c, width, height, INACTIVE_TITLE_COLOR_T,
         INACTIVE_TOP_COLOR_T, INACTIVE_TOP_COLOR_B, INACTIVE_BOTTOM_COLOR_T,
         INACTIVE_BOTTOM_COLOR_B, inactive);
    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g, JComponent c, int width, int height) {
         paintFrame(g, c, width, height, ACTIVE_TITLE_COLOR_T,
         ACTIVE_TOP_COLOR_T, ACTIVE_TOP_COLOR_B, ACTIVE_BOTTOM_COLOR_T,
         ACTIVE_BOTTOM_COLOR_B, active);
    }

     private void paintFrame(Graphics2D g, JComponent c, int width, int
     height, Color topColor, Color titleBottomColor,
     Color topToolBarBottomColor, Color bottomToolBarTopColor, Color
     bottomColor, ColorSet colors) {
        Shape s = decodePath(0, 0, width - 1, height - 1, 8.0);
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

        s = decodePath(1, 1, width - 2, height - 2, 7.0);
         g.setPaint(decodeGradient(s, titleHeight, topToolBarHeight,
         bottomToolBarHeight, topColor, titleBottomColor,
         topToolBarBottomColor,
         bottomToolBarTopColor, bottomColor));
         if (true) {
            g.setColor(colors.mainColor);
            g.fill(s);
            g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
            g.fill(s);
            g.setPaint(decodeGradientTopShine(path, colors.upperShineTop, colors.upperShineBottom));
         }
        g.fill(s);

        s = decodePath(1, 1, width - 3, height - 3, 6.0);
        g.setPaint(INNER_HIGHLIGHT_COLOR);
        g.draw(s);
    }

    private Shape decodePath(double x, double y, double width, double height, double arc) {
        path.reset();
        arc = arc / 2.0;
        double r = x + width;
        double b = y + height;
        path.moveTo(x, b);
        path.lineTo(x, y + arc);
        path.quadTo(x, y, x + arc, y);
        path.lineTo(r - arc, y);
        path.quadTo(r, y, r, y + arc);
        path.lineTo(r, b);
        path.closePath();
        return path;
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
        if (true) {
            return decodeGradient(midX, y, x + midX, y + h, new float[] { 0f, 1f }, new Color[] { topColor, bottomColor });
        }
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

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the button.
     */
    public static class ColorSet {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public ColorSet(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom, float lowerShineMidpoint,
            Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShineTop = upperShineTop;
            this.upperShineBottom = upperShineBottom;
            this.lowerShineTop = lowerShineTop;
            this.lowerShineBottom = lowerShineBottom;
            this.lowerShineMidpoint = lowerShineMidpoint;
            this.mainColor = mainColor;
            this.backgroundTop = backgroundTop;
            this.backgroundBottom = backgroundBottom;
        }
    }
}
