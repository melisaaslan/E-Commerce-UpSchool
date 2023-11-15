package com.maslan.capstoneupschool.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.maslan.capstoneupschool.common.Resource
import kotlinx.coroutines.tasks.await


class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun isUserLoggedIn() = firebaseAuth.currentUser != null

    fun getUserId() = firebaseAuth.currentUser?.uid.orEmpty()

    suspend fun signIn(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Bir hata oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("Böyle bir kullanıcı yok")
        }
    }

    suspend fun signUp(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Bir hata oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu")
        }
    }

    fun logOut() = firebaseAuth.signOut()
}