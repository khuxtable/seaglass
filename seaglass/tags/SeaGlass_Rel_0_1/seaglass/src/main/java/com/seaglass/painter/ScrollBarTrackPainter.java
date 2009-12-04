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
 * ScrollBarTrackPainter implementation.
 */
public final class ScrollBarTrackPainter extends AbstractImagePainter<ScrollBarTrackPainter.Which> {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private static final Insets    insets    = new Insets(0, 0, 0, 0);
    private static final Dimension dimension = new Dimension(19, 15);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = 2.0;

    public ScrollBarTrackPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
    }

    protected String getImageName(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "h_scroll_track_disabled";
        case BACKGROUND_ENABLED:
            return "h_scroll_track_enabled";
        }
        return null;
    }
}
