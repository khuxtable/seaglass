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

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;

/**
 * Are scroll bar buttons to be placed together or apart?
 */
public class ScrollBarButtonsTogetherState extends State {

    /**
     * Creates a new ScrollBarButtonsTogetherState object.
     */
    public ScrollBarButtonsTogetherState() {
        super("ButtonsTogether");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        while (c != null && !(c instanceof JScrollBar)) {
            c = (JComponent) c.getParent();
        }

        if (c != null) {
            Object clientProperty = c.getClientProperty("SeaGlass.Override.ScrollBarButtonsTogether");

            if (clientProperty != null && clientProperty instanceof Boolean) {
                return (Boolean) clientProperty;
            }
        }

        return UIManager.getBoolean("SeaGlass.ScrollBarButtonsTogether");
    }
}
