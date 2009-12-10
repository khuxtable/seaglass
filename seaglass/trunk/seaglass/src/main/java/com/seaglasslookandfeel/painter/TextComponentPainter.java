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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.FocusEffect;

/**
 * TextComponentPainter implementation.
 */
public final class TextComponentPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_SOLID_DISABLED,
        BACKGROUND_SOLID_ENABLED,
        BACKGROUND_SELECTED,
        BORDER_DISABLED,
        BORDER_FOCUSED,
        BORDER_ENABLED,
    }

    private static final Color     DISABLED_BORDER = new Color(0xdddddd);
    private static final Color     ENABLED_BORDER  = new Color(0xbbbbbb);

    private static final Effect    dropShadow      = new TextFieldDropShadowEffect();
    private static final Effect    focusGlow       = new FocusEffect();
    private static final Dimension dimension       = new Dimension(122, 24);
    private static final Insets    insets          = new Insets(5, 3, 3, 3);

    Rectangle2D                    path            = new Rectangle2D.Double();

    private Which                  state;
    private PaintContext           ctx;

    public TextComponentPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(insets, dimension, false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
            paintBackgroundDisabled(g, c, width, height);
            break;
        case BACKGROUND_ENABLED:
            paintBackgroundEnabled(g, c, width, height);
            break;
        case BACKGROUND_SOLID_DISABLED:
            paintBackgroundSolidDisabled(g, c, width, height);
            break;
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolidEnabled(g, c, width, height);
            break;
        case BACKGROUND_SELECTED:
            paintBackgroundEnabled(g, c, width, height);
            break;
        case BORDER_DISABLED:
            paintBorderDisabled(g, c, width, height);
            break;
        case BORDER_ENABLED:
            paintBorderEnabled(g, c, width, height);
            break;
        case BORDER_FOCUSED:
            paintBorderFocused(g, c, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g, JComponent c, int width, int height) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        g.setColor(color);
        g.fillRect(3, 3, width - 7, height - 7);
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        g.setColor(c.getBackground());
        g.fillRect(3, 3, width - 7, height - 7);
    }

    private void paintBackgroundSolidDisabled(Graphics2D g, JComponent c, int width, int height) {
        Color color = c.getBackground();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    private void paintBackgroundSolidEnabled(Graphics2D g, JComponent c, int width, int height) {
        g.setColor(c.getBackground());
        g.fillRect(0, 0, width, height);
    }

    private void paintBorderDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintInternalDropShadow(g, width, height);

        g.setColor(DISABLED_BORDER);
        g.drawRect(2, 2, width - 5, height - 5);
    }

    private void paintBorderEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintInternalDropShadow(g, width, height);

        g.setColor(ENABLED_BORDER);
        g.drawRect(2, 2, width - 5, height - 5);
    }

    private void paintBorderFocused(Graphics2D g, JComponent c, int width, int height) {
        path.setRect(2, 2, width - 4, height - 4);
        focusGlow.paint(g, path, width, height);
        paintBorderEnabled(g, c, width, height);
    }

    private void paintInternalDropShadow(Graphics2D g, int width, int height) {
        Shape s = g.getClip();
        g.setClip(2, 2, width - 5, height - 5);
        path.setRect(2, 2, width - 5, 2);
        dropShadow.paint(g, path, width, height);
        g.setClip(s);
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    public static class TextFieldDropShadowEffect extends DropShadowEffect {

        public TextFieldDropShadowEffect() {
            color = new Color(211, 211, 211);
            angle = 90;
            distance = 0;
            size = 2;
            opacity = 0.35f;
        }
    }
}
