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
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of TableHeaderRendererPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_ENABLED_FOCUSED = 3;
    public static final int BACKGROUND_MOUSEOVER = 4;
    public static final int BACKGROUND_PRESSED = 5;
    public static final int BACKGROUND_ENABLED_SORTED = 6;
    public static final int BACKGROUND_ENABLED_FOCUSED_SORTED = 7;
    public static final int BACKGROUND_DISABLED_SORTED = 8;

    public TableHeaderRendererPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "table_header_disabled";
            case BACKGROUND_ENABLED: return "table_header";
            case BACKGROUND_ENABLED_FOCUSED: return "table_header";
            case BACKGROUND_MOUSEOVER: return "table_header";
            case BACKGROUND_PRESSED: return "table_header_pressed";
            case BACKGROUND_ENABLED_SORTED: return "table_header_sorted";
            case BACKGROUND_ENABLED_FOCUSED_SORTED: return "table_header_sorted";
            case BACKGROUND_DISABLED_SORTED: return "table_header_disabled_sorted";
        }
        return null;
    }
}
