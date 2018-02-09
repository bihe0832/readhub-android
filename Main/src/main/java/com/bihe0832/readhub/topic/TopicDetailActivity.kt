package com.bihe0832.readhub.topic

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.bihe0832.readhub.topic.viewmodel.TopicViewModel
import com.bihe0832.readhub.webview.WebviewActivity
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.framework.model.News
import com.ottd.libs.framework.model.Topic
import kotlinx.android.synthetic.main.activity_topic_detail.*
import kotlinx.android.synthetic.main.activity_topic_detail_news_item.view.*
import kotlinx.android.synthetic.main.activity_topic_detail_timeline_item.view.*
import kotlin.properties.Delegates

const val INTENT_EXTRA_KEY_TOPIC_ID = "topic_id"

class TopicDetailActivity : AppCompatActivity() {
    private val viewModel: TopicViewModel by lazy { ViewModelProviders.of(this).get(TopicViewModel::class.java) }
    private val timelineListAdapter: TimelineListAdapter by lazy { TimelineListAdapter() }
    private val newsListAdapter: NewsListAdapter by lazy { NewsListAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(INTENT_EXTRA_KEY_TOPIC_ID)
        if (id.isNullOrBlank()) {
            finish()
        }

        setContentView(R.layout.activity_topic_detail)
        titleBar.title = resources.getString(R.string.app_name)//设置主标题
        setSupportActionBar(titleBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        titleBar.setNavigationOnClickListener { finish() }

        timelineList.apply {
            adapter = timelineListAdapter
            layoutManager = LinearLayoutManager(this@TopicDetailActivity)
        }
        newsList.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(this@TopicDetailActivity)
        }
        viewModel.topicDetail.observe(::getLifecycle) { detail ->
            detail?.let {
                Log.d(TopicListFragment.TAG, "observe:${it.id}")
                detailTitleView.text = it.title
                detailSummaryView.text = it.summary
                if (it.timeline == null || it.timeline?.topics?.size == 0) {
                    timelineDsc.visibility = View.GONE
                    timelineList.visibility = View.GONE
                } else {
                    it.timeline?.let {
                        timelineDsc.visibility = View.VISIBLE
                        timelineList.visibility = View.VISIBLE
                        timelineListAdapter.topicList = it.topics
                    }
                }
                if (it.newsArray == null || it.newsArray?.size == 0) {
                    newsDsc.visibility = View.GONE
                    newsList.visibility = View.GONE
                } else {
                    it.newsArray?.let {
                        newsDsc.visibility = View.VISIBLE
                        newsList.visibility = View.VISIBLE
                        newsListAdapter.newsList = it
                    }
                }
                timelineList.invalidate()
                newsList.invalidate()
                mainLayout.invalidate()
            }
        }
        viewModel.getTopicDetail(id)
    }
}

class TimelineListAdapter : RecyclerView.Adapter<CommonViewHolder>() {


    var topicList: List<Topic> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(topicList[position]) { topic ->
            date.text = "2.5"
            year.text = "2017"
            timelineTitle.text = topic.title
            when (position) {
                0 -> timelineTopLine.visibility = View.GONE
                topicList.size - 1 -> timelineBottomLine.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = topicList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
            CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_topic_detail_timeline_item, parent, false))
}

class NewsListAdapter : RecyclerView.Adapter<CommonViewHolder>() {

    var newsList: List<News> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(newsList[position]) { news ->
            newsTitle.text = news.title
            newsFrom.text = news.siteName
            setOnClickListener {
                WebviewActivity.openNewWeb(context.resources.getString(R.string.app_name),
                        news.mobileUrl ?: news.url)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
            CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_topic_detail_news_item, parent, false))

}