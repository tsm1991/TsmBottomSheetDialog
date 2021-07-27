package com.tsm.tsmbottomsheetdialog

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.tsm_bottom_sheet.TsmBaseBottomSheetDialog
import java.util.*

/**
 * Create by tsm
 * on 2021/5/24
 */
class TsmBottomSheetDialog(context: Activity) : TsmBaseBottomSheetDialog(context) {
    override val layoutId: Int
        protected get() = R.layout.dialog_tsm_bottom_sheet

    override fun initViews() {
        val recycler_view = findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view!!.adapter = object :BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_simple_test,getList(23)){
            override fun convert(holder: BaseViewHolder, item: String) {
                holder?.setText(R.id.tv_item, item)
            }
        }
    }

    private fun getList(count: Int): MutableList<String>? {
        var list: MutableList<String> = MutableList(count,init = {
            it.toString()
        })
        return list
    }

    override val bottomLayoutId: Int
        protected get() = R.layout.botttom_sheet_bottom_view
}