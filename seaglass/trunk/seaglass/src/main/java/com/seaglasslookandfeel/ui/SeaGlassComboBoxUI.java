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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

import com.seaglasslookandfeel.SeaGlassContext;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.SeaGlassStyle;
import com.seaglasslookandfeel.component.SeaGlassArrowButton;
import com.seaglasslookandfeel.component.SeaGlassComboPopup;

/**
 * SeaGlassComboBoxUI.
 * 
 * Based on SynthComboBoxUI by Scott Violet.
 * 
 * @see javax.swing.plaf.synth.SynthComboBoxUI
 */
public class SeaGlassComboBoxUI extends BasicComboBoxUI implements PropertyChangeListener, SeaglassUI {
    private SeaGlassStyle      style;
    private boolean            useListColors;

    /**
     * Used to adjust the location and size of the popup. Very useful for
     * situations such as we find in Nimbus where part of the border is used to
     * paint the focus. In such cases, the border is empty space, and not part
     * of the "visual" border, and in these cases, you'd like the popup to be
     * adjusted such that it looks as if it were next to the visual border. You
     * may want to use negative insets to get the right look.
     */
    Insets                     popupInsets;

    /**
     * This flag may be set via UIDefaults. By default, it is false, to preserve
     * backwards compatibility. If true, then the combo will "act as a button"
     * when it is not editable.
     */
    private boolean            buttonWhenNotEditable;

    /**
     * A flag to indicate that the combo box and combo box button should remain
     * in the PRESSED state while the combo popup is visible.
     */
    private boolean            pressedWhenPopupVisible;

    /**
     * When buttonWhenNotEditable is true, this field is used to help make the
     * combo box appear and function as a button when the combo box is not
     * editable. In such a state, you can click anywhere on the button to get it
     * to open the popup. Also, anywhere you hover over the combo will cause the
     * entire combo to go into "rollover" state, and anywhere you press will go
     * into "pressed" state. This also keeps in sync the state of the combo and
     * the arrowButton.
     */
    private ButtonHandler      buttonHandler;

    /**
     * Handler for repainting combo when editor component gains/loses focus
     */
    private EditorFocusHandler editorFocusHandler;

    /**
     * If true, then the cell renderer will be forced to be non-opaque when used
     * for rendering the selected item in the combo box (not in the list), and
     * forced to opaque after rendering the selected value.
     */
    private boolean            forceOpaque = false;

    /**
     * NOTE: This serves the same purpose as the same field in BasicComboBoxUI.
     * It is here because I could not give the padding field in BasicComboBoxUI
     * protected access in an update release.
     */
    private Insets             padding;

    public static ComponentUI createUI(JComponent c) {
        return new SeaGlassComboBoxUI();
    }

    /**
     * Overridden to ensure that ButtonHandler is created prior to any of the
     * other installXXX methods, since several of them reference buttonHandler.
     */
    @Override
    public void installUI(JComponent c) {
        buttonHandler = new ButtonHandler();
        super.installUI(c);
    }

    @Override
    protected void installDefaults() {
        // NOTE: This next line of code was added because, since squareButton in
        // BasicComboBoxUI is private, I need to have some way of reading it
        // from UIManager.
        // This is an incomplete solution (since it implies that squareButons,
        // once set, cannot be reset per state. Probably ok, but not always ok).
        // This line of code should be removed at the same time that
        // squareButton
        // is made protected in the super class.
        super.installDefaults();

        // This is here instead of in updateStyle because the value for padding
        // needs to remain consistent with the value for padding in
        // BasicComboBoxUI. I wouldn't have this value here at all if not
        // for the fact that I cannot make "padding" protected in any way
        // for an update release. This *should* be fixed in Java 7
        padding = UIManager.getInsets("ComboBox.padding");

        updateStyle(comboBox);
    }

    private void updateStyle(JComboBox comboBox) {
        SeaGlassStyle oldStyle = style;
        SeaGlassContext context = getContext(comboBox, ENABLED);

        SynthStyle s = SeaGlassLookAndFeel.updateStyle(context, this);
        if (s instanceof SeaGlassStyle) {
            style = (SeaGlassStyle) s;
            if (style != oldStyle) {
                popupInsets = (Insets) style.get(context, "ComboBox.popupInsets");
                useListColors = style.getBoolean(context, "ComboBox.rendererUseListColors", true);
                buttonWhenNotEditable = style.getBoolean(context, "ComboBox.buttonWhenNotEditable", false);
                pressedWhenPopupVisible = style.getBoolean(context, "ComboBox.pressedWhenPopupVisible", false);

                if (oldStyle != null) {
                    uninstallKeyboardActions();
                    installKeyboardActions();
                }
                forceOpaque = style.getBoolean(context, "ComboBox.forceOpaque", false);
            }
        }
        context.dispose();
    }

