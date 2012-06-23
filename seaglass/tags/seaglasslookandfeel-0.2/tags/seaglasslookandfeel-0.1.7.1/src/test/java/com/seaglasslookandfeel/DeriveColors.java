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

public class DeriveColors {

    private static JTextField c;
    private static JTextField h;
    private static JTextField s;
    private static JTextField b;
    private static JTextPane  result;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                c = new JTextField();
                c.setPreferredSize(new Dimension(180, 25));
                h = new JTextField();
                h.setPreferredSize(new Dimension(180, 25));
                s = new JTextField();
                s.setPreferredSize(new Dimension(180, 25));
                b = new JTextField();
                b.setPreferredSize(new Dimension(180, 25));

                JButton calc = new JButton();
                calc.setPreferredSize(new Dimension(100, 30));
                calc.setAction(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        String cStr = c.getText();
                        if (cStr.startsWith("0x")) cStr = cStr.substring(2);
                        Color color = new Color(Integer.parseInt(cStr, 16));
                        float hOffset = Float.parseFloat(h.getText());
                        float sOffset = Float.parseFloat(s.getText());
                        float bOffset = Float.parseFloat(b.getText());
                        result.setText(deriveColor(color, hOffset, sOffset, bOffset));
                    }
                });
                calc.setText("Calculate");

                result = new JTextPane();
                result.setPreferredSize(new Dimension(180, 100));
                result.setEditable(false);

                JPanel panel = new JPanel();
                panel.setBackground(new Color(248, 248, 248));
                panel.add(c);
                panel.add(h);
                panel.add(s);
                panel.add(b);
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

    private static String deriveColor(Color color, float hOffset, float sOffset, float bOffset) {
        Color dest = deriveColor(color, hOffset, sOffset, bOffset, 0);
        return String.format("%06x", (dest.getRGB() & 0xFFFFFF));
    }

    private static Color deriveColor(Color src, float hOffset, float sOffset, float bOffset, int aOffset) {
        float[] tmp = Color.RGBtoHSB(src.getRed(), src.getGreen(), src.getBlue(), null);

        // apply offsets
        tmp[0] = clamp(tmp[0] + hOffset);
        tmp[1] = clamp(tmp[1] + sOffset);
        tmp[2] = clamp(tmp[2] + bOffset);
        int alpha = clamp(src.getAlpha() + aOffset);

        return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24), true);
    }

    private static float clamp(float value) {
        if (value < 0) {
            value = 0;
        } else if (value > 1) {
            value = 1;
        }
        return value;
    }

    private static int clamp(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return value;
    }
}
