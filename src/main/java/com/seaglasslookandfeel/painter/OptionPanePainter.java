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
 * $Id: MenuPainter.java 1097 2010-02-06 13:31:05Z kathryn@kathrynhuxtable.org $
 */
package com.seaglasslookandfeel.painter;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.icons.ErrorSign;
import com.seaglasslookandfeel.icons.InfoSign;
import com.seaglasslookandfeel.icons.QuestionSign;
import com.seaglasslookandfeel.icons.WarningSign;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class OptionPanePainter extends AbstractRegionPainter {
    public static enum Which {
        ERRORICON_ENABLED, INFORMATIONICON_ENABLED, 
        QUESTIONICON_ENABLED, WARNINGICON_ENABLED
    };

    private Which        state;
    private PaintContext ctx;
   
    /**
     * @param state
     */
    public OptionPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case ERRORICON_ENABLED:
            paintErrorSign(g, c, width, height);
            break;
        case INFORMATIONICON_ENABLED:
            paintInfoSign(g, c, width, height);
            break;
        case QUESTIONICON_ENABLED:
            paintQuestionSign(g, c, width, height);
            break;
        case WARNINGICON_ENABLED:
            paintWarningSign(g, c, width, height);
            break;
        }
    }

    private void paintWarningSign(Graphics2D g, JComponent c, int width, int height) {
        WarningSign icon = new WarningSign();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, -3, -3);
    }

    private void paintQuestionSign(Graphics2D g, JComponent c, int width, int height) {
        QuestionSign icon = new QuestionSign();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, -3, -3);
    }

    private void paintInfoSign(Graphics2D g, JComponent c, int width, int height) {
        InfoSign icon = new InfoSign();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, -3, -3);
    }

    private void paintErrorSign(Graphics2D g, JComponent c, int width, int height) {
        ErrorSign icon = new ErrorSign();
        icon.setDimension(new Dimension(width, height));
        icon.paintIcon(c, g, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }
    
    

  
}
