package com.hectordelgado.celestial.feature.solarflare

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.hectordelgado.celestial.network.dto.SolarFlareDto

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

        SolarFlareScreenContent(state)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SolarFlareScreenContent(state: SolarFlareState) {

    val title by remember(state.headlinerTitle) { mutableStateOf(state.headlinerTitle) }

    Column {
        Text(
            title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.W400
        )

        Card(backgroundColor = Color.LightGray) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                state.solarFlareSections.forEach { (headerTitle, solarFlareItems) ->
                    stickyHeader {
                        Text(headerTitle, modifier = Modifier.fillMaxWidth().background(Color.LightGray))
                    }

                    items(solarFlareItems) {
                        SolarFlareItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun SolarFlareItem(item: SolarFlareDto) {
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
