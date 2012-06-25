package com.seaglasslookandfeel.demo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.laffy.Laffy;

public class SeaGlassLaffyDemo {

    public static void main(String[] args) throws Exception {
        // Set look and feel
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                // set SeaGlass laf if available
                try {
                    UIManager.installLookAndFeel("SeaGlass", "com.seaglasslookandfeel.SeaGlassLookAndFeel");
                    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
                } catch (Exception e) {
                    System.err.println("Seaglass LAF not available using Ocean.");
                    try {
                        UIManager.setLookAndFeel(new MetalLookAndFeel());
                    } catch (Exception e2) {
                        System.err.println("Unable to use Ocean LAF using default.");
                    }
                }
            }
        });

        // create laffy and load
        Laffy.getInstance().load(args);
    }
}
