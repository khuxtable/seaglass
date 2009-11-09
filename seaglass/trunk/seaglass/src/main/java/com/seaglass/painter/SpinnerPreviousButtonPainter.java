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
public final class SpinnerPreviousButtonPainter extends AbstractImagePainter<SpinnerPreviousButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_MOUSEOVER_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        FOREGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_FOCUSED,
        FOREGROUND_MOUSEOVER_FOCUSED,
        FOREGROUND_PRESSED_FOCUSED,
        FOREGROUND_MOUSEOVER,
        FOREGROUND_PRESSED,
    }

    private static final Insets    insets      = new Insets(0, 0, 0, 0);
    private static final Dimension dimension   = new Dimension(6, 5);
    private static final CacheMode cacheMode   = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH        = Double.POSITIVE_INFINITY;
    private static final Double    maxV        = 5.0;

    private static final Insets    bgInsets    = new Insets(3, 3, 4, 4);
    private static final Dimension bgDimension = new Dimension(21, 11);
    private static final CacheMode bgCacheMode = CacheMode.FIXED_SIZES;

    public SpinnerPreviousButtonPainter(Which state) {
        super(state);
        Insets ins = insets;
        Dimension dim = dimension;
        CacheMode cm = cacheMode;
        Double maxH = SpinnerPreviousButtonPainter.maxH;
        Double maxV = SpinnerPreviousButtonPainter.maxV;
        boolean inverted = true;
        if (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_ENABLED || state == Which.BACKGROUND_FOCUSED
                || state == Which.BACKGROUND_MOUSEOVER_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED
                || state == Which.BACKGROUND_MOUSEOVER || state == Which.BACKGROUND_PRESSED) {
            ins = bgInsets;
            dim = bgDimension;
            cm = bgCacheMode;
            maxH = 1.0;
            maxV = 1.0;
            inverted = false;
        }
        setPaintContext(new PaintContext(ins, dim, inverted, cm, maxH, maxV));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "spinner_button_down_disabled";
        case BACKGROUND_ENABLED:
            return "spinner_button_down_enabled";
        case BACKGROUND_FOCUSED:
            return "spinner_button_down_enabled";
        case BACKGROUND_MOUSEOVER_FOCUSED:
            return "spinner_button_down_enabled";
        case BACKGROUND_PRESSED_FOCUSED:
            return "spinner_button_down_pressed";
        case BACKGROUND_MOUSEOVER:
            return "spinner_button_down_enabled";
        case BACKGROUND_PRESSED:
            return "spinner_button_down_pressed";
        case FOREGROUND_DISABLED:
            return "spinner_arrow_down_disabled";
        case FOREGROUND_ENABLED:
            return "spinner_arrow_down_enabled";
        case FOREGROUND_FOCUSED:
            return "spinner_arrow_down_enabled";
        case FOREGROUND_MOUSEOVER_FOCUSED:
            return "spinner_arrow_down_enabled";
        case FOREGROUND_PRESSED_FOCUSED:
            return "spinner_arrow_down_enabled";
        case FOREGROUND_MOUSEOVER:
            return "spinner_arrow_down_enabled";
        case FOREGROUND_PRESSED:
            return "spinner_arrow_down_enabled";
        }
        return null;
    }
}
