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
package com.seaglasslookandfeel.util;

import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

import sun.swing.MenuItemLayoutHelper;
import sun.swing.StringUIClientPropertyKey;
import sun.swing.MenuItemLayoutHelper.ColumnAlignment;
import sun.swing.MenuItemLayoutHelper.LayoutResult;
import sun.swing.plaf.synth.SynthIcon;

/**
 * @author Kathryn Huxtable
 *
 */
public class SeaGlassMenuItemLayoutHelper extends MenuItemLayoutHelper {

    public static final StringUIClientPropertyKey MAX_ACC_OR_ARROW_WIDTH =
            new StringUIClientPropertyKey("maxAccOrArrowWidth");

    public static final ColumnAlignment LTR_ALIGNMENT_1 =
            new ColumnAlignment(
                    SwingConstants.LEFT,
                    SwingConstants.LEFT,
                    SwingConstants.LEFT,
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT
            );
    public static final ColumnAlignment LTR_ALIGNMENT_2 =
            new ColumnAlignment(
                    SwingConstants.LEFT,
                    SwingConstants.LEFT,
                    SwingConstants.LEFT,
                    SwingConstants.LEFT,
                    SwingConstants.RIGHT
            );
    public static final ColumnAlignment RTL_ALIGNMENT_1 =
            new ColumnAlignment(
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT,
                    SwingConstants.LEFT,
                    SwingConstants.LEFT
            );
    public static final ColumnAlignment RTL_ALIGNMENT_2 =
            new ColumnAlignment(
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT,
                    SwingConstants.RIGHT,
                    SwingConstants.LEFT
            );

    private SynthContext context;
    private SynthContext accContext;
    private SynthStyle style;
    private SynthStyle accStyle;
    private SynthGraphicsUtils gu;
    private SynthGraphicsUtils accGu;
    private boolean alignAcceleratorText;
    private int maxAccOrArrowWidth;

    public SeaGlassMenuItemLayoutHelper(SynthContext context, SynthContext accContext,
                                     JMenuItem mi, Icon checkIcon, Icon arrowIcon,
                                     Rectangle viewRect, int gap, String accDelimiter,
                                     boolean isLeftToRight, boolean useCheckAndArrow,
                                     String propertyPrefix) {
        this.context = context;
        this.accContext = accContext;
        this.style = context.getStyle();
        this.accStyle = accContext.getStyle();
        this.gu = style.getGraphicsUtils(context);
        this.accGu = accStyle.getGraphicsUtils(accContext);
        this.alignAcceleratorText = getAlignAcceleratorText(propertyPrefix);
        reset(mi, checkIcon, arrowIcon, viewRect, gap, accDelimiter,
              isLeftToRight, style.getFont(context), accStyle.getFont(accContext),
              useCheckAndArrow, propertyPrefix);
        setLeadingGap(0);
    }

    private boolean getAlignAcceleratorText(String propertyPrefix) {
        return style.getBoolean(context,
                propertyPrefix + ".alignAcceleratorText", true);
    }

    protected void calcWidthsAndHeights() {
        // iconRect
        if (getIcon() != null) {
            getIconSize().setWidth(SynthIcon.getIconWidth(getIcon(), context));
            getIconSize().setHeight(SynthIcon.getIconHeight(getIcon(), context));
        }

        // accRect
        if (!getAccText().equals("")) {
             getAccSize().setWidth(accGu.computeStringWidth(getAccContext(),
                    getAccFontMetrics().getFont(), getAccFontMetrics(),
                    getAccText()));
            getAccSize().setHeight(getAccFontMetrics().getHeight());
        }

        // textRect
        if (getText() == null) {
            setText("");
        } else if (!getText().equals("")) {
            if (getHtmlView() != null) {
                // Text is HTML
                getTextSize().setWidth(
                        (int) getHtmlView().getPreferredSpan(View.X_AXIS));
                getTextSize().setHeight(
                        (int) getHtmlView().getPreferredSpan(View.Y_AXIS));
            } else {
                // Text isn't HTML
                getTextSize().setWidth(gu.computeStringWidth(context,
                        getFontMetrics().getFont(), getFontMetrics(),
                        getText()));
                getTextSize().setHeight(getFontMetrics().getHeight());
            }
        }

        if (useCheckAndArrow()) {
            // checkIcon
            if (getCheckIcon() != null) {
                getCheckSize().setWidth(
                        SynthIcon.getIconWidth(getCheckIcon(), context));
                getCheckSize().setHeight(
                        SynthIcon.getIconHeight(getCheckIcon(), context));
            }
            // arrowRect
            if (getArrowIcon() != null) {
                getArrowSize().setWidth(
                        SynthIcon.getIconWidth(getArrowIcon(), context));
                getArrowSize().setHeight(
                        SynthIcon.getIconHeight(getArrowIcon(), context));
            }
        }

        // labelRect
        if (isColumnLayout()) {
            getLabelSize().setWidth(getIconSize().getWidth() 
                    + getTextSize().getWidth() + getGap());
            getLabelSize().setHeight(MenuItemLayoutHelper.max(
                    getCheckSize().getHeight(),
                    getIconSize().getHeight(),
                    getTextSize().getHeight(),
                    getAccSize().getHeight(),
                    getArrowSize().getHeight()));
        } else {
            Rectangle textRect = new Rectangle();
            Rectangle iconRect = new Rectangle();
            gu.layoutText(context, getFontMetrics(), getText(), getIcon(),
                    getHorizontalAlignment(), getVerticalAlignment(),
                    getHorizontalTextPosition(), getVerticalTextPosition(),
                    getViewRect(), iconRect, textRect, getGap());
            textRect.width += getLeftTextExtraWidth();
            Rectangle labelRect = iconRect.union(textRect);
            getLabelSize().setHeight(labelRect.height);
            getLabelSize().setWidth(labelRect.width);
        }
    }

