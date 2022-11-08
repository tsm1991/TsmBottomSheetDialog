package com.tsm.tsmbottomsheetdialog.drawable

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater

/**
 * Create by 田守明
 * on 2021/4/29
 */
abstract class ZHomeConstraintLayout : ZHomeDrawableConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)


    override fun initViews(context: Context, attributeSet: AttributeSet?) {
        super.initViews(context, attributeSet)
        LayoutInflater.from(context).inflate(getLayoutId(),this,true)
        if(!isInEditMode){
            initViewData()
        }
    }

    protected  abstract fun getLayoutId(): Int

    abstract fun initViewData()


}