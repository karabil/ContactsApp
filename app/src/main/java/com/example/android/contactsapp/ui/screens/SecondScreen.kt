package com.example.android.contactsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android.contactsapp.data.model.Contact
import com.example.android.contactsapp.data.viewmodel.MainViewModel
import com.example.android.contactsapp.util.Util

@Composable
fun SecondScreen(navController: NavController, displayName: String?, mainViewModel: MainViewModel) {
    require(displayName != null)

    // Initializing contact details..
    mainViewModel.getContactByDisplayName(displayName)
    val currentContact by mainViewModel.currentContact.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ContactCard(displayName, mainViewModel.getContactByDisplayName(displayName))
        Spacer(modifier = Modifier.height(16.dp))
        AllPhonesList(list = currentContact?.allPhonesList, mainViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        AllEmailsList(list = currentContact?.allEmailsList, mainViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            //Going back to home screen
            navController.navigateUp()
        }) {
            Text(text = "Go Back")
        }
    }
}

@Composable
fun ContactCard(displayName: String, contact: Contact?) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                if (contact?.image != null) {
                    ContactImage(photoUri = contact.image!!)
                } else {
                    contact?.name?.let {
                        Text(
                            text = it.substring(0, 1),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier.padding(0.dp),
                            fontSize = 20.sp
                        )

                    }
                }
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.h1,
                fontSize = 22.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 10.dp),
                maxLines = 1
            )
        }
    }
}

@Composable
fun AllPhonesList(list: List<Contact>?, mainViewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp)
            .background(Color.Yellow)
    ) {
        Text(
            text = "All Phones:", fontSize = 18.sp, modifier = Modifier.padding(10.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            if (list != null) {
                items(list.size) { index ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        list[index].numberType?.let {
                            Text(
                                textAlign = TextAlign.Center,
                                // Replacing type numbers from provider data to readable text.
                                text = Util.replacePhoneTypeWithDescription(it.toInt())
                            )
                        }
                        list[index].number?.let { it ->
                            var text by remember {
                                mutableStateOf(it)
                            }
                            TextField(value = text,
                                onValueChange = { it1 ->
                                    text = it1
                                },
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .focusRequester(FocusRequester())
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) {
                                            mainViewModel.onTextFocused(text)
                                        } else {
                                            mainViewModel.onPhoneTextUnfocused(text)
                                        }
                                    })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllEmailsList(list: List<Contact>?, mainViewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp)
            .background(Color.Blue)
    ) {

        Text(
            text = "All Emails:", fontSize = 18.sp, modifier = Modifier.padding(10.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            if (list != null) {
                items(list.size) { index ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        list[index].emailType?.let {
                            Text(
                                textAlign = TextAlign.Center,
                                text = Util.replaceEmailTypeWithDescription(it.toInt())
                            )
                        }
                        list[index].email?.let {
                            var text by remember {
                                mutableStateOf(it)
                            }
                            TextField(value = text,
                                onValueChange = { it1 ->
                                    text = it1
                                },
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .focusRequester(FocusRequester())
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) {
                                            mainViewModel.onTextFocused(text)
                                        } else {
                                            mainViewModel.onEmailTextUnfocused(text)
                                        }
                                    })
                        }
                    }
                }
            }
        }
    }
}