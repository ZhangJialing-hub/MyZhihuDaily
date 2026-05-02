package com.example.myzhihudaily.api

import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.model.BeforeNewsResponse
import com.example.myzhihudaily.model.NewsDetailResponse
import com.example.myzhihudaily.model.LongCommentsResponse
import com.example.myzhihudaily.model.ShortCommentsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.rxjava3.core.Observable

/**
 * description:  ToDo:存放知乎api
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/1
 */

interface ZhihuApi{
    //最新消息
    @GET("api/4/news/latest")
    fun getLatestNews(): Observable<LatestNewsResponse>

    //过往消息
    @GET("api/4/news/before/{date}")
    fun getBeforeNews(@Path("date")date:String):Observable<BeforeNewsResponse>

    //新闻额外信息
    @GET("api/4/story-extra/{id}")
    fun getNewsDetail(@Path("id")id:Int):Observable<NewsDetailResponse>

    //新闻对应长评论查看
    @GET("api/4/story/{id}/long-comments")
   fun getLongComments(@Path("id")id:Int):Observable<LongCommentsResponse>

   //新闻对应短评论查看
    @GET("api/4/story/{id}/short-comments")
   fun getShortComments(@Path("id")id:Int):Observable<ShortCommentsResponse>
}