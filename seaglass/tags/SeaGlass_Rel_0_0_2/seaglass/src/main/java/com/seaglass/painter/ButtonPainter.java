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

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Button painter. This paints both regular and toggle buttons because they look
 * the same except for the state.
 * 
 * @author Kathryn Huxtable
 */
public final class ButtonPainter extends AbstractImagePainter {

    // Integers representing the available states that this painter will paint.
    // These are used when creating a new instance of ButtonPainter to determine
    // which region/state is being painted by that instance.
    public static final int BACKGROUND_DEFAULT                   = 1;
    public static final int BACKGROUND_DEFAULT_FOCUSED           = 2;
    public static final int BACKGROUND_MOUSEOVER_DEFAULT         = 3;
    public static final int BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED = 4;
    public static final int BACKGROUND_PRESSED_DEFAULT           = 5;
    public static final int BACKGROUND_PRESSED_DEFAULT_FOCUSED   = 6;
    public static final int BACKGROUND_DISABLED                  = 7;
    public static final int BACKGROUND_ENABLED                   = 8;
    public static final int BACKGROUND_FOCUSED                   = 9;
    public static final int BACKGROUND_MOUSEOVER                 = 10;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED         = 11;
    public static final int BACKGROUND_PRESSED                   = 12;
    public static final int BACKGROUND_PRESSED_FOCUSED           = 13;
    public static final int BACKGROUND_SELECTED                  = 14;
    public static final int BACKGROUND_SELECTED_FOCUSED          = 15;
    public static final int BACKGROUND_PRESSED_SELECTED          = 16;
    public static final int BACKGROUND_PRESSED_SELECTED_FOCUSED  = 17;
    public static final int BACKGROUND_DISABLED_SELECTED         = 18;

    private ImageIcon       segmentedFirst;
    private ImageIcon       segmentedMiddle;
    private ImageIcon       segmentedLast;

    /**
     * Create a new ButtonPainter.
     * 
     * @param ctx
     *            the PaintContext to be used.
     * @param state
     *            the state of the button to be painted.
     */
    public ButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
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

    private String getInternalImageName(int state) {
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
            return "button";
        case BACKGROUND_FOCUSED:
            return "button";
        case BACKGROUND_MOUSEOVER:
            return "button";
        case BACKGROUND_MOUSEOVER_FOCUSED:
            return "button";
        case BACKGROUND_PRESSED:
            return "button_pressed";
        case BACKGROUND_PRESSED_FOCUSED:
            return "button_pressed";
        case BACKGROUND_SELECTED:
            return "button_selected";
        case BACKGROUND_SELECTED_FOCUSED:
            return "button_selected";
        case BACKGROUND_PRESSED_SELECTED:
            return "button_pressed";
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return "button_pressed";
        case BACKGROUND_DISABLED_SELECTED:
            return "button_disabled_selected";
        }
        // Catch-all for anything we don't specify.
        return "button";
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
