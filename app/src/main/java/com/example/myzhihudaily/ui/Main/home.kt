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
import com.example.myzhihudaily.model.LatestNewsResponse
import com.example.myzhihudaily.utils.DateUtil
import com.example.myzhihudaily.utils.ShareUtil
import com.example.myzhihudaily.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.example.myzhihudaily.ui.Main.NewsListItem

@OptIn(ExperimentalMaterial3Api::class)  // 只保留这一个
@Composable
fun HomeScreen(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNewsClick: (Int) -> Unit
) {
    val newsList by viewModel.newsList.observeAsState(emptyList())
    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

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
                modifier = Modifier.fillMaxSize()
            ) {
                // 轮播 Banner
                newsList.firstOrNull()?.top_stories?.let {
                    item { Banner(it, onNewsClick) }
                }

                // 日期分组 + 新闻列表
                newsList.forEach { dayNews ->
                    item {
                        Text(
                            text = DateUtil.formatDate(dayNews.date ?: "暂无日期"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    val stories = dayNews.stories ?: emptyList()
                    items(newsList.story) { story ->
                        NewsListItem(
                            story = story,
                            onClick = { onNewsClick(story.id) },
                            onShare = {
                                ShareUtil.shareNews(
                                    context,
                                    story.title,
                                    "https://daily.zhihu.com/story/${story.id}"
                                )
                            }
                        )
                    }

                }
            }
        }
    }
}