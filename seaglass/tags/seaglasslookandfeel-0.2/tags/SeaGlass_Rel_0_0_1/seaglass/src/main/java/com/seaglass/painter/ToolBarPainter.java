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
package com.seaglass.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import com.seaglass.util.PlatformUtils;
import com.seaglass.util.WindowUtils;

/**
 * @author Ken Orr
 */
public class ToolBarPainter implements Painter<Component> {
    final boolean IS_MAC_OSX                     = PlatformUtils.isMac();

    // For non-Mac use Snow Leopard colors because it has the same Gamma
    // correction.
    private Color ACTIVE_TOP_GRADIENT_COLOR      = IS_MAC_OSX ? new Color(0xbcbcbc) : new Color(0xc4c4c4);
    private Color ACTIVE_BOTTOM_GRADIENT_COLOR   = IS_MAC_OSX ? new Color(0x9a9a9a) : new Color(0xb2b2b2);
    private Color INACTIVE_TOP_GRADIENT_COLOR    = IS_MAC_OSX ? new Color(0xe4e4e4) : new Color(0xe7e7e7);
    private Color INACTIVE_BOTTOM_GRADIENT_COLOR = IS_MAC_OSX ? new Color(0xd1d1d1) : new Color(0xdfdfdf);

    public void paint(Graphics2D graphics2D, Component component, int width, int height) {
        boolean containedInActiveWindow = WindowUtils.isParentWindowFocused(component);

        Color topColor = containedInActiveWindow ? ACTIVE_TOP_GRADIENT_COLOR : INACTIVE_TOP_GRADIENT_COLOR;
        Color bottomColor = containedInActiveWindow ? ACTIVE_BOTTOM_GRADIENT_COLOR : INACTIVE_BOTTOM_GRADIENT_COLOR;

        GradientPaint paint = new GradientPaint(0, 1, topColor, 0, height, bottomColor);
        graphics2D.setPaint(paint);
        graphics2D.fillRect(0, 0, width, height);
    }

}
