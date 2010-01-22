package com.seaglasslookandfeel.demo;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.explodingpixels.macwidgets.HudWindow;

public class TestHudWindow {

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

                JFrame frame = new JFrame();
                frame.setSize(400, 400);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // JTextField textField = new JTextField("foo bar");
                // textField.setOpaque(false);

                HudWindow hud = new HudWindow("Window", frame);
                // hud.getContentPane().add(textField);

                JDialog dialog = hud.getJDialog();

                // WindowUtils.createAndInstallRepaintWindowFocusListener(frame);
                dialog.setSize(300, 350);
                dialog.setLocationRelativeTo(null);
                dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });
    }

}
