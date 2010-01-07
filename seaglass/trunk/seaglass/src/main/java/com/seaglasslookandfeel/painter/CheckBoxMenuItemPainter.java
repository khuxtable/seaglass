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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class CheckBoxMenuItemPainter extends AbstractRegionPainter {
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

    private Path2D       path                  = new Path2D.Float();
    private Rectangle2D  rect                  = new Rectangle2D.Float(0, 0, 0, 0);

    private Color        color1                = new Color(0x5873d9);
    private Color        color2                = new Color(0x1441a8);
    private Color        iconDisabledSelected  = decodeColor("nimbusBlueGrey", 0.0f, -0.08983666f, -0.17647058f, 0);
    private Color        iconEnabledSelected   = decodeColor("nimbusBlueGrey", 0.055555582f, -0.096827686f, -0.45882353f, 0);
    private Color        iconSelectedMouseOver = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    public CheckBoxMenuItemPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_SELECTED_MOUSEOVER:
            ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 3), false, CacheMode.NO_CACHING, 1.0, 1.0);
            break;
        default:
            ctx = new PaintContext(new Insets(5, 5, 5, 5), new Dimension(9, 10), false, CacheMode.FIXED_SIZES, 1.0, 1.0);
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
            paintcheckIconDisabledAndSelected(g);
            break;
        case CHECKICON_ENABLED_SELECTED:
            paintcheckIconEnabledAndSelected(g);
            break;
        case CHECKICON_SELECTED_MOUSEOVER:
            paintcheckIconSelectedAndMouseOver(g);
            break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundMouseOver(Graphics2D g, int width, int height) {
        Shape s = decodeBackground(width, height);
        g.setPaint(decodeSelectedGradient(s, width, height));
        g.fill(s);

    }

    private void paintcheckIconDisabledAndSelected(Graphics2D g) {
        Shape s = decodeIconPath();
        g.setPaint(iconDisabledSelected);
        g.fill(s);

    }

    private void paintcheckIconEnabledAndSelected(Graphics2D g) {
        Shape s = decodeIconPath();
        g.setPaint(iconEnabledSelected);
        g.fill(s);

    }

    private void paintcheckIconSelectedAndMouseOver(Graphics2D g) {
        Shape s = decodeIconPath();
        g.setPaint(iconSelectedMouseOver);
        g.fill(s);

    }

    private Shape decodeBackground(int width, int height) {
        rect.setRect(0, 0, width, height);
        return rect;
    }

    private Shape decodeIconPath() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(1.5f));
        path.lineTo(decodeX(0.4292683f), decodeY(1.5f));
        path.lineTo(decodeX(0.7121951f), decodeY(2.4780488f));
        path.lineTo(decodeX(2.5926828f), decodeY(0.0f));
        path.lineTo(decodeX(3.0f), decodeY(0.0f));
        path.lineTo(decodeX(3.0f), decodeY(0.2f));
        path.lineTo(decodeX(2.8317075f), decodeY(0.39512196f));
        path.lineTo(decodeX(0.8f), decodeY(3.0f));
        path.lineTo(decodeX(0.5731707f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(1.5f));
        path.closePath();
        return path;
    }

    private Paint decodeSelectedGradient(Shape s, int width, int height) {
        float midX = width / 2.0f;
        return decodeGradient(midX, 0, midX, height, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }
}