    protected void calcMaxWidths() {
        calcMaxWidth(getCheckSize(), MAX_CHECK_WIDTH);
        maxAccOrArrowWidth =
                calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getArrowSize().getWidth());
        maxAccOrArrowWidth =
                calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getAccSize().getWidth());

        if (isColumnLayout()) {
            calcMaxWidth(getIconSize(), MAX_ICON_WIDTH);
            calcMaxWidth(getTextSize(), MAX_TEXT_WIDTH);
            int curGap = getGap();
            if ((getIconSize().getMaxWidth() == 0)
                    || (getTextSize().getMaxWidth() == 0)) {
                curGap = 0;
            }
            getLabelSize().setMaxWidth(
                    calcMaxValue(MAX_LABEL_WIDTH, getIconSize().getMaxWidth()
                            + getTextSize().getMaxWidth() + curGap));
        } else {
            // We shouldn't use current icon and text widths
            // in maximal widths calculation for complex layout.
            getIconSize().setMaxWidth(getParentIntProperty(
                    MAX_ICON_WIDTH));
            calcMaxWidth(getLabelSize(), MAX_LABEL_WIDTH);
            // If maxLabelWidth is wider
            // than the widest icon + the widest text + gap,
            // we should update the maximal text witdh
            int candidateTextWidth = getLabelSize().getMaxWidth() -
                    getIconSize().getMaxWidth();
            if (getIconSize().getMaxWidth() > 0) {
                candidateTextWidth -= getGap();
            }
            getTextSize().setMaxWidth(calcMaxValue(
                    MAX_TEXT_WIDTH, candidateTextWidth));
        }
    }

    public SynthContext getContext() {
        return context;
    }

    public SynthContext getAccContext() {
        return accContext;
    }

    public SynthStyle getStyle() {
        return style;
    }

    public SynthStyle getAccStyle() {
        return accStyle;
    }

    public SynthGraphicsUtils getGraphicsUtils() {
        return gu;
    }

    public SynthGraphicsUtils getAccGraphicsUtils() {
        return accGu;
    }

    public boolean alignAcceleratorText() {
        return alignAcceleratorText;
    }

    public int getMaxAccOrArrowWidth() {
        return maxAccOrArrowWidth;
    }

    protected void prepareForLayout(LayoutResult lr) {
        lr.getCheckRect().width = getCheckSize().getMaxWidth();
        // An item can have an arrow or a check icon at once
        if (useCheckAndArrow() && (!"".equals(getAccText()))) {
            lr.getAccRect().width = maxAccOrArrowWidth;
        } else {
            lr.getArrowRect().width = maxAccOrArrowWidth;
        }
    }

    public ColumnAlignment getLTRColumnAlignment() {
        if (alignAcceleratorText()) {
            return LTR_ALIGNMENT_2;
        } else {
            return LTR_ALIGNMENT_1;
        }
    }

    public ColumnAlignment getRTLColumnAlignment() {
        if (alignAcceleratorText()) {
            return RTL_ALIGNMENT_2;
        } else {
            return RTL_ALIGNMENT_1;
        }
    }

    protected void layoutIconAndTextInLabelRect(LayoutResult lr) {
        lr.setTextRect(new Rectangle());
        lr.setIconRect(new Rectangle());
        gu.layoutText(context, getFontMetrics(), getText(), getIcon(),
                getHorizontalAlignment(), getVerticalAlignment(),
                getHorizontalTextPosition(), getVerticalTextPosition(),
                lr.getLabelRect(), lr.getIconRect(), lr.getTextRect(), getGap());
    }

}
