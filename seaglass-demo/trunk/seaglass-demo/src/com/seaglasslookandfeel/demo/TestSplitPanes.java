package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestSplitPanes {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                JButton b1 = new JButton("Button 1");
                JButton b2 = new JButton("Button 2");

                JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, b1, b2);
                pane.setFocusable(true);
                pane.setOneTouchExpandable(true);

                JFrame frame = new JFrame();
                frame.add(pane, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
