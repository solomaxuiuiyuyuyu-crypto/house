package com.example.house_rental_app.theme.screens.property

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.house_rental_app.R
import com.example.house_rental_app.data.HouseViewModel
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.navigation.ROUTE_CONTACT_LANDLORD
import com.example.house_rental_app.theme.screens.menuscreens.loadBitmapFromFilePath
import com.example.house_rental_app.theme.screens.menuscreens.toAnnotatedText

@Composable
fun DetailedProperty(navController: NavController, houseKey: String) {

    val houseViewModel: HouseViewModel = viewModel()
    val houseEntity by houseViewModel.viewedHouse.observeAsState()
    LaunchedEffect(houseKey) {
        houseViewModel.getHouseByKey(houseKey)
    }
    val imagePaths = houseEntity?.images
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow{
        if (!imagePaths.isNullOrEmpty()) {

            items(imagePaths) { imagePath ->
                val bitmap = loadBitmapFromFilePath(imagePath)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.generic_image_content_description),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }}

        // Show property details
        Text(toAnnotatedText(text = stringResource(R.string.label_address_with_value), houseEntity?.address ?: ""), style = MaterialTheme.typography.labelLarge,)
        Text(toAnnotatedText(text = stringResource(R.string.label_lease_with_value), houseEntity?.lease?:""), style = MaterialTheme.typography.labelLarge,)
        Text(toAnnotatedText(text = stringResource(R.string.label_price_with_value), houseEntity?.price.toString() ?: "0"), style = MaterialTheme.typography.labelLarge,)
        Text(toAnnotatedText(text = stringResource(R.string.label_description_with_value), houseEntity?.description ?: ""), style = MaterialTheme.typography.labelLarge,)

        // Button to contact landlord
        Button(onClick = {
            val ownerKey = houseEntity?.ownerKey.orEmpty()
            println("=== DEBUG: ownerKey = '$ownerKey'")  // ← Добавь лог
            println("=== DEBUG: houseEntity = $houseEntity")  // ← Добавь лог

            if (ownerKey.isNotBlank()) {
                navController.navigate("$ROUTE_CONTACT_LANDLORD/$ownerKey")
            } else {
                // Покажи Toast об ошибке
                android.widget.Toast.makeText(
                    navController.context,
                    "Ошибка: данные арендодателя не найдены",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(stringResource(R.string.contact_landlord_button))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DetailedPropertyPreview() {
//    val navController = rememberNavController()
//    DetailedProperty(navController = navController,  HouseEntity( address = "111 Main St", price = 234, ownerId = 23, lease = "April 1st 2022", images = "img_2", description = "A 2 bedroom" ))
//}
