package com.example.myzhihudaily.viewmodel

/**
 * description:   ToDo:详情页ViewModel模块
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2   16：38
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myzhihudaily.model.NewsDetailResponse
import com.example.myzhihudaily.model.LongCommentsResponse
import com.example.myzhihudaily.model.ShortCommentsResponse
import com.example.myzhihudaily.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import android.util.Log

class DetailViewModel : ViewModel() {

    //新闻详情
    private val _newsDetail = MutableLiveData<NewsDetailResponse>()
    val newsDetail: LiveData<NewsDetailResponse>
        get() = _newsDetail

    //长评论
    private val _longComments = MutableLiveData<LongCommentsResponse>()
    val longComments: LiveData<LongCommentsResponse>
        get() = _longComments

    //短评论
    private val _shortComments = MutableLiveData<ShortCommentsResponse>()
    val shortComments: LiveData<ShortCommentsResponse>
        get() = _shortComments

    //加载新闻详情
    fun loadDetail(id: Int) {
        NetRepository.getNewsDetail(id)
            .subscribe(object : Observer<NewsDetailResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DetailViewModel", "详情错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: NewsDetailResponse) {
                    _newsDetail.postValue(t)
                }
            })
    }

    //加载长评论
    fun loadLongComments(id: Int) {
        NetRepository.getLongComment(id)
            .subscribe(object : Observer<LongCommentsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DetailViewModel", "评论错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: LongCommentsResponse) {
                    _longComments.postValue(t)
                }
            })
    }

    //加载短评论
    fun loadShortComments(id: Int) {
        NetRepository.getShortComment(id)
            .subscribe(object : Observer<ShortCommentsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("DetailViewModel", "评论错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: ShortCommentsResponse) {
                    _shortComments.postValue(t)
                }
            })
    }
}