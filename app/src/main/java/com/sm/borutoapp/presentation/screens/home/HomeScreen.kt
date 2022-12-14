package com.sm.borutoapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sm.borutoapp.navigation.Screen
import com.sm.borutoapp.presentation.common.ListContent
import com.sm.borutoapp.ui.theme.statusBarColor

@ExperimentalCoilApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController : NavHostController ,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val allHeroes = homeViewModel.getAllHeroes.collectAsLazyPagingItems()

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = MaterialTheme.colors.statusBarColor)

    Scaffold(
        topBar = {
            HomeTopBar(onSearchClicked = { navController.navigate(Screen.Search.route) })
        } ,
        content = {
            ListContent(heroes = allHeroes , navController = navController)
        }
    )

}