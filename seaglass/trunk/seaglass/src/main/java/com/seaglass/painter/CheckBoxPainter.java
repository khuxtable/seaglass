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
package com.seaglass.painter;


/**
 * CheckBoxPainter implementation.
 */
public final class CheckBoxPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of CheckBoxPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int ICON_DISABLED = 3;
    public static final int ICON_ENABLED = 4;
    public static final int ICON_FOCUSED = 5;
    public static final int ICON_MOUSEOVER = 6;
    public static final int ICON_MOUSEOVER_FOCUSED = 7;
    public static final int ICON_PRESSED = 8;
    public static final int ICON_PRESSED_FOCUSED = 9;
    public static final int ICON_SELECTED = 10;
    public static final int ICON_SELECTED_FOCUSED = 11;
    public static final int ICON_PRESSED_SELECTED = 12;
    public static final int ICON_PRESSED_SELECTED_FOCUSED = 13;
    public static final int ICON_MOUSEOVER_SELECTED = 14;
    public static final int ICON_MOUSEOVER_SELECTED_FOCUSED = 15;
    public static final int ICON_DISABLED_SELECTED = 16;

    public CheckBoxPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case ICON_DISABLED: return "check_box_disabled";
            case ICON_ENABLED: return "check_box";
            case ICON_FOCUSED: return "check_box";
            case ICON_MOUSEOVER: return "check_box";
            case ICON_MOUSEOVER_FOCUSED: return "check_box";
            case ICON_PRESSED: return "check_box_pressed";
            case ICON_PRESSED_FOCUSED: return "check_box_pressed";
            case ICON_SELECTED: return "check_box_selected";
            case ICON_SELECTED_FOCUSED: return "check_box_selected";
            case ICON_PRESSED_SELECTED: return "check_box_selected_pressed";
            case ICON_PRESSED_SELECTED_FOCUSED: return "check_box_selected_pressed";
            case ICON_MOUSEOVER_SELECTED: return "check_box_selected";
            case ICON_MOUSEOVER_SELECTED_FOCUSED: return "check_box_selected";
            case ICON_DISABLED_SELECTED: return "check_box_disabled_selected";
        }
        return null;
    }
}
