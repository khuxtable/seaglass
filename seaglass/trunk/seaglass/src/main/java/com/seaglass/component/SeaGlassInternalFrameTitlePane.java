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
 * $Id: SeaGlassRootPaneUI.java 272 2009-10-22 23:16:03Z kathryn@kathrynhuxtable.org $
 */
package com.seaglass.component;

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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglass.SeaGlassContext;
import com.seaglass.SeaGlassLookAndFeel;

import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthUI;

/**
 * Manage a title bar for an internal frame.
 * 
 * Copied from SynthInternalFrameTitlePane, which is package local.
 * 
 * @see javax.swing.plaf.synth.SynthInternalFrameTitlePane
 */
public class SeaGlassInternalFrameTitlePane extends JComponent implements SynthUI, PropertyChangeListener {
    // Basic
    protected JMenuBar               menuBar;
    protected JButton                iconButton;
    protected JButton                maxButton;
    protected JButton                closeButton;

    protected JMenu                  windowMenu;
    protected JInternalFrame         frame;

    protected Color                  selectedTitleColor;
    protected Color                  selectedTextColor;
    protected Color                  notSelectedTitleColor;
    protected Color                  notSelectedTextColor;

    protected Icon                   maxIcon;
    protected Icon                   minIcon;
    protected Icon                   iconIcon;
    protected Icon                   closeIcon;

    protected PropertyChangeListener propertyChangeListener;

    protected Action                 closeAction;
    protected Action                 maximizeAction;
    protected Action                 iconifyAction;
    protected Action                 restoreAction;
    protected Action                 moveAction;
    protected Action                 sizeAction;

    protected static final String    CLOSE_CMD    = UIManager.getString("InternalFrameTitlePane.closeButtonText");
    protected static final String    ICONIFY_CMD  = UIManager.getString("InternalFrameTitlePane.minimizeButtonText");
    protected static final String    RESTORE_CMD  = UIManager.getString("InternalFrameTitlePane.restoreButtonText");
    protected static final String    MAXIMIZE_CMD = UIManager.getString("InternalFrameTitlePane.maximizeButtonText");
    protected static final String    MOVE_CMD     = UIManager.getString("InternalFrameTitlePane.moveButtonText");
    protected static final String    SIZE_CMD     = UIManager.getString("InternalFrameTitlePane.sizeButtonText");

    private String                   closeButtonToolTip;
    private String                   iconButtonToolTip;
    private String                   restoreButtonToolTip;
    private String                   maxButtonToolTip;
    private Handler                  handler;

    // Synth
    protected JPopupMenu             systemPopupMenu;
    protected JButton                menuButton;

    private SynthStyle               style;
    private int                      titleSpacing;
    private int                      buttonSpacing;
    // Alignment for the title, one of SwingConstants.(LEADING|TRAILING|CENTER)
    private int                      titleAlignment;

    public SeaGlassInternalFrameTitlePane(JInternalFrame f) {
        frame = f;
        installTitlePane();
    }

