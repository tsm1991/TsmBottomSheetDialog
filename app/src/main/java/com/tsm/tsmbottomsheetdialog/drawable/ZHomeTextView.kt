package com.tsm.tsmbottomsheetdialog.drawable

import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tsm.tsmbottomsheetdialog.R
import com.tsm.tsmbottomsheetdialog.drawable.utils.ZHomeDrawableCreateUtils
import com.tsm.tsmbottomsheetdialog.drawable.utils.ZHomeDrawableModel
import com.tsm.tsmbottomsheetdialog.drawable.utils.ZHomeRippleDrawable

/**
 * Create by 田守明
 * on 2021/4/28
 */
open class ZHomeTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        initViews(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initViews(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initViews(context, attributeSet)
    }

    fun initViews(context: Context, attributeSet: AttributeSet?) {
        includeFontPadding=false
        attributeSet?.let {
            var type = context.obtainStyledAttributes(attributeSet, R.styleable.ZHomeTextView)

            var model = ZHomeDrawableModel()
            /**
             * selector
             */
            model.div_selector = type.getInt(R.styleable.ZHomeTextView_zhome_div_selector, 0)

            /**
             * 颜色
             */
            model.div_color = type.getColor(R.styleable.ZHomeTextView_zhome_div_color, 0)
            model.div_state_color = type.getColor(R.styleable.ZHomeTextView_zhome_div_select_state_color, 0)

            /**
             * 圆角
             */
            model.div_top_left_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_top_left_radius, 0f)
            model.div_top_right_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_top_right_radius, 0f)
            model.div_bottom_left_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_bottom_left_radius, 0f)
            model.div_bottom_right_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_bottom_right_radius, 0f)
            model.div_top_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_top_radius, 0f)
            model.div_bottom_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_bottom_radius, 0f)
            model.div_radius = type.getDimension(R.styleable.ZHomeTextView_zhome_div_radius, 0f)

            /**
             * 虚线
             */
            model.div_stroken_color = type.getColor(R.styleable.ZHomeTextView_zhome_div_stroken_color, 0)
            model.stroken_width = type.getDimension(R.styleable.ZHomeTextView_zhome_div_stroken_width, 0f)
            model.stroken_dashgap = type.getDimension(R.styleable.ZHomeTextView_zhome_div_stroken_dashgap, 0f)
            model.stroken_dashwidth = type.getDimension(R.styleable.ZHomeTextView_zhome_div_stroken_dashwidth, 0f)
            model.div_state_stroken_color = type.getColor(R.styleable.ZHomeTextView_zhome_div_select_state_stroken_color, 0)


            model.div_angle=type.getInt(R.styleable.ZHomeTextView_zhome_div_angle,0)


            var startColor=type.getColor(R.styleable.ZHomeTextView_zhome_div_start_color,0)
            var centerColor=type.getColor(R.styleable.ZHomeTextView_zhome_div_center_color,0)
            var endColor=type.getColor(R.styleable.ZHomeTextView_zhome_div_end_color,0)
            if(startColor!=0||centerColor!=0||endColor!=0){
                model.div_colors= ZHomeDrawableCreateUtils.getColors(startColor,centerColor,endColor)
            }

            model.bottomLine=type.getBoolean(R.styleable.ZHomeTextView_zhome_div_bottom_line,false)
            model.bottomLinePadding=type.getDimension(R.styleable.ZHomeTextView_zhome_div_bottom_line_padding,0f)
            var pressedScale=type.getBoolean(R.styleable.ZHomeTextView_zhome_div_pressed_scale,false)
            var ripple=type.getBoolean(R.styleable.ZHomeTextView_zhome_div_ripple,false)


            var selectTextColor=type.getColor(R.styleable.ZHomeTextView_zhome_div_select_text_color,0)

            type.recycle()


            if(pressedScale){
                stateListAnimator= AnimatorInflater.loadStateListAnimator(context, R.animator.button_press)
            }

            model?.drawDrawable(context)?.let {
                if(model.div_selector==4){
                    this.isClickable=true
                }
                background= it
            }

            if(ripple){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.foreground= ZHomeRippleDrawable.getRippleDrawable(
                        ContextCompat.getColor(context, R.color.zhome_ripper_color),null , null)
                }
            }
            setZhomeTextColor(selectTextColor, model.div_selector)
        }
    }

    private fun setZhomeTextColor(selectTextColor:Int ,selector:Int){
        if(selectTextColor!=0){
            when(selector){
                1->{
                    zhomeTextColor(android.R.attr.state_selected,selectTextColor)
                }
                2->{
                    zhomeTextColor(android.R.attr.state_checked,selectTextColor)
                }
                3->{
                    zhomeTextColor(android.R.attr.state_enabled,selectTextColor)
                }
                4->{
                    zhomeTextColor(android.R.attr.state_pressed,selectTextColor)
                }
            }
        }
    }


    private fun zhomeTextColor(state:Int,selectTextColor: Int){
        var states = arrayOf(
            intArrayOf(state),
            IntArray(0)
        )
        var colors = intArrayOf(
            selectTextColor,
            textColors.defaultColor
        )
        setTextColor(ColorStateList(states, colors))
    }



}