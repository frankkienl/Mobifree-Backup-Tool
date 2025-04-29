package nl.frankkie.mobifreebackuptool.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import nl.frankkie.mobifreebackuptool.AppViewModel
import nl.frankkie.mobifreebackuptool.ui.screens.AboutScreen
import nl.frankkie.mobifreebackuptool.ui.screens.AppListScreen
import nl.frankkie.mobifreebackuptool.ui.screens.CalendarScreen
import nl.frankkie.mobifreebackuptool.ui.screens.ContactsScreen
import nl.frankkie.mobifreebackuptool.ui.screens.HomeScreen
import nl.frankkie.mobifreebackuptool.ui.screens.SettingsScreen

object Routes {

    @Serializable
    object HOME

    @Serializable
    object APP_LIST

    @Serializable
    object CONTACTS

    @Serializable
    object CALENDAR

    @Serializable
    object SETTINGS

    @Serializable
    object ABOUT
}

@Composable
fun createNavGraph(navController: NavHostController, appViewModel: AppViewModel) {
    NavHost(navController, startDestination = Routes.HOME) {
        composable<Routes.HOME> { HomeScreen(navController, appViewModel) }
        composable<Routes.APP_LIST> { AppListScreen(navController, appViewModel) }
        composable<Routes.CALENDAR> { CalendarScreen(navController, appViewModel) }
        composable<Routes.CONTACTS> { ContactsScreen(navController, appViewModel) }
        composable<Routes.SETTINGS> { SettingsScreen(navController, appViewModel) }
        composable<Routes.ABOUT> { AboutScreen(navController, appViewModel) }
    }
}