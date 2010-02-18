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

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

/**
 * Is the window for this toolbar active?
 */
public class ToolBarHasNorthToolBarState extends State {

    private static final State isNorthState = new ToolBarNorthState();

    /**
     * Creates a new ToolBarHasNorthToolBarState object.
     */
    public ToolBarHasNorthToolBarState() {
        super("HasNorthToolBar");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c.getParent();

        while (parent.getParent() != null) {

            if (parent instanceof JInternalFrame || parent instanceof JRootPane) {
                break;
            }

            parent = parent.getParent();
        }

        Component[] cArray = null;

        if (parent != null) {

            if (parent instanceof JInternalFrame) {
                cArray = ((JInternalFrame) parent).getContentPane().getComponents();
            } else if (parent instanceof JRootPane) {
                cArray = ((JRootPane) parent).getContentPane().getComponents();
            }
        }

        if (cArray != null) {

            for (Component comp : cArray) {

                if (comp instanceof JToolBar) {

                    if (comp != c && isNorthState.isInState((JComponent) comp)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
