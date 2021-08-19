package com.tsm.tsmbottomsheetdialog

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*

/**
 * Create by tsm
 * on 2021/7/29
 */

/**
 * 淡入浅出效果
 * parentRoot 是这个控件的ViewGroup 可以不用是直接父控件
 * 不能连点,否则会出现控件莫名消失的情况,
 */
fun  View?.fadeVisibleOrGone(parentRoot:ViewGroup,boolean: Boolean?){
    //Fade,Slide,Explode:根据view的visibility的状态执行渐入渐出，滑动，分解动画。
    TransitionManager.beginDelayedTransition(parentRoot, Fade())
    if (boolean == true) {
        this?.visibility = View.VISIBLE
    } else {
        this?.visibility = View.GONE
    }
}

/**
 * textView  展开收起
 */
fun  TextView?.expandOrCollosed(parentRoot:ViewGroup,boolean: Boolean){
    TransitionManager.beginDelayedTransition(parentRoot, ChangeBounds())
    if(boolean){
        this?.maxLines=1
    }else{
        this?.maxLines=100
    }

}
