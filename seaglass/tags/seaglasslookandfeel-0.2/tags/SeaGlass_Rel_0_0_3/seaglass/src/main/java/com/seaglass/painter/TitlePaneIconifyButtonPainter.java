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

import java.awt.Dimension;
import java.awt.Insets;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Title pane iconify button painter implementation.
 */
public final class TitlePaneIconifyButtonPainter extends AbstractImagePainter<TitlePaneIconifyButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_DISABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_PRESSED_WINDOWNOTFOCUSED,

        BACKGROUND_MINIMIZED_DISABLED,
        BACKGROUND_MINIMIZED_ENABLED,
        BACKGROUND_MINIMIZED_MOUSEOVER,
        BACKGROUND_MINIMIZED_PRESSED,
        BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED,
    }

    public TitlePaneIconifyButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(new Insets(0, 0, 0, 0), new Dimension(26, 18), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
    }

    @Override
    protected String getImageName(Which state) {
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
