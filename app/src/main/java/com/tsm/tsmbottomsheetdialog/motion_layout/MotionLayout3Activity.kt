package com.tsm.tsmbottomsheetdialog.motion_layout

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tsm.tsmbottomsheetdialog.R
import com.tsm.tsmbottomsheetdialog.statusbar.StatusBarUtils
import kotlinx.android.synthetic.main.activity_motion_layout_3.*


/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/14
 */
class MotionLayout3Activity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_3)

        StatusBarUtils.fitStatusBar(this,true)

        iv_tsm_3.setOnClickListener {
            val location_index = IntArray(2)
            iv_tsm_3.getLocationOnScreen(location_index)
            val alphaAnim = ObjectAnimator.ofFloat(iv_tsm_3, "alpha", 1.0f,0.8f, 0f)
            var scan=getScreenWidth(this)*1f/iv_tsm_3.width
            val scaleXAnim = ObjectAnimator.ofFloat(iv_tsm_3, "scaleX", 1f, scan)
            val scaleYAnim = ObjectAnimator.ofFloat(iv_tsm_3, "scaleY", 1f, scan)
            val transXAnim: ObjectAnimator =ObjectAnimator.ofFloat(iv_tsm_3, "translationX", location_index[0]*1f-iv_tsm_3.width/2, 0f)
            val transYAnim: ObjectAnimator = ObjectAnimator.ofFloat(iv_tsm_3, "translationY", location_index[1]*1f-iv_tsm_3.height/2+StatusBarUtils.getStatusBarHeight(), 0f)
            val set = AnimatorSet()
            set.playTogether(alphaAnim, scaleXAnim, scaleYAnim, transXAnim, transYAnim)
            set.duration = 2000
            set.start()
        }



    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

}