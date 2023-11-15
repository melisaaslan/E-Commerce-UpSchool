package com.maslan.capstoneupschool.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maslan.capstoneupschool.common.Resource
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.data.repository.AuthRepository
import com.maslan.capstoneupschool.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState



    fun getCartProducts() = viewModelScope.launch {
        _cartState.value = CartState.Loading

        _cartState.value = when (val result = productRepository.getCartProduct(authRepository.getUserId())) {
            is Resource.Success -> CartState.SuccessState(result.data)
            is Resource.Fail -> CartState.EmptyScreen(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }

    fun deleteProduct(productId: Int) = viewModelScope.launch {
        _cartState.value = CartState.Loading

        _cartState.value = when (val result = productRepository.deleteProductFromCart(productId, authRepository.getUserId())) {
            is Resource.Success -> {
                getCartProducts()
                CartState.ShowPopUp(result.data.message.orEmpty())
            }

            is Resource.Fail -> CartState.ShowPopUp(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }

    fun clearProduct() = viewModelScope.launch {
        when (val result = productRepository.clearProductFromCart(authRepository.getUserId())) {
            is Resource.Success -> {
                getCartProducts()
            }
            is Resource.Error ->
                _cartState.value = CartState.ShowPopUp(result.errorMessage)

            is Resource.Fail-> CartState.EmptyScreen(result.failMessage)

        }
    }
}

sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val product: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}
