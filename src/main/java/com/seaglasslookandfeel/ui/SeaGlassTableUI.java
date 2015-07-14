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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.component.SeaGlassBorder;
import com.seaglasslookandfeel.painter.ViewportPainter;
import com.seaglasslookandfeel.util.WindowUtils;

/**
 * SeaGlassTableUI implementation.
 *
 * <p>Based on SynthTableUI, which is package local.</p>
 *
 * @see javax.swing.plaf.synth.SynthTableUI
 */
public class SeaGlassTableUI extends BasicTableUI implements SeaglassUI, PropertyChangeListener, ViewportPainter {

    private static final CellRendererPane CELL_RENDER_PANE = new CellRendererPane();

    //
    // Instance Variables
    //

    private SynthStyle                    style;

    private boolean useTableColors;
    private boolean useUIBorder;
    // The background color to use for cells for alternate cells.
    private Color   alternateColor;

    private Color             selectionActiveBottomBorderColor;
    private Color             selectionInactiveBottomBorderColor;
    private Color             transparentColor;

    // TableCellRenderer installed on the JTable at the time we're installed,
    // cached so that we can reinstall them at uninstallUI time.
    private TableCellRenderer dateRenderer;
    private TableCellRenderer numberRenderer;
    private TableCellRenderer doubleRender;
    private TableCellRenderer floatRenderer;
    private TableCellRenderer iconRenderer;
    private TableCellRenderer imageIconRenderer;
    private TableCellRenderer booleanRenderer;
    private TableCellRenderer objectRenderer;

    /**
     * The installation/uninstall procedures and support
     *
     * @param  c the component.
     *
     * @return the UI delegate.
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTableUI();
    }

    /**
     * Initialize JTable properties, e.g. font, foreground, and background. The
     * font, foreground, and background properties are only set if their current
     * value is either null or a UIResource, other properties are set if the
     * current value is null.
     *
     * @see #installUI
     */
    protected void installDefaults() {
        dateRenderer      = installRendererIfPossible(Date.class, null);
        numberRenderer    = installRendererIfPossible(Number.class, null);
        doubleRender      = installRendererIfPossible(Double.class, null);
        floatRenderer     = installRendererIfPossible(Float.class, null);
        iconRenderer      = installRendererIfPossible(Icon.class, null);
        imageIconRenderer = installRendererIfPossible(ImageIcon.class, null);
        booleanRenderer   = installRendererIfPossible(Boolean.class, new SeaGlassBooleanTableCellRenderer());
        objectRenderer    = installRendererIfPossible(Object.class, new SeaGlassTableCellRenderer());

        updateStyle(table);
    }

    /**
     * Installs a renderer if the existing renderer is an instance of
     * UIResource.
     *
     * @param  objectClass the class for which to install the renderer.
     * @param  renderer    the renderer instance.
     *
     * @return the previous renderer.
     */
    private TableCellRenderer installRendererIfPossible(Class objectClass, TableCellRenderer renderer) {
        TableCellRenderer currentRenderer = table.getDefaultRenderer(objectClass);

        if (currentRenderer instanceof UIResource) {
            table.setDefaultRenderer(objectClass, renderer);
        }

        return currentRenderer;
    }

    /**
     * Static wrapper around {@link forceInstallRenderer(Class objectClass)}.
     *
     * @param table       the table to install the renderer on.
     * @param objectClass the class to install the renderer on.
     */
    public static void forceInstallRenderer(JTable table, Class objectClass) {
        if (table.getUI() instanceof SeaGlassTableUI) {
            ((SeaGlassTableUI) table.getUI()).forceInstallRenderer(objectClass);
        }
    }

    /**
     * Force the installation of the appropriate Sea Glass Renderer. We don't
     * need to save the old renderer, because in principle that has already been
     * done in {@link installDefaults()}.
     *
     * @param objectClass the object class to install the renderer on.
     */
    public void forceInstallRenderer(Class objectClass) {
        if (objectClass == Date.class) {
            table.setDefaultRenderer(Date.class, null);
        } else if (objectClass == Number.class) {
            table.setDefaultRenderer(Number.class, null);
        } else if (objectClass == Float.class) {
            table.setDefaultRenderer(Float.class, null);
        } else if (objectClass == Icon.class) {
            table.setDefaultRenderer(Icon.class, null);
        } else if (objectClass == ImageIcon.class) {
            table.setDefaultRenderer(ImageIcon.class, null);
        } else if (objectClass == Boolean.class) {
            table.setDefaultRenderer(Boolean.class, new SeaGlassBooleanTableCellRenderer());
        } else if (objectClass == Object.class) {
            table.setDefaultRenderer(Object.class, new SeaGlassTableCellRenderer());
        }
    }

