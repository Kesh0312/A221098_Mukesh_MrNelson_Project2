package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(viewModel: CodeCardViewModel) {

    // Automatically fetch community decks from Firestore when the screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchCommunityDecks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBFF))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Screen Branded Header Layout
        Column {
            Text(
                text = "Community Decks",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color(0xFF5D5F96)
            )
            Text(
                text = "Explore shared flashcards from global coders",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Handle Empty or Loading State UI beautifully
        if (viewModel.communityCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5D5F96))
            }
        } else {
            // Render the live fetched Firestore Data cards layout list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.communityCards) { card ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = card.topic,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF333333)
                                )
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text("Priority: ${card.priority}") }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = card.notes,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Displays author info to verify cloud identity tracking
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Shared by: Mukesh A/L Gobi",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp)) // Safe padding layout spacer for bottom tab bar
    }
}
