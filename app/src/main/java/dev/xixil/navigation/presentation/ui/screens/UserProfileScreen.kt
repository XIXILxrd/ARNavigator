package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.User
import dev.xixil.navigation.presentation.ui.common.SmallPrimitiveButton
import dev.xixil.navigation.presentation.ui.common.SwitcherWithText
import dev.xixil.navigation.presentation.ui.common.TopNavigationBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.presentation.viewmodels.UserViewModel
import dev.xixil.navigation.presentation.viewmodels.ViewModelState

@Composable
fun UserProfileScreen(
    onSingOutPressed: () -> Unit,
) {
    ARNavigationTheme {
        ProfileScreen(
            onSingOutPressed = onSingOutPressed,
        )
    }
}

@Composable
fun ProfileScreen(
    onSingOutPressed: () -> Unit,
) {
    val viewModel: UserViewModel = hiltViewModel()

    val userData by produceState<ViewModelState<User>>(
        initialValue = ViewModelState.None(),
    ) {
        value = viewModel.getSignedUser().await()
    }

    when (val user = userData) {
        is ViewModelState.Error -> {
            SignInScreen {

            }
        }

        is ViewModelState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is ViewModelState.None -> {}
        is ViewModelState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopNavigationBar {
                    Text(
                        text = "Profile",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                AsyncImage(
                    model = user.data.photoUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.data.name,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                SwitcherWithText(
                    onChecked = { },
                    text = stringResource(id = R.string.admin_mode_text)
                )

                Spacer(modifier = Modifier.weight(1f))

                SmallPrimitiveButton(text = "Sign out", onClick = {
                    viewModel.signOut()
                    onSingOutPressed()
                })

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
