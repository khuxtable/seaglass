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
package com.seaglasslookandfeel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * <p>Extends SynthContext by Scott Violet and reimplements most of its methods
 * because too much is package local.</p>
 *
 * @see javax.swing.plaf.synth.SynthContext
 */
public class SeaGlassContext extends SynthContext {

    private static final SynthPainter EMPTY_PAINTER = new SynthPainter() {
    };

    /** This button *must* not have a UI or we end up throwing an NPE. */
    private static final JButton FAKE_BUTTON   = new JButton() {
        public void updateUI() {
            // Do nothing.
        }
    };

    // Fake items are created because there's no public empty constructor for
    // SynthContext and we need a way to determine when our own empty
    // constructor was called. We pass these to SynthContext's constructor and
    // then test for them later.

    private static final JButton fakeComponent = FAKE_BUTTON;
    private static Region        fakeRegion    = Region.BUTTON;
    private static SynthStyle    fakeStyle     = new SeaGlassStyle(null, null);

    // From SynthContext.

    private static final Map     contextMap;

    static {
        contextMap = new HashMap();
    }

    private JComponent component;
    private Region     region;
    private SynthStyle style;
    private int        state;

    /**
     * Creates a new SeaGlassContext object.
     */
    public SeaGlassContext() {
        super(fakeComponent, fakeRegion, fakeStyle, 0);
    }

    /**
     * Creates a SeaGlassContext with the specified values. This is meant for
     * subclasses and custom UI implementors. You very rarely need to construct
     * a SeaGlassContext, though some methods will take one.
     *
     * @param component JComponent
     * @param region    Identifies the portion of the JComponent
     * @param style     Style associated with the component
     * @param state     State of the component as defined in SynthConstants.
     */
    public SeaGlassContext(JComponent component, Region region, SynthStyle style, int state) {
        super(component, region, style, state);

        if (component == fakeComponent) {
            this.component = null;
            this.region    = null;
            this.style     = null;

            return;
        }

        if (component == null || region == null || style == null) {
            throw new NullPointerException("You must supply a non-null component, region and style");
        }

        reset(component, region, style, state);
    }

    /**
     * The method used to get a context.
     *
     * @param  type      the class of the context.
     * @param  component the component.
     * @param  region    the region.
     * @param  style     the style.
     * @param  state     the state.
     *
     * @return the newly constructed context, corresponding to the arguments.
     */
    public static SeaGlassContext getContext(Class type, JComponent component, Region region, SynthStyle style, int state) {
        SeaGlassContext context = null;

        synchronized (contextMap) {
            List instances = (List) contextMap.get(type);

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

    /**
     * Release a context for re-use.
     *
     * @param context the context to release.
     */
    static void releaseContext(SeaGlassContext context) {
        synchronized (contextMap) {
            List instances = (List) contextMap.get(context.getClass());

            if (instances == null) {
                instances = new ArrayList(5);
                contextMap.put(context.getClass(), instances);
            }

            instances.add(context);
        }
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
     *
     * @return {@code true} if the context represents a subregion, {@code false}
     *         otherwise.
     */
    @SuppressWarnings("all")
    public boolean isSubregion() {
        return getRegion().isSubregion();
    }

    /**
     * Sets the current style for the context.
     *
     * @param style the new style.
     */
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

    /**
     * Sets the current state for a component/region.
     *
     * @param state the new state.
     */
    @SuppressWarnings("all")
    public void setComponentState(int state) {
        this.state = state;
    }

    /**
     * Returns the state of the widget, which is a bitmask of the values defined
     * in <code>SynthConstants</code>. A region will at least be in one of
     * <code>ENABLED</code>, <code>MOUSE_OVER</code>, <code>PRESSED</code> or
     * <code>DISABLED</code>.
     *
     * @see    SynthConstants
     *
     * @return State of Component
     */
    public int getComponentState() {
        return state;
    }

    /**
     * Resets the state of the Context.
     *
     * @param component the new component.
     * @param region    the new region.
     * @param style     the new style.
     * @param state     the new state.
     */
    @SuppressWarnings("all")
    public void reset(JComponent component, Region region, SynthStyle style, int state) {
        this.component = component;
        this.region    = region;
        this.style     = style;
        this.state     = state;
    }

    /**
     * Release a context for re-use after nulling the component and style.
     */
    @SuppressWarnings("all")
    public void dispose() {
        this.component = null;
        this.style     = null;
        releaseContext(this);
    }

    /**
     * Convenience method to get the Painter from the current SynthStyle. This
     * will NEVER return null.
     *
     * @return the painter for the style and context, otherwise the empty
     *         painter.
     */
    @SuppressWarnings("all")
    public SynthPainter getPainter() {
        SynthPainter painter = getStyle().getPainter(this);

        if (painter != null) {
            return painter;
        }

        return EMPTY_PAINTER;
    }
}
