package com.project.mobile_project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun TopAppBarFunction(
  currentScreen: String,
  canNavigateBack: Boolean,
  navigateUp: () -> Unit,
  modifier: Modifier = Modifier,
  onSettingsButtonClicked: () -> Unit
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
      if (canNavigateBack) {
        IconButton(onClick = navigateUp) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back button"
          )
        }
      }
    },
    actions = {
      if (currentScreen == AppScreen.Home.name) {
        IconButton(onClick = { /* doSomething() */ }) {
          Icon(Icons.Filled.Search, contentDescription = "Search")
        }
      }
      if (currentScreen != AppScreen.Settings.name) {
        IconButton(onClick =  onSettingsButtonClicked ) {
          Icon(
            Icons.Filled.Settings,
            contentDescription = "Settings"// stringResource(id = R.string.settings)
          )
        }
      }
    },
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    )
  )
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

  Scaffold(
    topBar = {
      TopAppBarFunction(
        currentScreen = currentScreen,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateUp = { navController.navigateUp() },
        onSettingsButtonClicked = {navController.navigate(AppScreen.Settings.name)}
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