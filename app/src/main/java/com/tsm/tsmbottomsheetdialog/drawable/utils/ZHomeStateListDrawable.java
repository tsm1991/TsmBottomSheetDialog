package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.AttrRes;


/**
 *
 * 可以配合 AsyncLayoutInflater  加快速度
 *  或者使用x2c 的方式更好
 *
 * 这种用法不仅可以不用写文件节省了包体积,而且还节省了xml 转 bean 的过程速度更快,
 *
 *  类似draw.xml 文件的select 用法
 * <selector>  </selector>
 */
public class ZHomeStateListDrawable extends StateListDrawable {
    /**
     *
     * 初始和按下背景
     *
     * @param pressedDrawable
     * @param unPressedDrawable
     * @return
     */
    public ZHomeStateListDrawable setPressedDrawable(Drawable pressedDrawable, Drawable unPressedDrawable){
        int pressed =  android.R.attr.state_pressed;
        this.addState(new int[] {pressed},pressedDrawable);
        this.addState(new int[] {-pressed},unPressedDrawable);
        return this;
    }
    /**
     * 初始和选择背景
     * @param selectDrawable
     * @param unsSelectDrawable
     * @return
     */
    public ZHomeStateListDrawable setSelectDrawable(Drawable selectDrawable, Drawable unsSelectDrawable){
        int select =  android.R.attr.state_selected;
        this.addState(new int[] {select},selectDrawable);
        this.addState(new int[] {-select},unsSelectDrawable);
        return this;
    }

    public ZHomeStateListDrawable setEnableDrawable(Drawable selectDrawable, Drawable unsSelectDrawable){
        int select =  android.R.attr.state_enabled;
        this.addState(new int[] {select},selectDrawable);
        this.addState(new int[] {-select},unsSelectDrawable);
        return this;
    }


    public ZHomeStateListDrawable setCheckableeDrawable(Drawable selectDrawable, Drawable unsSelectDrawable){
        int select =  android.R.attr.state_checkable;
        this.addState(new int[] {select},selectDrawable);
        this.addState(new int[] {-select},unsSelectDrawable);
        return this;
    }


    public ZHomeStateListDrawable setStateDrawable(@AttrRes int state, Drawable stateDrawable, Drawable unsStateDrawable){
        this.addState(new int[] {state},stateDrawable);
        this.addState(new int[] {-state},unsStateDrawable);
        return this;
    }


}
