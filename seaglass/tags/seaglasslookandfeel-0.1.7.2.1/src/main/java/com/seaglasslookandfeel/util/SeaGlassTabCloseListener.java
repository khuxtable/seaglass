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
package com.seaglasslookandfeel.util;

import java.awt.Component;

/**
 * A listener for tabbed pane tab close actions.
 *
 * @author Ken Orr
 */
public interface SeaGlassTabCloseListener {

    /**
     * Called when the user clicks on a close button.
     *
     * @param  tabIndex the tab to be closed.
     *
     * @return {@code true} if the tab may be closed, {@code false} if the tab
     *         should not be closed.
     */
    boolean tabAboutToBeClosed(int tabIndex);

    /**
     * Called when a tab is actually closed.
     *
     * @param title     the title of the tab content pane.
     * @param component the tab content pane that is being closed.
     */
    void tabClosed(String title, Component component);

}
