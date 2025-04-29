package nl.frankkie.mobifreebackuptool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import nl.frankkie.mobifreebackuptool.ui.createNavGraph
import nl.frankkie.mobifreebackuptool.ui.theme.MobifreeBackupToolTheme

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel { AppViewModel() }
) {
    val appState = appViewModel.appState.collectAsState().value
    MobifreeBackupToolTheme {
        createNavGraph(navController, appViewModel)
    }
}