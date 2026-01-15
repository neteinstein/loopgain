package org.neteinstein.loopgain.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.neteinstein.loopgain.ui.screens.CardDeckScreen
import org.neteinstein.loopgain.ui.screens.LoadingScreen

sealed class Screen(val route: String) {
    data object Loading : Screen("loading")
    data object CardDeck : Screen("card_deck")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf(Screen.Loading.route) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Loading.route) {
            LoadingScreen()
            
            LaunchedEffect(Unit) {
                delay(2500) // Show loading screen for 2.5 seconds
                navController.navigate(Screen.CardDeck.route) {
                    popUpTo(Screen.Loading.route) { inclusive = true }
                }
            }
        }
        
        composable(Screen.CardDeck.route) {
            CardDeckScreen()
        }
    }
}
