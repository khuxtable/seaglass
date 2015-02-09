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
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.JSlider;

import com.seaglasslookandfeel.effect.SeaGlassInternalShadowEffect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeGenerator.CornerSize;

/**
 * Nimbus's SliderTrackPainter.
 */
public final class SliderTrackPainter extends AbstractCommonColorsPainter {
    
    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static enum Which {
        BACKGROUND_DISABLED, BACKGROUND_ENABLED
    }

    private Color sliderTrackInteriorBase = decodeColor("sliderTrackInteriorBase");

    private TwoColors sliderTrackInteriorEnabled  = new TwoColors(deriveColor(sliderTrackInteriorBase, 0f, 0f, 0.078431f, 0),
                                                                  deriveColor(sliderTrackInteriorBase, 0f, 0f, 0.474510f, 0));
    
    private TwoColors sliderTrackInteriorDisabled = disable(sliderTrackInteriorEnabled);
    
    private FourColors  interiorValueEnabled      = getCommonInteriorColors(CommonControlState.SELECTED);
    private FourColors  interiorValueDisabled     = disable(interiorValueEnabled);

    SeaGlassInternalShadowEffect effect = new SeaGlassInternalShadowEffect();

    private Which        state;
    private PaintContext ctx;

    /**
     * Creates a new SliderTrackPainter object.
     *
     * @param state DOCUMENT ME!
     */
    public SliderTrackPainter(Which state) {
        super();
        this.state = state;
        this.ctx   = new PaintContext(CacheMode.NO_CACHING);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        Shape s = shapeGenerator.createRoundRectangle(0, 0, width, height, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSliderTrackBorderPaint(s));
        g.fill(s);

        s = shapeGenerator.createRoundRectangle(1, 1, width - 2, height - 2, CornerSize.ROUND_HEIGHT);
        g.setPaint(getSliderTrackInteriorPaint(s));
        g.fill(s);
        
        effect.fill(g, s, false, false);
        
        paintValueTrack(g, c, width, height);
    }
    
    
    /**
     * @param g
     * @param c
     * @param width
     * @param height
     */
    private void paintValueTrack(Graphics2D g, JComponent c, int width, int height) {
          JSlider slider = (JSlider) c;
          int orientation = slider.getOrientation();
          double trackLength = slider.getMaximum()-slider.getMinimum();
          double percentFilled = (slider.getValue()-slider.getMinimum()) / trackLength;
          if ( "LogarithmicJSlider".equals( c.getClass().getSimpleName() ) ) {
              percentFilled = (Math.log((slider.getValue()))-Math.log(slider.getMinimum())) / 
              ((double) Math.log(slider.getMaximum()) - (double) Math.log(slider.getMinimum()));
          }
          if (percentFilled > 0) {
              Shape s = getValueShape(c, width, height, orientation, percentFilled);
              g.setPaint(getValuePaint(s));
              g.fill(s);
          }
    }

    /**
     * @param width
     * @param height
     * @param orientation
     * @param percentFilled
     * @return
     */
    private Shape getValueShape(JComponent c,int width, int height, int orientation, double percentFilled) {
        Shape s; 
        JSlider slider = (JSlider)c;
        if ((orientation == JSlider.HORIZONTAL && slider.getComponentOrientation().isLeftToRight()) || slider.getInverted()) { 
              s = shapeGenerator.createRoundRectangle(1, 1, (int) (width*percentFilled), height - 2, CornerSize.ROUND_HEIGHT);
          } else {
              s = shapeGenerator.createRoundRectangle(width-(int)(width*percentFilled), 1, width, height - 2, CornerSize.ROUND_HEIGHT);
          }
        return s;
    }
    
    /**
     * @param s
     * @param type
     * @return
     */
    public Paint getValuePaint(Shape s) {
        FourColors colors = getValueColors();
        return createVerticalGradient(s, colors);
    }
    
    /**
     * @param type
     * @return
     */
    private FourColors getValueColors() {
        switch (state) {
        case BACKGROUND_ENABLED:
            return interiorValueEnabled;
        case BACKGROUND_DISABLED:
            return interiorValueDisabled;
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    protected PaintContext getPaintContext() {
        return ctx;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getSliderTrackBorderPaint(Shape s) {
        return getSliderTrackBorderColors();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Paint getSliderTrackInteriorPaint(Shape s) {
        return createVerticalGradient(s, getSliderTrackInteriorColors());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Paint getSliderTrackBorderColors() {
        switch (state) {

        case BACKGROUND_DISABLED:
            return getTextBorderPaint(CommonControlState.DISABLED,false);

        case BACKGROUND_ENABLED:
            return getTextBorderPaint(CommonControlState.ENABLED,false);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private TwoColors getSliderTrackInteriorColors() {
        switch (state) {

        case BACKGROUND_DISABLED:
            return sliderTrackInteriorDisabled; //getCommonInteriorColors(CommonControlState.DISABLED_SELECTED);

        case BACKGROUND_ENABLED:
            return  sliderTrackInteriorEnabled;  //getCommonInteriorColors(CommonControlState.ENABLED_SELECTED);
        }
        return null;
    }
}
