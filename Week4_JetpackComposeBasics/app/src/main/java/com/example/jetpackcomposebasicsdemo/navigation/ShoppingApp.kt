package com.example.jetpackcomposebasicsdemo.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.navigation
import kotlin.times


@Composable
fun ShoppingApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFlow"
    ) {
        navigation(startDestination = "home", "mainFlow"){
            composable("home") { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }

                val cartViewModel: CartViewModel = viewModel(mainFlowEntry)


                HomeScreen(cartViewModel, navController)
            }

            composable("productList") {backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val cartViewModel: CartViewModel = viewModel(mainFlowEntry)

                val productListViewModel: ProductListViewModel = viewModel()
                ProductListScreen(cartViewModel, productListViewModel, navController)
            }

            composable("productDetails") {
                ProductDetailsScreen()
            }

            composable("cart") { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val cartViewModel: CartViewModel = viewModel(mainFlowEntry)

                CartScreen(cartViewModel, navController)
            }
        }

    }

}

@Composable
fun HomeScreen(
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val itemCount by cartViewModel.itemCount.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Shop")

        Text("Cart Item: ${itemCount}")

        Button(onClick = {navController.navigate("productList")}) {
            Text("Browse Products")
        }

        Button(onClick = {navController.navigate("cart")}) {
            Text("View Cart")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    cartViewModel: CartViewModel,
    productListViewModel: ProductListViewModel,
    navController: NavController
) {
    val itemCount by cartViewModel.itemCount.collectAsState()
    val products by productListViewModel.products.collectAsState()
    val isLoading by productListViewModel.isLoading.collectAsState()

    Column (modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {Text("Products (Cart: ${itemCount})")},
            actions = {
                IconButton(onClick = {navController.navigate("cart")}) {
                    Icon(Icons.Default.ShoppingCart, "Cart")
                }
            }
        )

        if(isLoading){
            CircularProgressIndicator()
        } else {
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(products) {
                    product ->
                    ProductCard(product, navController, {cartViewModel.addItem(product)})
                }
            }
        }

    }
}

@Composable
fun ProductCard(product: Product,
                navController: NavController,
                onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Row (
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (modifier = Modifier.weight(1f)) {
                Text(product.name)
                Text("$${product.price}")
            }
            Button(onClick = {navController.navigate("productDetails")}) {
                Text("View Details")
            }
            Button(onClick = onAddToCart ) {
                Text("Add to Cart")
            }
        }
    }
}


@Composable
fun ProductDetailsScreen() {
    Column {
        Text("Product details screen")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Shopping Cart") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your cart is empty")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("productList") }) {
                        Text("Start Shopping")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onRemove = { cartViewModel.removeItem(item.productId) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Total: ${"%.2f".format(totalPrice)}",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { cartViewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Cart")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("productList") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue Shopping")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, style = MaterialTheme.typography.headlineMedium)
                Text("${item.price} x ${item.quantity}")
                Text(
                    "Subtotal: ${"%.2f".format(item.price * item.quantity)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remove")
            }
        }
    }
}


















