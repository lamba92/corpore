package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import io.github.lamba92.app_core.generated.resources.Res
import org.koin.compose.koinInject

val LocalCoilImageLoaderProvider =
    staticCompositionLocalOf<ImageLoader> { error("No CoilDebugLogger provided") }

@Composable
fun DebugCoilImageLoader(): ImageLoader =
    ImageLoader(LocalPlatformContext.current)
        .newBuilder()
        .logger(koinInject())
        .build()

@Composable
fun WithCoilDebugLogger(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalCoilImageLoaderProvider provides DebugCoilImageLoader(),
        content = content,
    )
}

@Composable
fun ResourceImage(
    path: String,
    imageLoader: ImageLoader = LocalCoilImageLoaderProvider.current,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    transform: (State) -> State = DefaultTransform,
    onState: ((State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) {
    AsyncImage(
        model = Res.getUri(path),
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        transform = transform,
        onState = onState,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,
    )
}
