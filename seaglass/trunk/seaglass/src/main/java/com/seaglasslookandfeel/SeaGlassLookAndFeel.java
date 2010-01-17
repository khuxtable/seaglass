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

import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
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

import com.seaglasslookandfeel.component.SeaGlassTitlePane;
import com.seaglasslookandfeel.component.TableScrollPaneCorner;
import com.seaglasslookandfeel.painter.ArrowButtonPainter;
import com.seaglasslookandfeel.painter.ButtonPainter;
import com.seaglasslookandfeel.painter.CheckBoxMenuItemPainter;
import com.seaglasslookandfeel.painter.CheckBoxPainter;
import com.seaglasslookandfeel.painter.ComboBoxArrowButtonPainter;
import com.seaglasslookandfeel.painter.ComboBoxPainter;
import com.seaglasslookandfeel.painter.ComboBoxTextFieldPainter;
import com.seaglasslookandfeel.painter.DesktopIconPainter;
import com.seaglasslookandfeel.painter.DesktopPanePainter;
import com.seaglasslookandfeel.painter.FrameAndRootPainter;
import com.seaglasslookandfeel.painter.MenuBarMenuPainter;
import com.seaglasslookandfeel.painter.MenuItemPainter;
import com.seaglasslookandfeel.painter.MenuPainter;
import com.seaglasslookandfeel.painter.PopupMenuPainter;
import com.seaglasslookandfeel.painter.PopupMenuSeparatorPainter;
import com.seaglasslookandfeel.painter.ProgressBarPainter;
import com.seaglasslookandfeel.painter.RadioButtonMenuItemPainter;
import com.seaglasslookandfeel.painter.RadioButtonPainter;
import com.seaglasslookandfeel.painter.ScrollBarButtonPainter;
import com.seaglasslookandfeel.painter.ScrollBarThumbPainter;
import com.seaglasslookandfeel.painter.ScrollBarTrackPainter;
import com.seaglasslookandfeel.painter.ScrollPanePainter;
import com.seaglasslookandfeel.painter.SearchFieldIconPainter;
import com.seaglasslookandfeel.painter.SearchFieldPainter;
import com.seaglasslookandfeel.painter.SliderThumbPainter;
import com.seaglasslookandfeel.painter.SliderTrackPainter;
import com.seaglasslookandfeel.painter.SpinnerFormattedTextFieldPainter;
import com.seaglasslookandfeel.painter.SpinnerNextButtonPainter;
import com.seaglasslookandfeel.painter.SpinnerPreviousButtonPainter;
import com.seaglasslookandfeel.painter.SplitPaneDividerPainter;
import com.seaglasslookandfeel.painter.TabbedPaneTabAreaPainter;
import com.seaglasslookandfeel.painter.TabbedPaneTabPainter;
import com.seaglasslookandfeel.painter.TableHeaderPainter;
import com.seaglasslookandfeel.painter.TableHeaderRendererPainter;
import com.seaglasslookandfeel.painter.TextComponentPainter;
import com.seaglasslookandfeel.painter.TitlePaneCloseButtonPainter;
import com.seaglasslookandfeel.painter.TitlePaneIconifyButtonPainter;
import com.seaglasslookandfeel.painter.TitlePaneMaximizeButtonPainter;
import com.seaglasslookandfeel.painter.TitlePaneMenuButtonPainter;
import com.seaglasslookandfeel.painter.ToolBarHandlePainter;
import com.seaglasslookandfeel.painter.ToolBarPainter;
import com.seaglasslookandfeel.painter.ToolBarToggleButtonPainter;
import com.seaglasslookandfeel.state.ComboBoxArrowButtonEditableState;
import com.seaglasslookandfeel.state.ComboBoxEditableState;
import com.seaglasslookandfeel.state.InternalFrameWindowFocusedState;
import com.seaglasslookandfeel.state.ProgressBarFinishedState;
import com.seaglasslookandfeel.state.ProgressBarIndeterminateState;
import com.seaglasslookandfeel.state.RootPaneNoFrameState;
import com.seaglasslookandfeel.state.RootPaneWindowFocusedState;
import com.seaglasslookandfeel.state.ScrollBarButtonIsIncreaseButtonState;
import com.seaglasslookandfeel.state.ScrollBarButtonsTogetherState;
import com.seaglasslookandfeel.state.SliderArrowShapeState;
import com.seaglasslookandfeel.state.SplitPaneDividerVerticalState;
import com.seaglasslookandfeel.state.SplitPaneVerticalState;
import com.seaglasslookandfeel.state.TableHeaderRendererSortedState;
import com.seaglasslookandfeel.state.TextAreaNotInScrollPaneState;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;
import com.seaglasslookandfeel.state.TitlePaneCloseButtonWindowModifiedState;
import com.seaglasslookandfeel.state.TitlePaneCloseButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneIconifyButtonWindowMinimizedState;
import com.seaglasslookandfeel.state.TitlePaneIconifyButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneMaximizeButtonWindowMaximizedState;
import com.seaglasslookandfeel.state.TitlePaneMaximizeButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneMenuButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneWindowFocusedState;
import com.seaglasslookandfeel.state.ToolBarEastState;
import com.seaglasslookandfeel.state.ToolBarNorthState;
import com.seaglasslookandfeel.state.ToolBarSouthState;
import com.seaglasslookandfeel.state.ToolBarWestState;
import com.seaglasslookandfeel.state.ToolBarWindowIsActiveState;
import com.seaglasslookandfeel.util.MacKeybindings;
import com.seaglasslookandfeel.util.PlatformUtils;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

import sun.swing.plaf.GTKKeybindings;
import sun.swing.plaf.WindowsKeybindings;
import sun.swing.plaf.synth.DefaultSynthStyle;
import sun.swing.plaf.synth.SynthUI;

/**
 * This is the main Sea Glass Look and Feel class.
 * 
 * At the moment, it customizes Nimbus, and includes some code from
 * NimbusLookAndFeel and SynthLookAndFeel where those methods were package
 * local.
 * 
 * @author Kathryn Huxtable
 * @author Kenneth Orr
 * 
 * @see javax.swing.plaf.synth.SynthLookAndFeel
 * @see com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
 */
public class SeaGlassLookAndFeel extends NimbusLookAndFeel {

