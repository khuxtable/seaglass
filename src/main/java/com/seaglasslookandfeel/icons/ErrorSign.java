package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class ErrorSign implements Icon {
    
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
        shape0.moveTo(22.857143, 2.285714);
        shape0.curveTo(26.327536, 10.207261, 26.257889, 6.7617254, 23.110447, 14.817042);
        shape0.curveTo(19.963005, 22.87236, 22.350119, 20.38675, 14.428572, 23.857143);
        shape0.curveTo(6.5070243, 27.327534, 9.95256, 27.257887, 1.897243, 24.110447);
        shape0.curveTo(-6.1580744, 20.963003, -3.6724648, 23.350119, -7.142857, 15.428571);
        shape0.curveTo(-10.613249, 7.507024, -10.543602, 10.95256, -7.39616, 2.8972425);
        shape0.curveTo(-4.2487183, -5.1580744, -6.6358323, -2.6724653, 1.2857151, -6.1428576);
        shape0.curveTo(9.207262, -9.61325, 5.7617264, -9.543603, 13.817043, -6.396161);
        shape0.curveTo(21.87236, -3.2487192, 19.386751, -5.6358333, 22.857143, 2.2857141);
        shape0.closePath();
        g.setPaint(new LinearGradientPaint(new Point2D.Double(11.280372619628906, 10.725106239318848), new Point2D.Double(-7.898429870605469, 3.1176528930664062), new float[] {0.0f,1.0f}, new Color[] {new Color(213, 233, 249, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
        g.fill(shape0);
        g.setPaint(new Color(112, 154, 208, 255));
        g.setStroke(new BasicStroke(1.1198422f,1,0,4.0f,null,0.0f));
        g.draw(shape0);
    }

    private void paintTextNode_0_0_0_1(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        GeneralPath shape1 = new GeneralPath();
        shape1.moveTo(22.591343, 27.46657);
        shape1.lineTo(29.291311, 38.11756);
        shape1.lineTo(24.194117, 38.11756);
        shape1.lineTo(20.187181, 30.877123);
        shape1.lineTo(16.208202, 38.11756);
        shape1.lineTo(11.232148, 38.11756);
        shape1.lineTo(17.810976, 27.615665);
        shape1.lineTo(11.455791, 17.253544);
        shape1.lineTo(16.552986, 17.253544);
        shape1.lineTo(20.215137, 24.121244);
        shape1.lineTo(24.05434, 17.253544);
        shape1.lineTo(29.030394, 17.253544);
        shape1.closePath();
        g.fill(shape1);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
    }

    private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
        // _0_0_0_0
        AffineTransform trans_0_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.1813052892684937f, 0.5089349150657654f, -0.4840554893016815f, 1.1700929403305054f, 18.924236297607422f, 9.711734771728516f));
        paintShapeNode_0_0_0_0(g);
        g.setTransform(trans_0_0_0_0);
        // _0_0_0_1
        AffineTransform trans_0_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.1537352800369263f, 0.0f, 0.0f, 0.8667499423027039f, 0.0f, 0.0f));
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
        return 0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 0;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 48;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 48;
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
    public ErrorSign() {
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

