/*
 * Copyright (c) 2009 Kathryn Huxtable and Ken Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Kathryn Huxtable
 * 
 */
public class DSeaGlassAll extends JFrame {
    private static final long serialVersionUID = -7179033510099069637L;

    private boolean           isModified       = false;

    public DSeaGlassAll() {
        setTitle("SeaGlass L&F Demo - DSeaGlassAll");

        JMenuBar menuBar = createMenuBar();

        JToolBar toolBar = createToolBar();

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel controlPane = createControlPane();
        tabbedPane.add("Controls", controlPane);

        JSplitPane splitPane = createSplitPane();
        tabbedPane.add("Split", splitPane);

        JSplitPane tableAndList = createTableAndList();
        tabbedPane.add("Table/List", tableAndList);

        setJMenuBar(menuBar);
        // new WindowDragger(this, menuBar);

        add(toolBar, BorderLayout.NORTH);
        // new WindowDragger(this, toolBar);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Build the first menu.
        JMenu menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        // a group of JMenuItems
        JMenuItem menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);

        menuItem = new JMenuItem("Both text and icon", new ImageIcon(DSeaGlassAll.class
            .getResource("/com/seaglass/demo/icons/mailbox.png")));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);

        menuItem = new JMenuItem(new ImageIcon(DSeaGlassAll.class.getResource("/com/seaglass/demo/icons/lock.png")));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);

        // a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        // a group of check box menu items
        menu.addSeparator();
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);

        // a submenu
        menu.addSeparator();
        JMenu submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);

        // Build second menu in the menu bar.
        menu = new JMenu("Another Menu");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
        menuBar.add(menu);

        return menuBar;
    }

    /**
     * @return
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton buttonOne = new JButton(new ImageIcon(DSeaGlassAll.class.getResource("/com/seaglass/demo/icons/mailbox.png")));
        buttonOne.setFocusable(false);
        buttonOne.setToolTipText("A tooltip");

        JToggleButton buttonTwo = new JToggleButton();
        buttonTwo.setAction(new AbstractAction() {
            private static final long serialVersionUID = 5147999623643315036L;

            public void actionPerformed(ActionEvent e) {
                isModified = !isModified;
                getRootPane().putClientProperty("Window.documentModified", isModified);
            }
        });
        buttonTwo.setIcon(new ImageIcon(DSeaGlassAll.class.getResource("/com/seaglass/demo/icons/lock.png")));
        buttonTwo.setFocusable(false);
        buttonTwo.setToolTipText("<html>A long tooltip just to see what happens when we go on and on<br>"
                + "and on until we start foaming at the mouth and fall over backwards.<br>(cf Eric Idle)");

        toolBar.add(buttonOne);
        toolBar.add(buttonTwo);

        toolBar.setFloatable(false);
        return toolBar;
    }

    /**
     * @return
     */
    private JPanel createControlPane() {
        PanelBuilder builder = new PanelBuilder(new FormLayout("4dlu, pref, 8dlu, pref, 4dlu, pref, 4dlu, pref",
            "9dlu, top:pref, 9dlu, center:pref, 9dlu, bottom:pref, 9dlu, fill:pref, 9dlu, pref"), new JPanel());

        String[] patternExamples = {
            "dd MMMMM yyyy",
            "dd.MM.yy",
            "MM/dd/yy",
            "yyyy.MM.dd G 'at' hh:mm:ss z",
            "EEE, MMM d, yy",
            "h:mm a",
            "H:mm:ss:SSS",
            "K:mm a,z",
            "yyyy.MMMMM.dd GGG hh:mm aaa" };

        CellConstraints cc = new CellConstraints();

        int row = 2;
        PanelBuilder buttonBuilder = new PanelBuilder(new FormLayout("4dlu, pref, 8dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref",
            "pref"));
        buttonBuilder.add(new JLabel("Buttons:"), cc.xy(2, 1));
        buttonBuilder.add(new JButton("Standard"), cc.xy(4, 1));
        buttonBuilder.add(new JToggleButton("Toggle"), cc.xy(6, 1));
        buttonBuilder.add(new JRadioButton("Radio"), cc.xy(8, 1));
        buttonBuilder.add(new JCheckBox("Check"), cc.xy(10, 1));
        builder.add(buttonBuilder.getPanel(), cc.xy(2, row));
        row += 2;

        PanelBuilder textBuilder = new PanelBuilder(new FormLayout("4dlu, pref, 8dlu, pref", "pref"));
        textBuilder.add(new JTextField("Sample text"), cc.xy(2, 1));
        textBuilder.add(new JPasswordField("pass"), cc.xy(4, 1));
        builder.add(textBuilder.getPanel(), cc.xy(2, row));
        row += 2;

        PanelBuilder cbAndSliderBuilder = new PanelBuilder(new FormLayout("4dlu, pref, 8dlu, pref", "pref"));
        JComboBox cb = new JComboBox(patternExamples);
        cb.setEditable(true);
        cbAndSliderBuilder.add(cb, cc.xy(2, 1));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 30, 15);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        cbAndSliderBuilder.add(slider, cc.xy(4, 1));
        builder.add(cbAndSliderBuilder.getPanel(), cc.xy(2, row));
        row += 2;

        PanelBuilder spinnerBuilder = new PanelBuilder(new FormLayout("4dlu, pref, 8dlu, pref", "pref"));
        SpinnerModel model = new SpinnerNumberModel(1582, 1500, 1599, 1);
        JSpinner spinner = new JSpinner(model);
        spinnerBuilder.add(spinner, cc.xy(2, 1));
        builder.add(spinnerBuilder.getPanel(), cc.xy(2, row));
        row += 2;

        return builder.getPanel();
    }

    /**
     * @return
     */
    private JSplitPane createSplitPane() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Java Series");
        createNodes(top);
        JTree tree = new JTree(top);
        JScrollPane treeView = new JScrollPane(tree);

        JTextPane textPane = new JTextPane();
        textPane.setText(TEXT);
        textPane.setCaretPosition(0);
        JScrollPane textScrollPane = new JScrollPane(textPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, textScrollPane);
        splitPane.setDividerLocation(175);
        splitPane.setDividerSize(1);
        splitPane.setContinuousLayout(true);
        return splitPane;
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        category = new DefaultMutableTreeNode("Books for Java Programmers");
        top.add(category);

        // original Tutorial
        book = new DefaultMutableTreeNode("The Java Tutorial: A Short Course on the Basics");
        category.add(book);

        // Tutorial Continued
        book = new DefaultMutableTreeNode("The Java Tutorial Continued: The Rest of the JDK");
        category.add(book);

        // JFC Swing Tutorial
        book = new DefaultMutableTreeNode("The JFC Swing Tutorial: A Guide to Constructing GUIs");
        category.add(book);

        // ...add more books for programmers...

        category = new DefaultMutableTreeNode("Books for Java Implementers");
        top.add(category);

        // VM
        book = new DefaultMutableTreeNode("The Java Virtual Machine Specification");
        category.add(book);

        // Language Spec
        book = new DefaultMutableTreeNode("The Java Language Specification");
        category.add(book);
    }

    // private SortableTableModel sortedModel;

    public JSplitPane createTableAndList() {
        Object[][] tableData = new String[][] {
            { "All These Things I Hate (Revolve Around Me)", "Bullet For My Valentine", "The Poison" },
            { "Cries In Vain", "Bullet For My Valentine", "The Poison" },
            { "The End", "Bullet For My Valentine", "The Poison" },
            { "All Around My Hat", "Steeleye Span", "All Around My Hat" },
            { "Alison Gross", "Steeleye Span", "Parcel of Rogues" },
            { "Rogues In A Nation", "Steeleye Span", "Parcel of Rogues" },
            { "Ashes of the Innocent", "Bullet For My Valentine", "Scream Aim Fire" },
            { "Samain", "Steeleye Span", "They Called Her Babylon" },
            { "Teacher", "Jethro Tull", "Benefit" },
            { "To Cry You A Song", "Jethro Tull", "Benefit" },
            { "Suffocating Under Words Of Sorrow (What Can I Do)", "Bullet For My Valentine", "The Poison" },
            { "Aqualung", "Jethro Tull", "Aqualung" },
            { "Sweet Dream", "Jethro Tull", "Living In The Past" },
            { "Somewhere in New Mexico", "Jill Sobule", "Pink Pearl" },
            { "Open the Door", "Magnapop", "Rubbing Doesn't Help" },
            { "Pontiaka", "Boiled In Lead", "Antler Dance" },
            { "Robin's Complaint", "Boiled In Lead", "Antler Dance" } };

        DefaultTableModel tableModel = new DefaultTableModel(tableData, new String[] { "Name", "Artist", "Album" });
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(BorderFactory.createEtchedBorder());

        String[] listData = {
            "All These Things I Hate (Revolve Around Me)",
            "Cries In Vain",
            "The End",
            "All Around My Hat",
            "Alison Gross",
            "Rogues In A Nation",
            "Ashes of the Innocent",
            "Samain",
            "Teacher",
            "To Cry You A Song",
            "Suffocating Under Words Of Sorrow (What Can I Do)",
            "Aqualung",
            "Sweet Dream",
            "Somewhere in New Mexico",
            "Open the Door",
            "Pontiaka",
            "Robin's Complaint", };

        JList list = new JList(listData);

        JScrollPane listPane = new JScrollPane(list);
        listPane.setBorder(BorderFactory.createEtchedBorder());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane, listPane);
        splitPane.setDividerLocation(400);
        splitPane.setContinuousLayout(true);

        return splitPane;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglass.SeaGlassLookAndFeel");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new DSeaGlassAll();
                frame.setSize(650, 300);
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
                                             + "in mi. Phasellus turpis arcu, interdum congue, pulvinar ac, egestas id, "
                                             + "tellus." + "\n\n"
                                             + "Sed faucibus lacinia nibh. Integer ut lorem eu velit lacinia ultricies. "
                                             + "Phasellus vehicula tempor nibh. Duis gravida, sapien ut pellentesque "
                                             + "sodales, leo purus venenatis quam, eu gravida lectus neque vitae felis. Ut "
                                             + "odio. Duis consequat, ligula nec varius ultricies, ipsum diam consequat "
                                             + "purus, non posuere diam ante at purus. Maecenas et libero. Donec sagittis "
                                             + "nibh. Duis quis metus non purus ultrices tempus. Morbi consequat "
                                             + "ullamcorper nunc. Aliquam orci lacus, sagittis sit amet, ultrices ut, "
                                             + "feugiat eget, nunc. Morbi ante dui, bibendum vitae, convallis et, imperdiet "
                                             + "id, pede.";
}
