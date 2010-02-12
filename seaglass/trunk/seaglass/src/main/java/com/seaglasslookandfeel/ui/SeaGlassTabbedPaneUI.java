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
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassRegion;
import com.seaglasslookandfeel.component.SeaGlassArrowButton;
import com.seaglasslookandfeel.util.ControlOrientation;

import sun.swing.SwingUtilities2;

import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass's TabbedPane UI delegate.
 *
 * <p>Based on SynthTabbedPaneUI.</p>
 */
public class SeaGlassTabbedPaneUI extends BasicTabbedPaneUI implements SynthUI, PropertyChangeListener {

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

    private boolean selectedTabIsPressed = false;

    private SynthScrollableTabButton scrollForwardButton;
    private SynthScrollableTabButton scrollBackwardButton;
    private int                      leadingTabIndex  = 0;
    private int                      trailingTabIndex = 0;

    /**
     * Creates a new SeaGlassTabbedPaneUI object.
     */
    SeaGlassTabbedPaneUI() {
        textRect = new Rectangle();
        iconRect = new Rectangle();
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
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    private void updateStyle(JTabbedPane c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

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
     * DOCUMENT ME!
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
     * DOCUMENT ME!
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
        super.uninstallListeners();
        tabPane.removePropertyChangeListener(this);
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
     * @see sun.swing.plaf.synth.SynthUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  state DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c         DOCUMENT ME!
     * @param  subregion DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SeaGlassContext getContext(JComponent c, Region subregion) {
        return getContext(c, subregion, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c         DOCUMENT ME!
     * @param  subregion DOCUMENT ME!
     * @param  state     DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param  c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createScrollButton(int)
     */
    protected JButton createScrollButton(int direction) {
        SynthScrollableTabButton b = new SynthScrollableTabButton(direction);

        b.setName("TabbedPaneTabArea.button");
        return b;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
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
        final MouseListener       delegate  = super.createMouseListener();
        final MouseMotionListener delegate2 = (MouseMotionListener) delegate;

        return new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                delegate.mouseClicked(e);
            }

            public void mouseEntered(MouseEvent e) {
                delegate.mouseEntered(e);
            }

            public void mouseExited(MouseEvent e) {
                delegate.mouseExited(e);
            }

            public void mousePressed(MouseEvent e) {
                if (!tabPane.isEnabled()) {
                    return;
                }

                int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());

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

            public void mouseReleased(MouseEvent e) {
                if (selectedTabIsPressed) {
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
        };
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
        context.getPainter().paintTabbedPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
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
     * @see sun.swing.plaf.synth.SynthUI#paintBorder(javax.swing.plaf.synth.SynthContext,
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
        int tabPlacement  = tabPane.getTabPlacement();

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
        // This can be invoked from ScrollableTabPanel
        Insets insets = tabPane.getInsets();
        int    x      = insets.left;
        int    y      = insets.top;
        int    width  = tabPane.getWidth() - insets.left - insets.right;
        int    height = tabPane.getHeight() - insets.top - insets.bottom;

        paintTabArea(tabAreaContext, g, tabPlacement, selectedIndex, new Rectangle(x, y, width, height));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ss            DOCUMENT ME!
     * @param g             DOCUMENT ME!
     * @param tabPlacement  DOCUMENT ME!
     * @param selectedIndex DOCUMENT ME!
     * @param tabAreaBounds DOCUMENT ME!
     */
    protected void paintTabArea(SeaGlassContext ss, Graphics g, int tabPlacement, int selectedIndex, Rectangle tabAreaBounds) {
        Rectangle clipRect = g.getClipBounds();

        ss.setComponentState(SynthConstants.ENABLED);

        // Paint the tab area.

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabAreaBounds);
        ss.getPainter().paintTabbedPaneTabAreaBackground(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width,
                                                         tabAreaBounds.height);
        ss.getPainter().paintTabbedPaneTabAreaBorder(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width, tabAreaBounds.height,
                                                     tabPlacement);

        iconRect.setBounds(0, 0, 0, 0);
        textRect.setBounds(0, 0, 0, 0);

        if (runCount == 0) {
            return;
        }

        if (scrollBackwardButton.isVisible()) {
            paintScrollButtonBackground(g, tabPlacement, scrollBackwardButton);
        }

        if (scrollForwardButton.isVisible()) {
            paintScrollButtonBackground(g, tabPlacement, scrollForwardButton);
        }

        for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
            if (rects[i].intersects(clipRect) && selectedIndex != i) {
                paintTab(tabContext, g, tabPlacement, rects, i, iconRect, textRect);
            }
        }

        if (selectedIndex >= 0) {
            if (rects[selectedIndex].intersects(clipRect)) {
                paintTab(tabContext, g, tabPlacement, rects, selectedIndex, iconRect, textRect);
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
     * DOCUMENT ME!
     *
     * @param ss           DOCUMENT ME!
     * @param g            DOCUMENT ME!
     * @param tabPlacement DOCUMENT ME!
     * @param rects        DOCUMENT ME!
     * @param tabIndex     DOCUMENT ME!
     * @param iconRect     DOCUMENT ME!
     * @param textRect     DOCUMENT ME!
     */
    protected void paintTab(SeaGlassContext ss, Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
            Rectangle textRect) {
        Rectangle  tabRect       = rects[tabIndex];
        int        selectedIndex = tabPane.getSelectedIndex();
        boolean    isSelected    = selectedIndex == tabIndex;
        JComponent b             = ss.getComponent();

        if (!"segmented".equals(b.getClientProperty("JButton.buttonType"))) {
            b.putClientProperty("JButton.buttonType", "segmented");
        }

        boolean flipSegments    = (tabPlacement != LEFT && tabPlacement != RIGHT && !tabPane.getComponentOrientation().isLeftToRight());
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

        b.putClientProperty("JButton.segmentPosition", segmentPosition);
        updateTabContext(tabIndex, isSelected, isSelected && selectedTabIsPressed, (getRolloverTab() == tabIndex),
                         (getFocusIndex() == tabIndex));

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabRect);
        int x         = tabRect.x;
        int y         = tabRect.y;
        int height    = tabRect.height;
        int width     = tabRect.width;
        int placement = tabPane.getTabPlacement();

        tabContext.getPainter().paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, tabIndex, placement);
        tabContext.getPainter().paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, tabIndex, placement);

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
     * DOCUMENT ME!
     *
     * @param g            DOCUMENT ME!
     * @param tabPlacement DOCUMENT ME!
     * @param scrollButton DOCUMENT ME!
     */
    protected void paintScrollButtonBackground(Graphics g, int tabPlacement, JButton scrollButton) {
        Rectangle tabRect = scrollButton.getBounds();
        int       x       = tabRect.x;
        int       y       = tabRect.y;
        int       height  = tabRect.height;
        int       width   = tabRect.width;

        boolean flipSegments = (tabPlacement != LEFT && tabPlacement != RIGHT && !tabPane.getComponentOrientation().isLeftToRight());

        tabPane.putClientProperty("JButton.segmentPosition", ((scrollButton == scrollBackwardButton) ^ flipSegments) ? "first" : "last");

        tabContext.getPainter().paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, -1, tabPlacement);
        tabContext.getPainter().paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, -1, tabPlacement);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ss           DOCUMENT ME!
     * @param tabPlacement DOCUMENT ME!
     * @param metrics      DOCUMENT ME!
     * @param tabIndex     DOCUMENT ME!
     * @param title        DOCUMENT ME!
     * @param icon         DOCUMENT ME!
     * @param tabRect      DOCUMENT ME!
     * @param iconRect     DOCUMENT ME!
     * @param textRect     DOCUMENT ME!
     * @param isSelected   DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param ss           DOCUMENT ME!
     * @param g            DOCUMENT ME!
     * @param tabPlacement DOCUMENT ME!
     * @param font         DOCUMENT ME!
     * @param metrics      DOCUMENT ME!
     * @param tabIndex     DOCUMENT ME!
     * @param title        DOCUMENT ME!
     * @param textRect     DOCUMENT ME!
     * @param isSelected   DOCUMENT ME!
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

            g.setColor(ss.getStyle().getColor(ss, ColorType.TEXT_FOREGROUND));
            ss.getStyle().getGraphicsUtils(ss).paintText(ss, g, title, textRect, mnemIndex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ss            DOCUMENT ME!
     * @param g             DOCUMENT ME!
     * @param tabPlacement  DOCUMENT ME!
     * @param selectedIndex DOCUMENT ME!
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
     * DOCUMENT ME!
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
        } else {
            if (icon != null) {
                width += icon.getIconWidth() + textIconGap;
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
     * DOCUMENT ME!
     *
     * @param  font DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected FontMetrics getFontMetrics(Font font) {
        return tabPane.getFontMetrics(font);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index       DOCUMENT ME!
     * @param selected    DOCUMENT ME!
     * @param isMouseDown DOCUMENT ME!
     * @param isMouseOver DOCUMENT ME!
     * @param hasFocus    DOCUMENT ME!
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
            state &= ~SynthConstants.FOCUSED; // don't use tabbedpane focus

            // state
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
     * Request focus for visible component.
     *
     * <p>There is a comment from 7/29/98 that this method in BasicTabbedPaneUI
     * should be made protected when API changes are allowed. Obviously, no one
     * at Sun reads comments. -KAH</p>
     *
     * @return {@code true} if granted.
     */
    @SuppressWarnings("all")
    protected boolean requestFocusForVisibleComponent() {
        return SwingUtilities2.tabbedPaneChangeFocusTo(getVisibleComponent());
    }

    /**
     * The scrollable tab button.
     */
    private class SynthScrollableTabButton extends SeaGlassArrowButton implements UIResource {
        private static final long serialVersionUID = -3983149584304630486L;

        /**
         * Creates a new SynthScrollableTabButton object.
         *
         * @param direction DOCUMENT ME!
         */
        public SynthScrollableTabButton(int direction) {
            super(direction);
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

            int       tabCount         = tabPane.getTabCount();
            int       tabPlacement     = tabPane.getTabPlacement();
            Insets    insets           = tabPane.getInsets();
            int       selectedIndex    = tabPane.getSelectedIndex();
            Component visibleComponent = getVisibleComponent();

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

            calculateLayoutInfo();

            Component selectedComponent = null;

            if (selectedIndex < 0) {
                if (visibleComponent != null) {
                    // The last tab was removed, so remove the component
                    setVisibleComponent(null);
                }
            } else {
                selectedComponent = tabPane.getComponentAt(selectedIndex);
            }

            if (tabCount == 0) {
                scrollForwardButton.setVisible(false);
                scrollBackwardButton.setVisible(false);
                return;
            }

            boolean shouldChangeFocus = false;

            // In order to allow programs to use a single component
            // as the display for multiple tabs, we will not change
            // the visible compnent if the currently selected tab
            // has a null component.  This is a bit dicey, as we don't
            // explicitly state we support this in the spec, but since
            // programs are now depending on this, we're making it work.
            //
            if (selectedComponent != null) {
                if (selectedComponent != visibleComponent && visibleComponent != null) {
                    if (findFocusOwner(visibleComponent) != null) {
                        shouldChangeFocus = true;
                    }
                }

                setVisibleComponent(selectedComponent);
            }

            // tab area bounds
            int       tx;
            int       ty;
            int       tw;
            int       th;
            // content area bounds
            int       cx;
            int       cy;
            int       cw;
            int       ch;
            Insets    contentInsets = getContentBorderInsets(tabPlacement);
            Rectangle bounds        = tabPane.getBounds();
            int       numChildren   = tabPane.getComponentCount();

            if (numChildren > 0) {

                switch (tabPlacement) {

                case LEFT:
                    // calculate tab area bounds
                    tw = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                    th = bounds.height - insets.top - insets.bottom;
                    tx = insets.left;
                    ty = insets.top;

                    // calculate content area bounds
                    cx = tx + tw + contentInsets.left;
                    cy = ty + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - tw - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                    break;

                case RIGHT:
                    // calculate tab area bounds
                    tw = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                    th = bounds.height - insets.top - insets.bottom;
                    tx = bounds.width - insets.right - tw;
                    ty = insets.top;

                    // calculate content area bounds
                    cx = insets.left + contentInsets.left;
                    cy = insets.top + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - tw - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - contentInsets.top - contentInsets.bottom;
                    break;

                case BOTTOM:
                    // calculate tab area bounds
                    tw = bounds.width - insets.left - insets.right;
                    th = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                    tx = insets.left;
                    ty = bounds.height - insets.bottom - th;

                    // calculate content area bounds
                    cx = insets.left + contentInsets.left;
                    cy = insets.top + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - th - contentInsets.top - contentInsets.bottom;
                    break;

                case TOP:
                default:
                    // calculate tab area bounds
                    tw = bounds.width - insets.left - insets.right;
                    th = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                    tx = insets.left;
                    ty = insets.top;

                    // calculate content area bounds
                    cx = tx + contentInsets.left;
                    cy = ty + th + contentInsets.top;
                    cw = bounds.width - insets.left - insets.right - contentInsets.left - contentInsets.right;
                    ch = bounds.height - insets.top - insets.bottom - th - contentInsets.top - contentInsets.bottom;
                }

                for (int i = 0; i < numChildren; i++) {
                    Component child = tabPane.getComponent(i);

                    if (child == scrollBackwardButton || child == scrollForwardButton) {
                        // Ignore these buttons. They have already been positioned.
                    } else {
                        // All content children...
                        child.setBounds(cx, cy, cw, ch);
                    }
                }

                layoutTabComponents();

                if (shouldChangeFocus) {
                    if (!requestFocusForVisibleComponent()) {
                        tabPane.requestFocus();
                    }
                }
            }
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
                Insets    insets        = getTabInsets(tabPane.getTabPlacement(), i);
                int       outerX        = rect.x + insets.left;
                int       outerY        = rect.y + insets.top;
                int       outerWidth    = rect.width - insets.left - insets.right;
                int       outerHeight   = rect.height - insets.top - insets.bottom;
                // centralize component
                int       x             = outerX + (outerWidth - preferredSize.width) / 2;
                int       y             = outerY + (outerHeight - preferredSize.height) / 2;
                int       tabPlacement  = tabPane.getTabPlacement();
                boolean   isSeleceted   = i == tabPane.getSelectedIndex();

                c.setBounds(x + getTabLabelShiftX(tabPlacement, i, isSeleceted),
                            y + getTabLabelShiftY(tabPlacement, i, isSeleceted),
                            preferredSize.width, preferredSize.height);
            }
        }

        /**
         * @see com.apple.laf.AquaTabbedPaneCopyFromBasicUI$TabbedPaneLayout#calculateTabRects(int,
         *      int)
         */
        protected void calculateTabRects(int tabPlacement, int tabCount) {
            Dimension          size            = tabPane.getSize();
            Insets             insets          = tabPane.getInsets();
            Insets             tabAreaInsets   = getTabAreaInsets(tabPlacement);
            int                selectedIndex   = tabPane.getSelectedIndex();
            boolean            verticalTabRuns = (tabPlacement == LEFT || tabPlacement == RIGHT);
            ControlOrientation orientation     = ControlOrientation.getOrientation(verticalTabRuns ? VERTICAL : HORIZONTAL);
            boolean            leftToRight     = tabPane.getComponentOrientation().isLeftToRight();

            //
            // Calculate bounds within which a tab run must fit
            //
            switch (tabPlacement) {

            case LEFT:
            case RIGHT:
                maxTabWidth = calculateMaxTabWidth(tabPlacement);
                break;

            case BOTTOM:
            case TOP:
            default:
                maxTabHeight = calculateMaxTabHeight(tabPlacement);
            }

            if (tabCount == 0) {
                scrollBackwardButton.setVisible(false);
                scrollForwardButton.setVisible(false);
                runCount    = 0;
                selectedRun = -1;
                return;
            }

            selectedRun = 0;
            runCount    = 1;

            int totalLength = calcMaxLength(tabPlacement, tabCount, verticalTabRuns);

            if (leadingTabIndex > selectedIndex) {
                leadingTabIndex = selectedIndex;
            }

            // Make use of the fact that the scroll buttons have the same preferred size. Only assign one.
            int buttonLength         = orientation.getLength(scrollForwardButton.getPreferredSize());
            int tabAreaLength        = orientation.getPosition(size.width - tabAreaInsets.left - tabAreaInsets.right,
                                                               size.height - tabAreaInsets.top - tabAreaInsets.bottom);
            int leadingTabOffset     = orientation.getPosition(rects[leadingTabIndex]);
            int selectedTabEndOffset = orientation.getPosition(rects[selectedIndex].x + rects[selectedIndex].width,
                                                               rects[selectedIndex].y + rects[selectedIndex].height);

            if (totalLength <= tabAreaLength) {
                // Fits with no scroll buttons.
                leadingTabIndex  = 0;
                trailingTabIndex = tabCount - 1;
            } else if (totalLength + buttonLength - leadingTabOffset <= tabAreaLength) {
                // Fits from current leading tab index, with scroll backward button. Leave leadingTabIndex alone.
                trailingTabIndex = tabCount - 1;
            } else if (selectedTabEndOffset - leadingTabOffset + 2 * buttonLength <= tabAreaLength) {
                // Selected index fits with current leading tab index and two scroll button. Leave leadingTabIndex alone.
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

            tabRuns[0]       = leadingTabIndex;

            // Rebalance the layout such that the leading tab is at position 0.
            leadingTabOffset = orientation.getPosition(rects[leadingTabIndex]);
            for (int i = 0; i < tabCount; i++) {
                if (i < leadingTabIndex || i > trailingTabIndex) {
                    rects[i].setBounds(-1, -1, 0, 0);
                } else {
                    orientation.updateBoundsPosition(rects[i], orientation.getPosition(rects[i]) - leadingTabOffset);
                }
            }

            totalLength = orientation.getPosition(rects[trailingTabIndex].x + rects[trailingTabIndex].width,
                                                  rects[trailingTabIndex].y + rects[trailingTabIndex].height);

            // Subtract off the button length from the available length.
            if (leadingTabIndex > 0) {
                tabAreaLength -= buttonLength;
            }

            if (trailingTabIndex < tabCount - 1) {
                tabAreaLength -= buttonLength;
            }

            int offset    = orientation.getOrthogonalOffset(rects[leadingTabIndex]);
            int thickness = orientation.getThickness(rects[leadingTabIndex]);

            if (leadingTabIndex > 0 || trailingTabIndex < tabCount - 1) {
                // Fill the tabs to the available width.
                float multiplier = ((float) tabAreaLength / totalLength);

                for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
                    int position = (i == leadingTabIndex)
                        ? (orientation.getPosition(tabAreaInsets.left, tabAreaInsets.top) + (leadingTabIndex > 0 ? buttonLength : 0))
                        : orientation.getPosition(rects[i - 1]) + orientation.getLength(rects[i - 1]);
                    int length   = (int) (orientation.getLength(rects[i]) * multiplier);

                    rects[i].setBounds(orientation.createBounds(position, offset, length, thickness));
                }
            } else {
                // Center the tabs.
                int delta = -(tabAreaLength - totalLength) / 2 - orientation.getPosition(tabAreaInsets.left, tabAreaInsets.top);

                for (int i = leadingTabIndex; i <= trailingTabIndex; i++) {
                    int position = orientation.getPosition(rects[i]) - delta;
                    int length   = orientation.getLength(rects[i]);

                    rects[i].setBounds(orientation.createBounds(position, offset, length, thickness));
                }
            }

            setScrollButtonPositions(scrollBackwardButton, orientation, (leadingTabIndex > 0),
                                     orientation.getPosition(rects[leadingTabIndex]) - buttonLength);
            setScrollButtonPositions(scrollForwardButton, orientation, (trailingTabIndex < tabCount - 1),
                                     orientation.getPosition(rects[trailingTabIndex]) + orientation.getLength(rects[trailingTabIndex]));

            // if right to left and tab placement on the top or
            // the bottom, flip x positions and adjust by widths
            if (!leftToRight && !verticalTabRuns) {
                int rightMargin = size.width - (insets.right + tabAreaInsets.right);

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
        }

        /**
         * DOCUMENT ME!
         *
         * @param child       DOCUMENT ME!
         * @param orientation DOCUMENT ME!
         * @param visible     DOCUMENT ME!
         * @param position    DOCUMENT ME!
         */
        private void setScrollButtonPositions(Component child, ControlOrientation orientation, boolean visible, int position) {
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
         * Run through tabs and lay them all out in a single run, assigning
         * maxTabWidth and maxTabHeight.
         *
         * @param  tabPlacement    the side the tabs are on.
         * @param  tabCount        the number of tabs.
         * @param  verticalTabRuns {@code true} if tabs run vertically,
         *                         {@code false} otherwise.
         *
         * @return the maximum width, if tabs run horizontall, otherwise the
         *         maximum height.
         */
        private int calcMaxLength(int tabPlacement, int tabCount, boolean verticalTabRuns) {
            FontMetrics metrics    = getFontMetrics();
            int         fontHeight = metrics.getHeight();
            int         maxLength  = 0;
            int         offset;

            switch (tabPlacement) {

            case LEFT:
                offset = getTabAreaInsets(tabPlacement).left;
                break;

            case RIGHT:
                offset = tabPane.getSize().width - maxTabWidth - getTabAreaInsets(tabPlacement).right;
                break;

            case BOTTOM:
                offset = tabPane.getSize().height - maxTabHeight - getTabAreaInsets(tabPlacement).bottom;
                break;

            case TOP:
            default:
                offset = getTabAreaInsets(tabPlacement).top;
                break;
            }

            // Run through tabs and lay them out in a single run
            for (int i = 0; i < tabCount; i++) {
                Rectangle rect = rects[i];

                if (!verticalTabRuns) {
                    // Tabs on TOP or BOTTOM....
                    if (i > 0) {
                        rect.x = rects[i - 1].x + rects[i - 1].width;
                    } else {
                        tabRuns[0]  = 0;
                        maxTabWidth = 0;
                        rect.x      = 0;
                    }

                    rect.width  = calculateTabWidth(tabPlacement, i, metrics);
                    maxLength   = rect.x + rect.width;
                    maxTabWidth = Math.max(maxTabWidth, rect.width);

                    rect.y      = offset;
                    rect.height = maxTabHeight;
                } else {
                    // Tabs on LEFT or RIGHT...
                    if (i > 0) {
                        rect.y = rects[i - 1].y + rects[i - 1].height;
                    } else {
                        tabRuns[0]   = 0;
                        maxTabHeight = 0;
                        rect.y       = 0;
                    }

                    rect.height  = calculateTabHeight(tabPlacement, i, fontHeight);
                    maxLength    = rect.y + rect.height;
                    maxTabHeight = Math.max(maxTabHeight, rect.height);

                    rect.x     = offset;
                    rect.width = maxTabWidth;
                }
            }

            return maxLength;
        }

        /**
         * Find the focus owner of the component.
         *
         * @param  visibleComponent the component.
         *
         * @return the focus owner of the component.
         */
        @SuppressWarnings("deprecation")
        private Component findFocusOwner(Component visibleComponent) {
            return SwingUtilities.findFocusOwner(visibleComponent);
        }
    }
}
