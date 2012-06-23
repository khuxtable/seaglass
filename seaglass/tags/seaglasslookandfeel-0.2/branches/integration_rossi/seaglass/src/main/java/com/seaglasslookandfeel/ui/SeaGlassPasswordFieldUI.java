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

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * Sea Glass PasswordField UI delegate.
 *
 * <p>Based on SynthPasswordTextFieldUI, but needs to extend SeaGlassTextFieldUI
 * instead.</p>
 */
public class SeaGlassPasswordFieldUI extends SeaGlassTextFieldUI {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        Character echoChar = (Character) UIManager.get("PasswordField.echoChar");
        if (echoChar != null) {
            LookAndFeel.installProperty(getComponent(), "echoChar", echoChar);    
        }
    }

    /**
     * Creates a UI for a JPasswordField.
     *
     * @param  c the JPasswordField
     *
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassPasswordFieldUI();
    }

    /**
     * Fetches the name used as a key to look up properties through the
     * UIManager. This is used as a prefix to all the standard text properties.
     *
     * @return the name ("PasswordField")
     */
    protected String getPropertyPrefix() {
        return "PasswordField";
    }

    /**
     * Creates a view (PasswordView) for an element.
     *
     * @param  elem the element
     *
     * @return the view
     */
    public View create(Element elem) {
        return new PasswordView(elem);
    }

    /**
     * @see com.seaglasslookandfeel.ui.SeaGlassTextFieldUI#paintBackground(com.seaglasslookandfeel.SeaGlassContext,
     *      java.awt.Graphics, javax.swing.JComponent)
     */
    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintPasswordFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    /**
     * @see com.seaglasslookandfeel.ui.SeaGlassTextFieldUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintPasswordFieldBorder(context, g, x, y, w, h);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#installKeyboardActions()
     */
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        ActionMap map = SwingUtilities.getUIActionMap(getComponent());

        if (map != null && map.get(DefaultEditorKit.selectWordAction) != null) {
            Action a = map.get(DefaultEditorKit.selectLineAction);

            if (a != null) {
                map.put(DefaultEditorKit.selectWordAction, a);
            }
        }
    }
}
