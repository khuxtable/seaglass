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
 * $Id: ToolBarPainter.java 1081 2010-02-03 18:43:10Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */

// Rossi: ToolTip has now a nicer border and is blue

public class ToolTipPainter extends AbstractRegionPainter {
    public static enum Which {
        BORDER_ENABLED,
    };

    private final Color tooltipBorderColor = decodeColor("seaGlassToolTipBorder");
    
    private PaintContext ctx;

    public ToolTipPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.BORDER);
        g.setPaint(c.getBackground());
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(0, 0, width - 1, height - 1, CornerSize.BORDER);
        g.setPaint(tooltipBorderColor);
        g.draw(s);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }
}
