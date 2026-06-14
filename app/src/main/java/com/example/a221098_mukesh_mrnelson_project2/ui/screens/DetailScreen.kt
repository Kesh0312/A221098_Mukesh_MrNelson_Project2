package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(cardId: String?, viewModel: CodeCardViewModel, navController: NavController) {
    val idInt = cardId?.toIntOrNull() ?: 0

    // CRITICAL THREAD SAFETY FIX: Observe live flows directly inside the screen container
    val flashcards by viewModel.flashcardListFlow.collectAsState()
    val card = remember(flashcards, idInt) { flashcards.find { it.id == idInt } }

    var isFlipped by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Mode", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D5F96))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("edit/$idInt") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Card", tint = Color(0xFF5D5F96))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFCFBFF))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCFBFF))
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (card == null) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Flashcard not found or has been deleted.", color = Color.Gray)
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { isFlipped = !isFlipped }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (!isFlipped) {
                                Text(
                                    text = "TOPIC",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D5F96),
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = card.topic,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF2C2D4E),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Badge(containerColor = Color(0xFFE9E9F2)) {
                                    Text(
                                        text = "Priority Level: ${card.priority}",
                                        color = Color(0xFF5D5F96),
                                        modifier = Modifier.padding(6.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "(Tap card to reveal study notes)",
                                    fontSize = 12.sp,
                                    color = Color.LightGray
                                )
                            } else {
                                Text(
                                    text = "STUDY NOTES",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = card.notes.ifBlank { "No structural notes added to this card yet." },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "(Tap card to flip back to front)",
                                    fontSize = 12.sp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                OutlinedButton(
                    onClick = { navController.navigate("edit/$idInt") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF5D5F96))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modify or Delete This Card", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}