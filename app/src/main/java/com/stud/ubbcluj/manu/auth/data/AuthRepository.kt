package com.stud.ubbcluj.manu.auth.data

import com.stud.ubbcluj.manu.auth.data.remote.RemoteAuthDataSource
import com.stud.ubbcluj.manu.utils.Api
import com.stud.ubbcluj.manu.utils.MyResult

object AuthRepository {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): MyResult<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        if (result is MyResult.Success<TokenHolder>) {
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        this.user = user
        Api.tokenInterceptor.token = tokenHolder.token
    }
}