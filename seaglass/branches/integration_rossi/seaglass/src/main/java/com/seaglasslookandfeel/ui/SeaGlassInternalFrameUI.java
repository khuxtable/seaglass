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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.component.SeaGlassInternalFrameTitlePane;

/**
 * SeaGlassInternalFrameUI implementation.
 * 
 * Copied from SynthInternalFrameUI.
 * 
 * @see javax.swing.plaf.synth.SynthInternalFrameUI
 */
public class SeaGlassInternalFrameUI extends BasicInternalFrameUI implements SeaglassUI, PropertyChangeListener {
    private SynthStyle                     style;

    private SeaGlassInternalFrameTitlePane titlePane;

    public static ComponentUI createUI(JComponent b) {
        return new SeaGlassInternalFrameUI((JInternalFrame) b);
    }

    public SeaGlassInternalFrameUI(JInternalFrame b) {
        super(b);
    }

    public void installDefaults() {
        // We want to draw rounded corners, so the internal frame must not be
        // opaque. LookAndFeel.installProperty doesn't seem to work here.
        frame.setOpaque(false);

        frame.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 2));

        frame.setLayout(internalFrameLayout = createLayoutManager());
        updateStyle(frame);
    }

    protected void installListeners() {
        super.installListeners();
        frame.addPropertyChangeListener(this);
    }

    protected void uninstallComponents() {
        if (frame.getComponentPopupMenu() instanceof UIResource) {
            frame.setComponentPopupMenu(null);
        }
        super.uninstallComponents();
    }

    protected void uninstallListeners() {
        frame.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            Icon frameIcon = frame.getFrameIcon();
            if (frameIcon == null || frameIcon instanceof UIResource) {
                frame.setFrameIcon(context.getStyle().getIcon(context, "InternalFrame.icon"));
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(frame, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        if (frame.getLayout() == internalFrameLayout) {
            frame.setLayout(null);
        }

    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    public int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    protected JComponent createNorthPane(JInternalFrame w) {
        titlePane = new SeaGlassInternalFrameTitlePane(w);
        titlePane.setName("InternalFrame.northPane");
        return titlePane;
    }

    protected ComponentListener createComponentListener() {
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            return new ComponentHandler() {
                public void componentResized(ComponentEvent e) {
                    if (frame != null && frame.isMaximum()) {
                        JDesktopPane desktop = (JDesktopPane) e.getSource();
                        for (Component comp : desktop.getComponents()) {
                            if (comp instanceof SeaGlassDesktopPaneUI.TaskBar) {
                                frame.setBounds(0, 0, desktop.getWidth(), desktop.getHeight() - comp.getHeight());
                                frame.revalidate();
                                break;
                            }
                        }
                    }

                    // Update the new parent bounds for next resize, but don't
                    // let the super method touch this frame
                    JInternalFrame f = frame;
                    frame = null;
                    super.componentResized(e);
                    frame = f;
                }
            };
        } else {
            return super.createComponentListener();
        }
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintInternalFrameBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintInternalFrameBorder(context, g, x, y, w, h);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        SynthStyle oldStyle = style;
        JInternalFrame f = (JInternalFrame) evt.getSource();
        String prop = evt.getPropertyName();

        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle(f);
        }

        if (style == oldStyle && (prop == JInternalFrame.IS_MAXIMUM_PROPERTY || prop == JInternalFrame.IS_SELECTED_PROPERTY)) {
            // Border (and other defaults) may need to change
            SeaGlassContext context = getContext(f, ENABLED);
            style.uninstallDefaults(context);
            ((SeaGlassStyle) style).installDefaults(context, this);
        }
    }
}
