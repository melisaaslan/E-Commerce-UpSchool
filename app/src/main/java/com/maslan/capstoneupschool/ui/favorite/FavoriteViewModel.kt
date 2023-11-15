package com.maslan.capstoneupschool.ui.favorite

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
class FavoriteViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private var _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun getFavorites() = viewModelScope.launch {
        _favoritesState.value = FavoritesState.Loading

        _favoritesState.value = when (val result = productRepository.getFavorites()) {
            is Resource.Success -> FavoritesState.SuccessState(result.data)
            is Resource.Fail -> FavoritesState.EmptyScreen(result.failMessage)
            is Resource.Error -> FavoritesState.ShowPopUp(result.errorMessage)
        }
    }

    fun deleteFromFavorites(product: ProductUI) = viewModelScope.launch {
        productRepository.deleteFromFavorites(product)
        getFavorites()
    }

    fun deleteAllProducts() = viewModelScope.launch {
        productRepository.deleteAllFromFav()
        getFavorites()

    }


}

sealed interface FavoritesState {
    object Loading : FavoritesState
    data class SuccessState(val products: List<ProductUI>) : FavoritesState
    data class EmptyScreen(val failMessage: String) : FavoritesState
    data class ShowPopUp(val errorMessage: String) : FavoritesState
}