package com.ztory.lib.sleek.util;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jonruna on 2017-09-06.
 */
public class UtilWindow {

  public static void setStatusAndNavbarColor(
      Window window,
      int statusBarColor,
      int navBarColor
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      // clear FLAG_TRANSLUCENT_STATUS flag:
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

      // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

      window.setStatusBarColor(statusBarColor);
      window.setNavigationBarColor(navBarColor);
    }
  }

}
