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
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/**
 * Is the control in a toolbar?
 */
public class ControlInToolBarState extends State {

    /**
     * Creates a new ControlInToolBarState object.
     */
    public ControlInToolBarState() {
        super("InToolBar");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        for (Component comp = (Component) c; comp != null; comp = ((Container) comp).getParent()) {

            if (comp instanceof JToolBar) {
                return true;
            } else if (!(comp instanceof Container)) {
                break;
            }
        }

        return false;
    }
}
