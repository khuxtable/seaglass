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

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * SeaGlassToggleButtonUI.
 */
public class SeaGlassToggleButtonUI extends SeaGlassButtonUI {

    public static ComponentUI createUI(JComponent b) {
        return new SeaGlassToggleButtonUI();
    }

    @Override
    protected String getPropertyPrefix() {
        return "ToggleButton.";
    }

    @Override
    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        if (((AbstractButton) c).isContentAreaFilled()) {
            int x = 0, y = 0, w = c.getWidth(), h = c.getHeight();
            SynthPainter painter = context.getPainter();
            painter.paintToggleButtonBackground(context, g, x, y, w, h);
        }
    }

    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintToggleButtonBorder(context, g, x, y, w, h);
    }
}
