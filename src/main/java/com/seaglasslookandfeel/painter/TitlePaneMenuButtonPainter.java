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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Title pane menu button painter implementation.
 *
 * @author Kathryn Huxtable
 */
public final class TitlePaneMenuButtonPainter extends TitlePaneButtonPainter {

    /**
     * Control states.
     */
    public static enum Which {
        ICON_ENABLED, ICON_DISABLED, ICON_MOUSEOVER, ICON_PRESSED, ICON_ENABLED_WINDOWNOTFOCUSED, ICON_MOUSEOVER_WINDOWNOTFOCUSED,
        ICON_PRESSED_WINDOWNOTFOCUSED,
    }

    private ButtonColors enabled = new ButtonColors(white16, black66, white4c, black66, black33, white33, transparentColor, black99,
                                                    white99);
    private ButtonColors hover   = new ButtonColors(white68, black66, white8c, black66, black33, white46, white59, gray_10_e5, white);
    private ButtonColors pressed = new ButtonColors(gray_9b_82, black66, gray_a9_9e, black66, black33, white33, gray_e6_59, gray_0e_e5,
                                                    gray_e6);

    private Path2D path = new Path2D.Double();

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new TitlePaneMenuButtonPainter object.
     *
     * @param state button state.
     */
    public TitlePaneMenuButtonPainter(Which state) {
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

        case ICON_ENABLED:
        case ICON_DISABLED:
        case ICON_ENABLED_WINDOWNOTFOCUSED:
            paintEnabled(g, c, width, height);
            break;

        case ICON_MOUSEOVER:
        case ICON_MOUSEOVER_WINDOWNOTFOCUSED:
            paintHover(g, c, width, height);
            break;

        case ICON_PRESSED:
        case ICON_PRESSED_WINDOWNOTFOCUSED:
            paintPressed(g, c, width, height);
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
    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, enabled);
    }

    /**
     * Paint the background mouse-over state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintHover(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, hover);
    }

    /**
     * Paint the background pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, pressed);
    }

    /**
     * Paint the button using the specified colors.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     * @param colors the color set to use to paint the button.
     */
    private void paintMenu(Graphics2D g, JComponent c, int width, int height, ButtonColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(colors.top);
        g.drawLine(0, 0, width - 2, 0);

        g.setColor(colors.leftOuter);
        g.drawLine(0, 0, 0, height - 4);

        g.setColor(colors.leftInner);
        g.drawLine(1, 1, 1, height - 4);
        g.drawLine(2, height - 3, 2, height - 3);

        Shape s = decodeInterior(width, height);

        g.setColor(colors.interior);
        g.fill(s);

        s = decodeEdge(width, height);
        g.setColor(colors.edge);
        g.draw(s);

        g.setColor(colors.edgeShade);
        g.drawLine(2, height - 2, 2, height - 2);
        g.drawLine(1, height - 3, 1, height - 3);
        g.drawLine(0, height - 4, 0, height - 4);

        s = decodeShadow(width, height);
        g.setColor(colors.shadow);
        g.draw(s);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        s = decodeMarkInterior(width, height);
        g.setColor(colors.markInterior);
        g.fill(s);

        s = decodeMarkBorder(width, height);
        g.setColor(colors.markBorder);
        g.draw(s);
    }

    /**
     * Create the button interior shape
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the button interior.
     */
    private Shape decodeInterior(int width, int height) {
        path.reset();
        path.moveTo(1, 1);
        path.lineTo(width - 2, 1);
        path.lineTo(width - 2, height - 3);
        path.lineTo(width - 3, height - 2);
        path.lineTo(3, height - 2);
        path.lineTo(2, height - 3);
        path.closePath();

        return path;
    }

    /**
     * Create the button edge shape.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the button edge.
     */
    private Shape decodeEdge(int width, int height) {
        path.reset();
        path.moveTo(width - 2, 0);
        path.lineTo(width - 2, height - 4);
        path.lineTo(width - 4, height - 2);
        path.lineTo(3, height - 2);

        return path;
    }

    /**
     * Create the button shadow shape.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the button shadow.
     */
    private Shape decodeShadow(int width, int height) {
        path.reset();
        path.moveTo(width - 1, 0);
        path.lineTo(width - 1, height - 4);
        path.lineTo(width - 4, height - 1);
        path.lineTo(2, height - 1);
        path.lineTo(1, height - 2);

        return path;
    }

    /**
     * Create the mark border shape.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the mark border.
     */
    private Shape decodeMarkBorder(int width, int height) {
        double left = width / 2.0 - 4;
        double top  = height / 2.0 - 4;

        path.reset();
        path.moveTo(left + 0, top + 0);
        path.lineTo(left + 8, top);
        path.lineTo(left + 4, top + 6);
        path.closePath();

        return path;
    }

    /**
     * Create the mark interior shape.
     *
     * @param  width  the width.
     * @param  height the height.
     *
     * @return the shape of the mark interior.
     */
    private Shape decodeMarkInterior(int width, int height) {
        double left = width / 2.0 - 4;
        double top  = height / 2.0 - 4;

        path.reset();
        path.moveTo(left + 1, top + 1);
        path.lineTo(left + 8, top + 1);
        path.lineTo(left + 4, top + 6);
        path.closePath();

        return path;
    }

    /**
     * A class encapsulating the colors used for a button.
     */
    private static class ButtonColors {

        /** Top line color. */
        public Color top;

        /** Left outer edge color. */
        public Color leftOuter;

        /** Left inner edge color. */
        public Color leftInner;

        /** Main edge color. */
        public Color edge;

        /** Main edge shade color. */
        public Color edgeShade;

        /** Main shadow color. */
        public Color shadow;

        /** Interior color. */
        public Color interior;

        /** Mark border color. */
        public Color markBorder;

        /** Mark interior color. */
        public Color markInterior;

        /**
         * Creates a new ButtonColors object.
         *
         * @param top          the top line color.
         * @param leftOuter    the left outer edge color.
         * @param leftInner    the left inner edge color.
         * @param edge         the main edge color.
         * @param edgeShade    the main edge shade color.
         * @param shadow       the main shadow color.
         * @param interior     the interior color.
         * @param markBorder   the mark border color.
         * @param markInterior the mark interior color.
         */
        public ButtonColors(Color top, Color leftOuter, Color leftInner, Color edge, Color edgeShade, Color shadow, Color interior,
                Color markBorder, Color markInterior) {
            this.top          = top;
            this.leftOuter    = leftOuter;
            this.leftInner    = leftInner;
            this.edge         = edge;
            this.edgeShade    = edgeShade;
            this.shadow       = shadow;
            this.interior     = interior;
            this.markBorder   = markBorder;
            this.markInterior = markInterior;
        }
    }
}
