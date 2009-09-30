/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id: SeaGlassLookAndFeel.java 123 2009-09-28 07:38:51Z kathryn@kathrynhuxtable.org $
 */
package com.seaglass;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.DefaultMenuLayout;
import javax.swing.plaf.synth.SynthContext;

import sun.swing.plaf.synth.SynthUI;

/**
 * SeaGlassPopupMenuUI.
 */
public class SeaGlassPopupMenuUI extends BasicPopupMenuUI implements PropertyChangeListener, SynthUI {
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
        }
        updateStyle(popupMenu);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SeaGlassStyle oldStyle = style;
        style = (SeaGlassStyle) SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
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
