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
 * ToolBarToggleButtonPainter implementation.
 */
public final class ToolBarToggleButtonPainter extends AbstractImagePainter<ToolBarToggleButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_SELECTED,
        BACKGROUND_SELECTED_FOCUSED,
        BACKGROUND_PRESSED_SELECTED,
        BACKGROUND_PRESSED_SELECTED_FOCUSED,
        BACKGROUND_DISABLED_SELECTED,
    }

    private static final Insets    insets    = new Insets(5, 5, 5, 5);
    private static final Dimension dimension = new Dimension(104, 34);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = 2.0;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    public ToolBarToggleButtonPainter(Which state) {
        super(state);
        PaintContext ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
        setPaintContext(ctx);
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_FOCUSED:
            return "toolbar_button_focused";
        case BACKGROUND_PRESSED:
            return "toolbar_button_pressed";
        case BACKGROUND_PRESSED_FOCUSED:
            return "toolbar_button_focused_pressed";
        case BACKGROUND_SELECTED:
            return "toolbar_button_selected";
        case BACKGROUND_SELECTED_FOCUSED:
            return "toolbar_button_selected_focused";
        case BACKGROUND_PRESSED_SELECTED:
            return "toolbar_button_selected";
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return "toolbar_button_selected_focused";
        case BACKGROUND_DISABLED_SELECTED:
            return "toolbar_button_selected";
        }
        return null;
    }
}
