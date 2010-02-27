package com.seaglasslookandfeel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GenerateDifferences {

    private static JTextField c1;
    private static JTextField c2;
    private static JTextPane  result;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add("First item");
                popupMenu.add("Second item");
                popupMenu.add("Third item");

                c1 = new JTextField();
                c1.setPreferredSize(new Dimension(180, 25));
                c2 = new JTextField();
                c2.setPreferredSize(new Dimension(180, 25));

                JButton calc = new JButton();
                calc.setPreferredSize(new Dimension(100, 30));
                calc.setAction(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        String c1Str = c1.getText();
                        String c2Str = c2.getText();
                        if (c1Str.startsWith("0x")) c1Str = c1Str.substring(2);
                        if (c2Str.startsWith("0x")) c2Str = c2Str.substring(2);
                        Color src = new Color(Integer.parseInt(c1Str, 16));
                        Color dest = new Color(Integer.parseInt(c2Str, 16));
                        result.setText(calcOffsets(src, dest));
                    }
                });
                calc.setText("Calculate");

                result = new JTextPane();
                result.setPreferredSize(new Dimension(300, 100));
                result.setEditable(false);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(new Color(248, 248, 248));
                panel.add(c1);
                panel.add(c2);
                panel.add(calc);
                panel.add(result);

                JFrame frame = new JFrame("Title");
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(325, 225);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private static String calcOffsets(Color src, Color dest) {
        float[] tmp1 = Color.RGBtoHSB(src.getRed(), src.getGreen(), src.getBlue(), null);
        float[] tmp2 = Color.RGBtoHSB(dest.getRed(), dest.getGreen(), dest.getBlue(), null);

        // determine offsets
        float hOffset = tmp2[0] - tmp1[0];
        float sOffset = tmp2[1] - tmp1[1];
        float bOffset = tmp2[2] - tmp1[2];

        return String.format("deriveColor(src, %ff, %ff, %ff, 0)", hOffset, sOffset, bOffset);
    }
}
