package dev.xixil.navigation.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = colors,
        shape = RoundedCornerShape(12),
        onClick = onClick
    ) {
        Text(text = stringResource(id = text), color = colors.contentColor)
    }
}

@Composable
fun NavigateFloatingActionButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier
            .clip(CircleShape)
            .wrapContentSize(),
        containerColor = colors.containerColor,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Outlined.NearMe,
            contentDescription = stringResource(R.string.find_location_content_desctiption_text)
        )
    }
}

@Composable
fun SmallPrimitiveButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .width(110.dp)
            .wrapContentHeight(),
        colors = colors,
        shape = RoundedCornerShape(12),
        onClick = onClick
    ) {
        Text(text = stringResource(id = text))
    }
}

@Preview
@Composable
private fun ButtonPreview() {
    Column {
        SmallPrimitiveButton(text = R.string.home_content_description_text) {

        }

        PrimaryButton(text = R.string.profile_content_description_text) {

        }

        NavigateFloatingActionButton {

        }
    }
}