    public String getUIClassID() {
        return "InternalFrameTitlePaneUI";
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        if (frame != null) {
            if (frame.isSelected()) {
                return SELECTED;
            }
        }
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    protected void installTitlePane() {
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

    protected void addSubComponents() {
        menuButton.setName("InternalFrameTitlePane.menuButton");
        iconButton.setName("InternalFrameTitlePane.iconifyButton");
        maxButton.setName("InternalFrameTitlePane.maximizeButton");
        closeButton.setName("InternalFrameTitlePane.closeButton");

        add(menuButton);
        add(iconButton);
        add(maxButton);
        add(closeButton);
    }

    protected void createActions() {
        maximizeAction = new MaximizeAction();
        iconifyAction = new IconifyAction();
        closeAction = new CloseAction();
        restoreAction = new RestoreAction();
        moveAction = new MoveAction();
        sizeAction = new SizeAction();
    }

    ActionMap createActionMap() {
        ActionMap map = new ActionMapUIResource();
        map.put("showSystemMenu", new ShowSystemMenuAction(true));
        map.put("hideSystemMenu", new ShowSystemMenuAction(false));
        return map;
    }

    protected void installListeners() {
        // Basic
        if (propertyChangeListener == null) {
            propertyChangeListener = createPropertyChangeListener();
        }
        frame.addPropertyChangeListener(propertyChangeListener);

        // Synth
        frame.addPropertyChangeListener(this);
        addPropertyChangeListener(this);
    }

    protected void uninstallListeners() {
        // Synth
        frame.removePropertyChangeListener(this);
        removePropertyChangeListener(this);

        // Basic
        frame.removePropertyChangeListener(propertyChangeListener);
        handler = null;
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(this, ENABLED);
        SynthStyle oldStyle = style;
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            maxIcon = style.getIcon(context, "InternalFrameTitlePane.maximizeIcon");
            minIcon = style.getIcon(context, "InternalFrameTitlePane.minimizeIcon");
            iconIcon = style.getIcon(context, "InternalFrameTitlePane.iconifyIcon");
            closeIcon = style.getIcon(context, "InternalFrameTitlePane.closeIcon");
            titleSpacing = style.getInt(context, "InternalFrameTitlePane.titleSpacing", 2);
            buttonSpacing = style.getInt(context, "InternalFrameTitlePane.buttonSpacing", 0);
            String alignString = (String) style.get(context, "InternalFrameTitlePane.titleAlignment");
            titleAlignment = SwingConstants.LEADING;
            if (alignString != null) {
                alignString = alignString.toUpperCase();
                if (alignString.equals("TRAILING")) {
                    titleAlignment = SwingConstants.TRAILING;
                } else if (alignString.equals("CENTER")) {
                    titleAlignment = SwingConstants.CENTER;
                }
            }
        }
        context.dispose();
    }

    protected void installDefaults() {
        // Basic
        maxIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
        minIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
        iconIcon = UIManager.getIcon("InternalFrame.iconifyIcon");
        closeIcon = UIManager.getIcon("InternalFrame.closeIcon");

        selectedTitleColor = UIManager.getColor("InternalFrame.activeTitleBackground");
        selectedTextColor = UIManager.getColor("InternalFrame.activeTitleForeground");
        notSelectedTitleColor = UIManager.getColor("InternalFrame.inactiveTitleBackground");
        notSelectedTextColor = UIManager.getColor("InternalFrame.inactiveTitleForeground");
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        closeButtonToolTip = UIManager.getString("InternalFrame.closeButtonToolTip");
        iconButtonToolTip = UIManager.getString("InternalFrame.iconButtonToolTip");
        restoreButtonToolTip = UIManager.getString("InternalFrame.restoreButtonToolTip");
        maxButtonToolTip = UIManager.getString("InternalFrame.maxButtonToolTip");

        // Synth
        updateStyle(this);
    }

    protected void uninstallDefaults() {
        // Synth
        SeaGlassContext context = getContext(this, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        JInternalFrame.JDesktopIcon di = frame.getDesktopIcon();
        if (di != null && di.getComponentPopupMenu() == systemPopupMenu) {
            // Release link to systemMenu from the JInternalFrame
            di.setComponentPopupMenu(null);
        }

        // Basic
    }

    protected void createButtons() {
        iconButton = new NoFocusButton("InternalFrameTitlePane.iconifyButtonAccessibleName", "InternalFrameTitlePane.iconifyButtonOpacity");
        iconButton.addActionListener(iconifyAction);
        if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
            iconButton.setToolTipText(iconButtonToolTip);
        }

        maxButton = new NoFocusButton("InternalFrameTitlePane.maximizeButtonAccessibleName", "InternalFrameTitlePane.maximizeButtonOpacity");
        maxButton.addActionListener(maximizeAction);

        closeButton = new NoFocusButton("InternalFrameTitlePane.closeButtonAccessibleName", "InternalFrameTitlePane.closeButtonOpacity");
        closeButton.addActionListener(closeAction);
        if (closeButtonToolTip != null && closeButtonToolTip.length() != 0) {
            closeButton.setToolTipText(closeButtonToolTip);
        }

        setButtonIcons();
    }

    protected void setButtonIcons() {
        if (frame.isIcon()) {
            if (minIcon != null) {
                iconButton.setIcon(minIcon);
            }
            if (restoreButtonToolTip != null && restoreButtonToolTip.length() != 0) {
                iconButton.setToolTipText(restoreButtonToolTip);
            }
            if (maxIcon != null) {
                maxButton.setIcon(maxIcon);
            }
            if (maxButtonToolTip != null && maxButtonToolTip.length() != 0) {
                maxButton.setToolTipText(maxButtonToolTip);
            }
        } else if (frame.isMaximum()) {
            if (iconIcon != null) {
                iconButton.setIcon(iconIcon);
            }
            if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
                iconButton.setToolTipText(iconButtonToolTip);
            }
            if (minIcon != null) {
                maxButton.setIcon(minIcon);
            }
            if (restoreButtonToolTip != null && restoreButtonToolTip.length() != 0) {
                maxButton.setToolTipText(restoreButtonToolTip);
            }
        } else {
            if (iconIcon != null) {
                iconButton.setIcon(iconIcon);
            }
            if (iconButtonToolTip != null && iconButtonToolTip.length() != 0) {
                iconButton.setToolTipText(iconButtonToolTip);
            }
            if (maxIcon != null) {
                maxButton.setIcon(maxIcon);
            }
            if (maxButtonToolTip != null && maxButtonToolTip.length() != 0) {
                maxButton.setToolTipText(maxButtonToolTip);
            }
        }
        if (closeIcon != null) {
            closeButton.setIcon(closeIcon);
        }
    }

