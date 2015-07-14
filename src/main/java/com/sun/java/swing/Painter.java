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
 * $Id: org.eclipse.jdt.ui.prefs 172 2009-10-06 18:31:12Z kathryn@kathrynhuxtable.org $
 */
package com.sun.java.swing;

import java.awt.Graphics2D;

/**
 * This is here for Java 6 compability.
 * @author Bernd Rosstauscher
 *
 */
public interface Painter<T> {
    
    /**
     * @param g
     * @param object
     * @param width
     * @param height
     */
    public void paint(Graphics2D g, T object, int width, int height);

}
