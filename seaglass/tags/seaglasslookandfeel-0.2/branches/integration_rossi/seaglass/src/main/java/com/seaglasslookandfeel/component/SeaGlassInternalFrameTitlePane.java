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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.ui.SeaglassUI;

/**
 * Manage a title bar for an internal frame.
 *
 * <p>Copied from SynthInternalFrameTitlePane, which is package local.</p>
 *
 * @see javax.swing.plaf.synth.SynthInternalFrameTitlePane
 */
public class SeaGlassInternalFrameTitlePane extends JComponent implements SeaglassUI, PropertyChangeListener {
    private static final long serialVersionUID = 6852740789566412690L;

    private static final String CLOSE_CMD    = UIManager.getString("InternalFrameTitlePane.closeButtonText");
    private static final String ICONIFY_CMD  = UIManager.getString("InternalFrameTitlePane.minimizeButtonText");
    private static final String RESTORE_CMD  = UIManager.getString("InternalFrameTitlePane.restoreButtonText");
    private static final String MAXIMIZE_CMD = UIManager.getString("InternalFrameTitlePane.maximizeButtonText");
    private static final String MOVE_CMD     = UIManager.getString("InternalFrameTitlePane.moveButtonText");
    private static final String SIZE_CMD     = UIManager.getString("InternalFrameTitlePane.sizeButtonText");

    // Basic
    private JButton iconButton;
    private JButton maxButton;
    private JButton closeButton;

    private JMenu          windowMenu;
    private JInternalFrame frame;

    private Action closeAction;
    private Action maximizeAction;
    private Action iconifyAction;
    private Action restoreAction;
    private Action moveAction;
    private Action sizeAction;

    private String closeButtonToolTip;
    private String iconButtonToolTip;
    private String restoreButtonToolTip;
    private String maxButtonToolTip;

    // Synth
    private JPopupMenu systemPopupMenu;
    private JButton    menuButton;

    private SynthStyle style;
    private int        titleSpacing;

