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
package com.seaglass.component;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.synth.Region;

import com.seaglass.SeaGlassContext;
import com.seaglass.SeaGlassLookAndFeel;
import com.seaglass.ui.SeaGlassSplitPaneUI;

import sun.swing.DefaultLookup;

/**
 * SeaGlassSplitPaneDivider implementation.
 * 
 * Based on Synth's divider.
 * 
 * @see javax.swing.plaf.synth.SynthSplitPaneDivider
 */
public class SeaGlassSplitPaneDivider extends BasicSplitPaneDivider {
    public SeaGlassSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }

    protected void setMouseOver(boolean mouseOver) {
        if (isMouseOver() != mouseOver) {
            repaint();
        }
        super.setMouseOver(mouseOver);
    }

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

    public void paint(Graphics g) {
        Graphics g2 = g.create();

        SeaGlassContext context = ((SeaGlassSplitPaneUI) splitPaneUI).getContext(splitPane, Region.SPLIT_PANE_DIVIDER);
        Rectangle bounds = getBounds();
        bounds.x = bounds.y = 0;
        SeaGlassLookAndFeel.updateSubregion(context, g, bounds);
        context.getPainter().paintSplitPaneDividerBackground(context, g, 0, 0, bounds.width, bounds.height,
            splitPane.getOrientation());

        context.getPainter().paintSplitPaneDividerForeground(context, g, 0, 0, getWidth(), getHeight(), splitPane.getOrientation());
        context.dispose();

        // super.paint(g2);
        for (int counter = 0; counter < getComponentCount(); counter++) {
            Component child = getComponent(counter);
            Rectangle childBounds = child.getBounds();
            Graphics childG = g.create(childBounds.x, childBounds.y, childBounds.width, childBounds.height);
            child.paint(childG);
            childG.dispose();
        }
        g2.dispose();
    }

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
     * the left component in the split pane.
     */
    protected JButton createLeftOneTouchButton() {
        SeaGlassArrowButton b = new SeaGlassArrowButton(SwingConstants.NORTH);
        int oneTouchSize = lookupOneTouchSize();

        b.setName("SplitPaneDivider.leftOneTouchButton");
        b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        b.setDirection(mapDirection(true));
        return b;
    }

    private int lookupOneTouchSize() {
        return DefaultLookup.getInt(splitPaneUI.getSplitPane(), splitPaneUI, "SplitPaneDivider.oneTouchButtonSize", ONE_TOUCH_SIZE);
    }

    /**
     * Creates and return an instance of JButton that can be used to collapse
     * the right component in the split pane.
     */
    protected JButton createRightOneTouchButton() {
        SeaGlassArrowButton b = new SeaGlassArrowButton(SwingConstants.NORTH);
        int oneTouchSize = lookupOneTouchSize();

        b.setName("SplitPaneDivider.rightOneTouchButton");
        b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        b.setDirection(mapDirection(false));
        return b;
    }
}
