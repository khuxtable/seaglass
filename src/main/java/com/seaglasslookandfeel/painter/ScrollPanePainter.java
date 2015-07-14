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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Nimbus's ScrollPanePainter.
 */
public final class ScrollPanePainter extends AbstractRegionPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_ENABLED, BORDER_ENABLED, BORDER_ENABLED_FOCUSED, CORNER_ENABLED,
    }

    private Which        state;
    private PaintContext ctx;

    private Color borderColor = decodeColor("seaGlassTextEnabledBorder");
    private Color cornerColor1 = new Color(250, 250, 250);
    private Color cornerColor2 = new Color(190, 190, 190);

    /**
     * Creates a new ScrollPanePainter object.
     *
     * @param state DOCUMENT ME!
     */
    public ScrollPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.FIXED_SIZES);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackground(g, c, width, height);
            break;

        case BORDER_ENABLED:
            paintBorderEnabled(g, width, height);
            break;

        case BORDER_ENABLED_FOCUSED:
            paintBorderFocused(g, width, height);
            break;

        case CORNER_ENABLED:
            paintCornerEnabled(g, width, height);
            break;
        }
    }

    private void paintBackground(Graphics2D g, JComponent c, int width, int height) {
        JViewport viewport = ((JScrollPane)c).getViewport();
        if (viewport.isOpaque()) {
            Shape s = shapeGenerator.createRoundRectangle(0, 0, width - 1, height - 1, CornerSize.BORDER);
            g.setPaint(viewport.getBackground());
            g.fill(s);
        }
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#getPaintContext()
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBorderEnabled(Graphics2D g, int width, int height) {
        Shape   s;
        s = shapeGenerator.createRoundRectangle(0, 0, width - 1, height - 1, CornerSize.BORDER);
        g.setPaint(borderColor);
        g.draw(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBorderFocused(Graphics2D g, int width, int height) {
        Shape   s;

        s = shapeGenerator.createRoundRectangle(0, 0, width - 1, height - 1, CornerSize.OUTER_FOCUS);
        g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, false));
        g.draw(s);

        s = shapeGenerator.createRoundRectangle(1, 1, width - 3, height - 3, CornerSize.INNER_FOCUS);
        g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, false));
        g.draw(s);
        
        s = shapeGenerator.createRoundRectangle(2, 2, width - 5, height - 5, CornerSize.BORDER);
        g.setPaint(borderColor);
        g.draw(s);

    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintCornerEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeCornerBorder(width, height);

//        g.setPaint(cornerBorder);
//        g.fill(s);
        s = decodeCornerInside(width, height);
        g.setPaint(decodeCornerGradient(s));
        g.fill(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     * @param  height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Shape decodeCornerBorder(int width, int height) {
        return shapeGenerator.createRectangle(0, 0, width, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     * @param  height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Shape decodeCornerInside(int width, int height) {
        return shapeGenerator.createRectangle(0, 0, width, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Paint decodeCornerGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float       w      = (float) bounds.getWidth();
        float       h      = (float) bounds.getHeight();

        return createGradient(0, 0, w - 1, h - 1, new float[] { 0f, 1f }, new Color[] { cornerColor2, cornerColor1 });
    }
}
