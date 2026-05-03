package com.example.myzhihudaily.ui.theme

/**
 * description:  ToDo:  实现首页Banner
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/3   13：06
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myzhihudaily.model.TopStory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Banner(topStories: List<TopStory>, onBannerClick: (Int) -> Unit) {
    if (topStories.isEmpty()) return
    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = Int.MAX_VALUE / 2
    )
    val coroutineScope = rememberCoroutineScope()
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    //自动轮播
    if (!isDragged) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                coroutineScope.launch {
                    val nextPage = pagerState.currentPage + 1
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
    )
    {
        HorizontalPager(state = pagerState) { page ->
            val realIndex = page % topStories.size
            val story = topStories[realIndex]

            // 整个的容器
            Box(
                modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = rememberAsyncImagePainter(story.image),
                    contentDescription = story.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onBannerClick(story.id) },
                    contentScale = ContentScale.Crop
                )

            //标题容器
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column{
                    Text(
                        text = story.title ?: "暂无标题信息",
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                        Text(
                            text = story.hint ?: "暂无作者信息",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                }
                }
            }
        }

        //右下角翻页示意小白点
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(topStories.size) { index ->
                val color = if (index == pagerState.currentPage % topStories.size)
                    Color.White
                else
                    Color.White.copy(alpha = 0.5f)

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        }
    }
}