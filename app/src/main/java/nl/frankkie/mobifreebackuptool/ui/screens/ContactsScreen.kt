package nl.frankkie.mobifreebackuptool.ui.screens

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
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
fun ContactsScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
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
                Text(text = "Contacts Screen")
                val contactsPermissionState =
                    rememberPermissionState(android.Manifest.permission.READ_CONTACTS)
                if (!contactsPermissionState.status.isGranted) {
                    Button(onClick = { contactsPermissionState.launchPermissionRequest() }) {
                        Text("Request contacts permission")
                    }
                    return@Box
                }

                var contactListLoading by remember {
                    mutableStateOf(true)
                }
                var contactList by remember {
                    mutableStateOf(emptyList<MyContact>())
                }
                val scope = rememberCoroutineScope()
                LaunchedEffect(null) {
                    scope.launch {
                        contactList = readContacts()
                        contactListLoading = false
                    }
                }

                if (contactListLoading) {
                    Text("Loading contacts...")
                    return@Box
                }

                if (contactList.isEmpty()) {
                    Text("No contacts found.")
                    return@Box
                }

                Text("Contacts:")
                LazyColumn {
                    items(contactList.size) { index ->
                        val contact = contactList[index]
                        ListItem(
                            headlineContent = {
                                Text(text = contact.displayName)
                            },
                            supportingContent = {
                                //Text(text = contact.phoneNumber)
                                Text("Example phone number")
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.AccountCircle,
                                    contentDescription = "Contact Icon",
                                )
                            }
                        )
                    }

                }
            }
        }
    }
}


@SuppressLint("Range")
fun readContacts(): List<MyContact> {
    val TAG = "MFBT"
    if (ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return emptyList()
    }
    //Permission is granted, read contacts
    val cursor = appContext.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            //TODO: Get actual phone number, not just *if* there is a phone number.
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        ),
        null,
        null,
        null
    )
    val contactsList = mutableListOf<MyContact>()
    try {
        cursor?.let { safeCursor ->
            while (safeCursor.moveToNext()) {
                val id = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                )
                val displayName = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                )
                val phoneNumber = safeCursor.getString(
                    safeCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )
                val contact = MyContact(id, displayName, phoneNumber)
                contactsList.add(contact)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e(TAG, "Error reading contacts: ${e.message}", e)
        return emptyList()
    } finally {
        cursor?.close()
    }
    return contactsList
}



data class MyContact(
    val id: String,
    val displayName: String,
    val phoneNumber: String
)