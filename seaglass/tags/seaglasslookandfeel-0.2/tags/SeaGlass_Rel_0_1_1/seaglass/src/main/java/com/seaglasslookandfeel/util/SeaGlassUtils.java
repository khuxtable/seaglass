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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * This is a dumping ground for random stuff we want to use in several places.
 * 
 * It should probably migrate into SeaGlassGraphicsUtils.
 * 
 * @author Ken Orr
 * @author Kathryn Huxtable
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
