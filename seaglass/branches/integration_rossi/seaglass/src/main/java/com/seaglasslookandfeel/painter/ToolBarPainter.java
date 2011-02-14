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

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarPainter extends AbstractRegionPainter {
    public static enum Which {
        BORDER_ENABLED,
    };

    private PaintContext ctx;
    private Which state;
    private Color borderColor = decodeColor("seaGlassDropShadow");
    private JToolBar toolbar;

    public ToolBarPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
        this.state = state;
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        this.toolbar = (JToolBar) c;
        switch (state) {
            case BORDER_ENABLED:
                paintBorder(g, width, height);
                break;
        }
    }
    
    private void paintBorder(Graphics2D g, int width, int height) {
        g.setColor(borderColor);
        g.drawLine(0, height, width, height);
//        g.setColor(borderColor.brighter());
//        g.drawLine(0, height - 0, width, height - 0);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }
}
