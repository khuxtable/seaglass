package com.seaglasslookandfeel.painter.titlepaneforegound;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Painter for title pane button foreground shapes.
 *
 * @author Kathryn Huxtable
 */
public abstract class TitlePaneButtonForegroundPainter {

    private Color enabledBorder   = UIManager.getColor("seaGlassTitlePaneButtonEnabledBorder");
    private Color enabledCorner   = UIManager.getColor("seaGlassTitlePaneButtonEnabledCorner");
    private Color enabledInterior = UIManager.getColor("seaGlassTitlePaneButtonEnabledInterior");
    private Color hoverBorder     = UIManager.getColor("seaGlassTitlePaneButtonHoverBorder");
    private Color hoverCorner     = UIManager.getColor("seaGlassTitlePaneButtonHoverCorner");
    private Color hoverInterior   = UIManager.getColor("seaGlassTitlePaneButtonHoverInterior");
    private Color pressedBorder   = UIManager.getColor("seaGlassTitlePaneButtonPressedBorder");
    private Color pressedCorner   = UIManager.getColor("seaGlassTitlePaneButtonPressedCorner");
    private Color pressedInterior = UIManager.getColor("seaGlassTitlePaneButtonPressedInterior");

    /**
     * Paint the enabled state of the button foreground.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the button to paint.
     * @param width  the width to paint.
     * @param height the height to paint.
     */
    public void paintEnabled(Graphics2D g, JComponent c, int width, int height) {
        paint(g, c, width, height, enabledBorder, enabledCorner, enabledInterior);
    }

    /**
     * Paint the mouse-over state of the button foreground.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the button to paint.
     * @param width  the width to paint.
     * @param height the height to paint.
     */
    public void paintHover(Graphics2D g, JComponent c, int width, int height) {
        paint(g, c, width, height, hoverBorder, hoverCorner, hoverInterior);
    }

    /**
     * Paint the pressed state of the button foreground.
     *
     * @param g      the Graphics2D context to paint with.
     * @param c      the button to paint.
     * @param width  the width to paint.
     * @param height the height to paint.
     */
    public void paintPressed(Graphics2D g, JComponent c, int width, int height) {
        paint(g, c, width, height, pressedBorder, pressedCorner, pressedInterior);
    }

    /**
     * Paint the button foreground with the specified colors.
     *
     * @param g        the Graphics2D context to paint with.
     * @param c        the button to paint.
     * @param width    the width to paint.
     * @param height   the height to paint.
     * @param border   the border color.
     * @param corner   the corner color.
     * @param interior the interior color.
     */
    protected abstract void paint(Graphics2D g, JComponent c, int width, int height, Color border, Color corner, Color interior);
}
