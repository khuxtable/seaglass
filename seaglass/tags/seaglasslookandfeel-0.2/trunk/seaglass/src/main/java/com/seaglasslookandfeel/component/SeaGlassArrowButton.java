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

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.UIResource;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.ui.SeaGlassButtonUI;

/**
 * JButton object that draws a scaled Arrow in one of the cardinal directions.
 *
 * <p>Based on SynthArrowButton by Scott Violet.</p>
 *
 * @see javax.swing.plaf.synth.SynthArrowButton
 */
public class SeaGlassArrowButton extends JButton implements SwingConstants, UIResource {
    private static final long serialVersionUID = 2673388860074501956L;
    private int               direction;

    /**
     * Creates a new SeaGlassArrowButton object.
     *
     * @param direction the direction to point the arrow. This will be one of
     *                  the SwingConstants {@code NORTH}, {@code WEST},
     *                  {@code SOUTH}, or {@code EAST}.
     */
    public SeaGlassArrowButton(int direction) {
        super();
        super.setFocusable(false);
        setDirection(direction);
        setDefaultCapable(false);
    }

    /**
     * @see javax.swing.JButton#getUIClassID()
     */
    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    /**
     * @see javax.swing.JButton#updateUI()
     */
    public void updateUI() {
        setUI(new SeaGlassArrowButtonUI());
    }

    /**
     * Set the arrow's direction.
     *
     * @param dir the direction to point the arrow. This will be one of the
     *            SwingConstants {@code NORTH}, {@code WEST}, {@code SOUTH}, or
     *            {@code EAST}.
     */
    public void setDirection(int dir) {
        direction = dir;
        putClientProperty("__arrow_direction__", new Integer(dir));
        repaint();
    }

    /**
     * Get the direction of the arrow.
     *
     * @return the direction the arrow points. This will be one of the
     *         SwingConstants {@code NORTH}, {@code WEST}, {@code SOUTH}, or
     *         {@code EAST}.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @see java.awt.Component#setFocusable(boolean)
     */
    public void setFocusable(boolean focusable) {
    }

    /**
     * The UI delegate for the arrow button.
     */
    private static class SeaGlassArrowButtonUI extends SeaGlassButtonUI {

        /**
         * @see com.seaglasslookandfeel.ui.SeaGlassButtonUI#installDefaults(javax.swing.AbstractButton)
         */
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            updateStyle(b);
        }

        /**
         * @see com.seaglasslookandfeel.ui.SeaGlassButtonUI#paint(com.seaglasslookandfeel.SeaGlassContext,
         *      java.awt.Graphics)
         */
        protected void paint(SeaGlassContext context, Graphics g) {
            SeaGlassArrowButton button = (SeaGlassArrowButton) context.getComponent();

            Double scale = (Double)button.getClientProperty("__arrow_scale__");
            if (scale == null) {
                scale = 1.0;
            }
            int width = (int) (button.getWidth()*scale);
            int height = (int) (button.getHeight()*scale);
            int x = (button.getWidth()-width)/2 * (button.getDirection() == SwingConstants.EAST?-1:1);
            int y = (button.getHeight()-height)/2 * (button.getDirection() == SwingConstants.SOUTH ?-1:1);
            
            context.getPainter().paintArrowButtonForeground(context, g, x, y, width, height,
                                                            button.getDirection());
        }

        /**
         * Paint the arrow background.
         *
         * @param context the SynthContext.
         * @param g       the Graphics context.
         * @param c       the arrow component.
         */
        @SuppressWarnings("all")
        void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
            context.getPainter().paintArrowButtonBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        }

        /**
         * Paint the arrow border.
         *
         * @param context the SynthContext.
         * @param g       the Graphics context.
         * @param x       the x coordinate corresponding to the upper-left
         *                corner to paint.
         * @param y       the y coordinate corresponding to the upper-left
         *                corner to paint.
         * @param w       the width to paint.
         * @param h       the height to paint.
         */
        @SuppressWarnings("unused")
        public void paintBorder(SeaGlassContext context, Graphics g, int x, int y, int w, int h) {
            context.getPainter().paintArrowButtonBorder(context, g, x, y, w, h);
        }

        /**
         * Get the minimum size for the arrow.
         *
         * @return the minimum size.
         */
        @SuppressWarnings("unused")
        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        /**
         * Get the maximum size for the arrow.
         *
         * @return the maximum size.
         */
        @SuppressWarnings("unused")
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        /**
         * @see com.seaglasslookandfeel.ui.SeaGlassButtonUI#getPreferredSize(javax.swing.JComponent)
         */
        public Dimension getPreferredSize(JComponent c) {
            SeaGlassContext context = getContext(c);
            Dimension       dim     = null;

            if (context.getComponent().getName() == "ScrollBar.button") {

                // ScrollBar arrow buttons can be non-square when
                // the ScrollBar.squareButtons property is set to FALSE
                // and the ScrollBar.buttonSize property is non-null
                dim = (Dimension) context.getStyle().get(context, "ScrollBar.buttonSize");
            }

            if (dim == null) {

                // For all other cases (including Spinner, ComboBox), we will
                // fall back on the single ArrowButton.size value to create
                // a square return value
                int size = context.getStyle().getInt(context, "ArrowButton.size", 16);

                dim = new Dimension(size, size);
            }

            // handle scaling for sizeVarients for special case components. The
            // key "JComponent.sizeVariant" scales for large/small/mini
            // components are based on Apples LAF
            JComponent parent = (JComponent) context.getComponent().getParent();

            if (parent != null && !(parent instanceof JComboBox)) {
                String scaleKey = SeaGlassStyle.getSizeVariant(parent);
                if (scaleKey != null) {
                    if (SeaGlassStyle.LARGE_KEY.equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 1.15), (int) (dim.height * 1.15));
                    } else if (SeaGlassStyle.SMALL_KEY.equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 0.857), (int) (dim.height * 0.857));
                    } else if (SeaGlassStyle.MINI_KEY.equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 0.714), (int) (dim.height * 0.714));
                    }
                }
            }

            context.dispose();

            return dim;
        }
    }
}
