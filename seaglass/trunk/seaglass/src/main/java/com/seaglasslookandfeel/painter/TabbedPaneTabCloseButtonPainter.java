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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Search field icon implementation.
 */
public final class TabbedPaneTabCloseButtonPainter extends AbstractRegionPainter {

    /**
     * Control state.
     */
    public static enum Which {
        DISABLED, ENABLED, MOUSEOVER, FOCUSED, PRESSED,
    }

    private Color borderMouseOver = decodeColor("seaGlassTabbedPaneTabCloseBorderBase", 0f, 0f, 0f, -170);
    private Color borderFocused   = decodeColor("seaGlassTabbedPaneTabCloseBorderBase", 0f, 0f, 0f, -100);
    private Color borderPressed   = decodeColor("seaGlassTabbedPaneTabCloseBorderBase", 0f, 0f, 0f, -40);

    private Color graphicInnerShadow1 = decodeColor("seaGlassTabbedPaneTabCloseGraphicInnerShadowBase", 0f, 0f, 0f, -130);
    private Color graphicInnerShadow2 = decodeColor("seaGlassTabbedPaneTabCloseGraphicInnerShadowBase", 0f, 0f, 0f, -170);
    private Color graphicInnerShadow3 = decodeColor("seaGlassTabbedPaneTabCloseGraphicInnerShadowBase", 0f, 0f, 0f, -190);
    private Color graphicInnerShadow4 = decodeColor("seaGlassTabbedPaneTabCloseGraphicInnerShadowBase", 0f, 0f, 0f, -150);

    private Color graphicBase        = decodeColor("seaGlassTabbedPaneTabCloseGraphicBase");
    private Color graphicDropShadow1 = decodeColor("seaGlassTabbedPaneTabCloseGraphicDropShadowBase", 0f, 0f, 0f, -230);
    private Color graphicDropShadow2 = decodeColor("seaGlassTabbedPaneTabCloseGraphicDropShadowBase", 0f, 0f, 0f, -240);
    private Color graphicDropShadow3 = decodeColor("seaGlassTabbedPaneTabCloseGraphicDropShadowBase", 0f, 0f, 0f, -240);
    private Color graphicDropShadow4 = decodeColor("seaGlassTabbedPaneTabCloseGraphicDropShadowBase", 0f, 0f, 0f, -230);

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new TabbedPaneTabCloseButtonPainter object.
     *
     * @param state the control state to paint.
     */
    public TabbedPaneTabCloseButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (state) {

        case ENABLED:
            drawEnabledGraphic(g, width, height);
            break;

        case MOUSEOVER:
            drawBorder(g, width, height, borderMouseOver, 0.4f);
            drawOverlayGraphic(g, width, height);
            break;

        case FOCUSED:
            drawBorder(g, width, height, borderFocused, 0.4f);
            drawOverlayGraphic(g, width, height);
            break;

        case PRESSED:
            drawBorder(g, width, height, borderPressed, 0.5f);
            drawOverlayGraphic(g, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Draw a border around the graphic.
     *
     * @param g      the Graphic context.
     * @param width  the width of the border.
     * @param height the height of the border.
     * @param color  the color of the border.
     * @param size   the spread of the border from outside in, expressed as a
     *               percentage of the height.
     */
    private void drawBorder(Graphics2D g, int width, int height, Color color, float size) {
        int max        = (int) (Math.min((height - 2) * size, height / 2.0f) + 0.5);
        int alphaDelta = color.getAlpha() / max;

        for (int i = 0; i < max; i++) {
            Shape s        = shapeGenerator.createRoundRectangle(i, i, width - 2 * i - 1, height - 2 * i - 1, CornerSize.CHECKBOX_INTERIOR);
            Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() - i * alphaDelta);

            g.setPaint(newColor);
            g.draw(s);
        }
    }

    /**
     * Draw the "close" graphic for the simple enabled state.
     *
     * @param g      the Graphic context.
     * @param width  the width of the graphic.
     * @param height the height of the graphic.
     */
    private void drawEnabledGraphic(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createTabCloseIcon(2, 2, width - 4, height - 4);

        g.setPaint(createGraphicInnerShadowGradient(s));
        g.fill(s);
    }

    /**
     * Draw the "close" graphic for a state where it overlays a border.
     *
     * @param g      the Graphic context.
     * @param width  the width of the graphic.
     * @param height the height of the graphic.
     */
    private void drawOverlayGraphic(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createTabCloseIcon(2, 2, width - 4, height - 4);

        g.setPaint(graphicBase);
        g.fill(s);

        s = shapeGenerator.createTabCloseIcon(2, 3, width - 4, height - 4);
        g.setPaint(createGraphicDropShadowGradient(s));
        Shape oldClip = g.getClip();

        g.setClip(2, 3, width - 4, height - 4);
        g.fill(s);
        g.setClip(oldClip);
    }

    /**
     * Create the gradient for the "x" inner shadow.
     *
     * @param  s the Shape for the gradient.
     *
     * @return the gradient paint.
     */
    private Paint createGraphicInnerShadowGradient(Shape s) {
        Rectangle2D b    = s.getBounds2D();
        float       midX = (float) b.getCenterX();
        float       y1   = (float) b.getMinY();
        float       y2   = (float) b.getMaxY();

        return createGradient(midX, y1, midX, y2, new float[] { 0f, 0.43f, 0.57f, 1f },
                              new Color[] { graphicInnerShadow1, graphicInnerShadow2, graphicInnerShadow3, graphicInnerShadow4 });
    }

    /**
     * Create the gradient for the "x" drop shadow.
     *
     * @param  s the Shape for the gradient.
     *
     * @return the gradient paint.
     */
    private Paint createGraphicDropShadowGradient(Shape s) {
        Rectangle2D b    = s.getBounds2D();
        float       midX = (float) b.getCenterX();
        float       y1   = (float) b.getMinY();
        float       y2   = (float) b.getMaxY();

        return createGradient(midX, y1, midX, y2, new float[] { 0f, 0.43f, 0.57f, 1f },
                              new Color[] { graphicDropShadow1, graphicDropShadow2, graphicDropShadow3, graphicDropShadow4 });
    }
}
