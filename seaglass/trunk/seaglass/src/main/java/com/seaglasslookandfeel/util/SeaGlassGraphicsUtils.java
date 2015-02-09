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
package com.seaglasslookandfeel.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

import sun.swing.MenuItemLayoutHelper;
import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthIcon;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassIcon;

/**
 * Customize the graphics utilities used by Synth.
 *
 * @author Kathryn Huxtable
 */
public class SeaGlassGraphicsUtils extends SynthGraphicsUtils {

    // These are used in the text painting code to avoid allocating a bunch of
    // garbage.
    private Rectangle paintIconR  = new Rectangle();
    private Rectangle paintTextR  = new Rectangle();
    private Rectangle paintViewR  = new Rectangle();
    private Insets    paintInsets = new Insets(0, 0, 0, 0);

    
    public static void applyInsets(Rectangle rect, Insets insets, boolean leftToRight) {
        if (insets != null) {
            rect.x += (leftToRight ? insets.left : insets.right);
            rect.y += insets.top;
            rect.width -= (leftToRight ? insets.right : insets.left) + rect.x;
            rect.height -= (insets.bottom + rect.y);
        }
    }

    
    public static void paint(SynthContext context, SynthContext accContext, Graphics g, Icon checkIcon, Icon arrowIcon,
        String acceleratorDelimiter, int defaultTextIconGap, String propertyPrefix) {
        JMenuItem mi = (JMenuItem) context.getComponent();
        SynthStyle style = context.getStyle();
        g.setFont(style.getFont(context));

        Rectangle viewRect = new Rectangle(0, 0, mi.getWidth(), mi.getHeight());
        boolean leftToRight = SeaGlassLookAndFeel.isLeftToRight(mi);
        applyInsets(viewRect, mi.getInsets(), leftToRight);

        SeaGlassMenuItemLayoutHelper lh = new SeaGlassMenuItemLayoutHelper(context, accContext, mi, checkIcon, arrowIcon, viewRect,
            defaultTextIconGap, acceleratorDelimiter, leftToRight, MenuItemLayoutHelper.useCheckAndArrow(mi), propertyPrefix);
        MenuItemLayoutHelper.LayoutResult lr = lh.layoutMenuItem();

        paintMenuItem(g, lh, lr);
    }

    
    public static void paintMenuItem(Graphics g, SeaGlassMenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr) {
        // Save original graphics font and color
        Font holdf = g.getFont();
        Color holdc = g.getColor();

        paintCheckIcon(g, lh, lr);
        paintIcon(g, lh, lr);
        paintText(g, lh, lr);
        paintAccText(g, lh, lr);
        paintArrowIcon(g, lh, lr);

        // Restore original graphics font and color
        g.setColor(holdc);
        g.setFont(holdf);
    }

    static void paintBackground(Graphics g, SeaGlassMenuItemLayoutHelper lh) {
        paintBackground(lh.getContext(), g, lh.getMenuItem());
    }

    static void paintBackground(SynthContext context, Graphics g, JComponent c) {
        ((SeaGlassContext)context).getPainter().paintMenuItemBackground(context, g, 0, 0,
                c.getWidth(), c.getHeight());
    }

    static void paintIcon(Graphics g, SeaGlassMenuItemLayoutHelper lh,
                          MenuItemLayoutHelper.LayoutResult lr) {
        if (lh.getIcon() != null) {
            Icon icon;
            JMenuItem mi = lh.getMenuItem();
            ButtonModel model = mi.getModel();
            if (!model.isEnabled()) {
                icon = mi.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = mi.getPressedIcon();
                if (icon == null) {
                    // Use default icon
                    icon = mi.getIcon();
                }
            } else {
                icon = mi.getIcon();
            }

            if (icon != null) {
                Rectangle iconRect = lr.getIconRect();
                SynthIcon.paintIcon(icon, lh.getContext(), g, iconRect.x,
                        iconRect.y, iconRect.width, iconRect.height);
            }
        }
    }

    static void paintCheckIcon(Graphics g, SeaGlassMenuItemLayoutHelper lh,
                               MenuItemLayoutHelper.LayoutResult lr) {
        if (lh.getCheckIcon() != null) {
            Rectangle checkRect = lr.getCheckRect();
            SynthIcon.paintIcon(lh.getCheckIcon(), lh.getContext(), g,
                    checkRect.x, checkRect.y, checkRect.width, checkRect.height);
        }
    }

