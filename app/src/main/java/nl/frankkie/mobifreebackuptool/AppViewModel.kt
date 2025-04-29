package nl.frankkie.mobifreebackuptool

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class AppViewModel : ViewModel() {
    private val _appState = MutableStateFlow(
        AppState()
    )
    val appState = _appState.asStateFlow()

    private fun setListOfInstalledAppsToBackup(list: List<String>) {
        _appState.value = _appState.value.copy(
            listOfInstalledAppsToBackup = list
        )
        //Save to shared preferences

    }

    fun setBackupEnabled(packageName: String, enabled: Boolean) {
        val currentList = _appState.value.listOfInstalledAppsToBackup.toMutableList()
        if (enabled) {
            currentList.add(packageName)
        } else {
            currentList.remove(packageName)
        }
        setListOfInstalledAppsToBackup(currentList)
    }
}

data class AppState(
    val isLoading: Boolean = false,
    val listOfInstalledAppsToBackup: List<String> = readListOfInstalledAppsToBackupFromPreferences(),
) {
    companion object {
        fun initial() = AppState()
        fun readListOfInstalledAppsToBackupFromPreferences(): List<String> {
            //TODO: Read from shared preferences
            return emptyList()
        }
    }
}
