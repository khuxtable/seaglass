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
package com.seaglasslookandfeel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.component.SeaGlassBorder;
import com.seaglasslookandfeel.painter.Painter;
import com.seaglasslookandfeel.util.SeaGlassGraphicsUtils;
import com.sun.java.swing.plaf.nimbus.NimbusStyle;

import sun.swing.plaf.synth.SynthUI;

/**
 * A SynthStyle implementation used by SeaGlass. This just wraps a SynthStyle
 * with a SeaGlass style in order to get at some SynthStyle methods that are
 * package local.
 * 
 * @see javax.swing.plaf.synth.SynthStyle
 */
public final class SeaGlassStyleWrapper extends SeaGlassStyle {
    /* Keys and scales for large/small/mini components, based on Apple's sizes */
    public static final String              LARGE_KEY         = "large";
    public static final String              SMALL_KEY         = "small";
    public static final String              MINI_KEY          = "mini";
    public static final double              LARGE_SCALE       = 1.15;
    public static final double              SMALL_SCALE       = 0.857;
    public static final double              MINI_SCALE        = 0.714;

    /**
     * Shared SynthGraphics.
     */
    private static final SynthGraphicsUtils SEAGLASS_GRAPHICS = new SeaGlassGraphicsUtils();

    /**
     * The SynthPainter that will be returned from this NimbusStyle. The
     * SynthPainter returned will be a SeaGlassSynthPainterImpl, which will in
     * turn delegate back to this NimbusStyle for the proper Painter (not
     * SynthPainter) to use for painting the foreground, background, or border.
     */
    private SynthPainter                    painter;

    private SynthStyle                      style;

    /**
     * Create a new NimbusStyle. Only the prefix must be supplied. At the
     * appropriate time, installDefaults will be called. At that point, all of
     * the state information will be pulled from UIManager and stored locally
     * within this style.
     * 
     * @param prefix
     *            Something like Button or Slider.Thumb or
     *            org.jdesktop.swingx.JXStatusBar or
     *            ComboBox."ComboBox.arrowButton"
     * @param c
     *            an optional reference to a component that this NimbusStyle
     *            should be associated with. This is only used when the
     *            component has Nimbus overrides registered in its client
     *            properties and should be null otherwise.
     */
    SeaGlassStyleWrapper(SynthStyle style) {
        super(null, null);
        this.style = style;
        this.painter = new SeaGlassSynthPainterImpl(this);
    }

    /**
     * Returns the <code>SynthGraphicUtils</code> for the specified context.
     * 
     * @param context
     *            SynthContext identifying requester
     * @return SynthGraphicsUtils
     */
    public SynthGraphicsUtils getGraphicsUtils(SynthContext context) {
        return SEAGLASS_GRAPHICS;
    }

    /**
     * Re-implements SynthStyle.installDefaults(SynthContext, SynthUI) because
     * it's package local.
     */
    public void installDefaults(SeaGlassContext context, SynthUI ui) {
        // Special case the Border as this will likely change when the LAF
        // can have more control over this.
        if (!context.isSubregion()) {
            JComponent c = context.getComponent();
            Border border = c.getBorder();

            if (border == null || border instanceof UIResource) {
                c.setBorder(new SeaGlassBorder(ui, getInsets(context, null)));
            }
        }
        installDefaults(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installDefaults(SynthContext ctx) {
        // delegate to the superclass to install defaults such as background,
        // foreground, font, and opaque onto the swing component.
        style.installDefaults(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getInsets(SynthContext ctx, Insets in) {
        return style.getInsets(ctx, in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColorForState(SynthContext ctx, ColorType type) {
        return style.getColor(ctx, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Font getFontForState(SynthContext ctx) {
        Font f = (Font) get(ctx, "font");
        if (f == null) f = UIManager.getFont("defaultFont");

        // Account for scale
        // The key "JComponent.sizeVariant" is used to match Apple's LAF
        String scaleKey = (String) ctx.getComponent().getClientProperty("JComponent.sizeVariant");
        if (scaleKey != null) {
            if (LARGE_KEY.equals(scaleKey)) {
                f = f.deriveFont(Math.round(f.getSize2D() * LARGE_SCALE));
            } else if (SMALL_KEY.equals(scaleKey)) {
                f = f.deriveFont(Math.round(f.getSize2D() * SMALL_SCALE));
            } else if (MINI_KEY.equals(scaleKey)) {
                f = f.deriveFont(Math.round(f.getSize2D() * MINI_SCALE));
            }
        }
        return f;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SynthPainter getPainter(SynthContext ctx) {
        return painter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpaque(SynthContext ctx) {
        return style.isOpaque(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(SynthContext ctx, Object key) {
        return style.get(ctx, key);
    }

    /**
     * Gets the appropriate background Painter, if there is one, for the state
     * specified in the given SynthContext. This method does appropriate
     * fallback searching, as described in #get.
     * 
     * @param ctx
     *            The SynthContext. Must not be null.
     * @return The background painter associated for the given state, or null if
     *         none could be found.
     */
    @SuppressWarnings("unchecked")
    public Painter getBackgroundPainter(SynthContext ctx) {
        if (!(style instanceof NimbusStyle)) {
            return null;
        }
        return new PainterWrapper(((NimbusStyle) style).getBackgroundPainter(ctx));
    }

    /**
     * Gets the appropriate foreground Painter, if there is one, for the state
     * specified in the given SynthContext. This method does appropriate
     * fallback searching, as described in #get.
     * 
     * @param ctx
     *            The SynthContext. Must not be null.
     * @return The foreground painter associated for the given state, or null if
     *         none could be found.
     */
    @SuppressWarnings("unchecked")
    public Painter getForegroundPainter(SynthContext ctx) {
        if (!(style instanceof NimbusStyle)) {
            return null;
        }
        return new PainterWrapper(((NimbusStyle) style).getForegroundPainter(ctx));
    }

    /**
     * Gets the appropriate border Painter, if there is one, for the state
     * specified in the given SynthContext. This method does appropriate
     * fallback searching, as described in #get.
     * 
     * @param ctx
     *            The SynthContext. Must not be null.
     * @return The border painter associated for the given state, or null if
     *         none could be found.
     */
    @SuppressWarnings("unchecked")
    public Painter getBorderPainter(SynthContext ctx) {
        if (!(style instanceof NimbusStyle)) {
            return null;
        }
        return new PainterWrapper(((NimbusStyle) style).getBorderPainter(ctx));
    }

    /**
     * Wrap the sun Painter class with our own.
     */
    public class PainterWrapper implements Painter {
        private com.sun.java.swing.Painter painter;

        public PainterWrapper(com.sun.java.swing.Painter painter) {
            this.painter = painter;
        }

        public void paint(Graphics2D g, Object object, int width, int height) {
            painter.paint(g, object, width, height);
        }
    }
}
