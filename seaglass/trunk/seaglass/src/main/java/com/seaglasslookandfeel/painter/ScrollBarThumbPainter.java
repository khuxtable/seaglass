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

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ColorUtil.TwoLayerFourColors;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_PRESSED,
    }

    private PaintContext       ctx;
    private TwoLayerFourColors colors;

    public ScrollBarThumbPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        colors = ColorUtil.getScrollBarThumbColors(getButtonType(state));
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = ShapeUtil.createRoundRectangle(0, 0, width, height, CornerSize.ROUND_HEIGHT);
        ColorUtil.fillTwoColorGradientVertical(g, s, colors.background);

        s = ShapeUtil.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.ROUND_HEIGHT);
        ColorUtil.fillTwoLayerFourColorGradientVertical(g, s, colors);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
            return ButtonType.ENABLED;
        case BACKGROUND_PRESSED:
            return ButtonType.PRESSED;
        }
        return null;
    }
}
