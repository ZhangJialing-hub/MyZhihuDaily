package com.example.myzhihudaily.utils

/**
 * description:   ToDo:分享工具
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2   18：19
 */

import android.content.Context
import android.content.Intent
object ShareUtil{
    fun shareNews(context:Context,title:String,url:String){
        val intent=Intent(Intent.ACTION_SEND).apply{
            type= "text/plain"
            putExtra(Intent.EXTRA_TEXT,
                "$title\n$url")
        }
        context.startActivity(Intent.createChooser(intent,
            "分享到"))
    }
}