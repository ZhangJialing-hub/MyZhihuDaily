package com.example.myzhihudaily.viewmodel

/**
 * description:  ToDo:首页ViewModel模块
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2   14：46
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import android.util.Log
import com.example.myzhihudaily.model.BeforeNewsResponse
import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.repository.NetRepository

class MainViewModel : ViewModel() {

    //使用MutableLiveData
    private val _latestNews = MutableLiveData<LatestNewsResponse>()
    val latestNews: LiveData<LatestNewsResponse>
        get() = _latestNews

    private val _beforeNews = MutableLiveData<BeforeNewsResponse>()
    val beforeNews: LiveData<BeforeNewsResponse>
        get() = _beforeNews

    //ViewModel的初始化方法
    init {
        getLatestNews()
    }

    //获取最新数据
    fun getLatestNews() {
        NetRepository.getLatestNews()
            .subscribe(object : Observer<LatestNewsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: LatestNewsResponse) {
                    _latestNews.postValue(t)
                }
            })
    }

    //获取历史数据
    fun getBeforeNews(date: String) {
        NetRepository.getBeforeNews(date)
            .subscribe(object : Observer<BeforeNewsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: BeforeNewsResponse) {
                    _beforeNews.postValue(t)
                }
            })
    }
}