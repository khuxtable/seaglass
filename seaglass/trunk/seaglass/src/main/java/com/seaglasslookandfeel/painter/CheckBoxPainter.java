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
 * CheckBoxPainter implementation.
 */
public final class CheckBoxPainter extends AbstractImagePainter<CheckBoxPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        ICON_DISABLED,
        ICON_ENABLED,
        ICON_FOCUSED,
        ICON_PRESSED,
        ICON_PRESSED_FOCUSED,
        ICON_SELECTED,
        ICON_SELECTED_FOCUSED,
        ICON_PRESSED_SELECTED,
        ICON_PRESSED_SELECTED_FOCUSED,
        ICON_DISABLED_SELECTED,
    }

    private static final Insets    insets    = new Insets(5, 5, 5, 5);
    private static final Dimension dimension = new Dimension(16, 9);
    private static final CacheMode cacheMode = CacheMode.FIXED_SIZES;
    private static final Double    maxH      = 1.0;
    private static final Double    maxV      = 1.0;

    public CheckBoxPainter(Which state) {
        super(state);
        PaintContext ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
        setPaintContext(ctx);
    }

    @Override
    protected String getImageName(Which state) {
        // generate this entire method. Each state/bg/fg/border combo that has
        // been painted gets its own KEY and paint method.
        switch (state) {
        case ICON_DISABLED:
            return "check_box_disabled";
        case ICON_ENABLED:
            return "check_box_enabled";
        case ICON_FOCUSED:
            return "check_box_enabled";
        case ICON_PRESSED:
            return "check_box_pressed";
        case ICON_PRESSED_FOCUSED:
            return "check_box_pressed";
        case ICON_SELECTED:
            return "check_box_selected";
        case ICON_SELECTED_FOCUSED:
            return "check_box_selected";
        case ICON_PRESSED_SELECTED:
            return "check_box_selected_pressed";
        case ICON_PRESSED_SELECTED_FOCUSED:
            return "check_box_selected_pressed";
        case ICON_DISABLED_SELECTED:
            return "check_box_disabled_selected";
        }
        return null;
    }
}
