package com.example.house_rental_app.theme.screens.menuscreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.house_rental_app.R
import com.example.house_rental_app.data.HouseViewModel
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.data.UserViewModel
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.navigation.ROUTE_DETAILED_PROPERTY
import com.example.house_rental_app.theme.screens.LoadingPage
import kotlinx.coroutines.delay

@Composable
fun AllListings(navController: NavController, sharedViewModel: SharedViewModel) {

    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.currentUser.observeAsState()
    Log.println(Log.INFO, "",user.toString())
    val houseViewModel: HouseViewModel = viewModel()
    val allHouses by houseViewModel.allHouses.observeAsState(emptyList())
    var isLoading by remember { mutableStateOf(true) }

    Log.println(Log.INFO, "", allHouses.toString())
    LaunchedEffect(key1 = true) {
        delay(2000)
        isLoading = false
    }
    if (isLoading) {
        LoadingPage(stringResource(R.string.loading_fetching_listings))
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if(allHouses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_rentals_available),
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            else {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(R.string.all_listings_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                )
                ScrollableListWithImagesAll(
                    houseEntities = allHouses,
                    navController = navController
                )
            }

        }
    }


}
@Composable
fun ScrollableListWithImagesAll(
    houseEntities: List<HouseEntity>,
    navController: NavController
) {
    LazyColumn {
        items(houseEntities) { houseEntity ->
            ImageListItemAll(
                houseEntity = houseEntity,
                onClick = {
                    val key = houseEntity.firebaseHouseId
                    println("=== DEBUG: Navigating with key: $key")
                    if (key.isNotBlank()) {
                        navController.navigate("$ROUTE_DETAILED_PROPERTY/$key")
                    }
                }
            )
        }
    }
}
@Composable
fun ImageListItemAll(
    houseEntity: HouseEntity,
    onClick: () -> Unit
) {
    val imagePaths = houseEntity.images
        .split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (imagePaths.isNotEmpty()) {
                LazyRow {
                    items(imagePaths) { imagePath ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(200.dp),

                            ) {
                            val bitmap = loadBitmapFromFilePath(imagePath)
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                contentDescription = stringResource(R.string.property_image_content_description),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(250.dp)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = toAnnotatedText(stringResource(R.string.label_address_with_value), houseEntity.address),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            Text(
                text = toAnnotatedText(stringResource(R.string.label_lease_with_value), houseEntity.lease),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            Text(
                text = toAnnotatedText(stringResource(R.string.label_price_with_value), houseEntity.price.toString()),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }
    }
}
fun toAnnotatedText(text: String, value: String): AnnotatedString {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text)
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
            append(value)
        }
    }
    return annotatedText
}
@Preview(showBackground = true)
@Composable
fun AllListingsPreview() {
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel().apply { setUserId("") }
    AllListings(navController = navController, sharedViewModel = sharedViewModel)
}
