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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.VolatileImage;
import java.awt.print.PrinterGraphics;

import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator;
import com.seaglasslookandfeel.state.ControlInToolBarState;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.util.ImageCache;
import com.seaglasslookandfeel.util.SeaGlassGraphicsUtils;

/**
 * Convenient base class for defining Painter instances for rendering a region
 * or component in Sea Glass.
 *
 * <p>Based on Nimbus's AbstractRegionPainter by Jasper Potts and Richard Bair.
 * This was package local.</p>
 *
 */
public abstract class AbstractRegionPainter implements SeaGlassPainter<JComponent> {
    private static final State inToolBarState = new ControlInToolBarState();

    /**
     * Focus ring color state.
     */
    public enum FocusType {
        INNER_FOCUS, OUTER_FOCUS,
    }

    /**
     * PaintContext, which holds a lot of the state needed for cache hinting and
     * x/y value decoding The data contained within the context is typically
     * only computed once and reused over multiple paint calls, whereas the
     * other values (w, h, f, leftWidth, etc) are recomputed for each call to
     * paint.
     *
     * <p>This field is retrieved from subclasses on each paint operation. It is
     * up to the subclass to compute and cache the PaintContext over multiple
     * calls.</p>
     */
    private PaintContext ctx;

    /** The generator for almost all of the shapes we use to draw controls. */
    protected ShapeGenerator shapeGenerator = new ShapeGenerator();

    /**
     * Insets used for positioning the control border in order to leave enough
     * room for the focus indicator.
     */
    protected Insets focusInsets;

    private Color outerFocus        = decodeColor("seaGlassOuterFocus");
    private Color innerFocus        = decodeColor("seaGlassFocus");
    private Color outerToolBarFocus = decodeColor("seaGlassToolBarOuterFocus");
    private Color innerToolBarFocus = decodeColor("seaGlassToolBarFocus");

    /**
     * Create a new AbstractRegionPainter
     */
    protected AbstractRegionPainter() {
        focusInsets = UIManager.getInsets("seaGlassFocusInsets");
    }

    /**
     * Returns true if we should paint focus using light colors on a blue
     * toolbar.
     *
     * @param  c
     *
     * @return
     */
    protected boolean isInToolBar(JComponent c) {
        return inToolBarState.isInState(c);
    }

    /**
     * {@inheritDoc}
     */
    public final void paint(Graphics2D g, JComponent c, int w, int h) {
        // don't render if the width/height are too small
        if (w <= 0 || h <= 0)
            return;

        Object[] extendedCacheKeys = getExtendedCacheKeys(c);

        ctx = getPaintContext();
        CacheMode cacheMode = ctx == null ? CacheMode.NO_CACHING : ctx.getCacheMode();

        if (cacheMode == CacheMode.NO_CACHING || !ImageCache.getInstance().isImageCachable(w, h) || g instanceof PrinterGraphics) {
            paintDirectly(g, c, w, h, extendedCacheKeys);
        } else {
            paintWithCaching(g, c, w, h, extendedCacheKeys);
        }
    }

    /**
     * Get any extra attributes which the painter implementation would like to
     * include in the image cache lookups. This is checked for every call of the
     * paint(g, c, w, h) method.
     *
     * @param  c The component on the current paint call
     *
     * @return Array of extra objects to be included in the cache key
     */
    protected Object[] getExtendedCacheKeys(JComponent c) {
        return null;
    }

    /**
     * <p>Gets the PaintContext for this painting operation. This method is
     * called on every paint, and so should be fast and produce no garbage. The
     * PaintContext contains information such as cache hints. It also contains
     * data necessary for decoding points at runtime, such as the stretching
     * insets, the canvas size at which the encoded points were defined, and
     * whether the stretching insets are inverted.</p>
     *
     * <p>This method allows for subclasses to package the painting of different
     * states with possibly different canvas sizes, etc, into one
     * AbstractRegionPainter implementation.</p>
     *
     * @return a PaintContext associated with this paint operation.
     */
    protected abstract PaintContext getPaintContext();

    /**
     * Get the paint to use for a focus ring.
     *
     * @param  s               the shape to paint.
     * @param  focusType       the focus type.
     * @param  useToolBarFocus whether we should use the colors for a toolbar control.
     *
     * @return the paint to use to paint the focus ring.
     */
    public Paint getFocusPaint(Shape s, FocusType focusType, boolean useToolBarFocus) {
        if (focusType == FocusType.OUTER_FOCUS) {
            return useToolBarFocus ? outerToolBarFocus : outerFocus;
        } else {
            return useToolBarFocus ? innerToolBarFocus : innerFocus;
        }
    }

