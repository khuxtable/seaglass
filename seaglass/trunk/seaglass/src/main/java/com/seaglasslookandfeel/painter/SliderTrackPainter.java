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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * Nimbus's SliderTrackPainter.
 */
public final class SliderTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Color        sliderTrackBorderBase       = decodeColor("sliderTrackBorderBase");
    private Color        sliderTrackInteriorBase     = decodeColor("sliderTrackInteriorBase");

    private TwoColors    sliderTrackBorderEnabled    = new TwoColors(deriveColor(sliderTrackBorderBase, 0f, 0f, -0.149020f, 0),
                                                         deriveColor(sliderTrackBorderBase, 0f, 0f, 0.145098f, 0));
    private TwoColors    sliderTrackInteriorEnabled  = new TwoColors(deriveColor(sliderTrackInteriorBase, 0f, 0f, -0.078431f, 0),
                                                         deriveColor(sliderTrackInteriorBase, 0f, 0f, 0.074510f, 0));
    private TwoColors    sliderTrackInteriorDisabled = disable(sliderTrackInteriorEnabled);
    private TwoColors    sliderTrackBorderDisabled   = desaturate(sliderTrackBorderEnabled);

    private PaintContext ctx;
    private CommonControlType   type;

    public SliderTrackPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
        this.type = getButtonType(state);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = ShapeUtil.createRoundRectangle(0, 0, width, height, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSliderTrackBorderPaint(s, type));
        g.fill(s);
        s = ShapeUtil.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSliderTrackInteriorPaint(s, type));
        g.fill(s);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED:
            return CommonControlType.ENABLED;
        }
        return null;
    }

    public Paint getSliderTrackBorderPaint(Shape s, CommonControlType type) {
        TwoColors colors = getSliderTrackBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getSliderTrackInteriorPaint(Shape s, CommonControlType type) {
        TwoColors colors = getSliderTrackInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    private TwoColors getSliderTrackBorderColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackBorderDisabled;
        case ENABLED:
            return sliderTrackBorderEnabled;
        }
        return null;
    }

    private TwoColors getSliderTrackInteriorColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return sliderTrackInteriorDisabled;
        case ENABLED:
            return sliderTrackInteriorEnabled;
        }
        return null;
    }
}
