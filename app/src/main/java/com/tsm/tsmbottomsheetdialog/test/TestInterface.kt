package com.tsm.tsmbottomsheetdialog.test

import android.widget.LinearLayout
import java.util.*

/**
 * Create by tsm
 * on 2021/11/10
 */
interface TestInterface {

    fun addTop(linearTop:LinearLayout)

    fun refreshData(isLogin:Boolean)

    fun onSelectDate(date:Date)
}