    /**
     * <p>Configures the given Graphics2D. Often, rendering hints or
     * compositiing rules are applied to a Graphics2D object prior to painting,
     * which should affect all of the subsequent painting operations. This
     * method provides a convenient hook for configuring the Graphics object
     * prior to rendering, regardless of whether the render operation is
     * performed to an intermediate buffer or directly to the display.</p>
     *
     * @param g The Graphics2D object to configure. Will not be null.
     */
    protected void configureGraphics(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * Actually performs the painting operation. Subclasses must implement this
     * method. The graphics object passed may represent the actual surface being
     * rendererd to, or it may be an intermediate buffer. It has also been
     * pre-translated. Simply render the component as if it were located at 0, 0
     * and had a width of <code>width</code> and a height of <code>height</code>
     * . For performance reasons, you may want to read the clip from the
     * Graphics2D object and only render within that space.
     *
     * @param g                 The Graphics2D surface to paint to
     * @param c                 The JComponent related to the drawing event. For
     *                          example, if the region being rendered is Button,
     *                          then <code>c</code> will be a JButton. If the
     *                          region being drawn is ScrollBarSlider, then the
     *                          component will be JScrollBar. This value may be
     *                          null.
     * @param width             The width of the region to paint. Note that in
     *                          the case of painting the foreground, this value
     *                          may differ from c.getWidth().
     * @param height            The height of the region to paint. Note that in
     *                          the case of painting the foreground, this value
     *                          may differ from c.getHeight().
     * @param extendedCacheKeys The result of the call to getExtendedCacheKeys()
     */
    protected abstract void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys);

    /**
     * Decodes and returns a base color in UI defaults.
     *
     * @param  key A key corresponding to the value in the UI Defaults table of
     *             UIManager where the base color is defined
     *
     * @return The base color.
     */
    protected final Color decodeColor(String key) {
        return decodeColor(key, 0f, 0f, 0f, 0);
    }

    /**
     * Decodes and returns a color, which is derived from a base color in UI
     * defaults.
     *
     * @param  key     A key corresponding to the value in the UI Defaults table
     *                 of UIManager where the base color is defined
     * @param  hOffset The hue offset used for derivation.
     * @param  sOffset The saturation offset used for derivation.
     * @param  bOffset The brightness offset used for derivation.
     * @param  aOffset The alpha offset used for derivation. Between 0...255
     *
     * @return The derived color, whos color value will change if the parent
     *         uiDefault color changes.
     */
    protected final Color decodeColor(String key, float hOffset, float sOffset, float bOffset, int aOffset) {
        if (UIManager.getLookAndFeel() instanceof SeaGlassLookAndFeel) {
            SeaGlassLookAndFeel laf = (SeaGlassLookAndFeel) UIManager.getLookAndFeel();

            return laf.getDerivedColor(key, hOffset, sOffset, bOffset, aOffset, true);
        } else {

            // can not give a right answer as painter should not be used outside
            // of nimbus laf but do the best we can
            return Color.getHSBColor(hOffset, sOffset, bOffset);
        }
    }

