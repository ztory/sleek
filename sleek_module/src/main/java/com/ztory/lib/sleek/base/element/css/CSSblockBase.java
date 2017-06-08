package com.ztory.lib.sleek.base.element.css;

import android.graphics.Color;
import android.graphics.Rect;
import com.ztory.lib.sleek.mapd.Mapd;
import com.ztory.lib.sleek.util.UtilPx;
import java.util.HashMap;
import java.util.Map;

/**
 * Supported CSS properties:
 * {
 * background: #d8d8d8;
 * background-color: #d8d8d8;
 * background-image: url("https://example.com/example.png");
 * background-size: cover;
 * border-radius: 22px;
 * border: 1px solid #0000ff;
 * box-shadow: 1px 2px 4px rgba(120, 130, 140, 0.5);
 * padding: 5px 10px 15px 20px;
 * color: #666;
 * font-size: 10px;
 * line-height: 46px;
 * text-align: center;
 * vertical-align: middle;
 * text-shadow: 1px 1px 2px black;
 * }
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
        if (iterKeyValue.contains("url(")) {
          //String[] keyValueArrau = iterKeyValue.split(":");
          String key = iterKeyValue.substring(0, iterKeyValue.indexOf(':'));
          String value =
              iterKeyValue.substring(iterKeyValue.indexOf(':') + 1, iterKeyValue.length()).trim();
          put(key, value);
        } else {
          String[] keyValueArrau = iterKeyValue.split(":");
          String key = keyValueArrau[0].trim();
          String value = keyValueArrau[1].trim();
          put(key, value);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Supported syntax:
   * #RGB #RRGGBB, #AARRGGBB, or color words such as "red", also supports rgba(80,0,0,0.7) syntax.
   *
   * @return an Integer if background-color is set or null if it is not set
   */
  @Override public Integer getBackgroundColor() {

    String backgroundColorString = Mapd.get(this, CSS.Property.BACKGROUND_COLOR, String.class);
    if (backgroundColorString == null) {
      backgroundColorString = Mapd.get(this, CSS.Property.BACKGROUND, String.class);
    }

    if (backgroundColorString != null) {
      try {
        return getColorFromString(backgroundColorString);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public String getBackgroundImage() {
    String backgroundImageString = Mapd.get(this, CSS.Property.BACKGROUND_IMAGE, String.class);
    if (backgroundImageString != null) {
      return backgroundImageString.substring(5, backgroundImageString.length() - 2);
    }
    return null;
  }

  @Override public String getBackgroundSize() {
    return Mapd.get(this, CSS.Property.BACKGROUND_SIZE, String.class);
  }

  /**
   * Supported syntax:
   * #RGB #RRGGBB, #AARRGGBB, or color words such as "red", also supports rgba(80,0,0,0.7) syntax.
   *
   * @return an Integer if background-color is set or null if it is not set
   */
  @Override public Integer getColor() {
    String colorString = Mapd.get(this, CSS.Property.COLOR, String.class);
    if (colorString != null) {
      try {
        return getColorFromString(colorString);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Supported syntax:
   * 10px
   *
   * @return an Integer if border-radius is set or null if it is not set
   */
  @Override public Integer getBorderRadius() {
    String borderRadiusString = Mapd.get(this, CSS.Property.BORDER_RADIUS, String.class);
    if (borderRadiusString != null) {
      try {
        int indexOfPX = borderRadiusString.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = borderRadiusString.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getFontSize() {
    String fontSizeString = Mapd.get(this, CSS.Property.FONT_SIZE, String.class);
    if (fontSizeString != null) {
      try {
        int indexOfPX = fontSizeString.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = fontSizeString.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getLineHeight() {
    String lineHeightString = Mapd.get(this, CSS.Property.LINE_HEIGHT, String.class);
    if (lineHeightString != null) {
      try {
        int indexOfPX = lineHeightString.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = lineHeightString.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public String getTextAlign() {
    return Mapd.get(this, CSS.Property.TEXT_ALIGN, String.class);
  }

  @Override public String getVerticalAlign() {
    return Mapd.get(this, CSS.Property.VERTICAL_ALIGN, String.class);
  }

  @Override public Integer getBoxShadowBlurRadius() {

    //offset-x | offset-y | blur-radius | color
    //box-shadow: 10px 5px 5px #ff0000;

    String boxShadowString = Mapd.get(this, CSS.Property.BOX_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String blurRadiusString = boxShadowParams[2];
        int indexOfPX = blurRadiusString.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = blurRadiusString.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getBoxShadowColor() {

    //offset-x | offset-y | blur-radius | color
    //box-shadow: 10px 5px 5px #ff0000;

    String boxShadowString = Mapd.get(this, CSS.Property.BOX_SHADOW, String.class);
    if (boxShadowString != null) {
      try {

        String colorString;
        if (boxShadowString.indexOf('(') != -1) {
          // Convert "10px 5px 5px rgba(255, 0, 0, 0.7)" -> "255, 0, 0, 0.7"
          colorString = boxShadowString.substring(boxShadowString.indexOf('(') + 1,
              boxShadowString.length() - 1);
        } else {
          String[] boxShadowParams = boxShadowString.split(" ");
          colorString = boxShadowParams[3];
        }

        return getColorFromString(colorString);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getBoxShadowOffsetX() {

    //offset-x | offset-y | blur-radius | color
    //box-shadow: 10px 5px 5px #ff0000;

    String boxShadowString = Mapd.get(this, CSS.Property.BOX_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String offsetXstring = boxShadowParams[0];
        int indexOfPX = offsetXstring.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = offsetXstring.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getBoxShadowOffsetY() {

    //offset-x | offset-y | blur-radius | color
    //box-shadow: 10px 5px 5px #ff0000;

    String boxShadowString = Mapd.get(this, CSS.Property.BOX_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String offsetYstring = boxShadowParams[1];
        int indexOfPX = offsetYstring.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = offsetYstring.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getTextShadowBlurRadius() {

    //offset-x | offset-y | blur-radius | color
    //text-shadow: 1px 1px 2px black;

    String boxShadowString = Mapd.get(this, CSS.Property.TEXT_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String blurRadiusString = boxShadowParams[2];
        int indexOfPX = blurRadiusString.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = blurRadiusString.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getTextShadowColor() {

    //offset-x | offset-y | blur-radius | color
    //text-shadow: 1px 1px 2px black;

    String boxShadowString = Mapd.get(this, CSS.Property.TEXT_SHADOW, String.class);
    if (boxShadowString != null) {
      try {

        String colorString;
        if (boxShadowString.indexOf('(') != -1) {
          // Convert "10px 5px 5px rgba(255, 0, 0, 0.7)" -> "255, 0, 0, 0.7"
          colorString = boxShadowString.substring(boxShadowString.indexOf('(') + 1,
              boxShadowString.length() - 1);
        } else {
          String[] boxShadowParams = boxShadowString.split(" ");
          colorString = boxShadowParams[3];
        }

        return getColorFromString(colorString);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getTextShadowOffsetX() {

    //offset-x | offset-y | blur-radius | color
    //text-shadow: 1px 1px 2px black;

    String boxShadowString = Mapd.get(this, CSS.Property.TEXT_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String offsetXstring = boxShadowParams[0];
        int indexOfPX = offsetXstring.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = offsetXstring.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public Integer getTextShadowOffsetY() {

    //offset-x | offset-y | blur-radius | color
    //text-shadow: 1px 1px 2px black;

    String boxShadowString = Mapd.get(this, CSS.Property.TEXT_SHADOW, String.class);
    if (boxShadowString != null) {
      try {
        String[] boxShadowParams = boxShadowString.split(" ");
        String offsetYstring = boxShadowParams[1];
        int indexOfPX = offsetYstring.indexOf(CSS.Unit.PX);
        if (indexOfPX > -1) {
          String valueString = offsetYstring.substring(0, indexOfPX);
          return UtilPx.getPixels(Integer.parseInt(valueString));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Supported syntax:
   * padding: 5px 10px 15px 20px;
   * padding: 5px 10px 20px;
   * padding: 10px 20px;
   * padding: 10px;
   *
   * @return an Integer if padding is set or null if it is not set
   */
  @Override public Rect getPadding() {
    String boxShadowString = Mapd.get(this, CSS.Property.PADDING, String.class);
    if (boxShadowString != null) {
      try {
        String[] paddingParams = boxShadowString.split(" ");
        String topPaddingString;
        String rightPaddingString;
        String bottomPaddingString;
        String leftPaddingString;

        if (paddingParams.length == 4) {
          topPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          rightPaddingString = paddingParams[1].substring(0, paddingParams[1].indexOf(CSS.Unit.PX));
          bottomPaddingString =
              paddingParams[2].substring(0, paddingParams[2].indexOf(CSS.Unit.PX));
          leftPaddingString = paddingParams[3].substring(0, paddingParams[3].indexOf(CSS.Unit.PX));
        } else if (paddingParams.length == 3) {
          topPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          rightPaddingString = paddingParams[1].substring(0, paddingParams[1].indexOf(CSS.Unit.PX));
          bottomPaddingString =
              paddingParams[2].substring(0, paddingParams[2].indexOf(CSS.Unit.PX));
          leftPaddingString = paddingParams[1].substring(0, paddingParams[1].indexOf(CSS.Unit.PX));
        } else if (paddingParams.length == 2) {
          topPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          rightPaddingString = paddingParams[1].substring(0, paddingParams[1].indexOf(CSS.Unit.PX));
          bottomPaddingString =
              paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          leftPaddingString = paddingParams[1].substring(0, paddingParams[1].indexOf(CSS.Unit.PX));
        } else if (paddingParams.length == 1) {
          topPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          rightPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          bottomPaddingString =
              paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
          leftPaddingString = paddingParams[0].substring(0, paddingParams[0].indexOf(CSS.Unit.PX));
        } else {
          topPaddingString = "0";
          rightPaddingString = "0";
          bottomPaddingString = "0";
          leftPaddingString = "0";
        }

        return new Rect(UtilPx.getPixels(Integer.parseInt(leftPaddingString)),//left,
            UtilPx.getPixels(Integer.parseInt(topPaddingString)),//top,
            UtilPx.getPixels(Integer.parseInt(rightPaddingString)),//right,
            UtilPx.getPixels(Integer.parseInt(bottomPaddingString))//bottom
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Supported syntax:
   * border: 1px solid #0000ff;
   * @return a Rect with the border-width value, or null if no border-width is set.
   */
  @Override public Rect getBorderWidth() {
    String borderString = Mapd.get(this, CSS.Property.BORDER, String.class);
    if (borderString != null) {
      try {
        String[] paddingParams = borderString.split(" ");
        if (paddingParams.length != 3) {
          return null;
        }

        // Strip "px" suffix
        String borderSizeString = paddingParams[0].substring(0, paddingParams[0].length() - 2);
        int borderSize = UtilPx.getPixels(Integer.parseInt(borderSizeString));

        return new Rect(
            borderSize,//left,
            borderSize,//top,
            borderSize,//right,
            borderSize//bottom
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Supported syntax:
   * border: 1px solid #0000ff;
   * @return a color or null if no border-color is set.
   */
  @Override public Integer getBorderColor() {
    String borderString = Mapd.get(this, CSS.Property.BORDER, String.class);
    if (borderString != null) {
      try {
        String[] paddingParams = borderString.split(" ");
        if (paddingParams.length != 3) {
          return null;
        }

        return getColorFromString(paddingParams[2]);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override public void clear() {
    super.clear();
    updateModifiedTimestamp();
  }

  @Override public String put(String key, String value) {
    String returnString = super.put(key, value);
    updateModifiedTimestamp();
    return returnString;
  }

  @Override public void putAll(Map<? extends String, ? extends String> m) {
    super.putAll(m);
    updateModifiedTimestamp();
  }

  @Override public String remove(Object key) {
    String returnString = super.remove(key);
    updateModifiedTimestamp();
    return returnString;
  }

  @Override public long getModifiedTimestamp() {
    return modifiedTimestamp;
  }

  public void updateModifiedTimestamp() {
    modifiedTimestamp = System.currentTimeMillis();
  }

  public static final Integer getColorFromString(String colorString) {
    try {
      if (colorString.startsWith("#")) {
        if (colorString.length() == 7) {//#RRGGBB
          return Color.parseColor(colorString);
        } else if (colorString.length() == 4) {//#RGB -> #RRGGBB
          String calculatedColorString = "#"
              + colorString.charAt(1)
              + colorString.charAt(1)
              + colorString.charAt(2)
              + colorString.charAt(2)
              + colorString.charAt(3)
              + colorString.charAt(3);
          return Color.parseColor(calculatedColorString);
        } else if (colorString.length() == 9) {//#RRGGBBAA -> #AARRGGBB
          String calculatedColorString =
              "#" + colorString.substring(7, 9) + colorString.substring(1, 7);
          return Color.parseColor(calculatedColorString);
        } else {
          return null;
        }
      }

      int indexOfComma = colorString.indexOf(',');

      if (indexOfComma > -1) {

        String[] colorValues;
        int indexOfStartParenthesis = colorString.indexOf('(');
        if (indexOfStartParenthesis > -1) {// rgba(255, 0, 0, 0.7)
          // Convert "rgba(255, 0, 0, 0.7)" -> "255, 0, 0, 0.7"
          colorValues = colorString.substring(indexOfStartParenthesis + 1, colorString.length() - 1)
              .split(",");
        } else {//255, 0, 0, 0.7
          colorValues = colorString.split(",");
        }

        int red = Integer.parseInt(colorValues[0].trim());
        int green = Integer.parseInt(colorValues[1].trim());
        int blue = Integer.parseInt(colorValues[2].trim());
        float alpha = Float.parseFloat(colorValues[3].trim());
        return Color.argb((int) (255 * alpha), red, green, blue);
      } else {// parse values such as "red" or "blue"
        return Color.parseColor(colorString);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
