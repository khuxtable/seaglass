package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class HardDiskIcon implements Icon {
	
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
		RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double(0.9674652814865112, 3.099303722381592, 14.624661445617676, 9.95406723022461, 1.1638600826263428, 1.1638600826263428);
		g.setPaint(new Color(119, 119, 118, 91));
		g.fill(shape0);
	}

	private void paintShapeNode_0_0_0_1(Graphics2D g) {
		RoundRectangle2D.Double shape1 = new RoundRectangle2D.Double(0.6041178703308105, 2.625133991241455, 14.624661445617676, 9.996208190917969, 1.1687873601913452, 1.1687873601913452);
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape1);
		g.setPaint(new Color(0, 119, 203, 161));
		g.setStroke(new BasicStroke(0.23092683f,0,2,4.0f,null,0.0f));
		g.draw(shape1);
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo(7.5232677, 3.498762);
		shape2.curveTo(7.5232677, 4.1954064, 5.2491736, 4.760148, 2.4439359, 4.760148);
		shape2.curveTo(-0.36130166, 4.760148, -2.635396, 4.1954064, -2.635396, 3.498762);
		shape2.curveTo(-2.635396, 2.8021176, -0.36130166, 2.2373757, 2.4439359, 2.2373757);
		shape2.curveTo(5.2491736, 2.2373757, 7.5232677, 2.8021176, 7.5232677, 3.498762);
		shape2.closePath();
		g.fill(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		GeneralPath shape3 = new GeneralPath();
		shape3.moveTo(7.5232677, 3.498762);
		shape3.curveTo(7.5232677, 4.1954064, 5.2491736, 4.760148, 2.4439359, 4.760148);
		shape3.curveTo(-0.36130166, 4.760148, -2.635396, 4.1954064, -2.635396, 3.498762);
		shape3.curveTo(-2.635396, 2.8021176, -0.36130166, 2.2373757, 2.4439359, 2.2373757);
		shape3.curveTo(5.2491736, 2.2373757, 7.5232677, 2.8021176, 7.5232677, 3.498762);
		shape3.closePath();
		g.fill(shape3);
	}

	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo(7.5232677, 3.498762);
		shape4.curveTo(7.5232677, 4.1954064, 5.2491736, 4.760148, 2.4439359, 4.760148);
		shape4.curveTo(-0.36130166, 4.760148, -2.635396, 4.1954064, -2.635396, 3.498762);
		shape4.curveTo(-2.635396, 2.8021176, -0.36130166, 2.2373757, 2.4439359, 2.2373757);
		shape4.curveTo(5.2491736, 2.2373757, 7.5232677, 2.8021176, 7.5232677, 3.498762);
		shape4.closePath();
		g.fill(shape4);
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
		g.transform(new AffineTransform(1.249703049659729f, 0.0f, 0.0f, 1.5031185150146484f, 4.900796413421631f, 4.956447124481201f));
		paintShapeNode_0_0_0_2(g);
		g.setTransform(trans_0_0_0_2);
		// _0_0_0_3
		AffineTransform trans_0_0_0_3 = g.getTransform();
		g.transform(new AffineTransform(1.249703049659729f, 0.0f, 0.0f, 1.5031185150146484f, 4.9993205070495605f, 2.4191110134124756f));
		paintShapeNode_0_0_0_3(g);
		g.setTransform(trans_0_0_0_3);
		// _0_0_0_4
		AffineTransform trans_0_0_0_4 = g.getTransform();
		g.transform(new AffineTransform(1.249703049659729f, 0.0f, 0.0f, 1.5031185150146484f, 5.044368743896484f, -0.08436819911003113f));
		paintShapeNode_0_0_0_4(g);
		g.setTransform(trans_0_0_0_4);
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
        return 3;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 16;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 11;
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
	public HardDiskIcon() {
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

