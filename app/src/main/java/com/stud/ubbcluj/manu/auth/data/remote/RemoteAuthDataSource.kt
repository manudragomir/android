package com.stud.ubbcluj.manu.auth.data.remote

import com.stud.ubbcluj.manu.auth.data.TokenHolder
import com.stud.ubbcluj.manu.auth.data.User
import com.stud.ubbcluj.manu.utils.Api
import com.stud.ubbcluj.manu.utils.MyResult
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object RemoteAuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): MyResult<TokenHolder> {
        try {
            return MyResult.Success(authService.login(user))
        } catch (e: Exception) {
            return MyResult.Error(e)
        }
    }
}