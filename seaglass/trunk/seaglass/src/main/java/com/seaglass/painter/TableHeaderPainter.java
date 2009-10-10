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
package com.seaglass.painter;

/**
 */
public final class TableHeaderPainter extends AbstractImagePainter {
    public static final int ASCENDINGSORTICON_ENABLED  = 1;
    public static final int DESCENDINGSORTICON_ENABLED = 2;

    public TableHeaderPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    protected String getImageName(int state) {
        switch (state) {
        case ASCENDINGSORTICON_ENABLED:
            return "table_header_sort_ascending_enabled";
        case DESCENDINGSORTICON_ENABLED:
            return "table_header_sort_descending_enabled";
        }
        return null;
    }
}
