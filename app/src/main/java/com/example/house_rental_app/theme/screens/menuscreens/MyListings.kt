package com.example.house_rental_app.theme.screens.menuscreens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.house_rental_app.R
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.data.HouseViewModel
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.theme.screens.LoadingPage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListings(navController: NavController, sharedViewModel: SharedViewModel) {

    val houseViewModel: HouseViewModel = viewModel()
    val userKey = sharedViewModel.userId.observeAsState().value.orEmpty()
    val userId = userKey.hashCode()
    LaunchedEffect(key1 = userId) {
        houseViewModel.viewHousesByOwnerId(userId)
    }
    val allHouses by houseViewModel.allHouses.observeAsState(initial = emptyList())
    Log.println(Log.INFO, "", allHouses.toString())
    var isLoading by remember { mutableStateOf(true) }

    Log.println(Log.INFO, "", allHouses.toString())
    LaunchedEffect(key1 = true) {
        // Simulate a network loading delay or wait for a real network call
        delay(3000) // Remove this line if you are observing real data changes
        isLoading = false
    }
    //Each Property Card
    if (isLoading) {
        LoadingPage(stringResource(R.string.loading_fetching_listings))
    }
    else {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (allHouses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(R.string.my_listings_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                ScrollableListWithImages(
                    houseEntities = allHouses,
                    houseViewModel = houseViewModel,
                    navController = navController
                )

            }
            else{
                Box(
                    modifier = Modifier.fillMaxSize(), // Fill the parent size to ensure the Box is as big as the screen
                    contentAlignment = Alignment.Center // Align the content of the Box to the center
                ) {
                    Text(
                        text = stringResource(R.string.no_rentals_posted),
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                        // Additional Text styling here
                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun ImageListItem(
    modifier: Modifier = Modifier,
    houseEntity: HouseEntity,
    houseViewModel: HouseViewModel,
    onEditClick: () -> Unit,
    onSaveClick: (HouseEntity) -> Unit
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
//            val trimmedImages = houseEntity.images.trimStart('[').trimEnd(']')
//
//            val imagesList: List<String> = trimmedImages.split(", ")
    val imagePaths = houseEntity.images
        .split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
//            val painter: Painter = painterResource(id = houseEntity.images.toInt())
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickable { },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyRow {
                items(imagePaths) { imagePath ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(200.dp),

                        ) {
//                                val uri = Uri.parse(imagePath)
                        val bitmap = remember { loadBitmapFromFilePath(imagePath)}
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = stringResource(R.string.generic_image_content_description),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(250.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            var address by remember { mutableStateOf(houseEntity.address) }
            var leaseAvailability by remember { mutableStateOf(houseEntity.lease) }
            var price by remember { mutableStateOf(houseEntity.price.toString()) }
            if (isEditing) {

                // TextFields for editing
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.field_address)) })
                TextField(
                    value = leaseAvailability,
                    onValueChange = { leaseAvailability = it },
                    label = { Text(stringResource(R.string.field_lease_availability)) })
                TextField(
                    value = price,
                    onValueChange = { price = it},
                    label = {Text(stringResource(R.string.field_price)) })
                Button(onClick = {
                    val parsedPrice = price.toIntOrNull() ?: houseEntity.price
                    onSaveClick(
                        //TODO EDIT OP
                        houseEntity.copy(
                            houseId = houseEntity.houseId,
                            address = address,
                            lease = leaseAvailability,
                            price = parsedPrice
                        )
                    )
                    isEditing = false
                }) {
                    Text(stringResource(R.string.save_changes),
                        fontFamily = FontFamily.Monospace)
                }
            } else {
                // Display text with an edit button
                Text(
                    toAnnotatedText(stringResource(R.string.label_address_with_value), address),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    toAnnotatedText(stringResource(R.string.label_lease_with_value), leaseAvailability),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    toAnnotatedText(stringResource(R.string.label_price_with_value), price),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row() {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            houseViewModel.deleteHouse(houseEntity.houseId)  // ← передаём Int
                            Toast.makeText(context, "Удаление...", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            }
        }
    }
}

@Composable
fun ScrollableListWithImages(
    houseEntities: List<HouseEntity>,
    houseViewModel: HouseViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn {
        items(houseEntities) { houseEntity ->
            ImageListItem(
                houseEntity = houseEntity,
                houseViewModel = houseViewModel,
                onEditClick = { /* Handle edit click */ },
                onSaveClick = { updatedDetails ->
                    // Handle the save action, e.g., update the list or backend
                    coroutineScope.launch {
                      //  houseViewModel.editHouse(updatedDetails)
                    }
                    Log.d("EditProperty", "Saved: $updatedDetails")
                }
            )
        }
    }
}
fun loadBitmapFromFilePath(filePath: String): Bitmap? {
    return try {
        BitmapFactory.decodeFile(filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

