package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.utils.WeightUnit
import io.github.lamba92.corpore.app.core.utils.toStringWithPrecision

@Composable
fun WeightTextField(
    weight: Weight = Weight.ZERO,
    weightUnit: WeightUnit,
    onValueChange: (Weight) -> Unit,
    modifier: Modifier = Modifier,
    precision: Int = 2,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    label: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = defaultWeightTextFieldIcon(),
) {
    OutlinedTextField(
        modifier = modifier,
        value =
            weight
                .to(weightUnit)
                .toStringWithPrecision(precision),
        onValueChange = {
            it
                .toDoubleOrNull()
                ?.let { Weight.from(it, weightUnit) }
                ?.let(onValueChange)
        },
        label = label,
        colors = colors,
        trailingIcon = trailingIcon,
        keyboardOptions =
            KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Decimal,
            ),
    )
}

fun defaultWeightTextFieldIcon(): @Composable () -> Unit =
    {
        ResourceImage(
            path = "files/icons/weight_24dp.svg",
            contentDescription = "weight icon",
            modifier = Modifier.size(24.dp),
        )
    }

@Composable
fun defaultTextFieldLabel(text: String): @Composable () -> Unit =
    {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
        )
    }
