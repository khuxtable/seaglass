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
 * ToolBarToggleButtonPainter implementation.
 */
public final class ToolBarToggleButtonPainter extends AbstractImagePainter {
    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ToolBarToggleButtonPainter to determine which region/state is being
    // painted
    // by that instance.
    public static final int BACKGROUND_ENABLED                    = 1;
    public static final int BACKGROUND_FOCUSED                    = 2;
    public static final int BACKGROUND_MOUSEOVER                  = 3;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED          = 4;
    public static final int BACKGROUND_PRESSED                    = 5;
    public static final int BACKGROUND_PRESSED_FOCUSED            = 6;
    public static final int BACKGROUND_SELECTED                   = 7;
    public static final int BACKGROUND_SELECTED_FOCUSED           = 8;
    public static final int BACKGROUND_PRESSED_SELECTED           = 9;
    public static final int BACKGROUND_PRESSED_SELECTED_FOCUSED   = 10;
    public static final int BACKGROUND_MOUSEOVER_SELECTED         = 11;
    public static final int BACKGROUND_MOUSEOVER_SELECTED_FOCUSED = 12;
    public static final int BACKGROUND_DISABLED_SELECTED          = 13;

    public ToolBarToggleButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
            case BACKGROUND_FOCUSED: return "toolbar_button_focused";
            case BACKGROUND_MOUSEOVER: return "empty_image";
            case BACKGROUND_MOUSEOVER_FOCUSED: return "toolbar_button_focused";
            case BACKGROUND_PRESSED: return "toolbar_button_pressed";
            case BACKGROUND_PRESSED_FOCUSED: return "toolbar_button_focused_pressed";
            case BACKGROUND_SELECTED: return "toolbar_button_selected";
            case BACKGROUND_SELECTED_FOCUSED: return "toolbar_button_selected_focused";
            case BACKGROUND_PRESSED_SELECTED: return "toolbar_button_selected";
            case BACKGROUND_PRESSED_SELECTED_FOCUSED: return "toolbar_button_selected_focused";
            case BACKGROUND_MOUSEOVER_SELECTED: return "toolbar_button_selected";
            case BACKGROUND_MOUSEOVER_SELECTED_FOCUSED: return "toolbar_button_selected_focused";
            case BACKGROUND_DISABLED_SELECTED: return "toolbar_button_selected";
        }
        return null;
    }
}
