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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassRegion;
import com.seaglasslookandfeel.SeaGlassSynthPainterImpl;
import com.seaglasslookandfeel.component.SeaGlassArrowButton;
import com.seaglasslookandfeel.util.ControlOrientation;
import com.seaglasslookandfeel.util.SeaGlassTabCloseListener;

/**
 * Sea Glass's TabbedPane UI delegate.
 *
 * <p>Based on SynthTabbedPaneUI.</p>
 */
public class SeaGlassTabbedPaneUI extends BasicTabbedPaneUI implements SeaglassUI, PropertyChangeListener {

    private SeaGlassContext tabAreaContext;
    private SeaGlassContext tabContext;
    private SeaGlassContext tabCloseContext;
    private SeaGlassContext tabContentContext;

    private SynthStyle style;
    private SynthStyle tabStyle;
    private SynthStyle tabCloseStyle;
    private SynthStyle tabAreaStyle;
    private SynthStyle tabContentStyle;

    private Rectangle textRect;
    private Rectangle iconRect;
    private Rectangle tabAreaRect;
    private Rectangle contentRect;

    private boolean selectedTabIsPressed = false;

    /**
     * Actions to be performed when tab close button is pressed and when tab is
     * actually closed.
     */
    protected SeaGlassTabCloseListener tabCloseListener;

    /** The scroll forward button. This may or may not be visible. */
    protected SynthScrollableTabButton scrollForwardButton;

    /** The scroll backward button. This may or may not be visible. */
    protected SynthScrollableTabButton scrollBackwardButton;

    /** Margin for the close button. */
    protected Insets closeButtonInsets;

    /** The size of the close button. */
    protected int closeButtonSize;

    /** Index of initial displayed tab. */
    protected int leadingTabIndex = 0;

    /** Index of final displayed tab. */
    protected int trailingTabIndex = 0;

    /** Side the tabs are on. */
    protected int tabPlacement;

    /** Horizontal/vertical abstraction. */
    protected ControlOrientation orientation;

    /**
     * Placement of the tab close button: LEFT, RIGHT, or CENTER (the default)
     * for none.
     */
    protected int tabCloseButtonPlacement;

    /** Index of the armed close button, or -1 if none is armed. */
    protected int closeButtonArmedIndex;

    /**
     * Index of the close button the mouse is hovering over, or -1 if none is
     * being hovered over.
     */
    protected int closeButtonHoverIndex;

    /**
     * Creates a new SeaGlassTabbedPaneUI object.
     */
    SeaGlassTabbedPaneUI() {
        textRect    = new Rectangle();
        iconRect    = new Rectangle();
        tabAreaRect = new Rectangle();
        contentRect = new Rectangle();
    }

