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
 * $Id: SeaGlassRootPaneUI.java 862 2010-01-22 16:02:13Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.ui;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass Panel UI delegate.
 * 
 * Based on SynthPanelUI, but sets the panel's opaque property to false.
 */
public class SeaGlassPanelUI extends BasicPanelUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private boolean    originalOpacity;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassPanelUI();
    }

    public void installUI(JComponent c) {
        JPanel p = (JPanel) c;

        super.installUI(c);
        installListeners(p);
    }

    public void uninstallUI(JComponent c) {
        JPanel p = (JPanel) c;

        uninstallListeners(p);
        super.uninstallUI(c);
    }

    protected void installListeners(JPanel p) {
        p.addPropertyChangeListener(this);
    }

    protected void uninstallListeners(JPanel p) {
        p.removePropertyChangeListener(this);
    }

    protected void installDefaults(JPanel p) {
        originalOpacity = p.isOpaque();
        p.setOpaque(false);
        updateStyle(p);
    }

    protected void uninstallDefaults(JPanel p) {
        SeaGlassContext context = getContext(p, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        p.setOpaque(originalOpacity);
    }

    private void updateStyle(JPanel c) {
        SeaGlassContext context = getContext(c, ENABLED);
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintPanelBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        // do actual painting
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintPanelBorder(context, g, x, y, w, h);
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(pce)) {
            updateStyle((JPanel) pce.getSource());
        }
    }
}
