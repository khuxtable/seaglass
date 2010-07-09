package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;

/**
 * DOCUMENT ME!
 */
public class SeaGlassToolbarDisabledTest {

    /**
     * DOCUMENT ME!
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JFrame frame = new JFrame("SeaGlass Toolbar Disabled Test");

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    // frame.getRootPane().putClientProperty(SeaGlassRootPaneUI.UNIFIED_TOOLBAR_LOOK, Boolean.TRUE);

                    try {
                        Icon    icon    = new ImageIcon(SeaGlassToolbarDisabledTest.class.getResource(
                                                            "/com/seaglasslookandfeel/images/Event.png"));
                        JButton button1 = new JButton("Button1", icon);
                        button1.setEnabled(false);

                        JButton button2 = new JButton("Button2", icon);

                        JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);

                        toolbar.add(button1);
                        toolbar.add(button2);
                        frame.add(toolbar, BorderLayout.WEST);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
    }

}
