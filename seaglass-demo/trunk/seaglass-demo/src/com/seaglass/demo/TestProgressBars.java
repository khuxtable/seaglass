package com.seaglass.demo;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TestProgressBars {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglass.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JProgressBar progressBar = new JProgressBar();
                progressBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                progressBar.setMinimum(0);
                progressBar.setMaximum(100);
                progressBar.getModel().setValue(33);

                JScrollPane scrollPane = new JScrollPane(progressBar);

                JFrame frame = new JFrame();
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

}
