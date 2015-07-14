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
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Title pane close button implementation.
 *
 * @author Kathryn Huxtable
 */
public final class TitlePaneCloseButtonPainter extends TitlePaneButtonPainter {

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_MOUSEOVER, BACKGROUND_PRESSED, BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_PRESSED_WINDOWNOTFOCUSED,
    }

    private ButtonColors enabled = new ButtonColors(white16, white4c, black66, white33, transparentColor, transparentColor, black99,
                                                    white99);
    private ButtonColors hover   = new ButtonColors(closeButtonTopHover, closeButtonLeftHover, closeButtonEdgeHover, white33,
                                                    closeButtonInteriorTopHover, closeButtonInteriorBottomHover, closeButtonMarkBorderHover,
                                                    white);
    private ButtonColors pressed = new ButtonColors(closeButtonTopPressed, closeButtonLeftPressed, black66, white33,
                                                    closeButtonInteriorTopPressed, closeButtonInteriorTopPressed,
                                                    closeButtonMarkBorderPressed,
                                                    closeButtonMarkInteriorPressed);

    private MyPath2D path = new MyPath2D();

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new TitlePaneCloseButtonPainter object.
     *
     * @param state the button state.
     */
    public TitlePaneCloseButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {

        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_ENABLED_WINDOWNOTFOCUSED:
            paintCloseEnabled(g, c, width, height);
            break;

        case BACKGROUND_MOUSEOVER:
            paintCloseHover(g, c, width, height);
            break;

        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_WINDOWNOTFOCUSED:
            paintClosePressed(g, c, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Paint the background enabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintCloseEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintClose(g, c, width, height, enabled);
    }

    /**
     * Paint the background mouse-over state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintCloseHover(Graphics2D g, JComponent c, int width, int height) {
        paintClose(g, c, width, height, hover);
    }

    /**
     * Paint the background pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintClosePressed(Graphics2D g, JComponent c, int width, int height) {
        paintClose(g, c, width, height, pressed);
    }

    /**
     * Paint the background of the button using the specified colors.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     * @param colors the color set to use to paint the button.
     */
    private void paintClose(Graphics2D g, JComponent c, int width, int height, ButtonColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = decodeInterior(width, height);

        g.setPaint(decodeCloseGradient(s, colors.interiorTop, colors.interiorBottom));
        g.fill(s);
        s = decodeEdge(width, height);
        g.setColor(colors.edge);
        g.draw(s);
        s = decodeShadow(width, height);
        g.setColor(colors.shadow);
        g.draw(s);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(colors.top);
        g.drawLine(0, 0, width - 2, 0);
        g.setColor(colors.left);
        g.drawLine(0, 1, 0, height - 3);
        s = decodeMarkInterior(width, height);
        g.setColor(colors.markInterior);
        g.fill(s);
        s = decodeMarkBorder(width, height);
        g.setColor(colors.markBorder);
        g.draw(s);
    }

    /**
     * Create the gradient for the close button.
     *
     * @param  s      the shape to fill.
     * @param  top    the top color.
     * @param  bottom the bottom color.
     *
     * @return the gradient.
     */
    private Paint decodeCloseGradient(Shape s, Color top, Color bottom) {
        Rectangle r      = s.getBounds();
        int       width  = r.width;
        int       height = r.height;

        return createGradient(r.x + width / 2, r.y, r.x + width / 2, r.y + height - 1, new float[] { 0f, 1f }, new Color[] { top, bottom });
    }

    /**
     * Create the interior of the button.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape.
     */
    private Shape decodeInterior(int width, int height) {
        path.reset();
        path.moveTo(1, 1);
        path.lineTo(width - 2, 1);
        path.lineTo(width - 2, height - 3);
        path.lineTo(width - 3, height - 2);
        path.lineTo(1, height - 2);
        path.closePath();

        return path;
    }

    /**
     * Create the edge of the button.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the edge.
     */
    private Shape decodeEdge(int width, int height) {
        path.reset();
        path.moveTo(width - 2, 0);
        path.lineTo(width - 2, height - 4);
        path.lineTo(width - 4, height - 2);
        path.lineTo(0, height - 2);

        return path;
    }

    /**
     * Create the shadow for the button.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the shadow.
     */
    private Shape decodeShadow(int width, int height) {
        path.reset();
        path.moveTo(width - 1, 0);
        path.lineTo(width - 1, height - 4);
        path.lineTo(width - 4, height - 1);
        path.lineTo(0, height - 1);

        return path;
    }

    /**
     * Create the shape for the mark border.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the mark border.
     */
    private Shape decodeMarkBorder(int width, int height) {
        int left = (width - 3) / 2 - 5;
        int top  = (height - 2) / 2 - 5;

        path.reset();
        path.moveTo(left + 1, top + 0);
        path.lineTo(left + 3, top + 0);
        path.pointAt(left + 4, top + 1);
        path.pointAt(left + 5, top + 2);
        path.pointAt(left + 6, top + 1);
        path.moveTo(left + 7, top + 0);
        path.lineTo(left + 9, top + 0);
        path.pointAt(left + 10, top + 1);
        path.pointAt(left + 9, top + 2);
        path.pointAt(left + 8, top + 3);
        path.moveTo(left + 7, top + 4);
        path.lineTo(left + 7, top + 5);
        path.pointAt(left + 8, top + 6);
        path.pointAt(left + 9, top + 7);
        path.pointAt(left + 10, top + 8);
        path.moveTo(left + 9, top + 9);
        path.lineTo(left + 7, top + 9);
        path.pointAt(left + 6, top + 8);
        path.pointAt(left + 5, top + 7);
        path.pointAt(left + 4, top + 8);
        path.moveTo(left + 3, top + 9);
        path.lineTo(left + 1, top + 9);
        path.pointAt(left + 0, top + 8);
        path.pointAt(left + 1, top + 7);
        path.pointAt(left + 2, top + 6);
        path.moveTo(left + 3, top + 5);
        path.lineTo(left + 3, top + 4);
        path.pointAt(left + 2, top + 3);
        path.pointAt(left + 1, top + 2);
        path.pointAt(left + 0, top + 1);

        return path;
    }

    /**
     * Create the shape for the mark interior.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the mark interior.
     */
    private Shape decodeMarkInterior(int width, int height) {
        int left = (width - 3) / 2 - 5;
        int top  = (height - 2) / 2 - 5;

        path.reset();
        path.moveTo(left + 1, top + 1);
        path.lineTo(left + 4, top + 1);
        path.lineTo(left + 5, top + 3);
        path.lineTo(left + 7, top + 1);
        path.lineTo(left + 10, top + 1);
        path.lineTo(left + 7, top + 4);
        path.lineTo(left + 7, top + 5);
        path.lineTo(left + 10, top + 9);
        path.lineTo(left + 6, top + 8);
        path.lineTo(left + 5, top + 6);
        path.lineTo(left + 4, top + 9);
        path.lineTo(left + 0, top + 9);
        path.lineTo(left + 4, top + 5);
        path.lineTo(left + 4, top + 4);
        path.closePath();

        return path;
    }

    /**
     * Extend Path2D to have a pointAt method to draw a single point.
     */
    private static class MyPath2D extends Path2D.Double {
        private static final long serialVersionUID = -3577694636085901945L;

        /**
         * Draw a single point at the (x,y) coordinate specified.
         *
         * @param x the x coordinate to draw the point at.
         * @param y the y coordinate to draw the point at.
         */
        public void pointAt(int x, int y) {
            moveTo(x, y);
            lineTo(x, y);
        }
    }

    /**
     * A class encapsulating the colors used for a button.
     */
    private static class ButtonColors {

        /** Top line color. */
        public Color top;

        /** Left edge color. */
        public Color left;

        /** Main edge color. */
        public Color edge;

        /** Main shadow color. */
        public Color shadow;

        /** Interior top gradient color. */
        public Color interiorTop;

        /** Interior bottom gradient color. */
        public Color interiorBottom;

        /** Mark border color. */
        public Color markBorder;

        /** Mark interior color. */
        public Color markInterior;

        /**
         * Creates a new ButtonColors object.
         *
         * @param top            the top line color.
         * @param left           the left edge color.
         * @param edge           the main edge color.
         * @param shadow         the Main shadow color.
         * @param interiorTop    the interior top gradient color.
         * @param interiorBottom the interior bottom gradient color.
         * @param markBorder     the mark border color.
         * @param markInterior   the mark interior color.
         */
        public ButtonColors(Color top, Color left, Color edge, Color shadow, Color interiorTop, Color interiorBottom, Color markBorder,
                Color markInterior) {
            this.top            = top;
            this.left           = left;
            this.edge           = edge;
            this.shadow         = shadow;
            this.interiorTop    = interiorTop;
            this.interiorBottom = interiorBottom;
            this.markBorder     = markBorder;
            this.markInterior   = markInterior;
        }

    }
}