    /**
     * Create the UI delegate.
     *
     * @param  c the component (not used).
     *
     * @return the UI delegate.
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTabbedPaneUI();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installComponents()
     */
    @Override
    protected void installComponents() {
        super.installComponents();

        scrollBackwardButton = (SynthScrollableTabButton) createScrollButton(WEST);
        scrollForwardButton  = (SynthScrollableTabButton) createScrollButton(EAST);
        tabPane.add(scrollBackwardButton);
        tabPane.add(scrollForwardButton);
        scrollBackwardButton.setVisible(false);
        scrollForwardButton.setVisible(false);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallComponents()
     */
    @Override
    protected void uninstallComponents() {
        super.uninstallComponents();

        if (scrollBackwardButton != null) {
            tabPane.remove(scrollBackwardButton);
            scrollBackwardButton = null;
        }

        if (scrollForwardButton != null) {
            tabPane.remove(scrollForwardButton);
            scrollForwardButton = null;
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
     */
    protected void installDefaults() {
        leadingTabIndex  = 0;
        trailingTabIndex = 0;

        updateStyle(tabPane);
    }

    /**
     * Update the Synth styles when something changes.
     *
     * @param c the component.
     */
    private void updateStyle(JTabbedPane c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        tabPlacement = tabPane.getTabPlacement();
        orientation  = ControlOrientation.getOrientation(tabPlacement == LEFT || tabPlacement == RIGHT ? VERTICAL : HORIZONTAL);

        closeButtonArmedIndex = -1;
        Object o = c.getClientProperty("JTabbedPane.closeButton");

        if (o != null && "left".equals(o)) {
            tabCloseButtonPlacement = LEFT;
        } else if (o != null && "right".equals(o)) {
            tabCloseButtonPlacement = RIGHT;
        } else {
            tabCloseButtonPlacement = CENTER;
        }

        closeButtonSize   = style.getInt(context, "closeButtonSize", 6);
        closeButtonInsets = (Insets) style.get(context, "closeButtonInsets");
        if (closeButtonInsets == null) {
            closeButtonInsets = new Insets(2, 2, 2, 2);
        }

        o = c.getClientProperty("JTabbedPane.closeListener");
        if (o != null && o instanceof SeaGlassTabCloseListener) {
            if (tabCloseListener == null) {
                tabCloseListener = (SeaGlassTabCloseListener) o;
            }
        }

        // Add properties other than JComponent colors, Borders and
        // opacity settings here:
        if (style != oldStyle) {

            tabRunOverlay        = 0;
            textIconGap          = style.getInt(context, "TabbedPane.textIconGap", 0);
            selectedTabPadInsets = (Insets) style.get(context, "TabbedPane.selectedTabPadInsets");

            if (selectedTabPadInsets == null) {
                selectedTabPadInsets = new Insets(0, 0, 0, 0);
            }

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }

        context.dispose();

        if (tabContext != null) {
            tabContext.dispose();
        }

        tabContext    = getContext(c, Region.TABBED_PANE_TAB, ENABLED);
        this.tabStyle = SeaGlassLookAndFeel.updateStyle(tabContext, this);
        tabInsets     = tabStyle.getInsets(tabContext, null);

        if (tabCloseContext != null) {
            tabCloseContext.dispose();
        }

        tabCloseContext    = getContext(c, SeaGlassRegion.TABBED_PANE_TAB_CLOSE_BUTTON, ENABLED);
        this.tabCloseStyle = SeaGlassLookAndFeel.updateStyle(tabCloseContext, this);

        if (tabAreaContext != null) {
            tabAreaContext.dispose();
        }

        tabAreaContext    = getContext(c, Region.TABBED_PANE_TAB_AREA, ENABLED);
        this.tabAreaStyle = SeaGlassLookAndFeel.updateStyle(tabAreaContext, this);
        tabAreaInsets     = tabAreaStyle.getInsets(tabAreaContext, null);

        if (tabContentContext != null) {
            tabContentContext.dispose();
        }

        tabContentContext    = getContext(c, Region.TABBED_PANE_CONTENT, ENABLED);
        this.tabContentStyle = SeaGlassLookAndFeel.updateStyle(tabContentContext, this);
        contentBorderInsets  = tabContentStyle.getInsets(tabContentContext, null);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installListeners()
     */
    protected void installListeners() {
        super.installListeners();
        tabPane.addMouseMotionListener((MouseAdapter) mouseListener);
        tabPane.addPropertyChangeListener(this);

        scrollBackwardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollBackward();
                }
            });

        scrollForwardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollForward();
                }
            });
    }

    /**
     * Scroll the tab buttons backwards.
     */
    protected void scrollBackward() {
        int selectedIndex = tabPane.getSelectedIndex();

        if (--selectedIndex < 0) {
            tabPane.setSelectedIndex(tabPane.getTabCount() == 0 ? -1 : 0);
        } else {
            tabPane.setSelectedIndex(selectedIndex);
        }

        tabPane.repaint();
    }

    /**
     * Scroll the tab buttons forwards.
     */
    protected void scrollForward() {
        int selectedIndex = tabPane.getSelectedIndex();

        if (++selectedIndex >= tabPane.getTabCount()) {
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } else {
            tabPane.setSelectedIndex(selectedIndex);
        }

        tabPane.repaint();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallListeners()
     */
    protected void uninstallListeners() {
        if (mouseListener != null) {
            tabPane.removeMouseMotionListener((MouseAdapter) mouseListener);
        }

        tabPane.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(tabPane, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        tabStyle.uninstallDefaults(tabContext);
        tabContext.dispose();
        tabContext = null;
        tabStyle   = null;

        tabAreaStyle.uninstallDefaults(tabAreaContext);
        tabAreaContext.dispose();
        tabAreaContext = null;
        tabAreaStyle   = null;

        tabContentStyle.uninstallDefaults(tabContentContext);
        tabContentContext.dispose();
        tabContentContext = null;
        tabContentStyle   = null;
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * Create a SynthContext for the component and state. Use the default
     * region.
     *
     * @param  c     the component.
     * @param  state the state.
     *
     * @return the newly created SynthContext.
     */
    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Create a SynthContext for the component and subregion. Use the current
     * state.
     *
     * @param  c         the component.
     * @param  subregion the subregion.
     *
     * @return the newly created SynthContext.
     */
    public SeaGlassContext getContext(JComponent c, Region subregion) {
        return getContext(c, subregion, getComponentState(c));
    }

    /**
     * Create a SynthContext for the component, subregion, and state.
     *
     * @param  c         the component.
     * @param  subregion the subregion.
     * @param  state     the state.
     *
     * @return the newly created SynthContext.
     */
    private SeaGlassContext getContext(JComponent c, Region subregion, int state) {
        SynthStyle style = null;
        Class      klass = SeaGlassContext.class;

        if (subregion == Region.TABBED_PANE_TAB) {
            style = tabStyle;
        } else if (subregion == Region.TABBED_PANE_TAB_AREA) {
            style = tabAreaStyle;
        } else if (subregion == Region.TABBED_PANE_CONTENT) {
            style = tabContentStyle;
        } else if (subregion == SeaGlassRegion.TABBED_PANE_TAB_CLOSE_BUTTON) {
            style = tabCloseStyle;
        }

        return SeaGlassContext.getContext(klass, c, subregion, style, state);
    }

    /**
     * Get the current state for the component.
     *
     * @param  c the component.
     *
     * @return the component's state.
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Get the state for the specified tab's close button.
     *
     * @param  c               the tabbed pane.
     * @param  tabIndex        the index of the tab.
     * @param  tabIsMousedOver TODO
     *
     * @return the close button state.
     */
    public int getCloseButtonState(JComponent c, int tabIndex, boolean tabIsMousedOver) {
        if (!c.isEnabled()) {
            return DISABLED;
        } else if (tabIndex == closeButtonArmedIndex) {
            return PRESSED;
        } else if (tabIndex == closeButtonHoverIndex) {
            return FOCUSED;
        } else if (tabIsMousedOver) {
            return MOUSE_OVER;
        }

        return ENABLED;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createScrollButton(int)
     */
    protected JButton createScrollButton(int direction) {
        SynthScrollableTabButton b = new SynthScrollableTabButton(direction);
        b.setName("TabbedPaneTabArea.button" + direction);
        return b;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle(tabPane);
        } else if (e.getPropertyName() == "tabPlacement") {
            updateStyle(tabPane);
        }
    }

    /**
     * Create the mouse listener.
     *
     * <p>Overridden to keep track of whether the selected tab is also
     * pressed.</p>
     *
     * @return the mouse listener.
     *
     * @see    javax.swing.plaf.basic.BasicTabbedPaneUI#createMouseListener()
     */
    @Override
    protected MouseListener createMouseListener() {
        return new SeaGlassTabbedPaneMouseHandler(super.createMouseListener());
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createLayoutManager()
     */
    @Override
    protected LayoutManager createLayoutManager() {
        return new SeaGlassTabbedPaneLayout();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabLabelShiftX(int, int,
     *      boolean)
     */
    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabLabelShiftY(int, int,
     *      boolean)
     */
    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTabbedPaneBackground(context, g, tabAreaRect.x, tabAreaRect.y, tabAreaRect.width, tabAreaRect.height);
        paint(context, g);
        context.dispose();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getBaseline(int)
     */
    protected int getBaseline(int tab) {
        if (tabPane.getTabComponentAt(tab) != null || getTextViewForTab(tab) != null) {
            return super.getBaseline(tab);
        }

        String      title   = tabPane.getTitleAt(tab);
        Font        font    = tabContext.getStyle().getFont(tabContext);
        FontMetrics metrics = getFontMetrics(font);
        Icon        icon    = getIconForTab(tab);

        textRect.setBounds(0, 0, 0, 0);
        iconRect.setBounds(0, 0, 0, 0);
        calcRect.setBounds(0, 0, Short.MAX_VALUE, maxTabHeight);
        tabContext.getStyle().getGraphicsUtils(tabContext).layoutText(tabContext, metrics, title, icon, SwingUtilities.CENTER,
                                                                      SwingUtilities.CENTER, SwingUtilities.LEADING,
                                                                      SwingUtilities.TRAILING, calcRect, iconRect, textRect, textIconGap);

        return textRect.y + metrics.getAscent() + getBaselineOffset();
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTabbedPaneBorder(context, g, x, y, w, h);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * Paint the tabbed pane.
     *
     * @param context the SynthContext describing the control.
     * @param g       the Graphics context to paint with.
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        int selectedIndex = tabPane.getSelectedIndex();

        ensureCurrentLayout();

        // Paint content border.
        paintContentBorder(tabContentContext, g, tabPlacement, selectedIndex);
        paintTabArea(g, tabPlacement, selectedIndex);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabArea(java.awt.Graphics,
     *      int, int)
     */
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        paintTabArea(tabAreaContext, g, tabPlacement, selectedIndex, tabAreaRect);
    }

    /**
     * Paint the tab area, including the tabs.
     *
     * @param ss            the SynthContext.
     * @param g             the Graphics context.
     * @param tabPlacement  the side the tabs are on.
     * @param selectedIndex the current selected tab index.
     * @param tabAreaBounds the bounds of the tab area.
     */
    protected void paintTabArea(SeaGlassContext ss, Graphics g, int tabPlacement, int selectedIndex, Rectangle tabAreaBounds) {
        Rectangle clipRect = g.getClipBounds();

        ss.setComponentState(SynthConstants.ENABLED);

        // Paint the tab area.
        SeaGlassLookAndFeel.updateSubregion(ss, g, tabAreaBounds);
        ss.getPainter().paintTabbedPaneTabAreaBackground(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width,
                                                         tabAreaBounds.height, tabPlacement);
        ss.getPainter().paintTabbedPaneTabAreaBorder(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width, tabAreaBounds.height,
                                                     tabPlacement);

        iconRect.setBounds(0, 0, 0, 0);
        textRect.setBounds(0, 0, 0, 0);

        if (runCount == 0) {
            return;
        }

        if (scrollBackwardButton.isVisible()) {
            paintScrollButtonBackground(ss, g, scrollBackwardButton);
        }

        if (scrollForwardButton.isVisible()) {
            paintScrollButtonBackground(ss, g, scrollForwardButton);
        }

        for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
            if (rects[i].intersects(clipRect) && selectedIndex != i) {
                paintTab(tabContext, g, rects, i, iconRect, textRect);
            }
        }

        if (selectedIndex >= 0) {
            if (rects[selectedIndex].intersects(clipRect)) {
                paintTab(tabContext, g, rects, selectedIndex, iconRect, textRect);
            }
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#setRolloverTab(int)
     */
    protected void setRolloverTab(int index) {
        int oldRolloverTab = getRolloverTab();

        super.setRolloverTab(index);

        Rectangle r = null;

        if ((oldRolloverTab >= 0) && (oldRolloverTab < tabPane.getTabCount())) {
            r = getTabBounds(tabPane, oldRolloverTab);

            if (r != null) {
                tabPane.repaint(r);
            }
        }

        if (index >= 0) {
            r = getTabBounds(tabPane, index);

            if (r != null) {
                tabPane.repaint(r);
            }
        }
    }

    /**
     * Paint a tab.
     *
     * @param ss       the SynthContext.
     * @param g        the Graphics context.
     * @param rects    the array containing the bounds for the tabs.
     * @param tabIndex the tab index to paint.
     * @param iconRect the bounds in which to paint the tab icon, if any.
     * @param textRect the bounds in which to paint the tab text, if any.
     */
    protected void paintTab(SeaGlassContext ss, Graphics g, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        Rectangle  tabRect       = rects[tabIndex];
        int        selectedIndex = tabPane.getSelectedIndex();
        boolean    isSelected    = selectedIndex == tabIndex;
        JComponent b             = ss.getComponent();

        boolean flipSegments    = (orientation == ControlOrientation.HORIZONTAL && !tabPane.getComponentOrientation().isLeftToRight());
        String  segmentPosition = "only";

        if (tabPane.getTabCount() > 1) {
            if (tabIndex == 0 && tabIndex == leadingTabIndex) {
                segmentPosition = flipSegments ? "last" : "first";
            } else if (tabIndex == tabPane.getTabCount() - 1 && tabIndex == trailingTabIndex) {
                segmentPosition = flipSegments ? "first" : "last";
            } else {
                segmentPosition = "middle";
            }
        }

        b.putClientProperty("JTabbedPane.Tab.segmentPosition", segmentPosition);
        updateTabContext(tabIndex, isSelected, isSelected && selectedTabIsPressed, getRolloverTab() == tabIndex,
                         getFocusIndex() == tabIndex);

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabRect);

        int x      = tabRect.x;
        int y      = tabRect.y;
        int height = tabRect.height;
        int width  = tabRect.width;

        tabContext.getPainter().paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, tabIndex, tabPlacement);
        tabContext.getPainter().paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, tabIndex, tabPlacement);

        if (tabCloseButtonPlacement != CENTER) {
            tabRect = paintCloseButton(g, tabContext, tabIndex);
        }

        if (tabPane.getTabComponentAt(tabIndex) == null) {
            String      title   = tabPane.getTitleAt(tabIndex);
            Font        font    = ss.getStyle().getFont(ss);
            FontMetrics metrics = SwingUtilities2.getFontMetrics(tabPane, g, font);
            Icon        icon    = getIconForTab(tabIndex);

            layoutLabel(ss, tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);
            paintText(ss, g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
            paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);
        }
    }

    /**
     * Paint the close button for a tab.
     *
     * @param  g          the Graphics context.
     * @param  tabContext the SynthContext for the tab itself.
     * @param  tabIndex   the tab index to paint.
     *
     * @return the new tab bounds.
     */
    protected Rectangle paintCloseButton(Graphics g, SynthContext tabContext, int tabIndex) {
        Rectangle tabRect = new Rectangle(rects[tabIndex]);

        Rectangle bounds = getCloseButtonBounds(tabIndex);
        int       offset = bounds.width + textIconGap;
        boolean   onLeft = isCloseButtonOnLeft();

        if (onLeft) {
            tabRect.x     += offset;
            tabRect.width -= offset;
        } else {
            tabRect.width -= offset;
        }

        SeaGlassContext subcontext = getContext(tabPane, SeaGlassRegion.TABBED_PANE_TAB_CLOSE_BUTTON,
                                                getCloseButtonState(tabPane, tabIndex, (tabContext.getComponentState() & MOUSE_OVER) != 0));

        SeaGlassLookAndFeel.updateSubregion(subcontext, g, bounds);

        SeaGlassSynthPainterImpl painter = (SeaGlassSynthPainterImpl) subcontext.getPainter();

        painter.paintSearchButtonForeground(subcontext, g, bounds.x, bounds.y, bounds.width, bounds.height);

        subcontext.dispose();

        return tabRect;
    }

    /**
     * Paint the background for a tab scroll button.
     *
     * @param ss           the tab subregion SynthContext.
     * @param g            the Graphics context.
     * @param scrollButton the button to paint.
     */
    protected void paintScrollButtonBackground(SeaGlassContext ss, Graphics g, JButton scrollButton) {
        Rectangle tabRect = scrollButton.getBounds();
        int       x       = tabRect.x;
        int       y       = tabRect.y;
        int       height  = tabRect.height;
        int       width   = tabRect.width;

        boolean flipSegments = (orientation == ControlOrientation.HORIZONTAL && !tabPane.getComponentOrientation().isLeftToRight());

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabRect);

        tabPane.putClientProperty("JTabbedPane.Tab.segmentPosition",
                                  ((scrollButton == scrollBackwardButton) ^ flipSegments) ? "first" : "last");

        int         oldState    = tabContext.getComponentState();
        ButtonModel model       = scrollButton.getModel();
        int         isPressed   = model.isPressed() && model.isArmed() ? PRESSED : 0;
        int         buttonState = SeaGlassLookAndFeel.getComponentState(scrollButton) | isPressed;

        tabContext.setComponentState(buttonState);
        tabContext.getPainter().paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, -1, tabPlacement);
        tabContext.getPainter().paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, -1, tabPlacement);
        tabContext.setComponentState(oldState);
    }

    /**
     * Layout label text for a tab.
     *
     * @param ss           the SynthContext.
     * @param tabPlacement the side the tabs are on.
     * @param metrics      the font metrics.
     * @param tabIndex     the index of the tab to lay out.
     * @param title        the text for the label, if any.
     * @param icon         the icon for the label, if any.
     * @param tabRect      Rectangle to layout text and icon in.
     * @param iconRect     Rectangle to place icon bounds in
     * @param textRect     Rectangle to place text in
     * @param isSelected   is the tab selected?
     */
    protected void layoutLabel(SeaGlassContext ss, int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon,
            Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        View v = getTextViewForTab(tabIndex);

        if (v != null) {
            tabPane.putClientProperty("html", v);
        }

        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        ss.getStyle().getGraphicsUtils(ss).layoutText(ss, metrics, title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER,
                                                      SwingUtilities.LEADING, SwingUtilities.TRAILING, tabRect, iconRect, textRect,
                                                      textIconGap);

        tabPane.putClientProperty("html", null);

        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);

        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    /**
     * Paint the label text for a tab.
     *
     * @param ss           the SynthContext.
     * @param g            the Graphics context.
     * @param tabPlacement the side the tabs are on.
     * @param font         the font to use.
     * @param metrics      the font metrics.
     * @param tabIndex     the index of the tab to lay out.
     * @param title        the text for the label, if any.
     * @param textRect     Rectangle to place text in
     * @param isSelected   is the tab selected?
     */
    protected void paintText(SeaGlassContext ss, Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title,
            Rectangle textRect, boolean isSelected) {
        g.setFont(font);

        View v = getTextViewForTab(tabIndex);

        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            FontMetrics    fm = SwingUtilities2.getFontMetrics(tabPane, g);
            title = SwingUtilities2.clipStringIfNecessary(tabPane, fm, title, textRect.width);
            g.setColor(ss.getStyle().getColor(ss, ColorType.TEXT_FOREGROUND));
            ss.getStyle().getGraphicsUtils(ss).paintText(ss, g, title, textRect, mnemIndex);
        }
    }

    /**
     * Paint the content pane's border.
     *
     * @param ss            the SynthContext.
     * @param g             the Graphics context.
     * @param tabPlacement  the side the tabs are on.
     * @param selectedIndex the current selected tab index.
     */
    protected void paintContentBorder(SeaGlassContext ss, Graphics g, int tabPlacement, int selectedIndex) {
        int    width  = tabPane.getWidth();
        int    height = tabPane.getHeight();
        Insets insets = tabPane.getInsets();

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

        switch (tabPlacement) {

        case LEFT:
            x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
            w -= (x - insets.left);
            break;

        case RIGHT:
            w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
            break;

        case BOTTOM:
            h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            break;

        case TOP:
        default:
            y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            h -= (y - insets.top);
        }

        SeaGlassLookAndFeel.updateSubregion(ss, g, new Rectangle(x, y, w, h));
        ss.getPainter().paintTabbedPaneContentBackground(ss, g, x, y, w, h);
        ss.getPainter().paintTabbedPaneContentBorder(ss, g, x, y, w, h);
    }

    /**
     * Make sure we have laid out the pane with the current layout.
     */
    private void ensureCurrentLayout() {
        if (!tabPane.isValid()) {
            tabPane.validate();
        }

        /*
         * If tabPane doesn't have a peer yet, the validate() call will silently
         * fail. We handle that by forcing a layout if tabPane is still invalid.
         * See bug 4237677.
         */
        if (!tabPane.isValid()) {
            TabbedPaneLayout layout = (TabbedPaneLayout) tabPane.getLayout();

            layout.calculateLayoutInfo();
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateMaxTabHeight(int)
     */
    public int calculateMaxTabHeight(int tabPlacement) {
        FontMetrics metrics    = getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int         tabCount   = tabPane.getTabCount();
        int         result     = 0;
        int         fontHeight = metrics.getHeight();

        for (int i = 0; i < tabCount; i++) {
            result = Math.max(calculateTabHeight(tabPlacement, i, fontHeight), result);
        }

        return result;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabWidth(int, int,
     *      java.awt.FontMetrics)
     */
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Icon      icon         = getIconForTab(tabIndex);
        Insets    tabInsets    = getTabInsets(tabPlacement, tabIndex);
        int       width        = tabInsets.left + tabInsets.right + 3;
        Component tabComponent = tabPane.getTabComponentAt(tabIndex);

        if (tabComponent != null) {
            width += tabComponent.getPreferredSize().width;
            if (tabIndex < rects.length && tabCloseButtonPlacement != CENTER) {
                width += getCloseButtonBounds(tabIndex).width + textIconGap;
            }
        } else {
            if (icon != null) {
                width += icon.getIconWidth() + textIconGap;
            }

            // Hack to prevent array index out of bounds before the size has been set and the rectangles created.
            if (tabIndex < rects.length && tabCloseButtonPlacement != CENTER) {
                width += getCloseButtonBounds(tabIndex).width + textIconGap;
            }

            View v = getTextViewForTab(tabIndex);

            if (v != null) {
                // html
                width += (int) v.getPreferredSpan(View.X_AXIS);
            } else {
                // plain text
                String title = tabPane.getTitleAt(tabIndex);

                width += tabContext.getStyle().getGraphicsUtils(tabContext).computeStringWidth(tabContext, metrics.getFont(), metrics,
                                                                                               title);
            }
        }

        return width;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateMaxTabWidth(int)
     */
    public int calculateMaxTabWidth(int tabPlacement) {
        FontMetrics metrics  = getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int         tabCount = tabPane.getTabCount();
        int         result   = 0;

        for (int i = 0; i < tabCount; i++) {
            result = Math.max(calculateTabWidth(tabPlacement, i, metrics), result);
        }

        return result;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabInsets(int, int)
     */
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        updateTabContext(tabIndex, false, false, false, (getFocusIndex() == tabIndex));

        return tabInsets;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getFontMetrics()
     */
    protected FontMetrics getFontMetrics() {
        return getFontMetrics(tabContext.getStyle().getFont(tabContext));
    }

    /**
     * Get the font metrics for the font.
     *
     * @param  font the font.
     *
     * @return the metrics for the font.
     */
    protected FontMetrics getFontMetrics(Font font) {
        return tabPane.getFontMetrics(font);
    }

    /**
     * Update the SynthContext for the tab area for a specified tab.
     *
     * @param index       the tab to update for.
     * @param selected    is the tab selected?
     * @param isMouseDown is the mouse down?
     * @param isMouseOver is the mouse over the tab?
     * @param hasFocus    do we have focus?
     */
    private void updateTabContext(int index, boolean selected, boolean isMouseDown, boolean isMouseOver, boolean hasFocus) {
        int state = 0;

        if (!tabPane.isEnabled() || !tabPane.isEnabledAt(index)) {
            state |= SynthConstants.DISABLED;

            if (selected) {
                state |= SynthConstants.SELECTED;
            }
        } else if (selected) {
            state |= (SynthConstants.ENABLED | SynthConstants.SELECTED);

            if (isMouseOver && UIManager.getBoolean("TabbedPane.isTabRollover")) {
                state |= SynthConstants.MOUSE_OVER;
            }
        } else if (isMouseOver) {
            state |= (SynthConstants.ENABLED | SynthConstants.MOUSE_OVER);
        } else {
            state =  SeaGlassLookAndFeel.getComponentState(tabPane);
            state &= ~SynthConstants.FOCUSED; // Don't use tabbedpane focus state.
        }

        if (hasFocus && tabPane.hasFocus()) {
            state |= SynthConstants.FOCUSED; // individual tab has focus
        }

        if (isMouseDown) {
            state |= SynthConstants.PRESSED;
        }

        tabContext.setComponentState(state);
    }

    /**
     * Determine whether the mouse is over a tab close button, and if so, set
     * the hover index.
     *
     * @param  x the current mouse x coordinate.
     * @param  y the current mouse y coordinate.
     *
     * @return {@code true} if the mouse is over a close button, {@code false}
     *         otherwise.
     */
    protected boolean isOverCloseButton(int x, int y) {
        if (tabCloseButtonPlacement != CENTER) {
            int tabCount = tabPane.getTabCount();

            for (int i = 0; i < tabCount; i++) {
                if (getCloseButtonBounds(i).contains(x, y)) {
                    closeButtonHoverIndex = i;
                    return true;
                }
            }
        }

        closeButtonHoverIndex = -1;
        return false;
    }

    /**
     * Get the bounds for a tab close button.
     *
     * @param  tabIndex the tab index.
     *
     * @return the bounds.
     */
    protected Rectangle getCloseButtonBounds(int tabIndex) {
        Rectangle bounds = new Rectangle(rects[tabIndex]);

        bounds.width  = closeButtonSize;
        bounds.height = closeButtonSize;

        bounds.y += (rects[tabIndex].height - closeButtonSize - closeButtonInsets.top - closeButtonInsets.bottom) / 2
            + closeButtonInsets.top;

        boolean onLeft = isCloseButtonOnLeft();

        if (onLeft) {
            int offset = orientation == ControlOrientation.VERTICAL || tabIndex == 0 ? 6 : 3;

            bounds.x += offset;
        } else {
            int offset = orientation == ControlOrientation.VERTICAL || tabIndex == tabPane.getTabCount() - 1 ? 6 : 4;

            bounds.x += rects[tabIndex].width - bounds.width - offset;
        }

        return bounds;
    }

    /**
     * Return {@code true} if the tab close button should be placed on the left,
     * {@code false} otherwise.
     *
     * @return {@code true} if the tab close button should be placed on the
     *         left, {@code false} otherwise.
     */
    private boolean isCloseButtonOnLeft() {
        return (tabCloseButtonPlacement == LEFT) == tabPane.getComponentOrientation().isLeftToRight();
    }

    /**
     * Called when a close tab button is pressed.
     *
     * @param tabIndex TODO
     */
    protected void doClose(int tabIndex) {
        if (tabCloseListener == null || tabCloseListener.tabAboutToBeClosed(tabIndex)) {
            String    title     = tabPane.getTitleAt(tabIndex);
            Component component = tabPane.getComponentAt(tabIndex);

            tabPane.removeTabAt(tabIndex);

            if (tabCloseListener != null) {
                tabCloseListener.tabClosed(title, component);
            }
        }
    }

    /**
     * The scrollable tab button.
     */
    private class SynthScrollableTabButton extends SeaGlassArrowButton implements UIResource {
        private static final long serialVersionUID = -3983149584304630486L;

        /**
         * Creates a new SynthScrollableTabButton object.
         *
         * @param direction the arrow direction.
         */
        public SynthScrollableTabButton(int direction) {
            super(direction);
            putClientProperty("__arrow_scale__", 0.6);
        }

        /**
         * @see javax.swing.JComponent#getPreferredSize()
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, 20);
        }
    }

    /**
     * The layout manager.
     */
    protected class SeaGlassTabbedPaneLayout extends TabbedPaneLayout {

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#preferredTabAreaHeight(int,
         *      int)
         */
        protected int preferredTabAreaHeight(int tabPlacement, int width) {
            return calculateMaxTabHeight(tabPlacement);
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#preferredTabAreaWidth(int,
         *      int)
         */
        protected int preferredTabAreaWidth(int tabPlacement, int height) {
            return calculateMaxTabWidth(tabPlacement);
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container parent) {
            setRolloverTab(-1);

            setScrollButtonDirections();
            calculateLayoutInfo();

            boolean shouldChangeFocus = verifyFocus(tabPane.getSelectedIndex());

            if (tabPane.getTabCount() <= 0) {
                return;
            }

            calcContentRect();
            
            for (int i = 0; i < tabPane.getComponentCount(); i++) {
                Component child = tabPane.getComponent(i);
                // Ignore the scroll buttons. They have already been positioned in
                // calculateTabRects, which will have been called by calculateLayoutInfo,
                // which is called above.
                if (child != scrollBackwardButton && child != scrollForwardButton) {
                    child.setBounds(contentRect);
                }
            }
            setTabContainerBounds();
            layoutTabComponents();

            if (shouldChangeFocus && !SwingUtilities2.tabbedPaneChangeFocusTo(getVisibleComponent())) {
                tabPane.requestFocus();
            }
        }
        
        /**
         * Check if we have a custom tab component container.
         * @return the container used to hold custom tab components.
         */
        private Component getTabContainer() {
            int tabCount = tabPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                Component tabComponent = tabPane.getTabComponentAt(i);
                if (tabComponent != null) {
                    return tabComponent.getParent();
                }
            }
            return null;
        }

        /**
         * Set the position of the tab container that is used to layout custom tab components.
         */
        private void setTabContainerBounds() {
            Component tabContainer = getTabContainer();
            if (tabContainer != null) {
                int tabContainerX = tabPlacement == RIGHT ? contentRect.width : 0; 
                int tabContainerY =  tabPlacement == BOTTOM ? contentRect.height : 0;
                int tabContainerWidth = tabPlacement == LEFT || tabPlacement == RIGHT ? tabPane.getWidth() - contentRect.width : contentRect.width;
                int tabContainerHeight = tabPlacement == TOP || tabPlacement == BOTTOM ? tabPane.getHeight() - contentRect.height : contentRect.height;
                tabContainer.setBounds(tabContainerX, tabContainerY, tabContainerWidth, tabContainerHeight);
            }
        }

        /**
         * Set the directions of the arrows in the scroll buttons if necessary.
         */
        private void setScrollButtonDirections() {
            if (tabPlacement == LEFT || tabPlacement == RIGHT) {
                if (scrollForwardButton.getDirection() != SOUTH) {
                    scrollForwardButton.setDirection(SOUTH);
                }

                if (scrollBackwardButton.getDirection() != NORTH) {
                    scrollBackwardButton.setDirection(NORTH);
                }
            } else if (tabPane.getComponentOrientation().isLeftToRight()) {
                if (scrollForwardButton.getDirection() != EAST) {
                    scrollForwardButton.setDirection(EAST);
                }

                if (scrollBackwardButton.getDirection() != WEST) {
                    scrollBackwardButton.setDirection(WEST);
                }
            } else {
                if (scrollForwardButton.getDirection() != WEST) {
                    scrollForwardButton.setDirection(WEST);
                }

                if (scrollBackwardButton.getDirection() != EAST) {
                    scrollBackwardButton.setDirection(EAST);
                }
            }
        }

        /**
         * Verify that the currently focused element exists. Reset the focus to
         * none if it doesn't. Return whether focus needs to be changed.
         *
         * @param  selectedIndex the current selected index.
         *
         * @return {@code true} if the focus needs to be changed, {@code false}
         *         otherwise.
         */
        private boolean verifyFocus(int selectedIndex) {
            Component visibleComponent  = getVisibleComponent();
            Component selectedComponent = null;

            if (selectedIndex < 0) {
                if (visibleComponent != null) {
                    // The last tab was removed, so remove the component.
                    setVisibleComponent(null);
                }
            } else {
                selectedComponent = tabPane.getComponentAt(selectedIndex);
            }

            if (tabPane.getTabCount() == 0) {
                return false;
            }

            boolean shouldChangeFocus = false;

            // In order to allow programs to use a single component
            // as the display for multiple tabs, we will not change
            // the visible compnent if the currently selected tab
            // has a null component.  This is a bit dicey, as we don't
            // explicitly state we support this in the spec, but since
            // programs are now depending on this, we're making it work.
            if (selectedComponent != null) {
                if (selectedComponent != visibleComponent && visibleComponent != null) {
                    if (findFocusOwner(visibleComponent) != null) {
                        shouldChangeFocus = true;
                    }
                }

                setVisibleComponent(selectedComponent);
            }

            return shouldChangeFocus;
        }

        /**
         * Calculate the bounds Rectangle for the content panes.
         */
        private void calcContentRect() {
            Insets    contentInsets = getContentBorderInsets(tabPlacement);
            Rectangle bounds        = tabPane.getBounds();
            Insets    insets        = tabPane.getInsets();

            int cx;
            int cy;
            int cw;
            int ch;

            switch (tabPlacement) {

            case LEFT:
                cx = tabAreaRect.x + tabAreaRect.width + contentInsets.left;
                cy = tabAreaRect.y + contentInsets.top;
                cw = bounds.width - insets.left - insets.right - tabAreaRect.width - contentInsets.left - contentInsets.right;
                ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                break;

            case RIGHT:
                cx = insets.left + contentInsets.left;
                cy = insets.top + contentInsets.top;
                cw = bounds.width - insets.left - insets.right - tabAreaRect.width - contentInsets.left - contentInsets.right;
                ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                break;

            case BOTTOM:
                cx = insets.left + contentInsets.left;
                cy = insets.top + contentInsets.top;
                cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                ch = bounds.height - insets.top - insets.bottom - tabAreaRect.height - contentInsets.top - contentInsets.bottom;
                break;

            case TOP:
            default:
                cx = tabAreaRect.x + contentInsets.left;
                cy = tabAreaRect.y + tabAreaRect.height + contentInsets.top;
                cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                ch = bounds.height - insets.top - insets.bottom - tabAreaRect.height - contentInsets.top - contentInsets.bottom;
            }

            contentRect.setBounds(cx, cy, cw, ch);
        }

        /**
         * Layout the tab components.
         */
        private void layoutTabComponents() {
            Rectangle rect = new Rectangle();

            for (int i = 0; i < tabPane.getTabCount(); i++) {
                Component c = tabPane.getTabComponentAt(i);

                if (c == null) {
                    continue;
                }

                getTabBounds(i, rect);

                Dimension preferredSize = c.getPreferredSize();
                Insets    insets        = getTabInsets(tabPlacement, i);
                int       outerX        = (rect.x-c.getParent().getX()) + insets.left;
                int       outerY        = (rect.y-c.getParent().getY()) + insets.top;
                int       outerWidth    = rect.width - insets.left - insets.right;
                int       outerHeight   = rect.height - insets.top - insets.bottom;

                // centralize component
                // TODO rossi 30.06.2011 this may need adjustment to respect the optional close buttons
                int       x             = outerX + (outerWidth - preferredSize.width) / 2;
                int       y             = outerY + (outerHeight - preferredSize.height) / 2;
                boolean   isSelected    = i == tabPane.getSelectedIndex();

                c.setBounds(x + getTabLabelShiftX(tabPlacement, i, isSelected),
                            y + getTabLabelShiftY(tabPlacement, i, isSelected),
                            preferredSize.width, preferredSize.height);
            }
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#calculateTabRects(int,
         *      int)
         */
        protected void calculateTabRects(int tabPlacement, int tabCount) {
            if (orientation == ControlOrientation.HORIZONTAL) {
                maxTabHeight = calculateMaxTabHeight(tabPlacement);
            } else {
                maxTabWidth = calculateMaxTabWidth(tabPlacement);
            }

            // Calculate the tab area itself.
            calcTabAreaRect();

            if (tabCount == 0) {
                scrollBackwardButton.setVisible(false);
                scrollForwardButton.setVisible(false);
                runCount    = 0;
                selectedRun = -1;
                return;
            }

            selectedRun = 0;
            runCount    = 1;

            int selectedIndex = tabPane.getSelectedIndex();

            if (leadingTabIndex > selectedIndex) {
                leadingTabIndex = selectedIndex;
            }

            Insets    tabAreaInsets = getTabAreaInsets(tabPlacement);
            Dimension size          = new Dimension(tabAreaRect.width - tabAreaInsets.left - tabAreaInsets.right,
                                                    tabAreaRect.height - tabAreaInsets.top - tabAreaInsets.bottom);
            int       tabAreaLength = orientation.getLength(size);
            int       buttonLength  = orientation.getLength(scrollForwardButton.getPreferredSize());

            determineVisibleTabIndices(tabCount, selectedIndex, tabAreaLength, buttonLength);
            resetTabPositionsToLeadingTabIndex(tabCount);
            int totalLength = orientation.getPosition(rects[trailingTabIndex].x + rects[trailingTabIndex].width,
                                                      rects[trailingTabIndex].y + rects[trailingTabIndex].height);

            if (leadingTabIndex > 0 || trailingTabIndex < tabCount - 1) {
                resizeTabs(tabCount, totalLength, buttonLength, tabAreaLength);
            } else {
                centerTabs(tabCount, totalLength, tabAreaLength);
            }

            // Set the positions and visibility of the scroll buttons.
            setScrollButtonPositions(scrollBackwardButton, (leadingTabIndex > 0),
                                     orientation.getPosition(rects[leadingTabIndex]) - buttonLength);
            setScrollButtonPositions(scrollForwardButton, (trailingTabIndex < tabCount - 1),
                                     orientation.getPosition(rects[trailingTabIndex]) + orientation.getLength(rects[trailingTabIndex]));

            // If component orientation right to left and tab placement is on the top or the bottom,
            // flip x positions and adjust by widths.
            if (!tabPane.getComponentOrientation().isLeftToRight() && orientation == ControlOrientation.HORIZONTAL) {
                flipRightToLeft(tabCount, tabPane.getSize());
            }
        }

        /**
         * Calculate the rectangle into which the tabs will be drawn. This does
         * not include the tab area insets, but does include the tab pane
         * insets.
         *
         * <p>This is used for painting the background as well as for laying out
         * the tabs.</p>
         */
        private void calcTabAreaRect() {
            Insets    insets        = tabPane.getInsets();
            Insets    tabAreaInsets = getTabAreaInsets(tabPlacement);
            Rectangle bounds        = tabPane.getBounds();

            if (tabPane.getTabCount() == 0) {
                tabAreaRect.setBounds(0, 0, 0, 0);
                return;
            }

            // Calculate bounds within which a tab run must fit.
            int position;
            int offset;
            int length;
            int thickness;

            if (orientation == ControlOrientation.HORIZONTAL) {
                length    = bounds.width - insets.left - insets.right;
                position  = insets.left;
                thickness = maxTabHeight + tabAreaInsets.top + tabAreaInsets.bottom;
                offset    = (tabPlacement == BOTTOM) ? bounds.height - insets.bottom - thickness : insets.top;
            } else {
                length    = bounds.height - insets.top - insets.bottom;
                position  = insets.top;
                thickness = maxTabWidth + tabAreaInsets.left + tabAreaInsets.right;
                offset    = (tabPlacement == RIGHT) ? bounds.width - insets.right - thickness : insets.left;
            }

            tabAreaRect.setBounds(orientation.createBounds(position, offset, length, thickness));
        }

        /**
         * Calculate the leading and trailing tab indices that will fit in the
         * length, keeping the selected index visible.
         *
         * @param tabCount      the number of tabs.
         * @param selectedIndex the current tab.
         * @param tabAreaLength the length of the tab area. This takes the tab
         *                      area insets into account.
         * @param buttonLength  the length of a scroll button. They are both the
         *                      same length.
         */
        private void determineVisibleTabIndices(int tabCount, int selectedIndex, int tabAreaLength, int buttonLength) {
            int desiredMaximumLength = calcDesiredMaximumLength(tabCount);
            int leadingTabOffset     = orientation.getPosition(rects[leadingTabIndex]);
            int selectedTabEndOffset = orientation.getPosition(rects[selectedIndex].x + rects[selectedIndex].width,
                                                               rects[selectedIndex].y + rects[selectedIndex].height);

            if (desiredMaximumLength <= tabAreaLength) {
                // Fits with no scroll buttons.
                leadingTabIndex  = 0;
                trailingTabIndex = tabCount - 1;
            } else if (desiredMaximumLength - leadingTabOffset + buttonLength <= tabAreaLength) {
                // Fits from current leading tab index, with scroll backward button. Leave leadingTabIndex alone.
                trailingTabIndex = tabCount - 1;
            } else if ((leadingTabIndex == 0 && selectedTabEndOffset - leadingTabOffset + buttonLength <= tabAreaLength)
                    || (selectedTabEndOffset - leadingTabOffset + 2 * buttonLength <= tabAreaLength)) {
                // Selected index fits with current leading tab index and one or two scroll buttons. Leave leadingTabIndex alone.
                trailingTabIndex = -1;

                for (int i = tabCount - 1; i > selectedIndex; i--) {
                    int end = orientation.getPosition(rects[i].x + rects[i].width, rects[i].y + rects[i].height);

                    if (end - leadingTabOffset + 2 * buttonLength <= tabAreaLength) {
                        trailingTabIndex = i;
                        break;
                    }
                }

                if (trailingTabIndex == -1) {
                    trailingTabIndex = selectedIndex;
                }
            } else {
                // Selected index does not fit with current leading index and two scroll buttons.
                // Make selected index the trailing index and find the leading index that will fit.
                trailingTabIndex = selectedIndex;
                leadingTabIndex  = -1;

                for (int i = 0; i < selectedIndex; i++) {
                    int start = orientation.getPosition(rects[i]);

                    if (selectedTabEndOffset - start + 2 * buttonLength <= tabAreaLength) {
                        leadingTabIndex = i;
                        break;
                    }
                }

                if (leadingTabIndex == -1) {
                    leadingTabIndex = selectedIndex;
                }
            }

            tabRuns[0] = leadingTabIndex;
        }

        /**
         * Run through tabs and lay them all out in a single run, assigning
         * maxTabWidth and maxTabHeight. The offset and thickness are set to
         * zero in this method. They will be assigned good values later.
         *
         * @param  tabCount the number of tabs.
         *
         * @return the maximum width, if tabs run horizontall, otherwise the
         *         maximum height.
         */
        private int calcDesiredMaximumLength(int tabCount) {
            FontMetrics metrics       = getFontMetrics();
            int         fontHeight    = metrics.getHeight();
            Insets      tabAreaInsets = getTabAreaInsets(fontHeight);
            Point       corner        = new Point(tabAreaRect.x + tabAreaInsets.left,
                                                  tabAreaRect.y + tabAreaInsets.top);
            int         offset        = orientation.getOrthogonalOffset(corner);
            int         thickness     = (orientation == ControlOrientation.HORIZONTAL) ? maxTabWidth : maxTabHeight;
            int         position      = 0;
            int         maxTabLength  = 0;

            // Run through tabs and lay them out in a single long run.
            for (int i = 0; i < tabCount; i++) {
                int length = (orientation == ControlOrientation.HORIZONTAL) ? calculateTabWidth(TOP, i, metrics)
                                                                            : calculateTabHeight(LEFT, i, fontHeight);

                rects[i].setBounds(orientation.createBounds(position, offset, length, thickness));

                // Update the maximum length and the next tab position.
                maxTabLength =  Math.max(maxTabLength, length);
                position     += length;
            }

            // Update the BasicTabbedPaneUI length variable.
            if (orientation == ControlOrientation.HORIZONTAL) {
                maxTabWidth = maxTabLength;
            } else {
                maxTabHeight = maxTabLength;
            }

            return position;
        }

        /**
         * Reset the positions of the tabs between leadingTabIndex and
         * trailingTabIndex, inclusive, such that the leadingTabIndex is at
         * position zero.
         *
         * @param tabCount the number of tabs.
         */
        private void resetTabPositionsToLeadingTabIndex(int tabCount) {
            // Rebalance the layout such that the leading tab is at position 0.
            int leadingTabPosition = orientation.getPosition(rects[leadingTabIndex]);

            for (int i = 0; i < tabCount; i++) {
                if (i < leadingTabIndex || i > trailingTabIndex) {
                    rects[i].setBounds(-1, -1, 0, 0);
                } else {
                    orientation.updateBoundsPosition(rects[i], orientation.getPosition(rects[i]) - leadingTabPosition);
                }
            }
        }

        /**
         * Center the tabs in the tab area.
         *
         * @param tabCount      the number of tabs.
         * @param totalLength   the total length available of the tabs.
         * @param tabAreaLength the total length available.
         */
        private void centerTabs(int tabCount, int totalLength, int tabAreaLength) {
            Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
            Point  corner        = new Point(tabAreaRect.x + tabAreaInsets.left, tabAreaRect.y + tabAreaInsets.top);
            int    startPosition = orientation.getPosition(corner);
            int    offset        = orientation.getOrthogonalOffset(corner);
            int    thickness     = (orientation == ControlOrientation.HORIZONTAL) ? maxTabHeight : maxTabWidth;
            int    delta         = -(tabAreaLength - totalLength) / 2 - startPosition;

            for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
                int position = orientation.getPosition(rects[i]) - delta;
                int length   = orientation.getLength(rects[i]);

                rects[i].setBounds(orientation.createBounds(position, offset, length, thickness));
            }
        }

        /**
         * Fill out the visible tabs and scroll buttons to fit the available
         * length.
         *
         * @param tabCount      the number of tabs.
         * @param totalLength   the total length available of the tabs.
         * @param buttonLength  the size of a scroll button.
         * @param tabAreaLength the total length available.
         */
        private void resizeTabs(int tabCount, int totalLength, int buttonLength, int tabAreaLength) {
            // Subtract off the button length from the available length.
            if (leadingTabIndex > 0) {
                tabAreaLength -= buttonLength;
            }

            if (trailingTabIndex < tabCount - 1) {
                tabAreaLength -= buttonLength;
            }

            Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
            Point  corner        = new Point(tabAreaRect.x + tabAreaInsets.left, tabAreaRect.y + tabAreaInsets.top);
            int    startPosition = orientation.getPosition(corner);
            int    offset        = orientation.getOrthogonalOffset(corner);
            int    thickness     = (orientation == ControlOrientation.HORIZONTAL) ? maxTabHeight : maxTabWidth;

            // Fill the tabs to the available width.
            float  multiplier    = ((float) tabAreaLength / totalLength);

            for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
                int position = (i == leadingTabIndex) ? startPosition + (leadingTabIndex > 0 ? buttonLength : 0)
                                                      : orientation.getPosition(rects[i - 1]) + orientation.getLength(rects[i - 1]);
                int length   = (int) (orientation.getLength(rects[i]) * multiplier);

                rects[i].setBounds(orientation.createBounds(position, offset, length, thickness));
            }
        }

        /**
         * Set the bounds Rectangle for a scroll button.
         *
         * @param child    the scroll button.
         * @param visible  whether the button is visible or not.
         * @param position the position from the start of the tab run.
         */
        private void setScrollButtonPositions(Component child, boolean visible, int position) {
            if (visible) {
                child.setBounds(orientation.createBounds(position,
                                                         orientation.getOrthogonalOffset(rects[leadingTabIndex]),
                                                         orientation.getLength(child.getPreferredSize()),
                                                         orientation.getThickness(rects[leadingTabIndex])));
            }

            child.setEnabled(tabPane.isEnabled());
            child.setVisible(visible);
        }

        /**
         * Flip the buttons right to left.
         *
         * @param tabCount the number of tabs.
         * @param size     the rectangle to fit them in.
         */
        private void flipRightToLeft(int tabCount, Dimension size) {
            int rightMargin = size.width;

            for (int i = 0; i < tabCount; i++) {
                rects[i].x = rightMargin - rects[i].x - rects[i].width;
            }

            if (scrollBackwardButton.isVisible()) {
                Rectangle b = scrollBackwardButton.getBounds();

                scrollBackwardButton.setLocation(rightMargin - b.x - b.width, b.y);
            }

            if (scrollForwardButton.isVisible()) {
                Rectangle b = scrollForwardButton.getBounds();

                scrollForwardButton.setLocation(rightMargin - b.x - b.width, b.y);
            }
        }

        /**
         * Find the focus owner of the component.
         *
         * @param  visibleComponent the component.
         *
         * @return the focus owner of the component.
         */
        private Component findFocusOwner(Component visibleComponent) {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

            // Verify that focusOwner is a descendant of visibleComponent.
            for (Component temp = focusOwner; temp != null; temp = (temp instanceof Window) ? null : temp.getParent()) {
                if (temp == visibleComponent) {
                    return focusOwner;
                }
            }

            return null;
        }
    }

    /**
     * Our Mouse Handler. Delegates to the basic UI's mouse handler if we don't
     * need to handle an event.
     */
    public class SeaGlassTabbedPaneMouseHandler extends MouseAdapter {

        /** Current mouse x coordinate. */
        protected transient int currentMouseX;

        /** Current mouse y coordinate. */
        protected transient int currentMouseY;

        private MouseListener       delegate;
        private MouseMotionListener delegate2;

        /**
         * Creates a new SeaGlassTabbedPaneMouseHandler object.
         *
         * @param originalMouseListener the original mouse handler.
         */
        public SeaGlassTabbedPaneMouseHandler(MouseListener originalMouseListener) {
            delegate  = originalMouseListener;
            delegate2 = (MouseMotionListener) originalMouseListener;

            closeButtonHoverIndex = -1;
            closeButtonArmedIndex = -1;
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent e) {
            delegate.mouseClicked(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
            delegate.mouseEntered(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent e) {
            delegate.mouseExited(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {
            int oldHoverIndex = closeButtonHoverIndex;

            // Test for mouse position and set hover index.
            currentMouseX = e.getX();
            currentMouseY = e.getY();

            isOverCloseButton(currentMouseX, currentMouseY);

            if (oldHoverIndex != closeButtonHoverIndex) {
                tabPane.repaint();
                return;
            }

            delegate2.mouseMoved(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent e) {
            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (closeButtonArmedIndex != -1 && !isOverCloseButton(currentMouseX, currentMouseY)) {
                // isOverCloseButton resets closeButtonArmedIndex.
                tabPane.repaint();
            }
        }

        /**
         * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
            if (!tabPane.isEnabled()) {
                return;
            }

            if (!SwingUtilities.isLeftMouseButton(e) || !tabPane.isEnabled()) {
                return;
            }

            int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (isOverCloseButton(currentMouseX, currentMouseY)) {
                closeButtonArmedIndex = tabIndex;
                tabPane.repaint();
                return;
            } else if (closeButtonArmedIndex != -1) {
                // isOverCloseButton resets closeButtonArmedIndex.
                tabPane.repaint();
                return;
            }

            if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex)) {
                if (tabIndex == tabPane.getSelectedIndex()) {
                    // Clicking on selected tab
                    selectedTabIsPressed = true;

                    // TODO need to just repaint the tab area!
                    tabPane.repaint();
                }
            }

            // forward the event (this will set the selected index, or none
            // at all
            delegate.mousePressed(e);
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {
            if (closeButtonArmedIndex != -1) {
                if (isOverCloseButton(currentMouseX, currentMouseY)) {
                    doClose(closeButtonArmedIndex);
                }

                closeButtonArmedIndex = -1;

                tabPane.repaint();
            } else if (selectedTabIsPressed) {
                selectedTabIsPressed = false;

                // TODO need to just repaint the tab area!
                tabPane.repaint();
            }

            // forward the event
            delegate.mouseReleased(e);

            // hack: The super method *should* be setting the mouse-over
            // property correctly here, but it doesn't. That is, when the
            // mouse is released, whatever tab is below the released mouse
            // should be in rollover state. But, if you select a tab and
            // don't move the mouse, this doesn't happen. Hence, forwarding
            // the event.
            delegate2.mouseMoved(e);
        }
    }
}
