package com.maslan.capstoneupschool.ui.signup

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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> get() = _signUpState

    fun signUp(email: String, password: String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _signUpState.value = SignUpState.Loading

            _signUpState.value = when (val result = authRepository.signUp(email, password)) {
                is Resource.Success -> SignUpState.GoToHome
                is Resource.Fail -> SignUpState.ShowPopUp(result.failMessage)
                is Resource.Error -> SignUpState.ShowPopUp(result.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Email field cannot be left blank")
                false
            }

            password.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Password field cannot be left blank")
                false
            }

            password.length < 6 -> {
                _signUpState.value = SignUpState.ShowPopUp("password field cannot be less than 6 characters")
                false
            }

            else -> true
        }
    }
}

sealed interface SignUpState {
    object Loading : SignUpState
    object GoToHome : SignUpState
    data class ShowPopUp(val errorMessage: String) : SignUpState
}