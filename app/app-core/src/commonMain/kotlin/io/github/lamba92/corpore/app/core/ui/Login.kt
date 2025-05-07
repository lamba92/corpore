package io.github.lamba92.corpore.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.login_footer_tos
import io.github.lamba92.app_core.generated.resources.login_footer_tos_link1
import io.github.lamba92.app_core.generated.resources.login_footer_tos_link2
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onLoginWithGoogle: () -> Unit,
    onLoginWithApple: () -> Unit,
    onTOSClicked: () -> Unit,
    isLoggingInUsingGoogle: Boolean,
    isLoggingInUsingApple: Boolean,
    isError: Boolean,
) {

}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    isLoggingIn: Boolean,
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
                androidx.compose.material3.Icon(

                )
            }
        }
    }
}

@Composable
fun LoginFooterTOSText(
    onTOSClicked: () -> Unit,
) {
    val text = buildAnnotatedString {
        val link1 = stringResource(Res.string.login_footer_tos_link1)
        val link2 = stringResource(Res.string.login_footer_tos_link2)
        val fullString = stringResource(Res.string.login_footer_tos, link1, link2,)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onTOSClicked() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }
}