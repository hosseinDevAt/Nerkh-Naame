package com.example.nerkhnaame.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.nerkhnaame.R
import com.example.nerkhnaame.nav.NavScreens
import com.example.nerkhnaame.ui.theme.BackViewBlack
import com.example.nerkhnaame.ui.theme.GoldText
import com.example.nerkhnaame.viewModel.NetworkUiState
import com.example.nerkhnaame.viewModel.SplashViewModel

@Composable
fun Splash(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel(),
    context: Context
) {

    val uiState by viewModel.networkState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver{_, event ->
            if (event == Lifecycle.Event.ON_RESUME){
                viewModel.checkNetwork()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {

            is NetworkUiState.Success -> {
                val isConnected = (uiState as NetworkUiState.Success).isConnected
                if (isConnected) {
                    navController.navigate(NavScreens.Home.route) {
                        popUpTo(NavScreens.Splash.route) { inclusive = true }
                    }
                }
            }

            is NetworkUiState.Error -> {
                val errorMessage = (uiState as NetworkUiState.Error).message
                Log.e("Tag_Err_Network", errorMessage)
            }

            else -> {}
        }
    }

    if (uiState is NetworkUiState.Success && !(uiState as NetworkUiState.Success).isConnected){
        PanelNetworkError(
            "اتصال نداشتن به اینترنت",
            "اتصال به اینترنت ناموفق بود برای روشن کردن اینترنت روی دکمه تنظیمات کلیک کنید.",
            onDismiss = {},
            onRetry = {
                viewModel.checkNetwork()
            },
            context
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackViewBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.img_splash),
            contentDescription = null,
            modifier = Modifier
                .size(220.dp, 220.dp)
                .fillMaxWidth()

        )

        Spacer(Modifier.height(35.dp))

        Text(
            "قیمت طلا و ارز",
            color = GoldText,
            fontSize = 44.sp,
        )

        Spacer(Modifier.height(15.dp))

        Text(
            "بروزترین اخبار و قیمت طلا و ارز",
            color = Color.White,
            fontSize = 30.sp,
        )

        if (uiState is NetworkUiState.Loading) {
            Spacer(Modifier.height(35.dp))
            CircularProgressIndicator()
        }

    }

}

@Composable
fun PanelNetworkError(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    context: Context
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        shape = RoundedCornerShape(12.dp),
        confirmButton = {
            TextButton(
                onClick = {
                    onRetry()
                    onDismiss()
                }
            ) {
                Text("تلاش مجدد")
            }
        },
        dismissButton = {
            TextButton(
                {
                    try {
                        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "امکان باز کردن تنظیمات وجود ندارد.",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                    onDismiss()
                }
            ) {
                Text(
                    "باز کردن تنظیمات"
                )
            }
        },
        title = {
            Text(
                title,
                style = TextStyle(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        text = {
            Text(
                text,
                style = TextStyle(
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()

            )
        },
        icon = {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = Color.Red
            )
        }
    )


}
