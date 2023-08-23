package com.project.mobile_project

import android.app.Application
import android.app.PendingIntent.getActivity
import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.mobile_project.data.AppDatabase
import com.project.mobile_project.ui.*
import com.project.mobile_project.viewModel.ActivitiesViewModel
import com.project.mobile_project.viewModel.SettingsViewModel
import com.project.mobile_project.viewModel.UsersViewModel
import dagger.hilt.android.HiltAndroidApp
import java.security.AccessController.getContext

@HiltAndroidApp
class TravelApp : Application() {
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { AppDatabase.getDatabase(this) }
}

sealed class AppScreen(val name: String) {
    object Home : AppScreen("Home")
    object Details : AppScreen("Details Screen")

    object Login : AppScreen("Login")
    object Profile : AppScreen("Profile")

    object Record : AppScreen("Record")

    object Settings : AppScreen("Settings")
}

@Composable
fun TopAppBarFunction(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingsButtonClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen,
                fontWeight = FontWeight.Medium,
            )
        },
        modifier = modifier,
        navigationIcon = {
            //se si può navigare indietro (non home screen) allora appare la freccetta
            if (canNavigateBack && currentScreen != AppScreen.Login.name) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        },
        actions = {
            if (currentScreen != AppScreen.Settings.name && currentScreen != AppScreen.Login.name) {
                IconButton(onClick =  onSettingsButtonClicked ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
            if (currentScreen == AppScreen.Profile.name) {
                IconButton(onClick = onLogoutButtonClicked ) {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun BottomAppBarFunction(
    currentScreen: String,
    onHomeButtonClicked: () -> Unit,
    onRecordButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentScreen == AppScreen.Home.name,
            onClick = onHomeButtonClicked
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Registra") },
            label = { Text("Record") },
            selected = currentScreen == AppScreen.Record.name,
            onClick = onRecordButtonClicked
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentScreen == AppScreen.Profile.name,
            onClick = onProfileButtonClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController(),
    sharedPreferences: SharedPreferences,
    context: Context
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Home.name

    Scaffold(
        topBar = {
            TopAppBarFunction(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onSettingsButtonClicked = {navController.navigate(AppScreen.Settings.name)},
                onLogoutButtonClicked = {logout(sharedPreferences, context)}
            )
        },
        bottomBar = {
            BottomAppBarFunction(
                currentScreen = currentScreen,
                onHomeButtonClicked = {navController.navigate(AppScreen.Home.name)},
                onRecordButtonClicked = {navController.navigate(AppScreen.Record.name)},
                onProfileButtonClicked = {navController.navigate(AppScreen.Profile.name)}
            )
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding)
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val usersViewModel = hiltViewModel<UsersViewModel>()
    val activitiesVieModel = hiltViewModel<ActivitiesViewModel>()
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = AppScreen.Home.name) {
            HomeScreen(
                onItemClicked = {
                    navController.navigate(AppScreen.Details.name)
                },
                activitiesViewModel = activitiesVieModel
            )
        }
        composable(route = AppScreen.Details.name) {
            DetailsScreen(activitiesViewModel = activitiesVieModel)
        }
        composable(route = AppScreen.Login.name) {
            //AddScreen { navController.popBackStack(AppScreen.Home.name, inclusive = false) }
            //LoginScreen()
        }
        composable(route = AppScreen.Record.name) {
            // HomeScreen("REGISTRAAAAAAAAAA")
            //AddScreen { navController.popBackStack(AppScreen.Home.name, inclusive = false) }
        }
        composable(route = AppScreen.Profile.name) {
            ProfileScreen(usersViewModel = usersViewModel)
        }
        composable(route = AppScreen.Settings.name) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(settingsViewModel)
        }
    }
}

private fun logout(sharedPreferences: SharedPreferences, context: Context) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    val loginIntent = Intent(context, LoginScreen::class.java)
    loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    editor.putBoolean(context.getString(R.string.user_logged_shared_pref), false)
    editor.putString(context.getString(R.string.username_shared_pref), "")
    editor.apply()

    startActivity(context, loginIntent, null)
}