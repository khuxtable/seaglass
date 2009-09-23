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
 * $Id: SeaGlassUtils.java 42 2009-09-22 22:20:49Z kathryn@kathrynhuxtable.org $
 */
package com.seaglass.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

/**
 * @author Ken Orr
 */
public class WindowDragger {

    private Window             fWindow;

    private Component          fComponent;

    private MouseListener      mouseListener;
    private MouseMotionAdapter mouseMotionListener;

    private int                dX;

    private int                dY;

    public WindowDragger(Window window, Component component) {

        fWindow = window;
        fComponent = component;

        mouseListener = createMouseListener();
        mouseMotionListener = createMouseMotionListener();

        fComponent.addMouseListener(mouseListener);
        fComponent.addMouseMotionListener(mouseMotionListener);
    }

    public void uninstallDragger() {
        fComponent.removeMouseListener(mouseListener);
        fComponent.removeMouseMotionListener(mouseMotionListener);
    }

    private MouseListener createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = new Point(e.getPoint());
                SwingUtilities.convertPointToScreen(clickPoint, fComponent);

                dX = clickPoint.x - fWindow.getX();
                dY = clickPoint.y - fWindow.getY();
            }
        };
    }

    private MouseMotionAdapter createMouseMotionListener() {
        return new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point dragPoint = new Point(e.getPoint());
                SwingUtilities.convertPointToScreen(dragPoint, fComponent);

                fWindow.setLocation(dragPoint.x - dX, dragPoint.y - dY);
            }
        };
    }
}
