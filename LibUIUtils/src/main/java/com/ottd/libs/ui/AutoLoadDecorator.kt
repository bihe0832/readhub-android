package com.tencent.jygame.base.subscribe.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by enzowei on 2017/12/25.
 */
class AutoLoadDecorator(private val attachedRecyclerView: RecyclerView) {
  private val layoutManager: LinearLayoutManager = attachedRecyclerView.layoutManager as LinearLayoutManager
  var isLoadingMore = false
  private var onLoadMore: () -> Unit = {}
  private var onScrolled: ((recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit)? = null
  private var onScrollStateChanged: ((recyclerView: RecyclerView?, newState: Int) -> Unit)? = null

  init {
    attachedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        onScrolled?.invoke(recyclerView, dx, dy)
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        //有回调接口，且不是加载状态，且计算后剩下2个item
        if (!isLoadingMore && lastVisibleItemPosition >= attachedRecyclerView.adapter.itemCount - 2) {
          onLoadMore()
          isLoadingMore = true
        }
      }

      override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        onScrollStateChanged?.invoke(recyclerView, newState)
      }

    })
  }

  fun onLoadMore(onLoadMore: () -> Unit) {
    this.onLoadMore = onLoadMore
  }

  fun onScrolled(onScrolled: (recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit) {
    this.onScrolled = onScrolled
  }

  fun onScrollStateChanged(onScrollStateChanged: (recyclerView: RecyclerView?, newState: Int) -> Unit) {
    this.onScrollStateChanged = onScrollStateChanged
  }
}
