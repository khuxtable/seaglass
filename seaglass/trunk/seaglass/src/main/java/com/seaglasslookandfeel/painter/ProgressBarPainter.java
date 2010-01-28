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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;

/**
 * ProgressBarPainter implementation.
 */
public final class ProgressBarPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED,
        BACKGROUND_DISABLED,

        FOREGROUND_ENABLED,
        FOREGROUND_ENABLED_FINISHED,
        FOREGROUND_ENABLED_INDETERMINATE,
        FOREGROUND_DISABLED,
        FOREGROUND_DISABLED_FINISHED,
        FOREGROUND_DISABLED_INDETERMINATE,
    }

    private Color        innerShadowColor1      = new Color(0x20000000, true);
    private Color        innerShadowColor2      = new Color(0x10000000, true);

    private Effect       dropShadow             = new SeaGlassDropShadowEffect();

    private Color        disabledTrack1         = new Color(0x803f76bf, true);
    private Color        disabledTrack2         = new Color(0x804076bf, true);
    private Color        disabledTrackInterior  = new Color(0x80ffffff, true);

    private Color        enabledTrack1          = new Color(0x3f76bf);
    private Color        enabledTrack2          = new Color(0x4076bf);
    private Color        enabledTrackInterior   = new Color(0xffffff);

    private Color        disabledBar1           = new Color(0x80bccedf, true);
    private Color        disabledBar2           = new Color(0x807fa7cd, true);
    private Color        disabledBar3           = new Color(0x8082b0d6, true);
    private Color        disabledBar4           = new Color(0x80b0daf6, true);
    private Color        disabledBarEnd         = new Color(0x804076bf, true);

    private Color        disabledIndeterminate1 = new Color(0x80fbfdfe, true);
    private Color        disabledIndeterminate2 = new Color(0x80d6eaf9, true);
    private Color        disabledIndeterminate3 = new Color(0x80d2e8f8, true);
    private Color        disabledIndeterminate4 = new Color(0x80f5fafd, true);

    private Color        enabledBar1            = new Color(0xbccedf);
    private Color        enabledBar2            = new Color(0x7fa7cd);
    private Color        enabledBar3            = new Color(0x82b0d6);
    private Color        enabledBar4            = new Color(0xb0daf6);
    private Color        enabledBarEnd          = new Color(0x4076bf);

    private Color        indeterminate1         = new Color(0xfbfdfe);
    private Color        indeterminate2         = new Color(0xd6eaf9);
    private Color        indeterminate3         = new Color(0xd2e8f8);
    private Color        indeterminate4         = new Color(0xf5fafd);

    private Which        state;
    private PaintContext ctx;

    public ProgressBarPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_ENABLED:
            paintTrackEnabled(g, width, height);
            break;
        case BACKGROUND_DISABLED:
            paintTrackDisabled(g, width, height);
            break;
        case FOREGROUND_ENABLED:
            paintBarEnabled(g, width, height, false);
            break;
        case FOREGROUND_ENABLED_FINISHED:
            paintBarEnabled(g, width, height, true);
            break;
        case FOREGROUND_ENABLED_INDETERMINATE:
            paintIndeterminateEnabled(g, width, height);
            break;
        case FOREGROUND_DISABLED:
            paintBarDisabled(g, width, height, false);
            break;
        case FOREGROUND_DISABLED_FINISHED:
            paintBarDisabled(g, width, height, true);
            break;
        case FOREGROUND_DISABLED_INDETERMINATE:
            paintIndeterminateDisabled(g, width, height);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintTrackDisabled(Graphics2D g, int width, int height) {
        paintTrack(g, width, height, disabledTrack1, disabledTrack2, disabledTrackInterior);
    }

    private void paintTrackEnabled(Graphics2D g, int width, int height) {
        paintTrack(g, width, height, enabledTrack1, enabledTrack2, enabledTrackInterior);
    }

    private void paintBarDisabled(Graphics2D g, int width, int height, boolean isFinished) {
        paintBar(g, width, height, isFinished, disabledBar1, disabledBar2, disabledBar3, disabledBar4, disabledBarEnd);
    }

    private void paintBarEnabled(Graphics2D g, int width, int height, boolean isFinished) {
        paintBar(g, width, height, isFinished, enabledBar1, enabledBar2, enabledBar3, enabledBar4, enabledBarEnd);
    }

    private void paintIndeterminateDisabled(Graphics2D g, int width, int height) {
        paintIndeterminate(g, width, height, disabledBar1, disabledBar2, disabledBar3, disabledBar4, disabledIndeterminate1,
            disabledIndeterminate2, disabledIndeterminate3, disabledIndeterminate4);
    }

    private void paintIndeterminateEnabled(Graphics2D g, int width, int height) {
        paintIndeterminate(g, width, height, enabledBar1, enabledBar2, enabledBar3, enabledBar4, indeterminate1, indeterminate2,
            indeterminate3, indeterminate4);
    }

    private void paintTrack(Graphics2D g, int width, int height, Color color1, Color color2, Color colorInterior) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = ShapeUtil.createRoundRectangle(2, 2, width - 5, height - 5, CornerSize.ROUND_HEIGHT_DRAW);
        if (state != Which.BACKGROUND_DISABLED) {
            dropShadow.fill(g, s);
        }
        g.setPaint(decodeTrackGradient(s, color1, color2));
        g.draw(s);

        s = ShapeUtil.createRoundRectangle(3, 3, width - 6, height - 6, CornerSize.ROUND_HEIGHT);
        g.setColor(colorInterior);
        g.fill(s);

        s = ShapeUtil.createRoundedInternalDropShadowLight(width, height);
        g.setColor(innerShadowColor2);
        g.draw(s);
        s = ShapeUtil.createRoundedInternalDropShadowDark(width, height);
        g.setColor(innerShadowColor1);
        g.draw(s);
    }

    private void paintBar(Graphics2D g, int width, int height, boolean isFinished, Color color1, Color color2, Color color3, Color color4,
        Color colorEnd) {
        Shape s = ShapeUtil.createRectangle(0, 0, width, height);
        g.setPaint(decodeBarGradient(s, color1, color2, color3, color4));
        g.fill(s);

        if (!isFinished) {
            g.setColor(colorEnd);
            g.drawLine(width - 1, 0, width - 1, height - 1);
        }
    }

    private void paintIndeterminate(Graphics2D g, int width, int height, Color dark1, Color dark2, Color dark3, Color dark4, Color light1,
        Color light2, Color light3, Color light4) {
        Shape s = ShapeUtil.createProgressBarIndeterminateLight(width, height);
        g.setPaint(decodeBarGradient(s, light1, light2, light3, light4));
        g.fill(s);
        s = ShapeUtil.createProgressBarIndeterminateDark(width, height);
        g.setPaint(decodeBarGradient(s, dark1, dark2, dark3, dark4));
        g.fill(s);
    }

    private Paint decodeTrackGradient(Shape s, Color color1, Color color2) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        int y1 = r.y;
        int y2 = r.y + r.height;
        return decodeGradient(x, y1, x, y2, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    private Paint decodeBarGradient(Shape s, Color color1, Color color2, Color color3, Color color4) {
        Rectangle r = s.getBounds();
        int x = r.x + r.width / 2;
        int y1 = r.y;
        int y2 = r.y + r.height;
        return decodeGradient(x, y1, x, y2, new float[] { 0f, 0.45f, 0.6f, 1f }, new Color[] { color1, color2, color3, color4 });
    }
}
