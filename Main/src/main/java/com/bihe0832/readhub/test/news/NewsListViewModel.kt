package com.bihe0832.readhub.test.news

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
  val newsList: MutableLiveData<NewsList> = MutableLiveData<NewsList>()

  fun getNewsList() {
    ReadHubApi.apiService.news().enqueue {
      onResponse { _, response ->
        Log.d("NewsListViewModel", "onResponse:${response?.body()?.pageSize}")
        response?.let {
          newsList.value = it.body()
        }
      }
      onFailure { _, t ->
        Log.e("NewsListViewModel", t?.localizedMessage)
      }
    }
  }
}