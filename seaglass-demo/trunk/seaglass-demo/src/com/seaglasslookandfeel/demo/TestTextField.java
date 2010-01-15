package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;

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
                tf1.putClientProperty("JTextField.variant", "search");

                JTextField tf2 = new JTextField("Here is some more text");

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
