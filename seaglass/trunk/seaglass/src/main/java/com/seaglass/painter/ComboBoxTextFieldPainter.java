/*
 * ComboBoxTextFieldPainter.java %E%
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass.painter;


/**
 */
public final class ComboBoxTextFieldPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxTextFieldPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_SELECTED = 3;

    public ComboBoxTextFieldPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "combo_box_textfield_disabled";
            case BACKGROUND_ENABLED: return "combo_box_textfield_enabled";
            case BACKGROUND_SELECTED: return "combo_box_textfield_selected";
        }
        return null;
    }
}
