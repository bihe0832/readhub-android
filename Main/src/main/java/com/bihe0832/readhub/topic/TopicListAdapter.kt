package com.bihe0832.readhub.topic

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bihe0832.readhub.R
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.config.Config
import com.ottd.libs.framework.OttdFramework
import com.ottd.libs.framework.model.Topic
import com.ottd.libs.framework.utils.getDateCompareResult
import com.ottd.libs.framework.utils.startWith
import kotlinx.android.synthetic.main.fragment_topic_item_list.view.*
import kotlinx.android.synthetic.main.fragment_topic_item_summary.view.*
import kotlin.properties.Delegates


class TopicListAdapter : RecyclerView.Adapter<CommonViewHolder>() {

    private val config = Config.readConfig(CONFIG_KEY_TOPIC_VIEW_TYPE, TOPIC_VIEW_TYPE_LIST)

    var topicList: List<Topic> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(topicList[position]) { topic ->
            if (config == TOPIC_VIEW_TYPE_LIST) {
                listTopicTitle.text = topic.title
                listTopicTips.setTips(topic)
                fragment_topic_item_list.setOnClickListener {
                    goToDetailPage(topic.id,topic.title)
                }
            } else {
                summaryTopicTitle.text = topic.title
                summaryTopicTips.setTips(topic)
                summaryTopicSummary.text = topic.summary

                val pixelDrawableSize = Math.round(summaryTopicSubTopic.lineHeight * 0.9f)

                summaryTopicSubTopic.apply {
                    val drawable = context.resources.getDrawable(R.drawable.ic_star_black_24dp).apply {
                        setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                        colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.card_title_color), PorterDuff.Mode.SRC_IN)
                    }
                    setCompoundDrawables(drawable, null, null, null)
                    setOnClickListener {
                        OttdFramework.getInstance().showWaitting()
                    }
                    visibility = View.GONE
                }

                summaryTopicMoreAboutTopic.apply {
                    val drawable = context.resources.getDrawable(R.drawable.ic_open_in_new_black_24dp).apply {
                        setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                        colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.primary_blue), PorterDuff.Mode.SRC_IN)
                    }
                    setCompoundDrawables(drawable, null, null, null)
                    setOnClickListener {
                        goToDetailPage(topic.id,topic.title)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = topicList.size

    private fun goToDetailPage(id: String,title: String) {
        Intent().apply {
            setClass(OttdFramework.getInstance().applicationContext, TopicDetailActivity::class.java)
            putExtra(INTENT_EXTRA_KEY_TOPIC_ID, id)
            putExtra(INTENT_EXTRA_KEY_TOPIC_TITLE, title)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.startWith(OttdFramework.getInstance().applicationContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val viewId = if (config == TOPIC_VIEW_TYPE_SUMMARY) {
            R.layout.fragment_topic_item_summary
        } else {
            R.layout.fragment_topic_item_list
        }
        return CommonViewHolder(LayoutInflater.from(parent.context).inflate(viewId, parent, false))
    }

    private fun TextView.setTips(topic: Topic) {
        text = when {
            topic.newsArray.size > 3 -> "${topic.newsArray.size} 家媒体已报道"
            else -> topic.publishDate.getDateCompareResult()
        }
    }

}