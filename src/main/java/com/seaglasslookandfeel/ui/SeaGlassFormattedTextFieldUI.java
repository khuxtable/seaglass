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
package com.seaglasslookandfeel.ui;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * Sea Glass FormattedTextField UI delegate.
 *
 * <p>Based on SynthFormattedTextFieldUI, but needs to extend
 * SeaGlassTextFieldUI instead.</p>
 */
public class SeaGlassFormattedTextFieldUI extends SeaGlassTextFieldUI {

    /**
     * Creates a UI for a JFormattedTextField.
     *
     * @param  c the formatted text field
     *
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassFormattedTextFieldUI();
    }

    /**
     * Fetches the name used as a key to lookup properties through the
     * UIManager. This is used as a prefix to all the standard text properties.
     *
     * @return the name "FormattedTextField"
     */
    protected String getPropertyPrefix() {
        return "FormattedTextField";
    }

    /**
     * @see com.seaglasslookandfeel.ui.SeaGlassTextFieldUI#paintBackground(com.seaglasslookandfeel.SeaGlassContext,
     *      java.awt.Graphics, javax.swing.JComponent)
     */
    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintFormattedTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    /**
     * @see com.seaglasslookandfeel.ui.SeaGlassTextFieldUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintFormattedTextFieldBorder(context, g, x, y, w, h);
    }
}
