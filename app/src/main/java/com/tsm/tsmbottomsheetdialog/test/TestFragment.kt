package com.tsm.tsmbottomsheetdialog.test

import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import java.util.*

/**
 * Create by tsm
 * on 2021/11/10
 */
class TestFragment :Fragment() , TestInterface {

    @CallSuper
    override fun addTop(linearTop: LinearLayout) {
    }

    @CallSuper
    override fun refreshData(isLogin: Boolean) {
    }

    @CallSuper
    override fun onSelectDate(date: Date) {
    }
}