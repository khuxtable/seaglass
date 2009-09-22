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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.seaglass.painter.Painter;
import com.seaglass.util.MacPainterFactory;
import com.seaglass.util.PlatformUtils;
import com.seaglass.util.SeaGlassUtils;
import com.seaglass.util.WindowUtils;

import sun.swing.SwingUtilities2;

/**
 * Class that manages a JLF awt.Window-descendant class's title bar.
 * <p>
 * This class assumes it will be created with a particular window decoration
 * style, and that if the style changes, a new one will be created.
 */
/**
 * @author Kathryn Huxtable
 * 
 */
public class SeaGlassTitlePane extends JComponent {
    private static final Color     DEFAULT_EMPHASIS_COLOR = new Color(255, 255, 255, 110);
    private static final Border    handyEmptyBorder       = new EmptyBorder(0, 0, 0, 0);
    private static final int       IMAGE_HEIGHT           = 16;
    private static final int       IMAGE_WIDTH            = 16;

    /**
     * PropertyChangeListener added to the JRootPane.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * Listener installed on buttons to listen for rollover changes.
     */
    private RolloverListener       rolloverListener       = new RolloverListener();

    /**
     * Action used to close the Window.
     */
    private Action                 closeAction;

    /**
     * Action used to iconify the Frame.
     */
    private Action                 iconifyAction;

    /**
     * Action to restore the Frame size.
     */
    private Action                 restoreAction;

    /**
     * Action to restore the Frame size.
     */
    private Action                 maximizeAction;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton                toggleButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton                iconifyButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton                closeButton;

    private Icon                   closeIcon;
    private Icon                   closeIconModified;
    private Icon                   closeIconUnfocused;
    private Icon                   closeIconUnfocusedModified;

    private Icon                   iconifyIcon;
    private Icon                   iconifyIconUnfocused;

    /**
     * Icon used for toggleButton when window is normal size.
     */
    private Icon                   maximizeIcon;
    private Icon                   maximizeIconUnfocused;

    /**
     * Icon used for toggleButton when window is maximized.
     */
    private Icon                   minimizeIcon;
    private Icon                   minimizeIconUnfocused;

    /**
     * Listens for changes in the state of the Window listener to update the
     * state of the widgets.
     */
    private WindowListener         windowListener;

    /**
     * Window we're currently in.
     */
    private Window                 window;

    /**
     * Listens for changes in the property indicating that the document has been
     * modified. This is installed on the rootPane.
     */
    private PropertyChangeListener documentModifiedListener;

    /**
     * JRootPane rendering for.
     */
    private JRootPane              rootPane;

    // private int buttonsWidth;

    /**
     * Buffered Frame.state property. As state isn't bound, this is kept to
     * determine when to avoid updating widgets.
     */
    private int                    state;

    /**
     * AqvavitRootPaneUI that created us.
     */
    private SeaGlassRootPaneUI     rootPaneUI;

    // Colors
    private Color                  inactiveForeground     = UIManager.getColor("textInactiveText");
    private Color                  activeForeground       = null;
    private Painter<Component>     backgroundPainter      = null;

    public SeaGlassTitlePane(JRootPane root, SeaGlassRootPaneUI ui) {
        this.rootPane = root;
        rootPaneUI = ui;

        state = -1;

        installSubcomponents();
        determineColors();
        installDefaults();

        setLayout(createLayout());
        WindowUtils.installJComponentRepainterOnWindowFocusChanged(this);
    }

    /**
     * Uninstalls the necessary state.
     */
    // private void uninstall() {
    // uninstallListeners();
    // window = null;
    // removeAll();
    // }
    /**
     * Installs the necessary listeners.
     */
    private void installListeners() {
        documentModifiedListener = createDocumentModifiedListener();
        rootPane.addPropertyChangeListener(documentModifiedListener);
        if (window != null) {
            windowListener = createWindowListener();
            window.addWindowListener(windowListener);
            propertyChangeListener = createWindowPropertyChangeListener();
            window.addPropertyChangeListener(propertyChangeListener);
        }
    }

    /**
     * Uninstalls the necessary listeners.
     */
    private void uninstallListeners() {
        if (window != null) {
            window.removeWindowListener(windowListener);
            window.removePropertyChangeListener(propertyChangeListener);
        }
        rootPane.removePropertyChangeListener(documentModifiedListener);
    }

