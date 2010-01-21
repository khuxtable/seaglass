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
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class RadioButtonMenuItemPainter extends MenuItemPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_SELECTED_MOUSEOVER,
        CHECKICON_DISABLED_SELECTED,
        CHECKICON_ENABLED_SELECTED,
        CHECKICON_SELECTED_MOUSEOVER,
    }

    private Which        state;
    private PaintContext ctx;

    private Ellipse2D    ellipse               = new Ellipse2D.Float();

    private Color        iconDisabledSelected  = decodeColor("nimbusDisabledText");
    private Color        iconEnabledSelected   = decodeColor("text");
    private Color        iconSelectedMouseOver = decodeColor("nimbusSelectedText");

    public RadioButtonMenuItemPainter(Which state) {
        super(MenuItemPainter.Which.BACKGROUND_ENABLED);
        this.state = state;
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_SELECTED_MOUSEOVER:
            this.ctx = new PaintContext(CacheMode.NO_CACHING);
            break;
        default:
            this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_MOUSEOVER:
            paintBackgroundMouseOver(g, width, height);
            break;
        case BACKGROUND_SELECTED_MOUSEOVER:
            paintBackgroundMouseOver(g, width, height);
            break;
        case CHECKICON_DISABLED_SELECTED:
            paintCheckIconDisabledAndSelected(g, width, height);
            break;
        case CHECKICON_ENABLED_SELECTED:
            paintCheckIconEnabledAndSelected(g, width, height);
            break;
        case CHECKICON_SELECTED_MOUSEOVER:
            paintCheckIconSelectedAndMouseOver(g, width, height);
            break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintCheckIconDisabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeRadioPath(width, height);
        g.setPaint(iconDisabledSelected);
        g.fill(s);
    }

    private void paintCheckIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeRadioPath(width, height);
        g.setPaint(iconEnabledSelected);
        g.fill(s);
    }

    private void paintCheckIconSelectedAndMouseOver(Graphics2D g, int width, int height) {
        Shape s = decodeRadioPath(width, height);
        g.setPaint(iconSelectedMouseOver);
        g.fill(s);
    }

    private Shape decodeRadioPath(int width, int height) {
        int size = Math.min(width, height);
        ellipse.setFrame(0, 1, size - 1, size - 1);
        return ellipse;
    }
}
