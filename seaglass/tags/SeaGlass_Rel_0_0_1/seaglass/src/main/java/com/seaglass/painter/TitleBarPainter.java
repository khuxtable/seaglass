/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id: org.eclipse.jdt.ui.prefs 30 2009-09-22 11:14:17Z kathryn@kathrynhuxtable.org $
 */
package com.seaglass.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import com.seaglass.util.WindowUtils;

/**
 * @author Ken Orr
 */
public class TitleBarPainter implements Painter<Component> {

    // Use Snow Leopard colors because it has the same Gamma correction
    // as non-Mac systems.
    private Color ACTIVE_TOP_GRADIENT_COLOR      = new Color(0xd0d0d0);
    private Color ACTIVE_BOTTOM_GRADIENT_COLOR   = new Color(0xc4c4c4);
    private Color INACTIVE_TOP_GRADIENT_COLOR    = new Color(0xececec);
    private Color INACTIVE_BOTTOM_GRADIENT_COLOR = new Color(0xe7e7e7);

    public void paint(Graphics2D graphics2D, Component component, int width, int height) {
        boolean containedInActiveWindow = WindowUtils.isParentWindowFocused(component);

        // We'll only paint this on a non-Mac.
        Color topColor = containedInActiveWindow ? ACTIVE_TOP_GRADIENT_COLOR : INACTIVE_TOP_GRADIENT_COLOR;
        Color bottomColor = containedInActiveWindow ? ACTIVE_BOTTOM_GRADIENT_COLOR : INACTIVE_BOTTOM_GRADIENT_COLOR;

        GradientPaint paint = new GradientPaint(0, 1, topColor, 0, height, bottomColor);
        graphics2D.setPaint(paint);
        graphics2D.fillRect(0, 0, width, height);
    }

}
