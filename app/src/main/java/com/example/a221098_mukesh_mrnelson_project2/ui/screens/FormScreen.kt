package com.example.a221098_mukesh_mrnelson_project2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a221098_mukesh_mrnelson_project2.viewmodel.CodeCardViewModel

@Composable
fun FormScreen(viewModel: CodeCardViewModel, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Fill Up Card Details", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = viewModel.inputTopic, onValueChange = { viewModel.inputTopic = it }, label = { Text("Topic") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.inputNotes, onValueChange = { viewModel.inputNotes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Text("Priority: ${viewModel.inputPriority}")
        Slider(value = viewModel.inputPriority.toFloat(), onValueChange = { viewModel.inputPriority = it.toInt() }, valueRange = 1f..5f, steps = 3)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("review") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            // SAFETY: Button only works if Topic and Notes are NOT empty
            enabled = viewModel.inputTopic.isNotBlank() && viewModel.inputNotes.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Next to Review")
        }    }
}
