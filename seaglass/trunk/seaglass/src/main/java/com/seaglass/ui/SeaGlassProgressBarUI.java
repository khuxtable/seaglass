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
package com.seaglass.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
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

import com.seaglass.SeaGlassContext;
import com.seaglass.SeaGlassLookAndFeel;

import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthUI;

/**
 * SeaGlassProgressBarUI implementation.
 * 
 * Based on SynthProgressBarUI by Joshua Outwater.
 * 
 * @see javax.swing.plaf.synth.SynthProgressBarUI
 */
public class SeaGlassProgressBarUI extends BasicProgressBarUI implements SynthUI, PropertyChangeListener {
    private SynthStyle style;
    private int        progressPadding;
    private boolean    rotateText;           // added for Nimbus LAF
    private boolean    paintOutsideClip;
    private int        trackThickness;
    private int        arcSize;
    private int        progressRightInset;
    private boolean    tileWhenIndeterminate; // whether to tile indeterminate
    // painting
    private int        tileWidth;            // the width of each tile

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
        arcSize = style.getInt(context, "ProgressBar.arcSize", 9);
        progressRightInset = style.getInt(context, "ProgressBar.progressRightInset", 2);
        tileWidth = style.getInt(context, "ProgressBar.tileWidth", 15);
        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = (String) progressBar.getClientProperty("JComponent.sizeVariant");
        if (scaleKey != null) {
            if ("large".equals(scaleKey)) {
                trackThickness = 24;
                tileWidth *= 1.15;
            } else if ("small".equals(scaleKey)) {
                trackThickness = 17;
                tileWidth *= 0.857;
            } else if ("mini".equals(scaleKey)) {
                trackThickness = 15;
                tileWidth *= 0.784;
            }
        }
        context.dispose();
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

    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        JProgressBar pBar = (JProgressBar) c;
        int x = 0;
        int y = 0;
        int width = c.getWidth();
        int height = c.getHeight();
        if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
            height = Math.min(height, trackThickness);
            y += (height - trackThickness) / 2;
        } else {
            width = Math.min(width, trackThickness);
            x += (width - trackThickness) / 2;
        }
        context.getPainter().paintProgressBarBackground(context, g, x, y, width, height, pBar.getOrientation());
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
        int x = 0;
        int y = 0;
        int width = pBar.getWidth();
        int height = pBar.getHeight();
        if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
            height = Math.min(height, trackThickness);
            y += (height - trackThickness) / 2;
        } else {
            width = Math.min(width, trackThickness);
            x += (width - trackThickness) / 2;
        }
        x += pBarInsets.left + progressPadding;
        y += pBarInsets.top + progressPadding;
        width -= pBarInsets.left + pBarInsets.right + progressPadding + progressPadding;
        height -= pBarInsets.top + pBarInsets.bottom + progressPadding + progressPadding;

        int size = 0;
        boolean isFinished = false;
        if (!pBar.isIndeterminate()) {
            double percentComplete = pBar.getPercentComplete();
            if (percentComplete == 1.0) {
                isFinished = true;
            } else if (percentComplete > 0.0) {
                if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                    size = (int) (percentComplete * width);
                } else { // JProgressBar.VERTICAL
                    size = (int) (percentComplete * height);
                }
            }
        }

        // if tiling and indeterminate, then paint the progress bar foreground a
        // bit wider than it should be. Shift as needed to ensure that there is
        // an animated effect
        Shape clip = g.getClip();
        Graphics2D g2d = (Graphics2D) g;
        g2d.clip(new RoundRectangle2D.Float(x, y, width, height, arcSize, arcSize));
        if (tileWhenIndeterminate && pBar.isIndeterminate()) {
            double percentComplete = (double) getAnimationIndex() / (double) getFrameCount();
            int offset = (int) (percentComplete * tileWidth);
            if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                // If we're right-to-left, flip the direction of animation.
                if (!SeaGlassLookAndFeel.isLeftToRight(pBar)) {
                    offset = tileWidth - offset;
                }
                // paint each tile horizontally
                for (int i = x - tileWidth + offset; i <= width; i += tileWidth) {
                    context.getPainter().paintProgressBarForeground(context, g2d, i, y, tileWidth, height, pBar.getOrientation());
                }
            } else { // JProgressBar.VERTICAL
                // paint each tile vertically
                for (int i = y - offset; i < height + tileWidth; i += tileWidth) {
                    context.getPainter().paintProgressBarForeground(context, g2d, x, i, width, tileWidth, pBar.getOrientation());
                }
            }
        } else {
            if (pBar.getOrientation() == JProgressBar.HORIZONTAL) {
                int start = x;
                if (isFinished) {
                    size = width + progressRightInset;
                } else if (!SeaGlassLookAndFeel.isLeftToRight(pBar)) {
                    start = x + (width - size);
                    size = width - size;
                }
                context.getPainter().paintProgressBarForeground(context, g2d, start, y, size, height, pBar.getOrientation());
            } else {
                // When the progress bar is vertical we always paint from bottom
                // to top, not matter what the component orientation is.
                int start = y + height;
                if (isFinished) {
                    size = height + progressRightInset;
                }
                context.getPainter().paintProgressBarForeground(context, g2d, x, start, width, size, pBar.getOrientation());
            }
        }
        g.setClip(clip);

        if (pBar.isStringPainted()) {
            paintText(context, g, pBar.getString());
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
                Rectangle textRect = new Rectangle((bounds.width / 2) - (strLength / 2),
                    (bounds.height - (fm.getAscent() + fm.getDescent())) / 2, 0, 0);

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
        String scaleKey = (String) progressBar.getClientProperty("JComponent.sizeVariant");
        if (scaleKey != null) {
            if ("large".equals(scaleKey)) {
                size.width *= 1.15f;
                size.height *= 1.15f;
            } else if ("small".equals(scaleKey)) {
                size.width *= 0.90f;
                size.height *= 0.90f;
            } else if ("mini".equals(scaleKey)) {
                size.width *= 0.784f;
                size.height *= 0.784f;
            }
        }

        size.width += border.left + border.right;
        size.height += border.top + border.bottom;

        return size;
    }
}
