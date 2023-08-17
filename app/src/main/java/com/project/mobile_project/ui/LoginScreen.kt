package com.project.mobile_project.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.mobile_project.viewModel.UsersViewModel

@Preview
@Composable
fun LoginScreen() {

  val usersViewModel = hiltViewModel<UsersViewModel>()
  val users = usersViewModel.allUsers.collectAsState(initial = listOf()).value

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
      onClick = { insertNewUser(username, password, usersViewModel, users) },
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

private fun insertNewUser(username: String, password: String, usersViewModel: UsersViewModel, users: List<User>) {
    var newUser = User(
    firstName = "Marco",
    lastName = "Raggini",
    username = username,
    email = "a",
    password = password,
    salt = null,
    profileImg = "a"
    )

    val userInDb = userExist(users, username)
    if(userInDb != null) {
      //TODO far uscire qualcosa di UI per dire che l'username esiste già
    } else {
      usersViewModel.insertUser(newUser)
    }
}

private fun login(users: List<User>, username: String) {
  val userInDb = userExist(users, username)
  if(userInDb != null) {
    //TODO cambiare schermata
  } else {
    //TODO far uscire qualcosa di UI per dire che quell'username non esiste
  }
}

private fun userExist(users: List<User>, username: String): User?{
  var exist: User? = null

  for(user in users) {
    if(user.username == username) {
      exist = user
    }
  }

  return exist
}