    /**
     * Creates a new SeaGlassInternalFrameTitlePane object.
     *
     * @param f the internal frame containing the title pane.
     */
    public SeaGlassInternalFrameTitlePane(JInternalFrame f) {
        frame = f;
        installTitlePane();
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
     * Get a SynthContext for the title pane.
     *
     * @param  c     the title pane.
     * @param  state the state.
     *
     * @return the SynthContext describing the component.
     */
    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Get the state for the component.
     *
     * @param  c the component.
     *
     * @return the state for the component.
     */
    private int getComponentState(JComponent c) {
        if (frame != null) {

            if (frame.isSelected()) {
                return SELECTED;
            }
        }

        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Install the UI and listeners for the title pane.
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

    /**
     * Add the button subcomponents to the title pane.
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
     * Create the actions for the buttons.
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
     * Install the listeners on the title pane.
     */
    protected void installListeners() {
        frame.addPropertyChangeListener(this);
        addPropertyChangeListener(this);
    }

    /**
     * Uninstall the listeners from the title pane.
     */
    protected void uninstallListeners() {
        frame.removePropertyChangeListener(this);
        removePropertyChangeListener(this);
    }

    /**
     * Install defaults, update the Synth Style.
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
        JInternalFrame.JDesktopIcon di = frame.getDesktopIcon();

        if (di != null && di.getComponentPopupMenu() == systemPopupMenu) {

            // Release link to systemMenu from the JInternalFrame
            di.setComponentPopupMenu(null);
        }
    }

    /**
     * Update the Synth Style.
     *
     * @param c the component.
     */
    private void updateStyle(JComponent c) {
        SeaGlassContext context  = getContext(this, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            titleSpacing = style.getInt(context, "InternalFrameTitlePane.titleSpacing", 2);
        }

        context.dispose();
    }

    /**
     * Create the buttons and add action listeners.
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
     * Set the buttons' tooltip text.
     */
    private void setButtonTooltips() {
        if (frame.isIcon()) {

            if (restoreButtonToolTip != null && restoreButtonToolTip.length() != 0) {
                iconButton.setToolTipText(restoreButtonToolTip);
            }

            if (maxButtonToolTip != null && maxButtonToolTip.length() != 0) {
                maxButton.setToolTipText(maxButtonToolTip);
            }
        } else if (frame.isMaximum()) {

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

    /**
     * Create the system menu.
     */
    private void assembleSystemMenu() {
        systemPopupMenu = new JPopupMenuUIResource();
        addSystemMenuItems(systemPopupMenu);
        enableActions();
        menuButton = new NoFocusButton("InternalFrameTitlePane.menuButtonAccessibleName");
        updateMenuIcon();
        menuButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    try {
                        frame.setSelected(true);
                    } catch (PropertyVetoException pve) {
                    }

                    showSystemMenu();
                }
            });

        JPopupMenu p = frame.getComponentPopupMenu();

        if (p == null || p instanceof UIResource) {
            frame.setComponentPopupMenu(systemPopupMenu);
        }

        if (frame.getDesktopIcon() != null) {
            p = frame.getDesktopIcon().getComponentPopupMenu();

            if (p == null || p instanceof UIResource) {
                frame.getDesktopIcon().setComponentPopupMenu(systemPopupMenu);
            }
        }

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
        Insets insets = frame.getInsets();

        if (!frame.isIcon()) {
            systemPopupMenu.show(frame, insets.left, getY() + getHeight());
        } else {
            systemPopupMenu.show(menuButton, getX() - insets.left - insets.right,
                                 getY() - systemPopupMenu.getPreferredSize().height - insets.bottom - insets.top);
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
     * @param context the SynthContext to get the component from.
     * @param g       the Graphics context to paint with.
     */
    private void paint(SeaGlassContext context, Graphics g) {
        String title = frame.getTitle();

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
                color = Color.WHITE;
            }

            g.setColor(color);
            g.setFont(style.getFont(context));

            // Center text vertically.
            FontMetrics fm         = SwingUtilities2.getFontMetrics(frame, g);
            int         baseline   = (getHeight() + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
            JButton     lastButton = null;

            if (frame.isIconifiable()) {
                lastButton = iconButton;
            } else if (frame.isMaximizable()) {
                lastButton = maxButton;
            } else if (frame.isClosable()) {
                lastButton = closeButton;
            }

            int     maxX;
            int     minX;
            boolean ltr = SeaGlassLookAndFeel.isLeftToRight(frame);

            if (ltr) {

                if (lastButton != null) {
                    maxX = lastButton.getX() - titleSpacing;
                } else {
                    maxX = frame.getWidth() - frame.getInsets().right - titleSpacing;
                }

                minX = menuButton.getX() + menuButton.getWidth() + titleSpacing;
            } else {

                if (lastButton != null) {
                    minX = lastButton.getX() + lastButton.getWidth() + titleSpacing;
                } else {
                    minX = frame.getInsets().left + titleSpacing;
                }

                maxX = menuButton.getX() - titleSpacing;
            }

            String clippedTitle = getTitle(title, fm, maxX - minX);

            if (clippedTitle == title) {
                int width = style.getGraphicsUtils(context).computeStringWidth(context, g.getFont(), fm, title);

                minX = Math.max(minX, (getWidth() - width) / 2);
                minX = Math.min(maxX - width, minX);
            }

            style.getGraphicsUtils(context).paintText(context, g, clippedTitle, minX, baseline - fm.getAscent(), -1);
        }
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
        return SwingUtilities2.clipStringIfNecessary(frame, fm, text, availTextWidth);
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
            if (evt.getPropertyName() == JInternalFrame.FRAME_ICON_PROPERTY) {
                updateMenuIcon();
            }
        }

        // Basic (from Handler inner class)
        String prop = (String) evt.getPropertyName();

        if (prop == JInternalFrame.IS_SELECTED_PROPERTY) {
            repaint();

            return;
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
     * Resets the menuButton icon to match that of the frame.
     */
    private void updateMenuIcon() {
        Icon            frameIcon = frame.getFrameIcon();
        SeaGlassContext context   = getContext(this);

        if (frameIcon != null) {
            Dimension maxSize   = (Dimension) context.getStyle().get(context, "InternalFrameTitlePane.maxFrameIconSize");
            int       maxWidth  = 16;
            int       maxHeight = 16;

            if (maxSize != null) {
                maxWidth  = maxSize.width;
                maxHeight = maxSize.height;
            }

            if ((frameIcon.getIconWidth() > maxWidth || frameIcon.getIconHeight() > maxHeight) && (frameIcon instanceof ImageIcon)) {
                frameIcon = new ImageIcon(((ImageIcon) frameIcon).getImage().getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH));
            }
        }

        context.dispose();
        menuButton.setIcon(frameIcon);
    }

    /**
     * Post a WINDOW_CLOSING-like event to the frame, so that it can be treated
     * like a regular Frame.
     *
     * @param frame the internal frame to be closed.
     */
    protected void postClosingEvent(JInternalFrame frame) {
        InternalFrameEvent e = new InternalFrameEvent(frame, InternalFrameEvent.INTERNAL_FRAME_CLOSING);

        // Try posting event, unless there's a SecurityManager.
        if (JInternalFrame.class.getClassLoader() == null) {

            try {
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);

                return;
            } catch (SecurityException se) {
                // Use dispatchEvent instead.
            }
        }

        frame.dispatchEvent(e);
    }

    /**
     * Set the enable/disabled state for the buttons.
     */
    private void enableActions() {
        restoreAction.setEnabled(frame.isMaximum() || frame.isIcon());
        maximizeAction.setEnabled((frame.isMaximizable() && !frame.isMaximum() && !frame.isIcon())
                                      || (frame.isMaximizable() && frame.isIcon()));
        iconifyAction.setEnabled(frame.isIconifiable() && !frame.isIcon());
        closeAction.setEnabled(frame.isClosable());
        sizeAction.setEnabled(false);
        moveAction.setEnabled(false);
    }

    /**
     * A special UIResource used for the system menu.
     */
    private static class JPopupMenuUIResource extends JPopupMenu implements UIResource {
        private static final long serialVersionUID = 4511298062453723303L;
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
            SeaGlassContext context = getContext(SeaGlassInternalFrameTitlePane.this);
            int             width   = 8;
            int             height  = 0;

            int       buttonCount = 0;
            Dimension pref;

            if (frame.isClosable()) {
                pref   = closeButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }

            if (frame.isMaximizable()) {
                pref   = maxButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }

            if (frame.isIconifiable()) {
                pref   = iconButton.getPreferredSize();
                width  += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }

            pref   = menuButton.getPreferredSize();
            width  += pref.width;
            height = Math.max(pref.height, height);

            FontMetrics        fm            = SeaGlassInternalFrameTitlePane.this.getFontMetrics(getFont());
            SynthGraphicsUtils graphicsUtils = context.getStyle().getGraphicsUtils(context);
            String             frameTitle    = frame.getTitle();
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

            height = Math.max(fm.getHeight() + 2, height);

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

            int y = 0;

            if (c == menuButton) {
                // y = insets.top + (getHeight() - insets.top - insets.bottom -
                // pref.height) / 2;
            }

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

            if (SeaGlassLookAndFeel.isLeftToRight(frame)) {
                center(menuButton, insets, insets.left + 4, false);
                int x = getWidth() - insets.right - 4;

                if (frame.isClosable()) {
                    x = center(closeButton, insets, x, true);
                }

                if (frame.isMaximizable()) {
                    x = center(maxButton, insets, x, true);
                }

                if (frame.isIconifiable()) {
                    x = center(iconButton, insets, x, true);
                }
            } else {
                center(menuButton, insets, getWidth() - insets.right - 4, true);
                int x = insets.left + 4;

                if (frame.isClosable()) {
                    x = center(closeButton, insets, x, false);
                }

                if (frame.isMaximizable()) {
                    x = center(maxButton, insets, x, false);
                }

                if (frame.isIconifiable()) {
                    x = center(iconButton, insets, x, false);
                }
            }
        }
    }

