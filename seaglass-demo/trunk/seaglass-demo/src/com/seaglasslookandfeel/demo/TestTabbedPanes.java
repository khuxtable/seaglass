package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * DOCUMENT ME!
 *
 * @author  $author$
 * @version $Revision$, $Date$
 */
public class TestTabbedPanes {

    static JTabbedPane tabbedPane;
    static int         tabNum = 3;

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
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

                    tabbedPane.setPreferredSize(new Dimension(200, 150));

                    tabbedPane.addTab("Tab 1", createPanel(true));
                    tabbedPane.addTab("Tab 2", createPanel(false));

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
     * DOCUMENT ME!
     *
     * @param  setBgColor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
