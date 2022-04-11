package com.tsm.tsmbottomsheetdialog.scroll

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.tsm.tsmbottomsheetdialog.MainActivity
import com.tsm.tsmbottomsheetdialog.R
import kotlinx.android.synthetic.main.scroll_view_activity.*

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/3/22
 */
class ScrollActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_view_activity)


        mainVerticalSeekBar.setOnProgressChangeListener {
            Log.i("tian.shm","progress:${it}")
        }


        var bar= ProgressBar(this)
        bar.max=100

        Intent(this, MainActivity::class.java).apply {
            putExtra("intent",TsmBean())
            startActivity(this)
        }
    }

}