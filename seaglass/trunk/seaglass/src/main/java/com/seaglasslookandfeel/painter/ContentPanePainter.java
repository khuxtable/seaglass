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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ContentPanePainter implementation.
 */
public class ContentPanePainter extends AbstractRegionPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED,
    }

    private TwoColors rootPaneActive   = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"),
                                                       decodeColor("seaGlassToolBarActiveBottomB"));
    private TwoColors rootPaneInactive = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"),
                                                       decodeColor("seaGlassToolBarInactiveBottomB"));

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new ContentPanePainter object.
     *
     * @param state DOCUMENT ME!
     */
    public ContentPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.NO_CACHING);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRectangle(0, 0, width, height);

        g.setPaint(getRootPaneInteriorPaint(s, state));
        g.fill(s);
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
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private TwoColors getRootPaneInteriorColors(Which type) {
        switch (type) {

        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return rootPaneActive;

        case BACKGROUND_ENABLED:
            return rootPaneInactive;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s    DOCUMENT ME!
     * @param  type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getRootPaneInteriorPaint(Shape s, Which type) {
        return createVerticalGradient(s, getRootPaneInteriorColors(type));
    }
}
