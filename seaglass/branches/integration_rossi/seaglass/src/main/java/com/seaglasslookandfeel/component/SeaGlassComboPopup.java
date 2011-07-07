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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * SeaGlassComboPopup. The code to calculate the popup bounds was lifted from
 * Quaqua by Werner Randelshofer.
 *
 * @see javax.swing.plaf.synth.SynthComboPopup
 * @see ch.randelshofer.quaqua.QuaquaComboPopup
 */
public class SeaGlassComboPopup extends BasicComboPopup {
    private static final long serialVersionUID = -2424496840321170560L;

    private static final int LEFT_SHIFT = 5;

    private Color BORDER_COLOR = UIManager.getColor("seaGlassPopupBorder");

    private Border LIST_BORDER = new LineBorder(BORDER_COLOR, 1);

    /**
     * Creates a new SeaGlassComboPopup object.
     *
     * @param combo the combo box.
     */
    public SeaGlassComboPopup(JComboBox combo) {
        super(combo);
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI() {
        // TODO Auto-generated method stub
        super.updateUI();
    }



    /**
     * Is the combo box popup a "drop down" popup, or does it overlay the box?
     *
     * @return {@code true} if the popup should drop down from the combo box,
     *         {@code false} if it should overlay the combo box.
     */
    private boolean isDropDown() {
        return comboBox.isEditable() || hasScrollBars();
    }

    /**
     * Does the combo box have scroll bars?
     *
     * @return {@code true} if the combo box popup has scroll bars,
     *         {@code false} otherwise.
     */
    private boolean hasScrollBars() {
        return comboBox.getModel().getSize() > getMaximumRowCount();
    }

    /**
     * Is the combo box editable?
     *
     * @return {@code true} if the combo box popup is editable, {@code false}
     *         otherwise.
     */
    private boolean isEditable() {
        return comboBox.isEditable();
    }

    /**
     * Configures the list which is used to hold the combo box items in the
     * popup. This method is called when the UI class is created.
     */
    @Override
    protected void configureList() {
        list.setFont(comboBox.getFont());
        list.setBorder(null);
        list.setCellRenderer(comboBox.getRenderer());
        list.setFocusable(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int selectedIndex = comboBox.getSelectedIndex();

        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }

        installListListeners();
    }

    /**
     * Configures the popup portion of the combo box. This method is called when
     * the UI class is created.
     */
    protected void configurePopup() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorderPainted(true);
        setBorder(LIST_BORDER);
        setOpaque(false);
        add(scroller);
        setDoubleBuffered(true);
        setFocusable(false);
    }

    /**
     * Calculate the placement and size of the popup portion of the combo box
     * based on the combo box location and the enclosing screen bounds. If no
     * transformations are required, then the returned rectangle will have the
     * same values as the parameters.
     *
     * @param  px starting x location
     * @param  py starting y location
     * @param  pw starting width
     * @param  ph starting height
     *
     * @return a rectangle which represents the placement and size of the popup
     */
    protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
        Toolkit   toolkit      = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;
        int       listWidth    = getList().getPreferredSize().width;
        Insets    margin       = comboBox.getInsets();

        if (hasScrollBars()) {
            px += margin.left;
            pw = Math.max(pw - margin.left - margin.right, listWidth + 16);
        } else {
            px += margin.left;
            pw = Math.max(pw - LEFT_SHIFT - margin.left, listWidth);
        }

        // Calculate the desktop dimensions relative to the combo box.
        GraphicsConfiguration gc = comboBox.getGraphicsConfiguration();
        Point                 p  = new Point();

        SwingUtilities.convertPointFromScreen(p, comboBox);

        if (gc == null) {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        } else {

            // Get the screen insets.
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

            screenBounds        =  new Rectangle(gc.getBounds());
            screenBounds.width  -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x      += screenInsets.left;
            screenBounds.y      += screenInsets.top;
        }

        if (isDropDown()) {

            if (isEditable()) {
                py -= margin.bottom + 2;
            } else {
                py -= margin.bottom;
            }
        } else {
            int yOffset       = -margin.top;
            int selectedIndex = comboBox.getSelectedIndex();

            if (selectedIndex <= 0) {
                py = -yOffset;
            } else {
                py = -yOffset - list.getCellBounds(0, selectedIndex - 1).height;

            }
        }

        // Compute the rectangle for the popup menu
        Rectangle rect = new Rectangle(px, Math.max(py, p.y + screenBounds.y), Math.min(screenBounds.width, pw),
                                       Math.min(screenBounds.height - 40, ph));

        // Add the preferred scroll bar width, if the popup does not fit
        // on the available rectangle.
        if (rect.height < ph) {
            rect.width += 16;
        }

        return rect;
    }

    /**
     * Sets the list selection index to the selectedIndex. This method is used
     * to synchronize the list selection with the combo box selection.
     *
     * @param selectedIndex the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }
    }

    /**
     * Implementation of ComboPopup.show().
     */
    public void show() {
        setListSelection(comboBox.getSelectedIndex());

        Point location = getPopupLocation();

        show(comboBox, location.x, location.y);

        // For some reason, this is needed to clear the old selection being
        // displayed. Calling list.clearSelection() doesn't cut it.
        list.repaint();
    }

    /**
     * Get the maximum row count to display. If none is set, return 100.
     *
     * @return the maximum row count to display.
     */
    private int getMaximumRowCount() {
       // return isEditable() ? comboBox.getMaximumRowCount() : 100;
        return comboBox.getMaximumRowCount();
    }

    /**
     * Calculates the upper left location of the Popup.
     *
     * @return the Point representing the upper-left coordinate of the Popup.
     */
    private Point getPopupLocation() {
        Dimension popupSize = comboBox.getSize();
        Insets    insets    = getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the combo box.
        popupSize.setSize(popupSize.width - (insets.right + insets.left), getPopupHeightForRowCount(getMaximumRowCount()));
        Rectangle popupBounds   = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, popupSize.height);
        Dimension scrollSize    = popupBounds.getSize();
        Point     popupLocation = popupBounds.getLocation();

        scroller.setMaximumSize(scrollSize);
        scroller.setPreferredSize(scrollSize);
        scroller.setMinimumSize(scrollSize);

        list.revalidate();

        return popupLocation;
    }
}
