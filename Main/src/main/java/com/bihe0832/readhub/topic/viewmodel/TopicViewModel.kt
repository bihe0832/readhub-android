package com.bihe0832.readhub.topic.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ottd.libs.framework.model.TopicDetail
import com.ottd.libs.framework.model.TopicList
import com.ottd.libs.framework.network.ReadHubApi
import topic.network.enqueue


const val DEFAULT_PAGE_SIZE_TOPIC = 5
const val DEFAULT_CURSOR_TOPIC = ""

class TopicViewModel : ViewModel() {

    val topicList by lazy { MutableLiveData<TopicList>() }
    val topicDetail by lazy { MutableLiveData<TopicDetail>() }
    val error by lazy { MutableLiveData<String>() }

    fun getTopicList(lastCursor: String = DEFAULT_CURSOR_TOPIC, pageSize: Int = DEFAULT_PAGE_SIZE_TOPIC) {

        ReadHubApi.apiService.topic(lastCursor, pageSize).enqueue {
            onResponse { _, response ->
                Log.d("getNewsList", "onResponse:${response?.body()?.pageSize}")
                response?.let {
                    topicList.value = it.body()
                }
            }
            onFailure { _, t ->
                if (t == null) {
                    error.value = "Unknow Error"
                } else {
                    Log.e("getNewsList", t.localizedMessage)
                    error.value = t.toString()
                }
            }
        }
    }

    fun getTopicDetail(id: String) {
        ReadHubApi.apiService.topicDetail(id).enqueue {
            onResponse { _, response ->
                Log.d("getTopicDetail", "onResponse:${response?.body()?.id}")
                response?.let {
                    topicDetail.value = it.body()
                }
            }
            onFailure { _, t ->
                if (t == null) {
                    error.value = "Unknow Error"
                } else {
                    Log.e("getTopicDetail", t.localizedMessage)
                    error.value = t.toString()
                }
            }
        }
    }
}