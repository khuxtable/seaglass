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
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * PopupMenuPainter implementation.
 * 
 * Based on Nimbus's implementation.
 */
public final class PopupMenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Which        state;
    private PaintContext ctx;

    private Color        disabledBorderColor   = new Color(0x80dddddd, true);
    private Color        disabledInteriorColor = new Color(0x80ffffff, true);

    private Color        enabledBorderColor    = new Color(0xdddddd);
    private Color        enabledInteriorColor  = new Color(0xffffff);

    public PopupMenuPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, width, height);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g, int width, int height) {
        Shape s = decodeBorder(width, height);
        g.setPaint(disabledBorderColor);
        g.fill(s);
        s = decodeInterior(width, height);
        g.setPaint(disabledInteriorColor);
        g.fill(s);

    }

    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeBorder(width, height);
        g.setPaint(enabledBorderColor);
        g.fill(s);
        s = decodeInterior(width, height);
        g.setPaint(enabledInteriorColor);
        g.fill(s);

    }

    private Shape decodeBorder(int width, int height) {
        return ShapeUtil.createRoundRectangle(CornerSize.POPUP_BORDER, 0, 0, width, height);
    }

    private Shape decodeInterior(int width, int height) {
        return ShapeUtil.createRoundRectangle(CornerSize.POPUP_INTERIOR, 1, 1, width - 2, height - 2);
    }
}
