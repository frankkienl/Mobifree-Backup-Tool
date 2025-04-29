package nl.frankkie.mobifreebackuptool

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import nl.frankkie.mobifreebackuptool.ui.createNavGraph
import nl.frankkie.mobifreebackuptool.ui.theme.MobifreeBackupToolTheme

//TODO: Add a DataStore for settings
//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
lateinit var appContext: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this.applicationContext
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}