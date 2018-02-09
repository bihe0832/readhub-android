package com.bihe0832.readhub.topic

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.R
import com.bihe0832.readhub.topic.viewmodel.TopicViewModel
import com.ottd.libs.framework.fragment.BaseBackFragment
import kotlinx.android.synthetic.main.fragment_topic_detail.*


class TopicDetailFragment : BaseBackFragment() {

    private val viewModel: TopicViewModel by lazy { ViewModelProviders.of(this).get(TopicViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_topic_detail, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.topicDetail.observe(::getLifecycle) { detail ->
            detail?.let {
                Log.d(TopicListFragment.TAG, "observe:${it.id}")
                title.text = it.title
            }
        }
    }


    companion object {
        val TAG = "TopicDetailFragment"
        @JvmStatic
        fun newInstance(): TopicDetailFragment = TopicDetailFragment()
    }
}