/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
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
package com.seaglass;

import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.reflect.Constructor;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import com.seaglass.painter.AbstractRegionPainter;
import com.seaglass.painter.ScrollBarScrollBarButtonPainter;
import com.seaglass.painter.ScrollBarScrollBarThumbPainter;
import com.seaglass.painter.SeaGlassIcon;
import com.seaglass.painter.TitlePaneCloseButtonPainter;
import com.seaglass.util.MacPainterFactory;
import com.seaglass.util.PlatformUtils;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.sun.java.swing.plaf.nimbus.NimbusStyle;

public class SeaGlassLookAndFeel extends NimbusLookAndFeel {

    private UIDefaults        uiDefaults = null;

    private SynthStyleFactory nimbusFactory;

    // Install into the UI.
    static {
        UIManager.installLookAndFeel("SeaGlass", "com.seaglass.SeaGlassLookAndFeel");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        nimbusFactory = getStyleFactory();
        // create synth style factory
        setStyleFactory(new SynthStyleFactory() {
            @Override
            public SynthStyle getStyle(JComponent c, Region r) {
                return nimbusFactory.getStyle(c, r);
            }
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    public UIDefaults getDefaults() {
        if (uiDefaults == null) {
            uiDefaults = super.getDefaults();

            Font font = new Font("Lucida Grande", Font.PLAIN, 13);
            if (font == null) {
                font = new Font("Lucida Sans Unicode", Font.PLAIN, 13);
            }

            uiDefaults.put("defaultFont", font);

            String packageName = "com.seaglass.SeaGlass";
            uiDefaults.put("RootPaneUI", packageName + "RootPaneUI");
            uiDefaults.put("ScrollPaneUI", "com.seaglass.SeaGlassScrollPaneUI");

            uiDefaults.put("seaglassBase", new ColorUIResource(20, 40, 110));
            uiDefaults.put("nimbusBase", new ColorUIResource(61, 95, 140));
            // Original control: 220, 233, 239
            uiDefaults.put("control", new ColorUIResource(227, 231, 232));
            uiDefaults.put("scrollbar", new ColorUIResource(255, 255, 255));
            // Original blue grey: 170, 178, 194
            uiDefaults.put("nimbusBlueGrey", new ColorUIResource(214, 218, 228));
            uiDefaults.put("seaglassScrollBarBase", new ColorUIResource(176, 180, 188));
            uiDefaults.put("nimbusSelectionBackground", new ColorUIResource(82, 127, 187));
            uiDefaults.put("nimbusSelection", new ColorUIResource(113, 193, 242));
            uiDefaults.put("nimbusOrange", new ColorUIResource(246, 188, 96));
            uiDefaults.put("nimbusGreen", new ColorUIResource(144, 203, 96));
            uiDefaults.put("nimbusRed", new ColorUIResource(236, 67, 60));

            uiDefaults.put("Table.background", new ColorUIResource(255, 255, 255));
            uiDefaults.put("Table.alternateRowColor", new Color(220, 233, 239));

            uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter", new LazyPainter(
                "com.seaglass.painter.ScrollBarScrollBarButtonPainter",
                ScrollBarScrollBarButtonPainter.FOREGROUND_ENABLED, new Insets(1, 1, 1, 1), new Dimension(25, 15), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
            uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Disabled].foregroundPainter", new LazyPainter(
                "com.seaglass.painter.ScrollBarScrollBarButtonPainter",
                ScrollBarScrollBarButtonPainter.FOREGROUND_DISABLED, new Insets(1, 1, 1, 1), new Dimension(25, 15), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
            uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Pressed].foregroundPainter", new LazyPainter(
                "com.seaglass.painter.ScrollBarScrollBarButtonPainter",
                ScrollBarScrollBarButtonPainter.FOREGROUND_PRESSED, new Insets(1, 1, 1, 1), new Dimension(25, 15), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));

            uiDefaults.put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter", new ScrollBarScrollBarThumbPainter(
                new AbstractRegionPainter.PaintContext(new Insets(0, 15, 0, 15), new Dimension(38, 15), false,
                    AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0),
                ScrollBarScrollBarThumbPainter.BACKGROUND_ENABLED));
            uiDefaults.put("ScrollBar:ScrollBarThumb[Pressed].backgroundPainter", new ScrollBarScrollBarThumbPainter(
                new AbstractRegionPainter.PaintContext(new Insets(0, 15, 0, 15), new Dimension(38, 15), false,
                    AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0),
                ScrollBarScrollBarThumbPainter.BACKGROUND_PRESSED));

            killMouseOver(uiDefaults);

            if (!PlatformUtils.isMac()) {
                setTitlePaneButtons();
            }

            if (PlatformUtils.shouldManuallyPaintTexturedWindowBackground()) {
                uiDefaults.put("ToolBarUI", packageName + "ToolBarUI");
                Object toolBarGradient = MacPainterFactory.createTexturedWindowWorkaroundPainter();
                uiDefaults.put("ToolBar.gradient", toolBarGradient);
            }

            uiDefaults.put("InternalFrame:InternalFrameTitlePane.textForeground", new Color(0, 0, 0));

            if (!PlatformUtils.isMac()) {
                // If we're not on a Mac, draw our own title bar.
                JFrame.setDefaultLookAndFeelDecorated(true);
            } else {
                // If we're on a Mac, use the screen menu bar.
                System.setProperty("apple.laf.useScreenMenuBar", "true");

                // If we're on a Mac, use Aqua for some things.
                try {
                    // Instantiate Aqua but don't install it.
                    Class<?> lnfClass = Class.forName(UIManager.getSystemLookAndFeelClassName(), true, Thread.currentThread()
                        .getContextClassLoader());
                    LookAndFeel aqua = (LookAndFeel) lnfClass.newInstance();
                    UIDefaults aquaDefaults = aqua.getDefaults();

                    // Use Mac key bindings. Nimbus uses Windows keybindings on
                    // Windows and GTK on anything else. We should use Aqua's
                    // bindings on Mac.
                    uiDefaults.put("ComboBox.ancestorInputMap", aquaDefaults.get("ComboBox.ancestorInputMap"));
                    uiDefaults.put("ComboBox.editorInputMap", aquaDefaults.get("ComboBox.editorInputMap"));
                    uiDefaults.put("FormattedTextField.focusInputMap", aquaDefaults.get("FormattedTextField.focusInputMap"));
                    uiDefaults.put("PasswordField.focusInputMap", aquaDefaults.get("PasswordField.focusInputMap"));
                    uiDefaults.put("Spinner.ancestorInputMap", aquaDefaults.get("Spinner.ancestorInputMap"));
                    uiDefaults.put("Spinner.focusInputMap", aquaDefaults.get("Spinner.focusInputMap"));
                    uiDefaults.put("TabbedPane.focusInputMap", aquaDefaults.get("TabbedPane.focusInputMap"));
                    uiDefaults.put("TabbedPane.ancestorInputMap", aquaDefaults.get("TabbedPane.ancestorInputMap"));
                    uiDefaults.put("TabbedPane.wrap.focusInputMap", aquaDefaults.get("TabbedPane.wrap.focusInputMap"));
                    uiDefaults.put("TabbedPane.wrap.ancestorInputMap", aquaDefaults.get("TabbedPane.wrap.ancestorInputMap"));
                    uiDefaults.put("TabbedPane.scroll.focusInputMap", aquaDefaults.get("TabbedPane.scroll.focusInputMap"));
                    uiDefaults.put("TabbedPane.scroll.ancestorInputMap", aquaDefaults.get("TabbedPane.scroll.ancestorInputMap"));
                    uiDefaults.put("TextArea.focusInputMap", aquaDefaults.get("TextArea.focusInputMap"));
                    uiDefaults.put("TextField.focusInputMap", aquaDefaults.get("TextField.focusInputMap"));
                    uiDefaults.put("TextPane.focusInputMap", aquaDefaults.get("TextPane.focusInputMap"));

                    // Use Aqua for any menu UI classes.

                    uiDefaults.put("MenuBarUI", aquaDefaults.get("MenuBarUI"));
                    uiDefaults.put("MenuUI", aquaDefaults.get("MenuUI"));
                    uiDefaults.put("MenuItemUI", aquaDefaults.get("MenuItemUI"));
                    uiDefaults.put("CheckBoxMenuItemUI", aquaDefaults.get("CheckBoxMenuItemUI"));
                    uiDefaults.put("RadioButtonMenuItemUI", aquaDefaults.get("RadioButtonMenuItemUI"));
                    uiDefaults.put("PopupMenuUI", aquaDefaults.get("PopupMenuUI"));

                    // If the Mac paint doesn't exist, use Aqua to paint a
                    // unified toolbar.
                    if (!PlatformUtils.shouldManuallyPaintTexturedWindowBackground()) {
                        uiDefaults.put("ToolBarUI", aquaDefaults.get("ToolBarUI"));
                        uiDefaults.put("ToolBar.border", aquaDefaults.get("ToolBar.border"));
                        uiDefaults.put("ToolBar.background", aquaDefaults.get("ToolBar.background"));
                        uiDefaults.put("ToolBar.foreground", aquaDefaults.get("ToolBar.foreground"));
                        uiDefaults.put("ToolBar.font", aquaDefaults.get("ToolBar.font"));
                        uiDefaults.put("ToolBar.dockingBackground", aquaDefaults.get("ToolBar.dockingBackground"));
                        uiDefaults.put("ToolBar.floatingBackground", aquaDefaults.get("ToolBar.floatingBackground"));
                        uiDefaults.put("ToolBar.dockingForeground", aquaDefaults.get("ToolBar.dockingForeground"));
                        uiDefaults.put("ToolBar.floatingForeground", aquaDefaults.get("ToolBar.floatingForeground"));
                        uiDefaults.put("ToolBar.rolloverBorder", aquaDefaults.get("ToolBar.rolloverBorder"));
                        uiDefaults.put("ToolBar.nonrolloverBorder", aquaDefaults.get("ToolBar.nonrolloverBorder"));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return uiDefaults;
    }

    /**
     * Set the icons to paint the title pane decorations.
     */
    private void setTitlePaneButtons() {
        // Set the multiplicity of states for the Close button.
        uiDefaults.put("TitlePane:seaglassCloseButton.backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_ENABLED));
        uiDefaults.put("TitlePane:seaglassCloseButton[Modified].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_MODIFIED));
        uiDefaults.put("TitlePane:seaglassCloseButton[Unfocused].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        uiDefaults.put("TitlePane:seaglassCloseButton[Unfocused+Modified].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_MODIFIED_WINDOWNOTFOCUSED));
        uiDefaults.put("TitlePane.closeIcon", new SeaGlassIcon("TitlePane:seaglassCloseButton", "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.closeIconModified", new SeaGlassIcon("TitlePane:seaglassCloseButton[Modified]",
            "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.closeIconUnfocused", new SeaGlassIcon("TitlePane:seaglassCloseButton[Unfocused]",
            "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.closeIconUnfocusedModified", new SeaGlassIcon(
            "TitlePane:seaglassCloseButton[Unfocused+Modified]", "backgroundPainter", 19, 18));

        // Set the iconify button
        uiDefaults.put("TitlePane.iconifyIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"", "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.iconifyIconUnfocused", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused]",
            "backgroundPainter", 19, 18));

        // Set the maximize button
        uiDefaults.put("TitlePane.maximizeIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"", "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.maximizeIconUnfocused", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused]",
            "backgroundPainter", 19, 18));

        // Set the minimize button
        uiDefaults.put("TitlePane.minimizeIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized]",
            "backgroundPainter", 19, 18));
        uiDefaults
            .put(
                "TitlePane.minimizeIconUnfocused",
                new SeaGlassIcon(
                    "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused]",
                    "backgroundPainter", 19, 18));
    }

    private void killMouseOver(UIDefaults uiDefaults) {
        // Kill MouseOver on Button.
        uiDefaults.put("Button[Default+MouseOver].backgroundPainter", null);
        uiDefaults.put("Button[Default+Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("Button[MouseOver].backgroundPainter", null);
        uiDefaults.put("Button[Focused+MouseOver].backgroundPainter", null);

        // Kill MouseOver on ToggleButton.
        uiDefaults.put("ToggleButton[MouseOver].backgroundPainter", null);
        uiDefaults.put("ToggleButton[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("ToggleButton[MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("ToggleButton[Focused+MouseOver+Selected].backgroundPainter", null);

        // Initialize RadioButton
        uiDefaults.put("RadioButton[MouseOver].iconPainter", null);
        uiDefaults.put("RadioButton[Focused+MouseOver].iconPainter", null);
        uiDefaults.put("RadioButton[MouseOver+Selected].iconPainter", null);
        uiDefaults.put("RadioButton[Focused+MouseOver+Selected].iconPainter", null);

        // Initialize CheckBox
        uiDefaults.put("CheckBox[MouseOver].iconPainter", null);
        uiDefaults.put("CheckBox[Focused+MouseOver].iconPainter", null);
        uiDefaults.put("CheckBox[MouseOver+Selected].iconPainter", null);
        uiDefaults.put("CheckBox[Focused+MouseOver+Selected].iconPainter", null);

        // Initialize ComboBox
        uiDefaults.put("ComboBox[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("ComboBox[MouseOver].backgroundPainter", null);
        uiDefaults.put("ComboBox[Editable+MouseOver].backgroundPainter", null);
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", null);
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", null);

        // Initialize InternalFrame
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver].iconPainter", null);
        uiDefaults.put(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver+WindowNotFocused].iconPainter",
            null);
        uiDefaults.put(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver].backgroundPainter", null);
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized].backgroundPainter",
                null);
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter",
                null);
        uiDefaults.put(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver].backgroundPainter", null);
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver].backgroundPainter",
            null);
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);

        // Initialize MenuItem
        uiDefaults.put("MenuItem[MouseOver].textForeground", null);
        uiDefaults.put("MenuItem[MouseOver].backgroundPainter", null);
        uiDefaults.put("MenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize RadioButtonMenuItem
        uiDefaults.put("RadioButtonMenuItem[MouseOver].textForeground", null);
        uiDefaults.put("RadioButtonMenuItem[MouseOver].backgroundPainter", null);
        uiDefaults.put("RadioButtonMenuItem[MouseOver+Selected].textForeground", null);
        uiDefaults.put("RadioButtonMenuItem[MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("RadioButtonMenuItem[MouseOver+Selected].checkIconPainter", null);
        uiDefaults.put("RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize CheckBoxMenuItem
        uiDefaults.put("CheckBoxMenuItem[MouseOver].textForeground", null);
        uiDefaults.put("CheckBoxMenuItem[MouseOver].backgroundPainter", null);
        uiDefaults.put("CheckBoxMenuItem[MouseOver+Selected].textForeground", null);
        uiDefaults.put("CheckBoxMenuItem[MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("CheckBoxMenuItem[MouseOver+Selected].checkIconPainter", null);
        uiDefaults.put("CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize Menu
        uiDefaults.put("Menu:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize ScrollBar
        uiDefaults.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", null);
        uiDefaults.put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter", uiDefaults
            .get("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter"));

        // Initialize Slider
        uiDefaults.put("Slider:SliderThumb[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("Slider:SliderThumb[MouseOver].backgroundPainter", uiDefaults
            .get("Slider:SliderThumb[Enabled].backgroundPainter"));
        uiDefaults.put("Slider:SliderThumb[ArrowShape+MouseOver].backgroundPainter", null);
        uiDefaults.put("Slider:SliderThumb[ArrowShape+Focused+MouseOver].backgroundPainter", null);

        // Initialize Spinner
        uiDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].backgroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].foregroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].foregroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].backgroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].foregroundPainter", null);
        uiDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].foregroundPainter", null);

        // Initialize TabbedPane
        uiDefaults.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", null);
        uiDefaults.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter", null);

        // Initialize TableHeader
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter", null);

        // Initialize ToolBar
        uiDefaults.put("ToolBar:Button[MouseOver].backgroundPainter", null);
        uiDefaults.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", null);
        uiDefaults.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", null);
        uiDefaults.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", null);
        uiDefaults.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", null);
    }

    /**
     * Gets the style associated with the given component and region. This will
     * never return null. If an appropriate component and region cannot be
     * determined, then a default style is returned.
     * 
     * @param c
     *            a non-null reference to a JComponent
     * @param r
     *            a non-null reference to the region of the component c
     * @return a non-null reference to a NimbusStyle.
     */
    public static NimbusStyle getStyle(JComponent c, Region r) {
        return (NimbusStyle) SynthLookAndFeel.getStyle(c, r);
    }

    /**
     * Return a short string that identifies this look and feel. This String
     * will be the unquoted String "Nimbus".
     * 
     * @return a short string identifying this look and feel.
     */
    @Override
    public String getName() {
        return "SeaGlass";
    }

    /**
     * Return a string that identifies this look and feel. This String will be
     * the unquoted String "Nimbus".
     * 
     * @return a short string identifying this look and feel.
     */
    @Override
    public String getID() {
        return "SeaGlass";
    }

    /**
     * Returns a textual description of this look and feel.
     * 
     * @return textual description of this look and feel.
     */
    @Override
    public String getDescription() {
        return "SeaGlass Look and Feel";
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNativeLookAndFeel() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean getSupportsWindowDecorations() {
        if (PlatformUtils.isMac()) {
            return false;
        }
        return true;
    }

    /**
     * Package private method which returns either BorderLayout.NORTH,
     * BorderLayout.SOUTH, BorderLayout.EAST, or BorderLayout.WEST depending on
     * the location of the toolbar in its parent. The toolbar might be in
     * PAGE_START, PAGE_END, CENTER, or some other position, but will be
     * resolved to either NORTH,SOUTH,EAST, or WEST based on where the toolbar
     * actually IS, with CENTER being NORTH.
     * 
     * This code is used to determine where the border line should be drawn by
     * the custom toolbar states, and also used by NimbusIcon to determine
     * whether the handle icon needs to be shifted to look correct.
     * 
     * Toollbars are unfortunately odd in the way these things are handled, and
     * so this code exists to unify the logic related to toolbars so it can be
     * shared among the static files such as NimbusIcon and generated files such
     * as the ToolBar state classes.
     */
    public static Object resolveToolbarConstraint(JToolBar toolbar) {
        // NOTE: we don't worry about component orientation or PAGE_END etc
        // because the BasicToolBarUI always uses an absolute position of
        // NORTH/SOUTH/EAST/WEST.
        if (toolbar != null) {
            Container parent = toolbar.getParent();
            if (parent != null) {
                LayoutManager m = parent.getLayout();
                if (m instanceof BorderLayout) {
                    BorderLayout b = (BorderLayout) m;
                    Object con = b.getConstraints(toolbar);
                    if (con == SOUTH || con == EAST || con == WEST) {
                        return con;
                    }
                    return NORTH;
                }
            }
        }
        return NORTH;
    }

    /**
     * This class is private because it relies on the constructor of the
     * auto-generated AbstractRegionPainter subclasses. Hence, it is not
     * generally useful, and is private.
     * <p/>
     * LazyPainter is a LazyValue class. It will create the
     * AbstractRegionPainter lazily, when asked. It uses reflection to load the
     * proper class and invoke its constructor.
     */
    private static final class LazyPainter implements UIDefaults.LazyValue {
        private int which;
        private AbstractRegionPainter.PaintContext ctx;
        private String className;

        LazyPainter(String className, int which, Insets insets,
                    Dimension canvasSize, boolean inverted) {
            if (className == null) {
                throw new IllegalArgumentException(
                        "The className must be specified");
            }

            this.className = className;
            this.which = which;
            this.ctx = new AbstractRegionPainter.PaintContext(
                insets, canvasSize, inverted);
        }

        LazyPainter(String className, int which, Insets insets,
                    Dimension canvasSize, boolean inverted,
                    AbstractRegionPainter.PaintContext.CacheMode cacheMode,
                    double maxH, double maxV) {
            if (className == null) {
                throw new IllegalArgumentException(
                        "The className must be specified");
            }

            this.className = className;
            this.which = which;
            this.ctx = new AbstractRegionPainter.PaintContext(
                    insets, canvasSize, inverted, cacheMode, maxH, maxV);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object createValue(UIDefaults table) {
            try {
                Class c;
                Object cl;
                // See if we should use a separate ClassLoader
                if (table == null || !((cl = table.get("ClassLoader"))
                                       instanceof ClassLoader)) {
                    cl = Thread.currentThread().
                                getContextClassLoader();
                    if (cl == null) {
                        // Fallback to the system class loader.
                        cl = ClassLoader.getSystemClassLoader();
                    }
                }

                c = Class.forName(className, true, (ClassLoader)cl);
                Constructor constructor = c.getConstructor(
                        AbstractRegionPainter.PaintContext.class, int.class);
                if (constructor == null) {
                    throw new NullPointerException(
                            "Failed to find the constructor for the class: " +
                            className);
                }
                return constructor.newInstance(ctx, which);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
