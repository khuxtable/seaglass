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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class RadioButtonMenuItemPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_SELECTED_MOUSEOVER,
        CHECKICON_DISABLED_SELECTED,
        CHECKICON_ENABLED_SELECTED,
        CHECKICON_SELECTED_MOUSEOVER,
        ICON_DISABLED,
        ICON_ENABLED,
        ICON_MOUSEOVER,
    }

    private Which        state;
    private PaintContext ctx;

    private Path2D       path                  = new Path2D.Float();
    private Rectangle2D  rect                  = new Rectangle2D.Float(0, 0, 0, 0);

    private ColorSet     colors                = new ColorSet(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(
                                                   0x00A8D9FC, true), new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(
                                                   0x4F7BBF), new Color(0x3F76BF));

    private Color        iconDisabledSelected  = decodeColor("nimbusBlueGrey", 0.0f, -0.08983666f, -0.17647058f, 0);
    private Color        iconEnabledSelected   = decodeColor("nimbusBlueGrey", 0.055555582f, -0.09663743f, -0.4627451f, 0);
    private Color        iconSelectedMouseOver = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    public RadioButtonMenuItemPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_SELECTED_MOUSEOVER:
            ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(100, 3), false, CacheMode.NO_CACHING, 1.0, 1.0);
            break;
        default:
            ctx = new PaintContext(new Insets(5, 5, 5, 5), new Dimension(9, 10), false, CacheMode.FIXED_SIZES, 1.0, 1.0);
            break;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_MOUSEOVER:
            paintBackgroundMouseOver(g, width, height);
            break;
        case BACKGROUND_SELECTED_MOUSEOVER:
            paintBackgroundMouseOver(g, width, height);
            break;
        case CHECKICON_DISABLED_SELECTED:
            paintcheckIconDisabledAndSelected(g);
            break;
        case CHECKICON_ENABLED_SELECTED:
            paintcheckIconEnabledAndSelected(g);
            break;
        case CHECKICON_SELECTED_MOUSEOVER:
            paintcheckIconSelectedAndMouseOver(g);
            break;

        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundMouseOver(Graphics2D g, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = decodeBorder(width, height);
        g.setPaint(decodeGradientBackground(s, colors.borderTop, colors.borderBottom));
        // g.fill(s);
        s = decodeInterior(width, height);
        g.setColor(colors.mainColor);
        g.fill(s);
        g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(s);
        g.setPaint(decodeGradientTopShine(s, colors.upperShineTop, colors.upperShineBottom));
        g.fill(s);
    }

    private void paintcheckIconDisabledAndSelected(Graphics2D g) {
        Shape s = decodePath1();
        g.setPaint(iconDisabledSelected);
        g.fill(s);
    }

    private void paintcheckIconEnabledAndSelected(Graphics2D g) {
        Shape s = decodePath2();
        g.setPaint(iconEnabledSelected);
        g.fill(s);
    }

    private void paintcheckIconSelectedAndMouseOver(Graphics2D g) {
        Shape s = decodePath2();
        g.setPaint(iconSelectedMouseOver);
        g.fill(s);
    }

    private Shape decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(2.097561f));
        path.lineTo(decodeX(0.90975606f), decodeY(0.20243903f));
        path.lineTo(decodeX(3.0f), decodeY(2.102439f));
        path.lineTo(decodeX(0.90731704f), decodeY(3.0f));
        path.lineTo(decodeX(0.0f), decodeY(2.097561f));
        path.closePath();
        return path;
    }

    private Shape decodePath2() {
        path.reset();
        path.moveTo(decodeX(0.0024390244f), decodeY(2.097561f));
        path.lineTo(decodeX(0.90975606f), decodeY(0.20243903f));
        path.lineTo(decodeX(3.0f), decodeY(2.102439f));
        path.lineTo(decodeX(0.90731704f), decodeY(3.0f));
        path.lineTo(decodeX(0.0024390244f), decodeY(2.097561f));
        path.closePath();
        return path;
    }

    private Rectangle2D decodeBorder(int width, int height) {
        rect.setRect(0, 0, width, height);
        return rect;
    }

    private Rectangle2D decodeInterior(int width, int height) {
        rect.setRect(1, 1, width - 2, height - 2);
        return rect;
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the highlight.
     */
    public class ColorSet {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color borderTop;
        public Color borderBottom;

        public ColorSet(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom, float lowerShineMidpoint,
            Color mainColor, Color borderTop, Color borderBottom) {
            this.upperShineTop = upperShineTop;
            this.upperShineBottom = upperShineBottom;
            this.lowerShineTop = lowerShineTop;
            this.lowerShineBottom = lowerShineBottom;
            this.lowerShineMidpoint = lowerShineMidpoint;
            this.mainColor = mainColor;
            this.borderTop = borderTop;
            this.borderBottom = borderBottom;
        }
    }
}
