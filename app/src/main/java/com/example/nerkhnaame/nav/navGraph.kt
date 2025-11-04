package com.example.nerkhnaame.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nerkhnaame.ui.screens.Home
import com.example.nerkhnaame.ui.screens.Splash

@Composable
fun SetUpNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavScreens.Splash.route
    ){


        composable(
            route = NavScreens.Splash.route
        ) {
            Splash(navController, context = context)
        }


        composable(
            route = NavScreens.Home.route
        ) {
            Home()
        }


    }


}