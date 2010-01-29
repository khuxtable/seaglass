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
package com.seaglasslookandfeel.painter;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.ToolBarNorthState;
import com.seaglasslookandfeel.state.ToolBarSouthState;

/**
 * Nimbus's FrameAndRootPainter.
 */
public final class FrameAndRootPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED, BACKGROUND_ENABLED_NOFRAME
    };

    private static final int   TITLE_BAR_HEIGHT  = 25;

    private static final State toolBarNorthState = new ToolBarNorthState();
    private static final State toolBarSouthState = new ToolBarSouthState();

    private PaintContext       ctx;
    private ButtonType         type;

    public FrameAndRootPainter(Which state) {
        super();
        this.ctx = new PaintContext(PaintContext.CacheMode.FIXED_SIZES);
        type = getButtonType(state);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        paintFrame(g, c, width, height);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_ENABLED:
            return ButtonType.INACTIVE;
        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return ButtonType.ACTIVE;
        }
        return null;
    }

    private void paintFrame(Graphics2D g, JComponent c, int width, int height) {
        Shape s = ShapeUtil.createRoundRectangle(0, 0, (width - 1), (height - 1), CornerSize.FRAME_BORDER);
        if (type != null) {
            ColorUtil.drawFrameBorderColors(g, s, type);
        }

        JMenuBar mb = null;
        Component[] cArray = null;
        if (c instanceof JInternalFrame) {
            JInternalFrame iframe = (JInternalFrame) c;
            mb = iframe.getJMenuBar();
            cArray = iframe.getContentPane().getComponents();
        } else if (c instanceof JRootPane) {
            JRootPane root = (JRootPane) c;
            mb = root.getJMenuBar();
            cArray = root.getContentPane().getComponents();
        }

        int topToolBarHeight = 0;
        int bottomToolBarHeight = 0;
        if (cArray != null) {
            for (Component comp : cArray) {
                if (comp instanceof JToolBar) {
                    if (toolBarNorthState.isInState((JComponent) comp)) {
                        topToolBarHeight = comp.getHeight();
                    } else if (toolBarSouthState.isInState((JComponent) comp)) {
                        bottomToolBarHeight = comp.getHeight();
                    }
                }
            }
        }

        int titleHeight = TITLE_BAR_HEIGHT;
        if (mb != null && !"true".equals(c.getClientProperty("SeaGlass.JRootPane.MenuInTitle"))) {
            titleHeight += mb.getHeight();
        }

        s = ShapeUtil.createRoundRectangle(1, 1, (width - 2), (height - 2), CornerSize.FRAME_INNER_HIGHLIGHT);
        ColorUtil.fillFrameInteriorColors(g, s, type, titleHeight, topToolBarHeight, bottomToolBarHeight);

        s = ShapeUtil.createRoundRectangle(1, 1, (width - 3), (height - 3), CornerSize.FRAME_INTERIOR);
        ColorUtil.drawFrameInnerHighlightColors(g, s, type);
    }
}
