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

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.component.SeaGlassInternalFrameTitlePane;

/**
 * SeaGlassDesktopIconUI implementation.
 * 
 * Based on Synth's implementation, which is package local.
 * 
 * @see javax.swing.plaf.synth.SynthDesktopIconUI
 */
public class SeaGlassDesktopIconUI extends BasicDesktopIconUI implements SeaglassUI, ActionListener, PropertyChangeListener {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassDesktopIconUI();
    }

    protected void installComponents() {
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            iconPane = new JToggleButton(frame.getTitle(), frame.getFrameIcon()) {
                public String getToolTipText() {
                    return getText();
                }

                public JPopupMenu getComponentPopupMenu() {
                    return frame.getComponentPopupMenu();
                }
            };
            ToolTipManager.sharedInstance().registerComponent(iconPane);
            iconPane.setFont(desktopIcon.getFont());
            iconPane.setBackground(desktopIcon.getBackground());
            iconPane.setForeground(desktopIcon.getForeground());
        } else {
            iconPane = new SeaGlassInternalFrameTitlePane(frame);
            iconPane.setName("InternalFrame.northPane");
        }
        desktopIcon.setLayout(new BorderLayout());
        desktopIcon.add(iconPane, BorderLayout.CENTER);
    }

    protected void installListeners() {
        super.installListeners();
        desktopIcon.addPropertyChangeListener(this);

        if (iconPane instanceof JToggleButton) {
            frame.addPropertyChangeListener(this);
            ((JToggleButton) iconPane).addActionListener(this);
        }
    }

    protected void uninstallListeners() {
        if (iconPane instanceof JToggleButton) {
            frame.removePropertyChangeListener(this);
        }
        desktopIcon.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    protected void installDefaults() {
        updateStyle(desktopIcon);
    }

    private void updateStyle(JComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(desktopIcon, ENABLED);
        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        Region region = getRegion(c);
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    Region getRegion(JComponent c) {
        return SeaGlassLookAndFeel.getRegion(c);
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintDesktopIconBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
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
        ((SeaGlassContext) context).getPainter().paintDesktopIconBorder(context, g, x, y, w, h);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JToggleButton) {
            // Either iconify the frame or deiconify and activate it.
            JToggleButton button = (JToggleButton) evt.getSource();
            try {
                boolean selected = button.isSelected();
                if (!selected && !frame.isIconifiable()) {
                    button.setSelected(true);
                } else {
                    frame.setIcon(!selected);
                    if (selected) {
                        frame.setSelected(true);
                    }
                }
            } catch (PropertyVetoException e2) {
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof JInternalFrame.JDesktopIcon) {
            if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
                updateStyle((JInternalFrame.JDesktopIcon) evt.getSource());
            }
        } else if (evt.getSource() instanceof JInternalFrame) {
            JInternalFrame frame = (JInternalFrame) evt.getSource();
            if (iconPane instanceof JToggleButton) {
                JToggleButton button = (JToggleButton) iconPane;
                String prop = evt.getPropertyName();
                if (prop == "title") {
                    button.setText((String) evt.getNewValue());
                } else if (prop == "frameIcon") {
                    button.setIcon((Icon) evt.getNewValue());
                } else if (prop == JInternalFrame.IS_ICON_PROPERTY || prop == JInternalFrame.IS_SELECTED_PROPERTY) {
                    button.setSelected(!frame.isIcon() && frame.isSelected());
                }
            }
        }
    }
}
