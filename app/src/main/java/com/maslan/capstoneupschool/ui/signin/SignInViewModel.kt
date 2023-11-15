package com.maslan.capstoneupschool.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslan.capstoneupschool.common.Resource
import com.maslan.capstoneupschool.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _signInState = MutableLiveData<SignInState>()
    val signInState: LiveData<SignInState> get() = _signInState

    fun signIn(email: String, password: String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _signInState.value = SignInState.Loading

            _signInState.value = when (val result = authRepository.signIn(email, password)) {
                is Resource.Success -> SignInState.GoToHome
                is Resource.Fail -> SignInState.ShowPopUp(result.failMessage)
                is Resource.Error -> SignInState.ShowPopUp(result.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _signInState.value = SignInState.ShowPopUp("Email field cannot be left blank")
                false
            }

            password.isEmpty() -> {
                _signInState.value = SignInState.ShowPopUp("\n" + "Password field cannot be left blank")
                false
            }

            password.length < 6 -> {
                _signInState.value = SignInState.ShowPopUp("password field cannot be less than 6 characters")
                false
            }

            else -> true
        }
    }
}

sealed interface SignInState {
    object Loading : SignInState
    object GoToHome: SignInState
    data class ShowPopUp(val errorMessage: String) : SignInState

}