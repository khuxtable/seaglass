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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassRegion;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.SeaGlassSynthPainterImpl;
import com.seaglasslookandfeel.component.SeaGlassBorder;
import com.seaglasslookandfeel.state.SearchFieldHasPopupState;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;

/**
 * Sea Glass TextField UI delegate.
 *
 * <p>Based on SynthTextFieldUI, but we need to set preferred sizes and handle
 * search fields.</p>
 */
public class SeaGlassTextFieldUI extends BasicTextFieldUI implements SeaglassUI, FocusListener {

    private static final State isSearchField = new TextFieldIsSearchState();
    private static final State hasPopupMenu  = new SearchFieldHasPopupState();

    private TextFieldBorder textFieldBorder;

    private MouseAdapter mouseListener;

    private boolean isCancelArmed;

    private SynthStyle style;
    private SynthStyle findStyle;
    private SynthStyle cancelStyle;

    private String placeholderText;
    private Color  placeholderColor;

    private ActionListener findAction;
    private JPopupMenu     findPopup;
    private ActionListener cancelAction;

    private int searchIconWidth;
    private int popupIconWidth;
    private int cancelIconWidth;
    private int searchLeftInnerMargin;
    private int searchRightInnerMargin;

    /**
     * Creates a new SeaGlassTextFieldUI object.
     */
    public SeaGlassTextFieldUI() {
        super();
    }

    /**
     * Creates a UI for a JTextField.
     *
     * @param  c the text field
     *
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassTextFieldUI();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    private void updateStyle(JTextComponent c) {
        SeaGlassContext context  = getContext(c, ENABLED);
        SynthStyle      oldStyle = style;

        SynthStyle s = SeaGlassLookAndFeel.updateStyle(context, this);
        if (s instanceof SeaGlassStyle) {
            style = (SeaGlassStyle) s;

            updateSearchStyle(c, context, getPropertyPrefix());

            if (style != oldStyle) {
                updateStyle(c, context, getPropertyPrefix());

                if (oldStyle != null) {
                    uninstallKeyboardActions();
                    installKeyboardActions();
                }
            }
        }
        
        context.dispose();

        context   = getContext(c, SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON, ENABLED);
        findStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();

        context     = getContext(c, SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON, ENABLED);
        cancelStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    /**
     * Sea Glass code to support the search JTextField.variant.
     *
     * @param c       the JTextField component.
     * @param context the SeaGlassContext.
     * @param prefix  the control prefix, e.g. "TextField",
     *                "FormattedTextField", or "PasswordField".
     */
    private void updateSearchStyle(JTextComponent c, SeaGlassContext context, String prefix) {
        searchIconWidth = 0;
        Object o = style.get(context, prefix + ".searchIconWidth");

        if (o != null && o instanceof Integer) {
            searchIconWidth = (Integer) o;
        }

        popupIconWidth = 0;
        o              = style.get(context, prefix + ".popupIconWidth");
        if (o != null && o instanceof Integer) {
            popupIconWidth = (Integer) o;
        }

        cancelIconWidth = 0;
        o               = style.get(context, prefix + ".cancelIconWidth");
        if (o != null && o instanceof Integer) {
            cancelIconWidth = (Integer) o;
        }

        searchLeftInnerMargin = 0;
        o                     = style.get(context, prefix + ".searchLeftInnerMargin");
        if (o != null && o instanceof Integer) {
            searchLeftInnerMargin = (Integer) o;
        }

        searchRightInnerMargin = 0;
        o                      = style.get(context, prefix + ".searchRightInnerMargin");
        if (o != null && o instanceof Integer) {
            searchRightInnerMargin = (Integer) o;
        }

        placeholderColor = Color.GRAY;
        o                = style.get(context, "placeholderTextColor");
        if (o != null && o instanceof Color) {
            placeholderColor = (Color) o;
        }

        Border border = c.getBorder();

        if (border == null || border instanceof UIResource && !(border instanceof TextFieldBorder)) {
            c.setBorder(createTextFieldBorder(context));
        }

        if (isSearchField.isInState(c)) {
            o = c.getClientProperty("JTextField.Search.PlaceholderText");
            if (o != null && o instanceof String) {
                placeholderText = (String) o;
            } else if (placeholderText != null) {
                placeholderText = null;
            }

            o = c.getClientProperty("JTextField.Search.FindAction");
            if (o != null && o instanceof ActionListener) {
                if (findAction == null) {
                    findAction = (ActionListener) o;
                }
            }

            o = c.getClientProperty("JTextField.Search.FindPopup");
            if (o != null && o instanceof JPopupMenu) {
                if (findPopup == null) {
                    findPopup = (JPopupMenu) o;
                }
            }

            o = c.getClientProperty("JTextField.Search.CancelAction");
            if (o != null && o instanceof ActionListener) {
                if (cancelAction == null) {
                    cancelAction = (ActionListener) o;
                }
            }

            installMouseListeners();
        } else {
            placeholderText = null;

            if (findAction != null) {
                findAction = null;
            }

            if (findPopup != null) {
                findPopup = null;
            }

            if (cancelAction != null) {
                cancelAction = null;
            }

            uninstallMouseListeners();
        }
    }

