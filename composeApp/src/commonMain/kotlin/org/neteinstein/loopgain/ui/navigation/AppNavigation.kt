package org.neteinstein.loopgain.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.loopgain.ui.screens.CardDeckScreen
import org.neteinstein.loopgain.ui.screens.LoadingScreen
import org.neteinstein.loopgain.ui.screens.session.SessionFlowScreen
import org.neteinstein.loopgain.ui.screens.session.tablet.TabletSessionFlowScreen
import org.neteinstein.loopgain.ui.viewmodel.SessionViewModel

/**
 * Below this width the phone's "Faithful deck" screen-per-stage flow is used; at or above it, the
 * tablet's wide facilitator board. There is no window-size-class infrastructure in this codebase
 * yet, so this is a plain width check rather than a formal breakpoint API — 840.dp matches Material's
 * own "expanded" width class, which is a reasonable stand-in for "this is a tablet, not a phone".
 */
private val TABLET_MIN_WIDTH = 840.dp

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
            // One SessionViewModel instance shared by whichever layout renders — session state
            // must not fork between the phone and tablet UIs.
            val viewModel: SessionViewModel = koinViewModel()
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                if (maxWidth >= TABLET_MIN_WIDTH) {
                    TabletSessionFlowScreen(viewModel = viewModel)
                } else {
                    SessionFlowScreen(viewModel = viewModel)
                }
            }
        }

        composable(Screen.CardDeck.route) {
            CardDeckScreen()
        }
    }
}
