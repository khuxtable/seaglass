package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class WarningSign implements Icon {
    
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
        GeneralPath shape0 = new GeneralPath();
        shape0.moveTo(-19.182493, 23.930012);
        shape0.curveTo(-20.776783, 26.717371, -63.513023, 26.805553, -65.098076, 24.03298);
        shape0.curveTo(-66.683136, 21.260406, -45.63019, -15.202528, -42.43162, -15.16815);
        shape0.curveTo(-39.23305, -15.13377, -17.588203, 21.142653, -19.182491, 23.930014);
        shape0.closePath();
        g.setPaint(new LinearGradientPaint(new Point2D.Double(-37.85232925415039, 1.7269972562789917), new Point2D.Double(-58.980552673339844, -3.6761529445648193), new float[] {0.0f,1.0f}, new Color[] {new Color(213, 233, 249, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
        g.fill(shape0);
        g.setPaint(new Color(112, 154, 208, 255));
        g.setStroke(new BasicStroke(1.9253905f,2,1,4.0f,null,0.0f));
        g.draw(shape0);
    }

    private void paintTextNode_0_0_0_1(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        GeneralPath shape1 = new GeneralPath();
        shape1.moveTo(23.345022, 32.095295);
        shape1.curveTo(24.396088, 32.095295, 25.262243, 32.96145, 25.262243, 34.00278);
        shape1.curveTo(25.262243, 35.083042, 24.396088, 35.949196, 23.345022, 35.949196);
        shape1.curveTo(22.274494, 35.949196, 21.408339, 35.083042, 21.408339, 34.00278);
        shape1.curveTo(21.408339, 32.96145, 22.274494, 32.095295, 23.345022, 32.095295);
        shape1.closePath();
        shape1.moveTo(23.831627, 30.35325);
        shape1.lineTo(22.751366, 30.35325);
        shape1.lineTo(22.751366, 29.399508);
        shape1.curveTo(22.751366, 28.260855, 22.53726, 25.720781, 22.30369, 24.436146);
        shape1.lineTo(21.641909, 20.523851);
        shape1.curveTo(21.281822, 18.489845, 21.281822, 18.489845, 21.281822, 17.828064);
        shape1.curveTo(21.281822, 15.79406, 22.001995, 14.81112, 23.442343, 14.81112);
        shape1.curveTo(24.78537, 14.81112, 25.593134, 15.8232565, 25.593134, 17.565298);
        shape1.curveTo(25.593134, 18.188152, 25.43742, 19.239216, 25.174654, 20.611439);
        shape1.lineTo(24.337694, 24.67945);
        shape1.curveTo(24.065197, 26.080868, 23.831627, 28.299782, 23.831627, 29.428703);
        shape1.closePath();
        g.fill(shape1);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
    }

    private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
        // _0_0_0_0
        AffineTransform trans_0_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(0.8519411683082581f, 0.0f, 0.0f, 0.8725070357322693f, 59.39081573486328f, 18.207469940185547f));
        paintShapeNode_0_0_0_0(g);
        g.setTransform(trans_0_0_0_0);
        // _0_0_0_1
        AffineTransform trans_0_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        paintTextNode_0_0_0_1(g);
        g.setTransform(trans_0_0_0_1);
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
        return 3;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 5;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 42;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 38;
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
    public WarningSign() {
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

