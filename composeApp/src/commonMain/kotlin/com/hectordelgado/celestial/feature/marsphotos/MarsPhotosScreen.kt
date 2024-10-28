package com.hectordelgado.celestial.feature.marsphotos

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import com.hectordelgado.celestial.actualexpect.OrientationType
import com.hectordelgado.celestial.actualexpect.getCurrentDeviceOrientation
import com.hectordelgado.celestial.feature.core.app.BaseScreen
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.network.dto.MarsPhoto
import com.hectordelgado.celestial.network.dto.MarsPhotosDto

class MarsPhotosScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<MarsPhotoScreenModel>()
        val state by viewModel.state.collectAsState()
        val contentState by viewModel.contentState.collectAsState()

        val selectedPhoto by remember(state.selectedMarsPhoto) {
            mutableStateOf(state.selectedMarsPhoto)
        }

        LaunchedEffect(true) {
            viewModel.onMarsPhotosRequested(0)
        }

        BaseScreen(contentState) {
            AnimatedContent(
                targetState = selectedPhoto,
                label = "transition_animation"
            ) { photo ->
                photo?.let {
                    MarsPhotoDetailScreen(
                        item = it,
                        onCloseDetails = { viewModel.onMarsPhotoSelected(null) }
                    )
                } ?: run {
                    MarsPhotosMainContent(
                        state = state,
                        onRoverSelected = viewModel::onRoverSelected,
                        onMarsPhotosRequested = viewModel::onMarsPhotosRequested,
                        onMarsPhotoClicked = viewModel::onMarsPhotoSelected,
                        onLoadNextPage = viewModel::onLoadNextPage
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarsPhotosMainContent(
    state: MarsPhotosState,
    onRoverSelected: (MarsPhotosDto.Rover) -> Unit,
    onMarsPhotosRequested: (Long) -> Unit,
    onMarsPhotoClicked: (MarsPhoto?) -> Unit,
    onLoadNextPage: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    val reachedBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 5
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            onLoadNextPage(state.page)
        }
    }

    Column {
        if (state.photos.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                state = listState
            ) {
                itemsIndexed(state.photos) { index, photo ->
                    Card(
                        onClick = {
                            TopBarManager.updateState {
                                setIsVisible(false)
                                setLeftIcon(TopBarLeftIcon.BACK)
                            }
                            onMarsPhotoClicked(photo)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Box {
                            AsyncImage(
                                model = photo.img_src,
                                contentDescription = "",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.height(150.dp)
                            )

                            Text(text = "#$index", modifier = Modifier.background(Color.LightGray))
                        }
                    }
                }

                if (state.isLoadingAdditionalPhotos) {
                    item {
                        Box(modifier = Modifier.background(Color.White).fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    "No photos available",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MarsPhotosDto.Rover.entries.forEach {
                    val fontWeight = if (state.selectedRover == it)
                        FontWeight.W800 else FontWeight.Normal
                    Text(
                        it.value.capitalize(Locale.current),
                        modifier = Modifier
                            .clickable { onRoverSelected(it) },
                        fontWeight = fontWeight
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                TextButton(
                    { onMarsPhotosRequested(state.daysOffset + 1) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }
                TextButton(
                    { onMarsPhotosRequested(state.daysOffset - 1) },
                    modifier = Modifier.weight(1f),
                    enabled = state.daysOffset > 0
                ) {
                    Text("Forward")
                }
            }
        }
    }
}

@Composable
private fun MarsPhotoDetailScreen(
    item: MarsPhoto,
    onCloseDetails: () -> Unit
) {
    val orientation = getCurrentDeviceOrientation()
    val scrollState = rememberScrollState()

    val containerModifier = if (orientation == OrientationType.LANDSCAPE) {
        Modifier
    } else {
        Modifier.verticalScroll(scrollState)
    }

    val imageModifier = if (orientation == OrientationType.LANDSCAPE) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxWidth()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(containerModifier)
    ) {
        Box() {
            AsyncImage(
                model = item.img_src,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier
            )
            IconButton(
                onClick = {
                    TopBarManager.updateState {
                        setIsVisible(true)
                        setLeftIcon(TopBarLeftIcon.BACK)
                    }
                    onCloseDetails()
                },
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp
                    )
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Icon(Icons.Default.Close, "")
            }
        }

        if (orientation != OrientationType.LANDSCAPE) {
            MarsPhotoDataField("Mars sol", item.sol)
            MarsPhotoDataField("Earth date", item.earth_date)
            MarsPhotoDataField("Camera", "[${item.camera.name}]")
            MarsPhotoDataField("Rover", item.rover.name)
        }
    }
}

@Composable
private fun MarsPhotoDataField(key: String, value: Any) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(key, modifier = Modifier.weight(1f), fontWeight = FontWeight.W800)
        Text(value.toString(), modifier = Modifier.padding(start = 16.dp))
    }
}
