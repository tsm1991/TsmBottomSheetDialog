package com.tsm.tsmbottomsheetdialog

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/23
 */
class App :Application() {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}