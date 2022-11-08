package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

/**
 * Create by 田守明
 * on 2021/5/13
 */
object ZHomeDrawableCreateUtils  {


    /**
     * 通过属性创建Drawable
     */
    fun drawDrawable(model : ZHomeDrawableModel):Drawable?{
        var drawable : Drawable? = null
        if (model.div_selector > 0) {
            /**
             * 讲道理这里应该把每个 item 都缓存起来,但是实际应用起来就不一样了
             */
            drawable = drawColor(model.div_color, null)
            drawable = drawRadius(drawable, model.div_top_left_radius, model.div_top_right_radius, model.div_bottom_left_radius, model.div_bottom_right_radius, model.div_top_radius, model.div_bottom_radius, model.div_radius)
            drawable = drawStroken(model.stroken_width, model.div_stroken_color, model.stroken_dashgap, model.stroken_dashwidth, drawable)

            var stateDrawable: ZHomeGradientDrawable? = null
            stateDrawable = drawColor(model.div_state_color, stateDrawable)
            stateDrawable = drawRadius(stateDrawable, model.div_top_left_radius, model.div_top_right_radius, model.div_bottom_left_radius, model.div_bottom_right_radius, model.div_top_radius, model.div_bottom_radius, model.div_radius)
            stateDrawable = drawStroken(model.stroken_width, model.div_state_stroken_color, model.stroken_dashgap, model.stroken_dashwidth, stateDrawable)
            var draw =drawSelector(model.div_selector, drawable, stateDrawable)
            return draw
        }

        if (model.isOnlyColorDrawable) {
            return ColorDrawable(model.div_color)
        }

        drawable = drawColor(model.div_color, null)
        drawable = drawColors(model.div_colors,model.div_angle, drawable)
        drawable = drawRadius(drawable, model.div_top_left_radius, model.div_top_right_radius, model.div_bottom_left_radius, model.div_bottom_right_radius, model.div_top_radius, model.div_bottom_radius, model.div_radius)
        drawable = drawStroken(model.stroken_width, model.div_stroken_color, model.stroken_dashgap, model.stroken_dashwidth, drawable)
        return drawable
    }



    /**
     * 绘制边界线
     */
    @JvmStatic
    fun drawStroken(width: Float, color: Int, gap: Float, dsshWidth: Float, drawable: ZHomeGradientDrawable?): ZHomeGradientDrawable? {
        if (width > 0f && color != 0) {
            return getZiRoomViewDraw(drawable).drawStroke(width.toInt(), color, dsshWidth, gap)
        }
        return drawable
    }

    /**
     * 绘制填充色
     */
    @JvmStatic
    fun drawColor(color: Int, drawable: ZHomeGradientDrawable?): ZHomeGradientDrawable? {
        if (color != 0) {
            return getZiRoomViewDraw(drawable).drawColor(color)
        }
        return drawable
    }


    /**
     * 绘制填充色
     */
    @JvmStatic
    fun drawColors(colors: IntArray?, angle:Int, drawable: ZHomeGradientDrawable?): ZHomeGradientDrawable? {
        if (colors != null) {
            return getZiRoomViewDraw(drawable).drawColors(colors).drawAngle(angle)
        }
        return drawable
    }


    /**
     * 绘制圆角
     */
    @JvmStatic
    fun drawRadius(drawable: ZHomeGradientDrawable?, topLeft: Float, topRight: Float, bottomLeft: Float, bottomRight: Float, topRadius: Float, bottomRadius: Float, radius: Float): ZHomeGradientDrawable? {
        var totleRadius = topLeft + topRight + bottomLeft + bottomRight + topRadius + bottomRadius + radius
        if (totleRadius <= 0) {
            return drawable
        }
        var array = FloatArray(8)
        if (radius > 0) {
            array[0] = radius
            array[1] = radius
            array[2] = radius
            array[3] = radius
            array[4] = radius
            array[5] = radius
            array[6] = radius
            array[7] = radius
        }
        if (topRadius > 0) {
            array[0] = topRadius
            array[1] = topRadius
            array[2] = topRadius
            array[3] = topRadius
        }
        if (bottomRadius > 0) {
            array[4] = bottomRadius
            array[5] = bottomRadius
            array[6] = bottomRadius
            array[7] = bottomRadius
        }
        if (topLeft > 0) {
            array[0] = topLeft
            array[1] = topLeft
        }
        if (topRight > 0) {
            array[2] = topRight
            array[3] = topRight
        }
        if (bottomLeft > 0) {
            array[6] = bottomLeft
            array[7] = bottomLeft

        }
        if (bottomRight > 0) {
            array[4] = bottomRight
            array[5] = bottomRight
        }



        return getZiRoomViewDraw(drawable).drawRadiusDp(array)
    }

    @JvmStatic
    fun drawSelector(selector: Int, drawable: ZHomeGradientDrawable?, stateDrawable: ZHomeGradientDrawable?): ZHomeStateListDrawable? {
        drawable?.let {
            stateDrawable?.let {
                when (selector) {
                    1 -> {
                        return ZHomeStateListDrawable()
                            .setSelectDrawable(stateDrawable, drawable)
                    }
                    2 -> {
                        return ZHomeStateListDrawable()
                            .setCheckableeDrawable(stateDrawable, drawable)
                    }
                    3 -> {
                        return ZHomeStateListDrawable()
                            .setEnableDrawable(stateDrawable, drawable)
                    }
                    4 -> {
                        return ZHomeStateListDrawable()
                            .setPressedDrawable(stateDrawable, drawable)
                    }
                    else ->
                        return null
                }
            }
        }
        return null
    }



    /**
     * 获取drawable
     */
    @JvmStatic
    fun getZiRoomViewDraw(drawable: ZHomeGradientDrawable?): ZHomeGradientDrawable {
        return drawable ?: ZHomeGradientDrawable()
            .drawRect()
    }


    @JvmStatic
    fun getColors(startColor:Int,centerColor :Int,endColor:Int) : IntArray {
        var list:ArrayList<Int> = ArrayList()
        if(startColor!=0){
            list.add(startColor)
        }
        if(centerColor!=0){
            list.add(centerColor)
        }
        if(endColor!=0){
            list.add(endColor)
        }
        var array:IntArray
        if(list.size==3){
            array=IntArray(3)
            array[0]=list.get(0)
            array[1]=list.get(1)
            array[2]=list.get(2)
        }else if(list.size==2){
            array=IntArray(2)
            array[0]=list.get(0)
            array[1]=list.get(1)
        }else{
            array=IntArray(1)
            array[0]=list.get(0)
        }
        return array
    }


}