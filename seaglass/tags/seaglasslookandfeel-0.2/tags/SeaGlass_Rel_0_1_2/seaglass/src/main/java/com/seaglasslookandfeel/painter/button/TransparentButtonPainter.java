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
package com.seaglasslookandfeel.painter.button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.ButtonPainter.Which;

/**
 * Paint a "textured" button. This is suitable for placing on darker grey
 * backgrounds such as toolbars and bottom bars.
 * 
 * @author Kathryn Huxtable
 */
public class TransparentButtonPainter extends ButtonVariantPainter {

    private static final Color OUTER_FOCUS_COLOR = new Color(0x8072a5d2, true);
    private static final Color INNER_FOCUS_COLOR = new Color(0x73a4d1);

    Path2D                     path              = new Path2D.Double();

    /**
     * Create a segmented button painter.
     * 
     * @param state
     *            the button state.
     * @param ctx
     *            the paint context.
     */
    public TransparentButtonPainter(Which state, PaintContext ctx) {
        super(state, ctx);
    }

    /**
     * {@inheritDoc}
     */
    public void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (focused) {
            path = decodeInnerFocus(width, height);
            g.setColor(INNER_FOCUS_COLOR);
            g.draw(path);
            path = decodeOuterFocus(width, height);
            g.setColor(OUTER_FOCUS_COLOR);
            g.draw(path);
        }
    }

    Path2D decodeOuterFocus(int width, int height) {
        double arcSize = 6d;
        int x = 0;
        int y = 0;
        width -= 1;
        height -= 1;
        decodeFocusPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    Path2D decodeInnerFocus(int width, int height) {
        double arcSize = 5d;
        int x = 1;
        int y = 1;
        width -= 3;
        height -= 3;
        decodeFocusPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    private void decodeFocusPath(int left, int top, int width, int height, Double arcW, Double arcH) {
        int bottom = top + height;
        int right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }
}
