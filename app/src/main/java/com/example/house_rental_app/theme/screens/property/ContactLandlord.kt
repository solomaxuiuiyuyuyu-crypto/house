package com.example.house_rental_app.theme.screens.property

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.house_rental_app.R
import com.example.house_rental_app.data.UserViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactLandlord(
    navController: NavController,
    ownerKey: String
) {
    val userViewModel: UserViewModel = viewModel()
    val landlordDetails = userViewModel.userDetails.value
    var messageText by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Загружаем данные арендодателя
    LaunchedEffect(ownerKey) {
        isLoading = true
        userViewModel.fetchUserById(ownerKey)
        delay(500)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contact_landlord_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Информация об арендодателе
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.landlord_info),
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (landlordDetails != null) {
                        Text(
                            text = "${stringResource(R.string.name)}: ${landlordDetails.username}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (landlordDetails.showOnlyEmail) {
                            Text(
                                text = "${stringResource(R.string.email)}: ${landlordDetails.email}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (landlordDetails.showOnlyPhone) {
                            Text(
                                text = "${stringResource(R.string.phone)}: ${landlordDetails.phoneNumber}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (!landlordDetails.showOnlyEmail && !landlordDetails.showOnlyPhone) {
                            Text(
                                text = stringResource(R.string.contact_hidden),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.landlord_not_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Поле для сообщения - убрали minLines и maxLines
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text(stringResource(R.string.message_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Кнопка отправки
            Button(
                onClick = {
                    showSuccessMessage = true
                    messageText = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = messageText.isNotBlank() && landlordDetails != null && !isLoading
            ) {
                Text(stringResource(R.string.send_message))
            }

            // Сообщение об успехе
            if (showSuccessMessage) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    showSuccessMessage = false
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = stringResource(R.string.message_sent),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}