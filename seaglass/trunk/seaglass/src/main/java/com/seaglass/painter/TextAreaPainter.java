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
public final class TextAreaPainter extends AbstractImagePainter<TextAreaPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_DISABLED_NOTINSCROLLPANE,
        BACKGROUND_ENABLED_NOTINSCROLLPANE,
        BACKGROUND_SELECTED,
        BORDER_DISABLED_NOTINSCROLLPANE,
        BORDER_FOCUSED_NOTINSCROLLPANE,
        BORDER_ENABLED_NOTINSCROLLPANE,
    }

    public TextAreaPainter(Which state) {
        super(state);
        // FIXME These are not properly assigned.
        setPaintContext(new PaintContext(new Insets(3, 3, 3, 3), new Dimension(26, 16), false, CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "textarea_disabled";
        case BACKGROUND_ENABLED:
            return "textarea";
        case BACKGROUND_DISABLED_NOTINSCROLLPANE:
            return "textarea_disabled_notinscroll";
        case BACKGROUND_ENABLED_NOTINSCROLLPANE:
            return "textarea_notinscroll";
        case BACKGROUND_SELECTED:
            return "textarea_selected";
        case BORDER_DISABLED_NOTINSCROLLPANE:
            return "textarea_border_disabled_notinscroll";
        case BORDER_FOCUSED_NOTINSCROLLPANE:
            return "textarea_border_focused_notinscroll";
        case BORDER_ENABLED_NOTINSCROLLPANE:
            return "textarea_border_notinscroll";
        }
        return null;
    }
}
