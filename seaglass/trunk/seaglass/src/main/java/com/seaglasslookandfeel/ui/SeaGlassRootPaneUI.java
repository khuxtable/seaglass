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
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassBorder;
import com.seaglasslookandfeel.component.SeaGlassTitlePane;
import com.seaglasslookandfeel.painter.ContentPanePainter;
import com.seaglasslookandfeel.painter.SeaGlassPainter;
import com.seaglasslookandfeel.painter.util.ShapeGenerator;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerStyle;
import com.seaglasslookandfeel.state.RootPaneWindowFocusedState;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.util.PlatformUtils;
import com.seaglasslookandfeel.util.WindowUtils;

/**
 * SeaGlassRootPaneUI implementation.
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassRootPaneUI extends BasicRootPaneUI implements SeaglassUI {

    /**
     * Set to <code>true</code> if we want a unified toolbar look with a
     * textured background.
     */
    public static final String UNIFIED_TOOLBAR_LOOK = "SeaGlass.UnifiedToolbarLook";

    /** The amount of space (in pixels) that the cursor is changed on. */
    private static final int CORNER_DRAG_WIDTH = 16;

    /** Region from edges that dragging is active from. */
    private static final int BORDER_DRAG_THICKNESS = 5;

    private static final State   isWindowFocused = new RootPaneWindowFocusedState();
    private static final SeaGlassPainter contentActive   = new ContentPanePainter(ContentPanePainter.Which.BACKGROUND_ENABLED_WINDOWFOCUSED);
    private static final SeaGlassPainter contentInactive = new ContentPanePainter(ContentPanePainter.Which.BACKGROUND_ENABLED);

    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] cursorMapping = new int[] {
        Cursor.NW_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        Cursor.N_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.W_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.E_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        Cursor.S_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR
    };

    private Color transparentColor = UIManager.getColor("seaGlassTransparent");

    private SynthStyle style;

    /** Window the <code>JRootPane</code> is in. */
    private Window window;

    /** Paint a textured background if <code>true</code>. */
    private boolean paintTextured;

    /**
     * <code>JComponent</code> providing window decorations. This will be null
     * if not providing window decorations.
     */
    private JComponent titlePane;

    /**
     * <code>MouseInputListener</code> that is added to the parent <code>
     * Window</code> the <code>JRootPane</code> is contained in.
     */
    private MouseInputListener mouseInputListener;

    /**
     * The <code>LayoutManager</code> that is set on the <code>JRootPane</code>.
     */
    private LayoutManager layoutManager;

    /**
     * <code>LayoutManager</code> of the <code>JRootPane</code> before we
     * replaced it.
     */
    private LayoutManager savedOldLayout;

    /** <code>JRootPane</code> providing the look and feel for. */
    private JRootPane root;

    /**
     * <code>Cursor</code> used to track the cursor set by the user. This is
     * initially <code>Cursor.DEFAULT_CURSOR</code>.
     */
    private Cursor lastCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private WindowListener windowListener;

    /**
     * Creates a UI for a <code>JRootPane</code>.
     *
     * @param  c the JRootPane the RootPaneUI will be created for
     *
     * @return the RootPaneUI implementation for the passed in JRootPane
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassRootPaneUI();
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * Get the SynthContext.
     *
     * @param  c     the component.
     * @param  state the state.
     *
     * @return the SynthContext.
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Get the component state.
     *
     * @param  c the component.
     *
     * @return the state.
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Invokes supers implementation of <code>installUI</code> to install the
     * necessary state onto the passed in <code>JRootPane</code> to render the
     * metal look and feel implementation of <code>RootPaneUI</code>. If the
     * <code>windowDecorationStyle</code> property of the <code>JRootPane</code>
     * is other than <code>JRootPane.NONE</code>, this will add a custom <code>
     * Component</code> to render the widgets to <code>JRootPane</code>, as well
     * as installing a custom <code>Border</code> and <code>LayoutManager</code>
     * on the <code>JRootPane</code>.
     *
     * @param c the JRootPane to install state onto.
     */
    public void installUI(JComponent c) {
        super.installUI(c);

        root = (JRootPane) c;

        updateTextured();

        int       style  = root.getWindowDecorationStyle();
        Container parent = root.getParent();

        if (parent != null && (parent instanceof JFrame || parent instanceof JDialog) && style != JRootPane.NONE) {
            installClientDecorations(root);
        }
    }

    /**
     * Set the textured properties.
     */
    private void updateTextured() {
        // Need the content pane to not be opaque.
        paintTextured = (root.getClientProperty(UNIFIED_TOOLBAR_LOOK) == Boolean.TRUE);

        if (paintTextured && PlatformUtils.isMac()) {
            if (root.isValid()) {
                throw new IllegalArgumentException("This method only works if the given JRootPane has not yet been realized.");
            }

            root.putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);

            // Need the content pane to not be opaque.
            LookAndFeel.installProperty((JComponent) root.getContentPane(), "opaque", Boolean.FALSE);
        } else {
            root.putClientProperty("apple.awt.brushMetalLook", null);
            // FIXME Restore original content pane opacity.
        }
    }

    /**
     * Invokes supers implementation to uninstall any of its state. This will
     * also reset the <code>LayoutManager</code> of the <code>JRootPane</code>.
     * If a <code>Component</code> has been added to the <code>JRootPane</code>
     * to render the window decoration style, this method will remove it.
     * Similarly, this will revert the Border and LayoutManager of the <code>
     * JRootPane</code> to what it was before <code>installUI</code> was
     * invoked.
     *
     * @param c the JRootPane to uninstall state from
     */
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        uninstallClientDecorations(root);

        layoutManager      = null;
        mouseInputListener = null;
        root               = null;
    }

    /**
     * @see javax.swing.plaf.basic.BasicRootPaneUI#installDefaults(javax.swing.JRootPane)
     */
    protected void installDefaults(JRootPane c) {
        updateStyle(c);
    }

    /**
     * Update te control style.
     *
     * @param c the component.
     */
    private void updateStyle(JComponent c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions((JRootPane) c);
                installKeyboardActions((JRootPane) c);
            }
        }

        context.dispose();
    }

    /**
     * Installs the appropriate <code>Border</code> onto the <code>
     * JRootPane</code>.
     *
     * @param root the root pane.
     */
    public void installBorder(JRootPane root) {
        int style = root.getWindowDecorationStyle();

        if (style == JRootPane.NONE) {
            LookAndFeel.uninstallBorder(root);
        } else {
            root.setBorder(new SeaGlassBorder(this, new Insets(0, 1, 1, 1)));
        }
    }

    /**
     * Removes any border that may have been installed.
     *
     * @param root the root pane.
     */
    private void uninstallBorder(JRootPane root) {
        LookAndFeel.uninstallBorder(root);
    }

    /**
     * Installs the necessary Listeners on the parent <code>Window</code>, if
     * there is one.
     *
     * <p>This takes the parent so that cleanup can be done from <code>
     * removeNotify</code>, at which point the parent hasn't been reset yet.</p>
     *
     * @param root   the JRootPane.
     * @param parent The parent of the JRootPane
     */
    private void installWindowListeners(JRootPane root, Component parent) {
        if (parent instanceof Window) {
            window = (Window) parent;
        } else {
            window = SwingUtilities.getWindowAncestor(parent);
        }

        if (window != null) {
            if (mouseInputListener == null) {
                mouseInputListener = createWindowMouseInputListener(root);
            }

            window.addMouseListener(mouseInputListener);
            window.addMouseMotionListener(mouseInputListener);
            if (windowListener == null) {
                windowListener = createFocusListener();
                window.addWindowListener(windowListener);
            }
        }
    }

    /**
     * Creates the focus listener.
     *
     * @return the focus listener.
     */
    private WindowListener createFocusListener() {
        return new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                if (root != null) {
                    root.repaint();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                if (root != null) {
                    root.repaint();
                }
            }
        };
    }

    /**
     * Uninstalls the necessary Listeners on the <code>Window</code> the
     * Listeners were last installed on.
     *
     * @param root the JRootPane.
     */
    private void uninstallWindowListeners(JRootPane root) {
        if (window != null) {
            window.removeMouseListener(mouseInputListener);
            window.removeMouseMotionListener(mouseInputListener);
        }
    }

    /**
     * Installs the appropriate LayoutManager on the <code>JRootPane</code> to
     * render the window decorations.
     *
     * @param root the JRootPane.
     */
    private void installLayout(JRootPane root) {
        if (layoutManager == null) {
            layoutManager = createLayoutManager();
        }

        savedOldLayout = root.getLayout();
        root.setLayout(layoutManager);
    }

    /**
     * Uninstalls the previously installed <code>LayoutManager</code>.
     *
     * @param root the JRootPane.
     */
    private void uninstallLayout(JRootPane root) {
        if (savedOldLayout != null) {
            root.setLayout(savedOldLayout);
            savedOldLayout = null;
        }
    }

    /**
     * Installs the necessary state onto the JRootPane to render client
     * decorations. This is ONLY invoked if the <code>JRootPane</code> has a
     * decoration style other than <code>JRootPane.NONE</code>.
     *
     * @param root the JRootPane.
     */
    private void installClientDecorations(JRootPane root) {
        installBorder(root);
        if (root.getParent() instanceof JFrame || root.getParent() instanceof JDialog) {
            if (PlatformUtils.isMac()) {
                makeFrameBackgroundTransparent(root);
            } else {
                shapeWindow(root);
            }
        }

        JComponent titlePane = createTitlePane(root);

        setTitlePane(root, titlePane);
        installWindowListeners(root, root.getParent());
        installLayout(root);
        if (window != null) {
            root.revalidate();
            root.repaint();
        }
    }

    /**
     * @param root
     */
    private void makeFrameBackgroundTransparent(JRootPane root) {
        // Indicate that this frame should not make all the content
        // draggable. By default, when you set the opacity to 0 (like we do
        // below) this property automatically gets set to true. Also note
        // that this client property must be set *before* changing the
        // opacity (not sure why).

        root.putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
        WindowUtils.makeWindowNonOpaque((Window) root.getParent());
    }

    /**
     * @param root
     */
    private void shapeWindow(JRootPane root) {
        root.getParent().addComponentListener(new ComponentAdapter() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void componentResized(ComponentEvent e) {
                ShapeGenerator shapeGenerator = new ShapeGenerator();
                int width = e.getComponent().getWidth();
                int height = e.getComponent().getHeight();
                Shape s = shapeGenerator.createRoundRectangle(0, 0, width, height, CornerSize.FRAME_INNER_HIGHLIGHT,
                    CornerStyle.ROUNDED, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED);
                WindowUtils.setWindowShape(window, s);
            }
        });
    }

    /**
     * Uninstalls any state that <code>installClientDecorations</code> has
     * installed.
     *
     * <p>NOTE: This may be called if you haven't installed client decorations
     * yet (ie before <code>installClientDecorations</code> has been invoked).
     * </p>
     *
     * @param root the JRootPane.
     */
    private void uninstallClientDecorations(JRootPane root) {
        uninstallBorder(root);
        uninstallWindowListeners(root);
        setTitlePane(root, null);
        uninstallLayout(root);
        // We have to revalidate/repaint root if the style is JRootPane.NONE
        // only. When we needs to call revalidate/repaint with other styles
        // the installClientDecorations is always called after this method
        // imediatly and it will cause the revalidate/repaint at the proper
        // time.
        int style = root.getWindowDecorationStyle();

        if (style == JRootPane.NONE) {
            root.repaint();
            root.revalidate();
        }
        // Reset the cursor, as we may have changed it to a resize cursor
        if (window != null) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        window = null;
    }

    /**
     * Returns the <code>JComponent</code> to render the window decoration
     * style.
     *
     * @param  root the JRootPane.
     *
     * @return the SeaGlassTitlePane object.
     */
    private JComponent createTitlePane(JRootPane root) {
        return new SeaGlassTitlePane(root, this);
    }

    /**
     * Returns a <code>MouseListener</code> that will be added to the <code>
     * Window</code> containing the <code>JRootPane</code>.
     *
     * @param  root the JRootPane.
     *
     * @return the mouse listener.
     */
    private MouseInputListener createWindowMouseInputListener(JRootPane root) {
        return new MouseInputHandler();
    }

    /**
     * Returns a <code>LayoutManager</code> that will be set on the <code>
     * JRootPane</code>.
     *
     * @return the layout manager.
     */
    private LayoutManager createLayoutManager() {
        return new SeaGlassRootLayout();
    }

    /**
     * Sets the window title pane -- the JComponent used to provide a plaf a way
     * to override the native operating system's window title pane with one
     * whose look and feel are controlled by the plaf. The plaf creates and sets
     * this value; the default is null, implying a native operating system
     * window title pane.
     *
     * @param root      content the <code>JComponent</code> to use for the
     *                  window title pane.
     * @param titlePane the SeaGlassTitlePane.
     */
    private void setTitlePane(JRootPane root, JComponent titlePane) {
        JLayeredPane layeredPane  = root.getLayeredPane();
        JComponent   oldTitlePane = getTitlePane();

        if (oldTitlePane != null) {
            oldTitlePane.setVisible(false);
            layeredPane.remove(oldTitlePane);
        }

        if (titlePane != null) {
            layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
            titlePane.setVisible(true);
        }

        this.titlePane = titlePane;
    }

    /**
     * Returns the <code>JComponent</code> rendering the title pane. If this
     * returns null, it implies there is no need to render window decorations.
     *
     * @return the current window title pane, or null
     *
     * @see    #setTitlePane
     */
    private JComponent getTitlePane() {
        return titlePane;
    }

    /**
     * Returns the <code>JRootPane</code> we're providing the look and feel for.
     *
     * @return the JRootPane.
     */
    private JRootPane getRootPane() {
        return root;
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        if (((JRootPane) c).getWindowDecorationStyle() != JRootPane.NONE) {
            context.getPainter().paintRootPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        } else if (PlatformUtils.isMac()) {
            // We may need to paint the rootpane on a Mac if the window is
            // decorated.
            boolean   shouldPaint       = false;
            Container toplevelContainer = c.getParent();

            if (toplevelContainer instanceof JFrame) {
                shouldPaint = !((JFrame) toplevelContainer).isUndecorated();
            }

            if (shouldPaint) {
                if (!paintTextured) {
                    g.setColor(c.getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                } else if (isWindowFocused.isInState(c)) {
                    contentActive.paint((Graphics2D) g, c, c.getWidth(), c.getHeight());
                } else {
                    contentInactive.paint((Graphics2D) g, c, c.getWidth(), c.getHeight());
                }
            }
        }

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
     * Paint the object.
     *
     * @param context the SynthContext.
     * @param g       the Graphics context.
     */
    protected void paint(SynthContext context, Graphics g) {
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintRootPaneBorder(context, g, x, y, w, h);
    }

    /**
     * Invoked when a property changes. <code>AqvavitRootPaneUI</code> is
     * primarily interested in events originating from the <code>
     * JRootPane</code> it has been installed on identifying the property <code>
     * windowDecorationStyle</code>. If the <code>windowDecorationStyle</code>
     * has changed to a value other than <code>JRootPane.NONE</code>, this will
     * add a <code>Component</code> to the <code>JRootPane</code> to render the
     * window decorations, as well as installing a <code>Border</code> on the
     * <code>JRootPane</code>. On the other hand, if the <code>
     * windowDecorationStyle</code> has changed to <code>JRootPane.NONE</code>,
     * this will remove the <code>Component</code> that has been added to the
     * <code>JRootPane</code> as well resetting the Border to what it was before
     * <code>installUI</code> was invoked.
     *
     * @param e A PropertyChangeEvent object describing the event source and the
     *          property that has changed.
     */
    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);

        String propertyName = e.getPropertyName();

        if (propertyName == null) {
            return;
        }

        if (propertyName.equals("windowDecorationStyle")) {
            JRootPane root  = (JRootPane) e.getSource();
            int       style = root.getWindowDecorationStyle();

            // This is potentially more than needs to be done,
            // but it rarely happens and makes the install/uninstall process
            // simpler. AqvavitTitlePane also assumes it will be recreated if
            // the decoration style changes.
            uninstallClientDecorations(root);
            Container parent = root.getParent();

            if (parent != null && (parent instanceof JFrame || parent instanceof JDialog) && style != JRootPane.NONE) {
                installClientDecorations(root);
            }
        } else if (propertyName.equals("ancestor")) {
            uninstallWindowListeners(root);
            installWindowListeners(root, root.getParent());
        } else if (propertyName.equals(UNIFIED_TOOLBAR_LOOK)) {
            updateTextured();
        }
    }

    /**
     * A custom layout manager that is responsible for the layout of
     * layeredPane, glassPane, menuBar and titlePane, if one has been installed.
     */
    // NOTE: Ideally this would extends JRootPane.RootLayout, but that
    // would force this to be non-static.
    private class SeaGlassRootLayout implements LayoutManager2 {

        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param  parent the Container for which this layout manager is being
         *                used
         *
         * @return a Dimension object containing the layout's preferred size
         */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension cpd;
            Dimension mbd;
            Dimension tpd;
            int       cpWidth  = 0;
            int       cpHeight = 0;
            int       mbWidth  = 0;
            int       mbHeight = 0;
            int       tpWidth  = 0;
            int       tpHeight = 0;
            Insets    i        = parent.getInsets();
            JRootPane root     = (JRootPane) parent;

            if (root.getContentPane() != null) {
                cpd = root.getContentPane().getPreferredSize();
            } else {
                cpd = root.getSize();
            }

            if (cpd != null) {
                cpWidth  = cpd.width;
                cpHeight = cpd.height;
            }

            if (root.getJMenuBar() != null) {
                mbd = root.getJMenuBar().getPreferredSize();
                if (mbd != null) {
                    mbWidth  = mbd.width;
                    mbHeight = mbd.height;
                }
            }

            if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof SeaGlassRootPaneUI)) {
                JComponent titlePane = ((SeaGlassRootPaneUI) root.getUI()).getTitlePane();

                if (titlePane != null) {
                    tpd = titlePane.getPreferredSize();
                    if (tpd != null) {
                        tpWidth  = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right,
                                 cpHeight + mbHeight + tpHeight + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param  parent the Container for which this layout manager is being
         *                used
         *
         * @return a Dimension object containing the layout's minimum size
         */
        public Dimension minimumLayoutSize(Container parent) {
            Dimension cpd;
            Dimension mbd;
            Dimension tpd;
            int       cpWidth  = 0;
            int       cpHeight = 0;
            int       mbWidth  = 0;
            int       mbHeight = 0;
            int       tpWidth  = 0;
            int       tpHeight = 0;
            Insets    i        = parent.getInsets();
            JRootPane root     = (JRootPane) parent;

            if (root.getContentPane() != null) {
                cpd = root.getContentPane().getMinimumSize();
            } else {
                cpd = root.getSize();
            }

            if (cpd != null) {
                cpWidth  = cpd.width;
                cpHeight = cpd.height;
            }

            if (root.getJMenuBar() != null) {
                mbd = root.getJMenuBar().getMinimumSize();
                if (mbd != null) {
                    mbWidth  = mbd.width;
                    mbHeight = mbd.height;
                }
            }

            if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof SeaGlassRootPaneUI)) {
                JComponent titlePane = ((SeaGlassRootPaneUI) root.getUI()).getTitlePane();

                if (titlePane != null) {
                    tpd = titlePane.getMinimumSize();
                    if (tpd != null) {
                        tpWidth  = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right,
                                 cpHeight + mbHeight + tpHeight + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param  target the Container for which this layout manager is being
         *                used
         *
         * @return a Dimension object containing the layout's maximum size
         */
        public Dimension maximumLayoutSize(Container target) {
            Dimension cpd;
            Dimension mbd;
            Dimension tpd;
            int       cpWidth  = Integer.MAX_VALUE;
            int       cpHeight = Integer.MAX_VALUE;
            int       mbWidth  = Integer.MAX_VALUE;
            int       mbHeight = Integer.MAX_VALUE;
            int       tpWidth  = Integer.MAX_VALUE;
            int       tpHeight = Integer.MAX_VALUE;
            Insets    i        = target.getInsets();
            JRootPane root     = (JRootPane) target;

            if (root.getContentPane() != null) {
                cpd = root.getContentPane().getMaximumSize();
                if (cpd != null) {
                    cpWidth  = cpd.width;
                    cpHeight = cpd.height;
                }
            }

            if (root.getJMenuBar() != null) {
                mbd = root.getJMenuBar().getMaximumSize();
                if (mbd != null) {
                    mbWidth  = mbd.width;
                    mbHeight = mbd.height;
                }
            }

            if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof SeaGlassRootPaneUI)) {
                JComponent titlePane = ((SeaGlassRootPaneUI) root.getUI()).getTitlePane();

                if (titlePane != null) {
                    tpd = titlePane.getMaximumSize();
                    if (tpd != null) {
                        tpWidth  = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            int maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight);
            // Only overflows if 3 real non-MAX_VALUE heights, sum to > MAX_VALUE
            // Only will happen if sums to more than 2 billion units. Not likely.
            if (maxHeight != Integer.MAX_VALUE) {
                maxHeight = cpHeight + mbHeight + tpHeight + i.top + i.bottom;
            }

            int maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth);
            // Similar overflow comment as above
            if (maxWidth != Integer.MAX_VALUE) {
                maxWidth += i.left + i.right;
            }

            return new Dimension(maxWidth, maxHeight);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param parent the Container for which this layout manager is being
         *               used
         */
        public void layoutContainer(Container parent) {
            JRootPane root  = (JRootPane) parent;
            Rectangle b     = root.getBounds();
            Insets    i     = root.getInsets();
            int       nextY = 0;
            int       w     = b.width - i.right - i.left;
            int       h     = b.height - i.top - i.bottom;

            if (root.getLayeredPane() != null) {
                root.getLayeredPane().setBounds(i.left, i.top, w, h);
            }

            if (root.getGlassPane() != null) {
                root.getGlassPane().setBounds(i.left, i.top, w, h);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our children.
            if (root.getWindowDecorationStyle() != JRootPane.NONE && (root.getUI() instanceof SeaGlassRootPaneUI)) {
                JComponent titlePane = ((SeaGlassRootPaneUI) root.getUI()).getTitlePane();

                if (titlePane != null) {
                    Dimension tpd = titlePane.getPreferredSize();

                    if (tpd != null) {
                        int tpHeight = tpd.height;

                        titlePane.setBounds(0, 0, w, tpHeight);
                        nextY += tpHeight;
                    }
                }
            }

            if (root.getJMenuBar() != null) {
                boolean   menuInTitle = (root.getClientProperty("JRootPane.MenuInTitle") == Boolean.TRUE);
                Dimension mbd         = root.getJMenuBar().getPreferredSize();
                int x = menuInTitle? 20 : 0;
                root.getJMenuBar().setBounds(x, menuInTitle ? 0 : nextY, w, mbd.height);
                root.getJMenuBar().setOpaque(false);
                root.getJMenuBar().setBackground(transparentColor);
                if (!menuInTitle) {
                    nextY += mbd.height;
                }
            }

            if (root.getContentPane() != null) {
                /* Dimension cpd = */ root.getContentPane().getPreferredSize();
                root.getContentPane().setBounds(0, nextY, w, h < nextY ? 0 : h - nextY);
            }
        }

        /**
         * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
         */
        public void addLayoutComponent(String name, Component comp) {
        }

        /**
         * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
         */
        public void removeLayoutComponent(Component comp) {
        }

        /**
         * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
         */
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        /**
         * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
         */
        public float getLayoutAlignmentX(Container target) {
            return 0.0f;
        }

        /**
         * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
         */
        public float getLayoutAlignmentY(Container target) {
            return 0.0f;
        }

        /**
         * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
         */
        public void invalidateLayout(Container target) {
        }
    }

    /**
     * MouseInputHandler is responsible for handling resize/moving of the
     * Window. It sets the cursor directly on the Window when then mouse moves
     * over a hot spot.
     */
    private class MouseInputHandler implements MouseInputListener {

        /** Set to true if the drag operation is moving the window. */
        private boolean isMovingWindow;

        /** Used to determine the corner the resize is occuring from. */
        private int dragCursor;

        /** X location the mouse went down on for a drag operation. */
        private int dragOffsetX;

        /** Y location the mouse went down on for a drag operation. */
        private int dragOffsetY;

        /** Width of the window when the drag started. */
        private int dragWidth;

        /** Height of the window when the drag started. */
        private int dragHeight;

        /**
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent ev) {
            JRootPane rootPane = getRootPane();

            if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
                return;
            }

            Point  dragWindowOffset = ev.getPoint();
            Window w                = (Window) ev.getSource();

            if (w != null) {
                w.toFront();
            }

            Frame  f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else if (w instanceof Dialog) {
                d = (Dialog) w;
            }

            int frameState = (f != null) ? f.getExtendedState() : 0;

            if (getTitlePane() != null) {
                Point convertedDragWindowOffset = SwingUtilities.convertPoint(w, dragWindowOffset, getTitlePane());

                if (getTitlePane().contains(convertedDragWindowOffset)) {
                    if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null))
                            && dragWindowOffset.y >= BORDER_DRAG_THICKNESS && dragWindowOffset.x >= BORDER_DRAG_THICKNESS
                            && dragWindowOffset.x < w.getWidth() - BORDER_DRAG_THICKNESS) {
                        isMovingWindow = true;
                        dragOffsetX    = dragWindowOffset.x;
                        dragOffsetY    = dragWindowOffset.y;
                        return;
                    }
                }
            }

            if (f != null && f.isResizable() && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null && d.isResizable())) {
                dragOffsetX = dragWindowOffset.x;
                dragOffsetY = dragWindowOffset.y;
                dragWidth   = w.getWidth();
                dragHeight  = w.getHeight();
                dragCursor  = getCursor(calculateCorner(w, dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent ev) {
            if (dragCursor != 0 && window != null && !window.isValid()) {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                window.validate();
                getRootPane().repaint();
            }

            isMovingWindow = false;
            dragCursor     = 0;
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent ev) {
            JRootPane root = getRootPane();

            if (root.getWindowDecorationStyle() == JRootPane.NONE) {
                return;
            }

            Window w = (Window) ev.getSource();

            Frame  f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else if (w instanceof Dialog) {
                d = (Dialog) w;
            }

            // Update the cursor
            int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));

            if (cursor != 0
                    && ((f != null && (f.isResizable() && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))
                        || (d != null && d.isResizable()))) {
                w.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
                w.setCursor(lastCursor);
            }
        }

        /**
         * Adjust the window bounds.
         *
         * @param bounds      the original bounds.
         * @param min         the minimum window size.
         * @param deltaX      the x delta.
         * @param deltaY      the y delta.
         * @param deltaWidth  the width delta.
         * @param deltaHeight the height delta.
         */
        private void adjust(Rectangle bounds, Dimension min, int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
            bounds.x      += deltaX;
            bounds.y      += deltaY;
            bounds.width  += deltaWidth;
            bounds.height += deltaHeight;
            if (min != null) {
                if (bounds.width < min.width) {
                    int correction = min.width - bounds.width;

                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }

                    bounds.width = min.width;
                }

                if (bounds.height < min.height) {
                    int correction = min.height - bounds.height;

                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }

                    bounds.height = min.height;
                }
            }
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent ev) {
            Window w  = (Window) ev.getSource();
            Point  pt = ev.getPoint();

            if (isMovingWindow) {
                Point eventLocationOnScreen = ev.getLocationOnScreen();

                w.setLocation(eventLocationOnScreen.x - dragOffsetX, eventLocationOnScreen.y - dragOffsetY);
            } else if (dragCursor != 0) {
                Rectangle r           = w.getBounds();
                Rectangle startBounds = new Rectangle(r);
                Dimension min         = w.getMinimumSize();

                switch (dragCursor) {

                case Cursor.E_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, 0);
                    break;

                case Cursor.S_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                case Cursor.N_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, 0, -(pt.y - dragOffsetY));
                    break;

                case Cursor.W_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), 0);
                    break;

                case Cursor.NE_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, pt.x + (dragWidth - dragOffsetX) - r.width, -(pt.y - dragOffsetY));
                    break;

                case Cursor.SE_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                case Cursor.NW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, pt.y - dragOffsetY, -(pt.x - dragOffsetX), -(pt.y - dragOffsetY));
                    break;

                case Cursor.SW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                default:
                    break;
                }

                if (!r.equals(startBounds)) {
                    w.setBounds(r);
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        w.validate();
                        getRootPane().repaint();
                    }
                }
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent ev) {
            Window w = (Window) ev.getSource();

            lastCursor = w.getCursor();
            mouseMoved(ev);
        }

        /**
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent ev) {
            Window w = (Window) ev.getSource();

            w.setCursor(lastCursor);
        }

        /**
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            Frame  f = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else {
                return;
            }

            if (getTitlePane() != null) {
                Point convertedPoint = SwingUtilities.convertPoint(w, ev.getPoint(), getTitlePane());

                int state = f.getExtendedState();

                if (getTitlePane().contains(convertedPoint)) {
                    if ((ev.getClickCount() % 2) == 0 && ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
                        if (f.isResizable()) {
                            if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                                f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                            } else {
                                f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                            }

                            return;
                        }
                    }
                }
            }
        }

        /**
         * Returns the corner that contains the point <code>x</code>, <code>
         * y</code>, or -1 if the position doesn't match a corner.
         *
         * @param  w the window.
         * @param  x the x coordinate.
         * @param  y the y coordinate.
         *
         * @return the corner containing the (x, y) coordinate, or -1 if the
         *         position doesn't match a corner.
         */
        private int calculateCorner(Window w, int x, int y) {
            Insets insets    = w.getInsets();
            int    xPosition = calculatePosition(x - insets.left, w.getWidth() - insets.left - insets.right);
            int    yPosition = calculatePosition(y - insets.top, w.getHeight() - insets.top - insets.bottom);

            if (xPosition == -1 || yPosition == -1) {
                return -1;
            }

            return yPosition * 5 + xPosition;
        }

        /**
         * Returns the Cursor to render for the specified corner. This returns 0
         * if the corner doesn't map to a valid Cursor
         *
         * @param  corner the corner index.
         *
         * @return the cursor mapping.
         */
        private int getCursor(int corner) {
            if (corner == -1) {
                return 0;
            }

            return cursorMapping[corner];
        }

        /**
         * Returns an integer indicating the position of <code>spot</code> in
         * <code>width</code>. The return value will be: 0 if <
         * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >=
         * CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS 3 if >= width -
         * CORNER_DRAG_WIDTH 4 if >= width - BORDER_DRAG_THICKNESS 5 otherwise
         *
         * @param  spot  DOCUMENT ME!
         * @param  width DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private int calculatePosition(int spot, int width) {
            if (spot < BORDER_DRAG_THICKNESS) {
                return 0;
            }

            if (spot < CORNER_DRAG_WIDTH) {
                return 1;
            }

            if (spot >= (width - BORDER_DRAG_THICKNESS)) {
                return 4;
            }

            if (spot >= (width - CORNER_DRAG_WIDTH)) {
                return 3;
            }

            return 2;
        }
    }
}
