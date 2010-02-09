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
import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.RootPaneContainer;

/**
 * Is the window for this maximize button in maximized state?
 */
public class TitlePaneMaximizeButtonWindowMaximizedState extends State {

    /**
     * Creates a new TitlePaneMaximizeButtonWindowMaximizedState object.
     */
    public TitlePaneMaximizeButtonWindowMaximizedState() {
        super("WindowMaximized");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c;

        while (parent.getParent() != null) {

            if (parent instanceof RootPaneContainer) {
                break;
            }

            parent = parent.getParent();
        }

        if (parent instanceof JFrame) {
            return (((JFrame) parent).getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
        } else if (parent instanceof JInternalFrame) {
            return ((JInternalFrame) parent).isMaximum();
        }

        return false;
    }
}
