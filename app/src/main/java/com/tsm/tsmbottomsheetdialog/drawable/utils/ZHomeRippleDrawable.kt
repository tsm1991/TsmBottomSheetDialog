package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.ColorInt

object ZHomeRippleDrawable  {


    /**
     *
     * 创建按下水波纹状态的按钮  需要android 5.0以上 ,不过现在大部分兼容的sdk等级都是5.0以上了
     *
     * @param rippleColor  水波纹颜色,
     * @param bgDrawable  未按下时的背景drawable
     * @param maskDrawable  水波纹drawable
     *
     *
     * 示例xml 文件
     *
     *
    <ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#0CC">
    <!--显示默认的 drawable-->
    <item android:drawable="@drawable/button_default" />

    <!--匹配默认值的波纹效果剪切遮罩-->
    <item
    android:id="@android:id/mask"
    android:drawable="@drawable/button_default" />
    </ripple>
     *
     *
     */
    @JvmStatic
    fun getRippleDrawable(
        @ColorInt rippleColor: Int = Color.LTGRAY,
        bgDrawable: Drawable?,
        maskDrawable: Drawable?
    ): RippleDrawable {
        var states = arrayOf(
            intArrayOf(android.R.attr.state_pressed)
        )
        var colors = intArrayOf(
            rippleColor
        )
        var listDrawable = ColorStateList(states, colors)

        return RippleDrawable(listDrawable, bgDrawable, maskDrawable)
    }



}