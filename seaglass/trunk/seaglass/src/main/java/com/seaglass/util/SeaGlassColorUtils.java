/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Aqvavit Pluggable Look and Feel.
 *
 * Aqvavit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * Aqvavit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Aqvavit.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.util;

import java.awt.Color;

/**
 * Utility methods for dealing with Mac colors.
 * 
 * @author Ken Orr
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
