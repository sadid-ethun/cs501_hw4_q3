package com.example.cs501_hw4_q3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cs501_hw4_q3.ui.theme.Cs501_hw4_q3Theme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cs501_hw4_q3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TrailApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TrailApp(modifier: Modifier = Modifier) {
    val trailPoints = remember {
        listOf(
            LatLng(40.7716, -73.9742),
            LatLng(40.7730, -73.9715),
            LatLng(40.7750, -73.9680),
            LatLng(40.7772, -73.9655),
            LatLng(40.7795, -73.9635)
        )
    }

    val parkPoints = remember {
        listOf(
            LatLng(40.8007, -73.9583),
            LatLng(40.7968, -73.9498),
            LatLng(40.7642, -73.9730),
            LatLng(40.7681, -73.9817)
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.7812, -73.9665), 13.5f)
    }

    var colorPreset by rememberSaveable { mutableIntStateOf(0) }
    var trailWidth by rememberSaveable { mutableFloatStateOf(8f) }
    var parkBorderWidth by rememberSaveable { mutableFloatStateOf(4f) }
    var infoText by rememberSaveable { mutableStateOf("Tap the trail or park") }

    val trailColor =
        when (colorPreset) {
            0 -> Color.Blue
            1 -> Color.Red
            else -> Color.Magenta
        }

    val parkColor =
        when (colorPreset) {
            0 -> Color.Green.copy(alpha = 0.3f)
            1 -> Color.Yellow.copy(alpha = 0.3f)
            else -> Color.Cyan.copy(alpha = 0.3f)
        }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { colorPreset = 0 }) {
                Text("Blue/Green")
            }

            Button(onClick = { colorPreset = 1 }) {
                Text("Red/Yellow")
            }

            Button(onClick = { colorPreset = 2 }) {
                Text("Pink/Cyan")
            }
        }

        Text(
            text = "Trail Width: ${trailWidth.toInt()}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Slider(
            value = trailWidth,
            onValueChange = { trailWidth = it },
            valueRange = 4f..20f,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = "Park Border Width: ${parkBorderWidth.toInt()}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Slider(
            value = parkBorderWidth,
            onValueChange = { parkBorderWidth = it },
            valueRange = 2f..20f,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = infoText,
            modifier = Modifier.padding(8.dp)
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Polyline(
                points = trailPoints,
                color = trailColor,
                width = trailWidth,
                clickable = false
            )

            Polygon(
                points = parkPoints,
                fillColor = parkColor,
                strokeColor = trailColor,
                strokeWidth = parkBorderWidth,
                clickable = true,
                onClick = {
                    infoText = "Area: Central Park"
                }
            )

            Marker(
                state = MarkerState(position = trailPoints.first()),
                title = "Trail Start"
            )
        }
    }
}