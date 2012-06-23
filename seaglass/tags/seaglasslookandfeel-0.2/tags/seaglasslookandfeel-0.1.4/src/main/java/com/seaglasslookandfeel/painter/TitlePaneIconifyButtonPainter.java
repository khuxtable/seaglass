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
import java.awt.RenderingHints;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.titlepaneforegound.TitlePaneIconifyButtonForegroundPainter;
import com.seaglasslookandfeel.painter.titlepaneforegound.TitlePaneRestoreButtonForegroundPainter;

/**
 * Title pane iconify button painter implementation.
 */
public final class TitlePaneIconifyButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_DISABLED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_PRESSED_WINDOWNOTFOCUSED,

        BACKGROUND_MINIMIZED_DISABLED,
        BACKGROUND_MINIMIZED_ENABLED,
        BACKGROUND_MINIMIZED_MOUSEOVER,
        BACKGROUND_MINIMIZED_PRESSED,
        BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED,
    }

    private static final ButtonColors               enabled        = new ButtonColors(new Color(0x16ffffff, true), new Color(0x66000000,
                                                                       true), new Color(0x4cffffff, true), new Color(0x66000000, true),
                                                                       new Color(0x33000000, true), new Color(0x33ffffff, true), new Color(
                                                                           0, true));
    private static final ButtonColors               hover          = new ButtonColors(new Color(0x68ffffff, true), new Color(0x66000000,
                                                                       true), new Color(0x8cffffff, true), new Color(0x66000000, true),
                                                                       new Color(0x33000000, true), new Color(0x46ffffff, true), new Color(
                                                                           0x59ffffff, true));
    private static final ButtonColors               pressed        = new ButtonColors(new Color(0x829b9b9b, true), new Color(0x66000000,
                                                                       true), new Color(0x9ea9a9a9, true), new Color(0x66000000, true),
                                                                       new Color(0x33000000, true), new Color(0x33ffffff, true), new Color(
                                                                           0x59e6e6e6, true));

    private TitlePaneIconifyButtonForegroundPainter iconifyPainter = new TitlePaneIconifyButtonForegroundPainter();
    private TitlePaneRestoreButtonForegroundPainter restorePainter = new TitlePaneRestoreButtonForegroundPainter();

    private Which                                   state;
    private PaintContext                            ctx;

    public TitlePaneIconifyButtonPainter(Which state) {
        super();
        this.state = state;
        ctx = new PaintContext(new Insets(0, 0, 0, 0), new Dimension(26, 18), false, CacheMode.FIXED_SIZES, 1.0, 1.0);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED_WINDOWNOTFOCUSED:
            paintBackgroundEnabled(g, c, width, height);
            paintMinimizeEnabled(g, c, width, height);
            break;
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED:
            paintBackgroundHover(g, c, width, height);
            paintMinimizeHover(g, c, width, height);
            break;
        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_WINDOWNOTFOCUSED:
            paintBackgroundPressed(g, c, width, height);
            paintMinimizePressed(g, c, width, height);
            break;

        case BACKGROUND_MINIMIZED_DISABLED:
        case BACKGROUND_MINIMIZED_ENABLED:
        case BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED:
            paintBackgroundEnabled(g, c, width, height);
            paintRestoreEnabled(g, c, width, height);
            break;
        case BACKGROUND_MINIMIZED_MOUSEOVER:
        case BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED:
            paintBackgroundHover(g, c, width, height);
            paintRestoreHover(g, c, width, height);
            break;
        case BACKGROUND_MINIMIZED_PRESSED:
        case BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED:
            paintBackgroundPressed(g, c, width, height);
            paintRestorePressed(g, c, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, enabled);
    }

    private void paintBackgroundHover(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, hover);
    }

    private void paintBackgroundPressed(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, pressed);
    }

    private void paintMinimizeEnabled(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintEnabled(g, c, width, height);
    }

    private void paintMinimizeHover(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintHover(g, c, width, height);
    }

    private void paintMinimizePressed(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintPressed(g, c, width, height);
    }

    private void paintRestoreEnabled(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintEnabled(g, c, width, height);
    }

    private void paintRestoreHover(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintHover(g, c, width, height);
    }

    private void paintRestorePressed(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintPressed(g, c, width, height);
    }

    private void paintBackground(Graphics2D g, JComponent c, int width, int height, ButtonColors colors) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(colors.top);
        g.drawLine(1, 0, width - 2, 0);
        g.setColor(colors.leftOuter);
        g.drawLine(0, 0, 0, height - 4);
        g.drawLine(1, height - 3, 1, height - 3);
        g.setColor(colors.leftInner);
        g.drawLine(1, 1, 1, height - 4);
        g.drawLine(2, height - 3, 2, height - 3);
        g.setColor(colors.edge);
        g.drawLine(width - 1, 0, width - 1, height - 2);
        g.drawLine(3, height - 2, width - 2, height - 2);
        g.setColor(colors.edgeShade);
        g.drawLine(2, height - 2, 2, height - 2);
        g.setColor(colors.shadow);
        g.drawLine(4, height - 1, width - 1, height - 1);
        g.setColor(colors.interior);
        g.fillRect(1, 1, width - 1, height - 3);
        g.drawLine(3, height - 3, width - 2, height - 3);
    }

    private static class ButtonColors {
        public Color top;
        public Color leftOuter;
        public Color leftInner;
        public Color edge;
        public Color edgeShade;
        public Color shadow;
        public Color interior;

        public ButtonColors(Color top, Color leftOuter, Color leftInner, Color edge, Color edgeShade, Color shadow, Color interior) {
            this.top = top;
            this.leftOuter = leftOuter;
            this.leftInner = leftInner;
            this.edge = edge;
            this.edgeShade = edgeShade;
            this.shadow = shadow;
            this.interior = interior;
        }

    }
}
