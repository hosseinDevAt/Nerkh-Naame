package com.example.nerkhnaame.nav

sealed class NavScreens(val route: String) {

    object Splash: NavScreens("splash")
    object Home: NavScreens("home")


}