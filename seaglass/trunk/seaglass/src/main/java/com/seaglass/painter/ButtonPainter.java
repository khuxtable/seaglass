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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Button painter. This paints both regular and toggle buttons because they look
 * the same except for the state.
 * 
 * @author Kathryn Huxtable
 */
public final class ButtonPainter extends AbstractImagePainter<ButtonPainter.Which> {
    public static enum Which {
        BACKGROUND_DEFAULT,
        BACKGROUND_DEFAULT_FOCUSED,
        BACKGROUND_MOUSEOVER_DEFAULT,
        BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED,
        BACKGROUND_PRESSED_DEFAULT,
        BACKGROUND_PRESSED_DEFAULT_FOCUSED,
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_MOUSEOVER_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_SELECTED,
        BACKGROUND_SELECTED_FOCUSED,
        BACKGROUND_PRESSED_SELECTED,
        BACKGROUND_PRESSED_SELECTED_FOCUSED,
        BACKGROUND_DISABLED_SELECTED
    };

    private static final Insets    insets    = new Insets(7, 7, 7, 7);
    private static final Dimension dimension = new Dimension(86, 28);
    private static final CacheMode cacheMode = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH      = Double.POSITIVE_INFINITY;
    private static final Double    maxV      = Double.POSITIVE_INFINITY;

    private ImageIcon              segmentedFirst;
    private ImageIcon              segmentedMiddle;
    private ImageIcon              segmentedLast;

    /**
     * Create a new ButtonPainter.
     * 
     * @param ctx
     *            the PaintContext to be used.
     * @param state
     *            the state of the button to be painted.
     */
    public ButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
    }

    protected String getImageName(Which state) {
        String name = getInternalImageName(state);

        try {
            segmentedFirst = getImage(name + "_segmented_first");
            segmentedMiddle = getImage(name + "_segmented_middle");
            segmentedLast = getImage(name + "_segmented_last");
        } catch (Exception e) {
            segmentedFirst = getImage(name);
            segmentedMiddle = segmentedFirst;
            segmentedLast = segmentedFirst;
        }

        return name;
    }

    private String getInternalImageName(Which state) {
        switch (state) {
        case BACKGROUND_DEFAULT:
            return "button_default";
        case BACKGROUND_DEFAULT_FOCUSED:
            return "button_default";
        case BACKGROUND_MOUSEOVER_DEFAULT:
            return "button_default";
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
            return "button_default";
        case BACKGROUND_PRESSED_DEFAULT:
            return "button_default_pressed";
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            return "button_default_pressed";
        case BACKGROUND_DISABLED:
            return "button_disabled";
        case BACKGROUND_ENABLED:
            return "button_enabled";
        case BACKGROUND_FOCUSED:
            return "button_enabled";
        case BACKGROUND_MOUSEOVER:
            return "button_enabled";
        case BACKGROUND_MOUSEOVER_FOCUSED:
            return "button_enabled";
        case BACKGROUND_PRESSED:
            return "button_pressed";
        case BACKGROUND_PRESSED_FOCUSED:
            return "button_pressed";
        case BACKGROUND_SELECTED:
            return "button_default";
        case BACKGROUND_SELECTED_FOCUSED:
            return "button_default";
        case BACKGROUND_PRESSED_SELECTED:
            return "button_pressed";
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return "button_pressed";
        case BACKGROUND_DISABLED_SELECTED:
            return "button_disabled_selected";
        }
        // Catch-all for anything we don't specify.
        return "button_enabled";
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if ("segmented".equals(c.getClientProperty("JButton.buttonType"))) {
            String position = (String) c.getClientProperty("JButton.segmentPosition");
            if ("first".equals(position)) {
                segmentedFirst.paintIcon(c, g, 0, 0);
                return;
            } else if ("middle".equals(position)) {
                segmentedMiddle.paintIcon(c, g, 0, 0);
                return;
            } else if ("last".equals(position)) {
                segmentedLast.paintIcon(c, g, 0, 0);
                return;
            }
        }
        image.paintIcon(c, g, 0, 0);
    }
}
