package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestTextField {

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

                JTextField tf1 = new JTextField("Here is some text");
                tf1.setPreferredSize(new Dimension(180, 25));
                tf1.putClientProperty("JTextField.variant", "search");
                tf1.putClientProperty("JTextField.Search.PlaceholderText", "Search");
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

                JTextField tf2 = new JTextField("Here is some more text");
                tf2.setPreferredSize(new Dimension(180, 25));

                JPasswordField tf3 = new JPasswordField("foo");
                tf3.setPreferredSize(new Dimension(180, 25));

                JPanel panel = new JPanel();
                panel.add(tf1);
                panel.add(tf2);
                panel.add(tf3);

                JFrame frame = new JFrame("Title");
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    public static class MyLayout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }

        public void layoutContainer(Container parent) {
        }

        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        public void removeLayoutComponent(Component comp) {
        }
    }
}
