package com.example.myapplication.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun ShoppingApp() {
    // nav controller
    val navController = rememberNavController()

    //Nav Host
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //home screen


        //product list screen

        //product details screen

        //cart screen

    }

}

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Welcome to the Shop")

        Text("Cart Items: 10")

        Button(onClick = {}) {
            Text("Browse Products")
        }

        Button(onClick = {}) {
            Text("View Cart")
        }

    }

}