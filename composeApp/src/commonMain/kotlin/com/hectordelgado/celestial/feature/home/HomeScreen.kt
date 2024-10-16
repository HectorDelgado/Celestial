package com.hectordelgado.celestial.feature.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import celestial.composeapp.generated.resources.Res
import celestial.composeapp.generated.resources.spiral_background
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDayScreen
import com.hectordelgado.celestial.feature.marsphotos.MarsPhotosScreen
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<HomeScreenModel>()

        LaunchedEffect(true) {
            TopBarManager.updateState {
                setIsVisible(false)
            }
        }

        SplashScreenContent(onNavigationRequested = viewModel::onNavigationRequested)
    }
}

@Composable
fun SplashScreenContent(
    onNavigationRequested: (Navigator, Screen) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val rotationAnimation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing),
            )
        )

        Image(
            painter = painterResource(Res.drawable.spiral_background),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.10f,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotationAnimation.value
                    scaleX = 2f
                    scaleY = 2f
                }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Celestial", fontSize = 24.sp, color = Color.White)

            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                OutlinedButton(
                    onClick = { onNavigationRequested(navigator, MarsPhotosScreen()) },
                    modifier = Modifier.fillMaxHeight().weight(1f)
                ) {
                    Text(
                        "Mars photos",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                OutlinedButton(
                    onClick = { onNavigationRequested(navigator, ImageOfTheDayScreen())},
                    modifier = Modifier.fillMaxHeight().weight(1f)
                ) {
                    Text(
                        "Image of the day",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}