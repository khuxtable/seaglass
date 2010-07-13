package com.seaglasslookandfeel.demo;

import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Test combo boxes with many elements.
 */
public class TestComboBoxes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel            contentPane      = null;
    private JComboBox         comboBox         = null;

    /**
     * Constructor.
     */
    public TestComboBoxes() {
        super("Test Combo Boxes");
        setSize(300, 200);

        comboBox = new JComboBox();
        comboBox.setBounds(new Rectangle(87, 61, 122, 25));

        for (int i = 0; i < 50; i++) {
            comboBox.addItem(i);
        }

        setLayout(null);
        add(comboBox);
    }

    /**
     * Main method.
     *
     * @param args command line args.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    TestComboBoxes thisClass = new TestComboBoxes();

                    thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    thisClass.setVisible(true);
                }
            });
    }
}
