package com.bihe0832.readhub.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.bihe0832.readhub.news.NewsListFragment
import com.bihe0832.readhub.test.debug.TestDebugFragment
import com.bihe0832.readhub.topic.TopicListFragment
import com.ottd.libs.framework.fragment.BaseMainFragment
import kotlinx.android.synthetic.main.com_bihe0832_test_main_fragment.*


/**
 * Created by hardyshi on 16/6/30.
 */
class MainFragment : BaseMainFragment() {
    private val tabString by lazy { arrayOf(TAB_FOR_DEVELOPER, TAB_FOR_NEW) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.com_bihe0832_test_main_fragment, container, false)
        initView()
        framework_tab.currentTab = arguments?.getInt(MainFragment.INTENT_EXTRA_KEY_TEST_ITEM_TAB) ?: DEFAULT_TAB
        return view
    }

    private fun initView() {
        framework_viewPager.apply {
            adapter = MyTaskPagerFragmentAdapter(childFragmentManager)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    framework_tab.hideMsg(position)
                }

                override fun onPageSelected(position: Int) {
                    framework_tab.hideMsg(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }

        framework_tab.setViewPager(framework_viewPager)
    }

    private inner class MyTaskPagerFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when {
            //修改此处，重新编译，即可使用new tab调试任意独立的无Activity模块的fragment
                tabString[position] == TAB_FOR_NEW -> NewsListFragment.newInstance()
                tabString[position] == TAB_FOR_DEVELOPER -> TopicListFragment.newInstance()
                else -> TestDebugFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return tabString.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabString[position]
        }
    }

    companion object {

        val DEFAULT_TAB = 0
        val INTENT_EXTRA_KEY_TEST_ITEM_TAB = MainFragment::class.java.name + "INTENT_KEY_TAB"
        private val TAB_FOR_DEVELOPER = "动态测试"
        private val TAB_FOR_NEW = "资讯测试"

        fun newInstance(tab: Int): MainFragment = MainFragment().apply {
            arguments = Bundle().apply {
                putInt(MainFragment.INTENT_EXTRA_KEY_TEST_ITEM_TAB, tab)
            }
        }
    }
}
