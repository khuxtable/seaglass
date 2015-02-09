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
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.DefaultMenuLayout;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.util.PlatformUtils;
import com.seaglasslookandfeel.util.WindowUtils;

/**
 * SeaGlassPopupMenuUI.
 * 
 * Based on SynthPopupMenuUI by Georges Saab, David Karlton, and Arnaud Weber.
 * 
 * @see javax.swing.plaf.synth.SynthPopupMenuUI
 */
public class SeaGlassPopupMenuUI extends BasicPopupMenuUI implements PropertyChangeListener, SeaglassUI {
    /**
     * Maximum size of the text portion of the children menu items.
     */
    private int           maxTextWidth;

    /**
     * Maximum size of the text for the acclerator portion of the children menu
     * items.
     */
    private int           maxAcceleratorWidth;

    /*
     * Maximum icon and text offsets of the children menu items.
     */
    private int           maxTextOffset;
    private int           maxIconOffset;

    private SeaGlassStyle style;

    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassPopupMenuUI();
    }

    public void installDefaults() {
        if (popupMenu.getLayout() == null || popupMenu.getLayout() instanceof UIResource) {
            popupMenu.setLayout(new DefaultMenuLayout(popupMenu, BoxLayout.Y_AXIS));
            popupMenu.setOpaque(false);
        }
        updateStyle(popupMenu);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        Window window = SwingUtilities.getWindowAncestor(popupMenu);
        if (PlatformUtils.isMac() && window != null) {
            WindowUtils.makeWindowNonOpaque(window);
        }
        SeaGlassStyle oldStyle = style;
        
        SynthStyle s = SeaGlassLookAndFeel.updateStyle(context, this);
        if (s instanceof SeaGlassStyle) {
            style = (SeaGlassStyle) s;
            if (style != oldStyle) {
                if (oldStyle != null) {
                    uninstallKeyboardActions();
                    installKeyboardActions();
                }
            }
        }
        context.dispose();
    }

    protected void installListeners() {
        super.installListeners();
        popupMenu.addPropertyChangeListener(this);
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(popupMenu, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        if (popupMenu.getLayout() instanceof UIResource) {
            popupMenu.setLayout(null);
        }
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        popupMenu.removePropertyChangeListener(this);
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

    /**
     * Resets the max text and accerator widths, text and icon offsets.
     */
    void resetAlignmentHints() {
        maxTextWidth = maxAcceleratorWidth = maxTextOffset = maxIconOffset = 0;
    }

    /**
     * Adjusts the width needed to display the maximum menu item string.
     * 
     * @param width
     *            Text width.
     * @return max width
     */
    int adjustTextWidth(int width) {
        maxTextWidth = Math.max(maxTextWidth, width);
        return maxTextWidth;
    }

    /**
     * Adjusts the width needed to display the maximum accelerator.
     * 
     * @param width
     *            Text width.
     * @return max width
     */
    int adjustAcceleratorWidth(int width) {
        maxAcceleratorWidth = Math.max(maxAcceleratorWidth, width);
        return maxAcceleratorWidth;
    }

    /**
     * Maximum size to display text of children menu items.
     */
    int getMaxTextWidth() {
        return maxTextWidth;
    }

    /**
     * Maximum size needed to display accelerators of children menu items.
     */
    int getMaxAcceleratorWidth() {
        return maxAcceleratorWidth;
    }

    /**
     * Adjusts the text offset needed to align text horizontally.
     * 
     * @param offset
     *            Text offset
     * @return max offset
     */
    int adjustTextOffset(int offset) {
        maxTextOffset = Math.max(maxTextOffset, offset);
        return maxTextOffset;
    }

    /**
     * Adjusts the icon offset needed to align icons horizontally
     * 
     * @param offset
     *            Icon offset
     * @return max offset
     */
    int adjustIconOffset(int offset) {
        maxIconOffset = Math.max(maxIconOffset, offset);
        return maxIconOffset;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintPopupMenuBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintPopupMenuBorder(context, g, x, y, w, h);
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle(popupMenu);
        }
    }
}
