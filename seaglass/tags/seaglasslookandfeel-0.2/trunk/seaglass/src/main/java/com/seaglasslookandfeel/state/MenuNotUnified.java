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
 * $Id: ScrollBarButtonIsIncreaseButtonState.java 1118 2010-02-09 21:41:47Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.state;

import javax.swing.JComponent;

import com.seaglasslookandfeel.ui.SeaGlassRootPaneUI;

/**
 * Is the main menu bar unified with the title area?
 */
public class MenuNotUnified extends State {

    /**
     * Creates a new MenuUnified state object
     */
    public MenuNotUnified() {
        super("NotUnified");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
       return c.getRootPane() != null &&
                    c.getRootPane().getClientProperty(
                        SeaGlassRootPaneUI.UNIFIED_TOOLBAR_LOOK) != Boolean.TRUE && 
                        c.getRootPane().getClientProperty("JRootPane.MenuInTitle") != Boolean.TRUE;
    }
}
