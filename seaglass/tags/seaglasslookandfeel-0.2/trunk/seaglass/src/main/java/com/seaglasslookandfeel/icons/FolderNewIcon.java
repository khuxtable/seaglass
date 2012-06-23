package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class FolderNewIcon implements Icon {
	
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
		RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double(-12.893506050109863, 1.184890627861023, 9.45958423614502, 12.219721794128418, 1.428767442703247, 1.428767442703247);
		g.setPaint(new Color(118, 118, 118, 91));
		g.fill(shape0);
	}

	private void paintShapeNode_0_0_0_1(Graphics2D g) {
		RoundRectangle2D.Double shape1 = new RoundRectangle2D.Double(-12.6668119430542, 1.0308878421783447, 9.45958423614502, 12.219721794128418, 1.428767442703247, 1.428767442703247);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8083381652832031f, 0.0f, 0.0f, 0.8346183896064758f, -1.4673138856887817f, 0.577239990234375f)));
		g.fill(shape1);
		g.setPaint(new Color(0, 119, 203, 255));
		g.setStroke(new BasicStroke(0.20534329f,0,2,4.0f,null,0.0f));
		g.draw(shape1);
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		RoundRectangle2D.Double shape2 = new RoundRectangle2D.Double(-13.02892017364502, 4.155174255371094, 9.054187774658203, 12.219721794128418, 1.428767442703247, 1.428767442703247);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.7736963033676147f, 0.0f, 0.0f, 0.8346183896064758f, -2.309382677078247f, 3.701526403427124f)));
		g.fill(shape2);
		g.setPaint(new Color(0, 119, 203, 255));
		g.setStroke(new BasicStroke(0.20089507f,0,2,4.0f,null,0.0f));
		g.draw(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo(6.903622, 5.2467628);
		shape3.lineTo(6.903622, 7.4342628);
		shape3.lineTo(4.809872, 7.4342628);
		shape3.lineTo(4.809872, 8.809263);
		shape3.lineTo(6.903622, 8.809263);
		shape3.lineTo(6.903622, 10.996763);
		shape3.lineTo(8.528622, 10.996763);
		shape3.lineTo(8.528622, 8.809263);
		shape3.lineTo(10.622372, 8.809263);
		shape3.lineTo(10.622372, 7.434263);
		shape3.lineTo(8.528622, 7.434263);
		shape3.lineTo(8.528622, 5.246763);
		shape3.lineTo(6.9036217, 5.246763);
		shape3.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-1.851777195930481, -12.676636695861816), new Point2D.Double(-5.0426025390625, -12.65538501739502), new float[] {0.0f,1.0f}, new Color[] {new Color(33, 153, 255, 255),new Color(171, 236, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.8868346214294434f, 0.0f, 0.0f, 1.4989235401153564f, 14.2290678024292f, 26.43236541748047f)));
		g.fill(shape3);
		g.setPaint(new Color(0, 38, 64, 255));
		g.setStroke(new BasicStroke(0.4204329f,0,0,4.0f,null,0.0f));
		g.draw(shape3);
	}

	private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
		// _0_0_0_0
		AffineTransform trans_0_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_0(g);
		g.setTransform(trans_0_0_0_0);
		// _0_0_0_1
		AffineTransform trans_0_0_0_1 = g.getTransform();
		g.transform(new AffineTransform(0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_1(g);
		g.setTransform(trans_0_0_0_1);
		// _0_0_0_2
		AffineTransform trans_0_0_0_2 = g.getTransform();
		g.transform(new AffineTransform(0.2419995665550232f, -0.9702763557434082f, 1.0f, 0.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_2(g);
		g.setTransform(trans_0_0_0_2);
		// _0_0_0_3
		AffineTransform trans_0_0_0_3 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_3(g);
		g.setTransform(trans_0_0_0_3);
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
        return 1;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 4;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 15;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 10;
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
	public FolderNewIcon() {
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

