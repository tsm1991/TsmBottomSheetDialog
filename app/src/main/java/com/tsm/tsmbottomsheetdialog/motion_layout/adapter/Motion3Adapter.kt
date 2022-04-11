package com.tsm.tsmbottomsheetdialog.motion_layout.adapter

import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/14
 */
class Motion3Adapter(data:MutableList<String>?) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_motion_layout_3,data) {
    override fun convert(holder: BaseViewHolder, item: String) {
    }
}