    @Override
    protected void installListeners() {
        comboBox.addPropertyChangeListener(this);
        comboBox.addMouseListener(buttonHandler);
        editorFocusHandler = new EditorFocusHandler(comboBox);
        super.installListeners();
    }

    @Override
    public void uninstallUI(JComponent c) {
        if (popup instanceof SeaGlassComboPopup) {
            ((SeaGlassComboPopup) popup).removePopupMenuListener(buttonHandler);
        }
        super.uninstallUI(c);
        buttonHandler = null;
    }

    @Override
    protected void uninstallDefaults() {
        SeaGlassContext context = getContext(comboBox, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();
        style = null;
    }

    @Override
    protected void uninstallListeners() {
        editorFocusHandler.unregister();
        comboBox.removePropertyChangeListener(this);
        comboBox.removeMouseListener(buttonHandler);
        buttonHandler.pressed = false;
        buttonHandler.over = false;
        super.uninstallListeners();
    }

    public SeaGlassContext getContext(JComponent c) {
        return getContext(c, getComponentState(c));
    }

    private SeaGlassContext getContext(JComponent c, int state) {
        return SeaGlassContext.getContext(SeaGlassContext.class, c, SeaGlassLookAndFeel.getRegion(c), style, state);
    }

    private int getComponentState(JComponent c) {
        // currently we have a broken situation where if a developer
        // takes the border from a JComboBox and sets it on a JTextField
        // then the codepath will eventually lead back to this method
        // but pass in a JTextField instead of JComboBox! In case this
        // happens, we just return the normal synth state for the component
        // instead of doing anything special
        if (!(c instanceof JComboBox)) return SeaGlassLookAndFeel.getComponentState(c);

        JComboBox box = (JComboBox) c;
        if (shouldActLikeButton()) {
            int state = ENABLED;
            if ((!c.isEnabled())) {
                state = DISABLED;
            }
            if (buttonHandler.isPressed()) {
                state |= PRESSED;
            }
            if (buttonHandler.isRollover()) {
                state |= MOUSE_OVER;
            }
            if (box.isFocusOwner()) {
                state |= FOCUSED;
            }
            return state;
        } else {
            // for editable combos the editor component has the focus not the
            // combo box its self, so we should make the combo paint focused
            // when its editor has focus
            int basicState = SeaGlassLookAndFeel.getComponentState(c);
            if (box.isEditable() && box.getEditor().getEditorComponent().isFocusOwner()) {
                basicState |= FOCUSED;
            }
            return basicState;
        }
    }

    @Override
    protected ComboPopup createPopup() {
        SeaGlassComboPopup p = new SeaGlassComboPopup(comboBox);
        p.addPopupMenuListener(buttonHandler);
        return p;
    }

    @Override
    protected ListCellRenderer createRenderer() {
        return new SynthComboBoxRenderer();
    }

    @Override
    protected ComboBoxEditor createEditor() {
    	// In case the combo box is editable we inherit the size variant to the editor.
        // TODO this needs to be done later to support client property override
        // because the editor is created in the ComboBox constructor already a later set of the property has no effect
        SynthComboBoxEditor result = new SynthComboBoxEditor();
        String scaleKey = SeaGlassStyle.getSizeVariant(comboBox);
        if (scaleKey != null) {
            ((JComponent)result.getEditorComponent()).putClientProperty("JComponent.sizeVariant", scaleKey);
        }
        return result;

    }

    //
    // end UI Initialization
    // ======================

    public void propertyChange(PropertyChangeEvent e) {
        if (SeaGlassLookAndFeel.shouldUpdateStyle(e)) {
            updateStyle(comboBox);
        }
    }

    @Override
    protected JButton createArrowButton() {
        SeaGlassArrowButton button = new SeaGlassArrowButton(SwingConstants.SOUTH);
        button.setName("ComboBox.arrowButton");
        button.setModel(buttonHandler);
        return button;
    }

    // =================================
    // begin ComponentUI Implementation

    @Override
    public void update(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        SeaGlassLookAndFeel.update(context, g);
        context.getPainter().paintComboBoxBackground(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        SeaGlassContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SeaGlassContext context, Graphics g) {
        hasFocus = comboBox.hasFocus();
        if (!comboBox.isEditable()) {
            Rectangle r = rectangleForCurrentValue();
            paintCurrentValue(g, r, hasFocus);
        }
    }

    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        ((SeaGlassContext) context).getPainter().paintComboBoxBorder(context, g, x, y, w, h);
    }

    /**
     * Paints the currently selected item.
     */
    @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer renderer = comboBox.getRenderer();
        Component c;

        c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);

