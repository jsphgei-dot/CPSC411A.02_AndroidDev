package com.example.jetpackcomposebasicsdemo.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProductListViewModel: ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadProducts()
    }


    private fun loadProducts() {
        viewModelScope.launch {
            delay(2000)
            _products.value = listOf(
                Product("1", "Product 1", 10.0),
                Product("2", "Product 2", 20.0),
                Product("3", "Product 3", 30.0),
                Product("4", "Product 4", 40.0),

            )
        }
    }
}

class CartViewModel: ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    val itemCount: StateFlow<Int> = _cartItems.map {
        it.sumOf { item -> item.quantity }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalPrice: StateFlow<Double> = _cartItems.map {
        item -> item.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    fun addItem(product: Product) {
        val current = _cartItems.value.toMutableList()

        val existingIndex = current.indexOfFirst { it.productId == product.id }

        if(existingIndex != -1) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else{
            current.add(CartItem(product.id, product.name, product.price, 1))

        }
        _cartItems.value = current
    }

    fun removeItem(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.productId != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }


}






























