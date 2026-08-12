package com.smartscan.app.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Scanner : Screen("scanner")
    object Preview : Screen("preview/{documentId}") {
        fun createRoute(documentId: String) = "preview/$documentId"
    }
    object OcrEditor : Screen("ocr_editor/{documentId}") {
        fun createRoute(documentId: String) = "ocr_editor/$documentId"
    }
    object Library : Screen("library")
    object Tools : Screen("tools")
    object Settings : Screen("settings")
}
