package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Test tabbed panes.
 */
public class TestTabbedPanes {

    static JTabbedPane tabbedPane;
    static int         tabNum = 2;

    /**
     * Main method.
     *
     * @param args command line args (ignored).
     */
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
                    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
                    tabbedPane.putClientProperty("JTabbedPane.closeButton", "left");
                    tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    // tabbedPane.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);

                    tabbedPane.setPreferredSize(new Dimension(200, 150));

                    tabbedPane.addTab("Tab 0", createPanel(true));
                    tabbedPane.addTab("Tab 1", createPanel(false));

                    JPanel panel = new JPanel();

                    panel.setBackground(new Color(248, 248, 248));

                    panel.setPreferredSize(new Dimension(300, 200));
                    panel.setLayout(new BorderLayout());
                    panel.add(tabbedPane, BorderLayout.CENTER);

                    JButton button = new JButton("Add tab");

                    button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {
                                tabbedPane.addTab("Tab " + tabNum++, createPanel(false));
                            }
                        });

                    JPanel buttonPanel = new JPanel();

                    buttonPanel.add(button);

                    JFrame frame = new JFrame();

                    frame.add(buttonPanel, BorderLayout.NORTH);
                    frame.add(panel, BorderLayout.CENTER);
                    frame.setSize(500, 300);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
    }

    /**
     * Create a panel for the tabbed pane.
     *
     * @param  setBgColor {@code true} if the background color should be set,
     *                    {@code false} if we just use the control background.
     *
     * @return the panel.
     */
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
