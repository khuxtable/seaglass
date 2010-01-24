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
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ScrollBarTrackPainter implementation.
 */
public final class ScrollBarTrackPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_DISABLED_TOGETHER, BACKGROUND_ENABLED_TOGETHER,
    }

    private static final Color             trackBackground1 = new Color(0xeeeeee);
    private static final Color             trackBackground2 = new Color(0xffffff);

    private static final Color             trackInner1      = new Color(0x33000000, true);
    private static final Color             trackInner2      = new Color(0x15000000, true);
    private static final Color             trackInner3      = new Color(0x00000000, true);
    private static final Color             trackInner4      = new Color(0x12000000, true);

    private static final ButtonStateColors enabledDecrease  = new ButtonStateColors(new Color(0xffffff), new Color(0xbbbbbb), new Color(
                                                                0xbdbdbd));

    private final Color                    colorShadow      = new Color(0x000000);
    private Effect                         dropShadow       = new ScrollButtonDropShadowEffect();

    private Path2D                         path             = new Path2D.Double();
    private Rectangle2D                    rect             = new Rectangle2D.Double();

    private Which                          state;
    private PaintContext                   ctx;

    public ScrollBarTrackPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        paintBackgroundTrack(g, width, height);
        if (state == Which.BACKGROUND_DISABLED_TOGETHER || state == Which.BACKGROUND_ENABLED_TOGETHER) {
            paintLeftCap(g, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundTrack(Graphics2D g, int width, int height) {
        Shape s = decodeTrackBackground(width, height);
        g.setPaint(decodeGradientTrack(s));
        g.fill(s);
        g.setPaint(decodeGradientTrackInnerShadow(s));
        g.fill(s);
    }

    private void paintLeftCap(Graphics2D g, int width, int height) {
        Shape s = decodeTopCapPath(width, height);
        dropShadow.fill(g, s, colorShadow);
        g.setPaint(decodeTopCapGradient(s, enabledDecrease.top, enabledDecrease.bottom));
        g.fill(s);
        g.setColor(enabledDecrease.line);
        g.drawLine(0, 0, width - 1, 0);
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

    private Paint decodeTopCapGradient(Shape s, Color top, Color bottom) {
        Rectangle bounds = s.getBounds();
        int width = bounds.width;
        int height = bounds.height;
        return decodeGradient(0, height / 2, width - 1, height / 2, new float[] { 0f, 1f }, new Color[] { top, bottom });
    }

    private Shape decodeTrackBackground(int width, int height) {
        rect.setRect(0, 0, width, height);
        return rect;
    }

    private Shape decodeTopCapPath(int width, int height) {
        width = 9;
        height = 15;
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width, height);
        path.quadTo(width - height / 3.0, height, width - height / 3.0, height / 2.0);
        path.quadTo(width - height / 3.0, 0, width, 0);
        path.closePath();
        return path;
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    private static class ScrollButtonDropShadowEffect extends DropShadowEffect {

        public ScrollButtonDropShadowEffect() {
            color = new Color(150, 150, 150);
            angle = 0;
            distance = 0;
            size = 3;
            opacity = 0.25f;
        }
    }

    private static class ButtonStateColors {
        public Color top;
        public Color bottom;
        public Color line;

        public ButtonStateColors(Color top, Color bottom, Color line) {
            this.top = top;
            this.bottom = bottom;
            this.line = line;
        }
    }
}
