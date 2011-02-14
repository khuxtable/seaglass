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
package com.seaglasslookandfeel.state;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthConstants;

/**
 * <p>Represents a built in, or custom, state in Sea Glass.</p>
 *
 * <p>Synth provides several built in states, which are:</p>
 *
 * <ul>
 *   <li>Enabled</li>
 *   <li>Mouse Over</li>
 *   <li>Pressed</li>
 *   <li>Disabled</li>
 *   <li>Focused</li>
 *   <li>Selected</li>
 *   <li>Default</li>
 * </ul>
 *
 * <p>However, there are many more states that could be described in a
 * LookAndFeel, and it would be nice to style components differently based on
 * these different states. For example, a progress bar could be "indeterminate".
 * It would be very convenient to allow this to be defined as a "state".</p>
 *
 * <p>This class, State, is intended to be used for such situations. Simply
 * implement the abstract #isInState method. It returns true if the given
 * JComponent is "in this state", false otherwise. This method will be called
 * <em>many</em> times in <em>performance sensitive loops</em>. It must execute
 * very quickly.</p>
 *
 * <p>For example, the following might be an implementation of a custom
 * "Indeterminate" state for JProgressBars:</p>
 *
 * <pre>
 * &lt;code&gt;
 *     public final class IndeterminateState extends State&lt;JProgressBar&gt; {
 *         public IndeterminateState() {
 *             super(&quot;Indeterminate&quot;);
 *         }
 *
 *         &#064;Override
 *         protected boolean isInState(JProgressBar c) {
 *             return c.isIndeterminate();
 *         }
 *     }
 * &lt;/code&gt;
 * </pre>
 *
 * <p>Based on Nimbus's State class, which has too much package local stuff.</p>
 *
 */
public abstract class State<T extends JComponent> {
    static final Map<String, StandardState> standardStates = new HashMap<String, StandardState>(7);

    /** Enabled state. */
    public static final State               Enabled   = new StandardState(SynthConstants.ENABLED);

    /** MouseOver state. */
    public static final State               MouseOver = new StandardState(SynthConstants.MOUSE_OVER);

    /** Pressed state. */
    public static final State               Pressed  = new StandardState(SynthConstants.PRESSED);

    /** Disabled state. */
    public static final State               Disabled = new StandardState(SynthConstants.DISABLED);

    /** Focused state. */
    public static final State               Focused  = new StandardState(SynthConstants.FOCUSED);

    /** Selected state. */
    public static final State               Selected = new StandardState(SynthConstants.SELECTED);

    /** Default state. */
    public static final State               Default = new StandardState(SynthConstants.DEFAULT);

    private String name;

    /**
     * <p>Create a new custom State. Specify the name for the state. The name
     * should be unique within the states set for any one particular component.
     * The name of the state should coincide with the name used in UIDefaults.
     * </p>
     *
     * <p>For example, the following would be correct:</p>
     *
     * <pre>
     * &lt;code&gt;
     *     defaults.put(&quot;Button.States&quot;, &quot;Enabled, Foo, Disabled&quot;);
     *     defaults.put(&quot;Button.Foo&quot;, new FooState(&quot;Foo&quot;));
     * &lt;/code&gt;
     * </pre>
     *
     * @param name a simple user friendly name for the state, such as
     *             "Indeterminate" or "EmbeddedPanel" or "Blurred". It is
     *             customary to use camel case, with the first letter
     *             capitalized.
     */
    protected State(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /**
     * <p>This is the main entry point, called by NimbusStyle.</p>
     *
     * <p>There are both custom states and standard states. Standard states
     * correlate to the states defined in SynthConstants. When a UI delegate
     * constructs a SynthContext, it specifies the state that the component is
     * in according to the states defined in SynthConstants. Our NimbusStyle
     * will then take this state, and query each State instance in the style
     * asking whether isInState(c, s).</p>
     *
     * <p>Now, only the standard states care about the "s" param. So we have
     * this odd arrangement:</p>
     *
     * <ul>
     *   <li>NimbusStyle calls State.isInState(c, s)</li>
     *   <li>State.isInState(c, s) simply delegates to State.isInState(c)</li>
     *   <li><em>EXCEPT</em>, StandardState overrides State.isInState(c, s) and
     *     returns directly from that method after checking its state, and does
     *     not call isInState(c) (since it is not needed for standard
     *     states).</li>
     * </ul>
     *
     * @param  c DOCUMENT ME!
     * @param  s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInState(T c, int s) {
        return isInState(c);
    }

    /**
     * <p>Gets whether the specified JComponent is in the custom state
     * represented by this class. <em>This is an extremely performance sensitive
     * loop.</em> Please take proper precautions to ensure that it executes
     * quickly.</p>
     *
     * <p>Nimbus uses this method to help determine what state a JComponent is
     * in. For example, a custom State could exist for JProgressBar such that it
     * would return <code>true</code> when the progress bar is indeterminate.
     * Such an implementation of this method would simply be:</p>
     *
     * <pre>
     * &lt;code&gt; return c.isIndeterminate();&lt;/code&gt;
     * </pre>
     *
     * @param  c the JComponent to test. This will never be null.
     *
     * @return true if <code>c</code> is in the custom state represented by this
     *         <code>State</code> instance
     */
    public abstract boolean isInState(T c);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isStandardStateName(String name) {
        return standardStates.containsKey(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static StandardState getStandardState(String name) {
        return standardStates.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @author  $author$
     * @version $Revision$, $Date$
     */
    public static final class StandardState extends State<JComponent> {
        private int state;

        /**
         * Creates a new StandardState object.
         *
         * @param state DOCUMENT ME!
         */
        private StandardState(int state) {
            super(toString(state));
            this.state = state;
            standardStates.put(getName(), this);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getState() {
            return state;
        }

        /**
         * @see com.seaglasslookandfeel.state.State#isInState(JComponent, int)
         */
        public boolean isInState(JComponent c, int s) {
            return (s & state) == state;
        }

        /**
         * @see com.seaglasslookandfeel.state.State#isInState(JComponent)
         */
        public boolean isInState(JComponent c) {
            throw new AssertionError("This method should never be called");
        }

        /**
         * DOCUMENT ME!
         *
         * @param  state DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private static String toString(int state) {
            StringBuffer buffer = new StringBuffer();

            if ((state & SynthConstants.DEFAULT) == SynthConstants.DEFAULT) {
                buffer.append("Default");
            }

            if ((state & SynthConstants.DISABLED) == SynthConstants.DISABLED) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("Disabled");
            }

            if ((state & SynthConstants.ENABLED) == SynthConstants.ENABLED) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("Enabled");
            }

            if ((state & SynthConstants.FOCUSED) == SynthConstants.FOCUSED) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("Focused");
            }

            if ((state & SynthConstants.MOUSE_OVER) == SynthConstants.MOUSE_OVER) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("MouseOver");
            }

            if ((state & SynthConstants.PRESSED) == SynthConstants.PRESSED) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("Pressed");
            }

            if ((state & SynthConstants.SELECTED) == SynthConstants.SELECTED) {

                if (buffer.length() > 0)
                    buffer.append("+");

                buffer.append("Selected");
            }

            return buffer.toString();
        }
    }
}
