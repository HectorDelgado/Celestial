package com.hectordelgado.celestial.feature.imageoftheday

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.hectordelgado.celestial.feature.core.app.BaseScreen
import kotlinx.coroutines.launch

class ImageOfTheDayScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<ImageOfTheDayScreenModel>()
        val state by viewModel.state.collectAsState()
        val contentState by viewModel.contentState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchFavoritePictures()
            viewModel.fetchPictureOfTheDay(0L)
        }

        BaseScreen(contentState) {
            ImageOfTheDayScreenContent(
                state = state,
                fetchPictureOfTheDay = viewModel::fetchPictureOfTheDay,
                onFavoriteClick = viewModel::onFavoriteClick,
                onViewFavoritesClicked = viewModel::onViewFavoritesClicked
            )
        }
    }
}

@Composable
fun ImageOfTheDayScreenContent(
    state: ImageOfTheDayState,
    fetchPictureOfTheDay: (Long) -> Unit,
    onFavoriteClick: (ImageOfTheDay) -> Unit,
    onViewFavoritesClicked: (Navigator, Screen) -> Unit
) {
    var imageOfTheDay by remember {
        mutableStateOf<ImageOfTheDay?>(null)
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val navigator = LocalNavigator.currentOrThrow

    // Make sure we don't scroll after isSaved property updates
    LaunchedEffect(state.imageOfTheDay) {
        if (imageOfTheDay?.title != state.imageOfTheDay?.title) {
            scrollState.scrollTo(0)
        }

        imageOfTheDay = state.imageOfTheDay
    }

    Column {
        state.imageOfTheDay?.let {
            ImageOfTheDay(it, Modifier.weight(1f).verticalScroll(scrollState), onFavoriteClick)
        }

        Column(modifier = Modifier) {
            Divider(modifier = Modifier.fillMaxWidth())
            TextButton(
                onClick = {
                    onViewFavoritesClicked(navigator, FavoriteImagesOfTheDayScreen())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Favorites")
            }
            Divider(modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = {
                        fetchPictureOfTheDay(state.daysOffset + 1)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Previous")
                }
                TextButton(
                    onClick =  {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                            fetchPictureOfTheDay(state.daysOffset - 1)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = state.daysOffset > 0
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun ImageOfTheDay(
    item: ImageOfTheDay,
    modifier: Modifier = Modifier,
    onFavoriteClick: (ImageOfTheDay) -> Unit = {}
) {
    val imageRequest = ImageRequest
        .Builder(LocalPlatformContext.current)
        .data(item.imageUrl)
        .build()
    val uriHandler = LocalUriHandler.current
    val scaleAnimation = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(item.isSaved) {
        scaleAnimation.animateTo(1.5f)
        scaleAnimation.animateTo(1f)
    }

    Column(modifier = modifier) {
        Text(
            item.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(item.displayDate, fontSize = 11.sp)

        if (item.mediaType == "video") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                TextButton({uriHandler.openUri(item.imageUrl)}) {
                    Text(text = "See video in external app")
                }
            }
        } else {
            AsyncImage(
                imageRequest,
                "",
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.FillWidth
            )
        }

        Text(item.explanation, modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Copyright: ${item.copyright}",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp
            )

            val iconTint = if (item.isSaved)
                Color.Green
            else
                Color.LightGray
            val iconBorderTint = if (item.isSaved)
                Color.Black
            else Color.LightGray


            Icon(
                Icons.Outlined.Star,
                "",
                tint = iconTint,
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 16.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, iconBorderTint, RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onFavoriteClick(item) }
                    )
                    .graphicsLayer {
                        scaleX = scaleAnimation.value
                        scaleY = scaleAnimation.value
                    }
            )
        }
    }
}
