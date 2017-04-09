package com.ztory.lib.sleek.base.element.css;

import android.graphics.Color;

import com.ztory.lib.sleek.mapd.Mapd;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported CSS properties:
{
    background: #d8d8d8;
    border-radius: 22px;
    color: #666;
    font-size: 10px;
    line-height: 46px;
    text-align: center;
    vertical-align: center;
}
 * Created by jonruna on 2017-04-07.
 */
public class CSSblockBase extends HashMap<String, String> implements CSSblock {

    protected long modifiedTimestamp;

    public CSSblockBase() {
        super();
    }

    public CSSblockBase(int initialCapacity) {
        super(initialCapacity);
    }

    public CSSblockBase(String CSSblockString) {
        super(12);

        // Remove first and last char brackets {}
        String parseString = CSSblockString.substring(1, CSSblockString.length() - 1);

        String[] keyValuePairs = parseString.split(";");
        for (String iterKeyValue : keyValuePairs) {
            iterKeyValue = iterKeyValue.trim();
            try {
                String[] keyValueArrau = iterKeyValue.split(":");
                String key = keyValueArrau[0].trim();
                String value = keyValueArrau[1].trim();
                put(key, value);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Supported syntax:
     * #RRGGBB, #AARRGGBB, or color words such as "red"
     * @return an Integer if background-color is set or null if it is not set
     */
    @Override
    public Integer getBackgroundColor() {

        String backgroundColorString = Mapd.get(this, CSS.Property.BACKGROUND_COLOR, String.class);
        if (backgroundColorString == null) {
            backgroundColorString = Mapd.get(this, CSS.Property.BACKGROUND, String.class);
        }

        if (backgroundColorString != null) {
            try {
                return Color.parseColor(backgroundColorString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Supported syntax:
     * #RRGGBB, #AARRGGBB, or color words such as "red"
     * @return an Integer if background-color is set or null if it is not set
     */
    @Override
    public Integer getColor() {
        String colorString = Mapd.get(this, CSS.Property.COLOR, String.class);
        if (colorString != null) {
            try {
                return Color.parseColor(colorString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Supported syntax:
     * 10px
     * @return an Integer if border-radius is set or null if it is not set
     */
    @Override
    public Integer getBorderRadius() {
        String borderRadiusString = Mapd.get(this, CSS.Property.BORDER_RADIUS, String.class);
        if (borderRadiusString != null) {
            try {
                int indexOfPX = borderRadiusString.indexOf(CSS.Unit.PX);
                if (indexOfPX > -1) {
                    String valueString = borderRadiusString.substring(0, indexOfPX);
                    return Integer.parseInt(valueString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer getFontSize() {
        String fontSizeString = Mapd.get(this, CSS.Property.FONT_SIZE, String.class);
        if (fontSizeString != null) {
            try {
                int indexOfPX = fontSizeString.indexOf(CSS.Unit.PX);
                if (indexOfPX > -1) {
                    String valueString = fontSizeString.substring(0, indexOfPX);
                    return Integer.parseInt(valueString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer getLineHeight() {
        String lineHeightString = Mapd.get(this, CSS.Property.LINE_HEIGHT, String.class);
        if (lineHeightString != null) {
            try {
                int indexOfPX = lineHeightString.indexOf(CSS.Unit.PX);
                if (indexOfPX > -1) {
                    String valueString = lineHeightString.substring(0, indexOfPX);
                    return Integer.parseInt(valueString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getTextAlign() {
        return Mapd.get(this, CSS.Property.TEXT_ALIGN, String.class);
    }

    @Override
    public String getVerticalAlign() {
        return Mapd.get(this, CSS.Property.VERTICAL_ALIGN, String.class);
    }

    @Override
    public void clear() {
        super.clear();
        updateModifiedTimestamp();
    }

    @Override
    public String put(String key, String value) {
        String returnString = super.put(key, value);
        updateModifiedTimestamp();
        return returnString;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        super.putAll(m);
        updateModifiedTimestamp();
    }

    @Override
    public String remove(Object key) {
        String returnString = super.remove(key);
        updateModifiedTimestamp();
        return returnString;
    }

    @Override
    public long getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void updateModifiedTimestamp() {
        modifiedTimestamp = System.currentTimeMillis();
    }

}
