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

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassBorder;

import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.SynthUI;

/**
 * SeaGlassTableHeaderUI implementation
 * 
 * Based on SynthTableHeaderUI, which is package local.
 * 
 * @see javax.swing.plaf.synth.SynthTableHeaderUI
 */
public class SeaGlassTableHeaderUI extends BasicTableHeaderUI implements PropertyChangeListener, SynthUI {

    private TableCellRenderer prevRenderer = null;

    private SynthStyle        style;

    public static ComponentUI createUI(JComponent h) {
        return new SeaGlassTableHeaderUI();
    }

    protected void installDefaults() {
        prevRenderer = header.getDefaultRenderer();
        if (prevRenderer instanceof UIResource) {
            header.setDefaultRenderer(new HeaderRenderer());
        }
        updateStyle(header);
    }

    private void updateStyle(JTableHeader c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    protected void installListeners() {
        super.installListeners();
        header.addPropertyChangeListener(this);
    }

    protected void uninstallDefaults() {
        if (header.getDefaultRenderer() instanceof HeaderRenderer) {
            header.setDefaultRenderer(prevRenderer);
        }

        SeaGlassContext context = getContext(header, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    protected void uninstallListeners() {
        header.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTableHeaderBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        super.paint(g, context.getComponent());
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTableHeaderBorder(context, g, x, y, w, h);
    }

    //
    // SynthUI
    //
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SynthLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTableHeader) evt.getSource());
        }
    }

    @Override
    protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
        header.repaint(header.getHeaderRect(oldColumn));
        header.repaint(header.getHeaderRect(newColumn));
    }

    public static class HeaderRenderer extends DefaultTableCellHeaderRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.LEADING);
            setName("TableHeader.renderer");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            boolean hasRollover = false;// (column == getRolloverColumn());
            if (isSelected || hasRollover || hasFocus) {
                SeaGlassLookAndFeel.setSelectedUI((SeaGlassLabelUI) SeaGlassLookAndFeel.getUIOfType(getUI(), SeaGlassLabelUI.class),
                    isSelected, hasFocus, table.isEnabled(), hasRollover);
            } else {
                SeaGlassLookAndFeel.resetSelectedUI();
            }

            // Stuff a variable into the client property of this renderer
            // indicating the sort order, so that different rendering can be
            // done for the header based on sorted state.
            RowSorter rs = table == null ? null : table.getRowSorter();
            java.util.List<? extends RowSorter.SortKey> sortKeys = rs == null ? null : rs.getSortKeys();
            if (sortKeys != null && sortKeys.size() > 0 && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                switch (sortKeys.get(0).getSortOrder()) {
                case ASCENDING:
                    putClientProperty("Table.sortOrder", "ASCENDING");
                    break;
                case DESCENDING:
                    putClientProperty("Table.sortOrder", "DESCENDING");
                    break;
                case UNSORTED:
                    putClientProperty("Table.sortOrder", "UNSORTED");
                    break;
                default:
                    throw new AssertionError("Cannot happen");
                }
            } else {
                putClientProperty("Table.sortOrder", "UNSORTED");
            }

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            return this;
        }

        @Override
        public void setBorder(Border border) {
            if (border instanceof SeaGlassBorder) {
                super.setBorder(border);
            }
        }
    }

    public static class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer implements UIResource {
        private boolean   horizontalTextPositionSet;
        private Icon      sortArrow;
        private EmptyIcon emptyIcon = new EmptyIcon();

        public DefaultTableCellHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        public void setHorizontalTextPosition(int textPosition) {
            horizontalTextPositionSet = true;
            super.setHorizontalTextPosition(textPosition);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Icon sortIcon = null;

            boolean isPaintingForPrint = false;

            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    Color fgColor = null;
                    Color bgColor = null;
                    if (hasFocus) {
                        fgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellForeground");
                        bgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellBackground");
                    }
                    if (fgColor == null) {
                        fgColor = header.getForeground();
                    }
                    if (bgColor == null) {
                        bgColor = header.getBackground();
                    }
                    setForeground(fgColor);
                    setBackground(bgColor);

                    setFont(header.getFont());

                    isPaintingForPrint = header.isPaintingForPrint();
                }

                if (!isPaintingForPrint && table.getRowSorter() != null) {
                    if (!horizontalTextPositionSet) {
                        // There is a row sorter, and the developer hasn't
                        // set a text position, change to leading.
                        setHorizontalTextPosition(JLabel.LEADING);
                    }
                    SortOrder sortOrder = getColumnSortOrder(table, column);
                    if (sortOrder != null) {
                        switch (sortOrder) {
                        case ASCENDING:
                            sortIcon = DefaultLookup.getIcon(this, ui, "Table.ascendingSortIcon");
                            break;
                        case DESCENDING:
                            sortIcon = DefaultLookup.getIcon(this, ui, "Table.descendingSortIcon");
                            break;
                        case UNSORTED:
                            sortIcon = DefaultLookup.getIcon(this, ui, "Table.naturalSortIcon");
                            break;
                        }
                    }
                }
            }

            setText(value == null ? "" : value.toString());
            setIcon(sortIcon);
            sortArrow = sortIcon;

            Border border = null;
            if (hasFocus) {
                border = DefaultLookup.getBorder(this, ui, "TableHeader.focusCellBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "TableHeader.cellBorder");
            }
            setBorder(border);

            return this;
        }

        public static SortOrder getColumnSortOrder(JTable table, int column) {
            SortOrder rv = null;
            if (table == null || table.getRowSorter() == null) {
                return rv;
            }
            java.util.List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
            if (sortKeys.size() > 0 && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                rv = sortKeys.get(0).getSortOrder();
            }
            return rv;
        }

        @Override
        public void paintComponent(Graphics g) {
            boolean b = DefaultLookup.getBoolean(this, ui, "TableHeader.rightAlignSortArrow", false);
            if (b && sortArrow != null) {
                // emptyIcon is used so that if the text in the header is right
                // aligned, or if the column is too narrow, then the text will
                // be sized appropriately to make room for the icon that is
                // about
                // to be painted manually here.
                emptyIcon.width = sortArrow.getIconWidth();
                emptyIcon.height = sortArrow.getIconHeight();
                setIcon(emptyIcon);
                super.paintComponent(g);
                Point position = computeIconPosition(g);
                sortArrow.paintIcon(this, g, position.x, position.y);
            } else {
                super.paintComponent(g);
            }
        }

        private Point computeIconPosition(Graphics g) {
            FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle viewR = new Rectangle();
            Rectangle textR = new Rectangle();
            Rectangle iconR = new Rectangle();
            Insets i = getInsets();
            viewR.x = i.left;
            viewR.y = i.top;
            viewR.width = getWidth() - (i.left + i.right);
            viewR.height = getHeight() - (i.top + i.bottom);
            SwingUtilities.layoutCompoundLabel(this, fontMetrics, getText(), sortArrow, getVerticalAlignment(), getHorizontalAlignment(),
                getVerticalTextPosition(), getHorizontalTextPosition(), viewR, iconR, textR, getIconTextGap());
            int x = getWidth() - i.right - sortArrow.getIconWidth();
            int y = iconR.y;
            return new Point(x, y);
        }

        private class EmptyIcon implements Icon, Serializable {
            int width  = 0;
            int height = 0;

            public void paintIcon(Component c, Graphics g, int x, int y) {
            }

            public int getIconWidth() {
                return width;
            }

            public int getIconHeight() {
                return height;
            }
        }
    }
}
