package com.ztory.lib.sleek.base.element.css;

import android.graphics.Rect;

import java.util.Map;

/**
 * Created by jonruna on 2017-04-09.
 */
public interface CSSblock extends Map<String, String> {

  long getModifiedTimestamp();

  Integer getBackgroundColor();
  String getBackgroundImage();
  String getBackgroundSize();

  Integer getColor();
  Integer getBorderRadius();
  Integer getFontSize();
  Integer getLineHeight();
  String getTextAlign();
  String getVerticalAlign();

  /* offset-x | offset-y | blur-radius | color */
  //box-shadow: 10px 5px 5px black;
  Integer getBoxShadowBlurRadius();
  Integer getBoxShadowColor();
  Integer getBoxShadowOffsetX();
  Integer getBoxShadowOffsetY();

  Rect getPadding();

  Rect getBorderWidth();
  Integer getBorderColor();
}
