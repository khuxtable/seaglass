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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.UIResource;

/**
 * JButton object that draws a scaled Arrow in one of the cardinal directions.
 * 
 * Based on SynthArrowButton by Scott Violet.
 * 
 * @see javax.swing.plaf.synth.SynthArrowButton
 */
public class SeaGlassArrowButton extends JButton implements SwingConstants, UIResource {
    private int direction;

    public SeaGlassArrowButton(int direction) {
        super();
        super.setFocusable(false);
        setDirection(direction);
        setDefaultCapable(false);
    }

    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    public void updateUI() {
        setUI(new SeaGlassArrowButtonUI());
    }

    public void setDirection(int dir) {
        direction = dir;
        putClientProperty("__arrow_direction__", new Integer(dir));
        repaint();
    }

    public int getDirection() {
        return direction;
    }

    public void setFocusable(boolean focusable) {
    }

    private static class SeaGlassArrowButtonUI extends SeaGlassButtonUI {
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            updateStyle(b);
        }

        protected void paint(SeaGlassContext context, Graphics g) {
            SeaGlassArrowButton button = (SeaGlassArrowButton) context.getComponent();
            context.getPainter().paintArrowButtonForeground(context, g, 0, 0, button.getWidth(), button.getHeight(),
                button.getDirection());
        }

        void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
            context.getPainter().paintArrowButtonBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        }

        public void paintBorder(SeaGlassContext context, Graphics g, int x, int y, int w, int h) {
            context.getPainter().paintArrowButtonBorder(context, g, x, y, w, h);
        }

        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public Dimension getPreferredSize(JComponent c) {
            SeaGlassContext context = getContext(c);
            Dimension dim = null;
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
                String scaleKey = (String) parent.getClientProperty("JComponent.sizeVariant");
                if (scaleKey != null) {
                    if ("large".equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 1.15), (int) (dim.height * 1.15));
                    } else if ("small".equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 0.857), (int) (dim.height * 0.857));
                    } else if ("mini".equals(scaleKey)) {
                        dim = new Dimension((int) (dim.width * 0.714), (int) (dim.height * 0.714));
                    }
                }
            }

            context.dispose();
            return dim;
        }
    }
}
