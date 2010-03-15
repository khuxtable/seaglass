package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Test column coloring in JTable.
 */
public class TestTables2 {

    /**
     * Creates a new TestTables2 object.
     *
     * @param args command line args.
     */
    TestTables2(String[] args) {
        if (args.length > 0) {
            try {
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[]   headers = { "col 1", "col 2" };
        String[][] rows    = {
            { "1", "2" },
            { "2", "3" },
            { "3", "4" },
            { "4", "5" }
        };

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable table = new JTable(rows, headers);

        table.setDefaultRenderer(java.lang.Object.class, new MyRenderer());

        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }

    /**
     * Main method.
     *
     * @param args command line args.
     */
    public static void main(String[] args) {
        new TestTables2(args);
    }

    /**
     * Custom renderer.
     */
    public class MyRenderer extends DefaultTableCellRenderer {

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value == null)
                return this;

            Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column % 2 == 1) {
                renderer.setBackground(Color.green);
            } else {
                renderer.setBackground(Color.blue);
            }

            return renderer;
        }
    }
}
