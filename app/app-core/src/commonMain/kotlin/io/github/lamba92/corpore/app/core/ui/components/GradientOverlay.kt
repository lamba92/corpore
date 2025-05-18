package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class GradientDirection {
    TopToBottom,
    BottomToTop,
}

fun Modifier.gradientOverlay(
    color: Color,
    direction: GradientDirection,
    initialAlpha: Float = 0f,
    endAlpha: Float = 1f,
) = this.then(
    Modifier.drawBehind {
        val gradientStops =
            when (direction) {
                GradientDirection.TopToBottom ->
                    arrayOf(
                        0.0f to color.copy(alpha = initialAlpha),
                        0.3f to color.copy(alpha = endAlpha),
                        1.0f to color.copy(alpha = endAlpha),
                    )

                GradientDirection.BottomToTop ->
                    arrayOf(
                        0.0f to color.copy(alpha = endAlpha),
                        0.7f to color.copy(alpha = endAlpha),
                        1.0f to color.copy(alpha = initialAlpha),
                    )
            }

        drawRect(
            brush = Brush.verticalGradient(colorStops = gradientStops),
            blendMode = BlendMode.SrcOver,
        )
    },
)
