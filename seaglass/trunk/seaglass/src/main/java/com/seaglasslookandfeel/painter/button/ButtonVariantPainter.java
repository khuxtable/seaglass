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
package com.seaglasslookandfeel.painter.button;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractCommonColorsPainter;
import com.seaglasslookandfeel.painter.ButtonPainter.Which;

/**
 * Base class for button variant painters. This knows about focus states, and
 * accepts the paint context and other state information from
 * {@link org.seaglass.painter.ButtonPainter}.
 *
 * @author Kathryn Huxtable
 */
public abstract class ButtonVariantPainter extends AbstractCommonColorsPainter {

    /** The button state to paint. */
    protected Which state;

    /** The PaintContext containing cache information. */
    protected PaintContext ctx;

    /** Are we in a focused state? */
    protected boolean focused;

    /**
     * Sets the button painter properties. Initializes the focus state.
     *
     * @param state the button state.
     * @param ctx   the paint context.
     */
    public ButtonVariantPainter(Which state, PaintContext ctx) {
        super();
        this.state = state;
        this.ctx   = ctx;

        switch (state) {

        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_SELECTED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            focused = true;
            break;

        default:
            focused = false;
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys);

    /**
     * {@inheritDoc}
     */
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = new Object[] {};

        return extendedCacheKeys;
    }

    /**
     * {@inheritDoc}
     */
    protected final PaintContext getPaintContext() {
        return ctx;
    }
}
