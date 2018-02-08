package com.bihe0832.readhub.test.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.ottd.libs.framework.fragment.BaseBackFragment
import kotlinx.android.synthetic.main.com_bihe0832_test_tab_fragment.*

abstract class BaseTestFragment : BaseBackFragment() {
    private val pagerAdapter: TestPagerAdapter by lazy { TestPagerAdapter(_mActivity) }

    abstract fun getDataList(): List<TestItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.com_bihe0832_test_tab_fragment, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        pagerAdapter.setData(getDataList())
        test_recy.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(_mActivity)
            adapter = pagerAdapter
        }
    }

    protected fun sendInfo(title: String, content: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }.let { startActivity(Intent.createChooser(it, title)) }
    }

}
