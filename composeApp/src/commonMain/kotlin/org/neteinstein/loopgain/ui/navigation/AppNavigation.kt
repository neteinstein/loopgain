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
import org.neteinstein.loopgain.ui.screens.session.SessionFlowScreen

sealed class Screen(val route: String) {
    data object Loading : Screen("loading")

    // Superseded by Session below and no longer reachable from Loading, but kept — not
    // deleted — while the new session flow settles in. See AGENTS.md.
    data object CardDeck : Screen("card_deck")
    data object Session : Screen("session")
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
                navController.navigate(Screen.Session.route) {
                    popUpTo(Screen.Loading.route) { inclusive = true }
                }
            }
        }

        composable(Screen.Session.route) {
            SessionFlowScreen()
        }

        composable(Screen.CardDeck.route) {
            CardDeckScreen()
        }
    }
}
