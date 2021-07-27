package com.tsm.tsmbottomsheetdialog.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Create by tsm
 * on 2021/6/24
 * 权限包共享,对外调用由 StatusBarUtils 提供
 * Android 21 及以上沉浸式状态栏
 */
class LollipStatusBarUtils {

    private static final String MIUI_FORCE_FSG_NAV_BAR = "force_fsg_nav_bar";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";


    /**
     * 适配状态栏
     * 1.低调模式 隐藏不重要的图标
     * View.SYSTEM_UI_FLAG_LOW_PROFILE
     *
     * 2.隐藏底部导航栏,有操作后就会显示出来
     * View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
     *
     * 3.隐藏状态栏，从状态栏位置下拉重新出现
     * View.SYSTEM_UI_FLAG_FULLSCREEN
     *
     * 4.将布局内容拓展到导航栏的后面
     * View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
     *
     * 5.在不隐藏StatusBar的情况下，将view所在window的显示范围扩展到StatusBar下面。
     * 本次方案就使用了这个属性
     * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     *
     * 6.稳定布局，需要配合SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
     * 和SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN使用，
     * 同时设置布局的android:fitsSystemWindows属性。
     *  本次就是使用的这个方案,在StatusBarUtils中声明了SYSTEM_UI_FLAG_LAYOUT_STABLE,
     *  然后沉浸式的时候添加 SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * View.SYSTEM_UI_FLAG_LAYOUT_STABLE
     *
     * 7.配合SYSTEM_UI_FLAG_HIDE_NAVIGATION和SYSTEM_UI_FLAG_FULLSCREEN使用，
     * 使状态栏和导航栏真正的进入沉浸模式。
     * 点击屏幕任意区域，不会退出全屏模式，
     * 只有用户上下拉状态栏或者导航栏时才会退出。
     * View.SYSTEM_UI_FLAG_IMMERSIVE
     *
     * 8.效果同上，
     * 当用户上下拉状态栏或者导航栏时，
     * 这些系统栏会以半透明的状态显示，
     * 并且在一段时间后消失。
     * View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
     *
     * 9设置状态栏的颜色，6.0版本以后有效。 本方案中就使用了这个属性
     * View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  static int initBarAboveLOLLIPOP(Activity activity, int uiFlags) {
        try {
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;//将布局内容拓展到状态栏的后面
            Window mWindow = activity.getWindow();
            int defaultNavigationBarColor = mWindow.getNavigationBarColor();
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //判断是否存在导航栏 如果导航栏存在移出导航栏透明效果,
            if (getNavigationBarHeight(activity)>0) {
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            //需要设置这个才能设置状态栏和导航栏颜色
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            mWindow.setStatusBarColor(Color.TRANSPARENT);
            //回复默认导航栏颜色
            mWindow.setNavigationBarColor(defaultNavigationBarColor);
        }catch (Exception e){

        }
        return uiFlags;
    }



    /**
     * 适配刘海屏
     *
     * 默认情况，全屏页面不可用刘海区域，非全屏页面可以进行使用
     * public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0;
     * 允许页面延伸到刘海区域
     * public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1;
     * 不允许使用刘海区域
     * public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2;
     *
     * @param window
     */
    public static void fitsNotchScreen(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
    }

    /**
     * 是否有底部状态栏
     * @param context
     * @return
     */
    @TargetApi(14)
    private static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if ((context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
                    key = NAV_BAR_HEIGHT_RES_NAME;
                } else {
                    key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }


    /**
     * 适配底部状态栏是否是黑色的
     * @param uiFlags
     * @param dark
     * @return
     */
    public static int setNavigationIconDark(int uiFlags,boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && dark) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;//设置黑色的底部导航栏
        } else {
            return uiFlags;
        }
    }


    /**
     * 是否有底部状态栏
     * @param activity
     * @return
     */
    @TargetApi(14)
    private static boolean hasNavBar(Activity activity) {
        //判断小米手机是否开启了全面屏,开启了，直接返回false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(activity.getContentResolver(), MIUI_FORCE_FSG_NAV_BAR, 0) != 0) {
                return false;
            }
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }



    /**
     *
     * @param context
     * @param key
     * @return
     */
    private static int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                int sizeOne = context.getResources().getDimensionPixelSize(resourceId);
                int sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId);

                if (sizeTwo >= sizeOne) {
                    return sizeTwo;
                } else {
                    float densityOne = context.getResources().getDisplayMetrics().density;
                    float densityTwo = Resources.getSystem().getDisplayMetrics().density;
                    return Math.round(sizeOne * densityTwo / densityOne);
                }
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }




    /**
     * 修改状态栏字体
     * @param window
     * @param isDarkMode
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarLightMode(@NonNull final Window window,
                                             final boolean isDarkMode) {
        switch (RomUtils.getLightStatusBarAvailableRomType()) {
            case RomUtils.AvailableRomType.MIUI:
                XiaoMiStatusBarTextFontUtils.MIUISetStatusBarLightMode(window, isDarkMode);
                break;
            case RomUtils.AvailableRomType.FLYME:
                FlymeStatusbarTextFontUtils.setStatusBarDarkIcon(window,isDarkMode);
                break;
            case RomUtils.AvailableRomType.ANDROID_NATIVE:
                AndroidStatusbarTextFontUtils.setAndroidNativeLightStatusBar(window, isDarkMode);
                break;
        }
    }



    /**
     * 修改状态栏字体
     * @param window
     * @param isDarkMode
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setOtherStatusBarLightMode(@NonNull final Window window,
                                             final boolean isDarkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (RomUtils.getLightStatusBarAvailableRomType()) {
                case RomUtils.AvailableRomType.MIUI:
                    XiaoMiStatusBarTextFontUtils.MIUISetStatusBarLightMode(window, isDarkMode);
                    break;
                case RomUtils.AvailableRomType.FLYME:
                    FlymeStatusbarTextFontUtils.setStatusBarDarkIcon(window,isDarkMode);
                    break;
            }
        }
    }



}
