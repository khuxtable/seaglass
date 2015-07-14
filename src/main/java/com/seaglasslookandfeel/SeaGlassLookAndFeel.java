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
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

import sun.swing.plaf.GTKKeybindings;
import sun.swing.plaf.WindowsKeybindings;
import sun.swing.plaf.synth.DefaultSynthStyle;

import com.seaglasslookandfeel.component.SeaGlassIcon;
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
import com.seaglasslookandfeel.painter.FileChooserPainter;
import com.seaglasslookandfeel.painter.FrameAndRootPainter;
import com.seaglasslookandfeel.painter.MenuItemPainter;
import com.seaglasslookandfeel.painter.MenuPainter;
import com.seaglasslookandfeel.painter.OptionPanePainter;
import com.seaglasslookandfeel.painter.SeaGlassPainter;
import com.seaglasslookandfeel.painter.PopupMenuPainter;
import com.seaglasslookandfeel.painter.ProgressBarPainter;
import com.seaglasslookandfeel.painter.RadioButtonMenuItemPainter;
import com.seaglasslookandfeel.painter.RadioButtonPainter;
import com.seaglasslookandfeel.painter.ScrollBarButtonPainter;
import com.seaglasslookandfeel.painter.ScrollBarThumbPainter;
import com.seaglasslookandfeel.painter.ScrollBarTrackPainter;
import com.seaglasslookandfeel.painter.ScrollPanePainter;
import com.seaglasslookandfeel.painter.SearchFieldIconPainter;
import com.seaglasslookandfeel.painter.SearchFieldPainter;
import com.seaglasslookandfeel.painter.SeparatorPainter;
import com.seaglasslookandfeel.painter.SliderThumbPainter;
import com.seaglasslookandfeel.painter.SliderTrackPainter;
import com.seaglasslookandfeel.painter.SpinnerFormattedTextFieldPainter;
import com.seaglasslookandfeel.painter.SpinnerNextButtonPainter;
import com.seaglasslookandfeel.painter.SpinnerPreviousButtonPainter;
import com.seaglasslookandfeel.painter.SplitPaneDividerPainter;
import com.seaglasslookandfeel.painter.TabbedPaneTabAreaPainter;
import com.seaglasslookandfeel.painter.TabbedPaneTabCloseButtonPainter;
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
import com.seaglasslookandfeel.painter.ToolTipPainter;
import com.seaglasslookandfeel.painter.TreeCellEditorPainter;
import com.seaglasslookandfeel.painter.TreeCellPainter;
import com.seaglasslookandfeel.painter.TreePainter;
import com.seaglasslookandfeel.state.ComboBoxArrowButtonEditableState;
import com.seaglasslookandfeel.state.ComboBoxEditableState;
import com.seaglasslookandfeel.state.InternalFrameWindowFocusedState;
import com.seaglasslookandfeel.state.MenuNotUnified;
import com.seaglasslookandfeel.state.ProgressBarFinishedState;
import com.seaglasslookandfeel.state.ProgressBarIndeterminateState;
import com.seaglasslookandfeel.state.RootPaneNoFrameState;
import com.seaglasslookandfeel.state.RootPaneWindowFocusedState;
import com.seaglasslookandfeel.state.ScrollBarButtonIsIncreaseButtonState;
import com.seaglasslookandfeel.state.ScrollBarButtonsTogetherState;
import com.seaglasslookandfeel.state.SearchFieldHasPopupState;
import com.seaglasslookandfeel.state.SliderArrowShapeState;
import com.seaglasslookandfeel.state.SplitPaneDividerVerticalState;
import com.seaglasslookandfeel.state.SplitPaneVerticalState;
import com.seaglasslookandfeel.state.TabbedPaneBottomTabState;
import com.seaglasslookandfeel.state.TabbedPaneLeftTabState;
import com.seaglasslookandfeel.state.TabbedPaneRightTabState;
import com.seaglasslookandfeel.state.TabbedPaneTopTabState;
import com.seaglasslookandfeel.state.TableHeaderRendererSortedState;
import com.seaglasslookandfeel.state.TextAreaNotInScrollPaneState;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;
import com.seaglasslookandfeel.state.TitlePaneCloseButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneIconifyButtonWindowMinimizedState;
import com.seaglasslookandfeel.state.TitlePaneIconifyButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneMaximizeButtonWindowMaximizedState;
import com.seaglasslookandfeel.state.TitlePaneMaximizeButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneMenuButtonWindowNotFocusedState;
import com.seaglasslookandfeel.state.TitlePaneWindowFocusedState;
import com.seaglasslookandfeel.state.ToolBarWindowIsActiveState;
import com.seaglasslookandfeel.ui.SeaglassUI;
import com.seaglasslookandfeel.util.MacKeybindings;
import com.seaglasslookandfeel.util.PlatformUtils;
import com.seaglasslookandfeel.util.SeaGlassGraphicsUtils;

/**
 * This is the main Sea Glass Look and Feel class.
 *
 * <p>At the moment, it customizes Nimbus, and includes some code from
 * NimbusLookAndFeel and SynthLookAndFeel where those methods were package
 * local.</p>
 *
 * @author Kathryn Huxtable
 * @author Kenneth Orr
 * @see    javax.swing.plaf.synth.SynthLookAndFeel
 */
public class SeaGlassLookAndFeel extends SynthLookAndFeel {
    private static final long serialVersionUID = 4589080729685347322L;

