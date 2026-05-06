package com.example.myzhihudaily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myzhihudaily.ui.detail.DetailScreen
import com.example.myzhihudaily.ui.Main.HomeScreen
import com.example.myzhihudaily.ui.theme.MyZhihuDailyTheme
import com.example.myzhihudaily.viewmodel.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyZhihuDailyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}



@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    val allNewsIds = mainViewModel.newsList.value.flatMap { dayNews ->
        dayNews.stories?.map { it.id } ?: emptyList()
    }

    NavHost(navController = navController, startDestination = "home") {

        // 首页
        composable("home") {
            HomeScreen(onNewsClick = { id,date->
                navController.navigate("detail/$id/$date")
            })
        }

        // 详情页
        composable(
            route = "detail/{newsId}/{newsDate}",
            arguments = listOf(
                navArgument("newsId") { type = NavType.IntType },
                navArgument("newsDate") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getInt("newsId") ?: 0
            val newsDate = backStackEntry.arguments?.getString("newsDate") ?: ""

            DetailScreen(
                newsId = newsId,
                newsDate = newsDate,
                allNewsIds = allNewsIds,
                onBack = { navController.popBackStack() }
            )
        }
    }
}