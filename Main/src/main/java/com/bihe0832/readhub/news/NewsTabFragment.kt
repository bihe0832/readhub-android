package com.bihe0832.readhub.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.libs.framework.fragment.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_news_tab.*
import me.yokeyword.fragmentation.SupportFragment

class NewsTabFragment : BaseMainFragment() {
    private val fragments = arrayOfNulls<SupportFragment>(2)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_news_tab, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val firstFragment = findChildFragment(NewsListFragment::class.java)
        if (firstFragment == null) {
            fragments[TYPE_NORMAL_NEWS] = NewsListFragment.newInstance()
            fragments[TYPE_TECH_NEWS] = NewsListFragment.newInstance(TYPE_TECH_NEWS)

            loadMultipleRootFragment(R.id.content, TYPE_NORMAL_NEWS,
                    fragments[TYPE_NORMAL_NEWS],
                    fragments[TYPE_TECH_NEWS])
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到fragments的引用
            fragments[TYPE_NORMAL_NEWS] = firstFragment
            fragments[TYPE_TECH_NEWS] = findChildFragment(NewsListFragment::class.java)
        }

        normal_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
        tech_news_tab.setTextColor(resources.getColor(R.color.primary_text))

        normal_news_tab.setOnClickListener {
            normal_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
            tech_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            showHideFragment(fragments[0], fragments[1])
        }

        tech_news_tab.setOnClickListener {
            normal_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            tech_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
            showHideFragment(fragments[1], fragments[0])
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): NewsTabFragment = NewsTabFragment()
    }
}
