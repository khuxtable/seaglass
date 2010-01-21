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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's SplitPaneDividerPainter.
 */
public final class SplitPaneDividerPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        FOREGROUND_ENABLED,
        FOREGROUND_ENABLED_VERTICAL,
        FOREGROUND_FOCUSED,
        FOREGROUND_FOCUSED_VERTICAL,
    }

    private Which              state;
    private PaintContext       ctx;

    private RoundRectangle2D   roundRect         = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color              bgOuter           = new Color(0xd9d9d9);
    private Color              bgEnabled         = decodeColor("control", 0f, 0f, 0f, 0);

    private static final Color OUTER_FOCUS_COLOR = new Color(0x8072a5d2, true);
    private static final Color INNER_FOCUS_COLOR = new Color(0x73a4d1);

    private Color              fgBorder1         = new Color(0x88ade0);
    private Color              fgBorder2         = new Color(0x5785bf);

    private Color              fgInside1         = new Color(0xfbfdfe);
    private Color              fgInside2         = new Color(0xd2e8f8);
    private Color              fgInside3         = new Color(0xf5fafd);

    public SplitPaneDividerPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, width, height);
            break;
        case BACKGROUND_FOCUSED:
            paintBackgroundFocused(g, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintForegroundEnabled(g, width, height);
            break;
        case FOREGROUND_ENABLED_VERTICAL:
            paintForegroundEnabledAndVertical(g, width, height);
            break;
        case FOREGROUND_FOCUSED:
            paintForegroundFocused(g, width, height);
            break;
        case FOREGROUND_FOCUSED_VERTICAL:
            paintForegroundFocusedAndVertical(g, width, height);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        g.setPaint(bgEnabled);
        g.fillRect(0, 0, width, height);
        g.setPaint(bgOuter);
        int y = height / 2;
        g.drawLine(0, y, width - 1, y);
    }

    private void paintBackgroundFocused(Graphics2D g, int width, int height) {
        int y = height / 2;

        g.setPaint(bgEnabled);
        g.fillRect(0, 0, width, height);

        g.setPaint(OUTER_FOCUS_COLOR);
        g.fillRect(0, y - 1, width, 3);

        g.setPaint(INNER_FOCUS_COLOR);
        g.drawLine(0, y, width - 1, y);
    }

    private void paintForegroundEnabledAndVertical(Graphics2D g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, height);
        g2.rotate(Math.toRadians(-90));

        paintForegroundEnabled(g2, height, width);
    }

    private void paintForegroundFocusedAndVertical(Graphics2D g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, height);
        g2.rotate(Math.toRadians(-90));

        paintForegroundFocused(g2, height, width);
    }

    private void paintForegroundEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeForegroundBorder(width, height);
        g.setPaint(decodeGradientForegroundBorder(s, fgBorder1, fgBorder2));
        g.fill(s);
        s = decodeForegroundInside(width, height);
        g.setPaint(decodeGradientForegroundInside(s, fgInside1, fgInside2, fgInside3));
        g.fill(s);
    }

    private void paintForegroundFocused(Graphics2D g, int width, int height) {
        Shape s = decodeForegroundOuterFocus(width, height);
        g.setPaint(OUTER_FOCUS_COLOR);
        g.fill(s);
        s = decodeForegroundInnerFocus(width, height);
        g.setPaint(INNER_FOCUS_COLOR);
        g.fill(s);
        s = decodeForegroundBorder(width, height);
        g.setPaint(decodeGradientForegroundBorder(s, fgBorder1, fgBorder2));
        g.fill(s);
        s = decodeForegroundInside(width, height);
        g.setPaint(decodeGradientForegroundInside(s, fgInside1, fgInside2, fgInside3));
        g.fill(s);
    }

    private Shape decodeForegroundOuterFocus(int width, int height) {
        double x = width / 2 - 10;
        double y = height / 2 - 5;
        roundRect.setRoundRect(x - 1, y + 1, 22, 9, 9f, 9f);
        return roundRect;
    }

    private Shape decodeForegroundInnerFocus(int width, int height) {
        double x = width / 2 - 10;
        double y = height / 2 - 5;
        roundRect.setRoundRect(x, y + 2, 20, 7, 7f, 7f);
        return roundRect;
    }

    private Shape decodeForegroundBorder(int width, int height) {
        double x = width / 2 - 10;
        double y = height / 2 - 5;
        roundRect.setRoundRect(x + 1, y + 3, 18, 5, 5f, 5f);
        return roundRect;
    }

    private Shape decodeForegroundInside(int width, int height) {
        double x = width / 2 - 10;
        double y = height / 2 - 5;
        roundRect.setRoundRect(x + 2, y + 4, 16, 3, 3f, 3f);
        return roundRect;
    }

    private Paint decodeGradientForegroundBorder(Shape s, Color border1, Color border2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
            new float[] { 0.20645161f, 0.5f, 0.7935484f }, new Color[] { border1, decodeColor(border1, border2, 0.5f), border2 });
    }

    private Paint decodeGradientForegroundInside(Shape s, Color inside1, Color inside2, Color inside3) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.090322584f,
            0.2951613f,
            0.5f,
            0.5822581f,
            0.66451615f }, new Color[] {
            inside1,
            decodeColor(inside1, inside2, 0.5f),
            inside2,
            decodeColor(inside2, inside3, 0.5f),
            inside3 });
    }
}
