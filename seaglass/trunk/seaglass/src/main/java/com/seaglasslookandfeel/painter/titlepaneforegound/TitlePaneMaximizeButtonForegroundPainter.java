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
 * Painter for iconify foreground shape.
 *
 * @author Kathryn Huxtable
 */
public class TitlePaneMaximizeButtonForegroundPainter extends TitlePaneButtonForegroundPainter {

    /**
     * {@inheritDoc}
     */
    protected void paint(Graphics2D g, JComponent c, int width, int height, Color border, Color corner, Color interior) {
        int top  = (height - 2) / 2 - 5;
        int left = (width - 2) / 2 - 5;

        g = (Graphics2D) g.create();
        g.translate(left, top);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(interior);
        g.drawRect(1, 1, 9, 7);
        g.drawRect(2, 2, 7, 5);

        g.setColor(border);
        g.drawRect(3, 3, 5, 3);
        g.drawLine(1, 0, 10, 0);
        g.drawLine(1, 9, 10, 9);
        g.drawLine(0, 1, 0, 8);
        g.drawLine(11, 1, 11, 8);

        g.setColor(corner);
        g.drawLine(0, 0, 0, 0);
        g.drawLine(11, 0, 11, 0);
        g.drawLine(0, 9, 0, 9);
        g.drawLine(11, 9, 11, 9);
    }
}
