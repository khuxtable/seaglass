package com.seaglasslookandfeel.demo;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.UIManager;

import java.awt.Rectangle;

/**
 * DOCUMENT ME!
 */
public class TestComboBoxes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel            jContentPane     = null;
    private JComboBox         jComboBox        = null;

    /**
     * This is the default constructor
     */
    public TestComboBoxes() {
        super();
        initialize();
    }

    /**
     * This method initializes jComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBox() {
        if (jComboBox == null) {
            jComboBox = new JComboBox();
            jComboBox.setBounds(new Rectangle(87, 61, 122, 25));
        }

        return jComboBox;
    }

    /**
     * Main method.
     * 
     * @param args
     *            command line args.
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

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
        for (int i = 0; i < 50; i++) {
            jComboBox.addItem(i);
        }
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJComboBox(), null);
        }

        return jContentPane;
    }

}
