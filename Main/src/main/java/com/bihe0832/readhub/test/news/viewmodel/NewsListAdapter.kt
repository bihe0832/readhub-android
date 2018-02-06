package com.ottd.base.topic

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ezstudio.myapplication.R
import com.ottd.libs.framework.model.News
import kotlinx.android.synthetic.main.fragment_news_item.view.*
import kotlin.properties.Delegates

/**
 * Created by enzowei on 2017/12/1.
 */
class NewsListAdapter : RecyclerView.Adapter<CommonViewHolder>() {
  var newsList: List<News> by Delegates.observable(emptyList()) { prop, old, new ->
    //    autoNotify(old, new) { o, n -> o.id == n.id }
    Log.e("Delegates.observable", "$new")
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
    holder.bind(newsList[position]) { news ->
      titleTextView.text = news.title
    }
  }

  override fun getItemCount(): Int = newsList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
      CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item, parent, false))
}



