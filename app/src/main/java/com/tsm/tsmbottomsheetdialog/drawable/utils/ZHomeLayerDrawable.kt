package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.ColorInt
import com.tsm.tsmbottomsheetdialog.dp2px

object ZHomeLayerDrawable {


    /**
     * 创建 上面 带有横线的 drawable
     */
    @JvmStatic
    fun getTopLineDrawable(context: Context, bgDrawable: Drawable, @ColorInt lineColor: Int = Color.LTGRAY , lineHeight:Int=0.5f.dp2px(context).toInt()):LayerDrawable{
        return getLineDrawable(bgDrawable,lineColor,true,lineHeight,0)
    }


    /**
     * 创建下面带有横线的drawable
     */
    @JvmStatic
    fun getBottomLineDrawable(context: Context,bgDrawable: Drawable, @ColorInt lineColor: Int = Color.LTGRAY ,lineHeight:Int=0.5f.dp2px(context).toInt()):LayerDrawable{
        return getLineDrawable(bgDrawable,lineColor,false,lineHeight,0)
    }

    @JvmStatic
    fun getLineDrawable(bgDrawable: Drawable,lineColor: Int,isTop:Boolean,lineHeight: Int,horPadding:Int):LayerDrawable{
        var line=ZHomeGradientDrawable.getInstance().drawRect().drawColor(lineColor)
        /**
         * 注意顺序,先放进去的是line ,后方的是bgDrawable
         */
        val arrays = arrayOf<Drawable>(line,bgDrawable)
        var layer=LayerDrawable(arrays)
        layer.setLayerInset(0,horPadding,0,horPadding,0)
        /**
         * 修改 顺序为1 的drawable 的底部边距为 5dp
         * 这样就能显示出来 顺序为 0 的 Drawable 了
         */
        layer.setLayerInset(1,0, if(isTop) lineHeight else 0 ,0, if(isTop) 0 else lineHeight)
        return layer
    }



}