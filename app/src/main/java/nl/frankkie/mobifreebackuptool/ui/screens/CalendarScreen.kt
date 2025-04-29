package nl.frankkie.mobifreebackuptool.ui.screens

import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import nl.frankkie.mobifreebackuptool.AppViewModel
import nl.frankkie.mobifreebackuptool.appContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CalendarScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
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
                Text(text = "Calendar Screen")
                val calendarPermissionState =
                    rememberPermissionState(android.Manifest.permission.READ_CALENDAR)
                if (!calendarPermissionState.status.isGranted) {
                    Button(onClick = { calendarPermissionState.launchPermissionRequest() }) {
                        Text(text = "Request Calendar Permission")
                    }
                    return@Box
                }

                var calendarEventsLoading by remember { mutableStateOf(true) }
                var calendarEvents by remember { mutableStateOf(emptyList<MyCalendarEvent>()) }
                val scope = rememberCoroutineScope()

                LaunchedEffect(null) {
                    scope.launch {
                        calendarEvents = loadCalendarEvents()
                        calendarEventsLoading = false
                    }
                }

                if (calendarEventsLoading) {
                    Text(text = "Loading calendar events...")
                    return@Box
                }

                if (calendarEvents.isEmpty()) {
                    Text(text = "No calendar events found.")
                    return@Box
                }

                Text(text = "Calendar events:")
                LazyColumn {
                    items(calendarEvents.size) { index ->
                        val event = calendarEvents[index]
                        ListItem(
                            headlineContent = {
                                Text(text = event.title ?: "No title")
                            },
                            supportingContent = {
                                if (event.allDay) {
                                    Text(text = "All day event")
                                } else {
                                    Text(text = "Start: ${event.startTime}; End: ${event.endTime}")
                                }
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.DateRange,
                                    contentDescription = "Calendar Event Icon",
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

fun loadCalendarEvents(): List<MyCalendarEvent> {
    val TAG = "MFBT"
    if (ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.READ_CALENDAR
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return emptyList()
    }

    val cursor = appContext.contentResolver.query(
        CalendarContract.Events.CONTENT_URI,
        arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DESCRIPTION
        ),
        null,
        null,
        null
    )

    val eventsList = mutableListOf<MyCalendarEvent>()
    try {
        cursor?.let { safeCursor ->
            while (safeCursor.moveToNext()) {
                val id = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
                )
                val title = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
                )
                val dtStart = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
                )
                val dtEnd = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
                )
                val allDay = safeCursor.getInt(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)
                ) != 0
                val location = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION)
                )
                val description = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)
                )
                val event =
                    MyCalendarEvent(id, title, dtStart, dtEnd, allDay, location, description)
                eventsList.add(event)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e(TAG, "Error reading calendar: ${e.message}", e)
        return emptyList()
    } finally {
        cursor?.close()
    }

    return emptyList()
}

data class MyCalendarEvent(
    val id: String?,
    val title: String?,
    val startTime: String?,
    val endTime: String?,
    val allDay: Boolean,
    val location: String?,
    val description: String?,
)