package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class FloppyDiskIcon implements Icon {
	
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
		RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double(2.589247465133667, 3.0542542934417725, 12.417235374450684, 9.95406723022461, 1.704453945159912, 1.704453945159912);
		g.setPaint(new Color(119, 119, 118, 91));
		g.fill(shape0);
	}

	private void paintShapeNode_0_0_0_1(Graphics2D g) {
		RoundRectangle2D.Double shape1 = new RoundRectangle2D.Double(3.965064764022827, 6.47649621963501, 9.023069381713867, 6.1344475746154785, 0.7172584533691406, 0.7172584533691406);
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape1);
		g.setPaint(new Color(0, 119, 203, 161));
		g.setStroke(new BasicStroke(0.14209497f,0,2,4.0f,null,0.0f));
		g.draw(shape1);
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		RoundRectangle2D.Double shape2 = new RoundRectangle2D.Double(-12.713322639465332, 2.44954514503479, 9.912422180175781, 12.219721794128418, 1.428767442703247, 1.428767442703247);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8470339775085449f, 0.0f, 0.0f, 0.8346183896064758f, -0.9776959419250488f, 1.9958971738815308f)));
		g.fill(shape2);
		g.setPaint(new Color(0, 119, 203, 255));
		g.setStroke(new BasicStroke(0.21020082f,0,2,4.0f,null,0.0f));
		g.draw(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		Rectangle2D.Double shape3 = new Rectangle2D.Double(6.419553279876709, 2.905667304992676, 6.532177448272705, 4.279702663421631);
		g.setPaint(new Color(183, 206, 243, 255));
		g.fill(shape3);
	}

	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		Rectangle2D.Double shape4 = new Rectangle2D.Double(4.324751377105713, 2.8455440998077393, 6.532177448272705, 4.279702663421631);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(8.784652709960938, 6.652227878570557), new Point2D.Double(8.424254417419434, 2.7329208850860596), new float[] {0.0f,1.0f}, new Color[] {new Color(192, 192, 192, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.771276593208313f, 0.0f, 0.0f, 1.0439560413360596f, 0.9891719818115234f, 0.01601026952266693f)));
		g.fill(shape4);
	}

	private void paintShapeNode_0_0_0_5(Graphics2D g) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo(4.205356, 9.242575);
		shape5.lineTo(12.929209, 9.242575);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6166481971740723f, 0.0f, 0.0f, 0.8346183896064758f, 6.707635402679443f, 20.285995483398438f)));
		g.fill(shape5);
		g.setPaint(new Color(0, 119, 203, 255));
		g.setStroke(new BasicStroke(0.17935055f,0,2,4.0f,null,0.0f));
		g.draw(shape5);
	}

	private void paintShapeNode_0_0_0_6(Graphics2D g) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo(4.19748, 10.188616);
		shape6.lineTo(12.921332, 10.188616);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6166481971740723f, 0.0f, 0.0f, 0.8346183896064758f, 6.699759483337402f, 21.232038497924805f)));
		g.fill(shape6);
		g.setPaint(new Color(0, 119, 203, 255));
		g.draw(shape6);
	}

	private void paintShapeNode_0_0_0_7(Graphics2D g) {
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo(4.287579, 11.224758);
		shape7.lineTo(13.011431, 11.224758);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(-13.97996711730957, 7.864083766937256), new Point2D.Double(-2.027459144592285, 7.864083766937256), new float[] {0.0f,1.0f}, new Color[] {new Color(92, 184, 255, 255),new Color(213, 239, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6166481971740723f, 0.0f, 0.0f, 0.8346183896064758f, 6.789857864379883f, 22.26818084716797f)));
		g.fill(shape7);
		g.setPaint(new Color(0, 119, 203, 255));
		g.draw(shape7);
	}

	private void paintShapeNode_0_0_0_8(Graphics2D g) {
		Rectangle2D.Double shape8 = new Rectangle2D.Double(-10.92683219909668, -2.965221881866455, 6.534221649169922, 0.28475862741470337);
		g.setPaint(new LinearGradientPaint(new Point2D.Double(8.784652709960938, 6.652227878570557), new Point2D.Double(8.424254417419434, 2.7329208850860596), new float[] {0.0f,1.0f}, new Color[] {new Color(192, 192, 192, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.7715179324150085f, 0.0f, 0.0f, 0.06946171075105667f, -14.263456344604492f, -3.1534905433654785f)));
		g.fill(shape8);
	}

	private void paintShapeNode_0_0_0_9(Graphics2D g) {
		Rectangle2D.Double shape9 = new Rectangle2D.Double(2.9732677936553955, 3.2284653186798096, 0.7207910418510437, 0.8108909726142883);
		g.setPaint(new Color(162, 162, 185, 255));
		g.fill(shape9);
	}

	private void paintShapeNode_0_0_0_10(Graphics2D g) {
		Rectangle2D.Double shape10 = new Rectangle2D.Double(13.289604187011719, 3.2509899139404297, 0.7207910418510437, 0.8108909726142883);
		g.fill(shape10);
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
		g.transform(new AffineTransform(0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
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
		g.transform(new AffineTransform(-1.0f, -1.3289620983414352E-4f, 0.02440091036260128f, -0.9997022747993469f, 0.0f, 0.0f));
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
        return 13;
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
	public FloppyDiskIcon() {
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

