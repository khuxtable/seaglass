package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
                JTextField tf1 = new JTextField("Here is some text");
                tf1.setPreferredSize(new Dimension(180, 25));
                tf1.putClientProperty("JTextField.variant", "search");
                tf1.putClientProperty("JTextField.Search.PlaceholderText", "Search");
                tf1.putClientProperty("JTextField.Search.FindPopup", new PopupMenu());

                JTextField tf2 = new JTextField("Here is some more text");
                tf2.setPreferredSize(new Dimension(180, 25));

                JPanel panel = new JPanel();
                panel.add(tf1);
                panel.add(tf2);

                JFrame frame = new JFrame();
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
