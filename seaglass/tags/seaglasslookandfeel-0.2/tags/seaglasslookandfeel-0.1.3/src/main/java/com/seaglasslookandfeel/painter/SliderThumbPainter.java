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
 * SliderThumbPainter implementation.
 */
public final class SliderThumbPainter extends AbstractImagePainter<SliderThumbPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_FOCUSED_MOUSEOVER,
        BACKGROUND_FOCUSED_PRESSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_ARROWSHAPE,
        BACKGROUND_DISABLED_ARROWSHAPE,
        BACKGROUND_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_PRESSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_ARROWSHAPE,
        BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE,
        BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE,
    }

    private static final Insets    insets    = new Insets(5, 5, 5, 5);
    private static final Dimension dimension = new Dimension(15, 20);
    private static final Dimension discreteDimension = new Dimension(15, 20);
    private static final CacheMode cacheMode = CacheMode.FIXED_SIZES;
    private static final Double    maxH      = 1.0;
    private static final Double    maxV      = 1.0;

    public SliderThumbPainter(Which state) {
        super(state);
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_FOCUSED_MOUSEOVER:
        case BACKGROUND_FOCUSED_PRESSED:
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_PRESSED:
            setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
            break;
        case BACKGROUND_ENABLED_ARROWSHAPE:
        case BACKGROUND_DISABLED_ARROWSHAPE:
        case BACKGROUND_MOUSEOVER_ARROWSHAPE:
        case BACKGROUND_PRESSED_ARROWSHAPE:
        case BACKGROUND_FOCUSED_ARROWSHAPE:
        case BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE:
        case BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE:
            setPaintContext(new PaintContext(insets, discreteDimension, false, cacheMode, maxH, maxV));
            break;
        }
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "slider_knob_continuous";
        case BACKGROUND_ENABLED:
            return "slider_knob_continuous";
        case BACKGROUND_FOCUSED:
            return "slider_knob_continuous";
        case BACKGROUND_FOCUSED_MOUSEOVER:
            return "slider_knob_continuous";
        case BACKGROUND_FOCUSED_PRESSED:
            return "slider_knob_continuous_pressed";
        case BACKGROUND_MOUSEOVER:
            return "slider_knob_continuous";
        case BACKGROUND_PRESSED:
            return "slider_knob_continuous_pressed";
        case BACKGROUND_ENABLED_ARROWSHAPE:
            return "slider_knob_discrete";
        case BACKGROUND_DISABLED_ARROWSHAPE:
            return "slider_knob_discrete";
        case BACKGROUND_MOUSEOVER_ARROWSHAPE:
            return "slider_knob_discrete";
        case BACKGROUND_PRESSED_ARROWSHAPE:
            return "slider_knob_discrete_pressed";
        case BACKGROUND_FOCUSED_ARROWSHAPE:
            return "slider_knob_discrete";
        case BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE:
            return "slider_knob_discrete";
        case BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE:
            return "slider_knob_discrete_pressed";
        }
        return null;
    }
}
