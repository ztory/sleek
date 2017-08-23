package com.ztory.lib.sleek.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by jonruna on 2017-04-19.
 */
public class UtilResources {

  public static Bitmap getBitmap(int resourceId) {
    return BitmapFactory.decodeResource(
        UtilPx.getDefaultContext().getResources(),
        resourceId,
        null
    );
  }

  public static String getString(int resourceId) {
    return UtilPx.getDefaultContext().getResources().getString(resourceId);
  }

  public static int getResourceIdDrawable(String resourceName) {
    return UtilPx.getDefaultContext().getResources().getIdentifier(
        resourceName,
        "drawable",
        UtilPx.getDefaultContext().getPackageName()
    );
  }

  private static String getRawResourceString(int rawResourceId) {
    try {
      return UtilDownload.inputStreamToString(
          UtilPx.getDefaultContext().getResources().openRawResource(rawResourceId)
      );
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
