package com.example.a221098_mukesh_mrnelson_project1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project1.viewmodel.CodeCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: CodeCardViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBFF))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // The Product Brand
                Text(
                    text = "FlashTech",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF5D5F96)
                )
                Text(
                    text = "Project: A221098_Mukesh_MrNelson_Project1",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
            }

            // Your Profile Avatar
            Surface(
                shape = CircleShape,
                color = Color(0xFF5D5F96),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("M", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- WORKING SEARCH BAR ---
        OutlinedTextField(
            value = viewModel.searchText,
            onValueChange = { viewModel.searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search your study deck...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.searchText.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchText = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF5D5F96),
                focusedContainerColor = Color(0xFFE9E9F2),
                unfocusedContainerColor = Color(0xFFE9E9F2)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // List Section
        Text("Your Deck List", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f) // Allows list to take available space
        ) {
            items(viewModel.filteredList) { card ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("detail/${card.id}") },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    ListItem(
                        headlineContent = { Text(card.topic, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Priority: ${card.priority}", color = Color.Gray) },
                        trailingContent = { Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.LightGray) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Padding for Bottom Bar
    }
}