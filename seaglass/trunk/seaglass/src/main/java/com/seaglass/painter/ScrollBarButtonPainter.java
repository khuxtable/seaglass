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

    public ScrollBarButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch (state) {
        case FOREGROUND_ENABLED:
            return "scrollbar/scroll_button_apart";
        case FOREGROUND_DISABLED:
            return "scrollbar/scroll_button_apart";
        case FOREGROUND_MOUSEOVER:
            return "scrollbar/scroll_button_apart";
        case FOREGROUND_PRESSED:
            return "scrollbar/scroll_button_apart_pressed";
        }
        return null;
    }
}
