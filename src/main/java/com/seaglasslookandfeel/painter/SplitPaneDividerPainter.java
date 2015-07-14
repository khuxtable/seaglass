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

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Nimbus's SplitPaneDividerPainter.
 */
public final class SplitPaneDividerPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        FOREGROUND_ENABLED,
        FOREGROUND_ENABLED_VERTICAL,
        FOREGROUND_FOCUSED,
        FOREGROUND_FOCUSED_VERTICAL,
    }

    private Color        splitPaneDividerBackgroundEnabled = decodeColor("control");
    private Color        splitPaneDividerBackgroundOuter   = decodeColor("splitPaneDividerBackgroundOuter");

    private FourColors   button                            = getCommonInteriorColors(CommonControlState.ENABLED);

    private TwoColors    splitPaneDividerBorder            = getCommonBorderColors(CommonControlState.ENABLED);
    private ThreeColors  splitPaneDividerInterior          = new ThreeColors(button.top, button.lowerMid, button.bottom);

    private Which        state;
    private PaintContext ctx;

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
            paintBackgroundFocused(g, c, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintForegroundEnabled(g, width, height);
            break;
        case FOREGROUND_ENABLED_VERTICAL:
            paintForegroundEnabledAndVertical(g, width, height);
            break;
        case FOREGROUND_FOCUSED:
            paintForegroundFocused(g, c, width, height);
            break;
        case FOREGROUND_FOCUSED_VERTICAL:
            paintForegroundFocusedAndVertical(g, c, width, height);
            break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g, int width, int height) {
        g.setPaint(getSplitPaneDividerBackgroundPaint());
        g.fillRect(0, 0, width, height);
        g.setPaint(getSplitPaneDividerBackgroundOuterPaint());
        int y = height / 2;
        g.drawLine(0, y, width - 1, y);
    }

    private void paintBackgroundFocused(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        int y = height / 2;

        g.setPaint(getSplitPaneDividerBackgroundPaint());
        g.fillRect(0, 0, width, height);

        Shape s = shapeGenerator.createRectangle(0, y - 1, width, 3);
        g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
        g.fill(s);

        s = shapeGenerator.createRectangle(0, y, width, 1);
        g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
        g.fill(s);
    }

    private void paintForegroundEnabledAndVertical(Graphics2D g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, height);
        g2.rotate(Math.toRadians(-90));

        paintForegroundEnabled(g2, height, width);
    }

    private void paintForegroundFocusedAndVertical(Graphics2D g, JComponent c, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, height);
        g2.rotate(Math.toRadians(-90));

        paintForegroundFocused(g2, c, height, width);
    }

    private void paintForegroundFocused(Graphics2D g, JComponent c, int width, int height) {
        paintFocus(g, c, width, height);
        paintForegroundEnabled(g, width, height);
    }

    private void paintForegroundEnabled(Graphics2D g, int width, int height) {
        Shape s = shapeGenerator.createRoundRectangle(width / 2 - 9, height / 2 - 2, 18, 5, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSplitPaneDividerBorderPaint(s));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(width / 2 - 8, height / 2 - 1, 16, 3, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSplitPaneDividerInteriorPaint(s));
        g.fill(s);
    }

    private void paintFocus(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);

        Shape s = shapeGenerator.createRoundRectangle(width / 2 - 11, height / 2 - 4, 22, 9, CornerSize.ROUND_HEIGHT);
        g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(width / 2 - 10, height / 2 - 3, 20, 7, CornerSize.ROUND_HEIGHT);
        g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
        g.fill(s);
    }

    public Paint getSplitPaneDividerBackgroundPaint() {
        return splitPaneDividerBackgroundEnabled;
    }

    public Paint getSplitPaneDividerBackgroundOuterPaint() {
        return splitPaneDividerBackgroundOuter;
    }

    public Paint getSplitPaneDividerBorderPaint(Shape s) {
        return decodeSplitPaneDividerBorderGradient(s, splitPaneDividerBorder.top, splitPaneDividerBorder.bottom);
    }

    public Paint getSplitPaneDividerInteriorPaint(Shape s) {
        return decodeSplitPaneDividerInsideGradient(s, splitPaneDividerInterior.top, splitPaneDividerInterior.mid,
            splitPaneDividerInterior.bottom);
    }

    private Paint decodeSplitPaneDividerBorderGradient(Shape s, Color border1, Color border2) {
        Rectangle2D bounds = s.getBounds2D();
        float midX = (float) bounds.getCenterX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return createGradient(midX, y, midX, y + h, new float[] { 0.20645161f, 0.5f, 0.7935484f }, new Color[] {
            border1,
            decodeColor(border1, border2, 0.5f),
            border2 });
    }

    private Paint decodeSplitPaneDividerInsideGradient(Shape s, Color inside1, Color inside2, Color inside3) {
        Rectangle2D bounds = s.getBounds2D();
        float midX = (float) bounds.getCenterX();
        float y = (float) bounds.getY();
        float h = (float) bounds.getHeight();
        return createGradient(midX, y, midX, y + h, new float[] { 0.090322584f, 0.2951613f, 0.5f, 0.5822581f, 0.66451615f }, new Color[] {
            inside1,
            decodeColor(inside1, inside2, 0.5f),
            inside2,
            decodeColor(inside2, inside3, 0.5f),
            inside3 });
    }
}
