package com.example.myshopapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShopViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<Product>()
    val cartItems: List<Product> get() = _cartItems

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    fun addToCart(product: Product) {
        _cartItems.add(product)
        calculateTotal()
    }

    fun removeFromCart(product: Product) {
        _cartItems.remove(product)
        calculateTotal()
    }

    fun clearCart() {
        _cartItems.clear()
        calculateTotal()
    }

    private fun calculateTotal() {
        _totalPrice.value = _cartItems.sumOf { it.price }
    }
}
