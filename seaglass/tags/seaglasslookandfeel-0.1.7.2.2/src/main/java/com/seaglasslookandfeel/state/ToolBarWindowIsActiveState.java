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

import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 * Is the window for this toolbar active?
 */
public class ToolBarWindowIsActiveState extends State {

    /**
     * Creates a new ToolBarWindowIsActiveState object.
     */
    public ToolBarWindowIsActiveState() {
        super("WindowIsActive");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c;

        while (parent.getParent() != null) {

            if (parent instanceof JInternalFrame || parent instanceof Window) {
                break;
            }

            parent = parent.getParent();
        }

        if (parent instanceof JInternalFrame) {
            return ((JInternalFrame) parent).isSelected();
        } else if (parent instanceof Window) {
            return ((Window) parent).isActive();
        }

        // Default to true.
        return true;
    }
}
