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
package com.seaglasslookandfeel.painter.button;

import java.awt.Color;
import java.awt.Dimension;

import com.seaglasslookandfeel.painter.ButtonPainter.Which;

/**
 * Paint a "textured" button. This is suitable for placing on darker grey
 * backgrounds such as toolbars and bottom bars.
 * 
 * @author Kathryn Huxtable
 */
public class TexturedButtonPainter extends SegmentedButtonPainter {

    /**
     * Create a textured button painter.
     * 
     * @param state
     *            the button state.
     * @param ctx
     *            the paint context.
     * @param dimension
     *            the button dimensions for scaling.
     */
    public TexturedButtonPainter(Which state, PaintContext ctx, Dimension dimension) {
        super(state, ctx, dimension);

        setEnabled(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0, true),
            0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c)));
        setEnabledPressed(new ButtonStateColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(0xffcccccc,
            true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c)));
        setDefault(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0, true),
            0.5f, new Color(0x999999), new Color(0x555555), new Color(0x4c4c4c)));
        setDefaultPressed(new ButtonStateColors(new Color(0, true), new Color(0, true), new Color(0x00888888, true), new Color(0xffcccccc,
            true), 0.5f, new Color(0x777777), new Color(0x555555), new Color(0x4c4c4c)));
        setDisabled(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(0, true),
            0.5f, new Color(0xbbbbbb), new Color(0x555555), new Color(0x4c4c4c)));
        setDisabledSelected(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0, true), new Color(
            0, true), 0.5f, new Color(0xaaaaaa), new Color(0x555555), new Color(0x4c4c4c)));
    }
}
