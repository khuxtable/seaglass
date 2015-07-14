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

/**
 * Is the text area not in a scroll pane?
 */
public class TextAreaNotInScrollPaneState extends State {

    /**
     * Creates a new TextAreaNotInScrollPaneState object.
     */
    public TextAreaNotInScrollPaneState() {
        super("NotInScrollPane");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        return !(c.getParent() instanceof javax.swing.JViewport);
    }
}
