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
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * SeaGlassComboPopup.
 */
class SeaGlassComboPopup extends BasicComboPopup {

    private static final int               LEFT_SHIFT                     = 5;

    private ComboBoxVerticalCenterProvider comboBoxVerticalCenterProvider = new DefaultVerticalCenterProvider();

    public SeaGlassComboPopup(JComboBox combo) {
        super(combo);
    }

    /**
     * Configures the list which is used to hold the combo box items in the
     * popup. This method is called when the UI class is created.
     * 
     * @see #createList
     */
    @Override
    protected void configureList() {
        list.setFont(comboBox.getFont());

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
     * @inheritDoc Overridden to take into account any popup insets specified in
     *             SeaGlassComboBoxUI
     */
    @Override
    protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
        ComboBoxUI ui = comboBox.getUI();
        if (ui instanceof SeaGlassComboBoxUI) {
            SeaGlassComboBoxUI sui = (SeaGlassComboBoxUI) ui;
            if (sui.popupInsets != null) {
                Insets i = sui.popupInsets;
                px += i.left;
                py += i.top;
                pw -= i.left - i.right;
                ph -= i.top - i.bottom;
            }
        }
        return computeInitialPopupBounds(px, py, pw, ph);
    }

    /**
     * Calculate the placement and size of the popup portion of the combo box
     * based on the combo box location and the enclosing screen bounds. If no
     * transformations are required, then the returned rectangle will have the
     * same values as the parameters.
     * 
     * @param px
     *            starting x location
     * @param py
     *            starting y location
     * @param pw
     *            starting width
     * @param ph
     *            starting height
     * @return a rectangle which represents the placement and size of the popup
     */
    private Rectangle computeInitialPopupBounds(int px, int py, int pw, int ph) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;

        // Reduce the width to match the width of the button.
        pw -= LEFT_SHIFT;
        int selectedItemIndex = list.getSelectedIndex();
        int componentCenter = comboBoxVerticalCenterProvider.provideCenter(comboBox);
        System.out.println("index = " + selectedItemIndex + ", count = " + list.getComponentCount());
        int menuItemHeight = 0;
        if (selectedItemIndex >= 0) {
            menuItemHeight = list.getCellRenderer().getListCellRendererComponent(list,
                list.getModel().getElementAt(selectedItemIndex), selectedItemIndex, true, true).getPreferredSize().height;
        }
        int menuItemCenter = getInsets().top + (selectedItemIndex * menuItemHeight) + menuItemHeight / 2;
        py = componentCenter - menuItemCenter;

        // Calculate the desktop dimensions relative to the combo box.
        GraphicsConfiguration gc = comboBox.getGraphicsConfiguration();
        Point p = new Point();
        SwingUtilities.convertPointFromScreen(p, comboBox);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x += (p.x + screenInsets.left);
            screenBounds.y += (p.y + screenInsets.top);
        } else {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        }

        Rectangle rect = new Rectangle(px, py, pw, ph);
        if (py + ph > screenBounds.y + screenBounds.height && ph < screenBounds.height) {
            rect.y = -rect.height;
        }
        return rect;
    }

    public void setVerticalComponentCenterProvider(ComboBoxVerticalCenterProvider comboBoxVerticalCenterProvider) {
        if (comboBoxVerticalCenterProvider == null) {
            throw new IllegalArgumentException("The given CompnonentCenterProvider cannot be null.");
        }
        this.comboBoxVerticalCenterProvider = comboBoxVerticalCenterProvider;
    }

    /**
     * Sets the list selection index to the selectedIndex. This method is used
     * to synchronize the list selection with the combo box selection.
     * 
     * @param selectedIndex
     *            the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }
    }

    // public void show() {
    // clearAndFillMenu();
    // // if there are combo box items, then show the popup menu.
    // if (comboBox.getModel().getSize() > 0) {
    // // Point popupLocation = placePopupOnScreen();
    // Rectangle popupBounds = calculateInitialPopupBounds();
    //
    // // fPopupMenu.show(fComboBox, popupLocation.x, popupLocation.y);
    // show(comboBox, popupBounds.x, popupBounds.y);
    // forceCorrectPopupSelectionIfNeccessary();
    // }
    // }

    /**
     * Implementation of ComboPopup.show().
     */
    public void show() {
        list.setModel(comboBox.getModel());
        configureList();
        setListSelection(comboBox.getSelectedIndex());

        Point location = getPopupLocation();
        show(comboBox, location.x, location.y);
    }

    /**
     * Calculates the upper left location of the Popup.
     */
    private Point getPopupLocation() {
        Dimension popupSize = comboBox.getSize();
        Insets insets = getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the combo box.
        popupSize.setSize(popupSize.width - (insets.right + insets.left), getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
        Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, popupSize.height);
        Dimension scrollSize = popupBounds.getSize();
        Point popupLocation = popupBounds.getLocation();

        scroller.setMaximumSize(scrollSize);
        scroller.setPreferredSize(scrollSize);
        scroller.setMinimumSize(scrollSize);

        list.revalidate();

        return popupLocation;
    }

    // An interface to allow a third-party to provide the center of a given
    // compoennt. ////////////

    public interface ComboBoxVerticalCenterProvider {
        int provideCenter(JComboBox comboBox);
    }

    // A default implementation of ComboBoxVerticalCenterProvider.
    // ////////////////////////////////

    private static class DefaultVerticalCenterProvider implements ComboBoxVerticalCenterProvider {
        public int provideCenter(JComboBox comboBox) {
            return comboBox.getHeight() / 2;
        }
    }
}