    /**
     * Handles closing internal frame.
     */
    private class CloseAction extends AbstractAction {
        private static final long serialVersionUID = 1214165629626107441L;

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
            if (frame.isClosable()) {
                frame.doDefaultCloseAction();
            }
        }
    }

    /**
     * Handles maximizing/restoring internal frame.
     */
    private class MaximizeAction extends AbstractAction {
        private static final long serialVersionUID = 6286449065315966825L;

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
            if (frame.isMaximizable()) {

                if (frame.isMaximum() && frame.isIcon()) {

                    try {
                        frame.setIcon(false);
                    } catch (PropertyVetoException e) {
                    }
                } else if (!frame.isMaximum()) {

                    try {
                        frame.setMaximum(true);
                    } catch (PropertyVetoException e) {
                    }
                } else {

                    try {
                        frame.setMaximum(false);
                    } catch (PropertyVetoException e) {
                    }
                }
            }
        }
    }

    /**
     * Handles iconifying/uniconifying internal frame.
     */
    private class IconifyAction extends AbstractAction {
        private static final long serialVersionUID = 4337075125371606258L;

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
            if (frame.isIconifiable()) {

                if (!frame.isIcon()) {

                    try {
                        frame.setIcon(true);
                    } catch (PropertyVetoException e1) {
                    }
                } else {

                    try {
                        frame.setIcon(false);
                    } catch (PropertyVetoException e1) {
                    }
                }
            }
        }
    }

    /**
     * Restores internal frame to regular state.
     */
    private class RestoreAction extends AbstractAction {
        private static final long serialVersionUID = -961768442993544104L;

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
            if (frame.isMaximizable() && frame.isMaximum() && frame.isIcon()) {

                try {
                    frame.setIcon(false);
                } catch (PropertyVetoException e) {
                }
            } else if (frame.isMaximizable() && frame.isMaximum()) {

                try {
                    frame.setMaximum(false);
                } catch (PropertyVetoException e) {
                }
            } else if (frame.isIconifiable() && frame.isIcon()) {

                try {
                    frame.setIcon(false);
                } catch (PropertyVetoException e) {
                }
            }
        }
    }

    /**
     * Handles moving internal frame.
     */
    private class MoveAction extends AbstractAction {
        private static final long serialVersionUID = 7940900832761279111L;

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
        private static final long serialVersionUID = 4020962135221310152L;
        private boolean           show; // whether to show the menu

        /**
         * Creates a new ShowSystemMenuAction object.
         *
         * @param show the ShowSystemMenuAction object.
         */
        public ShowSystemMenuAction(boolean show) {
            this.show = show;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (show) {
                windowMenu.doClick();
            } else {
                windowMenu.setVisible(false);
            }
        }
    }

    /**
     * Handles resizing internal frame.
     */
    private class SizeAction extends AbstractAction {
        private static final long serialVersionUID = -1079290485715681580L;

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
        private static final long serialVersionUID = 797986964401472674L;
        private String            uiKey;

        /**
         * Creates a new NoFocusButton object.
         *
         * @param uiKey the UI key for the button properties.
         */
        public NoFocusButton(String uiKey) {
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
            setFocusable(false);
            this.uiKey = uiKey;
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
