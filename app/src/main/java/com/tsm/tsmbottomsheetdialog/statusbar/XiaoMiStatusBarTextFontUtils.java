package com.tsm.tsmbottomsheetdialog.statusbar;

import android.os.Build;
import android.os.Environment;
import android.view.Window;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import androidx.annotation.RequiresApi;

/**
 * Create by tsm
 * on 2021/6/24
 * 权限包共享,对外调用由 StatusBarUtils 提供
 * 小米官方提供的方法
 * @See(https://dev.mi.com/docs/appsmarket/technical_docs/immersion/)
 *
 */
class XiaoMiStatusBarTextFontUtils {

    private static final String MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    /**
     * 小米修改状态栏字体
     *
     * @param dark
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            if (window != null) {
                Class<? extends Window> clazz = window.getClass();
                try {
                    int darkModeFlag;
                    Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField(MIUI_STATUS_BAR_DARK);
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    if (dark) {
                        //状态栏透明且黑色字体
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                    } else {
                        //清除黑色字体
                        extraFlagField.invoke(window, 0, darkModeFlag);
                    }
                } catch (Exception ignored) {

                }
                AndroidStatusbarTextFontUtils.setAndroidNativeLightStatusBar(window, dark);
            }
            result = true;
        }
        return result;
    }


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";


    static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }

    }

}
