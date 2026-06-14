package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel

// CRITICAL GOOGLE PLAY SERVICES IMPORTS FOR THE SENSOR PIPELINE
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun DashboardScreen(viewModel: CodeCardViewModel, navController: NavController) {
    val liveCards by viewModel.flashcardListFlow.collectAsState()
    val context = LocalContext.current

    // Live state UI coordinate variables linked directly to the hardware sensor stream
    var deviceLatitude by remember { mutableStateOf(0.0) }
    var deviceLongitude by remember { mutableStateOf(0.0) }
    var isSensorTrackingActive by remember { mutableStateOf(false) }

    // --- SUBSCRIPTION LOOP FOR THE PHYSICAL GPS SENSOR ---
    DisposableEffect(Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Set up the hardware polling rules: Request high accuracy updates every 5 seconds
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        // Core listener that catches incoming raw coordinate numbers from the hardware chip
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Update the layout view states dynamically on screen
                    deviceLatitude = location.latitude
                    deviceLongitude = location.longitude
                    isSensorTrackingActive = true

                    // CORE INTEGRATION PIPELINE: Location parameters automatically update the Web API fetch
                    viewModel.fetchLiveTermByHardwareGPS(location.latitude, location.longitude)
                }
            }
        }

        try {
            // Turn on hardware polling requests
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            isSensorTrackingActive = false
        }

        // Clean up resources and turn off the hardware tracking listener when navigating away
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    val displayList = if (viewModel.searchText.isEmpty()) {
        liveCards
    } else {
        liveCards.filter { it.topic.contains(viewModel.searchText, ignoreCase = true) }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBFF))
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Branding Layout Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "FlashTech", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color(0xFF5D5F96))
                Text(text = "Project: A221098_Mukesh_MrNelson_Project2", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Surface(shape = CircleShape, color = Color(0xFF5D5F96), modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("M", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // INTEGRATED LOCATION & INTERNET STATUS CARD PANEL
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1FA))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (isSensorTrackingActive) Color(0xFF2E7D32) else Color(0xFF5D5F96)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSensorTrackingActive) "🟢 Functional Sensor Active" else "📍 Initializing Hardware GPS...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSensorTrackingActive) Color(0xFF2E7D32) else Color(0xFF5D5F96)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Live tracking layout text fields reflecting hardware coordinates shifting dynamically
                Text("Latitude: $deviceLatitude", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text("Longitude: $deviceLongitude", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E2EC))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("🌐 Live Web API Term Sourced By GPS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D5F96))
                    if (viewModel.isApiLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color(0xFF5D5F96))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = viewModel.apiFetchedTopic, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(text = viewModel.apiFetchedNotes, fontSize = 13.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(10.dp))

                Text("🔥 Live Cloud Status: ${viewModel.activeGlobalLearnersCount} Coders studying this right now", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.saveApiTermToLocalRoom() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D5F96)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Sourced Term to local Room Deck", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Search Field Component
        OutlinedTextField(
            value = viewModel.searchText,
            onValueChange = { viewModel.searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search your study deck...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF5D5F96),
                focusedContainerColor = Color(0xFFE9E9F2),
                unfocusedContainerColor = Color(0xFFE9E9F2),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Your Local Room Deck List", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            displayList.forEach { card ->
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("detail/${card.id}") },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    ListItem(
                        headlineContent = { Text(card.topic, fontWeight = FontWeight.Bold, color = Color.Black) },
                        supportingContent = { Text("Priority: ${card.priority}", color = Color.Gray) },
                        trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}