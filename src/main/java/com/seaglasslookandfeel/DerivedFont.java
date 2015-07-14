package com.seaglasslookandfeel;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class DerivedFont implements UIDefaults.ActiveValue {


    private float sizeOffset;
    private Boolean bold;
    private Boolean italic;
    private String parentKey;

    /**
     * Create a new DerivedFont.
     *
     * @param key The UIDefault key associated with this derived font's
     *            parent or source. If this key leads to a null value, or a
     *            value that is not a font, then null will be returned as
     *            the derived font. The key must not be null.
     * @param sizeOffset The size offset, as a percentage, to use. For
     *                   example, if the source font was a 12pt font and the
     *                   sizeOffset were specified as .9, then the new font
     *                   will be 90% of what the source font was, or, 10.8
     *                   pts which is rounded to 11pts. This fractional
     *                   based offset allows for proper font scaling in high
     *                   DPI or large system font scenarios.
     * @param bold Whether the new font should be bold. If null, then this
     *             new font will inherit the bold setting of the source
     *             font.
     * @param italic Whether the new font should be italicized. If null,
     *               then this new font will inherit the italic setting of
     *               the source font.
     */
    public DerivedFont(String key, float sizeOffset, Boolean bold,
                       Boolean italic) {
        //validate the constructor arguments
        if (key == null) {
            throw new IllegalArgumentException("You must specify a key");
        }

        //set the values
        this.parentKey = key;
        this.sizeOffset = sizeOffset;
        this.bold = bold;
        this.italic = italic;
    }
    /**
     * @inheritDoc
     */
    @Override
    public Object createValue(UIDefaults defaults) {
        Font f = UIManager.getFont(parentKey);
        if (f != null) {
            // always round size for now so we have exact int font size
            // (or we may have lame looking fonts)
            float size = Math.round(f.getSize2D() * sizeOffset);
            int style = f.getStyle();
            if (bold != null) {
                if (bold.booleanValue()) {
                    style = style | Font.BOLD;
                } else {
                    style = style & ~Font.BOLD;
                }
            }
            if (italic != null) {
                if (italic.booleanValue()) {
                    style = style | Font.ITALIC;
                } else {
                    style = style & ~Font.ITALIC;
                }
            }
            return f.deriveFont(style, size);
        } else {
            return null;
        }
    }
    
}
