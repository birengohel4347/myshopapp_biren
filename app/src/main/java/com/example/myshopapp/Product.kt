package com.example.myshopapp

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val description: String,
    val imageResName: String
)

val sampleProducts = listOf(
    Product(1, "Premium Cotton T-Shirt", "T-Shirts", 24.99, "A premium quality cotton t-shirt, perfect for everyday comfort and style.", "tshirt1"),
    Product(2, "Slim Fit Denim Jeans", "Pants", 59.99, "Stylish slim-fit denim jeans with a modern look and durable fabric.", "jeans"),
    Product(3, "Urban Oversized Hoodie", "Hoodies", 44.99, "Stay cozy and stylish with this oversized hoodie, ideal for cool evenings.", "hoodie"),
    Product(4, "Classic White Tee", "T-Shirts", 19.99, "A versatile classic white tee that goes with everything.", "tshirt2")
)
