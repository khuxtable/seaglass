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
 * Title pane close button implementation.
 */
public final class TitlePaneCloseButtonPainter extends AbstractImagePainter<TitlePaneCloseButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_MODIFIED,
        BACKGROUND_MODIFIED_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MODIFIED_WINDOWNOTFOCUSED,
        BACKGROUND_PRESSED_WINDOWNOTFOCUSED,
    }

    public TitlePaneCloseButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(new Insets(0, 0, 0, 0), new Dimension(43, 18), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "window_close_enabled";
        case BACKGROUND_ENABLED:
            return "window_close_enabled";
        case BACKGROUND_MOUSEOVER:
            return "window_close_hover";
        case BACKGROUND_MODIFIED:
            return "window_close_modified";
        case BACKGROUND_MODIFIED_MOUSEOVER:
            return "window_close_hover_modified";
        case BACKGROUND_PRESSED:
            return "window_close_pressed";
        case BACKGROUND_ENABLED_WINDOWNOTFOCUSED:
            return "window_close_enabled";
        case BACKGROUND_MODIFIED_WINDOWNOTFOCUSED:
            return "window_close_modified";
        case BACKGROUND_PRESSED_WINDOWNOTFOCUSED:
            return "window_close_pressed";
        }
        return null;
    }
}
