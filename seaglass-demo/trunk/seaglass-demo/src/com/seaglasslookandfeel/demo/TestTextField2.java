package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;

/**
 * Test text fields.
 */
public class TestTextField2 {

    /**
     * Main method.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JPopupMenu popupMenu = new JPopupMenu();

                    popupMenu.add("First item");
                    popupMenu.add("Second item");
                    popupMenu.add("Third item");

                    JTextField          tf1 = new JTextField("Here is some text");
                    JTextField          tf2 = new JTextField("Here is some more text");
                    JFormattedTextField tf3 = new JFormattedTextField("Here is some formatted text");
                    JPasswordField      tf4 = new JPasswordField("foo");

                    tf1.setPreferredSize(new Dimension(180, 25));
                    tf2.setPreferredSize(new Dimension(180, 25));
                    tf3.setPreferredSize(new Dimension(180, 25));
                    tf4.setPreferredSize(new Dimension(180, 25));

                    tf1.putClientProperty("JTextField.variant", "search");
                    tf1.putClientProperty("JTextField.Search.PlaceholderText", "Search right here");
                    tf1.putClientProperty("JTextField.Search.FindPopup", popupMenu);

                    tf1.putClientProperty("JTextField.Search.CancelAction", new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("cancel source = " + e.getSource().getClass().getName());
                            }
                        });

                    tf1.putClientProperty("JTextField.Search.FindAction", new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("find source = " + e.getSource().getClass().getName());
                            }
                        });

                    JPanel panel = new JPanel();

                    panel.add(tf1);
                    panel.add(tf2);
                    panel.add(tf3);
                    panel.add(tf4);

                    JFrame frame = new JFrame("Title");
                    frame.getRootPane().putClientProperty(SeaGlassRootPaneUI.UNIFIED_TOOLBAR_LOOK, Boolean.TRUE);

                    frame.add(panel, BorderLayout.CENTER);
                    frame.setSize(275, 150);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                }
            });
    }
}
