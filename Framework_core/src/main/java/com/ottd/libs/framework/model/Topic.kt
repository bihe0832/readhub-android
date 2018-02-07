package com.ottd.libs.framework.model

/**
 * Created by enzowei on 2018/2/5.
 */
data class Topic(val id: String?,
                 val title: String?,
                 val summary: String?,
                 val newsArray: List<News>?,
                 val weiboArray: List<News>?,
                 val wechatArray: List<News>?,
                 val relatedTopicArray: List<News>?,
                 val publishUserId: String?,
                 val order: String?,
                 val publishDate: String?,
                 val createdAt: String?,
                 val updatedAt: String?)


