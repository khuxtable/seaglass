/*
 * Copyright (c) 2010 Kathryn Huxtable and Kenneth Orr.
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.SeparatorUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * A Synth L&F implementation of SeparatorUI. This implementation is a
 * "combined" view/controller.
 *
 * @version 1.16 11/17/05
 * @author  Shannon Hickey
 * @author  Joshua Outwater
 */
public class SeaGlassSeparatorUI extends SeparatorUI implements PropertyChangeListener, SeaglassUI {
    private SynthStyle style;

    /**
     * DOCUMENT ME!
     *
     * @param  c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassSeparatorUI();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        installDefaults((JSeparator) c);
        installListeners((JSeparator) c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void uninstallDefaults(JComponent c) {
        uninstallListeners((JSeparator) c);
        uninstallDefaults((JSeparator) c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void installDefaults(JSeparator c) {
        updateStyle(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sep DOCUMENT ME!
     */
    private void updateStyle(JSeparator sep) {
        SeaGlassContext context  = getContext(sep, ENABLED);
        SynthStyle      oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            if (sep instanceof JToolBar.Separator) {
                Dimension size = ((JToolBar.Separator) sep).getSeparatorSize();

                if (size == null || size instanceof UIResource) {
                    size = (DimensionUIResource) style.get(context, "ToolBar.separatorSize");
                    if (true || size == null) {
                        size = new DimensionUIResource(10, 10);
                    }

                    ((JToolBar.Separator) sep).setSeparatorSize(size);
                }
            }
        }

        context.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void uninstallDefaults(JSeparator c) {
        SeaGlassContext context = getContext(c, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void installListeners(JSeparator c) {
        c.addPropertyChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void uninstallListeners(JSeparator c) {
        c.removePropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        JSeparator separator = (JSeparator) context.getComponent();

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintSeparatorBackground(context,
                                                      g, 0, 0, c.getWidth(), c.getHeight(),
                                                      separator.getOrientation());
        paint(context, g);
        context.dispose();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        JSeparator separator = (JSeparator) context.getComponent();

        context.getPainter().paintSeparatorForeground(context, g, 0, 0, separator.getWidth(), separator.getHeight(),
                                                      separator.getOrientation());
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SeaGlassContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        JSeparator separator = (JSeparator) context.getComponent();

        ((SeaGlassContext) context).getPainter().paintSeparatorBorder(context, g, x, y, w, h, separator.getOrientation());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#getPreferredSize(javax.swing.JComponent)
     */
    public Dimension getPreferredSize(JComponent c) {
        SeaGlassContext context = getContext(c);

        int       thickness = style.getInt(context, "Separator.thickness", 5);
        Insets    insets    = c.getInsets();
        Dimension size;

        if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
            size = new Dimension(insets.left + insets.right + thickness,
                                 insets.top + insets.bottom);
        } else {
            size = new Dimension(insets.left + insets.right,
                                 insets.top + insets.bottom + thickness);
        }

        context.dispose();
        return size;
    }

    /**
     * @see javax.swing.plaf.ComponentUI#getMinimumSize(javax.swing.JComponent)
     */
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#getMaximumSize(javax.swing.JComponent)
     */
    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  state DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c,
                                          SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JSeparator) evt.getSource());
        }
    }
}
