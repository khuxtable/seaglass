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
            case BACKGROUND_ENABLED: return "combo_box_enabled";
            case BACKGROUND_FOCUSED: return "combo_box_enabled";
            case BACKGROUND_MOUSEOVER_FOCUSED: return "combo_box";
            case BACKGROUND_MOUSEOVER: return "combo_box_enabled";
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
