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

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * SeaGlass EditorPaneUI delegate.
 * 
 * Based on SynthEditorPaneUI by Georges Saab and David Karlton.
 * 
 * The only reason this exists is that we had to modify SynthEditorPaneUI.
 * 
 * @see javax.swing.plaf.synth.SynthEditorPaneUI
 */
public class SeaGlassEditorPaneUI extends BasicEditorPaneUI implements SeaglassUI {
    private SynthStyle style;
    /*
     * I would prefer to use UIResource instad of this.
     * Unfortunately Boolean is a final class
     */
    private Boolean localTrue = Boolean.TRUE;

    /**
     * Creates a new UI object for the given component.
     *
     * @param c component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassEditorPaneUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();
        JComponent c = getComponent();
        Object clientProperty =
            c.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
        if (clientProperty == null) {
            c.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, localTrue);
        }
        updateStyle(getComponent());
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(getComponent(), ENABLED);
        JComponent c = getComponent();
        c.putClientProperty("caretAspectRatio", null);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        Object clientProperty =
            c.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
        if (clientProperty == localTrue) {
            c.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
                                             Boolean.FALSE);
        }
        super.uninstallDefaults();
    }

    /**
     * This method gets called when a bound property is changed
     * on the associated JTextComponent.  This is a hook
     * which UI implementations may change to reflect how the
     * UI displays bound properties of JTextComponent subclasses.
     * This is implemented to rebuild the ActionMap based upon an
     * EditorKit change.
     *
     * @param evt the property change event
     */
    @Override
    protected void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent)evt.getSource());
        }
        super.propertyChange(evt);
    }

    private void updateStyle(JTextComponent comp) {
        SeaGlassContext context = getContext(comp, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            SeaGlassTextFieldUI.updateStyle(comp, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
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

    /**
     * Notifies this UI delegate to repaint the specified component.
     * This method paints the component background, then calls
     * the {@link #paint(SynthContext,Graphics)} method.
     *
     * <p>In general, this method does not need to be overridden by subclasses.
     * All Look and Feel rendering code should reside in the {@code paint} method.
     *
     * @param g the {@code Graphics} object used for painting
     * @param c the component being painted
     * @see #paint(SynthContext,Graphics)
     */
    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        paintBackground(context, g, c);
        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the specified component.
     *
     * @param context context for the component being painted
     * @param g the {@code Graphics} object used for painting
     * @see #update(Graphics,JComponent)
     */
    protected void paint(SynthContext context, Graphics g) {
        super.paint(g, getComponent());
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void paintBackground(Graphics g) {
        // Overriden to do nothing, all our painting is done from update/paint.
    }

    void paintBackground(SynthContext context, Graphics g, JComponent c) {
        ((SeaGlassContext)context).getPainter().paintEditorPaneBackground(context, g, 0, 0,
                                                  c.getWidth(), c.getHeight());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintEditorPaneBorder(context, g, x, y, w, h);
    }
}
