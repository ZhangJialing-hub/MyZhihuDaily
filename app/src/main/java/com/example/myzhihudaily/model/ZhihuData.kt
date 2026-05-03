package com.example.myzhihudaily.model

/**
 * description:  ToDo:存放知乎api包含的数据
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/1
 */
//最新消息
data class LatestNewsResponse(
    val date: String?,
    val stories: List<Story>?,
    val top_stories: List<TopStory>?
)

//过往消息
data class BeforeNewsResponse(
    val date: String?,
    val stories: List<Story>?,
    val top_stories: List<TopStory>?
)

//新闻
data class Story(
    val id: Int,
    val title: String?,
    val hint: String?,
    val images: List<String>?,
    val type: Int?,
    val ga_prefix: String?
)

//界面顶部的Banner图
data class TopStory(
    val id: Int,
    val title: String?,
    val hint: String?,
    val image: String?,
    val type: Int?,
    val ga_prefix: String?
)

//额外信息
data class NewsDetailResponse(
    val long_comments: Int?,
    val popularity: Int?,
    val short_comments: Int?,
    val comments: Int?
)

//长评论
data class LongCommentsResponse(
    val comments: List<Comment>?
)

//短评论
data class ShortCommentsResponse(
    val comments: List<Comment>?
)

//评论列表
data class Comment(
    val author: String?,
    val content: String?,
    val avatar: String?,
    val time: Long?,
    val id: Int,
    val likes: Int?
)