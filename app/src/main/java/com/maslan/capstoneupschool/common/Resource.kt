package com.maslan.capstoneupschool.common

sealed interface Resource<out T : Any> {

    data class Success<out T : Any>(val data: T) : Resource<T>
    data class Error(val errorMessage: String) : Resource<Nothing>
    data class Fail(val failMessage: String) : Resource<Nothing>
}
