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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import android.util.Log
import com.example.myzhihudaily.model.BeforeNewsResponse
import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.repository.NetRepository
import com.example.myzhihudaily.utils.DateUtil
class MainViewModel : ViewModel() {

    //使用MutableLiveData
    private val _latestNews = MutableLiveData<LatestNewsResponse>()
    val latestNews: LiveData<LatestNewsResponse>
        get() = _latestNews

    private val _beforeNews = MutableLiveData<BeforeNewsResponse>()
    val beforeNews: LiveData<BeforeNewsResponse>
        get() = _beforeNews

    //2026/5/3 22：19 为实现首页刷新等新增
    //合并后的新闻列表
    private val _newsList = MutableLiveData<List<LatestNewsResponse>>(emptyList())
    val newsList: LiveData<List<LatestNewsResponse>> = _newsList

    //刷新状态
    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    //是否正在加载更多
    private val _isLoadMore = MutableLiveData(false)
    val isLoadMore: LiveData<Boolean> = _isLoadMore

    // 记录上拉加载当前到哪天了
    private var currentOldDate = MutableLiveData<String?>()

    //ViewModel的初始化方法
    init {
        getLatestNews()
    }

    //获取最新数据
    fun getLatestNews() {
        _isRefreshing.value = true
        NetRepository.getLatestNews()
            .subscribe(object : Observer<LatestNewsResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.d("MainViewModel", "错误：${e.message}")
                }

                override fun onComplete() {}

                override fun onNext(t: LatestNewsResponse) {
                    _latestNews.postValue(t)
                    _newsList.postValue(listOf(t))
                    _isRefreshing.value = false
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

                    // 把 BeforeNewsResponse 转成 LatestNewsResponse 追加到合并列表
                    val currentList = _newsList.value?.toMutableList() ?: mutableListOf()
                    currentList.add(
                        LatestNewsResponse(
                            date = t.date,
                            stories = t.stories,
                            top_stories = t.top_stories
                        )
                    )
                    _newsList.postValue(currentList)
                    _isLoadMore.value = false
                }
            })
    }


    //下拉刷新
    fun refresh() {
        _isRefreshing.value = true
        getLatestNews()
    }

    //加载更多

    fun loadMore() {
        if (_isLoadMore.value == true) return
        _isLoadMore.value = true

        val lastDate = _newsList.value?.lastOrNull()?.date
        if (lastDate.isNullOrBlank()) {
            _isLoadMore
                .value = false
            return
        }

        getBeforeNews(lastDate)
        }
    }