        // Fix for 4238829: should lay out the JPanel.
        boolean shouldValidate = false;
        if (c instanceof JPanel) {
            shouldValidate = true;
        }

        if (c instanceof UIResource) {
            c.setName("ComboBox.renderer");
        }

        boolean force = forceOpaque && c instanceof JComponent;
        if (force) {
            ((JComponent) c).setOpaque(false);
        }

        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        if (padding != null) {
            x = bounds.x + padding.left;
            y = bounds.y + padding.top;
            w = bounds.width - (padding.left + padding.right);
            h = bounds.height - (padding.top + padding.bottom);
        }

        currentValuePane.paintComponent(g, c, comboBox, x, y, w, h, shouldValidate);

        if (force) {
            ((JComponent) c).setOpaque(true);
        }

    }

    /**
     * @return true if this combo box should act as one big button. Typically
     *         only happens when buttonWhenNotEditable is true, and
     *         comboBox.isEditable is false.
     */
    private boolean shouldActLikeButton() {
        return buttonWhenNotEditable && !comboBox.isEditable();
    }

    /**
     * Return the default size of an empty display area of the combo box using
     * the current renderer and font.
     * 
     * This method was overridden to use SynthComboBoxRenderer instead of
     * DefaultListCellRenderer as the default renderer when calculating the size
     * of the combo box. This is used in the case of the combo not having any
     * data.
     * 
     * @return the size of an empty display area
     * @see #getDisplaySize
     */
    @Override
    protected Dimension getDefaultSize() {
        SynthComboBoxRenderer r = new SynthComboBoxRenderer();
        Dimension d = getSizeForComponent(r.getListCellRendererComponent(listBox, " ", -1, false, false));
        return new Dimension(d.width, d.height);
    }

    /**
     * This has been refactored out in hopes that it may be investigated and
     * simplified for the next major release. adding/removing the component to
     * the currentValuePane and changing the font may be redundant operations.
     * 
     * NOTE: This method was copied in its entirety from BasicComboBoxUI. Might
     * want to make it protected in BasicComboBoxUI in Java 7
     */
    protected Dimension getSizeForComponent(Component comp) {
        currentValuePane.add(comp);
        
        Font f = comboBox.getFont();
        
        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = SeaGlassStyle.getSizeVariant(comboBox);
        if (scaleKey != null) {
            if (SeaGlassStyle.LARGE_KEY.equals(scaleKey)) {
                f = f.deriveFont(f.getSize2D()*1.15f);
            } else if (SeaGlassStyle.SMALL_KEY.equals(scaleKey)) {
                f = f.deriveFont(f.getSize2D()*0.857f);
            } else if (SeaGlassStyle.MINI_KEY.equals(scaleKey)) {
                f = f.deriveFont(f.getSize2D()*0.784f);
            }
        }
        comp.setFont(f);
        Dimension d = comp.getPreferredSize();
        currentValuePane.remove(comp);
        return d;
    }

    /**
     * From BasicComboBoxRenderer v 1.18.
     * 
     * Be aware that SynthFileChooserUIImpl relies on the fact that the default
     * renderer installed on a Synth combo box is a JLabel. If this is changed,
     * then an assert will fail in SynthFileChooserUIImpl
     */
    private class SynthComboBoxRenderer extends JLabel implements ListCellRenderer, UIResource {
        public SynthComboBoxRenderer() {
            super();
            setName("ComboBox.renderer");
            setText(" ");
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setName("ComboBox.listRenderer");
            SeaGlassLookAndFeel.resetSelectedUI();
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                if (!useListColors) {
                    SeaGlassLookAndFeel.setSelectedUI((SeaGlassLabelUI) SeaGlassLookAndFeel.getUIOfType(getUI(), SeaGlassLabelUI.class),
                        isSelected, cellHasFocus, list.isEnabled(), false);
                }
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setFont(list.getFont());

            if (value instanceof Icon) {
                setIcon((Icon) value);
                setText("");
            } else {
                String text = (value == null) ? " " : value.toString();

                if ("".equals(text)) {
                    text = " ";
                }
                setText(text);
            }

            // The renderer component should inherit the enabled and
            // orientation state of its parent combobox. This is
            // especially needed for GTK comboboxes, where the
            // ListCellRenderer's state determines the visual state
            // of the combobox.
            if (comboBox != null) {
                setEnabled(comboBox.isEnabled());
                setComponentOrientation(comboBox.getComponentOrientation());
            }

            return this;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            SeaGlassLookAndFeel.resetSelectedUI();
        }
    }

    /**
     * From BasicCombBoxEditor v 1.24.
     */
    private static class SynthComboBoxEditor implements ComboBoxEditor, UIResource {
        protected JTextField editor;
        private Object       oldValue;

        public SynthComboBoxEditor() {
            editor = new JTextField("", 9);
            editor.setName("ComboBox.textField");
        }

        public Component getEditorComponent() {
            return editor;
        }

        /**
         * Sets the item that should be edited.
         * 
         * @param anObject
         *            the displayed value of the editor
         */
        public void setItem(Object anObject) {
            String text;

            if (anObject != null) {
                text = anObject.toString();
                oldValue = anObject;
            } else {
                text = "";
            }
            // workaround for 4530952
            if (!text.equals(editor.getText())) {
                editor.setText(text);
            }
        }

        public Object getItem() {
            Object newValue = editor.getText();

            if (oldValue != null && !(oldValue instanceof String)) {
                // The original value is not a string. Should return the value
                // in it's
                // original type.
                if (newValue.equals(oldValue.toString())) {
                    return oldValue;
                } else {
                    // Must take the value from the editor and get the value and
                    // cast it to the new type.
                    Class cls = oldValue.getClass();
                    try {
                        Method method = cls.getMethod("valueOf", new Class[] { String.class });
                        newValue = method.invoke(oldValue, new Object[] { editor.getText() });
                    } catch (Exception ex) {
                        // Fail silently and return the newValue (a String
                        // object)
                    }
                }
            }
            return newValue;
        }

        public void selectAll() {
            editor.selectAll();
            editor.requestFocus();
        }

        public void addActionListener(ActionListener l) {
            editor.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            editor.removeActionListener(l);
        }
    }

    /**
     * Handles all the logic for treating the combo as a button when it is not
     * editable, and when shouldActLikeButton() is true. This class is a special
     * ButtonModel, and installed on the arrowButton when appropriate. It also
     * is installed as a mouse listener and mouse motion listener on the combo
     * box. In this way, the state between the button and combo are in sync.
     * Whenever one is "over" both are. Whenever one is pressed, both are.
     */
    private final class ButtonHandler extends DefaultButtonModel implements MouseListener, PopupMenuListener {
        /**
         * Indicates that the mouse is over the combo or the arrow button. This
         * field only has meaning if buttonWhenNotEnabled is true.
         */
        private boolean over;
        /**
         * Indicates that the combo or arrow button has been pressed. This field
         * only has meaning if buttonWhenNotEnabled is true.
         */
        private boolean pressed;

        // ------------------------------------------------------------------
        // State Methods
        // ------------------------------------------------------------------

        /**
         * <p>
         * Updates the internal "pressed" state. If shouldActLikeButton() is
         * true, and if this method call will change the internal state, then
         * the combo and button will be repainted.
         * </p>
         * 
         * <p>
         * Note that this method is called either when a press event occurs on
         * the combo box, or on the arrow button.
         * </p>
         */
        private void updatePressed(boolean p) {
            this.pressed = p && isEnabled();
            if (shouldActLikeButton()) {
                comboBox.repaint();
            }
        }

        /**
         * <p>
         * Updates the internal "over" state. If shouldActLikeButton() is true,
         * and if this method call will change the internal state, then the
         * combo and button will be repainted.
         * </p>
         * 
         * <p>
         * Note that this method is called either when a mouseover/mouseoff
         * event occurs on the combo box, or on the arrow button.
         * </p>
         */
        private void updateOver(boolean o) {
            boolean old = isRollover();
            this.over = o && isEnabled();
            boolean newo = isRollover();
            if (shouldActLikeButton() && old != newo) {
                comboBox.repaint();
            }
        }

        // ------------------------------------------------------------------
        // DefaultButtonModel Methods
        // ------------------------------------------------------------------

        /**
         * Indicates if the button is pressed.
         * 
         * @return <code>true</code> if the button is pressed
         * 
         *         Ensures that isPressed() will return true if the combo is
         *         pressed, or the arrowButton is pressed, <em>or</em> if the
         *         combo popup is visible. This is the case because a combo box
         *         looks pressed when the popup is visible, and so should the
         *         arrow button.
         */
        @Override
        public boolean isPressed() {
            boolean b = shouldActLikeButton() ? pressed : super.isPressed();
            return b || (pressedWhenPopupVisible && comboBox.isPopupVisible());
        }

        /**
         * Indicates partial commitment towards triggering the button.
         * 
         * @return <code>true</code> if the button is armed, and ready to be
         *         triggered
         * @see javax.swing.ButtonModel#setArmed
         * 
         *      Ensures that the armed state is in sync with the pressed state
         *      if shouldActLikeButton is true. Without this method, the arrow
         *      button will not look pressed when the popup is open, regardless
         *      of the result of isPressed() alone.
         */
        @Override
        public boolean isArmed() {
            boolean b = shouldActLikeButton() || (pressedWhenPopupVisible && comboBox.isPopupVisible());
            return b ? isPressed() : super.isArmed();
        }

        /**
         * Indicates that the mouse is over the button.
         * 
         * @return <code>true</code> if the mouse is over the button
         * 
         *         Ensures that isRollover() will return true if the combo is
         *         rolled over, or the arrowButton is rolled over.
         */
        @Override
        public boolean isRollover() {
            return shouldActLikeButton() ? over : super.isRollover();
        }

        /**
         * Sets the button to pressed or unpressed.
         * 
         * @param b
         *            whether or not the button should be pressed
         * @see javax.swing.ButtonModel#isPressed
         * 
         *      Forwards pressed states to the internal "pressed" field
         */
        @Override
        public void setPressed(boolean b) {
            super.setPressed(b);
            updatePressed(b);
        }

        /**
         * Sets or clears the button's rollover state
         * 
         * @param b
         *            whether or not the button is in the rollover state
         * @see javax.swing.ButtonModel#isRollover
         * 
         *      Forwards rollover states to the internal "over" field
         */
        @Override
        public void setRollover(boolean b) {
            super.setRollover(b);
            updateOver(b);
        }

        // ------------------------------------------------------------------
        // MouseListener/MouseMotionListener Methods
        // ------------------------------------------------------------------

        public void mouseEntered(MouseEvent mouseEvent) {
            updateOver(true);
        }

        public void mouseExited(MouseEvent mouseEvent) {
            updateOver(false);
        }

        public void mousePressed(MouseEvent mouseEvent) {
            updatePressed(true);
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            updatePressed(false);
        }

        public void mouseClicked(MouseEvent e) {
        }

        // ------------------------------------------------------------------
        // PopupMenuListener Methods
        // ------------------------------------------------------------------

        /**
         * This method is called when the popup menu is canceled
         * 
         * Ensures that the combo box is repainted when the popup is closed.
         * This avoids a bug where clicking off the combo wasn't causing a
         * repaint, and thus the combo box still looked pressed even when it was
         * not.
         * 
         * This bug was only noticed when acting as a button, but may be
         * generally present. If so, remove the if() block
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            if (shouldActLikeButton() || pressedWhenPopupVisible) {
                comboBox.repaint();
            }
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }
    }

    /**
     * Handler for repainting combo when editor component gains/loses focus
     */
    private static class EditorFocusHandler implements FocusListener, PropertyChangeListener {
        private JComboBox      comboBox;
        private ComboBoxEditor editor          = null;
        private Component      editorComponent = null;

        private EditorFocusHandler(JComboBox comboBox) {
            this.comboBox = comboBox;
            editor = comboBox.getEditor();
            if (editor != null) {
                editorComponent = editor.getEditorComponent();
                if (editorComponent != null) {
                    editorComponent.addFocusListener(this);
                }
            }
            comboBox.addPropertyChangeListener("editor", this);
        }

        public void unregister() {
            comboBox.removePropertyChangeListener(this);
            if (editorComponent != null) {
                editorComponent.removeFocusListener(this);
            }
        }

        /** Invoked when a component gains the keyboard focus. */
        public void focusGained(FocusEvent e) {
            // repaint whole combo on focus gain
            comboBox.repaint();
        }

        /** Invoked when a component loses the keyboard focus. */
        public void focusLost(FocusEvent e) {
            // repaint whole combo on focus loss
            comboBox.repaint();
        }

        /**
         * Called when the combos editor changes
         * 
         * @param evt
         *            A PropertyChangeEvent object describing the event source
         *            and the property that has changed.
         */
        public void propertyChange(PropertyChangeEvent evt) {
            ComboBoxEditor newEditor = comboBox.getEditor();
            if (editor != newEditor) {
                if (editorComponent != null) {
                    editorComponent.removeFocusListener(this);
                }
                editor = newEditor;
                if (editor != null) {
                    editorComponent = editor.getEditorComponent();
                    if (editorComponent != null) {
                        editorComponent.addFocusListener(this);
                    }
                }
            }
        }
    }
}
