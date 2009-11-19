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
 */
public final class TitlePaneMenuButtonPainter extends AbstractImagePainter<TitlePaneMenuButtonPainter.Which> {
    public static enum Which {
        ICON_ENABLED,
        ICON_DISABLED,
        ICON_MOUSEOVER,
        ICON_PRESSED,
        ICON_ENABLED_WINDOWNOTFOCUSED,
        ICON_MOUSEOVER_WINDOWNOTFOCUSED,
        ICON_PRESSED_WINDOWNOTFOCUSED,
    }

    public TitlePaneMenuButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
    }

    protected String getImageName(Which state) {
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
