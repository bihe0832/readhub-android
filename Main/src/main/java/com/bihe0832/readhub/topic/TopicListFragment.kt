package com.bihe0832.readhub.topic

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bihe0832.readhub.R
import com.bihe0832.readhub.topic.viewmodel.TopicListViewModel
import com.ottd.libs.framework.fragment.BaseMainFragment
import com.tencent.jygame.base.subscribe.ui.AutoLoadDecorator
import kotlinx.android.synthetic.main.fragment_news_list.*

class TopicListFragment : BaseMainFragment() {

	private val viewModel: TopicListViewModel by lazy { ViewModelProviders.of(this).get(TopicListViewModel::class.java) }

	private val topicListAdapter: TopicListAdapter by lazy { TopicListAdapter() }

	private val autoLoadDecorator by lazy { AutoLoadDecorator(list) }

	private var canLoadMore = false

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
														savedInstanceState: Bundle?): View? =
			inflater.inflate(R.layout.fragment_topic_list, container, false)

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

//    refreshLayout.setOnPullListener(onPullListener)
		refreshLayout.setOnRefreshListener(onRefreshListener)

		list.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = topicListAdapter
		}

//    autoLoadDecorator.onScrollStateChanged(topicListAdapter.onScrollStateChanged)
		autoLoadDecorator.onLoadMore(::getMore)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel.topicList.observe(::getLifecycle, { newsList ->
			Log.d(TAG, "observe:${newsList?.pageSize}")
			newsList?.let {
				if (autoLoadDecorator.isLoadingMore) {
					topicListAdapter.topicList += it.data
				} else {
					topicListAdapter.topicList = it.data
				}
				autoLoadDecorator.isLoadingMore = false
				refreshLayout.isRefreshing = false
				canLoadMore = true
			}
		})

		viewModel.getTopicList()
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
		viewModel.getTopicList()
//    }
	}

	private fun getMore() {
		Log.d(TAG, "getMore")
		if (canLoadMore) {
			viewModel.getTopicList()
		} else {
			Toast.makeText(context, "滑到底部了，加载更多~", Toast.LENGTH_SHORT).show()
		}
	}

	companion object {
		val TAG = "TopicListFragment"
		@JvmStatic
		fun newInstance(): TopicListFragment = TopicListFragment()
	}
}
