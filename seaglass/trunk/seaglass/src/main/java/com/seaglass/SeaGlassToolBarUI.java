/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Aqvavit Pluggable Look and Feel.
 *
 * Aqvavit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * Aqvavit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Aqvavit.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToolBarUI;

import com.seaglass.painter.Painter;
import com.seaglass.util.SeaGlassUtils;
import com.seaglass.util.WindowUtils;

/**
 * AqvavitToolBarUI implementation.
 */
public class SeaGlassToolBarUI extends BasicToolBarUI {
    /**
     * An array of WeakReferences that point to JComponents. This will contain
     * instances of JToolBars and JMenuBars and is used to find
     * JToolBars/JMenuBars that border each other.
     */
    private static java.util.List<WeakReference<?>> components = new ArrayList<WeakReference<?>>();

    /**
     * This protected field is implemenation specific. Do not access directly or
     * override. Use the create method instead.
     * 
     * @see #createContainerListener
     */
    protected ContainerListener                     contListener;

    /**
     * This protected field is implemenation specific. Do not access directly or
     * override. Use the create method instead.
     * 
     * @see #createRolloverListener
     */
    protected PropertyChangeListener                rolloverListener;

    private static Border                           nonRolloverBorder;

    /**
     * Registers the specified component.
     */
    synchronized static void register(JComponent c) {
        if (c == null) {
            // Exception is thrown as convenience for callers that are
            // typed to throw an NPE.
            throw new NullPointerException("JComponent must be non-null");
        }
        components.add(new WeakReference<JComponent>(c));
    }

    /**
     * Unregisters the specified component.
     */
    synchronized static void unregister(JComponent c) {
        for (int counter = components.size() - 1; counter >= 0; counter--) {
            // Search for the component, removing any flushed references
            // along the way.
            /* WeakReference<?> ref = */components.get(counter);
            Object target = components.get(counter).get();

            if (target == c || target == null) {
                components.remove(counter);
            }
        }
    }

    /**
     * Finds a previously registered component of class <code>target</code> that
     * shares the JRootPane ancestor of <code>from</code>.
     */
    synchronized static Object findRegisteredComponentOfType(JComponent from, Class<?> target) {
        JRootPane rp = SwingUtilities.getRootPane(from);
        if (rp != null) {
            for (int counter = components.size() - 1; counter >= 0; counter--) {
                Object component = components.get(counter).get();

                if (component == null) {
                    // WeakReference has gone away, remove the WeakReference
                    components.remove(counter);
                } else if (target.isInstance(component) && SwingUtilities.getRootPane((Component) component) == rp) {
                    return component;
                }
            }
        }
        return null;
    }

