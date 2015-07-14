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

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.synth.Region;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.ui.SeaGlassSplitPaneUI;

import sun.swing.DefaultLookup;

/**
 * SeaGlassSplitPaneDivider implementation.
 *
 * <p>Based on Synth's divider.</p>
 *
 * @see javax.swing.plaf.synth.SynthSplitPaneDivider
 */
public class SeaGlassSplitPaneDivider extends BasicSplitPaneDivider {
    private static final long serialVersionUID = 5876267823818018895L;

    /**
     * Width or height of the divider based on orientation BasicSplitPaneUI adds
     * two to this.
     */
    protected static final int ONE_TOUCH_SIZE = 6;

    /** The offset for the one-touch buttons. */
    protected static final int ONE_TOUCH_OFFSET = 2;

    private int oneTouchSize;
    private int oneTouchOffset;

    /** If true the one touch buttons are centered on the divider. */
    private boolean centerOneTouchButtons;

    int oneTouchVOffset;

    /**
     * Creates a new SeaGlassSplitPaneDivider object.
     *
     * @param ui the parent UI delegate.
     */
    public SeaGlassSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        oneTouchSize          = DefaultLookup.getInt(ui.getSplitPane(), ui, "SplitPane.oneTouchButtonSize", ONE_TOUCH_SIZE);
        oneTouchOffset        = DefaultLookup.getInt(ui.getSplitPane(), ui, "SplitPane.oneTouchButtonOffset", ONE_TOUCH_OFFSET);
        centerOneTouchButtons = DefaultLookup.getBoolean(ui.getSplitPane(), ui, "SplitPane.centerOneTouchButtons", true);
        oneTouchVOffset       = DefaultLookup.getInt(ui.getSplitPane(), ui, "SplitPane.oneTouchButtonVOffset", 0);
        setLayout(new DividerLayout());
    }

    /**
     * @see javax.swing.plaf.basic.BasicSplitPaneDivider#setMouseOver(boolean)
     */
    protected void setMouseOver(boolean mouseOver) {
        if (isMouseOver() != mouseOver) {
            repaint();
        }

        super.setMouseOver(mouseOver);
    }

    /**
     * @see javax.swing.plaf.basic.BasicSplitPaneDivider#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);

        if (e.getSource() == splitPane) {

            if (e.getPropertyName() == JSplitPane.ORIENTATION_PROPERTY) {

                if (leftButton instanceof SeaGlassArrowButton) {
                    ((SeaGlassArrowButton) leftButton).setDirection(mapDirection(true));
                }

                if (rightButton instanceof SeaGlassArrowButton) {
                    ((SeaGlassArrowButton) rightButton).setDirection(mapDirection(false));
                }
            }
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicSplitPaneDivider#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        Graphics g2 = g.create();

        SeaGlassContext context = ((SeaGlassSplitPaneUI) splitPaneUI).getContext(splitPane, Region.SPLIT_PANE_DIVIDER);
        Rectangle       bounds  = getBounds();

        bounds.x = bounds.y = 0;
        SeaGlassLookAndFeel.updateSubregion(context, g, bounds);
        context.getPainter().paintSplitPaneDividerBackground(context, g, 0, 0, bounds.width, bounds.height, splitPane.getOrientation());

        context.getPainter().paintSplitPaneDividerForeground(context, g, 0, 0, getWidth(), getHeight(), splitPane.getOrientation());
        context.dispose();

        // super.paint(g2);
        for (int counter = 0; counter < getComponentCount(); counter++) {
            Component child       = getComponent(counter);
            Rectangle childBounds = child.getBounds();
            Graphics  childG      = g.create(childBounds.x, childBounds.y, childBounds.width, childBounds.height);

            child.paint(childG);
            childG.dispose();
        }

        g2.dispose();
    }

    /**
     * Convert the orientation of the pane into compass points based on the pane
     * orientation and the left-right orientation of the containter.
     *
     * @param  isLeft {@code true} if the component's container is laid out
     *                left-to-right, otherwise {@code false}.
     *
     * @return the compass direction for increasing the divider.
     */
    private int mapDirection(boolean isLeft) {
        if (isLeft) {

            if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                return SwingConstants.WEST;
            }

            return SwingConstants.NORTH;
        }

        if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
            return SwingConstants.EAST;
        }

        return SwingConstants.SOUTH;
    }

    /**
     * Creates and return an instance of JButton that can be used to collapse
     * the left/top component in the split pane.
     *
     * @return a one-touch button.
     */
    protected JButton createLeftOneTouchButton() {
        SeaGlassArrowButton b            = new SeaGlassArrowButton(SwingConstants.NORTH);
        int                 oneTouchSize = lookupOneTouchSize();

        b.setName("SplitPaneDivider.leftOneTouchButton");
        b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
        b.setCursor(Cursor.getPredefinedCursor(
            splitPane.getOrientation() == 
                JSplitPane.HORIZONTAL_SPLIT ? 
                        Cursor.W_RESIZE_CURSOR:Cursor.N_RESIZE_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        b.setDirection(mapDirection(true));

        return b;
    }

    /**
     * Get the size of the one-touch button.
     *
     * @return the button size.
     */
    private int lookupOneTouchSize() {
        return DefaultLookup.getInt(splitPaneUI.getSplitPane(), splitPaneUI, "SplitPaneDivider.oneTouchButtonSize", ONE_TOUCH_SIZE);
    }

    /**
     * Creates and return an instance of JButton that can be used to collapse
     * the right component in the split pane.
     *
     * @return a one-touch button
     */
    protected JButton createRightOneTouchButton() {
        SeaGlassArrowButton b            = new SeaGlassArrowButton(SwingConstants.NORTH);
        int                 oneTouchSize = lookupOneTouchSize();

        b.setName("SplitPaneDivider.rightOneTouchButton");
        b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
        // Rossi: Change cursors on "one touch" buttons. Better would be an mouse over effect
        b.setCursor(Cursor.getPredefinedCursor(
            splitPane.getOrientation() == 
                JSplitPane.HORIZONTAL_SPLIT ? 
                        Cursor.E_RESIZE_CURSOR:Cursor.S_RESIZE_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        b.setDirection(mapDirection(false));

        return b;
    }

    /**
     * Used to layout a <code>BasicSplitPaneDivider</code>. Layout for the
     * divider involves appropriately moving the left/right buttons around.
     */
    protected class DividerLayout implements LayoutManager {

        /**
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container c) {
            if (leftButton != null && rightButton != null && c == SeaGlassSplitPaneDivider.this) {

                if (splitPane.isOneTouchExpandable()) {
                    Insets insets = getInsets();

                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        int extraX    = (insets != null) ? insets.left : 0;
                        int blockSize = getHeight();

                        if (insets != null) {
                            blockSize -= (insets.top + insets.bottom);
                            blockSize = Math.max(blockSize, 0);
                        }

                        blockSize = Math.min(blockSize, oneTouchSize);

                        int y = (c.getSize().height - blockSize) / 2;

                        if (!centerOneTouchButtons) {
                            y      = (insets != null) ? insets.top : 0;
                            extraX = 0;
                        }

                        leftButton.setBounds(extraX + oneTouchOffset, y - oneTouchVOffset, blockSize * 2, blockSize);
                        rightButton.setBounds(extraX + oneTouchOffset, y + oneTouchVOffset + 1, blockSize * 2, blockSize);
                    } else {
                        int extraY    = (insets != null) ? insets.top : 0;
                        int blockSize = getWidth();

                        if (insets != null) {
                            blockSize -= (insets.left + insets.right);
                            blockSize = Math.max(blockSize, 0);
                        }

                        blockSize = Math.min(blockSize, oneTouchSize);

                        int x = (c.getSize().width - blockSize) / 2;

                        if (!centerOneTouchButtons) {
                            x      = (insets != null) ? insets.left : 0;
                            extraY = 0;
                        }

                        leftButton.setBounds(x - oneTouchVOffset, extraY + oneTouchOffset, blockSize, blockSize * 2);
                        rightButton.setBounds(x + oneTouchVOffset + 1, extraY + oneTouchOffset, blockSize, blockSize * 2);
                    }
                } else {
                    leftButton.setBounds(-5, -5, 1, 1);
                    rightButton.setBounds(-5, -5, 1, 1);
                }
            }
        }

        /**
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        public Dimension minimumLayoutSize(Container c) {
            // NOTE: This isn't really used, refer to
            // BasicSplitPaneDivider.getPreferredSize for the reason.
            // I leave it in hopes of having this used at some point.
            if (c != SeaGlassSplitPaneDivider.this || splitPane == null) {
                return new Dimension(0, 0);
            }

            Dimension buttonMinSize = null;

            if (splitPane.isOneTouchExpandable() && leftButton != null) {
                buttonMinSize = leftButton.getMinimumSize();
            }

            Insets insets = getInsets();
            int    width  = getDividerSize();
            int    height = width;

            if (orientation == JSplitPane.VERTICAL_SPLIT) {

                if (buttonMinSize != null) {
                    int size = buttonMinSize.height;

                    if (insets != null) {
                        size += insets.top + insets.bottom;
                    }

                    height = Math.max(height, size);
                }

                width = 1;
            } else {

                if (buttonMinSize != null) {
                    int size = buttonMinSize.width;

                    if (insets != null) {
                        size += insets.left + insets.right;
                    }

                    width = Math.max(width, size);
                }

                height = 1;
            }

            return new Dimension(width, height);
        }

        /**
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        /**
         * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
         */
        public void removeLayoutComponent(Component c) {
        }

        /**
         * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
         */
        public void addLayoutComponent(String string, Component c) {
        }
    }
}
