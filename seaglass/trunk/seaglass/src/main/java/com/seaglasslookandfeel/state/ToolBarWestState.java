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
package com.seaglasslookandfeel.state;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * Is the toolbar on the (right) west side?
 */
public class ToolBarWestState extends State {

    /**
     * Creates a new ToolBarWestState object.
     */
    public ToolBarWestState() {
        super("West");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        JToolBar toolbar = (JToolBar) c;

        return SeaGlassLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.WEST;
    }
}
