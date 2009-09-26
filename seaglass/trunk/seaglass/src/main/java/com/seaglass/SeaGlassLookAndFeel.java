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
import java.awt.Component;
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
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import com.seaglass.painter.AbstractRegionPainter;
import com.seaglass.painter.ButtonPainter;
import com.seaglass.painter.CheckBoxPainter;
import com.seaglass.painter.ComboBoxArrowButtonPainter;
import com.seaglass.painter.ComboBoxPainter;
import com.seaglass.painter.RadioButtonPainter;
import com.seaglass.painter.ScrollBarButtonPainter;
import com.seaglass.painter.ScrollBarThumbPainter;
import com.seaglass.painter.ScrollBarTrackPainter;
import com.seaglass.painter.SplitPaneDividerPainter;
import com.seaglass.painter.TableHeaderRendererPainter;
import com.seaglass.painter.TitlePaneCloseButtonPainter;
import com.seaglass.painter.TitlePaneIconifyButtonPainter;
import com.seaglass.painter.TitlePaneMaximizeButtonPainter;
import com.seaglass.painter.ToolBarButtonPainter;
import com.seaglass.painter.ToolBarPainter;
import com.seaglass.painter.ToolBarToggleButtonPainter;
import com.seaglass.util.PlatformUtils;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.sun.java.swing.plaf.nimbus.NimbusStyle;

public class SeaGlassLookAndFeel extends NimbusLookAndFeel {

    // Set the package name.
    private static final String PACKAGE_PREFIX = "com.seaglass.SeaGlass";

    private UIDefaults          uiDefaults     = null;

