package com.example.house_rental_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splashscreen: ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()

            val mContext = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            coroutineScope.launch{
                delay(3000)
                mContext.startActivity(Intent(mContext, MainActivity::class.java))
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(18.dp))

        // Исправленный код Lottie
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.animation_lmqarsif)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition
        )

        LottieAnimation(
            composition = composition,
            progress =  progress ,
            modifier = Modifier.size(600.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color.Black,
            strokeWidth = 10.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun workPreview(){
    SplashScreen()
}
