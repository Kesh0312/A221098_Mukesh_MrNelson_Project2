package com.example.a221098_mukesh_mrnelson_project2

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a221098_mukesh_mrnelson_project2.ui.navigation.Screen
import com.example.a221098_mukesh_mrnelson_project2.ui.theme.A221098_Mukesh_MrNelson_Project2Theme
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.DashboardScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.DetailScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.FormScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.InsightsScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.ReviewScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.EditScreen
import com.example.a221098_mukesh_mrnelson_project2.ui.screens.CommunityScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- HARDWARE LOCATION PERMISSION REGISTERED ---
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise hardware GPS location access granted
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Approximate hardware cell/WiFi location access granted
                }
                else -> {
                    // Permission denied by user
                }
            }
        }

        // Trigger the explicit runtime permission popup window dialog automatically on launch
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            A221098_Mukesh_MrNelson_Project2Theme {
                val app = application as CodeCardsApplication
                val database = app.database

                val viewModel: CodeCardViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return CodeCardViewModel(database.flashcardDao()) as T
                        }
                    }
                )

                // Convert the Room Flow stream into a reactive Compose state tracker
                val flashcards by viewModel.flashcardListFlow.collectAsState()

                MainAppView(viewModel)
            }
        }
    }
}

@Composable
fun MainAppView(viewModel: CodeCardViewModel) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF8F9FF),
                tonalElevation = 8.dp
            ) {
                // BUTTON 1: HOME (DASHBOARD)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentDestination?.route == Screen.Dashboard.route,
                    onClick = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )

                // BUTTON 2: CREATE (FORM)
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

                // BUTTON 3: CLOUD DECK (COMMUNITY)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Share, contentDescription = "Community") },
                    label = { Text("Community") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Community.route } == true,
                    onClick = {
                        navController.navigate(Screen.Community.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // BUTTON 4: STATS (INSIGHTS)
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

            // SCREEN 6: EDIT DETAILS SCREEN
            composable("edit/{id}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("id")
                EditScreen(cardId, viewModel, navController)
            }

            // SCREEN 7: COMMUNITY VIEW SCREEN
            composable(Screen.Community.route) {
                CommunityScreen(viewModel = viewModel)
            }
        }
    }
}