    /**
     * Update the style.
     *
     * @param c the component.
     */
    private void updateStyle(JTable c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        SynthStyle s = SeaGlassLookAndFeel.updateStyle(context, this);
        if (s instanceof SeaGlassStyle) {
            style = (SeaGlassStyle) s;

            selectionActiveBottomBorderColor   = UIManager.getColor("seaGlassTableSelectionActiveBottom");
            selectionInactiveBottomBorderColor = UIManager.getColor("seaGlassTableSelectionInactiveBottom");
            transparentColor                   = UIManager.getColor("seaGlassTransparent");

            if (style != oldStyle) {
                table.remove(rendererPane);
                rendererPane = createCustomCellRendererPane();
                table.add(rendererPane);

                context.setComponentState(ENABLED | SELECTED);

                Color sbg = table.getSelectionBackground();

                if (sbg == null || sbg instanceof UIResource) {
                    table.setSelectionBackground(style.getColor(context, ColorType.TEXT_BACKGROUND));
                }

                Color sfg = table.getSelectionForeground();

                if (sfg == null || sfg instanceof UIResource) {
                    table.setSelectionForeground(style.getColor(context, ColorType.TEXT_FOREGROUND));
                }

                context.setComponentState(ENABLED);

                Color gridColor = table.getGridColor();

                if (gridColor == null || gridColor instanceof UIResource) {
                    gridColor = (Color) style.get(context, "Table.gridColor");
                    if (gridColor == null) {
                        gridColor = style.getColor(context, ColorType.FOREGROUND);
                    }

                    table.setGridColor(gridColor);
                }

                useTableColors = style.getBoolean(context, "Table.rendererUseTableColors", true);
                useUIBorder    = style.getBoolean(context, "Table.rendererUseUIBorder", true);

                Object rowHeight = style.get(context, "Table.rowHeight");

                if (rowHeight != null) {
                    LookAndFeel.installProperty(table, "rowHeight", rowHeight);
                }

                boolean showGrid = style.getBoolean(context, "Table.showGrid", true);

                if (!showGrid) {
                    table.setShowGrid(false);
                }

                Dimension d = table.getIntercellSpacing();
                // if (d == null || d instanceof UIResource) {
                if (d != null) {
                    d = (Dimension) style.get(context, "Table.intercellSpacing");
                }

                alternateColor = (Color) style.get(context, "Table.alternateRowColor");
                if (d != null) {
                    table.setIntercellSpacing(d);
                }

                table.setOpaque(false);

                if (alternateColor != null) {
                    table.setShowHorizontalLines(false);
                }

                // Make header column extend the width of the viewport (if there is
                // a viewport).
                table.getTableHeader().setBorder(createTableHeaderEmptyColumnPainter(table));
                setViewPortListeners(table);

                if (oldStyle != null) {
                    uninstallKeyboardActions();
                    installKeyboardActions();
                }
            }
        }
        
        context.dispose();
    }

