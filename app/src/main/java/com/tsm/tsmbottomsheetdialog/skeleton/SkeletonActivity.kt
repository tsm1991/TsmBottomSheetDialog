package com.tsm.tsmbottomsheetdialog.skeleton

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R
import kotlinx.android.synthetic.main.activity_skeleton_layout.*

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/23
 */
class SkeletonActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skeleton_layout)

//        var skeletonScreen = ViewSkeletonScreen.Builder(skeleton_parent)
//            .replaceWith(R.layout.skeleton_layout)
//            .replace()
//
//


        var skeletonScreen =RecyclerViewSkeletonScreen.Builder(recycler_view_skeleton_layout,TsmTestAdapter(
            mutableListOf("1","2","3","4","5","6")))
            .replace()

        Handler().postDelayed({skeletonScreen.reset()},5000)
    }
}


class TsmTestAdapter(data:MutableList<String>) : BaseQuickAdapter<String,BaseViewHolder>(R.layout.text_skeleton_item,data){
    override fun convert(holder: BaseViewHolder, item: String) {
    }
}