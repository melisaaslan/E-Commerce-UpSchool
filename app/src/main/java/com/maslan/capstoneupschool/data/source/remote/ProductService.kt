package com.maslan.capstoneupschool.data.source.remote


import com.maslan.capstoneupschool.data.model.request.AddToCartRequest
import com.maslan.capstoneupschool.data.model.request.ClearCartRequest
import com.maslan.capstoneupschool.data.model.request.DeleteFromCartRequest
import com.maslan.capstoneupschool.data.model.response.GetProductDetailResponse
import com.maslan.capstoneupschool.data.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ProductService {
    @GET("get_products.php")
    suspend fun getProducts(): Response<GetProductsResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<GetProductDetailResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(
    ): Response<GetProductsResponse>

    @GET("get_products_by_category.php")
    suspend fun getProductsByCategory(
        @Query("category") categoryValue: String
    ): Response<GetProductsResponse>

    @GET("search_product.php")
    suspend fun getSearchProduct(
        @Query("query") queryValue: String
    ): Response<GetProductsResponse>

    @GET("get_cart_products.php")
    suspend fun getCartProduct(
        @Query("userId") userId: String
    ): GetProductsResponse

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): GetProductDetailResponse

    @POST("delete_from_cart.php")
    suspend fun deleteProduct(
        @Body request: DeleteFromCartRequest
    ): GetProductsResponse

    @POST("clear_cart.php")
    suspend fun clearCart(
        @Body request: ClearCartRequest
    ): GetProductsResponse

}




