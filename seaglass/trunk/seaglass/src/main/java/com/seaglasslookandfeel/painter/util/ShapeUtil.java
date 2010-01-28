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
import java.awt.geom.Ellipse2D;
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
        ROUND_HEIGHT(0),
        ROUND_HEIGHT_DRAW(0),
        ROUND_WIDTH(0),
        ROUND_WIDTH_DRAW(0),

        INTERIOR(baseArcSize - 1),
        BORDER(baseArcSize),
        INNER_FOCUS(baseArcSize + 1),
        OUTER_FOCUS(baseArcSize + 2),

        SLIDER_INTERIOR(baseArcSize - 2),
        SLIDER_BORDER(baseArcSize - 1),
        SLIDER_INNER_FOCUS(baseArcSize),
        SLIDER_OUTER_FOCUS(baseArcSize + 1),

        CHECKBOX_INTERIOR(baseArcSize / 2),
        CHECKBOX_BORDER((baseArcSize + 1) / 2),
        CHECKBOX_INNER_FOCUS((baseArcSize + 2) / 2),
        CHECKBOX_OUTER_FOCUS((baseArcSize + 3) / 2),

        POPUP_BORDER(3),
        POPUP_INTERIOR(2.5);

        private double radius;

        CornerSize(double radius) {
            this.radius = radius;
        }

        public double getRadius(int w, int h) {
            switch (this) {
            case ROUND_HEIGHT:
                return h / 2.0;
            case ROUND_HEIGHT_DRAW:
                return (h + 1) / 2.0;
            case ROUND_WIDTH:
                return w / 2.0;
            case ROUND_WIDTH_DRAW:
                return (w + 1) / 2.0;
            default:
                return radius;
            }
        }
    }

    private static Path2D       path        = new Path2D.Double(Path2D.WIND_EVEN_ODD);
    private static Ellipse2D    ellipse     = new Ellipse2D.Float();

    private static final double baseArcSize = 4d;

    /**
     * Return a path for a rectangle with square corners.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the rectangle
     * @param y
     *            the Y coordinate of the upper-left corner of the rectangle
     * @param w
     *            the width of the rectangle
     * @param h
     *            the height of the rectangle
     * 
     * @return a path representing the shape.
     */
    public static Shape createRectangle(final int x, final int y, final int w, final int h) {
        return createRoundRectangleInternal(x, y, w, h, 0, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE);
    }

    /**
     * Return a path for a rectangle with rounded corners.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the rectangle
     * @param y
     *            the Y coordinate of the upper-left corner of the rectangle
     * @param w
     *            the width of the rectangle
     * @param h
     *            the height of the rectangle
     * @param size
     *            the CornerSize value representing the amount of rounding
     * 
     * @return a path representing the shape.
     */
    public static Shape createRoundRectangle(final int x, final int y, final int w, final int h, final CornerSize size) {
        return createRoundRectangle(x, y, w, h, size, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDED);
    }

    /**
     * Return a path for a rectangle with rounded corners.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the rectangle
     * @param y
     *            the Y coordinate of the upper-left corner of the rectangle
     * @param w
     *            the width of the rectangle
     * @param h
     *            the height of the rectangle
     * @param size
     *            the CornerSize value representing the amount of rounding
     * @param topLeft
     *            the CornerStyle of the upper-left corner. This must be one of
     *            {@code CornerStyle.SQUARE} or {@code CornerStyle.ROUNDED}.
     * @param bottomLeft
     *            the CornerStyle of the lower-left corner. This must be one of
     *            {@code CornerStyle.SQUARE} or {@code CornerStyle.ROUNDED}.
     * @param bottomRight
     *            the CornerStyle of the lower-right corner. This must be one of
     *            {@code CornerStyle.SQUARE} or {@code CornerStyle.ROUNDED}.
     * @param topRight
     *            the CornerStyle of the upper-right corner. This must be one of
     *            {@code CornerStyle.SQUARE} or {@code CornerStyle.ROUNDED}.
     * 
     * @return a path representing the shape.
     */
    public static Shape createRoundRectangle(final int x, final int y, final int w, final int h, final CornerSize size,
        final CornerStyle topLeft, final CornerStyle bottomLeft, final CornerStyle bottomRight, final CornerStyle topRight) {
        return createRoundRectangleInternal(x, y, w, h, size.getRadius(w, h), topLeft, bottomLeft, bottomRight, topRight);
    }

    /**
     * Return a path for a rectangle with square corners and no right side. This
     * is used for text fields that are part of a larger control, which is
     * placed to their left, e.g. spinners and editable combo boxes.
     * <p>
     * This path is suitable for drawing, not for filling.
     * </p>
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the rectangle
     * @param y
     *            the Y coordinate of the upper-left corner of the rectangle
     * @param w
     *            the width of the rectangle
     * @param h
     *            the height of the rectangle
     * 
     * @return a path representing the shape.
     */
    public static Shape createOpenRectangle(final int x, final int y, final int w, final int h) {
        path.reset();
        path.moveTo(x + w, y);
        path.lineTo(x, y);
        path.lineTo(x, y + h);
        path.lineTo(x + w, y + h);
        return path;
    }

    /**
     * Return a path for a check mark.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the check mark
     * @param y
     *            the Y coordinate of the upper-left corner of the check mark
     * @param w
     *            the width of the check mark
     * @param h
     *            the height of the check mark
     * 
     * @return a path representing the shape.
     */
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

    /**
     * Return a path for an arrow pointing to the left.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the arrow
     * @param y
     *            the Y coordinate of the upper-left corner of the arrow
     * @param w
     *            the width of the arrow
     * @param h
     *            the height of the arrow
     * 
     * @return a path representing the shape.
     */
    public static Shape createArrowLeft(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x + w, y);
        path.lineTo(x, y + h / 2.0);
        path.lineTo(x + w, y + h);
        path.closePath();
        return path;
    }

    /**
     * Return a path for an arrow pointing to the right.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the arrow
     * @param y
     *            the Y coordinate of the upper-left corner of the arrow
     * @param w
     *            the width of the arrow
     * @param h
     *            the height of the arrow
     * 
     * @return a path representing the shape.
     */
    public static Shape createArrowRight(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x + w, y + h / 2);
        path.lineTo(x, y + h);
        path.closePath();
        return path;
    }

    /**
     * Return a path for an arrow pointing up.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the arrow
     * @param y
     *            the Y coordinate of the upper-left corner of the arrow
     * @param w
     *            the width of the arrow
     * @param h
     *            the height of the arrow
     * 
     * @return a path representing the shape.
     */
    public static Shape createArrowUp(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y + h);
        path.lineTo(x + w / 2, y);
        path.lineTo(x + w, y + h);
        path.closePath();
        return path;
    }

    /**
     * Return a path for an arrow pointing down.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the arrow
     * @param y
     *            the Y coordinate of the upper-left corner of the arrow
     * @param w
     *            the width of the arrow
     * @param h
     *            the height of the arrow
     * 
     * @return a path representing the shape.
     */
    public static Shape createArrowDown(final double x, final double y, final double w, final double h) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x + w / 2, y + h);
        path.lineTo(x + w, y);
        path.closePath();
        return path;
    }

    /**
     * Return a path for the patterned portion of an indeterminate progress bar.
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the region
     * @param y
     *            the Y coordinate of the upper-left corner of the region
     * @param w
     *            the width of the region
     * @param h
     *            the height of the region
     * 
     * @return a path representing the shape.
     */
    public static Shape createProgressBarIndeterminatePattern(int x, int y, int width, int height) {
        final double wHalf = width / 2.0;
        final double xOffset = wHalf / 4.0;

        path.reset();
        path.moveTo(xOffset, 0);
        path.lineTo(wHalf + xOffset, 0);
        path.lineTo(width, height);
        path.lineTo(wHalf, height);
        path.closePath();

        return path;
    }

    public static Shape createRoundedInternalShadow(int x, int y, int width, int height) {
        double halfHeight = height / 2.0;

        double top = y;
        double left = x;
        double right = left + width;

        double midLine = top + halfHeight;

        double leftCurve = left + halfHeight;
        double rightCurve = right - halfHeight;

        path.reset();
        path.moveTo(left, midLine);
        path.quadTo(left, top, leftCurve, top);
        path.lineTo(rightCurve, top);
        path.quadTo(right, top, right, midLine);
        path.closePath();
        return path;
    }

    public static Shape createRoundedInternalDropShadowDark(int width, int height) {
        path.reset();
        path.moveTo(4, (height - 5) / 2 - 1);
        path.quadTo(5, 3, 8, 3);
        path.lineTo(width - 11, 3);
        path.quadTo(width - 5, 3, width - 5, (height - 5) / 2 - 1);
        return path;
    }

    public static Shape createRoundedInternalDropShadowLight(int width, int height) {
        path.reset();
        path.moveTo(4, (height - 5) / 2);
        path.quadTo(5, 4, 8, 4);
        path.lineTo(width - 11, 4);
        path.quadTo(width - 5, 4, width - 5, (height - 5) / 2);
        return path;
    }

    /**
     * Return a path for a focus rectangle.
     * <p>
     * This path is suitable for filling.
     * </p>
     * 
     * @param x
     *            the X coordinate of the upper-left corner of the rectangle
     * @param y
     *            the Y coordinate of the upper-left corner of the rectangle
     * @param w
     *            the width of the rectangle
     * @param h
     *            the height of the rectangle
     * 
     * @return a path representing the shape.
     */
    public static Shape createFillableFocusRectangle(int x, int y, int width, int height) {
        final int left = x;
        final int top = y;
        final int right = x + width;
        final int bottom = y + height;

        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);

        final float left2 = left - 1.4f;
        final float top2 = top - 1.4f;
        final float right2 = right + 1.4f;
        final float bottom2 = bottom + 1.4f;

        // TODO These two lines were curveTo in Nimbus. Perhaps we should
        // revisit this?
        path.lineTo(right2, top);
        path.lineTo(right2, bottom2);
        path.lineTo(left2, bottom2);
        path.lineTo(left2, top2);
        path.lineTo(right2, top2);
        path.lineTo(right2, top);
        path.closePath();

        return path;
    }

    public static Shape createBullet(int x, int y, int diameter) {
        return createEllipseInternal(x, y, diameter, diameter);
    }

    public static Shape createRadioButton(int x, int y, int diameter) {
        return createEllipseInternal(x, y, diameter, diameter);
    }

    public static Shape createSliderThumbContinuous(final int x, final int y, final int diameter) {
        return createEllipseInternal(x, y, diameter, diameter);
    }

    public static Shape createSliderThumbDiscrete(final int x, final int y, final int w, final int h, final CornerSize size) {
        final double topArc = size.getRadius(w, h);
        final double bottomArcH = size == CornerSize.INTERIOR ? 0 : 1;
        final double bottomArcW = 3;

        path.reset();
        path.moveTo(x, y + topArc);
        path.quadTo(x, y, x + topArc, y);
        path.lineTo(x + w - topArc, y);
        path.quadTo(x + w, y, x + w, y + topArc);
        path.lineTo(x + w, y + h / 2.0);
        path.quadTo(x + w - bottomArcW, y + h - bottomArcH, x + w / 2.0, y + h);
        path.quadTo(x + bottomArcW, y + h - bottomArcH, x, y + h / 2.0);
        path.closePath();
        return path;
    }

    public static Shape createCancelIcon(int x, int y, int width, int height) {
        final double xMid = x + width / 2.0;
        final double yMid = y + height / 2.0;

        // Draw the circle.
        path.reset();
        path.moveTo(xMid, y);
        path.quadTo(x, y, x, yMid);
        path.quadTo(x, y + height, xMid, y + height);
        path.quadTo(x + width, y + height, x + width, yMid);
        path.quadTo(x + width, y, xMid, y);
        path.closePath();

        final double xOffsetL = width / 2.0 - 3;
        final double xOffsetS = width / 2.0 - 4;
        final double yOffsetL = height / 2.0 - 3;
        final double yOffsetS = height / 2.0 - 4;
        final double offsetC = 1.5;

        // Erase the "x" with an inner subpath.
        path.moveTo(xMid, yMid - offsetC);
        path.lineTo(xMid + xOffsetS, yMid - yOffsetL);
        path.lineTo(yMid + xOffsetL, yMid - yOffsetS);
        path.lineTo(xMid + offsetC, yMid);
        path.lineTo(xMid + xOffsetL, yMid + yOffsetS);
        path.lineTo(xMid + xOffsetS, yMid + yOffsetL);
        path.lineTo(xMid, yMid + offsetC);
        path.lineTo(xMid - xOffsetS, yMid + yOffsetL);
        path.lineTo(xMid - xOffsetL, yMid + yOffsetS);
        path.lineTo(xMid - offsetC, yMid);
        path.lineTo(xMid - xOffsetL, yMid - yOffsetS);
        path.lineTo(xMid - xOffsetS, yMid - yOffsetL);
        path.closePath();

        return path;
    }

    public static Shape createScrollCap(int x, int y, int width, int height) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x, y + height);
        path.lineTo(x + width, y + height);
        addScrollGapPath(x, y, width, height);
        path.closePath();
        return path;
    }

    public static Shape createScrollButtonApart(int x, int y, int width, int height) {
        path.reset();
        path.moveTo(x, y);
        path.lineTo(x, y + height);
        path.lineTo(x + width, y + height);
        addScrollGapPath(x, y, width, height);
        path.closePath();
        return path;
    }

    public static Shape createScrollButtonTogetherDecrease(int x, int y, int width, int height) {
        path.reset();
        path.moveTo(x + width, y);
        path.lineTo(x + width, y + height);
        path.lineTo(x, y + height);
        addScrollGapPathBackwards(x, y, height);
        path.closePath();
        return path;
    }

    private static void addScrollGapPath(int x, int y, int width, int height) {
        path.quadTo(x + width - height / 2.0, y + height, x + width - height / 2.0, y + height / 2.0);
        path.quadTo(x + width - height / 2.0, y, x + width, y);
    }

    private static void addScrollGapPathBackwards(int x, int y, int height) {
        path.quadTo(x + height / 2.0, y + height, x + height / 2.0, y + height / 2.0);
        path.quadTo(x + height / 2.0, y, x, y);
    }

    private static Shape createEllipseInternal(int x, int y, int w, int h) {
        ellipse.setFrame(x, y, w, h);
        return ellipse;
    }

    private static Shape createRoundRectangleInternal(final int x, final int y, final int w, final int h, final double radius,
        final CornerStyle topLeft, final CornerStyle bottomLeft, final CornerStyle bottomRight, final CornerStyle topRight) {
        // Convenience variables.
        final int left = x;
        final int top = y;
        final int right = x + w;
        final int bottom = y + h;

        // Start the path.
        path.reset();
        // Move to top left and draw rounded corner if requested.
        switch (topLeft) {
        case SQUARE:
            path.moveTo(left, top);
            break;
        case ROUNDED:
            path.moveTo(left + radius, top);
            path.quadTo(left, top, left, top + radius);
            break;
        }
        // Draw through bottom left corner.
        switch (bottomLeft) {
        case SQUARE:
            path.lineTo(left, bottom);
            break;
        case ROUNDED:
            path.lineTo(left, bottom - radius);
            path.quadTo(left, bottom, left + radius, bottom);
            break;
        }
        // Draw through bottom right corner.
        switch (bottomRight) {
        case SQUARE:
            path.lineTo(right, bottom);
            break;
        case ROUNDED:
            path.lineTo(right - radius, bottom);
            path.quadTo(right, bottom, right, bottom - radius);
        }
        // Draw through top right corner.
        switch (topRight) {
        case SQUARE:
            path.lineTo(right, top);
            break;
        case ROUNDED:
            path.lineTo(right, top + radius);
            path.quadTo(right, top, right - radius, top);
            break;
        }
        // Close the path.
        path.closePath();
        return path;
    }
}
