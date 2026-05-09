package com.example.a221098_mukesh_mrnelson_project1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project1.viewmodel.CodeCardViewModel

@Composable
fun DetailScreen(cardId: String?, viewModel: CodeCardViewModel, navController: NavController) {
    val card = viewModel.flashcardList.find { it.id == cardId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5D5F96)) // Dark Navy background for focus mode
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("STUDY MODE", color = Color.White.copy(alpha = 0.7f), letterSpacing = 2.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        // THE FLASHCARD
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = Color(0xFFE1E2F9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Priority ${card?.priority}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF5D5F96)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = card?.topic ?: "Unknown",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = card?.notes ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Finish Session", color = Color(0xFF5D5F96), fontWeight = FontWeight.Bold)
        }
    }
}
