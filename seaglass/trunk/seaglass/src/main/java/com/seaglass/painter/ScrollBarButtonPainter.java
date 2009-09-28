/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.painter;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

import com.seaglass.SeaGlassScrollBarUI;

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractImagePainter {
    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ScrollBarButtonPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int FOREGROUND_ENABLED   = 1;
    public static final int FOREGROUND_DISABLED  = 2;
    public static final int FOREGROUND_MOUSEOVER = 3;
    public static final int FOREGROUND_PRESSED   = 4;
    
    ImageIcon rightImage;

    public ScrollBarButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        String leftName = null;
        String rightName = null;
        switch (state) {
        case FOREGROUND_ENABLED:
            leftName = "h_scroll_left_apart";
            rightName = "h_scroll_right_apart";
            break;
        case FOREGROUND_DISABLED:
            leftName = "h_scroll_left_apart";
            rightName = "h_scroll_right_apart";
            break;
        case FOREGROUND_MOUSEOVER:
            leftName = "h_scroll_left_apart";
            rightName = "h_scroll_right_apart";
            break;
        case FOREGROUND_PRESSED:
            leftName = "h_scroll_left_apart_pressed";
            rightName = "h_scroll_right_apart_pressed";
        }
        rightImage = new ImageIcon(ScrollBarButtonPainter.class.getResource("/com/seaglass/resources/images/" + rightName + ".png"));
        return leftName;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (c != null && c.getParent() != null) {
            SeaGlassScrollBarUI ui = (SeaGlassScrollBarUI) ((JScrollBar) c.getParent()).getUI();
            if (ui.getIncreaseButton() == c) {
                rightImage.paintIcon(c, g, 0, 0);
                return;
            }
        }
        image.paintIcon(c, g, 0, 0);
    }
}
