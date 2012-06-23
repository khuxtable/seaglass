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
package com.seaglasslookandfeel.painter.titlepaneforegound;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * Painter for restore foreground shape.
 *
 * @author Kathryn Huxtable
 */
public class TitlePaneRestoreButtonForegroundPainter extends TitlePaneButtonForegroundPainter {

    /**
     * {@inheritDoc}
     */
    protected void paint(Graphics2D g, JComponent c, int width, int height, Color border, Color corner, Color interior) {
        int top  = (height - 2) / 2 - 6;
        int left = (width - 2) / 2 - 5;

        g = (Graphics2D) g.create();
        g.translate(left, top);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(interior);
        g.fillRect(4, 1, 8, 2);
        g.fillRect(10, 3, 2, 6);
        g.fillRect(4, 7, 2, 2);
        g.drawRect(1, 4, 7, 7);
        g.drawRect(2, 5, 5, 5);

        g.setColor(border);
        g.drawRect(3, 6, 3, 3);
        g.drawLine(4, 0, 11, 0);
        g.drawLine(1, 3, 9, 3);
        g.drawLine(10, 9, 11, 9);
        g.drawLine(1, 12, 8, 12);
        g.drawLine(0, 4, 0, 11);
        g.drawLine(3, 1, 3, 2);
        g.drawLine(9, 4, 9, 11);
        g.drawLine(12, 1, 12, 8);

        g.setColor(corner);
        g.drawLine(3, 0, 3, 0);
        g.drawLine(12, 0, 12, 0);
        g.drawLine(0, 3, 0, 3);
        g.drawLine(12, 9, 12, 9);
        g.drawLine(0, 12, 0, 12);
        g.drawLine(9, 12, 9, 12);
    }
}
