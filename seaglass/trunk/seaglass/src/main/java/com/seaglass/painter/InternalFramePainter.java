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
package com.seaglass.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 */
public final class InternalFramePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    };

    // Constants for the PaintContext.
    private static final Insets    insets    = new Insets(25, 6, 6, 6);
    private static final Dimension dimension = new Dimension(25, 36);
    private static final CacheMode cacheMode = PaintContext.CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    private Which                  state;
    private PaintContext           ctx;

    private Path2D                 path      = new Path2D.Float();
    private Rectangle2D            rect      = new Rectangle2D.Float(0, 0, 0, 0);
    private RoundRectangle2D       roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    private Color                  color1    = decodeColor("nimbusBase", 0.032459438f, -0.53637654f, 0.043137252f, 0);
    private Color                  color2    = decodeColor("nimbusBlueGrey", 0.004273474f, -0.039488062f, -0.027450979f, 0);
    private Color                  color3    = decodeColor("nimbusBlueGrey", -0.00505054f, -0.056339122f, 0.05098039f, 0);
    private Color                  color4    = decodeColor("nimbusBlueGrey", -0.01111114f, -0.06357796f, 0.09019607f, 0);
    private Color                  color5    = decodeColor("nimbusBlueGrey", 0.0f, -0.023821115f, -0.06666666f, 0);
    private Color                  color6    = decodeColor("control", 0.0f, 0.0f, 0.0f, 0);
    private Color                  color7    = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07399663f, 0.11372548f, 0);
    private Color                  color8    = decodeColor("nimbusBase", 0.02551502f, -0.47885156f, -0.34901965f, 0);
    private Color                  color9    = new Color(255, 200, 0, 255);
    private Color                  color10   = decodeColor("nimbusBase", 0.004681647f, -0.6274498f, 0.39999998f, 0);
    private Color                  color11   = decodeColor("nimbusBase", 0.032459438f, -0.5934608f, 0.2862745f, 0);
    private Color                  color12   = new Color(204, 207, 213, 255);
    private Color                  color13   = decodeColor("nimbusBase", 0.032459438f, -0.55506915f, 0.18039215f, 0);
    private Color                  color14   = decodeColor("nimbusBase", 0.004681647f, -0.52792984f, 0.10588235f, 0);
    private Color                  color15   = decodeColor("nimbusBase", 0.03801495f, -0.4794643f, -0.04705882f, 0);
    private Color                  color16   = decodeColor("nimbusBase", 0.021348298f, -0.61416256f, 0.3607843f, 0);
    private Color                  color17   = decodeColor("nimbusBase", 0.032459438f, -0.5546332f, 0.17647058f, 0);
    private Color                  color18   = new Color(235, 236, 238, 255);

    public InternalFramePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g);
            break;
        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            paintBackgroundEnabledAndWindowFocused(g);
            break;
        }
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        roundRect = decodeRoundRect1();
        g.setPaint(color1);
        g.fill(roundRect);
        path = decodePath1();
        g.setPaint(decodeGradient1(path));
        g.fill(path);
        path = decodePath2();
        g.setPaint(color3);
        g.fill(path);
        path = decodePath3();
        g.setPaint(color4);
        g.fill(path);
        path = decodePath4();
        g.setPaint(color5);
        g.fill(path);
        rect = decodeRect1();
        g.setPaint(color6);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(color7);
        g.fill(rect);

    }

    private void paintBackgroundEnabledAndWindowFocused(Graphics2D g) {
        roundRect = decodeRoundRect2();
        g.setPaint(color8);
        g.fill(roundRect);
        path = decodePath5();
        g.setPaint(color9);
        g.fill(path);
        path = decodePath1();
        g.setPaint(decodeGradient2(path));
        g.fill(path);
        path = decodePath6();
        g.setPaint(color12);
        g.fill(path);
        path = decodePath7();
        g.setPaint(color13);
        g.fill(path);
        path = decodePath8();
        g.setPaint(color14);
        g.fill(path);
        path = decodePath9();
        g.setPaint(color15);
        g.fill(path);
        rect = decodeRect1();
        g.setPaint(color6);
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(color9);
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(color9);
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(color9);
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(decodeGradient3(rect));
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(color18);
        g.fill(rect);

    }

    private RoundRectangle2D decodeRoundRect1() {
        roundRect.setRoundRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f), // height
            4.6666665f, 4.6666665f); // rounding
        return roundRect;
    }

    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.16666667f), decodeY(0.12f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(0.11999999731779099f, -1.0f), decodeAnchorX(0.5f, -1.0f),
            decodeAnchorY(0.03999999910593033f, 0.0f), decodeX(0.5f), decodeY(0.04f));
        path.curveTo(decodeAnchorX(0.5f, 1.0f), decodeAnchorY(0.03999999910593033f, 0.0f), decodeAnchorX(2.5f, -1.0f), decodeAnchorY(
            0.03999999910593033f, 0.0f), decodeX(2.5f), decodeY(0.04f));
        path.curveTo(decodeAnchorX(2.5f, 1.0f), decodeAnchorY(0.03999999910593033f, 0.0f), decodeAnchorX(2.8333332538604736f, 0.0f),
            decodeAnchorY(0.11999999731779099f, -1.0f), decodeX(2.8333333f), decodeY(0.12f));
        path.curveTo(decodeAnchorX(2.8333332538604736f, 0.0f), decodeAnchorY(0.11999999731779099f, 1.0f), decodeAnchorX(
            2.8333332538604736f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeX(2.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(0.16666667f), decodeY(0.96f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeAnchorX(0.1666666716337204f,
            0.0f), decodeAnchorY(0.11999999731779099f, 1.0f), decodeX(0.16666667f), decodeY(0.12f));
        path.closePath();
        return path;
    }

    private Path2D decodePath2() {
        path.reset();
        path.moveTo(decodeX(0.6666667f), decodeY(0.96f));
        path.lineTo(decodeX(0.16666667f), decodeY(0.96f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeAnchorX(0.1666666716337204f,
            0.0f), decodeAnchorY(2.5f, -1.0f), decodeX(0.16666667f), decodeY(2.5f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(2.5f, 1.0f), decodeAnchorX(0.5f, -1.0f), decodeAnchorY(
            2.8333332538604736f, 0.0f), decodeX(0.5f), decodeY(2.8333333f));
        path.curveTo(decodeAnchorX(0.5f, 1.0f), decodeAnchorY(2.8333332538604736f, 0.0f), decodeAnchorX(2.5f, -1.0f), decodeAnchorY(
            2.8333332538604736f, 0.0f), decodeX(2.5f), decodeY(2.8333333f));
        path.curveTo(decodeAnchorX(2.5f, 1.0f), decodeAnchorY(2.8333332538604736f, 0.0f), decodeAnchorX(2.8333332538604736f, 0.0f),
            decodeAnchorY(2.5f, 1.0f), decodeX(2.8333333f), decodeY(2.5f));
        path.curveTo(decodeAnchorX(2.8333332538604736f, 0.0f), decodeAnchorY(2.5f, -1.0f), decodeAnchorX(2.8333332538604736f, 0.0f),
            decodeAnchorY(0.9599999785423279f, 0.0f), decodeX(2.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.3333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.3333333f), decodeY(2.3333333f));
        path.lineTo(decodeX(0.6666667f), decodeY(2.3333333f));
        path.lineTo(decodeX(0.6666667f), decodeY(0.96f));
        path.closePath();
        return path;
    }

    private Path2D decodePath3() {
        path.reset();
        path.moveTo(decodeX(0.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(0.6666667f), decodeY(0.96f));
        path.lineTo(decodeX(0.6666667f), decodeY(2.3333333f));
        path.lineTo(decodeX(2.3333333f), decodeY(2.3333333f));
        path.lineTo(decodeX(2.3333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(0.96f));
        path.closePath();
        return path;
    }

    private Path2D decodePath4() {
        path.reset();
        path.moveTo(decodeX(2.1666667f), decodeY(1.0f));
        path.lineTo(decodeX(1.0f), decodeY(1.0f));
        path.lineTo(decodeX(1.0f), decodeY(2.0f));
        path.lineTo(decodeX(2.0f), decodeY(2.0f));
        path.lineTo(decodeX(2.0f), decodeY(1.0f));
        path.lineTo(decodeX(2.1666667f), decodeY(1.0f));
        path.lineTo(decodeX(2.1666667f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(1.0f));
        path.closePath();
        return path;
    }

    private Rectangle2D decodeRect1() {
        rect.setRect(decodeX(1.0f), // x
            decodeY(1.0f), // y
            decodeX(2.0f) - decodeX(1.0f), // width
            decodeY(2.0f) - decodeY(1.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect2() {
        rect.setRect(decodeX(0.33333334f), // x
            decodeY(2.6666667f), // y
            decodeX(2.6666667f) - decodeX(0.33333334f), // width
            decodeY(2.8333333f) - decodeY(2.6666667f)); // height
        return rect;
    }

    private RoundRectangle2D decodeRoundRect2() {
        roundRect.setRoundRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(3.0f) - decodeX(0.0f), // width
            decodeY(3.0f) - decodeY(0.0f), // height
            4.8333335f, 4.8333335f); // rounding
        return roundRect;
    }

    private Path2D decodePath5() {
        path.reset();
        path.moveTo(decodeX(0.16666667f), decodeY(0.08f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(0.07999999821186066f, 1.0f), decodeAnchorX(
            0.1666666716337204f, 0.0f), decodeAnchorY(0.07999999821186066f, -1.0f), decodeX(0.16666667f), decodeY(0.08f));
        path.closePath();
        return path;
    }

    private Path2D decodePath6() {
        path.reset();
        path.moveTo(decodeX(0.5f), decodeY(0.96f));
        path.lineTo(decodeX(0.16666667f), decodeY(0.96f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeAnchorX(0.1666666716337204f,
            0.0f), decodeAnchorY(2.5f, -1.0f), decodeX(0.16666667f), decodeY(2.5f));
        path.curveTo(decodeAnchorX(0.1666666716337204f, 0.0f), decodeAnchorY(2.5f, 1.0f), decodeAnchorX(0.5f, -1.0f), decodeAnchorY(
            2.8333332538604736f, 0.0f), decodeX(0.5f), decodeY(2.8333333f));
        path.curveTo(decodeAnchorX(0.5f, 1.0f), decodeAnchorY(2.8333332538604736f, 0.0f), decodeAnchorX(2.5f, -1.0f), decodeAnchorY(
            2.8333332538604736f, 0.0f), decodeX(2.5f), decodeY(2.8333333f));
        path.curveTo(decodeAnchorX(2.5f, 1.0f), decodeAnchorY(2.8333332538604736f, 0.0f), decodeAnchorX(2.8333332538604736f, 0.0f),
            decodeAnchorY(2.5f, 1.0f), decodeX(2.8333333f), decodeY(2.5f));
        path.curveTo(decodeAnchorX(2.8333332538604736f, 0.0f), decodeAnchorY(2.5f, -1.0f), decodeAnchorX(2.8333332538604736f, 0.0f),
            decodeAnchorY(0.9599999785423279f, 0.0f), decodeX(2.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.5f), decodeY(0.96f));
        path.lineTo(decodeX(2.5f), decodeY(2.5f));
        path.lineTo(decodeX(0.5f), decodeY(2.5f));
        path.lineTo(decodeX(0.5f), decodeY(0.96f));
        path.closePath();
        return path;
    }

    private Path2D decodePath7() {
        path.reset();
        path.moveTo(decodeX(0.6666667f), decodeY(0.96f));
        path.lineTo(decodeX(0.33333334f), decodeY(0.96f));
        path.curveTo(decodeAnchorX(0.3333333432674408f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeAnchorX(0.3333333432674408f,
            0.0f), decodeAnchorY(2.3333332538604736f, -1.0f), decodeX(0.33333334f), decodeY(2.3333333f));
        path.curveTo(decodeAnchorX(0.3333333432674408f, 0.0f), decodeAnchorY(2.3333332538604736f, 1.0f), decodeAnchorX(0.6666666865348816f,
            -1.0f), decodeAnchorY(2.6666667461395264f, 0.0f), decodeX(0.6666667f), decodeY(2.6666667f));
        path.curveTo(decodeAnchorX(0.6666666865348816f, 1.0f), decodeAnchorY(2.6666667461395264f, 0.0f), decodeAnchorX(2.3333332538604736f,
            -1.0f), decodeAnchorY(2.6666667461395264f, 0.0f), decodeX(2.3333333f), decodeY(2.6666667f));
        path.curveTo(decodeAnchorX(2.3333332538604736f, 1.0f), decodeAnchorY(2.6666667461395264f, 0.0f), decodeAnchorX(2.6666667461395264f,
            0.0f), decodeAnchorY(2.3333332538604736f, 1.0f), decodeX(2.6666667f), decodeY(2.3333333f));
        path.curveTo(decodeAnchorX(2.6666667461395264f, 0.0f), decodeAnchorY(2.3333332538604736f, -1.0f), decodeAnchorX(
            2.6666667461395264f, 0.0f), decodeAnchorY(0.9599999785423279f, 0.0f), decodeX(2.6666667f), decodeY(0.96f));
        path.lineTo(decodeX(2.3333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.3333333f), decodeY(2.3333333f));
        path.lineTo(decodeX(0.6666667f), decodeY(2.3333333f));
        path.lineTo(decodeX(0.6666667f), decodeY(0.96f));
        path.closePath();
        return path;
    }

    private Path2D decodePath8() {
        path.reset();
        path.moveTo(decodeX(2.3333333f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(0.96f));
        path.lineTo(decodeX(2.1666667f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(2.1666667f));
        path.lineTo(decodeX(0.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(0.6666667f), decodeY(0.96f));
        path.lineTo(decodeX(0.6666667f), decodeY(2.3333333f));
        path.lineTo(decodeX(2.3333333f), decodeY(2.3333333f));
        path.lineTo(decodeX(2.3333333f), decodeY(0.96f));
        path.closePath();
        return path;
    }

    private Path2D decodePath9() {
        path.reset();
        path.moveTo(decodeX(0.8333333f), decodeY(1.0f));
        path.lineTo(decodeX(0.8333333f), decodeY(2.1666667f));
        path.lineTo(decodeX(2.1666667f), decodeY(2.1666667f));
        path.lineTo(decodeX(2.1666667f), decodeY(0.96f));
        path.lineTo(decodeX(0.8333333f), decodeY(0.96f));
        path.lineTo(decodeX(0.8333333f), decodeY(1.0f));
        path.lineTo(decodeX(2.0f), decodeY(1.0f));
        path.lineTo(decodeX(2.0f), decodeY(2.0f));
        path.lineTo(decodeX(1.0f), decodeY(2.0f));
        path.lineTo(decodeX(1.0f), decodeY(1.0f));
        path.lineTo(decodeX(0.8333333f), decodeY(1.0f));
        path.closePath();
        return path;
    }

    private Rectangle2D decodeRect3() {
        rect.setRect(decodeX(0.0f), // x
            decodeY(0.0f), // y
            decodeX(0.0f) - decodeX(0.0f), // width
            decodeY(0.0f) - decodeY(0.0f)); // height
        return rect;
    }

    private Rectangle2D decodeRect4() {
        rect.setRect(decodeX(0.33333334f), // x
            decodeY(0.08f), // y
            decodeX(2.6666667f) - decodeX(0.33333334f), // width
            decodeY(0.96f) - decodeY(0.08f)); // height
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 0.3203593f, 1.0f },
            new Color[] { color2, decodeColor(color2, color3, 0.5f), color3 });
    }

    private Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 0.5f, 1.0f },
            new Color[] { color10, decodeColor(color10, color11, 0.5f), color11 });
    }

    private Paint decodeGradient3(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y, new float[] { 0.0f, 0.24251497f, 1.0f },
            new Color[] { color16, decodeColor(color16, color17, 0.5f), color17 });
    }
}
