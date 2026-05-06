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
import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.model.LongCommentsResponse
import com.example.myzhihudaily.model.ShortCommentsResponse
import com.example.myzhihudaily.repository.NetRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import android.util.Log

class DetailViewModel : ViewModel() {

    //新闻详情
    private val _newsDetail = MutableLiveData<LatestNewsResponse>()
    val newsDetail: LiveData<LatestNewsResponse>
        get() = _newsDetail
    private val _newsExtraInfo = MutableLiveData<NewsDetailResponse>()
    val newsExtraInfo: LiveData<NewsDetailResponse> =
        _newsExtraInfo
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
        NetRepository.getLatestNews(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // onNext
                _newsDetail.postValue(it)
            }, {
                // onError
                Log.d("DetailViewModel", "详情错误：${it.message}")
            })
    }
    fun loadNewsExtraInfo(id: Int) {
        NetRepository.getNewsDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ extraData ->
                _newsExtraInfo.postValue(extraData)
            }, {
                Log.d("DetailViewModel", "额外信息错误：${it.message}")
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


