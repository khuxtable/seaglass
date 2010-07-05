package com.seaglasslookandfeel.demo;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Test frame rendering and also Chinese characters. I wonder what they mean?
 */
public class TestFrame extends JFrame {

    /**
     * Creates a new TestFrame object.
     */
    public TestFrame() {
        super("TestFrame");
        setTitle("简单例子");

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(new JButton("简单例子"));

        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Main method.
     *
     * @param args Command line args.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new TestFrame().setVisible(true);
                }
            });
    }

}
