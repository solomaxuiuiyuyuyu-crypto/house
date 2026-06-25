package com.example.house_rental_app.theme.screens.menuscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.house_rental_app.ApiKeyFactory
import com.example.house_rental_app.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val apiKey = ApiKeyFactory.getApiKey(context)
    val keyMissing = apiKey.isBlank() || apiKey == "YOUR_YANDEX_MAPKIT_API_KEY"
    var initError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.map_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = when {
                keyMissing -> stringResource(R.string.yandex_map_key_missing)
                initError -> stringResource(R.string.map_init_error)
                else -> stringResource(R.string.map_hint)
            },
            style = MaterialTheme.typography.bodyMedium
        )

        Card(modifier = Modifier.fillMaxSize()) {
            if (keyMissing || initError) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when {
                            keyMissing -> stringResource(R.string.yandex_map_key_missing)
                            initError -> stringResource(R.string.map_init_error)
                            else -> stringResource(R.string.map_init_error)
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                val mapView = remember {
                    try {
                        MapView(context).apply {
                            mapWindow.map.move(
                                CameraPosition(Point(55.751244, 37.618423), 11.0f, 0.0f, 0.0f)
                            )
                        }
                    } catch (_: Exception) {
                        initError = true
                        null
                    }
                }

                if (mapView != null) {
                    DisposableEffect(mapView) {
                        MapKitFactory.getInstance().onStart()
                        mapView.onStart()
                        onDispose {
                            mapView.onStop()
                            MapKitFactory.getInstance().onStop()
                        }
                    }

                    AndroidView(
                        modifier = Modifier.fillMaxWidth().fillMaxSize(),
                        factory = { mapView }
                    )
                }
            }
        }
    }
}
