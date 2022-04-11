package com.tsm.tsmbottomsheetdialog.motion_layout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/11
 * 学习过程
 * https://blog.csdn.net/knight1996/article/details/108015536
 */
class TsmMotionLayoutActivity :AppCompatActivity(), OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var adapter = object :BaseQuickAdapter<String?, BaseViewHolder>(R.layout.item_simple_test, getList()) {
            override fun convert(holder: BaseViewHolder, item: String?) {
                holder?.setText(R.id.tv_item, item)
            }
        }
        adapter.setOnItemClickListener(this)
        recycler_view_main.adapter=adapter
    }


    private fun getList(): MutableList<String?> {
        val list = mutableListOf<String?>()
        list.add("点击移动")
        list.add("滑动动画")
        list.add("点赞动画")
        return list
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (position) {
            0-> startActivity(Intent(this,MotionLayout1Activity::class.java))
            1-> startActivity(Intent(this,MotionLayout2Activity::class.java))
            2-> startActivity(Intent(this,MotionLayout3Activity::class.java))
        }
    }
}