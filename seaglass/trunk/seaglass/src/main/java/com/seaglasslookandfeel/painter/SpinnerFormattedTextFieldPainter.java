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
            s = shapeGenerator.createRectangle(0, 0, width, height);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useFocusColors));
            g.fill(s);
            s = shapeGenerator.createRectangle(1, 1, width - 1, height - 2);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useFocusColors));
            g.fill(s);
        }

        g.setPaint(c.getBackground());
        g.fillRect(3, 3, width - 3, height - 6);

        s = shapeGenerator.createRectangle(3, 3, width - 3, height - 6);
        internalShadow.fill(g, s, false, false);

        s = shapeGenerator.createOpenRectangle(2, 2, width - 2 - 1, height - 4 - 1);
        g.setPaint(getTextBorderPaint(type, false));
        g.draw(s);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
}
