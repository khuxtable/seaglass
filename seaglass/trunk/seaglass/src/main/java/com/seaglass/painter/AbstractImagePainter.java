/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * SeaGlass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * SeaGlass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SeaGlass.  If not, see
 *     <http://www.gnu.org/licenses/>.
 * 
 * $Id$
 */
package com.seaglass.painter;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * 
 */
public abstract class AbstractImagePainter extends AbstractRegionPainter {

    private static final String IMAGE_DIR = "/com/seaglass/resources/images";

    private StringBuilder       imagePath = new StringBuilder(IMAGE_DIR);

    private PaintContext        ctx;

    protected ImageIcon         image;

    public AbstractImagePainter(PaintContext ctx, int state) {
        super();
        this.ctx = ctx;
        String imageName = getImageName(state);
        if (imageName == null) {
            throw new RuntimeException("No file was found for the state " + state);
        }
        imagePath.append('/');
        imagePath.append(imageName);
        imagePath.append(".png");

        image = new ImageIcon(AbstractImagePainter.class.getResource(imagePath.toString()));
    }

    protected abstract String getImageName(int state);

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        image.paintIcon(c, g, 0, 0);
    }

    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys = new Object[] {};
        return extendedCacheKeys;
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }
}
