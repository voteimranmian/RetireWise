package com.retirewise.core

actual class Platform {
    actual val name: String = "Android"
}

actual fun currentPlatform(): Platform = Platform()