    /** Used in a handful of places where we need an empty Insets. */
    public static final Insets EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);

    /** Used in IMAGE_DIRECTORY and UI_PACKAGE_PREFIX. */
    private static final String PACKAGE_DIRECTORY = SeaGlassLookAndFeel.class.getPackage().getName();

    /** Set the image directory name based on the root package. */
    private static final String PAINTER_PREFIX = PACKAGE_DIRECTORY + ".painter.";

    /** Set the package name for UI delegates based on the root package. */
    private static final String UI_PACKAGE_PREFIX = PACKAGE_DIRECTORY + ".ui.SeaGlass";

    private static final String[] UI_LIST = new String[]{
        "Button",
        "CheckBox",
        "CheckBoxMenuItem",
        "ComboBox",
        "DesktopPane",
        "EditorPane",
        "FormattedTextField",
        "Label",
        "List",
        "Menu",
        "MenuItem",
        "InternalFrame",
        "DesktopIcon",
        "OptionPane",
        "Panel",
        "PasswordField",
        "PopupMenu",
        "ProgressBar",
        "RadioButton",
        "RadioButtonMenuItem",
        "RootPane",
        "ScrollBar",
        "ScrollPane",
        "SearchFieldButton",
        "Separator",
        "Slider",
        "Spinner",
        "SplitPane",
        "TabbedPane",
        "Table",
        "TableHeader",
        "TextField",
        "TextArea",
        "TextPane",
        "ToolTip",
        "ToggleButton",
        "ToolBar",
        "Tree",
        "Viewport",
        "PopupMenuSeparator", 
        "ToolBarSeparator",
    };
    
    
    /** Refer to setSelectedUI */
    public static ComponentUI selectedUI;

    /** Refer to setSelectedUI */
    public static int selectedUIState;

    /**
     * The map of SynthStyles. This map is keyed by Region. Each Region maps to
     * a List of LazyStyles. Each LazyStyle has a reference to the prefix that
     * was registered with it. This reference can then be inspected to see if it
     * is the proper lazy style.
     *
     * <p>There can be more than one LazyStyle for a single Region if there is
     * more than one prefix defined for a given region. For example, both Button
     * and "MyButton" might be prefixes assigned to the Region.Button region.
     * </p>
     */
    private Map<Region, List<LazyStyle>> styleMap = new HashMap<Region, List<LazyStyle>>();

    /**
     * A map of regions which have been registered. This mapping is maintained
     * so that the Region can be found based on prefix in a very fast manner.
     * This is used in the "matches" method of LazyStyle.
     */

    private Map<String, Region> registeredRegions = new HashMap<String, Region>();

    /**
     * Our fallback style to avoid NPEs if the proper style cannot be found in
     * this class. Not sure if relying on DefaultSynthStyle is the best choice.
     */
    private DefaultSynthStyle defaultStyle;

    /**
     * The default font that will be used. I store this value so that it can be
     * set in the UIDefaults when requested.
     */
    private Font defaultFont;
    
    private UIDefaults uiDefaults = null;

    /**
     * Create a new Sea Glass Look and Feel instance.
     */
    public SeaGlassLookAndFeel() {
        super();
        /*
         * Register all of the regions and their states that this class will use
         * for later lookup. Additional regions can be registered later by 3rd
         * party components. These are simply the default registrations.
         */
        registerStyles();
    }

    /**
     * @return 
     * 
     */
    private DefaultSynthStyle getDefaultStyle() {
        if (defaultStyle == null) {
            defaultStyle = new DefaultSynthStyle();
            defaultStyle.setFont((Font) this.uiDefaults.get("defaultFont"));
        }
        return defaultStyle;
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
        removeOurUIs();
        resetDefaultBorders();
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
        register(SeaGlassRegion.SCROLL_BAR_CAP, "ScrollBar:ScrollBarCap");
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
        register(SeaGlassRegion.TABBED_PANE_TAB_CLOSE_BUTTON, "TabbedPane:TabbedPaneTab:TabbedPaneTabClaseButton");
        register(Region.TABBED_PANE_TAB_AREA, "TabbedPane:TabbedPaneTabArea");
        register(Region.ARROW_BUTTON, "TabbedPane:TabbedPaneTabArea:\"TabbedPaneTabArea.button\"");
        register(Region.TABBED_PANE_CONTENT, "TabbedPane:TabbedPaneContent");
        register(Region.TABLE, "Table");
        register(Region.LABEL, "Table:\"Table.cellRenderer\"");
        register(Region.TABLE_HEADER, "TableHeader");
        register(Region.LABEL, "TableHeader:\"TableHeader.renderer\"");
        register(Region.TEXT_FIELD, "\"Table.editor\"");
        register(Region.TEXT_FIELD, "\"Tree.cellEditor\"");
        register(Region.TEXT_FIELD, "TextField");
        register(SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON, "TextField:SearchFieldFindButton");
        register(SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON, "TextField:SearchFieldCancelButton");
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
     * Returns the defaults for SeaGlassLookAndFeel.
     *
     * @return the UI defaults for SeaGlassLookAndFeel.
     */
    @Override
    public UIDefaults getDefaults() {
        if (uiDefaults == null) {
            uiDefaults =  new UIWrapper(super.getDefaults());

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
            useOurUIs();

            defineBaseColors(uiDefaults);
            defineDefaultBorders(uiDefaults);
            defineArrowButtons(uiDefaults);
            defineButtons(uiDefaults);
            defineComboBoxes(uiDefaults);
            defineDesktopPanes(uiDefaults);
            defineInternalFrames(uiDefaults);
            defineInternalFrameMenuButtons(uiDefaults);
            defineInternalFrameCloseButtons(uiDefaults);
            defineInternalFrameIconifyButtons(uiDefaults);
            defineInternalFrameMaximizeButton(uiDefaults);
            defineLists(uiDefaults);
            defineMenus(uiDefaults);
            definePanels(uiDefaults);
            definePopups(uiDefaults);
            defineProgressBars(uiDefaults);
            defineRootPanes(uiDefaults);
            defineSeparators(uiDefaults);
            defineSpinners(uiDefaults);
            defineScrollBars(uiDefaults);
            defineScrollPane(uiDefaults);
            defineSliders(uiDefaults);
            defineSplitPanes(uiDefaults);
            defineTabbedPanes(uiDefaults);
            defineTables(uiDefaults);
            defineTextControls(uiDefaults);
            defineToolBars(uiDefaults);
            defineTrees(uiDefaults);
            defineToolTips(uiDefaults);
            defineOptionPane(uiDefaults);
            defineFileChooser(uiDefaults);

            if (!PlatformUtils.isMac()) {
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

    private void defineOptionPane(UIDefaults d) {
        //Initialize OptionPane
        d.put("OptionPane.contentMargins", new InsetsUIResource(15, 15, 15, 15));
        d.put("OptionPane.opaque", Boolean.TRUE);
        d.put("OptionPane.background", getDerivedColor("control", 0, 0, 0, 0, true));
        d.put("OptionPane.buttonOrientation", new Integer(4));
        d.put("OptionPane.messageAnchor", new Integer(17));
        d.put("OptionPane.separatorPadding", new Integer(0));
        d.put("OptionPane.sameSizeButtons", Boolean.FALSE);
        d.put("OptionPane:\"OptionPane.separator\".contentMargins", new InsetsUIResource(1, 0, 0, 0));
        d.put("OptionPane:\"OptionPane.messageArea\".contentMargins", new InsetsUIResource(0, 0, 10, 0));
        d.put("OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\".contentMargins", new InsetsUIResource(0, 10, 10, 10));

        d.put("OptionPane[Enabled].errorIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.OptionPanePainter", OptionPanePainter.Which.ERRORICON_ENABLED));
        d.put("OptionPane.errorIcon", new SeaGlassIcon("OptionPane", "errorIconPainter", 48, 48));
        d.put("OptionPane[Enabled].informationIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.OptionPanePainter", OptionPanePainter.Which.INFORMATIONICON_ENABLED));
        d.put("OptionPane.informationIcon", new SeaGlassIcon("OptionPane", "informationIconPainter", 48, 48));
        d.put("OptionPane[Enabled].questionIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.OptionPanePainter", OptionPanePainter.Which.QUESTIONICON_ENABLED));
        d.put("OptionPane.questionIcon", new SeaGlassIcon("OptionPane", "questionIconPainter", 48, 48));
        d.put("OptionPane[Enabled].warningIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.OptionPanePainter", OptionPanePainter.Which.WARNINGICON_ENABLED));
        d.put("OptionPane.warningIcon", new SeaGlassIcon("OptionPane", "warningIconPainter", 48, 48));
        
        // Color Chooser Dialog
        d.put("ColorChooser.swatchesDefaultRecentColor", getDerivedColor("control", 0, 0, 0, 0, true));
    }
    
    
    private void defineFileChooser(UIDefaults d) {
        //Initialize FileChooser
        d.put("FileChooser.contentMargins", new InsetsUIResource(10, 10, 10, 10));
        d.put("FileChooser.opaque", Boolean.TRUE);
        d.put("FileChooser.usesSingleFilePane", Boolean.TRUE);
        d.put("FileChooser[Enabled].fileIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.FILEICON_ENABLED));
        d.put("FileChooser.fileIcon", new SeaGlassIcon("FileChooser", "fileIconPainter", 16, 16));
        d.put("FileChooser[Enabled].directoryIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.DIRECTORYICON_ENABLED));
        d.put("FileChooser.directoryIcon", new SeaGlassIcon("FileChooser", "directoryIconPainter", 16, 16));
        d.put("FileChooser[Enabled].upFolderIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.UPFOLDERICON_ENABLED));
        d.put("FileChooser.upFolderIcon", new SeaGlassIcon("FileChooser", "upFolderIconPainter", 16, 16));
        d.put("FileChooser[Enabled].newFolderIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.NEWFOLDERICON_ENABLED));
        d.put("FileChooser.newFolderIcon", new SeaGlassIcon("FileChooser", "newFolderIconPainter", 16, 16));
        d.put("FileChooser[Enabled].hardDriveIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.HARDDRIVEICON_ENABLED));
        d.put("FileChooser.hardDriveIcon", new SeaGlassIcon("FileChooser", "hardDriveIconPainter", 16, 16));
        d.put("FileChooser[Enabled].floppyDriveIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.FLOPPYDRIVEICON_ENABLED));
        d.put("FileChooser.floppyDriveIcon", new SeaGlassIcon("FileChooser", "floppyDriveIconPainter", 16, 16));
        d.put("FileChooser[Enabled].homeFolderIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.HOMEFOLDERICON_ENABLED));
        d.put("FileChooser.homeFolderIcon", new SeaGlassIcon("FileChooser", "homeFolderIconPainter", 16, 16));
        d.put("FileChooser[Enabled].detailsViewIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.DETAILSVIEWICON_ENABLED));
        d.put("FileChooser.detailsViewIcon", new SeaGlassIcon("FileChooser", "detailsViewIconPainter", 16, 16));
        d.put("FileChooser[Enabled].listViewIconPainter", new LazyPainter("com.seaglasslookandfeel.painter.FileChooserPainter", FileChooserPainter.Which.LISTVIEWICON_ENABLED));
        d.put("FileChooser.listViewIcon", new SeaGlassIcon("FileChooser", "listViewIconPainter", 16, 16));
    }

    /**
     * Define some settings for tool tips.
     * @param d
     */
    private void defineToolTips(UIDefaults d) {
        String p = "ToolTip";
        d.put("seaGlassToolTipBorder", new Color(0x5b7ea4));
        d.put(p + ".contentMargins", new InsetsUIResource(4, 4, 4, 4));
        d.put(p + ".opaque", Boolean.FALSE);
        d.put(p + ".background", new ColorUIResource(0xd5, 0xe8, 0xf7));
        d.put(p + ".backgroundPainter", new ToolTipPainter(ToolTipPainter.Which.BORDER_ENABLED));
    }

    /**
     * Use our UI delegate for the specified UI control type.
     *
     * @param d      the UI defaults map.
     * @param uiName the UI type, e.g. "ScrollPane".
     */
    private void useOurUIs() {
        for (String uiName : UI_LIST) {
            uiName = uiName + "UI";
            uiDefaults.put(uiName, UI_PACKAGE_PREFIX + uiName);
        }
    }
    
    
    /**
     * A convience method that will reset the Style of StyleContext if
     * necessary.
     *
     * @return newStyle
     */
   
    public static SynthStyle updateSeaglassStyle(SynthContext context, SeaglassUI ui) {
        SynthStyle newStyle = getStyle(context.getComponent(), context.getRegion());
        // TODO rossi 04.07.2011 this code is now private in the Synth L&F
//        SynthStyle oldStyle = context.getStyle();
//
//        if (newStyle != oldStyle) {
//            if (oldStyle != null) {
//                oldStyle.uninstallDefaults(context);
//            }
//            context.setStyle(newStyle);
//            newStyle.installDefaults(context, ui);
//        }
        return newStyle;
    }    

    /**
     * Use our UI delegate for the specified UI control type.
     *
     * @param d      the UI defaults map.
     * @param uiName the UI type, e.g. "ScrollPane".
     */
    private void removeOurUIs() {
        for (String uiName : UI_LIST) {
            uiDefaults.remove(uiName + "UI");
        }
    }
    
    /**
     * Initialize the default font.
     *
     * @return the default font.
     */
    protected Font initializeDefaultFont() {
        /*
         * Set the default font to Lucida Grande if available, else use Lucida
         * Sans Unicode. Grande is a later font and a bit nicer looking, but it
         * is a derivation of Sans Unicode, so they're compatible.
         */
        if (defaultFont == null) {
    
            if (PlatformUtils.isMac()) {
                defaultFont = new Font("Lucida Grande", Font.PLAIN, 13);
            }
    
            if (defaultFont == null) {
                defaultFont = new Font("Dialog", Font.PLAIN, 13);
            }
    
            if (defaultFont == null) {
                defaultFont = new Font("SansSerif", Font.PLAIN, 13);
            }
        }

        return defaultFont;
    }
       
    /**
     * Initialize the base font.
     *
     * @param d the UI defaults map.
     */
    private void defineDefaultFont(UIDefaults d) {
        d.put("defaultFont", initializeDefaultFont());
    }

    /**
     * Initialize the base colors.
     *
     * @param d the UI defaults map.
     */
    private void defineBaseColors(UIDefaults d) {
        d.put("control", new ColorUIResource(0xf8f8f8));

        // Copied from Nimbus

        //Color palette
        addColor(d, "text", 0, 0, 0, 255);
        addColor(d, "seaGlassBase", 51, 98, 140, 255);
        addColor(d, "seaGlassBlueGrey", "seaGlassBase", 0.032459438f, -0.52518797f, 0.19607842f, 0);
        addColor(d, "seaGlassOrange", 191, 98, 4, 255);
        addColor(d, "seaGlassGreen", 176, 179, 50, 255);
        addColor(d, "seaGlassRed", 169, 46, 34, 255);
        addColor(d, "seaGlassBorder", "seaGlassBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
        addColor(d, "seaGlassSelection", "seaGlassBase", -0.010750473f, -0.04875779f, -0.007843137f, 0);
        addColor(d, "seaGlassInfoBlue", 47, 92, 180, 255);
        addColor(d, "seaGlassAlertYellow", 255, 220, 35, 255);
        addColor(d, "seaGlassFocus", 115, 164, 209, 255);
        addColor(d, "seaGlassSelectedText", 255, 255, 255, 255);
        addColor(d, "seaGlassSelectionBackground", 57, 105, 138, 255);
        addColor(d, "seaGlassDisabledText", 142, 143, 145, 255);
        addColor(d, "seaGlassLightBackground", 255, 255, 255, 255);
        addColor(d, "infoText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "info", 242, 242, 189, 255);
        addColor(d, "menuText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "menu", "seaGlassBase", 0.021348298f, -0.6150531f, 0.39999998f, 0);
        addColor(d, "scrollbar", "seaGlassBlueGrey", -0.006944418f, -0.07296763f, 0.09019607f, 0);
        addColor(d, "controlText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "controlHighlight", "seaGlassBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
        addColor(d, "controlLHighlight", "seaGlassBlueGrey", 0.0f, -0.098526314f, 0.2352941f, 0);
        addColor(d, "controlShadow", "seaGlassBlueGrey", -0.0027777553f, -0.0212406f, 0.13333333f, 0);
        addColor(d, "controlDkShadow", "seaGlassBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
        addColor(d, "textHighlight", "seaGlassSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "textHighlightText", "seaGlassSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "textInactiveText", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "desktop", "seaGlassBase", -0.009207249f, -0.13984653f, -0.07450983f, 0);
        addColor(d, "activeCaption", "seaGlassBlueGrey", 0.0f, -0.049920253f, 0.031372547f, 0);
        addColor(d, "inactiveCaption", "seaGlassBlueGrey", -0.00505054f, -0.055526316f, 0.039215684f, 0);

        //The global style definition
        addColor(d, "textForeground", "text", 0.0f, 0.0f, 0.0f, 255);
        addColor(d, "[Disabled].textForeground", "textForeground", 0.0f, 0.0f, 0.0f, 80);
        addColor(d, "textBackground", "seaGlassSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "background", "control", 0, 0, 0, 0);
        
        d.put("TitledBorder.position", "ABOVE_TOP");
        d.put("TitledBorder.titleColor", Color.BLACK);
        d.put("TitledBorder.font", new DerivedFont("defaultFont", 1.0f, false, false));
        d.put("FileView.fullRowSelection", Boolean.TRUE);
        
        // Seaglass starts here
        
        
        d.put("seaGlassSelection", new Color(0x6181a5));
        d.put("seaGlassFocus", new Color(0x73a4d1));
        d.put("seaGlassBorder", new Color(0x9297a1));
        d.put("seaGlassSelectedText", Color.WHITE);
        d.put("seaGlassSelectionBackground", new Color(0x6181a5));
        d.put("seaGlassDisabledText", new Color(0xc8c8c8));
        d.put("textHighlight", d.get("seaGlassSelectionBackground"));
        d.put("textHighlightText", Color.WHITE);
        d.put("textInactiveText", d.get("seaGlassDisabledText"));
        d.put("menuText", Color.WHITE);
        d.put("menu", Color.WHITE);

        d.put("seaGlassTransparent", new Color(0x0, true));
        d.put("seaGlassTextEmphasis", new Color(255, 255, 255, 110));
        d.put("seaGlassDropShadow", new Color(211, 211, 211));
        d.put("seaGlassInnerShadow", new Color(0x20000000, true));
        d.put("seaGlassStyleDefaultColor", new ColorUIResource(Color.BLACK));
        d.put("seaGlassFocusInsets", new Insets(2, 2, 2, 2));
        d.put("seaGlassTransparent", new Color(0, true));
        d.put("seaGlassFocus", new Color(0x73a4d1));
        d.put("seaGlassOuterFocus", getDerivedColor("seaGlassFocus", -0.0028f, 0.01f, 0f, -0x80, true));

        if (PlatformUtils.isMac()) {
            d.put("seaGlassToolBarFocus", d.get("seaGlassFocus"));
            d.put("seaGlassToolBarOuterFocus", d.get("seaGlassOuterFocus"));
            d.put("seaGlassToolBarDisabledText", new Color(0x80000000, true));
        } else {
            d.put("seaGlassToolBarFocus", new Color(0x5b7ea4)); // Rossi: new color for toolbar line
            d.put("seaGlassToolBarOuterFocus", getDerivedColor("seaGlassToolBarFocus", -0.0028f, 0.01f, 0f, -0x80, true));
            d.put("seaGlassToolBarDisabledText", new Color(0x80000000, true));
        }

        d.put("seaGlassTableSelectionActiveBottom", new Color(0x7daaea));
        d.put("seaGlassTableSelectionInactiveBottom", new Color(0xe0e0e0));

        d.put("seaGlassSearchPlaceholderText", new Color(0x808080));

        d.put("seaGlassSearchIcon", new Color(0x404040));
        d.put("seaGlassCancelIcon", new Color(0xb3b3b3));
        d.put("seaGlassCancelIconPressed", new Color(0x808080));

        d.put("seaGlassTextEnabledBorder", new Color(0x709ad0)); // Rossi: Blue borders for text fields
        d.put("seaGlassTextDisabledBorder", SeaGlassGraphicsUtils.disable( new Color(0x709ad0))); // new Color(0xdddddd)
        d.put("seaGlassTextEnabledToolbarBorder", new Color(0x888888));

        d.put("seaGlassMenuIcon", new Color(0x5b7ea4)); // Rossi: Menu "Icons" (Arrow, checkbox, radio) are now no longer black
    }
    
    /**
     * Border factory is static so we need to do some nasty reflection tricks to change it.
     * @param d
     */
    private void defineDefaultBorders(UIDefaults d) {
        try {
            if (Boolean.FALSE.toString().equalsIgnoreCase(System.getProperty("SeaGlass.BorderFactory.overrideDefaults"))) {
                return; 
            }
            
            // Etched Borders
            Field highLightColor = EtchedBorder.class.getDeclaredField("highlight");
            highLightColor.setAccessible(true);
            Field shadowcolor = EtchedBorder.class.getDeclaredField("shadow");
            shadowcolor.setAccessible(true);

            Border etchedBorder = BorderFactory.createEtchedBorder();
            highLightColor.set(etchedBorder, new Color(0xd5e8f7));
            shadowcolor.set(etchedBorder, new Color(0x709ad0));

            Border raisedEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            highLightColor.set(raisedEtchedBorder, new Color(0xd5e8f7));
            shadowcolor.set(raisedEtchedBorder, new Color(0x709ad0));
            
            // Bevel Borders
            Field highlightOuter = BevelBorder.class.getDeclaredField("highlightOuter");
            highlightOuter.setAccessible(true);
            Field highlightInner = BevelBorder.class.getDeclaredField("highlightInner");
            highlightInner.setAccessible(true);
            Field shadowOuter = BevelBorder.class.getDeclaredField("shadowOuter");
            shadowOuter.setAccessible(true);
            Field shadowInner = BevelBorder.class.getDeclaredField("shadowInner");
            shadowInner.setAccessible(true);

            Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
            highlightOuter.set(loweredBevelBorder, new Color(0xd5e8f7));
            highlightInner.set(loweredBevelBorder, Color.WHITE);
            shadowOuter.set(loweredBevelBorder, new Color(0x5f83a0));
            shadowInner.set(loweredBevelBorder, new Color(0xd5e8f7));

            Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
            highlightOuter.set(raisedBevelBorder, new Color(0x95c5eb));
            highlightInner.set(raisedBevelBorder, Color.WHITE);
            shadowOuter.set(raisedBevelBorder, new Color(0x5f83a0));
            shadowInner.set(raisedBevelBorder, new Color(0xd5e8f7));
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * Border factory is static so we need to do some nasty reflection tricks to change it.
     * @param d
     */
    private void resetDefaultBorders() {
        try {
            // Etched Borders
            Field highLightColor = EtchedBorder.class.getDeclaredField("highlight");
            highLightColor.setAccessible(true);
            Field shadowcolor = EtchedBorder.class.getDeclaredField("shadow");
            shadowcolor.setAccessible(true);

            Border etchedBorder = BorderFactory.createEtchedBorder();
            highLightColor.set(etchedBorder, null);
            shadowcolor.set(etchedBorder, null);

            Border raisedEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            highLightColor.set(raisedEtchedBorder, null);
            shadowcolor.set(raisedEtchedBorder, null);
            
            // Bevel Borders
            Field highlightOuter = BevelBorder.class.getDeclaredField("highlightOuter");
            highlightOuter.setAccessible(true);
            Field highlightInner = BevelBorder.class.getDeclaredField("highlightInner");
            highlightInner.setAccessible(true);
            Field shadowOuter = BevelBorder.class.getDeclaredField("shadowOuter");
            shadowOuter.setAccessible(true);
            Field shadowInner = BevelBorder.class.getDeclaredField("shadowInner");
            shadowInner.setAccessible(true);

            Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
            highlightOuter.set(loweredBevelBorder, null);
            highlightInner.set(loweredBevelBorder, null);
            shadowOuter.set(loweredBevelBorder, null);
            shadowInner.set(loweredBevelBorder, null);

            Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
            highlightOuter.set(raisedBevelBorder, null);
            highlightInner.set(raisedBevelBorder, null);
            shadowOuter.set(raisedBevelBorder, null);
            shadowInner.set(raisedBevelBorder, null);
            
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Use Aqua settings for some properties if we're on a Mac.
     *
     * @param d the UI defaults map.
     */
    private void defineAquaSettings(UIDefaults d) {
        try {

            // Instantiate Aqua but don't install it.
            Class<?>    lnfClass     = Class.forName(UIManager.getSystemLookAndFeelClassName(), true,
                                                     Thread.currentThread().getContextClassLoader());
            LookAndFeel aqua         = (LookAndFeel) lnfClass.newInstance();
            UIDefaults  aquaDefaults = aqua.getDefaults();

            // Use Aqua for any menu UI classes.
            d.put("MenuBarUI", aquaDefaults.get("MenuBarUI"));
            d.put("MenuUI", aquaDefaults.get("MenuUI"));
        } catch (Exception e) {

            // TODO Should we do something with this exception?
            e.printStackTrace();
        }
    }

    /**
     * Initialize the arrow button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineArrowButtons(UIDefaults d) {
        String c = PAINTER_PREFIX + "ArrowButtonPainter";
        String p = "ArrowButton";

        d.put(p + ".States", "Enabled,MouseOver,Disabled,Pressed");
        d.put(p + "[Disabled].foreground", new ColorUIResource(0x9ba8cf));
        d.put(p + "[Enabled].foreground",  new ColorUIResource(0x5b7ea4));            // getDerivedColor("seaGlassBlueGrey", -0.6111111f, -0.110526316f, -0.34509805f, 0, true)); // JTabbedPane arrows no longer black: Nimbus dependency should be replaced with color value?
        
        d.put(p + "[Pressed].foreground", new ColorUIResource(0x134D8C));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[MouseOver].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_PRESSED));
    }

    /**
     * Initialize button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineButtons(UIDefaults d) {
        
        d.put("Button.contentMargins", new InsetsUIResource(6, 14, 6, 14));
        d.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        
        d.put("buttonBorderBaseEnabled", new Color(0x709ad0));
        d.put("buttonBorderBasePressed", new Color(0x4879bf));

        d.put("buttonInteriorBaseEnabled", new Color(0xd5e8f7));
        d.put("buttonInteriorBasePressed", new Color(0x6d8fba));
        d.put("buttonInteriorBaseSelected", new Color(0x80a6d2));
        d.put("buttonInteriorBasePressedSelected", new Color(0x7497c2));

        d.put("texturedButtonBorderBaseEnabled", new Color(0x999999));

        d.put("texturedButtonInteriorBaseEnabled", new Color(0xf0f0f0));
        d.put("texturedButtonInteriorBasePressed", new Color(0x8eb3d2));
        d.put("texturedButtonInteriorBaseSelected", new Color(0x98c1e2));
        d.put("texturedButtonInteriorBasePressedSelected", new Color(0x7e9fba));

        d.put("buttonBulletBottomEnabled", Color.BLACK);

        d.put("buttonArrow", Color.BLACK);

        String p = "Button";
        String c = PAINTER_PREFIX + "ButtonPainter";

        // Initialize Button
        d.put(p + ".States", "Enabled,Pressed,Disabled,Focused,Default");
        d.put(p + "[Default+Pressed].textForeground", new ColorUIResource(Color.black));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Default].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DEFAULT));
        d.put(p + "[Default+Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DEFAULT_FOCUSED));
        d.put(p + "[Default+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_DEFAULT));
        d.put(p + "[Default+Focused+Pressed].backgroundPainter",
              new LazyPainter(c,
                              ButtonPainter.Which.BACKGROUND_PRESSED_DEFAULT_FOCUSED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));

        // Initialize ToggleButton
        p = "ToggleButton";
        d.put(p + ".contentMargins", new InsetsUIResource(6, 14, 6, 14));
        d.put(p + ".States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put(p + "[Selected].textForeground", new ColorUIResource(Color.black));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Default+Pressed].textForeground", new ColorUIResource(Color.black));
        d.put(p + "[Focused+Selected].textForeground", new ColorUIResource(Color.black));
        d.put(p + "[Disabled+Selected].textForeground", new ColorUIResource(new Color(0, 0, 0, 0x80)));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter",
              new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter",
              new LazyPainter(c, ButtonPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put(p + "[Pressed+Selected].backgroundPainter",
              new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_SELECTED));
        d.put(p + "[Focused+Pressed+Selected].backgroundPainter",
              new LazyPainter(c,
                              ButtonPainter.Which.BACKGROUND_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled+Selected].backgroundPainter",
              new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED_SELECTED));

        // Initialize CheckBox
        p = "CheckBox";
        c = PAINTER_PREFIX + "CheckBoxPainter";
        d.put(p + ".States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_DISABLED));
        d.put(p + "[Enabled].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_ENABLED));
        d.put(p + "[Focused].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_FOCUSED));
        d.put(p + "[Pressed].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED));
        d.put(p + "[Focused+Pressed].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_FOCUSED));
        d.put(p + "[Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_SELECTED));
        d.put(p + "[Focused+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_SELECTED_FOCUSED));
        d.put(p + "[Pressed+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_SELECTED));
        d.put(p + "[Focused+Pressed+Selected].iconPainter",
              new LazyPainter(c, CheckBoxPainter.Which.ICON_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled+Selected].iconPainter", new LazyPainter(c, CheckBoxPainter.Which.ICON_DISABLED_SELECTED));
        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 18, 18));

        // Initialize RadioButton
        p = "RadioButton";
        c = PAINTER_PREFIX + "RadioButtonPainter";
        d.put(p + ".States", "Enabled,Pressed,Disabled,Focused,Selected");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_DISABLED));
        d.put(p + "[Enabled].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_ENABLED));
        d.put(p + "[Focused].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_FOCUSED));
        d.put(p + "[Pressed].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED));
        d.put(p + "[Focused+Pressed].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED_FOCUSED));
        d.put(p + "[Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_SELECTED));
        d.put(p + "[Focused+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_SELECTED_FOCUSED));
        d.put(p + "[Pressed+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_PRESSED_SELECTED));
        d.put(p + "[Focused+Pressed+Selected].iconPainter",
              new LazyPainter(c,
                              RadioButtonPainter.Which.ICON_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled+Selected].iconPainter", new LazyPainter(c, RadioButtonPainter.Which.ICON_DISABLED_SELECTED));
        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 18, 18));
    }

    /**
     * Initialize the combo box settings.
     *
     * @param d the UI defaults map.
     */
    private void defineComboBoxes(UIDefaults d) {
        // Copied from nimbus
        
        d.put("ComboBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("ComboBox.forceOpaque", Boolean.TRUE);
        d.put("ComboBox.buttonWhenNotEditable", Boolean.TRUE);
        d.put("ComboBox.rendererUseListColors", Boolean.FALSE);
        d.put("ComboBox.pressedWhenPopupVisible", Boolean.TRUE);
        d.put("ComboBox.squareButton", Boolean.FALSE);
        d.put("ComboBox.popupInsets", new InsetsUIResource(-2, 2, 0, 2));
        d.put("ComboBox.padding", new InsetsUIResource(2, 2, 2, 2));

        d.put("ComboBox:\"ComboBox.arrowButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("ComboBox:\"ComboBox.arrowButton\".size", new Integer(19));
       
        d.put("ComboBox:\"ComboBox.listRenderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
        d.put("ComboBox:\"ComboBox.listRenderer\".opaque", Boolean.TRUE);
        addColor(d, "ComboBox:\"ComboBox.listRenderer\".background", "seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "ComboBox:\"ComboBox.listRenderer\"[Disabled].textForeground", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", "seaGlassSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "ComboBox:\"ComboBox.listRenderer\"[Selected].background", "seaGlassSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
       
        d.put("ComboBox:\"ComboBox.renderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
        addColor(d, "ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "ComboBox:\"ComboBox.renderer\"[Selected].textForeground", "seaGlassSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "ComboBox:\"ComboBox.renderer\"[Selected].background", "seaGlassSelectionBackground", 0.0f, 0.0f, 0.0f, 0);

        //Initialize \"ComboBox.scrollPane\"
        d.put("\"ComboBox.scrollPane\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        
        // Seaglass starts below.
        
        String p = "ComboBox";
        d.put(p + ".States", "Enabled,Pressed,Selected,Disabled,Focused,Editable");
        d.put(p + ".Editable", new ComboBoxEditableState());
        d.put(p + ":\"ComboBox.arrowButton\".Editable", new ComboBoxArrowButtonEditableState());


        // Background
        String c = PAINTER_PREFIX + "ComboBoxPainter";

        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Disabled+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED_PRESSED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+Selected].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED_SELECTED));

        d.put(p + "[Disabled+Editable].backgroundPainter",
              new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_DISABLED_EDITABLE));
        d.put(p + "[Editable+Enabled].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_ENABLED_EDITABLE));
        d.put(p + "[Editable+Focused].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_FOCUSED_EDITABLE));
        d.put(p + "[Editable+Pressed].backgroundPainter", new LazyPainter(c, ComboBoxPainter.Which.BACKGROUND_PRESSED_EDITABLE));

        // Editable arrow
        c = PAINTER_PREFIX + "ComboBoxArrowButtonPainter";
        p = "ComboBox:\"ComboBox.arrowButton\"";
        d.put(p + ".size", new Integer(22));
        d.put(p + ".States", "Enabled,Pressed,Disabled,Editable");
        d.put(p + "[Disabled+Editable].backgroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Editable+Enabled].backgroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Editable+Pressed].backgroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Editable+Selected].backgroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_PRESSED));
        d.put(p + "[Selected].foregroundPainter", new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_SELECTED));
        d.put(p + "[Editable].foregroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_ENABLED_EDITABLE));
        d.put(p + "[Editable+Disabled].foregroundPainter",
              new LazyPainter(c, ComboBoxArrowButtonPainter.Which.FOREGROUND_DISABLED_EDITABLE));

        // Textfield
        c = PAINTER_PREFIX + "ComboBoxTextFieldPainter";
        
        p = "ComboBox:\"ComboBox.textField\"";
        d.put(p + ".contentMargins", new InsetsUIResource(2, 6, 2, 0));
        d.put(p + ".background", Color.WHITE);
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ComboBoxTextFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ComboBoxTextFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, ComboBoxTextFieldPainter.Which.BACKGROUND_SELECTED));
    }

    /**
     * Initialize the desktop pane UI settings.
     *
     * @param d the UI defaults map.
     */
    private void defineDesktopPanes(UIDefaults d) {
        d.put("seaGlassDesktopPane", new ColorUIResource(0x556ba6));
        String c = PAINTER_PREFIX + "DesktopPanePainter";

        String p = "DesktopPane";

        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, DesktopPanePainter.Which.BACKGROUND_ENABLED));

        // Initialize DesktopIcon
        p = "DesktopIcon";
        c = PAINTER_PREFIX + "DesktopIconPainter";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 6, 5, 4));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, DesktopIconPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Set the icons to paint the title pane decorations.
     *
     * @param d the UI defaults map.
     */
    private void defineInternalFrames(UIDefaults d) {
        
        // Copied from nimbus
        
        //Initialize InternalFrameTitlePane
        d.put("InternalFrameTitlePane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("InternalFrameTitlePane.maxFrameIconSize", new DimensionUIResource(18, 18));

        //Initialize InternalFrame
        d.put("InternalFrame.contentMargins", new InsetsUIResource(1, 6, 6, 6));
        d.put("InternalFrame:InternalFrameTitlePane.contentMargins", new InsetsUIResource(3, 0, 3, 0));
        d.put("InternalFrame:InternalFrameTitlePane.titleAlignment", "CENTER");
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        d.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        
        // Seaglass starts below
        
        if (PlatformUtils.isMac()) {
            d.put("frameBaseActive", new Color(0xa8a8a8));
        } else {
            d.put("frameBaseActive", new Color(0x96adc4));
        }

        d.put("frameBaseInactive", new Color(0xe0e0e0));

        d.put("frameBorderBase", new Color(0x545454));

        d.put("frameInnerHighlightInactive", new Color(0x55ffffff, true));
        d.put("frameInnerHighlightActive", new Color(0x55ffffff, true));

        d.put("seaGlassTitlePaneButtonEnabledBorder", new Color(0x99000000, true));
        d.put("seaGlassTitlePaneButtonEnabledCorner", new Color(0x26000000, true));
        d.put("seaGlassTitlePaneButtonEnabledInterior", new Color(0x99ffffff, true));

        d.put("seaGlassTitlePaneButtonHoverBorder", new Color(0xe5101010, true));
        d.put("seaGlassTitlePaneButtonHoverCorner", new Color(0x267a7a7a, true));
        d.put("seaGlassTitlePaneButtonHoverInterior", new Color(0xffffff));

        d.put("seaGlassTitlePaneButtonPressedBorder", new Color(0xe50e0e0e, true));
        d.put("seaGlassTitlePaneButtonPressedCorner", new Color(0x876e6e6e, true));
        d.put("seaGlassTitlePaneButtonPressedInterior", new Color(0xe6e6e6));

        String p = "InternalFrame";
        String c = PAINTER_PREFIX + "FrameAndRootPainter";
        
        d.put(p + ".titleFont", new DerivedFont("defaultFont", 1.0f, true, null));

        d.put(p + ".States", "Enabled,WindowFocused");
        d.put(p + ":InternalFrameTitlePane.WindowFocused", new TitlePaneWindowFocusedState());
        d.put(p + ".WindowFocused", new InternalFrameWindowFocusedState());

        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+WindowFocused].backgroundPainter",
              new LazyPainter(c,
                              FrameAndRootPainter.Which.BACKGROUND_ENABLED_WINDOWFOCUSED));

        p = "InternalFrameTitlePane";
        d.put(p + ".buttonSpacing", 0);

        p = "InternalFrame:InternalFrameTitlePane";
        d.put(p + "[Enabled].textForeground", d.get("seaGlassDisabledText"));
        d.put(p + "[WindowFocused].textForeground", Color.BLACK);
    }

    /**
     * Initialize the internal frame close button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineInternalFrameCloseButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"";
        String c = PAINTER_PREFIX + "TitlePaneCloseButtonPainter";

        // Set the multiplicity of states for the Close button.
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
        d.put(p + ".WindowNotFocused", new TitlePaneCloseButtonWindowNotFocusedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneCloseButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 43, 18));
    }

    /**
     * Initialize the internal frame iconify button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineInternalFrameIconifyButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"";
        String c = PAINTER_PREFIX + "TitlePaneIconifyButtonPainter";

        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused,WindowMinimized");
        d.put(p + ".WindowNotFocused", new TitlePaneIconifyButtonWindowNotFocusedState());
        d.put(p + ".WindowMinimized", new TitlePaneIconifyButtonWindowMinimizedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the iconify button states.
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter(c, TitlePaneIconifyButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));
        
        // Set the restore button states.
        d.put(p + "[Disabled+WindowMinimized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_DISABLED));
        d.put(p + "[Enabled+WindowMinimized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_ENABLED));
        d.put(p + "[MouseOver+WindowMinimized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_MOUSEOVER));
        d.put(p + "[Pressed+WindowMinimized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_PRESSED));
        d.put(p + "[Enabled+WindowMinimized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowMinimized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowMinimized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneIconifyButtonPainter.Which.BACKGROUND_MINIMIZED_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 26, 18));
    }

    /**
     * Initialize the internal frame maximize button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineInternalFrameMaximizeButton(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"";
        String c = PAINTER_PREFIX + "TitlePaneMaximizeButtonPainter";

        d.put(p + ".WindowNotFocused", new TitlePaneMaximizeButtonWindowNotFocusedState());
        d.put(p + ".WindowMaximized", new TitlePaneMaximizeButtonWindowMaximizedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the maximize button states.
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TitlePaneMaximizeButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_PRESSED_WINDOWNOTFOCUSED));

        // Set the restore button states.
        d.put(p + "[Disabled+WindowMaximized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_DISABLED));
        d.put(p + "[Enabled+WindowMaximized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_ENABLED));
        d.put(p + "[MouseOver+WindowMaximized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_MOUSEOVER));
        d.put(p + "[Pressed+WindowMaximized].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_PRESSED));
        d.put(p + "[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter",
              new LazyPainter(c,
                              TitlePaneMaximizeButtonPainter.Which.BACKGROUND_MAXIMIZED_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 25, 18));
    }

    /**
     * Initialize the internal frame menu button settings.
     *
     * @param d the UI defaults map.
     */
    private void defineInternalFrameMenuButtons(UIDefaults d) {
        String p = "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"";
        String c = PAINTER_PREFIX + "TitlePaneMenuButtonPainter";

        d.put(p + ".WindowNotFocused", new TitlePaneMenuButtonWindowNotFocusedState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Set the states for the Menu button.
        d.put(p + "[Enabled].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_ENABLED));
        d.put(p + "[Disabled].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_DISABLED));
        d.put(p + "[MouseOver].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_MOUSEOVER));
        d.put(p + "[Pressed].iconPainter", new LazyPainter(c, TitlePaneMenuButtonPainter.Which.ICON_PRESSED));
        d.put(p + "[Enabled+WindowNotFocused].iconPainter",
              new LazyPainter(c,
                              TitlePaneMenuButtonPainter.Which.ICON_ENABLED_WINDOWNOTFOCUSED));
        d.put(p + "[MouseOver+WindowNotFocused].iconPainter",
              new LazyPainter(c,
                              TitlePaneMenuButtonPainter.Which.ICON_MOUSEOVER_WINDOWNOTFOCUSED));
        d.put(p + "[Pressed+WindowNotFocused].iconPainter",
              new LazyPainter(c,
                              TitlePaneMenuButtonPainter.Which.ICON_PRESSED_WINDOWNOTFOCUSED));

        d.put(p + ".icon", new SeaGlassIcon(p, "iconPainter", 19, 18));
    }

    /**
     * Initialize the list settings.
     *
     * @param d the UI defaults map.
     */
    private void defineLists(UIDefaults d) {
        String p = "List";

        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.TRUE);

        d.put(p + ".background", d.get("seaGlassLightBackground"));
        d.put(p + ".dropLineColor", d.get("seaGlassFocus"));
        d.put(p + ".rendererUseListColors", Boolean.TRUE);
        d.put(p + ".rendererUseUIBorder", Boolean.TRUE);
        d.put(p + ".cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        d.put(p + ".focusCellHighlightBorder",
              new BorderUIResource(new PainterBorder("Tree:TreeCell[Enabled+Focused].backgroundPainter",
                                                     new Insets(2, 5, 2, 5))));

        // TODO Why doesn't ColorUIResource work here?
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled+Selected].textBackground", Color.WHITE);
        d.put(p + "[Disabled].textForeground", d.get("seaGlassDisabledText"));

        p = "List:\"List.cellRenderer\"";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + "[Disabled].textForeground", d.get("seaGlassDisabledText"));
        d.put(p + "[Disabled].background", d.get("seaGlassSelectionBackground"));
    }

    /**
     * Initialize the menu settings.
     *
     * @param d the UI defaults map.
     */
    private void defineMenus(UIDefaults d) {
        d.put("menuItemBackgroundBase", new Color(0x5b7ea4));

        // Initialize Menu
        String c = PAINTER_PREFIX + "MenuPainter";
        String p = "Menu";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 5));
        d.put(p + "[Disabled].textForeground", d.get("seaGlassDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Enabled+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[Enabled+Selected].backgroundPainter", new LazyPainter(c, MenuPainter.Which.BACKGROUND_ENABLED_SELECTED));
        d.put(p + "[Disabled].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_DISABLED));
        d.put(p + "[Enabled].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_ENABLED));
        d.put(p + "[Enabled+Selected].arrowIconPainter", new LazyPainter(c, MenuPainter.Which.ARROWICON_ENABLED_SELECTED));
        d.put(p + ".arrowIcon", new SeaGlassIcon(p + "", "arrowIconPainter", 9, 10));
        d.put(p + ".checkIcon", new SeaGlassIcon(p + "", "checkIconPainter", 6, 10));

        p = "Menu:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // We don't paint MenuBar backgrounds. Remove the painters.
        c = PAINTER_PREFIX + "MenuBarPainter";
        p = "MenuBar";
        d.put(p + ".contentMargins", new InsetsUIResource(2, 6, 2, 6));

        if (d.get(p + "[Enabled].backgroundPainter") != null) {
            d.remove(p + "[Enabled].backgroundPainter");
        }

        if (d.get(p + "[Enabled].borderPainter") != null) {
           d.remove(p + "[Enabled].borderPainter");
        }

        // Rossi: "Selected Menu" color changed to dark blue. Not tested with "unified" title/menu/toolbar
        c = PAINTER_PREFIX + "MenuItemPainter";
        p = "MenuBar:Menu";
        d.put(p + ".States", "Enabled,Selected,Disabled,NotUnified");
        d.put(p + ".NotUnified", new MenuNotUnified());
        d.put(p + ".contentMargins", new InsetsUIResource(1, 4, 2, 4));
        
        d.put(p + "[Disabled].textForeground", d.getColor("seaGlassDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[Selected].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, MenuItemPainter.Which.BACKGROUND_MOUSEOVER_UNIFIED));

        d.put(p + "[Enabled+NotUnified].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Enabled+Selected+NotUnified].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[Enabled+Selected+NotUnified].backgroundPainter", new LazyPainter(c, MenuItemPainter.Which.BACKGROUND_MOUSEOVER));

        p = "MenuBar:Menu:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));

        // Initialize MenuItem
        c = PAINTER_PREFIX + "MenuItemPainter";
        p = "MenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + ".acceleratorFont", new DerivedFont("defaultFont", 1.0f, null, null));
        d.put(p + "[Disabled].textForeground", d.getColor("seaGlassDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, MenuItemPainter.Which.BACKGROUND_MOUSEOVER));

        p = "MenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", d.getColor("seaGlassDisabledText"));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // Initialize CheckBoxMenuItem
        c = PAINTER_PREFIX + "CheckBoxMenuItemPainter";
        p = "CheckBoxMenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + "[Disabled].textForeground", d.getColor("seaGlassDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, CheckBoxMenuItemPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver+Selected].backgroundPainter",
              new LazyPainter(c, CheckBoxMenuItemPainter.Which.BACKGROUND_SELECTED_MOUSEOVER));

        d.put(p + "[Disabled+Selected].checkIconPainter",
              new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_DISABLED_SELECTED));
        d.put(p + "[Enabled+Selected].checkIconPainter",
              new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_ENABLED_SELECTED));
// Rossi: Added painter that shows an "indicator" that menu item is a "selectable checkbox"
        d.put(p + "[Enabled].checkIconPainter",
            new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_ENABLED));
        d.put(p + "[MouseOver].checkIconPainter",
            new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_ENABLED_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].checkIconPainter",
              new LazyPainter(c, CheckBoxMenuItemPainter.Which.CHECKICON_SELECTED_MOUSEOVER));
        d.put(p + ".checkIcon", new SeaGlassIcon(p, "checkIconPainter", 9, 10));

        p = "CheckBoxMenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));

        // Initialize RadioButtonMenuItem
        c = PAINTER_PREFIX + "RadioButtonMenuItemPainter";
        p = "RadioButtonMenuItem";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 12, 2, 13));
        d.put(p + ".textIconGap", new Integer(5));
        d.put(p + "[Disabled].textForeground", d.getColor("seaGlassDisabledText"));
        d.put(p + "[Enabled].textForeground", new ColorUIResource(Color.BLACK));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, RadioButtonMenuItemPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].textForeground", new ColorUIResource(Color.WHITE));
        d.put(p + "[MouseOver+Selected].backgroundPainter",
              new LazyPainter(c, RadioButtonMenuItemPainter.Which.BACKGROUND_SELECTED_MOUSEOVER));
        
        d.put(p + "[Disabled+Selected].checkIconPainter",
              new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_DISABLED_SELECTED));
        d.put(p + "[Enabled+Selected].checkIconPainter",
              new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_ENABLED_SELECTED));
        // Rossi: Added painter that shows an "indicator" that menu item is a "selectable radio button"
        d.put(p + "[Enabled].checkIconPainter",
            new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_ENABLED));
        d.put(p + "[MouseOver].checkIconPainter",
            new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_ENABLED_MOUSEOVER));
        d.put(p + "[MouseOver+Selected].checkIconPainter",
              new LazyPainter(c, RadioButtonMenuItemPainter.Which.CHECKICON_SELECTED_MOUSEOVER));
        d.put(p + ".checkIcon", new SeaGlassIcon(p, "checkIconPainter", 9, 10));

        p = "RadioButtonMenuItem:MenuItemAccelerator";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[MouseOver].textForeground", new ColorUIResource(Color.WHITE));
    }

    /**
     * Initialize the panel settings.
     *
     * @param d the UI defaults map.
     */
    private void definePanels(UIDefaults d) {
        String p = "Panel";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".background", new ColorUIResource((Color) d.get("control")));
        d.put(p + ".opaque", Boolean.TRUE);
    }

    /**
     * Initialize the popup settings.
     *
     * @param d the UI defaults map.
     */
    private void definePopups(UIDefaults d) {
        d.put("seaGlassPopupBorder", new ColorUIResource(0xbbbbbb));

        d.put("popupMenuInteriorEnabled", Color.WHITE);
        // Rossi: Changed color of popup / submenus to get better contrast to bright backgrounds.
//        d.put("popupMenuBorderEnabled", new Color(0xdddddd));
        d.put("popupMenuBorderEnabled", new Color(0x5b7ea4));

        String c = PAINTER_PREFIX + "PopupMenuPainter";
        String p = "PopupMenu";

        d.put(p + ".contentMargins", new InsetsUIResource(6, 1, 6, 1));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".consumeEventOnClose", Boolean.TRUE);
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, PopupMenuPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, PopupMenuPainter.Which.BACKGROUND_ENABLED));

        // Initialize PopupMenuSeparator
        c = PAINTER_PREFIX + "SeparatorPainter";
        p = "PopupMenuSeparator";
        d.put(p + ".contentMargins", new InsetsUIResource(1, 0, 2, 0));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SeparatorPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Initialize the progress bar settings.
     *
     * @param d the UI defaults map.
     */
    private void defineProgressBars(UIDefaults d) {
        // Copied from nimbus

      //Initialize ProgressBar
        d.put("ProgressBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("ProgressBar.States", "Enabled,Disabled,Indeterminate,Finished");
        d.put("ProgressBar.tileWhenIndeterminate", Boolean.TRUE);
        d.put("ProgressBar.paintOutsideClip", Boolean.TRUE);
        d.put("ProgressBar.rotateText", Boolean.TRUE);
        d.put("ProgressBar.vertictalSize", new DimensionUIResource(19, 150));
        d.put("ProgressBar.horizontalSize", new DimensionUIResource(150, 19));
        addColor(d, "ProgressBar[Disabled].textForeground", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        d.put("ProgressBar[Disabled+Indeterminate].progressPadding", new Integer(3));
        
        // Seaglass starts below.
        
        d.put("progressBarTrackInterior", Color.WHITE);
        d.put("progressBarTrackBase", new Color(0x4076bf));

        d.put("ProgressBar.Indeterminate", new ProgressBarIndeterminateState());
        d.put("ProgressBar.Finished", new ProgressBarFinishedState());

        String p = "ProgressBar";
        String c = PAINTER_PREFIX + "ProgressBarPainter";

        d.put(p + ".cycleTime", 500);
        d.put(p + ".progressPadding", new Integer(3));
        d.put(p + ".trackThickness", new Integer(19));
        d.put(p + ".tileWidth", new Integer(24));
        d.put(p + ".backgroundFillColor", Color.WHITE);
        d.put(p + ".font", new DerivedFont("defaultFont", 0.769f, null, null));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ProgressBarPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ProgressBarPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Enabled+Finished].foregroundPainter",
              new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_ENABLED_FINISHED));
        d.put(p + "[Enabled+Indeterminate].foregroundPainter",
              new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_ENABLED_INDETERMINATE));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Disabled+Finished].foregroundPainter",
              new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_DISABLED_FINISHED));
        d.put(p + "[Disabled+Indeterminate].foregroundPainter",
              new LazyPainter(c, ProgressBarPainter.Which.FOREGROUND_DISABLED_INDETERMINATE));
    }

    /**
     * Initialize the root pane settings.
     *
     * @param d the UI defaults map.
     */
    private void defineRootPanes(UIDefaults d) {
        String c = PAINTER_PREFIX + "FrameAndRootPainter";
        String p = "RootPane";

        d.put(p + ".States", "Enabled,WindowFocused,NoFrame");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.FALSE);
        d.put(p + ".NoFrame", new RootPaneNoFrameState());
        d.put(p + ".WindowFocused", new RootPaneWindowFocusedState());

        d.put(p + "[Enabled+NoFrame].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED_NOFRAME));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+WindowFocused].backgroundPainter", new LazyPainter(c, FrameAndRootPainter.Which.BACKGROUND_ENABLED_WINDOWFOCUSED));
    }
    
    /**
     * Initialize the scroll pane UI
     * @param d
     */
    private void defineScrollPane(UIDefaults d) {
        // Define ScrollPane border painters.
        String c = PAINTER_PREFIX + "ScrollPanePainter";
        String p = "ScrollPane";
        d.put(p + ".opaque", Boolean.FALSE);
        d.put(p + ".contentMargins", new InsetsUIResource(3, 3, 3, 3));
        // d.put(p + ".useChildTextComponentFocus", Boolean.TRUE);
        d.put(p + ".backgroundPainter", new LazyPainter(c, ScrollPanePainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+Focused].borderPainter", new LazyPainter(c, ScrollPanePainter.Which.BORDER_ENABLED_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, ScrollPanePainter.Which.BORDER_ENABLED));

        // Store ScrollPane Corner Component
        d.put(p + ".cornerPainter", new LazyPainter(c, ScrollPanePainter.Which.CORNER_ENABLED));
        
        //Initialize Viewport
        p = "Viewport";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.TRUE);
    }

    /**
     * Initialize the scroll bar UI settings.
     *
     * @param d the UI defaults map.
     */
    private void defineScrollBars(UIDefaults d) {
        d.put("scrollBarThumbBorderBasePressed", new Color(0x4879bf));
        d.put("scrollBarThumbInteriorBasePressed", new Color(0x82a8ca));
        d.put("scrollBarButtonBase", Color.WHITE);
        d.put("scrollBarButtonBasePressed", new Color(0xa1bfdb));
        d.put("scrollBarTrackBackgroundBase", Color.WHITE);
        d.put("scrollBarTrackGradientBase", d.get("seaGlassTransparent"));

        d.put("ScrollBar.incrementButtonGap", new Integer(-7));
        d.put("ScrollBar.decrementButtonGap", new Integer(-7));
        d.put("ScrollBar.capSize", new Integer(11));
        d.put("ScrollBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("ScrollBar.thumbHeight", new Integer(15));
        d.put("ScrollBar.minimumThumbSize", new DimensionUIResource(29, 29));
        d.put("ScrollBar.maximumThumbSize", new DimensionUIResource(1000, 1000));

        // Buttons
        String c = PAINTER_PREFIX + "ScrollBarButtonPainter";
        String p = "ScrollBar:\"ScrollBar.button\"";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".size", new Integer(22));
        
        d.put(p + ".States", "Enabled,Pressed,MouseOver,IncreaseButton,Disabled,ButtonsTogether");
        d.put(p + ".IncreaseButton", new ScrollBarButtonIsIncreaseButtonState());
        d.put(p + ".ButtonsTogether", new ScrollBarButtonsTogetherState());
        d.put(p + ".foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[MouseOver].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_PRESSED));
        d.put(p + "[MouseOver+Pressed].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_PRESSED));
        d.put(p + "[IncreaseButton+Enabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_ENABLED));
        d.put(p + "[MouseOver+IncreaseButton].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_ENABLED));
        d.put(p + "[IncreaseButton+Disabled].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_DISABLED));
        d.put(p + "[IncreaseButton+Pressed].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_PRESSED));
        d.put(p + "[MouseOver+IncreaseButton+Pressed].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_PRESSED));
        d.put(p + "[Enabled+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_ENABLED_TOGETHER));
        d.put(p + "[Disabled+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_DISABLED_TOGETHER));
        d.put(p + "[Pressed+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_PRESSED_TOGETHER));
        d.put(p + "[IncreaseButton+Enabled+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_ENABLED_TOGETHER));
        d.put(p + "[IncreaseButton+Disabled+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_DISABLED_TOGETHER));
        d.put(p + "[IncreaseButton+Pressed+ButtonsTogether].foregroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_INCREASE_PRESSED_TOGETHER));

        // Thumb
        // Seems to be a bug somewhere where MouseOver is always delivered even
        // when we don't want it, but if it's not specified nothing at all is
        // painted.
        c = PAINTER_PREFIX + "ScrollBarThumbPainter";
        p = "ScrollBar:ScrollBarThumb";
        d.put(p + ".States", "Enabled,Pressed,MouseOver,Disabled");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[MouseOver+Pressed].backgroundPainter", new LazyPainter(c, ScrollBarThumbPainter.Which.BACKGROUND_PRESSED));

        // Track
        c = PAINTER_PREFIX + "ScrollBarTrackPainter";
        p = "ScrollBar:ScrollBarTrack";
        d.put(p + ".States", "Enabled,Disabled");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ScrollBarTrackPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ScrollBarTrackPainter.Which.BACKGROUND_ENABLED));

        // Cap
        c = PAINTER_PREFIX + "ScrollBarButtonPainter";
        p = "ScrollBar:ScrollBarCap";
        d.put(p + ".States", "Enabled,Disabled");
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_CAP));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ScrollBarButtonPainter.Which.FOREGROUND_CAP));
    }

    /**
     * Initialize the separator settings.
     *
     * @param d the UI defaults map.
     */
    private void defineSeparators(UIDefaults d) {
        String c = PAINTER_PREFIX + "SeparatorPainter";
        d.put("Separator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("Separator[Enabled].backgroundPainter", new LazyPainter(c, SeparatorPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Initialize the slider settings.
     *
     * @param d the UI defaults map.
     */
    private void defineSliders(UIDefaults d) { 
    	// Rossi: slider inner color changed from gray to "white"
        d.put("sliderTrackBorderBase", new Color(0x709ad0));
//        d.put("sliderTrackInteriorBase", new Color(0x709ad0)); // blue better?
        d.put("sliderTrackInteriorBase", Color.WHITE); // Light blue better?
        String p = "Slider";
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        d.put(p + ".ArrowShape", new SliderArrowShapeState());
        d.put(p + ".thumbWidth", new Integer(17));
        d.put(p + ".thumbHeight", new Integer(20));
        d.put(p + ".trackBorder", new Integer(0));
        d.put(p + ".trackHeight", new Integer(5));
        // Rossi: Changed ticks to dark blue to make them "less massive"
        d.put(p + ".tickColor", new Color(0x5b7ea4));
        d.put(p + "[Disabled].tickColor", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
        d.put(p + ".font", new DerivedFont("defaultFont", 0.769f, null, null));
        d.put(p + ".paintValue", Boolean.FALSE);

        p = "Slider:SliderThumb";
        String c = PAINTER_PREFIX + "SliderThumbPainter";

        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        d.put(p + ".ArrowShape", new SliderArrowShapeState());
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Focused+MouseOver].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED));
        d.put(p + "[MouseOver].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_MOUSEOVER));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[ArrowShape+Enabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_ENABLED_ARROWSHAPE));
        d.put(p + "[ArrowShape+Disabled].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_DISABLED_ARROWSHAPE));
        d.put(p + "[ArrowShape+MouseOver].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_MOUSEOVER_ARROWSHAPE));
        d.put(p + "[ArrowShape+Pressed].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_PRESSED_ARROWSHAPE));
        d.put(p + "[ArrowShape+Focused].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED_ARROWSHAPE));
        d.put(p + "[ArrowShape+Focused+MouseOver].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE));
        d.put(p + "[ArrowShape+Focused+Pressed].backgroundPainter", new LazyPainter(c, SliderThumbPainter.Which.BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE));

        p = "Slider:SliderTrack";
        c = PAINTER_PREFIX + "SliderTrackPainter";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        d.put(p + ".ArrowShape", new SliderArrowShapeState());
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SliderTrackPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SliderTrackPainter.Which.BACKGROUND_ENABLED));
    }

    /**
     * Initialize the spinner UI settings;
     *
     * @param d the UI defaults map.
     */
    private void defineSpinners(UIDefaults d) {
        d.put("spinnerNextBorderBottomEnabled", new Color(0x4779bf));
        d.put("spinnerNextBorderBottomPressed", new Color(0x4879bf));
        d.put("spinnerNextInteriorBottomEnabled", new Color(0x85abcf));
        d.put("spinnerNextInteriorBottomPressed", new Color(0x6e92b6));

        d.put("spinnerPrevBorderTopEnabled", new Color(0x4778bf));

        d.put("spinnerPrevInteriorTopEnabled", new Color(0x81aed4));
        d.put("spinnerPrevInteriorBottomEnabled", new Color(0xaad4f1));
        d.put("spinnerPrevInteriorPressedTop", new Color(0x6c91b8));
        d.put("spinnerPrevInteriorPressedBottom", new Color(0x9cc3de));

        d.put("spinnerPrevTopLineEnabled", new Color(0xacc8e0));
        d.put("spinnerPrevTopLinePressed", new Color(0x9eb6cf));

        d.put("Spinner.contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put("Spinner:\"Spinner.editor\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("Spinner:\"Spinner.textField\".contentMargins", new InsetsUIResource(4, 6, 4, 0));
        d.put("Spinner:\"Spinner.formattedTextField\".contentMargins", new InsetsUIResource(4, 6, 4, 2));

        
        String c = PAINTER_PREFIX + "SpinnerFormattedTextFieldPainter";
        String p = "Spinner:Panel:\"Spinner.formattedTextField\"";
        d.put(p + ".contentMargins", new InsetsUIResource(3, 10, 3, 2));
        d.put(p + ".background", Color.WHITE);
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c, SpinnerFormattedTextFieldPainter.Which.BACKGROUND_SELECTED_FOCUSED));

        c = PAINTER_PREFIX + "SpinnerPreviousButtonPainter";
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

        c = PAINTER_PREFIX + "SpinnerNextButtonPainter";
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
     * @param d the UI defaults map.
     */
    private void defineSplitPanes(UIDefaults d) {
        d.put("splitPaneDividerBackgroundOuter", new Color(0xd9d9d9));

        String p = "SplitPane";

        d.put(p + ".contentMargins", new InsetsUIResource(1, 1, 1, 1));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        d.put(p + ".Vertical", new SplitPaneVerticalState());
        d.put(p + ".size", new Integer(11));
        d.put(p + ".dividerSize", new Integer(11));
        d.put(p + ".centerOneTouchButtons", Boolean.TRUE);
        d.put(p + ".oneTouchButtonOffset", new Integer(20));
        d.put(p + ".oneTouchButtonVOffset", new Integer(3));
        d.put(p + ".oneTouchExpandable", Boolean.FALSE);
        d.put(p + ".continuousLayout", Boolean.TRUE);

        String c = PAINTER_PREFIX + "SplitPaneDividerPainter";

        p = "SplitPane:SplitPaneDivider";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        d.put(p + ".Vertical", new SplitPaneDividerVerticalState());
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Focused].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_FOCUSED));
        d.put(p + "[Enabled+Vertical].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_ENABLED_VERTICAL));
        d.put(p + "[Focused+Vertical].foregroundPainter", new LazyPainter(c, SplitPaneDividerPainter.Which.FOREGROUND_FOCUSED_VERTICAL));
    }

    /**
     * Initialize the tabbed pane settings.
     *
     * @param d the UI defaults map.
     */
    private void defineTabbedPanes(UIDefaults d) {
        
        // copied from nimbus
        
        d.put("TabbedPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put("TabbedPane.tabAreaStatesMatchSelectedTab", Boolean.TRUE);
        d.put("TabbedPane.nudgeSelectedLabel", Boolean.FALSE);
        d.put("TabbedPane.tabRunOverlay", new Integer(2));
        d.put("TabbedPane.tabOverlap", new Integer(-1));
        d.put("TabbedPane.extendTabsToBase", Boolean.TRUE);
        d.put("TabbedPane.useBasicArrows", Boolean.TRUE);
        addColor(d, "TabbedPane.shadow", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "TabbedPane.darkShadow", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "TabbedPane.highlight", "seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0);
        d.put("TabbedPane:TabbedPaneTab.contentMargins", new InsetsUIResource(2, 8, 3, 8));
        addColor(d, "TabbedPane:TabbedPaneTab[Disabled].textForeground", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", 255, 255, 255, 255);
        addColor(d, "TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", 255, 255, 255, 255);
        d.put("TabbedPane:TabbedPaneTabArea.contentMargins", new InsetsUIResource(3, 10, 4, 10));
        d.put("TabbedPane:TabbedPaneContent.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        
        // Below starts seaglass
        d.put("tabbedPaneTabAreaBackLineEnabled", new Color(0x647595));
        d.put("tabbedPaneTabAreaLightShadow", new Color(0x55eeeeee, true));
        d.put("tabbedPaneTabAreaDarkShadow", new Color(0x55aaaaaa, true));

        d.put("seaGlassTabbedPaneTabCloseGraphicInnerShadowBase", new Color(0x2a509b));
        d.put("seaGlassTabbedPaneTabCloseGraphicBase", Color.WHITE);
        d.put("seaGlassTabbedPaneTabCloseGraphicDropShadowBase", Color.BLACK);
        d.put("seaGlassTabbedPaneTabCloseBorderBase", new Color(0x20448e));

        String p = "TabbedPane";

        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".tabRunOverlay", new Integer(0));
        d.put(p + ".useBasicArrows", Boolean.FALSE);
        d.put(p + ".closeButtonMargins", new Rectangle(2, 2, 2, 2));
        d.put(p + ".closeButtonSize", new Integer(11));

        String c = PAINTER_PREFIX + "TabbedPaneTabPainter";

        p = "TabbedPane:TabbedPaneTab";
        d.put(p + ".States", "Enabled,Pressed,Disabled,MouseOver,Focused,Selected,Default");
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+Pressed].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Disabled+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_DISABLED_SELECTED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Selected+MouseOver].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Pressed+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_PRESSED_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put(p + "[Focused+Pressed+Selected].backgroundPainter", new LazyPainter(c, TabbedPaneTabPainter.Which.BACKGROUND_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled].textForeground", d.get("seaGlassDisabledText"));
        d.put(p + "[Pressed+Selected].textForeground", Color.BLACK);
        d.put(p + "[Focused+Pressed+Selected].textForeground", Color.BLACK);

        // Initialize tabbed pane "close" button.
        // Note that we co-opt MouseOver to mean mousing over the tab,
        // whereas Focused means mousing over the actual close button.
        p = "TabbedPane:TabbedPaneTab:TabbedPaneTabClaseButton";
        c = PAINTER_PREFIX + "TabbedPaneTabCloseButtonPainter";
        d.put(p + ".States", "Enabled,Pressed,Disabled,MouseOver,Focused");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, TabbedPaneTabCloseButtonPainter.Which.DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, TabbedPaneTabCloseButtonPainter.Which.ENABLED));
        d.put(p + "[MouseOver].foregroundPainter", new LazyPainter(c, TabbedPaneTabCloseButtonPainter.Which.MOUSEOVER));
        d.put(p + "[Focused].foregroundPainter", new LazyPainter(c, TabbedPaneTabCloseButtonPainter.Which.FOCUSED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, TabbedPaneTabCloseButtonPainter.Which.PRESSED));

        p = "TabbedPane:TabbedPaneTabArea";
        c = PAINTER_PREFIX + "TabbedPaneTabAreaPainter";
        d.put(p + ".contentMargins", new InsetsUIResource(3, 10, 3, 10));
        d.put(p + ".States", "Enabled,Disabled,Top,Left,Bottom,Right");
        d.put(p + ".Top", new TabbedPaneTopTabState());
        d.put(p + ".Left", new TabbedPaneLeftTabState());
        d.put(p + ".Bottom", new TabbedPaneBottomTabState());
        d.put(p + ".Right", new TabbedPaneRightTabState());
        d.put(p + "[Enabled+Top].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_TOP));
        d.put(p + "[Disabled+Top].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_DISABLED_TOP));
        d.put(p + "[Enabled+Left].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_LEFT));
        d.put(p + "[Disabled+Left].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_DISABLED_LEFT));
        d.put(p + "[Enabled+Bottom].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_BOTTOM));
        d.put(p + "[Disabled+Bottom].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_DISABLED_BOTTOM));
        d.put(p + "[Enabled+Right].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_ENABLED_RIGHT));
        d.put(p + "[Disabled+Right].backgroundPainter", new LazyPainter(c, TabbedPaneTabAreaPainter.Which.BACKGROUND_DISABLED_RIGHT));

        // Buttons
        c = PAINTER_PREFIX + "ArrowButtonPainter";
        p = "TabbedPane:TabbedPaneTabArea:\"TabbedPaneTabArea.button\"";
        d.put(p + ".States", "Enabled,Pressed,MouseOver,Disabled");
        d.put(p + "[Disabled].foreground", new ColorUIResource(0x9ba8cf));
        d.put(p + "[Enabled].foreground", new ColorUIResource(Color.BLACK));
        d.put(p + "[Pressed].foreground", new ColorUIResource(0x134D8C));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[MouseOver].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_ENABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(c, ArrowButtonPainter.Which.FOREGROUND_PRESSED));

        p = "TabbedPane:TabbedPaneContent";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
    }

    /**
     * Initialize the table UI settings.
     *
     * @param d the UI defaults map.
     */
    private void defineTables(UIDefaults d) {
        d.put("tableHeaderBorderEnabled", new Color(0xcad3e0));
        d.put("tableHeaderSortIndicator", new Color(0xc02a5481, true));
        // Rossi: table headers now blue and glassy.
        // I know you discussed this already but I like all interactive components to have the glassy look.
        d.put("tableHeaderInteriorBaseEnabled", new Color(0x80a6d2));

        String p = "TableHeader";
        String c = PAINTER_PREFIX + "TableHeaderPainter";

//        d.put(p + ".font", new DerivedFont("defaultFont", 0.846f, null, null));
        d.put(p + "[Enabled].ascendingSortIconPainter", new LazyPainter(c, TableHeaderPainter.Which.ASCENDINGSORTICON_ENABLED));
        d.put(p + "[Enabled].descendingSortIconPainter", new LazyPainter(c, TableHeaderPainter.Which.DESCENDINGSORTICON_ENABLED));

        p = "Table";
        d.put(p + ".background", new ColorUIResource(Color.WHITE));
        d.put(p + ".alternateRowColor", new ColorUIResource(0xebf5fc));
        d.put(p + ".showGrid", Boolean.FALSE);
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".intercellSpacing", new DimensionUIResource(0, 0));
        d.put(p + ".rendererUseTableColors", Boolean.TRUE);
        d.put(p + ".rendererUseUIBorder", Boolean.TRUE);
        d.put(p + ".cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // TODO Why doesn't ColorUIResource work on these next two?
        d.put(p + "[Enabled+Selected].textForeground", Color.WHITE);
        d.put(p + "[Enabled+Selected].textBackground", new Color(0x6181a5));
        d.put(p + "[Disabled+Selected].textBackground", new Color(0x6181a5));
        d.put(p + ".ascendingSortIcon", new SeaGlassIcon("TableHeader", "ascendingSortIconPainter", 8, 7));
        d.put(p + ".descendingSortIcon", new SeaGlassIcon("TableHeader", "descendingSortIconPainter", 8, 7));
        d.put(p + ".scrollPaneCornerComponent", TableScrollPaneCorner.class);
        
        c = PAINTER_PREFIX + "TableHeaderRendererPainter";
        p = "TableHeader:\"TableHeader.renderer\"";
        d.put(p + ".contentMargins", new InsetsUIResource(2, 4, 2, 4));
        d.put(p + ".States", "Enabled,Pressed,Disabled,Focused,Sorted");
        d.put(p + ".Sorted", new TableHeaderRendererSortedState());
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+Focused].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Enabled+Sorted].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_SORTED));
        d.put(p + "[Enabled+Focused+Sorted].backgroundPainter",
              new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_ENABLED_FOCUSED_SORTED));
        d.put(p + "[Disabled+Sorted].backgroundPainter", new LazyPainter(c, TableHeaderRendererPainter.Which.BACKGROUND_DISABLED_SORTED));
    }

    /**
     * Initialize the settings for text controls.
     *
     * @param d the UI defaults map.
     */
    private void defineTextControls(UIDefaults d) {
        String c  = PAINTER_PREFIX + "TextComponentPainter";
        String cs = PAINTER_PREFIX + "SearchFieldPainter";
        String ci = PAINTER_PREFIX + "SearchFieldIconPainter";

        // Initialize search field "find" button
        String p  = "TextField:SearchFieldFindButton";

        d.put(p + ".States", "Enabled,Pressed,Disabled,HasPopup");
        d.put(p + ".HasPopup", new SearchFieldHasPopupState());
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED));
        d.put(p + "[Enabled+HasPopup].foregroundPainter",
              new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED_POPUP));
        d.put(p + "[Pressed+HasPopup].foregroundPainter",
              new LazyPainter(ci, SearchFieldIconPainter.Which.FIND_ICON_ENABLED_POPUP));

        // Initialize search field "cancel" button
        p = "TextField:SearchFieldCancelButton";
        d.put(p + ".States", "Enabled,Pressed,Disabled");
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_DISABLED));
        d.put(p + "[Enabled].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_ENABLED));
        d.put(p + "[Pressed].foregroundPainter", new LazyPainter(ci, SearchFieldIconPainter.Which.CANCEL_ICON_PRESSED));

        p = "TextField";
        d.put(p + ".States", "Enabled,Selected,Disabled,Focused,SearchField");
        d.put(p + ".SearchField", new TextFieldIsSearchState());
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + ".placeholderTextColor", d.get("seaGlassSearchPlaceholderText"));
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
        
        d.put(p + "[SearchField].contentMargins", new InsetsUIResource(4, 26, 4, 23));

        // Initialize TextField
        d.put(p + ".background", getDerivedColor("seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0, true));
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
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + ".placeholderTextColor", d.get("seaGlassSearchPlaceholderText"));
        d.put(p + ".background", getDerivedColor("seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled].borderPainter", new LazyPainter(c, TextComponentPainter.Which.BORDER_ENABLED));

        // Paint with SearchFieldPainter.
        d.put(p + "[Disabled+SearchField].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
        d.put(p + "[Disabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Selected+SearchField].backgroundPainter", new LazyPainter(cs, SearchFieldPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_DISABLED));
        d.put(p + "[Focused+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_FOCUSED));
        d.put(p + "[Enabled+SearchField].borderPainter", new LazyPainter(cs, SearchFieldPainter.Which.BORDER_ENABLED));

        // Initialize PasswordField
        p = "PasswordField";
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".searchIconWidth", new Integer(15));
        d.put(p + ".cancelIconWidth", new Integer(15));
        d.put(p + ".popupIconWidth", new Integer(7));
        d.put(p + ".echoChar", Character.valueOf((char) 0x2022));
        d.put(p + ".searchLeftInnerMargin", new Integer(3));
        d.put(p + ".searchRightInnerMargin", new Integer(3));
        d.put(p + ".placeholderTextColor", d.get("seaGlassSearchPlaceholderText"));
        d.put(p + ".background", getDerivedColor("seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
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
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,NotInScrollPane");
        d.put(p + ".NotInScrollPane", new TextAreaNotInScrollPaneState());
        d.put(p + ".background", getDerivedColor("seaGlassLightBackground", 0.0f, 0.0f, 0.0f, 0, true));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_ENABLED));
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));

        // Rossi: TextArea painters to support new Client Property to draw "lines"
        d.put(p + "[Disabled].backgroundPainter",
              new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter",
              new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
        
        // TextArea not in scroll pane is visually the same as TextField.
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Disabled+NotInScrollPane].backgroundPainter",
              new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled+NotInScrollPane].backgroundPainter",
              new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_ENABLED));
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
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));

        // Initialize EditorPane
        p = "EditorPane";
        d.put(p + ".contentMargins", new InsetsUIResource(4, 6, 4, 6));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SOLID_ENABLED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, TextComponentPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Selected].textForeground", Color.WHITE);
        d.put(p + "[Selected].textBackground", d.get("seaGlassSelection"));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));
        
        p = "Label";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0, true));
    }

    /**
     * Initialize the tool bar settings.
     *
     * @param d the UI defaults map.
     */
    private void defineToolBars(UIDefaults d) {
        // Copied from nimbus
        
        d.put("ToolBar.contentMargins", new InsetsUIResource(2, 2, 2, 2));
        d.put("ToolBar.opaque", Boolean.TRUE);
        d.put("ToolBar:Button.contentMargins", new InsetsUIResource(4, 4, 4, 4));
        d.put("ToolBar:ToggleButton.contentMargins", new InsetsUIResource(4, 4, 4, 4));
        addColor(d, "ToolBar:ToggleButton[Disabled+Selected].textForeground", "seaGlassDisabledText", 0.0f, 0.0f, 0.0f, 0);

        //Initialize ToolBarSeparator
        d.put("ToolBarSeparator.contentMargins", new InsetsUIResource(2, 0, 3, 0));
        addColor(d, "ToolBarSeparator.textForeground", "seaGlassBorder", 0.0f, 0.0f, 0.0f, 0);

        
        // Below starts seaglass
        
        d.put("toolbarHandleMac", new Color(0xc8191919, true));
        // Rossi: Adjusted color for better look: Not tested with unified, ...
        d.put("toolbarToggleButtonBase", new Color(0x5b7ea4, true));
        

        if ((!PlatformUtils.isMac())) {
            d.put("seaGlassToolBarActiveTopT", new Color(0x466c97));
            d.put("seaGlassToolBarActiveBottomB", new Color(0x466c97));

            d.put("seaGlassToolBarInactiveTopT", new Color(0xe9e9e9));
            d.put("seaGlassToolBarInactiveBottomB", new Color(0xcacaca));
        } else if (PlatformUtils.isLion()) {
            d.put("seaGlassToolBarActiveTopT", new Color(0xdedede));
            d.put("seaGlassToolBarActiveBottomB", new Color(0xb0b0b0));

            d.put("seaGlassToolBarInactiveTopT", new Color(0xf3f3f3));
            d.put("seaGlassToolBarInactiveBottomB", new Color(0xdedede));
        } else if (PlatformUtils.isSnowLeopard()) {
            d.put("seaGlassToolBarActiveTopT", new Color(0xc9c9c9));
            d.put("seaGlassToolBarActiveBottomB", new Color(0xa7a7a7));
            
            d.put("seaGlassToolBarInactiveTopT", new Color(0xe9e9e9));
            d.put("seaGlassToolBarInactiveBottomB", new Color(0xcacaca));
        } else {
            d.put("seaGlassToolBarActiveTopT", new Color(0xbcbcbc));
            d.put("seaGlassToolBarActiveBottomB", new Color(0xa7a7a7));

            d.put("seaGlassToolBarInactiveTopT", new Color(0xe4e4e4));
            d.put("seaGlassToolBarInactiveBottomB", new Color(0xd8d8d8));
        }
        
        String c = PAINTER_PREFIX + "ToolBarPainter";
        String p = "ToolBar";

        d.put(p + ".contentMargins", new InsetsUIResource(2, 2, 2, 2));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".States", "WindowIsActive");
        d.put(p + ".WindowIsActive", new ToolBarWindowIsActiveState());

        d.put(p + ".backgroundPainter", new LazyPainter(c, ToolBarPainter.Which.BORDER_ENABLED));

        c = PAINTER_PREFIX + "ToolBarHandlePainter";
        d.put(p + ".handleIconPainter", new LazyPainter(c, ToolBarHandlePainter.Which.HANDLEICON_ENABLED));
        d.put(p + ".handleIcon", new SeaGlassIcon(p, "handleIconPainter", 11, 38));

        c = PAINTER_PREFIX + "ButtonPainter";
        p = "ToolBar:Button";
        d.put(p + ".States", "Enabled,Disabled,Focused,Pressed");
        d.put(p + "[Disabled].textForeground", d.get("seaGlassToolBarDisabledText"));
        d.put(p + "[Disabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_DISABLED));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter", new LazyPainter(c, ButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));

        c = PAINTER_PREFIX + "ToolBarToggleButtonPainter";
        p = "ToolBar:ToggleButton";
        d.put(p + ".States", "Enabled,Disabled,Focused,Pressed,Selected");
        d.put(p + "[Disabled].textForeground", d.get("seaGlassToolBarDisabledText"));
        d.put(p + "[Focused].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_FOCUSED));
        d.put(p + "[Pressed].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED));
        d.put(p + "[Focused+Pressed].backgroundPainter",
              new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_FOCUSED));
        d.put(p + "[Selected].backgroundPainter", new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_SELECTED));
        d.put(p + "[Focused+Selected].backgroundPainter",
              new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_SELECTED_FOCUSED));
        d.put(p + "[Pressed+Selected].backgroundPainter",
              new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_SELECTED));
        d.put(p + "[Focused+Pressed+Selected].backgroundPainter",
              new LazyPainter(c,
                              ToolBarToggleButtonPainter.Which.BACKGROUND_PRESSED_SELECTED_FOCUSED));
        d.put(p + "[Disabled+Selected].backgroundPainter",
              new LazyPainter(c, ToolBarToggleButtonPainter.Which.BACKGROUND_DISABLED_SELECTED));

        uiDefaults.put("ToolBarSeparator[Enabled].backgroundPainter", null);
    }

    /**
     * Initialize the tree settings.
     *
     * @param d the UI defaults map.
     */
    private void defineTrees(UIDefaults d) {
        //Initialize Tree
        String p = "Tree";
        String c = PAINTER_PREFIX + "TreePainter";

        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".textForeground", d.get("text"));
        d.put(p + ".textBackground", d.get("seaGlassLightBackground"));
        d.put(p + ".background", d.get("seaGlassLightBackground"));
        d.put(p + ".rendererFillBackground", Boolean.FALSE);
        d.put(p + ".leftChildIndent", new Integer(12));
        d.put(p + ".rightChildIndent", new Integer(8));
        d.put(p + ".drawHorizontalLines", Boolean.FALSE);
        d.put(p + ".drawVerticalLines", Boolean.FALSE);
        d.put(p + ".showsRootHandles", Boolean.FALSE);
        d.put(p + ".rendererUseTreeColors", Boolean.TRUE);
        d.put(p + ".repaintWholeRow", Boolean.TRUE);
        d.put(p + ".rowHeight", new Integer(0));
        d.put(p + ".rendererMargins", new InsetsUIResource(2, 5, 1, 5));
        d.put(p + ".selectionForeground", d.get("seaGlassSelectedText"));
        d.put(p + ".selectionBackground", d.get("seaGlassSelectionBackground"));
        d.put(p + ".dropLineColor", d.get("seaGlassFocus"));

        d.put(p + "[Enabled].collapsedIconPainter", new LazyPainter(c, TreePainter.Which.COLLAPSEDICON_ENABLED));
        d.put(p + "[Enabled+Selected].collapsedIconPainter",
              new LazyPainter(c, TreePainter.Which.COLLAPSEDICON_ENABLED_SELECTED));
        d.put(p + "[Enabled].expandedIconPainter", new LazyPainter(c, TreePainter.Which.EXPANDEDICON_ENABLED));
        d.put(p + "[Enabled+Selected].expandedIconPainter", new LazyPainter(c, TreePainter.Which.EXPANDEDICON_ENABLED_SELECTED));
        d.put(p + ".collapsedIcon", new SeaGlassIcon(p, "collapsedIconPainter", 7, 7));
        d.put(p + ".expandedIcon", new SeaGlassIcon(p, "expandedIconPainter", 7, 7));
        d.put(p + ".leafIcon", null);
        d.put(p + ".closedIcon", null);
        d.put(p + ".openIcon", null);

        p = "Tree:TreeCell";
        c = PAINTER_PREFIX + "TreeCellPainter";
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Enabled].background", d.get("seaGlassLightBackground"));
        d.put(p + "[Enabled+Focused].background", d.get("seaGlassLightBackground"));
        d.put(p + "[Enabled+Focused].backgroundPainter", new LazyPainter(c, TreeCellPainter.Which.BACKGROUND_ENABLED_FOCUSED));
        d.put(p + "[Enabled+Selected].textForeground", Color.WHITE);
        d.put(p + "[Enabled+Selected].backgroundPainter", new LazyPainter(c, TreeCellPainter.Which.BACKGROUND_ENABLED_SELECTED));
        d.put(p + "[Focused+Selected].textForeground", Color.WHITE);
        d.put(p + "[Focused+Selected].backgroundPainter", new LazyPainter(c, TreeCellPainter.Which.BACKGROUND_SELECTED_FOCUSED));

        p = "Tree:\"Tree.cellRenderer\"";
        d.put(p + ".font", new FontUIResource("SansSerif", Font.PLAIN, 13));
        d.put(p + ".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        d.put(p + "[Disabled].textForeground", getDerivedColor("seaGlassDisabledText", 0, 0, 0, 0, true));

        p = "\"Tree.cellEditor\"";
        c = PAINTER_PREFIX + "TreeCellEditorPainter";
        d.put(p + ".font", new FontUIResource("SansSerif", Font.PLAIN, 13));
        d.put(p + ".contentMargins", new InsetsUIResource(2, 5, 2, 5));
        d.put(p + ".opaque", Boolean.TRUE);
        d.put(p + ".background", d.get("control"));
        d.put(p + "[Disabled].textForeground", d.get("seaGlassDisabledText"));
        d.put(p + "[Selected].textForeground", d.get("seaGlassSelectedText"));
        d.put(p + "[Selected].textBackground", new ColorUIResource((Color) d.get("seaGlassSelection")));
        d.put(p + "[Enabled].backgroundPainter", new LazyPainter(c, TreeCellEditorPainter.Which.BACKGROUND_ENABLED));
        d.put(p + "[Enabled+Focused].backgroundPainter",
              new LazyPainter(c, TreeCellEditorPainter.Which.BACKGROUND_ENABLED_FOCUSED));
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
     * {@code RootPaneUI} instances support providing {@code Window} decorations
     * in a {@code JRootPane}.
     *
     * <p>Sea Glass returns {@code false} on a Macintosh, {@code true}
     * otherwise.</p>
     *
     * @return {@code true} if the {@code RootPaneUI} instances created by this
     *         look and feel support client side decorations. This returns
     *         {@code true} on a non-Macintosh.
     *
     * @see    JDialog#setDefaultLookAndFeelDecorated
     * @see    JFrame#setDefaultLookAndFeelDecorated
     * @see    JRootPane#setWindowDecorationStyle
     */
    public boolean getSupportsWindowDecorations() {
        return !PlatformUtils.isMac();
    }

    /**
     * A convenience method to return where the foreground should be painted for
     * the Component identified by the passed in AbstractSynthContext.
     *
     * @param  state  the SynthContext representing the current state.
     * @param  insets an Insets object to be filled with the painting insets.
     *
     * @return the insets object passed in and filled with the painting insets.
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
     *
     * @param  c the component.
     *
     * @return {@code true} if the component is laid out left-to-right,
     *         {@code false} if right-to-left.
     */
    public static boolean isLeftToRight(Component c) {
        return c.getComponentOrientation().isLeftToRight();
    }

    /**
     * Returns the ui that is of type <code>klass</code>, or null if one can not
     * be found.
     *
     * @param  ui    the UI delegate to be tested.
     * @param  klass the class to test against.
     *
     * @return {@code ui} if {@code klass} is an instance of {@code ui},
     *         {@code null} otherwise.
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
     * <p/>This code is used to determine where the border line should be drawn
     * by the custom toolbar states, and also used by SeaGlassIcon to determine
     * whether the handle icon needs to be shifted to look correct.</p>
     *
     * <p>Toollbars are unfortunately odd in the way these things are handled,
     * and so this code exists to unify the logic related to toolbars so it can
     * be shared among the static files such as SeaGlassIcon and generated files
     * such as the ToolBar state classes.</p>
     *
     * @param  toolbar a toolbar in the Swing hierarchy.
     *
     * @return the {@code BorderLayout} orientation of the toolbar, or
     *         {@code BorderLayout.NORTH} if none can be determined.
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
                    BorderLayout b   = (BorderLayout) m;
                    Object       con = b.getConstraints(toolbar);

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
     * Registers the given region and prefix. The prefix, if it contains quoted
     * sections, refers to certain named components. If there are not quoted
     * sections, then the prefix refers to a generic component type.
     *
     * <p>If the given region/prefix combo has already been registered, then it
     * will not be registered twice. The second registration attempt will fail
     * silently.</p>
     *
     * @param region The Synth Region that is being registered. Such as Button,
     *               or ScrollBarThumb.
     * @param prefix The UIDefault prefix. For example, could be ComboBox, or if
     *               a named components, "MyComboBox", or even something like
     *               ToolBar:"MyComboBox":"ComboBox.arrowButton"
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
     * Locate the style associated with the given region and component. This is
     * called from SeaGlassLookAndFeel in the SynthStyleFactory implementation.
     *
     * <p>Lookup occurs as follows:<br/>
     * Check the map of styles <code>styleMap</code>. If the map contains no
     * styles at all, then simply return the defaultStyle. If the map contains
     * styles, then iterate over all of the styles for the Region <code>r</code>
     * looking for the best match, based on prefix. If a match was made, then
     * return that SynthStyle. Otherwise, return the defaultStyle.</p>
     *
     * @param  c The component associated with this region. For example, if the
     *           Region is Region.Button then the component will be a JButton.
     *           If the Region is a subregion, such as ScrollBarThumb, then the
     *           associated component will be the component that subregion
     *           belongs to, such as JScrollBar. The JComponent may be named. It
     *           may not be null.
     * @param  r The region we are looking for a style for. May not be null.
     *
     * @return the style associated with the given region and component.
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
            return getDefaultStyle();
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
        return foundStyle == null ? getDefaultStyle() : foundStyle.getStyle(c);
    }

    /**
     * A convience method that will reset the Style of StyleContext if
     * necessary.
     *
     * @param  context the SynthContext corresponding to the current state.
     * @param  ui      the UI delegate.
     *
     * @return the new, updated style.
     */
    public static SynthStyle updateStyle(SeaGlassContext context, SeaglassUI ui) {
        SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();

        if (newStyle != oldStyle) {

            if (oldStyle != null) {
                oldStyle.uninstallDefaults(context);
            }

            context.setStyle(newStyle);
            if (newStyle instanceof SeaGlassStyle) {
                ((SeaGlassStyle) newStyle).installDefaults(context, ui);
            }
        }

        return newStyle;
    }

    /**
     * Returns the component state for the specified component. This should only
     * be used for Components that don't have any special state beyond that of
     * ENABLED, DISABLED or FOCUSED. For example, buttons shouldn't call into
     * this method.
     *
     * @param  c the component.
     *
     * @return the simple state of the component.
     */
    public static int getComponentState(Component c) {
        if (c.isEnabled()) {

            if (c.isFocusOwner()) {
                return SeaglassUI.ENABLED | SeaglassUI.FOCUSED;
            }

            return SeaglassUI.ENABLED;
        }

        return SeaglassUI.DISABLED;
    }

    /**
     * A convenience method that handles painting of the background. All SynthUI
     * implementations should override update and invoke this method.
     *
     * @param state the SynthContext describing the current component and state.
     * @param g     the Graphics context to use to paint the component.
     */
    public static void update(SynthContext state, Graphics g) {
        paintRegion(state, g, null);
    }

    /**
     * A convenience method that handles painting of the background for
     * subregions. All SynthUI's that have subregions should invoke this method,
     * than paint the foreground.
     *
     * @param state  the SynthContext describing the component, region, and
     *               state.
     * @param g      the Graphics context used to paint the subregion.
     * @param bounds the bounds to paint in.
     */
    public static void updateSubregion(SynthContext state, Graphics g, Rectangle bounds) {
        paintRegion(state, g, bounds);
    }

    /**
     * Paint a region.
     *
     * @param state  the SynthContext describing the current component, region,
     *               and state.
     * @param g      the Graphics context used to paint the subregion.
     * @param bounds the bounds to paint in.
     */
    private static void paintRegion(SynthContext state, Graphics g, Rectangle bounds) {
        JComponent c      = state.getComponent();
        SynthStyle style  = state.getStyle();
        int        x;
        int        y;
        int        width;
        int        height;

        if (bounds == null) {
            x      = 0;
            y      = 0;
            width  = c.getWidth();
            height = c.getHeight();
        } else {
            x      = bounds.x;
            y      = bounds.y;
            width  = bounds.width;
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
     * PropertyChangeEvent. This forwards to <code>
     * shouldUpdateStyleOnAncestorChanged</code> as necessary.
     *
     * @param  event the property change event.
     *
     * @return {@code true} if the style should be updated as a result of this
     *         property change, {@code false} otherwise.
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
     * state as set by this method is set in the field {@code selectedUIState}.
     * This provides a way for labels to have a state other than selected.
     *
     * @param uix      a UI delegate.
     * @param selected is the component selected?
     * @param focused  is the component focused?
     * @param enabled  is the component enabled?
     * @param rollover is the component's rollover state enabled?
     */
    public static void setSelectedUI(ComponentUI uix, boolean selected, boolean focused, boolean enabled, boolean rollover) {
        selectedUI      = uix;
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

    /**
     * This class is private because it relies on the constructor of the
     * auto-generated AbstractRegionPainter subclasses. Hence, it is not
     * generally useful, and is private.
     *
     * <p>LazyPainter is a LazyValue class. It will create the
     * AbstractRegionPainter lazily, when asked. It uses reflection to load the
     * proper class and invoke its constructor.</p>
     */
    private static final class LazyPainter implements UIDefaults.LazyValue {
        private Enum   which;
        private String className;

        /**
         * Creates a new LazyPainter object.
         *
         * @param className the painter class to be instantiated in a lazy
         *                  fashion.
         * @param which     the state for the painter class to paint. This will
         *                  be an enum defined in the painter class andspecific
         *                  to it.
         */
        LazyPainter(String className, Enum which) {
            if (className == null) {
                throw new IllegalArgumentException("The className must be specified");
            }

            this.className = className;
            this.which     = which;
        }

        /**
         * @see javax.swing.UIDefaults$LazyValue#createValue(javax.swing.UIDefaults)
         */
        @SuppressWarnings("unchecked")
        public Object createValue(UIDefaults table) {
            Constructor constructor = null;
            Object cl = null;
            try {
                Class  c;

                // See if we should use a separate ClassLoader
                // GM: use ClassLoader from this class, if no separate ClassLoader
                cl = table.get("ClassLoader");
                if (cl == null) {
                    cl = getClass().getClassLoader();
                }
                
// GM: doesn't work with WebStart and OSGi                
//                if (table == null || !(cl instanceof ClassLoader)) {
//                    cl = Thread.currentThread().getContextClassLoader();
//
//                    if (cl == null) {
//
//                        // Fallback to the system class loader.
//                        cl = ClassLoader.getSystemClassLoader();
//                    }
//                }

                c = Class.forName(className, true, (ClassLoader) cl);

                // Find inner class for state.
                Class stateClass = Class.forName(className + "$Which", false, (ClassLoader) cl);

                if (stateClass == null) {
                    throw new NullPointerException("Failed to find the constructor for the class: " + className + ".Which");
                }

                constructor = c.getConstructor(stateClass);

                if (constructor == null) {
                    throw new NullPointerException("Failed to find the constructor for the class: " + className);
                }

                return constructor.newInstance(which);
            } catch (Exception e) {
                System.err.println( "createValue: " + which.getClass() + ", " + (constructor != null ? constructor : "") );
                System.err.println( "class loaders: " + which.getClass().getClassLoader() + ", " + cl + ", UIDefaults = " + table );
                e.printStackTrace();

                return null;
            }
        }
    }

    /**
     * A class which creates the SeaGlassStyle associated with it lazily, but
     * also manages a lot more information about the style. It is less of a
     * LazyValue type of class, and more of an Entry or Item type of class, as
     * it represents an entry in the list of LazyStyles in the map styleMap.
     *
     * <p>The primary responsibilities of this class include:</p>
     *
     * <ul>
     *   <li>Determining whether a given component/region pair matches this
     *     style</li>
     *   <li>Splitting the prefix specified in the constructor into its
     *     constituent parts to facilitate quicker matching</li>
     *   <li>Creating and vending a SeaGlassStyle lazily.</li>
     * </ul>
     */
    private final class LazyStyle {

        /**
         * The prefix this LazyStyle was registered with. Something like Button
         * or ComboBox:"ComboBox.arrowButton"
         */
        private String prefix;

        /** Whether or not this LazyStyle represents an unnamed component */
        private boolean simple = true;

        /**
         * The various parts, or sections, of the prefix. For example, the
         * prefix: ComboBox:"ComboBox.arrowButton" will be broken into two
         * parts, ComboBox and "ComboBox.arrowButton"
         */
        private Part[] parts;

        /** Cached shared style. */
        private SeaGlassStyle style;

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
         * @param prefix The prefix associated with this style. Cannot be null.
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
         * @param  c the component for which to get the style.
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
                SeaGlassStyle                s   = ref == null ? null : ref.get();

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
         *
         * @param  c the component to test againts the current style hierarchy.
         *
         * @return {@code true} if the component matches the style,
         *         {@code false} otherwise.
         */
        boolean matches(JComponent c) {
            return matches(c, parts.length - 1);
        }

        /**
         * Internal method to determine whether a component matches a part of a
         * style. Recurses to do the work.
         *
         * @param  c         the component to test against the current style
         *                   hierarchy.
         * @param  partIndex the index of the part of the hierarchy.
         *
         * @return {@code true} if the component matches the style,
         *         {@code false} otherwise.
         */
        private boolean matches(Component c, int partIndex) {
            if (partIndex < 0)
                return true;

            if (c == null)
                return false;
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
                    Region    r      = registeredRegions.get(parts[partIndex].s);
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
         * @param  prefix the style prefix.
         *
         * @return a list of colon-separated elements.
         */
        private List<String> split(String prefix) {
            List<String> parts        = new ArrayList<String>();
            int          bracketCount = 0;
            boolean      inquotes     = false;
            int          lastIndex    = 0;

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

        /**
         * A private class representing the parts of a style name.
         */
        private final class Part {
            private String  s;

            // true if this part represents a component name
            private boolean named;
            private Class   c;

            /**
             * Creates a new Part object.
             *
             * @param s the element of the style name representing this part.
             */
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
     * A private Border class.
     */
    private static final class PainterBorder implements Border, UIResource {
        private Insets  insets;
        private SeaGlassPainter<Component> painter;
        private String  painterKey;

        /**
         * Creates a new PainterBorder object.
         *
         * @param painterKey the painter key.
         * @param insets     the insets.
         */
        PainterBorder(String painterKey, Insets insets) {
            this.insets     = insets;
            this.painterKey = painterKey;
        }

        /**
         * @see javax.swing.border.Border#paintBorder(java.awt.Component,java.awt.Graphics,
         *      int, int, int, int)
         */
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            if (painter == null) {
                painter = (SeaGlassPainter<Component>) UIManager.get(painterKey);

                if (painter == null)
                    return;
            }

            g.translate(x, y);

            if (g instanceof Graphics2D) {
                painter.paint((Graphics2D) g, c, w, h);
            } else {
                BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
                Graphics2D    gfx = img.createGraphics();

                painter.paint(gfx, c, w, h);
                gfx.dispose();
                g.drawImage(img, x, y, null);
                img = null;
            }

            g.translate(-x, -y);
        }

        /**
         * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
         */
        public Insets getBorderInsets(Component c) {
            return (Insets) insets.clone();
        }

        /**
         * @see javax.swing.border.Border#isBorderOpaque()
         */
        public boolean isBorderOpaque() {
            return false;
        }
    }
    
    
   // THIS IS ALL COPIED FROM NIMBUS L&F
//--------------------------------------------------------------------------------------------------------------------    

        /**
         * @inheritDoc
         * @return true
         */
        @Override public boolean shouldUpdateStyleOnAncestorChanged() {
            return true;
        }
        
        /**
         * Get a derived color, derived colors are shared instances and will be
         * updated when its parent UIDefault color changes.
         *
         * @param uiDefaultParentName The parent UIDefault key
         * @param hOffset The hue offset
         * @param sOffset The saturation offset
         * @param bOffset The brightness offset
         * @param aOffset The alpha offset
         * @param uiResource True if the derived color should be a UIResource,
         *        false if it should not be a UIResource
         * @return The stored derived color
         */
        public DerivedColor getDerivedColor(String parentUin,
                                            float hOffset, float sOffset,
                                            float bOffset, int aOffset,
                                            boolean uiResource){
            return getDerivedColor(null, parentUin,
                    hOffset, sOffset, bOffset, aOffset, uiResource);
        }

        private DerivedColor getDerivedColor(String uin, String parentUin,
                                            float hOffset, float sOffset,
                                            float bOffset, int aOffset,
                                            boolean uiResource) {
            DerivedColor color;
            if (uiResource) {
                color = new DerivedColor.UIResource(parentUin,
                        hOffset, sOffset, bOffset, aOffset);
            } else {
                color = new DerivedColor(parentUin, hOffset, sOffset,
                    bOffset, aOffset);
            }
            return color;
        }

        /**
         * Decodes and returns a color, which is derived from an offset between two
         * other colors.
         *
         * @param color1   The first color
         * @param color2   The second color
         * @param midPoint The offset between color 1 and color 2, a value of 0.0 is
         *                 color 1 and 1.0 is color 2;
         * @param uiResource True if the derived color should be a UIResource
         * @return The derived color
         */
        protected final Color getDerivedColor(Color color1, Color color2,
                                          float midPoint, boolean uiResource) {
            int argb = deriveARGB(color1, color2, midPoint);
            if (uiResource) {
                return new ColorUIResource(argb);
            } else {
                return new Color(argb);
            }
        }

        /**
         * Decodes and returns a color, which is derived from a offset between two
         * other colors.
         *
         * @param color1   The first color
         * @param color2   The second color
         * @param midPoint The offset between color 1 and color 2, a value of 0.0 is
         *                 color 1 and 1.0 is color 2;
         * @return The derived color, which will be a UIResource
         */
        protected final Color getDerivedColor(Color color1, Color color2,
                                          float midPoint) {
            return getDerivedColor(color1, color2, midPoint, true);
        }

        /**
         * Derives the ARGB value for a color based on an offset between two
         * other colors.
         *
         * @param color1   The first color
         * @param color2   The second color
         * @param midPoint The offset between color 1 and color 2, a value of 0.0 is
         *                 color 1 and 1.0 is color 2;
         * @return the ARGB value for a new color based on this derivation
         */
        static int deriveARGB(Color color1, Color color2, float midPoint) {
            int r = color1.getRed() +
                    (int) ((color2.getRed() - color1.getRed()) * midPoint + 0.5f);
            int g = color1.getGreen() +
                    (int) ((color2.getGreen() - color1.getGreen()) * midPoint +
                            0.5f);
            int b = color1.getBlue() +
                    (int) ((color2.getBlue() - color1.getBlue()) * midPoint + 0.5f);
            int a = color1.getAlpha() +
                    (int) ((color2.getAlpha() - color1.getAlpha()) * midPoint +
                            0.5f);
            return ((a & 0xFF) << 24) |
                    ((r & 0xFF) << 16) |
                    ((g & 0xFF) << 8) |
                    (b & 0xFF);
        }
        
        private void addColor(UIDefaults d, String uin, int r, int g, int b, int a) {
            Color color = new ColorUIResource(new Color(r, g, b, a));
            d.put(uin, color);
        }

        private void addColor(UIDefaults d, String uin, String parentUin,
                float hOffset, float sOffset, float bOffset, int aOffset) {
            addColor(d, uin, parentUin, hOffset, sOffset, bOffset, aOffset, true);
        }

        private void addColor(UIDefaults d, String uin, String parentUin,
                float hOffset, float sOffset, float bOffset,
                int aOffset, boolean uiResource) {
            Color color = getDerivedColor(uin, parentUin,
                    hOffset, sOffset, bOffset, aOffset, uiResource);
            d.put(uin, color);
        }
    
}
