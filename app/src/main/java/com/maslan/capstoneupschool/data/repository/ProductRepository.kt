package com.maslan.capstoneupschool.data.repository

import com.maslan.capstoneupschool.common.Resource
import com.maslan.capstoneupschool.data.mapper.mapProductEntityToProductUI
import com.maslan.capstoneupschool.data.mapper.mapProductToProductUI
import com.maslan.capstoneupschool.data.mapper.mapToProductEntity
import com.maslan.capstoneupschool.data.mapper.mapToProductUI
import com.maslan.capstoneupschool.data.model.request.AddToCartRequest
import com.maslan.capstoneupschool.data.model.response.BaseResponse
import com.maslan.capstoneupschool.data.model.request.ClearCartRequest
import com.maslan.capstoneupschool.data.model.request.DeleteFromCartRequest
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.data.source.local.ProductDao
import com.maslan.capstoneupschool.data.source.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao
) {

    suspend fun getProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }



    suspend fun getProductDetail(id: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getProductDetail(id).body()

                if (response?.status == 200 && response.product != null) {
                    Resource.Success(response.product.mapToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun addToFavorites(productUI: ProductUI) {
        productDao.addProduct(productUI.mapToProductEntity())
    }

    suspend fun deleteFromFavorites(productUI: ProductUI) {
        productDao.deleteProduct(productUI.mapToProductEntity())
    }

    suspend fun getFavorites(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val products = productDao.getProducts()

                if (products.isEmpty()) {
                    Resource.Fail("Products not found")
                } else {
                    Resource.Success(products.mapProductEntityToProductUI())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun deleteAllFromFav(){
        productDao.deleteAllProducts()

    }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getSaleProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            }catch (e: Exception) {
                    Resource.Error(e.message.orEmpty())
                }
        }

    suspend fun getSearchProduct(query: String): Resource<List<ProductUI>> {
        return try {
            val response = productService.getSearchProduct(query).body()
            Resource.Success(response?.products?.map { it.mapToProductUI() }.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun getCartProduct(userId: String): Resource<List<ProductUI>> {
        return try {
            val response = productService.getCartProduct(userId)
            Resource.Success(response.products?.map { it.mapToProductUI() }.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun addProductToCart(addToCartRequest: AddToCartRequest): BaseResponse {
        return productService.addToCart(addToCartRequest)
    }

    suspend fun deleteProductFromCart(productId: Int, userId: String): Resource<BaseResponse> {
        return try {
            val response = productService.deleteProduct(DeleteFromCartRequest(productId, userId))
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun clearProductFromCart(userId: String): Resource<BaseResponse> {
        return try {
            val response = productService.clearCart(ClearCartRequest(userId))
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }




}





















