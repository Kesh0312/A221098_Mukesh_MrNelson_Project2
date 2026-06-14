package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // CORRECT DESIGN IMPORT
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel
import com.example.a221098_mukesh_mrnelson_project2.data.Flashcard
import com.example.a221098_mukesh_mrnelson_project2.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(cardId: String?, viewModel: CodeCardViewModel, navController: NavController) {
    val idInt = cardId?.toIntOrNull() ?: 0

    val flashcards by viewModel.flashcardListFlow.collectAsState()
    val existingCard = remember(flashcards, idInt) { flashcards.find { it.id == idInt } }

    var topicState by remember { mutableStateOf("") }
    var notesState by remember { mutableStateOf("") }
    var priorityState by remember { mutableIntStateOf(1) }
    var originalTopic by remember { mutableStateOf("") }

    LaunchedEffect(existingCard) {
        existingCard?.let {
            topicState = it.topic
            notesState = it.notes
            priorityState = it.priority
            originalTopic = it.topic
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Flashcard", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // FIXED: Uses the updated AutoMirrored framework arrow icon asset
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D5F96))
                    }
                },
                actions = {
                    if (existingCard != null) {
                        IconButton(
                            onClick = {
                                viewModel.deleteCardFromRoom(existingCard)
                                navController.popBackStack(Screen.Dashboard.route, inclusive = false)
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFCFBFF))
            )
        }
    ) { innerPadding ->
        if (existingCard == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Flashcard not found.", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFCFBFF))
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = topicState,
                    onValueChange = { topicState = it },
                    label = { Text("Topic / Programming Language") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(0xFF5D5F96)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = notesState,
                    onValueChange = { notesState = it },
                    label = { Text("Study Notes / Concepts") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(0xFF5D5F96)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Review Priority: $priorityState", fontWeight = FontWeight.Medium, color = Color.Black)
                Slider(
                    value = priorityState.toFloat(),
                    onValueChange = { priorityState = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF5D5F96),
                        activeTrackColor = Color(0xFF5D5F96)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (topicState.isNotBlank()) {
                            val updatedCard = Flashcard(
                                id = idInt,
                                topic = topicState,
                                notes = notesState,
                                priority = priorityState
                            )

                            // Matches your exact viewmodel signature references perfectly
                            viewModel.updateCardInRoom(updatedCard, originalTopic)

                            navController.popBackStack(Screen.Dashboard.route, inclusive = false)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D5F96))
                ) {
                    Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}