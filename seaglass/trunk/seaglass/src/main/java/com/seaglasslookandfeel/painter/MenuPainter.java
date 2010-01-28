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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

public final class MenuPainter extends MenuItemPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_SELECTED,
        ARROWICON_DISABLED,
        ARROWICON_ENABLED,
        ARROWICON_ENABLED_SELECTED,
    };

    private Which        state;
    private PaintContext ctx;

    private Color        iconDisabledSelected  = decodeColor("nimbusDisabledText");
    private Color        iconEnabledSelected   = decodeColor("text");
    private Color        iconSelectedMouseOver = decodeColor("nimbusSelectedText");

    public MenuPainter(Which state) {
        super(MenuItemPainter.Which.BACKGROUND_ENABLED);
        this.state = state;
        switch (state) {
        case BACKGROUND_ENABLED_SELECTED:
            this.ctx = new PaintContext(CacheMode.NO_CACHING);
            break;
        case ARROWICON_DISABLED:
        case ARROWICON_ENABLED:
        case ARROWICON_ENABLED_SELECTED:
            this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED_SELECTED:
            paintBackgroundMouseOver(g, width, height);
            break;
        case ARROWICON_DISABLED:
            paintArrowIconDisabled(g, width, height);
            break;
        case ARROWICON_ENABLED:
            paintArrowIconEnabled(g, width, height);
            break;
        case ARROWICON_ENABLED_SELECTED:
            paintArrowIconEnabledAndSelected(g, width, height);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintArrowIconDisabled(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);
        g.setPaint(iconDisabledSelected);
        g.fill(s);
    }

    private void paintArrowIconEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);
        g.setPaint(iconEnabledSelected);
        g.fill(s);
    }

    private void paintArrowIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeArrowPath(width, height);
        g.setPaint(iconSelectedMouseOver);
        g.fill(s);
    }

    private Shape decodeArrowPath(int width, int height) {
        return ShapeUtil.createArrowRight(1, 1, width - 2, height - 2);
    }
}
