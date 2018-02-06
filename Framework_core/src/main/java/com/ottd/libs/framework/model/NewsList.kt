package com.ottd.libs.framework.model

/**
 * Created by enzowei on 2018/2/5.
 */
data class NewsList(val data:List<News>,
                    val pageSize:Int,
                    val totalItems:Long,
                    val totalPages:Long)