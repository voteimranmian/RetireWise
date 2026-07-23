package com.retirewise.core

import platform.UIKit.UIDevice

actual class Platform {
    actual val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun currentPlatform(): Platform = Platform()
