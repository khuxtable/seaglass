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
package com.seaglasslookandfeel.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.UIManager;

import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.PaintUtil.TwoColors;

public class SeaGlassInternalShadowEffect {

    private Color     transparentColor = UIManager.getColor("seaGlassTransparent");
    private Color     innerShadowBase  = UIManager.getColor("seaGlassInnerShadow");

    private Color     innerShadowLight = new Color(innerShadowBase.getRed(), innerShadowBase.getGreen(), innerShadowBase.getBlue(),
                                           innerShadowBase.getAlpha() / 2);
    private TwoColors innerShadow      = new TwoColors(innerShadowBase, innerShadowLight);

    public void fill(Graphics2D g, Shape s, boolean isRounded, boolean paintRightShadow) {
        if (isRounded) {
            fillInternalShadowRounded(g, s);
        } else {
            fillInternalShadow(g, s, paintRightShadow);
        }
    }

    private void fillInternalShadow(Graphics2D g, Shape s, boolean paintRightShadow) {
        Rectangle bounds = s.getBounds();
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        s = ShapeUtil.createRectangle(x, y, w, 2);
        g.setPaint(getTopShadowPaint(s));
        g.fill(s);

        s = ShapeUtil.createRectangle(x, y, 1, h);
        g.setPaint(getLeftShadowPaint(s));
        g.fill(s);

        if (paintRightShadow) {
            s = ShapeUtil.createRectangle(x + w - 1, y, 1, h);
            g.setPaint(getRightShadowPaint(s));
            g.fill(s);
        }
    }

    private void fillInternalShadowRounded(Graphics2D g, Shape s) {
        g.setPaint(getRoundedShadowPaint(s));
        g.fill(s);
    }

    public Paint getRoundedShadowPaint(Shape s) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        int y1 = r.y;
        float frac = 1.0f / r.height;
        int y2 = r.y + r.height;
        return new LinearGradientPaint(x, y1, x, y2, (new float[] { 0f, frac, 1f }), (new Color[] {
            innerShadow.top,
            innerShadow.bottom,
            innerShadow.bottom }));
    }

    public Paint getTopShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minY = (float) bounds.getMinY();
        float maxY = (float) bounds.getMaxY();
        float midX = (float) bounds.getCenterX();
        return new LinearGradientPaint(midX, minY, midX, maxY, (new float[] { 0f, 1f }),
            (new Color[] { innerShadow.top, transparentColor }));
    }

    public Paint getLeftShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX();
        float maxX = (float) bounds.getMaxX();
        float midY = (float) bounds.getCenterY();
        return new LinearGradientPaint(minX, midY, maxX, midY, (new float[] { 0f, 1f }), (new Color[] {
            innerShadow.bottom,
            transparentColor }));
    }

    public Paint getRightShadowPaint(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float minX = (float) bounds.getMinX() - 1;
        float maxX = (float) bounds.getMaxX() - 1;
        float midY = (float) bounds.getCenterY();
        return new LinearGradientPaint(minX, midY, maxX, midY, (new float[] { 0f, 1f }), (new Color[] {
            transparentColor,
            innerShadow.bottom }));
    }
}
