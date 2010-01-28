package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestButtons {

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

                JButton button1 = new JButton("Test 1");
                button1.putClientProperty("JButton.buttonType", "segmented");
                button1.putClientProperty("JButton.segmentPosition", "first");
                JButton button2 = new JButton("Test 2");
                button2.putClientProperty("JButton.buttonType", "segmented");
                button2.putClientProperty("JButton.segmentPosition", "middle");
                JButton button3 = new JButton("Test 3");
                button3.putClientProperty("JButton.buttonType", "segmented");
                button3.putClientProperty("JButton.segmentPosition", "last");

                JPanel panel = new JPanel();
                panel.setOpaque(true);
                panel.setBackground(new Color(panel.getBackground().getRGB()));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 0, 0);
                panel.setLayout(layout);

                panel.add(button1);
                panel.add(button2);
                panel.add(button3);

                JFrame frame = new JFrame();
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(275, 75);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
