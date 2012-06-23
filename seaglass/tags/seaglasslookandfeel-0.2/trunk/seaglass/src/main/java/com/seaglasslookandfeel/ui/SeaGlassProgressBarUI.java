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
package com.seaglasslookandfeel.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.painter.util.ShapeGenerator;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * SeaGlassProgressBarUI implementation.
 * 
 * Based on SynthProgressBarUI by Joshua Outwater.
 * 
 * @see javax.swing.plaf.synth.SynthProgressBarUI
 */
public class SeaGlassProgressBarUI extends BasicProgressBarUI implements SeaglassUI, PropertyChangeListener {
    private SynthStyle style;
    private int        progressPadding;
    // added for Nimbus LAF
    private boolean    rotateText;
    private boolean    paintOutsideClip;
    private int        trackThickness;
    // whether to tile indeterminate painting
    private boolean    tileWhenIndeterminate;
    // the width of each tile
    private int        tileWidth;
    private Color      bgFillColor;

    private Rectangle  boundsRect     = new Rectangle();
    private Rectangle  savedRect      = new Rectangle();

    private ShapeGenerator  shapeGenerator = new ShapeGenerator();

    public static ComponentUI createUI(JComponent x) {
        return new SeaGlassProgressBarUI();
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        progressBar.addPropertyChangeListener(this);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        progressBar.removePropertyChangeListener(this);
    }

    @Override
    protected void installDefaults() {
        updateStyle(progressBar);
    }

    private void updateStyle(JProgressBar c) {
        SeaGlassContext context = getContext(c, ENABLED);
        // SynthStyle oldStyle = style;
        style = SeaGlassLookAndFeel.updateStyle(context, this);
        setCellLength(style.getInt(context, "ProgressBar.cellLength", 1));
        setCellSpacing(style.getInt(context, "ProgressBar.cellSpacing", 0));
        progressPadding = style.getInt(context, "ProgressBar.progressPadding", 0);
        paintOutsideClip = style.getBoolean(context, "ProgressBar.paintOutsideClip", false);
        rotateText = style.getBoolean(context, "ProgressBar.rotateText", false);
        tileWhenIndeterminate = style.getBoolean(context, "ProgressBar.tileWhenIndeterminate", false);
        trackThickness = style.getInt(context, "ProgressBar.trackThickness", 19);
        tileWidth = style.getInt(context, "ProgressBar.tileWidth", 15);
        bgFillColor = (Color) style.get(context, "ProgressBar.backgroundFillColor");
        if (bgFillColor == null) {
            bgFillColor = Color.white;
        }
        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = SeaGlassStyle.getSizeVariant(progressBar);
        if (scaleKey != null) {
            if (SeaGlassStyle.LARGE_KEY.equals(scaleKey)) {
                trackThickness = 24;
                tileWidth *= 1.15;
            } else if (SeaGlassStyle.SMALL_KEY.equals(scaleKey)) {
                trackThickness = 17;
                tileWidth *= 0.857;
            } else if (SeaGlassStyle.MINI_KEY.equals(scaleKey)) {
                trackThickness = 15;
                tileWidth *= 0.784;
            }
        }
        context.dispose();
    }
    
    /**
     * Overwritten to fix a very strange issue on Java 7 in BasicProgressBarUI
     * The super method does exactly the same thing but has vertDim == null
     * We with the same code in our overwritten method get a correct vertDim?
     * Why? I don't know, but it fixes the issue.
     * As a small modification I adjusted only the default hardcoded values to 19/150.
     */
    protected Dimension getPreferredInnerVertical() {
        Dimension vertDim = (Dimension)DefaultLookup.get(progressBar, this,
            "ProgressBar.vertictalSize");
        if (vertDim == null) {
            vertDim = new Dimension(19, 150);
        }
        return vertDim;
    }

    @Override
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(progressBar, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        return SeaGlassLookAndFeel.getComponentState(c);
    }

    @Override
    public int getBaseline(JComponent c, int width, int height) {
        super.getBaseline(c, width, height);
        if (progressBar.isStringPainted() && progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            SeaGlassContext context = getContext(c);
            Font font = context.getStyle().getFont(context);
            FontMetrics metrics = progressBar.getFontMetrics(font);
            context.dispose();
            return (height - metrics.getAscent() - metrics.getDescent()) / 2 + metrics.getAscent();
        }
        return -1;
    }

    @Override
    protected Rectangle getBox(Rectangle r) {
        if (tileWhenIndeterminate) {
            return SwingUtilities.calculateInnerArea(progressBar, r);
        } else {
            return super.getBox(r);
        }
    }

    @Override
    protected void setAnimationIndex(int newValue) {
        if (paintOutsideClip) {
            if (getAnimationIndex() == newValue) {
                return;
            }
            super.setAnimationIndex(newValue);
            progressBar.repaint();
        } else {
            super.setAnimationIndex(newValue);
        }
    }

