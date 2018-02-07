package com.bihe0832.readhub.news

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.base.topic.CommonViewHolder
import com.ottd.libs.framework.model.News
import com.ottd.libs.framework.utils.getDateCompareResult
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
            tips.text = "${news.siteName} * ${news.publishDate.getDateCompareResult()}"
            summary.text = news.summary

            val pixelDrawableSize = Math.round(forwardBtn.lineHeight * 0.9f)

            val textViewDrawable = context.resources.getDrawable(R.drawable.ic_open_in_new_black_24dp).apply {
                setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                colorFilter = PorterDuffColorFilter(context.resources.getColor(R.color.primary_dark), PorterDuff.Mode.SRC_IN)
            }
            forwardBtn.text = "前往《${news.siteName}》 查看详细内容"
            forwardBtn.setCompoundDrawables(textViewDrawable, null, null, null)
        }
    }

    override fun getItemCount(): Int = newsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder =
            CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_news_item, parent, false))
}



