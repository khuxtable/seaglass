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
package com.seaglasslookandfeel.component;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import com.seaglasslookandfeel.painter.Painter;

/**
 * TableScrollPaneCorner - A simple component that paints itself using the table
 * header background painter. It is used to fill the top right corner of
 * scrollpane.
 * 
 * Based on Nimbus's TableScrollPaneCorner by Jasper Potts
 * 
 * @see com.sun.java.swing.plaf.nimbus.TableScrollPaneCorner
 */
public class TableScrollPaneCorner extends JComponent implements UIResource {

    /**
     * Paint the component using the Nimbus Table Header Background Painter
     */
    @Override
    protected void paintComponent(Graphics g) {
        Painter painter = (Painter) UIManager.get("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter");
        if (painter != null) {

            Graphics2D g2d = (Graphics2D) g;
            painter.paint(g2d, this, getWidth() + 1, getHeight());
        }
    }
}
