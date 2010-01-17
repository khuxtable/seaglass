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
 * $Id$
 */
package com.seaglasslookandfeel.ui;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;

import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass TextField UI delegate.
 * 
 * Based on SynthTextFieldUI, but we need to set preferred sizes.
 * 
 * The search code is based loosely on
 * elliotth.blogspot.com/2004/09/cocoa-like-search-field-for-java.html
 */
public class SeaGlassTextFieldUI extends BasicTextFieldUI implements SynthUI, FocusListener {

    private static final State isSearchState = new TextFieldIsSearchState();

    private SynthStyle         style;

    private CancelListener     cancelListener;
    private String             placeholderText;

    private ActionListener     findAction;
    private JPopupMenu         findPopup;
    private ActionListener     cancelAction;

    private int                searchIconWidth;
    private int                popupIconWidth;
    private int                cancelIconWidth;
    private int                searchLeftInnerMargin;
    private int                searchRightInnerMargin;

    /**
     * Creates a UI for a JTextField.
     * 
     * @param c
     *            the text field
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTextFieldUI();
    }

    public SeaGlassTextFieldUI() {
        super();
    }

    private void updateStyle(JTextComponent comp) {
        SeaGlassContext context = getContext(comp, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        updateSearchStyle(comp, context, getPropertyPrefix());

        if (style != oldStyle) {
            updateStyle(comp, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    private void updateSearchStyle(JTextComponent comp, SeaGlassContext context, String prefix) {
        searchIconWidth = 0;
        Object o = style.get(context, prefix + ".searchIconWidth");
        if (o != null && o instanceof Integer) {
            searchIconWidth = (Integer) o;
        }

        popupIconWidth = 0;
        o = style.get(context, prefix + ".popupIconWidth");
        if (o != null && o instanceof Integer) {
            popupIconWidth = (Integer) o;
        }

        cancelIconWidth = 0;
        o = style.get(context, prefix + ".cancelIconWidth");
        if (o != null && o instanceof Integer) {
            cancelIconWidth = (Integer) o;
        }

        searchLeftInnerMargin = 0;
        o = style.get(context, prefix + ".searchLeftInnerMargin");
        if (o != null && o instanceof Integer) {
            searchLeftInnerMargin = (Integer) o;
        }

        searchRightInnerMargin = 0;
        o = style.get(context, prefix + ".searchRightInnerMargin");
        if (o != null && o instanceof Integer) {
            searchRightInnerMargin = (Integer) o;
        }

        if (isSearchState.isInState(comp)) {
            Border border = comp.getBorder();
            if (!(border instanceof SearchBorder)) {
                comp.setBorder(new SearchBorder(border));
            }

            o = comp.getClientProperty("JTextField.Search.PlaceholderText");
            if (o != null && o instanceof String) {
                placeholderText = (String) o;
            } else if (placeholderText != null) {
                placeholderText = null;
            }

            if (cancelListener == null) {
                cancelListener = new CancelListener();
                comp.addMouseListener(cancelListener);
                comp.addMouseMotionListener(cancelListener);
            }

            o = comp.getClientProperty("JTextField.Search.FindAction");
            if (o != null && o instanceof ActionListener) {
                if (findAction == null) {
                    findAction = (ActionListener) o;
                }
            }

            o = comp.getClientProperty("JTextField.Search.FindPopup");
            if (o != null && o instanceof JPopupMenu) {
                if (findPopup == null) {
                    findPopup = (JPopupMenu) o;
                }
            }

            o = comp.getClientProperty("JTextField.Search.CancelAction");
            if (o != null && o instanceof ActionListener) {
                if (cancelAction == null) {
                    cancelAction = (ActionListener) o;
                }
            }
        } else {
            Border border = comp.getBorder();
            if (border instanceof SearchBorder) {
                comp.setBorder(((SearchBorder) border).getOriginalBorder());
            }

            placeholderText = null;

            if (cancelListener != null) {
                comp.removeMouseListener(cancelListener);
                comp.removeMouseMotionListener(cancelListener);
                cancelListener = null;
            }

            if (findAction != null) {
                findAction = null;
            }

            if (findPopup != null) {
                findPopup = null;
            }

            if (cancelAction != null) {
                cancelAction = null;
            }
        }
    }

    private void updateStyle(JTextComponent comp, SeaGlassContext context, String prefix) {
        SeaGlassStyle style = (SeaGlassStyle) context.getStyle();

        Color color = comp.getCaretColor();
        if (color == null || color instanceof UIResource) {
            comp.setCaretColor((Color) style.get(context, prefix + ".caretForeground"));
        }

        Color fg = comp.getForeground();
        if (fg == null || fg instanceof UIResource) {
            fg = style.getColorForState(context, ColorType.TEXT_FOREGROUND);
            if (fg != null) {
                comp.setForeground(fg);
            }
        }

        Object ar = style.get(context, prefix + ".caretAspectRatio");
        if (ar instanceof Number) {
            comp.putClientProperty("caretAspectRatio", ar);
        }

        context.setComponentState(SELECTED | FOCUSED);

        Color s = comp.getSelectionColor();
        if (s == null || s instanceof UIResource) {
            comp.setSelectionColor(style.getColor(context, ColorType.TEXT_BACKGROUND));
        }

        Color sfg = comp.getSelectedTextColor();
        if (sfg == null || sfg instanceof UIResource) {
            comp.setSelectedTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        context.setComponentState(DISABLED);

        Color dfg = comp.getDisabledTextColor();
        if (dfg == null || dfg instanceof UIResource) {
            comp.setDisabledTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        Insets margin = comp.getMargin();
        if (margin == null || margin instanceof UIResource) {
            margin = (Insets) style.get(context, prefix + ".margin");

            if (margin == null) {
                // Some places assume margins are non-null.
                margin = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
            comp.setMargin(margin);
        }

        Caret caret = comp.getCaret();
        if (caret instanceof UIResource) {
            Object o = style.get(context, prefix + ".caretBlinkRate");
            if (o != null && o instanceof Integer) {
                Integer rate = (Integer) o;
                caret.setBlinkRate(rate.intValue());
            }
        }
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        int state = SeaGlassLookAndFeel.getComponentState(c);
        return state;
    }

    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        paintBackground(context, g, c);
        paint(context, g);
        context.dispose();
    }

    /**
     * Paints the interface. This is routed to the paintSafely method under the
     * guarantee that the model won't change from the view of this thread while
     * it's rendering (if the associated model is derived from
     * AbstractDocument). This enables the model to potentially be updated
     * asynchronously.
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        super.paint(g, getComponent());
    }

    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        // If necessary, paint the placeholder text.
        paintPlaceholderText(context, g, c);
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTextFieldBorder(context, g, x, y, w, h);
    }

    protected void paintBackground(Graphics g) {
        // Overriden to do nothing, all our painting is done from update/paint.
    }

    private void paintPlaceholderText(SeaGlassContext context, Graphics g, JComponent c) {
        if (placeholderText != null && ((JTextComponent) c).getText().length() == 0 && !c.hasFocus()) {
            // TODO Don't hard-code this color.
            g.setColor(Color.GRAY);
            g.setFont(c.getFont());
            Rectangle innerArea = SwingUtilities.calculateInnerArea(c, null);
            // TODO Do better baseline calculation.
            context.getStyle().getGraphicsUtils(context).paintText(context, g, placeholderText, innerArea.x, innerArea.y - 1, -1);
        }
    }

    /**
     * This method gets called when a bound property is changed on the
     * associated JTextComponent. This is a hook which UI implementations may
     * change to reflect how the UI displays bound properties of JTextComponent
     * subclasses. This is implemented to do nothing (i.e. the response to
     * properties in JTextComponent itself are handled prior to calling this
     * method).
     * 
     * @param evt
     *            the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent) evt.getSource());
        }
        super.propertyChange(evt);
    }

    public void focusGained(FocusEvent e) {
        getComponent().repaint();
    }

    public void focusLost(FocusEvent e) {
        getComponent().repaint();
    }

    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();
        JTextComponent c = getComponent();
        updateStyle(c);
        getComponent().addFocusListener(this);
    }

    protected void uninstallDefaults() {
        JTextComponent c = getComponent();
        SeaGlassContext context = getContext(c, ENABLED);

        Border border = c.getBorder();
        if (border instanceof SearchBorder) {
            c.setBorder(((SearchBorder) border).getOriginalBorder());
        }

        c.putClientProperty("caretAspectRatio", null);
        c.removeFocusListener(this);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
        super.uninstallDefaults();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        updateStyle((JTextComponent) c);
    }

    protected void fireAction(ActionListener action) {
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        ActionEvent e = new ActionEvent(getComponent(), ActionEvent.ACTION_PERFORMED, getComponent().getText(), EventQueue
            .getMostRecentEventTime(), modifiers);

        action.actionPerformed(e);
    }

    public Dimension getPreferredSize(JComponent c) {
        // The following code has been derived from BasicTextUI.
        Document doc = ((JTextComponent) c).getDocument();
        Insets i = c.getInsets();
        Dimension d = c.getSize();
        View rootView = getRootView((JTextComponent) c);

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).readLock();
        }
        try {
            if ((d.width > (i.left + i.right)) && (d.height > (i.top + i.bottom))) {
                rootView.setSize(d.width - i.left - i.right, d.height - i.top - i.bottom);
            } else if (d.width == 0 && d.height == 0) {
                // Probably haven't been layed out yet, force some sort of
                // initial sizing.
                rootView.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            d.width = (int) Math.min((long) rootView.getPreferredSpan(View.X_AXIS) + (long) i.left + (long) i.right, Integer.MAX_VALUE);
            d.height = (int) Math.min((long) rootView.getPreferredSpan(View.Y_AXIS) + (long) i.top + (long) i.bottom, Integer.MAX_VALUE);
        } finally {
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).readUnlock();
            }
        }

        // Fix: The preferred width is always two pixels too small on a Mac.
        d.width += 2;

        // We'd like our heights to be odd by default.
        if ((d.height & 1) == 0) {
            d.height--;
        }

        return d;
    }

    public class SearchBorder extends AbstractBorder {
        private final Color GRAY = new Color(0.7f, 0.7f, 0.7f);

        private Border      originalBorder;

        /**
         * Creates a compound border with null outside and inside borders.
         */
        public SearchBorder() {
            this.originalBorder = null;
        }

