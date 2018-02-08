package com.bihe0832.readhub.news.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ottd.libs.framework.model.NewsList
import com.ottd.libs.framework.network.ReadHubApi
import topic.network.enqueue

/**
 * Created by enzowei on 2018/2/5.
 */
class NewsListViewModel : ViewModel() {
	val newsList = MutableLiveData<NewsList>()
	val error = MutableLiveData<String>()
	fun getNewsList(lastCursor: Long = System.currentTimeMillis(), pageSize: Int = 10) {
		ReadHubApi.apiService.news(lastCursor, pageSize).enqueue {
			onResponse { _, response ->
				Log.d("NewsListViewModel", "onResponse:${response?.body()?.pageSize}")
				response?.let {
					newsList.value = it.body()
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

	fun getTechNewsList(lastCursor: Long = System.currentTimeMillis(), pageSize: Int = 10) {
		ReadHubApi.apiService.technews(lastCursor, pageSize).enqueue {
			onResponse { _, response ->
				Log.d("NewsListViewModel", "onResponse:${response?.body()?.pageSize}")
				response?.let {
					newsList.value = it.body()
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