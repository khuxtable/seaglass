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

    private static Path2D       path        = new Path2D.Double();

    private static final double baseArcSize = 4d;

    public static Shape createRectangleNoRightSide(final int x, final int y, final int w, final int h) {
        path.reset();
        path.moveTo(x + w, y);
        path.lineTo(x, y);
        path.lineTo(x, y + h);
        path.lineTo(x + w, y + h);
        return path;
    }

    public static Shape createRectangle(final int x, final int y, final int w, final int h) {
        // The corner size doesn't matter, but must not be null.
        return createQuad(CornerSize.BORDER, x, y, w, h, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE);
    }

    public static Shape createRoundRectangle(final CornerSize size, final int x, final int y, final int w, final int h) {
        return createQuad(size, x, y, w, h, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDED);
    }

    public static Shape createQuad(final CornerSize size, final int x, final int y, final int w, final int h, final CornerStyle topLeft,
        final CornerStyle bottomLeft, final CornerStyle bottomRight, final CornerStyle topRight) {
        int right = x + w;
        int bottom = y + h;
        double arcSize = size.arcSize;

        // Start the path.
        path.reset();
        // Move to top left and draw rounded corner if requested.
        switch (topLeft) {
        case SQUARE:
            path.moveTo(x, y);
            break;
        case ROUNDED:
            path.moveTo(x + arcSize, y);
            path.quadTo(x, y, x, y + arcSize);
            break;
        }
        // Draw through bottom left corner.
        switch (bottomLeft) {
        case SQUARE:
            path.lineTo(x, bottom);
            break;
        case ROUNDED:
            path.lineTo(x, bottom - arcSize);
            path.quadTo(x, bottom, x + arcSize, bottom);
            break;
        }
        // Draw through bottom right corner.
        switch (bottomRight) {
        case SQUARE:
            path.lineTo(right, bottom);
            break;
        case ROUNDED:
            path.lineTo(right - arcSize, bottom);
            path.quadTo(right, bottom, right, bottom - arcSize);
        }
        // Draw through top right corner.
        switch (topRight) {
        case SQUARE:
            path.lineTo(right, y);
            break;
        case ROUNDED:
            path.lineTo(right, y + arcSize);
            path.quadTo(right, y, right - arcSize, y);
            break;
        }
        // Close the path.
        path.closePath();
        return path;
    }

    public static Shape createCheckMark(final int x, final int y, final int w, final int h) {
        double xf = w / 12.0;
        double hf = h / 12.0;

        path.reset();
        path.moveTo(x, y + 7.0 * hf);
        path.lineTo(x + 2.0 * xf, y + 7.0 * hf);
        path.lineTo(x + 4.75 * xf, y + 10.0 * hf);
        path.lineTo(x + 9.0 * xf, y);
        path.lineTo(x + 11.0 * xf, y);
        path.lineTo(x + 5.0 * xf, y + 12.0 * hf);
        path.closePath();
        return path;
    }

    public static Shape createArrowLeft(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x + w, y);
        path.lineTo(x, y + h / 2.0);
        path.lineTo(x + w, y + h);
        path.closePath();
        return path;
    }

    public static Shape createArrowRight(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x + w, y + h / 2);
        path.lineTo(x, y + h);
        path.closePath();
        return path;
    }

    public static Shape createArrowUp(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y + h);
        path.lineTo(x + w / 2, y);
        path.lineTo(x + w, y + h);
        path.closePath();
        return path;
    }

    public static Shape createArrowDown(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x + w / 2, y + h);
        path.lineTo(x + w, y);
        path.closePath();
        return path;
    }
}
