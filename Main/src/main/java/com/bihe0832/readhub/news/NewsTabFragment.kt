package com.bihe0832.readhub.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.libs.framework.fragment.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_news_tab.*
import me.yokeyword.fragmentation.SupportFragment

const val TYPE_NORMAL_NEWS = 0
const val TYPE_TECH_NEWS = 1
const val TYPE_BLOCK_CHAIN_NEWS = 2

class NewsTabFragment : BaseMainFragment() {
    private val fragments = arrayOfNulls<SupportFragment>(3)
    private var currentTab = TYPE_NORMAL_NEWS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_news_tab, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val firstFragment = findChildFragment(NewsListFragment::class.java)
        if (firstFragment == null) {
            fragments[TYPE_NORMAL_NEWS] = NewsListFragment.newInstance()
            fragments[TYPE_TECH_NEWS] = NewsListFragment.newInstance(TYPE_TECH_NEWS)
            fragments[TYPE_BLOCK_CHAIN_NEWS] = NewsListFragment.newInstance(TYPE_BLOCK_CHAIN_NEWS)

            loadMultipleRootFragment(R.id.content, TYPE_NORMAL_NEWS,
                    fragments[TYPE_NORMAL_NEWS],
                    fragments[TYPE_TECH_NEWS],
                    fragments[TYPE_BLOCK_CHAIN_NEWS])
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到fragments的引用
            fragments[TYPE_NORMAL_NEWS] = firstFragment
            fragments[TYPE_TECH_NEWS] = findChildFragment(NewsListFragment::class.java)
            fragments[TYPE_BLOCK_CHAIN_NEWS] = findChildFragment(NewsListFragment::class.java)
        }

        normal_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
        tech_news_tab.setTextColor(resources.getColor(R.color.primary_text))
        block_chain_news_tab.setTextColor(resources.getColor(R.color.primary_text))

        normal_news_tab.setOnClickListener {
            normal_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
            tech_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            block_chain_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            showHideFragment(fragments[TYPE_NORMAL_NEWS], fragments[currentTab])
            currentTab = TYPE_NORMAL_NEWS
        }

        tech_news_tab.setOnClickListener {
            normal_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            tech_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
            block_chain_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            showHideFragment(fragments[TYPE_TECH_NEWS], fragments[currentTab])
            currentTab = TYPE_TECH_NEWS
        }

        block_chain_news_tab.setOnClickListener {
            normal_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            tech_news_tab.setTextColor(resources.getColor(R.color.primary_text))
            block_chain_news_tab.setTextColor(resources.getColor(R.color.primary_blue))
            showHideFragment(fragments[TYPE_BLOCK_CHAIN_NEWS], fragments[currentTab])
            currentTab = TYPE_BLOCK_CHAIN_NEWS
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): NewsTabFragment = NewsTabFragment()
    }
}
