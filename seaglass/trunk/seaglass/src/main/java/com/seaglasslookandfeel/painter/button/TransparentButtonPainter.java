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
 * Paint a "transparent" button. This is suitable for placing on darker grey
 * backgrounds such as toolbars and bottom bars.
 * 
 * @author Kathryn Huxtable
 */
public class TransparentButtonPainter extends ButtonVariantPainter {

    private Color outerFocusColor        = decodeColor("seaGlassOuterFocus", 0f, 0f, 0f, 0);
    private Color innerFocusColor        = decodeColor("seaGlassFocus", 0f, 0f, 0f, 0);
    private Color outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus", 0f, 0f, 0f, 0);
    private Color innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus", 0f, 0f, 0f, 0);

    Path2D        path                   = new Path2D.Double();

    /**
     * Create a transparent button painter.
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
        boolean useToolBarFocus = true;

        int x = focusInsets.left;
        int y = focusInsets.top;
        width -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        if (focused) {
            path = decodeInnerFocus(x, y, width, height);
            g.setColor(useToolBarFocus ? innerToolBarFocusColor : innerFocusColor);
            g.draw(path);
            path = decodeOuterFocus(x, y, width, height);
            g.setColor(useToolBarFocus ? outerToolBarFocusColor : outerFocusColor);
            g.draw(path);
        }
    }

    Path2D decodeOuterFocus(int x, int y, int width, int height) {
        double arcSize = 6d;
        x -= 2;
        y -= 2;
        width += 3;
        height += 3;
        decodeFocusPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    Path2D decodeInnerFocus(int x, int y, int width, int height) {
        double arcSize = 5d;
        x -= 1;
        y -= 1;
        width += 1;
        height += 1;
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
