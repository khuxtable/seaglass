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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.text.View;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthIcon;

/**
 * Customize the graphics utilities used by Synth.
 * 
 * @author Kathryn Huxtable
 */
public class SeaGlassGraphicsUtils extends SynthGraphicsUtils {
    // These are used in the text painting code to avoid allocating a bunch of
    // garbage.
    private Rectangle paintIconR       = new Rectangle();
    private Rectangle paintTextR       = new Rectangle();
    private Rectangle paintViewR       = new Rectangle();
    private Insets    paintInsets      = new Insets(0, 0, 0, 0);

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
            SynthIcon.paintIcon(icon, ss, g2d, paintIconR.x, paintIconR.y, paintIconR.width, paintIconR.height);
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

    public void drawEmphasizedText(Graphics g, Color foreground, Color emphasis, String s, int x, int y) {
        drawEmphasizedText(g, foreground, emphasis, s, -1, x, y);
    }

    public void drawEmphasizedText(Graphics g, Color foreground, Color emphasis, String s, int underlinedIndex, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(emphasis);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y + 1);
        g2d.setColor(foreground);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y);
    }
}
