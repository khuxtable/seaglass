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

/**
 * @author Kathryn Huxtable
 *
 */

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.JTextComponent;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;

public class SeaGlassTextAreaUI extends BasicTextAreaUI implements SeaglassUI, FocusListener {
    private SeaGlassStyle style;

    /**
     * Creates a UI for a JTextArea.
     *
     * @param ta a text area
     * @return the UI
     */
    public static ComponentUI createUI(JComponent ta) {
        return new SeaGlassTextAreaUI();
    }

    public void focusGained(FocusEvent e) {
        getComponent().repaint();
    }

    public void focusLost(FocusEvent e) {
        getComponent().repaint();
    }
    
    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();
        updateStyle((JTextComponent)getComponent());
        getComponent().addFocusListener(this);
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(getComponent(), ENABLED);

        getComponent().putClientProperty("caretAspectRatio", null);
        getComponent().removeFocusListener(this);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        super.uninstallDefaults();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
    }

    private void updateStyle(JTextComponent comp) {
        SeaGlassContext context = getContext(comp, ENABLED);
        SeaGlassStyle oldStyle = style;

        style = (SeaGlassStyle) SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            SeaGlassTextFieldUI.updateStyle(comp, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c,
                    SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTextAreaBackground(context,
                          g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        super.paint(g, getComponent());
    }

    protected void paintBackground(Graphics g) {
        JTextComponent c = getComponent();
        SeaGlassContext context = getContext(c);
        context.getPainter().paintTextAreaBackground(context,
            g, 0, 0, c.getWidth(), c.getHeight());
   }

    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintTextAreaBorder(context, g, x, y, w, h);
    }

    /**
     * This method gets called when a bound property is changed
     * on the associated JTextComponent.  This is a hook
     * which UI implementations may change to reflect how the
     * UI displays bound properties of JTextComponent subclasses.
     * This is implemented to rebuild the View when the
     * <em>WrapLine</em> or the <em>WrapStyleWord</em> property changes.
     *
     * @param evt the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent)evt.getSource());
        }
        super.propertyChange(evt);
    }
  
}
