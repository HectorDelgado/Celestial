package com.hectordelgado.celestial.feature.imageoftheday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager

class ImageOfTheDayScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<ImageOfTheDayScreenModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            TopBarManager.resetStateTo {
                setIsVisible(true)
                setTitle("Image of the day")
                setLeftIcon(TopBarLeftIcon.BACK)
            }
            viewModel.fetchFavoritePictures()
            viewModel.fetchPictureOfTheDay(0L)
        }

        ImageOfTheDayScreenContent(
            state = state,
            fetchPictureOfTheDay = viewModel::fetchPictureOfTheDay,
            onFavoriteClick = viewModel::onFavoriteClick
        )
    }
}

@Composable
fun ImageOfTheDayScreenContent(
    state: ImageOfTheDayState,
    fetchPictureOfTheDay: (Long) -> Unit,
    onFavoriteClick: (ImageOfTheDay) -> Unit
) {
    val scrollState = rememberScrollState()
    Column {
        Column(modifier = Modifier.weight(1f).verticalScroll(scrollState)) {
            state.imageOfTheDay?.let {
                Text(
                    it.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(it.displayDate, fontSize = 11.sp)

                val imageLoader = ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(it.imageUrl)
                    .build()
                AsyncImage(
                    imageLoader,
                    "",
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.FillWidth
                )

                Text(it.explanation, modifier = Modifier.padding(16.dp))
                Row() {
                    Text(
                        "Copyright: ${it.copyright}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp, bottom = 16.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp
                    )
                    IconButton(onClick = {onFavoriteClick(it)}) {
                        val tint = if (it.isSaved) Color.Cyan else Color.Black
                        Icon(Icons.Filled.Star, "", tint = tint)
                    }
                }

            }
        }
        if (state.imageOfTheDay != null) {
            Column() {
                Divider(modifier = Modifier.fillMaxWidth())
                Row {
                    TextButton(
                        onClick = { fetchPictureOfTheDay(state.daysOffset + 1) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Previous")
                    }
                    TextButton(
                        onClick =  { fetchPictureOfTheDay(state.daysOffset - 1) },
                        modifier = Modifier.weight(1f),
                        enabled = state.daysOffset > 0
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}
