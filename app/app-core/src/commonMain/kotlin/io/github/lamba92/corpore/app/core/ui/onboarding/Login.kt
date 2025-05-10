package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.app_name
import io.github.lamba92.app_core.generated.resources.onboarding_continue_with_apple
import io.github.lamba92.app_core.generated.resources.onboarding_continue_with_google
import io.github.lamba92.app_core.generated.resources.onboarding_login_error
import io.github.lamba92.app_core.generated.resources.onboarding_login_footer_tos
import io.github.lamba92.app_core.generated.resources.onboarding_login_footer_tos_link1
import io.github.lamba92.app_core.generated.resources.onboarding_login_footer_tos_link2
import io.github.lamba92.app_core.generated.resources.onboarding_login_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_login_welcome
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.core.viewmodel.LoginScreenViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
) {
    val isLoggingInUsingGoogle by viewModel.isLoggingInUsingGoogleStateFlow.collectAsState()
    val isLoggingInUsingApple by viewModel.isLoggingInUsingAppleStateFlow.collectAsState()
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(Res.string.onboarding_login_error)
    LaunchedEffect(Unit) {
        viewModel
            .errorsFlow
            .onEach {
                errorSnackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    withDismissAction = true,
                )
            }
            .launchIn(this)
        viewModel
            .isLoggedInStateFlow
            .onEach { if (it) onLoginSuccess() }
            .launchIn(this)
    }

    LoginScreen(
        onLoginWithGoogleClick = viewModel::loginWithGoogle,
        onLoginWithAppleClick = viewModel::loginWithApple,
        onTOSClicked = viewModel::onTOSClicked,
        isLoggingInUsingGoogle = isLoggingInUsingGoogle,
        isLoggingInUsingApple = isLoggingInUsingApple,
        errorSnackbarHostState = errorSnackbarHostState,
    )
}

@Composable
fun LoginScreen(
    isLoggingInUsingGoogle: Boolean,
    isLoggingInUsingApple: Boolean,
    onLoginWithGoogleClick: () -> Unit = {},
    onLoginWithAppleClick: () -> Unit = {},
    onTOSClicked: () -> Unit = {},
    errorSnackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(errorSnackbarHostState) },
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = CorporeTheme.colorScheme.primary)
                    .padding(all = CorporeTheme.appMetrics.outerPadding),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = Res.getUri("files/icons/corpore-logo.svg"),
                    contentDescription = "Corpore logo",
                    modifier = Modifier.size(100.dp),
                )
                Text(
                    text =
                        stringResource(
                            resource = Res.string.onboarding_login_welcome,
                            formatArgs = arrayOf(stringResource(resource = Res.string.app_name)),
                        ),
                    style = CorporeTheme.typography.headlineLarge,
                    color = CorporeTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = stringResource(Res.string.onboarding_login_subtitle),
                    style = CorporeTheme.typography.bodyLarge,
                    color = CorporeTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GoogleLoginButton(
                    onClick = onLoginWithGoogleClick,
                    isLoggingIn = isLoggingInUsingGoogle,
                    enabled = !isLoggingInUsingApple && !isLoggingInUsingGoogle,
                )
                AppleLoginButton(
                    onClick = onLoginWithAppleClick,
                    isLoggingIn = isLoggingInUsingApple,
                    enabled = !isLoggingInUsingApple && !isLoggingInUsingGoogle,
                )
                LoginFooterTOSText(
                    onTOSClicked = onTOSClicked,
                    modifier = Modifier.padding(top = 16.dp),
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
    buttonContainerColor: Color,
    textColor: Color,
    enabled: Boolean,
    icon: @Composable () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(buttonContainerColor),
        modifier = Modifier.width(250.dp),
        enabled = enabled,
    ) {
        when {
            isLoggingIn -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else -> {
                icon()
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(text),
                    color = textColor,
                )
            }
        }
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
    enabled: Boolean,
) {
    OAuthLoginButton(
        onClick = onClick,
        isLoggingIn = isLoggingIn,
        text = Res.string.onboarding_continue_with_google,
        buttonContainerColor = Color.White,
        textColor = Color.Black,
        enabled = enabled,
        icon = {
            AsyncImage(
                model = Res.getUri("files/icons/google-g-logo.svg"),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp),
            )
        },
    )
}

@Composable
fun AppleLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
    enabled: Boolean,
) {
    OAuthLoginButton(
        onClick = onClick,
        isLoggingIn = isLoggingIn,
        text = Res.string.onboarding_continue_with_apple,
        buttonContainerColor = Color.Black,
        textColor = Color.White,
        enabled = enabled,
        icon = {
            AsyncImage(
                model = Res.getUri("files/icons/apple-logo-white.svg"),
                contentDescription = "Apple logo",
                modifier = Modifier.size(24.dp),
            )
        },
    )
}

@Composable
fun LoginFooterTOSText(
    onTOSClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text =
        buildAnnotatedString {
            val link1 = stringResource(Res.string.onboarding_login_footer_tos_link1)
            val link2 = stringResource(Res.string.onboarding_login_footer_tos_link2)
            val fullString = stringResource(Res.string.onboarding_login_footer_tos, link1, link2)
            addStyle(
                SpanStyle(color = CorporeTheme.colorScheme.secondary),
                start = fullString.indexOf(link1),
                end = fullString.indexOf(link1) + link1.length,
            )
            addStyle(
                SpanStyle(color = CorporeTheme.colorScheme.secondary),
                start = fullString.indexOf(link2),
                end = fullString.indexOf(link2) + link2.length,
            )
        }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onTOSClicked() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
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
        color = Color.White,
        style = CorporeTheme.typography.labelSmall,
        modifier = modifier.border(2.dp, Color.Red),
    )
}
