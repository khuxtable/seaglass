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
 */
public final class SpinnerPreviousButtonPainter extends AbstractImagePainter {
    public static final int BACKGROUND_DISABLED          = 1;
    public static final int BACKGROUND_ENABLED           = 2;
    public static final int BACKGROUND_FOCUSED           = 3;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
    public static final int BACKGROUND_PRESSED_FOCUSED   = 5;
    public static final int BACKGROUND_MOUSEOVER         = 6;
    public static final int BACKGROUND_PRESSED           = 7;
    public static final int FOREGROUND_DISABLED          = 8;
    public static final int FOREGROUND_ENABLED           = 9;
    public static final int FOREGROUND_FOCUSED           = 10;
    public static final int FOREGROUND_MOUSEOVER_FOCUSED = 11;
    public static final int FOREGROUND_PRESSED_FOCUSED   = 12;
    public static final int FOREGROUND_MOUSEOVER         = 13;
    public static final int FOREGROUND_PRESSED           = 14;

    public SpinnerPreviousButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
            case BACKGROUND_DISABLED: return "spinner_prev_button";
            case BACKGROUND_ENABLED: return "spinner_prev_button";
            case BACKGROUND_FOCUSED: return "spinner_prev_button";
            case BACKGROUND_MOUSEOVER_FOCUSED: return "spinner_prev_button";
            case BACKGROUND_PRESSED_FOCUSED: return "spinner_prev_button";
            case BACKGROUND_MOUSEOVER: return "spinner_prev_button";
            case BACKGROUND_PRESSED: return "spinner_prev_button";
            case FOREGROUND_DISABLED: return "spinner_prev_button_arrow";
            case FOREGROUND_ENABLED: return "spinner_prev_button_arrow";
            case FOREGROUND_FOCUSED: return "spinner_prev_button_arrow";
            case FOREGROUND_MOUSEOVER_FOCUSED: return "spinner_prev_button_arrow";
            case FOREGROUND_PRESSED_FOCUSED: return "spinner_prev_button_arrow";
            case FOREGROUND_MOUSEOVER: return "spinner_prev_button_arrow";
            case FOREGROUND_PRESSED: return "spinner_prev_button_arrow";
        }
        return null;
    }
}
