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

public final class TreePainter extends AbstractRegionPainter {
    public enum Which {
        COLLAPSEDICON_ENABLED, COLLAPSEDICON_ENABLED_SELECTED, EXPANDEDICON_ENABLED, EXPANDEDICON_ENABLED_SELECTED,
    }

    private Which        state;
    private PaintContext ctx;

    private Color        selectedColor = decodeColor("seaGlassBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color        enabledColor  = decodeColor("seaGlassBlueGrey", -0.6111111f, -0.110526316f, -0.34509805f, 0);

    public TreePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case COLLAPSEDICON_ENABLED:
            paintCollapsedIconEnabled(g, width, height);
            break;
        case COLLAPSEDICON_ENABLED_SELECTED:
            paintCollapsedIconEnabledAndSelected(g, width, height);
            break;
        case EXPANDEDICON_ENABLED:
            paintExpandedIconEnabled(g, width, height);
            break;
        case EXPANDEDICON_ENABLED_SELECTED:
            paintExpandedIconEnabledAndSelected(g, width, height);
            break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintCollapsedIconEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeCollapsedPath(width, height);
        g.setPaint(enabledColor);
        g.fill(s);
    }

    private void paintCollapsedIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeCollapsedPath(width, height);
        g.setPaint(selectedColor);
        g.fill(s);
    }

    private void paintExpandedIconEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeExpandedPath(width, height);
        g.setPaint(enabledColor);
        g.fill(s);
    }

    private void paintExpandedIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeExpandedPath(width, height);
        g.setPaint(selectedColor);
        g.fill(s);
    }

    private Shape decodeCollapsedPath(int width, int height) {
        return shapeGenerator.createArrowRight(0, 0, width, height);
    }

    private Shape decodeExpandedPath(int width, int height) {
        return shapeGenerator.createArrowDown(0, 0, width, height);
    }
}
