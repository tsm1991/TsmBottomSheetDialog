package com.tsm.tsmbottomsheetdialog.motion_layout

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.tsm.tsmbottomsheetdialog.R
import kotlinx.android.synthetic.main.activity_motion_layout_1.*

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/11
 */
class MotionLayout1Activity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_1)


        Handler().postDelayed({
            parent_root.transitionToEnd()
        },2000)
    }
}