    static void paintAccText(Graphics g, SeaGlassMenuItemLayoutHelper lh,
                             MenuItemLayoutHelper.LayoutResult lr) {
        String accText = lh.getAccText();
        if (accText != null && !accText.equals("")) {
            g.setColor(lh.getAccStyle().getColor(lh.getAccContext(),
                    ColorType.TEXT_FOREGROUND));
            g.setFont(lh.getAccStyle().getFont(lh.getAccContext()));
            lh.getAccGraphicsUtils().paintText(lh.getAccContext(), g, accText,
                    lr.getAccRect().x, lr.getAccRect().y, -1);
        }
    }

    static void paintText(Graphics g, SeaGlassMenuItemLayoutHelper lh,
                          MenuItemLayoutHelper.LayoutResult lr) {
        if (!lh.getText().equals("")) {
            if (lh.getHtmlView() != null) {
                // Text is HTML
                lh.getHtmlView().paint(g, lr.getTextRect());
            } else {
                // Text isn't HTML
                g.setColor(lh.getStyle().getColor(
                        lh.getContext(), ColorType.TEXT_FOREGROUND));
                g.setFont(lh.getStyle().getFont(lh.getContext()));
                lh.getGraphicsUtils().paintText(lh.getContext(), g, lh.getText(),
                        lr.getTextRect().x, lr.getTextRect().y,
                        lh.getMenuItem().getDisplayedMnemonicIndex());
            }
        }
    }

    static void paintArrowIcon(Graphics g, SeaGlassMenuItemLayoutHelper lh,
                               MenuItemLayoutHelper.LayoutResult lr) {
        if (lh.getArrowIcon() != null) {
            Rectangle arrowRect = lr.getArrowRect();
            SynthIcon.paintIcon(lh.getArrowIcon(), lh.getContext(), g,
                    arrowRect.x, arrowRect.y, arrowRect.width, arrowRect.height);
        }
    }

    /**
     * Paints text at the specified location. This will not attempt to render
     * the text as html nor will it offset by the insets of the component.
     *
     * @param ss
     *            SynthContext
     * @param g
     *            Graphics used to render string in.
     * @param text
     *            Text to render
     * @param x
     *            X location to draw text at.
     * @param y
     *            Upper left corner to draw text at.
     * @param mnemonicIndex
     *            Index to draw string at.
     */
    public void paintText(SynthContext ss, Graphics g, String text, int x, int y, int mnemonicIndex) {
        if (text != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            JComponent c = ss.getComponent();

            // SynthStyle style = ss.getStyle();
            FontMetrics fm = SwingUtilities2.getFontMetrics(c, g2d);

            y += fm.getAscent();
            SwingUtilities2.drawString(c, g2d, text, x, y);

            if (mnemonicIndex >= 0 && mnemonicIndex < text.length()) {
                int underlineX = x + SwingUtilities2.stringWidth(c, fm, text.substring(0, mnemonicIndex));
                int underlineY = y;
                int underlineWidth = fm.charWidth(text.charAt(mnemonicIndex));
                int underlineHeight = 1;

                g2d.fillRect(underlineX, underlineY + fm.getDescent() - 1, underlineWidth, underlineHeight);
            }
        }
    }

    /**
     * Returns a new color with the alpha of the old color cut in half.
     *
     * @param color
     *            the original color.
     *
     * @return the new color.
     */
    // Rossi: used in slider ui "paint ticks".
    public static Color disable(Color color) {
        int alpha = color.getAlpha();

        alpha /= 2;

        return new Color((color.getRGB() & 0xFFFFFF) | (alpha << 24), true);
    }

