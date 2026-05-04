
package com.example.myzhihudaily.ui.detail

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myzhihudaily.utils.ShareUtil
import com.example.myzhihudaily.viewmodel.DetailViewModel
import com.example.myzhihudaily.viewmodel.MainViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    newsId: Int,
    allNewsIds: List<Int>,
    onBack: () -> Unit,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val allNewsList by viewModel.currentList.observeAsState(emptyList())
    LaunchedEffect(newsId) {
        viewModel.loadDetail(newsId)
        viewModel.loadLongComments(newsId)
        viewModel.loadShortComments(newsId)
    }

    val newsDetail by viewModel.newsDetail.observeAsState()
    val longComments by viewModel.longComments.observeAsState()
    val shortComments by viewModel.shortComments.observeAsState()

    val allComments = buildList {
        longComments?.comments?.let { addAll(it) }
        shortComments?.comments?.let { addAll(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新闻详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {
                            newsDetail?.let { data ->
                                val targetStory = data.stories?.firstOrNull { it.id == newsId }
                                targetStory?.let { story ->
                                    ShareUtil.shareNews(
                                        context,
                                        story.title?:"暂无信息",
                                        "https://daily.zhihu.com/story/${story.id}"
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.Share, "分享")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                    }
                },
                update = { webView ->
                    // 👇 就用你自己定义的 allNewsIds！！！
                    val targetStory = allNewsIds
                        .flatMap { it.stories.orEmpty() }
                        .firstOrNull { it.id == newsId }

                    // 完全用你自己的真实URL，不拼接！
                    targetStory?.url?.let {
                        webView.loadUrl(it)
                    }
                },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            if (allComments.isNotEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("评论", style = MaterialTheme.typography.titleMedium)
                    allComments.forEach { comment ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(comment.author ?: "匿名", style = MaterialTheme.typography.titleSmall)
                            Text(comment.content ?: "", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}