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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * @author Kathryn Huxtable
 *
 */
public class SeaGlassToolTipUI extends BasicToolTipUI implements PropertyChangeListener, SeaglassUI {

    private SynthStyle style;

    /**
     * Creates a new UI object for the given component.
     *
     * @param c
     *            component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassToolTipUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults(JComponent c) {
        updateStyle(c);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallDefaults(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installListeners(JComponent c) {
        c.addPropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallListeners(JComponent c) {
        c.removePropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        JComponent comp = ((JToolTip) c).getComponent();

        if (comp != null && !comp.isEnabled()) {
            return DISABLED;
        }
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * Notifies this UI delegate to repaint the specified component. This method
     * paints the component background, then calls the
     * {@link #paint(SynthContext,Graphics)} method.
     *
     * <p>
     * In general, this method does not need to be overridden by subclasses. All
     * Look and Feel rendering code should reside in the {@code paint} method.
     *
     * @param g
     *            the {@code Graphics} object used for painting
     * @param c
     *            the component being painted
     * @see #paint(SynthContext,Graphics)
     */
    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintToolTipBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext)context).getPainter().paintToolTipBorder(context, g, x, y, w, h);
    }

    /**
     * Paints the specified component according to the Look and Feel.
     * <p>
     * This method is not used by Synth Look and Feel. Painting is handled by
     * the {@link #paint(SynthContext,Graphics)} method.
     *
     * @param g
     *            the {@code Graphics} object used for painting
     * @param c
     *            the component being painted
     * @see #paint(SynthContext,Graphics)
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the specified component.
     *
     * @param context
     *            context for the component being painted
     * @param g
     *            the {@code Graphics} object used for painting
     * @see #update(Graphics,JComponent)
     */
    protected void paint(SynthContext context, Graphics g) {
        JToolTip tip = (JToolTip) context.getComponent();

        Insets insets = tip.getInsets();
        View v = (View) tip.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            Rectangle paintTextR = new Rectangle(insets.left, insets.top, tip.getWidth() - (insets.left + insets.right), tip.getHeight()
                    - (insets.top + insets.bottom));
            v.paint(g, paintTextR);
        } else {
            g.setColor(context.getStyle().getColor(context, ColorType.TEXT_FOREGROUND));
            g.setFont(style.getFont(context));
            context.getStyle().getGraphicsUtils(context).paintText(context, g, tip.getTipText(), insets.left, insets.top, -1);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        SeaGlassContext context = getContext(c);
        Insets insets = c.getInsets();
        Dimension prefSize = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        String text = ((JToolTip) c).getTipText();

        if (text != null) {
            View v = (c != null) ? (View) c.getClientProperty("html") : null;
            if (v != null) {
                prefSize.width += (int) v.getPreferredSpan(View.X_AXIS);
                prefSize.height += (int) v.getPreferredSpan(View.Y_AXIS);
            } else {
                Font font = context.getStyle().getFont(context);
                FontMetrics fm = c.getFontMetrics(font);
                prefSize.width += context.getStyle().getGraphicsUtils(context).computeStringWidth(context, font, fm, text);
                prefSize.height += fm.getHeight();
            }
        }
        context.dispose();
        return prefSize;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JToolTip) e.getSource());
        }
        String name = e.getPropertyName();
        if (name.equals("tiptext") || "font".equals(name) || "foreground".equals(name)) {
            // remove the old html view client property if one
            // existed, and install a new one if the text installed
            // into the JLabel is html source.
            JToolTip tip = ((JToolTip) e.getSource());
            String text = tip.getTipText();
            BasicHTML.updateRenderer(tip, text);
        }
    }
}
