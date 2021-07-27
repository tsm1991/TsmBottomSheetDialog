package com.tsm.tsmbottomsheetdialog.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.tsm.tsmbottomsheetdialog.R;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;


/**
 * Create by tsm
 * on 2021/7/20
 * 权限包共享,对外调用由 StatusBarUtils 提供
 * android 修改沉浸式状态栏
 */
class KitKatStatusBar {


    /**
     * 初始化android 4.4状态栏
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void initBarKitKat(Activity activity,boolean isDarkMode) {
        //透明状态栏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //创建一个假的状态栏
        setStatusBarView(activity,isDarkMode);
        //底部导航栏也需要设置,这里不管
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    public static void setStatusBarView(Activity mActivity,boolean isDarkMode) {
        View statusBarView = mActivity.getWindow().getDecorView().findViewById(R.id.serviceui_status_bar_view);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    StatusBarUtils.getStatusBarHeight());
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(R.id.serviceui_status_bar_view);
            ((ViewGroup)mActivity.getWindow().getDecorView()).addView(statusBarView);
        }
        ///这里需要调试如何修改颜色, 一般这里使用带有透明度的灰色,因为字体颜色不可以改变,所以这里比较好适配
        if(isDarkMode){
            statusBarView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.serviceui_status_bar_bg));
        }else{
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }


}
