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
package com.seaglass;

import javax.swing.JComponent;

/**
 * <p>Represents a built in, or custom, state in Nimbus.</p>
 * 
 * <p>Synth provides several built in states, which are:
 * <ul>
 *  <li>Enabled</li>
 *  <li>Mouse Over</li>
 *  <li>Pressed</li>
 *  <li>Disabled</li>
 *  <li>Focused</li>
 *  <li>Selected</li>
 *  <li>Default</li>
 * </ul>
 * 
 * <p>However, there are many more states that could be described in a LookAndFeel, and it
 * would be nice to style components differently based on these different states.
 * For example, a progress bar could be "indeterminate". It would be very convenient
 * to allow this to be defined as a "state".</p>
 * 
 * <p>This class, State, is intended to be used for such situations.
 * Simply implement the abstract #isInState method. It returns true if the given
 * JComponent is "in this state", false otherwise. This method will be called
 * <em>many</em> times in <em>performance sensitive loops</em>. It must execute
 * very quickly.</p>
 * 
 * <p>For example, the following might be an implementation of a custom
 * "Indeterminate" state for JProgressBars:</p>
 * 
 * <pre><code>
 *     public final class IndeterminateState extends State&lt;JProgressBar&gt; {
 *         public IndeterminateState() {
 *             super("Indeterminate");
 *         }
 *            
 *         @Override
 *         protected boolean isInState(JProgressBar c) {
 *             return c.isIndeterminate();
 *         }
 *     }
 * </code></pre>
 */
public abstract class State<T extends JComponent> extends com.sun.java.swing.plaf.nimbus.State {
    
    private String name;

    /**
     * <p>Create a new custom State. Specify the name for the state. The name should
     * be unique within the states set for any one particular component.
     * The name of the state should coincide with the name used in UIDefaults.</p>
     * 
     * <p>For example, the following would be correct:</p>
     * <pre><code>
     *     defaults.put("Button.States", "Enabled, Foo, Disabled");
     *     defaults.put("Button.Foo", new FooState("Foo"));
     * </code></pre>
     * 
     * @param name a simple user friendly name for the state, such as "Indeterminate"
     *        or "EmbeddedPanel" or "Blurred". It is customary to use camel case,
     *        with the first letter capitalized.
     */
    protected State(String name) {
        super(name);
        this.name = name;
    }
    
    public String toString() { return name; }
}
