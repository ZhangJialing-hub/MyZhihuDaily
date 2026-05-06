package com.example.myzhihudaily.ui.detail

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
@OptIn(ExperimentalMaterial3Api::class,ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    newsId: Int,
    allNewsIds: List<Int>,
    onBack: () -> Unit,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(newsId) {
        viewModel.loadDetail(newsId)
        viewModel.loadLongComments(newsId)
        viewModel.loadShortComments(newsId)
        viewModel.loadNewsExtraInfo(newsId)
    }

    val newsDetail by viewModel.newsDetail.observeAsState()
    val newsExtraInfo by viewModel.newsExtraInfo.observeAsState()
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
                                    story.title ?: "暂无信息",
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
                    newsDetail?.let { data ->
                        val targetStory = data.stories?.firstOrNull { it.id == newsId }
                        targetStory?.let { story ->
                            webView.loadUrl(story.url)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            // 新闻 总点赞、总评论数 展示
            newsExtraInfo?.let { detail ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "点赞 ${detail.popularity ?: 0}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "共 ${detail.comments ?: 0} 条评论",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (allComments.isNotEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "评论",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn (modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)){
                        items(allComments) { comment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                androidx.compose.foundation.layout.Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                ) {
                                    GlideImage(
                                        model = comment.avatar,
                                        contentDescription = "头像"
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        comment.author ?: "匿名",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        comment.content ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                    Text(
                                        text = formatTime(comment.time),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Text(
                                        text = "点赞 ${comment.likes ?: 0}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(timestamp: Long?): String {
    if (timestamp == null) return "时间未知"
    val sdf = java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp * 1000))
}
