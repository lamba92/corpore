package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.lamba92.corpore.common.core.toStringWithPrecision
import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.LengthUnit

@Composable
fun LengthTextField(
    length: Length = Length.ZERO,
    lengthUnit: LengthUnit,
    onValueChange: (Length) -> Unit,
    modifier: Modifier = Modifier,
    precision: Int = 2,
    trailingIcon: @Composable () -> Unit = defaultLengthTextFieldTailingIcon(),
    label: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value =
            length
                .to(lengthUnit)
                .toStringWithPrecision(precision),
        onValueChange = {
            it
                .toDoubleOrNull()
                ?.let { Length.from(it, lengthUnit) }
                ?.let(onValueChange)
        },
        label = label,
        trailingIcon = trailingIcon,
        keyboardOptions =
            KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Decimal,
            ),
    )
}

fun defaultLengthTextFieldTailingIcon(): @Composable () -> Unit =
    {
        ResourceImage(
            path = "files/icons/straighten_24dp.svg",
            contentDescription = "weight icon",
            modifier = Modifier.size(24.dp),
        )
    }
