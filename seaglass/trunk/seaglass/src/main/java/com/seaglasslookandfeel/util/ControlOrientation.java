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
package com.seaglasslookandfeel.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.SwingConstants;

/**
 * An orientation corresponding to a {@link javax.swing.JScrollBar} or
 * {@link javax.swing.JTabbedPane}. The methods in this enumeration allow for
 * orentation-agnostic calculations, which can be used when laying out a scroll
 * bar or tabbed pane tab area. A scroll bar, regardless of its orientation, has
 * a length and thickness, as does the tab area of a tabbed pane. These values
 * correspond to different dimensions (x or y) depending on the orientation.
 *
 * @author Ken Orr
 * @author Kathryn Huxtable
 */
public enum ControlOrientation {

    HORIZONTAL {

        public int getThickness(Dimension size) {
            return size.height;
        }

        public int getLength(Dimension size) {
            return size.width;
        }

        public int getPosition(Point point) {
            return point.x;
        }

        public int getOrthogonalOffset(Point point) {
            return point.y;
        }

        public Rectangle updateBoundsPosition(Rectangle bounds, int newPosition) {
            bounds.setLocation(newPosition, bounds.y);
            return bounds;
        }

        public Rectangle createBounds(Component container, int position, int length) {
            return new Rectangle(position, 0, length, container.getHeight());
        }

        public Rectangle createCenteredBounds(Component container, int position, int thickness, int length) {
            int y = container.getHeight() / 2 - thickness / 2;

            return new Rectangle(position, y, length, thickness);
        }
    },

    VERTICAL {

        public int getThickness(Dimension size) {
            return size.width;
        }

        public int getLength(Dimension size) {
            return size.height;
        }

        public int getPosition(Point point) {
            return point.y;
        }

        public int getOrthogonalOffset(Point point) {
            return point.x;
        }

        public Rectangle updateBoundsPosition(Rectangle bounds, int newPosition) {
            bounds.setLocation(bounds.x, newPosition);
            return bounds;
        }

        public Rectangle createBounds(Component container, int position, int length) {
            return new Rectangle(0, position, container.getWidth(), length);
        }

        public Rectangle createCenteredBounds(Component container, int position, int thickness, int length) {
            int x = container.getWidth() / 2 - thickness / 2;

            return new Rectangle(x, position, thickness, length);
        }
    };

    /**
     * Converts a Swing orientation (either {@link SwingConstants#HORIZONTAL} or
     * {@link SwingConstants#VERTICAL} to a {@code ControlOrientation}.
     *
     * @param  swingScrollBarOrientation the Swing scroll bar orientation,
     *                                   either
     *                                   {@link SwingConstants#HORIZONTAL} or
     *                                   {@link SwingConstants#VERTICAL}
     *
     * @return the {@code ControlOrientation} to the corresponding Swing scroll
     *         bar orientation.
     */
    public static ControlOrientation getOrientation(int swingScrollBarOrientation) {
        if (swingScrollBarOrientation != SwingConstants.HORIZONTAL && swingScrollBarOrientation != SwingConstants.VERTICAL) {
            throw new IllegalArgumentException("The given value is not a valid orientation for ControlOrientation.");
        }

        return swingScrollBarOrientation == SwingConstants.HORIZONTAL ? HORIZONTAL : VERTICAL;
    }

    /**
     * Gets the thickness of the given size. Thickness corresponds to the
     * dimension that is orthogonal to the main direction of the component. That
     * is, a horizontal scroll bar's thickness corresponds to the y dimension,
     * while a vertical scroll bar's thickness corresponds to the x dimension.
     *
     * @param  size the 2-dimensional size to extract the thickness from.
     *
     * @return the thickness of the given size.
     */
    public abstract int getThickness(Dimension size);

    /**
     * Gets the length of the given size. Length corresponds to the dimension
     * that is in line with the main direction of the component. That is, a
     * horizontal scroll bar's length corresponds to the x dimension, while a
     * vertical scroll bar's length corresponds to the y dimension.
     *
     * @param  size the 2-dimensional size to extract the length from.
     *
     * @return the length of the given size.
     */
    public abstract int getLength(Dimension size);

    /**
     * Gets the position from the given {@link Point}. Position refers to the
     * dimension of a point in line with the main direction of the component.
     * That is, a horiztonal scroll bar's position corresponds to the x
     * dimension, while a vertical scroll bar's position corresponds to the y
     * dimension.
     *
     * @param  point the {@code Point} from which to extrac the position from.
     *
     * @return the position value of the given {@code Point}.
     */
    public abstract int getPosition(Point point);

    /**
     * Gets the orthogonal offset from the given {@link Point}. Orthogonal
     * offset refers to the dimension of a point orthogonal to the main
     * direction of the component. That is, a horiztonal scroll bar's position
     * corresponds to the x dimension, while a vertical scroll bar's position
     * corresponds to the y dimension.
     *
     * @param  point the {@code Point} from which to extrac the position from.
     *
     * @return the position value of the given {@code Point}.
     */
    public abstract int getOrthogonalOffset(Point point);

    /**
     * Moves the given bounds to the given position. For a horiztonal scroll bar
     * this translates into {@code bounds.x = newPosition}, while for a vertical
     * scroll bar this translates into {@code bounds.y = newPosition}.
     *
     * @param  bounds      the bounds to update with the new position.
     * @param  newPosition the new position to set the bounds to.
     *
     * @return the updated bounds.
     */
    public abstract Rectangle updateBoundsPosition(Rectangle bounds, int newPosition);

    /**
     * <p>Creates bounds based on the given {@link Component}, position and
     * length. The supplied component will be used to determine the thickness of
     * the bounds. The position will be used to locate the bounds along the
     * scrollable axis. The length will be used to determine the length of the
     * bounds along the scrollable axis.</p>
     *
     * <p>Horizontal scroll bars, the bounds will be derived like this:
     *
     * <pre>new Rectangle(position, 0, length, container.getHeigth())</pre>
     * </p>
     *
     * <p>Vertical scroll bar bounds will be derived like this:
     *
     * <pre>new Rectangle(0, container.getWidth(), position, length)</pre>
     * </p>
     *
     * @param  container the {@code Component} to use to determine the thickness
     *                   of the bounds.
     * @param  position  the position of the bounds.
     * @param  length    the length of the bounds.
     *
     * @return the created bounds.
     */
    public abstract Rectangle createBounds(Component container, int position, int length);

    /**
     * <p>Creates bounds centered in the given {@link Component} located at the
     * given position, with the given thickness and length.</p>
     *
     * <p>Horizontal scroll bars, the bounds will be derived like this:
     *
     * <pre>new Rectangle(position, container.getHeight()/2 - thickness/2, length, thickness)</pre>
     * </p>
     *
     * <p>Vertical scroll bars, the bounds will be derived like this:
     *
     * <pre>new Rectangle(container.getWidth()/2 - thickness/2, position, thickness, length)</pre>
     *
     * @param  container the {@code Component} to use to determine the thickness
     *                   of the bounds.
     * @param  position  the position of the bounds.
     * @param  thickness the thickness of the given bounds.
     * @param  length    the length of the bounds.
     *
     * @return the created bounds.
     */
    public abstract Rectangle createCenteredBounds(Component container, int position, int thickness, int length);
}
