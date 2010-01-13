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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Path2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.util.PlatformUtils;

/**
 * ToolBarPainter implementation.
 * 
 * Parts taken from Nimbus to draw drag handle.
 * 
 * @author Ken Orr
 * @author Modified by Kathryn Huxtable for SeaGlass
 */
public class ToolBarHandlePainter extends AbstractRegionPainter {
    public static enum Which {
        HANDLEICON_ENABLED,
    };

    private static final boolean IS_NON_MAC   = !PlatformUtils.isMac();

    private static final Color   HANDLE_COLOR = IS_NON_MAC ? new Color(0xddcccccc, true) : new Color(0xc8191919, true);

    private static final Path2D  path         = new Path2D.Float();

    private PaintContext         ctx;

    public ToolBarHandlePainter(Which state) {
        super();
        this.ctx = new PaintContext(new Insets(5, 5, 5, 5), new Dimension(11, 38), false, CacheMode.FIXED_SIZES,
            (Double) Double.POSITIVE_INFINITY, (Double) Double.POSITIVE_INFINITY);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        painthandleIconEnabled(g, width, height);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void painthandleIconEnabled(Graphics2D g, int width, int height) {
        path.reset();
        path.moveTo(4, 2);
        path.lineTo(4, height - 3);
        path.moveTo(6, height - 3);
        path.lineTo(6, 2);

        g.setColor(HANDLE_COLOR);
        g.draw(path);
    }
}
