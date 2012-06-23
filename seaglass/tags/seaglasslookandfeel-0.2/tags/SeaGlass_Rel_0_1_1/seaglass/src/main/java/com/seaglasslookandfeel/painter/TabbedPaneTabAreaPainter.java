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
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Nimbus's TabbedPaneTabAreaPainter.
 */
public final class TabbedPaneTabAreaPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_DISABLED, BACKGROUND_ENABLED_MOUSEOVER, BACKGROUND_ENABLED_PRESSED,
    }

    private Which        state;
    private PaintContext ctx;

    private Rectangle2D  rect    = new Rectangle2D.Float(0, 0, 0, 0);

    private Color        color1  = new Color(255, 200, 0, 255);
    private Color        color2  = decodeColor("tabbedPaneTabBase", 0.08801502f, 0.3642857f, -0.4784314f, 0);
    private Color        color3  = decodeColor("tabbedPaneTabBase", 5.1498413E-4f, -0.45471883f, 0.31764704f, 0);
    private Color        color4  = decodeColor("tabbedPaneTabBase", 5.1498413E-4f, -0.4633005f, 0.3607843f, 0);
    private Color        color5  = decodeColor("tabbedPaneTabBase", 0.05468172f, -0.58308274f, 0.19607842f, 0);
    private Color        color6  = decodeColor("tabbedPaneTabBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color        color7  = decodeColor("tabbedPaneTabBase", 5.1498413E-4f, -0.4690476f, 0.39215684f, 0);
    private Color        color8  = decodeColor("tabbedPaneTabBase", 5.1498413E-4f, -0.47635174f, 0.4352941f, 0);
    private Color        color9  = decodeColor("tabbedPaneTabBase", 0.0f, -0.05401492f, 0.05098039f, 0);
    private Color        color10 = decodeColor("tabbedPaneTabBase", 0.0f, -0.09303135f, 0.09411764f, 0);

    public TabbedPaneTabAreaPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(new Insets(0, 5, 6, 5), new Dimension(5, 24), false, CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g);
            break;
        case BACKGROUND_ENABLED_MOUSEOVER:
            paintBackgroundEnabledAndMouseOver(g);
            break;
        case BACKGROUND_ENABLED_PRESSED:
            paintBackgroundEnabledAndPressed(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);

    }

    private void paintBackgroundDisabled(Graphics2D g) {
        rect = decodeRect2();
        g.setPaint(decodeGradient2(rect));
        g.fill(rect);

    }

    private void paintBackgroundEnabledAndMouseOver(Graphics2D g) {
        rect = decodeRect2();
        g.setPaint(decodeGradient3(rect));
        g.fill(rect);

    }

    private void paintBackgroundEnabledAndPressed(Graphics2D g) {
        rect = decodeRect2();
        g.setPaint(decodeGradient4(rect));
        g.fill(rect);

    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(0.0f), // x
            decodeY(1.0f), // y
            decodeX(0.0f) - decodeX(0.0f), // width
            decodeY(1.0f) - decodeY(1.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect2() {
        rect.setRect(decodeX(0.0f), // x
            decodeY(2.1666667f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(2.1666667f)); // height
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.08387097f,
            0.09677419f,
            0.10967742f,
            0.43709677f,
            0.7645161f,
            0.7758064f,
            0.7870968f }, new Color[] {
            color2,
            decodeColor(color2, color3, 0.5f),
            color3,
            decodeColor(color3, color4, 0.5f),
            color4,
            decodeColor(color4, color2, 0.5f),
            color2 });
    }

    private Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.08387097f,
            0.09677419f,
            0.10967742f,
            0.43709677f,
            0.7645161f,
            0.7758064f,
            0.7870968f }, new Color[] {
            color5,
            decodeColor(color5, color3, 0.5f),
            color3,
            decodeColor(color3, color4, 0.5f),
            color4,
            decodeColor(color4, color5, 0.5f),
            color5 });
    }

    private Paint decodeGradient3(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.08387097f,
            0.09677419f,
            0.10967742f,
            0.43709677f,
            0.7645161f,
            0.7758064f,
            0.7870968f }, new Color[] {
            color6,
            decodeColor(color6, color7, 0.5f),
            color7,
            decodeColor(color7, color8, 0.5f),
            color8,
            decodeColor(color8, color2, 0.5f),
            color2 });
    }

    private Paint decodeGradient4(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] {
            0.08387097f,
            0.09677419f,
            0.10967742f,
            0.43709677f,
            0.7645161f,
            0.7758064f,
            0.7870968f }, new Color[] {
            color2,
            decodeColor(color2, color9, 0.5f),
            color9,
            decodeColor(color9, color10, 0.5f),
            color10,
            decodeColor(color10, color2, 0.5f),
            color2 });
    }
}
