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
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's ScrollPanePainter.
 */
public final class ScrollPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BORDER_ENABLED, BORDER_ENABLED_FOCUSED, CORNER_ENABLED,
    }

    private Which        state;
    private PaintContext ctx;

    private Rectangle2D  rect         = new Rectangle2D.Double();
    private Path2D       path         = new Path2D.Float();

    private Color        borderColor  = decodeColor("nimbusBorder");
    private Color        focusColor   = decodeColor("nimbusFocus");

    private Color        cornerBorder = new Color(192, 192, 192);
    private Color        cornerColor1 = new Color(240, 240, 240);
    private Color        cornerColor2 = new Color(212, 212, 212);

    public ScrollPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BORDER_ENABLED:
            paintBorderEnabled(g, width, height);
            break;
        case BORDER_ENABLED_FOCUSED:
            paintBorderFocused(g, width, height);
            break;
        case CORNER_ENABLED:
            paintCornerEnabled(g, width, height);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBorderEnabled(Graphics2D g, int width, int height) {
        g.setPaint(borderColor);
        g.drawLine(3, 2, width - 4, 2);
        g.drawLine(2, 2, 2, height - 3);
        g.drawLine(width - 3, 2, width - 3, height - 3);
        g.drawLine(3, height - 3, width - 4, height - 3);
    }

    private void paintBorderFocused(Graphics2D g, int width, int height) {
        paintBorderEnabled(g, width, height);

        Shape s = decodeFocusPath(width, height);
        g.setPaint(focusColor);
        g.fill(s);
    }

    private void paintCornerEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeCornerBorder(width, height);
        g.setPaint(cornerBorder);
        g.fill(s);
        s = decodeCornerInside(width, height);
        g.setPaint(decodeCornerGradient(s));
        g.fill(s);
    }

    private Shape decodeFocusPath(int width, int height) {
        float left = 2;
        float top = 2;
        float right = width - 2;
        float bottom = height - 2;

        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);

        float left2 = 0.6f;
        float top2 = 0.6f;
        float right2 = width - 0.6f;
        float bottom2 = height - 0.6f;

        // TODO These two lines were curveTo in Nimbus. Perhaps we should
        // revisit this?
        path.lineTo(right2, top);
        path.lineTo(right2, bottom2);
        path.lineTo(left2, bottom2);
        path.lineTo(left2, top2);
        path.lineTo(right2, top2);
        path.lineTo(right2, top);
        path.closePath();

        return path;
    }

    private Shape decodeCornerBorder(int width, int height) {
        rect.setRect(0, 0, width, height);
        return rect;
    }

    private Shape decodeCornerInside(int width, int height) {
        rect.setRect(1, 1, width - 2, height - 2);
        return rect;
    }

    private Paint decodeCornerGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient(1, 1, w - 2, h - 2, new float[] { 0f, 1f }, new Color[] { cornerColor1, cornerColor2 });
    }
}
