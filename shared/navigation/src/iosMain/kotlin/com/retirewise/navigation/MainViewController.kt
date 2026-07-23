package com.retirewise.navigation

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/**
 * Entry point called from iosApp's SwiftUI host to embed the shared
 * Compose Multiplatform UI. See ARCHITECTURE.md section 15 for the module
 * boundary between shared/navigation (Kotlin) and apps/iosApp (Swift host).
 */
@Suppress("ktlint:standard:function-naming")
fun MainViewController(): UIViewController = ComposeUIViewController { App() }
