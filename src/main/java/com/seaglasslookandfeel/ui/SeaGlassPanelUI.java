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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * Sea Glass Panel UI delegate.
 *
 * <p>Based on SynthPanelUI, but sets the panel's opaque property to false.</p>
 */
public class SeaGlassPanelUI extends BasicPanelUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;

    private boolean originalOpacity;

    /**
     * Create a UI delegate.
     *
     * @param  c the component for the delegate.
     *
     * @return the UI delegate.
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassPanelUI();
    }

    /**
     * @see javax.swing.plaf.basic.BasicPanelUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        JPanel p = (JPanel) c;

        super.installUI(c);
        installListeners(p);
    }

    /**
     * @see javax.swing.plaf.basic.BasicPanelUI#uninstallUI(javax.swing.JComponent)
     */
    public void uninstallUI(JComponent c) {
        JPanel p = (JPanel) c;

        uninstallListeners(p);
        super.uninstallUI(c);
    }

    /**
     * Install the listeners.
     *
     * @param p the panel.
     */
    protected void installListeners(JPanel p) {
        p.addPropertyChangeListener(this);
    }

    /**
     * Uninstall the listeners.
     *
     * @param p the panel.
     */
    protected void uninstallListeners(JPanel p) {
        p.removePropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.basic.BasicPanelUI#installDefaults(javax.swing.JPanel)
     */
    protected void installDefaults(JPanel p) {
        this.originalOpacity = p.isOpaque();
        updateStyle(p);
    }

    /**
     * @see javax.swing.plaf.basic.BasicPanelUI#uninstallDefaults(javax.swing.JPanel)
     */
    protected void uninstallDefaults(JPanel p) {
        SeaGlassContext context = getContext(p, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        // Restore original opacity if not changed by the code.
        LookAndFeel.installProperty(p, "opaque", originalOpacity);
    }

    /**
     * Update the Synth style if a property changes.
     *
     * @param c the panel.
     */
    private void updateStyle(JPanel c) {
        SeaGlassContext context = getContext(c, ENABLED);

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();

        // Set the opacity according to whether the background has been set.
        // Don't set it if the user has already set it.
        LookAndFeel.installProperty(c, "opaque", !(c.getBackground() instanceof UIResource));
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * Get the Synth context.
     *
     * @param  c     the panel.
     * @param  state the Synth state.
     *
     * @return the context.
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Get the Synth state of the panel.
     *
     * @param  c the panel.
     *
     * @return the state.
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintPanelBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);

        context.dispose();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * Paint the panel. Nothing to do here. The panel is painted by its
     * components.
     *
     * @param context the Synth context.
     * @param g       the Graphics context.
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        // do actual painting
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintPanelBorder(context, g, x, y, w, h);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(pce)) {
            updateStyle((JPanel) pce.getSource());
        } else if (pce.getPropertyName() == "background") {
            updateStyle((JPanel) pce.getSource());
        }
    }
}
