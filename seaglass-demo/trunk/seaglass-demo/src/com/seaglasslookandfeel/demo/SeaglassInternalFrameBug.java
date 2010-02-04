package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

public class SeaglassInternalFrameBug extends JFrame {

    public static void main(String[] args) {
        // set look and feel
        try {

            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            System.out.println(">> Set Seaglass look and feel");
            //           
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            // System.out.println(">> Set Nimbus look and feel");
        } catch (Exception e) {
            System.err.println(">> Cannot set look and feel");
            e.printStackTrace();
        }

        SeaglassInternalFrameBug demo = new SeaglassInternalFrameBug();
        demo.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public SeaglassInternalFrameBug() {
        super("Seaglass");

        JDesktopPane dPane = new JDesktopPane();
        dPane.setPreferredSize(new Dimension(725, 525));

        JInternalFrame iFrame1 = new JInternalFrame("test", true, true, true, true);
        dPane.add(iFrame1);
        iFrame1.setLayout(new FlowLayout());
        iFrame1.add(new JButton("B"));
        iFrame1.setBounds(10, 10, 200, 100);
        iFrame1.pack();
        iFrame1.setVisible(true);

        JInternalFrame iFrame2 = new JInternalFrame("test", true, true, true, true);
        dPane.add(iFrame2);
        iFrame2.setLayout(new FlowLayout());
        iFrame2.add(new JButton("B"));
        iFrame2.setBounds(50, 50, 200, 100);
        iFrame2.pack();
        iFrame2.setVisible(true);

        JInternalFrame iFrame3 = new JInternalFrame("test", true, true, true, true);
        dPane.add(iFrame3);
        iFrame3.setLayout(new FlowLayout());
        iFrame3.add(new JButton("B"));
        iFrame3.setBounds(75, 75, 200, 100);
        iFrame3.pack();
        iFrame3.setVisible(true);

        // this.setContentPane(dPane);
        this.add(dPane, BorderLayout.CENTER);

        this.setBounds(0, 100, 800, 600);
        this.setResizable(true);
        try {
            this.setVisible(true);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
