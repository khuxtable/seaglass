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

/**
 */
public final class TextFieldPainter extends AbstractImagePainter<TextFieldPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED, BORDER_DISABLED, BORDER_FOCUSED, BORDER_ENABLED,
    }

    public TextFieldPainter(Which state) {
        super(state);
        switch (state) {
        case BORDER_DISABLED:
            setPaintContext(new PaintContext(new Insets(5, 3, 3, 3), new Dimension(122, 24), false,
                AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
            break;
        default:
            setPaintContext(new PaintContext(new Insets(5, 5, 5, 5), new Dimension(122, 24), false,
                AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
            break;
        }
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "text_field_disabled";
        case BACKGROUND_ENABLED:
            return "textfield";
        case BACKGROUND_SELECTED:
            return "textfield";
        case BORDER_DISABLED:
            return "text_field_border_disabled";
        case BORDER_FOCUSED:
            return "text_field_border_focused";
        case BORDER_ENABLED:
            return "text_field_border";
        }
        return null;
    }
}
