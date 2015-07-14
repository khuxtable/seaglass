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

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.button.ButtonVariantPainter;
import com.seaglasslookandfeel.painter.button.SegmentedButtonPainter;
import com.seaglasslookandfeel.painter.button.TexturedButtonPainter;

/**
 * Button painter. This paints both regular and toggle buttons because they look
 * the same except for the state. This actually delegates to a
 * SegmentedButtonPainter, which does the actual painting based on client
 * properties.
 *
 * @author Kathryn Huxtable
 */
public final class ButtonPainter extends AbstractRegionPainter {

    /**
     * Control state.
     */
    public static enum Which {
        BACKGROUND_DEFAULT, BACKGROUND_DEFAULT_FOCUSED, BACKGROUND_PRESSED_DEFAULT, BACKGROUND_PRESSED_DEFAULT_FOCUSED, BACKGROUND_DISABLED,
        BACKGROUND_ENABLED, BACKGROUND_FOCUSED, BACKGROUND_PRESSED, BACKGROUND_PRESSED_FOCUSED, BACKGROUND_SELECTED,
        BACKGROUND_SELECTED_FOCUSED, BACKGROUND_PRESSED_SELECTED, BACKGROUND_PRESSED_SELECTED_FOCUSED, BACKGROUND_DISABLED_SELECTED
    }

    private PaintContext ctx;

    private ButtonVariantPainter standard;
    private ButtonVariantPainter textured;

    /**
     * Create a new ButtonPainter.
     *
     * @param state the state of the button to be painted.
     */
    public ButtonPainter(Which state) {
        super();

        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        standard = new SegmentedButtonPainter(state, ctx);
        textured = new TexturedButtonPainter(state, ctx);
    }

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
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        getButtonPainter(c).doPaint(g, c, width, height, extendedCacheKeys);
    }

    /**
     * Determine the button painter variant from the component's client
     * property.
     *
     * @param  c the component.
     *
     * @return the button painter variant.
     */
    private ButtonVariantPainter getButtonPainter(JComponent c) {
        Object               buttonType = c.getClientProperty("JButton.buttonType");
        ButtonVariantPainter button     = standard;

        if ("textured".equals(buttonType) || "segmentedTextured".equals(buttonType)) {
            button = textured;
        }

        return button;
    }
}
