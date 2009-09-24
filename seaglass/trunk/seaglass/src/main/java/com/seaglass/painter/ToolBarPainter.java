/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Aqvavit Pluggable Look and Feel.
 *
 * Aqvavit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * Aqvavit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Aqvavit.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglass.util.PlatformUtils;
import com.seaglass.util.WindowUtils;

/**
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarPainter extends AbstractRegionPainter {
    final boolean           IS_MAC_OSX                     = PlatformUtils.isMac();

    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ToolBarPainter to determine which region/state is being painted
    // by that instance.
    public static final int BORDER_NORTH                   = 1;
    public static final int BORDER_SOUTH                   = 2;
    public static final int BORDER_EAST                    = 3;
    public static final int BORDER_WEST                    = 4;
    public static final int HANDLEICON_ENABLED             = 5;

    // For non-Mac use Snow Leopard colors because it has the same Gamma
    // correction.
    private Color           ACTIVE_TOP_GRADIENT_COLOR      = IS_MAC_OSX ? new Color(0xbcbcbc) : new Color(0xc4c4c4);
    private Color           ACTIVE_BOTTOM_GRADIENT_COLOR   = IS_MAC_OSX ? new Color(0x9a9a9a) : new Color(0xb2b2b2);
    private Color           INACTIVE_TOP_GRADIENT_COLOR    = IS_MAC_OSX ? new Color(0xe4e4e4) : new Color(0xe7e7e7);
    private Color           INACTIVE_BOTTOM_GRADIENT_COLOR = IS_MAC_OSX ? new Color(0xd1d1d1) : new Color(0xdfdfdf);

    // Refers to one of the static final ints above
    private int             state;
    private PaintContext    ctx;

    public ToolBarPainter(PaintContext ctx, int state) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        boolean containedInActiveWindow = WindowUtils.isParentWindowFocused(c);

        Color topColor = containedInActiveWindow ? ACTIVE_TOP_GRADIENT_COLOR : INACTIVE_TOP_GRADIENT_COLOR;
        Color bottomColor = containedInActiveWindow ? ACTIVE_BOTTOM_GRADIENT_COLOR : INACTIVE_BOTTOM_GRADIENT_COLOR;

        GradientPaint paint;
        if (state == BORDER_EAST || state == BORDER_WEST) {
            paint = new GradientPaint(1, 0, topColor, width, 0, bottomColor);
        } else {
            paint = new GradientPaint(0, 1, topColor, 0, height, bottomColor);
        }
        g.setPaint(paint);
        g.fillRect(0, 0, width, height);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
