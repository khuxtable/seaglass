/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.plaf.synth.SynthUI;

/**
 * SeaGlassScrollBarUI implementation.
 * 
 * Based on SynthScrollBarUI by Scott Violet.
 * 
 * @see javax.swing.plaf.synth.SynthScrollBarUI
 */
public class SeaGlassScrollBarUI extends BasicScrollBarUI implements PropertyChangeListener, SynthUI {

    private SynthStyle          style;
    private SynthStyle          thumbStyle;
    private SynthStyle          trackStyle;

    private MouseButtonListener mouseButtonListener;

    private boolean             validMinimumThumbSize;
    private int                 scrollBarWidth;

    // These two variables should be removed when the corrosponding ones in
    // BasicScrollBarUI are made protected
    private int                 incrGap;
    private int                 decrGap;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassScrollBarUI();
    }

    protected void installDefaults() {
        // NOTE: This next line of code was added because, since incrGap and
        // decrGap in
        // BasicScrollBarUI are private, I need to have some way of updating
        // them.
        // This is an incomplete solution (since it implies that the incrGap and
        // decrGap
        // are set once, and not reset per state. Probably ok, but not always
        // ok).
        // This line of code should be removed at the same time that incrGap and
        // decrGap are removed and made protected in the super class.
        super.installDefaults();

        trackHighlight = NO_HIGHLIGHT;
        if (scrollbar.getLayout() == null || (scrollbar.getLayout() instanceof UIResource)) {
            scrollbar.setLayout(this);
        }
        updateStyle(scrollbar);
    }

    protected void configureScrollBarColors() {
    }

    private void updateStyle(JScrollBar c) {
        SynthStyle oldStyle = style;
        SeaGlassContext context = getContext(c, ENABLED);
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            scrollBarWidth = style.getInt(context, "ScrollBar.thumbHeight", 14);

            minimumThumbSize = (Dimension) style.get(context, "ScrollBar.minimumThumbSize");
            if (minimumThumbSize == null) {
                minimumThumbSize = new Dimension();
                validMinimumThumbSize = false;
            } else {
                validMinimumThumbSize = true;
            }
            maximumThumbSize = (Dimension) style.get(context, "ScrollBar.maximumThumbSize");
            if (maximumThumbSize == null) {
                maximumThumbSize = new Dimension(4096, 4097);
            }

            incrGap = style.getInt(context, "ScrollBar.incrementButtonGap", 0);
            decrGap = style.getInt(context, "ScrollBar.decrementButtonGap", 0);

            // handle scaling for sizeVarients for special case components. The
            // key "JComponent.sizeVariant" scales for large/small/mini
            // components are based on Apples LAF
            String scaleKey = (String) scrollbar.getClientProperty("JComponent.sizeVariant");
            if (scaleKey != null) {
                if ("large".equals(scaleKey)) {
                    scrollBarWidth *= 1.15;
                    incrGap *= 1.15;
                    decrGap *= 1.15;
                } else if ("small".equals(scaleKey)) {
                    scrollBarWidth *= 0.857;
                    incrGap *= 0.857;
                    decrGap *= 0.857;
                } else if ("mini".equals(scaleKey)) {
                    scrollBarWidth *= 0.714;
                    incrGap *= 0.714;
                    decrGap *= 0.714;
                }
            }

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();

        context = getContext(c, Region.SCROLL_BAR_TRACK, ENABLED);
        trackStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();

        context = getContext(c, Region.SCROLL_BAR_THUMB, ENABLED);
        thumbStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    protected void installListeners() {
        super.installListeners();
        scrollbar.addPropertyChangeListener(this);
        mouseButtonListener = new MouseButtonListener();
        scrollbar.addMouseListener(mouseButtonListener);
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        scrollbar.removePropertyChangeListener(this);
        scrollbar.removeMouseListener(mouseButtonListener);
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(scrollbar, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        context = getContext(scrollbar, Region.SCROLL_BAR_TRACK, ENABLED);
        trackStyle.uninstallDefaults(context);
        context.dispose();
        trackStyle = null;

        context = getContext(scrollbar, Region.SCROLL_BAR_THUMB, ENABLED);
        thumbStyle.uninstallDefaults(context);
        context.dispose();
        thumbStyle = null;

        super.uninstallDefaults();
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SynthLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    private SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        SynthStyle style = trackStyle;

        if (region == Region.SCROLL_BAR_THUMB) {
            style = thumbStyle;
        }
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    public JButton getDecreaseButton() {
        return decrButton;
    }

    public JButton getIncreaseButton() {
        return incrButton;
    }

    public boolean isMouseButtonDown() {
        return isMouseButtonDown;
    }

    private int getComponentState(JComponent c, Region region) {
        if (region == Region.SCROLL_BAR_THUMB && isThumbRollover() && c.isEnabled()) {
            if (isMouseButtonDown) {
                return PRESSED;
            }
            return MOUSE_OVER;
        }
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    public boolean getSupportsAbsolutePositioning() {
        SeaGlassContext context = getContext(scrollbar);
        boolean value = style.getBoolean(context, "ScrollBar.allowsAbsolutePositioning", false);
        context.dispose();
        return value;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintScrollBarBackground(context, g, 0, 0, c.getWidth(), c.getHeight(), scrollbar.getOrientation());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        SeaGlassContext subcontext = getContext(scrollbar, Region.SCROLL_BAR_TRACK);
        paintTrack(subcontext, g, getTrackBounds());
        subcontext.dispose();

        subcontext = getContext(scrollbar, Region.SCROLL_BAR_THUMB);
        paintThumb(subcontext, g, getThumbBounds());
        subcontext.dispose();
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintScrollBarBorder(context, g, x, y, w, h, scrollbar.getOrientation());
    }

    protected void paintTrack(SeaGlassContext ss, Graphics g, Rectangle trackBounds) {
        SeaGlassLookAndFeel.updateSubregion(ss, g, trackBounds);
        ss.getPainter().paintScrollBarTrackBackground(ss, g, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height,
            scrollbar.getOrientation());
        ss.getPainter().paintScrollBarTrackBorder(ss, g, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height,
            scrollbar.getOrientation());
    }

    protected void paintThumb(SeaGlassContext ss, Graphics g, Rectangle thumbBounds) {
        SeaGlassLookAndFeel.updateSubregion(ss, g, thumbBounds);
        int orientation = scrollbar.getOrientation();
        ss.getPainter().paintScrollBarThumbBackground(ss, g, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height,
            orientation);
        ss.getPainter().paintScrollBarThumbBorder(ss, g, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height,
            orientation);
    }

    /**
     * A vertical scrollbar's preferred width is the maximum of preferred widths
     * of the (non <code>null</code>) increment/decrement buttons, and the
     * minimum width of the thumb. The preferred height is the sum of the
     * preferred heights of the same parts. The basis for the preferred size of
     * a horizontal scrollbar is similar.
     * <p>
     * The <code>preferredSize</code> is only computed once, subsequent calls to
     * this method just return a cached size.
     * 
     * @param c
     *            the <code>JScrollBar</code> that's delegating this method to
     *            us
     * @return the preferred size of a Basic JScrollBar
     * @see #getMaximumSize
     * @see #getMinimumSize
     */
    public Dimension getPreferredSize(JComponent c) {
        Insets insets = c.getInsets();
        return (scrollbar.getOrientation() == JScrollBar.VERTICAL) ? new Dimension(scrollBarWidth + insets.left + insets.right, 48)
                : new Dimension(48, scrollBarWidth + insets.top + insets.bottom);
    }

    protected Dimension getMinimumThumbSize() {
        if (!validMinimumThumbSize) {
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                minimumThumbSize.width = scrollBarWidth;
                minimumThumbSize.height = 7;
            } else {
                minimumThumbSize.width = 7;
                minimumThumbSize.height = scrollBarWidth;
            }
        }
        return minimumThumbSize;
    }

    protected JButton createDecreaseButton(int orientation) {
        SeaGlassArrowButton synthArrowButton = new SeaGlassArrowButton(orientation) {
            @Override
            public boolean contains(int x, int y) {
                if (decrGap < 0) { // there is an overlap between the track and
                    // button
                    int width = getWidth();
                    int height = getHeight();
                    if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                        // adjust the height by decrGap
                        // Note: decrGap is negative!
                        height += decrGap;
                    } else {
                        // adjust the width by decrGap
                        // Note: decrGap is negative!
                        width += decrGap;
                    }
                    return (x >= 0) && (x < width) && (y >= 0) && (y < height);
                }
                return super.contains(x, y);
            }
        };
        synthArrowButton.setName("ScrollBar.button");
        return synthArrowButton;
    }

    protected JButton createIncreaseButton(int orientation) {
        SeaGlassArrowButton synthArrowButton = new SeaGlassArrowButton(orientation) {
            @Override
            public boolean contains(int x, int y) {
                if (incrGap < 0) { // there is an overlap between the track and
                    // button
                    int width = getWidth();
                    int height = getHeight();
                    if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                        // adjust the height and y by incrGap
                        // Note: incrGap is negative!
                        height += incrGap;
                        y += incrGap;
                    } else {
                        // adjust the width and x by incrGap
                        // Note: incrGap is negative!
                        width += incrGap;
                        x += incrGap;
                    }
                    return (x >= 0) && (x < width) && (y >= 0) && (y < height);
                }
                return super.contains(x, y);
            }
        };
        synthArrowButton.setName("ScrollBar.button");
        return synthArrowButton;
    }

    protected void setThumbRollover(boolean active) {
        if (isThumbRollover() != active) {
            scrollbar.repaint(getThumbBounds());
            super.setThumbRollover(active);
        }
    }

    private void updateButtonDirections() {
        int orient = scrollbar.getOrientation();
        if (scrollbar.getComponentOrientation().isLeftToRight()) {
            ((SeaGlassArrowButton) incrButton).setDirection(orient == HORIZONTAL ? EAST : SOUTH);
            ((SeaGlassArrowButton) decrButton).setDirection(orient == HORIZONTAL ? WEST : NORTH);
        } else {
            ((SeaGlassArrowButton) incrButton).setDirection(orient == HORIZONTAL ? WEST : SOUTH);
            ((SeaGlassArrowButton) decrButton).setDirection(orient == HORIZONTAL ? EAST : NORTH);
        }
    }

    //
    // PropertyChangeListener
    //
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();

        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JScrollBar) e.getSource());
        }

        if ("orientation" == propertyName) {
            updateButtonDirections();
        } else if ("componentOrientation" == propertyName) {
            updateButtonDirections();
        }
    }

    private boolean isMouseButtonDown = false;

    /**
     * Track mouse drags.
     */
    protected class MouseButtonListener extends MouseAdapter implements MouseListener {
        protected transient int currentMouseX, currentMouseY;

        public void mouseReleased(MouseEvent e) {
            if (isMouseButtonDown) {
                isMouseButtonDown = false;

            }
        }

        /**
         * If the mouse is pressed above the "thumb" component then reduce the
         * scrollbars value by one page ("page up"), otherwise increase it by
         * one page. If there is no thumb then page up if the mouse is in the
         * upper half of the track.
         */
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)
                    || (!getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e))) return;
            if (!scrollbar.isEnabled()) return;

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            isMouseButtonDown = false;

            // Clicked in the Thumb area?
            if (getThumbBounds().contains(currentMouseX, currentMouseY)) {
                isMouseButtonDown = true;
                scrollbar.repaint();
            }
        }
    }

    /**
     * Listener for cursor keys.
     */
    protected class ArrowButtonListener extends MouseAdapter {
        // Because we are handling both mousePressed and Actions
        // we need to make sure we don't fire under both conditions.
        // (keyfocus on scrollbars causes action without mousePress
        boolean handledEvent;

        public void mousePressed(MouseEvent e) {
            if (!scrollbar.isEnabled()) {
                return;
            }
            // not an unmodified left mouse button
            // if(e.getModifiers() != InputEvent.BUTTON1_MASK) {return; }
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            int direction = (e.getSource() == incrButton) ? 1 : -1;

            scrollByUnit(direction);
            scrollTimer.stop();
            scrollListener.setDirection(direction);
            scrollListener.setScrollByBlock(false);
            scrollTimer.start();

            handledEvent = true;
            if (!scrollbar.hasFocus() && scrollbar.isRequestFocusEnabled()) {
                scrollbar.requestFocus();
            }
        }

        public void mouseReleased(MouseEvent e) {
            scrollTimer.stop();
            handledEvent = false;
            scrollbar.setValueIsAdjusting(false);
        }
    }
}
