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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
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
import com.seaglasslookandfeel.component.SeaGlassArrowButton;

import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass's TabbedPane UI delegate.
 * 
 * Based on SynthTabbedPaneUI.
 */
public class SeaGlassTabbedPaneUI extends BasicTabbedPaneUI implements SynthUI, PropertyChangeListener {

    private SeaGlassContext tabAreaContext;
    private SeaGlassContext tabContext;
    private SeaGlassContext tabContentContext;

    private SynthStyle      style;
    private SynthStyle      tabStyle;
    private SynthStyle      tabAreaStyle;
    private SynthStyle      tabContentStyle;

    private Rectangle       textRect;
    private Rectangle       iconRect;

    private boolean         selectedTabIsPressed = false;

    private int             originalTabLayoutPolicy;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTabbedPaneUI();
    }

    SeaGlassTabbedPaneUI() {
        textRect = new Rectangle();
        iconRect = new Rectangle();
    }

    @Override
    public void installUI(JComponent c) {
        // Force the tabs to be scrolled rather than wrapped.
        JTabbedPane tabPane = (JTabbedPane) c;
        originalTabLayoutPolicy = tabPane.getTabLayoutPolicy();
        if (originalTabLayoutPolicy != JTabbedPane.SCROLL_TAB_LAYOUT) {
            tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }

        super.installUI(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);

        // Restore original tab layout policy.
        if (originalTabLayoutPolicy != JTabbedPane.SCROLL_TAB_LAYOUT) {
            ((JTabbedPane) c).setTabLayoutPolicy(originalTabLayoutPolicy);
        }
    }

    protected void installDefaults() {
        updateStyle(tabPane);
    }

    private void updateStyle(JTabbedPane c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        // Add properties other than JComponent colors, Borders and
        // opacity settings here:
        if (style != oldStyle) {

            tabRunOverlay = 0;
            textIconGap = style.getInt(context, "TabbedPane.textIconGap", 0);
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
        tabContext = getContext(c, Region.TABBED_PANE_TAB, ENABLED);
        this.tabStyle = SeaGlassLookAndFeel.updateStyle(tabContext, this);
        tabInsets = tabStyle.getInsets(tabContext, null);

        if (tabAreaContext != null) {
            tabAreaContext.dispose();
        }
        tabAreaContext = getContext(c, Region.TABBED_PANE_TAB_AREA, ENABLED);
        this.tabAreaStyle = SeaGlassLookAndFeel.updateStyle(tabAreaContext, this);
        tabAreaInsets = tabAreaStyle.getInsets(tabAreaContext, null);

        if (tabContentContext != null) {
            tabContentContext.dispose();
        }
        tabContentContext = getContext(c, Region.TABBED_PANE_CONTENT, ENABLED);
        this.tabContentStyle = SeaGlassLookAndFeel.updateStyle(tabContentContext, this);
        contentBorderInsets = tabContentStyle.getInsets(tabContentContext, null);
    }

    protected void installListeners() {
        super.installListeners();
        tabPane.addPropertyChangeListener(this);
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        tabPane.removePropertyChangeListener(this);
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(tabPane, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        tabStyle.uninstallDefaults(tabContext);
        tabContext.dispose();
        tabContext = null;
        tabStyle = null;

        tabAreaStyle.uninstallDefaults(tabAreaContext);
        tabAreaContext.dispose();
        tabAreaContext = null;
        tabAreaStyle = null;

        tabContentStyle.uninstallDefaults(tabContentContext);
        tabContentContext.dispose();
        tabContentContext = null;
        tabContentStyle = null;
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    public SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    public SeaGlassContext getContext(JComponent c, Region subregion) {
        return getContext(c, subregion, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, Region subregion, int state) {
        SynthStyle style = null;
        Class klass = SeaGlassContext.class;

        if (subregion == Region.TABBED_PANE_TAB) {
            style = tabStyle;
        } else if (subregion == Region.TABBED_PANE_TAB_AREA) {
            style = tabAreaStyle;
        } else if (subregion == Region.TABBED_PANE_CONTENT) {
            style = tabContentStyle;
        }
        return SeaGlassContext.getContext(klass, c, subregion, style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    protected JButton createScrollButton(int direction) {
        // added for Nimbus LAF so that it can use the basic arrow buttons
        // UIManager is queried directly here because this is called before
        // updateStyle is called so the style can not be queried directly
        if (UIManager.getBoolean("TabbedPane.useBasicArrows")) {
            JButton btn = super.createScrollButton(direction);
            btn.setBorder(BorderFactory.createEmptyBorder());
            return btn;
        }
        return new SynthScrollableTabButton(direction);
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle(tabPane);
        }
    }

    /**
     * @inheritDoc
     * 
     *             Overridden to keep track of whether the selected tab is also
     *             pressed.
     */
    @Override
    protected MouseListener createMouseListener() {
        final MouseListener delegate = super.createMouseListener();
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

    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTabbedPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    protected int getBaseline(int tab) {
        if (tabPane.getTabComponentAt(tab) != null || getTextViewForTab(tab) != null) {
            return super.getBaseline(tab);
        }
        String title = tabPane.getTitleAt(tab);
        Font font = tabContext.getStyle().getFont(tabContext);
        FontMetrics metrics = getFontMetrics(font);
        Icon icon = getIconForTab(tab);
        textRect.setBounds(0, 0, 0, 0);
        iconRect.setBounds(0, 0, 0, 0);
        calcRect.setBounds(0, 0, Short.MAX_VALUE, maxTabHeight);
        tabContext.getStyle().getGraphicsUtils(tabContext).layoutText(tabContext, metrics, title, icon, SwingUtilities.CENTER,
            SwingUtilities.CENTER, SwingUtilities.LEADING, SwingUtilities.TRAILING, calcRect, iconRect, textRect, textIconGap);
        return textRect.y + metrics.getAscent() + getBaselineOffset();
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTabbedPaneBorder(context, g, x, y, w, h);
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        int selectedIndex = tabPane.getSelectedIndex();
        int tabPlacement = tabPane.getTabPlacement();

        ensureCurrentLayout();

        // Paint content border.
        paintContentBorder(tabContentContext, g, tabPlacement, selectedIndex);
    }

    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        // This can be invoked from ScrollableTabPanel
        Insets insets = tabPane.getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = tabPane.getWidth() - insets.left - insets.right;
        int height = tabPane.getHeight() - insets.top - insets.bottom;

        paintTabArea(tabAreaContext, g, tabPlacement, selectedIndex, new Rectangle(x, y, width, height));
    }

    protected void paintTabArea(SeaGlassContext ss, Graphics g, int tabPlacement, int selectedIndex, Rectangle tabAreaBounds) {
        Rectangle clipRect = g.getClipBounds();

        ss.setComponentState(SynthConstants.ENABLED);

        // Paint the tab area.

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabAreaBounds);
        ss.getPainter()
            .paintTabbedPaneTabAreaBackground(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width, tabAreaBounds.height);
        ss.getPainter().paintTabbedPaneTabAreaBorder(ss, g, tabAreaBounds.x, tabAreaBounds.y, tabAreaBounds.width, tabAreaBounds.height,
            tabPlacement);

        int tabCount = tabPane.getTabCount();

        iconRect.setBounds(0, 0, 0, 0);
        textRect.setBounds(0, 0, 0, 0);

        // Paint tabRuns of tabs from back to front
        for (int i = runCount - 1; i >= 0; i--) {
            int start = tabRuns[i];
            int next = tabRuns[(i == runCount - 1) ? 0 : i + 1];
            int end = (next != 0 ? next - 1 : tabCount - 1);
            for (int j = start; j <= end; j++) {
                if (rects[j].intersects(clipRect) && selectedIndex != j) {
                    paintTab(tabContext, g, tabPlacement, rects, j, iconRect, textRect);
                }
            }
        }

        if (selectedIndex >= 0) {
            if (rects[selectedIndex].intersects(clipRect)) {
                paintTab(tabContext, g, tabPlacement, rects, selectedIndex, iconRect, textRect);
            }
        }
    }

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

    protected void paintTab(SeaGlassContext ss, Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
        Rectangle textRect) {
        Rectangle tabRect = rects[tabIndex];
        int selectedIndex = tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;
        JComponent b = ss.getComponent();
        if (!"segmented".equals(b.getClientProperty("JButton.buttonType"))) {
            b.putClientProperty("JButton.buttonType", "segmented");
        }
        String segmentPosition = "only";
        if (tabPane.getTabCount() > 1) {
            if (tabIndex == 0) {
                segmentPosition = "first";
            } else if (tabIndex == tabPane.getTabCount() - 1) {
                segmentPosition = "last";
            } else {
                segmentPosition = "middle";
            }
        }
        b.putClientProperty("JButton.segmentPosition", segmentPosition);
        updateTabContext(tabIndex, isSelected, isSelected && selectedTabIsPressed, (getRolloverTab() == tabIndex),
            (getFocusIndex() == tabIndex));

        SeaGlassLookAndFeel.updateSubregion(ss, g, tabRect);
        int x = tabRect.x;
        int y = tabRect.y;
        int height = tabRect.height;
        int width = tabRect.width;
        int placement = tabPane.getTabPlacement();

        tabContext.getPainter().paintTabbedPaneTabBackground(tabContext, g, x, y, width, height, tabIndex, placement);
        tabContext.getPainter().paintTabbedPaneTabBorder(tabContext, g, x, y, width, height, tabIndex, placement);

        if (tabPane.getTabComponentAt(tabIndex) == null) {
            String title = tabPane.getTitleAt(tabIndex);
            Font font = ss.getStyle().getFont(ss);
            FontMetrics metrics = SwingUtilities2.getFontMetrics(tabPane, g, font);
            Icon icon = getIconForTab(tabIndex);

            layoutLabel(ss, tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);

            paintText(ss, g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);

            paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);
        }
    }

    protected void layoutLabel(SeaGlassContext ss, int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon,
        Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            tabPane.putClientProperty("html", v);
        }

        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        ss.getStyle().getGraphicsUtils(ss).layoutText(ss, metrics, title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER,
            SwingUtilities.LEADING, SwingUtilities.TRAILING, tabRect, iconRect, textRect, textIconGap);

        tabPane.putClientProperty("html", null);

        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

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

    protected void paintContentBorder(SeaGlassContext ss, Graphics g, int tabPlacement, int selectedIndex) {
        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
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

    public int calculateMaxTabHeight(int tabPlacement) {
        FontMetrics metrics = getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int tabCount = tabPane.getTabCount();
        int result = 0;
        int fontHeight = metrics.getHeight();
        for (int i = 0; i < tabCount; i++) {
            result = Math.max(calculateTabHeight(tabPlacement, i, fontHeight), result);
        }
        return result;
    }

    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Icon icon = getIconForTab(tabIndex);
        Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
        int width = tabInsets.left + tabInsets.right + 3;
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

    public int calculateMaxTabWidth(int tabPlacement) {
        FontMetrics metrics = getFontMetrics(tabContext.getStyle().getFont(tabContext));
        int tabCount = tabPane.getTabCount();
        int result = 0;
        for (int i = 0; i < tabCount; i++) {
            result = Math.max(calculateTabWidth(tabPlacement, i, metrics), result);
        }
        return result;
    }

    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        updateTabContext(tabIndex, false, false, false, (getFocusIndex() == tabIndex));
        return tabInsets;
    }

    protected FontMetrics getFontMetrics() {
        return getFontMetrics(tabContext.getStyle().getFont(tabContext));
    }

    protected FontMetrics getFontMetrics(Font font) {
        return tabPane.getFontMetrics(font);
    }

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
            state = SeaGlassLookAndFeel.getComponentState(tabPane);
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

    private class SynthScrollableTabButton extends SeaGlassArrowButton implements UIResource {
        public SynthScrollableTabButton(int direction) {
            super(direction);
        }
    }
}
