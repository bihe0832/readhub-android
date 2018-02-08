package com.bihe0832.readhub.topic.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ottd.libs.framework.model.TopicList
import com.ottd.libs.framework.network.ReadHubApi
import topic.network.enqueue


const val DEFAULT_PAGE_SIZE_TOPIC = 5
const val DEFAULT_CURSOR_TOPIC = ""

class TopicListViewModel : ViewModel() {

	val topicList = MutableLiveData<TopicList>()
	val error = MutableLiveData<String>()

	fun getTopicList(lastCursor: String = DEFAULT_CURSOR_TOPIC, pageSize: Int = DEFAULT_PAGE_SIZE_TOPIC) {

		ReadHubApi.apiService.topic(lastCursor, pageSize).enqueue {
			onResponse { _, response ->
				Log.d("TopicListViewModel", "onResponse:${response?.body()?.pageSize}")
				response?.let {
					topicList.value = it.body()
				}
			}
			onFailure { _, t ->
				if (t == null) {
					error.value = "Unknow Error"
				} else {
					Log.e("NewsListViewModel", t.localizedMessage)
					error.value = t.toString()
				}
			}
		}
	}
}