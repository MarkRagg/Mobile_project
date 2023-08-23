package com.project.mobile_project.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import com.project.mobile_project.MainActivity
import com.project.mobile_project.R
import com.project.mobile_project.data.Activity
import com.project.mobile_project.ui.theme.Mobile_projectTheme
import com.project.mobile_project.viewModel.ActivitiesViewModel
import com.project.mobile_project.viewModel.SettingsViewModel
import com.project.mobile_project.viewModel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen: ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.user_shared_preferences), Context.MODE_PRIVATE)

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
                            label = { Text(getString(R.string.username_text)) },
                            placeholder = { Text(getString(R.string.username_text)) }
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { newText: String -> password = newText },
                            label = { Text(getString(R.string.password_text)) },
                            placeholder = { Text(getString(R.string.password_text)) },
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

                        Spacer(modifier = Modifier.padding(5.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                                onClick = { login(
                                    username = username,
                                    password = password,
                                    viewModel = usersViewModel,
                                    lifecycleOwner = activity,
                                    context = activity,
                                    sharedPreferences = sharedPreferences,
                                )
                                        //insertNewActivity()
                                },

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
                            Spacer(modifier = Modifier.padding(20.dp))
                            ClickableText(
                                text = AnnotatedString("Registrati ora.."),
                                onClick = { goingToRegistrationScreen(activity) },
                                modifier = Modifier.align(alignment = Alignment.Bottom)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun insertNewActivity(sharedPreferences: SharedPreferences, context: Context, activitiesViewModel: ActivitiesViewModel) {
    val userCreator = sharedPreferences.getString(context.getString(R.string.username_shared_pref), "")?.let {
        activitiesViewModel.insertActivity(
            Activity(
                userCreatorUsername = it,
                name = "Attività di prova",
                description = "La descriptiones es mas importante" ,
                totalTime = 2,
                distance = 50,
                speed = 20 ,
                pace = null,
                steps = null,
                onFoot = null
            )
        )
    }
}

private fun login(username: String, password: String, viewModel: UsersViewModel, lifecycleOwner: LifecycleOwner, context: Context, sharedPreferences: SharedPreferences) {
  viewModel.getUserFromUsername(username, password)
  viewModel.userLiveData.observe(lifecycleOwner) {
    if(it != null) {
        saveLoggedUser(username, sharedPreferences, context)
        val homeIntent = Intent(context, MainActivity::class.java)
        startActivity(context, homeIntent, null)
    } else {
        Toast.makeText(context, "Username o password sbagliata!", Toast.LENGTH_LONG).show()
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

fun saveLoggedUser(username: String, sharedPreferences: SharedPreferences, context: Context) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    editor.putBoolean(context.getString(R.string.user_logged_shared_pref), true)
    editor.putString(context.getString(R.string.username_shared_pref), username)
    editor.apply()
}

private fun goingToRegistrationScreen(context: Context) {
    val registrationIntent = Intent(context, RegistrationScreen::class.java)
    startActivity(context, registrationIntent, null)
}