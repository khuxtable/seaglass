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
public final class ProgressBarPainter extends AbstractImagePainter<ProgressBarPainter.Which> {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_ENABLED_FINISHED,
        FOREGROUND_ENABLED_INDETERMINATE,
        FOREGROUND_DISABLED,
        FOREGROUND_DISABLED_FINISHED,
        FOREGROUND_DISABLED_INDETERMINATE,
    }

    private static final Insets    bgInsets                 = new Insets(8, 8, 8, 8);
    private static final Dimension bgDimension              = new Dimension(19, 19);
    private static final Insets    fgInsets                 = new Insets(0, 0, 0, 2);
    private static final Dimension fgDimension              = new Dimension(24, 13);
    private static final Insets    fgIndeterminateInsets    = new Insets(0, 0, 0, 0);
    private static final Dimension fgIndeterminateDimension = new Dimension(24, 13);
    private static final CacheMode cacheMode                = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                     = Double.POSITIVE_INFINITY;
    private static final Double    maxV                     = Double.POSITIVE_INFINITY;

    public ProgressBarPainter(Which state) {
        super(state);

        Insets insets;
        Dimension dimension;
        switch (state) {
        case BACKGROUND_ENABLED:
        case BACKGROUND_DISABLED:
            insets = bgInsets;
            dimension = bgDimension;
            break;
        case FOREGROUND_ENABLED_INDETERMINATE:
        case FOREGROUND_DISABLED_INDETERMINATE:
            insets = fgIndeterminateInsets;
            dimension = fgIndeterminateDimension;
            break;
        default:
            insets = fgInsets;
            dimension = fgDimension;
            break;
        }

        setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_ENABLED:
            return "progress_bar_track";
        case BACKGROUND_DISABLED:
            return "progress_bar_track";
        case FOREGROUND_ENABLED:
            return "progress_bar";
        case FOREGROUND_ENABLED_FINISHED:
            return "progress_bar";
        case FOREGROUND_ENABLED_INDETERMINATE:
            return "progress_bar_indeterminate";
        case FOREGROUND_DISABLED:
            return "progress_bar";
        case FOREGROUND_DISABLED_FINISHED:
            return "progress_bar";
        case FOREGROUND_DISABLED_INDETERMINATE:
            return "progress_bar_indeterminate";
        }
        return null;
    }
}
