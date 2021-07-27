package com.tsm.tsmbottomsheetdialog.statusbar;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Create by tsm
 * on 2021/6/24
 * 权限包共享,对外调用由 StatusBarUtils 提供
 */
class HuaWeiStatusBarUtils {
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";

    public static boolean isEMUI3_0() {
        String property = getEMUIVersion();
        if (property.contains("EmotionUI_3.0")) {
            return true;
        }
        return false;
    }

    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        if ("EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1")) {
            return true;
        }
        return false;
    }

    public static boolean isEMUI3_x() {
        return HuaWeiStatusBarUtils.isEMUI3_0() || HuaWeiStatusBarUtils.isEMUI3_1();
    }

    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_NAME, "") : "";
    }
    public static boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }
    private static String getSystemProperty(String key, String defaultValue) {
        try {
            @SuppressLint("PrivateApi") Class<?> clz = Class.forName("android.os.SystemProperties");
            Method method = clz.getMethod("get", String.class, String.class);
            return (String) method.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
