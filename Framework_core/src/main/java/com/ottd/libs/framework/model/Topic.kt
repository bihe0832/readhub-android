package com.ottd.libs.framework.model

/**
 * Created by enzowei on 2018/2/5.
 */
data class Topic(val id:Long,
                 val url:String,
                 val title:String,
                 val groupId:Int,
                 val siteName:String,
                 val siteSlug:String,
                 val mobileUrl:String,
                 val authorName:String,
                 val duplicateId:Int,
                 val publishDate:String)