/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id$
 */
package com.seaglasslookandfeel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import com.seaglasslookandfeel.painter.SeaGlassPainter;

/**
 * SeaGlassBrowser. Generates a web page displaying the controls and defaults
 * used by the Sea Glass L&F.
 * 
 * Based on NimbusBrowser by Jasper Potts, available at various places on the
 * web.
 */
public class SeaGlassBrowser {

    private static Set<String> NIMBUS_PRIMARY_COLORS   = new HashSet<String>(Arrays.asList("text", "control", "seaGlassBase",
                                                           "seaGlassOrange", "seaGlassGreen", "seaGlassRed", "seaGlassInfoBlue",
                                                           "seaGlassAlertYellow", "seaGlassFocus", "seaGlassSelectedText",
                                                           "seaGlassSelectionBackground", "seaGlassDisabledText",
                                                           "seaGlassLightBackground", "info", "seaGlassButton1", "seaGlassButton1c",
                                                           "seaGlassButton2", "seaGlassButton2c", "seaGlassScrollBarButtonBase",
                                                           "seaGlassScrollBarTrackBase", "seaGlassScrollThumbBase",
                                                           "seaGlassSplitPaneDividerBase"));
    private static Set<String> NIMBUS_SECONDARY_COLORS = new HashSet<String>(Arrays.asList("textForeground", "textBackground",
                                                           "background", "seaGlassBlueGrey", "seaGlassBorder", "seaGlassSelection",
                                                           "infoText", "menuText", "menu", "scrollbar", "controlText",
                                                           "controlHighlight", "controlLHighlight", "controlShadow",
                                                           "controlDkShadow", "textHighlight", "textHighlightText",
                                                           "textInactiveText", "desktop", "activeCaption", "inactiveCaption"));
    private static String[]    NIMBUS_COMPONENTS       = new String[] {
        "ArrowButton",
        "Button",
        "ToggleButton",
        "RadioButton",
        "CheckBox",
        "ColorChooser",
        "ComboBox",
        "\"ComboBox.scrollPane\"",
        "FileChooser",
        "InternalFrameTitlePane",
        "InternalFrame",
        "DesktopIcon",
        "DesktopPane",
        "Label",
        "List",
        "MenuBar",
        "MenuItem",
        "RadioButtonMenuItem",
        "CheckBoxMenuItem",
        "Menu",
        "PopupMenu",
        "PopupMenuSeparator",
        "OptionPane",
        "Panel",
        "ProgressBar",
        "Separator",
        "ScrollBar",
        "ScrollPane",
        "Viewport",
        "Slider",
        "Spinner",
        "SplitPane",
        "TabbedPane",
        "Table",
        "TableHeader",
        "\"Table.editor\"",
        "\"Tree.cellEditor\"",
        "TextField",
        "FormattedTextField",
        "PasswordField",
        "TextArea",
        "TextPane",
        "EditorPane",
        "ToolBar",
        "ToolBarSeparator",
        "ToolTip",
        "Tree",
        "RootPane"                                    };

    // private static String DEFAULT_FONT = "defaultFont";