    private static class JPopupMenuUIResource extends JPopupMenu implements UIResource {
    }

    protected void assembleSystemMenu() {
        systemPopupMenu = new JPopupMenuUIResource();
        addSystemMenuItems(systemPopupMenu);
        enableActions();
        menuButton = createNoFocusButton();
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

    protected void addSystemMenuItems(JPopupMenu menu) {
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

    protected JMenu createSystemMenu() {
        return new JMenu("    ");
    }

    protected JMenuBar createSystemMenuBar() {
        menuBar = new SystemMenuBar();
        menuBar.setBorderPainted(false);
        return menuBar;
    }

    protected void showSystemMenu() {
        Insets insets = frame.getInsets();
        if (!frame.isIcon()) {
            systemPopupMenu.show(frame, insets.left, getY() + getHeight());
        } else {
            systemPopupMenu.show(menuButton, getX() - insets.left - insets.right, getY() - systemPopupMenu.getPreferredSize().height
                    - insets.bottom - insets.top);
        }
    }

    // SeaGlassInternalFrameTitlePane has no UI, we'll invoke paint on it.
    public void paintComponent(Graphics g) {
        SeaGlassContext context = getContext(this);
        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintInternalFrameTitlePaneBackground(context, g, 0, 0, getWidth(), getHeight());
        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        String title = frame.getTitle();

        if (title != null) {
            SynthStyle style = context.getStyle();

            g.setColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
            g.setFont(style.getFont(context));

            // Center text vertically.
            FontMetrics fm = SwingUtilities2.getFontMetrics(frame, g);
            int baseline = (getHeight() + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
            JButton lastButton = null;
            if (frame.isIconifiable()) {
                lastButton = iconButton;
            } else if (frame.isMaximizable()) {
                lastButton = maxButton;
            } else if (frame.isClosable()) {
                lastButton = closeButton;
            }
            int maxX;
            int minX;
            boolean ltr = SeaGlassLookAndFeel.isLeftToRight(frame);
            int titleAlignment = this.titleAlignment;
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
                if (titleAlignment == SwingConstants.LEADING) {
                    titleAlignment = SwingConstants.TRAILING;
                } else if (titleAlignment == SwingConstants.TRAILING) {
                    titleAlignment = SwingConstants.LEADING;
                }
            }
            String clippedTitle = getTitle(title, fm, maxX - minX);
            if (clippedTitle == title) {
                // String fit, align as necessary.
                if (titleAlignment == SwingConstants.TRAILING) {
                    minX = maxX - style.getGraphicsUtils(context).computeStringWidth(context, g.getFont(), fm, title);
                } else if (titleAlignment == SwingConstants.CENTER) {
                    int width = style.getGraphicsUtils(context).computeStringWidth(context, g.getFont(), fm, title);
                    minX = Math.max(minX, (getWidth() - width) / 2);
                    minX = Math.min(maxX - width, minX);
                }
            }
            style.getGraphicsUtils(context).paintText(context, g, clippedTitle, minX, baseline - fm.getAscent(), -1);
        }
    }

    protected String getTitle(String text, FontMetrics fm, int availTextWidth) {
        return SwingUtilities2.clipStringIfNecessary(frame, fm, text, availTextWidth);
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintInternalFrameTitlePaneBorder(context, g, x, y, w, h);
    }

    protected LayoutManager createLayout() {
        SeaGlassContext context = getContext(this);
        LayoutManager lm = (LayoutManager) style.get(context, "InternalFrameTitlePane.titlePaneLayout");
        context.dispose();
        return (lm != null) ? lm : new SeaGlassTitlePaneLayout();
    }

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
    }

    /**
     * Resets the menuButton icon to match that of the frame.
     */
    private void updateMenuIcon() {
        Icon frameIcon = frame.getFrameIcon();
        SeaGlassContext context = getContext(this);
        if (frameIcon != null) {
            Dimension maxSize = (Dimension) context.getStyle().get(context, "InternalFrameTitlePane.maxFrameIconSize");
            int maxWidth = 16;
            int maxHeight = 16;
            if (maxSize != null) {
                maxWidth = maxSize.width;
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

    protected void enableActions() {
        restoreAction.setEnabled(frame.isMaximum() || frame.isIcon());
        maximizeAction.setEnabled((frame.isMaximizable() && !frame.isMaximum() && !frame.isIcon())
                || (frame.isMaximizable() && frame.isIcon()));
        iconifyAction.setEnabled(frame.isIconifiable() && !frame.isIcon());
        closeAction.setEnabled(frame.isClosable());
        sizeAction.setEnabled(false);
        moveAction.setEnabled(false);
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    class SeaGlassTitlePaneLayout implements LayoutManager {
        public void addLayoutComponent(String name, Component c) {
        }

        public void removeLayoutComponent(Component c) {
        }

        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        public Dimension minimumLayoutSize(Container c) {
            SeaGlassContext context = getContext(SeaGlassInternalFrameTitlePane.this);
            int width = 0;
            int height = 0;

            int buttonCount = 0;
            Dimension pref;

            if (frame.isClosable()) {
                pref = closeButton.getPreferredSize();
                width += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }
            if (frame.isMaximizable()) {
                pref = maxButton.getPreferredSize();
                width += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }
            if (frame.isIconifiable()) {
                pref = iconButton.getPreferredSize();
                width += pref.width;
                height = Math.max(pref.height, height);
                buttonCount++;
            }
            pref = menuButton.getPreferredSize();
            width += pref.width;
            height = Math.max(pref.height, height);

            width += Math.max(0, (buttonCount - 1) * buttonSpacing);

            FontMetrics fm = SeaGlassInternalFrameTitlePane.this.getFontMetrics(getFont());
            SynthGraphicsUtils graphicsUtils = context.getStyle().getGraphicsUtils(context);
            String frameTitle = frame.getTitle();
            int title_w = frameTitle != null ? graphicsUtils.computeStringWidth(context, fm.getFont(), fm, frameTitle) : 0;
            int title_length = frameTitle != null ? frameTitle.length() : 0;

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
            width += insets.left + insets.right;
            context.dispose();
            return new Dimension(width, height);
        }

        private int center(Component c, Insets insets, int x, boolean trailing) {
            Dimension pref = c.getPreferredSize();
            int width = pref.width;
            if (c instanceof JButton && ((JButton) c).getIcon() != null) {
                width = ((JButton) c).getIcon().getIconWidth();
            }
            if (trailing) {
                x -= width;
            }
            int y = 0;
            if (c == menuButton) {
                y = insets.top + (getHeight() - insets.top - insets.bottom - pref.height) / 2;
            }
            c.setBounds(x, y, pref.width, pref.height);
            if (pref.width > 0) {
                if (!trailing) {
                    return x + width + buttonSpacing;
                }
            }
            return x;
        }

        public void layoutContainer(Container c) {
            Insets insets = c.getInsets();

            if (SeaGlassLookAndFeel.isLeftToRight(frame)) {
                center(menuButton, insets, insets.left, false);
                int x = getWidth() - insets.right;
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
                center(menuButton, insets, getWidth() - insets.right, true);
                int x = insets.left;
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

    private JButton createNoFocusButton() {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

    // Basic

    private class Handler implements LayoutManager, PropertyChangeListener {
        //
        // PropertyChangeListener
        //
        public void propertyChange(PropertyChangeEvent evt) {
            String prop = (String) evt.getPropertyName();

            if (prop == JInternalFrame.IS_SELECTED_PROPERTY) {
                repaint();
                return;
            }

            if (prop == JInternalFrame.IS_ICON_PROPERTY || prop == JInternalFrame.IS_MAXIMUM_PROPERTY) {
                setButtonIcons();
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

        //
        // LayoutManager
        //
        public void addLayoutComponent(String name, Component c) {
        }

        public void removeLayoutComponent(Component c) {
        }

        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        public Dimension minimumLayoutSize(Container c) {
            // Calculate width.
            int width = 22;

            if (frame.isClosable()) {
                width += 19;
            }
            if (frame.isMaximizable()) {
                width += 19;
            }
            if (frame.isIconifiable()) {
                width += 19;
            }

            FontMetrics fm = frame.getFontMetrics(getFont());
            String frameTitle = frame.getTitle();
            int title_w = frameTitle != null ? SwingUtilities2.stringWidth(frame, fm, frameTitle) : 0;
            int title_length = frameTitle != null ? frameTitle.length() : 0;

            // Leave room for three characters in the title.
            if (title_length > 3) {
                int subtitle_w = SwingUtilities2.stringWidth(frame, fm, frameTitle.substring(0, 3) + "...");
                width += (title_w < subtitle_w) ? title_w : subtitle_w;
            } else {
                width += title_w;
            }

            // Calculate height.
            Icon icon = frame.getFrameIcon();
            int fontHeight = fm.getHeight();
            fontHeight += 2;
            int iconHeight = 0;
            if (icon != null) {
                // SystemMenuBar forces the icon to be 16x16 or less.
                iconHeight = Math.min(icon.getIconHeight(), 16);
            }
            iconHeight += 2;

            int height = Math.max(fontHeight, iconHeight);

            Dimension dim = new Dimension(width, height);

            // Take into account the border insets if any.
            if (getBorder() != null) {
                Insets insets = getBorder().getBorderInsets(c);
                dim.height += insets.top + insets.bottom;
                dim.width += insets.left + insets.right;
            }
            return dim;
        }

        public void layoutContainer(Container c) {
            boolean leftToRight = isLeftToRight(frame);

            int w = getWidth();
            int h = getHeight();
            int x;

            int buttonHeight = closeButton.getIcon().getIconHeight();

            Icon icon = frame.getFrameIcon();
            int iconHeight = 0;
            if (icon != null) {
                iconHeight = icon.getIconHeight();
            }
            x = (leftToRight) ? 2 : w - 16 - 2;
            menuBar.setBounds(x, (h - iconHeight) / 2, 16, 16);

            x = (leftToRight) ? w - 16 - 2 : 2;

            if (frame.isClosable()) {
                closeButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
                x += (leftToRight) ? -(16 + 2) : 16 + 2;
            }

            if (frame.isMaximizable()) {
                maxButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
                x += (leftToRight) ? -(16 + 2) : 16 + 2;
            }

            if (frame.isIconifiable()) {
                iconButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class PropertyChangeHandler implements PropertyChangeListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        public void propertyChange(PropertyChangeEvent evt) {
            getHandler().propertyChange(evt);
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class TitlePaneLayout implements LayoutManager {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        public void addLayoutComponent(String name, Component c) {
            getHandler().addLayoutComponent(name, c);
        }

        public void removeLayoutComponent(Component c) {
            getHandler().removeLayoutComponent(c);
        }

        public Dimension preferredLayoutSize(Container c) {
            return getHandler().preferredLayoutSize(c);
        }

        public Dimension minimumLayoutSize(Container c) {
            return getHandler().minimumLayoutSize(c);
        }

        public void layoutContainer(Container c) {
            getHandler().layoutContainer(c);
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class CloseAction extends AbstractAction {
        public CloseAction() {
            super(CLOSE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.isClosable()) {
                frame.doDefaultCloseAction();
            }
        }
    } // end CloseAction

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
            super(MAXIMIZE_CMD);
        }

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
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class IconifyAction extends AbstractAction {
        public IconifyAction() {
            super(ICONIFY_CMD);
        }

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
    } // end IconifyAction

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class RestoreAction extends AbstractAction {
        public RestoreAction() {
            super(RESTORE_CMD);
        }

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
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class MoveAction extends AbstractAction {
        public MoveAction() {
            super(MOVE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
            // This action is currently undefined
        }
    } // end MoveAction

    /*
     * Handles showing and hiding the system menu.
     */
    private class ShowSystemMenuAction extends AbstractAction {
        private boolean show; // whether to show the menu

        public ShowSystemMenuAction(boolean show) {
            this.show = show;
        }

        public void actionPerformed(ActionEvent e) {
            if (show) {
                windowMenu.doClick();
            } else {
                windowMenu.setVisible(false);
            }
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class SizeAction extends AbstractAction {
        public SizeAction() {
            super(SIZE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
            // This action is currently undefined
        }
    } // end SizeAction

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class SystemMenuBar extends JMenuBar {
        public boolean isFocusTraversable() {
            return false;
        }

        public void requestFocus() {
        }

        public void paint(Graphics g) {
            Icon icon = frame.getFrameIcon();
            if (icon == null) {
                icon = (Icon) DefaultLookup.get(frame, frame.getUI(), "InternalFrame.icon");
            }
            if (icon != null) {
                // Resize to 16x16 if necessary.
                if (icon instanceof ImageIcon && (icon.getIconWidth() > 16 || icon.getIconHeight() > 16)) {
                    Image img = ((ImageIcon) icon).getImage();
                    ((ImageIcon) icon).setImage(img.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
                }
                icon.paintIcon(this, g, 0, 0);
            }
        }

        public boolean isOpaque() {
            return true;
        }
    } // end SystemMenuBar

    private class NoFocusButton extends JButton {
        private String uiKey;

        public NoFocusButton(String uiKey, String opacityKey) {
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
            this.uiKey = uiKey;

            Object opacity = UIManager.get(opacityKey);
            if (opacity instanceof Boolean) {
                setOpaque(((Boolean) opacity).booleanValue());
            }
        }

        public boolean isFocusTraversable() {
            return false;
        }

        public void requestFocus() {
        };

        public AccessibleContext getAccessibleContext() {
            AccessibleContext ac = super.getAccessibleContext();
            if (uiKey != null) {
                ac.setAccessibleName(UIManager.getString(uiKey));
                uiKey = null;
            }
            return ac;
        }
    }; // end NoFocusButton

    /*
     * Convenience function for determining ComponentOrientation. Helps us avoid
     * having Munge directives throughout the code.
     */
    private static boolean isLeftToRight(Component c) {
        return c.getComponentOrientation().isLeftToRight();
    }
}
