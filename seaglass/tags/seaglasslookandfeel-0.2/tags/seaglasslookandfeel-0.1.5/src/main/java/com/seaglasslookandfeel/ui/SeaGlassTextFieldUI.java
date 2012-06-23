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
import java.awt.Dimension;
import java.awt.EventQueue;
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

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassRegion;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.SeaGlassSynthPainterImpl;
import com.seaglasslookandfeel.component.SeaGlassBorder;
import com.seaglasslookandfeel.state.SearchFieldHasPopupState;
import com.seaglasslookandfeel.state.State;
import com.seaglasslookandfeel.state.TextFieldIsSearchState;

import sun.swing.plaf.synth.SynthUI;

/**
 * Sea Glass TextField UI delegate.
 * 
 * Based on SynthTextFieldUI, but we need to set preferred sizes and handle
 * search fields.
 */
public class SeaGlassTextFieldUI extends BasicTextFieldUI implements SynthUI, FocusListener {

    private static final State isSearchField = new TextFieldIsSearchState();
    private static final State hasPopupMenu  = new SearchFieldHasPopupState();

    private TextFieldBorder    textFieldBorder;

    private MouseAdapter       mouseListener;

    private boolean            isCancelArmed;

    private SynthStyle         style;
    private SynthStyle         findStyle;
    private SynthStyle         cancelStyle;

    private String             placeholderText;
    private Color              placeholderColor;

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

    private void updateStyle(JTextComponent c) {
        SeaGlassContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SeaGlassLookAndFeel.updateStyle(context, this);

        updateSearchStyle(c, context, getPropertyPrefix());

        if (style != oldStyle) {
            updateStyle(c, context, getPropertyPrefix());

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();

        context = getContext(c, SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON, ENABLED);
        findStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();

        context = getContext(c, SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON, ENABLED);
        cancelStyle = SeaGlassLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    /**
     * Sea Glass code to support the search JTextField.variant.
     * 
     * @param c
     *            the JTextField component.
     * @param context
     *            the SeaGlassContext.
     * @param prefix
     *            the control prefix, e.g. "TextField", "FormattedTextField", or
     *            "PasswordField".
     */
    private void updateSearchStyle(JTextComponent c, SeaGlassContext context, String prefix) {
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

        placeholderColor = Color.GRAY;
        o = style.get(context, "placeholderTextColor");
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
     * @param c
     *            the JTextField component.
     * @param context
     *            the SeaGlassContext.
     * @param prefix
     *            the control prefix, e.g. "TextField", "FormattedTextField", or
     *            "PasswordField".
     */
    private void updateStyle(JTextComponent c, SeaGlassContext context, String prefix) {
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

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private SeaGlassContext getContext(JComponent c, Region region) {
        return getContext(c, region, getComponentState(c, region));
    }

    private SeaGlassContext getContext(JComponent c, Region region, int state) {
        SynthStyle style = findStyle;

        if (region == SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON) {
            style = cancelStyle;
        }
        return SeaGlassContext.getContext(SeaGlassContext.class, c, region, style, state);
    }

    private int getComponentState(JComponent c) {
        int state = SeaGlassLookAndFeel.getComponentState(c);
        return state;
    }

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
        JTextComponent c = getComponent();
        super.paint(g, c);
    }

    void paintBackground(SeaGlassContext context, Graphics g, JComponent c) {
        context.getPainter().paintTextFieldBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        // If necessary, paint the placeholder text.
        if (placeholderText != null && ((JTextComponent) c).getText().length() == 0 && !c.hasFocus()) {
            paintPlaceholderText(context, g, c);
        }
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintTextFieldBorder(context, g, x, y, w, h);

        JTextComponent c = getComponent();
        if (isSearchField.isInState(c)) {
            paintSearchButton(g, c, SeaGlassRegion.SEARCH_FIELD_FIND_BUTTON);
            paintSearchButton(g, c, SeaGlassRegion.SEARCH_FIELD_CANCEL_BUTTON);
        }
    }

    protected void paintBackground(Graphics g) {
        // Overridden to do nothing, all our painting is done from update/paint.
    }

    protected void paintPlaceholderText(SeaGlassContext context, Graphics g, JComponent c) {
        g.setColor(placeholderColor);
        g.setFont(c.getFont());
        Rectangle innerArea = SwingUtilities.calculateInnerArea(c, null);
        // TODO Do better baseline calculation than just subtracting 1.
        context.getStyle().getGraphicsUtils(context).paintText(context, g, placeholderText, innerArea.x, innerArea.y - 1, -1);
    }

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

    protected Rectangle getFindButtonBounds() {
        JTextComponent c = getComponent();
        final int x = c.getHeight() / 2 - 6;
        final int y = c.getHeight() / 2 - 6;

        return new Rectangle(x, y, 22, 17);
    }

    protected Rectangle getCancelButtonBounds() {
        JTextComponent c = getComponent();
        final int x = c.getWidth() - c.getHeight() / 2 - 9;
        final int y = c.getHeight() / 2 - 8;

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
        c.addFocusListener(this);
    }

    protected void uninstallDefaults() {
        JTextComponent c = getComponent();
        SeaGlassContext context = getContext(c, ENABLED);

        // Remove the search border, if present.
        Border border = c.getBorder();
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

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        uninstallMouseListeners();
    }

    /**
     * 
     */
    private void installMouseListeners() {
        if (mouseListener == null) {
            mouseListener = createMouseListener();

            getComponent().addMouseListener(mouseListener);
            getComponent().addMouseMotionListener(mouseListener);
        }
    }

    /**
     * 
     */
    private void uninstallMouseListeners() {
        if (mouseListener != null) {
            getComponent().removeMouseListener(mouseListener);
            getComponent().removeMouseMotionListener(mouseListener);
            mouseListener = null;
        }
    }

    protected MouseAdapter createMouseListener() {
        if (mouseListener == null) {
            mouseListener = new MouseButtonListener();
        }
        return mouseListener;
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        updateStyle((JTextComponent) c);
    }

    protected TextFieldBorder createTextFieldBorder(SeaGlassContext context) {
        if (textFieldBorder == null) {
            textFieldBorder = new TextFieldBorder(this, context.getStyle().getInsets(context, null));
        }
        return textFieldBorder;
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

    private void doFind() {
        if (findAction != null) {
            fireAction(findAction);
        }

        doPopup();
    }

    private void doCancel() {
        // Erase the text in the search field.
        getComponent().setText("");

        if (cancelAction != null) {
            fireAction(cancelAction);
        }
    }

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

    protected class TextFieldBorder extends SeaGlassBorder {

        public TextFieldBorder(SynthUI ui, Insets insets) {
            super(ui, insets);
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
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }

            super.getBorderInsets(c, insets);

            if (isSearchField.isInState((JTextComponent) c)) {
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
        protected transient int currentMouseX, currentMouseY;

        public MouseButtonListener() {
            isCancelArmed = false;
        }

        public void mouseReleased(MouseEvent e) {
            if (isCancelArmed) {
                isCancelArmed = false;
                if (isOverCancelButton()) {
                    doCancel();
                }
                getComponent().repaint();
            }
        }

        public void mouseDragged(MouseEvent e) {

            currentMouseX = e.getX();
            currentMouseY = e.getY();

            if (isCancelArmed && !isOverCancelButton()) {
                isCancelArmed = false;
                getComponent().repaint();
            }
        }

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

        private boolean isOverFindButton() {
            return getFindButtonBounds().contains(currentMouseX, currentMouseY);
        }

        public boolean isOverCancelButton() {
            return getCancelButtonBounds().contains(currentMouseX, currentMouseY);
        }
    }
}
