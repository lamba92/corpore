package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Scope for defining items within the [ToggleButton].
 */
@LayoutScopeMarker
@Immutable
interface ToggleButtonScope {
    /**
     * Adds an item to the toggle button.
     *
     * @param content The composable content for this item. It receives:
     *  - `isSelected`: A boolean indicating if this item is currently selected.
     *  - `defaultAnimationDurationMillis`: The animation duration configured for the toggle,
     *    which can be used for consistent animations within the item's content (e.g., text color).
     */
    fun item(content: ToggleButtonItem)
}

fun interface ToggleButtonItem {
    /**
     * The content of the toggle button item.
     *
     * @param isSelected Indicates if this item is currently selected.
     */
    @Composable
    operator fun invoke(isSelected: Boolean)
}

private class ToggleButtonScopeImpl : ToggleButtonScope {
    private val items: MutableList<ToggleButtonItem> = mutableListOf()

    fun getItems(): List<ToggleButtonItem> = items.toList()

    override fun item(content: ToggleButtonItem) {
        items.add(content)
    }

    fun clear() {
        items.clear()
    }
}

/**
 * Toggle button that allows defining multiple items using a DSL.
 * The selection indicator slides under the currently selected item.
 *
 * @param selectedIndex The index of the currently selected item.
 * @param onItemSelect Callback invoked when an item is selected, providing its index.
 * @param modifier Optional [Modifier] for this component.
 * @param cornerRadius The corner radius for the toggle button's background.
 * @param animationDuration Duration for the sliding and content animations.
 * @param containerColor The background color of the toggle button itself.
 * @param selectedIndicatorColor The color of the sliding selection indicator.
 * @param content A block defining the items for the toggle button using [ToggleButtonScope.item].
 */
@Composable
fun ToggleButton(
    selectedIndex: Int,
    onItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: CornerBasedShape = MaterialTheme.shapes.extraLarge,
    animationDuration: Duration = 300.milliseconds,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    content: ToggleButtonScope.() -> Unit,
) {
    // Create and remember the scope, populate items
    val items =
        remember(content) { ToggleButtonScopeImpl().apply(content) }.getItems()

    if (items.isEmpty()) {
        // Optionally, render a placeholder or throw an error if no items are defined.
        // For now, just don't render anything if empty.
        return
    }

    val density = LocalDensity.current

    // Ensure selectedIndex is valid
    val actualSelectedIndex = selectedIndex.coerceIn(0, items.size - 1)

    var componentWidth by remember { mutableStateOf(0.dp) }

    val itemWidths = remember { mutableStateMapOf<Int, DpSize>() }
    val selectedItemSize by derivedStateOf {
        if (itemWidths.isEmpty()) DpSize.Zero else itemWidths[actualSelectedIndex] ?: DpSize.Zero
    }

    val indicatorOffsetPx by animateFloatAsState(
        targetValue =
            when {
                componentWidth > 0.dp ->
                    with(density) {
                        var widthInPx = 0f
                        repeat(selectedIndex) { index ->
                            widthInPx += itemWidths[index]?.width?.toPx() ?: 0f
                        }
                        widthInPx
                    }

                else -> 0f
            },
        animationSpec = tween(durationMillis = animationDuration.inWholeMilliseconds.toInt()),
        label = "IndicatorOffset",
    )

    Box(
        modifier =
            modifier
                .onSizeChanged { componentWidth = with(density) { it.width.toDp() } }
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius.topStart,
                        topEnd = cornerRadius.topEnd,
                        bottomStart = cornerRadius.bottomStart,
                        bottomEnd = cornerRadius.bottomEnd,
                    ),
                )
                .background(containerColor),
    ) {
        // Selected state indicator (the moving background)
        if (selectedItemSize != DpSize.Zero) {
            Box(
                modifier =
                    Modifier
                        .offset { IntOffset(indicatorOffsetPx.roundToInt(), 0) }
                        .size(selectedItemSize)
                        .clip(
                            RoundedCornerShape(
                                topStart = cornerRadius.topStart,
                                topEnd = cornerRadius.topEnd,
                                bottomStart = cornerRadius.bottomStart,
                                bottomEnd = cornerRadius.bottomEnd,
                            ),
                        )
                        .background(selectedIndicatorColor),
            )
        }

        // Item contents
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items.forEachIndexed { index, itemContent ->
                Box(
                    modifier =
                        Modifier
                            .padding(4.dp)
                            .onSizeChanged { itemWidths[index] = it.toDp(density) }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                // No ripple, as the background moves
                                indication = null,
                                onClick = { onItemSelect(index) },
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    // Provide the item's composable with its selection state and animation duration
                    itemContent(isSelected = index == actualSelectedIndex)
                }
            }
        }
    }
}

fun IntSize.toDp(density: Density): DpSize =
    DpSize(
        width = with(density) { width.toDp() },
        height = with(density) { height.toDp() },
    )