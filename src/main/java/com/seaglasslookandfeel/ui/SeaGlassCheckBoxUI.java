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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.ui;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthCheckBoxUI;
import javax.swing.plaf.synth.SynthContext;

import com.seaglasslookandfeel.SeaGlassContext;

/**
 * @author Kathryn Huxtable
 *
 */
public class SeaGlassCheckBoxUI extends SeaGlassRadioButtonUI {

    // ********************************
    //            Create PLAF
    // ********************************
    /**
     * Creates a new UI object for the given component.
     *
     * @param b component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent b) {
        return new SeaGlassCheckBoxUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected String getPropertyPrefix() {
        return "CheckBox.";
    }

    @Override
    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        ((SeaGlassContext) context).getPainter().paintCheckBoxBackground(context, g, 0, 0,
                                                  c.getWidth(), c.getHeight());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintCheckBoxBorder(context, g, x, y, w, h);
    }

}
