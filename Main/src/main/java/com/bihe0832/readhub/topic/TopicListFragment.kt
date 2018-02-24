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
import com.bihe0832.readhub.topic.viewmodel.DEFAULT_PAGE_SIZE_TOPIC
import com.bihe0832.readhub.topic.viewmodel.TopicViewModel
import com.ottd.libs.framework.fragment.BaseBackFragment
import com.tencent.jygame.base.subscribe.ui.AutoLoadDecorator
import kotlinx.android.synthetic.main.fragment_topic_list.*


const val TOPIC_VIEW_TYPE_LIST = 1
const val TOPIC_VIEW_TYPE_SUMMARY = 2
const val CONFIG_KEY_TOPIC_VIEW_TYPE = "READHUB_TOPIC_TYPE"
const val CONFIG_KEY_NEWS_HAS_EN = "READHUB_NEWS_EN"

class TopicListFragment : BaseBackFragment() {

    private val viewModel: TopicViewModel by lazy { ViewModelProviders.of(this).get(TopicViewModel::class.java) }

    private val topicListAdapter: TopicListAdapter by lazy { TopicListAdapter() }

    private val autoLoadDecorator by lazy { AutoLoadDecorator(list) }

    private var canLoadMore = false

    private var isFirstLoad = true

    private var lastCursor = ""
    private var pageSize = DEFAULT_PAGE_SIZE_TOPIC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_topic_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        refreshLayout.setOnRefreshListener(onRefreshListener)

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topicListAdapter
        }

        autoLoadDecorator.onLoadMore(::getMore)

        errorText.setOnClickListener {
            viewModel.getTopicList(lastCursor, pageSize)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.topicList.observe(::getLifecycle) { newsList ->
            Log.d(TAG, "observe:${newsList?.pageSize}")
            newsList?.let {
                isFirstLoad = false

                if (autoLoadDecorator.isLoadingMore) {
                    topicListAdapter.topicList += it.data
                } else {
                    topicListAdapter.topicList = it.data
                }
                autoLoadDecorator.isLoadingMore = false
                refreshLayout.isRefreshing = false
                canLoadMore = true

                lastCursor = it.data.last().order
                val intOrder = it.data.last().order.toInt().rem(DEFAULT_PAGE_SIZE_TOPIC)
                pageSize = when {
                    intOrder > 0 -> intOrder
                    else -> DEFAULT_PAGE_SIZE_TOPIC
                }

                errorText.visibility = View.GONE
                refreshLayout.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(::getLifecycle) { _ ->
            if (isFirstLoad) {
                errorText.visibility = View.VISIBLE
                refreshLayout.visibility = View.GONE
            }
        }

        viewModel.getTopicList(lastCursor, pageSize)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        Log.d(TAG, "onRefresh")
        Log.d(TAG, "start refresh")
        viewModel.getTopicList()
    }

    private fun getMore() {
        Log.d(TAG, "getMore")
        if (canLoadMore) {
            viewModel.getTopicList(lastCursor, pageSize)
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
