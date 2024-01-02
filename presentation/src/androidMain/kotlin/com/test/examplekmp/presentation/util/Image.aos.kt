package com.test.examplekmp.presentation.util

import android.content.Context
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.option.androidContext
import okio.Path.Companion.toOkioPath
import org.koin.mp.KoinPlatformTools


actual fun createImageLoader() = ImageLoader {
    val context: Context = KoinPlatformTools.defaultContext().get().get()
    options {
        androidContext(context)
    }
    components {
        setupDefaultComponents()
    }
    interceptor {
        memoryCacheConfig {
            // Set the max size to 25% of the app's available memory.
            maxSizePercent(context, 0.25)
        }
        diskCacheConfig {
            directory(context.cacheDir.resolve("image_cache").toOkioPath())
            maxSizeBytes(512L * 1024 * 1024) // 512MB
        }
    }
}