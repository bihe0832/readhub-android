package com.ottd.libs.framework.model

/**
 * Created by enzowei on 2018/2/5.
 */
data class TopicDetail(val id: String,
                       val createdAt: String,
                       val newsArray: List<News>?,
                       val order: String,
                       val publishDate: String,
                       val summary: String,
                       val title: String,
                       val updatedAt: String,
                       val timeline: Timeline?,
                       val entityTopic: List<Entity>,
//                       val entityEventTopics: Any,//暂时未知属性
                       val hasInstantView: Boolean
)

data class Timeline(val topics: List<Topic>,
                    val message: String,
                    val keyword: List<String>,
                    val errorCode: Int,
                    val commonEntites: List<Entity>)

data class Entity(val weight: Int = 0,
                  val nerName: String,
                  val entityId: Int,
                  val entityName: String,
                  val entityType: String,
                  val entityUniqueId: String?,
                  val eventType: Int = 0,
                  val eventTypeLabel: String?)