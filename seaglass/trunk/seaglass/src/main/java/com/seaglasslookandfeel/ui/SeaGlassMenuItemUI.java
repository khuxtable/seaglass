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
package com.seaglasslookandfeel.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassIcon;

/**
 * Sea Glass MenuItemUI delegate.
 * 
 * Based on SynthMenuItemUI by Georges Saab, David Karlton, Arnaud Weber, and
 * Fredrik Lagerblad.
 * 
 * @see javax.swing.plaf.synth.SynthMenuItemUI
 */
public class SeaGlassMenuItemUI extends BasicMenuItemUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;
    private SynthStyle accStyle;

    private String     acceleratorDelimiter;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassMenuItemUI();
    }

    //
    // The next handful of static methods are used by both SynthMenuUI
    // and SeaGlassMenuItemUI. This is necessitated by SynthMenuUI not
    // extending SeaGlassMenuItemUI.
    //
    static Dimension getPreferredMenuItemSize(SeaGlassContext context, SeaGlassContext accContext, boolean useCheckAndArrow, JComponent c,
        Icon checkIcon, Icon arrowIcon, int defaultTextIconGap, String acceleratorDelimiter) {
        JMenuItem b = (JMenuItem) c;
        Icon icon = (Icon) b.getIcon();
        String text = b.getText();
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";

        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText += acceleratorDelimiter;
            }
            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                acceleratorText += KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText += accelerator.getKeyChar();
            }
        }

        Font font = context.getStyle().getFont(context);
        FontMetrics fm = b.getFontMetrics(font);
        FontMetrics fmAccel = b.getFontMetrics(accContext.getStyle().getFont(accContext));

        resetRects();

        layoutMenuItem(context, fm, accContext, text, fmAccel, acceleratorText, icon, checkIcon, arrowIcon, b.getVerticalAlignment(), b
            .getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect,
            acceleratorRect, checkIconRect, arrowIconRect, text == null ? 0 : defaultTextIconGap, defaultTextIconGap, useCheckAndArrow);
        // find the union of the icon and text rects
        r.setBounds(textRect);
        r = SwingUtilities.computeUnion(iconRect.x, iconRect.y, iconRect.width, iconRect.height, r);
        // To make the accelerator texts appear in a column,
        // find the widest MenuItem text and the widest accelerator text.

        // Get the parent, which stores the information.
        Container parent = b.getParent();

        if (parent instanceof JPopupMenu) {
            SeaGlassPopupMenuUI popupUI = (SeaGlassPopupMenuUI) SeaGlassLookAndFeel.getUIOfType(((JPopupMenu) parent).getUI(),
                SeaGlassPopupMenuUI.class);

            if (popupUI != null) {
                r.width = popupUI.adjustTextWidth(r.width);

                popupUI.adjustAcceleratorWidth(acceleratorRect.width);

                r.width += popupUI.getMaxAcceleratorWidth();
            }
        } else if (parent != null && !(b instanceof JMenu && ((JMenu) b).isTopLevelMenu())) {
            r.width += acceleratorRect.width;
        }

        if (useCheckAndArrow) {
            // Add in the checkIcon
            r.width += checkIconRect.width;
            r.width += defaultTextIconGap;

            // Add in the arrowIcon
            r.width += defaultTextIconGap;
            r.width += arrowIconRect.width;
        }

        r.width += 2 * defaultTextIconGap;

        Insets insets = b.getInsets();
        if (insets != null) {
            r.width += insets.left + insets.right;
            r.height += insets.top + insets.bottom;
        }

        // if the width is even, bump it up one. This is critical
        // for the focus dash line to draw properly
        if (r.width % 2 == 0) {
            r.width++;
        }

        // if the height is even, bump it up one. This is critical
        // for the text to center properly
        if (r.height % 2 == 0) {
            r.height++;
        }
        return r.getSize();
    }

    static void paint(SeaGlassContext context, SeaGlassContext accContext, Graphics g, Icon checkIcon, Icon arrowIcon,
        boolean useCheckAndArrow, String acceleratorDelimiter, int defaultTextIconGap) {
        JComponent c = context.getComponent();
        JMenuItem b = (JMenuItem) c;
        ButtonModel model = b.getModel();
        Insets i = b.getInsets();

        resetRects();

        viewRect.setBounds(0, 0, b.getWidth(), b.getHeight());

        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);

        SynthStyle style = context.getStyle();
        Font f = style.getFont(context);
        g.setFont(f);
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);
        FontMetrics accFM = SwingUtilities2.getFontMetrics(c, g, accContext.getStyle().getFont(accContext));

        // get Accelerator text
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText += acceleratorDelimiter;
            }

            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                acceleratorText += KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText += accelerator.getKeyChar();
            }
        }

        // layoutl the text and icon
        String text = layoutMenuItem(context, fm, accContext, b.getText(), accFM, acceleratorText, b.getIcon(), checkIcon, arrowIcon, b
            .getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect,
            iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, b.getText() == null ? 0 : defaultTextIconGap,
            defaultTextIconGap, useCheckAndArrow);

        // Paint the Check
        if (checkIcon != null && useCheckAndArrow) {
            SeaGlassIcon.paintIcon(checkIcon, context, g, checkIconRect.x, checkIconRect.y, checkIconRect.width, checkIconRect.height);
        }

        // Paint the Icon
        if (b.getIcon() != null) {
            Icon icon;
            if (!model.isEnabled()) {
                icon = (Icon) b.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = (Icon) b.getPressedIcon();
                if (icon == null) {
                    // Use default icon
                    icon = (Icon) b.getIcon();
                }
            } else {
                icon = (Icon) b.getIcon();
            }

            if (icon != null) {
                SeaGlassIcon.paintIcon(icon, context, g, iconRect.x, iconRect.y, iconRect.width, iconRect.height);
            }
        }

        // Draw the Text
        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                g.setColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
                g.setFont(style.getFont(context));
                style.getGraphicsUtils(context).paintText(context, g, text, textRect.x, textRect.y, b.getDisplayedMnemonicIndex());
            }
        }

        // Draw the Accelerator Text
        if (acceleratorText != null && !acceleratorText.equals("")) {
            // Get the maxAccWidth from the parent to calculate the offset.
            int accOffset = 0;
            Container parent = b.getParent();
            if (parent != null && parent instanceof JPopupMenu) {
                SeaGlassPopupMenuUI popupUI = (SeaGlassPopupMenuUI) ((JPopupMenu) parent).getUI();
                if (popupUI != null) {
                    // Calculate the offset, with which the accelerator texts
                    // will be drawn with.
                    int max = popupUI.getMaxAcceleratorWidth();
                    if (max > 0) {
                        accOffset = max - acceleratorRect.width;
                    }
                }
            }

            SynthStyle accStyle = accContext.getStyle();

            g.setColor(accStyle.getColor(accContext, ColorType.TEXT_FOREGROUND));
            g.setFont(accStyle.getFont(accContext));
            accStyle.getGraphicsUtils(accContext).paintText(accContext, g, acceleratorText, acceleratorRect.x - accOffset,
                acceleratorRect.y, -1);
        }

        // Paint the Arrow
        if (arrowIcon != null && useCheckAndArrow) {
            SeaGlassIcon.paintIcon(arrowIcon, context, g, arrowIconRect.x, arrowIconRect.y, arrowIconRect.width, arrowIconRect.height);
        }
    }

    /**
     * Compute and return the location of the icons origin, the location of
     * origin of the text baseline, and a possibly clipped version of the
     * compound labels string. Locations are computed relative to the viewRect
     * rectangle.
     */

    private static String layoutMenuItem(SeaGlassContext context, FontMetrics fm, SeaGlassContext accContext, String text,
        FontMetrics fmAccel, String acceleratorText, Icon icon, Icon checkIcon, Icon arrowIcon, int verticalAlignment,
        int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect,
        Rectangle textRect, Rectangle acceleratorRect, Rectangle checkIconRect, Rectangle arrowIconRect, int textIconGap, int menuItemGap,
        boolean useCheckAndArrow) {
        // If parent is JPopupMenu, get and store it's UI
        SeaGlassPopupMenuUI popupUI = null;
        JComponent b = context.getComponent();
        Container parent = b.getParent();
        if (parent instanceof JPopupMenu) {
            popupUI = (SeaGlassPopupMenuUI) SeaGlassLookAndFeel.getUIOfType(((JPopupMenu) parent).getUI(), SeaGlassPopupMenuUI.class);
        }

        context.getStyle().getGraphicsUtils(context).layoutText(context, fm, text, icon, horizontalAlignment, verticalAlignment,
            horizontalTextPosition, verticalTextPosition, viewRect, iconRect, textRect, textIconGap);

        /*
         * Initialize the acceelratorText bounds rectangle textRect. If a null
         * or and empty String was specified we substitute "" here and use
         * 0,0,0,0 for acceleratorTextRect.
         */
        if ((acceleratorText == null) || acceleratorText.equals("")) {
            acceleratorRect.width = acceleratorRect.height = 0;
            acceleratorText = "";
        } else {
            SynthStyle style = accContext.getStyle();
            acceleratorRect.width = style.getGraphicsUtils(accContext).computeStringWidth(accContext, fmAccel.getFont(), fmAccel,
                acceleratorText);
            acceleratorRect.height = fmAccel.getHeight();
        }

        /*
         * Initialize the checkIcon bounds rectangle's width & height.
         */

        if (useCheckAndArrow) {
            if (checkIcon != null) {
                checkIconRect.width = SeaGlassIcon.getIconWidth(checkIcon, context);
                checkIconRect.height = SeaGlassIcon.getIconHeight(checkIcon, context);
            } else {
                checkIconRect.width = checkIconRect.height = 0;
            }

            /*
             * Initialize the arrowIcon bounds rectangle width & height.
             */

            if (arrowIcon != null) {
                arrowIconRect.width = SeaGlassIcon.getIconWidth(arrowIcon, context);
                arrowIconRect.height = SeaGlassIcon.getIconHeight(arrowIcon, context);
            } else {
                arrowIconRect.width = arrowIconRect.height = 0;
            }
        }

        Rectangle labelRect = iconRect.union(textRect);
        if (SeaGlassLookAndFeel.isLeftToRight(context.getComponent())) {
            textRect.x += menuItemGap;
            iconRect.x += menuItemGap;

            // Position the Accelerator text rect
            acceleratorRect.x = viewRect.x + viewRect.width - arrowIconRect.width - menuItemGap - acceleratorRect.width;

            // Position the Check and Arrow Icons
            if (useCheckAndArrow) {
                checkIconRect.x = viewRect.x + menuItemGap;
                textRect.x += menuItemGap + checkIconRect.width;
                iconRect.x += menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap - arrowIconRect.width;
            }
            /* Align icons and text horizontally */
            if (popupUI != null) {
                int thisTextOffset = popupUI.adjustTextOffset(textRect.x - viewRect.x);
                textRect.x = thisTextOffset + viewRect.x;

                if (icon != null) {
                    if (horizontalTextPosition == SwingConstants.TRAILING || horizontalTextPosition == SwingConstants.RIGHT) {
                        int thisIconOffset = popupUI.adjustIconOffset(iconRect.x - viewRect.x);
                        iconRect.x = thisIconOffset + viewRect.x;
                    } else if (horizontalTextPosition == SwingConstants.LEADING || horizontalTextPosition == SwingConstants.LEFT) {
                        iconRect.x = textRect.x + textRect.width + menuItemGap;
                    } else {
                        int maxIconValue = popupUI.adjustIconOffset(0);
                        iconRect.x = Math.max(textRect.x + textRect.width / 2 - iconRect.width / 2, maxIconValue + viewRect.x);
                    }
                }
            }
        } else {
            textRect.x -= menuItemGap;
            iconRect.x -= menuItemGap;

            // Position the Accelerator text rect
            acceleratorRect.x = viewRect.x + arrowIconRect.width + menuItemGap;

            // Position the Check and Arrow Icons
            if (useCheckAndArrow) {
                checkIconRect.x = viewRect.x + viewRect.width - menuItemGap - checkIconRect.width;
                textRect.x -= menuItemGap + checkIconRect.width;
                iconRect.x -= menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + menuItemGap;
            }
            /* Align icons and text horizontally */
            if (popupUI != null) {
                int thisTextOffset = viewRect.x + viewRect.width - textRect.x - textRect.width;
                thisTextOffset = popupUI.adjustTextOffset(thisTextOffset);
                textRect.x = viewRect.x + viewRect.width - thisTextOffset - textRect.width;
                if (icon != null) {
                    if (horizontalTextPosition == SwingConstants.TRAILING || horizontalTextPosition == SwingConstants.LEFT) {
                        int thisIconOffset = viewRect.x + viewRect.width - iconRect.x - iconRect.width;
                        thisIconOffset = popupUI.adjustIconOffset(thisIconOffset);
                        iconRect.x = viewRect.x + viewRect.width - thisIconOffset - iconRect.width;

                    } else if (horizontalTextPosition == SwingConstants.LEADING || horizontalTextPosition == SwingConstants.RIGHT) {
                        iconRect.x = textRect.x - menuItemGap - iconRect.width;
                    } else {
                        int maxIconValue = popupUI.adjustIconOffset(0);
                        iconRect.x = textRect.x + textRect.width / 2 - iconRect.width / 2;
                        if (iconRect.x + iconRect.width > viewRect.x + viewRect.width - maxIconValue) {
                            iconRect.x = iconRect.x = viewRect.x + viewRect.width - maxIconValue - iconRect.width;
                        }
                    }
                }
            }
        }

        // Align the accelertor text and the check and arrow icons vertically
        // with the center of the label rect.
        acceleratorRect.y = labelRect.y + (labelRect.height / 2) - (acceleratorRect.height / 2);
        if (useCheckAndArrow) {
            arrowIconRect.y = labelRect.y + (labelRect.height / 2) - (arrowIconRect.height / 2);
            checkIconRect.y = labelRect.y + (labelRect.height / 2) - (checkIconRect.height / 2);
        }

        return text;
    }

    // these rects are used for painting and preferredsize calculations.
    // they used to be regenerated constantly. Now they are reused.
    static Rectangle iconRect        = new Rectangle();
    static Rectangle textRect        = new Rectangle();
    static Rectangle acceleratorRect = new Rectangle();
    static Rectangle checkIconRect   = new Rectangle();
    static Rectangle arrowIconRect   = new Rectangle();
    static Rectangle viewRect        = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
    static Rectangle r               = new Rectangle();

    private static void resetRects() {
        iconRect.setBounds(0, 0, 0, 0);
        textRect.setBounds(0, 0, 0, 0);
        acceleratorRect.setBounds(0, 0, 0, 0);
        checkIconRect.setBounds(0, 0, 0, 0);
        arrowIconRect.setBounds(0, 0, 0, 0);
        viewRect.setBounds(0, 0, Short.MAX_VALUE, Short.MAX_VALUE);
        r.setBounds(0, 0, 0, 0);
    }

    protected void installDefaults() {
        updateStyle(menuItem);
    }

    protected void installListeners() {
        super.installListeners();
        menuItem.addPropertyChangeListener(this);
    }

    private void updateStyle(JMenuItem mi) {
        SeaGlassContext context = getContext(mi, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (oldStyle != style) {
            String prefix = getPropertyPrefix();

            Object value = style.get(context, prefix + ".textIconGap");
            if (value != null) {
                LookAndFeel.installProperty(mi, "iconTextGap", value);
            }
            defaultTextIconGap = mi.getIconTextGap();

            if (menuItem.getMargin() == null || (menuItem.getMargin() instanceof UIResource)) {
                Insets insets = (Insets) style.get(context, prefix + ".margin");

                if (insets == null) {
                    // Some places assume margins are non-null.
                    insets = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                }
                menuItem.setMargin(insets);
            }
            acceleratorDelimiter = style.getString(context, prefix + ".acceleratorDelimiter", "+");

            arrowIcon = style.getIcon(context, prefix + ".arrowIcon");

            checkIcon = style.getIcon(context, prefix + ".checkIcon");
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();

        SeaGlassContext accContext = getContext(mi, Region.MENU_ITEM_ACCELERATOR, ENABLED);

        accStyle = SeaGlassLookAndFeel.updateStyle(accContext, this);
        accContext.dispose();
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(menuItem, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        SeaGlassContext accContext = getContext(menuItem, Region.MENU_ITEM_ACCELERATOR, ENABLED);
        accStyle.uninstallDefaults(accContext);
        accContext.dispose();
        accStyle = null;

        super.uninstallDefaults();
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        menuItem.removePropertyChangeListener(this);
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    public SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, accStyle, state);
    }

    private int getComponentState(JComponent c) {
        int state;

        if (!c.isEnabled()) {
            state = DISABLED;
        } else if (menuItem.isArmed()) {
            state = MOUSE_OVER;
        } else {
            state = SeaGlassLookAndFeel.getComponentState(c);
        }
        if (menuItem.isSelected()) {
            state |= SELECTED;
        }
        return state;
    }

    private int getComponentState(JComponent c, Region region) {
        return getComponentState(c);
    }

    protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        SeaGlassContext context = getContext(c);
        SeaGlassContext accContext = getContext(c, Region.MENU_ITEM_ACCELERATOR);
        Dimension value = getPreferredMenuItemSize(context, accContext, true, c, checkIcon, arrowIcon, defaultTextIconGap,
            acceleratorDelimiter);
        context.dispose();
        accContext.dispose();
        return value;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        paintBackground(context, g, c);
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        SeaGlassContext accContext = getContext(menuItem, Region.MENU_ITEM_ACCELERATOR);

        String prefix = getPropertyPrefix();
        Icon arIcon = style.getIcon(
            getContext(context.getComponent()), prefix + ".arrowIcon");
        paint(context, accContext, g, style.getIcon(getContext(context.getComponent()), prefix + ".checkIcon"), arIcon, true, acceleratorDelimiter, defaultTextIconGap);
        accContext.dispose();
    }

    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintMenuItemBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintMenuItemBorder(context, g, x, y, w, h);
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JMenuItem) e.getSource());
        }
    }
}
