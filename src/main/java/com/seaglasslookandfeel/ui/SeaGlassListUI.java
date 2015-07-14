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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLabelUI;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassBorder;

/**
 * @author Kathryn Huxtable
 *
 */
public class SeaGlassListUI extends BasicListUI implements PropertyChangeListener, SeaglassUI {

    private SynthStyle style;
    private boolean useListColors;
    private boolean useUIBorder;

    /**
     * Creates a new UI object for the given component.
     *
     * @param list component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent list) {
        return new SeaGlassListUI();
    }

    /**
     * Notifies this UI delegate to repaint the specified component.
     * This method paints the component background, then calls
     * the {@link #paint} method.
     *
     * <p>In general, this method does not need to be overridden by subclasses.
     * All Look and Feel rendering code should reside in the {@code paint} method.
     *
     * @param g the {@code Graphics} object used for painting
     * @param c the component being painted
     * @see #paint
     */
    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintListBackground(context,
                          g, 0, 0, c.getWidth(), c.getHeight());
        context.dispose();
        paint(g, c);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintListBorder(context, g, x, y, w, h);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        list.addPropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle((JList)e.getSource());
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        list.removePropertyChangeListener(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults() {
        if (list.getCellRenderer() == null ||
                 (list.getCellRenderer() instanceof UIResource)) {
            list.setCellRenderer(new SynthListCellRenderer());
        }
        updateStyle(list);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        if (style != oldStyle) {
            context.setComponentState(SELECTED);
            Color sbg = list.getSelectionBackground();
            if (sbg == null || sbg instanceof UIResource) {
                list.setSelectionBackground(style.getColor(
                                 context, ColorType.TEXT_BACKGROUND));
            }

            Color sfg = list.getSelectionForeground();
            if (sfg == null || sfg instanceof UIResource) {
                list.setSelectionForeground(style.getColor(
                                 context, ColorType.TEXT_FOREGROUND));
            }

            useListColors = style.getBoolean(context,
                                  "List.rendererUseListColors", true);
            useUIBorder = style.getBoolean(context,
                                  "List.rendererUseUIBorder", true);

            int height = style.getInt(context, "List.cellHeight", -1);
            if (height != -1) {
                list.setFixedCellHeight(height);
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallDefaults() {
        super.uninstallDefaults();

        SeaGlassContext context = getContext(list, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
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
        return SeaGlassLookAndFeel.getComponentState(c);
    }


    private class SynthListCellRenderer extends DefaultListCellRenderer.UIResource {
        @Override public String getName() {
            return "List.cellRenderer";
        }

        @Override public void setBorder(Border b) {
            if (useUIBorder || b instanceof SeaGlassBorder) {
                super.setBorder(b);
            }
        }

        @Override public Component getListCellRendererComponent(JList list, Object value,
                  int index, boolean isSelected, boolean cellHasFocus) {
            if (!useListColors && (isSelected || cellHasFocus)) {
                SeaGlassLookAndFeel.setSelectedUI((SynthLabelUI)SeaGlassLookAndFeel.
                             getUIOfType(getUI(), SynthLabelUI.class),
                                   isSelected, cellHasFocus, list.isEnabled(), false);
            }
            else {
                SeaGlassLookAndFeel.resetSelectedUI();
            }

            super.getListCellRendererComponent(list, value, index,
                                               isSelected, cellHasFocus);
            return this;
        }

        @Override public void paint(Graphics g) {
            super.paint(g);
            SeaGlassLookAndFeel.resetSelectedUI();
        }
    }
    
}
