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
 * Title pane maximize/minimize button (aka "toggleButton") painter.
 */
public final class TitlePaneMaximizeButtonPainter extends AbstractImagePainter {
    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of
    // InternalFrameInternalFrameTitlePaneInternalFrameTitlePaneMaximizeButtonPainter
    // to determine which region/state is being painted by that instance.
    public static final int BACKGROUND_DISABLED_WINDOWMAXIMIZED                   = 1;
    public static final int BACKGROUND_ENABLED_WINDOWMAXIMIZED                    = 2;
    public static final int BACKGROUND_MOUSEOVER_WINDOWMAXIMIZED                  = 3;
    public static final int BACKGROUND_PRESSED_WINDOWMAXIMIZED                    = 4;
    public static final int BACKGROUND_ENABLED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED   = 5;
    public static final int BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED_WINDOWMAXIMIZED = 6;
    public static final int BACKGROUND_PRESSED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED   = 7;
    public static final int BACKGROUND_DISABLED                                   = 8;
    public static final int BACKGROUND_ENABLED                                    = 9;
    public static final int BACKGROUND_MOUSEOVER                                  = 10;
    public static final int BACKGROUND_PRESSED                                    = 11;
    public static final int BACKGROUND_ENABLED_WINDOWNOTFOCUSED                   = 12;
    public static final int BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED                 = 13;
    public static final int BACKGROUND_PRESSED_WINDOWNOTFOCUSED                   = 14;

    public TitlePaneMaximizeButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED_WINDOWMAXIMIZED: return "titlepane_restorebutton_disabled";
            case BACKGROUND_ENABLED_WINDOWMAXIMIZED: return "titlepane_restorebutton";
            case BACKGROUND_MOUSEOVER_WINDOWMAXIMIZED: return "titlepane_restorebutton";
            case BACKGROUND_PRESSED_WINDOWMAXIMIZED: return "titlepane_restorebutton_pressed";
            case BACKGROUND_ENABLED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED: return "titlepane_restorebutton_notfocused";
            case BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED_WINDOWMAXIMIZED: return "titlepane_restorebutton_notfocused";
            case BACKGROUND_PRESSED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED: return "titlepane_restorebutton_notfocused";
            case BACKGROUND_DISABLED: return "titlepane_maximizebutton_disabled";
            case BACKGROUND_ENABLED: return "titlepane_maximizebutton";
            case BACKGROUND_MOUSEOVER: return "titlepane_maximizebutton";
            case BACKGROUND_PRESSED: return "titlepane_maximizebutton_pressed";
            case BACKGROUND_ENABLED_WINDOWNOTFOCUSED: return "titlepane_maximizebutton_notfocused";
            case BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED: return "titlepane_maximizebutton_notfocused";
            case BACKGROUND_PRESSED_WINDOWNOTFOCUSED: return "titlepane_maximizebutton_notfocused";
        }
        return null;
    }
}
