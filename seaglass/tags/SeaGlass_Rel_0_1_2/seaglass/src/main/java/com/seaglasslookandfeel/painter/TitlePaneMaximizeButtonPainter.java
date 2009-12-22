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
package com.seaglasslookandfeel.painter;

import java.awt.Dimension;
import java.awt.Insets;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Title pane maximize/minimize button (aka "toggleButton") painter.
 */
public final class TitlePaneMaximizeButtonPainter extends AbstractImagePainter<TitlePaneMaximizeButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_PRESSED_WINDOWNOTFOCUSED,

        BACKGROUND_MAXIMIZED_DISABLED,
        BACKGROUND_MAXIMIZED_ENABLED,
        BACKGROUND_MAXIMIZED_MOUSEOVER,
        BACKGROUND_MAXIMIZED_PRESSED,
        BACKGROUND_MAXIMIZED_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MAXIMIZED_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_MAXIMIZED_PRESSED_WINDOWNOTFOCUSED,
    }

    public TitlePaneMaximizeButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(new Insets(0, 0, 0, 0), new Dimension(25, 18), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "window_maximize_enabled";
        case BACKGROUND_ENABLED:
            return "window_maximize_enabled";
        case BACKGROUND_MOUSEOVER:
            return "window_maximize_hover";
        case BACKGROUND_PRESSED:
            return "window_maximize_pressed";
        case BACKGROUND_ENABLED_WINDOWNOTFOCUSED:
            return "window_maximize_enabled";
        case BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED:
            return "window_maximize_hover";
        case BACKGROUND_PRESSED_WINDOWNOTFOCUSED:
            return "window_maximize_pressed";

        case BACKGROUND_MAXIMIZED_DISABLED:
            return "window_restore_enabled";
        case BACKGROUND_MAXIMIZED_ENABLED:
            return "window_restore_enabled";
        case BACKGROUND_MAXIMIZED_MOUSEOVER:
            return "window_restore_hover";
        case BACKGROUND_MAXIMIZED_PRESSED:
            return "window_restore_pressed";
        case BACKGROUND_MAXIMIZED_ENABLED_WINDOWNOTFOCUSED:
            return "window_restore_enabled";
        case BACKGROUND_MAXIMIZED_MOUSEOVER_WINDOWNOTFOCUSED:
            return "window_restore_hover";
        case BACKGROUND_MAXIMIZED_PRESSED_WINDOWNOTFOCUSED:
            return "window_restore_pressed";
        }
        return null;
    }
}
