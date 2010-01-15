package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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

                JTextField textfield = new JTextField("Here is some text");
                textfield.putClientProperty("JTextField.variant", "search");
                
                JPanel panel = new JPanel();
                panel.add(textfield);

                JFrame frame = new JFrame();
                frame.add(panel, BorderLayout.CENTER);
                frame.add(new JButton("B1"), BorderLayout.SOUTH);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
