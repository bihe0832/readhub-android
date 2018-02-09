package com.ottd.libs.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ottd.base.topic.CommonViewHolder

/**
 * Created by enzowei on 2018/2/9.
 */
class CommonRecyclerAdapter<T>(build: CommonRecyclerAdapter<T>.() -> Unit) : RecyclerView.Adapter<CommonViewHolder>() {
    private lateinit var onLayout: (viewType: Int) -> Int
    private lateinit var onItem: (position: Int) -> T
    private var onCount: (() -> Int)? = null
    private lateinit var onBind: View.(T) -> Unit

    init {
        build()
    }

    fun onLayout(onLayout: (viewType: Int) -> Int) {
        this.onLayout = onLayout
    }

    fun onItem(onItem: (position: Int) -> T) {
        this.onItem = onItem
    }

    fun onBind(onBind: View.(T) -> Unit) {
        this.onBind = onBind
    }

    fun onCount(onCount: () -> Int) {
        this.onCount = onCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
            CommonViewHolder(LayoutInflater.from(parent.context).inflate(onLayout(viewType), parent, false))

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(onItem(position), onBind)
    }

    override fun getItemCount(): Int =
            onCount?.invoke() ?: 0

}