    /**
     * Private method to update styles.
     *
     * @param c       the JTextField component.
     * @param context the SeaGlassContext.
     * @param prefix  the control prefix, e.g. "TextField",
     *                "FormattedTextField", or "PasswordField".
     */
    static void updateStyle(JTextComponent c, SeaGlassContext context, String prefix) {
        SeaGlassStyle style = (SeaGlassStyle) context.getStyle();

        Color color = c.getCaretColor();

        if (color == null || color instanceof UIResource) {
            c.setCaretColor((Color) style.get(context, prefix + ".caretForeground"));
        }

        Color fg = c.getForeground();

        if (fg == null || fg instanceof UIResource) {
            fg = style.getColorForState(context, ColorType.TEXT_FOREGROUND);
            if (fg != null) {
                c.setForeground(fg);
            }
        }

        Object ar = style.get(context, prefix + ".caretAspectRatio");

        if (ar instanceof Number) {
            c.putClientProperty("caretAspectRatio", ar);
        }

        context.setComponentState(SELECTED | FOCUSED);

        Color s = c.getSelectionColor();

        if (s == null || s instanceof UIResource) {
            c.setSelectionColor(style.getColor(context, ColorType.TEXT_BACKGROUND));
        }

        Color sfg = c.getSelectedTextColor();

        if (sfg == null || sfg instanceof UIResource) {
            c.setSelectedTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        context.setComponentState(DISABLED);

        Color dfg = c.getDisabledTextColor();

        if (dfg == null || dfg instanceof UIResource) {
            c.setDisabledTextColor(style.getColor(context, ColorType.TEXT_FOREGROUND));
        }

        Insets margin = c.getMargin();

        if (margin == null || margin instanceof UIResource) {
            margin = (Insets) style.get(context, prefix + ".margin");
            if (margin == null) {
                // Some places assume margins are non-null.
                margin = SeaGlassLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }

            c.setMargin(margin);
        }

        Caret caret = c.getCaret();

        if (caret instanceof UIResource) {
            Object o = style.get(context, prefix + ".caretBlinkRate");

            if (o != null && o instanceof Integer) {
                Integer rate = (Integer) o;

                caret.setBlinkRate(rate.intValue());
            }
        }
    }

    /**
     * @see SeaglassUI#getContext(javax.swing.JComponent)
     */
    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  state DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c      DOCUMENT ME!
     * @param  region DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c      DOCUMENT ME!
     * @param  region DOCUMENT ME!
     * @param  state  DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        SynthStyle style = findStyle;

        if (region == SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON) {
            style = cancelStyle;
        }

        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentState(JComponent c) {
        int state = SeaGlassLookAndFeel.getComponentState(c);

        return state;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c      DOCUMENT ME!
     * @param  region DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentState(JComponent c, Region region) {
        if (region == SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON && c.isEnabled()) {
            if (((JTextComponent) c).getText().length() == 0) {
                return DISABLED;
            } else if (isCancelArmed) {
                return PRESSED;
            }

            return ENABLED;
        }

        return SeaGlassLookAndFeel.getComponentState(c);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
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
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     */
    protected void paint(SeaGlassContext context, Graphics g) {
        JTextComponent c = getComponent();

        super.paint(g, c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     * @param c       DOCUMENT ME!
     */
    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        // If necessary, paint the placeholder text.
        if (placeholderText != null && ((JTextComponent) c).getText().length() == 0 && !c.hasFocus()) {
            paintPlaceholderText(context, g, c);
        }
    }

    /**
     * @see SeaglassUI#paintBorder(javax.swing.plaf.synth.SynthContext,
     *      java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTextFieldBorder(context, g, x, y, w, h);

        JTextComponent c = getComponent();

        if (isSearchField.isInState(c)) {
            paintSearchButton(g, c, SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON);
            paintSearchButton(g, c, SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON);
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#paintBackground(java.awt.Graphics)
     */
    protected void paintBackground(Graphics g) {
        // Overridden to do nothing, all our painting is done from update/paint.
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param g       DOCUMENT ME!
     * @param c       DOCUMENT ME!
     */
    protected void paintPlaceholderText(SeaGlassContext context, Graphics g, JComponent c) {
        g.setColor(placeholderColor);
        g.setFont(c.getFont());
        Rectangle innerArea    = SwingUtilities.calculateInnerArea(c, null);
        Rectangle cancelBounds = getCancelButtonBounds();
        context.getStyle().getGraphicsUtils(context).paintText(context, g, getPlaceholderText(g, innerArea.width + cancelBounds.width),
                                                               innerArea.x,
                                                               innerArea.y, -1);
    }

    /**
     * Get the placeholder text, clipped if necessary.
     *
     * @param  g              fm the font metrics to compute size with.
     * @param  availTextWidth the available space to display the title in.
     *
     * @return the text, clipped to fit the available space.
     */
    private String getPlaceholderText(Graphics g, int availTextWidth) {
        JTextComponent c  = getComponent();
        FontMetrics    fm = SwingUtilities2.getFontMetrics(c, g);

        return SwingUtilities2.clipStringIfNecessary(c, fm, placeholderText, availTextWidth);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g      DOCUMENT ME!
     * @param c      DOCUMENT ME!
     * @param region DOCUMENT ME!
     */
    protected void paintSearchButton(Graphics g, JTextComponent c, Region region) {
        Rectangle bounds;

        if (region == SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON) {
            bounds = getFindButtonBounds();
        } else {
            bounds = getCancelButtonBounds();
        }

        SeaGlassContext subcontext = getContext(c, region);

        SeaGlassLookAndFeel.updateSubregion(subcontext, g, bounds);

        SeaGlassSynthPainterImpl painter = (SeaGlassSynthPainterImpl) subcontext.getPainter();

        painter.paintSearchButtonForeground(subcontext, g, bounds.x, bounds.y, bounds.width, bounds.height);

        subcontext.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Rectangle getFindButtonBounds() {
        JTextComponent c = getComponent();
        final int      x = c.getHeight() / 2 - 6;
        final int      y = c.getHeight() / 2 - 6;

        return new Rectangle(x, y, 22, 17);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Rectangle getCancelButtonBounds() {
        JTextComponent c = getComponent();
        final int      x = c.getWidth() - c.getHeight() / 2 - 9;
        final int      y = c.getHeight() / 2 - 8;

        return new Rectangle(x, y, 17, 17);
    }

    /**
     * This method gets called when a bound property is changed on the
     * associated JTextComponent. This is a hook which UI implementations may
     * change to reflect how the UI displays bound properties of JTextComponent
     * subclasses. This is implemented to do nothing (i.e. the response to
     * properties in JTextComponent itself are handled prior to calling this
     * method).
     *
     * @param evt the property change event
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(evt)) {
            updateStyle((JTextComponent) evt.getSource());
        }

        super.propertyChange(evt);
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        getComponent().repaint();
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {
        getComponent().repaint();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#installDefaults()
     */
    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();

        JTextComponent c = getComponent();

        updateStyle(c);
        c.addFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        JTextComponent  c       = getComponent();
        SeaGlassContext context = getContext(c, ENABLED);

        // Remove the search border, if present.
        Border          border  = c.getBorder();

        if (border instanceof TextFieldBorder) {
            c.setBorder(null);
        }

        c.putClientProperty("caretAspectRatio", null);
        c.removeFocusListener(this);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;

        super.uninstallDefaults();
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#uninstallListeners()
     */
    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        uninstallMouseListeners();
    }

    /**
     * DOCUMENT ME!
     */
    private void installMouseListeners() {
        if (mouseListener == null) {
            mouseListener = createMouseListener();

            getComponent().addMouseListener(mouseListener);
            getComponent().addMouseMotionListener(mouseListener);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void uninstallMouseListeners() {
        if (mouseListener != null) {
            getComponent().removeMouseListener(mouseListener);
            getComponent().removeMouseMotionListener(mouseListener);
            mouseListener = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected MouseAdapter createMouseListener() {
        if (mouseListener == null) {
            mouseListener = new MouseButtonListener();
        }

        return mouseListener;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        super.installUI(c);
        updateStyle((JTextComponent) c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected TextFieldBorder createTextFieldBorder(SeaGlassContext context) {
        if (textFieldBorder == null) {
            textFieldBorder = new TextFieldBorder(this, context.getStyle().getInsets(context, null));
        }

        return textFieldBorder;
    }

    /**
     * @see javax.swing.plaf.basic.BasicTextUI#getPreferredSize(javax.swing.JComponent)
     */
    public Dimension getPreferredSize(JComponent c) {
        // The following code has been derived from BasicTextUI.
        Document  doc      = ((JTextComponent) c).getDocument();
        Insets    i        = c.getInsets();
        Dimension d        = c.getSize();
        View      rootView = getRootView((JTextComponent) c);

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

            d.width  = (int) Math.min((long) rootView.getPreferredSpan(View.X_AXIS) + (long) i.left + (long) i.right, Integer.MAX_VALUE);
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

    /**
     * DOCUMENT ME!
     */
    private void doFind() {
        if (findAction != null) {
            fireAction(findAction);
        }

        doPopup();
    }

    /**
     * DOCUMENT ME!
     */
    private void doCancel() {
        // Erase the text in the search field.
        getComponent().setText("");

        if (cancelAction != null) {
            fireAction(cancelAction);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void doPopup() {
        if (findPopup != null) {
            JTextComponent c = getComponent();

            findPopup.pack();
            // The "-1" just snugs us up a bit under the text field.
            findPopup.show(c, 0, c.getHeight() - 1);

            // Set focus back to the text field.
            // TODO Fix caret positioning, selection, etc.
            c.requestFocusInWindow();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    protected void fireAction(ActionListener action) {
        int      modifiers    = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();

        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }

        ActionEvent e = new ActionEvent(getComponent(), ActionEvent.ACTION_PERFORMED, getComponent().getText(),
                                        EventQueue.getMostRecentEventTime(), modifiers);

        action.actionPerformed(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    protected class TextFieldBorder extends SeaGlassBorder {
        private static final long serialVersionUID = -3475926670707905862L;

        /**
         * Creates a new TextFieldBorder object.
         *
         * @param ui     DOCUMENT ME!
         * @param insets DOCUMENT ME!
         */
        public TextFieldBorder(SeaglassUI ui, Insets insets) {
            super(ui, insets);
        }

        /**
         * Reinitialize the insets parameter with this Border's current Insets.
         *
         * @param  c      the component for which this border insets value
         *                applies
         * @param  insets the object to be reinitialized
         *
         * @return DOCUMENT ME!
         */
        public Insets getBorderInsets(Component c, Insets insets) {
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }

            super.getBorderInsets(c, insets);

            if (c instanceof JComponent && isSearchField.isInState((JComponent) c)) {
                insets.left += searchIconWidth + searchLeftInnerMargin;
                if (hasPopupMenu.isInState((JComponent) c)) {
                    insets.left += popupIconWidth;
                }

                insets.right += cancelIconWidth + searchRightInnerMargin;
            }

            return insets;
        }
    }

    /**
     * Track mouse clicks and moves.
     */
    protected class MouseButtonListener extends MouseAdapter {

        /** DOCUMENT ME! */
        protected transient int currentMouseX;

        /** DOCUMENT ME! */
        protected transient int currentMouseY;

        /**
         * Creates a new MouseButtonListener object.
         */
        public MouseButtonListener() {
            isCancelArmed = false;
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {
            if (isCancelArmed) {
                isCancelArmed = false;
                if (isOverCancelButton()) {
                    doCancel();
                }

                getComponent().repaint();
            }
        }

        /**
         * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent e) {
            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (isCancelArmed && !isOverCancelButton()) {
                isCancelArmed = false;
                getComponent().repaint();
            }
        }

        /**
         * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || !getComponent().isEnabled()) {
                return;
            }

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (isOverFindButton()) {
                doFind();
            }

            if (isOverCancelButton()) {
                isCancelArmed = true;
                getComponent().repaint();
            } else if (isCancelArmed) {
                isCancelArmed = false;
                getComponent().repaint();
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            currentMouseX = e.getX();
            currentMouseY = e.getY();
            
            Cursor cursorToUse = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            if (isOverCancelButton() || isOverFindButton()) {
               cursorToUse = Cursor.getDefaultCursor();  
            }
            JComponent c = (JComponent) e.getSource();
            if (!cursorToUse.equals(c.getCursor())) {
                c.setCursor(cursorToUse);
            }
            super.mouseMoved(e);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private boolean isOverFindButton() {
            return getFindButtonBounds().contains(currentMouseX, currentMouseY);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isOverCancelButton() {
            return getCancelButtonBounds().contains(currentMouseX, currentMouseY);
        }
    }
}
