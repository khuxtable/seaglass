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
package com.seaglass.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.seaglass.SeaGlassBorder;
import com.seaglass.SeaGlassContext;
import com.seaglass.SeaGlassLookAndFeel;

import sun.swing.plaf.synth.SynthUI;
import sun.swing.table.DefaultTableCellHeaderRenderer;

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

    private class HeaderRenderer extends DefaultTableCellHeaderRenderer {
        HeaderRenderer() {
            setHorizontalAlignment(JLabel.LEADING);
            setName("TableHeader.renderer");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {

            boolean hasRollover = (column == getRolloverColumn());
            if (isSelected || hasRollover || hasFocus) {
                SeaGlassLookAndFeel.setSelectedUI(
                    (SeaGlassLabelUI) SeaGlassLookAndFeel.getUIOfType(getUI(), SeaGlassLabelUI.class), isSelected, hasFocus, table
                        .isEnabled(), hasRollover);
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
}
