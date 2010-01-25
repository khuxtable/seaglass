package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestScrollBars {

    public static void main(String[] args) {
        if (true) {
            try {
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Set the global default.
            UIManager.put("SeaGlass.ScrollBarButtonsTogether", Boolean.TRUE);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JTextPane textPane = new JTextPane();
                textPane.setPreferredSize(new Dimension(500, 500));
                textPane.setBackground(Color.WHITE);
                textPane.setText(TEXT);

                JScrollPane scrollPane = new JScrollPane(textPane);
                // Set to override the global default.
                // scrollPane.getVerticalScrollBar().putClientProperty("SeaGlass.Override.ScrollBarButtonsTogether",
                // Boolean.FALSE);

                JFrame frame = new JFrame();
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.setSize(275, 125);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private static final String TEXT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus cursus, purus "
                                             + "suscipit sagittis volutpat, est ipsum ullamcorper est, ac varius sem metus "
                                             + "et lacus. Phasellus fringilla. Phasellus commodo orci id metus. Curabitur "
                                             + "eros. Sed nulla. Sed odio lorem, lobortis nec, sollicitudin in, hendrerit "
                                             + "vitae, metus. Phasellus molestie. Ut fermentum est a neque. Curabitur nec "
                                             + "dolor non dolor pretium condimentum. Praesent vestibulum, leo sed hendrerit "
                                             + "tristique, risus leo sagittis quam, ut pellentesque purus metus a felis. "
                                             + "Vivamus egestas, ligula vel bibendum elementum, sem ante tincidunt dui, "
                                             + "eget suscipit nulla urna nec lorem. Pellentesque non dolor ac odio "
                                             + "ultricies ultricies. Aliquam pellentesque tortor et ante. Sed accumsan mi "
                                             + "in mi. Phasellus turpis arcu, interdum congue, pulvinar ac, egestas id, " + "tellus."
                                             + "\n\n" + "Sed faucibus lacinia nibh. Integer ut lorem eu velit lacinia ultricies. "
                                             + "Phasellus vehicula tempor nibh. Duis gravida, sapien ut pellentesque "
                                             + "sodales, leo purus venenatis quam, eu gravida lectus neque vitae felis. Ut "
                                             + "odio. Duis consequat, ligula nec varius ultricies, ipsum diam consequat "
                                             + "purus, non posuere diam ante at purus. Maecenas et libero. Donec sagittis "
                                             + "nibh. Duis quis metus non purus ultrices tempus. Morbi consequat "
                                             + "ullamcorper nunc. Aliquam orci lacus, sagittis sit amet, ultrices ut, "
                                             + "feugiat eget, nunc. Morbi ante dui, bibendum vitae, convallis et, imperdiet " + "id, pede.";
}
