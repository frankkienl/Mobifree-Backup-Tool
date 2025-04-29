package nl.frankkie.mobifreebackuptool.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import nl.frankkie.mobifreebackuptool.AppViewModel
import nl.frankkie.mobifreebackuptool.util.MyPackageInfo
import nl.frankkie.mobifreebackuptool.util.PackagesUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("App List") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "App List Screen")
                Text("Select apps to be backed up.")

                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                val appState = appViewModel.appState.collectAsState().value
                var listOfMyPackageInfo by remember {
                    mutableStateOf(emptyList<MyPackageInfo>())
                }
                LaunchedEffect(null) {
                    scope.launch {
                        listOfMyPackageInfo = PackagesUtil.getListOfMyPackageInfo(context)
                    }
                }

                val filteredList = listOfMyPackageInfo.filter { packageInfo ->
                    !packageInfo.isSystemApp
                }

                LazyColumn {
                    items(filteredList.size) { index ->
                        val packageInfo = filteredList[index]
                        ListItem(
                            headlineContent = {
                                Text(text = packageInfo.displayName)
                            },
                            supportingContent = {
                                Text(text = packageInfo.packageName)
                            },
                            leadingContent = {
                                //No idea how to use Drawable in Compose, so AndroidView it is.
                                AndroidView(
                                    modifier = Modifier.size(48.dp),
                                    factory = { context ->
                                        val imageView = android.widget.ImageView(context)
                                        imageView.setImageDrawable(
                                            PackagesUtil.getIconAsDrawable(
                                                context,
                                                packageInfo.packageName
                                            )
                                        )
                                        imageView
                                    }
                                )
                            },
                            trailingContent = {
                                Checkbox(
                                    appState.listOfInstalledAppsToBackup.contains(packageInfo.packageName),
                                    onCheckedChange = {
                                        appViewModel.setBackupEnabled(packageInfo.packageName, it)
                                    })
                            }
                        )
                    }
                }
            }
        }
    }
}