    /**
     * Attaches listeners to the JTable.
     */
    protected void installListeners() {
        super.installListeners();
        table.addPropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableUI#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        table.setDefaultRenderer(Date.class, dateRenderer);
        table.setDefaultRenderer(Number.class, numberRenderer);
        table.setDefaultRenderer(Double.class, doubleRender);
        table.setDefaultRenderer(Float.class, floatRenderer);
        table.setDefaultRenderer(Icon.class, iconRenderer);
        table.setDefaultRenderer(ImageIcon.class, imageIconRenderer);
        table.setDefaultRenderer(Boolean.class, booleanRenderer);
        table.setDefaultRenderer(Object.class, objectRenderer);

        if (table.getTransferHandler() instanceof UIResource) {
            table.setTransferHandler(null);
        }

        SeaGlassContext context = getContext(table, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableUI#uninstallListeners()
     */
    protected void uninstallListeners() {
        table.removePropertyChangeListener(this);
        super.uninstallListeners();
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
     * Get the Synth context.
     *
     * @param  c     the component.
     * @param  state the state.
     *
     * @return the Synth context.
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SynthLookAndFeel.getRegion(c), style, state);
    }

    /**
     * Get the component state.
     *
     * @param  c the component.
     *
     * @return the state.
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    //
    // Paint methods and support
    //

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics,
     *      javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintTableBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTableBorder(context, g, x, y, w, h);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTableUI#paint(java.awt.Graphics,
     *      javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * Paint the component.
     *
     * @param context the Synth context.
     * @param g       the Graphics context.
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        Rectangle clip = g.getClipBounds();

        Rectangle bounds = table.getBounds();
        // Account for the fact that the graphics has already been translated
        // into the table's bounds.
        bounds.x = bounds.y = 0;

        // This check prevents us from painting the entire table when the clip
        // doesn't intersect our bounds at all.
        if (table.getRowCount() <= 0 || table.getColumnCount() <= 0 || !bounds.intersects(clip)) {
            paintDropLines(context, g);
            return;
        }

        boolean ltr = table.getComponentOrientation().isLeftToRight();

        Point upperLeft = clip.getLocation();

        if (!ltr) {
            upperLeft.x++;
        }

        Point lowerRight = new Point(clip.x + clip.width - (ltr ? 1 : 0), clip.y + clip.height);

        int rMin = table.rowAtPoint(upperLeft);
        int rMax = table.rowAtPoint(lowerRight);
        // This should never happen (as long as our bounds intersect the clip,
        // which is why we bail above if that is the case).
        if (rMin == -1) {
            rMin = 0;
        }
        // If the table does not have enough rows to fill the view we'll get -1.
        // (We could also get -1 if our bounds don't intersect the clip,
        // which is why we bail above if that is the case).
        // Replace this with the index of the last row.
        if (rMax == -1) {
            rMax = table.getRowCount() - 1;
        }

        int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
        int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get
        // -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = table.getColumnCount() - 1;
        }

        // Paint the grid.
        if (!(table.getParent() instanceof JViewport)
                || (table.getParent() != null && !(table.getParent().getParent() instanceof JScrollPane))) {
            // FIXME We need to not repaint the entire table any time something
            // changes.
            // paintGrid(context, g, rMin, rMax, cMin, cMax);
            paintStripesAndGrid(context, g, table, table.getWidth(), table.getHeight(), 0);
        }

        // Paint the cells.
        paintCells(context, g, rMin, rMax, cMin, cMax);

        paintDropLines(context, g);
    }

    /**
     * @see com.seaglasslookandfeel.painter.ViewportPainter#paintViewport(com.seaglasslookandfeel.SeaGlassContext,
     *      java.awt.Graphics, javax.swing.JViewport)
     */
    public void paintViewport(SeaGlassContext context, Graphics g, JViewport c) {
        paintStripesAndGrid(context, g, c, c.getWidth(), c.getHeight(), table.getLocation().y);
    }

    /**
     * Paint the stripes and grid.
     *
     * @param context the Synth context.
     * @param g       the Graphics context.
     * @param c       the component.
     * @param width   the width of the table.
     * @param height  the height of the table.
     * @param top     the top row to paint (for viewports).
     */
    public void paintStripesAndGrid(SeaGlassContext context, Graphics g, JComponent c, int width, int height, int top) {
        int rh  = table.getRowHeight();
        int n   = table.getRowCount();
        int row = Math.abs(top / rh);
        // if (true) return;

        // TableCellRenderer renderer =
        // table.getDefaultRenderer(java.lang.Object.class);

        // Paint the background, including stripes if requested.
        if (alternateColor != null) {
            // Fill the viewport with background color.
        	// Rossi: Ugly broken hack to make the first column white instead of blue.
        	// To see how the new blue table headers look like.
        	// This is should be done by modifying the "for loop" instead.
            g.setColor(alternateColor);
            g.fillRect(0, 0, width, height);

            // Now check if we need to paint some stripes
            g.setColor(table.getBackground());

            // Paint table rows to fill the viewport.
            for (int y = top + row * rh, ymax = height; y < ymax; y += rh) {
                if (row % 2 == 0) {
                    g.fillRect(0, y, width, rh);
                }

                row++;
            }
        } else {
            // Fill the viewport with the background color of the table
            g.setColor(table.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }

        SynthGraphicsUtils synthG = context.getStyle().getGraphicsUtils(context);

        // Paint the horizontal grid lines
        if (table.getShowHorizontalLines()) {
            g.setColor(table.getGridColor());
            row = Math.abs(top / rh);
            int y = top + row * rh + rh - 1;

            while (y < height) {
                synthG.drawLine(context, "Table.grid", g, 0, y, width, y);
                y += rh;
            }
        }

        // Paint the vertical grid lines
        if (table.getShowVerticalLines()) {
            g.setColor(table.getGridColor());
            TableColumnModel cm = table.getColumnModel();

            n = cm.getColumnCount();
            int y = top + row * rh;

            ;
            int x = -1;

            for (int i = 0; i < n; i++) {
                TableColumn col = cm.getColumn(i);

                x += col.getWidth();
                synthG.drawLine(context, "Table.grid", g, x, y, x, height);
            }
        }
    }

    /**
     * Paint the drop lines, if any.
     *
     * @param context the Synth context.
     * @param g       the Graphics context.
     */
    private void paintDropLines(SeaGlassContext context, Graphics g) {
        JTable.DropLocation loc = table.getDropLocation();

        if (loc == null) {
            return;
        }

        Color color      = (Color) style.get(context, "Table.dropLineColor");
        Color shortColor = (Color) style.get(context, "Table.dropLineShortColor");

        if (color == null && shortColor == null) {
            return;
        }

        Rectangle rect;

        rect = getHDropLineRect(loc);
        if (rect != null) {
            int x = rect.x;
            int w = rect.width;

            if (color != null) {
                extendRect(rect, true);
                g.setColor(color);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }

            if (!loc.isInsertColumn() && shortColor != null) {
                g.setColor(shortColor);
                g.fillRect(x, rect.y, w, rect.height);
            }
        }

        rect = getVDropLineRect(loc);
        if (rect != null) {
            int y = rect.y;
            int h = rect.height;

            if (color != null) {
                extendRect(rect, false);
                g.setColor(color);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }

            if (!loc.isInsertRow() && shortColor != null) {
                g.setColor(shortColor);
                g.fillRect(rect.x, y, rect.width, h);
            }
        }
    }

    /**
     * Get the horizontal drop line rectangle.
     *
     * @param  loc the drop location.
     *
     * @return the rectangle.
     */
    private Rectangle getHDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertRow()) {
            return null;
        }

        int row = loc.getRow();
        int col = loc.getColumn();

        if (col >= table.getColumnCount()) {
            col--;
        }

        Rectangle rect = table.getCellRect(row, col, true);

        if (row >= table.getRowCount()) {
            row--;
            Rectangle prevRect = table.getCellRect(row, col, true);

            rect.y = prevRect.y + prevRect.height;
        }

        if (rect.y == 0) {
            rect.y = -1;
        } else {
            rect.y -= 2;
        }

        rect.height = 3;

        return rect;
    }

