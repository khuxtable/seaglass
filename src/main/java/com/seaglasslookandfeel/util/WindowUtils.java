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
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Various utitilies used to manage window drawing.
 *
 * @author Ken Orr
 * @author Kathryn Huxtable
 */
public class WindowUtils {

    /**
     * Tries to make the given {@link Window} non-opqaue (transparent) across
     * platforms and JREs. This method is not guaranteed to succeed, and will
     * fail silently if the given {@code Window} cannot be made non-opaque.
     *
     * <p>This method is useful, for example, when creating a HUD style window
     * that is semi-transparent, and thus doesn't want the window background to
     * be drawn.</p>
     *
     * @param window the {@code Window} to make non-opaque.
     */
    public static void makeWindowNonOpaque(Window window) {
        if (PlatformUtils.isJava6()) {
            // on non-mac platforms, try to use the facilities of Java 6 update 10.
            if (!PlatformUtils.isMac()) {
                quietlyTryToMakeWindowNonOqaque(window);
            } else {
                window.setBackground(UIManager.getColor("seaGlassTransparent"));
            }
        }
    }
    
    /**
     * Sets the shape of a window.
     * This will be done via a com.sun API and may be not available on all platforms.
     * @param window to change the shape for 
     * @param s the new shape for the window.
     */

    public static void setWindowShape(Window window, Shape s) {
        if (PlatformUtils.isJava6()) {
            setWindowShapeJava6(window, s);
        } else {
            setWindowShapeJava7(window, s);
        }
    }

    /**
     * @param window
     * @param s
     */
    private static void setWindowShapeJava7(Window window, Shape s) {
        try {
            Class  clazz  = window.getClass();
            Method method = clazz.getMethod("setShape", Shape.class);
            method.invoke(window, s);
        } catch (Exception e) {
            // silently ignore this exception.
        }
    }
    
    /**
     * @param window
     * @param s
     */
    private static void setWindowShapeJava6(Window window, Shape s) {
        try {
            Class  clazz  = Class.forName("com.sun.awt.AWTUtilities");
            Method method = clazz.getMethod("setWindowShape", java.awt.Window.class, Shape.class);
            method.invoke(clazz, window, s);
        } catch (Exception e) {
            // silently ignore this exception.
        }
    }


    /**
     * Trys to invoke
     * {@code com.sun.awt.AWTUtilities.setWindowOpaque(window,false)} on the
     * given {@link Window}. This will only work when running with JRE 6 update
     * 10 or higher. This method will silently fail if the method cannot be
     * invoked.
     *
     * @param window the {@code Window} to try and make non-opaque.
     */
    @SuppressWarnings("unchecked")
    private static void quietlyTryToMakeWindowNonOqaque(Window window) {
        try {
            Class  clazz  = Class.forName("com.sun.awt.AWTUtilities");
            Method method = clazz.getMethod("setWindowOpaque", java.awt.Window.class, Boolean.TYPE);

            method.invoke(clazz, window, false);
        } catch (Exception e) {
            // silently ignore this exception.
        }
    }

    /**
     * Create and install the repaint window focus listener.
     *
     * @param  window the window.
     *
     * @return the listener.
     */
    public static WindowFocusListener createAndInstallRepaintWindowFocusListener(Window window) {
        // create a WindowFocusListener that repaints the window on focus
        // changes.
        WindowFocusListener windowFocusListener = new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                e.getWindow().repaint();
            }

            public void windowLostFocus(WindowEvent e) {
                e.getWindow().repaint();
            }
        };

        window.addWindowFocusListener(windowFocusListener);

        return windowFocusListener;
    }

    /**
     * {@code true} if the given {@link Component}'s parent {@link Window} is
     * currently active (focused).
     *
     * @param  component the {@code Component} to check the parent
     *                   {@code Window}'s focus for.
     *
     * @return {@code true} if the given {@code Component}'s parent
     *         {@code Window} is currently active.
     */
    public static boolean isParentWindowFocused(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);

        return window != null && window.isFocused();
    }

    /**
     * Installs a {@link WindowFocusListener} on the given {@link JComponent}'s
     * parent {@link Window}. If the {@code JComponent} doesn't yet have a
     * parent, then the listener will be installed when the component is added
     * to a container.
     *
     * @param component     the component who's parent frame to listen to focus
     *                      changes on.
     * @param focusListener the {@code WindowFocusListener} to notify when focus
     *                      changes.
     */
    public static void installWeakWindowFocusListener(JComponent component, WindowFocusListener focusListener) {
        WindowListener   weakFocusListener = createWeakWindowFocusListener(focusListener);
        AncestorListener ancestorListener  = createAncestorListener(component, weakFocusListener);

        component.addAncestorListener(ancestorListener);
    }

    /**
     * Create the weak window focus listener.
     *
     * @param  windowFocusListener the focus listener
     *
     * @return the weak referenced listener.
     */
    private static WindowListener createWeakWindowFocusListener(WindowFocusListener windowFocusListener) {
        final WeakReference<WindowFocusListener> weakReference = new WeakReference<WindowFocusListener>(windowFocusListener);

        return new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                // TODO if the WeakReference's object is null, remove the
                // WeakReference as a FocusListener.
                if (weakReference.get() != null) {
                    weakReference.get().windowGainedFocus(e);
                }
            }

            public void windowDeactivated(WindowEvent e) {
                if (weakReference.get() != null) {
                    weakReference.get().windowLostFocus(e);
                }
            }
        };
    }

    /**
     * Installs a listener on the given {@link JComponent}'s parent
     * {@link Window} that repaints the given component when the parent window's
     * focused state changes. If the given component does not have a parent at
     * the time this method is called, then an ancestor listener will be
     * installed that installs a window listener when the components parent
     * changes.
     *
     * @param component the {@code JComponent} to add the repaint focus listener
     *                  to.
     */
    public static void installJComponentRepainterOnWindowFocusChanged(JComponent component) {
        // TODO check to see if the component already has an ancestor.
        WindowListener   windowListener   = createWeakWindowFocusListener(createRepaintWindowListener(component));
        AncestorListener ancestorListener = createAncestorListener(component, windowListener);

        component.addAncestorListener(ancestorListener);
    }

    /**
     * Create the ancestor listener.
     *
     * @param  component      the component.
     * @param  windowListener the listener.
     *
     * @return the weak referenced listener.
     */
    private static AncestorListener createAncestorListener(JComponent component, final WindowListener windowListener) {
        final WeakReference<JComponent> weakReference = new WeakReference<JComponent>(component);

        return new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                // TODO if the WeakReference's object is null, remove the
                // WeakReference as an AncestorListener.
                Window window = weakReference.get() == null ? null : SwingUtilities.getWindowAncestor(weakReference.get());

                if (window != null) {
                    window.removeWindowListener(windowListener);
                    window.addWindowListener(windowListener);
                }
            }

            public void ancestorRemoved(AncestorEvent event) {
                Window window = weakReference.get() == null ? null : SwingUtilities.getWindowAncestor(weakReference.get());

                if (window != null) {
                    window.removeWindowListener(windowListener);
                }
            }

            public void ancestorMoved(AncestorEvent event) {
                // no implementation.
            }
        };
    }

    /**
     * Create the repaint window listener.
     *
     * @param  component the component.
     *
     * @return the listener.
     */
    private static WindowFocusListener createRepaintWindowListener(final JComponent component) {
        return new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                component.repaint();
            }

            public void windowDeactivated(WindowEvent e) {
                component.repaint();
            }
        };
    }

}
