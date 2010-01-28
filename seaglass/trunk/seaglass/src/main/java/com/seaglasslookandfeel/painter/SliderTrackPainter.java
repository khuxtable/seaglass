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
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

/**
 * Nimbus's SliderTrackPainter.
 */
public final class SliderTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Which        state;
    private PaintContext ctx;

    private Color        disabledBorderTop      = new Color(0x80909090, true);
    private Color        disabledBorderBottom   = new Color(0x80b4b4b4, true);
    private Color        disabledInteriorTop    = new Color(0x80c4c4c4, true);
    private Color        disabledInteriorBottom = new Color(0x80ebebeb, true);

    private Color        enabledBorderTop       = new Color(0x636363);
    private Color        enabledBorderBottom    = new Color(0xaeaeae);
    private Color        enabledInteriorTop     = new Color(0xc4c4c4);
    private Color        enabledInteriorBottom  = new Color(0xebebeb);

    public SliderTrackPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
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
        g.setPaint(decodeTrackGradient(s, disabledBorderTop, disabledBorderBottom));
        g.fill(s);
        s = decodeInterior(width, height);
        g.setPaint(decodeTrackGradient(s, disabledInteriorTop, disabledInteriorBottom));
        g.fill(s);
    }

    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeBorder(width, height);
        g.setPaint(decodeTrackGradient(s, enabledBorderTop, enabledBorderBottom));
        g.fill(s);
        s = decodeInterior(width, height);
        g.setPaint(decodeTrackGradient(s, enabledInteriorTop, enabledInteriorBottom));
        g.fill(s);
    }

    private Paint decodeTrackGradient(Shape s, Color color1, Color color2) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        int y1 = r.y;
        int y2 = y1 + r.height;
        return decodeGradient(x, y1, x, y2, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    private Shape decodeBorder(int width, int height) {
        return ShapeUtil.createRoundRectangle(0, 0, width, height, height / 2.0);
    }

    private Shape decodeInterior(int width, int height) {
        return ShapeUtil.createRoundRectangle(1, 1, width - 2, height - 2, height / 2.0 - 1);
    }
}
