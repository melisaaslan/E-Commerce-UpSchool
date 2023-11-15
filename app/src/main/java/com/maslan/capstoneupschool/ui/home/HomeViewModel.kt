package com.maslan.capstoneupschool.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslan.capstoneupschool.common.Resource
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.data.repository.AuthRepository
import com.maslan.capstoneupschool.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
    ) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    private var _salesState = MutableLiveData<SalesState>()
    val salesState: LiveData<SalesState>
        get() = _salesState


    fun getProducts() = viewModelScope.launch {
        _homeState.value = HomeState.Loading

        _homeState.value = when (val result = productRepository.getProducts()) {
            is Resource.Success -> HomeState.SuccessState(result.data)
            is Resource.Fail -> HomeState.EmptyScreen(result.failMessage)
            is Resource.Error -> HomeState.ShowPopUp(result.errorMessage)
        }
    }

    fun getSaleProducts() = viewModelScope.launch {
        _salesState.value = SalesState.Loading

        _salesState.value = when (val result = productRepository.getSaleProducts()) {
            is Resource.Success -> SalesState.SuccessState(result.data)
            is Resource.Fail -> SalesState.EmptyScreen(result.failMessage)
            is Resource.Error -> SalesState.ShowPopUp(result.errorMessage)
        }
    }



    fun setFavoriteState(product: ProductUI) = viewModelScope.launch {
        if (product.isFav) {
            productRepository.deleteFromFavorites(product)
        } else {
            productRepository.addToFavorites(product)
        }
        getProducts()
        getSaleProducts()

    }

    fun logOut() {
        authRepository.logOut()
        _homeState.value = HomeState.GoToSignIn
    }
}


sealed interface HomeState {
    object Loading : HomeState
    object GoToSignIn : HomeState
    data class SuccessState(val products: List<ProductUI>) : HomeState
    data class EmptyScreen(val failMessage: String) : HomeState
    data class ShowPopUp(val errorMessage: String) : HomeState
}
sealed interface SalesState {
    object Loading: SalesState
    data class SuccessState(val products: List<ProductUI>) : SalesState
    data class EmptyScreen(val failMessage: String) : SalesState
    data class ShowPopUp(val errorMessage: String) : SalesState
}
