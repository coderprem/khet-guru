package com.example.khetguru.presentation.state

import com.example.khetguru.domain.model.UserData

data class AppState(
    val isSignedIn: Boolean = false,
    val userData: UserData? = null,
    val signInError: String? = null,
)