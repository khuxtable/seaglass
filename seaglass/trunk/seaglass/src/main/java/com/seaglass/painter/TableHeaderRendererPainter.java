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
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractImagePainter<TableHeaderRendererPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SORTED,
        BACKGROUND_ENABLED_FOCUSED_SORTED,
        BACKGROUND_DISABLED_SORTED,
    }

    public TableHeaderRendererPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(new Insets(3, 3, 3, 3), new Dimension(26, 16), false, CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    @Override
    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "table_header_disabled";
        case BACKGROUND_ENABLED:
            return "table_header_enabled";
        case BACKGROUND_ENABLED_FOCUSED:
            return "table_header_enabled";
        case BACKGROUND_MOUSEOVER:
            return "table_header_enabled";
        case BACKGROUND_PRESSED:
            return "table_header_pressed";
        case BACKGROUND_ENABLED_SORTED:
            return "table_header_sorted";
        case BACKGROUND_ENABLED_FOCUSED_SORTED:
            return "table_header_sorted";
        case BACKGROUND_DISABLED_SORTED:
            return "table_header_disabled_sorted";
        }
        return null;
    }
}
