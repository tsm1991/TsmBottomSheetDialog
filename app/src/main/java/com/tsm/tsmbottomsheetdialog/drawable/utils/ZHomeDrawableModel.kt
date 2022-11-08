package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import com.tsm.tsmbottomsheetdialog.dp2px

/**
 * Create by 田守明
 * on 2021/5/13
 */
class ZHomeDrawableModel constructor(){


    constructor(div_color:Int,div_radius:Float) : this() {
        this.div_color=div_color
        this.div_radius=div_radius
    }

    /**
     * selector 属性   按下  选择  enable
     *
     * @default 0
     */
    var div_selector = 0

    /**
     * 背景色
     *
     * @default 0
     */
    var div_color = 0

    /**
     * state 状态下的背景色
     *
     * @default 0
     */
    var div_state_color = 0

    /**
     * 左上角圆角
     *
     * @default 0f
     */
    var div_top_left_radius = 0f

    /**
     * 右上角圆角
     *
     * @default 0f
     */
    var div_top_right_radius = 0f

    /**
     * 左下角圆角
     *
     * @default 0f
     */
    var div_bottom_left_radius = 0f

    /**
     * 右下角圆角
     *
     * @default 0f
     */
    var div_bottom_right_radius = 0f

    /**
     * 左上角和右上角圆角
     *
     * @default 0f
     */
    var div_top_radius = 0f

    /**
     * 左下角有右下角圆角
     *
     * @default 0f
     */
    var div_bottom_radius = 0f

    /**
     * 圆角
     *
     * @default 0f
     */
    var div_radius = 0f

    /**
     * 虚线颜色
     *
     * @default 0
     */
    var div_stroken_color = 0

    /**
     * state 状态下的虚线颜色
     *
     * @default 0
     */
    var div_state_stroken_color = 0

    /**
     * 虚线宽度
     *
     * @default 0f
     */
    var stroken_width = 0f

    /**
     * 虚线间隔
     *
     * @default 0f
     */
    var stroken_dashgap = 0f

    /**
     * 虚线间隔宽度
     *
     * @default 0f
     */
    var stroken_dashwidth = 0f


    var div_colors: IntArray? = null

    var div_angle:Int=0

    /**
     * 底部横线
     */
    var bottomLine:Boolean=false



    var bottomLinePadding=0f


    /**
     * 是否含有一个属性,具有一个有效的属性才可以生成一个drawable
     * 有效属性的意义就在于 stroken 的颜色和宽度必须是同时存在的
     * 如果只有一个属性则是无效属性
     */
    val isDrawable: Boolean
        get() {
            if (div_color != 0) return true
            if(div_colors!=null) return true
            if (div_radius > 0f) return true
            if (div_top_radius > 0f) return true
            if (div_bottom_radius > 0f) return true
            if (div_top_left_radius > 0f) return true
            if (div_top_right_radius > 0f) return true
            if (div_bottom_left_radius > 0f) return true
            if (div_bottom_right_radius > 0f) return true
            return if (div_stroken_color != 0 && stroken_width > 0f) true else false
        }

    /**
     * 是否只是一个颜色Drawable
     * 如果只是一个颜色的情况下则只需要创建一个ColorDrawable
     * 否则创建 GradientDrawable
     * @return
     */
    val isOnlyColorDrawable: Boolean
        get() {
            if(div_colors!=null) return false
            if (div_radius > 0f) return false
            if (div_top_radius > 0f) return false
            if (div_bottom_radius > 0f) return false
            if (div_top_left_radius > 0f) return false
            if (div_top_right_radius > 0f) return false
            if (div_bottom_left_radius > 0f) return false
            if (div_bottom_right_radius > 0f) return false
            if (div_stroken_color != 0 && stroken_width > 0f) return false
            return if (div_color != 0) true else false
        }






    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZHomeDrawableModel

        if (div_selector != other.div_selector) return false
        if (div_color != other.div_color) return false
        if (div_state_color != other.div_state_color) return false
        if (div_top_left_radius != other.div_top_left_radius) return false
        if (div_top_right_radius != other.div_top_right_radius) return false
        if (div_bottom_left_radius != other.div_bottom_left_radius) return false
        if (div_bottom_right_radius != other.div_bottom_right_radius) return false
        if (div_top_radius != other.div_top_radius) return false
        if (div_bottom_radius != other.div_bottom_radius) return false
        if (div_radius != other.div_radius) return false
        if (div_stroken_color != other.div_stroken_color) return false
        if (div_state_stroken_color != other.div_state_stroken_color) return false
        if (stroken_width != other.stroken_width) return false
        if (stroken_dashgap != other.stroken_dashgap) return false
        if (stroken_dashwidth != other.stroken_dashwidth) return false
        if (div_colors != null) {
            if (other.div_colors == null) return false
            if (!div_colors!!.contentEquals(other.div_colors!!)) return false
        } else if (other.div_colors != null) return false
        if (div_angle != other.div_angle) return false
        if (bottomLine != other.bottomLine) return false
        if (bottomLinePadding != other.bottomLinePadding) return false

        return true
    }

    override fun hashCode(): Int {
//      不让 select_state 和 div_selector 状态参与hashcode ,
//      保证读取缓存时排除影响  只缓存draw  将selector 拆分后缓存
        var result = div_selector
        result = 31 * result + div_color
        result = 31 * result + div_top_left_radius.hashCode()
        result = 31 * result + div_top_right_radius.hashCode()
        result = 31 * result + div_bottom_left_radius.hashCode()
        result = 31 * result + div_bottom_right_radius.hashCode()
        result = 31 * result + div_top_radius.hashCode()
        result = 31 * result + div_bottom_radius.hashCode()
        result = 31 * result + div_radius.hashCode()
        result = 31 * result + div_stroken_color
        result = 31 * result + stroken_width.hashCode()
        result = 31 * result + stroken_dashgap.hashCode()
        result = 31 * result + stroken_dashwidth.hashCode()
        result = 31 * result + div_state_color
        result = 31 * result + div_state_stroken_color
        result = 31 * result + (div_colors?.contentHashCode() ?: 0)
        result = 31 * result + div_angle
        result = 31 * result + bottomLine.hashCode()
        result = 31 * result + bottomLinePadding.hashCode()
        return result
    }

    fun clearSelector(){
        this.div_selector=0
        this.div_state_color=0
        this.div_state_stroken_color=0
    }

    /**
     * 获取 drawable
     */
    fun  drawDrawable(context: Context):Drawable?{
        if (!this.isDrawable){
            Log.i("ZHomeDrawableModel","attr can not build a drawable")
            return null
        }
        //先拿缓存
        var drawable= ZHomeDrawableCacheHelper.getInstance().getDrawable(this)
        if(drawable!=null){
            Log.i("ZHomeDrawableModel","get drawable from cache")
            return  drawable
        }
        drawable= ZHomeDrawableCreateUtils.drawDrawable(this)
        if(bottomLine){
            drawable?.let {
                drawable = ZHomeLayerDrawable.getLineDrawable(it, Color.LTGRAY,false,0.5f.dp2px(context).toInt(),bottomLinePadding.toInt())
            }
        }
        ZHomeDrawableCacheHelper.getInstance().cacheDrawable(this,drawable)
        Log.i("ZHomeDrawableModel","new drawable from attr, then cache it")
        return drawable
    }


}