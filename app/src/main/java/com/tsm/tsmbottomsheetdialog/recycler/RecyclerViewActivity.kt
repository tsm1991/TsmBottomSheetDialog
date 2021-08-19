package com.tsm.tsmbottomsheetdialog.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R
import com.tsm.tsmbottomsheetdialog.TsmDecoration
import kotlinx.android.synthetic.main.activity_recycler_view.*

/**
 * Create by tsm
 * on 2021/8/18
 */
class RecyclerViewActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        var drawable:MutableList<Bitmap> = mutableListOf(BitmapFactory.decodeResource(resources,R.mipmap.ccc),BitmapFactory.decodeResource(resources,R.mipmap.ccc),BitmapFactory.decodeResource(resources,R.mipmap.ccc),BitmapFactory.decodeResource(resources,R.mipmap.ccc))
        recycler_view.addItemDecoration(TsmDecoration(this,drawable))
        recycler_view.adapter=object : BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_decoration_test,getList(23)){
            override fun convert(holder: BaseViewHolder, item: String) {}
        }
    }


    private fun getList(count: Int): MutableList<String>? {
        var list: MutableList<String> = MutableList(count,init = {
            it.toString()
        })
        return list
    }

}