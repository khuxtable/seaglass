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
 * ComboBoxPainter implementation.
 */
public final class ComboBoxPainter extends AbstractImagePainter<ComboBoxPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_DISABLED_PRESSED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_MOUSEOVER_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SELECTED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_FOCUSED_EDITABLE,
        BACKGROUND_MOUSEOVER_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
    }

    private static final Insets    insets            = new Insets(8, 9, 8, 23);
    private static final Dimension dimension         = new Dimension(105, 23);
    private static final CacheMode cacheMode         = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH              = Double.POSITIVE_INFINITY;
    private static final Double    maxV              = 5.0;

    private static final Insets    editableInsets    = new Insets(0, 0, 0, 0);
    private static final Dimension editableDimension = new Dimension(1, 1);
    private static final Insets    focusInsets       = new Insets(5, 5, 5, 5);

    public ComboBoxPainter(Which state) {
        super(state);
        if (state == Which.BACKGROUND_DISABLED_EDITABLE || state == Which.BACKGROUND_ENABLED_EDITABLE
                || state == Which.BACKGROUND_PRESSED_EDITABLE) {
            setPaintContext(new PaintContext(editableInsets, editableDimension, false, cacheMode, maxH, maxV));
        } else if (state == Which.BACKGROUND_FOCUSED_EDITABLE) {
            setPaintContext(new PaintContext(focusInsets, dimension, false, cacheMode, maxH, maxV));
        } else {
            setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
        }
    }

    @Override
    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "combo_box_disabled";
        case BACKGROUND_DISABLED_PRESSED:
            return "combo_box_disabled";
        case BACKGROUND_ENABLED:
            return "combo_box_enabled";
        case BACKGROUND_FOCUSED:
            return "combo_box_enabled";
        case BACKGROUND_MOUSEOVER_FOCUSED:
            return "combo_box";
        case BACKGROUND_MOUSEOVER:
            return "combo_box_enabled";
        case BACKGROUND_PRESSED_FOCUSED:
            return "combo_box_pressed";
        case BACKGROUND_PRESSED:
            return "combo_box_pressed";
        case BACKGROUND_ENABLED_SELECTED:
            return "combo_box_pressed";
        case BACKGROUND_DISABLED_EDITABLE:
            return "empty_image";
        case BACKGROUND_ENABLED_EDITABLE:
            return "empty_image";
        case BACKGROUND_FOCUSED_EDITABLE:
            return "combo_box_focused_editable";
        case BACKGROUND_MOUSEOVER_EDITABLE:
            return "empty_image";
        case BACKGROUND_PRESSED_EDITABLE:
            return "empty_image";
        }
        return null;
    }
}
