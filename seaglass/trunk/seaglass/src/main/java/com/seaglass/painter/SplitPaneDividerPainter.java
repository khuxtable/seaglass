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
 * SplitPaneDividerPainter implementation.
 */
public final class SplitPaneDividerPainter extends AbstractImagePainter {
    public static final int BACKGROUND_ENABLED          = 1;
    public static final int BACKGROUND_FOCUSED          = 2;
    public static final int FOREGROUND_ENABLED          = 3;
    public static final int FOREGROUND_ENABLED_VERTICAL = 4;

    public SplitPaneDividerPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
        case BACKGROUND_ENABLED:
            return "splitpane_divider_background";
        case BACKGROUND_FOCUSED:
            return "splitpane_divider_background_focused";
        case FOREGROUND_ENABLED:
            return "splitpane_divider_horizontal_foreground";
        case FOREGROUND_ENABLED_VERTICAL:
            return "splitpane_divider_vertical_foreground";
        }
        return null;
    }
}
