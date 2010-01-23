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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * CheckBoxPainter implementation.
 */
public final class CheckBoxPainter extends AbstractRegionPainter {
    public static enum Which {
        ICON_DISABLED,
        ICON_ENABLED,
        ICON_FOCUSED,
        ICON_PRESSED,
        ICON_PRESSED_FOCUSED,
        ICON_SELECTED,
        ICON_SELECTED_FOCUSED,
        ICON_PRESSED_SELECTED,
        ICON_PRESSED_SELECTED_FOCUSED,
        ICON_DISABLED_SELECTED,
    }

    private Which            state;
    private PaintContext     ctx;
    private boolean          focused;
    private boolean          selected;

    private Color            outerFocusColor          = decodeColor("seaGlassOuterFocus");
    private Color            innerFocusColor          = decodeColor("seaGlassFocus");
    private Color            outerToolBarFocusColor   = decodeColor("seaGlassToolBarOuterFocus");
    private Color            innerToolBarFocusColor   = decodeColor("seaGlassToolBarFocus");

    private Color            colorShadow              = new Color(0x000000);
    private Effect           dropShadow               = new SeaGlassDropShadowEffect();

    private RoundRectangle2D rect                     = new RoundRectangle2D.Double();
    private Path2D           path                     = new Path2D.Double();

    private Color            bullet1                  = new Color(0x333333);
    private Color            bullet2                  = new Color(0x000000);

    private Color            bulletDisabled1          = new Color(0x80333333, true);
    private Color            bulletDisabled2          = new Color(0x80000000, true);

    private Color            enabledInternal1         = new Color(0xfbfdfe);
    private Color            enabledInternal2         = new Color(0xd6eaf9);
    private Color            enabledInternal3         = new Color(0xd2e8f8);
    private Color            enabledInternal4         = new Color(0xf5fafd);
    private Color            enabledBorder1           = new Color(0x88ade0);
    private Color            enabledBorder2           = new Color(0x5785bf);

    private Color            pressedInternal1         = new Color(0xacbdd0);
    private Color            pressedInternal2         = new Color(0x688db3);
    private Color            pressedInternal3         = new Color(0x6d93ba);
    private Color            pressedInternal4         = new Color(0xa4cbe4);
    private Color            pressedBorder1           = new Color(0x4f7bbf);
    private Color            pressedBorder2           = new Color(0x3f76bf);

    private Color            selectedInternal1        = new Color(0xbccedf);
    private Color            selectedInternal2        = new Color(0x7fa7cd);
    private Color            selectedInternal3        = new Color(0x82b0d6);
    private Color            selectedInternal4        = new Color(0xb0daf6);
    private Color            selectedBorder1          = new Color(0x4f7bbf);
    private Color            selectedBorder2          = new Color(0x3f76bf);

    private Color            pressedSelectedInternal1 = new Color(0xacbdd0);
    private Color            pressedSelectedInternal2 = new Color(0x688db3);
    private Color            pressedSelectedInternal3 = new Color(0x6d93ba);
    private Color            pressedSelectedInternal4 = new Color(0xa4cbe4);
    private Color            pressedSelectedBorder1   = new Color(0x4f7bbf);
    private Color            pressedSelectedBorder2   = new Color(0x3f76bf);

    private Color            disabledInternal1        = new Color(0x80fbfdfe, true);
    private Color            disabledInternal2        = new Color(0x80d6eaf9, true);
    private Color            disabledInternal3        = new Color(0x80d2e8f8, true);
    private Color            disabledInternal4        = new Color(0x80f5fafd, true);
    private Color            disabledBorder1          = new Color(0x8088ade0, true);
    private Color            disabledBorder2          = new Color(0x805785bf, true);

    public CheckBoxPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        focused = false;
        selected = false;
        if (state == Which.ICON_FOCUSED || state == Which.ICON_PRESSED_FOCUSED || state == Which.ICON_SELECTED_FOCUSED
                || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            focused = true;
        }
        if (state == Which.ICON_SELECTED || state == Which.ICON_PRESSED_SELECTED || state == Which.ICON_DISABLED_SELECTED
                || state == Which.ICON_SELECTED_FOCUSED || state == Which.ICON_PRESSED_SELECTED_FOCUSED) {
            selected = true;
        }
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (focused) {
            Shape s = setOuterFocus(width, height);
            g.setColor(isInToolBar(c) ? outerToolBarFocusColor : outerFocusColor);
            g.fill(s);
            s = setInnerFocus(width, height);
            g.setColor(isInToolBar(c) ? innerToolBarFocusColor : innerFocusColor);
            g.fill(s);
        }

