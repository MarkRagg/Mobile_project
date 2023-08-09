package com.project.mobile_project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.mobile_project.ui.HomeScreen

sealed class AppScreen(val name: String) {
  object Home : AppScreen("Home")
  object Login : AppScreen("Login Screen")
  object Profile : AppScreen("Profile Screen")
  object Settings : AppScreen("Settings Screen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(
  navController: NavHostController = rememberNavController()
) {
  // Get current back stack entry
  val backStackEntry by navController.currentBackStackEntryAsState()
  // Get the name of the current screen
  val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Home.name

  Scaffold { innerPadding ->
    NavigationGraph(navController, innerPadding)
  }
}

@Composable
private fun NavigationGraph(
  navController: NavHostController,
  innerPadding: PaddingValues,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController = navController,
    startDestination = AppScreen.Home.name,
    modifier = modifier.padding(innerPadding)
  ) {
    composable(route = AppScreen.Home.name) {
      HomeScreen("Sium")
    }
    composable(route = AppScreen.Login.name) {
      //AddScreen { navController.popBackStack(AppScreen.Home.name, inclusive = false) }
    }
    composable(route = AppScreen.Profile.name) {
      //DetailsScreen()
    }
    composable(route = AppScreen.Settings.name) {
      //SettingsScreen()
    }
  }
}