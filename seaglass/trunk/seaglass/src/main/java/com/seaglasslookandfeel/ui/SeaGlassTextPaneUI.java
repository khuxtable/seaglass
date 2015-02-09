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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * SeaGlass TextPaneUI delegate.
 * 
 * Based on SynthTextPaneUI by Georges Saab and David Karlton.
 * 
 * The only reason this exists is that we had to modify SynthTextPaneUI.
 * 
 * @see javax.swing.plaf.synth.SynthTextPaneUI
 */
public class SeaGlassTextPaneUI extends SeaGlassEditorPaneUI {

    /**
     * Creates a UI for the JTextPane.
     *
     * @param c the JTextPane object
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTextPaneUI();
    }

    /**
     * Fetches the name used as a key to lookup properties through the
     * UIManager.  This is used as a prefix to all the standard
     * text properties.
     *
     * @return the name ("TextPane")
     */
    @Override
    protected String getPropertyPrefix() {
        return "TextPane";
    }

    /**
     * Installs the UI for a component.  This does the following
     * things.
     * <ol>
     * <li>
     * Sets opaqueness of the associated component according to its style,
     * if the opaque property has not already been set by the client program.
     * <li>
     * Installs the default caret and highlighter into the
     * associated component. These properties are only set if their
     * current value is either {@code null} or an instance of
     * {@link UIResource}.
     * <li>
     * Attaches to the editor and model.  If there is no
     * model, a default one is created.
     * <li>
     * Creates the view factory and the view hierarchy used
     * to represent the model.
     * </ol>
     *
     * @param c the editor component
     * @see javax.swing.plaf.basic.BasicTextUI#installUI
     * @see ComponentUI#installUI
     */
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        updateForeground(c.getForeground());
        updateFont(c.getFont());
    }

    /**
     * This method gets called when a bound property is changed
     * on the associated JTextComponent.  This is a hook
     * which UI implementations may change to reflect how the
     * UI displays bound properties of JTextComponent subclasses.
     * If the font, foreground or document has changed, the
     * the appropriate property is set in the default style of
     * the document.
     *
     * @param evt the property change event
     */
    @Override
    protected void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);

        String name = evt.getPropertyName();

        if (name.equals("foreground")) {
            updateForeground((Color)evt.getNewValue());
        } else if (name.equals("font")) {
            updateFont((Font)evt.getNewValue());
        } else if (name.equals("document")) {
            JComponent comp = getComponent();
            updateForeground(comp.getForeground());
            updateFont(comp.getFont());
        }
    }

    /**
     * Update the color in the default style of the document.
     *
     * @param color the new color to use or null to remove the color attribute
     *              from the document's style
     */
    private void updateForeground(Color color) {
        StyledDocument doc = (StyledDocument)getComponent().getDocument();
        Style style = doc.getStyle(StyleContext.DEFAULT_STYLE);

        if (style == null) {
            return;
        }

        if (color == null) {
            style.removeAttribute(StyleConstants.Foreground);
        } else {
            StyleConstants.setForeground(style, color);
        }
    }

    /**
     * Update the font in the default style of the document.
     *
     * @param font the new font to use or null to remove the font attribute
     *             from the document's style
     */
    private void updateFont(Font font) {
        StyledDocument doc = (StyledDocument)getComponent().getDocument();
        Style style = doc.getStyle(StyleContext.DEFAULT_STYLE);

        if (style == null) {
            return;
        }

        if (font == null) {
            style.removeAttribute(StyleConstants.FontFamily);
            style.removeAttribute(StyleConstants.FontSize);
            style.removeAttribute(StyleConstants.Bold);
            style.removeAttribute(StyleConstants.Italic);
        } else {
            StyleConstants.setFontFamily(style, font.getName());
            StyleConstants.setFontSize(style, font.getSize());
            StyleConstants.setBold(style, font.isBold());
            StyleConstants.setItalic(style, font.isItalic());
        }
    }

    @Override
    void paintBackground(SynthContext context, Graphics g, JComponent c) {
        ((SeaGlassContext)context).getPainter().paintTextPaneBackground(context, g, 0, 0,
                                                  c.getWidth(), c.getHeight());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintTextPaneBorder(context, g, x, y, w, h);
    }
}
