package com.example.a221098_mukesh_mrnelson_project1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project1.viewmodel.CodeCardViewModel

@Composable
fun ReviewScreen(viewModel: CodeCardViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBFF))
            .padding(24.dp)
    ) {
        Text("Final Review", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Check everything is correct before saving.", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // SUMMARY LIST STYLE
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            ReviewItem(label = "Topic Name", value = viewModel.inputTopic, icon = Icons.Default.Edit)
            ReviewItem(label = "Study Notes", value = viewModel.inputNotes, icon = Icons.AutoMirrored.Filled.List)
            ReviewItem(label = "Priority Level", value = "Level ${viewModel.inputPriority}", icon = Icons.Default.Star)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.addCardToList()
                navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Confirm & Save to Deck", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ReviewItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = Color(0xFFE9E9F2), modifier = Modifier.size(40.dp)) {
            Icon(icon, null, modifier = Modifier.padding(10.dp), tint = Color(0xFF5D5F96))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}
