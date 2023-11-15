package com.maslan.capstoneupschool.data.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: Int
)