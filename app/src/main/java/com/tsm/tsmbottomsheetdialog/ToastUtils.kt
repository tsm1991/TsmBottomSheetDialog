package com.tsm.tsmbottomsheetdialog

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/3/28
 */
object ToastUtils {

    fun showSuccess(context: Context, message: CharSequence?){
        showSelf(context,R.drawable.toast_success,message)
    }
    fun showFail(context: Context, message: CharSequence?){
        showSelf(context,R.drawable.toast_faile,message)
    }


    fun showSelf(context: Context, @DrawableRes statusIconId: Int, message: CharSequence?) {
        if (TextUtils.isEmpty(message)) return
        val inflate = View.inflate(context, R.layout.layout_toast_base, null)
        val iv_status = inflate.findViewById<ImageView>(R.id.iv_status)
        iv_status.setImageResource(statusIconId)
        var toast= Toast.makeText(context,message,Toast.LENGTH_SHORT)
        toast.view=inflate
        toast.setGravity(Gravity.CENTER,0,0)
        toast.duration= Toast.LENGTH_SHORT
        toast.setText(message)
        toast.show()
    }

}