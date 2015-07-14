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
package com.seaglasslookandfeel.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.painter.ViewportPainter;

/**
 * SeaGlassViewportUI implementation.
 * 
 * Based on SynthViewPortUI, which is package local.
 * 
 * @see javax.swing.plaf.synth.SynthViewportUI
 */
public class SeaGlassViewportUI extends ViewportUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassViewportUI();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        installDefaults(c);
        installListeners(c);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        uninstallListeners(c);
        uninstallDefaults(c);
    }

    protected void installDefaults(JComponent c) {
        updateStyle(c);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);

        // Note: JViewport is special cased as it does not allow for
        // a border to be set. JViewport.setBorder is overriden to throw
        // an IllegalArgumentException. Refer to SynthScrollPaneUI for
        // details of this.
        SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();

        if (newStyle != oldStyle) {
            if (oldStyle != null) {
                oldStyle.uninstallDefaults(context);
            }
            context.setStyle(newStyle);
            newStyle.installDefaults(context);
        }
        this.style = newStyle;
        context.dispose();
    }

    protected void installListeners(JComponent c) {
        c.addPropertyChangeListener(this);
    }

    protected void uninstallListeners(JComponent c) {
        c.removePropertyChangeListener(this);
    }

    protected void uninstallDefaults(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, getRegion(c), style, state);
    }

    private Region getRegion(JComponent c) {
        return SynthLookAndFeel.getRegion(c);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintViewportBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        // This does nothing on purpose, JViewport doesn't allow a border
        // and therefor this will NEVER be called.
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        JComponent c = context.getComponent();
        JViewport viewport = (JViewport) c;
        if (c.isOpaque()) {
            Component view = viewport.getView();
            Object ui = (view == null) ? null : invokeGetter(view, "getUI", null);
            if (ui instanceof ViewportPainter) {
                ((ViewportPainter) ui).paintViewport(context, g, viewport);
            } else {
                if (viewport.getView() != null) {
                    g.setColor(viewport.getView().getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                }
            }
        }
    }

    /**
     * Invokes the specified getter method if it exists.
     * 
     * @param obj
     *            The object on which to invoke the method.
     * @param methodName
     *            The name of the method.
     * @param defaultValue
     *            This value is returned, if the method does not exist.
     * @return The value returned by the getter method or the default value.
     */
    private static Object invokeGetter(Object obj, String methodName, Object defaultValue) {
        try {
            Method method = obj.getClass().getMethod(methodName, new Class[0]);
            Object result = method.invoke(obj, new Object[0]);
            return result;
        } catch (NoSuchMethodException e) {
            return defaultValue;
        } catch (IllegalAccessException e) {
            return defaultValue;
        } catch (InvocationTargetException e) {
            return defaultValue;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JComponent) e.getSource());
        }
    }
}
