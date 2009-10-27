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
 * $Id: ComboBoxArrowButtonEditableState.java 273 2009-10-22 23:21:17Z kathryn@kathrynhuxtable.org $
 */
package com.seaglass.state;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;

/**
 */
public class RootPaneNoFrameState extends State {
    public RootPaneNoFrameState() {
        super("Frame");
    }

    protected boolean isInState(JComponent c) {
        Component parent = c.getParent();
        if (true) return ((JRootPane) c).getWindowDecorationStyle() == JRootPane.NONE;
        if (parent instanceof JFrame) return true;
        if (parent instanceof JInternalFrame) return true;
        if (parent instanceof JDialog) return true;
        return false;
    }
}
