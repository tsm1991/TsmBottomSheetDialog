package com.tsm.tsmbottomsheetdialog.statusbar;

import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;

/**
 * Create by tsm
 * on 2021/6/24
 * 权限包共享,对外调用由 StatusBarUtils 提供
 * Android 原生修改系统状态栏颜色
 */
class AndroidStatusbarTextFontUtils {

    /**
     * 修改字体颜色的Flag
     * @param uiFlags
     * @param dark
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  static int setAndroidNativeLightStatusBar(int uiFlags, boolean dark) {
        if (dark) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 只修改状态栏字体
     * @param window
     * @param dark
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  static void setAndroidNativeLightStatusBar(Window window, boolean dark) {
        View decor = window.getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
