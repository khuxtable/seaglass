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
package com.seaglasslookandfeel.effect;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

public class SeaGlassInternalShadowEffect {

    public void fill(Graphics2D g, Shape s, boolean isRounded, boolean paintRightShadow) {
        if (isRounded) {
            fillInternalShadowRounded(g, s);
        } else {
            fillInternalShadow(g, s, paintRightShadow);
        }
    }

    private void fillInternalShadow(Graphics2D g, Shape s, boolean paintRightShadow) {
        Rectangle bounds = s.getBounds();
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        s = ShapeUtil.createRectangle(x, y, w, 2);
        g.setPaint(ColorUtil.createTopShadowPaint(s));
        g.fill(s);

        s = ShapeUtil.createRectangle(x, y, 1, h);
        g.setPaint(ColorUtil.createLeftShadowPaint(s));
        g.fill(s);

        if (paintRightShadow) {
            s = ShapeUtil.createRectangle(x + w - 1, y, 1, h);
            g.setPaint(ColorUtil.createRightShadowPaint(s));
            g.fill(s);
        }
    }

    private void fillInternalShadowRounded(Graphics2D g, Shape s) {
        g.setPaint(ColorUtil.createRoundedShadowPaint(s));
        g.fill(s);
    }

}
