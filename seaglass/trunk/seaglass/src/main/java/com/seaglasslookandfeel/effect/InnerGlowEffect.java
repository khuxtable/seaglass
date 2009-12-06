/*
 * @(#)InnerGlowEffect.java	1.2 07/12/12
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.seaglasslookandfeel.effect;

import java.awt.Color;

/**
 * InnerGlowEffect
 * 
 * Based on Nimbus's InnerGlowEffect by Jasper Potts. This was package local.
 */
public class InnerGlowEffect extends InnerShadowEffect {
    public InnerGlowEffect() {
        distance = 0;
        color = new Color(255, 255, 211);
    }
}
