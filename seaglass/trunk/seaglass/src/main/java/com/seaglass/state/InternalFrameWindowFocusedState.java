/*
 * InternalFrameWindowFocusedState.java 07/12/12
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass.state;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import com.seaglass.State;

/**
 */
public class InternalFrameWindowFocusedState extends State {
    public InternalFrameWindowFocusedState() {
        super("WindowFocused");
    }

    protected boolean isInState(JComponent c) {
        return c instanceof JInternalFrame && ((JInternalFrame) c).isSelected();
    }
}
