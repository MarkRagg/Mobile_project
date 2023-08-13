package com.project.mobile_project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LoginScreen() {

  Column (
    modifier = Modifier
      .padding(all = 12.dp)
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
    var title by rememberSaveable { mutableStateOf("") }

    Text(
      text = "Accedi",
      fontSize = 20.sp,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      textAlign = TextAlign.Center
    )
    OutlinedTextField(
      value = title,
      onValueChange = { newText: String -> title = newText },
      label = { Text("Username") },
      placeholder = { Text("Marco") }
    )
  }
}