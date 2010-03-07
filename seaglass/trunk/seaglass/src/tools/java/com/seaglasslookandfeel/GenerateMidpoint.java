package com.seaglasslookandfeel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GenerateMidpoint {

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
                        Color color1 = new Color(Integer.parseInt(c1Str, 16));
                        Color color2 = new Color(Integer.parseInt(c2Str, 16));
                        result.setText(deriveColor(color1, color2));
                    }
                });
                calc.setText("Calculate");

                result = new JTextPane();
                result.setPreferredSize(new Dimension(180, 100));
                result.setEditable(false);

                JPanel panel = new JPanel();
                panel.setBackground(new Color(248, 248, 248));
                panel.add(c1);
                panel.add(c2);
                panel.add(calc);
                panel.add(result);

                JFrame frame = new JFrame("Title");
                frame.add(panel, BorderLayout.CENTER);
                frame.setSize(275, 225);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private static String deriveColor(Color color1, Color color2) {
        int dest = deriveARGB(color1, color2, 0.5f);
        return String.format("%06x", dest & 0xFFFFFF);
    }

    private static int deriveARGB(Color color1, Color color2, float midPoint) {
        int r = color1.getRed() + (int) ((color2.getRed() - color1.getRed()) * midPoint + 0.5f);
        int g = color1.getGreen() + (int) ((color2.getGreen() - color1.getGreen()) * midPoint + 0.5f);
        int b = color1.getBlue() + (int) ((color2.getBlue() - color1.getBlue()) * midPoint + 0.5f);
        int a = color1.getAlpha() + (int) ((color2.getAlpha() - color1.getAlpha()) * midPoint + 0.5f);
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
