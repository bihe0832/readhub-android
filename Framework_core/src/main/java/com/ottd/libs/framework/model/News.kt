package com.ottd.libs.framework.model

/**
 * Created by enzowei on 2018/2/5.
 */
data class News(val id: Long,
                val title: String,
                val summary: String,
                val summaryAuto: String,
                val url: String,
                val mobileUrl: String,
                val siteName: String,
                val siteSlug: String,
                val language: String,
                val authorName: String,
                val publishDate: String)