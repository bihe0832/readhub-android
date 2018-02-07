package com.bihe0832.readhub.news

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bihe0832.readhub.R
import com.bihe0832.readhub.news.viewmodel.NewsListViewModel
import com.ottd.libs.framework.utils.getReadhubTimeStamp
import com.tencent.jygame.base.subscribe.ui.AutoLoadDecorator
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment : Fragment() {
	private val viewModel: NewsListViewModel by lazy { ViewModelProviders.of(this).get(NewsListViewModel::class.java) }

	private val newsListAdapter: NewsListAdapter by lazy { NewsListAdapter() }

	private val autoLoadDecorator by lazy { AutoLoadDecorator(list) }

	private var canLoadMore = false

	private var lastCursor = System.currentTimeMillis()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
														savedInstanceState: Bundle?): View? =
			inflater.inflate(R.layout.fragment_news_list, container, false)

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

//    refreshLayout.setOnPullListener(onPullListener)
		refreshLayout.setOnRefreshListener(onRefreshListener)

		list.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = newsListAdapter
		}

//    autoLoadDecorator.onScrollStateChanged(newsListAdapter.onScrollStateChanged)
		autoLoadDecorator.onLoadMore(::getMore)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel.newsList.observe(::getLifecycle, { newsList ->
			Log.d(TAG, "observe:${newsList?.pageSize}")
			newsList?.let {
				if (autoLoadDecorator.isLoadingMore) {
					newsListAdapter.newsList += it.data
				} else {
					newsListAdapter.newsList = it.data
				}
				autoLoadDecorator.isLoadingMore = false
				refreshLayout.isRefreshing = false
				canLoadMore = true

				lastCursor = it.data.last().publishDate.getReadhubTimeStamp()
			}
		})

		viewModel.getNewsList(lastCursor)
	}

//  private val onPullListener = object : OnPullListener {
//    override fun onPulling(headView: View?) {
//      val versionInfo = String.format(resources.getString(R.string.bihe0832_common_refresh_sub_title), TimeUtils.getDateCompareResult(lastRefreshTimeStamp))
//      refreshLayout.setRefreshSubText(versionInfo)
//    }
//
//    override fun onRefreshing(headView: View?) {
//    }
//
//    override fun onCanRefreshing(headView: View?) {
//    }
//
//  }

	private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
		Log.d(TAG, "onRefresh")
//    if (refreshLayout.isEnabled && !refreshLayout.isRefreshing) {
		Log.d(TAG, "start refresh")
		viewModel.getNewsList()
//    }
	}

	private fun getMore() {
		Log.d(TAG, "getMore")
		if (canLoadMore) {
			viewModel.getNewsList(lastCursor)
		} else {
			Toast.makeText(context, "滑到底部了，加载更多~", Toast.LENGTH_SHORT).show()
		}
	}

	companion object {
		val TAG = "NewsListFragment"
		@JvmStatic
		fun newInstance(): NewsListFragment = NewsListFragment()
	}
}
