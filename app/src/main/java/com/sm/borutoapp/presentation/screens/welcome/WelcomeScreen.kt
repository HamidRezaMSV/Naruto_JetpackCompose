package com.sm.borutoapp.presentation.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import com.sm.borutoapp.R
import com.sm.borutoapp.domain.model.OnBoardingPage
import com.sm.borutoapp.navigation.Screen
import com.sm.borutoapp.ui.theme.*
import com.sm.borutoapp.util.Constants.LAST_ON_BOARDING_PAGE
import com.sm.borutoapp.util.Constants.ON_BOARDING_PAGE_COUNT

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun WelcomeScreen(
    navController:NavHostController ,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
    ) {

    val pages = listOf(
        OnBoardingPage.First ,
        OnBoardingPage.Second ,
        OnBoardingPage.Third
    )

    val pagerState = rememberPagerState()
    
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.welcomeScreenBackgroundColor) ,
        verticalArrangement = Arrangement.Center
    ){
        HorizontalPager(
            modifier = Modifier.weight(10f) ,
            state = pagerState ,
            count = ON_BOARDING_PAGE_COUNT ,
            verticalAlignment = Alignment.Top
        ) { position -> PagerScreen(onBoardingPage = pages[position]) }

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f) ,
            pagerState = pagerState ,
            activeColor = MaterialTheme.colors.activeIndicatorColor ,
            inactiveColor = MaterialTheme.colors.inactiveIndicatorColor ,
            indicatorWidth = PAGING_INDICATOR_WIDTH,
            spacing = PAGING_INDICATOR_SPACING ,
        )

        FinishButton(modifier = Modifier.weight(1f),pagerState = pagerState) {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
            welcomeViewModel.saveOnBoardingState(completed = true)
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f) ,
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = stringResource(R.string.on_boarding_image)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = onBoardingPage.title ,
            color = MaterialTheme.colors.titleColor ,
            fontSize = MaterialTheme.typography.h4.fontSize ,
            fontWeight = FontWeight.Bold ,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = EXTRA_LARGE_PADDING)
                .padding(top = SMALL_PADDING),
            text = onBoardingPage.description ,
            color = MaterialTheme.colors.descriptionColor ,
            fontSize = MaterialTheme.typography.subtitle1.fontSize ,
            fontWeight = FontWeight.Medium ,
            textAlign = TextAlign.Center
        )
    }
}


@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FinishButton(modifier: Modifier , pagerState: PagerState , onClick : () -> Unit) {
    Row(
        modifier = modifier.padding(horizontal = EXTRA_LARGE_PADDING) ,
        verticalAlignment = Alignment.Top ,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = modifier.fillMaxWidth() ,
            visible = pagerState.currentPage == LAST_ON_BOARDING_PAGE) {
                Button(
                    onClick = onClick ,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =  MaterialTheme.colors.finishButtonBackgroundColor,
                        contentColor = Color.White
                    )) {
                        Text(text = stringResource(R.string.finish))
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Preview(showBackground = true)
@Composable
fun SecondOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Second)
    }
}

@Preview(showBackground = true)
@Composable
fun ThirdOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Third)
    }
}