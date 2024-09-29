package com.hectordelgado.celestial.feature.marsphotos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import com.hectordelgado.celestial.feature.core.app.ScreenContent
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.network.response.MarsPhoto

class MarsPhotosScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<MarsPhotoScreenModel>()
        val state by viewModel.state.collectAsState()
        val screenState by viewModel.screenState.collectAsState()

        LaunchedEffect(true) {
            TopBarManager.updateState {
                setLeftIcon(TopBarLeftIcon.BACK)
            }
            viewModel.onMarsPhotosRequested(0)
        }

        ScreenContent(screenState) {
            MarsPhotosContent(state, viewModel::onMarsPhotosRequested)
        }
    }
}

@Composable
fun MarsPhotosContent(
    state: MarsPhotosState,
    onMarsPhotosRequested: (Long) -> Unit
) {
    Column {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            items(state.photos) { photo ->
                MarsPhotoItem(photo)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
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

@Composable
private fun MarsPhotoItem(item: MarsPhoto) {
    Box {
        AsyncImage(
            model = item.img_src,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        )
        Text(item.camera.full_name, modifier = Modifier.fillMaxSize().align(Alignment.BottomCenter))
    }
}