/*
 * ComboBoxArrowButtonPainter.java %E%
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass.painter;


/**
 */
public final class ComboBoxArrowButtonPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxArrowButtonPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_ENABLED_MOUSEOVER = 3;
    public static final int BACKGROUND_ENABLED_PRESSED = 4;
    public static final int BACKGROUND_DISABLED_EDITABLE = 5;
    public static final int BACKGROUND_ENABLED_EDITABLE = 6;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 7;
    public static final int BACKGROUND_PRESSED_EDITABLE = 8;
    public static final int BACKGROUND_SELECTED_EDITABLE = 9;
    public static final int FOREGROUND_ENABLED = 10;
    public static final int FOREGROUND_MOUSEOVER = 11;
    public static final int FOREGROUND_DISABLED = 12;
    public static final int FOREGROUND_PRESSED = 13;
    public static final int FOREGROUND_SELECTED = 14;

    public ComboBoxArrowButtonPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED_EDITABLE: return "combo_box_button_background_disabled";
            case BACKGROUND_ENABLED_EDITABLE: return "combo_box_button_background";
            case BACKGROUND_MOUSEOVER_EDITABLE: return "combo_box_button_background";
            case BACKGROUND_PRESSED_EDITABLE: return "combo_box_button_background_pressed";
            case BACKGROUND_SELECTED_EDITABLE: return "combo_box_button_background_pressed";
            case FOREGROUND_ENABLED: return "combo_box_arrow_foreground";
            case FOREGROUND_MOUSEOVER: return "combo_box_arrow_foreground";
            case FOREGROUND_DISABLED: return "combo_box_arrow_foreground_disabled";
            case FOREGROUND_PRESSED: return "combo_box_arrow_foreground";
            case FOREGROUND_SELECTED: return "combo_box_arrow_foreground";
        }
        return null;
    }
}
