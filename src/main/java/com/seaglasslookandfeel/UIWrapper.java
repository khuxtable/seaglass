package com.seaglasslookandfeel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

public class UIWrapper extends UIDefaults {
    
    private UIDefaults delegate;
    private Set<Object> missingDefaults = new HashSet();

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#get(java.lang.Object)
     */
    public Object get(Object key) {
        Object object = delegate.get(key);
//        if (object == null) {
//            missingDefaults.add(key);
//        }
        return object;
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#get(java.lang.Object, java.util.Locale)
     */
    public Object get(Object key, Locale l) {
        return delegate.get(key, l);
    }

    /**
     * @return
     * @see java.util.Hashtable#size()
     */
    public int size() {
        return delegate.size();
    }

    /**
     * @return
     * @see java.util.Hashtable#isEmpty()
     */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * @return
     * @see java.util.Hashtable#keys()
     */
    public Enumeration<Object> keys() {
        return delegate.keys();
    }

    /**
     * @return
     * @see java.util.Hashtable#elements()
     */
    public Enumeration<Object> elements() {
        return delegate.elements();
    }

    /**
     * @param value
     * @return
     * @see java.util.Hashtable#contains(java.lang.Object)
     */
    public boolean contains(Object value) {
        return delegate.contains(value);
    }

    /**
     * @param value
     * @return
     * @see java.util.Hashtable#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    /**
     * @param key
     * @param value
     * @return
     * @see javax.swing.UIDefaults#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value) {
        return delegate.put(key, value);
    }

    /**
     * @param key
     * @return
     * @see java.util.Hashtable#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    /**
     * @param keyValueList
     * @see javax.swing.UIDefaults#putDefaults(java.lang.Object[])
     */
    public void putDefaults(Object[] keyValueList) {
        delegate.putDefaults(keyValueList);
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getFont(java.lang.Object)
     */
    public Font getFont(Object key) {
        return delegate.getFont(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getFont(java.lang.Object, java.util.Locale)
     */
    public Font getFont(Object key, Locale l) {
        return delegate.getFont(key, l);
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getColor(java.lang.Object)
     */
    public Color getColor(Object key) {
        return delegate.getColor(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getColor(java.lang.Object, java.util.Locale)
     */
    public Color getColor(Object key, Locale l) {
        return delegate.getColor(key, l);
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getIcon(java.lang.Object)
     */
    public Icon getIcon(Object key) {
        return delegate.getIcon(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getIcon(java.lang.Object, java.util.Locale)
     */
    public Icon getIcon(Object key, Locale l) {
        return delegate.getIcon(key, l);
    }

    /**
     * @param key
     * @return
     * @see java.util.Hashtable#remove(java.lang.Object)
     */
    public Object remove(Object key) {
        return delegate.remove(key);
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getBorder(java.lang.Object)
     */
    public Border getBorder(Object key) {
        return delegate.getBorder(key);
    }

    /**
     * @param t
     * @see java.util.Hashtable#putAll(java.util.Map)
     */
    public void putAll(Map<? extends Object, ? extends Object> t) {
        delegate.putAll(t);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getBorder(java.lang.Object, java.util.Locale)
     */
    public Border getBorder(Object key, Locale l) {
        return delegate.getBorder(key, l);
    }

    /**
     * 
     * @see java.util.Hashtable#clear()
     */
    public void clear() {
        delegate.clear();
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getString(java.lang.Object)
     */
    public String getString(Object key) {
        return delegate.getString(key);
    }

    /**
     * @return
     * @see java.util.Hashtable#clone()
     */
    public Object clone() {
        return delegate.clone();
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getString(java.lang.Object, java.util.Locale)
     */
    public String getString(Object key, Locale l) {
        return delegate.getString(key, l);
    }

    /**
     * @return
     * @see java.util.Hashtable#toString()
     */
    public String toString() {
        return Arrays.toString(missingDefaults.toArray());
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getInt(java.lang.Object)
     */
    public int getInt(Object key) {
        return delegate.getInt(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getInt(java.lang.Object, java.util.Locale)
     */
    public int getInt(Object key, Locale l) {
        return delegate.getInt(key, l);
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getBoolean(java.lang.Object)
     */
    public boolean getBoolean(Object key) {
        return delegate.getBoolean(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getBoolean(java.lang.Object, java.util.Locale)
     */
    public boolean getBoolean(Object key, Locale l) {
        return delegate.getBoolean(key, l);
    }

    /**
     * @return
     * @see java.util.Hashtable#keySet()
     */
    public Set<Object> keySet() {
        return delegate.keySet();
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getInsets(java.lang.Object)
     */
    public Insets getInsets(Object key) {
        return delegate.getInsets(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getInsets(java.lang.Object, java.util.Locale)
     */
    public Insets getInsets(Object key, Locale l) {
        return delegate.getInsets(key, l);
    }

    /**
     * @return
     * @see java.util.Hashtable#entrySet()
     */
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        return delegate.entrySet();
    }

    /**
     * @param key
     * @return
     * @see javax.swing.UIDefaults#getDimension(java.lang.Object)
     */
    public Dimension getDimension(Object key) {
        return delegate.getDimension(key);
    }

    /**
     * @param key
     * @param l
     * @return
     * @see javax.swing.UIDefaults#getDimension(java.lang.Object, java.util.Locale)
     */
    public Dimension getDimension(Object key, Locale l) {
        return delegate.getDimension(key, l);
    }

    /**
     * @param uiClassID
     * @param uiClassLoader
     * @return
     * @see javax.swing.UIDefaults#getUIClass(java.lang.String, java.lang.ClassLoader)
     */
    public Class<? extends ComponentUI> getUIClass(String uiClassID, ClassLoader uiClassLoader) {
        return delegate.getUIClass(uiClassID, uiClassLoader);
    }

    /**
     * @return
     * @see java.util.Hashtable#values()
     */
    public Collection<Object> values() {
        return delegate.values();
    }

    /**
     * @param uiClassID
     * @return
     * @see javax.swing.UIDefaults#getUIClass(java.lang.String)
     */
    public Class<? extends ComponentUI> getUIClass(String uiClassID) {
        return delegate.getUIClass(uiClassID);
    }

    /**
     * @param o
     * @return
     * @see java.util.Hashtable#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    /**
     * @param target
     * @return
     * @see javax.swing.UIDefaults#getUI(javax.swing.JComponent)
     */
    public ComponentUI getUI(JComponent target) {
        return delegate.getUI(target);
    }

    /**
     * @return
     * @see java.util.Hashtable#hashCode()
     */
    public int hashCode() {
        return delegate.hashCode();
    }

    /**
     * @param listener
     * @see javax.swing.UIDefaults#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        delegate.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     * @see javax.swing.UIDefaults#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        delegate.removePropertyChangeListener(listener);
    }

    /**
     * @return
     * @see javax.swing.UIDefaults#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return delegate.getPropertyChangeListeners();
    }

    /**
     * @param bundleName
     * @see javax.swing.UIDefaults#addResourceBundle(java.lang.String)
     */
    public void addResourceBundle(String bundleName) {
        delegate.addResourceBundle(bundleName);
    }

    /**
     * @param bundleName
     * @see javax.swing.UIDefaults#removeResourceBundle(java.lang.String)
     */
    public void removeResourceBundle(String bundleName) {
        delegate.removeResourceBundle(bundleName);
    }

    /**
     * @param l
     * @see javax.swing.UIDefaults#setDefaultLocale(java.util.Locale)
     */
    public void setDefaultLocale(Locale l) {
        delegate.setDefaultLocale(l);
    }

    /**
     * @return
     * @see javax.swing.UIDefaults#getDefaultLocale()
     */
    public Locale getDefaultLocale() {
        return delegate.getDefaultLocale();
    }

    /**
     * @param delegate
     */
    public UIWrapper(UIDefaults delegate) {
        super();
        this.delegate = delegate;
        put("missing.keys", this.missingDefaults);
    }
 
}
