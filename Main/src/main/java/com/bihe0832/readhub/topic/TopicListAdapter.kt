package com.bihe0832.readhub.test.news

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.framework.model.Topic
import com.ottd.libs.ui.ResourceUtils
import kotlinx.android.synthetic.main.fragment_topic_item_summary.view.*
import kotlin.properties.Delegates
import android.graphics.drawable.Drawable




class TopicListAdapter : RecyclerView.Adapter<CommonViewHolder>() {
  var topicList: List<Topic> by Delegates.observable(emptyList()) { prop, old, new ->
    //    autoNotify(old, new) { o, n -> o.id == n.id }
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    holder.bind(topicList[position]) { topic ->
        topicTitle.text = topic.title

        val pixelDrawableSize = Math.round(subTopic.lineHeight * 0.9f)

        var textViewDrawable = context.resources.getDrawable(R.drawable.ic_star_black_24dp)
        textViewDrawable.setBounds(0, 0, pixelDrawableSize,pixelDrawableSize)
        textViewDrawable.colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.card_title_color), PorterDuff.Mode.SRC_IN)
        subTopic.setCompoundDrawables(textViewDrawable, null, null, null)

        textViewDrawable = context.resources.getDrawable(R.drawable.ic_open_in_new_black_24dp)
        textViewDrawable.setBounds(0, 0, pixelDrawableSize,pixelDrawableSize)
        textViewDrawable.colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.primary_dark), PorterDuff.Mode.SRC_IN)
        moreAboutTopic.setCompoundDrawables(textViewDrawable, null, null, null)
    }
  }

  override fun getItemCount(): Int = topicList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
      CommonViewHolder(
              if (true){
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_topic_item_summary, parent, false)
              }else{
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_topic_item_list, parent, false)
              }

      )
}



