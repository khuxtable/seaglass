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

import com.seaglasslookandfeel.painter.AbstractRegionPainter.TwoColors;
import com.seaglasslookandfeel.painter.util.ShapeGenerator;

/**
 * Effect to fill an internal shadow, e.g. in a text component or a progress
 * bar.
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassInternalShadowEffect {

    private Color transparentColor = UIManager.getColor("seaGlassTransparent");
    private Color innerShadowBase  = UIManager.getColor("seaGlassInnerShadow");

    private Color     innerShadowLight = new Color(innerShadowBase.getRed(), innerShadowBase.getGreen(), innerShadowBase.getBlue(),
                                                   innerShadowBase.getAlpha() / 2);
    private TwoColors innerShadow      = new TwoColors(innerShadowBase, innerShadowLight);

    private ShapeGenerator shapeGenerator = new ShapeGenerator();

    /**
     * Fill the shape with the internal shadow.
     *
     * @param g                the Graphics context to paint with.
     * @param s                the shape to fill. This is only used for its
     *                         bounds.
     * @param isRounded {@code true} if the shape is rounded, {@code false} if
     *                         it is rectangular.
     * @param paintRightShadow {@code true} if the shape is rectangular and the
     *                         rightmost shadow should be painted, {@code false}
     *                         otherwise.
     */
    public void fill(Graphics2D g, Shape s, boolean isRounded, boolean paintRightShadow) {
        if (isRounded) {
            fillInternalShadowRounded(g, s);
        } else {
            fillInternalShadow(g, s, paintRightShadow);
        }
    }

    /**
     * Fill a rectangular shadow.
     *
     * @param g                the Graphics context to paint with.
     * @param s                the shape to fill. This is only used for its
     *                         bounds.
     * @param paintRightShadow {@code true} if the shape is rectangular and the
     *                         rightmost shadow should be painted, {@code false}
     *                         otherwise.
     */
    private void fillInternalShadow(Graphics2D g, Shape s, boolean paintRightShadow) {
        Rectangle bounds = s.getBounds();
        int       x      = bounds.x;
        int       y      = bounds.y;
        int       w      = bounds.width;
        int       h      = bounds.height;

        s = shapeGenerator.createRectangle(x, y, w, 2);
        g.setPaint(getTopShadowGradient(s));
        g.fill(s);

        s = shapeGenerator.createRectangle(x, y, 1, h);
        g.setPaint(getLeftShadowGradient(s));
        g.fill(s);

        if (paintRightShadow) {
            s = shapeGenerator.createRectangle(x + w - 1, y, 1, h);
            g.setPaint(getRightShadowGradient(s));
            g.fill(s);
        }
    }

    /**
     * Fill a rounded shadow.
     *
     * @param g the Graphics context to paint with.
     * @param s the shape to fill. This is only used for its bounds.
     */
    private void fillInternalShadowRounded(Graphics2D g, Shape s) {
        g.setPaint(getRoundedShadowGradient(s));
        g.fill(s);
    }

    /**
     * Create the gradient for a rounded shadow.
     *
     * @param  s the shape of the gradient. This is only used for its bounds.
     *
     * @return the gradient.
     */
    public Paint getRoundedShadowGradient(Shape s) {
        Rectangle r    = s.getBounds();
        int       x    = r.x + r.width / 2;
        int       y1   = r.y;
        float     frac = 1.0f / r.height;
        int       y2   = r.y + r.height;

        return new LinearGradientPaint(x, y1, x, y2, (new float[] { 0f, frac, 1f }),
                                       new Color[] { innerShadow.top, innerShadow.bottom, innerShadow.bottom });
    }

    /**
     * Create the gradient for the top of a rectangular shadow.
     *
     * @param  s the shape of the gradient. This is only used for its bounds.
     *
     * @return the gradient.
     */
    public Paint getTopShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float       minY   = (float) bounds.getMinY();
        float       maxY   = (float) bounds.getMaxY();
        float       midX   = (float) bounds.getCenterX();

        return new LinearGradientPaint(midX, minY, midX, maxY, (new float[] { 0f, 1f }), new Color[] { innerShadow.top, transparentColor });
    }

    /**
     * Create the gradient for the left of a rectangular shadow.
     *
     * @param  s the shape of the gradient. This is only used for its bounds.
     *
     * @return the gradient.
     */
    public Paint getLeftShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float       minX   = (float) bounds.getMinX();
        float       maxX   = (float) bounds.getMaxX();
        float       midY   = (float) bounds.getCenterY();

        return new LinearGradientPaint(minX, midY, maxX, midY, (new float[] { 0f, 1f }),
                                       new Color[] { innerShadow.bottom, transparentColor });
    }

    /**
     * Create the gradient for the right of a rectangular shadow.
     *
     * @param  s the shape of the gradient. This is only used for its bounds.
     *
     * @return the gradient.
     */
    public Paint getRightShadowGradient(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float       minX   = (float) bounds.getMinX() - 1;
        float       maxX   = (float) bounds.getMaxX() - 1;
        float       midY   = (float) bounds.getCenterY();

        return new LinearGradientPaint(minX, midY, maxX, midY, (new float[] { 0f, 1f }),
                                       new Color[] { transparentColor, innerShadow.bottom });
    }
}
