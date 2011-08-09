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

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Shape;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassSplitPaneDivider;

/**
 * SeaGlassSplitPaneUI implementation.
 * 
 * Based on Synth's SplitPaneUI.
 * 
 * @see javax.swing.plaf.synth.SynthSplitPaneUI
 */
public class SeaGlassSplitPaneUI extends BasicSplitPaneUI implements PropertyChangeListener, SeaglassUI {
    /**
     * Keys to use for forward focus traversal when the JComponent is managing
     * focus.
     */
    private static Set managingFocusForwardTraversalKeys;

    /**
     * Keys to use for backward focus traversal when the JComponent is managing
     * focus.
     */
    private static Set managingFocusBackwardTraversalKeys;

    /**
     * Style for the JSplitPane.
     */
    private SynthStyle style;
    /**
     * Style for the divider.
     */
    private SynthStyle dividerStyle;

    /**
     * Creates a new SeaGlassSplitPaneUI instance
     */
    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassSplitPaneUI();
    }

    /**
     * Installs the UI defaults.
     */
    protected void installDefaults() {
        updateStyle(splitPane);

        setOrientation(splitPane.getOrientation());
        setContinuousLayout(splitPane.isContinuousLayout());

        resetLayoutManager();

        /*
         * Install the nonContinuousLayoutDivider here to avoid having to
         * add/remove everything later.
         */
        if (nonContinuousLayoutDivider == null) {
            setNonContinuousLayoutDivider(createDefaultNonContinuousLayoutDivider(), true);
        } else {
            setNonContinuousLayoutDivider(nonContinuousLayoutDivider, true);
        }

        // focus forward traversal key
        if (managingFocusForwardTraversalKeys == null) {
            managingFocusForwardTraversalKeys = new HashSet();
            managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        }
        splitPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, managingFocusForwardTraversalKeys);
        // focus backward traversal key
        if (managingFocusBackwardTraversalKeys == null) {
            managingFocusBackwardTraversalKeys = new HashSet();
            managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
        }
        splitPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, managingFocusBackwardTraversalKeys);
    }

    private void updateStyle(JSplitPane splitPane) {
        SeaGlassContext context = getContext(splitPane, Region.SPLIT_PANE_DIVIDER, ENABLED);
        SynthStyle oldDividerStyle = dividerStyle;
        dividerStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();

        context = getContext(splitPane, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            Object value = style.get(context, "SplitPane.size");
            if (value == null) {
                value = new Integer(6);
            }
            LookAndFeel.installProperty(splitPane, "dividerSize", value);

            value = style.get(context, "SplitPane.oneTouchExpandable");
            if (value != null) {
                LookAndFeel.installProperty(splitPane, "oneTouchExpandable", value);
            }

            if (divider != null) {
                splitPane.remove(divider);
                divider.setDividerSize(splitPane.getDividerSize());
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        if (style != oldStyle || dividerStyle != oldDividerStyle) {
            // Only way to force BasicSplitPaneDivider to reread the
            // necessary properties.
            if (divider != null) {
                splitPane.remove(divider);
            }
            divider = createDefaultDivider();
            divider.setBasicSplitPaneUI(this);
            splitPane.add(divider, JSplitPane.DIVIDER);
        }
        context.dispose();
    }

    /**
     * Installs the event listeners for the UI.
     */
    protected void installListeners() {
        super.installListeners();
        splitPane.addPropertyChangeListener(this);
    }

    /**
     * Uninstalls the UI defaults.
     */
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(splitPane, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        context = getContext(splitPane, Region.SPLIT_PANE_DIVIDER, ENABLED);
        dividerStyle.uninstallDefaults(context);
        context.dispose();
        dividerStyle = null;

        super.uninstallDefaults();
    }

    /**
     * Uninstalls the event listeners for the UI.
     */
    protected void uninstallListeners() {
        super.uninstallListeners();
        splitPane.removePropertyChangeListener(this);
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

    public SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        if (region == Region.SPLIT_PANE_DIVIDER) {
            return SeaGlassContext.getContext(SeaGlassContext.class, c, region, dividerStyle, state);
        }
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    private int getComponentState(JComponent c, Region subregion) {
        int state = SeaGlassLookAndFeel.getComponentState(c);

        if (divider.isMouseOver()) {
            state |= MOUSE_OVER;
        }
        return state;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JSplitPane) e.getSource());
        }
    }

    /**
     * Creates the default divider.
     */
    public BasicSplitPaneDivider createDefaultDivider() {
        SeaGlassSplitPaneDivider divider = new SeaGlassSplitPaneDivider(this);

        divider.setDividerSize(splitPane.getDividerSize());
        return divider;
    }

    protected Component createDefaultNonContinuousLayoutDivider() {
        return new Canvas() {
            public void paint(Graphics g) {
                paintDragDivider(g, 0, 0, getWidth(), getHeight());
            }
        };
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintSplitPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        // This is done to update package private variables in
        // BasicSplitPaneUI
        super.paint(g, splitPane);
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintSplitPaneBorder(context, g, x, y, w, h);
    }

    private void paintDragDivider(Graphics g, int x, int y, int w, int h) {
        SeaGlassContext context = getContext(splitPane, Region.SPLIT_PANE_DIVIDER);
        context.setComponentState(((context.getComponentState() | MOUSE_OVER) ^ MOUSE_OVER) | PRESSED);
        Shape oldClip = g.getClip();
        g.clipRect(x, y, w, h);
        context.getPainter().paintSplitPaneDragDivider(context, g, x, y, w, h, splitPane.getOrientation());
        g.setClip(oldClip);
        context.dispose();
    }

    public void finishedPaintingChildren(JSplitPane jc, Graphics g) {
        if (jc == splitPane && getLastDragLocation() != -1 && !isContinuousLayout() && !draggingHW) {
            if (jc.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                paintDragDivider(g, getLastDragLocation(), 0, dividerSize - 1, splitPane.getHeight() - 1);
            } else {
                paintDragDivider(g, 0, getLastDragLocation(), splitPane.getWidth() - 1, dividerSize - 1);
            }
        }
    }
}
