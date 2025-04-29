package com.example.khetguru.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.khetguru.presentation.state.AppState
import com.example.khetguru.domain.model.SignInResult
import com.example.khetguru.domain.model.UserData
import com.example.khetguru.domain.utils.USERS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    firestore: FirebaseFirestore
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()
    private val userCollection = firestore.collection(USERS_COLLECTION)
    var userDataListener: ListenerRegistration? = null
    fun resetState() {
        _state.value = AppState() // reset to default
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignedIn = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun addUserToFireStore(userData: UserData) {
        val userDataMap = mapOf(
            "userId" to userData.userId,
            "userName" to userData.userName,
            "profilePicUrl" to userData.profilePicUrl,
            "email" to userData.email
        )

        val userDocument = userCollection.document(userData.userId)
        userDocument.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    userDocument.update(userDataMap)
                        .addOnSuccessListener {
                            Log.d("AuthViewModel", "User updated in FireStore")
                        }
                        .addOnFailureListener {
                            Log.e("AuthViewModel", "Failed to update user in FireStore", it)
                        }
                } else {
                    userDocument.set(userDataMap)
                        .addOnSuccessListener {
                            Log.d("AuthViewModel", "User added to FireStore")
                        }
                        .addOnFailureListener {
                            Log.e("AuthViewModel", "Failed to add user to FireStore", it)
                        }
                }
            }
    }

    fun getUserData(userId: String) {
        userDataListener = userCollection.document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("AuthViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userData = snapshot.toObject(UserData::class.java)
                    _state.update {
                        it.copy(userData = userData)
                    }
                } else {
                    Log.d("AuthViewModel", "Current data: null")
                }
            }
    }
}