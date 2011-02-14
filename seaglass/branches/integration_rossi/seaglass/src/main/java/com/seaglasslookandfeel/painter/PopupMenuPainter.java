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

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * PopupMenuPainter implementation.
 */
public final class PopupMenuPainter extends AbstractRegionPainter {

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Color popupMenuInteriorEnabled  = decodeColor("popupMenuInteriorEnabled");
    private Color popupMenuBorderEnabled    = decodeColor("popupMenuBorderEnabled");
    private Color popupMenuBorderDisabled   = disable(popupMenuBorderEnabled);
    private Color popupMenuInteriorDisabled = disable(popupMenuBorderEnabled);

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new PopupMenuPainter object.
     *
     * @param state DOCUMENT ME!
     */
    public PopupMenuPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    /**
     * @see com.seaglasslookandfeel.painter.AbstractRegionPainter#doPaint(java.awt.Graphics2D,
     *      javax.swing.JComponent, int, int, java.lang.Object[])
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
    	// Rossi: Top-Left corner is round for popups but square for sub menus and real menus from menubar
        CornerStyle topLeftCornerStyle = getTopLeftCornerStyle(c);

        Shape s = shapeGenerator.createRoundRectangle(0, 0, width, height, 
            CornerSize.POPUP_BORDER,  
            topLeftCornerStyle, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED,CornerStyle.ROUNDED);
        
        g.setPaint(getPopupMenuBorderPaint(s));
        g.fill(s);
        
        s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, 
            CornerSize.POPUP_INTERIOR, 
            topLeftCornerStyle, CornerStyle.ROUNDED,
            CornerStyle.ROUNDED,CornerStyle.ROUNDED);
        
        g.setPaint(getPopupMenuInteriorPaint(s));
        g.fill(s);
    }

    /**
     * @param c
     * @return
     */
    private CornerStyle getTopLeftCornerStyle(JComponent c) {
        Component invoker = getInvoker(c);
        CornerStyle topLeftCornerStyle = CornerStyle.ROUNDED;
        if (invoker instanceof JMenu) {
            topLeftCornerStyle = CornerStyle.SQUARE;
        }
        return topLeftCornerStyle;
    }

    /**
     * @param c
     * @return
     */
    private Component getInvoker(JComponent c) {
        JPopupMenu popup = (JPopupMenu) c;
        return popup.getInvoker();
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
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getPopupMenuBorderPaint(Shape s) {
        return getPopupMenuBorderColors();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getPopupMenuInteriorPaint(Shape s) {
        return getPopupMenuInteriorColors();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Color getPopupMenuBorderColors() {
        switch (state) {

        case BACKGROUND_ENABLED:
            return popupMenuBorderEnabled;

        case BACKGROUND_DISABLED:
            return popupMenuBorderDisabled;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Color getPopupMenuInteriorColors() {
        switch (state) {

        case BACKGROUND_ENABLED:
            return popupMenuInteriorEnabled;

        case BACKGROUND_DISABLED:
            return popupMenuInteriorDisabled;
        }

        return null;
    }
}