    private Rectangle calcBounds(JProgressBar pBar) {
        boundsRect.x = 0;
        boundsRect.y = 0;
        boundsRect.width = pBar.getWidth();
        boundsRect.height = pBar.getHeight();
        if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
            boundsRect.y = (boundsRect.height - trackThickness) / 2;
            boundsRect.height = Math.min(boundsRect.height, trackThickness);
        } else {
            boundsRect.x = (boundsRect.width - trackThickness) / 2;
            boundsRect.width = Math.min(boundsRect.width, trackThickness);
        }
        return boundsRect;
    }

    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        JProgressBar pBar = (JProgressBar) c;
        Rectangle bounds = calcBounds(pBar);
        context.getPainter().paintProgressBarBackground(context, g, bounds.x, bounds.y, bounds.width, bounds.height, pBar.getOrientation());
        paint(context, g);
        context.dispose();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        JProgressBar pBar = (JProgressBar) context.getComponent();
        Insets pBarInsets = pBar.getInsets();
        Rectangle bounds = calcBounds(pBar);
        // Save away the track bounds.
        savedRect.setBounds(bounds);
        // Subtract out any insets for the progress indicator.
        bounds.x += pBarInsets.left + progressPadding;
        bounds.y += pBarInsets.top + progressPadding;
        bounds.width -= pBarInsets.left + pBarInsets.right + progressPadding + progressPadding;
        bounds.height -= pBarInsets.top + pBarInsets.bottom + progressPadding + progressPadding;

        int size = 0;
        boolean isFinished = false;
        if (!pBar.isIndeterminate()) {
            double percentComplete = pBar.getPercentComplete();
            if (percentComplete == 1.0) {
                isFinished = true;
            } else if (percentComplete > 0.0) {
                if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                    size = (int) (percentComplete * bounds.width);
                } else { // JProgressBar.VERTICAL
                    size = (int) (percentComplete * bounds.height);
                }
            }
        }

        // Create a translucent intermediate image in which we can perform soft
        // clipping.
        GraphicsConfiguration gc = ((Graphics2D) g).getDeviceConfiguration();
        BufferedImage img = gc.createCompatibleImage(bounds.width, bounds.height, Transparency.TRANSLUCENT);
        Graphics2D g2d = img.createGraphics();

        // Clear the image so all pixels have zero alpha
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, bounds.width, bounds.height);

        // Render our clip shape into the image. Enable antialiasing to achieve
        // a soft clipping effect.
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bgFillColor);
        CornerSize cornerSize = pBar.getOrientation() == JProgressBar.HORIZONTAL ? CornerSize.ROUND_HEIGHT : CornerSize.ROUND_WIDTH;
        g2d.fill(shapeGenerator.createRoundRectangle(0, 0, bounds.width, bounds.height, cornerSize));

        // Use SrcAtop, which effectively uses the alpha value as a coverage
        // value for each pixel stored in the destination. At the edges, the
        // antialiasing of the rounded rectangle gives us the desired soft
        // clipping effect.
        g2d.setComposite(AlphaComposite.SrcAtop);

        // We need to redraw the background, otherwise the interior is
        // completely white.
        context.getPainter().paintProgressBarBackground(context, g2d, savedRect.x - bounds.x, savedRect.y - bounds.y, savedRect.width,
            savedRect.height, pBar.getOrientation());
        paintProgressIndicator(context, g2d, bounds.width, bounds.height, size, isFinished);

        // Dispose of the image graphics and copy our intermediate image to the
        // main graphics.
        g2d.dispose();
        g.drawImage(img, bounds.x, bounds.y, null);

        if (pBar.isStringPainted()) {
            paintText(context, g, pBar.getString());
        }
    }

    /**
     * Paint the actual internal progress bar.
     * 
     * @param context
     * @param g2d
     * @param width
     * @param height
     * @param size
     * @param isFinished
     */
    private void paintProgressIndicator(SeaGlassContext context, Graphics2D g2d, int width, int height, int size, boolean isFinished) {
        JProgressBar pBar = (JProgressBar) context.getComponent();

        if (tileWhenIndeterminate && pBar.isIndeterminate()) {
            double offsetFraction = (double) getAnimationIndex() / (double) getFrameCount();
            int offset = (int) (offsetFraction * tileWidth);
            if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                // If we're right-to-left, flip the direction of animation.
                if (!SeaGlassLookAndFeel.isLeftToRight(pBar)) {
                    offset = tileWidth - offset;
                }
                // paint each tile horizontally
                for (int i = -tileWidth + offset; i <= width; i += tileWidth) {
                    context.getPainter().paintProgressBarForeground(context, g2d, i, 0, tileWidth, height, pBar.getOrientation());
                }
            } else {
                // paint each tile vertically
                for (int i = -offset; i < height + tileWidth; i += tileWidth) {
                    context.getPainter().paintProgressBarForeground(context, g2d, 0, i, width, tileWidth, pBar.getOrientation());
                }
            }
        } else {
            if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                int start = 0;
                if (isFinished) {
                    size = width;
                } else if (!SeaGlassLookAndFeel.isLeftToRight(pBar)) {
                    start = width - size;
                }
                context.getPainter().paintProgressBarForeground(context, g2d, start, 0, size, height, pBar.getOrientation());
            } else {
                // When the progress bar is vertical we always paint from bottom
                // to top, not matter what the component orientation is.
                int start = height;
                if (isFinished) {
                    size = height;
                }
                context.getPainter().paintProgressBarForeground(context, g2d, 0, start, width, size, pBar.getOrientation());
            }
        }
    }

    protected void paintText(SeaGlassContext context, Graphics g, String title) {
        if (progressBar.isStringPainted()) {
            SynthStyle style = context.getStyle();
            Font font = style.getFont(context);
            FontMetrics fm = SwingUtilities2.getFontMetrics(progressBar, g, font);
            int strLength = style.getGraphicsUtils(context).computeStringWidth(context, font, fm, title);
            Rectangle bounds = progressBar.getBounds();

            if (rotateText && progressBar.getOrientation() == JProgressBar.VERTICAL) {
                Graphics2D g2 = (Graphics2D) g;
                // Calculate the position for the text.
                Point textPos;
                AffineTransform rotation;
                if (progressBar.getComponentOrientation().isLeftToRight()) {
                    rotation = AffineTransform.getRotateInstance(-Math.PI / 2);
                    textPos = new Point((bounds.width + fm.getAscent() - fm.getDescent()) / 2, (bounds.height + strLength) / 2);
                } else {
                    rotation = AffineTransform.getRotateInstance(Math.PI / 2);
                    textPos = new Point((bounds.width - fm.getAscent() + fm.getDescent()) / 2, (bounds.height - strLength) / 2);
                }

                // Progress bar isn't wide enough for the font. Don't paint it.
                if (textPos.x < 0) {
                    return;
                }

                // Paint the text.
                font = font.deriveFont(rotation);
                g2.setFont(font);
                g2.setColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
                style.getGraphicsUtils(context).paintText(context, g, title, textPos.x, textPos.y, -1);
            } else {
                // Calculate the bounds for the text.
            	// Rossi: Move text down by one pixel: Looks better but is a hack that may not look good for other font sizes / fonts
                Rectangle textRect = new Rectangle((bounds.width / 2) - (strLength / 2),
                    (bounds.height - (fm.getAscent() + fm.getDescent())) / 2+1, 0, 0);

                // Progress bar isn't tall enough for the font. Don't paint it.
                if (textRect.y < 0) {
                    return;
                }

                // Paint the text.
                g.setColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
                g.setFont(font);
                style.getGraphicsUtils(context).paintText(context, g, title, textRect.x, textRect.y, -1);
            }
        }
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintProgressBarBorder(context, g, x, y, w, h, progressBar.getOrientation());
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e) || "indeterminate".equals(e.getPropertyName())) {
            updateStyle((JProgressBar) e.getSource());
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension size = null;
        Insets border = progressBar.getInsets();
        FontMetrics fontSizer = progressBar.getFontMetrics(progressBar.getFont());
        String progString = progressBar.getString();
        int stringHeight = fontSizer.getHeight() + fontSizer.getDescent();

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            size = new Dimension(getPreferredInnerHorizontal());
            if (progressBar.isStringPainted()) {
                // adjust the height if necessary to make room for the string
                if (stringHeight > size.height) {
                    size.height = stringHeight;
                }

                // adjust the width if necessary to make room for the string
                int stringWidth = SwingUtilities2.stringWidth(progressBar, fontSizer, progString);
                if (stringWidth > size.width) {
                    size.width = stringWidth;
                }
            }
        } else {
            size = new Dimension(getPreferredInnerVertical());
            if (progressBar.isStringPainted()) {
                // make sure the width is big enough for the string
                if (stringHeight > size.width) {
                    size.width = stringHeight;
                }

                // make sure the height is big enough for the string
                int stringWidth = SwingUtilities2.stringWidth(progressBar, fontSizer, progString);
                if (stringWidth > size.height) {
                    size.height = stringWidth;
                }
            }
        }

        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = SeaGlassStyle.getSizeVariant(progressBar);
        if (scaleKey != null) {
            if (SeaGlassStyle.LARGE_KEY.equals(scaleKey)) {
                size.width *= 1.15f;
                size.height *= 1.15f;
            } else if (SeaGlassStyle.SMALL_KEY.equals(scaleKey)) {
                size.width *= 0.90f;
                size.height *= 0.90f;
            } else if (SeaGlassStyle.MINI_KEY.equals(scaleKey)) {
                size.width *= 0.784f;
                size.height *= 0.784f;
            }
        }

        size.width += border.left + border.right;
        size.height += border.top + border.bottom;

        return size;
    }
}
