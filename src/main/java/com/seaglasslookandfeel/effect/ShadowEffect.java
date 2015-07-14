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
package com.seaglasslookandfeel.effect;

import java.awt.Color;

/**
 * ShadowEffect - base class with all the standard properties for shadow effects
 *
 * <p>Based on Nimbus's ShadowEffect by Jasper Potts. This was package local.
 * </p>
 */
public abstract class ShadowEffect extends Effect {

    /** The shadow effect's base color. */
    protected Color color = Color.BLACK;

    /** Opacity a float 0-1 for percentage */
    protected float opacity = 0.75f;

    /** Angle in degrees between 0-360 */
    protected int angle = 135;

    /** Distance in pixels */
    protected int distance = 5;

    /** The shadow spread between 0-100 % */
    protected int spread = 0;

    /** Size in pixels */
    protected int size = 5;

    // =================================================================================================================
    // Bean methods

    /**
     * Return the base color.
     *
     * @return the base color.
     */
    protected Color getColor() {
        return color;
    }

    /**
     * Set the base color.
     *
     * @param color the new base color.
     */
    protected void setColor(Color color) {
        // Color old = getColor();
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    protected float getOpacity() {
        return opacity;
    }

    /**
     * Set the effect opacity.
     *
     * @param opacity the new opacity, between 0 and 1.
     */
    protected void setOpacity(float opacity) {
        // float old = getOpacity();
        this.opacity = opacity;
    }

    /**
     * Get the effect angle.
     *
     * @return the effect angle.
     */
    protected int getAngle() {
        return angle;
    }

    /**
     * Set the effect angle.
     *
     * @param angle the new angle.
     */
    protected void setAngle(int angle) {
        // int old = getAngle();
        this.angle = angle;
    }

    /**
     * Get the effect distance.
     *
     * @return the effect distance.
     */
    protected int getDistance() {
        return distance;
    }

    /**
     * Set the effect distance.
     *
     * @param distance the new distance.
     */
    protected void setDistance(int distance) {
        // int old = getDistance();
        this.distance = distance;
    }

    /**
     * Get the effect's spread.
     *
     * @return the effect spread.
     */
    protected int getSpread() {
        return spread;
    }

    /**
     * Set the effect's spread.
     *
     * @param spread the new spread.
     */
    protected void setSpread(int spread) {
        // int old = getSpread();
        this.spread = spread;
    }

    /**
     * Get the effect's size.
     *
     * @return the effect size.
     */
    protected int getSize() {
        return size;
    }

    /**
     * Set the effect's size.
     *
     * @param size the new size.
     */
    protected void setSize(int size) {
        // int old = getSize();
        this.size = size;
    }
}