    /**
     * Get the vertical drop line rectangle.
     *
     * @param  loc the drop location.
     *
     * @return the rectangle.
     */
    private Rectangle getVDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertColumn()) {
            return null;
        }

        boolean   ltr  = table.getComponentOrientation().isLeftToRight();
        int       col  = loc.getColumn();
        Rectangle rect = table.getCellRect(loc.getRow(), col, true);

        if (col >= table.getColumnCount()) {
            col--;
            rect = table.getCellRect(loc.getRow(), col, true);
            if (ltr) {
                rect.x = rect.x + rect.width;
            }
        } else if (!ltr) {
            rect.x = rect.x + rect.width;
        }

        if (rect.x == 0) {
            rect.x = -1;
        } else {
            rect.x -= 2;
        }

        rect.width = 3;

        return rect;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rect       DOCUMENT ME!
     * @param  horizontal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Rectangle extendRect(Rectangle rect, boolean horizontal) {
        if (rect == null) {
            return rect;
        }

        if (horizontal) {
            rect.x     = 0;
            rect.width = table.getWidth();
        } else {
            rect.y = 0;

            if (table.getRowCount() != 0) {
                Rectangle lastRect = table.getCellRect(table.getRowCount() - 1, 0, true);

                rect.height = lastRect.y + lastRect.height;
            } else {
                rect.height = table.getHeight();
            }
        }

        return rect;
    }

    /**
     * Paints the grid lines within <I>aRect</I>, using the grid color set with
     * <I>setGridColor</I>. Paints vertical lines if <code>
     * getShowVerticalLines()</code> returns true and paints horizontal lines if
     * <code>getShowHorizontalLines()</code> returns true. TODO See if we want
     * to remove this method.
     *
     * @param context the Synth context.
     * @param g       the Graphics context.
     * @param rMin    DOCUMENT ME!
     * @param rMax    DOCUMENT ME!
     * @param cMin    DOCUMENT ME!
     * @param cMax    DOCUMENT ME!
     */
    @SuppressWarnings("unused")
    private void paintGrid(SeaGlassContext context, Graphics g, int rMin, int rMax, int cMin, int cMax) {
        g.setColor(table.getGridColor());

        Rectangle          minCell     = table.getCellRect(rMin, cMin, true);
        Rectangle          maxCell     = table.getCellRect(rMax, cMax, true);
        Rectangle          damagedArea = minCell.union(maxCell);
        SynthGraphicsUtils synthG      = context.getStyle().getGraphicsUtils(context);

        if (table.getShowHorizontalLines()) {
            int tableWidth = damagedArea.x + damagedArea.width;
            int y          = damagedArea.y;

            for (int row = rMin; row <= rMax; row++) {
                y += table.getRowHeight(row);
                synthG.drawLine(context, "Table.grid", g, damagedArea.x, y - 1, tableWidth - 1, y - 1);
            }
        }

        if (table.getShowVerticalLines()) {
            TableColumnModel cm          = table.getColumnModel();
            int              tableHeight = damagedArea.y + damagedArea.height;
            int              x;

            if (table.getComponentOrientation().isLeftToRight()) {
                x = damagedArea.x;
                for (int column = cMin; column <= cMax; column++) {
                    int w = cm.getColumn(column).getWidth();

                    x += w;
                    synthG.drawLine(context, "Table.grid", g, x - 1, 0, x - 1, tableHeight - 1);
                }
            } else {
                x = damagedArea.x;
                for (int column = cMax; column >= cMin; column--) {
                    int w = cm.getColumn(column).getWidth();

                    x += w;
                    synthG.drawLine(context, "Table.grid", g, x - 1, 0, x - 1, tableHeight - 1);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  aColumn DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int viewIndexForColumn(TableColumn aColumn) {
        TableColumnModel cm = table.getColumnModel();

        for (int column = 0; column < cm.getColumnCount(); column++) {
            if (cm.getColumn(column) == aColumn) {
                return column;
            }
        }

        return -1;
    }

    /**
     * Paint cells.
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     * @param rMin    DOCUMENT ME!
     * @param rMax    DOCUMENT ME!
     * @param cMin    DOCUMENT ME!
     * @param cMax    DOCUMENT ME!
     */
    private void paintCells(SeaGlassContext context, Graphics g, int rMin, int rMax, int cMin, int cMax) {
        JTableHeader header        = table.getTableHeader();
        TableColumn  draggedColumn = (header == null) ? null : header.getDraggedColumn();

        TableColumnModel cm           = table.getColumnModel();
        int              columnMargin = cm.getColumnMargin();

        Rectangle   cellRect;
        TableColumn aColumn;
        int         columnWidth;

        if (table.getComponentOrientation().isLeftToRight()) {
            for (int row = rMin; row <= rMax; row++) {
                cellRect = table.getCellRect(row, cMin, false);
                for (int column = cMin; column <= cMax; column++) {
                    aColumn        = cm.getColumn(column);
                    columnWidth    = aColumn.getWidth();
                    cellRect.width = columnWidth - columnMargin;
                    if (aColumn != draggedColumn) {
                        paintCell(context, g, cellRect, row, column);
                    }

                    cellRect.x += columnWidth;
                }
            }
        } else {
            for (int row = rMin; row <= rMax; row++) {
                cellRect = table.getCellRect(row, cMin, false);
                aColumn  = cm.getColumn(cMin);
                if (aColumn != draggedColumn) {
                    columnWidth    = aColumn.getWidth();
                    cellRect.width = columnWidth - columnMargin;
                    paintCell(context, g, cellRect, row, cMin);
                }

                for (int column = cMin + 1; column <= cMax; column++) {
                    aColumn        =  cm.getColumn(column);
                    columnWidth    =  aColumn.getWidth();
                    cellRect.width =  columnWidth - columnMargin;
                    cellRect.x     -= columnWidth;
                    if (aColumn != draggedColumn) {
                        paintCell(context, g, cellRect, row, column);
                    }
                }
            }
        }

        // Paint the dragged column if we are dragging.
        if (draggedColumn != null) {
            paintDraggedArea(context, g, rMin, rMax, draggedColumn, header.getDraggedDistance());
        }

        // Remove any renderers that may be left in the rendererPane.
        rendererPane.removeAll();
    }

    /**
     * DOCUMENT ME!
     *
     * @param context       DOCUMENT ME!
     * @param g             DOCUMENT ME!
     * @param rMin          DOCUMENT ME!
     * @param rMax          DOCUMENT ME!
     * @param draggedColumn DOCUMENT ME!
     * @param distance      DOCUMENT ME!
     */
    private void paintDraggedArea(SeaGlassContext context, Graphics g, int rMin, int rMax, TableColumn draggedColumn, int distance) {
        int draggedColumnIndex = viewIndexForColumn(draggedColumn);

        Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true);
        Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true);

        Rectangle vacatedColumnRect = minCell.union(maxCell);

        // Paint a gray well in place of the moving column.
        g.setColor(table.getParent().getBackground());
        g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);

        // Move to the where the cell has been dragged.
        vacatedColumnRect.x += distance;

        // Fill the background.
        g.setColor(context.getStyle().getColor(context, ColorType.BACKGROUND));
        g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);

        SynthGraphicsUtils synthG = context.getStyle().getGraphicsUtils(context);

        // Paint the vertical grid lines if necessary.
        if (table.getShowVerticalLines()) {
            g.setColor(table.getGridColor());
            int x1 = vacatedColumnRect.x;
            int y1 = vacatedColumnRect.y;
            int x2 = x1 + vacatedColumnRect.width - 1;
            int y2 = y1 + vacatedColumnRect.height - 1;
            // Left
            synthG.drawLine(context, "Table.grid", g, x1 - 1, y1, x1 - 1, y2);
            // Right
            synthG.drawLine(context, "Table.grid", g, x2, y1, x2, y2);
        }

        for (int row = rMin; row <= rMax; row++) {
            // Render the cell value
            Rectangle r = table.getCellRect(row, draggedColumnIndex, false);

            r.x += distance;
            paintCell(context, g, r, row, draggedColumnIndex);

            // Paint the (lower) horizontal grid line if necessary.
            if (table.getShowHorizontalLines()) {
                g.setColor(table.getGridColor());
                Rectangle rcr = table.getCellRect(row, draggedColumnIndex, true);

                rcr.x += distance;
                int x1 = rcr.x;
                int y1 = rcr.y;
                int x2 = x1 + rcr.width - 1;
                int y2 = y1 + rcr.height - 1;

                synthG.drawLine(context, "Table.grid", g, x1, y2, x2, y2);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context  DOCUMENT ME!
     * @param g        DOCUMENT ME!
     * @param cellRect DOCUMENT ME!
     * @param row      DOCUMENT ME!
     * @param column   DOCUMENT ME!
     */
    private void paintCell(SeaGlassContext context, Graphics g, Rectangle cellRect, int row, int column) {
        if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
            Component component = table.getEditorComponent();

            component.setBounds(cellRect);
            component.validate();
        } else {
            TableCellRenderer renderer  = table.getCellRenderer(row, column);
            Component         component = table.prepareRenderer(renderer, row, column);

            rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
        }
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(event)) {
            updateStyle((JTable) event.getSource());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Border getRowBorder() {
        return BorderFactory.createEmptyBorder(0, 5, 0, 5);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Border getSelectedRowBorder() {
        return BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getSelectedRowBottomHighlight()),
                                                  BorderFactory.createEmptyBorder(1, 5, 0, 5));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Color getSelectedRowBottomHighlight() {
        return WindowUtils.isParentWindowFocused(table) ? selectionActiveBottomBorderColor : selectionInactiveBottomBorderColor;
    }

    /**
     * Creates a {@link Border} that paints any empty space to the right of the
     * last column header in the given {@link JTable}'s {@link JTableHeader}.
     *
     * @param  table DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Border createTableHeaderEmptyColumnPainter(final JTable table) {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                // if this JTableHeader is parented in a JViewport, then paint
                // the table header background to the right of the last column
                // if neccessary.
                Container viewport = table.getParent();

                if ((viewport instanceof JViewport) && table.getWidth() < viewport.getWidth()) {
                    int startX           = table.getWidth();
                    int emptyColumnWidth = viewport.getWidth() - table.getWidth();

                    TableCellRenderer renderer  = table.getTableHeader().getDefaultRenderer();
                    // Rossi: Fix for indexoutofbounds exception: A try catch might be good too?
                    Component         component = renderer.getTableCellRendererComponent(table, "", false, false, 0, table.getColumnCount()-1);

                    component.setBounds(0, 0, emptyColumnWidth, table.getTableHeader().getHeight());

                    ((JComponent) component).setOpaque(true);
                    CELL_RENDER_PANE.paintComponent(g, component, null, startX, 0, emptyColumnWidth + 1,
                                                    table.getTableHeader().getHeight(), true);
                }
            }
        };
    }

    /**
     * Adds striping to the background of the given {@link JTable}. The actual
     * striping is installed on the containing {@link JScrollPane}'s
     * {@link JViewport}, so if this table is not added to a {@code JScrollPane}
     * , then no stripes will be painted. This method can be called before the
     * given table is added to a scroll pane, though, as a
     * {@link PropertyChangeListener} will be installed to handle "ancestor"
     * changes.
     *
     * @param table the table to paint row stripes for.
     */
    public static void setViewPortListeners(JTable table) {
        table.addPropertyChangeListener("ancestor", createAncestorPropertyChangeListener(table));
        // Install a listener to cause the whole table to repaint when a column
        // is resized. We do this because the extended grid lines may need to be
        // repainted. This could be cleaned up, but for now, it works fine.
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).addPropertyChangeListener(createAncestorPropertyChangeListener(table));
            table.getColumnModel().addColumnModelListener(createTableColumnModelListener(table));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  table DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static TableColumnModelListener createTableColumnModelListener(final JTable table) {
        return new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }

            public void columnMarginChanged(ChangeEvent e) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }

            public void columnRemoved(TableColumnModelEvent e) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param  table DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static PropertyChangeListener createAncestorPropertyChangeListener(final JTable table) {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
                    table.getParent().repaint();
                }
            }
        };
    }

    /**
     * Creates a custom {@link CellRendererPane} that sets the renderer
     * component to be non-opqaque if the associated row isn't selected. This
     * custom {@code CellRendererPane} is needed because a table UI delegate has
     * no prepare renderer like {@link JTable} has.
     *
     * @return DOCUMENT ME!
     */
    private CellRendererPane createCustomCellRendererPane() {
        return new CellRendererPane() {
            @Override
            public void paintComponent(Graphics graphics, Component component, Container container, int x, int y, int w, int h,
                    boolean shouldValidate) {
                int     rowAtPoint = table.rowAtPoint(new Point(x, y));
                boolean isSelected = table.isRowSelected(rowAtPoint);

                if (component instanceof JComponent && component instanceof UIResource) {
                    JComponent jComponent = (JComponent) component;

                    jComponent.setOpaque(true);
                    jComponent.setBorder(isSelected ? getSelectedRowBorder() : getRowBorder());
                    jComponent.setBackground(isSelected ? jComponent.getBackground() : transparentColor);

                    if (isSelected) {
                        jComponent.setForeground(unwrap(table.getSelectionForeground()));
                        jComponent.setBackground(unwrap(table.getSelectionBackground()));
                    } else {
                        jComponent.setForeground(unwrap(table.getForeground()));
                        jComponent.setBackground(transparentColor);
                    }
                }

                super.paintComponent(graphics, component, container, x, y, w, h, shouldValidate);
            }

            /**
             * DOCUMENT ME!
             *
             * @param  c DOCUMENT ME!
             *
             * @return DOCUMENT ME!
             */
            private Color unwrap(Color c) {
                if (c instanceof UIResource) {
                    return new Color(c.getRGB());
                }

                return c;
            }

            /**
             * @see javax.swing.JComponent#isOpaque()
             */
            @SuppressWarnings("unused")
            public boolean isOpaque(int x, int y) {
                int rowAtPoint = table.rowAtPoint(new Point(x, y));

                return table.isRowSelected(rowAtPoint) ? true : super.isOpaque();
            }
        };
    }

    /**
     * DOCUMENT ME!
     */
    public class SeaGlassBooleanTableCellRenderer extends JCheckBox implements TableCellRenderer, UIResource {
        private static final long serialVersionUID = 4625890509524329579L;
        private boolean           isRowSelected;

        /**
         * Creates a new SeaGlassBooleanTableCellRenderer object.
         */
        public SeaGlassBooleanTableCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setName("Table.cellRenderer");
        }

        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            isRowSelected = isSelected;

            if (isSelected) {
                setForeground(unwrap(table.getSelectionForeground()));
                setBackground(unwrap(table.getSelectionBackground()));
            } else {
                setForeground(unwrap(table.getForeground()));
                setBackground(unwrap(table.getBackground()));
            }

            setSelected((value != null && ((Boolean) value).booleanValue()));
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  c DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Color unwrap(Color c) {
            if (c instanceof UIResource) {
                return new Color(c.getRGB());
            }

            return c;
        }

        /**
         * @see javax.swing.JComponent#isOpaque()
         */
        public boolean isOpaque() {
            return isRowSelected ? true : super.isOpaque();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public class SeaGlassTableCellRenderer extends DefaultTableCellRenderer implements UIResource {
        private static final long serialVersionUID = 9159798558985747389L;
        private Object            numberFormat;
        private Object            dateFormat;
        private boolean           opaque;

        /**
         * @see javax.swing.JComponent#setOpaque(boolean)
         */
        public void setOpaque(boolean isOpaque) {
            opaque = isOpaque;
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#isOpaque()
         */
        public boolean isOpaque() {
            return opaque;
        }

        /**
         * @see java.awt.Component#getName()
         */
        public String getName() {
            String name = super.getName();

            if (name == null) {
                return "Table.cellRenderer";
            }

            return name;
        }

        /**
         * @see javax.swing.JComponent#setBorder(javax.swing.border.Border)
         */
        public void setBorder(Border b) {
            if (useUIBorder || b instanceof SeaGlassBorder) {
                super.setBorder(b);
            }
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (!useTableColors && (isSelected || hasFocus)) {
                SeaGlassLookAndFeel.setSelectedUI((SeaGlassLabelUI) SeaGlassLookAndFeel.getUIOfType(getUI(), SeaGlassLabelUI.class),
                                                  isSelected, hasFocus, table.isEnabled(), false);
            } else {
                SeaGlassLookAndFeel.resetSelectedUI();
            }

            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                comp.setBackground(alternateColor);
                setBackground(alternateColor);
            } else {
                comp.setBackground(table.getBackground());
                setBackground(table.getBackground());
            }

            setIcon(null);
            Class columnClass = table.getColumnClass(column);

            configureValue(value, columnClass);

            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param value       DOCUMENT ME!
         * @param columnClass DOCUMENT ME!
         */
        private void configureValue(Object value, Class columnClass) {
            if (columnClass == Object.class || columnClass == null) {
                setHorizontalAlignment(JLabel.LEADING);
            } else if (columnClass == Float.class || columnClass == Double.class) {
                if (numberFormat == null) {
                    numberFormat = NumberFormat.getInstance();
                }

                setHorizontalAlignment(JLabel.TRAILING);
                setText((value == null) ? "" : ((NumberFormat) numberFormat).format(value));
            } else if (columnClass == Number.class) {
                setHorizontalAlignment(JLabel.TRAILING);
                // Super will have set value.
            } else if (columnClass == Icon.class || columnClass == ImageIcon.class) {
                setHorizontalAlignment(JLabel.CENTER);
                setIcon((value instanceof Icon) ? (Icon) value : null);
                setText("");
            } else if (columnClass == Date.class) {
                if (dateFormat == null) {
                    dateFormat = DateFormat.getDateInstance();
                }

                setHorizontalAlignment(JLabel.LEADING);
                setText((value == null) ? "" : ((Format) dateFormat).format(value));
            } else {
                configureValue(value, columnClass.getSuperclass());
            }
        }

        /**
         * @see javax.swing.JComponent#paint(java.awt.Graphics)
         */
        public void paint(Graphics g) {
            super.paint(g);
            SeaGlassLookAndFeel.resetSelectedUI();
        }
    }
}
