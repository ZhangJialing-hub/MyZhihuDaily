package com.example.myzhihudaily.ui.Main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myzhihudaily.utils.DateUtil
import com.example.myzhihudaily.utils.ShareUtil
import com.example.myzhihudaily.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNewsClick: (Int, String) -> Unit
) {
    val newsList by viewModel.newsList.observeAsState(emptyList())
    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("知乎日报") }) }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = pullRefreshState,
            onRefresh = {
                coroutineScope.launch { viewModel.refresh() }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                // 轮播 Banner
                val firstDayNews = newsList.firstOrNull()
                val todayDate = firstDayNews?.date ?: ""
                firstDayNews?.top_stories?.let {
                    item {
                        Banner(it, onBannerClick = { id ->
                            onNewsClick(id, todayDate)
                        })
                    }
                }
                //新闻列表
                newsList.forEach { dayNews ->
                    item {
                        Text(
                            text = DateUtil.formatDate(dayNews.date ?: "暂无日期"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    val stories = dayNews.stories ?: emptyList()
                    items(stories) { story ->
                        NewsListItem(
                            story = story,
                            onClick = { onNewsClick(story.id,dayNews.date?:"") },
                            onShare = {
                                ShareUtil.shareNews(
                                    context,
                                    story.title?:"暂无信息",
                                    "https://daily.zhihu.com/story/${story.id}"
                                )
                            }
                        )
                    }

                }
            }
            // 加载更多
            LaunchedEffect(lazyListState.isScrollInProgress) {
                val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount
                if (lastVisibleIndex >= totalItemCount - 2 && !isRefreshing) {
                    viewModel.loadMore()
                }
    }
}}}