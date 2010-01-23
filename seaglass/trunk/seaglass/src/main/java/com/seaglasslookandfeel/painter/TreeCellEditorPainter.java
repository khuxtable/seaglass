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
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class TreeCellEditorPainter extends AbstractRegionPainter {
    public enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_FOCUSED,
    }

    private Which        state;
    private PaintContext ctx;

    private Path2D       path   = new Path2D.Float();

    private Color        color1 = decodeColor("nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
    private Color        color2 = decodeColor("nimbusFocus", 0.0f, 0.0f, 0.0f, 0);

    public TreeCellEditorPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, width, height);
            break;
        case BACKGROUND_ENABLED_FOCUSED:
            paintBackgroundEnabledAndFocused(g, width, height);
            break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        path = decodePath1(width, height);
        g.setPaint(color1);
        g.fill(path);
    }

    private void paintBackgroundEnabledAndFocused(Graphics2D g, int width, int height) {
        path = decodePath2(width, height);
        g.setPaint(color2);
        g.fill(path);
    }

    private Path2D decodePath1(int width, int height) {
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        path.lineTo(1, 0);
        path.lineTo(1, 1);
        path.lineTo(width - 1, 1);
        path.lineTo(width - 1, height - 1);
        path.lineTo(1, height - 1);
        path.lineTo(1, 0);
        path.lineTo(0, 0);
        path.closePath();
        return path;
    }

    private Path2D decodePath2(int width, int height) {
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        path.lineTo(1.2f, 0);
        path.lineTo(1.2f, 1.2f);
        path.lineTo(width - 1.2f, 1.2f);
        path.lineTo(width - 1.2f, height - 1.2f);
        path.lineTo(1.2f, height - 1.2f);
        path.lineTo(1.2f, 0);
        path.lineTo(0, 0);
        path.closePath();
        return path;
    }
}
