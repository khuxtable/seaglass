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

        public int getThickness(Rectangle bounds) {
            return bounds.height;
        }

        public int getLength(Dimension size) {
            return size.width;
        }

        public int getLength(Rectangle bounds) {
            return bounds.width;
        }

        public int getPosition(Point point) {
            return point.x;
        }

        public int getPosition(Rectangle bounds) {
            return bounds.x;
        }

        public int getPosition(int x, int y) {
            return x;
        }

        public int getOrthogonalOffset(Point point) {
            return point.y;
        }

        public int getOrthogonalOffset(Rectangle bounds) {
            return bounds.y;
        }

        public int getOrthogonalOffset(int x, int y) {
            return y;
        }

        public Rectangle updateBoundsPosition(Rectangle bounds, int newPosition) {
            bounds.setLocation(newPosition, bounds.y);
            return bounds;
        }

        public Rectangle updateBoundsOrthogonalOffset(Rectangle bounds, int newOrthogonalOffset) {
            bounds.setLocation(bounds.x, newOrthogonalOffset);
            return bounds;
        }

        public Rectangle updateBoundsLength(Rectangle bounds, int newLength) {
            bounds.setSize(newLength, bounds.height);
            return bounds;
        }

        public Rectangle updateBoundsThickness(Rectangle bounds, int newThickness) {
            bounds.setSize(bounds.width, newThickness);
            return bounds;
        }

        public Rectangle createBounds(Component container, int position, int length) {
            return new Rectangle(position, 0, length, container.getHeight());
        }

        public Rectangle createCenteredBounds(Component container, int position, int length, int thickness) {
            int y = container.getHeight() / 2 - thickness / 2;

            return new Rectangle(position, y, length, thickness);
        }

        public Rectangle createBounds(int position, int orthogonalOffset, int length, int thickness) {
            return new Rectangle(position, orthogonalOffset, length, thickness);
        }
    },

    VERTICAL {

        public int getThickness(Dimension size) {
            return size.width;
        }

        public int getThickness(Rectangle bounds) {
            return bounds.width;
        }

        public int getLength(Dimension size) {
            return size.height;
        }

        public int getLength(Rectangle bounds) {
            return bounds.height;
        }

        public int getPosition(Point point) {
            return point.y;
        }

        public int getPosition(Rectangle bounds) {
            return bounds.y;
        }

        public int getPosition(int x, int y) {
            return y;
        }

        public int getOrthogonalOffset(Point point) {
            return point.x;
        }

        public int getOrthogonalOffset(Rectangle bounds) {
            return bounds.x;
        }

        public int getOrthogonalOffset(int x, int y) {
            return x;
        }

        public Rectangle updateBoundsPosition(Rectangle bounds, int newPosition) {
            bounds.setLocation(bounds.x, newPosition);
            return bounds;
        }

        public Rectangle updateBoundsOrthogonalOffset(Rectangle bounds, int newOrthogonalOffset) {
            bounds.setLocation(newOrthogonalOffset, bounds.y);
            return bounds;
        }

        public Rectangle updateBoundsLength(Rectangle bounds, int newLength) {
            bounds.setSize(bounds.width, newLength);
            return bounds;
        }

        public Rectangle updateBoundsThickness(Rectangle bounds, int newThickness) {
            bounds.setSize(newThickness, bounds.height);
            return bounds;
        }

        public Rectangle createBounds(Component container, int position, int length) {
            return new Rectangle(0, position, container.getWidth(), length);
        }

        public Rectangle createCenteredBounds(Component container, int position, int length, int thickness) {
            int x = container.getWidth() / 2 - thickness / 2;

            return new Rectangle(x, position, thickness, length);
        }

        public Rectangle createBounds(int position, int orthogonalOffset, int length, int thickness) {
            return new Rectangle(orthogonalOffset, position, thickness, length);
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
     * @param  size the 2-dimensional size from which to extract the thickness.
     *
     * @return the thickness of the given size.
     */
    public abstract int getThickness(Dimension size);

    /**
     * Gets the thickness of the given rectangle. Thickness corresponds to the
     * dimension that is orthogonal to the main direction of the component. That
     * is, a horizontal scroll bar's thickness corresponds to the y dimension,
     * while a vertical scroll bar's thickness corresponds to the x dimension.
     *
     * @param  bounds the Rectangle from which to extract the thickness.
     *
     * @return the thickness of the given size.
     */
    public abstract int getThickness(Rectangle bounds);

    /**
     * Gets the length of the given size. Length corresponds to the dimension
     * that is in line with the main direction of the component. That is, a
     * horizontal scroll bar's length corresponds to the x dimension, while a
     * vertical scroll bar's length corresponds to the y dimension.
     *
     * @param  size the 2-dimensional size from which to extract the length.
     *
     * @return the length of the given size.
     */
    public abstract int getLength(Dimension size);

    /**
     * Gets the length of the given rectangle. Length corresponds to the
     * dimension that is in line with the main direction of the component. That
     * is, a horizontal scroll bar's length corresponds to the x dimension,
     * while a vertical scroll bar's length corresponds to the y dimension.
     *
     * @param  bounds the Rectangle from which to extract the length.
     *
     * @return the length of the given size.
     */
    public abstract int getLength(Rectangle bounds);

    /**
     * Gets the position from the given {@link Point}. Position refers to the
     * dimension of a point in line with the main direction of the component.
     * That is, a horiztonal scroll bar's position corresponds to the x
     * dimension, while a vertical scroll bar's position corresponds to the y
     * dimension.
     *
     * @param  point the {@code Point} from which to extract the position.
     *
     * @return the position value of the given {@code Point}.
     */
    public abstract int getPosition(Point point);

    /**
     * Gets the position from the given {@link Rectangle}. Position refers to
     * the dimension of a point in line with the main direction of the
     * component. That is, a horiztonal scroll bar's position corresponds to the
     * x dimension, while a vertical scroll bar's position corresponds to the y
     * dimension.
     *
     * @param  bounds the {@code Rectangle} from which to extract the position.
     *
     * @return the position value of the given {@code Point}.
     */
    public abstract int getPosition(Rectangle bounds);

    /**
     * Gets the position value from the given x and y values. Position refers to
     * the coordinate which is in line with the main direction of the component.
     * That is, a horiztonal scroll bar's value corresponds to the x value,
     * while a vertical scroll bar's value corresponds to the y value.
     *
     * @param  x the x value from which to extract the position.
     * @param  y the y value from which to extract the position.
     *
     * @return the position value of the given x and y values.
     */
    public abstract int getPosition(int x, int y);

    /**
     * Gets the orthogonal offset from the given {@link Point}. Orthogonal
     * offset refers to the dimension of a point orthogonal to the main
     * direction of the component. That is, a horiztonal scroll bar's position
     * corresponds to the x dimension, while a vertical scroll bar's position
     * corresponds to the y dimension.
     *
     * @param  point the {@code Point} from which to extract the offset.
     *
     * @return the orthogonal offset value of the given {@code Point}.
     */
    public abstract int getOrthogonalOffset(Point point);

    /**
     * Gets the orthogonal offset from the given {@link Rectangle}. Orthogonal
     * offset refers to the dimension of a point orthogonal to the main
     * direction of the component. That is, a horiztonal scroll bar's position
     * corresponds to the x dimension, while a vertical scroll bar's position
     * corresponds to the y dimension.
     *
     * @param  bounds the {@code Rectangle} from which to extract the offset.
     *
     * @return the orthogonal offset value of the given {@code Point}.
     */
    public abstract int getOrthogonalOffset(Rectangle bounds);

    /**
     * Gets the orthogonal offset from the given x and y positions. Orthogonal
     * offset refers to the dimension of a point orthogonal to the main
     * direction of the component. That is, a horiztonal scroll bar's position
     * corresponds to the x dimension, while a vertical scroll bar's position
     * corresponds to the y dimension.
     *
     * @param  x the x value from which to extract the offset.
     * @param  y the y value from which to extract the offset.
     *
     * @return the orthogonal offset value of the given x and y values.
     */
    public abstract int getOrthogonalOffset(int x, int y);

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
     * Moves the given bounds to the given orthogonal offset. For a horizontal
     * scroll bar this translates into {@code bounds.y = newOrthogonalOffset},
     * while for a vertical scroll bar this translates into
     * {@code bounds.x = newOrthogonalOffset}.
     *
     * @param  bounds              the bounds to update with the new orthogonal
     *                             offset.
     * @param  newOrthogonalOffset the new orthogonal offset to set the bounds
     *                             to.
     *
     * @return the updated bounds.
     */
    public abstract Rectangle updateBoundsOrthogonalOffset(Rectangle bounds, int newOrthogonalOffset);

    /**
     * Resize the given bounds to the given length. For a horizontal scroll bar
     * this translates into {@code bounds.width = newLength}, while for a
     * vertical scroll bar this translates into
     * {@code bounds.height = newLength}.
     *
     * @param  bounds    the bounds to update with the new length.
     * @param  newLength the new length to set the bounds to.
     *
     * @return the updated bounds.
     */
    public abstract Rectangle updateBoundsLength(Rectangle bounds, int newLength);

    /**
     * Resize the given bounds to the given thickness. For a horizontal scroll
     * bar this translates into {@code bounds.height = newThickness}, while for
     * a vertical scroll bar this translates into
     * {@code bounds.width = newThickness}.
     *
     * @param  bounds       the bounds to update with the new thickness.
     * @param  newThickness the new thickness to set the bounds to.
     *
     * @return the updated bounds.
     */
    public abstract Rectangle updateBoundsThickness(Rectangle bounds, int newThickness);

    /**
     * Creates bounds based on the given {@link Component}, position and length.
     * The supplied component will be used to determine the thickness of the
     * bounds. The position will be used to locate the bounds along the
     * scrollable axis. The length will be used to determine the length of the
     * bounds along the scrollable axis.
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
     * Creates bounds based on the position, orthogonal offset, length, and
     * thickness.
     *
     * @param  position         the new position.
     * @param  orthogonalOffset the new orthogonal offset.
     * @param  length           the new length.
     * @param  thickness        the new thickness.
     *
     * @return the new bounds Rectangle.
     */
    public abstract Rectangle createBounds(int position, int orthogonalOffset, int length, int thickness);

    /**
     * Creates bounds centered in the given {@link Component} located at the
     * given position, with the given thickness and length.
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
     * @param  length    the length of the bounds.
     * @param  thickness the thickness of the given bounds.
     *
     * @return the created bounds.
     */
    public abstract Rectangle createCenteredBounds(Component container, int position, int length, int thickness);
}