    /**
     * Used in a handful of places where we need an empty Insets.
     */
    public static final Insets           EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);

    /**
     * The map of SynthStyles. This map is keyed by Region. Each Region maps to
     * a List of LazyStyles. Each LazyStyle has a reference to the prefix that
     * was registered with it. This reference can then be inspected to see if it
     * is the proper lazy style.
     * <p/>
     * There can be more than one LazyStyle for a single Region if there is more
     * than one prefix defined for a given region. For example, both Button and
     * "MyButton" might be prefixes assigned to the Region.Button region.
     */
    private Map<Region, List<LazyStyle>> styleMap                = new HashMap<Region, List<LazyStyle>>();

    /**
     * A map of regions which have been registered. This mapping is maintained
     * so that the Region can be found based on prefix in a very fast manner.
     * This is used in the "matches" method of LazyStyle.
     */

    private Map<String, Region>          registeredRegions       = new HashMap<String, Region>();

    /**
     * Our fallback style to avoid NPEs if the proper style cannot be found in
     * this class. Not sure if relying on DefaultSynthStyle is the best choice.
     */
    private DefaultSynthStyle            defaultStyle;

    /**
     * The default font that will be used. I store this value so that it can be
     * set in the UIDefaults when requested.
     */
    private Font                         defaultFont;

    /**
     * Used in IMAGE_DIRECTORY and UI_PACKAGE_PREFIX.
     */
    private static final String          PACKAGE_DIRECTORY       = SeaGlassLookAndFeel.class.getPackage().getName();

    /**
     * Set the image directory name based on the root package.
     */
    private static final String          PAINTER_DIRECTORY       = PACKAGE_DIRECTORY + ".painter";

    /**
     * Set the package name for UI delegates based on the root package.
     */
    private static final String          UI_PACKAGE_PREFIX       = PACKAGE_DIRECTORY + ".ui.SeaGlass";

    private UIDefaults                   uiDefaults              = null;

    // Refer to setSelectedUI
    public static ComponentUI            selectedUI;
    // Refer to setSelectedUI
    public static int                    selectedUIState;

    /**
     * Create a new Sea Glass Look and Feel instance.
     */
    public SeaGlassLookAndFeel() {
        super();

        /*
         * Create the default font and default style.
         */
        defaultFont = getDefaultFont();
        defaultStyle = new DefaultSynthStyle();
        defaultStyle.setFont(defaultFont);

        /*
         * Register all of the regions and their states that this class will use
         * for later lookup. Additional regions can be registered later by 3rd
         * party components. These are simply the default registrations.
         */
        registerStyles();
    }

    /**
     * Called by UIManager when this look and feel is installed.
     */
    @Override
    public void initialize() {
        super.initialize();
        // create synth style factory
        setStyleFactory(new SynthStyleFactory() {
            @Override
            public SynthStyle getStyle(JComponent c, Region r) {
                SynthStyle style = getSeaGlassStyle(c, r);
                if (!(style instanceof SeaGlassStyle)) {
                    style = new SeaGlassStyleWrapper(style);
                }
                return style;
            }
        });
    }

    /**
     * Called by UIManager when this look and feel is uninstalled.
     */
    @Override
    public void uninitialize() {
        JFrame.setDefaultLookAndFeelDecorated(false);
        super.uninitialize();
    }

    /**
     * Initialize the map of styles.
     */
    private void registerStyles() {
        register(Region.ARROW_BUTTON, "ArrowButton");
        register(Region.BUTTON, "Button");
        register(Region.TOGGLE_BUTTON, "ToggleButton");
        register(Region.RADIO_BUTTON, "RadioButton");
        register(Region.CHECK_BOX, "CheckBox");
        register(Region.COLOR_CHOOSER, "ColorChooser");
        register(Region.PANEL, "ColorChooser:\"ColorChooser.previewPanelHolder\"");
        register(Region.LABEL, "ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\"");
        register(Region.COMBO_BOX, "ComboBox");
        register(Region.TEXT_FIELD, "ComboBox:\"ComboBox.textField\"");
        register(Region.ARROW_BUTTON, "ComboBox:\"ComboBox.arrowButton\"");
        register(Region.LABEL, "ComboBox:\"ComboBox.listRenderer\"");
        register(Region.LABEL, "ComboBox:\"ComboBox.renderer\"");
        register(Region.SCROLL_PANE, "\"ComboBox.scrollPane\"");
        register(Region.FILE_CHOOSER, "FileChooser");
        register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrameTitlePane");
        register(Region.INTERNAL_FRAME, "InternalFrame");
        register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrame:InternalFrameTitlePane");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"");
        register(Region.ROOT_PANE, "RootPane");
        register(Region.DESKTOP_ICON, "DesktopIcon");
        register(Region.TOOL_BAR, "ToolBar");
        register(Region.TOOL_BAR_CONTENT, "ToolBar");
        register(Region.TOOL_BAR_DRAG_WINDOW, "ToolBar");
        register(Region.TOOL_BAR_SEPARATOR, "ToolBarSeparator");
        register(Region.DESKTOP_PANE, "DesktopPane");
        register(Region.LABEL, "Label");
        register(Region.LIST, "List");
        register(Region.LABEL, "List:\"List.cellRenderer\"");
        register(Region.MENU_BAR, "MenuBar");
        register(Region.MENU, "MenuBar:Menu");
        register(Region.MENU_ITEM_ACCELERATOR, "MenuBar:Menu:MenuItemAccelerator");
        register(Region.MENU_ITEM, "MenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "MenuItem:MenuItemAccelerator");
        register(Region.RADIO_BUTTON_MENU_ITEM, "RadioButtonMenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "RadioButtonMenuItem:MenuItemAccelerator");
        register(Region.CHECK_BOX_MENU_ITEM, "CheckBoxMenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "CheckBoxMenuItem:MenuItemAccelerator");
        register(Region.MENU, "Menu");
        register(Region.MENU_ITEM_ACCELERATOR, "Menu:MenuItemAccelerator");
        register(Region.POPUP_MENU, "PopupMenu");
        register(Region.POPUP_MENU_SEPARATOR, "PopupMenuSeparator");
        register(Region.OPTION_PANE, "OptionPane");
        register(Region.SEPARATOR, "OptionPane:\"OptionPane.separator\"");
        register(Region.PANEL, "OptionPane:\"OptionPane.messageArea\"");
        register(Region.LABEL, "OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"");
        register(Region.PANEL, "Panel");
        register(Region.PROGRESS_BAR, "ProgressBar");
        register(Region.SEPARATOR, "Separator");
        register(Region.SCROLL_BAR, "ScrollBar");
        register(Region.ARROW_BUTTON, "ScrollBar:\"ScrollBar.button\"");
        register(Region.SCROLL_BAR_THUMB, "ScrollBar:ScrollBarThumb");
        register(Region.SCROLL_BAR_TRACK, "ScrollBar:ScrollBarTrack");
        register(Region.SCROLL_PANE, "ScrollPane");
        register(Region.VIEWPORT, "Viewport");
        register(Region.SLIDER, "Slider");
        register(Region.SLIDER_THUMB, "Slider:SliderThumb");
        register(Region.SLIDER_TRACK, "Slider:SliderTrack");
        register(Region.SPINNER, "Spinner");
        register(Region.PANEL, "Spinner:\"Spinner.editor\"");
        register(Region.FORMATTED_TEXT_FIELD, "Spinner:Panel:\"Spinner.formattedTextField\"");
        register(Region.ARROW_BUTTON, "Spinner:\"Spinner.previousButton\"");
        register(Region.ARROW_BUTTON, "Spinner:\"Spinner.nextButton\"");
        register(Region.SPLIT_PANE, "SplitPane");
        register(Region.SPLIT_PANE_DIVIDER, "SplitPane:SplitPaneDivider");
        register(Region.TABBED_PANE, "TabbedPane");
        register(Region.TABBED_PANE_TAB, "TabbedPane:TabbedPaneTab");
        register(Region.TABBED_PANE_TAB_AREA, "TabbedPane:TabbedPaneTabArea");
        register(Region.TABBED_PANE_CONTENT, "TabbedPane:TabbedPaneContent");
        register(Region.TABLE, "Table");
        register(Region.LABEL, "Table:\"Table.cellRenderer\"");
        register(Region.TABLE_HEADER, "TableHeader");
        register(Region.LABEL, "TableHeader:\"TableHeader.renderer\"");
        register(Region.TEXT_FIELD, "\"Table.editor\"");
        register(Region.TEXT_FIELD, "\"Tree.cellEditor\"");
        register(Region.TEXT_FIELD, "TextField");
        register(Region.BUTTON, "TextField:findButton");
        register(Region.BUTTON, "TextField:cancelButton");
        register(Region.FORMATTED_TEXT_FIELD, "FormattedTextField");
        register(Region.PASSWORD_FIELD, "PasswordField");
        register(Region.TEXT_AREA, "TextArea");
        register(Region.TEXT_PANE, "TextPane");
        register(Region.EDITOR_PANE, "EditorPane");
        register(Region.BUTTON, "ToolBar:Button");
        register(Region.TOGGLE_BUTTON, "ToolBar:ToggleButton");
        register(Region.TOOL_TIP, "ToolTip");
        register(Region.TREE, "Tree");
        register(Region.TREE_CELL, "Tree:TreeCell");
        register(Region.LABEL, "Tree:\"Tree.cellRenderer\"");
    }

    /**
     * Returns the defaults for this SynthLookAndFeel.
     */
    @Override
    public UIDefaults getDefaults() {
        if (uiDefaults == null) {
            uiDefaults = super.getDefaults();

            // Install Keybindings for the operating system.
            if (PlatformUtils.isWindows()) {
                WindowsKeybindings.installKeybindings(uiDefaults);
            } else if (PlatformUtils.isMac()) {
                MacKeybindings.installKeybindings(uiDefaults);
            } else {
                GTKKeybindings.installKeybindings(uiDefaults);
            }

            // Set the default font.
            defineDefaultFont(uiDefaults);

            // Override some of the Synth UI delegates with copied and modified
            // versions.
            useOurUI(uiDefaults, "Button");
            useOurUI(uiDefaults, "CheckBoxMenuItem");
            useOurUI(uiDefaults, "ComboBox");
            useOurUI(uiDefaults, "DesktopPane");
            useOurUI(uiDefaults, "FormattedTextField");
            useOurUI(uiDefaults, "Label");
            useOurUI(uiDefaults, "MenuItem");
            if (!PlatformUtils.isMac()) {
                useOurUI(uiDefaults, "InternalFrame");
                useOurUI(uiDefaults, "DesktopIcon");
            }
            useOurUI(uiDefaults, "PopupMenu");
            useOurUI(uiDefaults, "ProgressBar");
            useOurUI(uiDefaults, "RadioButtonMenuItem");
            useOurUI(uiDefaults, "RootPane");
            useOurUI(uiDefaults, "ScrollBar");
            useOurUI(uiDefaults, "ScrollPane");
            useOurUI(uiDefaults, "Slider");
            useOurUI(uiDefaults, "SplitPane");
            useOurUI(uiDefaults, "Table");
            useOurUI(uiDefaults, "TableHeader");
            useOurUI(uiDefaults, "TextField");
            useOurUI(uiDefaults, "ToggleButton");
            useOurUI(uiDefaults, "ToolBar");
            useOurUI(uiDefaults, "Viewport");

            defineBaseColors(uiDefaults);
            defineArrowButtons(uiDefaults);
            defineButtons(uiDefaults);
            defineComboBoxes(uiDefaults);
            defineDesktopPanes(uiDefaults);
            defineLists(uiDefaults);
            defineMenus(uiDefaults);
            definePopups(uiDefaults);
            defineProgressBars(uiDefaults);
            defineRootPanes(uiDefaults);
            defineSpinners(uiDefaults);
            defineScrollBars(uiDefaults);
            defineSliders(uiDefaults);
            defineSplitPanes(uiDefaults);
            defineTabbedPanes(uiDefaults);
            defineTables(uiDefaults);
            defineTextControls(uiDefaults);
            defineToolBars(uiDefaults);
            defineTrees(uiDefaults);

            if (!PlatformUtils.isMac()) {
                defineInternalFrames(uiDefaults);
                defineInternalFrameMenuButtons(uiDefaults);
                defineInternalFrameCloseButtons(uiDefaults);
                defineInternalFrameIconifyButtons(uiDefaults);
                defineInternalFrameMaximizeButton(uiDefaults);
                uiDefaults.put("MenuBar[Enabled].backgroundPainter", null);
                uiDefaults.put("MenuBar[Enabled].borderPainter", null);

                // If we're not on a Mac, draw our own title bar.
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            } else {
                // If we're on a Mac, use the screen menu bar.
                System.setProperty("apple.laf.useScreenMenuBar", "true");

                // If we're on a Mac, use Aqua for some things.
                defineAquaSettings(uiDefaults);
            }
        }
        return uiDefaults;
    }

    private void useOurUI(UIDefaults d, String uiName) {
        uiName = uiName + "UI";
        d.put(uiName, UI_PACKAGE_PREFIX + uiName);
    }

    /**
     * Initialize the default font.
     * 
     * @param d
     *            the UI defaults map.
     */
    private Font getDefaultFont() {
        /*
         * Set the default font to Lucida Grande if available, else use Lucida
         * Sans Unicode. Grande is a later font and a bit nicer looking, but it
         * is a derivation of Sans Unicode, so they're compatible.
         */
        Font font = null;
        if (PlatformUtils.isMac()) {
            font = new Font("Lucida Grande", Font.PLAIN, 13);
        }
        if (font == null) {
            font = new Font("Lucida Sans Unicode", Font.PLAIN, 13);
        }
        return font;
    }

    /**
     * Initialize the base font.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineDefaultFont(UIDefaults d) {
        d.put("defaultFont", defaultFont);
    }

    /**
     * Initialize the base colors.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineBaseColors(UIDefaults d) {
        d.put("control", new Color(248, 248, 248));

        d.put("nimbusSelection", new Color(97, 129, 165));
        d.put("nimbusFocus", new Color(115, 164, 209));
        d.put("nimbusBorder", new Color(146, 151, 161));
        d.put("nimbusSelectedText", Color.WHITE);
        d.put("nimbusSelectionBackground", new Color(97, 129, 165));
        d.put("nimbusDisabledText", new Color(200, 200, 200));
        d.put("textHighlight", d.get("nimbusSelectionBackground"));
        d.put("textHighlightText", Color.WHITE);
        d.put("textInactiveText", d.get("nimbusDisabledText"));
        d.put("menuText", Color.WHITE);
        d.put("menu", Color.WHITE);

        d.put("seaGlassFocusInsets", new Insets(2, 2, 2, 2));
        d.put("seaGlassFocus", new Color(0x73a4d1));
        d.put("seaGlassOuterFocus", getDerivedColor("seaGlassFocus", -0.0028f, 0.01f, 0f, -0x80, true));
    }

    /**
     * Use Aqua settings for some properties if we're on a Mac.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineAquaSettings(UIDefaults d) {
        try {
            // Instantiate Aqua but don't install it.
            Class<?> lnfClass = Class.forName(UIManager.getSystemLookAndFeelClassName(), true, Thread.currentThread()
                .getContextClassLoader());
            LookAndFeel aqua = (LookAndFeel) lnfClass.newInstance();
            UIDefaults aquaDefaults = aqua.getDefaults();

            d.put("Desktop.background", aquaDefaults.getColor("Desktop.background"));
            d.put("InternalFrame.activeTitleForeground", aquaDefaults.getColor("InternalFrame.activeTitleForeground"));
            d.put("InternalFrame.inactiveTitleForeground", aquaDefaults.getColor("InternalFrame.inactiveTitleForeground"));

            // Use Aqua for any menu UI classes.
            d.put("MenuBarUI", aquaDefaults.get("MenuBarUI"));
            d.put("MenuUI", aquaDefaults.get("MenuUI"));
            d.put("InternalFrameUI", aquaDefaults.get("InternalFrameUI"));
            d.put("InternalFrameTitlePaneUI", aquaDefaults.get("InternalFrameTitlePaneUI"));
            d.put("DesktopIconUI", aquaDefaults.get("DesktopIconUI"));
            d.put("DesktopPaneUI", aquaDefaults.get("DesktopPaneUI"));
        } catch (Exception e) {
            // TODO Should we do something with this exception?
            e.printStackTrace();
        }
    }

    /**
     * Initialize the arrow button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineArrowButtons(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".ArrowButtonPainter";
        String p = "ArrowButton";
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_ENABLED));
    }

    /**
     * Initialize button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineButtons(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".ButtonPainter";

        // Initialize Button
        d.put("Button.States", "Enabled,Pressed,Disabled,Focused,Default");
        d.put("Button[Default].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DEFAULT));
        d.put("Button[Default+Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DEFAULT_FOCUSED));
        d.put("Button[Default+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_DEFAULT));
        d.put("Button[Default+Focused+Pressed].backgroundPainter", new LazyPainter(c,
            ButtonPainter.Which.BACKGROUND_PRESSED_DEFAULT_FOCUSED));
        d.put("Button[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put("Button[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put("Button[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put("Button[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put("Button[Focused+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));

        // Initialize ToggleButton
        d.put("ToggleButton.States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put("ToggleButton[Selected].textForeground", new ColorUIResource(Color.black));
        d.put("Button[Default+Pressed].textForeground", new ColorUIResource(Color.black));
        d.put("ToggleButton[Focused+Selected].textForeground", new ColorUIResource(Color.black));
        d.put("ToggleButton[Disabled+Selected].textForeground", new ColorUIResource(Color.black));
        d.put("ToggleButton[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put("ToggleButton[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put("ToggleButton[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put("ToggleButton[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put("ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put("ToggleButton[Selected].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_SELECTED));
        d.put("ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put("ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_SELECTED));
        d.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(c,
            ButtonPainter.Which.BACKGROUND_PRESSED_SELECTED_FOCUSED));
        d.put("ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED_SELECTED));

        // Initialize CheckBox
        c = PAINTER_DIRECTORY + ".CheckBoxPainter";
        d.put("CheckBox.States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put("CheckBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("CheckBox[Disabled].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_DISABLED));
        d.put("CheckBox[Enabled].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_ENABLED));
        d.put("CheckBox[Focused].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_FOCUSED));
        d.put("CheckBox[Pressed].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED));
        d.put("CheckBox[Focused+Pressed].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_FOCUSED));
        d.put("CheckBox[Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_SELECTED));
        d.put("CheckBox[Focused+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_SELECTED_FOCUSED));
        d.put("CheckBox[Pressed+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_SELECTED));
        d.put("CheckBox[Focused+Pressed+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_SELECTED_FOCUSED));
        d.put("CheckBox[Disabled+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_DISABLED_SELECTED));
        d.put("CheckBox.icon", new SeaGlassIcon("CheckBox", "iconPainter", 18, 18));

        // Initialize RadioButton
        c = PAINTER_DIRECTORY + ".RadioButtonPainter";
        d.put("RadioButton.States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put("RadioButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("RadioButton[Disabled].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_DISABLED));
        d.put("RadioButton[Enabled].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_ENABLED));
        d.put("RadioButton[Focused].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_FOCUSED));
        d.put("RadioButton[Pressed].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED));
        d.put("RadioButton[Focused+Pressed].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED_FOCUSED));
        d.put("RadioButton[Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_SELECTED));
        d.put("RadioButton[Focused+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_SELECTED_FOCUSED));
        d.put("RadioButton[Pressed+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED_SELECTED));
        d.put("RadioButton[Focused+Pressed+Selected].iconPainter", new LazyPainter(c,
            RadioButtonPainter.Which.ICON_PRESSED_SELECTED_FOCUSED));
        d.put("RadioButton[Disabled+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_DISABLED_SELECTED));
        d.put("RadioButton.icon", new SeaGlassIcon("RadioButton", "iconPainter", 18, 18));
    }

    /**
     * Initialize the combo box settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineComboBoxes(UIDefaults d) {
        d.put("ComboBox.Editable", new ComboBoxEditableState());
        d.put("ComboBox:\"ComboBox.arrowButton\".Editable", new ComboBoxArrowButtonEditableState());

        d.put("ComboBox.States", "Enabled,Pressed,Selected,Disabled,Focused,Editable");

        // Background
        String c = PAINTER_DIRECTORY + ".ComboBoxPainter";
        d.put("ComboBox[Disabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED));
        d.put("ComboBox[Disabled+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED_PRESSED));
        d.put("ComboBox[Enabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED));
        d.put("ComboBox[Focused].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_FOCUSED));
        d.put("ComboBox[Focused+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put("ComboBox[Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED));
        d.put("ComboBox[Enabled+Selected].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED_SELECTED));

        d.put("ComboBox[Disabled+Editable].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED_EDITABLE));
        d.put("ComboBox[Editable+Enabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED_EDITABLE));
        d.put("ComboBox[Editable+Focused].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_FOCUSED_EDITABLE));
        d.put("ComboBox[Editable+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED_EDITABLE));

        // Editable arrow
        c = PAINTER_DIRECTORY + ".ComboBoxArrowButtonPainter";
        d.put("ComboBox:\"ComboBox.arrowButton\".size", new Integer(22));
        d.put("ComboBox:\"ComboBox.arrowButton\".States", "Enabled,Pressed,Disabled,Editable");
        d.put("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].backgroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.BACKGROUND_DISABLED_EDITABLE));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].backgroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.BACKGROUND_ENABLED_EDITABLE));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].backgroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.BACKGROUND_PRESSED_EDITABLE));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Selected].backgroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.BACKGROUND_SELECTED_EDITABLE));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Enabled].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Disabled].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_DISABLED));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Pressed].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_PRESSED));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Selected].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_SELECTED));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_EDITABLE));
        d.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Disabled].foregroundPainter", new LazyPainter(c,
            ComboBoxArrowButtonPainter.Which.FOREGROUND_EDITABLE_DISABLED));

        // Textfield
        c = PAINTER_DIRECTORY + ".ComboBoxTextFieldPainter";
        d.put("ComboBox:\"ComboBox.textField\"[Disabled].backgroundPainter", new LazyPainter(c,
            ComboBoxTextFieldPainter.Which.BACKGROUND_DISABLED));
        d.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new LazyPainter(c,
            ComboBoxTextFieldPainter.Which.BACKGROUND_ENABLED));
        d.put("ComboBox:\"ComboBox.textField\"[Selected].backgroundPainter", new LazyPainter(c,
            ComboBoxTextFieldPainter.Which.BACKGROUND_SELECTED));

        d.put("ComboBox:\"ComboBox.listRenderer\".contentMargins", new InsetsUIResource(2, 10, 2, 10));
    }

    /**
     * Initialize the desktop pane UI settings.
     * 
     * @param d
     *            the UI defaults map.
     * 
     */
    private void defineDesktopPanes(UIDefaults d) {
        d.put("nimbusBase.DesktopPane", new ColorUIResource(90, 120, 200));
        String c = PAINTER_DIRECTORY + ".DesktopPanePainter";
        d.put("DesktopPane[Enabled].backgroundPainter", new LazyPainter(c, DesktopPanePainter.Which.BACKGROUND_ENABLED));

        // Initialize DesktopIcon
        c = PAINTER_DIRECTORY + ".DesktopIconPainter";
        d.put("DesktopIcon.contentMargins", new InsetsUIResource(0, 6, 5, 4));
        d.put("DesktopIcon[Enabled].backgroundPainter", new LazyPainter(c, DesktopIconPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Set the icons to paint the title pane decorations.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineInternalFrames(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".FrameAndRootPainter";

        d.put("InternalFrame.States", "Enabled,WindowFocused");

        d.put("InternalFrameTitlePane.buttonSpacing", 0);
        d.put("InternalFrame:InternalFrameTitlePane.WindowFocused", new TitlePaneWindowFocusedState());
        d.put("InternalFrame.WindowFocused", new InternalFrameWindowFocusedState());

        d.put("InternalFrame[Enabled].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED));
        d.put("InternalFrame[Enabled+WindowFocused].backgroundPainter", new LazyPainter(c,
            FrameAndRootPainter.Which.BACKGROUND_ENABLED_WINDOWFOCUSED));

        d.put("InternalFrame:InternalFrameTitlePane[Enabled].textForeground", d.get("nimbusDisabledText"));
        d.put("InternalFrame:InternalFrameTitlePane[WindowFocused].textForeground", Color.BLACK);
    }

    /**
     * Initialize the internal frame close button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineInternalFrameCloseButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"";
        String c = PAINTER_DIRECTORY + ".TitlePaneCloseButtonPainter";

        // Set the multiplicity of states for the Close button.
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused,WindowModified");
        d.put(p + ".WindowNotFocused", new TitlePaneCloseButtonWindowNotFocusedState());
        d.put(p + ".WindowModified", new TitlePaneCloseButtonWindowModifiedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Enabled+MouseOver+WindowModified].backgroundPainter", new LazyPainter(c,
            TitlePaneCloseButtonPainter.Which.BACKGROUND_MODIFIED_MOUSEOVER));
        d.put(p + "[Enabled+WindowModified].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_MODIFIED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneCloseButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[Enabled+WindowNotFocused+MouseOver].backgroundPainter", new LazyPainter(c,
            TitlePaneCloseButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneCloseButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 43, 18));
    }

    /**
     * Initialize the internal frame iconify button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineInternalFrameIconifyButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"";
        String c = PAINTER_DIRECTORY + ".TitlePaneIconifyButtonPainter";

        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused,WindowMinimized");
        d.put(p + ".WindowNotFocused", new TitlePaneIconifyButtonWindowNotFocusedState());
        d.put(p + ".WindowMinimized", new TitlePaneIconifyButtonWindowMinimizedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the iconify button states.
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));

        // Set the restore button states.
        d.put(p + "[Disabled+WindowMinimized].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_DISABLED));
        d.put(p + "[Enabled+WindowMinimized].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_ENABLED));
        d.put(p + "[MouseOver+WindowMinimized].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_MOUSEOVER));
        d.put(p + "[Pressed+WindowMinimized].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_PRESSED));
        d.put(p + "[Enabled+WindowMinimized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowMinimized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowMinimized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 26, 18));
    }

    /**
     * Initialize the internal frame maximize button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineInternalFrameMaximizeButton(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"";
        String c = PAINTER_DIRECTORY + ".TitlePaneMaximizeButtonPainter";

        d.put(p + ".WindowNotFocused", new TitlePaneMaximizeButtonWindowNotFocusedState());
        d.put(p + ".WindowMaximized", new TitlePaneMaximizeButtonWindowMaximizedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the maximize button states.
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));

        // Set the restore button states.
        d.put(p + "[Disabled+WindowMaximized].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_DISABLED));
        d.put(p + "[Enabled+WindowMaximized].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_ENABLED));
        d.put(p + "[MouseOver+WindowMaximized].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_MOUSEOVER));
        d.put(p + "[Pressed+WindowMaximized].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_PRESSED));
        d.put(p + "[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter(c,
            TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 25, 18));
    }

    /**
     * Initialize the internal frame menu button settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineInternalFrameMenuButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"";
        String c = PAINTER_DIRECTORY + ".TitlePaneMenuButtonPainter";

        d.put(p + ".WindowNotFocused", new TitlePaneMenuButtonWindowNotFocusedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the states for the Menu button.
        d.put(p + "[Enabled].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_ENABLED));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_DISABLED));
        d.put(p + "[MouseOver].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_MOUSEOVER));
        d.put(p + "[Pressed].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].iconPainter", new LazyPainter(c,
            TitlePaneMenuButtonPainter.Which.ICON_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].iconPainter", new LazyPainter(c,
            TitlePaneMenuButtonPainter.Which.ICON_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].iconPainter", new LazyPainter(c,
            TitlePaneMenuButtonPainter.Which.ICON_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 19, 18));
    }

    /**
     * Initialize the list settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineLists(UIDefaults d) {
        d.put("List.background", d.get("control"));
        // TODO Why doesn't ColorUIResource work here?
        d.put("List[Selected].textForeground", Color.WHITE);
        d.put("List[Selected].textBackground", d.get("nimbusSelection"));
        d.put("List[Disabled+Selected].textBackground", Color.WHITE);
    }

    private void defineMenus(UIDefaults d) {

        // Initialize Menu
        String c = PAINTER_DIRECTORY + ".MenuPainter";
        String p = "Menu";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 5));
        d.put(p + "[Disabled].textForeground", d.get("nimbusDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Enabled+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[Enabled+Selected].backgroundPainter", new LazyPainter(c, MenuPainter.Which.BACKGROUND_ENABLED_SELECTED));
        d.put(p + "[Disabled].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_DISABLED));
        d.put(p + "[Enabled].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_ENABLED));
        d.put(p + "[Enabled+Selected].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_ENABLED_SELECTED));
        d.put(p + ".arrowIcon", new SeaGlassIcon(p + "", "arrowIconPainter", 9, 10));

        p = "Menu:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // We don't paint MenuBar backgrounds. Remove the painters.
        c = PAINTER_DIRECTORY + ".MenuBarPainter";
        p = "MenuBar";
        d.put(p + ".contentMargins", new InsetsUIResource(2, 6, 2, 6));
        if (d.get(p + "[Enabled].backgroundPainter") != null) {
            d.remove(p + "[Enabled].backgroundPainter");
        }
        if (d.get(p + "[Enabled].borderPainter") != null) {
            d.remove(p + "[Enabled].borderPainter");
        }

        c = PAINTER_DIRECTORY + ".MenuBarMenuPainter";
        p = "MenuBar:Menu";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 4, 2, 4));
        d.put(p + "[Disabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Selected].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, MenuBarMenuPainter.Which.BACKGROUND_SELECTED));

        p = "MenuBar:Menu:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Initialize MenuItem
        c = PAINTER_DIRECTORY + ".MenuItemPainter";
        p = "MenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + "[Disabled].textForeground", d.getColor("nimbusDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, MenuItemPainter.Which.BACKGROUND_MOUSEOVER));

        p = "MenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", d.getColor("nimbusDisabledText"));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // Initialize CheckBoxMenuItem
        c = PAINTER_DIRECTORY + ".CheckBoxMenuItemPainter";
        p = "CheckBoxMenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + "[Disabled].textForeground", d.getColor("nimbusDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, CheckBoxMenuItemPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d
            .put(p + "[MouseOver+Selected].backgroundPainter", new LazyPainter(c,
                CheckBoxMenuItemPainter.Which.BACKGROUND_SELECTED_MOUSEOVER));
        d.put(p + "[Disabled+Selected].checkIconPainter", new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_DISABLED_SELECTED));
        d.put(p + "[Enabled+Selected].checkIconPainter", new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_ENABLED_SELECTED));
        d.put(p + "[MouseOver+Selected].checkIconPainter", new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_SELECTED_MOUSEOVER));
        d.put(p + ".checkIcon", new SeaGlassIcon(p, "checkIconPainter", 9, 10));

        p = "CheckBoxMenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // Initialize RadioButtonMenuItem
        c = PAINTER_DIRECTORY + ".RadioButtonMenuItemPainter";
        p = "RadioButtonMenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + "[Disabled].textForeground", d.getColor("nimbusDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, RadioButtonMenuItemPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver+Selected].backgroundPainter", new LazyPainter(c,
            RadioButtonMenuItemPainter.Which.BACKGROUND_SELECTED_MOUSEOVER));
        d.put(p + "[Disabled+Selected].checkIconPainter", new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_DISABLED_SELECTED));
        d.put(p + "[Enabled+Selected].checkIconPainter", new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_ENABLED_SELECTED));
        d.put(p + "[MouseOver+Selected].checkIconPainter",
            new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_SELECTED_MOUSEOVER));
        d.put(p + ".checkIcon", new SeaGlassIcon(p, "checkIconPainter", 9, 10));

        p = "RadioButtonMenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
    }

    private void definePopups(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".PopupMenuPainter";
        String p = "PopupMenu";
        d.put(p + ".contentMargins", new InsetsUIResource(6, 1, 6, 1));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".consumeEventOnClose", Boolean.TRUE);
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, PopupMenuPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, PopupMenuPainter.Which.BACKGROUND_ENABLED));

        // Initialize PopupMenuSeparator
        c = PAINTER_DIRECTORY + ".PopupMenuSeparatorPainter";
        p = "PopupMenuSeparator";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 0, 2, 0));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, PopupMenuSeparatorPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Initialize the progress bar settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineProgressBars(UIDefaults d) {
        d.put("ProgressBar.Indeterminate", new ProgressBarIndeterminateState());
        d.put("ProgressBar.Finished", new ProgressBarFinishedState());

        String c = PAINTER_DIRECTORY + ".ProgressBarPainter";
        d.put("ProgressBar.cycleTime", 500);
        d.put("ProgressBar.progressPadding", new Integer(3));
        d.put("ProgressBar.trackThickness", new Integer(19));
        d.put("ProgressBar.tileWidth", new Integer(24));
        d.put("ProgressBar[Enabled].backgroundPainter", new LazyPainter(c, ProgressBarPainter.Which.BACKGROUND_ENABLED));
        d.put("ProgressBar[Disabled].backgroundPainter", new LazyPainter(c, ProgressBarPainter.Which.BACKGROUND_DISABLED));
        d.put("ProgressBar[Enabled].foregroundPainter", new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_ENABLED));
        d.put("ProgressBar[Enabled+Finished].foregroundPainter", new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_ENABLED_FINISHED));
        d.put("ProgressBar[Enabled+Indeterminate].foregroundPainter", new LazyPainter(c,
            ProgressBarPainter.Which.FOREGROUND_ENABLED_INDETERMINATE));
        d.put("ProgressBar[Disabled].foregroundPainter", new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_DISABLED));
        d
            .put("ProgressBar[Disabled+Finished].foregroundPainter", new LazyPainter(c,
                ProgressBarPainter.Which.FOREGROUND_DISABLED_FINISHED));
        d.put("ProgressBar[Disabled+Indeterminate].foregroundPainter", new LazyPainter(c,
            ProgressBarPainter.Which.FOREGROUND_DISABLED_INDETERMINATE));
    }

    /**
     * Initialize the root pane settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineRootPanes(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".FrameAndRootPainter";

        d.put("RootPane.States", "Enabled,WindowFocused,NoFrame");
        d.put("RootPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("RootPane.opaque", Boolean.FALSE);
        d.put("RootPane.NoFrame", new RootPaneNoFrameState());
        d.put("RootPane.WindowFocused", new RootPaneWindowFocusedState());

        d.put("RootPane[Enabled+NoFrame].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED_NOFRAME));
        d.put("RootPane[Enabled].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED));
        d.put("RootPane[Enabled+WindowFocused].backgroundPainter", new LazyPainter(c,
            FrameAndRootPainter.Which.BACKGROUND_ENABLED_WINDOWFOCUSED));
    }

    /**
     * Initialize the scroll bar UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineScrollBars(UIDefaults d) {
        d.put("ScrollBar.incrementButtonGap", new Integer(-5));
        d.put("ScrollBar.decrementButtonGap", new Integer(-5));
        d.put("ScrollBar:\"ScrollBar.button\".size", new Integer(20));

        // Buttons
        String c = PAINTER_DIRECTORY + ".ScrollBarButtonPainter";
        String p = "ScrollBar:\"ScrollBar.button\"";
        d.put(p + ".IncreaseButton", new ScrollBarButtonIsIncreaseButtonState());
        d.put(p + ".States", "Enabled,Pressed,Disabled,IncreaseButton");
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_PRESSED));
        d.put(p + "[IncreaseButton+Enabled].foregroundPainter",
            new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_ENABLED));
        d.put(p + "[IncreaseButton+Disabled].foregroundPainter", new LazyPainter(c,
            ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_DISABLED));
        d.put(p + "[IncreaseButton+Pressed].foregroundPainter",
            new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_PRESSED));

        // Thumb
        // Seems to be a bug somewhere where MouseOver is always delivered even
        // when we don't want it, but if it's not specified nothing at all is
        // painted.
        c = PAINTER_DIRECTORY + ".ScrollBarThumbPainter";
        p = "ScrollBar:ScrollBarThumb";
        d.put(p + ".States", "Enabled,Pressed,MouseOver,Disabled");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[MouseOver+Pressed].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_PRESSED));

        // Track
        c = PAINTER_DIRECTORY + ".ScrollBarTrackPainter";
        p = "ScrollBar:ScrollBarTrack";
        d.put(p + ".ButtonsTogether", new ScrollBarButtonsTogetherState());
        d.put(p + ".States", "Enabled,Disabled,EnabledTogether,DisabledTogether");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ScrollBarTrackPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ScrollBarTrackPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled+ButtonsTogether].backgroundPainter", new LazyPainter(c,
            ScrollBarTrackPainter.Which.BACKGROUND_DISABLED_TOGETHER));
        d.put(p + "[Enabled+ButtonsTogether].backgroundPainter",
            new LazyPainter(c, ScrollBarTrackPainter.Which.BACKGROUND_ENABLED_TOGETHER));

        // Define ScrollPane border painters.
        c = PAINTER_DIRECTORY + ".ScrollPanePainter";
        p = "ScrollPane";
        d.put(p + "[Enabled+Focused].borderPainter", new LazyPainter(c, ScrollPanePainter.Which.BORDER_ENABLED_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, ScrollPanePainter.Which.BORDER_ENABLED));

        // Store ScrollPane Corner Component
        uiDefaults.put(p + ".cornerPainter", new LazyPainter(c, ScrollPanePainter.Which.CORNER_ENABLED));
    }

    /**
     * Initialize the slider settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineSliders(UIDefaults d) {
        d.put("Slider.ArrowShape", new SliderArrowShapeState());
        d.put("Slider:SliderThumb.ArrowShape", new SliderArrowShapeState());
        d.put("Slider:SliderTrack.ArrowShape", new SliderArrowShapeState());
        d.put("Slider.thumbWidth", new Integer(17));
        d.put("Slider.thumbHeight", new Integer(20));
        d.put("Slider.trackBorder", new Integer(0));
        d.put("Slider.trackHeight", new Integer(5));

        String c = PAINTER_DIRECTORY + ".SliderThumbPainter";
        d.put("Slider:SliderThumb[Disabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_DISABLED));
        d.put("Slider:SliderThumb[Enabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_ENABLED));
        d.put("Slider:SliderThumb[Focused].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED));
        d.put("Slider:SliderThumb[Focused+MouseOver].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER));
        d.put("Slider:SliderThumb[Focused+Pressed].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED));
        d.put("Slider:SliderThumb[MouseOver].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_MOUSEOVER));
        d.put("Slider:SliderThumb[Pressed].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_PRESSED));
        d.put("Slider:SliderThumb[ArrowShape+Enabled].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_ENABLED_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+Disabled].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_DISABLED_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+MouseOver].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_MOUSEOVER_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+Pressed].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_PRESSED_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+Focused].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_FOCUSED_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+Focused+MouseOver].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE));
        d.put("Slider:SliderThumb[ArrowShape+Focused+Pressed].backgroundPainter", new LazyPainter(c,
            SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE));

        c = PAINTER_DIRECTORY + ".SliderTrackPainter";
        d.put("Slider:SliderTrack[Disabled].backgroundPainter", new LazyPainter(c, SliderTrackPainter.Which.BACKGROUND_DISABLED));
        d.put("Slider:SliderTrack[Enabled].backgroundPainter", new LazyPainter(c, SliderTrackPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Initialize the spinner UI settings;
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineSpinners(UIDefaults d) {
        d.put("Spinner.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("Spinner:\"Spinner.editor\".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        String c = PAINTER_DIRECTORY + ".SpinnerFormattedTextFieldPainter";
        String p = "Spinner:Panel:\"Spinner.formattedTextField\"";
        d.put(p + ".contentMargins", new InsetsUIResource(3, 10, 2, 10));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c,
            SpinnerFormattedTextFieldPainter.Which.BACKGROUND_SELECTED_FOCUSED));

        c = PAINTER_DIRECTORY + ".SpinnerPreviousButtonPainter";
        p = "Spinner:\"Spinner.previousButton\"";
        d.put(p + ".size", new Integer(22));
        d.put(p + ".States", "Disabled,Enabled,Focused,Pressed");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Focused].foregroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.FOREGROUND_FOCUSED));
        d.put(p + "[Focused+Pressed].foregroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.FOREGROUND_PRESSED_FOCUSED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, SpinnerPreviousButtonPainter.Which.FOREGROUND_PRESSED));

        c = PAINTER_DIRECTORY + ".SpinnerNextButtonPainter";
        p = "Spinner:\"Spinner.nextButton\"";
        d.put(p + ".size", new Integer(22));
        d.put(p + ".States", "Disabled,Enabled,Focused,Pressed");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Focused].foregroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.FOREGROUND_FOCUSED));
        d.put(p + "[Focused+Pressed].foregroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.FOREGROUND_PRESSED_FOCUSED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, SpinnerNextButtonPainter.Which.FOREGROUND_PRESSED));
    }

    /**
     * Initialize the split pane UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineSplitPanes(UIDefaults d) {
        String p = "SplitPane";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 1, 1, 1));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        d.put(p + ".Vertical", new SplitPaneVerticalState());
        d.put(p + ".size", new Integer(11));
        d.put(p + ".dividerSize", new Integer(11));
        d.put(p + ".centerOneTouchButtons", Boolean.TRUE);
        d.put(p + ".oneTouchButtonOffset", new Integer(30));
        d.put(p + ".oneTouchButtonVOffset", new Integer(1));
        d.put(p + ".oneTouchExpandable", Boolean.FALSE);
        d.put(p + ".continuousLayout", Boolean.TRUE);

        String c = PAINTER_DIRECTORY + ".SplitPaneDividerPainter";
        p = "SplitPane:SplitPaneDivider";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        d.put(p + ".Vertical", new SplitPaneDividerVerticalState());
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Enabled+Vertical].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_ENABLED_VERTICAL));
        d.put(p + "[Focused].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_FOCUSED));
        d.put(p + "[Focused+Vertical].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_FOCUSED_VERTICAL));
    }

    /**
     * Initialize the tabbed pane settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineTabbedPanes(UIDefaults d) {
        d.put("tabbedPaneTabBase", new Color(90, 120, 200));
        String c = PAINTER_DIRECTORY + ".TabbedPaneTabPainter";
        String p = "TabbedPane:TabbedPaneTab";
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+MouseOver].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_ENABLED_MOUSEOVER));
        d.put(p + "[Enabled+Pressed].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_ENABLED_PRESSED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Disabled+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_DISABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[MouseOver+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_MOUSEOVER));
        d.put(p + "[Pressed+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_PRESSED));
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put(p + "[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter(c,
            TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_MOUSEOVER_FOCUSED));
        d.put(p + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(c,
            TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_PRESSED_FOCUSED));

        p = "TabbedPane:TabbedPaneTabArea";
        c = PAINTER_DIRECTORY + ".TabbedPaneTabAreaPainter";
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+MouseOver].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_MOUSEOVER));
        d.put(p + "[Enabled+Pressed].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_PRESSED));
    }

    /**
     * Initialize the table UI settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineTables(UIDefaults d) {
        d.put("Table.background", new ColorUIResource(255, 255, 255));
        d.put("Table.alternateRowColor", new ColorUIResource(235, 245, 252));
        d.put("TableHeader:\"TableHeader.renderer\".Sorted", new TableHeaderRendererSortedState());
        // TODO Why doesn't ColorUIResource work on these next two?
        d.put("Table[Enabled+Selected].textBackground", new Color(97, 129, 165));
        d.put("Table[Disabled+Selected].textBackground", new Color(97, 129, 165));

        String c = PAINTER_DIRECTORY + ".TableHeaderPainter";
        d.put("TableHeader.font", defaultFont.deriveFont(11.0f));
        d.put("TableHeader[Enabled].ascendingSortIconPainter", new LazyPainter(c, TableHeaderPainter.Which.ASCENDINGSORTICON_ENABLED));
        d.put("Table.ascendingSortIcon", new SeaGlassIcon("TableHeader", "ascendingSortIconPainter", 8, 7));
        d.put("TableHeader[Enabled].descendingSortIconPainter", new LazyPainter(c, TableHeaderPainter.Which.DESCENDINGSORTICON_ENABLED));
        d.put("Table.descendingSortIcon", new SeaGlassIcon("TableHeader", "descendingSortIconPainter", 8, 7));

        c = PAINTER_DIRECTORY + ".TableHeaderRendererPainter";
        d.put("TableHeader:\"TableHeader.renderer\"[Disabled].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_DISABLED));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_ENABLED));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_FOCUSED));
        d.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_PRESSED));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_SORTED));
        d.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_FOCUSED_SORTED));
        d.put("TableHeader:\"TableHeader.renderer\"[Disabled+Sorted].backgroundPainter", new LazyPainter(c,
            TableHeaderRendererPainter.Which.BACKGROUND_DISABLED_SORTED));

        // Store Table ScrollPane Corner Component
        uiDefaults.put("Table.scrollPaneCornerComponent", TableScrollPaneCorner.class);
    }

    /**
     * Initialize the settings for text controls.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineTextControls(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".TextComponentPainter";
        String cs = PAINTER_DIRECTORY + ".SearchFieldPainter";
        String ci = PAINTER_DIRECTORY + ".SearchFieldIconPainter";

        // Initialize search field icons
        String p = "TextField:findButton";
        d.put(p + ".States", "Enabled,Disabled,HasPopup");
        d.put(p + ".HasPopup", "Enabled,Disabled,Popup");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_DISABLED));
        d.put(p + "[Enabled].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED));
        d.put(p + "[Enabled+HasPopup].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED_POPUP));
        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 20, 15));

        p = "TextField:cancelButton";
        d.put(p + ".States", "Enabled,Disabled,Pressed");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_DISABLED));
        d.put(p + "[Enabled].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_ENABLED));
        d.put(p + "[Pressed].iconPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_PRESSED));
        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 15, 15));

        // Initialize TextField
        p = "TextField";
        d.put(p + ".States", "Enabled,Selected,Disabled,Focused,SearchField");
        d.put(p + ".SearchField", new TextFieldIsSearchState());
        d.put(p + ".contentMargins", new InsetsUIResource(6, 6, 6, 6));
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_ENABLED));
        // Paint with SearchFieldPainter.
        d.put(p + "[Disabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_ENABLED));

        // Initialize FormattedTextField
        p = "FormattedTextField";
        d.put(p + ".States", "Enabled,Selected,Disabled,Focused,SearchField");
        d.put(p + ".SearchField", new TextFieldIsSearchState());
        d.put(p + ".contentMargins", new InsetsUIResource(6, 6, 6, 6));
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_ENABLED));
        // Paint with SearchFieldPainter.
        d.put(p + "[Disabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_ENABLED));

        // Initialize PasswordField
        p = "PasswordField";
        d.put(p + ".contentMargins", new InsetsUIResource(6, 6, 6, 6));
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_ENABLED));
        // Paint with SearchFieldPainter.
        d.put(p + "[Disabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_ENABLED));

        // Initialize TextArea
        // TextArea in scroll pane is visually the same as editor pane.
        p = "TextArea";
        d.put(p + ".contentMargins", new InsetsUIResource(6, 6, 6, 6));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,NotInScrollPane");
        d.put(p + ".NotInScrollPane", new TextAreaNotInScrollPaneState());
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_ENABLED));
        // TextArea not in scroll pane is visually the same as TextField.
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+NotInScrollPane].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+NotInScrollPane].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled+NotInScrollPane].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused+NotInScrollPane].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled+NotInScrollPane].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_ENABLED));

        // Initialize TextPane
        p = "TextPane";
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));

        // Initialize EditorPane
        p = "EditorPane";
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
    }

    /**
     * Initialize the tool bar settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineToolBars(UIDefaults d) {
        String c = PAINTER_DIRECTORY + ".ToolBarPainter";
        String p = "ToolBar";

        d.put(p + ".contentMargins", new InsetsUIResource(2, 2, 2, 2));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".States", "North,East,West,South,WindowIsActive,Pressed");
        d.put(p + ".North", new ToolBarNorthState());
        d.put(p + ".East", new ToolBarEastState());
        d.put(p + ".West", new ToolBarWestState());
        d.put(p + ".South", new ToolBarSouthState());
        d.put(p + ".WindowIsActive", new ToolBarWindowIsActiveState());

        d.put(p + "[North].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_NORTH));
        d.put(p + "[South].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_SOUTH));
        d.put(p + "[East].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_EAST));
        d.put(p + "[West].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_WEST));
        d.put(p + "[North+WindowIsActive].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_NORTH_ENABLED));
        d.put(p + "[South+WindowIsActive].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_SOUTH_ENABLED));
        d.put(p + "[East+WindowIsActive].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_EAST_ENABLED));
        d.put(p + "[West+WindowIsActive].backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_WEST_ENABLED));

        c = PAINTER_DIRECTORY + ".ToolBarHandlePainter";
        d.put(p + "[Enabled].handleIconPainter", new LazyPainter(c, ToolBarHandlePainter.Which.HANDLEICON_ENABLED));
        d.put(p + "[Pressed].handleIconPainter", new LazyPainter(c, ToolBarHandlePainter.Which.HANDLEICON_PRESSED));
        d.put(p + ".handleIcon", new SeaGlassIcon(p, "handleIconPainter", 11, 38));

        c = PAINTER_DIRECTORY + ".ButtonPainter";
        p = "ToolBar:Button";
        d.put(p + ".States", "Enabled,Disabled,Focused,Pressed");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));

        c = PAINTER_DIRECTORY + ".ToolBarToggleButtonPainter";
        p = "ToolBar:ToggleButton";
        d.put(p + ".States", "Enabled,Disabled,Focused,Pressed,Selected");
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put(p + "[Pressed+Selected].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_SELECTED));
        d.put(p + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(c,
            ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled+Selected].backgroundPainter",
            new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_DISABLED_SELECTED));

        uiDefaults.put("ToolBarSeparator[Enabled].backgroundPainter", null);
    }

    /**
     * Initialize the tree settings.
     * 
     * @param d
     *            the UI defaults map.
     */
    private void defineTrees(UIDefaults d) {
        // d.put("\"Tree.cellEditor\".background", d.get("control"));
        // d.put("\"Tree.cellEditor\"[Disabled].textForeground", new
        // ColorUIResource(200, 200, 200));
        // d.put("\"Tree.cellEditor\"[Selected].textForeground", new
        // ColorUIResource(Color.WHITE));
        // d.put("\"Tree.cellEditor\"[Selected].textBackground", new
        // ColorUIResource((Color) d.get("nimbusSelection")));

        d.put("Tree.leftChildIndent", new Integer(12));
        d.put("Tree.rightChildIndent", new Integer(2));

        d.put("Tree.leafIcon", null);
        d.put("Tree.closedIcon", null);
        d.put("Tree.openIcon", null);
    }

    /**
     * Return a short string that identifies this look and feel. This String
     * will be the unquoted String "SeaGlass".
     * 
     * @return a short string identifying this look and feel.
     */
    @Override
    public String getName() {
        return "SeaGlass";
    }

    /**
     * Return a string that identifies this look and feel. This String will be
     * the unquoted String "SeaGlass".
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
     * Returns false, SeaGlassLookAndFeel is not a native look and feel.
     * 
     * @return false
     */
    public boolean isNativeLookAndFeel() {
        return false;
    }

    /**
     * Returns true, SeaGlassLookAndFeel is always supported.
     * 
     * @return true.
     */
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    /**
     * Returns {@code true} if the <code>LookAndFeel</code> returned
     * <code>RootPaneUI</code> instances support providing {@code Window}
     * decorations in a <code>JRootPane</code>.
     * <p>
     * Sea Glass returns {@code false} on a Macintosh, {@code true} otherwise.
     * 
     * @return {@code true} if the {@code RootPaneUI} instances created by this
     *         look and feel support client side decorations. This will be the
     *         case on a non-Macintosh operating system.
     * 
     * @see JDialog#setDefaultLookAndFeelDecorated
     * @see JFrame#setDefaultLookAndFeelDecorated
     * @see JRootPane#setWindowDecorationStyle
     */
    public boolean getSupportsWindowDecorations() {
        return !PlatformUtils.isMac();
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
     * the custom toolbar states, and also used by SeaGlassIcon to determine
     * whether the handle icon needs to be shifted to look correct.
     * 
     * Toollbars are unfortunately odd in the way these things are handled, and
     * so this code exists to unify the logic related to toolbars so it can be
     * shared among the static files such as SeaGlassIcon and generated files
     * such as the ToolBar state classes.
     */
    public static Object resolveToolbarConstraint(JToolBar toolbar) {
        /*
         * NOTE: we don't worry about component orientation or PAGE_END etc
         * because the BasicToolBarUI always uses an absolute position of
         * NORTH/SOUTH/EAST/WEST.
         */
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
        private Enum   which;
        private String className;

        LazyPainter(String className, Enum which) {
            if (className == null) {
                throw new IllegalArgumentException("The className must be specified");
            }

            this.className = className;
            this.which = which;
        }

        @SuppressWarnings("unchecked")
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
                // Find inner class for state.
                Class stateClass = Class.forName(className + "$Which", false, (ClassLoader) cl);
                if (stateClass == null) {
                    throw new NullPointerException("Failed to find the constructor for the class: " + className + ".Which");
                }
                Constructor constructor = c.getConstructor(stateClass);
                if (constructor == null) {
                    throw new NullPointerException("Failed to find the constructor for the class: " + className);
                }
                return constructor.newInstance(which);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * <p>
     * Registers the given region and prefix. The prefix, if it contains quoted
     * sections, refers to certain named components. If there are not quoted
     * sections, then the prefix refers to a generic component type.
     * </p>
     * 
     * <p>
     * If the given region/prefix combo has already been registered, then it
     * will not be registered twice. The second registration attempt will fail
     * silently.
     * </p>
     * 
     * @param region
     *            The Synth Region that is being registered. Such as Button, or
     *            ScrollBarThumb.
     * @param prefix
     *            The UIDefault prefix. For example, could be ComboBox, or if a
     *            named components, "MyComboBox", or even something like
     *            ToolBar:"MyComboBox":"ComboBox.arrowButton"
     */
    public void register(Region region, String prefix) {
        // validate the method arguments
        if (region == null || prefix == null) {
            throw new IllegalArgumentException("Neither Region nor Prefix may be null");
        }

        // Add a LazyStyle for this region/prefix to styleMap.
        List<LazyStyle> styles = styleMap.get(region);
        if (styles == null) {
            styles = new LinkedList<LazyStyle>();
            styles.add(new LazyStyle(prefix));
            styleMap.put(region, styles);
        } else {
            // iterate over all the current styles and see if this prefix has
            // already been registered. If not, then register it.
            for (LazyStyle s : styles) {
                if (prefix.equals(s.prefix)) {
                    return;
                }
            }
            styles.add(new LazyStyle(prefix));
        }

        // add this region to the map of registered regions
        registeredRegions.put(region.getName(), region);
    }

    /**
     * <p>
     * Locate the style associated with the given region, and component. This is
     * called from SeaGlassLookAndFeel in the SynthStyleFactory implementation.
     * </p>
     * 
     * <p>
     * Lookup occurs as follows:<br/>
     * Check the map of styles <code>styleMap</code>. If the map contains no
     * styles at all, then simply return the defaultStyle. If the map contains
     * styles, then iterate over all of the styles for the Region <code>r</code>
     * looking for the best match, based on prefix. If a match was made, then
     * return that SynthStyle. Otherwise, return the defaultStyle.
     * </p>
     * 
     * @param c
     *            The component associated with this region. For example, if the
     *            Region is Region.Button then the component will be a JButton.
     *            If the Region is a subregion, such as ScrollBarThumb, then the
     *            associated component will be the component that subregion
     *            belongs to, such as JScrollBar. The JComponent may be named.
     *            It may not be null.
     * @param r
     *            The region we are looking for a style for. May not be null.
     */
    SynthStyle getSeaGlassStyle(JComponent c, Region r) {
        // validate method arguments
        if (c == null || r == null) {
            throw new IllegalArgumentException("Neither comp nor r may be null");
        }
        // if there are no lazy styles registered for the region r, then return
        // the default style
        List<LazyStyle> styles = styleMap.get(r);
        if (styles == null || styles.size() == 0) {
            return defaultStyle;
        }

        // Look for the best SynthStyle for this component/region pair.
        LazyStyle foundStyle = null;
        for (LazyStyle s : styles) {
            if (s.matches(c)) {
                /*
                 * Replace the foundStyle if foundStyle is null, or if the new
                 * style "s" is more specific (ie, its path was longer), or if
                 * the foundStyle was "simple" and the new style was not (ie:
                 * the foundStyle was for something like Button and the new
                 * style was for something like "MyButton", hence, being more
                 * specific). In all cases, favor the most specific style found.
                 */
                if (foundStyle == null || (foundStyle.parts.length < s.parts.length)
                        || (foundStyle.parts.length == s.parts.length && foundStyle.simple && !s.simple)) {
                    foundStyle = s;
                }
            }
        }

        // return the style, if found, or the default style if not found
        return foundStyle == null ? defaultStyle : foundStyle.getStyle(c);
    }

    /**
     * A class which creates the SeaGlassStyle associated with it lazily, but
     * also manages a lot more information about the style. It is less of a
     * LazyValue type of class, and more of an Entry or Item type of class, as
     * it represents an entry in the list of LazyStyles in the map styleMap.
     * 
     * The primary responsibilities of this class include:
     * <ul>
     * <li>Determining whether a given component/region pair matches this style</li>
     * <li>Splitting the prefix specified in the constructor into its
     * constituent parts to facilitate quicker matching</li>
     * <li>Creating and vending a SeaGlassStyle lazily.</li>
     * </ul>
     */
    private final class LazyStyle {
        /**
         * The prefix this LazyStyle was registered with. Something like Button
         * or ComboBox:"ComboBox.arrowButton"
         */
        private String                                                prefix;

        /**
         * Whether or not this LazyStyle represents an unnamed component
         */
        private boolean                                               simple = true;

        /**
         * The various parts, or sections, of the prefix. For example, the
         * prefix: ComboBox:"ComboBox.arrowButton"
         * 
         * will be broken into two parts, ComboBox and "ComboBox.arrowButton"
         */
        private Part[]                                                parts;

        /**
         * Cached shared style.
         */
        private SeaGlassStyle                                         style;

        /**
         * A weakly referenced hash map such that if the reference JComponent
         * key is garbage collected then the entry is removed from the map. This
         * cache exists so that when a JComponent has overrides in its client
         * map, a unique style will be created and returned for that JComponent
         * instance, always. In such a situation each JComponent instance must
         * have its own instance of SeaGlassStyle.
         */
        private WeakHashMap<JComponent, WeakReference<SeaGlassStyle>> overridesCache;

        /**
         * Create a new LazyStyle.
         * 
         * @param prefix
         *            The prefix associated with this style. Cannot be null.
         */
        private LazyStyle(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("The prefix must not be null");
            }

            this.prefix = prefix;

            /*
             * There is one odd case that needs to be supported here: cell
             * renderers. A cell renderer is defined as a named internal
             * component, so for example: List."List.cellRenderer" The problem
             * is that the component named List.cellRenderer is not a child of a
             * JList. Rather, it is treated more as a direct component.
             * 
             * Thus, if the prefix ends with "cellRenderer", then remove all the
             * previous dotted parts of the prefix name so that it becomes, for
             * example: "List.cellRenderer" Likewise, we have a hacked work
             * around for cellRenderer, renderer, and listRenderer.
             */
            String temp = prefix;
            if (temp.endsWith("cellRenderer\"") || temp.endsWith("renderer\"") || temp.endsWith("listRenderer\"")) {
                temp = temp.substring(temp.lastIndexOf(":\"") + 1);
            }

            // Otherwise, normal code path.
            List<String> sparts = split(temp);
            parts = new Part[sparts.size()];
            for (int i = 0; i < parts.length; i++) {
                parts[i] = new Part(sparts.get(i));
                if (parts[i].named) {
                    simple = false;
                }
            }
        }

        /**
         * Gets the style. Creates it if necessary.
         * 
         * @return the style
         */
        SynthStyle getStyle(JComponent c) {
            // If the component has overrides, it gets its own unique style
            // instead of the shared style.
            if (c.getClientProperty("SeaGlass.Overrides") != null) {
                if (overridesCache == null) {
                    overridesCache = new WeakHashMap<JComponent, WeakReference<SeaGlassStyle>>();
                }
                WeakReference<SeaGlassStyle> ref = overridesCache.get(c);
                SeaGlassStyle s = ref == null ? null : ref.get();
                if (s == null) {
                    s = new SeaGlassStyle(prefix, c);
                    overridesCache.put(c, new WeakReference<SeaGlassStyle>(s));
                }
                return s;
            }

            // Lazily create the style if necessary.
            if (style == null) {
                style = new SeaGlassStyle(prefix, null);
            }

            // Return the style.
            return style;
        }

        /**
         * This LazyStyle is a match for the given component if, and only if,
         * for each part of the prefix the component hierarchy matches exactly.
         * That is, if given "a":something:"b", then: c.getName() must equals
         * "b" c.getParent() can be anything c.getParent().getParent().getName()
         * must equal "a".
         */
        boolean matches(JComponent c) {
            return matches(c, parts.length - 1);
        }

        private boolean matches(Component c, int partIndex) {
            if (partIndex < 0) return true;
            if (c == null) return false;
            // only get here if partIndex > 0 and c == null

            String name = c.getName();
            if (parts[partIndex].named && parts[partIndex].s.equals(name)) {
                // so far so good, recurse
                return matches(c.getParent(), partIndex - 1);
            } else if (!parts[partIndex].named) {
                // If c is not named, and parts[partIndex] has an expected class
                // type registered, then check to make sure c is of the right
                // type;
                Class clazz = parts[partIndex].c;
                if (clazz != null && clazz.isAssignableFrom(c.getClass())) {
                    // so far so good, recurse
                    return matches(c.getParent(), partIndex - 1);
                } else if (clazz == null && registeredRegions.containsKey(parts[partIndex].s)) {
                    Region r = registeredRegions.get(parts[partIndex].s);
                    Component parent = r.isSubregion() ? c : c.getParent();
                    // special case the JInternalFrameTitlePane, because it
                    // doesn't fit the mold. very, very funky.
                    if (r == Region.INTERNAL_FRAME_TITLE_PANE && parent != null && parent instanceof JInternalFrame.JDesktopIcon) {
                        JInternalFrame.JDesktopIcon icon = (JInternalFrame.JDesktopIcon) parent;
                        parent = icon.getInternalFrame();
                    } else if (r == Region.INTERNAL_FRAME_TITLE_PANE && c instanceof SeaGlassTitlePane) {
                        // Also special case the title pane. Its parent is the
                        // layered pane and hasn't yet been assigned, but we
                        // want it to behave as if its parent is an internal
                        // frame.
                        if (partIndex <= 0
                                || (parts[partIndex - 1].c != null && parts[partIndex - 1].c.isAssignableFrom(JInternalFrame.class))) {
                            return true;
                        }
                    }
                    // it was the name of a region. So far, so good. Recurse.
                    return matches(parent, partIndex - 1);
                }
            }

            return false;
        }

        /**
         * Given some dot separated prefix, split on the colons that are not
         * within quotes, and not within brackets.
         * 
         * @param prefix
         * @return
         */
        private List<String> split(String prefix) {
            List<String> parts = new ArrayList<String>();
            int bracketCount = 0;
            boolean inquotes = false;
            int lastIndex = 0;
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);

                if (c == '[') {
                    bracketCount++;
                    continue;
                } else if (c == '"') {
                    inquotes = !inquotes;
                    continue;
                } else if (c == ']') {
                    bracketCount--;
                    if (bracketCount < 0) {
                        throw new RuntimeException("Malformed prefix: " + prefix);
                    }
                    continue;
                }

                if (c == ':' && !inquotes && bracketCount == 0) {
                    // found a character to split on.
                    parts.add(prefix.substring(lastIndex, i));
                    lastIndex = i + 1;
                }
            }
            if (lastIndex < prefix.length() - 1 && !inquotes && bracketCount == 0) {
                parts.add(prefix.substring(lastIndex));
            }
            return parts;

        }

        private final class Part {
            private String  s;
            // true if this part represents a component name
            private boolean named;
            private Class   c;

            Part(String s) {
                named = s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"';
                if (named) {
                    this.s = s.substring(1, s.length() - 1);
                } else {
                    this.s = s;
                    // TODO use a map of known regions for Synth and Swing, and
                    // then use [classname] instead of org_class_name style
                    try {
                        c = Class.forName("javax.swing.J" + s);
                    } catch (Exception e) {
                    }
                    try {
                        c = Class.forName(s.replace("_", "."));
                    } catch (Exception e) {
                    }
                }
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
    public static SynthStyle updateStyle(SeaGlassContext context, SynthUI ui) {
        SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();

        if (newStyle != oldStyle) {
            if (oldStyle != null) {
                oldStyle.uninstallDefaults(context);
            }
            context.setStyle(newStyle);
            ((SeaGlassStyle) newStyle).installDefaults(context, ui);
        }
        return newStyle;
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
        /*
         * Note: The following two Sea Glass based overrides should be
         * refactored to be in the SeaGlass LAF. Due to constraints in an update
         * release, we couldn't actually provide the public API necessary to
         * allow SeaGlassLookAndFeel (a subclass of SynthLookAndFeel) to provide
         * its own rules for shouldUpdateStyle.
         */
        else if ("SeaGlass.Overrides" == eName) {
            // Always update when the SeaGlass.Overrides client property has
            // been changed
            return true;
        } else if ("SeaGlass.Overrides.InheritDefaults" == eName) {
            // Always update when the SeaGlass.Overrides.InheritDefaults
            // client property has changed
            return true;
        } else if ("JComponent.sizeVariant" == eName) {
            // Always update when the JComponent.sizeVariant
            // client property has changed
            return true;
        } else if (eName != null && (eName.startsWith("JButton.") || eName.startsWith("JTextField."))) {
            // Always update when an Apple-style variant client property has
            // changed.
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
    public static void setSelectedUI(ComponentUI uix, boolean selected, boolean focused, boolean enabled, boolean rollover) {
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
    public static void resetSelectedUI() {
        selectedUI = null;
    }
}
