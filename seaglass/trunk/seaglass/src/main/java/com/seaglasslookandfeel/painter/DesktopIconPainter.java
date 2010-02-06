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
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Nimbus's DesktopIconPainter.
 */
public final class DesktopIconPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED
    }

    private TwoColors    rootPaneActive              = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"),
                                                         decodeColor("seaGlassToolBarActiveBottomB"));
    private TwoColors    rootPaneInactive            = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"),
                                                         decodeColor("seaGlassToolBarInactiveBottomB"));

    private Color        frameBorderBase             = decodeColor("frameBorderBase");

    private Color        frameInnerHighlightInactive = decodeColor("frameInnerHighlightInactive");
    private Color        frameInnerHighlightActive   = decodeColor("frameInnerHighlightActive");

    private Color        frameBorderActive           = frameBorderBase;
    private Color        frameBorderInactive         = frameBorderBase;

    private PaintContext ctx;

    public DesktopIconPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRoundRectangle(2, 0, width - 3, height - 2, CornerSize.FRAME_BORDER);
        getFrameBorderPaint(s, CommonControlType.DISABLED);

        s = shapeGenerator.createRoundRectangle(3, 1, width - 5, height - 4, CornerSize.FRAME_INNER_HIGHLIGHT);
        g.setPaint(getFrameInnerHighlightPaint(s, CommonControlType.DISABLED));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(4, 2, width - 7, height - 6, CornerSize.FRAME_INTERIOR);
        g.setPaint(getRootPaneInteriorPaint(s, CommonControlType.DISABLED));
        g.fill(s);
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private Color getFrameBorderColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return frameBorderInactive;
        case ENABLED:
            return frameBorderActive;
        }
        return null;
    }

    public Paint getFrameBorderPaint(Shape s, CommonControlType type) {
        return getFrameBorderColors(type);
    }

    private Color getFrameInnerHighlightColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return frameInnerHighlightInactive;
        case ENABLED:
            return frameInnerHighlightActive;
        }
        return null;
    }

    public Paint getFrameInnerHighlightPaint(Shape s, CommonControlType type) {
        return getFrameInnerHighlightColors(type);
    }

    private TwoColors getRootPaneInteriorColors(CommonControlType type) {
        switch (type) {
        case ENABLED:
            return rootPaneActive;
        case DISABLED:
            return rootPaneInactive;
        }
        return null;
    }

    public Paint getRootPaneInteriorPaint(Shape s, CommonControlType type) {
        TwoColors colors = getRootPaneInteriorColors(type);
        return createVerticalGradient(s, colors);
    }
}
