package com.project.mobile_project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mobile_project.data.User
import java.util.*
import com.project.mobile_project.TravelApp
import com.project.mobile_project.viewModel.UsersViewModelFactory
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel

@Preview
@Composable
fun LoginScreen() {

  val usersViewModel = hiltViewModel<UsersViewModel>()

  Column (
    modifier = Modifier
      .padding(all = 12.dp)
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Text(
      text = "Accedi",
      fontSize = 20.sp,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      textAlign = TextAlign.Center
    )
    OutlinedTextField(
      value = username,
      onValueChange = { newText: String -> username = newText },
      label = { Text("Username") },
      placeholder = { Text("...") }
    )
    OutlinedTextField(
      value = password,
      onValueChange = { newText: String -> password = newText },
      label = { Text("Password") },
      placeholder = { Text("...") },
      singleLine = true,
      visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      trailingIcon = {
        val image = if (passwordVisible)
          Icons.Filled.Visibility
        else Icons.Filled.VisibilityOff

        // Please provide localized description for accessibility services
        val description = if (passwordVisible) "Hide password" else "Show password"

        IconButton(onClick = {passwordVisible = !passwordVisible}){
          Icon(imageVector  = image, description)
        }
      }
    )
    Button(
      onClick = { insertNewUser(username, password, usersViewModel) },
      // Uses ButtonDefaults.ContentPadding by default
      contentPadding = PaddingValues(
        start = 20.dp,
        top = 12.dp,
        end = 20.dp,
        bottom = 12.dp
      )
    ) {
      Text("Login")
    }
  }
}

private fun insertNewUser(username: String, password: String, usersViewModel: UsersViewModel) {
    var newUser = User(
    userId = 1,
    firstName = "Marco",
    lastName = "Raggini",
    username = username,
    email = "a",
    password = password,
    salt = null,
    profileImg = "a"
    )

    usersViewModel.insertUser(newUser)
}