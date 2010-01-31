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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Title pane menu button painter implementation.
 */
public final class TitlePaneMenuButtonPainter extends TitlePaneButtonPainter {
    public static enum Which {
        ICON_ENABLED,
        ICON_DISABLED,
        ICON_MOUSEOVER,
        ICON_PRESSED,
        ICON_ENABLED_WINDOWNOTFOCUSED,
        ICON_MOUSEOVER_WINDOWNOTFOCUSED,
        ICON_PRESSED_WINDOWNOTFOCUSED,
    }

    private ButtonColors enabled = new ButtonColors(white16, black66, white4c,
                                     black66, black33, white33, transparentColor,
                                     black99, white99);
    private ButtonColors hover   = new ButtonColors(white68, black66, white8c,
                                     black66, black33, white46,
                                     white59, gray_10_e5, white);
    private ButtonColors pressed = new ButtonColors(gray_9b_82, black66, gray_a9_9e,
                                     black66, black33, white33,
                                     gray_e6_59, gray_0e_e5, gray_e6);

    private Path2D       path    = new Path2D.Double();

    private Which        state;
    private PaintContext ctx;

    public TitlePaneMenuButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case ICON_ENABLED:
        case ICON_DISABLED:
        case ICON_ENABLED_WINDOWNOTFOCUSED:
            paintEnabled(g, c, width, height);
            break;
        case ICON_MOUSEOVER:
        case ICON_MOUSEOVER_WINDOWNOTFOCUSED:
            paintHover(g, c, width, height);
            break;
        case ICON_PRESSED:
        case ICON_PRESSED_WINDOWNOTFOCUSED:
            paintPressed(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, enabled);
    }

    private void paintHover(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, hover);
    }

    private void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paintMenu(g, c, width, height, pressed);
    }

    private void paintMenu(Graphics2D g, JComponent c, int width, int height, ButtonColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(colors.top);
        g.drawLine(0, 0, width - 2, 0);

        g.setColor(colors.leftOuter);
        g.drawLine(0, 0, 0, height - 4);

        g.setColor(colors.leftInner);
        g.drawLine(1, 1, 1, height - 4);
        g.drawLine(2, height - 3, 2, height - 3);

        Shape s = decodeInterior(width, height);
        g.setColor(colors.interior);
        g.fill(s);

        s = decodeEdge(width, height);
        g.setColor(colors.edge);
        g.draw(s);

        g.setColor(colors.edgeShade);
        g.drawLine(2, height - 2, 2, height - 2);
        g.drawLine(1, height - 3, 1, height - 3);
        g.drawLine(0, height - 4, 0, height - 4);

        s = decodeShadow(width, height);
        g.setColor(colors.shadow);
        g.draw(s);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        s = decodeMarkInterior(width, height);
        g.setColor(colors.markInterior);
        g.fill(s);

        s = decodeMarkBorder(width, height);
        g.setColor(colors.markBorder);
        g.draw(s);
    }

    private Shape decodeInterior(int width, int height) {
        path.reset();
        path.moveTo(1, 1);
        path.lineTo(width - 2, 1);
        path.lineTo(width - 2, height - 3);
        path.lineTo(width - 3, height - 2);
        path.lineTo(3, height - 2);
        path.lineTo(2, height - 3);
        path.closePath();
        return path;
    }

    private Shape decodeEdge(int width, int height) {
        path.reset();
        path.moveTo(width - 2, 0);
        path.lineTo(width - 2, height - 4);
        path.lineTo(width - 4, height - 2);
        path.lineTo(3, height - 2);
        return path;
    }

    private Shape decodeShadow(int width, int height) {
        path.reset();
        path.moveTo(width - 1, 0);
        path.lineTo(width - 1, height - 4);
        path.lineTo(width - 4, height - 1);
        path.lineTo(2, height - 1);
        path.lineTo(1, height - 2);
        return path;
    }

    private Shape decodeMarkBorder(int width, int height) {
        double left = width / 2.0 - 4;
        double top = height / 2.0 - 4;
        path.reset();
        path.moveTo(left + 0, top + 0);
        path.lineTo(left + 8, top);
        path.lineTo(left + 4, top + 6);
        path.closePath();
        return path;
    }

    private Shape decodeMarkInterior(int width, int height) {
        double left = width / 2.0 - 4;
        double top = height / 2.0 - 4;
        path.reset();
        path.moveTo(left + 1, top + 1);
        path.lineTo(left + 8, top + 1);
        path.lineTo(left + 4, top + 6);
        path.closePath();
        return path;
    }

    private static class ButtonColors {
        public Color top;
        public Color leftOuter;
        public Color leftInner;
        public Color edge;
        public Color edgeShade;
        public Color shadow;
        public Color interior;
        public Color markBorder;
        public Color markInterior;

        public ButtonColors(Color top, Color leftOuter, Color leftInner, Color edge, Color edgeShade, Color shadow, Color interior,
            Color markBorder, Color markInterior) {
            this.top = top;
            this.leftOuter = leftOuter;
            this.leftInner = leftInner;
            this.edge = edge;
            this.edgeShade = edgeShade;
            this.shadow = shadow;
            this.interior = interior;
            this.markBorder = markBorder;
            this.markInterior = markInterior;
        }
    }
}
