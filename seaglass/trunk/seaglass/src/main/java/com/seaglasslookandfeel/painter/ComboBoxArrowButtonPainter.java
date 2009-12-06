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
 * ComboBoxArrowButtonPainter implementation.
 */
public final class ComboBoxArrowButtonPainter extends AbstractImagePainter<ComboBoxArrowButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_MOUSEOVER,
        BACKGROUND_ENABLED_PRESSED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_MOUSEOVER_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
        BACKGROUND_SELECTED_EDITABLE,
        FOREGROUND_ENABLED,
        FOREGROUND_MOUSEOVER,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_SELECTED,
        FOREGROUND_EDITABLE,
        FOREGROUND_EDITABLE_DISABLED,
    }

    private static final Insets    bgInsets            = new Insets(8, 1, 8, 8);
    private static final Dimension bgDimension         = new Dimension(21, 23);
    private static final CacheMode cacheMode           = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                = Double.POSITIVE_INFINITY;
    private static final Double    maxV                = 5.0;

    private static final Insets    fgInsets            = new Insets(0, 0, 0, 0);
    private static final Dimension fgDimension         = new Dimension(10, 5);

    private static final Dimension fgEditableDimension = new Dimension(6, 8);

    public ComboBoxArrowButtonPainter(Which state) {
        super(state);
        Insets ins = bgInsets;
        Dimension dim = bgDimension;
        boolean inverted = false;
        if (state == Which.FOREGROUND_ENABLED || state == Which.FOREGROUND_DISABLED || state == Which.FOREGROUND_PRESSED
                || state == Which.FOREGROUND_SELECTED) {
            ins = fgInsets;
            dim = fgDimension;
            inverted = true;
        } else if (state == Which.FOREGROUND_EDITABLE || state == Which.FOREGROUND_EDITABLE_DISABLED) {
            ins = fgInsets;
            dim = fgEditableDimension;
            inverted = true;
        }
        setPaintContext(new PaintContext(ins, dim, inverted, cacheMode, maxH, maxV));
    }

    @Override
    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED_EDITABLE:
            return "combo_box_editable_button_disabled";
        case BACKGROUND_ENABLED_EDITABLE:
            return "combo_box_editable_button_enabled";
        case BACKGROUND_MOUSEOVER_EDITABLE:
            return "combo_box_editable_button_enabled";
        case BACKGROUND_PRESSED_EDITABLE:
            return "combo_box_editable_button_pressed";
        case BACKGROUND_SELECTED_EDITABLE:
            return "combo_box_editable_button_pressed";
        case FOREGROUND_ENABLED:
            return "combo_box_arrows_enabled";
        case FOREGROUND_MOUSEOVER:
            return "combo_box_arrows_enabled";
        case FOREGROUND_DISABLED:
            return "combo_box_arrows_disabled";
        case FOREGROUND_PRESSED:
            return "combo_box_arrows_enabled";
        case FOREGROUND_SELECTED:
            return "combo_box_arrows_enabled";
        case FOREGROUND_EDITABLE:
            return "combo_box_editable_arrow_enabled";
        case FOREGROUND_EDITABLE_DISABLED:
            return "combo_box_editable_arrow_disabled";
        }
        return null;
    }
}
