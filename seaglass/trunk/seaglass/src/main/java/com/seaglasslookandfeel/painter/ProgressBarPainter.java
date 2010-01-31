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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.effect.SeaGlassInternalShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * ProgressBarPainter implementation.
 */
public final class ProgressBarPainter extends AbstractRegionPainter {
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

    private Effect                       dropShadow     = new SeaGlassDropShadowEffect();
    private SeaGlassInternalShadowEffect internalShadow = new SeaGlassInternalShadowEffect();

    private Which                        state;
    private PaintContext                 ctx;
    private ButtonType                   type;

    public ProgressBarPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
        type = getButtonType(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintTrack(g, width, height);
            break;
        case BACKGROUND_DISABLED:
            paintTrack(g, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintBar(g, width, height, false);
            break;
        case FOREGROUND_ENABLED_FINISHED:
            paintBar(g, width, height, true);
            break;
        case FOREGROUND_ENABLED_INDETERMINATE:
            paintIndeterminateBar(g, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintBar(g, width, height, false);
            break;
        case FOREGROUND_DISABLED_FINISHED:
            paintBar(g, width, height, true);
            break;
        case FOREGROUND_DISABLED_INDETERMINATE:
            paintIndeterminateBar(g, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case FOREGROUND_DISABLED:
        case FOREGROUND_DISABLED_FINISHED:
        case FOREGROUND_DISABLED_INDETERMINATE:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_ENABLED_FINISHED:
        case FOREGROUND_ENABLED_INDETERMINATE:
            return ButtonType.ENABLED;
        }
        return null;
    }

    private void paintTrack(Graphics2D g, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = ShapeUtil.createRoundRectangle(2, 2, width - 5, height - 5, CornerSize.ROUND_HEIGHT_DRAW);
        if (state != Which.BACKGROUND_DISABLED) {
            dropShadow.fill(g, s);
        }
        ColorUtil.drawProgressBarBorderColors(g, s, type);

        s = ShapeUtil.createRoundRectangle(3, 3, width - 6, height - 6, CornerSize.ROUND_HEIGHT);
        ColorUtil.fillProgressBarTrackColors(g, s, type);

        s = ShapeUtil.createInternalDropShadowRounded(3, 3, width - 6, height - 8);
        internalShadow.fill(g, s, true, true);
    }

    private void paintBar(Graphics2D g, int width, int height, boolean isFinished) {
        Shape s = ShapeUtil.createRectangle(0, 0, width, height);
        ColorUtil.fillProgressBarColors(g, s, type);

        if (!isFinished) {
            s = ShapeUtil.createRectangle(width - 1, 0, 1, height);
            ColorUtil.fillProgressBarEndColors(g, s, type);
        }
    }

    private void paintIndeterminateBar(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createRectangle(0, 0, width, height);
        ColorUtil.fillProgressBarColors(g, s, type);
        s = ShapeUtil.createProgressBarIndeterminatePattern(0, 0, width, height);
        ColorUtil.fillProgressBarIndeterminateColors(g, s, type);
    }
}