    private SynthStyleFactory   nimbusFactory;

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
                SynthStyle style = nimbusFactory.getStyle(c, r);
                if (style instanceof NimbusStyle) {
                    return new SeaGlassStyle(style);
                } else {
                    // Why are toolbars getting here?
                    return new SeaGlassStyle(style);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninitialize() {
        JFrame.setDefaultLookAndFeelDecorated(false);
        super.uninitialize();
    }

    /**
     * @inheritDoc
     */
    @Override
    public UIDefaults getDefaults() {
        if (uiDefaults == null) {
            uiDefaults = super.getDefaults();

            // Set the default font.
            initializeDefaultFont();

            // Override the root pane and scroll pane behavior.
            uiDefaults.put("RootPaneUI", PACKAGE_PREFIX + "RootPaneUI");
            uiDefaults.put("ScrollPaneUI", PACKAGE_PREFIX + "ScrollPaneUI");

            // Set base colors.
            initializeBaseColors();

            initializeButtons();

//            initializeComboBoxes();

            initializeTables();

            initializeScrollBars();

            initializeSplitPanes();

            eliminateMouseOverBehavior(uiDefaults);

            if (!PlatformUtils.isMac()) {
                initializeTitlePaneButtons();
            }

            if (PlatformUtils.shouldManuallyPaintTexturedWindowBackground()) {
                initializeToolBars();
            }

            uiDefaults.put("InternalFrame:InternalFrameTitlePane.textForeground", new Color(0, 0, 0));

            if (!PlatformUtils.isMac()) {
                // If we're not on a Mac, draw our own title bar.
                JFrame.setDefaultLookAndFeelDecorated(true);
            } else {
                // If we're on a Mac, use the screen menu bar.
                System.setProperty("apple.laf.useScreenMenuBar", "true");

                // If we're on a Mac, use Aqua for some things.
                initializeAquaSettings();
            }
        }
        return uiDefaults;
    }

    /**
     * Initialize the default font.
     */
    private void initializeDefaultFont() {
        // Set the default font to Lucida Grande if available, else use
        // Lucida Sans Unicode. Grande is a later font and a bit nicer
        // looking, but it is a derivation of Sans Unicode, so they're
        // compatible.
        Font font = null;
        if (PlatformUtils.isMac()) {
            font = new Font("Lucida Grande", Font.PLAIN, 13);
        }
        if (font == null) {
            font = new Font("Lucida Sans Unicode", Font.PLAIN, 13);
        }
        uiDefaults.put("defaultFont", font);
    }

    /**
     * Initialize the base colors.
     */
    private void initializeBaseColors() {
        uiDefaults.put("nimbusBase", new ColorUIResource(61, 95, 140));
        // Original control: 220, 233, 239
        uiDefaults.put("control", new ColorUIResource(231, 239, 243));
        uiDefaults.put("scrollbar", new ColorUIResource(255, 255, 255));
        // Original blue grey: 170, 178, 194
        uiDefaults.put("nimbusBlueGrey", new ColorUIResource(214, 218, 228));
        uiDefaults.put("seaglassScrollThumbBase", new ColorUIResource(90, 150, 255));
        uiDefaults.put("seaglassScrollBarButtonBase", new ColorUIResource(170, 174, 182));
        uiDefaults.put("seaglassScrollBarTrackBase", new ColorUIResource(205, 208, 218));
        uiDefaults.put("seaglassSplitPaneDividerBase", new ColorUIResource(170, 174, 182));
        uiDefaults.put("nimbusSelectionBackground", new ColorUIResource(82, 127, 187));
        uiDefaults.put("nimbusSelection", new ColorUIResource(113, 193, 242));
        uiDefaults.put("nimbusOrange", new ColorUIResource(246, 188, 96));
        uiDefaults.put("nimbusGreen", new ColorUIResource(144, 203, 96));
        uiDefaults.put("nimbusRed", new ColorUIResource(236, 67, 60));
        uiDefaults.put("seaglassButton1", new ColorUIResource(175, 207, 232));
        uiDefaults.put("seaglassButton1c", new ColorUIResource(160, 247, 255));
        uiDefaults.put("seaglassButton2", new ColorUIResource(16, 59, 116));
        uiDefaults.put("seaglassButton2c", new ColorUIResource(46, 194, 226));
    }

    /**
     * Use Aqua settings for some properties if we're on a Mac.
     */
    private void initializeAquaSettings() {
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

    /**
     * Initialize the tool bar settings.
     */
    private void initializeToolBars() {
        uiDefaults.put("ToolBar[North].borderPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.BORDER_NORTH, new Insets(0, 0, 1, 0), new Dimension(30, 30), false,
            AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        uiDefaults.put("ToolBar[South].borderPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.BORDER_SOUTH, new Insets(1, 0, 0, 0), new Dimension(30, 30), false,
            AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        uiDefaults.put("ToolBar[East].borderPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.BORDER_EAST, new Insets(1, 0, 0, 0), new Dimension(30, 30), false,
            AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        uiDefaults.put("ToolBar[West].borderPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.BORDER_WEST, new Insets(0, 0, 1, 0), new Dimension(30, 30), false,
            AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        uiDefaults.put("ToolBar[Enabled].handleIconPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.HANDLEICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(11, 38), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));

        uiDefaults.put("ToolBar:Button[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter", ToolBarButtonPainter.BACKGROUND_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:Button[MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter", ToolBarButtonPainter.BACKGROUND_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter", ToolBarButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:Button[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter", ToolBarButtonPainter.BACKGROUND_PRESSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:Button[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter", ToolBarButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));

        uiDefaults.put("ToolBar:ToggleButton[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_SELECTED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToolBar:ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_DISABLED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
}

    /**
     * Initialize button settings.
     */
    private void initializeButtons() {
        // Initialize Button
        uiDefaults.put("Button[Default].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DEFAULT, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Default+Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DEFAULT_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Default+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_DEFAULT, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Default+Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_ENABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("Button[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        // Initialize ToggleButton
        uiDefaults.put("ToggleButton[Selected].textForeground", new ColorUIResource(255, 255, 255));
        uiDefaults.put("ToggleButton[Focused+Selected].textForeground", new ColorUIResource(255, 255, 255));
        uiDefaults.put("ToggleButton[Disabled+Selected].textForeground", new ColorUIResource(255, 255, 255));
        uiDefaults.put("ToggleButton[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_ENABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_SELECTED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ButtonPainter", ButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, new Insets(7, 7, 7, 7),
            new Dimension(86, 28), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        uiDefaults.put("ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        // Initialize CheckBox
        uiDefaults.put("CheckBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uiDefaults.put("CheckBox[Disabled].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_DISABLED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Enabled].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused+MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Pressed].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused+Pressed].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused+Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Focused+MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox[Disabled+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_DISABLED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("CheckBox.icon", new SeaGlassIcon("CheckBox", "iconPainter", 16, 19));

        // Initialize RadioButton
        uiDefaults.put("RadioButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uiDefaults.put("RadioButton[Disabled].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_DISABLED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Enabled].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused+MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Pressed].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused+Pressed].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused+Pressed+Selected].iconPainter", new LazyPainter(
            "com.seaglass.painter.RadioButtonPainter", RadioButtonPainter.ICON_PRESSED_SELECTED_FOCUSED, new Insets(5, 5, 5, 5),
            new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Focused+MouseOver+Selected].iconPainter", new LazyPainter(
            "com.seaglass.painter.RadioButtonPainter", RadioButtonPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, new Insets(5, 5, 5, 5),
            new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton[Disabled+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_DISABLED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("RadioButton.icon", new SeaGlassIcon("RadioButton", "iconPainter", 16, 16));
    }

    /**
     * Initialize the combo box settings.
     */
    private void initializeComboBoxes() {
        uiDefaults.put("seaglassComboBoxBase", new ColorUIResource(61, 95, 140));
        uiDefaults.put("seaglassComboBoxBlueGrey", new ColorUIResource(175, 207, 232));

        uiDefaults.put("ComboBox[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Disabled+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED_PRESSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Focused+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Enabled+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_SELECTED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Disabled+Editable].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED_EDITABLE, new Insets(6, 5, 6, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Editable+Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(6, 5, 6, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Editable+Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED_EDITABLE, new Insets(5, 5, 5, 5), new Dimension(142, 27), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Editable+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(4, 5, 5, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox[Editable+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED_EDITABLE, new Insets(4, 5, 5, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));

        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_DISABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(23, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(23, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(23, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_PRESSED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(23, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_SELECTED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(23, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_ENABLED, new Insets(6, 9, 6,
                10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_ENABLED, new Insets(6, 9, 6,
                10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_DISABLED, new Insets(6, 9, 6,
                10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Pressed].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_PRESSED, new Insets(6, 9, 6,
                10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        uiDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Selected].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_SELECTED, new Insets(6, 9, 6,
                10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
    }

    /**
     * Initialize the table UI settings.
     */
    private void initializeTables() {
        uiDefaults.put("Table.background", new ColorUIResource(255, 255, 255));
        uiDefaults.put("Table.alternateRowColor", new Color(220, 233, 239));
        uiDefaults.put("seaglassTableHeaderBase", new Color(49, 77, 113));
        uiDefaults.put("seaglassTableHeaderBlueGrey", new Color(200, 214, 247));
        uiDefaults.put("seaglassTableHeaderFocus", new Color(77, 146, 209));

        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_DISABLED, new Insets(5, 5, 5,
                5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED,
            new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED, new Insets(5,
                5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        // uiDefaults.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter",
        // new LazyPainter(
        // "com.seaglass.painter.TableHeaderRendererPainter",
        // TableHeaderRendererPainter.BACKGROUND_MOUSEOVER, new Insets(5, 5, 5,
        // 5), new Dimension(22, 20), false,
        // AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
        // Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_PRESSED,
            new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_SORTED, new Insets(5,
                5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED_SORTED,
            new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_DISABLED_SORTED, new Insets(5,
                5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /**
     * Initialize the scroll bar UI settings.
     */
    private void initializeScrollBars() {
        uiDefaults.put("ScrollBar.incrementButtonGap", new Integer(-5));
        uiDefaults.put("ScrollBar.decrementButtonGap", new Integer(-5));
        uiDefaults.put("ScrollBar:\"ScrollBar.button\".size", new Integer(20));
        uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_ENABLED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Disabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_DISABLED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_MOUSEOVER, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        uiDefaults.put("ScrollBar:\"ScrollBar.button\"[Pressed].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_PRESSED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));

        uiDefaults.put("ScrollBar:ScrollBarThumb.States", "Disabled,Enabled,Focused,MouseOver,Pressed");
        uiDefaults.put("ScrollBar:ScrollBarThumb[Disabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarThumbPainter", ScrollBarThumbPainter.BACKGROUND_DISABLED, new Insets(0, 8, 0, 8),
            new Dimension(82, 14), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        uiDefaults.put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarThumbPainter", ScrollBarThumbPainter.BACKGROUND_ENABLED, new Insets(0, 8, 0, 8),
            new Dimension(82, 14), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        uiDefaults.put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarThumbPainter", ScrollBarThumbPainter.BACKGROUND_PRESSED, new Insets(0, 8, 0, 8),
            new Dimension(82, 14), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        uiDefaults.put("ScrollBar:ScrollBarThumb[Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarThumbPainter", ScrollBarThumbPainter.BACKGROUND_PRESSED, new Insets(0, 8, 0, 8),
            new Dimension(82, 14), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));

        uiDefaults.put("ScrollBar:ScrollBarTrack[Disabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarTrackPainter", ScrollBarTrackPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0),
            new Dimension(19, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        uiDefaults.put("ScrollBar:ScrollBarTrack[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarTrackPainter", ScrollBarTrackPainter.BACKGROUND_ENABLED, new Insets(0, 0, 0, 0),
            new Dimension(19, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
    }

    /**
     * Initialize the split pane UI settings.
     */
    private void initializeSplitPanes() {
        uiDefaults.put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.BACKGROUND_ENABLED, new Insets(3, 0, 3, 0),
            new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        uiDefaults.put("SplitPane:SplitPaneDivider[Focused].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.BACKGROUND_FOCUSED, new Insets(3, 0, 3, 0),
            new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        uiDefaults.put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.FOREGROUND_ENABLED, new Insets(0, 24, 0, 24),
            new Dimension(68, 10), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        uiDefaults.put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.FOREGROUND_ENABLED_VERTICAL, new Insets(5, 0,
                5, 0), new Dimension(10, 38), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /**
     * Set the icons to paint the title pane decorations.
     */
    private void initializeTitlePaneButtons() {
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
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter", TitlePaneIconifyButtonPainter.BACKGROUND_ENABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                TitlePaneIconifyButtonPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter", TitlePaneIconifyButtonPainter.BACKGROUND_PRESSED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                    TitlePaneIconifyButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                    TitlePaneIconifyButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("TitlePane.iconifyIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"", "backgroundPainter", 19, 18));
        uiDefaults.put("TitlePane.iconifyIconUnfocused", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused]",
            "backgroundPainter", 19, 18));

        // Set the maximize button
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_DISABLED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_DISABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_ENABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_PRESSED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter",
                    TitlePaneCloseButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19, 18),
                    false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        uiDefaults
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter",
                    TitlePaneCloseButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19, 18),
                    false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
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

    private void eliminateMouseOverBehavior(UIDefaults uiDefaults) {
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
     * Gets the style associated with the given component and region. This
     * will never return null. If an appropriate component and region cannot
     * be determined, then a default style is returned.
     * 
     * @param c a non-null reference to a JComponent
     * @param r a non-null reference to the region of the component c
     * @return a non-null reference to a NimbusStyle.
     */
//    public static NimbusStyle getStyle(JComponent c, Region r) {
//        System.out.println("Getting NimbusStyle");
//        return (NimbusStyle)SynthLookAndFeel.getStyle(c, r);
//    }

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
     * A convenience method to return where the foreground should be
     * painted for the Component identified by the passed in
     * AbstractSynthContext.
     */
    public static Insets getPaintingInsets(SynthContext state, Insets insets) {
        if (state.getRegion().isSubregion()) {
            insets = state.getStyle().getInsets(state, insets);
        }
        else {
            insets = state.getComponent().getInsets(insets);
        }
        return insets;
    }

    /*
     * Convenience function for determining ComponentOrientation. Helps us avoid
     * having Munge directives throughout the code.
     */
    public static boolean isLeftToRight(Component c) {
        return c.getComponentOrientation().isLeftToRight();
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
        private int                                which;
        private AbstractRegionPainter.PaintContext ctx;
        private String                             className;

        LazyPainter(String className, int which, Insets insets, Dimension canvasSize, boolean inverted) {
            if (className == null) {
                throw new IllegalArgumentException("The className must be specified");
            }

            this.className = className;
            this.which = which;
            this.ctx = new AbstractRegionPainter.PaintContext(insets, canvasSize, inverted);
        }

        LazyPainter(String className, int which, Insets insets, Dimension canvasSize, boolean inverted,
            AbstractRegionPainter.PaintContext.CacheMode cacheMode, double maxH, double maxV) {
            if (className == null) {
                throw new IllegalArgumentException("The className must be specified");
            }

            this.className = className;
            this.which = which;
            this.ctx = new AbstractRegionPainter.PaintContext(insets, canvasSize, inverted, cacheMode, maxH, maxV);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object createValue(UIDefaults table) {
            try {
                Class c;
                Object cl;
                // See if we should use a separate ClassLoader
                if (table == null || !((cl = table.get("ClassLoader")) instanceof ClassLoader)) {
                    cl = Thread.currentThread().getContextClassLoader();
                    if (cl == null) {
                        // Fallback to the system class loader.
                        cl = ClassLoader.getSystemClassLoader();
                    }
                }

                c = Class.forName(className, true, (ClassLoader) cl);
                Constructor constructor = c.getConstructor(AbstractRegionPainter.PaintContext.class, int.class);
                if (constructor == null) {
                    throw new NullPointerException("Failed to find the constructor for the class: " + className);
                }
                return constructor.newInstance(ctx, which);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
