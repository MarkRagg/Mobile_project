package com.project.mobile_project.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mobile_project.data.User
import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import com.project.mobile_project.ui.theme.Mobile_projectTheme
import com.project.mobile_project.viewModel.SettingsViewModel
import com.project.mobile_project.viewModel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen: ComponentActivity() {
  private val settingsViewModel: SettingsViewModel by viewModels()
  private val usersViewModel: UsersViewModel by viewModels()
  val activity = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val theme by settingsViewModel.theme.collectAsState(initial = "")
      Mobile_projectTheme(darkTheme = theme == "Dark") {

        val users = usersViewModel.allUsers.collectAsState(initial = listOf()).value
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
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
                onClick = { login(username, password, usersViewModel, activity, activity) },
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
      }
    }
  }
}

private fun insertNewUser(username: String, password: String, usersViewModel: UsersViewModel, users: List<User>, context: Context) {


    val newUser = User(
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
      Toast.makeText(context, "Username già usato!", Toast.LENGTH_LONG).show()
    } else {
      usersViewModel.insertUser(newUser)
    }
}

private fun login(username: String, password: String, viewModel: UsersViewModel, lifecycleOwner: LifecycleOwner, context: Context) {

  viewModel.getUserFromUsername(username, password)
  viewModel.userLiveData.observe(lifecycleOwner) {
    if(it != null) {
        Toast.makeText(context, "$it logged", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, "Username or password wrong!", Toast.LENGTH_LONG).show()
    }
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