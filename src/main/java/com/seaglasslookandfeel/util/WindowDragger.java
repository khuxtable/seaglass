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
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

/**
 * Window Dragger that can be installed on a toolbar or bottom bar, allowing it
 * to move the whole fram.
 *
 * @author Ken Orr
 */
public class WindowDragger {

    private Window fWindow;

    private Component fComponent;

    private MouseListener      mouseListener;
    private MouseMotionAdapter mouseMotionListener;

    private int dX;
    private int dY;

    /**
     * Creates a new WindowDragger object.
     *
     * @param window    the window to drag.
     * @param component the component to drag with.
     */
    public WindowDragger(Window window, Component component) {
        fWindow    = window;
        fComponent = component;

        mouseListener       = createMouseListener();
        mouseMotionListener = createMouseMotionListener();

        fComponent.addMouseListener(mouseListener);
        fComponent.addMouseMotionListener(mouseMotionListener);
    }

    /**
     * Uninstall the dragger.
     */
    public void uninstallDragger() {
        fComponent.removeMouseListener(mouseListener);
        fComponent.removeMouseMotionListener(mouseMotionListener);
    }

    /**
     * Create the mouse listener.
     *
     * @return the mouse listener.
     */
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

    /**
     * Create the mouse motion listener.
     *
     * @return the mouse motion listener.
     */
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
