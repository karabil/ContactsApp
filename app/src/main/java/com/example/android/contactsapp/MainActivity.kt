package com.example.android.contactsapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.android.contactsapp.data.viewmodel.MainViewModel
import com.example.android.contactsapp.data.viewmodel.MyViewModelFactory
import com.example.android.contactsapp.ui.screens.Navigation
import com.example.android.contactsapp.ui.theme.ContactsAppTheme

/**
 * Launcher activity (entry point).
 */
class MainActivity : ComponentActivity() {
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPermissionIsGranted()) {
            val myViewModelFactory = MyViewModelFactory(application)
            viewModel = ViewModelProvider(this, myViewModelFactory)[MainViewModel::class.java]
        }

        setContent {
            viewModel?.let { MyApp(it) }
        }
    }

    /**
     * Checking that the user enabled contact permissions.
     */
    private fun checkPermissionIsGranted(): Boolean {
        // Check condition
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // When permission is not granted
            // Request permission
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                100
            )
            return false
        } else {
            // When permission is granted
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check condition
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
        } else {
            // When permission is denied
            // Display toast
            Toast.makeText(this@MainActivity, "Permission Denied.", Toast.LENGTH_SHORT).show()
            // Call check permission method
            checkPermissionIsGranted()
        }
    }
}

/**
 * A composable used in the hosting activity
 * to support theming and action bar.
 */
@Composable
fun MyApp(mainViewModel: MainViewModel) {
    // Using a theme block to support day and dark theme later on.
    ContactsAppTheme {
        // A surface container using the 'background' color from the theme.
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            // Scaffold is set in the activity to support an action bar for all screens.
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.primary,
                        title = {
                            Text(
                                text = "Contacts App",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        })
                },
                // Content of the screens (equivalent to fragments in views).
                content = {
                    Column(modifier = Modifier.padding(it)) {
                        // Navigation composable (used to be navigation graph in views).
                        Navigation(mainViewModel)
                        // DisplayList()
                    }
                }
            )
        }
    }
}