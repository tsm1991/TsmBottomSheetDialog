package com.tsm.tsmbottomsheetdialog.recycler

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R
import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.CarouselLayoutManager
import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.CenterScrollListener
import kotlinx.android.synthetic.main.activity_recycler_view.*
import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.CarouselZoomPostLayoutListener
import android.widget.Toast

import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.PagerSnapHelper

import androidx.recyclerview.widget.RecyclerView
import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.CarouselLayoutManager.OnCenterItemSelectionListener

import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.DefaultChildSelectionListener
import com.tsm.tsmbottomsheetdialog.recycler.layoutmanager.DefaultChildSelectionListener.OnCenterItemClickListener
import java.util.*


/**
 * Create by tsm
 * on 2021/8/18
 */
class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        var layoutManager = CarouselLayoutManager()
        recycler_view.layoutManager = layoutManager
        recycler_view.addOnScrollListener(CenterScrollListener())
        recycler_view.setHasFixedSize(true)

        var colors= mutableListOf<Int>(Color.BLACK,Color.RED,Color.YELLOW,Color.CYAN,Color.GREEN,Color.GRAY)

        var adapter=object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_decoration_test, getList(23)) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder?.getView<View>(R.id.id_parent).setBackgroundColor(colors[holder?.adapterPosition % colors.size])
            }
            /**
             * 创建View时,同时指定屏幕的宽高比
             *
             */
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                var holder=super.onCreateViewHolder(parent, viewType)
                var id_parent=holder.getView<ConstraintLayout>(R.id.id_parent)
                id_parent?.apply {
                    var width:Int=(0.8f*getScreenWidth(this@RecyclerViewActivity)).toInt()
                    var params=ViewGroup.LayoutParams(width,(width*0.66f).toInt())
                    id_parent.layoutParams=params
                }
                return holder
            }
        }
        recycler_view.adapter = adapter

    }


    private fun getList(count: Int): MutableList<String>? {
        var list: MutableList<String> = MutableList(count, init = {
            it.toString()
        })
        return list
    }

    fun getScreenWidth(context: Context): Int {
        return context.applicationContext.resources.displayMetrics.widthPixels
    }


}