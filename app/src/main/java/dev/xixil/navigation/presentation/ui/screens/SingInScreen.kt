package dev.xixil.navigation.presentation.ui.screens

import android.app.Activity.RESULT_OK
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.xixil.navigation.presentation.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit
) {
    val viewModel: UserViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    viewModel.singIn(
                        intent = result.data ?: return@launch
                    )
                }
            }
        }
    )

    val singInIntent by produceState<IntentSender?>(initialValue = null) {
        value = viewModel.intent.await()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            launcher.launch(
                IntentSenderRequest.Builder(
                    singInIntent ?: return@Button
                ).build()
            )
            onSignInSuccess()
        }) {
            Text(text = "Sign in")
        }
    }
}