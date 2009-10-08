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
 * SpinnerFormattedTextFieldPainter implementation.
 */
public final class SpinnerFormattedTextFieldPainter extends AbstractImagePainter {
    public static final int BACKGROUND_DISABLED         = 1;
    public static final int BACKGROUND_ENABLED          = 2;
    public static final int BACKGROUND_SELECTED         = 3;
    public static final int BACKGROUND_FOCUSED          = 4;
    public static final int BACKGROUND_SELECTED_FOCUSED = 5;

    public SpinnerFormattedTextFieldPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "combo_box_textfield_disabled";
        case BACKGROUND_ENABLED:
            return "spinner_textfield_enabled";
        case BACKGROUND_FOCUSED:
            return "spinner_textfield_enabled";
        case BACKGROUND_SELECTED:
            return "combo_box_textfield_selected";
        case BACKGROUND_SELECTED_FOCUSED:
            return "combo_box_textfield_selected";
        }
        return null;
    }
}