    /**
     * Derive and returns a color, which is based on an existing color.
     *
     * @param  src     The source color from which to derive the new color.
     * @param  hOffset The hue offset used for derivation.
     * @param  sOffset The saturation offset used for derivation.
     * @param  bOffset The brightness offset used for derivation.
     * @param  aOffset The alpha offset used for derivation. Between 0...255
     *
     * @return The derived color.
     */
    protected Color deriveColor(Color src, float hOffset, float sOffset, float bOffset, int aOffset) {
        float[] tmp = Color.RGBtoHSB(src.getRed(), src.getGreen(), src.getBlue(), null);

        // apply offsets
        tmp[0] = clamp(tmp[0] + hOffset);
        tmp[1] = clamp(tmp[1] + sOffset);
        tmp[2] = clamp(tmp[2] + bOffset);
        int alpha = clamp(src.getAlpha() + aOffset);

        return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24), true);
    }

    /**
     * Decodes and returns a color, which is derived from a offset between two
     * other colors.
     *
     * @param  color1   The first color
     * @param  color2   The second color
     * @param  midPoint The offset between color 1 and color 2, a value of 0.0
     *                  is color 1 and 1.0 is color 2;
     *
     * @return The derived color
     */
    protected final Color decodeColor(Color color1, Color color2, float midPoint) {
        return new Color(deriveARGB(color1, color2, midPoint));
    }

    /**
     * Derives the ARGB value for a color based on an offset between two other
     * colors.
     *
     * @param  color1   The first color
     * @param  color2   The second color
     * @param  midPoint The offset between color 1 and color 2, a value of 0.0
     *                  is color 1 and 1.0 is color 2;
     *
     * @return the ARGB value for a new color based on this derivation
     */
    public static int deriveARGB(Color color1, Color color2, float midPoint) {
        int r = color1.getRed() + (int) ((color2.getRed() - color1.getRed()) * midPoint + 0.5f);
        int g = color1.getGreen() + (int) ((color2.getGreen() - color1.getGreen()) * midPoint + 0.5f);
        int b = color1.getBlue() + (int) ((color2.getBlue() - color1.getBlue()) * midPoint + 0.5f);
        int a = color1.getAlpha() + (int) ((color2.getAlpha() - color1.getAlpha()) * midPoint + 0.5f);

        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    /**
     * Given parameters for creating a LinearGradientPaint, this method will
     * create and return a linear gradient paint. One primary purpose for this
     * method is to avoid creating a LinearGradientPaint where the start and end
     * points are equal. In such a case, the end y point is slightly increased
     * to avoid the overlap.
     *
     * @param  x1
     * @param  y1
     * @param  x2
     * @param  y2
     * @param  midpoints
     * @param  colors
     *
     * @return a valid LinearGradientPaint. This method never returns null.
     */
    protected final LinearGradientPaint createGradient(float x1, float y1, float x2, float y2, float[] midpoints, Color[] colors) {
        if (x1 == x2 && y1 == y2) {
            y2 += .00001f;
        }

        return new LinearGradientPaint(x1, y1, x2, y2, midpoints, colors);
    }

    /**
     * Given parameters for creating a RadialGradientPaint, this method will
     * create and return a radial gradient paint. One primary purpose for this
     * method is to avoid creating a RadialGradientPaint where the radius is
     * non-positive. In such a case, the radius is just slightly increased to
     * avoid 0.
     *
     * @param  x
     * @param  y
     * @param  r
     * @param  midpoints
     * @param  colors
     *
     * @return a valid RadialGradientPaint. This method never returns null.
     */
    protected final RadialGradientPaint createRadialGradient(float x, float y, float r, float[] midpoints, Color[] colors) {
        if (r == 0f) {
            r = .00001f;
        }

        return new RadialGradientPaint(x, y, r, midpoints, colors);
    }

    /**
     * Creates a simple vertical gradient using the shape for bounds and the
     * colors for top and bottom colors.
     *
     * @param  s      the shape to use for bounds.
     * @param  colors the colors to use for the gradient.
     *
     * @return the gradient.
     */
    protected Paint createVerticalGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds  = s.getBounds2D();
        float       xCenter = (float) bounds.getCenterX();
        float       yMin    = (float) bounds.getMinY();
        float       yMax    = (float) bounds.getMaxY();

        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }

    /**
     * Creates a simple vertical gradient using the shape for bounds and the
     * colors for top, two middle, and bottom colors.
     *
     * @param  s      the shape to use for bounds.
     * @param  colors the colors to use for the gradient.
     *
     * @return the gradient.
     */
    protected Paint createVerticalGradient(Shape s, FourColors colors) {
        Rectangle2D bounds  = s.getBounds2D();
        float       xCenter = (float) bounds.getCenterX();
        float       yMin    = (float) bounds.getMinY();
        float       yMax    = (float) bounds.getMaxY();

        return createGradient(xCenter, yMin, xCenter, yMax, new float[] { 0f, 0.45f, 0.62f, 1f },
                              new Color[] { colors.top, colors.upperMid, colors.lowerMid, colors.bottom });
    }

    /**
     * Creates a simple horizontal gradient using the shape for bounds and the
     * colors for top and bottom colors.
     *
     * @param  s      the shape to use for bounds.
     * @param  colors the colors to use for the gradient.
     *
     * @return the gradient.
     */
    protected Paint createHorizontalGradient(Shape s, TwoColors colors) {
        Rectangle2D bounds  = s.getBounds2D();
        float       xMin    = (float) bounds.getMinX();
        float       xMax    = (float) bounds.getMaxX();
        float       yCenter = (float) bounds.getCenterY();

        return createGradient(xMin, yCenter, xMax, yCenter, new float[] { 0f, 1f }, new Color[] { colors.top, colors.bottom });
    }

    /**
     * Creates a simple horizontal gradient using the shape for bounds and the
     * colors for top, two middle, and bottom colors.
     *
     * @param  s      the shape to use for bounds.
     * @param  colors the colors to use for the gradient.
     *
     * @return the gradient.
     */
    protected Paint createHorizontalGradient(Shape s, FourColors colors) {
        Rectangle2D bounds = s.getBounds2D();
        float       x      = (float) bounds.getX();
        float       y      = (float) bounds.getY();
        float       w      = (float) bounds.getWidth();
        float       h      = (float) bounds.getHeight();

        return createGradient(x, (0.5f * h) + y, x + w, (0.5f * h) + y, new float[] { 0f, 0.45f, 0.62f, 1f },
                              new Color[] { colors.top, colors.upperMid, colors.lowerMid, colors.bottom });
    }

    /**
     * Get a color property from the given JComponent. First checks for a <code>
     * getXXX()</code> method and if that fails checks for a client property
     * with key <code>property</code>. If that still fails to return a Color
     * then <code>defaultColor</code> is returned.
     *
     * @param  c                The component to get the color property from
     * @param  property         The name of a bean style property or client
     *                          property
     * @param  defaultColor     The color to return if no color was obtained
     *                          from the component.
     * @param  saturationOffset The offset for the saturation.
     * @param  brightnessOffset the offset for the brightness.
     * @param  alphaOffset      the offset for alpha.
     *
     * @return The color that was obtained from the component or defaultColor
     */
    protected final Color getComponentColor(JComponent c, String property, Color defaultColor, float saturationOffset,
            float brightnessOffset, int alphaOffset) {
        Color color = null;

        if (c != null) {

            // handle some special cases for performance
            if ("background".equals(property)) {
                color = c.getBackground();
            } else if ("foreground".equals(property)) {
                color = c.getForeground();
            } else if (c instanceof JList && "selectionForeground".equals(property)) {
                color = ((JList) c).getSelectionForeground();
            } else if (c instanceof JList && "selectionBackground".equals(property)) {
                color = ((JList) c).getSelectionBackground();
            } else if (c instanceof JTable && "selectionForeground".equals(property)) {
                color = ((JTable) c).getSelectionForeground();
            } else if (c instanceof JTable && "selectionBackground".equals(property)) {
                color = ((JTable) c).getSelectionBackground();
            } else {
                String s = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);

                try {
                    Method method = c.getClass().getMethod(s);

                    color = (Color) method.invoke(c);
                } catch (Exception e) {
                    // don't do anything, it just didn't work, that's all.
                    // This could be a normal occurance if you use a property
                    // name referring to a key in clientProperties instead of
                    // a real property
                }

                if (color == null) {
                    Object value = c.getClientProperty(property);

                    if (value instanceof Color) {
                        color = (Color) value;
                    }
                }
            }
        }

        // we return the defaultColor if the color found is null, or if
        // it is a UIResource. This is done because the color for the
        // ENABLED state is set on the component, but you don't want to use
        // that color for the over state. So we only respect the color
        // specified for the property if it was set by the user, as opposed
        // to set by us.
        if (color == null || color instanceof UIResource) {
            return defaultColor;
        } else if (saturationOffset != 0 || brightnessOffset != 0 || alphaOffset != 0) {
            float[] tmp = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

            tmp[1] = clamp(tmp[1] + saturationOffset);
            tmp[2] = clamp(tmp[2] + brightnessOffset);
            int alpha = clamp(color.getAlpha() + alphaOffset);

            return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24));
        } else {
            return color;
        }
    }

    /**
     * Returns a new color with the alpha of the old color cut in half.
     *
     * @param  color the original color.
     *
     * @return the new color.
     */
    protected Color disable(Color color) {
        return SeaGlassGraphicsUtils.disable(color);
    }

    /**
     * Returns a new color with the saturation cut to one third the original,
     * and the brightness moved one third closer to white.
     *
     * @param  color the original color.
     *
     * @return the new color.
     */
    protected Color desaturate(Color color) {
        float[] tmp = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        tmp[1] /= 3.0f;
        tmp[2] = clamp(1.0f - (1.0f - tmp[2]) / 3f);

        return new Color((Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF));
    }

    /**
     * Returns a new TwoColors object with each color disabled using
     * {@link disable(Color)}.
     *
     * @param  colors the original colors.
     *
     * @return the new colors.
     */
    protected TwoColors disable(TwoColors colors) {
        return new TwoColors(disable(colors.top), disable(colors.bottom));
    }

    /**
     * Returns a new TwoColors object with each color desaturated using
     * {@link desaturate(Color)}.
     *
     * @param  colors the original colors.
     *
     * @return the new colors.
     */
    protected TwoColors desaturate(TwoColors colors) {
        return new TwoColors(desaturate(colors.top), desaturate(colors.bottom));
    }

    /**
     * Returns a new FourColors object with each color disabled using
     * {@link disable(Color)}.
     *
     * @param  colors the original colors.
     *
     * @return the new colors.
     */
    protected FourColors disable(FourColors colors) {
        return new FourColors(disable(colors.top), disable(colors.upperMid), disable(colors.lowerMid), disable(colors.bottom));
    }

    /**
     * Returns a new FourColors object with each color desaturated using
     * {@link desaturate(Color)}.
     *
     * @param  colors the original colors.
     *
     * @return the new colors.
     */
    protected FourColors desaturate(FourColors colors) {
        return new FourColors(desaturate(colors.top), desaturate(colors.upperMid), desaturate(colors.lowerMid), desaturate(colors.bottom));
    }

    // ---------------------- private methods

    /**
     * Paint the component, using a cached image if possible.
     *
     * @param g                 the Graphics2D context to paint with.
     * @param c                 the component to paint.
     * @param w                 the component width.
     * @param h                 the component height.
     * @param extendedCacheKeys extended cache keys.
     */
    private void paintWithCaching(Graphics2D g, JComponent c, int w, int h, Object[] extendedCacheKeys) {
        VolatileImage img = getImage(g.getDeviceConfiguration(), c, w, h, extendedCacheKeys);

        if (img != null) {

            // render cached image
            g.drawImage(img, 0, 0, null);
        } else {

            // render directly
            paintDirectly(g, c, w, h, extendedCacheKeys);
        }
    }

    /**
     * Convenience method which creates a temporary graphics object by creating
     * a clone of the passed in one, configuring it, drawing with it, disposing
     * it. These steps have to be taken to ensure that any hints set on the
     * graphics are removed subsequent to painting.
     *
     * @param g                 the Graphics2D context to paint with.
     * @param c                 the component to paint.
     * @param w                 the component width.
     * @param h                 the component height.
     * @param extendedCacheKeys extended cache keys.
     */
    private void paintDirectly(Graphics2D g, JComponent c, int w, int h, Object[] extendedCacheKeys) {
        g = (Graphics2D) g.create();
        configureGraphics(g);
        doPaint(g, c, w, h, extendedCacheKeys);
        g.dispose();
    }

    /**
     * Gets the rendered image for this painter at the requested size, either
     * from cache or create a new one
     *
     * @param  config            the graphics configuration.
     * @param  c                 the component to paint.
     * @param  w                 the component width.
     * @param  h                 the component height.
     * @param  extendedCacheKeys extended cache keys.
     *
     * @return the new image.
     */
    private VolatileImage getImage(GraphicsConfiguration config, JComponent c, int w, int h, Object[] extendedCacheKeys) {
        ImageCache imageCache = ImageCache.getInstance();

        // get the buffer for this component
        VolatileImage buffer = (VolatileImage) imageCache.getImage(config, w, h, this, extendedCacheKeys);

        int renderCounter = 0; // to avoid any potential, though unlikely,

        // infinite loop
        do {

            // validate the buffer so we can check for surface loss
            int bufferStatus = VolatileImage.IMAGE_INCOMPATIBLE;

            if (buffer != null) {
                bufferStatus = buffer.validate(config);
            }

            // If the buffer status is incompatible or restored, then we need to
            // re-render to the volatile image
            if (bufferStatus == VolatileImage.IMAGE_INCOMPATIBLE || bufferStatus == VolatileImage.IMAGE_RESTORED) {

                // if the buffer is null (hasn't been created), or isn't the
                // right size, or has lost its contents,
                // then recreate the buffer
                if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h
                        || bufferStatus == VolatileImage.IMAGE_INCOMPATIBLE) {

                    // clear any resources related to the old back buffer
                    if (buffer != null) {
                        buffer.flush();
                        buffer = null;
                    }

                    // recreate the buffer
                    buffer = config.createCompatibleVolatileImage(w, h, Transparency.TRANSLUCENT);

                    // put in cache for future
                    imageCache.setImage(buffer, config, w, h, this, extendedCacheKeys);
                }

                // create the graphics context with which to paint to the buffer
                Graphics2D bg = buffer.createGraphics();

                // clear the background before configuring the graphics
                bg.setComposite(AlphaComposite.Clear);
                bg.fillRect(0, 0, w, h);
                bg.setComposite(AlphaComposite.SrcOver);
                configureGraphics(bg);

                // paint the painter into buffer
                paintDirectly(bg, c, w, h, extendedCacheKeys);

                // close buffer graphics
                bg.dispose();
            }
        } while (buffer.contentsLost() && renderCounter++ < 3);

        // check if we failed
        if (renderCounter == 3)
            return null;

        // return image
        return buffer;
    }

    /**
     * Clamp the value to legal limits. These are between 0.0f and 1.0f,
     * inclusive.
     *
     * @param  value the original value.
     *
     * @return the new value.
     */
    private float clamp(float value) {
        if (value < 0) {
            value = 0;
        } else if (value > 1) {
            value = 1;
        }

        return value;
    }

    /**
     * Clamp the value to legal limits. These are between 0 and 255, inclusive.
     *
     * @param  value the original value.
     *
     * @return the new value.
     */
    private int clamp(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }

        return value;
    }

    /**
     * A class encapsulating state useful when painting. Generally, instances of
     * this class are created once, and reused for each paint request without
     * modification. This class contains values useful when hinting the cache
     * engine, and when decoding control points and bezier curve anchors.
     */
    public static class PaintContext {

        /**
         * The cache modes.
         */
        public static enum CacheMode {
            NO_CACHING, FIXED_SIZES
        }

        private CacheMode cacheMode;

        /**
         * Creates a new PaintContext.
         *
         * @param cacheMode A hint as to which caching mode to use. If null,
         *                  then set to no caching.
         */
        public PaintContext(CacheMode cacheMode) {
            this.cacheMode = cacheMode == null ? CacheMode.NO_CACHING : cacheMode;
        }

        /**
         * Returns the cache mode.
         *
         * @return the cache mode.
         */
        public CacheMode getCacheMode() {
            return cacheMode;
        }
    }

    /**
     * Two color gradients.
     */
    public static class TwoColors {

        /** Top (or left) color. */
        public Color top;

        /** Bottom (or right) color. */
        public Color bottom;

        /**
         * Creates a new TwoColors object.
         *
         * @param top    the top (or left) color.
         * @param bottom the bottom (or right) color.
         */
        public TwoColors(Color top, Color bottom) {
            this.top    = top;
            this.bottom = bottom;
        }
    }

    /**
     * Three color gradients.
     */
    public static class ThreeColors extends TwoColors {

        /** Middle color. */
        public Color mid;

        /**
         * Creates a new ThreeColors object.
         *
         * @param top    the top (or left) color.
         * @param mid    the middle color.
         * @param bottom the bottom (or right) color.
         */
        public ThreeColors(Color top, Color mid, Color bottom) {
            super(top, bottom);
            this.mid = mid;
        }
    }

    /**
     * Four color gradients.
     */
    public static class FourColors extends TwoColors {

        /** Upper (or left) middle color. */
        public Color upperMid;

        /** Lower (or right) middle color. */
        public Color lowerMid;

        /**
         * Creates a new FourColors object.
         *
         * @param top      the top (or left) color.
         * @param upperMid the upper (or left) middle color.
         * @param lowerMid the lower (or right) middle color.
         * @param bottom   the bottom (or right) color.
         */
        public FourColors(Color top, Color upperMid, Color lowerMid, Color bottom) {
            super(top, bottom);
            this.upperMid = upperMid;
            this.lowerMid = lowerMid;
        }
    }
}
