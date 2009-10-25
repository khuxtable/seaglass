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
 * Title pane iconify button painter implementation.
 */
public final class TitlePaneIconifyButtonPainter extends AbstractImagePainter {
    public static final int BACKGROUND_ENABLED                              = 1;
    public static final int BACKGROUND_DISABLED                             = 2;
    public static final int BACKGROUND_MOUSEOVER                            = 3;
    public static final int BACKGROUND_PRESSED                              = 4;
    public static final int BACKGROUND_ENABLED_WINDOWNOTFOCUSED             = 5;
    public static final int BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED           = 6;
    public static final int BACKGROUND_PRESSED_WINDOWNOTFOCUSED             = 7;

    public static final int BACKGROUND_MINIMIZED_DISABLED                   = 11;
    public static final int BACKGROUND_MINIMIZED_ENABLED                    = 12;
    public static final int BACKGROUND_MINIMIZED_MOUSEOVER                  = 13;
    public static final int BACKGROUND_MINIMIZED_PRESSED                    = 14;
    public static final int BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED   = 15;
    public static final int BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED = 16;
    public static final int BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED   = 17;

    public TitlePaneIconifyButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch (state) {
        case BACKGROUND_ENABLED:
            return "window_minimize_enabled";
        case BACKGROUND_DISABLED:
            return "window_minimize_enabled";
        case BACKGROUND_MOUSEOVER:
            return "window_minimize_hover";
        case BACKGROUND_PRESSED:
            return "window_minimize_pressed";
        case BACKGROUND_ENABLED_WINDOWNOTFOCUSED:
            return "window_minimize_enabled";
        case BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED:
            return "window_minimize_hover";
        case BACKGROUND_PRESSED_WINDOWNOTFOCUSED:
            return "window_minimize_pressed";

        case BACKGROUND_MINIMIZED_DISABLED:
            return "window_restore_enabled";
        case BACKGROUND_MINIMIZED_ENABLED:
            return "window_restore_enabled";
        case BACKGROUND_MINIMIZED_MOUSEOVER:
            return "window_restore_hover";
        case BACKGROUND_MINIMIZED_PRESSED:
            return "window_restore_pressed";
        case BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED:
            return "window_restore_enabled";
        case BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED:
            return "window_restore_hover";
        case BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED:
            return "window_restore_pressed";
        }
        return null;
    }
}
