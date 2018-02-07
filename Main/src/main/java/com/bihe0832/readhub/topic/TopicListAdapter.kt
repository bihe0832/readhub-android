package com.bihe0832.readhub.test.news

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.config.Config
import com.ottd.libs.framework.OttdFramework
import com.ottd.libs.framework.model.Topic
import com.ottd.libs.framework.utils.getDateCompareResult
import kotlinx.android.synthetic.main.fragment_topic_item_list.view.*
import kotlinx.android.synthetic.main.fragment_topic_item_summary.view.*
import kotlin.properties.Delegates


class TopicListAdapter : RecyclerView.Adapter<CommonViewHolder>() {
    var topicList: List<Topic> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(topicList[position]) { topic ->
            if (Config.readConfig(TOPIC_VIEW_TYPE_KEY, TOPIC_VIEW_TYPE_LIST) == TOPIC_VIEW_TYPE_LIST) {
                listTopicTitle.text = topic.title
                listTopicTips.text = when {
                    topic.newsArray.size > 3 -> "${topic.newsArray.size} 家媒体已报道"
                    else -> topic.publishDate.getDateCompareResult()
                }
            } else {
                summaryTopicTitle.text = topic.title
                summaryTopicTips.text = when {
                    topic.newsArray.size > 3 -> "${topic.newsArray.size} 家媒体已报道"
                    else -> topic.publishDate.getDateCompareResult()
                }
                //TODO hardy 摘要被折叠，无法打开
                summaryTopicSummary.text = topic.summary

                val pixelDrawableSize = Math.round(summaryTopicSubTopic.lineHeight * 0.9f)

                val textViewDrawable = context.resources.getDrawable(R.drawable.ic_star_black_24dp).apply {
                    setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                    colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.card_title_color), PorterDuff.Mode.SRC_IN)
                }
                summaryTopicSubTopic.apply {
                    setCompoundDrawables(textViewDrawable, null, null, null)
                    setOnClickListener {
                        OttdFramework.getInstance().showWaitting()
                    }
                }

                val moreAboutDrawable = context.resources.getDrawable(R.drawable.ic_open_in_new_black_24dp).apply {
                    setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                    colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.primary_dark), PorterDuff.Mode.SRC_IN)
                }
                summaryTopicMoreAboutTopic.apply {
                    setCompoundDrawables(moreAboutDrawable, null, null, null)
                    setOnClickListener {
                        OttdFramework.getInstance().showWaitting()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = topicList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val s = Config.readConfig(TOPIC_VIEW_TYPE_KEY, TOPIC_VIEW_TYPE_LIST)
        val view = if (s == TOPIC_VIEW_TYPE_SUMMARY) {
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_topic_item_summary, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_topic_item_list, parent, false)
        }
        return CommonViewHolder(view)
    }

}



