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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import com.project.mobile_project.MainActivity
import com.project.mobile_project.R
import com.project.mobile_project.ui.theme.Accent
import com.project.mobile_project.ui.theme.Mobile_projectTheme
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
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.padding(5.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { newText: String -> username = newText },
                            label = { Text(getString(R.string.username_text)) },
                            placeholder = { Text(getString(R.string.username_text)) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                focusedLabelColor = Accent,
                                focusedLeadingIconColor = Accent,
                                cursorColor = Accent
                            )
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
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                focusedLabelColor = Accent,
                                focusedLeadingIconColor = Accent,
                                cursorColor = Accent
                            )
                        )

                        Spacer(modifier = Modifier.padding(3.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.8f)
                                .padding(5.dp),
                            onClick = {
                                login(
                                    username = username,
                                    password = password,
                                    viewModel = usersViewModel,
                                    lifecycleOwner = activity,
                                    context = activity,
                                    sharedPreferences = sharedPreferences,
                                )
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

                        Spacer(modifier = Modifier.padding(2.dp))

                        TextButton(
                            onClick = { goingToRegistrationScreen(activity) }
                        ) {
                            Text("Registrati ora", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

private fun login(username: String, password: String, viewModel: UsersViewModel, lifecycleOwner: LifecycleOwner, context: Context, sharedPreferences: SharedPreferences) {
    val user = viewModel.getUserFromUsername(username, password)

    if(user != null) {
        saveLoggedUser(username, sharedPreferences, context)
        val homeIntent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, homeIntent, null)
    } else {
        Toast.makeText(context, "Username o password sbagliata!", Toast.LENGTH_LONG).show()
    }
}

fun saveLoggedUser(username: String, sharedPreferences: SharedPreferences, context: Context) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    editor.putBoolean(context.getString(R.string.user_logged_shared_pref), true)
    editor.putString(context.getString(R.string.username_shared_pref), username)
    editor.apply()
}

private fun goingToRegistrationScreen(context: Context) {
    val registrationIntent = Intent(context, RegistrationScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context, registrationIntent, null)
}