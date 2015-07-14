/*
 * InternalFrameWindowFocusedState.java 07/12/12
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglasslookandfeel.state;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 * Is internal frame's window in focused state?
 */
public class InternalFrameWindowFocusedState extends State {

    /**
     * Creates a new InternalFrameWindowFocusedState object.
     */
    public InternalFrameWindowFocusedState() {
        super("WindowFocused");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        return c instanceof JInternalFrame && ((JInternalFrame) c).isSelected();
    }
}
