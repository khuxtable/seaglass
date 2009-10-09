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
package com.seaglass.painter;


/**
 */
public final class TextAreaPainter extends AbstractImagePainter {
    public static final int BACKGROUND_DISABLED = 1;
    public static final int BACKGROUND_ENABLED = 2;
    public static final int BACKGROUND_DISABLED_NOTINSCROLLPANE = 3;
    public static final int BACKGROUND_ENABLED_NOTINSCROLLPANE = 4;
    public static final int BACKGROUND_SELECTED = 5;
    public static final int BORDER_DISABLED_NOTINSCROLLPANE = 6;
    public static final int BORDER_FOCUSED_NOTINSCROLLPANE = 7;
    public static final int BORDER_ENABLED_NOTINSCROLLPANE = 8;

    public TextAreaPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch(state) {
            case BACKGROUND_DISABLED: return "textarea_disabled";
            case BACKGROUND_ENABLED: return "textarea";
            case BACKGROUND_DISABLED_NOTINSCROLLPANE: return "textarea_disabled_notinscroll";
            case BACKGROUND_ENABLED_NOTINSCROLLPANE: return "textarea_notinscroll";
            case BACKGROUND_SELECTED: return "textarea_selected";
            case BORDER_DISABLED_NOTINSCROLLPANE: return "textarea_border_disabled_notinscroll";
            case BORDER_FOCUSED_NOTINSCROLLPANE: return "textarea_border_focused_notinscroll";
            case BORDER_ENABLED_NOTINSCROLLPANE: return "textarea_border_notinscroll";
        }
        return null;
    }
}
