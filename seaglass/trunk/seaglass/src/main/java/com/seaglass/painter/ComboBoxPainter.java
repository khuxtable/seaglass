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
 * ComboBoxPainter implementation.
 */
public final class ComboBoxPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_DISABLED_PRESSED = 2;
    public static final int BACKGROUND_ENABLED = 3;
    public static final int BACKGROUND_FOCUSED = 4;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED = 5;
    public static final int BACKGROUND_MOUSEOVER = 6;
    public static final int BACKGROUND_PRESSED_FOCUSED = 7;
    public static final int BACKGROUND_PRESSED = 8;
    public static final int BACKGROUND_ENABLED_SELECTED = 9;
    public static final int BACKGROUND_DISABLED_EDITABLE = 10;
    public static final int BACKGROUND_ENABLED_EDITABLE = 11;
    public static final int BACKGROUND_FOCUSED_EDITABLE = 12;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 13;
    public static final int BACKGROUND_PRESSED_EDITABLE = 14;

    public ComboBoxPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "combo_box_disabled";
            case BACKGROUND_DISABLED_PRESSED: return "combo_box_disabled";
            case BACKGROUND_ENABLED: return "combo_box";
            case BACKGROUND_FOCUSED: return "combo_box";
            case BACKGROUND_MOUSEOVER_FOCUSED: return "combo_box";
            case BACKGROUND_MOUSEOVER: return "combo_box";
            case BACKGROUND_PRESSED_FOCUSED: return "combo_box_pressed";
            case BACKGROUND_PRESSED: return "combo_box_pressed";
            case BACKGROUND_ENABLED_SELECTED: return "combo_box_pressed";
            case BACKGROUND_DISABLED_EDITABLE: return "empty_image";
            case BACKGROUND_ENABLED_EDITABLE: return "empty_image";
            case BACKGROUND_FOCUSED_EDITABLE: return "combo_box_focused_editable";
            case BACKGROUND_MOUSEOVER_EDITABLE: return "empty_image";
            case BACKGROUND_PRESSED_EDITABLE: return "empty_image";
        }
        return null;
    }
}
