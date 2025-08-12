package com.example.test.ui.theme
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.*

interface FinkApi {
 @POST("api/v1/objects")
 suspend fun postObject(@Body request:objectRequest):List<objectResponse>

 @POST("api/v1/conesearch")
 suspend fun postConesearch(@Body request: conesearchRequest): List<conesearchResponse>

 @POST("api/v1/latests")
 suspend fun postClasses(@Body request:classRequest): List<objectResponse>

 @POST("api/v1/anomaly")
 suspend fun postAnomaly(@Body request:anomalyRequest): List<objectResponse>

 @POST("api/v1/cutouts")
 suspend fun postImage(@Body request:imageRequest): ResponseBody

 @GET("api/v1/classes")
 suspend fun getClassesNames():classNameResponse
}


