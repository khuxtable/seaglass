package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

public class TestJFrameHeight extends JFrame {

    public TestJFrameHeight() {
        super("Test Seaglass");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.CENTER));
        content.setBackground(new Color(250, 250, 250));
        content.setPreferredSize(new Dimension(400,400));
        add(BorderLayout.CENTER, content);
        pack();
        setVisible(true);       
        System.out.println("window size = " + getSize());
        System.out.println("content size = " + content.getSize());
        System.out.println("content pane size = " + getRootPane().getContentPane().getPreferredSize());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new TestJFrameHeight();
    }
}
