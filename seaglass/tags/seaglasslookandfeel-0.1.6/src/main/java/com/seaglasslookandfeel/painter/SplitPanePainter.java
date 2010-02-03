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

import javax.swing.JComponent;

/**
 * SplitPanePainter implementation. Does nothing because the work is done by the
 * divider painter.
 */
public final class SplitPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED
    }

    private PaintContext ctx;

    public SplitPanePainter(Which state) {
        super();
        this.ctx = new PaintContext(PaintContext.CacheMode.FIXED_SIZES);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }
}
