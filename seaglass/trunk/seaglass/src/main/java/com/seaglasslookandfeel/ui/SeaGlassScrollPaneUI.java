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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.painter.SeaGlassPainter;

/**
 * SeaGlassScrollPaneUI implementation.
 *<p>
 * Minimum necessary copied from BasicScrollPaneUI with appropriate change to
 * support Apple-style horizontal wheel scrolling.
 * 
 * @see javax.swing.plaf.basic.BasicScrollPaneUI
 */
public class SeaGlassScrollPaneUI extends BasicScrollPaneUI implements PropertyChangeListener, ScrollPaneConstants, SeaglassUI {
    private MouseWheelListener       mouseScrollListener;
    private SynthStyle               style;
    private boolean                  viewportViewHasFocus = false;
    private ViewportViewFocusHandler viewportViewFocusHandler;

    /**
     * PropertyChangeListener installed on the vertical scrollbar.
     */
    private PropertyChangeListener   vsbPropertyChangeListener;

    /**
     * PropertyChangeListener installed on the horizontal scrollbar.
     */
    private PropertyChangeListener   hsbPropertyChangeListener;

    private Handler                  handler;

    private SeaGlassPainter                  cornerPainter;

    /**
     * State flag that shows whether setValue() was called from a user program
     * before the value of "extent" was set in right-to-left component
     * orientation.
     */
    private boolean                  setValueCalled       = false;

    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassScrollPaneUI();
    }

    protected void installDefaults(JScrollPane scrollpane) {
        LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
        LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background", "ScrollPane.foreground", "ScrollPane.font");

        Border vpBorder = scrollpane.getViewportBorder();
        if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
            vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
            scrollpane.setViewportBorder(vpBorder);
        }

        Object obj = UIManager.get("ScrollPane.cornerPainter");
        if (obj != null && obj instanceof SeaGlassPainter) {
            cornerPainter = (SeaGlassPainter) obj;
        }

        LookAndFeel.installProperty(scrollpane, "opaque", Boolean.TRUE);
        updateStyle(scrollpane);
    }

    protected void uninstallDefaults(JScrollPane c) {
        SeaGlassContext context = getContext(c, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();

        if (scrollpane.getViewportBorder() instanceof UIResource) {
            scrollpane.setViewportBorder(null);
        }
    }

    private void updateStyle(JScrollPane c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            Border vpBorder = scrollpane.getViewportBorder();
            if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
                scrollpane.setViewportBorder(new ViewportBorder(context));
            }
            if (oldStyle != null) {
                uninstallKeyboardActions(c);
                installKeyboardActions(c);
            }
        }
        context.dispose();
    }

    protected void installListeners(JScrollPane c) {
        vsbChangeListener = createVSBChangeListener();
        vsbPropertyChangeListener = createVSBPropertyChangeListener();
        hsbChangeListener = createHSBChangeListener();
        hsbPropertyChangeListener = createHSBPropertyChangeListener();
        viewportChangeListener = createViewportChangeListener();
        spPropertyChangeListener = createPropertyChangeListener();

        JViewport viewport = scrollpane.getViewport();
        JScrollBar vsb = scrollpane.getVerticalScrollBar();
        JScrollBar hsb = scrollpane.getHorizontalScrollBar();

        if (viewport != null) {
            viewport.addChangeListener(viewportChangeListener);
        }
        if (vsb != null) {
            vsb.getModel().addChangeListener(vsbChangeListener);
            vsb.addPropertyChangeListener(vsbPropertyChangeListener);
        }
        if (hsb != null) {
            hsb.getModel().addChangeListener(hsbChangeListener);
            hsb.addPropertyChangeListener(hsbPropertyChangeListener);
        }

        scrollpane.addPropertyChangeListener(spPropertyChangeListener);

        mouseScrollListener = createMouseWheelListener();
        scrollpane.addMouseWheelListener(mouseScrollListener);

        // From SynthScrollPaneUI.
        c.addPropertyChangeListener(this);
        if (UIManager.getBoolean("ScrollPane.useChildTextComponentFocus")) {
            viewportViewFocusHandler = new ViewportViewFocusHandler();
            c.getViewport().addContainerListener(viewportViewFocusHandler);
            Component view = c.getViewport().getView();
            if (view instanceof JTextComponent) {
                view.addFocusListener(viewportViewFocusHandler);
            }
        }
    }

    protected void uninstallListeners(JComponent c) {
        JViewport viewport = scrollpane.getViewport();
        JScrollBar vsb = scrollpane.getVerticalScrollBar();
        JScrollBar hsb = scrollpane.getHorizontalScrollBar();

        if (viewport != null) {
            viewport.removeChangeListener(viewportChangeListener);
        }
        if (vsb != null) {
            vsb.getModel().removeChangeListener(vsbChangeListener);
            vsb.removePropertyChangeListener(vsbPropertyChangeListener);
        }
        if (hsb != null) {
            hsb.getModel().removeChangeListener(hsbChangeListener);
            hsb.removePropertyChangeListener(hsbPropertyChangeListener);
        }

        scrollpane.removePropertyChangeListener(spPropertyChangeListener);

        if (mouseScrollListener != null) {
            scrollpane.removeMouseWheelListener(mouseScrollListener);
        }

        vsbChangeListener = null;
        hsbChangeListener = null;
        viewportChangeListener = null;
        spPropertyChangeListener = null;
        mouseScrollListener = null;
        handler = null;

        // From SynthScrollPaneUI.
        c.removePropertyChangeListener(this);
        if (viewportViewFocusHandler != null) {
            viewport.removeContainerListener(viewportViewFocusHandler);
            if (viewport.getView() != null) {
                viewport.getView().removeFocusListener(viewportViewFocusHandler);
            }
            viewportViewFocusHandler = null;
        }
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SynthLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        int baseState = SeaGlassLookAndFeel.getComponentState(c);
        if (viewportViewFocusHandler != null && viewportViewHasFocus) {
            baseState = baseState | FOCUSED;
        }
        return baseState;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle(scrollpane);
        }
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintScrollPaneBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paintScrollPaneCorner(g, c);
        paint(context, g);
        context.dispose();
    }

    /**
     * @param g
     * @param c
     */
    @SuppressWarnings("unchecked")
    private void paintScrollPaneCorner(Graphics g, JComponent c) {
        if (scrollpane == null) {
            return;
        }
        if (scrollpane.getHorizontalScrollBar() == null || !scrollpane.getHorizontalScrollBar().isVisible()) {
            return;
        }
        if (scrollpane.getVerticalScrollBar() == null || !scrollpane.getVerticalScrollBar().isVisible()) {
            return;
        }

        int vBarWidth = scrollpane.getVerticalScrollBar().getWidth();
        int hBarHeight = scrollpane.getHorizontalScrollBar().getHeight();

        Insets insets = c.getInsets();

        Graphics2D g2 = (Graphics2D) g.create();
        
        int translateX = c.getWidth() - insets.right - vBarWidth;
        int translateY = c.getHeight() - insets.bottom - hBarHeight;
        boolean ltr = scrollpane.getComponentOrientation().isLeftToRight();
        if (!ltr) {
            translateX = 15 + insets.right;
        }
        Rectangle visibleRect = scrollpane.getVisibleRect();
        
        // Berechnung, ob die Ecke im sichtbare Bereich liegt
        int clipX = Math.min( vBarWidth, visibleRect.x + visibleRect.width - translateX );
        int clipY = Math.min( hBarHeight, visibleRect.y + visibleRect.height - translateY );
        
        if ( clipY > 0 && clipX > 0 ) {
            g2.translate(translateX, translateY);
            if (!ltr) {
                g2.scale(-1, 1);
            }
            g2.setClip(0, 0, clipX, clipY);
            
            cornerPainter.paint(g2, c, 15, 15);
        }
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SynthContext context, Graphics g) {
        Border vpBorder = scrollpane.getViewportBorder();
        if (vpBorder != null) {
            Rectangle r = scrollpane.getViewportBorderBounds();
            vpBorder.paintBorder(scrollpane, g, r.x, r.y, r.width, r.height);
        }
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintScrollPaneBorder(context, g, x, y, w, h);
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    protected void syncScrollPaneWithViewport() {
        JViewport viewport = scrollpane.getViewport();
        JScrollBar vsb = scrollpane.getVerticalScrollBar();
        JScrollBar hsb = scrollpane.getHorizontalScrollBar();
        JViewport rowHead = scrollpane.getRowHeader();
        JViewport colHead = scrollpane.getColumnHeader();
        boolean ltr = scrollpane.getComponentOrientation().isLeftToRight();

        if (viewport != null) {
            Dimension extentSize = viewport.getExtentSize();
            Dimension viewSize = viewport.getViewSize();
            Point viewPosition = viewport.getViewPosition();

            if (vsb != null) {
                int extent = extentSize.height;
                int max = viewSize.height;
                int value = Math.max(0, Math.min(viewPosition.y, max - extent));
                vsb.setValues(value, extent, 0, max);
            }

            if (hsb != null) {
                int extent = extentSize.width;
                int max = viewSize.width;
                int value;

                if (ltr) {
                    value = Math.max(0, Math.min(viewPosition.x, max - extent));
                } else {
                    int currentValue = hsb.getValue();

                    /*
                     * Use a particular formula to calculate "value" until
                     * effective x coordinate is calculated.
                     */
                    if (setValueCalled && ((max - currentValue) == viewPosition.x)) {
                        value = Math.max(0, Math.min(max - extent, currentValue));
                        /*
                         * After "extent" is set, turn setValueCalled flag off.
                         */
                        if (extent != 0) {
                            setValueCalled = false;
                        }
                    } else {
                        if (extent > max) {
                            viewPosition.x = max - extent;
                            viewport.setViewPosition(viewPosition);
                            value = 0;
                        } else {
                            /*
                             * The following line can't handle a small value of
                             * viewPosition.x like Integer.MIN_VALUE correctly
                             * because (max - extent - viewPositoiin.x) causes
                             * an overflow. As a result, value becomes zero.
                             * (e.g. setViewPosition(Integer.MAX_VALUE, ...) in
                             * a user program causes a overflow. Its expected
                             * value is (max - extent).) However, this seems a
                             * trivial bug and adding a fix makes this
                             * often-called method slow, so I'll leave it until
                             * someone claims.
                             */
                            value = Math.max(0, Math.min(max - extent, max - extent - viewPosition.x));
                        }
                    }
                }
                hsb.setValues(value, extent, 0, max);
            }

            if (rowHead != null) {
                Point p = rowHead.getViewPosition();
                p.y = viewport.getViewPosition().y;
                p.x = 0;
                rowHead.setViewPosition(p);
            }

            if (colHead != null) {
                Point p = colHead.getViewPosition();
                if (ltr) {
                    p.x = viewport.getViewPosition().x;
                } else {
                    p.x = Math.max(0, viewport.getViewPosition().x);
                }
                p.y = 0;
                colHead.setViewPosition(p);
            }
        }
    }

    /**
     * Listener for viewport events.
     */
    public class ViewportChangeHandler implements ChangeListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        public void stateChanged(ChangeEvent e) {
            getHandler().stateChanged(e);
        }
    }

    protected ChangeListener createViewportChangeListener() {
        return getHandler();
    }

    /**
     * Horizontal scrollbar listener.
     */
    public class HSBChangeListener implements ChangeListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        public void stateChanged(ChangeEvent e) {
            getHandler().stateChanged(e);
        }
    }

    /**
     * Returns a <code>PropertyChangeListener</code> that will be installed on
     * the horizontal <code>JScrollBar</code>.
     */
    private PropertyChangeListener createHSBPropertyChangeListener() {
        return getHandler();
    }

    protected ChangeListener createHSBChangeListener() {
        return getHandler();
    }

    /**
     * Vertical scrollbar listener.
     */
    public class VSBChangeListener implements ChangeListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        public void stateChanged(ChangeEvent e) {
            getHandler().stateChanged(e);
        }
    }

    /**
     * Returns a <code>PropertyChangeListener</code> that will be installed on
     * the vertical <code>JScrollBar</code>.
     */
    private PropertyChangeListener createVSBPropertyChangeListener() {
        return getHandler();
    }

    protected ChangeListener createVSBChangeListener() {
        return getHandler();
    }

    /**
     * MouseWheelHandler is an inner class which implements the
     * MouseWheelListener interface. MouseWheelHandler responds to
     * MouseWheelEvents by scrolling the JScrollPane appropriately. If the
     * scroll pane's <code>isWheelScrollingEnabled</code> method returns false,
     * no scrolling occurs.
     * 
     * @see javax.swing.JScrollPane#isWheelScrollingEnabled
     * @see #createMouseWheelListener
     * @see java.awt.event.MouseWheelListener
     * @see java.awt.event.MouseWheelEvent
     * @since 1.4
     */
    protected class MouseWheelHandler implements MouseWheelListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        /**
         * Called when the mouse wheel is rotated while over a JScrollPane.
         * 
         * @param e
         *            MouseWheelEvent to be handled
         * @since 1.4
         */
        public void mouseWheelMoved(MouseWheelEvent e) {
            getHandler().mouseWheelMoved(e);
        }
    }

    /**
     * Creates an instance of MouseWheelListener, which is added to the
     * JScrollPane by installUI(). The returned MouseWheelListener is used to
     * handle mouse wheel-driven scrolling.
     * 
     * @return MouseWheelListener which implements wheel-driven scrolling
     * @see #installUI
     * @see MouseWheelHandler
     * @since 1.4
     */
    protected MouseWheelListener createMouseWheelListener() {
        return getHandler();
    }

    private void updateHorizontalScrollBar(PropertyChangeEvent pce) {
        updateScrollBar(pce, hsbChangeListener, hsbPropertyChangeListener);
    }

    private void updateVerticalScrollBar(PropertyChangeEvent pce) {
        updateScrollBar(pce, vsbChangeListener, vsbPropertyChangeListener);
    }

    private void updateScrollBar(PropertyChangeEvent pce, ChangeListener cl, PropertyChangeListener pcl) {
        JScrollBar sb = (JScrollBar) pce.getOldValue();
        if (sb != null) {
            if (cl != null) {
                sb.getModel().removeChangeListener(cl);
            }
            if (pcl != null) {
                sb.removePropertyChangeListener(pcl);
            }
        }
        sb = (JScrollBar) pce.getNewValue();
        if (sb != null) {
            if (cl != null) {
                sb.getModel().addChangeListener(cl);
            }
            if (pcl != null) {
                sb.addPropertyChangeListener(pcl);
            }
        }
    }

    public class PropertyChangeHandler implements PropertyChangeListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        public void propertyChange(PropertyChangeEvent e) {
            getHandler().propertyChange(e);
        }
    }

    /**
     * Creates an instance of PropertyChangeListener that's added to the
     * JScrollPane by installUI(). Subclasses can override this method to return
     * a custom PropertyChangeListener, e.g.
     * 
     * <pre>
     * class MyScrollPaneUI extends BasicScrollPaneUI {
     *    protected PropertyChangeListener &lt;b&gt;createPropertyChangeListener&lt;/b&gt;() {
     *        return new MyPropertyChangeListener();
     *    }
     *    public class MyPropertyChangeListener extends PropertyChangeListener {
     *        public void propertyChange(PropertyChangeEvent e) {
     *            if (e.getPropertyName().equals(&quot;viewport&quot;)) {
     *                // do some extra work when the viewport changes
     *            }
     *            super.propertyChange(e);
     *        }
     *    }
     * }
     * </pre>
     * 
     * @see java.beans.PropertyChangeListener
     * @see #installUI
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    class Handler implements ChangeListener, PropertyChangeListener, MouseWheelListener {
        //
        // MouseWheelListener
        //
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (scrollpane.isWheelScrollingEnabled() && e.getWheelRotation() != 0) {
                boolean isHorizontal = (e.getModifiersEx() & MouseWheelEvent.SHIFT_DOWN_MASK) != 0;
                JScrollBar toScroll = scrollpane.getVerticalScrollBar();
                int direction = e.getWheelRotation() < 0 ? -1 : 1;
                int orientation = SwingConstants.VERTICAL;

                // find which scrollbar to scroll, or return if none
                if (toScroll == null || !toScroll.isVisible() || isHorizontal) {
                    toScroll = scrollpane.getHorizontalScrollBar();
                    if (toScroll == null || !toScroll.isVisible()) {
                        return;
                    }
                    orientation = SwingConstants.HORIZONTAL;
                }

                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    JViewport vp = scrollpane.getViewport();
                    if (vp == null) {
                        return;
                    }
                    Component comp = vp.getView();
                    int units = Math.abs(e.getUnitsToScroll());

                    // When the scrolling speed is set to maximum, it's possible
                    // for a single wheel click to scroll by more units than
                    // will fit in the visible area. This makes it
                    // hard/impossible to get to certain parts of the scrolling
                    // Component with the wheel. To make for more accurate
                    // low-speed scrolling, we limit scrolling to the block
                    // increment if the wheel was only rotated one click.
                    boolean limitScroll = Math.abs(e.getWheelRotation()) == 1;

                    // Check if we should use the visibleRect trick
                    Object fastWheelScroll = toScroll.getClientProperty("JScrollBar.fastWheelScrolling");
                    if (Boolean.TRUE == fastWheelScroll && comp instanceof Scrollable) {
                        // 5078454: Under maximum acceleration, we may scroll
                        // by many 100s of units in ~1 second.
                        //
                        // BasicScrollBarUI.scrollByUnits() can bog down the EDT
                        // with repaints in this situation. However, the
                        // Scrollable interface allows us to pass in an
                        // arbitrary visibleRect. This allows us to accurately
                        // calculate the total scroll amount, and then update
                        // the GUI once. This technique provides much faster
                        // accelerated wheel scrolling.
                        Scrollable scrollComp = (Scrollable) comp;
                        Rectangle viewRect = vp.getViewRect();
                        int startingX = viewRect.x;
                        boolean leftToRight = comp.getComponentOrientation().isLeftToRight();
                        int scrollMin = toScroll.getMinimum();
                        int scrollMax = toScroll.getMaximum() - toScroll.getModel().getExtent();

                        if (limitScroll) {
                            int blockIncr = scrollComp.getScrollableBlockIncrement(viewRect, orientation, direction);
                            if (direction < 0) {
                                scrollMin = Math.max(scrollMin, toScroll.getValue() - blockIncr);
                            } else {
                                scrollMax = Math.min(scrollMax, toScroll.getValue() + blockIncr);
                            }
                        }

                        for (int i = 0; i < units; i++) {
                            int unitIncr = scrollComp.getScrollableUnitIncrement(viewRect, orientation, direction);
                            // Modify the visible rect for the next unit, and
                            // check to see if we're at the end already.
                            if (orientation == SwingConstants.VERTICAL) {
                                if (direction < 0) {
                                    viewRect.y -= unitIncr;
                                    if (viewRect.y <= scrollMin) {
                                        viewRect.y = scrollMin;
                                        break;
                                    }
                                } else { // (direction > 0
                                    viewRect.y += unitIncr;
                                    if (viewRect.y >= scrollMax) {
                                        viewRect.y = scrollMax;
                                        break;
                                    }
                                }
                            } else {
                                // Scroll left
                                if ((leftToRight && direction < 0) || (!leftToRight && direction > 0)) {
                                    viewRect.x -= unitIncr;
                                    if (leftToRight) {
                                        if (viewRect.x < scrollMin) {
                                            viewRect.x = scrollMin;
                                            break;
                                        }
                                    }
                                }
                                // Scroll right
                                else if ((leftToRight && direction > 0) || (!leftToRight && direction < 0)) {
                                    viewRect.x += unitIncr;
                                    if (leftToRight) {
                                        if (viewRect.x > scrollMax) {
                                            viewRect.x = scrollMax;
                                            break;
                                        }
                                    }
                                } else {
                                    assert false : "Non-sensical ComponentOrientation / scroll direction";
                                }
                            }
                        }
                        // Set the final view position on the ScrollBar
                        if (orientation == SwingConstants.VERTICAL) {
                            toScroll.setValue(viewRect.y);
                        } else {
                            if (leftToRight) {
                                toScroll.setValue(viewRect.x);
                            } else {
                                // rightToLeft scrollbars are oriented with
                                // minValue on the right and maxValue on the
                                // left.
                                int newPos = toScroll.getValue() - (viewRect.x - startingX);
                                if (newPos < scrollMin) {
                                    newPos = scrollMin;
                                } else if (newPos > scrollMax) {
                                    newPos = scrollMax;
                                }
                                toScroll.setValue(newPos);
                            }
                        }
                    } else {
                        // Viewport's view is not a Scrollable, or fast wheel
                        // scrolling is not enabled.
                        scrollByUnits(toScroll, direction, units, limitScroll);
                    }
                } else if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
                    scrollByBlock(toScroll, direction);
                }
                e.consume();
            }
        }

        /*
         * Method for scrolling by a block increment. Added for mouse wheel
         * scrolling support, RFE 4202656.
         */
        void scrollByBlock(JScrollBar scrollbar, int direction) {
            // This method is called from BasicScrollPaneUI to implement wheel
            // scrolling, and also from scrollByBlock().
            int oldValue = scrollbar.getValue();
            int blockIncrement = scrollbar.getBlockIncrement(direction);
            int delta = blockIncrement * ((direction > 0) ? +1 : -1);
            int newValue = oldValue + delta;

            // Check for overflow.
            if (delta > 0 && newValue < oldValue) {
                newValue = scrollbar.getMaximum();
            } else if (delta < 0 && newValue > oldValue) {
                newValue = scrollbar.getMinimum();
            }

            scrollbar.setValue(newValue);
        }

        /*
         * Method for scrolling by a unit increment. Added for mouse wheel
         * scrolling support, RFE 4202656.
         * 
         * If limitByBlock is set to true, the scrollbar will scroll at least 1
         * unit increment, but will not scroll farther than the block increment.
         * See BasicScrollPaneUI.Handler.mouseWheelMoved().
         */
        void scrollByUnits(JScrollBar scrollbar, int direction, int units, boolean limitToBlock) {
            // This method is called from BasicScrollPaneUI to implement wheel
            // scrolling, as well as from scrollByUnit().
            int delta;
            int limit = -1;

            if (limitToBlock) {
                if (direction < 0) {
                    limit = scrollbar.getValue() - scrollbar.getBlockIncrement(direction);
                } else {
                    limit = scrollbar.getValue() + scrollbar.getBlockIncrement(direction);
                }
            }

            for (int i = 0; i < units; i++) {
                if (direction > 0) {
                    delta = scrollbar.getUnitIncrement(direction);
                } else {
                    delta = -scrollbar.getUnitIncrement(direction);
                }

                int oldValue = scrollbar.getValue();
                int newValue = oldValue + delta;

                // Check for overflow.
                if (delta > 0 && newValue < oldValue) {
                    newValue = scrollbar.getMaximum();
                } else if (delta < 0 && newValue > oldValue) {
                    newValue = scrollbar.getMinimum();
                }
                if (oldValue == newValue) {
                    break;
                }

                if (limitToBlock && i > 0) {
                    assert limit != -1;
                    if ((direction < 0 && newValue < limit) || (direction > 0 && newValue > limit)) {
                        break;
                    }
                }
                scrollbar.setValue(newValue);
            }
        }

        //
        // ChangeListener: This is added to the vieport, and hsb/vsb models.
        //
        public void stateChanged(ChangeEvent e) {
            JViewport viewport = scrollpane.getViewport();

            if (viewport != null) {
                if (e.getSource() == viewport) {
                    viewportStateChanged(e);
                } else {
                    JScrollBar hsb = scrollpane.getHorizontalScrollBar();
                    if (hsb != null && e.getSource() == hsb.getModel()) {
                        hsbStateChanged(viewport, e);
                    } else {
                        JScrollBar vsb = scrollpane.getVerticalScrollBar();
                        if (vsb != null && e.getSource() == vsb.getModel()) {
                            vsbStateChanged(viewport, e);
                        }
                    }
                }
            }
        }

        private void vsbStateChanged(JViewport viewport, ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
            Point p = viewport.getViewPosition();
            p.y = model.getValue();
            viewport.setViewPosition(p);
        }

        private void hsbStateChanged(JViewport viewport, ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
            Point p = viewport.getViewPosition();
            int value = model.getValue();
            if (scrollpane.getComponentOrientation().isLeftToRight()) {
                p.x = value;
            } else {
                int max = viewport.getViewSize().width;
                int extent = viewport.getExtentSize().width;
                int oldX = p.x;

                /*
                 * Set new X coordinate based on "value".
                 */
                p.x = max - extent - value;

                /*
                 * If setValue() was called before "extent" was fixed, turn
                 * setValueCalled flag on.
                 */
                if ((extent == 0) && (value != 0) && (oldX == max)) {
                    setValueCalled = true;
                } else {
                    /*
                     * When a pane without a horizontal scroll bar was reduced
                     * and the bar appeared, the viewport should show the right
                     * side of the view.
                     */
                    if ((extent != 0) && (oldX < 0) && (p.x == 0)) {
                        p.x += value;
                    }
                }
            }
            viewport.setViewPosition(p);
        }

        private void viewportStateChanged(ChangeEvent e) {
            syncScrollPaneWithViewport();
        }

        //
        // PropertyChangeListener: This is installed on both the JScrollPane
        // and the horizontal/vertical scrollbars.
        //

        // Listens for changes in the model property and reinstalls the
        // horizontal/vertical PropertyChangeListeners.
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getSource() == scrollpane) {
                scrollPanePropertyChange(e);
            } else {
                sbPropertyChange(e);
            }
        }

        private void scrollPanePropertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();

            if (propertyName == "verticalScrollBarDisplayPolicy") {
                updateScrollBarDisplayPolicy(e);
            } else if (propertyName == "horizontalScrollBarDisplayPolicy") {
                updateScrollBarDisplayPolicy(e);
            } else if (propertyName == "viewport") {
                updateViewport(e);
            } else if (propertyName == "rowHeader") {
                updateRowHeader(e);
            } else if (propertyName == "columnHeader") {
                updateColumnHeader(e);
            } else if (propertyName == "verticalScrollBar") {
                updateVerticalScrollBar(e);
            } else if (propertyName == "horizontalScrollBar") {
                updateHorizontalScrollBar(e);
            } else if (propertyName == "componentOrientation") {
                scrollpane.revalidate();
                scrollpane.repaint();
            }
        }

        // PropertyChangeListener for the horizontal and vertical scrollbars.
        private void sbPropertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            Object source = e.getSource();

            if ("model" == propertyName) {
                JScrollBar sb = scrollpane.getVerticalScrollBar();
                BoundedRangeModel oldModel = (BoundedRangeModel) e.getOldValue();
                ChangeListener cl = null;

                if (source == sb) {
                    cl = vsbChangeListener;
                } else if (source == scrollpane.getHorizontalScrollBar()) {
                    sb = scrollpane.getHorizontalScrollBar();
                    cl = hsbChangeListener;
                }
                if (cl != null) {
                    if (oldModel != null) {
                        oldModel.removeChangeListener(cl);
                    }
                    if (sb.getModel() != null) {
                        sb.getModel().addChangeListener(cl);
                    }
                }
            } else if ("componentOrientation" == propertyName) {
                if (source == scrollpane.getHorizontalScrollBar()) {
                    JScrollBar hsb = scrollpane.getHorizontalScrollBar();
                    JViewport viewport = scrollpane.getViewport();
                    Point p = viewport.getViewPosition();
                    if (scrollpane.getComponentOrientation().isLeftToRight()) {
                        p.x = hsb.getValue();
                    } else {
                        p.x = viewport.getViewSize().width - viewport.getExtentSize().width - hsb.getValue();
                    }
                    viewport.setViewPosition(p);
                }
            }
        }
    }

    private class ViewportBorder extends AbstractBorder implements UIResource {
        private Insets insets;

        ViewportBorder(SeaGlassContext context) {
            this.insets = (Insets) context.getStyle().get(context, "ScrollPane.viewportBorderInsets");
            if (this.insets == null) {
                this.insets = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            JComponent jc = (JComponent) c;
            SeaGlassContext context = getContext(jc);
            SynthStyle style = context.getStyle();
            if (style == null) {
                assert false : "SynthBorder is being used outside after the " + " UI has been uninstalled";
                return;
            }
            context.getPainter().paintViewportBorder(context, g, x, y, width, height);
            context.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, null);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            if (insets == null) {
                return new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
            }
            insets.top = this.insets.top;
            insets.bottom = this.insets.bottom;
            insets.left = this.insets.left;
            insets.right = this.insets.left;
            return insets;
        }

        public boolean isBorderOpaque() {
            return false;
        }
    }

    /**
     * Handle keeping track of the viewport's view's focus
     */
    private class ViewportViewFocusHandler implements ContainerListener, FocusListener {
        public void componentAdded(ContainerEvent e) {
            if (e.getChild() instanceof JTextComponent) {
                e.getChild().addFocusListener(this);
                viewportViewHasFocus = e.getChild().isFocusOwner();
                scrollpane.repaint();
            }
        }

        public void componentRemoved(ContainerEvent e) {
            if (e.getChild() instanceof JTextComponent) {
                e.getChild().removeFocusListener(this);
            }
        }

        public void focusGained(FocusEvent e) {
            viewportViewHasFocus = true;
            scrollpane.repaint();
        }

        public void focusLost(FocusEvent e) {
            viewportViewHasFocus = false;
            scrollpane.repaint();
        }
    }
}
