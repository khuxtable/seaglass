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
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Paint table headers.
 */
public final class TableHeaderRendererPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_ENABLED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SORTED,
        BACKGROUND_ENABLED_FOCUSED_SORTED,
        BACKGROUND_DISABLED_SORTED,
    }

    private Which              state;
    private PaintContext       ctx;

    private static final Color border          = new Color(0x88ade0);
    private static final Color disabledBorder  = new Color(0x8088ade0, true);

    private static final Color disabled1       = new Color(0x80fbfdfe, true);
    private static final Color disabled2       = new Color(0x80d6eaf9, true);
    private static final Color disabled3       = new Color(0x80d2e8f8, true);
    private static final Color disabled4       = new Color(0x80f5fafd, true);

    private static final Color enabled1        = new Color(0xfbfdfe);
    private static final Color enabled2        = new Color(0xd6eaf9);
    private static final Color enabled3        = new Color(0xd2e8f8);
    private static final Color enabled4        = new Color(0xf5fafd);

    private static final Color sorted1         = new Color(0xbccedf);
    private static final Color sorted2         = new Color(0x7fa7cd);
    private static final Color sorted3         = new Color(0x82b0d6);
    private static final Color sorted4         = new Color(0xb0daf6);

    private static final Color pressed1        = new Color(0xacbdd0);
    private static final Color pressed2        = new Color(0x688db3);
    private static final Color pressed3        = new Color(0x6d93ba);
    private static final Color pressed4        = new Color(0xa4cbe4);

    private static final Color disabledSorted1 = new Color(0x80bccedf, true);
    private static final Color disabledSorted2 = new Color(0x807fa7cd, true);
    private static final Color disabledSorted3 = new Color(0x8082b0d6, true);
    private static final Color disabledSorted4 = new Color(0x80b0daf6, true);

    private Rectangle          rectangle       = new Rectangle();

    public TableHeaderRendererPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(new Insets(1, 0, 1, 1), new Dimension(26, 16), false, CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintDisabled(g, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintEnabled(g, width, height);
            break;
        case BACKGROUND_ENABLED_FOCUSED:
            paintEnabled(g, width, height);
            break;
        case BACKGROUND_PRESSED:
            paintPressed(g, width, height);
            break;
        case BACKGROUND_ENABLED_SORTED:
            paintSorted(g, width, height);
            break;
        case BACKGROUND_ENABLED_FOCUSED_SORTED:
            paintSorted(g, width, height);
            break;
        case BACKGROUND_DISABLED_SORTED:
            paintDisabledSorted(g, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, int width, int height) {
        paintBackground(g, width, height, disabledBorder, disabled1, disabled2, disabled3, disabled4);
    }

    private void paintEnabled(Graphics2D g, int width, int height) {
        paintBackground(g, width, height, border, enabled1, enabled2, enabled3, enabled4);
    }

    private void paintPressed(Graphics2D g, int width, int height) {
        paintBackground(g, width, height, border, pressed1, pressed2, pressed3, pressed4);
    }

    private void paintSorted(Graphics2D g, int width, int height) {
        paintBackground(g, width, height, border, sorted1, sorted2, sorted3, sorted4);
    }

    private void paintDisabledSorted(Graphics2D g, int width, int height) {
        paintBackground(g, width, height, disabledBorder, disabledSorted1, disabledSorted2, disabledSorted3, disabledSorted4);
    }

    private void paintBackground(Graphics2D g, int width, int height, Color border, Color color1, Color color2, Color color3, Color color4) {
        rectangle.setRect(0, 0, width - 1, height - 1);
        Paint p = getGradient(rectangle, color1, color2, color3, color4);
        g.setPaint(p);
        g.fill(rectangle);
        g.setColor(border);
        g.drawLine(0, height - 1, width, height - 1);
        g.drawLine(width - 1, 0, width - 1, height - 1);
    }

    private Paint getGradient(Rectangle r, Color color1, Color color2, Color color3, Color color4) {
        float x = (float) r.getX();
        float y = (float) r.getY();
        float w = (float) r.getWidth();
        float h = (float) r.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 0.45f, 0.6f, 1f }, new Color[] {
            color1,
            color2,
            color3,
            color4 });
    }
}
