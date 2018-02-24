package com.ottd.libs.framework.utils

import android.content.Context
import android.content.Intent

/**
 * Created by enzowei on 2018/2/9.
 */
fun Intent.startWith(context: Context) {
    context.startActivity(this)
}