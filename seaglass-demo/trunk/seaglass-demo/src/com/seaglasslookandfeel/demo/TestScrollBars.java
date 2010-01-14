package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestScrollBars {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
                scrollBar.setPreferredSize(new Dimension(15, 300));
                
                JPanel panel = new JPanel();
                panel.add(scrollBar, BorderLayout.CENTER);

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
