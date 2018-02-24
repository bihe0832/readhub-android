package com.ottd.libs.framework.model

data class TopicList(val data:List<Topic>,
                     val pageSize:Int,
                     val totalItems:Long,
                     val totalPages:Long)
