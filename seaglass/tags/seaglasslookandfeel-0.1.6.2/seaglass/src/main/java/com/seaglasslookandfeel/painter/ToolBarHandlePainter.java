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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;
import com.seaglasslookandfeel.util.PlatformUtils;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarHandlePainter extends AbstractCommonColorsPainter {
    public static enum Which {
        HANDLEICON_ENABLED,
    };

    private Color        toolbarHandleMac = decodeColor("toolbarHandleMac");

    private PaintContext ctx;

    public ToolBarHandlePainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (PlatformUtils.isMac()) {
            paintMacHandleIcon(g, width, height);
        } else {
            paintNonMacHandleIcon(g, width, height);
        }
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintMacHandleIcon(Graphics2D g, int width, int height) {
        g.setPaint(toolbarHandleMac);
        g.drawLine(4, 2, 4, height - 3);
        g.drawLine(6, 2, 6, height - 3);
    }

    private void paintNonMacHandleIcon(Graphics2D g, int width, int height) {
        Shape s = decodeNonMacHandleBorder(width, height);
        g.setPaint(getCommonBorderPaint(s, CommonControlState.ENABLED));
        g.fill(s);

        s = decodeNonMacHandleInside(width, height);
        g.setPaint(getCommonInteriorPaint(s, CommonControlState.ENABLED));
        g.fill(s);
    }

    private Shape decodeNonMacHandleBorder(int width, int height) {
        return shapeGenerator.createRoundRectangle(4, 2, width - 4, height - 4, CornerSize.ROUND_WIDTH);
    }

    private Shape decodeNonMacHandleInside(int width, int height) {
        return shapeGenerator.createRoundRectangle(5, 3, width - 6, height - 6, CornerSize.ROUND_WIDTH);
    }
}
