package com.example.myzhihudaily.repository

/**
 * description:  ToDo:网络请求
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2  10:11
 */

import com.example.myzhihudaily.api.ZhihuApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.model.BeforeNewsResponse
import com.example.myzhihudaily.model.NewsDetailResponse
import com.example.myzhihudaily.model.LongCommentsResponse
import com.example.myzhihudaily.model.ShortCommentsResponse

object NetRepository {
    private val retrofit=Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    private val zhihuApi= retrofit.create(ZhihuApi::class.java)

     //获取最新日报列表
    fun getLatestNews(): Observable<LatestNewsResponse> {
        return zhihuApi.getLatestNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    //获取过去日报列表
    fun getBeforeNews(date: String): Observable<BeforeNewsResponse> {
        return zhihuApi.getBeforeNews(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

     //获取新闻详情
    fun getNewsDetail(id: Int): Observable<NewsDetailResponse> {
        return zhihuApi.getNewsDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    //获取长评论
    fun getLongComment(id: Int): Observable<LongCommentsResponse> {
        return zhihuApi.getLongComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

     //获取短评论
    fun getShortComment(id: Int): Observable<ShortCommentsResponse> {
        return zhihuApi.getShortComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}