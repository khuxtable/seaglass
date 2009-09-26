/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglass.util.SeaGlassGraphicsUtils;
import com.sun.java.swing.Painter;
import com.sun.java.swing.plaf.nimbus.NimbusStyle;

/**
 * A SynthStyle implementation used by SeaGlass.
 */
public final class SeaGlassStyle extends SynthStyle {
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
     * SynthPainter returned will be a SeaGlassSynthPainterImpl, which will in turn
     * delegate back to this NimbusStyle for the proper Painter (not
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
    SeaGlassStyle(SynthStyle style) {
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
     * @InheritDoc Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary.
     */
    @Override
    public void installDefaults(SynthContext ctx) {
        // delegate to the superclass to install defaults such as background,
        // foreground, font, and opaque onto the swing component.
        style.installDefaults(ctx);
    }

    /**
     * @InheritDoc Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary.
     */
    @Override
    public Insets getInsets(SynthContext ctx, Insets in) {
        return style.getInsets(ctx, in);
    }

    /**
     * @InheritDoc <p>
     *             Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary.
     *             </p>
     * 
     *             <p>
     *             In addition, NimbusStyle handles ColorTypes slightly
     *             differently from Synth.
     *             </p>
     *             <ul>
     *             <li>ColorType.BACKGROUND will equate to the color stored in
     *             UIDefaults named "background".</li>
     *             <li>ColorType.TEXT_BACKGROUND will equate to the color stored
     *             in UIDefaults named "textBackground".</li>
     *             <li>ColorType.FOREGROUND will equate to the color stored in
     *             UIDefaults named "textForeground".</li>
     *             <li>ColorType.TEXT_FOREGROUND will equate to the color stored
     *             in UIDefaults named "textForeground".</li>
     *             </ul>
     */
    @Override
    protected Color getColorForState(SynthContext ctx, ColorType type) {
        return style.getColor(ctx, type);
    }

    /**
     * @InheritDoc Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary. If a value named "font" is not
     *             found in UIDefaults, then the "defaultFont" font in
     *             UIDefaults will be returned instead.
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
     * @InheritDoc Returns the SynthPainter for this style, which ends up
     *             delegating to the Painters installed in this style.
     */
    @Override
    public SynthPainter getPainter(SynthContext ctx) {
        return painter;
    }

    /**
     * @InheritDoc Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary. If opacity is not specified in
     *             UI defaults, then it defaults to being non-opaque.
     */
    @Override
    public boolean isOpaque(SynthContext ctx) {
        return style.isOpaque(ctx);
    }

    /**
     * @InheritDoc <p>
     *             Overridden to cause this style to populate itself with data
     *             from UIDefaults, if necessary.
     *             </p>
     * 
     *             <p>
     *             Properties in UIDefaults may be specified in a chained
     *             manner. For example:
     * 
     *             <pre>
     * background
     * Button.opacity
     * Button.Enabled.foreground
     * Button.Enabled+Selected.background
     * </pre>
     * 
     *             </p>
     * 
     *             <p>
     *             In this example, suppose you were in the Enabled+Selected
     *             state and searched for "foreground". In this case, we first
     *             check for Button.Enabled+Selected.foreground, but no such
     *             color exists. We then fall back to the next valid state, in
     *             this case, Button.Enabled.foreground, and have a match. So we
     *             return it.
     *             </p>
     * 
     *             <p>
     *             Again, if we were in the state Enabled and looked for
     *             "background", we wouldn't find it in Button.Enabled, or in
     *             Button, but would at the top level in UIManager. So we return
     *             that value.
     *             </p>
     * 
     *             <p>
     *             One special note: the "key" passed to this method could be of
     *             the form "background" or "Button.background" where "Button"
     *             equals the prefix passed to the NimbusStyle constructor. In
     *             either case, it looks for "background".
     *             </p>
     * 
     * @param ctx
     * @param key
     *            must not be null
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
        return ((NimbusStyle) style).getBackgroundPainter(ctx);
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
        return ((NimbusStyle) style).getForegroundPainter(ctx);
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
        return ((NimbusStyle) style).getBorderPainter(ctx);
    }
}
