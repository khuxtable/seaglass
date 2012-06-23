package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class QuestionSign implements Icon {
    
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
        shape1.moveTo(23.232983, 30.942616);
        shape1.curveTo(24.476648, 30.942616, 25.552896, 32.006905, 25.552896, 33.286446);
        shape1.curveTo(25.552896, 34.61382, 24.476648, 35.67811, 23.197107, 35.67811);
        shape1.curveTo(21.869736, 35.67811, 20.805445, 34.61382, 20.805445, 33.286446);
        shape1.curveTo(20.805445, 32.006905, 21.869736, 30.942616, 23.232983, 30.942616);
        shape1.closePath();
        shape1.moveTo(24.046148, 28.802076);
        shape1.lineTo(22.383942, 28.802076);
        shape1.curveTo(22.348068, 28.072618, 22.312193, 27.666037, 22.312193, 27.187704);
        shape1.curveTo(22.312193, 24.843874, 22.862274, 23.779585, 24.847355, 22.452211);
        shape1.lineTo(26.940062, 21.017214);
        shape1.curveTo(28.602266, 19.881172, 29.475224, 18.230925, 29.475224, 16.20997);
        shape1.curveTo(29.475224, 13.017099, 27.609726, 11.175519, 24.261398, 11.175519);
        shape1.curveTo(21.355528, 11.175519, 19.047573, 12.574642, 19.047573, 14.332515);
        shape1.curveTo(19.047573, 14.739097, 19.119322, 15.026096, 19.370447, 15.504429);
        shape1.curveTo(19.741154, 16.28172, 19.812904, 16.49697, 19.812904, 16.867678);
        shape1.curveTo(19.812904, 17.788467, 18.892115, 18.637508, 17.827824, 18.637508);
        shape1.curveTo(16.51241, 18.637508, 15.555744, 17.48951, 15.555744, 15.946887);
        shape1.curveTo(15.555744, 14.476014, 16.51241, 12.861642, 18.090906, 11.617976);
        shape1.curveTo(19.561779, 10.481936, 22.204567, 9.704646, 24.632107, 9.704646);
        shape1.curveTo(26.724812, 9.704646, 28.889267, 10.290604, 30.539515, 11.283144);
        shape1.curveTo(32.34522, 12.347434, 33.445385, 14.368389, 33.445385, 16.532845);
        shape1.curveTo(33.445385, 17.967842, 32.931175, 19.438715, 32.04626, 20.61063);
        shape1.curveTo(31.28093, 21.53142, 30.180765, 22.272837, 28.482683, 22.858793);
        shape1.lineTo(27.274893, 23.30125);
        shape1.curveTo(24.775606, 24.257917, 24.010273, 25.501581, 24.046148, 28.802076);
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
    public QuestionSign() {
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

