package nl.frankkie.mobifreebackuptool.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import nl.frankkie.mobifreebackuptool.AppViewModel
import nl.frankkie.mobifreebackuptool.BuildConfig
import nl.frankkie.mobifreebackuptool.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mobifree Backup Tool") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { navController.navigate(Routes.ABOUT)}) {
                        Icon(Icons.Filled.Info, contentDescription = "About")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Mobifree Backup Tool")
                Text("Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                Text("by FrankkieNL")
                Text("This app is not affiliated with or endorsed by Mobifree.")
                Text("This app is open source and available on GitHub.")
                Text("This app is not responsible for any data loss or damage caused by using this app.")
                Text("Use at your own risk.")
                Text("This app will use *ROOT* to try to backup your apps. No guarantee that it will work.")
                Text("By using this app, you agree to theses terms and conditions.")
                Text("And don't blame me if it doesn't work.")
                Text("This app was made for research purposes only.")
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Routes.APP_LIST) },
                ) {
                    Text("Backup Apps")
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Routes.CONTACTS) },
                ) {
                    Text("Backup Contacts")
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Routes.CALENDAR) },
                ) {
                    Text("Backup Calendar")
                }
            }
        }
    }
}