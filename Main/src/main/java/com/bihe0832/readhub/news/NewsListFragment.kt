package com.bihe0832.readhub.news

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
import com.bihe0832.readhub.news.viewmodel.NewsListViewModel
import com.ottd.libs.framework.fragment.BaseBackFragment
import com.ottd.libs.framework.utils.getReadhubTimeStamp
import com.tencent.jygame.base.subscribe.ui.AutoLoadDecorator
import kotlinx.android.synthetic.main.fragment_news_list.*

const val TYPE_NORMAL_NEWS = 0
const val TYPE_TECH_NEWS = 1

class NewsListFragment : BaseBackFragment() {
    private val viewModel: NewsListViewModel by lazy { ViewModelProviders.of(this).get(NewsListViewModel::class.java) }

    private val newsListAdapter: NewsListAdapter by lazy { NewsListAdapter() }

    private val autoLoadDecorator by lazy { AutoLoadDecorator(list) }

    private var canLoadMore = false

    private var isFirstLoad = true

    private var lastCursor = System.currentTimeMillis()

    private val type: Int by lazy { arguments?.getInt("type") ?: TYPE_NORMAL_NEWS }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_news_list, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        refreshLayout.setOnRefreshListener(onRefreshListener)

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }

        autoLoadDecorator.onLoadMore(::getMore)

        errorText.setOnClickListener {
            fetchData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.newsList.observe(::getLifecycle) { newsList ->
            Log.d(TAG, "observe:${newsList?.pageSize}")
            newsList?.let {
                isFirstLoad = false
                if (autoLoadDecorator.isLoadingMore) {
                    newsListAdapter.newsList += it.data
                } else {
                    newsListAdapter.newsList = it.data
                }
                autoLoadDecorator.isLoadingMore = false
                refreshLayout.isRefreshing = false
                canLoadMore = true

                lastCursor = it.data.last().publishDate.getReadhubTimeStamp()
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

        fetchData()
    }


    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        Log.d(TAG, "start refresh")
        fetchData()
    }

    private fun fetchData() {
        when (type) {
            TYPE_NORMAL_NEWS -> viewModel.getNewsList(lastCursor)
            else -> viewModel.getTechNewsList(lastCursor)
        }
    }

    private fun getMore() {
        Log.d(TAG, "getMore")
        if (canLoadMore) {
            fetchData()
        } else {
            Toast.makeText(context, "滑到底部了，加载更多~", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val TAG = "NewsListFragment"
        @JvmStatic
        fun newInstance(type: Int = TYPE_NORMAL_NEWS): NewsListFragment =
                NewsListFragment().apply {
                    arguments = Bundle().apply { putInt("type", type) }
                }
    }
}
