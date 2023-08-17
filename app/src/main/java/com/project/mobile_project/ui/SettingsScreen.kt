package com.project.mobile_project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.project.mobile_project.R
import com.project.mobile_project.ui.theme.Mobile_projectTheme
import com.project.mobile_project.viewModel.SettingsViewModel
import android.content.res.Resources
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val theme by settingsViewModel.theme.collectAsState(initial = "")
    Mobile_projectTheme(darkTheme = theme == "Dark") {

        Scaffold { paddingValues ->
            Column(
                Modifier
                    .selectableGroup()
                    .padding(paddingValues)
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Theme",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(3.dp)
                )
                
                Spacer(modifier = Modifier.size(3.dp))
                
                val radioOptions = listOf("Light", "Dark")
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == theme),
                                onClick = {
                                    settingsViewModel.saveTheme(text)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == theme),
                            onClick = null // null recommended for accessibility with screenreaders
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
