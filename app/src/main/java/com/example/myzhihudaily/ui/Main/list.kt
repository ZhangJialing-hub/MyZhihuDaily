package com.example.myzhihudaily.ui.Main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myzhihudaily.model.Story
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsListItem(
    story: Story,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = story.images?.firstOrNull(),
            contentDescription = story.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = story.title?:"暂无信息",
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )

        IconButton(onClick = onShare) {
            Icon(
                Icons.Outlined.Share,
                contentDescription = "分享"
            )
        }
    }
}
