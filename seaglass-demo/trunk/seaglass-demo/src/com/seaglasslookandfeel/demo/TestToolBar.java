package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;

public class TestToolBar {
    
    static JFrame frame;

    public static void main(String[] args) {
        if (true) {
            try {
                // System.setProperty("SeaGlass.Override.os.name", "Windows");
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.setProperty("apple.laf.useScreenMenuBar", "true");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JMenuBar mb = new JMenuBar();
                JMenu m1 = new JMenu("File");
                JMenu m2 = new JMenu("Edit");
                JMenu m3 = new JMenu("Help");
                mb.add(m1);
                mb.add(m2);
                mb.add(m3);

                JToolBar toolbar1 = makeToolBar("Toolbar1", JToolBar.HORIZONTAL);
                JToolBar toolbar2 = makeToolBar("Toolbar2", JToolBar.HORIZONTAL);
                JToolBar toolbar3 = makeToolBar("Toolbar3", JToolBar.VERTICAL);
                JToolBar toolbar4 = makeToolBar("Toolbar4", JToolBar.VERTICAL);

                frame = new JFrame("TestToolBar");
                frame.setJMenuBar(mb);
                frame.getRootPane().putClientProperty(SeaGlassRootPaneUI.UNIFIED_TOOLBAR_LOOK, Boolean.TRUE);

                JPanel outerPanel = new JPanel(new BorderLayout());
                JPanel panel = new JPanel();
                panel.add(new JLabel("Hi there!"));
                JButton open = new JButton("Open");
                panel.add(open);
                JToolBar tb = new JToolBar();
                tb.add(new JButton("B1"));
                outerPanel.add(panel, BorderLayout.CENTER);
                outerPanel.add(tb, BorderLayout.SOUTH);

                open.addActionListener(new ActionListener() {
                    JDialog dialog;

                    public void actionPerformed(ActionEvent e) {
                        dialog = new JDialog(frame, "Dialog");
                        JPanel panel = new JPanel();
                        panel.add(new JLabel("Boo!"));
                        JButton close = new JButton("Close");
                        close.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispose();
                            }
                        });
                        panel.add(close);
                        dialog.add(panel, BorderLayout.CENTER);
                        dialog.setSize(300, 200);
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    }
                });

                frame.getContentPane().add(toolbar1, BorderLayout.NORTH);
                frame.getContentPane().add(toolbar2, BorderLayout.SOUTH);
                frame.getContentPane().add(toolbar3, BorderLayout.WEST);
                frame.getContentPane().add(toolbar4, BorderLayout.EAST);
                frame.add(outerPanel, BorderLayout.CENTER);
                frame.setSize(600, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }

            /**
             * @return
             */
            private JToolBar makeToolBar(String name, int orientation) {
                JButton button1 = new JButton("Test 1");
                JToggleButton button2 = new JToggleButton("Test 2");
                JButton button3 = new JButton("Test 3");

                JToolBar toolbar = new JToolBar(name, orientation);

                JPanel foo = new JPanel();
                foo.add(button3);
                foo.add(new JLabel("Bar"));
                toolbar.add(button1);
                toolbar.addSeparator();
                toolbar.add(button2);
                toolbar.addSeparator();
                toolbar.add(foo);
                return toolbar;
            }
        });
    }

}
