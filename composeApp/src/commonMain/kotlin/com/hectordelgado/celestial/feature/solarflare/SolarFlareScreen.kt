package com.hectordelgado.celestial.feature.solarflare

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.network.response.SolarFlareResponse

class SolarFlareScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SolarFlareScreenModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(true) {
            TopBarManager.resetStateTo {
                setIsVisible(true)
                setTitle("Solar Flare Data")
                setLeftIcon(TopBarLeftIcon.BACK)
            }
            viewModel.fetchSolarFlareData()
        }

        SolarFlareScreenContent(
            state,
            onBackClick = viewModel::fetchSolarFlareData,
            onForwardClick = viewModel::fetchSolarFlareData
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SolarFlareScreenContent(
    state: SolarFlareState,
    onBackClick: (Long) -> Unit,
    onForwardClick: (Long) -> Unit
) {
    Column {
        Text(
            state.headlinerTitle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.W400
        )

        LazyColumn(modifier = Modifier.background(Color.LightGray).fillMaxWidth().weight(1f)) {
            state.solarFlareSections.forEach { (headerTitle, solarFlareItems) ->
                stickyHeader {
                    Text(headerTitle, modifier = Modifier.fillMaxWidth().background(Color.LightGray))
                }

                items(solarFlareItems) {
                    SolarFlareItem(it)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { onBackClick(state.daysOffset + 1) } ,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
            }
            IconButton(
                onClick = { onForwardClick(state.daysOffset - 1) },
                modifier = Modifier.weight(1f),
                enabled = state.daysOffset > 0
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
            }
        }
    }
}

@Composable
fun SolarFlareItem(item: SolarFlareResponse) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Begin time: ${item.beginTime}", fontWeight = FontWeight.W400)
            Text("Peak time: ${item.peakTime}", fontWeight = FontWeight.W400)
            item.endTime?.let { Text("End time: $it", fontWeight = FontWeight.W400) }
            Text("Class type: ${item.classType}", fontWeight = FontWeight.W400)
            Text("Source location: ${item.sourceLocation}", fontWeight = FontWeight.W400)
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}
