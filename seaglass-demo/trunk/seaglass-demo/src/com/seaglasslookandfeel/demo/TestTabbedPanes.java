package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestTabbedPanes {

    public static void main(String[] args) {
        if (true) {
            try {
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
                tabbedPane.setPreferredSize(new Dimension(200, 150));

                tabbedPane.addTab("Tab 1", createPanel(true));
                tabbedPane.addTab("Tab 2", createPanel(false));
                tabbedPane.addTab("Tab 3", createPanel(false));
                tabbedPane.addTab("Tab 4", createPanel(false));
                tabbedPane.addTab("Tab 5", createPanel(false));
                tabbedPane.addTab("Tab 6", createPanel(false));
                tabbedPane.addTab("Tab 7", createPanel(false));
                tabbedPane.addTab("Tab 8", createPanel(false));
                tabbedPane.addTab("Tab 9", createPanel(false));
                tabbedPane.addTab("Tab 10", createPanel(false));

                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(300, 200));
                panel.setLayout(new BorderLayout());
                panel.add(tabbedPane, BorderLayout.CENTER);

                JFrame frame = new JFrame();
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(500, 300);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static JPanel createPanel(boolean setBgColor) {
        JPanel panel = new JPanel();

        if (setBgColor) {
            panel.setBackground(new java.awt.Color(38, 128, 150));
        }
        panel.setMinimumSize(new java.awt.Dimension(50, 50));
        panel.setPreferredSize(new java.awt.Dimension(150, 50));
        panel.setLayout(new java.awt.BorderLayout());

        return panel;
    }
}
