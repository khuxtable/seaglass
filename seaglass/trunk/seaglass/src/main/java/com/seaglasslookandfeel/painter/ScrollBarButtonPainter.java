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
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        FOREGROUND_ENABLED,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_INCREASE_ENABLED,
        FOREGROUND_INCREASE_DISABLED,
        FOREGROUND_INCREASE_PRESSED,
    }

    private static final ButtonStateColors disabledIncrease = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xffffff), new Color(
                                                                0xbdbdbd), new Color(0x80555555, true));
    private static final ButtonStateColors enabledIncrease  = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xffffff), new Color(
                                                                0xbdbdbd), new Color(0x555555));
    private static final ButtonStateColors pressedIncrease  = new ButtonStateColors(new Color(0x8fb1d1), new Color(0xcee2f5), new Color(
                                                                0x82abd0), new Color(0x555555));

    private static final ButtonStateColors disabledDecrease = new ButtonStateColors(new Color(0xffffff), new Color(0xcccccc), new Color(
                                                                0xbdbdbd), new Color(0x80555555, true));
    private static final ButtonStateColors enabledDecrease  = new ButtonStateColors(new Color(0xffffff), new Color(0xcccccc), new Color(
                                                                0xbdbdbd), new Color(0x555555));
    private static final ButtonStateColors pressedDecrease  = new ButtonStateColors(new Color(0xcee2f5), new Color(0x8fb1d1), new Color(
                                                                0x82abd0), new Color(0x555555));

    private final Color                    colorShadow      = new Color(0x000000);
    private Effect                         dropShadow       = new ScrollButtonDropShadowEffect();

    private static final Insets            insets           = new Insets(0, 0, 0, 0);
    private static final Dimension         dimension        = new Dimension(20, 15);
    private static final CacheMode         cacheMode        = CacheMode.FIXED_SIZES;
    private static final Double            maxH             = 2.0;
    private static final Double            maxV             = 2.0;

    private Path2D                         path             = new Path2D.Double();

    private Which                          state;
    private PaintContext                   ctx;

    public ScrollBarButtonPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FOREGROUND_DISABLED:
            paintDisabledDecreaseButton(g, c, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintEnabledDecreaseButton(g, c, width, height);
            break;
        case FOREGROUND_PRESSED:
            paintPressedDecreaseButton(g, c, width, height);
            break;
        case FOREGROUND_INCREASE_DISABLED:
            paintDisabledIncreaseButton(g, c, width, height);
            break;
        case FOREGROUND_INCREASE_ENABLED:
            paintEnabledIncreaseButton(g, c, width, height);
            break;
        case FOREGROUND_INCREASE_PRESSED:
            paintPressedIncreaseButton(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabledIncreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintIncreaseButton(g, c, width, height, disabledIncrease);
    }

    private void paintEnabledIncreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintIncreaseButton(g, c, width, height, enabledIncrease);
    }

    private void paintPressedIncreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintIncreaseButton(g, c, width, height, pressedIncrease);
    }

    private void paintDisabledDecreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintDecreaseButton(g, c, width, height, disabledDecrease);
    }

    private void paintEnabledDecreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintDecreaseButton(g, c, width, height, enabledDecrease);
    }

    private void paintPressedDecreaseButton(Graphics2D g, JComponent c, int width, int height) {
        paintDecreaseButton(g, c, width, height, pressedDecrease);
    }

    private void paintIncreaseButton(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = decodeButtonBackgroundPath(width, height);
        g.drawImage(createDropShadowImage(path, width, height), 0, 0, null);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, 0, width - 1, 0);
        s = decodeButtonForegroundPath(width, height, 5, 2);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    private void paintDecreaseButton(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = decodeButtonBackgroundPath(width, height);
        g.drawImage(createDropShadowImage(path, width, height), 0, 0, null);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, height - 1, width - 1, height - 1);
        s = decodeButtonForegroundPath(width, height, 4, 3);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    /**
     * Create a drop shadow image.
     * 
     * @param s
     *            the shape to use as the shade.
     * @param width
     *            TODO
     * @param height
     *            TODO
     */
    private BufferedImage createDropShadowImage(Shape s, int width, int height) {
        BufferedImage bimage = ScrollButtonDropShadowEffect.createBufferedImage(width, height, true);
        Graphics2D gbi = bimage.createGraphics();
        gbi.setColor(colorShadow);
        gbi.fill(s);
        return dropShadow.applyEffect(bimage, null, width, height);
    }

    private Paint decodeButtonGradient(Shape s, Color top, Color bottom) {
        int width = s.getBounds().width;
        int height = s.getBounds().height;
        return decodeGradient(0, height / 2, width - 1, height / 2, new float[] { 0f, 1f }, new Color[] { top, bottom });
    }

    private Shape decodeButtonForegroundPath(int width, int height, int xOffset, int yOffset) {
        double x = width / 2.0 - xOffset;
        double y = height / 2.0 - yOffset;
        path.reset();
        path.moveTo(x + 0, y + 3);
        path.lineTo(x + 4, y + 6);
        path.lineTo(x + 4, y + 0);
        path.closePath();
        return path;
    }

    private Shape decodeButtonBackgroundPath(int width, int height) {
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
        public Color foreground;

        public ButtonStateColors(Color top, Color bottom, Color line, Color foreground) {
            this.top = top;
            this.bottom = bottom;
            this.line = line;
            this.foreground = foreground;
        }
    }
}
