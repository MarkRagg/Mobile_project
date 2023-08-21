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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.project.mobile_project.MainActivity
import com.project.mobile_project.R
import com.project.mobile_project.data.User
import com.project.mobile_project.ui.theme.Mobile_projectTheme
import com.project.mobile_project.viewModel.SettingsViewModel
import com.project.mobile_project.viewModel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationScreen: ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.user_shared_preferences), Context.MODE_PRIVATE)

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            Mobile_projectTheme(darkTheme = theme == getString(R.string.dark_theme)) {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var firstName by rememberSaveable { mutableStateOf("") }
                        var lastName by rememberSaveable { mutableStateOf("") }
                        var email by rememberSaveable { mutableStateOf("") }
                        var username by rememberSaveable { mutableStateOf("") }
                        var password by rememberSaveable { mutableStateOf("") }
                        var passwordVisible by rememberSaveable { mutableStateOf(false) }

                        Row {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { newText: String -> firstName = newText },
                                label = { Text(getString(R.string.first_name_text)) },
                                placeholder = { Text(getString(R.string.first_name_text)) }
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { newText: String -> lastName = newText },
                                label = { Text(getString(R.string.last_name_text)) },
                                placeholder = { Text(getString(R.string.last_name_text)) }
                            )
                        }

                        Row {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { newText: String -> email = newText },
                                label = { Text(getString(R.string.email_text)) },
                                placeholder = { Text(getString(R.string.email_text)) }
                            )
                            OutlinedTextField(
                                value = username,
                                onValueChange = { newText: String -> username = newText },
                                label = { Text(getString(R.string.username_text)) },
                                placeholder = { Text(getString(R.string.username_text)) }
                            )
                        }

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
                        Button(
                            onClick = { registerNewUser(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                username = username,
                                password = password,
                                viewModel = usersViewModel,
                                lifecycleOwner = activity,
                                sharedPreferences = sharedPreferences,
                                context = activity) },

                            // Uses ButtonDefaults.ContentPadding by default
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 12.dp,
                                end = 20.dp,
                                bottom = 12.dp
                            )
                        ) {
                            Text("Registrati")
                        }
                    }
                }
            }
        }
    }
}

private fun registerNewUser(firstName: String, lastName: String, username: String, password: String, email: String, viewModel: UsersViewModel, lifecycleOwner: LifecycleOwner, sharedPreferences: SharedPreferences, context: Context) {
    val newUser = User(
        firstName = firstName,
        lastName = lastName,
        username = username,
        email = email,
        password = password,
        salt = null,
        profileImg = null
    )

    viewModel.getUserFromUsername(username, password)
    viewModel.userLiveData.observe(lifecycleOwner) {
        if(it == null) {
            saveLoggedUser(username, sharedPreferences, context)
            viewModel.insertUser(newUser)
            val homeIntent = Intent(context, MainActivity::class.java)
            ContextCompat.startActivity(context, homeIntent, null)
        } else {
            Toast.makeText(context, "Username già esistente!", Toast.LENGTH_LONG).show()
        }
    }
}