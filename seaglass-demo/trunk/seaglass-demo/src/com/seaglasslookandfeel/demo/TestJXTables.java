package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

import com.seaglasslookandfeel.ui.SeaGlassTableUI;

/**
 * DOCUMENT ME!
 */
public class TestJXTables {

    /**
     * DOCUMENT ME!
     * 
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (true) try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Object[][] data = new Object[][] {
                    { "All These Things I Hate (Revolve Around Me)", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "Cries In Vain", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "The End", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "Her Voice Resides", "Bullet For My Valentine", "The Poison", Boolean.TRUE },
                    { "Hit The Floor", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "Intro", "Bullet For My Valentine Apocalyptica", "The Poison", Boolean.FALSE },
                    { "The Poison", "Bullet For My Valentine", "The Poison", Boolean.TRUE },
                    { "Room 409", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "Spit You Out", "Bullet For My Valentine", "The Poison", Boolean.TRUE },
                    { "Suffocating Under Words Of Sorrow (What Can I Do)", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "Tears Don't Fall", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "4 Words (To Choke Upon)", "Bullet For My Valentine", "The Poison", Boolean.FALSE },
                    { "10 Years Today", "Bullet For My Valentine", "The Poison", Boolean.FALSE } };

                JXTable table = new JXTable(new DefaultTableModel(data, new String[] { "Name", "Artist", "Album", "Checked" })) {
                    @Override
                    public Class<?> getColumnClass(int column) {
                        if (convertColumnIndexToModel(column) == 3) {
                            return Boolean.class;
                        } else {
                            return Object.class;
                        }
                    }
                };
                SeaGlassTableUI ui = (SeaGlassTableUI) table.getUI();
                // Override JXTables renderers with the Sea Glass ones.
                ui.forceInstallRenderer(Boolean.class);
                ui.forceInstallRenderer(Object.class);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setColumnControlVisible(true);
                table.getColumnModel().getColumn(0).setPreferredWidth(75);
                table.getColumnModel().getColumn(1).setPreferredWidth(50);
                TableModel tableModel = table.getModel();

                JScrollPane scrollPane = new JScrollPane(table);

                JFrame frame = new JFrame();

                frame.add(scrollPane, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
