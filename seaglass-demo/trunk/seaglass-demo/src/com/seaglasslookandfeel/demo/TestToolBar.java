package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestToolBar {

    public static void main(String[] args) {
        if (true) {
            try {
//                System.setProperty("SeaGlass.Override.os.name", "Windows");
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JButton button1 = new JButton("Test 1");
                JToggleButton button2 = new JToggleButton("Test 2");
                JButton button3 = new JButton("Test 3");

                JToolBar toolbar = new JToolBar();

                JPanel foo = new JPanel();
                foo.add(button3);
                toolbar.add(button1);
                toolbar.addSeparator();
                toolbar.add(button2);
                toolbar.addSeparator();
                toolbar.add(foo);

                JFrame frame = new JFrame();
                frame.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);
                panel.add(new JLabel("Hi there!"));

                frame.getContentPane().add(toolbar, BorderLayout.PAGE_START);
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(275, 175);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                System.out.println("UI is " + button1.getUI().getClass().getName());
            }
        });
    }

}
