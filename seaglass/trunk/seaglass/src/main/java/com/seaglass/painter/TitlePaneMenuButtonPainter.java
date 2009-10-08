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
public final class TitlePaneMenuButtonPainter extends AbstractImagePainter {
    public static final int ICON_ENABLED                    = 1;
    public static final int ICON_DISABLED                   = 2;
    public static final int ICON_MOUSEOVER                  = 3;
    public static final int ICON_PRESSED                    = 4;
    public static final int ICON_ENABLED_WINDOWNOTFOCUSED   = 5;
    public static final int ICON_MOUSEOVER_WINDOWNOTFOCUSED = 6;
    public static final int ICON_PRESSED_WINDOWNOTFOCUSED   = 7;

    public TitlePaneMenuButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
        case ICON_ENABLED:
            return "menubutton";
        case ICON_DISABLED:
            return "menubutton";
        case ICON_MOUSEOVER:
            return "menubutton";
        case ICON_PRESSED:
            return "menubutton";
        case ICON_ENABLED_WINDOWNOTFOCUSED:
            return "menubutton";
        case ICON_MOUSEOVER_WINDOWNOTFOCUSED:
            return "menubutton";
        case ICON_PRESSED_WINDOWNOTFOCUSED:
            return "menubutton";
        }
        return null;
    }
}
