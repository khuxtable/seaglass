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
package com.seaglass.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.seaglass.effect.DropShadowEffect;
import com.seaglass.effect.Effect;
import com.seaglass.effect.SeaGlassDropShadowEffect;
import com.seaglass.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * Button painter. This paints both regular and toggle buttons because they look
 * the same except for the state.
 * 
 * @author Kathryn Huxtable
 */
public final class ButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DEFAULT,
        BACKGROUND_DEFAULT_FOCUSED,
        BACKGROUND_MOUSEOVER_DEFAULT,
        BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED,
        BACKGROUND_PRESSED_DEFAULT,
        BACKGROUND_PRESSED_DEFAULT_FOCUSED,
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_MOUSEOVER,
        BACKGROUND_MOUSEOVER_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_SELECTED,
        BACKGROUND_SELECTED_FOCUSED,
        BACKGROUND_PRESSED_SELECTED,
        BACKGROUND_PRESSED_SELECTED_FOCUSED,
        BACKGROUND_DISABLED_SELECTED
    };

    private enum SegmentStatus {
        NONE, FIRST, MIDDLE, LAST
    };

    private static final Insets    insets                             = new Insets(7, 7, 7, 7);
    private static final Dimension dimension                          = new Dimension(86, 29);
    private static final CacheMode cacheMode                          = CacheMode.NINE_SQUARE_SCALE;
    private static final Double    maxH                               = Double.POSITIVE_INFINITY;
    private static final Double    maxV                               = Double.POSITIVE_INFINITY;

    private final Color            colorShadow                        = new Color(0x000000);
    private final Color            colorFocus                         = new Color(0x79a0cf);

    private final Color            enabledUpperShineTop               = new Color(0xf3ffffff, true);
    private final Color            enabledUpperShineBottom            = new Color(0x00ffffff, true);
    private final Color            enabledLowerShineTop               = new Color(0x00f7fcff, true);
    private final Color            enabledLowerShineBottom            = new Color(0xffffffff, true);
    private final float            enabledLowerShineMidpoint          = 0.5f;
    private final Color            enabledColor                       = new Color(0xa8d2f2);
    private final Color            enabledBackgroundTop               = new Color(0x88ade0);
    private final Color            enabledBackgroundBottom            = new Color(0x5785bf);

    private final Color            pressedUpperShineTop               = new Color(0xb3eeeeee, true);
    private final Color            pressedUpperShineBottom            = new Color(0x00ffffff, true);
    private final Color            pressedLowerShineTop               = new Color(0x00A8D9FC, true);
    private final Color            pressedLowerShineBottom            = new Color(0xffb4d9ee, true);
    private final float            pressedLowerShineMidpoint          = 0.4f;
    private final Color            pressedColor                       = new Color(0x134D8C);
    private final Color            pressedBackgroundTop               = new Color(0x4F7BBF);
    private final Color            pressedBackgroundBottom            = new Color(0x3F76BF);

    private final Color            defaultUpperShineTop               = new Color(0xc0ffffff, true);
    private final Color            defaultUpperShineBottom            = new Color(0x00eeeeee, true);
    private final Color            defaultLowerShineTop               = new Color(0x00A8D9FC, true);
    private final Color            defaultLowerShineBottom            = new Color(0xffC0E8FF, true);
    private final float            defaultLowerShineMidpoint          = 0.4f;
    private final Color            defaultColor                       = new Color(0x276FB2);
    private final Color            defaultBackgroundTop               = new Color(0x4F7BBF);
    private final Color            defaultBackgroundBottom            = new Color(0x3F76BF);

    private final Color            pressedDefaultUpperShineTop        = new Color(0xc0eeeeee, true);
    private final Color            pressedDefaultUpperShineBottom     = new Color(0x00eeeeee, true);
    private final Color            pressedDefaultLowerShineTop        = new Color(0x00A8D9FC, true);
    private final Color            pressedDefaultLowerShineBottom     = new Color(0xffB4D9EE, true);
    private final float            pressedDefaultLowerShineMidpoint   = 0.4f;
    private final Color            pressedDefaultColor                = new Color(0x134D8C);
    private final Color            pressedDefaultBackgroundTop        = new Color(0x4F7BBF);
    private final Color            pressedDefaultBackgroundBottom     = new Color(0x3F76BF);

    private final Color            disabledUpperShineTop              = new Color(0xc0F4F8FB, true);
    private final Color            disabledUpperShineBottom           = new Color(0x00ffffff, true);
    private final Color            disabledLowerShineTop              = new Color(0x00A8D9FC, true);
    private final Color            disabledLowerShineBottom           = new Color(0xffF7FCFF, true);
    private final float            disabledLowerShineMidpoint         = 0.4f;
    private final Color            disabledColor                      = new Color(0xeeeeee);
    private final Color            disabledBackgroundTop              = new Color(0x8AAFE0);
    private final Color            disabledBackgroundBottom           = new Color(0x5785BF);

    private final Color            disabledSelectedUpperShineTop      = new Color(0xc0F4F8FB, true);
    private final Color            disabledSelectedUpperShineBottom   = new Color(0x00ffffff, true);
    private final Color            disabledSelectedLowerShineTop      = new Color(0x00A8D9FC, true);
    private final Color            disabledSelectedLowerShineBottom   = new Color(0xffF7FCFF, true);
    private final float            disabledSelectedLowerShineMidpoint = 0.4f;
    private final Color            disabledSelectedColor              = new Color(0xaaaaaa);
    private final Color            disabledSelectedBackgroundTop      = new Color(0x8AAFE0);
    private final Color            disabledSelectedBackgroundBottom   = new Color(0x5785BF);

    private Path2D                 path                               = new Path2D.Double();

    private Which                  state;
    private boolean                focused;
    private Effect                 dropShadow                         = new SeaGlassDropShadowEffect();

    private PaintContext           ctx;

    /**
     * Create a new ButtonPainter.
     * 
     * @param state
     *            the state of the button to be painted.
     */
    public ButtonPainter(Which state) {
        super();
        this.state = state;
        switch (state) {
        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_MOUSEOVER_FOCUSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_SELECTED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            focused = true;
            break;
        default:
            focused = false;
            break;
        }

        ctx = new PaintContext(insets, dimension, false, cacheMode, maxH, maxV);
    }

    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = new Object[] {};
        return extendedCacheKeys;
    }

    protected final PaintContext getPaintContext() {
        return ctx;
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        SegmentStatus segmentStatus = SegmentStatus.NONE;
        Object buttonType = c.getClientProperty("JButton.buttonType");
        if ("segmented".equals(buttonType) || "segmentedTextured".equals(buttonType)) {
            String position = (String) c.getClientProperty("JButton.segmentPosition");
            if ("first".equals(position)) {
                segmentStatus = SegmentStatus.FIRST;
            } else if ("middle".equals(position)) {
                segmentStatus = SegmentStatus.MIDDLE;
            } else if ("last".equals(position)) {
                segmentStatus = SegmentStatus.LAST;
            }
        }

        switch (state) {
        case BACKGROUND_DEFAULT:
        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_MOUSEOVER_DEFAULT:
        case BACKGROUND_MOUSEOVER_DEFAULT_FOCUSED:
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            drawButton(g, c, width, height, segmentStatus, defaultBackgroundTop, defaultBackgroundBottom, defaultColor,
                defaultLowerShineTop, defaultLowerShineBottom, defaultLowerShineMidpoint, defaultUpperShineTop, defaultUpperShineBottom);
            break;
        case BACKGROUND_PRESSED_DEFAULT:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            drawButton(g, c, width, height, segmentStatus, pressedDefaultBackgroundTop, pressedDefaultBackgroundBottom,
                pressedDefaultColor, pressedDefaultLowerShineTop, pressedDefaultLowerShineBottom, pressedDefaultLowerShineMidpoint,
                pressedDefaultUpperShineTop, pressedDefaultUpperShineBottom);
            break;
        case BACKGROUND_DISABLED:
            drawButton(g, c, width, height, segmentStatus, disabledBackgroundTop, disabledBackgroundBottom, disabledColor,
                disabledLowerShineTop, disabledLowerShineBottom, disabledLowerShineMidpoint, disabledUpperShineTop,
                disabledUpperShineBottom);
            break;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_MOUSEOVER:
        case BACKGROUND_MOUSEOVER_FOCUSED:
            drawButton(g, c, width, height, segmentStatus, enabledBackgroundTop, enabledBackgroundBottom, enabledColor,
                enabledLowerShineTop, enabledLowerShineBottom, enabledLowerShineMidpoint, enabledUpperShineTop, enabledUpperShineBottom);
            break;
        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            drawButton(g, c, width, height, segmentStatus, pressedBackgroundTop, pressedBackgroundBottom, pressedColor,
                pressedLowerShineTop, pressedLowerShineBottom, pressedLowerShineMidpoint, pressedUpperShineTop, pressedUpperShineBottom);
            break;
        case BACKGROUND_DISABLED_SELECTED:
            drawButton(g, c, width, height, segmentStatus, disabledSelectedBackgroundTop, disabledSelectedBackgroundBottom,
                disabledSelectedColor, disabledSelectedLowerShineTop, disabledSelectedLowerShineBottom, disabledSelectedLowerShineMidpoint,
                disabledSelectedUpperShineTop, disabledSelectedUpperShineBottom);
            break;
        }
    }

    /*
     * Draw a button.
     */
    private void drawButton(Graphics2D g, JComponent c, int width, int height, SegmentStatus segmentStatus, Color backgroundTop,
        Color backgroundBottom, Color mainColor, Color lowerShineTop, Color lowerShineBottom, float lowerShineMidpoint,
        Color upperShineTop, Color upperShineBottom) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        path = decodeRoundBackground(segmentStatus);
        g.drawImage(createDropShadowImage(path), 0, 0, null);
        g.setPaint(decodeGradientBackground(path, backgroundTop, backgroundBottom));
        g.fill(path);
        if (focused) {
            path = decodeRoundFocus(segmentStatus);
            g.setColor(colorFocus);
            g.fill(path);
        }
        path = decodeRoundMain(segmentStatus);
        g.setColor(mainColor);
        g.fill(path);
        g.setPaint(decodeGradientBottomShine(path, lowerShineTop, lowerShineBottom, lowerShineMidpoint));
        g.fill(path);
        g.setPaint(decodeGradientTopShine(path, upperShineTop, upperShineBottom));
        g.fill(path);
    }

    /**
     * @param g
     */
    private BufferedImage createDropShadowImage(Shape s) {
        BufferedImage bimage = DropShadowEffect.createBufferedImage(dimension.width, dimension.height, true);
        Graphics2D gbi = bimage.createGraphics();
        gbi.setColor(colorShadow);
        gbi.fill(s);
        return dropShadow.applyEffect(bimage, null, dimension.width, dimension.height);
    }

    private Path2D decodeRoundFocus(SegmentStatus segmentStatus) {
        switch (segmentStatus) {
        case FIRST:
            setFirstRoundRect(0d, 0d, 86d, 28d, 4.75d, 4.75d);
            break;
        case MIDDLE:
            setRect(0d, 0d, 86d, 28d);
            break;
        case LAST:
            setLastRoundRect(0d, 0d, 86d, 28d, 4.75d, 4.75d);
            break;
        default:
            setRoundRect(0d, 0d, 86d, 28d, 4.75d, 4.75d);
            break;
        }
        return path;
    }

    private Path2D decodeRoundBackground(SegmentStatus segmentStatus) {
        switch (segmentStatus) {
        case FIRST:
            setFirstRoundRect(1d, 1d, 85d, 26d, 4d, 4d);
            break;
        case MIDDLE:
            setRect(0d, 1d, 86d, 26d);
            break;
        case LAST:
            setLastRoundRect(0d, 1d, 85d, 26d, 4d, 4d);
            break;
        default:
            setRoundRect(1d, 1d, 84d, 26d, 4d, 4d);
            break;
        }
        return path;
    }

    private Path2D decodeRoundMain(SegmentStatus segmentStatus) {
        switch (segmentStatus) {
        case FIRST:
            setFirstRoundRect(2d, 2d, 84d, 24d, 3d, 3d);
            break;
        case MIDDLE:
            setRect(1d, 2d, 85d, 24d);
            break;
        case LAST:
            setLastRoundRect(1d, 2d, 83d, 24d, 3d, 3d);
            break;
        default:
            setRoundRect(2d, 2d, 82d, 24d, 3d, 3d);
            break;
        }
        return path;
    }

    private void setRoundRect(Double left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }

    private void setFirstRoundRect(Double left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);
        path.closePath();
    }

    private void setLastRoundRect(Double left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }

    private void setRect(Double left, Double top, Double width, Double height) {
        Double bottom = top + height;
        Double right = left + width;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);
        path.closePath();
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }
}
