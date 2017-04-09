package com.ztory.lib.sleek.base.element.css;

import java.util.Map;

/**
 * Created by jonruna on 2017-04-09.
 */
public interface CSSblock extends Map<String, String> {
    long getModifiedTimestamp();
    Integer getBackgroundColor();
    Integer getColor();
    Integer getBorderRadius();
    Integer getFontSize();
    Integer getLineHeight();
    String getTextAlign();
    String getVerticalAlign();
}
