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

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class MenuBarMenuPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SELECTED,
    };

    private Which        state;
    private PaintContext ctx;

    private Color        backgroundColor = decodeColor("menu");

    public MenuBarMenuPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_SELECTED:
            paintBackgroundSelected(g, width, height);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundSelected(Graphics2D g, int width, int height) {
        g.setPaint(backgroundColor);
        g.fillRect(0, 0, width, height);
    }
}
