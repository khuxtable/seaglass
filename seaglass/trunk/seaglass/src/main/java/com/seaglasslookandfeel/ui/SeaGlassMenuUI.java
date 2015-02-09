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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import sun.swing.MenuItemLayoutHelper;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.util.SeaGlassGraphicsUtils;
import com.seaglasslookandfeel.util.SeaGlassMenuItemLayoutHelper;

/**
 * @author Kathryn Huxtable
 *
 */
public class SeaGlassMenuUI extends BasicMenuUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;
    private SynthStyle accStyle;

    /**
     * Creates a new UI object for the given component.
     *
     * @param x
     *            component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassMenuUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults() {
        updateStyle(menuItem);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        menuItem.addPropertyChangeListener(this);
    }

    private void updateStyle(JMenuItem mi) {
        SynthStyle oldStyle = style;
        SeaGlassContext context = getContext(mi, ENABLED);

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (oldStyle != style) {
            String prefix = getPropertyPrefix();
            defaultTextIconGap = style.getInt(context, prefix + ".textIconGap", 4);
            if (menuItem.getMargin() == null || (menuItem.getMargin() instanceof UIResource)) {
                Insets insets = (Insets) style.get(context, prefix + ".margin");

                if (insets == null) {
                    // Some places assume margins are non-null.
                    insets = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                }
                menuItem.setMargin(insets);
            }
            acceleratorDelimiter = style.getString(context, prefix + ".acceleratorDelimiter", "+");

            if (MenuItemLayoutHelper.useCheckAndArrow(menuItem)) {
                checkIcon = style.getIcon(context, prefix + ".checkIcon");
                arrowIcon = style.getIcon(context, prefix + ".arrowIcon");
            } else {
                // Not needed in this case
                checkIcon = null;
                arrowIcon = null;
            }

            ((JMenu) menuItem).setDelay(style.getInt(context, prefix + ".delay", 200));
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();

        SeaGlassContext accContext = getContext(mi, Region.MENU_ITEM_ACCELERATOR, ENABLED);

        accStyle = SeaGlassLookAndFeel.updateStyle(accContext, this);
        accContext.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        // Remove values from the parent's Client Properties.
        JComponent p = MenuItemLayoutHelper.getMenuItemParent((JMenuItem) c);
        if (p != null) {
            p.putClientProperty(SeaGlassMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(menuItem, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        SeaGlassContext accContext = getContext(menuItem, Region.MENU_ITEM_ACCELERATOR, ENABLED);
        accStyle.uninstallDefaults(accContext);
        accContext.dispose();
        accStyle = null;

        super.uninstallDefaults();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        menuItem.removePropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    SeaGlassContext getContext(JComponent c, int state) {
        Region region = SeaGlassLookAndFeel.getRegion(c);
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, accStyle, state);
    }

    private int getComponentState(JComponent c) {
        int state;

        if (!c.isEnabled()) {
            return DISABLED;
        }
        if (menuItem.isArmed()) {
            state = MOUSE_OVER;
        } else {
            state = SeaGlassLookAndFeel.getComponentState(c);
        }
        if (menuItem.isSelected()) {
            state |= SELECTED;
        }
        return state;
    }

    private int getComponentState(JComponent c, Region region) {
        return getComponentState(c);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        SeaGlassContext context = getContext(c);
        SeaGlassContext accContext = getContext(c, Region.MENU_ITEM_ACCELERATOR);
        Dimension value = SeaGlassGraphicsUtils.getPreferredMenuItemSize(context, accContext, c, checkIcon, arrowIcon, defaultTextIconGap,
            acceleratorDelimiter, MenuItemLayoutHelper.useCheckAndArrow(menuItem), getPropertyPrefix());
        context.dispose();
        accContext.dispose();
        return value;
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
        context.getPainter().paintMenuBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
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
        SeaGlassContext accContext = getContext(menuItem, Region.MENU_ITEM_ACCELERATOR);
        // Refetch the appropriate check indicator for the current state
        String prefix = getPropertyPrefix();
        Icon checkIcon = style.getIcon(context, prefix + ".checkIcon");
        Icon arrowIcon = style.getIcon(context, prefix + ".arrowIcon");
        SeaGlassGraphicsUtils.paint(context, accContext, g, checkIcon, arrowIcon, acceleratorDelimiter, defaultTextIconGap,
            getPropertyPrefix());
        accContext.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintMenuBorder(context, g, x, y, w, h);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)
                || (e.getPropertyName().equals("ancestor") && UIManager.getBoolean("Menu.useMenuBarForTopLevelMenus"))) {
            updateStyle((JMenu) e.getSource());
        }
    }
}
