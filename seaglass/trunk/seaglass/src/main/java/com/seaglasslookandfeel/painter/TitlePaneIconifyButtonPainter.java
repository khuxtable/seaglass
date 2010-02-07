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

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

import com.seaglasslookandfeel.painter.titlepaneforegound.TitlePaneButtonForegroundPainter;
import com.seaglasslookandfeel.painter.titlepaneforegound.TitlePaneIconifyButtonForegroundPainter;
import com.seaglasslookandfeel.painter.titlepaneforegound.TitlePaneRestoreButtonForegroundPainter;

/**
 * Title pane iconify button painter implementation.
 */
public final class TitlePaneIconifyButtonPainter extends TitlePaneButtonPainter {

    /**
     * Control states.
     */
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_DISABLED, BACKGROUND_MOUSEOVER, BACKGROUND_PRESSED, BACKGROUND_ENABLED_WINDOWNOTFOCUSED,
        BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED, BACKGROUND_PRESSED_WINDOWNOTFOCUSED,

        BACKGROUND_MINIMIZED_DISABLED, BACKGROUND_MINIMIZED_ENABLED, BACKGROUND_MINIMIZED_MOUSEOVER, BACKGROUND_MINIMIZED_PRESSED,
        BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED, BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED,
        BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED,
    }

    private ButtonColors enabled = new ButtonColors(white16, black66, white4c, black66, black33, white33, transparentColor);
    private ButtonColors hover   = new ButtonColors(white68, black66, white8c, black66, black33, white46, white59);
    private ButtonColors pressed = new ButtonColors(gray_9b_82, black66, gray_a9_9e, black66, black33, white33, gray_e6_59);

    private TitlePaneIconifyButtonForegroundPainter iconifyPainter = new TitlePaneIconifyButtonForegroundPainter();
    private TitlePaneButtonForegroundPainter        restorePainter = new TitlePaneRestoreButtonForegroundPainter();

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new TitlePaneIconifyButtonPainter object.
     *
     * @param state the component state.
     */
    public TitlePaneIconifyButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.FIXED_SIZES);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Paint the background enabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintBackgroundEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, enabled);
    }

    /**
     * Paint the background mouse-over state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintBackgroundHover(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, hover);
    }

    /**
     * Paint the background pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintBackgroundPressed(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, c, width, height, pressed);
    }

    /**
     * Paint the foreground minimized button enabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintMinimizeEnabled(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintEnabled(g, c, width, height);
    }

    /**
     * Paint the foreground minimized button mouse-over state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintMinimizeHover(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintHover(g, c, width, height);
    }

    /**
     * Paint the foreground minimize button pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintMinimizePressed(Graphics2D g, JComponent c, int width, int height) {
        iconifyPainter.paintPressed(g, c, width, height);
    }

    /**
     * Paint the foreground restore button enabled state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintRestoreEnabled(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintEnabled(g, c, width, height);
    }

    /**
     * Paint the foreground restore button mouse-over state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintRestoreHover(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintHover(g, c, width, height);
    }

    /**
     * Paint the foreground restore button pressed state.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     */
    private void paintRestorePressed(Graphics2D g, JComponent c, int width, int height) {
        restorePainter.paintPressed(g, c, width, height);
    }

    /**
     * Paint the background of the button using the specified colors.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the component.
     * @param width  the width of the component.
     * @param height the height of the component.
     * @param colors the color set to use to paint the button.
     */
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

    /**
     * A class encapsulating the colors used for a button.
     */
    private static class ButtonColors {

        /** Top line color. */
        public Color top;

        /** Left edge color. */
        public Color leftOuter;

        /** Left edge inside color. */
        public Color leftInner;

        /** Main edge color. */
        public Color edge;

        /** Main edge shade color. */
        public Color edgeShade;

        /** Main shadow color. */
        public Color shadow;

        /** Interior color. */
        public Color interior;

        /**
         * Creates a new ButtonColors object.
         *
         * @param top       the top line color.
         * @param leftOuter the left edge color.
         * @param leftInner the left edge inside color.
         * @param edge      the main edge color.
         * @param edgeShade the main edge shade color.
         * @param shadow    the shadow color.
         * @param interior  the interior color.
         */
        public ButtonColors(Color top, Color leftOuter, Color leftInner, Color edge, Color edgeShade, Color shadow, Color interior) {
            this.top       = top;
            this.leftOuter = leftOuter;
            this.leftInner = leftInner;
            this.edge      = edge;
            this.edgeShade = edgeShade;
            this.shadow    = shadow;
            this.interior  = interior;
        }

    }
}
