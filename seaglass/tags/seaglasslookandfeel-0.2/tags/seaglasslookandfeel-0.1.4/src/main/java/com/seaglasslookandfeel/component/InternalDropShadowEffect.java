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
 * $Id: TextComponentPainter.java 666 2010-01-05 18:04:22Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.component;

import java.awt.Color;

import com.seaglasslookandfeel.effect.DropShadowEffect;

public class InternalDropShadowEffect extends DropShadowEffect {

    public InternalDropShadowEffect() {
        color = new Color(230, 230, 230);
        angle = 90;
        distance = 0;
        size = 3;
        opacity = 0.35f;
    }
}
