/*
 * ComboBoxPainter.java %E%
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass.painter;


/**
 */
public final class ComboBoxPainter extends AbstractImagePainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxPainter to determine which region/state is being painted
    //by that instance.
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_DISABLED_PRESSED = 2;
    public static final int BACKGROUND_ENABLED = 3;
    public static final int BACKGROUND_FOCUSED = 4;
    public static final int BACKGROUND_MOUSEOVER_FOCUSED = 5;
    public static final int BACKGROUND_MOUSEOVER = 6;
    public static final int BACKGROUND_PRESSED_FOCUSED = 7;
    public static final int BACKGROUND_PRESSED = 8;
    public static final int BACKGROUND_ENABLED_SELECTED = 9;
    public static final int BACKGROUND_DISABLED_EDITABLE = 10;
    public static final int BACKGROUND_ENABLED_EDITABLE = 11;
    public static final int BACKGROUND_FOCUSED_EDITABLE = 12;
    public static final int BACKGROUND_MOUSEOVER_EDITABLE = 13;
    public static final int BACKGROUND_PRESSED_EDITABLE = 14;

    public ComboBoxPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "combo_box_disabled.png";
            case BACKGROUND_DISABLED_PRESSED: return "combo_box_disabled.png";
            case BACKGROUND_ENABLED: return "combo_box.png";
            case BACKGROUND_FOCUSED: return "combo_box.png";
            case BACKGROUND_MOUSEOVER_FOCUSED: return "combo_box.png";
            case BACKGROUND_MOUSEOVER: return "combo_box.png";
            case BACKGROUND_PRESSED_FOCUSED: return "combo_box_pressed.png";
            case BACKGROUND_PRESSED: return "combo_box_pressed.png";
            case BACKGROUND_ENABLED_SELECTED: return "combo_box_pressed.png";
            case BACKGROUND_DISABLED_EDITABLE: return "empty_image.png";
            case BACKGROUND_ENABLED_EDITABLE: return "empty_image.png";
            case BACKGROUND_FOCUSED_EDITABLE: return "combo_box_focused_editable.png";
            case BACKGROUND_MOUSEOVER_EDITABLE: return "empty_image.png";
            case BACKGROUND_PRESSED_EDITABLE: return "empty_image.png";
        }
        return null;
    }
}
