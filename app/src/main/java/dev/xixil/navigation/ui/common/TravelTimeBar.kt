package dev.xixil.navigation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R

@Preview
@Composable
private fun TravelTimeBarPreview() {
    TravelTimeBar(hours = 1, minutes = 13)
}

@Composable
fun TravelTimeBar(
    modifier: Modifier = Modifier,
    hours: Int,
    minutes: Int,
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                tint = Color(0xFFA09CAB),
                contentDescription = null
            )
            Text(
                text = if (hours <= 0) {
                    "$minutes " + stringResource(id = R.string.minunes_text)
                } else {
                    "$hours " + stringResource(id = R.string.hours_text) +
                            " $minutes " + stringResource(id = R.string.minunes_text)
                },
                color = Color(0xFFA09CAB)
            )
        }
    }
}