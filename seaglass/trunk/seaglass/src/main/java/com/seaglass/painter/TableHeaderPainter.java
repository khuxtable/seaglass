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
public final class TableHeaderPainter extends AbstractImagePainter<TableHeaderPainter.Which> {
    public static enum Which {
        ASCENDINGSORTICON_ENABLED, DESCENDINGSORTICON_ENABLED
    }

    public TableHeaderPainter(Which state) {
        super(state);
        if (state == Which.ASCENDINGSORTICON_ENABLED) {
            setPaintContext(new PaintContext(new Insets(0, 0, 0, 2), new Dimension(8, 7), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
        } else {
            setPaintContext(new PaintContext(new Insets(0, 0, 0, 0), new Dimension(8, 7), false, CacheMode.FIXED_SIZES, 1.0, 1.0));
        }
    }

    protected String getImageName(Which state) {
        switch (state) {
        case ASCENDINGSORTICON_ENABLED:
            return "table_header_sort_ascending_enabled";
        case DESCENDINGSORTICON_ENABLED:
            return "table_header_sort_descending_enabled";
        }
        return null;
    }
}
