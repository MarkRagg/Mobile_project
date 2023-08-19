package com.project.mobile_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.project.mobile_project.ui.LoginScreen
import com.project.mobile_project.ui.theme.Mobile_projectTheme
import com.project.mobile_project.viewModel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val USERNAME_LOGGED_PREF = "usernameLoggedPref"
    private val settingsViewModel: SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(USERNAME_LOGGED_PREF, Context.MODE_PRIVATE)

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            Mobile_projectTheme(darkTheme = theme == "Dark") {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if(sharedPreferences.getBoolean("userLogged", false)) {
                        NavigationApp(sharedPreferences = sharedPreferences, context = applicationContext)
                    } else {
                        val i = Intent(applicationContext, LoginScreen::class.java)
                        startActivity(i)
                    }
                }
            }
        }
    }
}