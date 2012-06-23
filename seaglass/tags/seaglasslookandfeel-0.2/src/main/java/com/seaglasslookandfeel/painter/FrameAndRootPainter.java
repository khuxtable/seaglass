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

import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.ToolBarNorthState;
import com.seaglasslookandfeel.state.ToolBarSouthState;
import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;

/**
 * Sea Glass FrameAndRootPainter.
 */
public final class FrameAndRootPainter extends AbstractRegionPainter {

    public static final int TITLE_BAR_HEIGHT = 25;

    private static final State toolBarNorthState = new ToolBarNorthState();
    private static final State toolBarSouthState = new ToolBarSouthState();

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    }

    private Color frameBorderBase = decodeColor("frameBorderBase");

    private Color frameInnerHighlightInactive = decodeColor("frameInnerHighlightInactive");
    private Color frameInnerHighlightActive   = decodeColor("frameInnerHighlightActive");

    private Color frameBaseActive   = decodeColor("frameBaseActive");
    private Color frameBaseInactive = decodeColor("frameBaseInactive");

    private Color frameBorderActive   = frameBorderBase;
    private Color frameBorderInactive = frameBorderBase;

    private Color frameTopActive        = deriveColor(frameBaseActive, 0.005208f, -0.080105f, 0.043137f, 0);
    private Color frameUpperMidActive   = frameBaseActive;
    private Color frameLowerMidActive   = frameBaseActive;
    private Color frameBottomActive     = deriveColor(frameBaseActive, 0f, 0.025723f, -0.015686f, 0);
    private Color frameTopInactive      = deriveColor(frameBaseInactive, 0f, 0f, 0.050980f, 0);
    private Color frameUpperMidInactive = frameBaseInactive;
    private Color frameLowerMidInactive = frameBaseInactive;
    private Color frameBottomInactive   = deriveColor(frameBaseInactive, 0f, 0f, -0.050980f, 0);

    private FourColors frameActive   = new FourColors(frameTopActive, frameUpperMidActive, frameLowerMidActive,
                                                      frameBottomActive);
    private FourColors frameInactive = new FourColors(frameTopInactive, frameUpperMidInactive, frameLowerMidInactive,
                                                      frameBottomInactive);

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new FrameAndRootPainter object.
     *
     * @param state the control state to paint.
     */
    public FrameAndRootPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(PaintContext.CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (state == Which.BACKGROUND_ENABLED_NOFRAME) {
            return;
        }

        Shape s = shapeGenerator.createRoundRectangle(0, 0, (width - 1), (height - 1), CornerSize.FRAME_BORDER,
                                                      CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);

        g.setPaint(getFrameBorderPaint(s));
        g.draw(s);

        JMenuBar    mb     = null;
        Component[] cArray = null;

        if (c instanceof JInternalFrame) {
            JInternalFrame iframe = (JInternalFrame) c;

            mb     = iframe.getJMenuBar();
            cArray = iframe.getContentPane().getComponents();
        } else if (c instanceof JRootPane) {
            JRootPane root = (JRootPane) c;

            mb     = root.getJMenuBar();
            cArray = root.getContentPane().getComponents();
        }

        int topToolBarHeight    = 0;
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

        if (mb != null && c.getClientProperty("SeaGlass.JRootPane.MenuInTitle") == Boolean.TRUE) {
            titleHeight += mb.getHeight();
        }

        if (c.getClientProperty(SeaGlassRootPaneUI.UNIFIED_TOOLBAR_LOOK) == Boolean.TRUE) {
            // Draw background gradient.
            s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.FRAME_INNER_HIGHLIGHT,
                                                    CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
            g.setPaint(getFrameInteriorPaint(s, titleHeight, topToolBarHeight, bottomToolBarHeight));
            g.fill(s);
        } else {
            // Paint title bar.
            s = shapeGenerator.createRoundRectangle(1, 1, width - 2, titleHeight, CornerSize.FRAME_INNER_HIGHLIGHT,
                                                    CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
            g.setPaint(getTitleBarInteriorPaint(s, titleHeight));
            g.fill(s);
            // Paint contents.
           s = shapeGenerator.createRoundRectangle(1, titleHeight, width - 2, height - titleHeight - 1, CornerSize.FRAME_INNER_HIGHLIGHT,
                                                    CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE);
            g.setPaint(c.getBackground());
            g.fill(s);
            // Draw separator line.
            g.setPaint(decodeColor("seaGlassMenuIcon"));
            g.drawLine(1, titleHeight, width - 2, titleHeight);
        }

        s = shapeGenerator.createRoundRectangle(1, 1, width - 3, height - 3, CornerSize.FRAME_INTERIOR);
        g.setPaint(getFrameInnerHighlightPaint(s));
        g.draw(s);
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Get the paint for the border.
     *
     * @param  s the border shape.
     *
     * @return the paint.
     */
    public Paint getFrameBorderPaint(Shape s) {
        switch (state) {

        case BACKGROUND_ENABLED:
            return frameBorderInactive;

        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return frameBorderActive;
        }

        return null;
    }

    /**
     * Get the color set to paint the interior.
     *
     * @return the color set.
     */
    private FourColors getFrameInteriorColors() {
        switch (state) {

        case BACKGROUND_ENABLED:
            return frameInactive;

        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return frameActive;
        }

        return null;
    }

    /**
     * Get the paint for the frame interior.
     *
     * @param  s                   the frame interior shape.
     * @param  titleHeight         the height of the title portion.
     * @param  topToolBarHeight    the height of the top toolbar, or 0 if none.
     * @param  bottomToolBarHeight the height of the bottom toolbar, or 0 if
     *                             none.
     *
     * @return the paint.
     */
    public Paint getFrameInteriorPaint(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight) {
        return createFrameGradient(s, titleHeight, topToolBarHeight, bottomToolBarHeight, getFrameInteriorColors());
    }

    /**
     * Get the paint for the title bar.
     *
     * @param  s           the frame interior shape.
     * @param  titleHeight the height of the title portion.
     *
     * @return the paint.
     */
    public Paint getTitleBarInteriorPaint(Shape s, int titleHeight) {
        return createTitleBarGradient(s, titleHeight, getFrameInteriorColors());
    }

    /**
     * Get the paint to paint the inner highlight with.
     *
     * @param  s the highlight shape.
     *
     * @return the paint.
     */
    public Paint getFrameInnerHighlightPaint(Shape s) {
        switch (state) {

        case BACKGROUND_ENABLED:
            return frameInnerHighlightInactive;

        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return frameInnerHighlightActive;
        }

        return null;
    }

    /**
     * Create the gradient to paint the frame interior.
     *
     * @param  s                   the interior shape.
     * @param  titleHeight         the height of the title bar, or 0 if none.
     * @param  topToolBarHeight    the height of the top toolbar, or 0 if none.
     * @param  bottomToolBarHeight the height of the bottom toolbar, or 0 if
     *                             none.
     * @param  defColors           the color set to construct the gradient from.
     *
     * @return the gradient.
     */
    private Paint createFrameGradient(Shape s, int titleHeight, int topToolBarHeight, int bottomToolBarHeight, FourColors defColors) {
        Rectangle2D bounds = s.getBounds2D();
        float       x      = (float) bounds.getX();
        float       y      = (float) bounds.getY();
        float       w      = (float) bounds.getWidth();
        float       h      = (float) bounds.getHeight();

        float midX        = x + w / 2.0f;
        float titleBottom = titleHeight / h;

        if (titleBottom >= 1.0f) {
            titleBottom = 1.0f - 0.00004f;
        }

        float[] midPoints = null;
        Color[] colors    = null;

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
            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
        } else if (topToolBarHeight > 0) {
            float toolBarBottom = (titleHeight + topToolBarHeight) / h;

            if (toolBarBottom >= 1.0f) {
                toolBarBottom = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, toolBarBottom, 1.0f };
            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid };
        } else if (bottomToolBarHeight > 0) {
            float bottomToolBarTop = (h - 2 - bottomToolBarHeight) / h;

            if (bottomToolBarTop >= 1.0f) {
                bottomToolBarTop = 1.0f - 0.00002f;
            }

            midPoints = new float[] { 0.0f, titleBottom, bottomToolBarTop, 1.0f };
            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.lowerMid, defColors.bottom };
        } else {
            midPoints = new float[] { 0.0f, titleBottom, 1.0f };
            colors    = new Color[] { defColors.top, defColors.upperMid, defColors.bottom };
        }

        return createGradient(midX, y, midX, y + h, midPoints, colors);
    }

    /**
     * Create the gradient to paint the frame interior.
     *
     * @param  s           the interior shape.
     * @param  titleHeight the height of the title bar, or 0 if none.
     * @param  defColors   the color set to construct the gradient from.
     *
     * @return the gradient.
     */
    private Paint createTitleBarGradient(Shape s, int titleHeight, FourColors defColors) {
        Rectangle2D bounds = s.getBounds2D();
        float       midX   = (float) bounds.getCenterX();
        float       y      = (float) bounds.getY();
        float       h      = (float) bounds.getHeight();

        return createGradient(midX, y, midX, y + h, new float[] { 0.0f, 1.0f },
                              new Color[] { defColors.top, defColors.upperMid });
    }
}
