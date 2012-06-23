/*
 * Copyright (c) 2010 Kathryn Huxtable and Kenneth Orr.
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

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * DOCUMENT ME!
 */
public final class SeparatorPainter extends AbstractCommonColorsPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_ENABLED,
    }

    private Color        baseColor      = decodeColor("toolbarToggleButtonBase");
    private TwoColors    seperatorInner     = new TwoColors(baseColor, deriveColor(baseColor, 0f, 0f, 0f, 0x28));
    private TwoColors    seperatorInnerEdge = new TwoColors(baseColor, deriveColor(baseColor, 0f, 0f, 0f, 0xf0));

    private PaintContext ctx;
    private Which state;

    /**
     * Creates a new SeparatorPainter object.
     *
     * @param state DOCUMENT ME!
     */
    public SeparatorPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, width, height);
            break;
        }
    }

    /**
     * @param g
     * @param width
     * @param height
     */
    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createRectangle(0, height/2, width, 1);
        g.setPaint(getSeperatorPaint(s, seperatorInnerEdge));
        g.fill(s);

        s = shapeGenerator.createRectangle(0, height/2-1, width, 1);
        g.setPaint(getSeperatorPaint(s, seperatorInner));
        g.fill(s);
        
        s = shapeGenerator.createRectangle(0, height/2+1, width, 1);
        g.setPaint(getSeperatorPaint(s, seperatorInner));
        g.fill(s);
    }
    
    /**
     * @param s
     * @param colors
     * @return
     */
    public Paint getSeperatorPaint(Shape s, TwoColors colors) {
        return createSeperatorGradient(s, colors);
    }

    /**
     * @param s
     * @param colors
     * @return
     */
    private Paint createSeperatorGradient(Shape s, TwoColors colors) {
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

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#getPaintContext()
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
