package com.maslan.capstoneupschool.data.model.response

data class GetProductsResponse(
    val products: List<Product>?
) : BaseResponse()
