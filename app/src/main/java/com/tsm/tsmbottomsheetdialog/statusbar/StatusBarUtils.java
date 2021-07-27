package com.tsm.tsmbottomsheetdialog.statusbar;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;


/**
 * Create by tsm
 * on 2021/6/24
 * 对外提供沉浸式方法
 */
public class StatusBarUtils {

    /**
     *  沉浸式状态栏
     * @param activity
     * @param isDarkMode
     */
    public static void fitStatusBar(Activity activity,boolean isDarkMode){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = activity.getWindow();
                    //始终显示状态栏
                    int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    //适配刘海屏
                    LollipStatusBarUtils.fitsNotchScreen(window);
                    //修改状态栏颜色,同时将flag增加 将布局内容拓展到状态栏的后面 ,这里也可以修改底部导航栏颜色
                    uiFlags=LollipStatusBarUtils.initBarAboveLOLLIPOP(activity,uiFlags);
                    //设置深色底部导航栏 即黑白图标
                    uiFlags = LollipStatusBarUtils.setNavigationIconDark(uiFlags,isDarkMode);
                    ///修改字体颜色
                    uiFlags=AndroidStatusbarTextFontUtils.setAndroidNativeLightStatusBar(uiFlags,isDarkMode);
                    //修改状态栏可见性
                    window.getDecorView().setSystemUiVisibility(uiFlags);
                    ///修改小米6和魅族部分系统的字体颜色
                    LollipStatusBarUtils.setOtherStatusBarLightMode(activity.getWindow(), isDarkMode);
                }else{//19-20 只能添加控件, 并修改背景颜色-------
                    KitKatStatusBar.initBarKitKat(activity,isDarkMode);
                }
            }
        }catch (Exception e){

        }
    }


    /**
     * 沉浸式态栏,再修改字体颜色,并修改Toolbar高度
     * @param activity
     * @param isDarkMode
     * @param toolbar
     */
    public static void fitStatusBarAttachToolBar(Activity activity,boolean isDarkMode,View toolbar){
        fitStatusBar(activity,isDarkMode);//沉浸式
        attachToolBarToStatusBar(toolbar);//修改顶部View的高度
    }


    /**
     *  修正状态栏高度
     * @param toolbar
     */
    public static void attachToolBarToStatusBar(View toolbar){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){// 达到了19就需要修改状态栏高度了
            if(!HuaWeiStatusBarUtils.isEMUI3_x()){
                int left=toolbar.getPaddingLeft();
                int top=toolbar.getPaddingTop();
                int right=toolbar.getPaddingRight();
                int bottom=toolbar.getPaddingBottom();
                toolbar.setPadding(left,top+getStatusBarHeight(),right,bottom);
            }else{///状态栏会自动隐藏,所以需要监听,先这样吧,

            }
        }
    }

    /**
     * 修改状态栏字体样式
     * @param activity
     * @param isDarkMode
     */
    public static void setStatusBarLightMode(@NonNull final Activity activity,
                                             final boolean isDarkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LollipStatusBarUtils.setStatusBarLightMode(activity.getWindow(), isDarkMode);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            KitKatStatusBar.setStatusBarView(activity,isDarkMode);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
