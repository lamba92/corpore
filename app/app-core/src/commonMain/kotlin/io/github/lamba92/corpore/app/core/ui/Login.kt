@file:OptIn(ExperimentalResourceApi::class)

package io.github.lamba92.corpore.app.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.continue_with_apple
import io.github.lamba92.app_core.generated.resources.continue_with_google
import io.github.lamba92.app_core.generated.resources.login_error
import io.github.lamba92.app_core.generated.resources.login_footer_tos
import io.github.lamba92.app_core.generated.resources.login_footer_tos_link1
import io.github.lamba92.app_core.generated.resources.login_footer_tos_link2
import io.github.lamba92.app_core.generated.resources.login_subtitle
import io.github.lamba92.app_core.generated.resources.login_welcome
import io.github.lamba92.corpore.app.core.viewmodel.LoginScreenViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
) {
    val viewModel = viewModel<LoginScreenViewModel>()
    val isLoggingInUsingGoogle by viewModel.isLoggingInUsingGoogleStateFlow.collectAsState()
    val isLoggingInUsingApple by viewModel.isLoggingInUsingAppleStateFlow.collectAsState()
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(Res.string.login_error)
    LaunchedEffect(Unit) {
        viewModel
            .errorsFlow
            .onEach {
                errorSnackbarHostState.showSnackbar(
                    snackbarMessage,
                    withDismissAction = true
                )
            }
            .launchIn(this)
        viewModel
            .isLoggedInStateFlow
            .onEach { if (it) onLoginSuccess() }
            .launchIn(this)
    }

    LoginScreen(
        onLoginWithGoogle = viewModel::loginWithGoogle,
        onLoginWithApple = viewModel::loginWithApple,
        onTOSClicked = viewModel::onTOSClicked,
        isLoggingInUsingGoogle = isLoggingInUsingGoogle,
        isLoggingInUsingApple = isLoggingInUsingApple,
        errorSnackbarHostState = errorSnackbarHostState
    )
}

@Composable
fun LoginScreen(
    isLoggingInUsingGoogle: Boolean,
    isLoggingInUsingApple: Boolean,
    onLoginWithGoogle: () -> Unit = {},
    onLoginWithApple: () -> Unit = {},
    onTOSClicked: () -> Unit = {},
    errorSnackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        snackbarHost = { SnackbarHost(errorSnackbarHostState) },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = Res.getUri("files/corpore-logo.svg"),
                    contentDescription = "Corpore logo",
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = stringResource(Res.string.login_welcome),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = stringResource(Res.string.login_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GoogleLoginButton(
                    onClick = onLoginWithGoogle,
                    isLoggingIn = isLoggingInUsingGoogle
                )
                AppleLoginButton(
                    onClick = onLoginWithApple,
                    isLoggingIn = isLoggingInUsingApple
                )
                LoginFooterTOSText(
                    onTOSClicked = onTOSClicked,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun OAuthLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
    text: StringResource,
    icon: @Composable (() -> Unit),
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = !isLoggingIn
    ) {
        when {
            isLoggingIn -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else -> {
                icon()
                Text(stringResource(text))
            }
        }
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
) {
    OAuthLoginButton(
        onClick = onClick,
        isLoggingIn = isLoggingIn,
        text = Res.string.continue_with_google,
        icon = {
            AsyncImage(
                model = Res.getUri("files/google-g-logo.svg"),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
fun AppleLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
) {
    OAuthLoginButton(
        onClick = onClick,
        isLoggingIn = isLoggingIn,
        text = Res.string.continue_with_apple,
        icon = {
            AsyncImage(
                model = Res.getUri("files/apple-logo-white.svg.svg"),
                contentDescription = "Apple logo",
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
fun LoginFooterTOSText(
    onTOSClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = buildAnnotatedString {
        val link1 = stringResource(Res.string.login_footer_tos_link1)
        val link2 = stringResource(Res.string.login_footer_tos_link2)
        val fullString = stringResource(Res.string.login_footer_tos, link1, link2)
        addStyle(
            SpanStyle(color = MaterialTheme.colorScheme.secondary),
            start = fullString.indexOf(link1),
            end = fullString.indexOf(link1) + link1.length
        )
        addStyle(
            SpanStyle(color = MaterialTheme.colorScheme.secondary),
            start = fullString.indexOf(link2),
            end = fullString.indexOf(link2) + link2.length
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTOSClicked() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FooterText(text)
    }
}

@Composable
fun FooterText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
    )
}
