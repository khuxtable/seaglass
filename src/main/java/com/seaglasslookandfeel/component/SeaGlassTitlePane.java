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
package com.seaglasslookandfeel.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.painter.FrameAndRootPainter;
import com.seaglasslookandfeel.ui.SeaGlassButtonUI;
import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;
import com.seaglasslookandfeel.ui.SeaglassUI;
import com.seaglasslookandfeel.util.SeaGlassGraphicsUtils;

/**
 * Class that manages a JLF awt.Window-descendant class's title bar.
 *
 * <p>This class assumes it will be created with a particular window decoration
 * style, and that if the style changes, a new one will be created.</p>
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassTitlePane extends JComponent implements SeaglassUI, PropertyChangeListener {
    private static final long   serialVersionUID         = 7006086880911744060L;
    private static final String WINDOW_DOCUMENT_MODIFIED = "Window.documentModified";

    private static final String CLOSE_CMD    = UIManager.getString("InternalFrameTitlePane.closeButtonText");
    private static final String ICONIFY_CMD  = UIManager.getString("InternalFrameTitlePane.minimizeButtonText");
    private static final String RESTORE_CMD  = UIManager.getString("InternalFrameTitlePane.restoreButtonText");
    private static final String MAXIMIZE_CMD = UIManager.getString("InternalFrameTitlePane.maximizeButtonText");
    private static final String MOVE_CMD     = UIManager.getString("InternalFrameTitlePane.moveButtonText");
    private static final String SIZE_CMD     = UIManager.getString("InternalFrameTitlePane.sizeButtonText");

    // Basic
    private JButton             iconButton;
    private JButton             maxButton;
    private JButton             closeButton;
    private JButton             menuButton;

    private JPopupMenu        windowMenu;
    private JRootPane         rootPane;
    private RootPaneContainer rootParent;

    private Action closeAction;
    private Action maximizeAction;
    private Action iconifyAction;
    private Action restoreAction;
    private Action moveAction;
    private Action sizeAction;

    private Color DEFAULT_EMPHASIS_COLOR = UIManager.getColor("seaGlassTextEmphasis");

    private String closeButtonToolTip;
    private String iconButtonToolTip;
    private String restoreButtonToolTip;
    private String maxButtonToolTip;

    private int                state        = -1;
    private SeaGlassRootPaneUI rootPaneUI;

    // Synth
    private SynthStyle         style;
    private int                titleSpacing;

    /**
     * Creates a new SeaGlassTitlePane object.
     *
     * @param rootPane the JRootPane containing the title pane.
     * @param ui       the UI delegate for the root pane.
     */
    public SeaGlassTitlePane(JRootPane rootPane, SeaGlassRootPaneUI ui) {
        this.rootPane   = rootPane;
        this.rootPaneUI = ui;
        rootParent      = (RootPaneContainer) rootPane.getParent();
        installTitlePane();
    }

    /**
     * @see javax.swing.JComponent#getRootPane()
     */
    public JRootPane getRootPane() {
        return rootPane;
    }

    /**
     * @see javax.swing.JComponent#getUIClassID()
     */
    public String getUIClassID() {
        return "InternalFrameTitlePaneUI";
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * Get the SynthContext for the title pane.
     *
     * @param  c     the component.
     * @param  state the state.
     *
     * @return the SynthContext object.
     */
    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Compute the state for the title pane.
     *
     * @param  c the title pane.
     *
     * @return the state.
     */
    private int getComponentState(JComponent c) {
        if (rootParent != null) {

            if (isParentSelected()) {
                return SELECTED;
            }
        }

        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Determine if the title pane's parent is active.
     *
     * @return {@code true} if the parent is active, {@code false} otherwise.
     */
    private boolean isParentSelected() {
        if (rootParent instanceof JFrame) {
            return ((JFrame) rootParent).isActive();
        } else if (rootParent instanceof JDialog) {
            return ((JDialog) rootParent).isActive();
        } else {
            return true;
        }
    }

    /**
     * Is the parent window iconified?
     *
     * @return {@code true} if the parent window is iconified, {@code false}
     *         otherwise.
     */
    private boolean isParentIcon() {
        if (rootParent instanceof JFrame) {
            return (((JFrame) rootParent).getExtendedState() & Frame.ICONIFIED) != 0;
        } else {
            return false;
        }
    }

    /**
     * Is the parent window maximized?
     *
     * @return {@code true} if the parent window is maximized, {@code false}
     *         otherwise.
     */
    private boolean isParentMaximum() {
        if (rootParent instanceof JFrame) {
            return (((JFrame) rootParent).getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
        } else {
            return false;
        }
    }

    /**
     * Is the parent window laid out left-to-right?
     *
     * @return {@code true} if the parent is laid out left-to-right,
     *         {@code false} otherwise.
     */
    private boolean isParentLeftToRight() {
        if (rootParent instanceof JFrame) {
            return SeaGlassLookAndFeel.isLeftToRight((JFrame) rootParent);
        } else if (rootParent instanceof JDialog) {
            return SeaGlassLookAndFeel.isLeftToRight((JDialog) rootParent);
        } else {
            return false;
        }
    }

    /**
     * Close the window.
     */
    private void doParentDefaultCloseAction() {
        ((Window) rootParent).dispatchEvent(new WindowEvent((Window) rootParent, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Iconify/Restore the window.
     *
     * @param iconify {@code true} if we are to iconify the window,
     *                {@code false} if we are to restore the window.
     */
    private void setParentIcon(boolean iconify) {
        if (rootParent instanceof JFrame) {
            JFrame frame = (JFrame) rootParent;
            int    state = frame.getExtendedState();

            ((JFrame) rootParent).setExtendedState(iconify ? state | Frame.ICONIFIED : state & ~Frame.ICONIFIED);
        }
    }

    /**
     * Maximize/Restore the window.
     *
     * @param maximize iconify {@code true} if we are to maximize the window,
     *                 {@code false} if we are to restore the window.
     */
    private void setParentMaximum(boolean maximize) {
        if (rootParent instanceof JFrame) {
            JFrame frame = (JFrame) rootParent;
            int    state = frame.getExtendedState();

            if (maximize) {
                GraphicsConfiguration gc = frame.getGraphicsConfiguration();
                Insets                i  = Toolkit.getDefaultToolkit().getScreenInsets(gc);
                Rectangle             r  = gc.getBounds();

                r.x      = 0;
                r.y      = 0;
                r.width  -= i.left + i.right;
                r.height -= i.top + i.bottom;
                frame.setMaximizedBounds(r);
            }

            frame.setExtendedState(maximize ? state | Frame.MAXIMIZED_BOTH : state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Add a property change listener to the root pane.
     *
     * @param listener the propertiy change listener to add.
     */
    private void addParentPropertyChangeListener(PropertyChangeListener listener) {
        if (rootParent instanceof JFrame) {
            ((JFrame) rootParent).addPropertyChangeListener(listener);
        } else if (rootParent instanceof JDialog) {
            ((JDialog) rootParent).addPropertyChangeListener(listener);
        }

        rootPane.addPropertyChangeListener(listener);
    }

    /**
     * Remove the property change listener from the root pane.
     *
     * @param listener the property change listener to remove.
     */
    private void removeParentPropertyChangeListener(PropertyChangeListener listener) {
        if (rootParent instanceof JFrame) {
            ((JFrame) rootParent).removePropertyChangeListener(listener);
        } else if (rootParent instanceof JDialog) {
            ((JDialog) rootParent).removePropertyChangeListener(listener);
        }
    }

    /**
     * Is the parent window closable?
     *
     * @return {@code true} if the parent window is closable, {@code false}
     *         otheriwes.
     */
    private boolean isParentClosable() {
        return true;
    }

    /**
     * Is the parent window iconifiable?
     *
     * @return {@code true} if the parent window is iconifiable, {@code false}
     *         otheriwes.
     */
    private boolean isParentIconifiable() {
        if (rootParent instanceof JFrame) {
            return ((JFrame)rootParent).isResizable();
        }
        return false;
    }

    /**
     * Is the parent window maximizable?
     *
     * @return {@code true} if the parent window is maximizable, {@code false}
     *         otheriwes.
     */
    private boolean isParentMaximizable() {
        if (rootParent instanceof JFrame) {
            return ((JFrame)rootParent).isResizable();
        }
        return false;
    }

    /**
     * Install actions, buttons, and listeners on title pane.
     */
    private void installTitlePane() {
        installDefaults();
        installListeners();

        createActions();
        enableActions();
        createActionMap();

        setLayout(createLayout());

        assembleSystemMenu();
        createButtons();
        addSubComponents();
    }

    private void assembleSystemMenu() {
        windowMenu = new JPopupMenu();
        addSystemMenuItems(windowMenu);
        enableActions();
        menuButton = new JButton();
        menuButton.setName("InternalFrameTitlePane.menuButtonAccessibleName");
        updateMenuIcon();
        menuButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    showSystemMenu();
                }
            });
        setInheritsPopupMenu(true);
    }
    
    /**
     * Add the items to the system menu.
     *
     * @param menu the system menu popup.
     */
    private void addSystemMenuItems(JPopupMenu menu) {
        // PENDING: this should all be localizable!
        JMenuItem mi = (JMenuItem) menu.add(restoreAction);

        mi.setMnemonic('R');
        mi = (JMenuItem) menu.add(moveAction);
        mi.setMnemonic('M');
        mi = (JMenuItem) menu.add(sizeAction);
        mi.setMnemonic('S');
        mi = (JMenuItem) menu.add(iconifyAction);
        mi.setMnemonic('n');
        mi = (JMenuItem) menu.add(maximizeAction);
        mi.setMnemonic('x');
        menu.add(new JSeparator());
        mi = (JMenuItem) menu.add(closeAction);
        mi.setMnemonic('C');
    }
    

    /**
     * Show the system menu.
     */
    private void showSystemMenu() {
        Insets insets = rootPane.getInsets();
        windowMenu.show(menuButton, getX() - insets.left - insets.right,
            getY() + this.getPreferredSize().height);
    }
    
    /**
     * Resets the menuButton icon to match that of the frame.
     */
    private void updateMenuIcon() {
        List<Image> iconList = null;
        if (rootParent instanceof JFrame) {
            iconList = ((JFrame) rootParent).getIconImages();
        } else if (rootParent instanceof JDialog) {
            iconList = ((JDialog) rootParent).getIconImages();
        }
        
        Image frameIcon = iconList != null && iconList.size() > 0 ? iconList.get(0) : null;

        SeaGlassContext context   = getContext(this);

        if (frameIcon != null) {
            Dimension maxSize   = (Dimension) context.getStyle().get(context, "InternalFrameTitlePane.maxFrameIconSize");
            int       maxWidth  = 16;
            int       maxHeight = 16;

            if (maxSize != null) {
                maxWidth  = maxSize.width;
                maxHeight = maxSize.height;
            }
            menuButton.setIcon(new ImageIcon(frameIcon.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH)));
        }

        context.dispose();
    }

    /**
     * Add the buttons.
     */
    private void addSubComponents() {
        menuButton.setName("InternalFrameTitlePane.menuButton");
        iconButton.setName("InternalFrameTitlePane.iconifyButton");
        maxButton.setName("InternalFrameTitlePane.maximizeButton");
        closeButton.setName("InternalFrameTitlePane.closeButton");

        add(menuButton);
        add(iconButton);
        add(maxButton);
        add(closeButton);
    }

    /**
     * Create actions for the buttons.
     */
    private void createActions() {
        maximizeAction = new MaximizeAction();
        iconifyAction  = new IconifyAction();
        closeAction    = new CloseAction();
        restoreAction  = new RestoreAction();
        moveAction     = new MoveAction();
        sizeAction     = new SizeAction();
    }

    /**
     * Create the action map for the system menu.
     *
     * @return the action map.
     */
    ActionMap createActionMap() {
        ActionMap map = new ActionMapUIResource();

        map.put("showSystemMenu", new ShowSystemMenuAction(true));
        map.put("hideSystemMenu", new ShowSystemMenuAction(false));

        return map;
    }

    /**
     * Install listeners.
     */
    protected void installListeners() {
        addParentPropertyChangeListener(this);
    }

    /**
     * Uninstall listeners.
     */
    protected void uninstallListeners() {
        removeParentPropertyChangeListener(this);
    }

    /**
     * Install the defaults and update the Synth Style.
     */
    private void installDefaults() {
        // Basic
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        closeButtonToolTip   = UIManager.getString("InternalFrame.closeButtonToolTip");
        iconButtonToolTip    = UIManager.getString("InternalFrame.iconButtonToolTip");
        restoreButtonToolTip = UIManager.getString("InternalFrame.restoreButtonToolTip");
        maxButtonToolTip     = UIManager.getString("InternalFrame.maxButtonToolTip");

        // Synth
        updateStyle(this);
    }

    /**
     * Uninstall the defaults.
     */
    public void uninstallDefaults() {
        SeaGlassContext context = getContext(this, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * Update the Synth Style.
     *
     * @param c the title pane.
     */
    private void updateStyle(JComponent c) {
        SeaGlassContext context  = getContext(this, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateSeaglassStyle(context, this);

        if (style != oldStyle) {
            titleSpacing = style.getInt(context, "InternalFrameTitlePane.titleSpacing", 2);
        }

        context.dispose();
    }

    /**
     * Create the buttons.
     */
    private void createButtons() {
        iconButton = new NoFocusButton("InternalFrameTitlePane.iconifyButtonAccessibleName");
        iconButton.addActionListener(iconifyAction);

        if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
            iconButton.setToolTipText(iconButtonToolTip);
        }

        maxButton = new NoFocusButton("InternalFrameTitlePane.maximizeButtonAccessibleName");
        maxButton.addActionListener(maximizeAction);

        closeButton = new NoFocusButton("InternalFrameTitlePane.closeButtonAccessibleName");
        closeButton.addActionListener(closeAction);

        if (closeButtonToolTip != null && closeButtonToolTip.length() != 0) {
            closeButton.setToolTipText(closeButtonToolTip);
        }

        setButtonTooltips();
    }

    /**
     * Set the tooltips for the buttons.
     */
    private void setButtonTooltips() {
        if (isParentIcon()) {

            if (restoreButtonToolTip != null && restoreButtonToolTip.length() != 0) {
                iconButton.setToolTipText(restoreButtonToolTip);
            }

            if (maxButtonToolTip != null && maxButtonToolTip.length() != 0) {
                maxButton.setToolTipText(maxButtonToolTip);
            }
        } else if (isParentMaximum()) {

            if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
                iconButton.setToolTipText(iconButtonToolTip);
            }

            if (restoreButtonToolTip != null && restoreButtonToolTip.length() != 0) {
                maxButton.setToolTipText(restoreButtonToolTip);
            }
        } else {

            if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
                iconButton.setToolTipText(iconButtonToolTip);
            }

            if (maxButtonToolTip != null && maxButtonToolTip.length() != 0) {
                maxButton.setToolTipText(maxButtonToolTip);
            }
        }
    }

    // SeaGlassInternalFrameTitlePane has no UI, we'll invoke paint on it.
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        SeaGlassContext context = getContext(this);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintInternalFrameTitlePaneBackground(context, g, 0, 0, getWidth(), getHeight());
        paint(context, g);
        context.dispose();
    }

    /**
     * Paint the title pane.
     *
     * @param context the SynthContext to use to get the component.
     * @param g       the Graphics context to paint with.
     */
    private void paint(SeaGlassContext context, Graphics g) {
        String title = getTitle();

        if (title != null) {
            SynthStyle style = context.getStyle();

            Color color = style.getColor(context, ColorType.TEXT_FOREGROUND);

            // TODO style.getColor returns improper color for state? Why?
            if ((context.getComponentState() & 512) != 0) {
                Object obj = style.get(context, "[WindowFocused].textForeground");

                if (obj != null && obj instanceof Color) {
                    color = (Color) obj;
                }

                // FIXME The state *still* doesn't get the color right!!!
                color = Color.BLACK;
            }

            g.setColor(color);
            g.setFont(style.getFont(context));

            // Center text vertically.
            FontMetrics fm         = SwingUtilities2.getFontMetrics(rootPane, g);
            int         baseline   = (getHeight() + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
            JButton     lastButton = null;

            if (isParentIconifiable()) {
                lastButton = iconButton;
            } else if (isParentMaximizable()) {
                lastButton = maxButton;
            } else if (isParentClosable()) {
                lastButton = closeButton;
            }

            int     maxX;
            int     minX;
            boolean ltr = isParentLeftToRight();

            if (ltr) {

                if (lastButton != null) {
                    maxX = lastButton.getX() - titleSpacing;
                } else {
                    maxX = getParentWidth() - getParentInsets().right - titleSpacing;
                }

                minX = menuButton.getX() + menuButton.getWidth() + titleSpacing;
            } else {

                if (lastButton != null) {
                    minX = lastButton.getX() + lastButton.getWidth() + titleSpacing;
                } else {
                    minX = getParentInsets().left + titleSpacing;
                }

                maxX = getParentWidth() - getParentInsets().right - menuButton.getX() - titleSpacing;
            }

            String clippedTitle = getTitle(title, fm, maxX - minX);

            if (clippedTitle == title) {
                int width = style.getGraphicsUtils(context).computeStringWidth(context, g.getFont(), fm, title);

                minX = Math.max(minX, (getWidth() - width) / 2);
                minX = Math.min(maxX - width, minX);
            }

            // style.getGraphicsUtils(context).paintText(context, g,
            // clippedTitle, minX, baseline - fm.getAscent(), -1);
            ((SeaGlassGraphicsUtils) style.getGraphicsUtils(context)).drawEmphasizedText(g, color, DEFAULT_EMPHASIS_COLOR, clippedTitle,
                                                                                         minX, baseline); // - fm.getAscent());
        }
    }

    /**
     * Returns the String to display as the title.
     *
     * @return the title String.
     */
    private String getTitle() {
        if (rootParent instanceof JFrame) {
            return ((JFrame) rootParent).getTitle();
        } else if (rootParent instanceof JDialog) {
            return ((JDialog) rootParent).getTitle();
        }

        return null;
    }

    /**
     * Get the parent insets.
     *
     * @return the insets.
     */
    private Insets getParentInsets() {
        if (rootParent instanceof JApplet) {
            return ((JApplet) rootParent).getInsets();
        }

        return ((Window) rootParent).getInsets();
    }

    /**
     * Get the parent width.
     *
     * @return the width.
     */
    private int getParentWidth() {
        if (rootParent instanceof JApplet) {
            return ((JApplet) rootParent).getWidth();
        }

        return ((Window) rootParent).getWidth();
    }

    /**
     * Get the title text, clipped if necessary.
     *
     * @param  text           the title text.
     * @param  fm             the font metrics to compute size with.
     * @param  availTextWidth the available space to display the title in.
     *
     * @return the text, clipped to fit the available space.
     */
    private String getTitle(String text, FontMetrics fm, int availTextWidth) {
        return SwingUtilities2.clipStringIfNecessary(rootPane, fm, text, availTextWidth);
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintInternalFrameTitlePaneBorder(context, g, x, y, w, h);
    }

    /**
     * Create the layout manager for the title pane.
     *
     * @return the layout manager.
     */
    private LayoutManager createLayout() {
        SeaGlassContext context = getContext(this);
        LayoutManager   lm      = (LayoutManager) style.get(context, "InternalFrameTitlePane.titlePaneLayout");

        context.dispose();

        return (lm != null) ? lm : new SeaGlassTitlePaneLayout();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == this) {

            if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
                updateStyle(this);
            }
        } else {
            // Changes for the internal frame
            if (evt.getPropertyName() == "iconImage") {
                updateMenuIcon();
            }
        }

        // Basic (from Handler inner class)
        String prop = (String) evt.getPropertyName();

        if (closeButton != null && WINDOW_DOCUMENT_MODIFIED.equals(prop)) {
            closeButton.revalidate();
            closeButton.repaint();

            return;
        }

        if (prop == JInternalFrame.IS_SELECTED_PROPERTY) {
            repaint();

            return;
        }

        // Frame.state isn't currently bound.
        if ("resizable".equals(prop) || "state".equals(prop)) {
            Frame frame = (JFrame) rootParent;

            if (frame != null) {
                setState(frame.getExtendedState(), true);
            }

            if ("resizable".equals(prop)) {
                getRootPane().repaint();
            }
        } else if ("title".equals(prop)) {
            repaint();
        } else if ("componentOrientation" == prop) {
            revalidate();
            repaint();
        } else if ("iconImage" == prop) {
            revalidate();
            repaint();
        }

        if (prop == JInternalFrame.IS_ICON_PROPERTY || prop == JInternalFrame.IS_MAXIMUM_PROPERTY) {
            setButtonTooltips();
            enableActions();

            return;
        }

        if ("closable" == prop) {

            if ((Boolean) evt.getNewValue() == Boolean.TRUE) {
                add(closeButton);
            } else {
                remove(closeButton);
            }
        } else if ("maximizable" == prop) {

            if ((Boolean) evt.getNewValue() == Boolean.TRUE) {
                add(maxButton);
            } else {
                remove(maxButton);
            }
        } else if ("iconable" == prop) {

            if ((Boolean) evt.getNewValue() == Boolean.TRUE) {
                add(iconButton);
            } else {
                remove(iconButton);
            }
        }

        enableActions();

        revalidate();
        repaint();
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is true
     * and the state has not changed, this will update anyway.
     *
     * @param state            the state of the window.
     * @param updateRegardless {@code true} if we are to update regardless of
     *                         the state, {@code false} otherwise.
     */
    private void setState(int state, boolean updateRegardless) {
        Window w = (Window) rootParent;

        if (w != null && rootPane.getWindowDecorationStyle() == JRootPane.FRAME) {

            if (this.state == state && !updateRegardless) {
                return;
            }

            Frame frame = (JFrame) rootParent;

            if (frame != null) {
                JRootPane rootPane = getRootPane();

                if (((state & Frame.MAXIMIZED_BOTH) != 0) && (rootPane.getBorder() == null || (rootPane.getBorder() instanceof UIResource))
                        && frame.isShowing()) {
                    rootPane.setBorder(null);
                } else if ((state & Frame.MAXIMIZED_BOTH) == 0) {

                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    rootPaneUI.installBorder(rootPane);
                }

                if (frame.isResizable()) {

                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {

                        // updateToggleButton(restoreAction, minimizeIcon);
                        maximizeAction.setEnabled(false);
                        restoreAction.setEnabled(true);
                    } else {

                        // updateToggleButton(maximizeAction, maximizeIcon);
                        maximizeAction.setEnabled(true);
                        restoreAction.setEnabled(false);
                    }
                    // if (toggleButton.getParent() == null ||
                    // iconifyButton.getParent() == null) {
                    // add(toggleButton);
                    // add(iconifyButton);
                    // revalidate();
                    // repaint();
                    // }
                    // toggleButton.setText(null);
                } else {
                    maximizeAction.setEnabled(false);
                    restoreAction.setEnabled(false);
                    // if (toggleButton.getParent() != null) {
                    // remove(toggleButton);
                    // revalidate();
                    // repaint();
                    // }
                }
            } else {

                // Not contained in a Frame
                maximizeAction.setEnabled(false);
                restoreAction.setEnabled(false);
                iconifyAction.setEnabled(false);

                // remove(toggleButton);
                // remove(iconifyButton);
                revalidate();
                repaint();
            }

            closeAction.setEnabled(true);
            this.state = state;
        }
    }

    /**
     * Enable/disable the button actions as needed.
     */
    private void enableActions() {
        restoreAction.setEnabled(isParentMaximum() || isParentIcon());
        maximizeAction.setEnabled((isParentMaximizable() && !isParentMaximum() && !isParentIcon())
                                      || (isParentMaximizable() && isParentIcon()));
        iconifyAction.setEnabled(isParentIconifiable() && !isParentIcon());
        closeAction.setEnabled(isParentClosable());
        sizeAction.setEnabled(false);
        moveAction.setEnabled(false);
    }

    /**
     * The layout manager for the title pane.
     */
    class SeaGlassTitlePaneLayout implements LayoutManager {

        /**
         * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
         */
        public void addLayoutComponent(String name, Component c) {
        }

        /**
         * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
         */
        public void removeLayoutComponent(Component c) {
        }

        /**
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        /**
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        public Dimension minimumLayoutSize(Container c) {
            SeaGlassContext context = getContext(SeaGlassTitlePane.this);
            int             width   = 10;
            int             height  = FrameAndRootPainter.TITLE_BAR_HEIGHT;

            int       buttonCount = 0;
            Dimension pref;

            if (isParentClosable()) {
                pref   = closeButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }

            if (isParentMaximizable()) {
                pref   = maxButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }

            if (isParentIconifiable()) {
                pref   = iconButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }
            
            pref   = menuButton.getPreferredSize();
            width  += pref.width;
            height = Math.max(pref.height, height);

            FontMetrics        fm            = getFontMetrics(getFont());
            SynthGraphicsUtils graphicsUtils = context.getStyle().getGraphicsUtils(context);
            String             frameTitle    = getTitle();
            int                title_w       = frameTitle != null ? graphicsUtils.computeStringWidth(context, fm.getFont(), fm, frameTitle)
                                                                  : 0;
            int                title_length  = frameTitle != null ? frameTitle.length() : 0;

            // Leave room for three characters in the title.
            if (title_length > 3) {
                int subtitle_w = graphicsUtils.computeStringWidth(context, fm.getFont(), fm, frameTitle.substring(0, 3) + "...");

                width += (title_w < subtitle_w) ? title_w : subtitle_w;
            } else {
                width += title_w;
            }

            height = Math.max(fm.getHeight(), height);

            width += titleSpacing + titleSpacing;

            Insets insets = getInsets();

            height += insets.top + insets.bottom;
            width  += insets.left + insets.right;
            context.dispose();

            return new Dimension(width, height);
        }

        /**
         * Determine the position of a button.
         *
         * @param  c        the button.
         * @param  insets   the title pane insets.
         * @param  x        the x position of the button.
         * @param  trailing Are we at the right edge?
         *
         * @return the modified x position for the button.
         */
        private int center(Component c, Insets insets, int x, boolean trailing) {
            Dimension pref  = c.getPreferredSize();
            int       width = pref.width;

            if (c instanceof JButton && ((JButton) c).getIcon() != null) {
                width = ((JButton) c).getIcon().getIconWidth();
            }

            if (trailing) {
                x -= width;
            }

            int y = 1;

            c.setBounds(x, y, pref.width, pref.height);

            if (pref.width > 0) {

                if (!trailing) {
                    return x + width;
                }
            }

            return x;
        }

        /**
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container c) {
            Insets insets = c.getInsets();

            if (isParentLeftToRight()) {
                center(menuButton, insets, insets.left + 4, false);
                int x = getWidth() - insets.right - 5;

                if (isParentClosable()) {
                    x = center(closeButton, insets, x, true);
                }

                if (isParentMaximizable()) {
                    x = center(maxButton, insets, x, true);
                }

                if (isParentIconifiable()) {
                    x = center(iconButton, insets, x, true);
                }
            } else {
                center(menuButton, insets, getWidth() - insets.right - 4, true);
                int x = insets.left + 5;

                if (isParentClosable()) {
                    x = center(closeButton, insets, x, false);
                }

                if (isParentMaximizable()) {
                    x = center(maxButton, insets, x, false);
                }

                if (isParentIconifiable()) {
                    x = center(iconButton, insets, x, false);
                }
            }
        }
    }

    /**
     * Handles closing internal frame.
     */
    private class CloseAction extends AbstractAction {
        private static final long serialVersionUID = 7476072734719417639L;

        /**
         * Creates a new CloseAction object.
         */
        public CloseAction() {
            super(CLOSE_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (isParentClosable()) {
                doParentDefaultCloseAction();
            }
        }
    }

    /**
     * Handles maximizing/restoring internal frame.
     */
    private class MaximizeAction extends AbstractAction {
        private static final long serialVersionUID = -74832854369507690L;

        /**
         * Creates a new MaximizeAction object.
         */
        public MaximizeAction() {
            super(MAXIMIZE_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent evt) {
            if (isParentMaximizable()) {

                if (isParentMaximum() && isParentIcon()) {
                    setParentIcon(false);
                } else if (!isParentMaximum()) {
                    setParentMaximum(true);
                } else {
                    setParentMaximum(false);
                }

                setButtonTooltips();
            }
        }
    }

    /**
     * Handles iconifying/uniconifying internal frame.
     */
    private class IconifyAction extends AbstractAction {
        private static final long serialVersionUID = -9131330678177895337L;

        /**
         * Creates a new IconifyAction object.
         */
        public IconifyAction() {
            super(ICONIFY_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (isParentIconifiable()) {

                if (!isParentIcon()) {
                    setParentIcon(true);
                } else {
                    setParentIcon(false);
                }
            }

            setButtonTooltips();
        }
    }

    /**
     * Restores internal frame to regular state.
     */
    private class RestoreAction extends AbstractAction {
        private static final long serialVersionUID = -1621305056572434154L;

        /**
         * Creates a new RestoreAction object.
         */
        public RestoreAction() {
            super(RESTORE_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent evt) {
            if (isParentMaximizable() && isParentMaximum() && isParentIcon()) {
                setParentIcon(false);
            } else if (isParentMaximizable() && isParentMaximum()) {
                setParentMaximum(false);
            } else if (isParentIconifiable() && isParentIcon()) {
                setParentIcon(false);
            }

            setButtonTooltips();
        }
    }

    /**
     * Handles moving internal frame.
     */
    private class MoveAction extends AbstractAction {
        private static final long serialVersionUID = 5637413107648659924L;

        /**
         * Creates a new MoveAction object.
         */
        public MoveAction() {
            super(MOVE_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            // This action is currently undefined
        }
    }

    /**
     * Handles showing and hiding the system menu.
     */
    private class ShowSystemMenuAction extends AbstractAction {
        private static final long serialVersionUID = 3363009814611622667L;
        private boolean           show; // whether to show the menu

        /**
         * Creates a new ShowSystemMenuAction object.
         *
         * @param show {@code true} if the action is to show, {@code false}
         *             otherwise.
         */
        public ShowSystemMenuAction(boolean show) {
            this.show = show;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (show) {
                showSystemMenu();
            } else {
            }
        }
    }

    /**
     * Handles resizing internal frame.
     */
    private class SizeAction extends AbstractAction {
        private static final long serialVersionUID = 210508132565915886L;

        /**
         * Creates a new SizeAction object.
         */
        public SizeAction() {
            super(SIZE_CMD);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            // This action is currently undefined
        }
    }

    /**
     * Window decoration button class.
     */
    private class NoFocusButton extends JButton {
        private static final long serialVersionUID = 5958100825982240844L;
        private String            uiKey;

        /**
         * Creates a new NoFocusButton object.
         *
         * @param uiKey the key for the UI component.
         */
        public NoFocusButton(String uiKey) {
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
            setFocusable(false);
            this.uiKey = uiKey;
            setUI(SeaGlassButtonUI.createUI(this));
        }

        /**
         * @see java.awt.Component#isFocusTraversable()
         */
        public boolean isFocusTraversable() {
            return false;
        }

        /**
         * @see javax.swing.JComponent#requestFocus()
         */
        public void requestFocus() {
        }

        /**
         * @see javax.swing.JButton#getAccessibleContext()
         */
        public AccessibleContext getAccessibleContext() {
            AccessibleContext ac = super.getAccessibleContext();

            if (uiKey != null) {
                ac.setAccessibleName(UIManager.getString(uiKey));
                uiKey = null;
            }

            return ac;
        }
    }
}
