/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Aqvavit Pluggable Look and Feel.
 *
 * Aqvavit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * Aqvavit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Aqvavit.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * This is a dumping ground for random stuff we want to use in several places.
 */
public class SeaGlassUtils {

    public static void drawEmphasizedString(Graphics g, Color foreground, Color emphasis, String s, int x, int y) {
        drawEmphasizedString(g, foreground, emphasis, s, -1, x, y);
    }

    public static void drawEmphasizedString(Graphics g, Color foreground, Color emphasis, String s, int underlinedIndex, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(emphasis);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y + 1);
        g2d.setColor(foreground);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y);
    }

    public static void drawString(Graphics g, String s, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, -1, x, y);
    }

    public static void drawStringUnderlineCharAt(Graphics g, String s, int underlinedIndex, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, s, underlinedIndex, x, y);
    }
}
