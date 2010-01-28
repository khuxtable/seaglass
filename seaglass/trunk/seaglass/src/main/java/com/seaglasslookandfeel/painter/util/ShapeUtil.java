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
package com.seaglasslookandfeel.painter.util;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Return various shapes used by the Painter classes.
 * 
 * @author Kathryn Huxtable
 */
public class ShapeUtil {

    public enum CornerStyle {
        SQUARE, ROUNDED,
    };

    public enum CornerSize {
        INTERIOR(baseArcSize - 1),
        BORDER(baseArcSize),
        INNER_FOCUS(baseArcSize + 1),
        OUTER_FOCUS(baseArcSize + 2),

        CHECKBOX_INTERIOR(baseArcSize / 2),
        CHECKBOX_BORDER((baseArcSize + 1) / 2),
        CHECKBOX_INNER_FOCUS((baseArcSize + 2) / 2),
        CHECKBOX_OUTER_FOCUS((baseArcSize + 3) / 2);

        public double arcSize;

        CornerSize(double arcSize) {
            this.arcSize = arcSize;
        }
    }

    private static Path2D           path        = new Path2D.Double();
    private static Rectangle2D      rect        = new Rectangle2D.Double();
    private static RoundRectangle2D roundRect   = new RoundRectangle2D.Double();

    private static final double     baseArcSize = 4d;

    public static Shape createRectangle(final int x, final int y, final int w, final int h) {
        rect.setRect(x, y, w, h);
        return rect;
    }

    public static Shape createRoundRectangle(final CornerSize size, final int x, final int y, final int w, final int h) {
        double arcSize = 2 * size.arcSize;
        roundRect.setRoundRect(x, y, w, h, arcSize, arcSize);
        return roundRect;
    }

    public static Shape createQuad(final CornerSize size, final int x, final int y, final int w, final int h, final CornerStyle topLeft,
        final CornerStyle bottomLeft, final CornerStyle bottomRight, final CornerStyle topRight) {
        if (topLeft == CornerStyle.SQUARE && bottomLeft == CornerStyle.SQUARE && bottomRight == CornerStyle.SQUARE
                && topRight == CornerStyle.SQUARE) {
            return createRectangle(x, y, w, h);
        } else if (topLeft == CornerStyle.ROUNDED && bottomLeft == CornerStyle.ROUNDED && bottomRight == CornerStyle.ROUNDED
                && topRight == CornerStyle.ROUNDED) {
            return createRoundRectangle(size, x, y, w, h);
        } else {
            double arcSize = size.arcSize;
            path.reset();
            switch (topLeft) {
            case SQUARE:
                path.moveTo(x, y);
                break;
            case ROUNDED:
                path.moveTo(x + arcSize, y);
                path.quadTo(x, y, x, y + arcSize);
                break;
            }
            switch (bottomLeft) {
            case SQUARE:
                path.lineTo(x, y + h);
                break;
            case ROUNDED:
                path.lineTo(x, y + h - arcSize);
                path.quadTo(x, y + h, x + arcSize, y + h);
                break;
            }
            switch (bottomRight) {
            case SQUARE:
                path.lineTo(x + w, y + h);
                break;
            case ROUNDED:
                path.lineTo(x + w - arcSize, y + h);
                path.quadTo(x + w, y + h, x + w, y + h - arcSize);
            }
            switch (topRight) {
            case SQUARE:
                path.lineTo(x + w, y);
                break;
            case ROUNDED:
                path.lineTo(x + w, y + arcSize);
                path.quadTo(x + w, y, x + w - arcSize, y);
                break;
            }
            path.closePath();
            return path;
        }
    }

    public static Shape createCheckMark(final int x, final int y, final int width, final int height) {
        double widthMult = width / 12.0;
        double heightMult = height / 12.0;
        path.reset();
        path.moveTo(x, y + 7.0 * heightMult);
        path.lineTo(x + 2.0 * widthMult, y + 7.0 * heightMult);
        path.lineTo(x + 4.75 * widthMult, y + 10.0 * heightMult);
        path.lineTo(x + 9.0 * widthMult, y);
        path.lineTo(x + 11.0 * widthMult, y);
        path.lineTo(x + 5.0 * widthMult, y + 12.0 * heightMult);
        path.closePath();
        return path;
    }
}
