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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * SeaGlass RadioButtonMenuItemUI delegate.
 * 
 * Based on SynthRadioButtonMenuItemUI by Georges Saab and David Karlton.
 * 
 * The only reason this exists is that we had to modify SynthMenuItemUI.
 * 
 * @see javax.swing.plaf.synth.SynthRadioButtonMenuItemUI
 */
public class SeaGlassRadioButtonMenuItemUI extends SeaGlassMenuItemUI {
    public static ComponentUI createUI(JComponent b) {
        return new SeaGlassRadioButtonMenuItemUI();
    }

    protected String getPropertyPrefix() {
        return "RadioButtonMenuItem";
    }

    public void processMouseEvent(JMenuItem item, MouseEvent e, MenuElement path[], MenuSelectionManager manager) {
        Point p = e.getPoint();
        if (p.x >= 0 && p.x < item.getWidth() && p.y >= 0 && p.y < item.getHeight()) {
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                manager.clearSelectedPath();
                item.doClick(0);
                item.setArmed(false);
            } else
                manager.setSelectedPath(path);
        } else if (item.getModel().isArmed()) {
            MenuElement newPath[] = new MenuElement[path.length - 1];
            int i, c;
            for (i = 0, c = path.length - 1; i < c; i++)
                newPath[i] = path[i];
            manager.setSelectedPath(newPath);
        }
    }

    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintRadioButtonMenuItemBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintRadioButtonMenuItemBorder(context, g, x, y, w, h);
    }
}
