package com.seaglasslookandfeel.icons;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class DesktopPane implements Icon {
    
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
        Rectangle2D.Double shape0 = new Rectangle2D.Double(0.5211939811706543, -0.20508375763893127, 640.34130859375, 481.18701171875);
        g.setPaint(new LinearGradientPaint(new Point2D.Double(84.21428680419922, 696.7192993164062), new Point2D.Double(84.21428680419922, 282.2907409667969), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 26, 200),new Color(0, 0, 86, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0522040128707886f, 0.0f, 0.0f, 1.1695518493652344f, -89.66770935058594f, -332.113037109375f)));
        g.fill(shape0);
    }

    private void paintShapeNode_0_0_0_1(Graphics2D g) {
        Rectangle2D.Double shape1 = new Rectangle2D.Double(-2.611206531524658, -0.22010937333106995, 643.34765625, 481.0891418457031);
        g.setPaint(new RadialGradientPaint(new Point2D.Double(692.9285888671875, 183.71932983398438), 307.2143f, new Point2D.Double(692.9285888671875, 183.71932983398438), new float[] {0.0f,0.51618636f,1.0f}, new Color[] {new Color(118, 255, 255, 255),new Color(0, 255, 255, 128),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0522040128707886f, -1.395545482635498f, 1.9748486280441284f, -1.764583945274353f, 1008.5991821289062f, 1289.26318359375f)));
        g.fill(shape1);
    }

    private void paintShapeNode_0_0_0_2(Graphics2D g) {
        GeneralPath shape2 = new GeneralPath();
        shape2.moveTo(488.57144, -181.92354);
        shape2.curveTo(488.57144, -129.85098, 427.8101, -87.637825, 352.85718, -87.637825);
        shape2.curveTo(277.90424, -87.637825, 217.14288, -129.85098, 217.14288, -181.92354);
        shape2.curveTo(217.14288, -233.9961, 277.90424, -276.20926, 352.85718, -276.20926);
        shape2.curveTo(427.8101, -276.20926, 488.57144, -233.9961, 488.57144, -181.92354);
        shape2.closePath();
        g.setPaint(new RadialGradientPaint(new Point2D.Double(352.8571472167969, -181.9235382080078), 135.71428f, new Point2D.Double(352.8571472167969, -181.9235382080078), new float[] {0.0f,1.0f}, new Color[] {new Color(118, 255, 255, 20),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.694736897945404f, 0.0f, -55.534549713134766f)));
        g.fill(shape2);
    }

    private void paintShapeNode_0_0_0_3(Graphics2D g) {
        GeneralPath shape3 = new GeneralPath();
        shape3.moveTo(488.57144, -181.92354);
        shape3.curveTo(488.57144, -129.85098, 427.8101, -87.637825, 352.85718, -87.637825);
        shape3.curveTo(277.90424, -87.637825, 217.14288, -129.85098, 217.14288, -181.92354);
        shape3.curveTo(217.14288, -233.9961, 277.90424, -276.20926, 352.85718, -276.20926);
        shape3.curveTo(427.8101, -276.20926, 488.57144, -233.9961, 488.57144, -181.92354);
        shape3.closePath();
        g.setPaint(new RadialGradientPaint(new Point2D.Double(352.8571472167969, -181.9235382080078), 135.71428f, new Point2D.Double(352.8571472167969, -181.9235382080078), new float[] {0.0f,1.0f}, new Color[] {new Color(118, 255, 255, 50),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.694736897945404f, 0.0f, -55.534549713134766f)));
        g.fill(shape3);
    }

    private void paintShapeNode_0_0_0_4(Graphics2D g) {
        GeneralPath shape4 = new GeneralPath();
        shape4.moveTo(488.57144, -181.92354);
        shape4.curveTo(488.57144, -129.85098, 427.8101, -87.637825, 352.85718, -87.637825);
        shape4.curveTo(277.90424, -87.637825, 217.14288, -129.85098, 217.14288, -181.92354);
        shape4.curveTo(217.14288, -233.9961, 277.90424, -276.20926, 352.85718, -276.20926);
        shape4.curveTo(427.8101, -276.20926, 488.57144, -233.9961, 488.57144, -181.92354);
        shape4.closePath();
        g.setPaint(new RadialGradientPaint(new Point2D.Double(352.8571472167969, -181.9235382080078), 135.71428f, new Point2D.Double(352.8571472167969, -181.9235382080078), new float[] {0.0f,1.0f}, new Color[] {new Color(118, 255, 255, 22),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.694736897945404f, 0.0f, -55.534549713134766f)));
        g.fill(shape4);
    }

    private void paintShapeNode_0_0_0_5(Graphics2D g) {
        GeneralPath shape5 = new GeneralPath();
        shape5.moveTo(488.57144, -181.92354);
        shape5.curveTo(488.57144, -129.85098, 427.8101, -87.637825, 352.85718, -87.637825);
        shape5.curveTo(277.90424, -87.637825, 217.14288, -129.85098, 217.14288, -181.92354);
        shape5.curveTo(217.14288, -233.9961, 277.90424, -276.20926, 352.85718, -276.20926);
        shape5.curveTo(427.8101, -276.20926, 488.57144, -233.9961, 488.57144, -181.92354);
        shape5.closePath();
        g.setPaint(new RadialGradientPaint(new Point2D.Double(352.8571472167969, -181.9235382080078), 135.71428f, new Point2D.Double(352.8571472167969, -181.9235382080078), new float[] {0.0f,1.0f}, new Color[] {new Color(118, 255, 255, 50),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.694736897945404f, 0.0f, -55.534549713134766f)));
        g.fill(shape5);
    }

    private void paintShapeNode_0_0_0_6(Graphics2D g) {
        GeneralPath shape6 = new GeneralPath();
        shape6.moveTo(488.57144, -181.92354);
        shape6.curveTo(488.57144, -129.85098, 427.8101, -87.637825, 352.85718, -87.637825);
        shape6.curveTo(277.90424, -87.637825, 217.14288, -129.85098, 217.14288, -181.92354);
        shape6.curveTo(217.14288, -233.9961, 277.90424, -276.20926, 352.85718, -276.20926);
        shape6.curveTo(427.8101, -276.20926, 488.57144, -233.9961, 488.57144, -181.92354);
        shape6.closePath();
        g.setPaint(new RadialGradientPaint(new Point2D.Double(352.8571472167969, -181.9235382080078), 135.71428f, new Point2D.Double(352.8571472167969, -181.9235382080078), new float[] {0.0f,1.0f}, new Color[] {new Color(118, 255, 255, 20),new Color(118, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.694736897945404f, 0.0f, -55.534549713134766f)));
        g.fill(shape6);
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
        g.transform(new AffineTransform(1.1386866569519043f, -0.04198896139860153f, -1.6342432498931885f, 0.49277809262275696f, -342.33349609375f, 207.8478240966797f));
        paintShapeNode_0_0_0_2(g);
        g.setTransform(trans_0_0_0_2);
        // _0_0_0_3
        AffineTransform trans_0_0_0_3 = g.getTransform();
        g.transform(new AffineTransform(1.1857566833496094f, 0.058202460408210754f, -1.7496743202209473f, 0.516425609588623f, -300.27532958984375f, 96.5987777709961f));
        paintShapeNode_0_0_0_3(g);
        g.setTransform(trans_0_0_0_3);
        // _0_0_0_4
        AffineTransform trans_0_0_0_4 = g.getTransform();
        g.transform(new AffineTransform(1.3638577461242676f, -0.5095428228378296f, -2.496513843536377f, 1.7003850936889648f, -561.0610961914062f, 687.0672607421875f));
        paintShapeNode_0_0_0_4(g);
        g.setTransform(trans_0_0_0_4);
        // _0_0_0_5
        AffineTransform trans_0_0_0_5 = g.getTransform();
        g.transform(new AffineTransform(0.3691782057285309f, -1.2535853385925293f, -0.023907439783215523f, 2.0120298862457275f, 472.0119323730469f, 961.8795166015625f));
        paintShapeNode_0_0_0_5(g);
        g.setTransform(trans_0_0_0_5);
        // _0_0_0_6
        AffineTransform trans_0_0_0_6 = g.getTransform();
        g.transform(new AffineTransform(0.8989167809486389f, -0.8614928126335144f, -0.9548150300979614f, 1.7095649242401123f, 23.705472946166992f, 835.3329467773438f));
        paintShapeNode_0_0_0_6(g);
        g.setTransform(trans_0_0_0_6);
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
        return 640;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 480;
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
    public DesktopPane() {
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
        g2d.scale(coef1, coef2);
        paint(g2d);
        g2d.dispose();
    }
}

