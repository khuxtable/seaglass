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

import java.awt.Graphics2D;

/**
 * An interface that allows painting to be delegated. The implementation of this
 * interface will be called during the painting process of the given {@code
 * objectToPaint}.
 */
public interface Painter<T> {

    /**
     * Renders to the given {@link Graphics2D}. The supplied graphics context
     * may be modified - it's state need not be restored upon completion of
     * painting.
     * 
     * @param graphics
     *            the graphics context to paint into. It's state need not be
     *            restored. Will not be null.
     * @param objectToPaint
     *            the object to be painted.
     * @param width
     *            the width within the object to paint.
     * @param height
     *            the height within the object to paint.
     */
    void paint(Graphics2D graphics, T objectToPaint, int width, int height);

}
