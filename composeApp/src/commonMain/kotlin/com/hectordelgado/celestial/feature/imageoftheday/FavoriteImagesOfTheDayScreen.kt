package com.hectordelgado.celestial.feature.imageoftheday

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.hectordelgado.celestial.feature.core.app.BaseScreen
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager

class FavoriteImagesOfTheDayScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<FavoriteImagesOfTheDayScreenModel>()
        val state by viewModel.state.collectAsState()
        val contentState by viewModel.contentState.collectAsState()

        LaunchedEffect(Unit) {
            TopBarManager.resetStateTo {
                setTitle("Favorites")
                setLeftIcon(TopBarLeftIcon.CLOSE)
                setIsVisible(true)
            }
        }

        LaunchedEffect(state.selectedPreview) {
            TopBarManager.updateState {
                setIsVisible(state.selectedPreview == null)
            }

            if (state.selectedPreview == null) {
                viewModel.loadFavorites()
            }
        }

        BaseScreen(contentState) {
            if (state.previews.isNotEmpty()) {
                FavoriteImagesOfTheDayScreenContent(
                    state = state,
                    onItemClick = viewModel::onItemClick,
                    onFavoriteClick = viewModel::onFavoriteClick
                )
            } else {
                Text(text = "No favorites found", modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FavoriteImagesOfTheDayScreenContent(
    state: FavoriteImagesOfTheDayState,
    onItemClick: (ImageOfTheDay?) -> Unit,
    onFavoriteClick: (ImageOfTheDay) -> Unit
) {
    val showDetail by remember(state.selectedPreview) {
        mutableStateOf(state.selectedPreview != null)
    }

    Crossfade(showDetail) {
        if (it) {
            state.selectedPreview?.let {
                FavoriteImageDetailView(
                    item = it,
                    onClosePreview = { onItemClick(null) },
                    onFavoriteClick = onFavoriteClick
                )

            }
        } else {
            FavoriteImagesListView(state.previews, onItemClick)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FavoriteImagesListView(
    previews: List<ImageOfTheDay>,
    onItemClick: (ImageOfTheDay) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(previews) { preview ->
            Card(
                onClick = { onItemClick(preview) },
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp
            ) {
                Box {
                    val imageLoader = ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(preview.imageUrl)
                        .build()
                    AsyncImage(
                        imageLoader,
                        "",
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = preview.title,
                            modifier = Modifier
                                .background(
                                    Color.LightGray.copy(alpha = 0.25f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(start = 4.dp, top = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteImageDetailView(
    item: ImageOfTheDay,
    onClosePreview: () -> Unit,
    onFavoriteClick: (ImageOfTheDay) -> Unit
) {
    Column {
        ImageOfTheDay(
            item = item,
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            onFavoriteClick = onFavoriteClick
        )

        Divider(modifier = Modifier.fillMaxWidth())
        TextButton(onClick = { onClosePreview() }, modifier = Modifier.padding(start = 16.dp)) {
            Text(text = "Close Preview")
        }
    }
}


