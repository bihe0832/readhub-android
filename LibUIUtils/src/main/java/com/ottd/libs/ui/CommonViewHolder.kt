package com.ottd.base.topic

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by enzowei on 2017/12/5.
 */
class CommonViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
  inline fun <T> bind(item: T, onBind: View.(T) -> Unit) = itemView.onBind(item)
}