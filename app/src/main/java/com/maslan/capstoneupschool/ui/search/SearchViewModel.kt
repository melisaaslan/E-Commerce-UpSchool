package com.maslan.capstoneupschool.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.maslan.capstoneupschool.common.Resource
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.data.repository.ProductRepository
import com.maslan.capstoneupschool.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel(){
    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    fun getSearchProduct(query: String)=
        viewModelScope.launch {
            _searchState.value = SearchState.Loading


            _searchState.value = when (val result = productRepository.getSearchProduct(query)) {
                is Resource.Success -> SearchState.SuccessState(result.data)
                is Resource.Fail -> SearchState.EmptyScreen(result.failMessage)
                is Resource.Error -> SearchState.ShowPopUp(result.errorMessage)
            }
        }

    sealed interface SearchState {
        object Loading : SearchState
        data class SuccessState(val products: List<ProductUI>) : SearchState
        data class EmptyScreen(val failMessage: String) :SearchState
        data class ShowPopUp(val errorMessage: String ): SearchState
    }
}