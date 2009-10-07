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
package com.seaglass.util;

import java.awt.Color;

/**
 * Utility methods for dealing with Mac colors.
 * 
 * From MacWidgets for Java by Ken Orr.
 * 
 * @author Ken Orr
 * @author Kathryn Huxtable
 */
public class SeaGlassColorUtils {

    public static Color EMPTY_COLOR = new Color(0, 0, 0, 0);

    // OS X unified toolbar colors. ///////////////////////////////////////////////////////////////

    private static final Color LEOPARD_TEXTURED_WINDOW_FOCUSED_BORDER_COLOR = new Color(64, 64, 64);
    private static final Color LEOPARD_TEXTURED_WINDOW_UNFOCUSED_BORDER_COLOR = new Color(135, 135, 135);
    private static final Color TEXTURED_WINDOW_FOCUSED_BORDER_COLOR = new Color(0x515151);
    private static final Color TEXTURED_WINDOW_UNFOCUSED_BORDER_COLOR = new Color(0x969696);

    /**
     * Gets the color used to separate a toolbar from the window content when the
     * window is active.
     *
     * @return the border color when the window is active.
     */
    public static Color getTexturedWindowToolbarBorderFocusedColor() {
        return PlatformUtils.isLeopard()
                ? LEOPARD_TEXTURED_WINDOW_FOCUSED_BORDER_COLOR
                : TEXTURED_WINDOW_FOCUSED_BORDER_COLOR;
    }

    /**
     * Gets the color used to separate a toolbar from the window content when the
     * window is inactive.
     *
     * @return the border color when the window is inactive.
     */
    public static Color getTexturedWindowToolbarBorderUnfocusedColor() {
        return PlatformUtils.isLeopard()
                ? LEOPARD_TEXTURED_WINDOW_UNFOCUSED_BORDER_COLOR
                : TEXTURED_WINDOW_UNFOCUSED_BORDER_COLOR;
    }
}
