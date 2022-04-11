package com.tsm.tsmbottomsheetdialog.recycler.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tsm.tsmbottomsheetdialog.R

/**
 * Create by tsm
 * on 2021/11/8
 */
class TsmRecordActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tsm_record_test)

    }
}




class RecordData{




}



class RecordAdapter:BaseQuickAdapter<RecordData,BaseViewHolder>{

    constructor(data:ArrayList<RecordData>?):super(R.layout.item_construction_news_view,data){

    }


    override fun convert(holder: BaseViewHolder, item: RecordData) {


    }




}
