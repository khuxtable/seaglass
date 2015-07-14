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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.SeaGlassInternalShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;

/**
 * ComboBoxTextFieldPainter implementation.
 */
public final class SpinnerFormattedTextFieldPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED, BACKGROUND_FOCUSED, BACKGROUND_SELECTED_FOCUSED,
    }

    private SeaGlassInternalShadowEffect internalShadow = new SeaGlassInternalShadowEffect();

    private PaintContext                 ctx;
    private CommonControlState            type;
    private boolean                      focused;

    public SpinnerFormattedTextFieldPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = (state == Which.BACKGROUND_DISABLED) ? CommonControlState.DISABLED : CommonControlState.ENABLED;
        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_SELECTED_FOCUSED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        boolean useFocusColors = isInToolBar(c);
        Shape s;
        if (focused) {
            s = shapeGenerator.createRoundRectangle(0, 0, width , height, 
                CornerSize.OUTER_FOCUS, 
                CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.SQUARE, CornerStyle.SQUARE);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useFocusColors));
            g.fill(s);

            s = shapeGenerator.createRoundRectangle(1, 1, width - 1, height - 2, 
                CornerSize.INNER_FOCUS, 
                CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.SQUARE, CornerStyle.SQUARE);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useFocusColors));
            g.fill(s);
        }

        g.setPaint(c.getBackground());
        s = shapeGenerator.createRoundRectangle(3, 3, width - 3, height - 6, 
            CornerSize.BORDER, 
            CornerStyle.ROUNDED, CornerStyle.ROUNDED,
            CornerStyle.SQUARE, CornerStyle.SQUARE);
        g.fill(s);

        if (type != CommonControlState.DISABLED) {
            s = shapeGenerator.createRoundRectangle(3, 3, width - 3, height - 6, 
                CornerSize.BORDER, 
                CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.SQUARE, CornerStyle.SQUARE);
            internalShadow.fill(g, s, false, false);
        }
        
        s = shapeGenerator.createRoundRectangle(2, 2, width - 2, height - 4 - 1, 
            CornerSize.BORDER, 
            CornerStyle.ROUNDED, CornerStyle.ROUNDED,
            CornerStyle.SQUARE, CornerStyle.SQUARE);

        g.setPaint(getTextBorderPaint(type, false));
        g.draw(s);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
