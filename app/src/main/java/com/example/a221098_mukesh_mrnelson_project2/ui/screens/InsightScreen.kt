package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel

@Composable
fun InsightsScreen(viewModel: CodeCardViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FF)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Deck Insights", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(40.dp))

        // Large Circle Progress UI
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            CircularProgressIndicator(
                progress = 1f, // Full circle background
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFE1E2F9),
                strokeWidth = 12.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${viewModel.flashcardList.size}", fontSize = 64.sp, fontWeight = FontWeight.Black, color = Color(0xFF5D5F96))
                Text("Cards", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Time Estimate Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
        ) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF5D5F96))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Total Study Time", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text("${viewModel.getStudyTimeEstimate()} Minutes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Back to Dashboard")
        }
    }
}
