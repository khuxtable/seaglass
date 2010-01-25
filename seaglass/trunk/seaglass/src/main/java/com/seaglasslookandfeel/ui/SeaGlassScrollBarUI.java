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

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassRegion;
import com.seaglasslookandfeel.component.SeaGlassArrowButton;
import com.seaglasslookandfeel.state.ScrollBarButtonsTogetherState;
import com.seaglasslookandfeel.state.State;

import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.SynthUI;

/**
 * SeaGlassScrollBarUI implementation.
 * 
 * Based on SynthScrollBarUI by Scott Violet.
 * 
 * @see javax.swing.plaf.synth.SynthScrollBarUI
 */
public class SeaGlassScrollBarUI extends BasicScrollBarUI implements PropertyChangeListener, SynthUI {

    private static final State  buttonsTogether = new ScrollBarButtonsTogetherState();

    private SynthStyle          style;
    private SynthStyle          thumbStyle;
    private SynthStyle          trackStyle;
    private SynthStyle          capStyle;

    private MouseButtonListener mouseButtonListener;

    private boolean             validMinimumThumbSize;
    private int                 scrollBarWidth;

    // These two variables should be removed when the corrosponding ones in
    // BasicScrollBarUI are made protected
    private int                 incrGap;
    private int                 decrGap;