        /**
         * Creates a compound border with the specified outside and inside
         * borders. Either border may be null.
         * 
         * @param originalBorder
         *            the outside border
         * @param insideBorder
         *            the inside border to be nested
         */
        public SearchBorder(Border originalBorder) {
            this.originalBorder = originalBorder;
        }

        /**
         * Returns whether or not this compound border is opaque. Returns true
         * if both the inside and outside borders are non-null and opaque;
         * returns false otherwise.
         */
        public boolean isBorderOpaque() {
            return (originalBorder == null || originalBorder.isBorderOpaque());
        }

        /**
         * Paints the compound border by painting the outside border with the
         * specified position and size and then painting the inside border at
         * the specified position and size offset by the insets of the outside
         * border.
         * 
         * @param c
         *            the component for which this border is being painted
         * @param g
         *            the paint graphics
         * @param x
         *            the x position of the painted border
         * @param y
         *            the y position of the painted border
         * @param width
         *            the width of the painted border
         * @param height
         *            the height of the painted border
         */
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Insets nextInsets;
            int px, py, pw, ph;

            px = x;
            py = y;
            pw = width;
            ph = height;

            if (originalBorder != null) {
                originalBorder.paintBorder(c, g, px, py, pw, ph);

                nextInsets = originalBorder.getBorderInsets(c);
                px += nextInsets.left;
                py += nextInsets.top;
                pw = pw - nextInsets.right - nextInsets.left;
                ph = ph - nextInsets.bottom - nextInsets.top;
            }

