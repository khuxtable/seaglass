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
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Constructor;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import com.seaglass.painter.AbstractRegionPainter;
import com.seaglass.painter.ButtonPainter;
import com.seaglass.painter.CheckBoxPainter;
import com.seaglass.painter.ComboBoxArrowButtonPainter;
import com.seaglass.painter.ComboBoxPainter;
import com.seaglass.painter.ComboBoxTextFieldPainter;
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

import sun.swing.plaf.synth.SynthUI;

public class SeaGlassLookAndFeel extends NimbusLookAndFeel {
    /**
     * Used in a handful of places where we need an empty Insets.
     */
    static final Insets         EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);

    // Set the package name.
    private static final String PACKAGE_PREFIX          = "com.seaglass.SeaGlass";

    private UIDefaults          uiDefaults              = null;

    private SynthStyleFactory   nimbusFactory;

    // Refer to setSelectedUI
    static ComponentUI          selectedUI;
    // Refer to setSelectedUI
    static int                  selectedUIState;

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
            initializeDefaultFont(uiDefaults);

            // Override the root pane and scroll pane behavior.
            useOurUI(uiDefaults, "ComboBoxUI");
            useOurUI(uiDefaults, "LabelUI");
            useOurUI(uiDefaults, "RootPaneUI");
            useOurUI(uiDefaults, "ScrollBarUI");
            useOurUI(uiDefaults, "ScrollPaneUI");

            // Set base colors.
            initializeBaseColors(uiDefaults);

            initializeButtons(uiDefaults);

            initializeComboBoxes(uiDefaults);

            initializeTables(uiDefaults);

            initializeScrollBars(uiDefaults);

            initializeSplitPanes(uiDefaults);

            eliminateMouseOverBehavior(uiDefaults);

            if (!PlatformUtils.isMac()) {
                initializeTitlePaneButtons(uiDefaults);
            }

            if (PlatformUtils.shouldManuallyPaintTexturedWindowBackground()) {
                initializeToolBars(uiDefaults);
            }

            uiDefaults.put("InternalFrame:InternalFrameTitlePane.textForeground", new Color(0, 0, 0));

            if (!PlatformUtils.isMac()) {
                // If we're not on a Mac, draw our own title bar.
                JFrame.setDefaultLookAndFeelDecorated(true);
            } else {
                // If we're on a Mac, use the screen menu bar.
                System.setProperty("apple.laf.useScreenMenuBar", "true");

                // If we're on a Mac, use Aqua for some things.
                initializeAquaSettings(uiDefaults);
            }
        }
        return uiDefaults;
    }

    private void useOurUI(UIDefaults d, String uiName) {
        d.put(uiName, PACKAGE_PREFIX + uiName);
    }

    /**
     * Initialize the default font.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeDefaultFont(UIDefaults d) {
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
        d.put("defaultFont", font);
    }

    /**
     * Initialize the base colors.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeBaseColors(UIDefaults d) {
        d.put("nimbusBase", new ColorUIResource(61, 95, 140));
        // Original control: 220, 233, 239
        d.put("control", new ColorUIResource(231, 239, 243));
        // d.put("control", new ColorUIResource(237, 237, 237));
        // d.put("control", new ColorUIResource(249, 243, 245));
        d.put("scrollbar", new ColorUIResource(255, 255, 255));
        // Original blue grey: 170, 178, 194
        d.put("nimbusBlueGrey", new ColorUIResource(214, 218, 228));
        d.put("seaglassScrollThumbBase", new ColorUIResource(90, 150, 255));
        d.put("seaglassScrollBarButtonBase", new ColorUIResource(170, 174, 182));
        d.put("seaglassScrollBarTrackBase", new ColorUIResource(205, 208, 218));
        d.put("seaglassSplitPaneDividerBase", new ColorUIResource(170, 174, 182));
        d.put("nimbusSelectionBackground", new ColorUIResource(82, 127, 187));
        d.put("nimbusSelection", new ColorUIResource(113, 193, 242));
        d.put("nimbusOrange", new ColorUIResource(246, 188, 96));
        d.put("nimbusGreen", new ColorUIResource(144, 203, 96));
        d.put("nimbusRed", new ColorUIResource(236, 67, 60));
        d.put("seaglassButton1", new ColorUIResource(175, 207, 232));
        d.put("seaglassButton1c", new ColorUIResource(160, 247, 255));
        d.put("seaglassButton2", new ColorUIResource(16, 59, 116));
        d.put("seaglassButton2c", new ColorUIResource(46, 194, 226));
    }

    /**
     * Use Aqua settings for some properties if we're on a Mac.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeAquaSettings(UIDefaults d) {
        try {
            // Instantiate Aqua but don't install it.
            Class<?> lnfClass = Class.forName(UIManager.getSystemLookAndFeelClassName(), true, Thread.currentThread()
                .getContextClassLoader());
            LookAndFeel aqua = (LookAndFeel) lnfClass.newInstance();
            UIDefaults aquaDefaults = aqua.getDefaults();

            // Use Mac key bindings. Nimbus uses Windows keybindings on
            // Windows and GTK on anything else. We should use Aqua's
            // bindings on Mac.
            d.put("ComboBox.ancestorInputMap", aquaDefaults.get("ComboBox.ancestorInputMap"));
            d.put("ComboBox.editorInputMap", aquaDefaults.get("ComboBox.editorInputMap"));
            d.put("FormattedTextField.focusInputMap", aquaDefaults.get("FormattedTextField.focusInputMap"));
            d.put("PasswordField.focusInputMap", aquaDefaults.get("PasswordField.focusInputMap"));
            d.put("Spinner.ancestorInputMap", aquaDefaults.get("Spinner.ancestorInputMap"));
            d.put("Spinner.focusInputMap", aquaDefaults.get("Spinner.focusInputMap"));
            d.put("TabbedPane.focusInputMap", aquaDefaults.get("TabbedPane.focusInputMap"));
            d.put("TabbedPane.ancestorInputMap", aquaDefaults.get("TabbedPane.ancestorInputMap"));
            d.put("TabbedPane.wrap.focusInputMap", aquaDefaults.get("TabbedPane.wrap.focusInputMap"));
            d.put("TabbedPane.wrap.ancestorInputMap", aquaDefaults.get("TabbedPane.wrap.ancestorInputMap"));
            d.put("TabbedPane.scroll.focusInputMap", aquaDefaults.get("TabbedPane.scroll.focusInputMap"));
            d.put("TabbedPane.scroll.ancestorInputMap", aquaDefaults.get("TabbedPane.scroll.ancestorInputMap"));
            d.put("TextArea.focusInputMap", aquaDefaults.get("TextArea.focusInputMap"));
            d.put("TextField.focusInputMap", aquaDefaults.get("TextField.focusInputMap"));
            d.put("TextPane.focusInputMap", aquaDefaults.get("TextPane.focusInputMap"));

            // Use Aqua for any menu UI classes.
            d.put("MenuBarUI", aquaDefaults.get("MenuBarUI"));
            d.put("MenuUI", aquaDefaults.get("MenuUI"));
            d.put("MenuItemUI", aquaDefaults.get("MenuItemUI"));
            // d.put("CheckBoxMenuItemUI",
            // aquaDefaults.get("CheckBoxMenuItemUI"));
            d.put("RadioButtonMenuItemUI", aquaDefaults.get("RadioButtonMenuItemUI"));
            // d.put("PopupMenuUI", aquaDefaults.get("PopupMenuUI"));

            // If the Mac paint doesn't exist, use Aqua to paint a
            // unified toolbar.
            if (!PlatformUtils.shouldManuallyPaintTexturedWindowBackground()) {
                d.put("ToolBarUI", aquaDefaults.get("ToolBarUI"));
                d.put("ToolBar.border", aquaDefaults.get("ToolBar.border"));
                d.put("ToolBar.background", aquaDefaults.get("ToolBar.background"));
                d.put("ToolBar.foreground", aquaDefaults.get("ToolBar.foreground"));
                d.put("ToolBar.font", aquaDefaults.get("ToolBar.font"));
                d.put("ToolBar.dockingBackground", aquaDefaults.get("ToolBar.dockingBackground"));
                d.put("ToolBar.floatingBackground", aquaDefaults.get("ToolBar.floatingBackground"));
                d.put("ToolBar.dockingForeground", aquaDefaults.get("ToolBar.dockingForeground"));
                d.put("ToolBar.floatingForeground", aquaDefaults.get("ToolBar.floatingForeground"));
                d.put("ToolBar.rolloverBorder", aquaDefaults.get("ToolBar.rolloverBorder"));
                d.put("ToolBar.nonrolloverBorder", aquaDefaults.get("ToolBar.nonrolloverBorder"));
            }
        } catch (Exception e) {
            // TODO Should we do something with this exception?
            e.printStackTrace();
        }
    }

    /**
     * Initialize the tool bar settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeToolBars(UIDefaults d) {
        d.put("ToolBar[North].borderPainter",
            new LazyPainter("com.seaglass.painter.ToolBarPainter", ToolBarPainter.BORDER_NORTH, new Insets(0, 0, 1, 0),
                new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        d.put("ToolBar[South].borderPainter",
            new LazyPainter("com.seaglass.painter.ToolBarPainter", ToolBarPainter.BORDER_SOUTH, new Insets(1, 0, 0, 0),
                new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        d.put("ToolBar[East].borderPainter",
            new LazyPainter("com.seaglass.painter.ToolBarPainter", ToolBarPainter.BORDER_EAST, new Insets(1, 0, 0, 0),
                new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        d.put("ToolBar[West].borderPainter",
            new LazyPainter("com.seaglass.painter.ToolBarPainter", ToolBarPainter.BORDER_WEST, new Insets(0, 0, 1, 0),
                new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        d.put("ToolBar[Enabled].handleIconPainter", new LazyPainter("com.seaglass.painter.ToolBarPainter",
            ToolBarPainter.HANDLEICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(11, 38), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));

        d.put("ToolBar:Button[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter",
            ToolBarButtonPainter.BACKGROUND_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:Button[MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter",
            ToolBarButtonPainter.BACKGROUND_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(104, 33), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter",
            ToolBarButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:Button[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter",
            ToolBarButtonPainter.BACKGROUND_PRESSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:Button[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarButtonPainter",
            ToolBarButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 33), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));

        d.put("ToolBar:ToggleButton[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter",
            ToolBarToggleButtonPainter.BACKGROUND_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(104, 34), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER, new Insets(5, 5, 5,
                5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_FOCUSED, new Insets(
                5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ToolBarToggleButtonPainter",
            ToolBarToggleButtonPainter.BACKGROUND_PRESSED, new Insets(5, 5, 5, 5), new Dimension(72, 25), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(5,
                5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_SELECTED, new Insets(5, 5, 5,
                5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_SELECTED_FOCUSED, new Insets(
                5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED, new Insets(
                5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED,
            new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED,
            new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_MOUSEOVER_SELECTED_FOCUSED,
            new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            2.0, Double.POSITIVE_INFINITY));
        d.put("ToolBar:ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ToolBarToggleButtonPainter", ToolBarToggleButtonPainter.BACKGROUND_DISABLED_SELECTED, new Insets(
                5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0,
            Double.POSITIVE_INFINITY));
    }

    /**
     * Initialize button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeButtons(UIDefaults d) {
        // Initialize Button
        d.put("Button[Default].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DEFAULT, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Default+Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DEFAULT_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Default+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_DEFAULT, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Default+Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_DEFAULT_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_ENABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("Button[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        // Initialize ToggleButton
        d.put("ToggleButton[Selected].textForeground", new ColorUIResource(255, 255, 255));
        d.put("ToggleButton[Focused+Selected].textForeground", new ColorUIResource(255, 255, 255));
        d.put("ToggleButton[Disabled+Selected].textForeground", new ColorUIResource(255, 255, 255));
        d.put("ToggleButton[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_ENABLED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_SELECTED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_PRESSED_SELECTED_FOCUSED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ButtonPainter",
            ButtonPainter.BACKGROUND_DISABLED_SELECTED, new Insets(7, 7, 7, 7), new Dimension(86, 28), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        // Initialize CheckBox
        d.put("CheckBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("CheckBox[Disabled].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_DISABLED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Enabled].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused+MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Pressed].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused+Pressed].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused+Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_PRESSED_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Focused+MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox[Disabled+Selected].iconPainter", new LazyPainter("com.seaglass.painter.CheckBoxPainter",
            CheckBoxPainter.ICON_DISABLED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 19), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("CheckBox.icon", new SeaGlassIcon("CheckBox", "iconPainter", 16, 19));

        // Initialize RadioButton
        d.put("RadioButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("RadioButton[Disabled].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_DISABLED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Enabled].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_ENABLED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused+MouseOver].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Pressed].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused+Pressed].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused+Pressed+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_PRESSED_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Focused+MouseOver+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_MOUSEOVER_SELECTED_FOCUSED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton[Disabled+Selected].iconPainter", new LazyPainter("com.seaglass.painter.RadioButtonPainter",
            RadioButtonPainter.ICON_DISABLED_SELECTED, new Insets(5, 5, 5, 5), new Dimension(16, 16), false,
            AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("RadioButton.icon", new SeaGlassIcon("RadioButton", "iconPainter", 16, 16));
    }

    /**
     * Initialize the combo box settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeComboBoxes(UIDefaults d) {
        d.put("seaglassComboBoxBase", new ColorUIResource(61, 95, 140));
        d.put("seaglassComboBoxBlueGrey", new ColorUIResource(175, 207, 232));

        // Background
        d.put("ComboBox[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Disabled+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED_PRESSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Focused+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Focused+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED_FOCUSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Enabled+Selected].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_SELECTED, new Insets(8, 9, 8, 23), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Disabled+Editable].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_DISABLED_EDITABLE, new Insets(0, 0, 0, 0), new Dimension(1, 1), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Editable+Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(0, 0, 0, 0), new Dimension(1, 1), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Editable+Focused].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_FOCUSED_EDITABLE, new Insets(5, 5, 5, 5), new Dimension(105, 23), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Editable+MouseOver].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(0, 0, 0, 0), new Dimension(1, 1), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox[Editable+Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ComboBoxPainter",
            ComboBoxPainter.BACKGROUND_PRESSED_EDITABLE, new Insets(0, 0, 0, 0), new Dimension(1, 1), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 5.0));

        // Editable arrow
        d.put("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_DISABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(21, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(21, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_ENABLED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(21, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_PRESSED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(21, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.BACKGROUND_SELECTED_EDITABLE, new Insets(
                8, 1, 8, 8), new Dimension(21, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_ENABLED,
            new Insets(1, 1, 1, 1), new Dimension(23, 6), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_ENABLED,
            new Insets(1, 1, 1, 1), new Dimension(23, 6), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Disabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_DISABLED, new Insets(1, 1, 1,
                1), new Dimension(23, 6), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Pressed].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_PRESSED,
            new Insets(1, 1, 1, 1), new Dimension(23, 6), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Selected].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_SELECTED, new Insets(1, 1, 1,
                1), new Dimension(23, 6), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_EDITABLE, new Insets(1, 1, 1,
                1), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Disabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxArrowButtonPainter", ComboBoxArrowButtonPainter.FOREGROUND_EDITABLE_DISABLED, new Insets(
                1, 1, 1, 1), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, 5.0));

        // Textfield
        d.put("ComboBox:\"ComboBox.textField\"[Disabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxTextFieldPainter", ComboBoxTextFieldPainter.BACKGROUND_DISABLED, new Insets(3, 3, 3, 1),
            new Dimension(64, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        d.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxTextFieldPainter", ComboBoxTextFieldPainter.BACKGROUND_ENABLED, new Insets(3, 3, 3, 1),
            new Dimension(64, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        d.put("ComboBox:\"ComboBox.textField\"[Selected].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ComboBoxTextFieldPainter", ComboBoxTextFieldPainter.BACKGROUND_SELECTED, new Insets(3, 3, 3, 1),
            new Dimension(64, 23), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
    }

    /**
     * Initialize the table UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeTables(UIDefaults d) {
        d.put("Table.background", new ColorUIResource(255, 255, 255));
        d.put("Table.alternateRowColor", new Color(220, 233, 239));

        d.put("TableHeader:\"TableHeader.renderer\"[Disabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_DISABLED, new Insets(3, 3, 3,
                3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED,
            new Insets(3, 3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED, new Insets(3,
                3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_PRESSED,
            new Insets(3, 3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_SORTED, new Insets(3,
                3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_ENABLED_FOCUSED_SORTED,
            new Insets(3, 3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TableHeader:\"TableHeader.renderer\"[Disabled+Sorted].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.TableHeaderRendererPainter", TableHeaderRendererPainter.BACKGROUND_DISABLED_SORTED, new Insets(3,
                3, 3, 3), new Dimension(84, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /**
     * Initialize the scroll bar UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeScrollBars(UIDefaults d) {
        d.put("ScrollBar.incrementButtonGap", new Integer(-5));
        d.put("ScrollBar.decrementButtonGap", new Integer(-5));
        d.put("ScrollBar:\"ScrollBar.button\".size", new Integer(20));

        // Buttons
        d.put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_ENABLED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("ScrollBar:\"ScrollBar.button\"[Disabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_DISABLED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_MOUSEOVER, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
        d.put("ScrollBar:\"ScrollBar.button\"[Pressed].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarButtonPainter", ScrollBarButtonPainter.FOREGROUND_PRESSED, new Insets(1, 1, 1, 1),
            new Dimension(20, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));

        // Thumb
        d.put("ScrollBar:ScrollBarThumb.States", "Disabled,Enabled,Focused,MouseOver,Pressed");
        d.put("ScrollBar:ScrollBarThumb[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ScrollBarThumbPainter",
            ScrollBarThumbPainter.BACKGROUND_DISABLED, new Insets(0, 8, 0, 8), new Dimension(82, 14), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0));
        d.put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ScrollBarThumbPainter",
            ScrollBarThumbPainter.BACKGROUND_ENABLED, new Insets(0, 8, 0, 8), new Dimension(82, 14), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0));
        d.put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.ScrollBarThumbPainter", ScrollBarThumbPainter.BACKGROUND_MOUSEOVER, new Insets(0, 8, 0, 8),
            new Dimension(82, 14), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            2.0));
        d.put("ScrollBar:ScrollBarThumb[Pressed].backgroundPainter", new LazyPainter("com.seaglass.painter.ScrollBarThumbPainter",
            ScrollBarThumbPainter.BACKGROUND_PRESSED, new Insets(0, 8, 0, 8), new Dimension(82, 14), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0));

        // Track
        d.put("ScrollBar:ScrollBarTrack[Disabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ScrollBarTrackPainter",
            ScrollBarTrackPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0), new Dimension(19, 15), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0));
        d.put("ScrollBar:ScrollBarTrack[Enabled].backgroundPainter", new LazyPainter("com.seaglass.painter.ScrollBarTrackPainter",
            ScrollBarTrackPainter.BACKGROUND_ENABLED, new Insets(0, 0, 0, 0), new Dimension(19, 15), false,
            AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /**
     * Initialize the split pane UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeSplitPanes(UIDefaults d) {
        d.put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.BACKGROUND_ENABLED, new Insets(3, 0, 3, 0),
            new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        d.put("SplitPane:SplitPaneDivider[Focused].backgroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.BACKGROUND_FOCUSED, new Insets(3, 0, 3, 0),
            new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        d.put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.FOREGROUND_ENABLED, new Insets(0, 24, 0, 24),
            new Dimension(68, 10), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY));
        d.put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter", new LazyPainter(
            "com.seaglass.painter.SplitPaneDividerPainter", SplitPaneDividerPainter.FOREGROUND_ENABLED_VERTICAL, new Insets(5, 0,
                5, 0), new Dimension(10, 38), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    /**
     * Set the icons to paint the title pane decorations.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void initializeTitlePaneButtons(UIDefaults d) {
        // Set the multiplicity of states for the Close button.
        d.put("TitlePane:seaglassCloseButton.backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_ENABLED));
        d.put("TitlePane:seaglassCloseButton[Modified].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_MODIFIED));
        d.put("TitlePane:seaglassCloseButton[Unfocused].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put("TitlePane:seaglassCloseButton[Unfocused+Modified].backgroundPainter", new TitlePaneCloseButtonPainter(
            new AbstractRegionPainter.PaintContext(new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            TitlePaneCloseButtonPainter.BACKGROUND_MODIFIED_WINDOWNOTFOCUSED));
        d.put("TitlePane.closeIcon", new SeaGlassIcon("TitlePane:seaglassCloseButton", "backgroundPainter", 19, 18));
        d.put("TitlePane.closeIconModified", new SeaGlassIcon("TitlePane:seaglassCloseButton[Modified]", "backgroundPainter", 19,
            18));
        d.put("TitlePane.closeIconUnfocused", new SeaGlassIcon("TitlePane:seaglassCloseButton[Unfocused]", "backgroundPainter", 19,
            18));
        d.put("TitlePane.closeIconUnfocusedModified", new SeaGlassIcon("TitlePane:seaglassCloseButton[Unfocused+Modified]",
            "backgroundPainter", 19, 18));

        // Set the iconify button
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter", TitlePaneIconifyButtonPainter.BACKGROUND_ENABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                TitlePaneIconifyButtonPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter", TitlePaneIconifyButtonPainter.BACKGROUND_PRESSED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                    TitlePaneIconifyButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneIconifyButtonPainter",
                    TitlePaneIconifyButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("TitlePane.iconifyIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"", "backgroundPainter", 19, 18));
        d.put("TitlePane.iconifyIconUnfocused", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused]",
            "backgroundPainter", 19, 18));

        // Set the maximize button
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_DISABLED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED_WINDOWMAXIMIZED, new Insets(0, 0, 0, 0),
                    new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_DISABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED, new Insets(0, 0, 0, 0), new Dimension(19, 18), false,
                AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneMaximizeButtonPainter",
                    TitlePaneMaximizeButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19,
                        18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Disabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_DISABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_ENABLED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed].backgroundPainter",
            new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter", TitlePaneCloseButtonPainter.BACKGROUND_PRESSED,
                new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter",
                    TitlePaneCloseButtonPainter.BACKGROUND_ENABLED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19, 18),
                    false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed+WindowNotFocused].backgroundPainter",
                new LazyPainter("com.seaglass.painter.TitlePaneCloseButtonPainter",
                    TitlePaneCloseButtonPainter.BACKGROUND_PRESSED_WINDOWNOTFOCUSED, new Insets(0, 0, 0, 0), new Dimension(19, 18),
                    false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY));
        d.put("TitlePane.maximizeIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"", "backgroundPainter", 19, 18));
        d.put("TitlePane.maximizeIconUnfocused", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused]",
            "backgroundPainter", 19, 18));

        // Set the minimize button
        d.put("TitlePane.minimizeIcon", new SeaGlassIcon(
            "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized]",
            "backgroundPainter", 19, 18));
        d
            .put(
                "TitlePane.minimizeIconUnfocused",
                new SeaGlassIcon(
                    "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused]",
                    "backgroundPainter", 19, 18));
    }

    private void eliminateMouseOverBehavior(UIDefaults d) {
        // Kill MouseOver on Button.
        d.put("Button[Default+MouseOver].backgroundPainter", null);
        d.put("Button[Default+Focused+MouseOver].backgroundPainter", null);
        d.put("Button[MouseOver].backgroundPainter", null);
        d.put("Button[Focused+MouseOver].backgroundPainter", null);

        // Kill MouseOver on ToggleButton.
        d.put("ToggleButton[MouseOver].backgroundPainter", null);
        d.put("ToggleButton[Focused+MouseOver].backgroundPainter", null);
        d.put("ToggleButton[MouseOver+Selected].backgroundPainter", null);
        d.put("ToggleButton[Focused+MouseOver+Selected].backgroundPainter", null);

        // Initialize RadioButton
        d.put("RadioButton[MouseOver].iconPainter", null);
        d.put("RadioButton[Focused+MouseOver].iconPainter", null);
        d.put("RadioButton[MouseOver+Selected].iconPainter", null);
        d.put("RadioButton[Focused+MouseOver+Selected].iconPainter", null);

        // Initialize CheckBox
        d.put("CheckBox[MouseOver].iconPainter", null);
        d.put("CheckBox[Focused+MouseOver].iconPainter", null);
        d.put("CheckBox[MouseOver+Selected].iconPainter", null);
        d.put("CheckBox[Focused+MouseOver+Selected].iconPainter", null);

        // Initialize ComboBox
        d.put("ComboBox[Editable+MouseOver].backgroundPainter", null);
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", null);
        d.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", null);

        // Initialize InternalFrame
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver].iconPainter", null);
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver+WindowNotFocused].iconPainter",
            null);
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver].backgroundPainter", null);
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized].backgroundPainter",
                null);
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter",
                null);
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver].backgroundPainter", null);
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver].backgroundPainter", null);
        d
            .put(
                "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver+WindowNotFocused].backgroundPainter",
                null);

        // Initialize MenuItem
        d.put("MenuItem[MouseOver].textForeground", null);
        d.put("MenuItem[MouseOver].backgroundPainter", null);
        d.put("MenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize RadioButtonMenuItem
        d.put("RadioButtonMenuItem[MouseOver].textForeground", null);
        d.put("RadioButtonMenuItem[MouseOver].backgroundPainter", null);
        d.put("RadioButtonMenuItem[MouseOver+Selected].textForeground", null);
        d.put("RadioButtonMenuItem[MouseOver+Selected].backgroundPainter", null);
        d.put("RadioButtonMenuItem[MouseOver+Selected].checkIconPainter", null);
        d.put("RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize CheckBoxMenuItem
        d.put("CheckBoxMenuItem[MouseOver].textForeground", null);
        d.put("CheckBoxMenuItem[MouseOver].backgroundPainter", null);
        d.put("CheckBoxMenuItem[MouseOver+Selected].textForeground", null);
        d.put("CheckBoxMenuItem[MouseOver+Selected].backgroundPainter", null);
        d.put("CheckBoxMenuItem[MouseOver+Selected].checkIconPainter", null);
        d.put("CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize Menu
        d.put("Menu:MenuItemAccelerator[MouseOver].textForeground", null);

        // Initialize ScrollBar
        d.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", null);

        // Initialize Slider
        d.put("Slider:SliderThumb[Focused+MouseOver].backgroundPainter", null);
        d.put("Slider:SliderThumb[MouseOver].backgroundPainter", d.get("Slider:SliderThumb[Enabled].backgroundPainter"));
        d.put("Slider:SliderThumb[ArrowShape+MouseOver].backgroundPainter", null);
        d.put("Slider:SliderThumb[ArrowShape+Focused+MouseOver].backgroundPainter", null);

        // Initialize Spinner
        d.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].backgroundPainter", null);
        d.put("Spinner:\"Spinner.previousButton\"[MouseOver].backgroundPainter", null);
        d.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].foregroundPainter", null);
        d.put("Spinner:\"Spinner.previousButton\"[MouseOver].foregroundPainter", null);
        d.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].backgroundPainter", null);
        d.put("Spinner:\"Spinner.nextButton\"[MouseOver].backgroundPainter", null);
        d.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].foregroundPainter", null);
        d.put("Spinner:\"Spinner.nextButton\"[MouseOver].foregroundPainter", null);

        // Initialize TabbedPane
        d.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", null);
        d.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", null);
        d.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", null);
        d.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter", null);

        // Initialize TableHeader
        d.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter", null);

        // Initialize ToolBar
        d.put("ToolBar:Button[MouseOver].backgroundPainter", null);
        d.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", null);
        d.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", null);
        d.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", null);
        d.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", null);
        d.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", null);
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
     * A convenience method to return where the foreground should be painted for
     * the Component identified by the passed in AbstractSynthContext.
     */
    public static Insets getPaintingInsets(SynthContext state, Insets insets) {
        if (state.getRegion().isSubregion()) {
            insets = state.getStyle().getInsets(state, insets);
        } else {
            insets = state.getComponent().getInsets(insets);
        }
        return insets;
    }

    /**
     * Convenience function for determining ComponentOrientation. Helps us avoid
     * having Munge directives throughout the code.
     */
    public static boolean isLeftToRight(Component c) {
        return c.getComponentOrientation().isLeftToRight();
    }

    /**
     * Returns the ui that is of type <code>klass</code>, or null if one can not
     * be found.
     */
    public static Object getUIOfType(ComponentUI ui, Class klass) {
        if (klass.isInstance(ui)) {
            return ui;
        }
        return null;
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

    // Private stuff added from Synth

    /**
     * A convience method that will reset the Style of StyleContext if
     * necessary.
     * 
     * @return newStyle
     */
    static SynthStyle updateStyle(SeaGlassContext context, SynthUI ui) {
        SeaGlassStyle newStyle = getMyStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();

        if (newStyle != oldStyle) {
            if (oldStyle != null) {
                oldStyle.uninstallDefaults(context);
            }
            context.setStyle(newStyle);
            newStyle.installDefaults(context, ui);
        }
        return newStyle;
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
    public static SeaGlassStyle getMyStyle(JComponent c, Region r) {
        return (SeaGlassStyle) SynthLookAndFeel.getStyle(c, r);
    }

    /**
     * Returns the component state for the specified component. This should only
     * be used for Components that don't have any special state beyond that of
     * ENABLED, DISABLED or FOCUSED. For example, buttons shouldn't call into
     * this method.
     */
    public static int getComponentState(Component c) {
        if (c.isEnabled()) {
            if (c.isFocusOwner()) {
                return SynthUI.ENABLED | SynthUI.FOCUSED;
            }
            return SynthUI.ENABLED;
        }
        return SynthUI.DISABLED;
    }

    /**
     * A convenience method that handles painting of the background. All SynthUI
     * implementations should override update and invoke this method.
     */
    public static void update(SynthContext state, Graphics g) {
        paintRegion(state, g, null);
    }

    /**
     * A convenience method that handles painting of the background for
     * subregions. All SynthUI's that have subregions should invoke this method,
     * than paint the foreground.
     */
    public static void updateSubregion(SynthContext state, Graphics g, Rectangle bounds) {
        paintRegion(state, g, bounds);
    }

    private static void paintRegion(SynthContext state, Graphics g, Rectangle bounds) {
        JComponent c = state.getComponent();
        SynthStyle style = state.getStyle();
        int x, y, width, height;

        if (bounds == null) {
            x = 0;
            y = 0;
            width = c.getWidth();
            height = c.getHeight();
        } else {
            x = bounds.x;
            y = bounds.y;
            width = bounds.width;
            height = bounds.height;
        }

        // Fill in the background, if necessary.
        boolean subregion = state.getRegion().isSubregion();
        if ((subregion && style.isOpaque(state)) || (!subregion && c.isOpaque())) {
            g.setColor(style.getColor(state, ColorType.BACKGROUND));
            g.fillRect(x, y, width, height);
        }
    }

    /**
     * Returns true if the Style should be updated in response to the specified
     * PropertyChangeEvent. This forwards to
     * <code>shouldUpdateStyleOnAncestorChanged</code> as necessary.
     */
    public static boolean shouldUpdateStyle(PropertyChangeEvent event) {
        String eName = event.getPropertyName();
        if ("name" == eName) {
            // Always update on a name change
            return true;
        } else if ("componentOrientation" == eName) {
            // Always update on a component orientation change
            return true;
        } else if ("ancestor" == eName && event.getNewValue() != null) {
            // Only update on an ancestor change when getting a valid
            // parent and the LookAndFeel wants this.
            LookAndFeel laf = UIManager.getLookAndFeel();
            return (laf instanceof SynthLookAndFeel && ((SynthLookAndFeel) laf).shouldUpdateStyleOnAncestorChanged());
        }
        // Note: The following two nimbus based overrides should be refactored
        // to be in the Nimbus LAF. Due to constraints in an update release,
        // we couldn't actually provide the public API necessary to allow
        // NimbusLookAndFeel (a subclass of SynthLookAndFeel) to provide its
        // own rules for shouldUpdateStyle.
        else if ("Nimbus.Overrides" == eName) {
            // Always update when the Nimbus.Overrides client property has
            // been changed
            return true;
        } else if ("Nimbus.Overrides.InheritDefaults" == eName) {
            // Always update when the Nimbus.Overrides.InheritDefaults
            // client property has changed
            return true;
        } else if ("JComponent.sizeVariant" == eName) {
            // Always update when the JComponent.sizeVariant
            // client property has changed
            return true;
        }
        return false;
    }

    /**
     * Used by the renderers. For the most part the renderers are implemented as
     * Labels, which is problematic in so far as they are never selected. To
     * accomodate this SeaGlassLabelUI checks if the current UI matches that of
     * <code>selectedUI</code> (which this methods sets), if it does, then a
     * state as set by this method is returned. This provides a way for labels
     * to have a state other than selected.
     */
    static void setSelectedUI(ComponentUI uix, boolean selected, boolean focused, boolean enabled, boolean rollover) {
        selectedUI = uix;
        selectedUIState = 0;
        if (selected) {
            selectedUIState = SynthConstants.SELECTED;
            if (focused) {
                selectedUIState |= SynthConstants.FOCUSED;
            }
        } else if (rollover && enabled) {
            selectedUIState |= SynthConstants.MOUSE_OVER | SynthConstants.ENABLED;
            if (focused) {
                selectedUIState |= SynthConstants.FOCUSED;
            }
        } else {
            if (enabled) {
                selectedUIState |= SynthConstants.ENABLED;
                selectedUIState = SynthConstants.FOCUSED;
            } else {
                selectedUIState |= SynthConstants.DISABLED;
            }
        }
    }

    /**
     * Clears out the selected UI that was last set in setSelectedUI.
     */
    static void resetSelectedUI() {
        selectedUI = null;
    }
}
