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
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.plaf.ColorUIResource;

import com.seaglasslookandfeel.effect.SeaGlassInternalShadowEffect;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * TextComponentPainter implementation.
 */
public final class TextComponentPainter extends AbstractCommonColorsPainter {

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED, BACKGROUND_SOLID_DISABLED, BACKGROUND_SOLID_ENABLED, BACKGROUND_SELECTED, BORDER_DISABLED,
        BORDER_FOCUSED, BORDER_ENABLED,
    }

    private Color defaultBackground = decodeColor("seaGlassLightBackground");
    // Rossi: All round corners like all other text components and added support for separators lines
    private Color lineSeparatorEnabled = new ColorUIResource(0xebf5fc);

    private SeaGlassInternalShadowEffect internalShadow = new SeaGlassInternalShadowEffect();

    private Which              state;
    private PaintContext       ctx;
    private CommonControlState type;
    private boolean            focused;

    // Array of current component colors, updated in each paint call
    private Object[]           componentColors;

    /**
     * Creates a new TextComponentPainter object.
     *
     * @param state the control state to paint.
     */
    public TextComponentPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES);

        type    = (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_SOLID_DISABLED || state == Which.BORDER_DISABLED)
            ? CommonControlState.DISABLED : CommonControlState.ENABLED;
        focused = (state == Which.BORDER_FOCUSED);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        // Populate componentColors array with colors calculated in getExtendedCacheKeys call.
        componentColors = extendedCacheKeys;

        int x = focusInsets.left;
        int y = focusInsets.top;

        width  -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        switch (state) {

        case BACKGROUND_DISABLED:
        case BACKGROUND_ENABLED:
        case BACKGROUND_SELECTED:
            paintBackground(g, c, x, y, width, height);
            break;

        case BACKGROUND_SOLID_DISABLED:
        case BACKGROUND_SOLID_ENABLED:
            paintBackgroundSolid(g, c, x, y, width, height);
            break;

        case BORDER_DISABLED:
        case BORDER_ENABLED:
        case BORDER_FOCUSED:
            paintBorder(g, c, x, y, width, height);
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = null;

        if (state == Which.BACKGROUND_ENABLED) {
            extendedCacheKeys = new Object[] { getComponentColor(c, "background", defaultBackground, 0.0f, 0.0f, 0) };
        }

        return extendedCacheKeys;
    }

    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * Paint the background of an editable control.
     *
     * @param g      DOCUMENT ME!
     * @param c      DOCUMENT ME!
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackground(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();

        if (state == Which.BACKGROUND_ENABLED) {
            color = (Color) componentColors[0];
        } else if (type == CommonControlState.DISABLED) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        }

        Shape s = shapeGenerator.createRoundRectangle(x + 1, y + 1, width - 2, height - 2, CornerSize.BORDER);

        g.setPaint(color);
        g.fill(s);
        if (isPaintLineSeperators(c)) {
            paintLineSeparator(g, c, width, height);
        }

    }

    /**
     * Test if we should also paint the line seperators.
     * @param c
     * @return
     */
    private boolean isPaintLineSeperators(JComponent c) {
        boolean paintLines = c instanceof JTextArea;

        // Global settings
        String globalOverride = System.getProperty("SeaGlass.JTextArea.drawLineSeparator");
        if (globalOverride != null && globalOverride.length() > 0) {
            paintLines = Boolean.valueOf(globalOverride);
        }
        
        // Settings per component
        Boolean overrideProperty = (Boolean) c.getClientProperty("SeaGlass.JTextArea.drawLineSeparator");
        if (overrideProperty != null) {
            paintLines = overrideProperty;   
        }
        return paintLines;
    }

    /**
     * Paint the background of an uneditable control, e.g. a JLabel.
     *
     * @param g      DOCUMENT ME!
     * @param c      DOCUMENT ME!
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBackgroundSolid(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        Color color = c.getBackground();

        if (type == CommonControlState.DISABLED) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
        }

        Shape s = shapeGenerator.createRoundRectangle(x-2, y-2, width+4, height+4, CornerSize.BORDER);
        g.setPaint(color);
        g.fill(s);
        if (isPaintLineSeperators(c)) {
            paintLineSeparator(g, c, width, height);
        }
        
    }

    /**
     * @param g
     * @param c
     * @param width
     * @param height
     */
    private void paintLineSeparator(Graphics2D g, JComponent c, int width, int height) {
        g.setPaint(lineSeparatorEnabled);
        int lineYIncrement = g.getFontMetrics(c.getFont()).getHeight();
        int lineY = lineYIncrement+c.getInsets().top-1;
        while (lineY < height) {
            g.drawLine(c.getInsets().left, lineY, width-c.getInsets().right, lineY);
            lineY += lineYIncrement;
        }
    }

    /**
     * Paint the border.
     *
     * @param g      DOCUMENT ME!
     * @param c      DOCUMENT ME!
     * @param x      DOCUMENT ME!
     * @param y      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    private void paintBorder(Graphics2D g, JComponent c, int x, int y, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape   s;

        if (focused) {
            s = shapeGenerator.createRoundRectangle(x - 2, y - 2, width + 3, height + 3, CornerSize.OUTER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.draw(s);
            s = shapeGenerator.createRoundRectangle(x - 1, y - 1, width + 1, height + 1, CornerSize.INNER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.draw(s);
        }

        if (type != CommonControlState.DISABLED) {
            s = shapeGenerator.createRoundRectangle(x + 1, x + 1, width - 2, height - 2, CornerSize.BORDER);
            internalShadow.fill(g, s, false, true);
        }

        s = shapeGenerator.createRoundRectangle(x, y, width - 1, height - 1, CornerSize.BORDER);
        g.setPaint(getTextBorderPaint(type, !focused && useToolBarColors));
        g.draw(s);
    }
}