            paintSearchBorder((JTextField) c, (Graphics2D) g.create(), px, py, pw, ph);
        }

        /**
         * Reinitialize the insets parameter with this Border's current Insets.
         * 
         * @param c
         *            the component for which this border insets value applies
         * @param insets
         *            the object to be reinitialized
         */
        public Insets getBorderInsets(Component c, Insets insets) {
            Insets nextInsets;

            insets.top = insets.left = insets.right = insets.bottom = 0;
            if (originalBorder != null) {
                nextInsets = originalBorder.getBorderInsets(c);
                insets.top += nextInsets.top;
                insets.left += nextInsets.left;
                insets.right += nextInsets.right;
                insets.bottom += nextInsets.bottom;
            }

            insets.left += searchIconWidth + searchLeftInnerMargin;
            Object findPopup = ((JComponent) c).getClientProperty("JTextField.Search.FindPopup");
            if (findPopup != null && findPopup instanceof JPopupMenu) {
                insets.left += popupIconWidth;
            }
            insets.right += cancelIconWidth + searchRightInnerMargin;

            return insets;
        }

        /**
         * Returns the insets of the composite border by adding the insets of
         * the outside border to the insets of the inside border.
         * 
         * @param c
         *            the component for which this border insets value applies
         */
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        /**
         * Returns the outside border object.
         */
        public Border getOriginalBorder() {
            return originalBorder;
        }

        public void paintSearchBorder(JTextField c, Graphics2D g, int x, int y, int width, int height) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            paintSearchGlass(g, x, y);

            Object findPopup = c.getClientProperty("JTextField.Search.FindPopup");
            if (findPopup != null && findPopup instanceof JPopupMenu) {
                paintPopupIcon(g, x, y);
            }

            // Draw the erase "x" if user-entered text is present.
            if (c.getText().length() > 0) {
                paintCancelIcon(g, x, y, width, height);
            }
        }

        private void paintSearchGlass(Graphics2D g, int x, int y) {
            g.setStroke(new BasicStroke(2));
            final int glassL = 8;
            final int handleL = 3;
            final int handleO = 7;
            final int glassX = 2;
            final int glassY = 2;
            g.setColor(Color.DARK_GRAY);
            g.drawOval(x + glassX, y + glassY, glassL, glassL);
            g.drawLine(x + glassX + handleO, y + glassY + handleO, x + glassX + handleO + handleL, y + glassY + handleO + handleL);
        }

        private void paintPopupIcon(Graphics2D g, int x, int y) {
            final int dropX = x + 13;
            final int dropY = y + 5;
            Path2D path = new Path2D.Float();
            path.moveTo(dropX, dropY);
            path.lineTo(dropX + 7, dropY);
            path.lineTo(dropX + 3.5, dropY + 4);
            path.closePath();
            g.fill(path);
        }

        private void paintCancelIcon(Graphics2D g, int x, int y, int width, int height) {
            final int circleL = height + 2;
            final int circleX = x + width - circleL;
            final int circleY = y + (height - 1 - circleL) / 2;
            g.setColor(cancelListener.isCancelArmed() ? Color.GRAY : GRAY);
            g.fillOval(circleX, circleY, circleL, circleL);
            final int lineL = circleL - 9;
            final int lineX = circleX + 4;
            final int lineY = circleY + 4;
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(lineX, lineY, lineX + lineL, lineY + lineL);
            g.drawLine(lineX, lineY + lineL, lineX + lineL, lineY);
        }
    }

    /**
     * Handles a click on the cancel button by clearing the text and notifying
     * any ActionListeners.
     */
    public class CancelListener extends MouseInputAdapter {

        private boolean isFindArmed   = false;
        private boolean isCancelArmed = false;

        public boolean isCancelArmed() {
            return isCancelArmed;
        }

        private boolean isOverCancelButton(MouseEvent e) {
            // TODO Make the actions actual buttons.

            // If the button is down, we might be outside the component
            // without having had mouseExited invoked.
            if (!getComponent().contains(e.getPoint())) {
                return false;
            }

            JTextComponent c = getComponent();
            Rectangle rect = new Rectangle();
            SwingUtilities.calculateInnerArea(c, rect);
            // Adjust for the placement of the cancel button.
            rect.x += rect.width + searchRightInnerMargin;
            rect.width = cancelIconWidth;
            return rect.contains(e.getPoint());
        }

        private boolean isOverFindButton(MouseEvent e) {
            // TODO Make the actions actual buttons.
            if (findPopup == null && findAction != null) {
                return false;
            }

            // If the button is down, we might be outside the component
            // without having had mouseExited invoked.
            if (!getComponent().contains(e.getPoint())) {
                return false;
            }

            JTextComponent c = getComponent();
            Rectangle rect = new Rectangle();
            SwingUtilities.calculateInnerArea(c, rect);
            // Adjust for the placement of the find button.
            rect.x -= searchIconWidth - searchLeftInnerMargin;
            rect.width = searchIconWidth;
            if (findPopup != null) {
                rect.x -= popupIconWidth;
                rect.width += popupIconWidth;
            }
            return rect.contains(e.getPoint());
        }

        public void mouseDragged(MouseEvent e) {
            armCancel(e);
            armFind(e);
        }

        public void mouseEntered(MouseEvent e) {
            armCancel(e);
            armFind(e);
        }

        public void mouseExited(MouseEvent e) {
            disarmCancel();
            disarmFind();
        }

        public void mousePressed(MouseEvent e) {
            armCancel(e);
            armFind(e);
        }

        private void cancel() {
            getComponent().setText("");
            if (cancelAction != null) {
                fireAction(cancelAction);
            }
        }

        private void find() {
            if (findAction != null) {
                fireAction(findAction);
            }
            if (findPopup != null) {
                System.out.println("Got here");
                findPopup.setVisible(true);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (isCancelArmed) {
                cancel();
            }
            disarmCancel();
            if (isFindArmed) {
                find();
            }
            disarmFind();
        }

        private void armCancel(MouseEvent e) {
            isCancelArmed = (isOverCancelButton(e) && SwingUtilities.isLeftMouseButton(e));
            getComponent().repaint();
        }

        private void disarmCancel() {
            isCancelArmed = false;
            getComponent().repaint();
        }

        private void armFind(MouseEvent e) {
            isFindArmed = (isOverFindButton(e) && SwingUtilities.isLeftMouseButton(e));
        }

        private void disarmFind() {
            isFindArmed = false;
        }
    }
}
