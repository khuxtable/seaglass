/*
 * @(#)SeaGlassContext.java	1.10 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

/**
 * An immutable transient object containing contextual information about a
 * <code>Region</code>. A <code>SeaGlassContext</code> should only be considered
 * valid for the duration of the method it is passed to. In other words you
 * should not cache a <code>SeaGlassContext</code> that is passed to you and
 * expect it to remain valid.
 * 
 * @version 1.10, 11/17/05
 * @since 1.5
 * @author Scott Violet
 */
public class SeaGlassContext extends SynthContext {
    private static final Map  contextMap;

    private static JButton    fakeComponent = new JButton();
    private static Region     fakeRegion    = Region.BUTTON;
    private static SynthStyle fakeStyle     = new SeaGlassStyle(null);

    private JComponent        component;
    private Region            region;
    private SynthStyle        style;
    private int               state;

    static {
        contextMap = new HashMap();
    }

    static SeaGlassContext getContext(Class type, JComponent component, Region region, SynthStyle style, int state) {
        SeaGlassContext context = null;

        synchronized (contextMap) {
            java.util.List instances = (java.util.List) contextMap.get(type);

            if (instances != null) {
                int size = instances.size();

                if (size > 0) {
                    context = (SeaGlassContext) instances.remove(size - 1);
                }
            }
        }
        if (context == null) {
            try {
                context = (SeaGlassContext) type.newInstance();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            } catch (InstantiationException ie) {
                ie.printStackTrace();
            }
        }
        context.reset(component, region, style, state);
        return context;
    }

    static void releaseContext(SeaGlassContext context) {
        synchronized (contextMap) {
            java.util.List instances = (java.util.List) contextMap.get(context.getClass());

            if (instances == null) {
                instances = new ArrayList(5);
                contextMap.put(context.getClass(), instances);
            }
            instances.add(context);
        }
    }

    public SeaGlassContext() {
        super(fakeComponent, fakeRegion, fakeStyle, 0);
    }

    /**
     * Creates a SeaGlassContext with the specified values. This is meant for
     * subclasses and custom UI implementors. You very rarely need to construct
     * a SeaGlassContext, though some methods will take one.
     * 
     * @param component
     *            JComponent
     * @param region
     *            Identifies the portion of the JComponent
     * @param style
     *            Style associated with the component
     * @param state
     *            State of the component as defined in SynthConstants.
     * @throws NullPointerException
     *             if component, region of style is null.
     */
    public SeaGlassContext(JComponent component, Region region, SynthStyle style, int state) {
        super(component, region, style, state);
        if (component == fakeComponent) {
            this.component = null;
            this.region = null;
            this.style = null;
            return;
        }
        if (component == null || region == null || style == null) {
            throw new NullPointerException("You must supply a non-null component, region and style");
        }
        reset(component, region, style, state);
    }

    /**
     * Returns the hosting component containing the region.
     * 
     * @return Hosting Component
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Returns the Region identifying this state.
     * 
     * @return Region of the hosting component
     */
    public Region getRegion() {
        return region;
    }

    /**
     * A convenience method for <code>getRegion().isSubregion()</code>.
     */
    @SuppressWarnings("all")
    public boolean isSubregion() {
        return getRegion().isSubregion();
    }

    @SuppressWarnings("all")
    public void setStyle(SynthStyle style) {
        this.style = style;
    }

    /**
     * Returns the style associated with this Region.
     * 
     * @return SynthStyle associated with the region.
     */
    public SynthStyle getStyle() {
        return style;
    }

    @SuppressWarnings("all")
    void setComponentState(int state) {
        this.state = state;
    }

    /**
     * Returns the state of the widget, which is a bitmask of the values defined
     * in <code>SynthConstants</code>. A region will at least be in one of
     * <code>ENABLED</code>, <code>MOUSE_OVER</code>, <code>PRESSED</code> or
     * <code>DISABLED</code>.
     * 
     * @see SynthConstants
     * @return State of Component
     */
    public int getComponentState() {
        return state;
    }

    /**
     * Resets the state of the Context.
     */
    @SuppressWarnings("all")
    public void reset(JComponent component, Region region, SynthStyle style, int state) {
        this.component = component;
        this.region = region;
        this.style = style;
        this.state = state;
    }

    @SuppressWarnings("all")
    public void dispose() {
        this.component = null;
        this.style = null;
        releaseContext(this);
    }

    /**
     * Convenience method to get the Painter from the current SynthStyle. This
     * will NEVER return null.
     */
    @SuppressWarnings("all")
    public SynthPainter getPainter() {
        SynthPainter painter = getStyle().getPainter(this);

        if (painter != null) {
            return painter;
        }
        return new SynthPainter() {
        };
    }
}
