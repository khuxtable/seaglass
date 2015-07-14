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

import javax.swing.plaf.synth.Region;

/**
 * Sea Glass extensions to Synth's Region.
 */
public class SeaGlassRegion extends Region {

    /** A Find button in a search field. */
    public static final Region SEARCH_FIELD_FIND_BUTTON = new SeaGlassRegion("SearchFieldFindButton", null, true);

    /** A Cancel button in a search field. */
    public static final Region SEARCH_FIELD_CANCEL_BUTTON = new SeaGlassRegion("SearchFieldCancelButton", null, true);

    /** A Close button for a tab in a tabbed pane. */
    public static final Region TABBED_PANE_TAB_CLOSE_BUTTON = new SeaGlassRegion("TabbedPaneTabClaseButton", null, true);

    /**
     * The cap for a scroll bar where the buttons are ploced together at the
     * other end.
     */
    public static final Region SCROLL_BAR_CAP = new SeaGlassRegion("ScrollBarCap", null, true);

    /**
     * Creates a new SeaGlassRegion object.
     *
     * @param name      the region name.
     * @param ui        the UI delegate, if any.
     * @param subregion {@code true} if this region is a sub-region,
     *                  {@code false} otherwise.
     */
    protected SeaGlassRegion(String name, String ui, boolean subregion) {
        super(name, ui, subregion);
    }
}
