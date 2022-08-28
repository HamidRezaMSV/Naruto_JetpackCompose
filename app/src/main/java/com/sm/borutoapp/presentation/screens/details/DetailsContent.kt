package com.sm.borutoapp.presentation.screens.details

import android.graphics.Color.parseColor
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sm.borutoapp.R
import com.sm.borutoapp.domain.model.Hero
import com.sm.borutoapp.presentation.components.InfoBox
import com.sm.borutoapp.presentation.components.OrderedList
import com.sm.borutoapp.ui.theme.*
import com.sm.borutoapp.util.Constants.ABOUT_TEXT_MAX_LINES
import com.sm.borutoapp.util.Constants.BASE_URL
import com.sm.borutoapp.util.Constants.MIN_BACKGROUND_IMAGE_HEIGHT

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun DetailsContent(
    navController: NavHostController,
    selectedHero: Hero? ,
    colors : Map<String,String>
) {

    var vibrant by remember { mutableStateOf("#000000") }
    var darkVibrant by remember { mutableStateOf("#000000") }
    var onDarkVibrant by remember { mutableStateOf("#FFFFFF") }

    LaunchedEffect(key1 = selectedHero){
        vibrant = colors["vibrant"]!!
        darkVibrant = colors["darkVibrant"]!!
        onDarkVibrant = colors["onDarkVibrant"]!!
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(parseColor(darkVibrant)))

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )

    val currentSheetFraction = scaffoldState.currentSheetFraction
    
    val radiusAnim by animateDpAsState(
        targetValue = if (currentSheetFraction == 1f){ EXTRA_LARGE_PADDING
        }else{ EXPANDED_RADIUS_LEVEL }
    )

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(topEnd = radiusAnim, topStart = radiusAnim) ,
        scaffoldState = scaffoldState ,
        sheetPeekHeight = MIN_SHEET_HEIGHT ,
        sheetContent = { selectedHero?.let { BottomSheetContent(
            selectedHero = it ,
            infoBoxIconColor = Color(parseColor(vibrant)) ,
            sheetBackgroundColor = Color(parseColor(darkVibrant)) ,
            contentColor = Color(parseColor(onDarkVibrant))
        ) } },
        content = {
            selectedHero?.let { hero ->
                BackgroundContent(
                    heroImage = hero.image ,
                    imageFraction = currentSheetFraction ,
                    onCloseClicked = { navController.popBackStack() } ,
                    backgroundColor = Color(parseColor(darkVibrant))
                )
            }
        }
    )

}

@Composable
fun BottomSheetContent(
    selectedHero : Hero ,
    infoBoxIconColor : Color = MaterialTheme.colors.primary ,
    sheetBackgroundColor : Color = MaterialTheme.colors.surface ,
    contentColor : Color = MaterialTheme.colors.titleColor
) {

    Column(
        modifier = Modifier
            .background(sheetBackgroundColor)
            .padding(all = LARGE_PADDING)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LARGE_PADDING) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(INFO_ICON_SIZE)
                    .weight(2f) ,
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(R.string.app_logo) ,
                tint = contentColor
            )
            Text(
                modifier = Modifier.weight(8f) ,
                text = selectedHero.name ,
                color = contentColor ,
                fontSize = MaterialTheme.typography.h4.fontSize ,
                fontWeight = FontWeight.Bold
            )
        }
        
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MEDIUM_PADDING) ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(
                icon = painterResource(id = R.drawable.ic_bolt),
                iconColor = infoBoxIconColor,
                bigText = "${selectedHero.power}" ,
                smallText = stringResource(R.string.power),
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.ic_calendar),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.month ,
                smallText = stringResource(R.string.month),
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.ic_cake),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.day ,
                smallText = stringResource(R.string.birthday),
                textColor = contentColor
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = stringResource(R.string.about) ,
            fontSize = MaterialTheme.typography.subtitle1.fontSize ,
            color = contentColor ,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .padding(bottom = MEDIUM_PADDING) ,
            text = selectedHero.about ,
            fontSize = MaterialTheme.typography.body1.fontSize ,
            color = contentColor ,
            maxLines = ABOUT_TEXT_MAX_LINES
        )

        Row(
            modifier = Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OrderedList(
                title = stringResource(R.string.family),
                items = selectedHero.family,
                textColor = contentColor
            )
            OrderedList(
                title = stringResource(R.string.abilities),
                items = selectedHero.abilities,
                textColor = contentColor
            )
            OrderedList(
                title = stringResource(R.string.nature_types),
                items = selectedHero.natureTypes,
                textColor = contentColor
            )
        }
    }

}

@ExperimentalCoilApi
@Composable
fun BackgroundContent(
    heroImage : String ,
    imageFraction : Float = 1f ,
    backgroundColor : Color = MaterialTheme.colors.surface ,
    onCloseClicked : () -> Unit
) {

    val imageUrl = "$BASE_URL$heroImage"
    val painter = rememberImagePainter(imageUrl){ error(R.drawable.ic_placeholder) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = imageFraction + MIN_BACKGROUND_IMAGE_HEIGHT)
                .align(Alignment.TopStart) ,
            painter = painter,
            contentDescription = stringResource(id = R.string.hero_image),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier.padding(all = SMALL_PADDING) ,
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    modifier = Modifier.size(INFO_ICON_SIZE) ,
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_icon) ,
                    tint = Color.White
                )
            }
        }
    }

}

@ExperimentalMaterialApi
val BottomSheetScaffoldState.currentSheetFraction : Float
    get() {
        val fraction = bottomSheetState.progress.fraction
        val targetValue = bottomSheetState.targetValue
        val currentValue = bottomSheetState.currentValue

        val collapsed = BottomSheetValue.Collapsed
        val expanded = BottomSheetValue.Expanded

        return when{
            currentValue == collapsed && targetValue == collapsed -> 1f
            currentValue == expanded && targetValue == expanded -> 0f
            currentValue == collapsed && targetValue == expanded -> 1f - fraction
            currentValue == expanded && targetValue == collapsed -> 0f + fraction
            else -> fraction
        }
    }

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    BottomSheetContent(selectedHero = Hero(
        id = 1,
        name = "Orochimaru",
        image = "",
        about = "Orochimaru is a fictional character in the Naruto manga and anime series created by Masashi Kishimoto. He is one of the main negative characters in the story. Orochimaru, known as Ninja 002300, was one of Sarutobi Hirozen or Hokage's third disciples on his three-member team, which included himself, Jiraiya, and Sonadeh, who later became known as the legendary Sanins.",
        rating = 3.8,
        power = 85,
        month = "Jan",
        day = "10rd",
        family = listOf("Kizashi" , "Mebuki" , "Sarada" , "Sasuke"),
        abilities = listOf("Chakra control" , "Strength" , "Intelligence"),
        natureTypes = listOf("Earth" , "Fire" , "water")
    ))
}