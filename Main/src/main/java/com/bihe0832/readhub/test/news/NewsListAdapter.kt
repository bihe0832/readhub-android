package com.bihe0832.readhub.test.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.framework.model.News
import kotlinx.android.synthetic.main.fragment_news_item.view.*
import kotlin.properties.Delegates

/**
 * Created by enzowei on 2017/12/1.
 */
class NewsListAdapter : RecyclerView.Adapter<CommonViewHolder>() {
  var newsList: List<News> by Delegates.observable(emptyList()) { prop, old, new ->
    //    autoNotify(old, new) { o, n -> o.id == n.id }
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    holder.bind(newsList[position]) { news ->
      title.text = news.title
    }
  }

  override fun getItemCount(): Int = newsList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
      CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item, parent, false))
}



