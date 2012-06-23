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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.icons;

/**
 * @author Kathryn Huxtable
 *
 */

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class FileIcon implements Icon {
    
    private float origAlpha = 1.0f;

    /**
     * Paints the transcoded SVG image on the specified graphics context. You
     * can install a custom transformation on the graphics context to scale the
     * image.
     * 
     * @param g
     *            Graphics context.
     */
    public void paint(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
        // _0
        AffineTransform trans_0 = g.getTransform();
        paintRootGraphicsNode_0(g);
        g.setTransform(trans_0);

    }

    private void paintShapeNode_0_0_0_0(Graphics2D g) {
        RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double(2.303576946258545, 0.9705909490585327, 11.702507972717285, 14.64108943939209, 1.711881160736084, 1.711881160736084);
        g.setPaint(new Color(119, 119, 118, 91));
        g.fill(shape0);
    }

    private void paintShapeNode_0_0_0_1(Graphics2D g) {
        RoundRectangle2D.Double shape1 = new RoundRectangle2D.Double(2.0128300189971924, 0.6831681728363037, 11.702507972717285, 14.64108943939209, 1.711881160736084, 1.711881160736084);
        g.setPaint(new Color(255, 255, 255, 255));
        g.fill(shape1);
        g.setPaint(new Color(0, 119, 203, 161));
        g.setStroke(new BasicStroke(0.25f,0,2,4.0f,null,0.0f));
        g.draw(shape1);
    }

    private void paintShapeNode_0_0_0_2(Graphics2D g) {
        Rectangle2D.Double shape2 = new Rectangle2D.Double(2.3876237869262695, 1.4490097761154175, 10.811881065368652, 0.5097317695617676);
        g.fill(shape2);
    }

    private void paintShapeNode_0_0_0_3(Graphics2D g) {
        Rectangle2D.Double shape3 = new Rectangle2D.Double(2.32004976272583, 2.365431070327759, 10.811881065368652, 0.5097317695617676);
        g.fill(shape3);
    }

    private void paintShapeNode_0_0_0_4(Graphics2D g) {
        Rectangle2D.Double shape4 = new Rectangle2D.Double(2.3087871074676514, 3.2361807823181152, 5.24826717376709, 0.5097317695617676);
        g.fill(shape4);
    }

    private void paintShapeNode_0_0_0_5(Graphics2D g) {
        Rectangle2D.Double shape5 = new Rectangle2D.Double(2.241213083267212, 4.152602195739746, 8.709463119506836, 0.5097317695617676);
        g.fill(shape5);
    }

    private void paintShapeNode_0_0_0_6(Graphics2D g) {
        Rectangle2D.Double shape6 = new Rectangle2D.Double(2.2392139434814453, 5.04191780090332, 10.811881065368652, 0.5097317695617676);
        g.fill(shape6);
    }

    private void paintShapeNode_0_0_0_7(Graphics2D g) {
        RoundRectangle2D.Double shape7 = new RoundRectangle2D.Double(7.039913177490234, 5.997589111328125, 5.957370281219482, 5.956849575042725, 1.711881160736084, 1.711881160736084);
        g.setStroke(new BasicStroke(0.19636719f,0,0,4.0f,null,0.0f));
        g.draw(shape7);
    }

    private void paintShapeNode_0_0_0_8(Graphics2D g) {
        Rectangle2D.Double shape8 = new Rectangle2D.Double(2.2815072536468506, 6.50723934173584, 4.356332302093506, 0.5097317695617676);
        g.fill(shape8);
    }

    private void paintShapeNode_0_0_0_9(Graphics2D g) {
        Rectangle2D.Double shape9 = new Rectangle2D.Double(2.313361883163452, 7.367318153381348, 4.356332302093506, 0.5097317695617676);
        g.fill(shape9);
    }

    private void paintShapeNode_0_0_0_10(Graphics2D g) {
        Rectangle2D.Double shape10 = new Rectangle2D.Double(2.329289436340332, 8.593729019165039, 4.356332302093506, 0.5097317695617676);
        g.fill(shape10);
    }

    private void paintShapeNode_0_0_0_11(Graphics2D g) {
        Rectangle2D.Double shape11 = new Rectangle2D.Double(2.3611440658569336, 9.453808784484863, 4.356332302093506, 0.5097317695617676);
        g.fill(shape11);
    }

    private void paintShapeNode_0_0_0_12(Graphics2D g) {
        Rectangle2D.Double shape12 = new Rectangle2D.Double(2.26557993888855, 10.600582122802734, 4.356332302093506, 0.5097317695617676);
        g.fill(shape12);
    }

    private void paintShapeNode_0_0_0_13(Graphics2D g) {
        Rectangle2D.Double shape13 = new Rectangle2D.Double(2.2974345684051514, 11.460661888122559, 4.356332302093506, 0.5097317695617676);
        g.fill(shape13);
    }

    private void paintShapeNode_0_0_0_14(Graphics2D g) {
        Rectangle2D.Double shape14 = new Rectangle2D.Double(2.271068811416626, 12.432233810424805, 10.811881065368652, 0.5097317695617676);
        g.fill(shape14);
    }

    private void paintShapeNode_0_0_0_15(Graphics2D g) {
        Rectangle2D.Double shape15 = new Rectangle2D.Double(2.271069049835205, 13.292313575744629, 8.709463119506836, 0.5097317695617676);
        g.fill(shape15);
    }

    private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
        // _0_0_0_0
        AffineTransform trans_0_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_0(g);
        g.setTransform(trans_0_0_0_0);
        // _0_0_0_1
        AffineTransform trans_0_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_1(g);
        g.setTransform(trans_0_0_0_1);
        // _0_0_0_2
        AffineTransform trans_0_0_0_2 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_2(g);
        g.setTransform(trans_0_0_0_2);
        // _0_0_0_3
        AffineTransform trans_0_0_0_3 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_3(g);
        g.setTransform(trans_0_0_0_3);
        // _0_0_0_4
        AffineTransform trans_0_0_0_4 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_4(g);
        g.setTransform(trans_0_0_0_4);
        // _0_0_0_5
        AffineTransform trans_0_0_0_5 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_5(g);
        g.setTransform(trans_0_0_0_5);
        // _0_0_0_6
        AffineTransform trans_0_0_0_6 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_6(g);
        g.setTransform(trans_0_0_0_6);
        // _0_0_0_7
        AffineTransform trans_0_0_0_7 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_7(g);
        g.setTransform(trans_0_0_0_7);
        // _0_0_0_8
        AffineTransform trans_0_0_0_8 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_8(g);
        g.setTransform(trans_0_0_0_8);
        // _0_0_0_9
        AffineTransform trans_0_0_0_9 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_9(g);
        g.setTransform(trans_0_0_0_9);
        // _0_0_0_10
        AffineTransform trans_0_0_0_10 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_10(g);
        g.setTransform(trans_0_0_0_10);
        // _0_0_0_11
        AffineTransform trans_0_0_0_11 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_11(g);
        g.setTransform(trans_0_0_0_11);
        // _0_0_0_12
        AffineTransform trans_0_0_0_12 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_12(g);
        g.setTransform(trans_0_0_0_12);
        // _0_0_0_13
        AffineTransform trans_0_0_0_13 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_13(g);
        g.setTransform(trans_0_0_0_13);
        // _0_0_0_14
        AffineTransform trans_0_0_0_14 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_14(g);
        g.setTransform(trans_0_0_0_14);
        // _0_0_0_15
        AffineTransform trans_0_0_0_15 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintShapeNode_0_0_0_15(g);
        g.setTransform(trans_0_0_0_15);
    }

    private void paintCanvasGraphicsNode_0_0(Graphics2D g) {
        // _0_0_0
        AffineTransform trans_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintCompositeGraphicsNode_0_0_0(g);
        g.setTransform(trans_0_0_0);
    }

    private void paintRootGraphicsNode_0(Graphics2D g) {
        // _0_0
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform trans_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintCanvasGraphicsNode_0_0(g);
        g.setTransform(trans_0_0);
    }



    /**
     * Returns the X of the bounding box of the original SVG image.
     * @return The X of the bounding box of the original SVG image.
     */
    public int getOrigX() {
        return 2;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 1;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 13;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 16;
    }
    
    
    /**
     * The current width of this resizable icon.
     */
    int width;

    /**
     * The current height of this resizable icon.
     */
    int height;

    /**
     * Creates a new transcoded SVG image.
     */
    public FileIcon() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.Icon#getIconHeight()
     */
    @Override
    public int getIconHeight() {
        return height;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.Icon#getIconWidth()
     */
    @Override
    public int getIconWidth() {
        return width;
    }

    /*
     * Set the dimension of the icon.
     */

    public void setDimension(Dimension newDimension) {
        this.width = newDimension.width;
        this.height = newDimension.height;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x, y);

        double coef1 = (double) this.width / (double) getOrigWidth();
        double coef2 = (double) this.height / (double) getOrigHeight();
        double coef = Math.min(coef1, coef2);
        g2d.scale(coef, coef);
        paint(g2d);
        g2d.dispose();
    }
}

