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

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import com.seaglass.util.WindowUtils;

/**
 * TitleBarPainter implementation.
 * 
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