    private int                 capSize;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassScrollBarUI();
    }

    protected void installDefaults() {
        /*
         * NOTE: This next line of code was added because, since incrGap and
         * decrGap in BasicScrollBarUI are private, I need to have some way of
         * updating them. This is an incomplete solution (since it implies that
         * the incrGap and decrGap are set once, and not reset per state.
         * Probably ok, but not always ok). This line of code should be removed
         * at the same time that incrGap and decrGap are removed and made
         * protected in the super class.
         */
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
            capSize = style.getInt(context, "ScrollBar.capSize", 0);

            /*
             * Handle scaling for sizeVarients for special case components. The
             * key "JComponent.sizeVariant" scales for large/small/mini
             * components are based on Apples LAF
             */
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

        context = getContext(c, SeaGlassRegion.SCROLL_BAR_CAP, ENABLED);
        capStyle = SeaGlassLookAndFeel.updateStyle(context, this);
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

        context = getContext(scrollbar, SeaGlassRegion.SCROLL_BAR_CAP, ENABLED);
        capStyle.uninstallDefaults(context);
        context.dispose();
        capStyle = null;

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
        } else if (region == SeaGlassRegion.SCROLL_BAR_CAP) {
            style = capStyle;
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

        if (buttonsTogether.isInState(context.getComponent())) {
            subcontext = getContext(scrollbar, SeaGlassRegion.SCROLL_BAR_CAP);
            paintCap(subcontext, g, getCapBounds());
            subcontext.dispose();
        }

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
        ss.getPainter().paintScrollBarThumbBorder(ss, g, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, orientation);
    }

    protected void paintCap(SeaGlassContext ss, Graphics g, Rectangle capBounds) {
        SeaGlassLookAndFeel.updateSubregion(ss, g, capBounds);
        int orientation = scrollbar.getOrientation();
        ss.getPainter().paintScrollBarThumbBackground(ss, g, capBounds.x, capBounds.y, capBounds.width, capBounds.height, orientation);
    }

    protected Rectangle getCapBounds() {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Rectangle(0, 0, scrollBarWidth, capSize);
        } else if (scrollbar.getComponentOrientation().isLeftToRight()) {
            return new Rectangle(0, 0, capSize, scrollBarWidth);
        } else {
            return new Rectangle(scrollbar.getWidth() - capSize, 0, capSize, scrollBarWidth);
        }
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
                // if there is an overlap between the track and button
                if (decrGap < 0) {
                    // FIXME Need to take RtL orientation into account.
                    if (buttonsTogether.isInState(scrollbar)) {
                        int minX = 0;
                        int minY = 0;
                        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                            // adjust the height by decrGap
                            // Note: decrGap is negative!
                            minY -= decrGap;
                        } else {
                            // adjust the width by decrGap
                            // Note: decrGap is negative!
                            minX -= decrGap;
                        }
                        return (x >= minX) && (x < getWidth()) && (y >= minY) && (y < getHeight());
                    } else {
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
                // if there is an overlap between the track and button
                if (incrGap < 0 && !buttonsTogether.isInState(scrollbar)) {
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

    protected void layoutVScrollbar(JScrollBar sb) {
        if (!buttonsTogether.isInState(sb)) {
            super.layoutVScrollbar(sb);
            return;
        }

        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Width and left edge of the buttons and thumb.
         */
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;

        /*
         * Nominal locations of the buttons, assuming their preferred size will
         * fit.
         */
        boolean squareButtons = DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);

        int incrButtonH = itemW + 1;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);

        int decrButtonH = squareButtons ? itemW : decrButton.getPreferredSize().height;
        int decrButtonY = sbSize.height - sbInsets.bottom - sbInsets.top - incrButtonH - decrButton.getPreferredSize().height;

        /*
         * The thumb must fit within the height left over after we subtract the
         * preferredSize of the buttons and the insets and the gaps
         */
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        int gaps = decrGap + incrGap;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH) - gaps;

        /*
         * If the buttons don't fit, allocate half of the available space to
         * each and move the lower one (incrButton) down.
         */
        int sbAvailButtonH = (sbSize.height - sbInsetsH);
        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);

        /*
         * Update the trackRect field.
         */
        int itrackY = capSize + decrGap;
        int itrackH = decrButtonY - incrGap - itrackY;
        trackRect.setBounds(itemX, itrackY, itemW, itrackH);

        /*
         * Compute the height and origin of the thumb. The case where the thumb
         * is at the bottom edge is handled specially to avoid numerical
         * problems in computing thumbY. Enforce the thumbs min/max dimensions.
         * If the thumb doesn't fit in the track (trackH) we'll hide it later.
         */
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = sb.getValue();

        /*
         * Update the thumb bounds.
         */
        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));
        thumbH = Math.max(thumbH, getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, getMaximumThumbSize().height);
        int thumbY = decrButtonY - incrGap - thumbH;
        if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;
            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbY += capSize + decrGap;
        }

        /*
         * If the thumb isn't going to fit, zero it's bounds. Otherwise make
         * sure it fits between the buttons. Note that setting the thumbs bounds
         * will cause a repaint.
         */
        if (thumbH >= (int) trackH) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if ((thumbY + thumbH) > decrButtonY - incrGap) {
                thumbY = decrButtonY - incrGap - thumbH;
            }
            if (thumbY < (capSize + decrGap)) {
                thumbY = capSize + decrGap + 1;
            }
            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutHScrollbar(JScrollBar sb) {
        if (!buttonsTogether.isInState(sb)) {
            super.layoutHScrollbar(sb);
            return;
        }

        if (sb.getComponentOrientation().isLeftToRight()) {
            layoutHScrollbarTogetherLeftToRight(sb);
        } else {
            layoutHScrollbarTogetherRightToLeft(sb);
        }
    }

    private void layoutHScrollbarTogetherLeftToRight(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Height and top edge of the buttons and thumb.
         */
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;

        /*
         * Nominal locations of the buttons, assuming their preferred size will
         * fit.
         */
        boolean squareButtons = DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);
        int incrButtonW = itemH + 1;
        int incrButtonX = sbSize.width - (sbInsets.right + incrButtonW);
        int decrButtonW = squareButtons ? itemH : decrButton.getPreferredSize().width;
        int decrButtonX = incrButtonX - decrButtonW;

        /*
         * The thumb must fit within the width left over after we subtract the
         * preferredSize of the buttons and the insets and the gaps
         */
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = decrButtonW + incrButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW) - (decrGap + incrGap);

        /*
         * If the buttons don't fit, allocate half of the available space to
         * each and move the right one over.
         */
        int sbAvailButtonW = (sbSize.width - sbInsetsW);
        if (sbAvailButtonW < sbButtonsW) {
            incrButtonW = decrButtonW = sbAvailButtonW / 2;
            incrButtonX = sbSize.width - (sbInsets.right + incrButtonW + incrGap);
        }

        decrButton.setBounds(decrButtonX, itemY, decrButtonW, itemH);
        incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);

        /*
         * Update the trackRect field.
         */
        int itrackX = sbInsets.left + capSize + decrGap;
        int itrackW = incrButtonX - incrGap - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);

        /*
         * Compute the width and origin of the thumb. Enforce the thumbs min/max
         * dimensions. The case where the thumb is at the right edge is handled
         * specially to avoid numerical problems in computing thumbX. If the
         * thumb doesn't fit in the track (trackH) we'll hide it later.
         */
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();

        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));
        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);

        int thumbX = decrButtonX - incrGap - thumbW;
        if (value < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            thumbX = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbX += sbInsets.left + capSize + decrGap;
        }

        /*
         * Make sure the thumb fits between the buttons. Note that setting the
         * thumbs bounds causes a repaint.
         */
        if (thumbW >= (int) trackW) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbX + thumbW > decrButtonX - incrGap) {
                thumbX = decrButtonX - incrGap - thumbW;
            }
            if (thumbX < sbInsets.left + capSize + decrGap) {
                thumbX = sbInsets.left + capSize + decrGap + 1;
            }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    private void layoutHScrollbarTogetherRightToLeft(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /*
         * Height and top edge of the buttons and thumb.
         */
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;

        /*
         * Nominal locations of the buttons, assuming their preferred size will
         * fit.
         */
        boolean squareButtons = DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);
        int incrButtonX = sbInsets.left;
        int incrButtonW = itemH + 1;
        int decrButtonW = squareButtons ? itemH : decrButton.getPreferredSize().width;
        int decrButtonX = incrButtonX + incrButtonW;

        /*
         * The thumb must fit within the width left over after we subtract the
         * preferredSize of the buttons and the insets and the gaps
         */
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = incrButtonW + decrButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW) - (incrGap + decrGap);

        /*
         * If the buttons don't fit, allocate half of the available space to
         * each and move the right one over.
         */
        int sbAvailButtonW = (sbSize.width - sbInsetsW);
        if (sbAvailButtonW < sbButtonsW) {
            decrButtonW = incrButtonW = sbAvailButtonW / 2;
            decrButtonX = sbSize.width - (sbInsets.right + decrButtonW + decrGap);
        }

        incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);
        decrButton.setBounds(decrButtonX, itemY, decrButtonW, itemH);

        /*
         * Update the trackRect field.
         */
        int itrackX = decrButtonX + decrButtonW + incrGap;
        int itrackW = sb.getWidth() - sbInsets.right - capSize - decrGap - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);

        /*
         * Compute the width and origin of the thumb. Enforce the thumbs min/max
         * dimensions. The case where the thumb is at the right edge is handled
         * specially to avoid numerical problems in computing thumbX. If the
         * thumb doesn't fit in the track (trackH) we'll hide it later.
         */
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();

        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));
        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);

        int thumbX = decrButtonX + decrButtonW + incrGap;
        if (value < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            thumbX = (int) (0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
            thumbX += decrButtonX + decrButtonW + incrGap;
        }

        /*
         * Make sure the thumb fits between the buttons. Note that setting the
         * thumbs bounds causes a repaint.
         */
        if (thumbW >= (int) trackW) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbX + thumbW > sb.getWidth() - sbInsets.right - capSize - decrGap) {
                thumbX = sb.getWidth() - sbInsets.right - capSize - decrGap - thumbW;
            }
            if (thumbX < decrButtonX + decrButtonW + incrGap) {
                thumbX = decrButtonX + decrButtonW + incrGap + 1;
            }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
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
            if (SwingUtilities.isRightMouseButton(e) || (!getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e)))
                return;
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
        /*
         * Because we are handling both mousePressed and Actions we need to make
         * sure we don't fire under both conditions. (keyfocus on scrollbars
         * causes action without mousePress
         */
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
