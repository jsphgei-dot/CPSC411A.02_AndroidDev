package com.example.jetpackcomposebasicsdemo.navigation

data class Product(val id: String, val name: String, val price: Double)

data class CartItem(
    val productId: String,
    val productName: String,
    val price: Double,
    var quantity: Int
)