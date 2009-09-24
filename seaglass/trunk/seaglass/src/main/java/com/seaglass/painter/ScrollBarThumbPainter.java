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


/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ScrollBarThumbPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_FOCUSED = 3;
    public static final int BACKGROUND_MOUSEOVER = 4;
    public static final int BACKGROUND_PRESSED = 5;

    public ScrollBarThumbPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "vertical_scrollbar/vertical_scrollbar_thumb_disabled";
            case BACKGROUND_ENABLED: return "vertical_scrollbar/vertical_scrollbar_thumb";
            case BACKGROUND_MOUSEOVER: return "vertical_scrollbar/vertical_scrollbar_thumb";
            case BACKGROUND_PRESSED: return "vertical_scrollbar/vertical_scrollbar_thumb_pressed";
        }
        return null;
    }
}
