package com.example.a221098_mukesh_mrnelson_project1.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Form : Screen("form")
    object Review : Screen("review")
    object Detail : Screen("detail/{id}")
    object Insights : Screen("insights")
}