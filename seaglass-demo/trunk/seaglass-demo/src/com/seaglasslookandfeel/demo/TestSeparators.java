package com.seaglasslookandfeel.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Test JSeparator.
 */
public class TestSeparators {

    /**
     * Creates a new TestSeparators object.
     */
    public TestSeparators() {
        JFrame frame = new JFrame();

        JComponent sep = new TitledSeparator(new JLabel("Foo"));

        frame.add(sep, BorderLayout.NORTH);
        frame.setSize(275, 125);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestSeparators();
            }
        });
    }

    /**
     * Internal class.
     */
    public class TitledSeparator extends JPanel {

        /**
         * Creates a new TitledSeparator object.
         * 
         * @param component
         *            a component.
         */
        public TitledSeparator(Component component) {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            add(component, c);

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.anchor = GridBagConstraints.WEST;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1;
            constraints.insets = new Insets(0, 0, 0, 2);

            add(new JSeparator(), constraints);
        }
    }
}
