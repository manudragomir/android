package com.stud.ubbcluj.manu.plants_model.model.remote

import com.google.gson.GsonBuilder
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.utils.Api
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object PlantApi {
    private const val URL = "http://192.168.1.2:3333/"

    interface Service {
        @GET("/api/plant/")
        suspend fun find(): List<Plant>

        @GET("/api/plant/{id}")
        suspend fun read(@Path("id") plantId: String): Plant;

        @Headers("Content-Type: application/json")
        @POST("/api/plant/new")
        suspend fun create(@Body plant: Plant): Plant

        @Headers("Content-Type: application/json")
        @PUT("/api/plant/{id}")
        suspend fun update(@Path("id") plantId: String, @Body plant: Plant): Plant

        @Headers("Content-Type: application/json")
        @DELETE("/api/plant/{id}")
        suspend fun delete(@Path("id") plantId: String): Plant
    }
    val service: Service = Api.retrofit.create(Service::class.java)
}