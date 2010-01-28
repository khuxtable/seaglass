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
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

/**
 * ScrollBarTrackPainter implementation.
 */
public final class ScrollBarTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED,
    }

    private static final Color trackBackground1 = new Color(0xeeeeee);
    private static final Color trackBackground2 = new Color(0xffffff);

    private static final Color trackInner1      = new Color(0x33000000, true);
    private static final Color trackInner2      = new Color(0x15000000, true);
    private static final Color trackInner3      = new Color(0x00000000, true);
    private static final Color trackInner4      = new Color(0x12000000, true);

    private Rectangle2D        rect             = new Rectangle2D.Double();

    private PaintContext       ctx;

    public ScrollBarTrackPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        paintBackgroundTrack(g, width, height);
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundTrack(Graphics2D g, int width, int height) {
        Shape s = ShapeUtil.createRectangle(0, 0, width, height);
        g.setPaint(decodeGradientTrack(s));
        g.fill(s);
        g.setPaint(decodeGradientTrackInnerShadow(s));
        g.fill(s);
    }

    private Paint decodeGradientTrack(Shape s) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return decodeGradient(width * 0.5f, 0, width * 0.5f, height - 1, new float[] { 0f, 1f }, new Color[] {
            trackBackground1,
            trackBackground2 });
    }

    private Paint decodeGradientTrackInnerShadow(Shape s) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return decodeGradient(width * 0.5f, 0, width * 0.5f, height - 1, new float[] { 0f, 0.142857143f, 0.5f, 0.785714286f, 1f },
            new Color[] { trackInner1, trackInner2, trackInner3, trackInner3, trackInner4 });
    }
}
