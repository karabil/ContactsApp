package com.example.android.contactsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.android.contactsapp.data.model.Contact
import com.example.android.contactsapp.data.viewmodel.MainViewModel

@Composable
fun FirstScreen(navController: NavController, mainViewModel: MainViewModel) {
    val list by mainViewModel.allContacts.observeAsState()
    mainViewModel.fetchContacts(mainViewModel.query)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        MySearchView(mainViewModel.query, mainViewModel)
        ContactsList(list = list, navController = navController)
    }
}

@Composable
fun ContactsList(list: List<Contact>?, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .height(0.dp)
    ) {
        list?.size?.let { size ->
            items(size) { index ->
                ContactCard(list, index, navController)
            }
        }
    }
}

@Composable
fun ContactCard(list: List<Contact>, index: Int, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.SecondScreen.withArgs(list[index].name.toString()))
            }, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {

                if (list[index].image != null) {
                    ContactImage(photoUri = list[index].image!!)
                } else {
                    list[index].name?.let {
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
            list[index].name?.let {
                Text(
                    // At this point list is not null due to previous check.
                    text = it,
                    style = MaterialTheme.typography.h1,
                    fontSize = 22.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MySearchView(query: String, viewModel: MainViewModel) {
    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = query, onValueChange = {
        viewModel.onQueryChanged(it)
    }, leadingIcon = {
        Icon(
            imageVector = Icons.Filled.Search, contentDescription = "Search View"
        )
    })
}

@Composable
fun ContactImage(photoUri: String) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = photoUri)
                .build()
        ),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape),
        contentScale = ContentScale.Crop
    )
}