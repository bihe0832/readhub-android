package com.ottd.libs.framework.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by enzowei on 2018/2/7.
 */
fun String.getReadhubTimeStamp(): Long = try {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this).time
} catch (e: ParseException) {
    0L
}

fun String.getDateCompareResult(): String = getReadhubTimeStamp().getDateCompareResult()

fun Long.getDateCompareResult(): String {
    val minute = (1000 * 60).toLong()
    val hour = minute * 60
    val day = hour * 24
    val month = day * 30
    val year = month * 12
    val currentTimestamp = System.currentTimeMillis()
//    println(oldTimestamp)
//    println(currentTimestamp)
    val diffValue = currentTimestamp - this
    val yearC = diffValue / year
    val monthC = diffValue / month
    val weekC = diffValue / (7 * day)
    val dayC = diffValue / day
    val hourC = diffValue / hour
    val minC = diffValue / minute
    return when {
        yearC > 0 -> "${yearC}年前"
        monthC > 0 -> "${monthC}月前"
        weekC > 0 -> "${weekC}周前"
        dayC > 0 -> "${dayC}天前"
        hourC > 0 -> "${hourC}小时前"
        minC > 0 -> "${minC}分钟前"
        diffValue > 0 -> "${diffValue}秒前"
        else -> "不久前"
    }
}