package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class InfoSign implements Icon {
    
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
        shape0.moveTo(45.857143, 27.857143);
        shape0.curveTo(45.857143, 37.798267, 37.798267, 45.857143, 27.857143, 45.857143);
        shape0.curveTo(17.916018, 45.857143, 9.857143, 37.798267, 9.857143, 27.857143);
        shape0.curveTo(9.857143, 17.916018, 17.916018, 9.857143, 27.857143, 9.857143);
        shape0.curveTo(37.798267, 9.857143, 45.857143, 17.916018, 45.857143, 27.857143);
        shape0.closePath();
        g.setPaint(new LinearGradientPaint(new Point2D.Double(28.212133407592773, 30.697072982788086), new Point2D.Double(21.69320297241211, 11.333908081054688), new float[] {0.0f,1.0f}, new Color[] {new Color(213, 233, 249, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
        g.fill(shape0);
        g.setPaint(new Color(112, 154, 208, 255));
        g.setStroke(new BasicStroke(1.5f,1,0,4.0f,null,0.0f));
        g.draw(shape0);
    }

    private void paintTextNode_0_0_0_1(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        GeneralPath shape1 = new GeneralPath();
        shape1.moveTo(23.404142, 31.87479);
        shape1.curveTo(24.69564, 31.87479, 25.75993, 32.93908, 25.75993, 34.21862);
        shape1.curveTo(25.75993, 35.545994, 24.69564, 36.610283, 23.404142, 36.610283);
        shape1.curveTo(22.088728, 36.610283, 21.024437, 35.545994, 21.024437, 34.21862);
        shape1.curveTo(21.024437, 32.93908, 22.088728, 31.87479, 23.404142, 31.87479);
        shape1.closePath();
        shape1.moveTo(24.002058, 29.734251);
        shape1.lineTo(22.674685, 29.734251);
        shape1.lineTo(22.674685, 28.562336);
        shape1.curveTo(22.674685, 27.163214, 22.411602, 24.042093, 22.124603, 22.463594);
        shape1.lineTo(21.311438, 17.656353);
        shape1.curveTo(20.868979, 15.157064, 20.868979, 15.157064, 20.868979, 14.343899);
        shape1.curveTo(20.868979, 11.84461, 21.753895, 10.636821, 23.523726, 10.636821);
        shape1.curveTo(25.173973, 10.636821, 26.166513, 11.880486, 26.166513, 14.021024);
        shape1.curveTo(26.166513, 14.786356, 25.97518, 16.077854, 25.652306, 17.763977);
        shape1.lineTo(24.62389, 22.762552);
        shape1.curveTo(24.289057, 24.48455, 24.002058, 27.211046, 24.002058, 28.598211);
        shape1.closePath();
        g.fill(shape1);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
    }

    private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
        // _0_0_0_0
        AffineTransform trans_0_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.1066666841506958f, 0.0f, 0.0f, 1.1066666841506958f, -7.114285945892334f, -7.400000095367432f));
        paintShapeNode_0_0_0_0(g);
        g.setTransform(trans_0_0_0_0);
        // _0_0_0_1
        AffineTransform trans_0_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0083141326904297f, 0.0f, 0.0f, 0.9917544722557068f, 0.0f, 0.0f));
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
        return 3;
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
        return 42;
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
    public InfoSign() {
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