    /**
     * Paints an icon and text. This will render the text as html, if necessary,
     * and offset the location by the insets of the component.
     *
     * @param ss
     *            SynthContext
     * @param g
     *            Graphics to render string and icon into
     * @param text
     *            Text to layout
     * @param icon
     *            Icon to layout
     * @param hAlign
     *            horizontal alignment
     * @param vAlign
     *            vertical alignment
     * @param hTextPosition
     *            horizontal text position
     * @param vTextPosition
     *            vertical text position
     * @param iconTextGap
     *            gap between icon and text
     * @param mnemonicIndex
     *            Index into text to render the mnemonic at, -1 indicates no
     *            mnemonic.
     * @param textOffset
     *            Amount to offset the text when painting
     */
    public void paintText(SynthContext ss, Graphics g, String text, Icon icon, int hAlign, int vAlign, int hTextPosition,
        int vTextPosition, int iconTextGap, int mnemonicIndex, int textOffset) {
        if ((icon == null) && (text == null)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        JComponent c = ss.getComponent();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g2d);
        Insets insets = SeaGlassLookAndFeel.getPaintingInsets(ss, paintInsets);

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = c.getWidth() - (insets.left + insets.right);
        paintViewR.height = c.getHeight() - (insets.top + insets.bottom);

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        String clippedText = layoutText(ss, fm, text, icon, hAlign, vAlign, hTextPosition, vTextPosition, paintViewR, paintIconR,
            paintTextR, iconTextGap);

        if (icon != null) {
            Color color = g2d.getColor();

            paintIconR.x += textOffset;
            paintIconR.y += textOffset;
            SeaGlassIcon.paintIcon(icon, ss, g2d, paintIconR.x, paintIconR.y, paintIconR.width, paintIconR.height);
            g2d.setColor(color);
        }

        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);

            if (v != null) {
                v.paint(g2d, paintTextR);
            } else {
                paintTextR.x += textOffset;
                paintTextR.y += textOffset;

                paintText(ss, g2d, clippedText, paintTextR, mnemonicIndex);
            }
        }
    }

    /**
     * Draw text with an emphasized background.
     *
     * @param g
     *            the Graphics context to draw with.
     * @param foreground
     *            the foreground color.
     * @param emphasis
     *            the emphasis color.
     * @param s
     *            the text to draw.
     * @param x
     *            the x coordinate to draw at.
     * @param y
     *            the y coordinate to draw at.
     */
    public void drawEmphasizedText(Graphics g, Color foreground, Color emphasis, String s, int x, int y) {
        drawEmphasizedText(g, foreground, emphasis, s, -1, x, y);
    }

    /**
     * Draw text with an emphasized background.
     *
     * @param g
     *            the Graphics context to draw with.
     * @param foreground
     *            the foreground color.
     * @param emphasis
     *            the emphasis color.
     * @param s
     *            the text to draw.
     * @param underlinedIndex
     *            the index to underline.
     * @param x
     *            the x coordinate to draw at.
     * @param y
     *            the y coordinate to draw at.
     */
    public void drawEmphasizedText(Graphics g, Color foreground, Color emphasis, String s, int underlinedIndex, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(emphasis);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y + 1);
        g2d.setColor(foreground);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y);
    }

    public static Dimension getPreferredMenuItemSize(SynthContext context, SynthContext accContext, JComponent c, Icon checkIcon,
        Icon arrowIcon, int defaultTextIconGap, String acceleratorDelimiter, boolean useCheckAndArrow, String propertyPrefix) {

        JMenuItem mi = (JMenuItem) c;
        SeaGlassMenuItemLayoutHelper lh = new SeaGlassMenuItemLayoutHelper(context, accContext, mi, checkIcon, arrowIcon,
            MenuItemLayoutHelper.createMaxRect(), defaultTextIconGap, acceleratorDelimiter, SeaGlassLookAndFeel.isLeftToRight(mi),
            useCheckAndArrow, propertyPrefix);

        Dimension result = new Dimension();

        // Calculate the result width
        int gap = lh.getGap();
        result.width = 0;
        MenuItemLayoutHelper.addMaxWidth(lh.getCheckSize(), gap, result);
        MenuItemLayoutHelper.addMaxWidth(lh.getLabelSize(), gap, result);
        MenuItemLayoutHelper.addWidth(lh.getMaxAccOrArrowWidth(), 5 * gap, result);
        // The last gap is unnecessary
        result.width -= gap;

        // Calculate the result height
        result.height = MenuItemLayoutHelper.max(lh.getCheckSize().getHeight(), lh.getLabelSize().getHeight(), lh.getAccSize().getHeight(),
            lh.getArrowSize().getHeight());

        // Take into account menu item insets
        Insets insets = lh.getMenuItem().getInsets();
        if (insets != null) {
            result.width += insets.left + insets.right;
            result.height += insets.top + insets.bottom;
        }

        // if the width is even, bump it up one. This is critical
        // for the focus dash lhne to draw properly
        if (result.width % 2 == 0) {
            result.width++;
        }

        // if the height is even, bump it up one. This is critical
        // for the text to center properly
        if (result.height % 2 == 0) {
            result.height++;
        }

        return result;

    }

}
