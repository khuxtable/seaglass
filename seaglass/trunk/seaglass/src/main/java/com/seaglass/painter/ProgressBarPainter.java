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
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

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

    // FIXME These are not assigned properly.
    private static final Insets    insets                   = new Insets(6, 6, 8, 6);
    private static final Dimension dimension                = new Dimension(34, 17);
    private static final CacheMode cacheMode                = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                     = Double.POSITIVE_INFINITY;
    private static final Double    maxV                     = Double.POSITIVE_INFINITY;

    private static final Insets    fgInsets                 = new Insets(1, 0, 3, 2);
    private static final Dimension fgDimension              = new Dimension(24, 17);
    private static final Insets    fgIndeterminateInsets    = new Insets(1, 0, 3, 0);
    private static final Dimension fgIndeterminateDimension = new Dimension(24, 17);

    private RoundRectangle2D       roundRect                = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Which                  state;

    public ProgressBarPainter(Which state) {
        super(state);
        this.state = state;
        if (state == Which.BACKGROUND_ENABLED || state == Which.BACKGROUND_DISABLED) {
            setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
        } else if (state == Which.FOREGROUND_DISABLED_INDETERMINATE || state == Which.FOREGROUND_ENABLED_INDETERMINATE) {
            setPaintContext(new PaintContext(fgIndeterminateInsets, fgIndeterminateDimension, false, cacheMode, maxH, maxV));
        } else {
            setPaintContext(new PaintContext(fgInsets, fgDimension, false, cacheMode, maxH, maxV));
        }
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

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (state != Which.BACKGROUND_ENABLED && state != Which.BACKGROUND_DISABLED) {
            roundRect = decodeRoundClip(width, height);
            // g.setClip(roundRect);
        }
        image.paintIcon(c, g, 0, 0);
    }

    private RoundRectangle2D decodeRoundClip(int width, int height) {
        roundRect.setRoundRect(0, 1, 24, 13, 13f, 13f);
        return roundRect;
    }
}
