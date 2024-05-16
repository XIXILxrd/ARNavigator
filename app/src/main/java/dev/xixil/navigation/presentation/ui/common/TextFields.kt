package dev.xixil.navigation.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R

@Preview
@Composable
private fun SmallTextFieldPreview() {
    SmallTextField(placeholder = stringResource(id = R.string.from_text)) {
        ""
    }
}

@Composable
fun LargeTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    onClick: () -> String,
) {
    var text by rememberSaveable { mutableStateOf(placeholder) }

    Surface(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        color = Color.White,
        border = BorderStroke(2.dp, Color(0xFFEFF1F5)),
        contentColor = Color(0xFFA09CAB),
        onClick = { text = onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            maxLines = 1
        )
    }
}


@Composable
fun SmallTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    onClick: () -> String,
) {
    var text by rememberSaveable { mutableStateOf(placeholder) }

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color(0xFFEFF1F5),
        contentColor = Color(0xFFA09CAB),
        onClick = { text = onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            maxLines = 1
        )
    }
}