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
import javax.swing.JTabbedPane;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Sea Glass TabbedPaneTabAreaPainter. Does nothing.
 */
public final class TabbedPaneTabAreaPainter extends AbstractRegionPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_ENABLED_TOP, BACKGROUND_ENABLED_LEFT, BACKGROUND_ENABLED_BOTTOM, BACKGROUND_ENABLED_RIGHT,
        BACKGROUND_DISABLED_TOP, BACKGROUND_DISABLED_LEFT, BACKGROUND_DISABLED_BOTTOM, BACKGROUND_DISABLED_RIGHT,
    }

    private Color        baseColor      = decodeColor("toolbarToggleButtonBase");
    private TwoColors    tabLineInner     = new TwoColors(baseColor, deriveColor(baseColor, 0f, 0f, 0f, 0x28));
    private TwoColors    tabLineInnerEdge = new TwoColors(baseColor, deriveColor(baseColor, 0f, 0f, 0f, 0xf0));

    /** DOCUMENT ME! */
    public Which         state;
    private PaintContext ctx;
    private boolean      isDisabled;

    /**
     * Creates a new TabbedPaneTabAreaPainter object.
     *
     * @param state DOCUMENT ME!
     */
    public TabbedPaneTabAreaPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.NO_CACHING);

        switch (state) {

        case BACKGROUND_DISABLED_TOP:
        case BACKGROUND_DISABLED_LEFT:
        case BACKGROUND_DISABLED_BOTTOM:
        case BACKGROUND_DISABLED_RIGHT:
            isDisabled = true;
            break;

        case BACKGROUND_ENABLED_TOP:
        case BACKGROUND_ENABLED_LEFT:
        case BACKGROUND_ENABLED_BOTTOM:
        case BACKGROUND_ENABLED_RIGHT:
            isDisabled = false;
            break;
        }
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        JTabbedPane tabPane     = (JTabbedPane) c;
        int         orientation = tabPane.getTabPlacement();

        if (orientation == JTabbedPane.LEFT || orientation == JTabbedPane.RIGHT) {
            paintVerticalLine(g, c, 0, height / 2, width, height);
        } else {
            paintHorizontalLine(g, c, 0, height / 2, width, height);
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
     * @param c      DOCUMENT ME!
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintHorizontalLine(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        paintLine(g, width, height);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param c      DOCUMENT ME!
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintVerticalLine(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        paintLine(g, width, height);
    }

    /**
     * @param g
     * @param width
     * @param height
     */
    private void paintLine(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createRectangle(0, height/2, width, 1);
        g.setPaint(getLinePaint(s, tabLineInnerEdge));
        g.fill(s);

        s = shapeGenerator.createRectangle(0, height/2-1, width, 1);
        g.setPaint(getLinePaint(s, tabLineInner));
        g.fill(s);
        
        s = shapeGenerator.createRectangle(0, height/2+1, width, 1);
        g.setPaint(getLinePaint(s, tabLineInner));
        g.fill(s);
    }
    
    /**
     * @param s
     * @param colors
     * @return
     */
    public Paint getLinePaint(Shape s, TwoColors colors) {
        return createLineGradient(s, colors);
    }

    /**
     * @param s
     * @param colors
     * @return
     */
    private Paint createLineGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        return createGradient(x, y, w + x, y, 
            new float[] { 0f, 0.02f, 0.98f, 1f }, 
            new Color[] {
                colors.top,
                colors.bottom,
                colors.bottom,
                colors.top 
           });
    }
   
}
