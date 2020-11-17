package com.stud.ubbcluj.manu.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.stud.ubbcluj.manu.auth.data.AuthRepository
import com.stud.ubbcluj.manu.auth.data.TokenHolder
import com.stud.ubbcluj.manu.utils.MyResult
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val mutableLoginResult = MutableLiveData<MyResult<TokenHolder>>()
    val loginResult: LiveData<MyResult<TokenHolder>> = mutableLoginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.v(TAG, "login...");
            mutableLoginResult.value = AuthRepository.login(username, password)
            Log.v(TAG, mutableLoginResult.value.toString());
        }
    }
}