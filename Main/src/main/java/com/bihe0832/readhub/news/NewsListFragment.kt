package com.bihe0832.readhub.news

import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import com.bihe0832.readhub.webview.WebviewActivity
import com.ottd.libs.framework.fragment.BaseBackFragment
import com.ottd.libs.framework.model.News
import com.ottd.libs.framework.utils.getDateCompareResult
import com.ottd.libs.framework.utils.getReadhubTimeStamp
import com.ottd.libs.ui.CommonRecyclerAdapter
import com.tencent.jygame.base.subscribe.ui.AutoLoadDecorator
import kotlinx.android.synthetic.main.fragment_news_item.view.*
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlin.properties.Delegates



class NewsListFragment : BaseBackFragment() {
    private val viewModel: NewsListViewModel by lazy { ViewModelProviders.of(this).get(NewsListViewModel::class.java) }

    private var newsList: List<News> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        list.adapter.notifyDataSetChanged()
    }

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
            adapter = CommonRecyclerAdapter<News> {
                onLayout { _ -> R.layout.fragment_news_item }
                onCount { newsList.size }
                onItem { position -> newsList[position] }
                onBind { news ->
                    title.text = news.title
                    tips.text = "${news.siteName} • ${news.publishDate.getDateCompareResult()}"
                    summary.text = news.summary

                    forwardBtn.apply {
                        text = "前往「 ${news.siteName} 」查看详细内容"

                        val pixelDrawableSize = Math.round(this.lineHeight * 0.9f)
                        val textViewDrawable = context.resources.getDrawable(R.drawable.ic_open_in_new_black_24dp).apply {
                            setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                            colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.primary_blue), PorterDuff.Mode.SRC_IN)
                        }
                        setCompoundDrawables(textViewDrawable, null, null, null)

                        setOnClickListener {
                            WebviewActivity.openNewWeb(context.resources.getString(R.string.app_name),
                                    news.mobileUrl ?: news.url)
                        }
                    }
                }
            }
        }

        autoLoadDecorator.onLoadMore(::getMore)

        errorText.setOnClickListener {
            fetchData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.newsList.observe(::getLifecycle) { _newsList ->
            Log.d(TAG, "observe:${_newsList?.pageSize}")
            _newsList?.let {
                isFirstLoad = false
                if (autoLoadDecorator.isLoadingMore) {
                    newsList += it.data
                } else {
                    newsList = it.data
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
        lastCursor = System.currentTimeMillis()
        fetchData()
    }

    private fun fetchData() {
        when (type) {
            TYPE_NORMAL_NEWS -> viewModel.getNewsList(lastCursor)
            TYPE_TECH_NEWS -> viewModel.getTechNewsList(lastCursor)
            TYPE_BLOCK_CHAIN_NEWS -> viewModel.getBlockchainNewsList(lastCursor)
            else -> viewModel.getNewsList(lastCursor)
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
