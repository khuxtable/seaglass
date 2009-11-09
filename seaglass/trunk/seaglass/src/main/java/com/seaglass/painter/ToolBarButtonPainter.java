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
 * ToolBarButtonPainter implementation.
 */
public final class ToolBarButtonPainter extends AbstractImagePainter<ToolBarButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_MOUSEOVER_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_PRESSED_FOCUSED,
    }

    private static final Insets    insets    = new Insets(5, 5, 5, 5);
    private static final Dimension dimension = new Dimension(104, 33);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = 2.0;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    public ToolBarButtonPainter(Which state) {
        super(state);
        PaintContext ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
        setPaintContext(ctx);
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_FOCUSED:
            return "toolbar_button_focused";
        case BACKGROUND_MOUSEOVER:
            return "empty_image";
        case BACKGROUND_MOUSEOVER_FOCUSED:
            return "toolbar_button_focused";
        case BACKGROUND_PRESSED:
            return "toolbar_button_pressed";
        case BACKGROUND_PRESSED_FOCUSED:
            return "toolbar_button_focused_pressed";
        }
        return null;
    }
}
