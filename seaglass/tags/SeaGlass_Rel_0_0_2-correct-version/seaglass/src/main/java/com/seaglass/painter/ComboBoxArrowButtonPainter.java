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
 * ComboBoxArrowButtonPainter implementation.
 */
public final class ComboBoxArrowButtonPainter extends AbstractImagePainter {
    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ComboBoxArrowButtonPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int BACKGROUND_DISABLED           = 1;
    public static final int BACKGROUND_ENABLED            = 2;
    public static final int BACKGROUND_ENABLED_MOUSEOVER  = 3;
    public static final int BACKGROUND_ENABLED_PRESSED    = 4;
    public static final int BACKGROUND_DISABLED_EDITABLE  = 5;
    public static final int BACKGROUND_ENABLED_EDITABLE   = 6;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 7;
    public static final int BACKGROUND_PRESSED_EDITABLE   = 8;
    public static final int BACKGROUND_SELECTED_EDITABLE  = 9;
    public static final int FOREGROUND_ENABLED            = 10;
    public static final int FOREGROUND_MOUSEOVER          = 11;
    public static final int FOREGROUND_DISABLED           = 12;
    public static final int FOREGROUND_PRESSED            = 13;
    public static final int FOREGROUND_SELECTED           = 14;
    public static final int FOREGROUND_EDITABLE           = 15;
    public static final int FOREGROUND_EDITABLE_DISABLED  = 16;

    public ComboBoxArrowButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch (state) {
        case BACKGROUND_DISABLED_EDITABLE:
            return "combo_box_button_background_disabled";
        case BACKGROUND_ENABLED_EDITABLE:
            return "combo_box_button_background";
        case BACKGROUND_MOUSEOVER_EDITABLE:
            return "combo_box_button_background";
        case BACKGROUND_PRESSED_EDITABLE:
            return "combo_box_button_background_pressed";
        case BACKGROUND_SELECTED_EDITABLE:
            return "combo_box_button_background_pressed";
        case FOREGROUND_ENABLED:
            return "combo_box_arrows";
        case FOREGROUND_MOUSEOVER:
            return "combo_box_arrows";
        case FOREGROUND_DISABLED:
            return "combo_box_arrows_disabled";
        case FOREGROUND_PRESSED:
            return "combo_box_arrows";
        case FOREGROUND_SELECTED:
            return "combo_box_arrows";
        case FOREGROUND_EDITABLE:
            return "combo_box_arrow_dropdown";
        case FOREGROUND_EDITABLE_DISABLED:
            return "combo_box_arrow_dropdown_disabled";
        }
        return null;
    }
}
