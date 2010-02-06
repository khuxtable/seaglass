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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ContentPanePainter implementation.
 */
public class ContentPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED,
    };

    private TwoColors         rootPaneActive   = new TwoColors(decodeColor("seaGlassToolBarActiveTopT"),
                                                   decodeColor("seaGlassToolBarActiveBottomB"));
    private TwoColors         rootPaneInactive = new TwoColors(decodeColor("seaGlassToolBarInactiveTopT"),
                                                   decodeColor("seaGlassToolBarInactiveBottomB"));

    private PaintContext      ctx;

    private CommonControlType type;

    public ContentPanePainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.NO_CACHING);

        type = getButtonType(state);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRectangle(0, 0, width, height);
        g.setPaint(getRootPaneInteriorPaint(s, type));
        g.fill(s);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_ENABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED_WINDOWFOCUSED:
            return CommonControlType.ENABLED;
        }
        return null;
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