    /**
     * Returns the <code>WindowListener</code> to add to the <code>Window</code>
     * .
     */
    private WindowListener createWindowListener() {
        return new WindowHandler();
    }

    /**
     * Returns the <code>PropertyChangeListener</code> to install on the
     * <code>Window</code>.
     */
    private PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    /**
     * Returns the <code>PropertyChangeListener</code> to install on the
     * <code>rootPane</code>.
     */
    private PropertyChangeListener createDocumentModifiedListener() {
        return new DocumentModifiedListener();
    }

    /**
     * Returns the <code>JRootPane</code> this was created for.
     */
    public JRootPane getRootPane() {
        return rootPane;
    }

    /**
     * Returns the decoration style of the <code>JRootPane</code>.
     */
    private int getWindowDecorationStyle() {
        return getRootPane().getWindowDecorationStyle();
    }

    public void addNotify() {
        super.addNotify();

        uninstallListeners();

        window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            if (window instanceof Frame) {
                setState(((Frame) window).getExtendedState());
            } else {
                setState(0);
            }
            setActive(window.isActive());
            installListeners();
        }
    }

    public void removeNotify() {
        super.removeNotify();

        uninstallListeners();
        window = null;
    }

    /**
     * Adds any sub-Components contained in the <code>AqvavitTitlePane</code>.
     */
    private void installSubcomponents() {
        int decorationStyle = getWindowDecorationStyle();
        if (decorationStyle == JRootPane.FRAME) {
            createActions();
            createButtons();
            add(iconifyButton);
            add(toggleButton);
            add(closeButton);
        } else if (decorationStyle == JRootPane.PLAIN_DIALOG || decorationStyle == JRootPane.INFORMATION_DIALOG
                || decorationStyle == JRootPane.ERROR_DIALOG || decorationStyle == JRootPane.COLOR_CHOOSER_DIALOG
                || decorationStyle == JRootPane.FILE_CHOOSER_DIALOG || decorationStyle == JRootPane.QUESTION_DIALOG
                || decorationStyle == JRootPane.WARNING_DIALOG) {
            createActions();
            createButtons();
            add(closeButton);
        }
    }

    /**
     * Determines the Colors to draw with.
     */
    private void determineColors() {
        backgroundPainter = MacPainterFactory.createTitleBarPainter();

        switch (getWindowDecorationStyle()) {
        case JRootPane.FRAME:
            activeForeground = UIManager.getColor("textForeground");
            break;
        case JRootPane.ERROR_DIALOG:
            activeForeground = UIManager.getColor("OptionPane.errorDialog.titlePane.foreground");
            break;
        case JRootPane.QUESTION_DIALOG:
        case JRootPane.COLOR_CHOOSER_DIALOG:
        case JRootPane.FILE_CHOOSER_DIALOG:
            activeForeground = UIManager.getColor("OptionPane.questionDialog.titlePane.foreground");
            break;
        case JRootPane.WARNING_DIALOG:
            activeForeground = UIManager.getColor("OptionPane.warningDialog.titlePane.foreground");
            break;
        case JRootPane.PLAIN_DIALOG:
        case JRootPane.INFORMATION_DIALOG:
        default:
            activeForeground = UIManager.getColor("activeCaptionText");
            break;
        }
    }

    /**
     * Installs the fonts and necessary properties on the AqvavitTitlePane.
     */
    private void installDefaults() {
        setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
    }

    /**
     * Uninstalls any previously installed UI values.
     */
    // private void uninstallDefaults() {
    // }
    /**
     * Closes the Window.
     */
    private void close() {
        Window window = getWindow();

        if (window != null) {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the Frame.
     */
    private void iconify() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(state | Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the Frame.
     */
    private void maximize() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Restores the Frame size.
     */
    private void restore() {
        Frame frame = getFrame();

        if (frame == null) {
            return;
        }

        if ((state & Frame.ICONIFIED) != 0) {
            frame.setExtendedState(state & ~Frame.ICONIFIED);
        } else {
            frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Create the <code>Action</code>s that get associated with the buttons and
     * menu items.
     */
    private void createActions() {
        closeAction = new CloseAction();
        if (getWindowDecorationStyle() == JRootPane.FRAME) {
            iconifyAction = new IconifyAction();
            restoreAction = new RestoreAction();
            maximizeAction = new MaximizeAction();
        }
    }

    /**
     * Returns a <code>JButton</code> appropriate for placement on the
     * TitlePane.
     */
    private JButton createTitleButton() {
        JButton button = new JButton();

        button.setFocusPainted(false);
        button.setFocusable(false);
        button.addMouseListener(rolloverListener);

        button.setUI((ButtonUI) SynthLookAndFeel.createUI(button));
        return button;
    }

    /**
     * Creates the Buttons that will be placed on the TitlePane.
     */
    private void createButtons() {
        closeButton = createTitleButton();
        closeButton.setAction(closeAction);
        closeButton.setName("TitlePane.closeButton");

        closeIcon = UIManager.getIcon("TitlePane.closeIcon");
        closeIconModified = UIManager.getIcon("TitlePane.closeIconModified");
        closeIconUnfocused = UIManager.getIcon("TitlePane.closeIconUnfocused");
        closeIconUnfocusedModified = UIManager.getIcon("TitlePane.closeIconUnfocusedModified");

        closeButton.setText(null);
        closeButton.putClientProperty("paintActive", Boolean.TRUE);
        closeButton.setBorder(handyEmptyBorder);
        closeButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Close");
        closeButton.setIcon(closeIcon);

        if (getWindowDecorationStyle() == JRootPane.FRAME) {
            maximizeIcon = UIManager.getIcon("TitlePane.maximizeIcon");
            minimizeIcon = UIManager.getIcon("TitlePane.minimizeIcon");
            maximizeIconUnfocused = UIManager.getIcon("TitlePane.maximizeIconUnfocused");
            minimizeIconUnfocused = UIManager.getIcon("TitlePane.minimizeIconUnfocused");
            
            iconifyIcon = UIManager.getIcon("TitlePane.iconifyIcon");
            iconifyIconUnfocused = UIManager.getIcon("TitlePane.iconifyIconUnfocused");

            iconifyButton = createTitleButton();
            iconifyButton.setAction(iconifyAction);
            iconifyButton.setName("InternalFrameTitlePane.iconifyButton");
            iconifyButton.setText(null);
            iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
            iconifyButton.setBorder(handyEmptyBorder);
            iconifyButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Iconify");
            iconifyButton.setIcon(iconifyIcon);

            toggleButton = createTitleButton();
            toggleButton.setAction(restoreAction);
            toggleButton.putClientProperty("paintActive", Boolean.TRUE);
            toggleButton.setName("InternalFrameTitlePane.maximizeButton");
            toggleButton.setBorder(handyEmptyBorder);
            toggleButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Maximize");
            toggleButton.setIcon(maximizeIcon);
        }
    }

    /**
     * Returns the <code>LayoutManager</code> that should be installed on the
     * <code>AqvavitTitlePane</code>.
     */
    private LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    /**
     * Updates state dependant upon the Window's active state.
     */
    private void setActive(boolean isActive) {
        Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

        closeButton.putClientProperty("paintActive", activeB);
        if (getWindowDecorationStyle() == JRootPane.FRAME) {
            iconifyButton.putClientProperty("paintActive", activeB);
            toggleButton.putClientProperty("paintActive", activeB);
        }
        // Repaint the whole thing as the Borders that are used have
        // different colors for active vs inactive
        getRootPane().repaint();
    }

    /**
     * Sets the state of the Window.
     */
    private void setState(int state) {
        setState(state, false);
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is true
     * and the state has not changed, this will update anyway.
     */
    private void setState(int state, boolean updateRegardless) {
        Window w = getWindow();

        if (w != null && getWindowDecorationStyle() == JRootPane.FRAME) {
            if (this.state == state && !updateRegardless) {
                return;
            }
            Frame frame = getFrame();

            if (frame != null) {
                JRootPane rootPane = getRootPane();

                if (((state & Frame.MAXIMIZED_BOTH) != 0)
                        && (rootPane.getBorder() == null || (rootPane.getBorder() instanceof UIResource)) && frame.isShowing()) {
                    rootPane.setBorder(null);
                } else if ((state & Frame.MAXIMIZED_BOTH) == 0) {
                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    rootPaneUI.installBorder(rootPane);
                }
                if (frame.isResizable()) {
                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                        updateToggleButton(restoreAction, minimizeIcon);
                        maximizeAction.setEnabled(false);
                        restoreAction.setEnabled(true);
                    } else {
                        updateToggleButton(maximizeAction, maximizeIcon);
                        maximizeAction.setEnabled(true);
                        restoreAction.setEnabled(false);
                    }
                    if (toggleButton.getParent() == null || iconifyButton.getParent() == null) {
                        add(toggleButton);
                        add(iconifyButton);
                        revalidate();
                        repaint();
                    }
                    toggleButton.setText(null);
                } else {
                    maximizeAction.setEnabled(false);
                    restoreAction.setEnabled(false);
                    if (toggleButton.getParent() != null) {
                        remove(toggleButton);
                        revalidate();
                        repaint();
                    }
                }
            } else {
                // Not contained in a Frame
                maximizeAction.setEnabled(false);
                restoreAction.setEnabled(false);
                iconifyAction.setEnabled(false);
                remove(toggleButton);
                remove(iconifyButton);
                revalidate();
                repaint();
            }
            closeAction.setEnabled(true);
            this.state = state;
        }
    }

    /**
     * Updates the toggle button to contain the Icon <code>icon</code>, and
     * Action <code>action</code>.
     */
    private void updateToggleButton(Action action, Icon icon) {
        toggleButton.setAction(action);
        toggleButton.setIcon(icon);
        toggleButton.setText(null);
        toggleButton.repaint();
    }

    /**
     * Returns the Frame rendering in. This will return null if the
     * <code>JRootPane</code> is not contained in a <code>Frame</code>.
     */
    private Frame getFrame() {
        Window window = getWindow();

        if (window instanceof Frame) {
            return (Frame) window;
        }
        return null;
    }

    /**
     * Returns the <code>Window</code> the <code>JRootPane</code> is contained
     * in. This will return null if there is no parent ancestor of the
     * <code>JRootPane</code>.
     */
    private Window getWindow() {
        return window;
    }

    /**
     * Returns the String to display as the title.
     */
    private String getTitle() {
        Window w = getWindow();

        if (w instanceof Frame) {
            return ((Frame) w).getTitle();
        } else if (w instanceof Dialog) {
            return ((Dialog) w).getTitle();
        }
        return null;
    }

    /**
     * Renders the TitlePane.
     */
    public void paintComponent(Graphics g) {
        // As state isn't bound, we need a convenience place to check
        // if it has changed. Changing the state typically changes the
        if (getFrame() != null) {
            setState(getFrame().getExtendedState());
        }
        JRootPane rootPane = getRootPane();
        Window window = getWindow();
        boolean leftToRight = (window == null) ? rootPane.getComponentOrientation().isLeftToRight() : window
            .getComponentOrientation().isLeftToRight();
        boolean isSelected = (window == null) ? true : window.isActive();
        int width = getWidth();
        int height = getHeight();

        Color foreground;

        boolean isModified = rootPane.getClientProperty("Window.documentModified") == Boolean.TRUE;
        if (isSelected) {
            foreground = activeForeground;
            closeButton.setIcon(isModified ? closeIconModified : closeIcon);
            iconifyButton.setIcon(iconifyIcon);
            Icon icon = toggleButton.getIcon();
            if (icon == minimizeIconUnfocused) {
                toggleButton.setIcon(minimizeIcon);
            } else if (icon == maximizeIconUnfocused) {
                toggleButton.setIcon(maximizeIcon);
            }
        } else {
            foreground = inactiveForeground;
            closeButton.setIcon(isModified ? closeIconUnfocusedModified : closeIconUnfocused);
            iconifyButton.setIcon(iconifyIconUnfocused);
            Icon icon = toggleButton.getIcon();
            if (icon == minimizeIcon) {
                toggleButton.setIcon(minimizeIconUnfocused);
            } else if (icon == maximizeIcon) {
                toggleButton.setIcon(maximizeIconUnfocused);
            }
        }

        backgroundPainter.paint((Graphics2D) g, this, getWidth(), getHeight());

        int xOffset = leftToRight ? 5 : width - 5;

        if (getWindowDecorationStyle() == JRootPane.FRAME) {
            xOffset += leftToRight ? 5 : -5;
        }

        if (PlatformUtils.isMac()) {
            if (toggleButton != null && toggleButton.getParent() != null) {
                Rectangle toggleRect = toggleButton.getBounds();
                xOffset += toggleRect.x + toggleRect.width + 5;
            }
        }

        if (!PlatformUtils.isMac()) {
            JMenuBar menuBar = getRootPane().getJMenuBar();
            for (int i = 0, size = menuBar.getMenuCount(); i < size; i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu.isOpaque()) {
                    menu.setOpaque(false);
                }
            }
            menuBar.paintComponents(g);
        }

        String theTitle = getTitle();
        if (theTitle != null) {
            FontMetrics fm = SwingUtilities2.getFontMetrics(rootPane, g);

            g.setColor(foreground);

            int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent();

            Rectangle rect = new Rectangle(0, 0, 0, 0);
            if (!PlatformUtils.isMac()) {
                if (iconifyButton != null && iconifyButton.getParent() != null) {
                    rect = iconifyButton.getBounds();
                }
            }
            int titleW;

            if (leftToRight) {
                if (rect.x == 0) {
                    rect.x = window.getWidth() - window.getInsets().right - 2;
                }
                titleW = rect.x - xOffset - 4;
                theTitle = SwingUtilities2.clipStringIfNecessary(rootPane, fm, theTitle, titleW);
            } else {
                titleW = xOffset - rect.x - rect.width - 4;
                theTitle = SwingUtilities2.clipStringIfNecessary(rootPane, fm, theTitle, titleW);
                xOffset -= SwingUtilities2.stringWidth(rootPane, fm, theTitle);
            }
            int titleLength = SwingUtilities2.stringWidth(rootPane, fm, theTitle);
            if (leftToRight) {
                xOffset = (width - titleLength) / 2;
            } else {
                xOffset -= (titleW - titleLength) / 2;
            }

            SeaGlassUtils.drawEmphasizedString(g, foreground, DEFAULT_EMPHASIS_COLOR, theTitle, xOffset, yOffset);
            xOffset += leftToRight ? titleLength + 5 : -5;
        }
    }

    /**
     * Actions used to <code>close</code> the <code>Window</code>.
     */
    private class CloseAction extends AbstractAction {
        private static final long serialVersionUID = 8870978129941197734L;

        public CloseAction() {
            super(UIManager.getString("AqvavitTitlePane.closeTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }

    /**
     * Actions used to <code>iconfiy</code> the <code>Frame</code>.
     */
    private class IconifyAction extends AbstractAction {
        private static final long serialVersionUID = -6117899153104799133L;

        public IconifyAction() {
            super(UIManager.getString("AqvavitTitlePane.iconifyTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            iconify();
        }
    }

    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class RestoreAction extends AbstractAction {
        private static final long serialVersionUID = 3012880765729186316L;

        public RestoreAction() {
            super(UIManager.getString("AqvavitTitlePane.restoreTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            restore();
        }
    }

    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class MaximizeAction extends AbstractAction {
        private static final long serialVersionUID = -8151657853606875012L;

        public MaximizeAction() {
            super(UIManager.getString("AqvavitTitlePane.maximizeTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            maximize();
        }
    }

    private class TitlePaneLayout implements LayoutManager {
        public void addLayoutComponent(String name, Component c) {
        }

        public void removeLayoutComponent(Component c) {
        }

        public Dimension preferredLayoutSize(Container c) {
            int height = computeHeight();
            return new Dimension(height, height);
        }

        public Dimension minimumLayoutSize(Container c) {
            return preferredLayoutSize(c);
        }

        private int computeHeight() {
            FontMetrics fm = rootPane.getFontMetrics(getFont());
            int fontHeight = fm.getHeight();
            fontHeight += 7;
            int iconHeight = 0;
            if (getWindowDecorationStyle() == JRootPane.FRAME) {
                iconHeight = IMAGE_HEIGHT;
            }

            int finalHeight = Math.max(fontHeight, iconHeight);
            return finalHeight;
        }

        public void layoutContainer(Container c) {
            boolean leftToRight = (window == null) ? getRootPane().getComponentOrientation().isLeftToRight() : window
                .getComponentOrientation().isLeftToRight();

            int w = getWidth();
            int x;
            int y = 3;
            int spacing;
            int buttonHeight;
            int buttonWidth;

            if (closeButton != null && closeButton.getIcon() != null) {
                buttonHeight = closeButton.getIcon().getIconHeight();
                buttonWidth = closeButton.getIcon().getIconWidth();
            } else {
                buttonHeight = IMAGE_HEIGHT;
                buttonWidth = IMAGE_WIDTH;
            }

            // assumes all buttons have the same dimensions
            // these dimensions include the borders

            if (PlatformUtils.isMac()) {
                x = leftToRight ? 0 : w;
                spacing = 7;
                x += leftToRight ? spacing : -spacing - buttonWidth;
                spacing = 5;
                if (closeButton != null) {
                    closeButton.setBounds(x, y, buttonWidth, buttonHeight);
                    x += leftToRight ? spacing + buttonWidth : -spacing - buttonWidth;
                }

                if (getWindowDecorationStyle() == JRootPane.FRAME) {
                    if (iconifyButton != null && iconifyButton.getParent() != null) {
                        iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
                        x += leftToRight ? spacing + buttonWidth : -spacing - buttonWidth;
                    }

                    if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                        if (toggleButton.getParent() != null) {
                            toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
                        }
                    }
                }
            } else {
                // spacing = 5;
                // x = leftToRight ? spacing : w - buttonWidth - spacing;
                // if (menuBar != null) {
                // menuBar.setBounds(x, 0/*y*/, buttonWidth, buttonHeight);
                // }

                x = leftToRight ? w : 0;
                spacing = 4;
                x += leftToRight ? -spacing - buttonWidth : spacing;
                if (closeButton != null) {
                    closeButton.setBounds(x, y, buttonWidth, buttonHeight);
                    x += leftToRight ? -spacing - buttonWidth : spacing + buttonWidth;
                }

                if (getWindowDecorationStyle() == JRootPane.FRAME) {
                    if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                        if (toggleButton.getParent() != null) {
                            toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
                            x += leftToRight ? -spacing - buttonWidth : spacing + buttonWidth;
                        }
                    }

                    if (iconifyButton != null && iconifyButton.getParent() != null) {
                        iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
                    }
                }
            }
        }
    }

    /**
     * PropertyChangeListener installed on the Window. Updates the necessary
     * state as the state of the Window changes.
     */
    private class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent pce) {
            String name = pce.getPropertyName();

            // Frame.state isn't currently bound.
            if ("resizable".equals(name) || "state".equals(name)) {
                Frame frame = getFrame();

                if (frame != null) {
                    setState(frame.getExtendedState(), true);
                }
                if ("resizable".equals(name)) {
                    getRootPane().repaint();
                }
            } else if ("title".equals(name)) {
                repaint();
            } else if ("componentOrientation" == name) {
                revalidate();
                repaint();
            } else if ("iconImage" == name) {
                revalidate();
                repaint();
            }
        }
    }

    /**
     * Installed on window decoration buttons to listen for the mouse entering
     * or leaving and to repaint the three buttons. This is used to display the
     * interior symbols or not. We'd use a check against rollover, but that
     * doesn't work if the buttons aren't enabled. Neither does this, however.
     */
    private class RolloverListener extends MouseAdapter {

        /**
         * {@inheritDoc}
         */
        public void mouseEntered(MouseEvent e) {
            closeButton.getModel().setRollover(true);
            if (getWindowDecorationStyle() == JRootPane.FRAME) {
                iconifyButton.getModel().setRollover(true);
                toggleButton.getModel().setRollover(true);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void mouseExited(MouseEvent e) {
            closeButton.getModel().setRollover(false);
            if (getWindowDecorationStyle() == JRootPane.FRAME) {
                iconifyButton.getModel().setRollover(false);
                toggleButton.getModel().setRollover(false);
            }
        }
    }

    /**
     * WindowListener installed on the Window, updates the state as necessary.
     */
    private class WindowHandler extends WindowAdapter {
        public void windowActivated(WindowEvent ev) {
            setActive(true);
        }

        public void windowDeactivated(WindowEvent ev) {
            setActive(false);
        }
    }

    /**
     * Listener to update the document changed indicator.
     */
    private class DocumentModifiedListener implements PropertyChangeListener {

        /**
         * Boolean.TRUE if the window has been modified.
         */
        private static final String WINDOW_DOCUMENT_MODIFIED = "Window.documentModified";

        /**
         * {@inheritDoc}
         */
        public void propertyChange(PropertyChangeEvent e) {
            if (closeButton != null && WINDOW_DOCUMENT_MODIFIED.equals(e.getPropertyName())) {
                boolean isModified = e.getNewValue() == Boolean.TRUE;
                Icon icon = isModified ? closeIconModified : closeIcon;
                closeButton.setIcon(icon);
                closeButton.revalidate();
                closeButton.repaint();
            }
        }
    }
}
