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

import sun.swing.DefaultLookup;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassBorder;

/**
 * SeaGlassTableHeaderUI implementation
 *
 * <p>Based on SynthTableHeaderUI, which is package local.</p>
 *
 * @see javax.swing.plaf.synth.SynthTableHeaderUI
 */
public class SeaGlassTableHeaderUI extends BasicTableHeaderUI implements PropertyChangeListener, SeaglassUI {

    private TableCellRenderer prevRenderer = null;

    private SynthStyle style;

    /**
     * DOCUMENT ME!
     *
     * @param  h DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComponentUI createUI(JComponent h) {
        return new SeaGlassTableHeaderUI();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#installDefaults()
     */
    protected void installDefaults() {
        prevRenderer = header.getDefaultRenderer();
        if (prevRenderer instanceof UIResource) {
            header.setDefaultRenderer(new HeaderRenderer());
        }

        updateStyle(header);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    private void updateStyle(JTableHeader c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }

        context.dispose();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#installListeners()
     */
    protected void installListeners() {
        super.installListeners();
        header.addPropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        if (header.getDefaultRenderer() instanceof HeaderRenderer) {
            header.setDefaultRenderer(prevRenderer);
        }

        SeaGlassContext context = getContext(header, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#uninstallListeners()
     */
    protected void uninstallListeners() {
        header.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTableHeaderBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        super.paint(g, context.getComponent());
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTableHeaderBorder(context, g, x, y, w, h);
    }

    //
    // SynthUI
    //
    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  state DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SynthLookAndFeel.getRegion(c), style, state);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTableHeader) evt.getSource());
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableHeaderUI#rolloverColumnUpdated(int, int)
     */
    @Override
    protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
        header.repaint(header.getHeaderRect(oldColumn));
        header.repaint(header.getHeaderRect(newColumn));
    }

    /**
     * DOCUMENT ME!
     */
    public static class HeaderRenderer extends DefaultTableCellHeaderRenderer {
        private static final long serialVersionUID = 3595483618538272322L;

        /**
         * Creates a new HeaderRenderer object.
         */
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.LEADING);
            setName("TableHeader.renderer");
        }

        /**
         * @see com.seaglasslookandfeel.ui.SeaGlassTableHeaderUI$DefaultTableCellHeaderRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            boolean hasRollover = false; // (column == getRolloverColumn());

            if (isSelected || hasRollover || hasFocus) {
                SeaGlassLookAndFeel.setSelectedUI((SeaGlassLabelUI) SeaGlassLookAndFeel.getUIOfType(getUI(), SeaGlassLabelUI.class),
                                                  isSelected, hasFocus, table.isEnabled(), hasRollover);
            } else {
                SeaGlassLookAndFeel.resetSelectedUI();
            }

            // Stuff a variable into the client property of this renderer
            // indicating the sort order, so that different rendering can be
            // done for the header based on sorted state.
            RowSorter                                   rs       = table == null ? null : table.getRowSorter();
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

        /**
         * @see javax.swing.JComponent#setBorder(javax.swing.border.Border)
         */
        @Override
        public void setBorder(Border border) {
            if (border instanceof SeaGlassBorder) {
                super.setBorder(border);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer implements UIResource {
        private static final long serialVersionUID = -4466195868054511962L;
        private Icon              sortArrow;
        private EmptyIcon         emptyIcon        = new EmptyIcon();

        /**
         * Creates a new DefaultTableCellHeaderRenderer object.
         */
        public DefaultTableCellHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        /**
         * @see javax.swing.JLabel#setHorizontalTextPosition(int)
         */
        public void setHorizontalTextPosition(int textPosition) {
            super.setHorizontalTextPosition(textPosition);
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
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

            if (value instanceof Icon) {
                setText("");
                setIcon((Icon) value);
            } else if (value instanceof JLabel) {
                JLabel label = (JLabel) value;

                setText(label.getText());
                setIcon(label.getIcon());
                setHorizontalAlignment(label.getHorizontalAlignment());
                setHorizontalTextPosition(label.getHorizontalTextPosition());
                setVerticalAlignment(label.getVerticalAlignment());
                setVerticalTextPosition(label.getVerticalTextPosition());
                setToolTipText(label.getToolTipText());
                
            } else {
                setText(value == null ? "" : value.toString());
                setIcon(null);
            }

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

        /**
         * DOCUMENT ME!
         *
         * @param  table  DOCUMENT ME!
         * @param  column DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
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

        /**
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        public void paintComponent(Graphics g) {
            if (sortArrow != null) {
                // emptyIcon is used so that if the text in the header is right
                // aligned, or if the column is too narrow, then the text will
                // be sized appropriately to make room for the icon that is
                // about to be painted manually here.
                emptyIcon.width  = sortArrow.getIconWidth();
                emptyIcon.height = sortArrow.getIconHeight();
                Point position = computeIconPosition(g);

                super.paintComponent(g);
                sortArrow.paintIcon(this, g, position.x, position.y);
            } else {
                super.paintComponent(g);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  g DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Point computeIconPosition(Graphics g) {
            FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle   viewR       = new Rectangle();
            Rectangle   textR       = new Rectangle();
            Rectangle   iconR       = new Rectangle();
            Insets      i           = getInsets();

            viewR.x      = i.left;
            viewR.y      = i.top;
            viewR.width  = getWidth() - (i.left + i.right);
            viewR.height = getHeight() - (i.top + i.bottom);
            SwingUtilities.layoutCompoundLabel(this, fontMetrics, getText(), sortArrow, getVerticalAlignment(), getHorizontalAlignment(),
                                               getVerticalTextPosition(), getHorizontalTextPosition(), viewR, iconR, textR,
                                               getIconTextGap());
            int x = getComponentOrientation().isLeftToRight() ? getWidth() - i.right - sortArrow.getIconWidth() : i.left;
            int y = iconR.y;

            return new Point(x, y);
        }

        /**
         * DOCUMENT ME!
         */
        private class EmptyIcon implements Icon, Serializable {
            private static final long serialVersionUID = -821523476678771032L;
            int                       width            = 0;
            int                       height           = 0;

            /**
             * @see javax.swing.Icon#paintIcon(java.awt.Component,java.awt.Graphics,
             *      int, int)
             */
            public void paintIcon(Component c, Graphics g, int x, int y) {
            }

            /**
             * @see javax.swing.Icon#getIconWidth()
             */
            public int getIconWidth() {
                return width;
            }

            /**
             * @see javax.swing.Icon#getIconHeight()
             */
            public int getIconHeight() {
                return height;
            }
        }
    }
}
