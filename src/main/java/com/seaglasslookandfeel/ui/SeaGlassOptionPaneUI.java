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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.DefaultLookup;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * SeaGlass OptionPaneUI delegate.
 * 
 * Based on SynthOptionPaneUI by Georges Saab and David Karlton.
 * 
 * The only reason this exists is that we had to modify SynthOptionPaneUI.
 * 
 * @see javax.swing.plaf.synth.SynthOptionPaneUI
 */
public class SeaGlassOptionPaneUI extends BasicOptionPaneUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;

    /**
     * Creates a new UI object for the given component.
     *
     * @param x
     *            component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassOptionPaneUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults() {
        updateStyle(optionPane);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        optionPane.addPropertyChangeListener(this);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            minimumSize = (Dimension) style.get(context, "OptionPane.minimumSize");
            if (minimumSize == null) {
                minimumSize = new Dimension(262, 90);
            }
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
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(optionPane, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        optionPane.removePropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installComponents() {
        optionPane.add(createMessageArea());

        Container separator = createSeparator();
        if (separator != null) {
            optionPane.add(separator);
            SeaGlassContext context = getContext(optionPane, ENABLED);
            optionPane.add(Box.createVerticalStrut(context.getStyle().getInt(context, "OptionPane.separatorPadding", 6)));
            context.dispose();
        }
        optionPane.add(createButtonArea());
        optionPane.applyComponentOrientation(optionPane.getComponentOrientation());
    }

    /**
     * @inheritDoc
     */
    @Override
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Notifies this UI delegate to repaint the specified component. This method
     * paints the component background, then calls the
     * {@link #paint(SynthContext,Graphics)} method.
     *
     * <p>
     * In general, this method does not need to be overridden by subclasses. All
     * Look and Feel rendering code should reside in the {@code paint} method.
     *
     * @param g
     *            the {@code Graphics} object used for painting
     * @param c
     *            the component being painted
     * @see #paint(SynthContext,Graphics)
     */
    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintOptionPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the specified component according to the Look and Feel.
     * <p>
     * This method is not used by Synth Look and Feel. Painting is handled by
     * the {@link #paint(SynthContext,Graphics)} method.
     *
     * @param g
     *            the {@code Graphics} object used for painting
     * @param c
     *            the component being painted
     * @see #paint(SynthContext,Graphics)
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the specified component. This implementation does nothing.
     *
     * @param context
     *            context for the component being painted
     * @param g
     *            the {@code Graphics} object used for painting
     * @see #update(Graphics,JComponent)
     */
    protected void paint(SynthContext context, Graphics g) {
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintOptionPaneBorder(context, g, x, y, w, h);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JOptionPane) e.getSource());
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected boolean getSizeButtonsToSameWidth() {
        return DefaultLookup.getBoolean(optionPane, this, "OptionPane.sameSizeButtons", true);
    }

    /**
     * Called from {@link #installComponents} to create a {@code Container}
     * containing the body of the message. The icon is the created by calling
     * {@link #addIcon}.
     */
    @Override
    protected Container createMessageArea() {
        JPanel top = new JPanel();
        top.setName("OptionPane.messageArea");
        top.setLayout(new BorderLayout());

        /* Fill the body. */
        Container body = new JPanel(new GridBagLayout());
        Container realBody = new JPanel(new BorderLayout());

        body.setName("OptionPane.body");
        realBody.setName("OptionPane.realBody");

        if (getIcon() != null) {
            JPanel sep = new JPanel();
            sep.setName("OptionPane.separator");
            sep.setPreferredSize(new Dimension(15, 1));
            realBody.add(sep, BorderLayout.BEFORE_LINE_BEGINS);
        }
        realBody.add(body, BorderLayout.CENTER);

        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = cons.gridy = 0;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridheight = 1;

        SeaGlassContext context = getContext(optionPane, ENABLED);
        cons.anchor = context.getStyle().getInt(context, "OptionPane.messageAnchor", GridBagConstraints.CENTER);
        context.dispose();

        cons.insets = new Insets(0, 0, 3, 0);

        addMessageComponents(body, cons, getMessage(), getMaxCharactersPerLineCount(), false);
        top.add(realBody, BorderLayout.CENTER);

        addIcon(top);
        return top;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected Container createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        separator.setName("OptionPane.separator");
        return separator;
    }
}
