package com.example.a221098_mukesh_mrnelson_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a221098_mukesh_mrnelson_project1.ui.navigation.Screen
import com.example.a221098_mukesh_mrnelson_project1.ui.screens.*
import com.example.a221098_mukesh_mrnelson_project1.ui.theme.A221098_Mukesh_MrNelson_Project1Theme
import com.example.a221098_mukesh_mrnelson_project1.viewmodel.CodeCardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // APPLYING THE CUSTOM THEME
            A221098_Mukesh_MrNelson_Project1Theme {
                val viewModel: CodeCardViewModel = viewModel()
                MainAppView(viewModel)
            }
        }
    }
}

@Composable
fun MainAppView(viewModel: CodeCardViewModel) {
    val navController = rememberNavController()

    // TRACKING THE CURRENT ROUTE FOR THE NAVIGATION BAR
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // PROFESSIONAL BOTTOM NAVIGATION BAR
            NavigationBar(
                containerColor = Color(0xFFF8F9FF), // Aesthetic light-blue tint
                tonalElevation = 8.dp
            ) {
                // HOME ITEM (DASHBOARD)
                // Inside your NavigationBar logic in MainActivity.kt
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentDestination?.route == Screen.Dashboard.route,
                    onClick = {
                        // This ensures the back button doesn't just loop forever
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false // Keep the dashboard
                            }
                            launchSingleTop = true
                        }
                    }
                )

                // CREATE ITEM (FORM)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create") },
                    label = { Text("Create") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Form.route } == true,
                    onClick = {
                        navController.navigate(Screen.Form.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // STATS ITEM (INSIGHTS) - THE 5TH SCREEN
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Stats") },
                    label = { Text("Stats") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Insights.route } == true,
                    onClick = {
                        navController.navigate(Screen.Insights.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        // THE NAVIGATION ENGINE (CONNECTING ALL 5 SCREENS)
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // SCREEN 1: DASHBOARD
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel, navController)
            }

            // SCREEN 2: FORM (INPUT)
            composable(Screen.Form.route) {
                FormScreen(viewModel, navController)
            }

            // SCREEN 3: REVIEW (CONFIRMATION)
            composable(Screen.Review.route) {
                ReviewScreen(viewModel, navController)
            }

            // SCREEN 4: DETAIL (STUDY MODE)
            composable(Screen.Detail.route) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("id")
                DetailScreen(cardId, viewModel, navController)
            }

            // SCREEN 5: INSIGHTS (ANALYTICS)
            composable(Screen.Insights.route) {
                InsightsScreen(viewModel, navController)
            }
        }
    }
}
