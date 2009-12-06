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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.ui.SeaGlassScrollBarUI;

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractImagePainter<ScrollBarButtonPainter.Which> {
    public static enum Which {
        FOREGROUND_ENABLED, FOREGROUND_DISABLED, FOREGROUND_MOUSEOVER, FOREGROUND_PRESSED,
    }

    private static final Insets    insets    = new Insets(1, 1, 1, 1);
    private static final Dimension dimension = new Dimension(20, 15);
    private static final CacheMode cacheMode = CacheMode.FIXED_SIZES;
    private static final Double    maxH      = 1.0;
    private static final Double    maxV      = 1.0;

    ImageIcon                      rightImage;

    public ScrollBarButtonPainter(Which state) {
        super(state);
        setPaintContext(new PaintContext(insets, dimension, false, cacheMode, maxH, maxV));
    }

    @Override
    protected String getImageName(Which state) {
        String leftName = null;
        String rightName = null;
        switch (state) {
        case FOREGROUND_ENABLED:
            leftName = "h_scroll_left_apart_enabled";
            rightName = "h_scroll_right_apart_enabled";
            break;
        case FOREGROUND_DISABLED:
            leftName = "h_scroll_left_apart_enabled";
            rightName = "h_scroll_right_apart_enabled";
            break;
        case FOREGROUND_MOUSEOVER:
            leftName = "h_scroll_left_apart_enabled";
            rightName = "h_scroll_right_apart_enabled";
            break;
        case FOREGROUND_PRESSED:
            leftName = "h_scroll_left_apart_pressed";
            rightName = "h_scroll_right_apart_pressed";
        }
        rightImage = new ImageIcon(ScrollBarButtonPainter.class.getResource("/com/seaglasslookandfeel/resources/images/" + rightName + ".png"));
        return leftName;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (c != null && c.getParent() != null) {
            SeaGlassScrollBarUI ui = (SeaGlassScrollBarUI) ((JScrollBar) c.getParent()).getUI();
            if (ui.getIncreaseButton() == c) {
                rightImage.paintIcon(c, g, 0, 0);
                return;
            }
        }
        image.paintIcon(c, g, 0, 0);
    }
}
