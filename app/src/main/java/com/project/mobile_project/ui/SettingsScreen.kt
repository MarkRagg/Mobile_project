package com.project.mobile_project.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.project.mobile_project.ui.theme.Mobile_projectTheme

class SettingsScreen : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeKey = "THEME_KEY"
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        //val theme = sharedPref.getString(themeKey, "Light")

        val theme = sharedPref.getString(themeKey, "Light")
        setContent {
            Mobile_projectTheme(darkTheme = theme == "Dark") {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val radioOptions = listOf("Light", "Dark")
                    val (selectedOption, onOptionSelected) = remember { mutableStateOf(theme) }

                    Column(Modifier.selectableGroup()) {
                        radioOptions.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = {
                                            onOptionSelected(text)
                                            with(sharedPref.edit()) {
                                                putString(themeKey, text)
                                                apply()
                                            }
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}