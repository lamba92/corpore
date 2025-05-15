package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IntTextField(
    value: Int = 0,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        enabled = enabled,
        value = value.toString(),
        onValueChange = { it.toIntOrNull()?.let(onValueChange) },
        trailingIcon = trailingIcon,
        label = label,
    )
}
