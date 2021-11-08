package com.tsm.tsmbottomsheetdialog

import java.math.BigDecimal


fun Long.divide(divisorNum: Long): String {
    //被除数
    val dividend = BigDecimal.valueOf(this)
    //除数
    val divisor = BigDecimal.valueOf(divisorNum)
    return dividend.divide(divisor).toString()
}