        switch (state) {
        case ICON_DISABLED:
            paintDisabled(g, c, width, height);
            break;
        case ICON_ENABLED:
        case ICON_FOCUSED:
            paintEnabled(g, c, width, height);
            break;
        case ICON_PRESSED:
        case ICON_PRESSED_FOCUSED:
            paintPressed(g, c, width, height);
            break;
        case ICON_SELECTED:
        case ICON_SELECTED_FOCUSED:
            paintSelected(g, c, width, height);
            break;
        case ICON_PRESSED_SELECTED:
        case ICON_PRESSED_SELECTED_FOCUSED:
            paintPressedSelected(g, c, width, height);
            break;
        case ICON_DISABLED_SELECTED:
            paintDisabledSelected(g, c, width, height);
            break;
        }

        if (selected) {
            if (state == Which.ICON_DISABLED_SELECTED) {
                paintCheckMark(g, width, height, bulletDisabled1, bulletDisabled2, false);
            } else {
                paintCheckMark(g, width, height, bullet1, bullet2, false);
            }
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, disabledBorder1, disabledBorder2, disabledInternal1, disabledInternal2, disabledInternal3,
            disabledInternal4);
    }

    private void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, enabledBorder1, enabledBorder2, enabledInternal1, enabledInternal2, enabledInternal3,
            enabledInternal4);
    }

    private void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, pressedBorder1, pressedBorder2, pressedInternal1, pressedInternal2, pressedInternal3,
            pressedInternal4);
    }

    private void paintSelected(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, selectedBorder1, selectedBorder2, selectedInternal1, selectedInternal2, selectedInternal3,
            selectedInternal4);
    }

    private void paintPressedSelected(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, pressedSelectedBorder1, pressedSelectedBorder2, pressedSelectedInternal1,
            pressedSelectedInternal2, pressedSelectedInternal3, pressedSelectedInternal4);
    }

    private void paintDisabledSelected(Graphics2D g, JComponent c, int width, int height) {
        paintBackground(g, width, height, disabledBorder1, disabledBorder2, disabledInternal1, disabledInternal2, disabledInternal3,
            disabledInternal4);
    }

    private void paintBackground(Graphics2D g, int width, int height, Color border1, Color border2, Color internal1, Color internal2,
        Color internal3, Color internal4) {
        Shape s = setBorder(width, height);
        if (!focused) {
            dropShadow.fill(g, s, colorShadow);
        }
        Paint p = setGradientBorder(s, border1, border2);
        g.setPaint(p);
        g.fill(s);
        s = setInternal(width, height);
        p = setGradientInterior(s, internal1, internal2, internal3, internal4);
        g.setPaint(p);
        g.fill(s);
    }

    private void paintCheckMark(Graphics2D g, int width, int height, Color color1, Color color2, boolean shadow) {
        Shape s = setCheckMark(width, height);
        Paint p = setGradientCheckMark(s, color1, color2);
        g.setPaint(p);
        g.fill(s);
    }

    private Shape setOuterFocus(int width, int height) {
        return setRect(width - 0.0, width, height, 7);
    }

    private Shape setInnerFocus(int width, int height) {
        return setRect(width - 2.0, width, height, 6);
    }

    private Shape setBorder(int width, int height) {
        return setRect(width - 4.0, width, height, 5);
    }

    private Shape setInternal(int width, int height) {
        return setRect(width - 6.0, width, height, 4);
    }

    private Shape setRect(Double diameter, int width, int height, int arc) {
        Double pos = (width - diameter) / 2.0;
        rect.setRoundRect(pos, pos, diameter, diameter, arc, arc);
        return rect;
    }

    private Shape setCheckMark(int width, int height) {
        double widthMult = width / 18.0;
        double heightMult = height / 18.0;
        path.reset();
        path.moveTo(5.0 * widthMult, 8.0 * heightMult);
        path.lineTo(7.0 * widthMult, 8.0 * heightMult);
        path.lineTo(9.75 * widthMult, 11.0 * heightMult);
        path.lineTo(14.0 * widthMult, 1.0 * heightMult);
        path.lineTo(16.0 * widthMult, 1.0 * heightMult);
        path.lineTo(10.0 * widthMult, 13.0 * heightMult);
        path.closePath();
        return path;
    }

    private Paint setGradientBorder(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    private Paint setGradientInterior(Shape s, Color color1, Color color2, Color color3, Color color4) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 0.45f, 0.6f, 1f }, new Color[] {
            color1,
            color2,
            color3,
            color4 });
    }

    private Paint setGradientCheckMark(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient(x + w, y, (0.3f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }
}