    private static File        IMAGES_DIR;
    private static int         IMAGE_COUNT             = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeel laf = new SeaGlassLookAndFeel();
                try {
                    UIManager.setLookAndFeel(laf);
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
                // get defaults
                UIDefaults defaults = UIManager.getLookAndFeelDefaults();
                // split of components
                Map<String, Map<String, Object>> componentDefaults = new HashMap<String, Map<String, Object>>();
                Map<String, Object> others = new HashMap<String, Object>();
                Object[] defaultKeys = defaults.keySet().toArray();
                for (Object keyObj : defaultKeys) {
                    String key = keyObj.toString();
                    if (key.contains("MouseOver")) {
                        continue;
                    }
                    boolean matchesComponent = false;
                    componentloop: for (String componentName : NIMBUS_COMPONENTS) {
                        if (key.startsWith(componentName + ".") || key.startsWith(componentName + ":")
                                || key.startsWith(componentName + "[")) {
                            Map<String, Object> keys = componentDefaults.get(componentName);
                            if (keys == null) {
                                keys = new HashMap<String, Object>();
                                componentDefaults.put(componentName, keys);
                            }
                            keys.put(key, defaults.get(key));
                            matchesComponent = true;
                            break componentloop;
                        }
                    }
                    if (!matchesComponent) others.put(key, defaults.get(key));
                }
                // split out primary, secondary colors
                Map<String, Object> primaryColors = new HashMap<String, Object>();
                Map<String, Object> secondaryColors = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : others.entrySet()) {
                    if (NIMBUS_PRIMARY_COLORS.contains(entry.getKey())) {
                        primaryColors.put(entry.getKey(), (Color) entry.getValue());
                    }
                    if (NIMBUS_SECONDARY_COLORS.contains(entry.getKey())) {
                        secondaryColors.put(entry.getKey(), (Color) entry.getValue());
                    }
                }
                for (String key : NIMBUS_PRIMARY_COLORS) {
                    others.remove(key);
                }
                for (String key : NIMBUS_SECONDARY_COLORS) {
                    others.remove(key);
                }
                // split out UIs
                Map<String, Object> uiClasses = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : others.entrySet()) {
                    if (entry.getKey().endsWith("UI")) {
                        uiClasses.put(entry.getKey(), entry.getValue());
                    }
                }
                for (String key : uiClasses.keySet()) {
                    others.remove(key);
                }
                // write html file
                try {
                    File dir = new File("target/browser");
                    dir.mkdir();
                    IMAGES_DIR = new File(dir, "images");
                    IMAGES_DIR.mkdir();
                    System.out.println("Outputing to " + dir.getAbsolutePath());
                    File htmlFile = new File(dir, "seaGlass.html");
                    PrintWriter html = new PrintWriter(htmlFile);

                    html.println("<html>");
                    html.println("<head>");
                    html.println("<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />");
                    html.println("</head>");
                    html.println("<body>");
                    html.println("<h1>Primary Colors</h1>");
                    printTable(html, primaryColors);

                    html.println("<h1>Secondary Colors</h1>");
                    printTable(html, secondaryColors);

                    html.println("<h1>Components</h1>");
                    for (Map.Entry<String, Map<String, Object>> entry : componentDefaults.entrySet()) {
                        html.println("<h2>" + entry.getKey() + "</h2>");
                        printTable(html, entry.getValue());
                    }

                    html.println("<h1>Others</h1>");
                    printTable(html, others);

                    html.println("<h1>UI Classes</h1>");
                    printTable(html, uiClasses);

                    html.println("</body>");
                    html.println("</html>");

                    html.flush();
                    html.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static void printTable(PrintWriter html, Map<String, Object> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        html.println("<table>");
        html.println("<tr><th>Key</th><th>Value</th><th>Preview</th></tr>");
        for (String key : keys) {
            printRow(html, key, map.get(key));
        }
        html.println("</table>");
    }

    static void printRow(PrintWriter html, String key, Object value) {
        html.println("<tr>");
        // key
        html.println("<td width=\"250\"><code>" + key + "</code></td>");
        if (value instanceof Color) {
            printColor(html, (Color) value);
        } else if (value instanceof Font) {
            printFont(html, (Font) value);
        } else if (value instanceof Dimension) {
            printDimension(html, (Dimension) value);
        } else if (value instanceof Insets) {
            printInsets(html, (Insets) value);
        } else if (value instanceof Border) {
            printBorder(html, (Border) value);
        } else if (value instanceof SeaGlassPainter) {
            printPainter(html, (SeaGlassPainter<?>) value);
        } else if (value instanceof InputMap) {
            // ignore, not intresting
            html.println("<td>&nbsp;</td>");
            html.println("<td>&nbsp;</td>");
        } else if (value instanceof Icon) {
            printIcon(html, (Icon) value);
        } else {
            html.println("<td>" + (value == null ? "no value" : value.toString()) + "</td>");
            html.println("<td>&nbsp;</td>");
        }
        html.println("</tr>");
    }

    static void printColor(PrintWriter html, Color color) {
        html.println("<td><pre>#" + getWebColor(color) + " (" + color.getRed() + "," + color.getGreen() + "," + color.getBlue()
                + ")</pre></td>");
        html.println("<td width=\"100\" bgcolor=\"#" + getWebColor(color) + "\">&nbsp;</td>");
    }

    static void printPainter(PrintWriter html, SeaGlassPainter<?> painter) {
        html.println("<td>Painter</td>");
        int w = 25, h = 25;
        try {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, w, h);
            g2.setComposite(old);
            painter.paint(g2, null, w, h);
            g2.dispose();
            html.println("<td>" + saveImage(img));
            html.println("</td>");
        } catch (Exception e) {
            // e.printStackTrace();
            html.println("<td>NPE&nbsp;</td>");
        }
    }

    static void printFont(PrintWriter html, Font font) {
        String style = "";
        if (font.isBold() && font.isItalic()) {
            style = "Bold & Italic";
        } else if (font.isBold()) {
            style = "Bold";
        } else if (font.isItalic()) {
            style = "Italic";
        }
        html.println("<td>Font: " + font.getFamily() + " " + font.getSize() + " " + style + "</td>");
        int w = 300, h = 30;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, w, h);
        g2.setComposite(old);
        g2.setColor(Color.BLACK);
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("The quick brown fox jumps over the lazy dog.", 5, 20);
        g2.dispose();
        html.println("<td>" + saveImage(img) + "</td>");
    }

    static void printInsets(PrintWriter html, Insets insets) {
        html.println("<td>Insets (" + insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right + ")</pre></td>");
        int w = 50 + insets.left + insets.right;
        int h = 20 + insets.top + insets.bottom;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, w, h);
        g2.setComposite(old);
        g2.setColor(Color.BLACK);
        g2.drawRect(insets.left, insets.top, 49, 19);
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, w - 1, h - 1);
        g2.dispose();
        html.println("<td>" + saveImage(img) + "</td>");
    }

    static void printBorder(PrintWriter html, Border border) {
        Insets insets = border.getBorderInsets(new JToolBar());
        html.println("<td>Border Insets(" + insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right
                + ")</pre></td>");
        int w = 50 + insets.left + insets.right;
        int h = 20 + insets.top + insets.bottom;
        try {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, w, h);
            g2.setComposite(old);
            g2.setColor(Color.RED);
            g2.fillRect(insets.left, insets.top, 49, 19);
            border.paintBorder(null, g2, 0, 0, w, h);
            g2.dispose();
            html.println("<td>" + saveImage(img) + "</td>");
        } catch (Exception e) {
            // e.printStackTrace();
            html.println("<td>NPE&nbsp;</td>");
        }
    }

    static void printDimension(PrintWriter html, Dimension dim) {
        html.println("<td>Dimension (" + dim.width + "," + dim.height + ")</pre></td>");
        int w = dim.width;
        int h = dim.height;
        if (w == 0 || h == 0) {
            html.println("<td>&nbsp;</td>");
        } else {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, w, h);
            g2.setComposite(old);
            g2.setColor(Color.RED);
            g2.drawRect(0, 0, w - 1, h - 1);
            g2.dispose();
            html.println("<td>" + saveImage(img) + "</td>");
        }
    }

    static void printIcon(PrintWriter html, Icon icon) {
        html.println("<td>Icon " + icon.getIconWidth() + " x " + icon.getIconHeight() + "</pre></td>");
        BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        g2.setComposite(old);
        JButton foo = new JButton();
        icon.paintIcon(foo, g2, 0, 0);
        g2.dispose();
        html.println("<td>" + saveImage(img) + "</td>");
    }

    static String saveImage(BufferedImage img) {
        File imgFile = new File(IMAGES_DIR, "img_" + (IMAGE_COUNT++) + ".png");
        try {
            ImageIO.write(img, "png", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<img src=\"images/" + imgFile.getName() + "\">";
    }

    static String getWebColor(Color color) {
        String result = "";
        String num;
        num = Integer.toHexString(color.getRed());
        if (num.length() == 1) num = "0" + num;
        result += num;
        num = Integer.toHexString(color.getGreen());
        if (num.length() == 1) num = "0" + num;
        result += num;
        num = Integer.toHexString(color.getBlue());
        if (num.length() == 1) num = "0" + num;
        result += num;
        return result;
    }

}
