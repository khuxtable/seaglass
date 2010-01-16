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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;

import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass TextField UI delegate.
 * 
 * Based on SynthTextFieldUI, but we need to set preferred sizes.
 */
public class SeaGlassTextFieldUI extends BasicTextFieldUI implements SynthUI, FocusListener {
    private SynthStyle         style;

    private static final State isSearchState = new TextFieldIsSearchState();

    /**
     * Creates a UI for a JTextField.
     * 
     * @param c
     *            the text field
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTextFieldUI();
    }

    public SeaGlassTextFieldUI() {
        super();
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

    static void updateStyle(JTextComponent comp, SeaGlassContext context, String prefix) {
        SeaGlassStyle style = (SeaGlassStyle) context.getStyle();

        Color color = comp.getCaretColor();
        if (color == null || color instanceof UIResource) {
            comp.setCaretColor((Color) style.get(context, prefix + ".caretForeground"));
        }

        Color fg = comp.getForeground();
        if (fg == null || fg instanceof UIResource) {
            fg = style.getColorForState(context, ColorType.TEXT_FOREGROUND);
            if (fg != null) {
                comp.setForeground(fg);
            }
        }

        Object ar = style.get(context, prefix + ".caretAspectRatio");
        if (ar instanceof Number) {
            comp.putClientProperty("caretAspectRatio", ar);
        }

        context.setComponentState(SELECTED | FOCUSED);

        Color s = comp.getSelectionColor();
        if (s == null || s instanceof UIResource) {
            comp.setSelectionColor(style.getColor(context, ColorType.TEXT_BACKGROUND));
        }

        Color sfg = comp.getSelectedTextColor();
        if (sfg == null || sfg instanceof UIResource) {
            comp.setSelectedTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        context.setComponentState(DISABLED);

        Color dfg = comp.getDisabledTextColor();
        if (dfg == null || dfg instanceof UIResource) {
            comp.setDisabledTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        Insets margin = comp.getMargin();
        if (margin == null || margin instanceof UIResource) {
            margin = (Insets) style.get(context, prefix + ".margin");

            if (margin == null) {
                // Some places assume margins are non-null.
                margin = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
            comp.setMargin(margin);
        }

        Caret caret = comp.getCaret();
        if (caret instanceof UIResource) {
            Object o = style.get(context, prefix + ".caretBlinkRate");
            if (o != null && o instanceof Integer) {
                Integer rate = (Integer) o;
                caret.setBlinkRate(rate.intValue());
            }
        }
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        int state = SeaGlassLookAndFeel.getComponentState(c);
        return state;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        paintBackground(context, g, c);
        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the interface. This is routed to the paintSafely method under the
     * guarantee that the model won't change from the view of this thread while
     * it's rendering (if the associated model is derived from
     * AbstractDocument). This enables the model to potentially be updated
     * asynchronously.
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        super.paint(g, getComponent());
    }

    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTextFieldBorder(context, g, x, y, w, h);
    }

    protected void paintBackground(Graphics g) {
        // Overriden to do nothing, all our painting is done from update/paint.
    }

    /**
     * This method gets called when a bound property is changed on the
     * associated JTextComponent. This is a hook which UI implementations may
     * change to reflect how the UI displays bound properties of JTextComponent
     * subclasses. This is implemented to do nothing (i.e. the response to
     * properties in JTextComponent itself are handled prior to calling this
     * method).
     * 
     * @param evt
     *            the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent) evt.getSource());
        }
        super.propertyChange(evt);
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
        updateStyle((JTextComponent) getComponent());
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
        updateStyle((JTextComponent) c);
    }

    public Dimension getPreferredSize(JComponent c) {
        // The following code has been derived from BasicTextUI.
        Document doc = ((JTextComponent) c).getDocument();
        Insets i = c.getInsets();
        Dimension d = c.getSize();
        View rootView = getRootView((JTextComponent) c);

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).readLock();
        }
        try {
            if ((d.width > (i.left + i.right)) && (d.height > (i.top + i.bottom))) {
                rootView.setSize(d.width - i.left - i.right, d.height - i.top - i.bottom);
            } else if (d.width == 0 && d.height == 0) {
                // Probably haven't been layed out yet, force some sort of
                // initial sizing.
                rootView.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            d.width = (int) Math.min((long) rootView.getPreferredSpan(View.X_AXIS) + (long) i.left + (long) i.right, Integer.MAX_VALUE);
            d.height = (int) Math.min((long) rootView.getPreferredSpan(View.Y_AXIS) + (long) i.top + (long) i.bottom, Integer.MAX_VALUE);
        } finally {
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).readUnlock();
            }
        }

        // Fix: The preferred width is always two pixels too small
        // on a Mac.
        d.width += 2;

        // The search field height needs to be odd to look good.
        if (isSearchState.isInState(c) && (d.height & 1) == 0) {
            d.height--;
        }

        return d;
    }
}