    /**
     * Returns true if the passed in JMenuBar is above a horizontal JToolBar.
     */
    static boolean doesMenuBarBorderToolBar(JMenuBar c) {
        JToolBar tb = (JToolBar) SeaGlassToolBarUI.findRegisteredComponentOfType(c, JToolBar.class);
        if (tb != null && tb.getOrientation() == JToolBar.HORIZONTAL) {
            JRootPane rp = SwingUtilities.getRootPane(c);
            Point point = new Point(0, 0);
            point = SwingUtilities.convertPoint(c, point, rp);
            int menuX = point.x;
            int menuY = point.y;
            point.x = point.y = 0;
            point = SwingUtilities.convertPoint(tb, point, rp);
            return (point.x == menuX && menuY + c.getHeight() == point.y && c.getWidth() == tb.getWidth());
        }
        return false;
    }

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassToolBarUI();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        WindowUtils.installJComponentRepainterOnWindowFocusChanged(c);
        // installUnifiedToolBarBorder(c);
        register(c);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        nonRolloverBorder = null;
        unregister(c);
    }

    protected void installListeners() {
        super.installListeners();

        contListener = createContainerListener();
        if (contListener != null) {
            toolBar.addContainerListener(contListener);
        }
        rolloverListener = createRolloverListener();
        if (rolloverListener != null) {
            toolBar.addPropertyChangeListener(rolloverListener);
        }
    }

    protected void uninstallListeners() {
        super.uninstallListeners();

        if (contListener != null) {
            toolBar.removeContainerListener(contListener);
        }
        rolloverListener = createRolloverListener();
        if (rolloverListener != null) {
            toolBar.removePropertyChangeListener(rolloverListener);
        }
    }

    protected Border createRolloverBorder() {
        return super.createRolloverBorder();
    }

    protected Border createNonRolloverBorder() {
        return super.createNonRolloverBorder();
    }

    /**
     * Creates a non rollover border for Toggle buttons in the toolbar.
     */
    private Border createNonRolloverToggleBorder() {
        return createNonRolloverBorder();
    }

    protected void setBorderToNonRollover(Component c) {
        if (c instanceof JToggleButton && !(c instanceof JCheckBox)) {
            // 4735514, 4886944: The method createNonRolloverToggleBorder() is
            // private in BasicToolBarUI so we can't override it. We still need
            // to call super from this method so that it can save away the
            // original border and then we install ours.

            // Before calling super we get a handle to the old border, because
            // super will install a non-UIResource border that we can't
            // distinguish from one provided by an application.
            JToggleButton b = (JToggleButton) c;
            Border border = b.getBorder();
            super.setBorderToNonRollover(c);
            if (border instanceof UIResource) {
                if (nonRolloverBorder == null) {
                    nonRolloverBorder = createNonRolloverToggleBorder();
                }
                b.setBorder(nonRolloverBorder);
            }
        } else {
            super.setBorderToNonRollover(c);
        }
    }

    /**
     * Creates a container listener that will be added to the JToolBar. If this
     * method returns null then it will not be added to the toolbar.
     * 
     * @return an instance of a <code>ContainerListener</code> or null
     */
    protected ContainerListener createContainerListener() {
        return null;
    }

    /**
     * Creates a property change listener that will be added to the JToolBar. If
     * this method returns null then it will not be added to the toolbar.
     * 
     * @return an instance of a <code>PropertyChangeListener</code> or null
     */
    protected PropertyChangeListener createRolloverListener() {
        return null;
    }

    protected MouseInputListener createDockingListener() {
        return new SeaGlassDockingListener(toolBar);
    }

    protected void setDragOffset(Point p) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (dragWindow == null) {
                dragWindow = createDragWindow(toolBar);
            }
            dragWindow.setOffset(p);
        }
    }

    /**
     * If necessary paints the background of the component, then invokes
     * <code>paint</code>.
     * 
     * @param g
     *            Graphics to paint to
     * @param c
     *            JComponent painting on
     * @throws NullPointerException
     *             if <code>g</code> or <code>c</code> is null
     * @see javax.swing.plaf.ComponentUI#update
     * @see javax.swing.plaf.ComponentUI#paint
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public void update(Graphics g, JComponent c) {
        if (g == null) {
            throw new NullPointerException("graphics must be non-null");
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (c.isOpaque() && (c.getBackground() instanceof UIResource) && ((JToolBar) c).getOrientation() == JToolBar.HORIZONTAL
                && UIManager.get("ToolBar.gradient") != null) {
            Painter<Component> painter = (Painter<Component>) UIManager.get("ToolBar.gradient");
            painter.paint(g2d, c, c.getWidth(), c.getHeight());
        } else {
            super.update(g2d, c);
        }
    }

    protected class SeaGlassDockingListener extends DockingListener {
        private boolean pressedInBumps = false;

        public SeaGlassDockingListener(JToolBar t) {
            super(t);
        }

        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (!toolBar.isEnabled()) {
                return;
            }
            pressedInBumps = false;
            Rectangle bumpRect = new Rectangle();

            if (toolBar.getOrientation() == JToolBar.HORIZONTAL) {
                int x = SeaGlassUtils.isLeftToRight(toolBar) ? 0 : toolBar.getSize().width - 14;
                bumpRect.setBounds(x, 0, 14, toolBar.getSize().height);
            } else { // vertical
                bumpRect.setBounds(0, 0, toolBar.getSize().width, 14);
            }
            if (bumpRect.contains(e.getPoint())) {
                pressedInBumps = true;
                Point dragOffset = e.getPoint();
                if (!SeaGlassUtils.isLeftToRight(toolBar)) {
                    dragOffset.x -= (toolBar.getSize().width - toolBar.getPreferredSize().width);
                }
                setDragOffset(dragOffset);
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (pressedInBumps) {
                super.mouseDragged(e);
            }
        }
    } // end class AqvavitDockingListener
}
